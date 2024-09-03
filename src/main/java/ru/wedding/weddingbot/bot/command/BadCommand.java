package ru.wedding.weddingbot.bot.command;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Effect;
import ru.wedding.weddingbot.bot.constants.Sticker;

@Component
public class BadCommand extends Command{

  public static final String name = "didntCome";

  protected BadCommand(TelegramClient telegramClient) {
    super(name, telegramClient);
  }

  @Override
  @SneakyThrows
  public void handle(Update update) {
    CallbackQuery callbackQuery = update.getCallbackQuery();
    telegramClient.execute(badCommand(callbackQuery.getMessage().getChatId()));
  }

  public static SendSticker badCommand(Long chatId){
    return SendSticker.builder()
        .chatId(chatId)
        .messageEffectId(Effect.SALUT)
        .sticker(new InputFile(Sticker.BAD))
        .build();
  }

  @Override
  public boolean support(Update update) {
    return callback(update);
  }
}
