package ru.wedding.weddingbot.bot.messagehandler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class DocumentHandler implements MessageHandler<SendDocument>{

  private final TelegramClient client;

  @Override
  @SneakyThrows
  public SendDocument handle(Message message, Long chatID, String prefix) {
    SendDocument msg = SendDocument.builder()
        .chatId(chatID)
        .caption(prefix + "\n" + message.getCaption())
        .document(mapDocument(message.getDocument()))
        .build();
    client.execute(msg);
    return msg;
  }

  @Override
  public boolean support(Message message) {
    return message.hasDocument();
  }


  private InputFile mapDocument(Document document) {
    return new InputFile(document.getFileId());
  }

}
