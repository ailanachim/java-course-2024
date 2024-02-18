package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMessageHandler implements MessageHandler {

    private final List<Command> commands;
    private final MessageHandler messageHandler;

    public UserMessageHandler(
        @Autowired List<Command> commands,
        MessageHandler messageHandler
    ) {
        this.commands = commands;
        this.messageHandler = messageHandler;
    }

    @Override
    public SendMessage handle(Update update) {
        for (Command command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }

        return messageHandler.handle(update);
    }
}
