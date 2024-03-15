package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultHandler implements MessageHandler {

    private final UserService userService;

    @Override
    public SendMessage handle(Update update) {
        var text = update.message().text();
        var userId = update.message().from().id();

        if (userService.isAddingTrack(userId)) {
            return addLink(userId, text);
        }

        if (userService.isRemovingTrack(userId)) {
            return removeLink(userId, text);
        }

        return new SendMessage(
            userId,
            "Неизвестная команда, используйте /help, чтобы посмотреть список доступных команд"
        );
    }

    private SendMessage addLink(Long userId, String text) {
        if (userService.addTrack(userId, text)) {
            return new SendMessage(userId, text + " теперь отслеживается");
        }
        return new SendMessage(userId, "Неверный адрес или ресурс уже отслеживается");
    }

    private SendMessage removeLink(Long userId, String text) {
        if (userService.removeTrack(userId, text)) {
            return new SendMessage(userId, text + " больше не отслеживается");
        }
        return new SendMessage(userId, "Вы не отслеживаете " + text);
    }
}
