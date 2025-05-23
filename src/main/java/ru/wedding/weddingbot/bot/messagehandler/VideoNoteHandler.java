package ru.wedding.weddingbot.bot.messagehandler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class VideoNoteHandler implements MessageHandler<SendVideoNote> {

  private final TelegramClient client;

  @Override
  @SneakyThrows
  public SendVideoNote handle(Message message, Long chatID, String prefix) {
    client.execute(SendMessage.builder()
        .chatId(chatID)
        .text(prefix)
        .build());

    client.execute(SendVideoNote.builder()
        .chatId(chatID)
        .videoNote(new InputFile(message.getVideoNote().getFileId()))
        .build());
    return null;
  }

  @Override
  public boolean support(Message message) {
    return message.hasVideoNote();
  }
}
