package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.UserService;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private final UserService userService;

    public ListCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ресурсов";
    }

    @Override
    public SendMessage handle(Update update) {
        var userId = update.message().from().id();

        var links = userService.getTrackedLinks(userId);
        if (links.isEmpty()) {
            return new SendMessage(userId, "Вы ничего не отслеживаете");
        }

        return new SendMessage(userId, "Сейчас отслеживаются:\n" + String.join("\n", links));
    }
}
