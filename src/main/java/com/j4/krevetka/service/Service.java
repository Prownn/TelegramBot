package com.j4.krevetka.service;


import com.j4.krevetka.untils.KeyboardUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class Service {
    public static SendMessage getResponseJavaGroupsMenu(long chatId) {
        // подготовка данных
        String answer = "Выберите направление.";
        String[] buttons = new String[] {
                "J1", "J2",
                "J3", "J4",
                "Начало"
        };

        // создание объекта сообщения
        SendMessage message = new SendMessage(String.valueOf(chatId), answer);

        // создание клавиатуры
        ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardUtils.createReplyKeyboardMarkup(buttons);
        message.setReplyMarkup(replyKeyboardMarkup);

        return message;
    }

    public static SendMessage getResponseJavaTimeGroup(long chatId, String group) {
        // подготовка данных
        String answer = "☕️ Расписание группы " + group + ":\n";

        switch (group) {
            case "J1" -> answer += "⚡️ Понедельник - 10.00-11.40\n⚡️ Четверг - 10.00-11.40";
            case "J2" -> answer += "⚡️ Понедельник - 16.00-17.40\n⚡️ Среда - 16.00-17.40";
            case "J3" -> answer += "⚡️ Среда - 10.00 - 11.40\n⚡️ Суббота - 14.00-15.40";
            case "J4" -> answer += "⚡️ Среда - 18.00 - 19.40\n⚡️ Суббота - 16.00-17.40";
        }

        String[] buttons = new String[] {
                "Подписаться на уведомление " + group,
                "Начало"
        };

        // создание объекта сообщения
        SendMessage message = new SendMessage(String.valueOf(chatId), answer);

        // создание клавиатуры
        ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardUtils.createReplyKeyboardMarkup(buttons);
        message.setReplyMarkup(replyKeyboardMarkup);

        return message;
    }

    public static SendMessage getResponseSubscribe(long chatId, String group) {
        // подготовка данных
        String answer = "Вы подписались на уведомления занятий группы - " + group;

        return new SendMessage(String.valueOf(chatId), answer);
    }

    public static SendMessage getResponseStart(long chatId) {
        // подготовка данных
        String answer = "Выберите направление.";
        String[] buttons = new String[] {
                "JAVA"
        };

        // создание объекта сообщения
        SendMessage message = new SendMessage(String.valueOf(chatId), answer);

        // создание клавиатуры
        ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardUtils.createReplyKeyboardMarkup(buttons);
        message.setReplyMarkup(replyKeyboardMarkup);

        return message;
    }
}