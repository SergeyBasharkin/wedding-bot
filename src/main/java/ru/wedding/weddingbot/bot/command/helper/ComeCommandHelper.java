package ru.wedding.weddingbot.bot.command.helper;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@UtilityClass
public class ComeCommandHelper {
  public static final String iComeMessage = """
      Используй кнопку "Я приду ✅", чтобы мы знали, что ты будешь 🫶🏻
        """;

  public static String msg(MaybeInaccessibleMessage replyMsg){
    return Optional.of(replyMsg)
        .filter(Message.class::isInstance)
        .map(Message.class::cast)
        .map(Message::getText)
        .orElse(iComeMessage);
  }

  public static MaybeInaccessibleMessage getMessage(Update update) {
    if (update.hasMessage()) {
      return update.getMessage();
    } else if (update.hasCallbackQuery()){
      return update.getCallbackQuery().getMessage();
    } else if (update.hasEditedMessage()) {
      return update.getEditedMessage();
    } else return null;
  }
}
