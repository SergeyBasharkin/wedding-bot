package ru.wedding.weddingbot.bot.command;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Buttons;
import ru.wedding.weddingbot.bot.constants.Effect;
import ru.wedding.weddingbot.utils.FileHelper;

@Component
public class WhenItHappensCommand extends Command {

  public static final String name = Buttons.INFO;

  protected WhenItHappensCommand(TelegramClient telegramClient) {
    super(name, telegramClient);
  }

  @Override
  @SneakyThrows
  public void handle(Update update) {
    telegramClient.execute(SendPhoto.builder()
        .chatId(update.getMessage().getChatId())
        .photo(FileHelper.getFile("when.JPG"))
        .messageEffectId(Effect.LOVE)
        .build());
  }
}
