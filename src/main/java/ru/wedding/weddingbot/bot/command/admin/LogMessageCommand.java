package ru.wedding.weddingbot.bot.command.admin;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.Command;
import ru.wedding.weddingbot.service.MessageService;

@Component
public class LogMessageCommand extends Command {

  private final MessageService messageService;

  protected LogMessageCommand(TelegramClient telegramClient, MessageService messageService) {
    super("/log", telegramClient);
    this.messageService = messageService;
  }

  @Override
  @Transactional
  public void handle(Update update) {
    Message tgMsg = Optional.ofNullable(update)
        .map(Update::getMessage)
        .orElse(null);
    CallbackQuery callback = Optional.ofNullable(update)
        .map(Update::getCallbackQuery)
        .orElse(null);
    Message editedMsg = Optional.ofNullable(update)
        .map(Update::getEditedMessage)
        .orElse(null);

    ru.wedding.weddingbot.entity.Message message = new ru.wedding.weddingbot.entity.Message();
    if (tgMsg != null) {
      message.setId(String.valueOf(tgMsg.getMessageId()));
      message.setText(tgMsg.getText());
      message.setUserId(tgMsg.getFrom().getId());
      if (tgMsg.getSticker() != null) {
        message.setSticker(tgMsg.getSticker().getFileId());
      }
    }

    if (callback != null && (callback.getMessage() != null)) {
      message.setId(callback.getId());
      message.setCallbackTrigger(callback.getData());
      message.setUserId(callback.getFrom().getId());
    }

    if (editedMsg != null) {
      message.setId(editedMsg.getMessageId() + "_edited");
      message.setUserId(editedMsg.getFrom().getId());
      message.setText(editedMsg.getText());
    }

    if (StringUtils.isNotBlank(message.getId())){
      messageService.save(message);
    }

  }

  @Override
  public boolean support(Update update) {
    return true;
  }
}
