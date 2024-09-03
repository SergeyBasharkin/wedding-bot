package ru.wedding.weddingbot.bot.command;

import com.vdurmont.emoji.EmojiParser;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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
              user
          );
        });
  }

  private String name(User user) {
    String name = user.getFirstName() + Optional.ofNullable(user.getLastName())
        .map(lastName -> " " + lastName)
        .orElse("");
    return user.getFirstName() == null ?
        user.getFirstName() : name;
  }

  private void sendMessage(Long chatId, User user) {
    SendMessage message = SendMessage.builder()
        .chatId(chatId)
        .text(EmojiParser.parseToUnicode(String.format("""
            Дорогой/ая %s!
                        
            Приглашаем тебя на нашу свадьбу!
            Этот бот поможет тебе узнать подробнее о нашем мероприятии
            Используй кнопки для ответа на популярные вопросы или просто напиши в чат.
                        
            Мы будем ждать тебя!
            Маша и Сережа
                     
            """, name(user))))
        .parseMode("HTML")
        .replyMarkup(ReplyKeyboardMarkup.builder()
            .keyboardRow(new KeyboardRow("Программа торжества"))
            .keyboardRow(new KeyboardRow("Организатор"))
            .keyboardRow(new KeyboardRow("Какой дресскод?"))
            .keyboardRow(new KeyboardRow("Что подарить"))
            .keyboardRow(new KeyboardRow("Что где когда?"))
            .keyboardRow(new KeyboardRow("Что по меню?"))
            .build())
        .build();

    try {
      telegramClient.execute(message);
      telegramClient.execute(IComeCommand.buildMessage(user));
    } catch (TelegramApiException e) {
      log.error(e.getMessage(), e);
    }
  }

  private User saveUser(Chat chat) {
    return Optional.ofNullable(chat)
        .map(it -> {
          User user = userService.findByUsername(it.getUserName())
              .orElseGet(User::new);
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
