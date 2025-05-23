package ru.wedding.weddingbot.bot.messagehandler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class AudioHandler implements MessageHandler<SendAudio> {

  private final TelegramClient client;

  @Override
  @SneakyThrows
  public SendAudio handle(Message message, Long chatID, String prefix) {
    SendAudio msg = SendAudio.builder()
        .chatId(chatID)
        .caption(prefix + "\n" + message.getCaption())
        .audio(new InputFile(message.getAudio().getFileId()))
        .build();
    client.execute(msg);
    return msg;
  }

  @Override
  public boolean support(Message message) {
    return message.hasAudio();
  }

}