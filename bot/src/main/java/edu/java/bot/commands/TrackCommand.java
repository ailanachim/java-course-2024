package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {

    private final UserService userService;

    public TrackCommand(@Autowired UserService userService) {
        this.userService = userService;
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "добавить ресурс на отслеживание";
    }

    @Override
    public SendMessage handle(Update update) {
        Long userId = update.message().from().id();
        userService.setAddingTrack(userId);
        return new SendMessage(userId, "Введите ссылку на ресурс, который хотите отслеживать");
    }

}
