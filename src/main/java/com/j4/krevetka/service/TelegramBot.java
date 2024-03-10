package com.j4.krevetka.service;

import com.j4.krevetka.config.Config;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final Config config;

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

    public TelegramBot(Config config) {
        this.config = config;
    }

    public String getBotUsername() {
        return config.getBotName();
    }


    public String getBotToken() {
        return config.getToken();
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            System.out.println(messageText);

            switch (messageText) {
                case "JAVA" -> onJavaGroupsMenu(chatId, update.getMessage().getChat().getFirstName());
                case "J1", "J2", "J3", "J4", "J(пректная группа)" ->
                        onJavaGroupsMenu(chatId, update.getMessage().getChat().getFirstName());

                default -> onStart(chatId, update.getMessage().getChat().getFirstName());
            }
        }
    }

    private void onStart(long chatId, String name) {
        System.out.println("\uD83D\uDD25 reqest " + name + ": /Start");

        //подготовка данных
        String answer = "https://media1.tenor.com/m/x8v1oNUOmg4AAAAd/rickroll-roll.gif";
        String[] buttons = new String[]{
                "Java"
        };

        //создание объекта сообщения
        SendMessage message = new SendMessage(String.valueOf(chatId), answer);

        //создание клавиатуры
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(buttons);
        message.setReplyMarkup(replyKeyboardMarkup);

        sendMessage(message);
    }

    private void onJavaGroupsMenu(long chatId, String name) {
        System.out.println("\uD83D\uDD25 reqest " + name + ": Прогромирование JAVA");

        //ПОДГОТОВКА ДАННЫХ
        String answer = "Выберите направление";
        String[] buttons = new String[]{
                "J1", "J2",
                "J3", "J4",
                "J(Проектная группа)",
                "Начало"
        };

        //Создание объекта сообщения
        SendMessage message = new SendMessage(String.valueOf(chatId), answer);


//создание клавиатуры
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(buttons);
        message.setReplyMarkup(replyKeyboardMarkup);

        sendMessage(message);


    }

    private void onJavaTimeGroup(long chatId, String group) {

        String answer = "Расписание группы " + group + ":\n";

        switch (group) {
            case "J1" -> answer += "Понидельник - 10 00 - 11 00\n Четверг - 10 00 - 11 40";
            case "J2" -> answer += "Понидельник - 16 00 - 17 00\n Среда - 16 00 - 17 40";
            case "J3" -> answer += "Среда - 10 00 - 11 40\n Суббота - 14 00 - 15 40";
            case "J4" -> answer += "Среда - 18 00 - 19 40\n Суббота - 16 00 - 17 40";
        }

        String[] buttons = new String[]{
                "Подписаться на уведомления " + group,
                "Начало"};

        //Создание объекта сообщения
        SendMessage message = new SendMessage(String.valueOf(chatId), answer);


//создание клавиатуры
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(buttons);
        message.setReplyMarkup(replyKeyboardMarkup);

        sendMessage(message);
    }

}
