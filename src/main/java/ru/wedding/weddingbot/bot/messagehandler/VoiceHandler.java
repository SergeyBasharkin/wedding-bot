package ru.wedding.weddingbot.bot.messagehandler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class VoiceHandler implements MessageHandler<SendVoice> {

  private final TelegramClient client;

  @Override
  @SneakyThrows
  public SendVoice handle(Message message, Long chatID, String prefix) {
    SendVoice build = SendVoice.builder()
        .chatId(chatID)
        .caption(message.getCaption())
        .voice(new InputFile(message.getVoice().getFileId()))
        .build();
    client.execute(build);
    return build;
  }

  @Override
  public boolean support(Message message) {
    return message.hasVoice();
  }

}
