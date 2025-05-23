package ru.wedding.weddingbot.bot.messagehandler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class StickerHandler implements MessageHandler<SendSticker> {

  private final TelegramClient client;

  @Override
  @SneakyThrows
  public SendSticker handle(Message message, Long chatID, String prefix) {
    client.execute(SendMessage.builder()
        .chatId(chatID)
        .text(prefix)
        .build());

    SendSticker sticker = SendSticker.builder()
        .chatId(chatID)
        .sticker(new InputFile(message.getSticker().getFileId()))
        .build();
    client.execute(sticker);
    return sticker;
  }

  @Override
  public boolean support(Message message) {
    return message.hasSticker();
  }
}
