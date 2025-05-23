package ru.wedding.weddingbot.bot.messagehandler;

import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class TextHandler implements MessageHandler<SendMessage> {

  private final Set<String> commands;
  private final TelegramClient client;

  public TextHandler(@Lazy Set<String> commands, TelegramClient client) {
    this.commands = commands;
    this.client = client;
  }

  @Override
  @SneakyThrows
  public SendMessage handle(Message message, Long chatId, String prefix) {
    SendMessage msg = SendMessage.builder()
        .chatId(chatId)
        .text(prefix + "\n" + message.getText())
        .build();
    client.execute(msg);
    return msg;
  }

  @Override
  public boolean support(Message message) {
    if (!message.hasText()) {
      return false;
    }

    return isNotCommandOrAdminCommand(message);
  }


  private boolean isNotCommandOrAdminCommand(Message message) {

    String messageText = message.getText();

    if (message.isCommand()) {
      return commands.stream()
          .noneMatch(messageText::contains);
    }

    return !commands.contains(messageText);
  }


}
