package ru.wedding.weddingbot.bot.command.admin;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.service.UserService;

@Component
public class SendStickerAllCommand extends SendStickerCommand {

  protected static final String name = "/stickerAll";

  protected SendStickerAllCommand(
      TelegramClient telegramClient,
      UserService userService) {
    super(telegramClient, userService);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String msgText() {
    return msgText + "всех";
  }

  @Override
  protected void message(Update update) {
    Long chatId = update.getMessage().getChatId();
    sendMessage(chatId, msgText + "всех");
  }

  @Override
  protected void sticker(Update update) {
    Sticker sticker = update.getMessage().getSticker();
    userService.findAll().stream()
        .map(User::getChatId)
        .forEach(chatId -> sendSticker(chatId, sticker.getFileId()));
  }


}
