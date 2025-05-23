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
public class SendCommand extends AdminCommand {

  private static final String name = "/send";

  private static final Pattern regex = Pattern.compile("^(/send) (@\\S+) (.*)$", Pattern.DOTALL);

  protected SendCommand(
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
      String username = matcher.group(2).substring(1);
      Long id;
      try {
        id = Long.valueOf(username);
      } catch (Exception ignore){
        id = -1L;
      }
      userService.findByUsernameOrId(username, id)
          .map(User::getChatId)
          .ifPresentOrElse(
              chatId -> sendMessage(chatId, matcher.group(3)),
              () -> sendMessage(update.getMessage().getChatId(),
                  "Пользователь " + username + " не найден")
          );
    } else {
      sendMessage(
          update.getMessage().getChatId(),
          "Ошибка формата. Формат команды /send message");
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
