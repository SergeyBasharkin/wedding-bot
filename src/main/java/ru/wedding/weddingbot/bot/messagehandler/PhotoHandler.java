package ru.wedding.weddingbot.bot.messagehandler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class PhotoHandler implements MessageHandler<SendPhoto> {

  private final TelegramClient client;

  @Override
  @SneakyThrows
  public SendPhoto handle(Message message, Long chatID, String prefix) {

    SendPhoto photo = SendPhoto.builder()
        .chatId(chatID)
        .caption(prefix + message.getCaption())
        .photo(new InputFile(message.getPhoto().get(message.getPhoto().size() - 1).getFileId()))
        .build();

    client.execute(photo);
    return photo;
  }

  @Override
  public boolean support(Message message) {
    return message.hasPhoto();
  }
}
