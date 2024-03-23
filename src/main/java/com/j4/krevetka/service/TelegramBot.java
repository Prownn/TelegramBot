package com.j4.krevetka.service;

import com.j4.krevetka.config.Config;
import com.j4.krevetka.schema.Subscriber;
import com.j4.krevetka.untils.CronUtils;
import javassist.ClassPath;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.owlike.genson.Genson;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final Config config;
    private ArrayList<Subscriber> subscribers;

    private Genson genson;

    private ClassPathResource resource = new ClassPathResource("Data/subscriber.json");

    private File file = resource.getFile();

    public TelegramBot(Config config) throws IOException {
        this.config = config;
        this.genson = new Genson.Builder().create();

        ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        FileReader fileReader = new FileReader(file);

        try {
            this.subscribers = OBJECT_MAPPER.readValue(fileReader,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, Subscriber.class));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private ReplyKeyboardMarkup createReplyKeyboardMarkup(String[] buttons) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> buttonRows = new ArrayList<>();


        //создание кнопок
        for (String button : buttons) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(button));
            buttonRows.add(row);
        }


        //Установка клавиатуры в сообщение
        replyKeyboardMarkup.setKeyboard(buttonRows);

        return replyKeyboardMarkup;
    }


    public String getBotUsername() {
        return config.getBotName();
    }


    public String getBotToken() {
        return config.getToken();
    }

    private void onStart(long chatId) {
        SendMessage response = Service.getResponseStart(chatId);
        sendMessage(response);
    }

    private void onJavaGroupsMenu(long chatId) {
        SendMessage response = Service.getResponseJavaGroupsMenu(chatId);
        sendMessage(response);
    }

    private void onJavaTimeGroup(long chatId, String group) {
        SendMessage response = Service.getResponseJavaTimeGroup(chatId, group);
        sendMessage(response);
    }

    public void updateSubscrobersList(){
        try (FileWriter writer = new FileWriter(file, false)) {
            genson.serialize(subscribers, writer);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getChatId());


        // пришло ли сообщение и есть ли там текст
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            final String CMD_SUBSCRIBE = "Подписаться на уведомление ";

            // обработка команд
            switch (messageText) {
                case "JAVA" -> onJavaGroupsMenu(chatId);
                case "J1", "J2", "J3", "J4" -> onJavaTimeGroup(chatId, messageText);
                case CMD_SUBSCRIBE + "J1", CMD_SUBSCRIBE + "J2", CMD_SUBSCRIBE + "J3", CMD_SUBSCRIBE + "J4" -> {
                    try {
                        onSubscribe(chatId, messageText);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> onStart(chatId);
            }
        }
    }


    private void onSubscribe(long chatId, String command) throws ParseException {
        String regex = "Подписаться на уведомление ([A-Z]\\d)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);

        String group = "";
        String cronExpression = "";

        while (matcher.find()) {
            group = matcher.group(1);
            System.out.println(group);
        }

        System.out.println(group);

        switch (group) {
            case "J1" -> cronExpression = "*/1 * * * * ?";
            case "J2" -> cronExpression = "0 15 15 ? * 2,4";
            case "J3" -> cronExpression = "0 9 13 ? * 4,7";
            case "J4" -> cronExpression = "0 15 17 ? * 4,7";
        }
        Date nextExecutionTime = CronUtils.getNextExecutionTime(cronExpression);

        subscribers.add(new Subscriber(chatId, nextExecutionTime, cronExpression));

        updateSubscrobersList();

        SendMessage respone = Service.getResponseSubscribe(chatId, group);
        sendMessage(respone);
    }


    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void sendNotifications() throws ParseException {
        for (Subscriber sub : subscribers) {
            long chatId = sub.getChatId();

            if (sub.getNextExecutionTime().before(new Date())) {
                String cronExpression = sub.getCronExpression();
                Date nextExecutionTime = CronUtils.getNextExecutionTime(cronExpression);

                sub.setNextExecutionTime(nextExecutionTime);

                SendMessage message = new SendMessage(String.valueOf(chatId), "This is your " + cronExpression + "notofication!");


                sendMessage(message);
            }
        }
    }
}
