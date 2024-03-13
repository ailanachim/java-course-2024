package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bot implements UpdatesListener, Runnable {

    @Autowired
    private ApplicationConfig applicationConfig;
    private TelegramBot bot;
    private final UserMessageHandler messageHandler;

    public Bot(UserMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        bot = new TelegramBot(applicationConfig.telegramToken());
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
                bot.execute(messageHandler.handle(update));
            }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
