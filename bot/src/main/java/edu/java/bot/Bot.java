package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

public class Bot {

    private final TelegramBot bot;

    public Bot(String telegramToken, MessageHandler messageHandler) {
        this.bot = new TelegramBot(telegramToken);

        bot.setUpdatesListener(updates -> {

            for (Update update : updates) {
                bot.execute(messageHandler.handle(update));
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
