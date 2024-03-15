package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private final List<Command> commands;

    public HelpCommand(@Autowired List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "показать список доступных команд";
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Список команд:\n");
        for (Command command : commands) {
            stringBuilder.append(String.format("%s - %s\n", command.command(), command.description()));
        }

        return new SendMessage(update.message().from().id(), stringBuilder.toString());
    }
}
