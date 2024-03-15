package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.UserService;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {

    private final UserService userService;

    public StartCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "начать взаимодействие с ботом";
    }

    @Override
    public SendMessage handle(Update update) {
        var userId = update.message().from().id();
        userService.addUser(userId);
        return new SendMessage(userId, "Привет! Это бот для отслеживания обновлений");
    }
}
