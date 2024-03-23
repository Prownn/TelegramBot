package com.j4.krevetka.untils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

public class KeyboardUtils {
    public static ReplyKeyboardMarkup createReplyKeyboardMarkup(String[] buttons) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> buttonRows = new ArrayList<>();

        // создание кнопок
        for (String button : buttons) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(button));
            buttonRows.add(row);
        }

        // Установка клавиатуры в сообщение
        replyKeyboardMarkup.setKeyboard(buttonRows);

        return replyKeyboardMarkup;
    }
}