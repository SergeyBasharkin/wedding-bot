package ru.wedding.weddingbot.bot.command;

import com.vdurmont.emoji.EmojiParser;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.service.UserService;

@Slf4j
@Component
public class StartCommand extends Command {

  private static final String name = "/start";

  private final UserService userService;

  protected StartCommand(TelegramClient telegramClient, UserService userService) {
    super(name, telegramClient);
    this.userService = userService;
  }

  @Override
  public void handle(Update update) {
    Optional.ofNullable(update)
        .map(Update::getMessage)
        .ifPresent(it -> {
          User user = saveUser(it.getChat());
          sendMessage(
              it.getChatId(),
              user.getFirstName() == null ?
                  user.getUsername()
                  :
                  user.getFirstName() + Optional.ofNullable(user.getLastName())
                      .map(lastName -> " " + lastName)
                      .orElse("")
              );
        });
  }

  private void sendMessage(Long chatId, String name) {
    SendMessage message = SendMessage.builder()
        .chatId(chatId)
        .text(EmojiParser.parseToUnicode(String.format("""
            <pre>Дорогой/ая %s!
            
            Приглашаем тебя на нашу свадьбу!
            Этот бот поможет тебе узнать подробнее о нашем мероприятии
            Используй кнопки для ответа на популярные вопросы или просто напиши в чат.
            
            Мы будем ждать тебя!
            Маша и Сережа
            
            P.S.
            Используй кнопку "Я приду :smile:", чтобы мы знали, что ты будешь :heart_hands:
            </pre>
            """, name)))
        .parseMode("HTML")
        .build();

    try {
      telegramClient.execute(message);
    } catch (TelegramApiException e) {
      log.error(e.getMessage(), e);
    }
  }

  private User saveUser(Chat chat) {
    return Optional.ofNullable(chat)
        .map(it -> {
          User user = new User();
          user.setId(it.getId());
          user.setChatId(it.getId());
          user.setFirstName(it.getFirstName());
          user.setLastName(it.getLastName());
          user.setUsername(it.getUserName());
          userService.save(user);
          return user;
        })
        .orElse(null);
  }
}
