package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {

    private final UserService userService;

    public UntrackCommand(@Autowired UserService userService) {
        this.userService = userService;
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "перестать отслеживать ресурс";
    }

    @Override
    public SendMessage handle(Update update) {
        var userId = update.message().from().id();

        var links = userService.getTrackedLinks(userId);
        if (links.isEmpty()) {
            return new SendMessage(userId, "Вы ничего не отслеживаете");
        }

        if (links.size() == 1) {
            var link = links.get(0);
            userService.removeTrack(userId, link);
            return new SendMessage(userId, "Вы больше не отслеживаете " + link);
        }

        userService.setRemovingTrack(userId);
        return new SendMessage(userId, "Введите ссылку, которую вы больше не хотите отслеживать");
    }
}
