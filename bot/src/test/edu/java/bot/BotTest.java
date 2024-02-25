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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

        assertThat(sendMessage("/start")).isEqualTo("Привет! Это бот для отслеживания обновлений");

        assertThat(sendMessage("/track")).isEqualTo("Введите ссылку на ресурс, который хотите отслеживать");
        assertThat(sendMessage(link)).isEqualTo(link + " теперь отслеживается");

        assertThat(sendMessage("/track")).isEqualTo("Введите ссылку на ресурс, который хотите отслеживать");
        assertThat(sendMessage(link1)).isEqualTo(link1 + " теперь отслеживается");

        assertThat(sendMessage("/track")).isEqualTo("Введите ссылку на ресурс, который хотите отслеживать");
        assertThat(sendMessage(link1)).isEqualTo("Неверный адрес или ресурс уже отслеживается");

        assertThat(sendMessage("/list")).isEqualTo("Сейчас отслеживаются:\n" + link + "\n" + link1);

        assertThat(sendMessage("/untrack")).isEqualTo("Введите ссылку, которую вы больше не хотите отслеживать");
        assertThat(sendMessage(link2)).isEqualTo("Вы не отслеживаете " + link2);

        assertThat(sendMessage("/untrack")).isEqualTo("Введите ссылку, которую вы больше не хотите отслеживать");
        assertThat(sendMessage(link1)).isEqualTo(link1 + " больше не отслеживается");

        assertThat(sendMessage("/list")).isEqualTo("Сейчас отслеживаются:\n" + link);

        assertThat(sendMessage("/untrack")).isEqualTo("Вы больше не отслеживаете " + link);

        assertThat(sendMessage("/list")).isEqualTo("Вы ничего не отслеживаете");
        assertThat(sendMessage("/untrack")).isEqualTo("Вы ничего не отслеживаете");
    }

}
