package ru.wedding.weddingbot.bot.command;

import static ru.wedding.weddingbot.bot.constants.Buttons.WEDDING_PROGRAM;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Sticker;

@Component
public class WeddingProgramCommand extends Command {

  public static final String name = WEDDING_PROGRAM;

  protected WeddingProgramCommand(TelegramClient telegramClient) {
    super(name, telegramClient);
  }

  @Override
  @SneakyThrows
  public void handle(Update update) {
    telegramClient.execute(SendMessage.builder()
        .chatId(update.getMessage().getChatId())
        .text("Упс, мы обновим этот раздел позже, может для начала узнать где мы с вами встретимся?")
        .build());

    telegramClient.execute(SendSticker.builder()
            .chatId(update.getMessage().getChatId())
            .sticker(new InputFile(Sticker.ERROR))
        .build());
  }
}
