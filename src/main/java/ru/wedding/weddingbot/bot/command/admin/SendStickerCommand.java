package ru.wedding.weddingbot.bot.command.admin;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.service.UserService;

@Component
public class SendStickerCommand extends AdminCommand {

  protected static final String name = "/sticker";
  protected static final String msgText = "Ответь на это сообщение стикером для ";
  protected static final Pattern regex = Pattern.compile("^(/sticker) (@.+)$");
  protected boolean isMessage = false;

  protected SendStickerCommand(
      TelegramClient telegramClient,
      UserService userService
  ) {
    super(name, telegramClient, userService);
  }

  public String msgText() {
    return msgText;
  }

  @Override
  protected void handleInternal(Update update) {
    if (isMessage) {
      message(update);
    } else {
      sticker(update);
    }
  }


  @Override
  public boolean support(Update update) {
    boolean messageSent = Optional.ofNullable(update)
        .map(Update::getMessage)
        .map(Message::getText)
        .filter(it -> it.contains(getName()))
        .isPresent();

    boolean replySent = Optional.ofNullable(update)
        .map(Update::getMessage)
        .map(Message::getReplyToMessage)
        .map(Message::getText)
        .filter(it -> it.contains(msgText()))
        .isPresent();

    isMessage = messageSent;

    return messageSent || replySent;
  }

  protected void message(Update update){
    String msg = update.getMessage().getText();
    Matcher matcher = regex.matcher(msg);
    if (matcher.matches()) {
      String username = matcher.group(2);
      Long chatId = update.getMessage().getChatId();
      sendMessage(chatId, msgText() + username);
    } else {
      sendMessage(
          update.getMessage().getChatId(),
          "Для отправки команды пользователю используйте формат команды /sticker @user");
    }
  }

  protected void sticker(Update update){
    if (update.getMessage().getReplyToMessage().getText().contains("@")) {
      Sticker sticker = update.getMessage().getSticker();
      String username = update.getMessage().getReplyToMessage().getText().split("@")[1];
      Long id;
      try {
        id = Long.valueOf(username);
      } catch (Exception ignore){
        id = -1L;
      }
      userService.findByUsernameOrId(username, id)
          .map(User::getChatId)
          .ifPresentOrElse(
              chatId -> sendSticker(chatId, sticker.getFileId()),
              () -> sendMessage(
                  update.getMessage().getChatId(),
                  "Пользователь " + username + " не найден"
              )
          );
    }
  }
}
