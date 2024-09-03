package ru.wedding.weddingbot.bot.command;

import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Effect;
import ru.wedding.weddingbot.bot.constants.Sticker;

@Component
public class SalutCommand extends Command{

  public static final String name = "salut";

  protected SalutCommand(TelegramClient telegramClient) {
    super(name, telegramClient);
  }

  public static SendSticker salutCommand(Long chatId){
    return SendSticker.builder()
        .chatId(chatId)
        .messageEffectId(Effect.SALUT)
        .sticker(new InputFile(Sticker.SALUT))
        .build();
  }

  @Override
  @SneakyThrows
  public void handle(Update update) {
    CallbackQuery callbackQuery = update.getCallbackQuery();
    telegramClient.execute(salutCommand(callbackQuery.getMessage().getChatId()));
  }

  @Override
  public boolean support(Update update) {
    return callback(update);
  }


}
