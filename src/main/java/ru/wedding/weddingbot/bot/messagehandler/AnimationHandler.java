package ru.wedding.weddingbot.bot.messagehandler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class AnimationHandler implements MessageHandler<SendAnimation> {

  private final TelegramClient client;

  @Override
  @SneakyThrows
  public SendAnimation handle(Message message, Long chatID, String prefix) {
    SendAnimation build = SendAnimation.builder()
        .chatId(chatID)
        .caption(prefix + "\n" + message.getCaption())
        .animation(new InputFile(message.getAnimation().getFileId()))
        .build();
    client.execute(build);
    return build;
  }

  @Override
  public boolean support(Message message) {
    return message.hasAnimation();
  }
}
