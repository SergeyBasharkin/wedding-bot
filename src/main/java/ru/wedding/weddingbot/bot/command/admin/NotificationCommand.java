package ru.wedding.weddingbot.bot.command.admin;

import static ru.wedding.weddingbot.bot.command.helper.ComeCommandHelper.getMessage;

import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.Command;
import ru.wedding.weddingbot.bot.command.helper.NameHelper;
import ru.wedding.weddingbot.bot.messagehandler.MessageHandler;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.service.UserService;

@Component
public class NotificationCommand extends Command {

  private final static String name = "/notification";

  private final UserService userService;
  private final List<MessageHandler<?>> handlers;

  protected NotificationCommand(TelegramClient telegramClient, UserService userService,
      List<MessageHandler<?>> handlers) {
    super(name, telegramClient);
    this.userService = userService;
    this.handlers = handlers;
  }

  @Override
  public void handle(Update update) {
    MaybeInaccessibleMessage message = getMessage(update);

    if (message == null) {
      return;
    }

    Optional<User> userOptional = userService.findByChatId(message.getChatId());

    if (userOptional.map(User::isAdmin).orElse(false)) {
      return;
    }

    String prefix = userOptional
        .map(NameHelper::nickname)
        .orElse(NameHelper.name(message.getChat()));

    userService.findAdmins()
        .forEach(it -> sendNotification(message, it.getChatId(), prefix));

  }

  @SneakyThrows
  private void sendNotification(@NonNull MaybeInaccessibleMessage message, Long chatId,
      String prefix) {

    if (message instanceof Message acceptedMessage) {
      handlers.stream()
          .filter(it -> it.support(acceptedMessage))
          .forEach(it ->  it.handle(acceptedMessage, chatId, prefix));
    } else {
      telegramClient.execute(SendMessage.builder()
          .chatId(chatId)
          .text(prefix + " написал недоступное сообщение ")
          .build());
    }
  }


  @Override
  public boolean support(Update update) {
    return getMessage(update) != null;
  }
}
