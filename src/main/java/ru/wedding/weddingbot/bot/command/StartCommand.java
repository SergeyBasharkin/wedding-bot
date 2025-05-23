package ru.wedding.weddingbot.bot.command;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.helper.ButtonsHelper;
import ru.wedding.weddingbot.bot.command.helper.NameHelper;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.service.UserService;
import ru.wedding.weddingbot.utils.FileHelper;

@Slf4j
@Component
@Order(0)
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

  private void sendNotificationToAdmin(User user) {
    userService.findAdmins().stream()
        .map(User::getChatId)
        .forEach(
            it -> sendMessage(it, "Новый пользователь! @" + Optional.ofNullable(user.getUsername())
                .orElse(String.valueOf(user.getId())))
        );
  }

  private void sendMessage(Long chatId, User user) {
    SendPhoto message = SendPhoto.builder()
        .chatId(chatId)
        .photo(FileHelper.getFile("start.JPG"))
        .caption(String.format("""
            Дорогой/ая %s!
                        
            Приглашаем тебя на нашу свадьбу, которая состоится 28-го сентября в Лофте Толстого в 15:30!
            Этот бот поможет тебе узнать подробнее о нашем мероприятии.
            Используй кнопки для ответа на популярные вопросы или просто напиши вопрос в чат.
                        
            Мы очень хотим растянуть цветочное удовольствие надолго, поэтому вместо цветов в день свадьбы вы можете подарить нам сертификат на цветочную подписку, чтобы получать букеты от вас каждую неделю💐
                        
            Мы будем ждать тебя!
            Маша и Сережа
                     
            """, NameHelper.name(user)))
        .parseMode("HTML")
        .replyMarkup(ButtonsHelper.buttons())
        .build();

    try {
      telegramClient.execute(message);
      telegramClient.execute(IComeCommand.buildMessage(user));
    } catch (TelegramApiException e) {
      log.error(e.getMessage(), e);
    }
  }

  private User saveUser(Chat chat) {

    if (chat != null) {
      User user = userService.findById(chat.getId())
          .orElse(null);
      if (user == null) {
        user = new User();
        user.setId(chat.getId());
        user.setChatId(chat.getId());
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setUsername(chat.getUserName());
        userService.save(user);
        sendNotificationToAdmin(user);
      }
      return user;
    }
    return null;
  }
}
