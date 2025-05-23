package ru.wedding.weddingbot.bot.command.admin;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.service.UserService;

@Component
public class SendAllCommand extends AdminCommand{
  private static final String name = "/sendAll";

  private static final Pattern regex = Pattern.compile("^(/sendAll) (.*)$", Pattern.DOTALL);

  protected SendAllCommand(
      TelegramClient telegramClient,
      UserService userService) {
    super(name, telegramClient, userService);
  }

  @Override
  @SneakyThrows
  protected void handleInternal(Update update) {
    String msg = update.getMessage().getText();
    Matcher matcher = regex.matcher(msg);
    if (matcher.matches()) {
      userService.findAll().stream()
          .map(User::getChatId)
          .forEach(chatId -> sendMessage(chatId, matcher.group(2)));
    } else {
      sendMessage(
          update.getMessage().getChatId(),
          "Ошибка формата. Формат команды /sendAll message");
    }
  }

  @Override
  public boolean support(Update update) {
    return Optional.ofNullable(update)
        .map(Update::getMessage)
        .map(Message::getText)
        .map(it -> it.contains(name))
        .orElse(false);
  }
}
