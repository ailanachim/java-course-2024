package edu.java.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BotTest {

    private MessageHandler messageHandler;

    @BeforeEach
    void init() {
        var userService = new InMemoryUserService();

        var trackCommand = new TrackCommand(userService);
        var untrackCommand = new UntrackCommand(userService);
        var listCommand = new ListCommand(userService);
        var startCommand = new StartCommand(userService);

        List<Command> commands = new ArrayList<>();
        commands.add(trackCommand);
        commands.add(untrackCommand);
        commands.add(listCommand);
        commands.add(startCommand);
        var helpCommand = new HelpCommand(commands);
        commands.add(helpCommand);

        messageHandler = new UserMessageHandler(
            commands,
            new DefaultHandler(userService)
        );
    }

    private String sendMessage(String text) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        User user = new User(0L);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(text);
        Mockito.when(message.from()).thenReturn(user);

        SendMessage sendMessage = messageHandler.handle(update);
        return (String) sendMessage.getParameters().get("text");
    }

    @Test
    void botTest() {
        String link = "https://github.com/pengrad/java-telegram-bot-api";
        String link1 = "https://stackoverflow.com/questions/43164923";
        String link2 = "https://stackoverflow.com/questions/46030405";

        assertEquals("Привет! Это бот для отслеживания обновлений", sendMessage("/start"));

        assertEquals("Введите ссылку на ресурс, который хотите отслеживать", sendMessage("/track"));
        assertEquals(link + " теперь отслеживается", sendMessage(link));

        assertEquals("Введите ссылку на ресурс, который хотите отслеживать", sendMessage("/track"));
        assertEquals(link1 + " теперь отслеживается", sendMessage(link1));

        assertEquals("Введите ссылку на ресурс, который хотите отслеживать", sendMessage("/track"));
        assertEquals("Неверный адрес или ресурс уже отслеживается", sendMessage(link1));

        assertEquals("Сейчас отслеживаются:\n" + link + "\n" + link1, sendMessage("/list"));

        assertEquals("Введите ссылку, которую вы больше не хотите отслеживать", sendMessage("/untrack"));
        assertEquals("Вы не отслеживаете " + link2, sendMessage(link2));

        assertEquals("Введите ссылку, которую вы больше не хотите отслеживать", sendMessage("/untrack"));
        assertEquals(link1 + " больше не отслеживается", sendMessage(link1));

        assertEquals("Сейчас отслеживаются:\n" + link, sendMessage("/list"));
        assertEquals("Вы больше не отслеживаете " + link, sendMessage("/untrack"));

        assertEquals("Вы ничего не отслеживаете", sendMessage("/untrack"));
        assertEquals("Вы ничего не отслеживаете", sendMessage("/list"));
    }

}
