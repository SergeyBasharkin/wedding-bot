package ru.wedding.weddingbot.bot.command;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Buttons;
import ru.wedding.weddingbot.entity.type.EatingType;
import ru.wedding.weddingbot.utils.FileHelper;

@Component
public class MenuCommand extends Command {

  private static final String name = Buttons.MENU;
  private static final String msg =
      "Для того чтобы всем было комфортно, мы решили спросить у каждого,"
          + " что он хочет видеть на столе, выберите правильный для себя вариант";

  protected MenuCommand(TelegramClient telegramClient) {
    super(name, telegramClient);
  }

  @Override
  @SneakyThrows
  public void handle(Update update) {
    Long chatId = update.getMessage().getChatId();
    telegramClient.execute(
        SendPhoto.builder()
            .chatId(chatId)
            .photo(FileHelper.getFile("matrix.JPG"))
            .caption(msg)
            .replyMarkup(replyMarkup())
            .build()
    );
  }

  public static InlineKeyboardMarkup replyMarkup() {
    return InlineKeyboardMarkup.builder()
        .keyboardRow(new InlineKeyboardRow(
            InlineKeyboardButton.builder()
                .text(EatingType.FISH.getName())
                .callbackData(EatingCallbackCommand.PREFIX + EatingType.FISH)
                .build(),
            InlineKeyboardButton.builder()
                .text(EatingType.MEAT.getName())
                .callbackData(EatingCallbackCommand.PREFIX + EatingType.MEAT)
                .build(),
            InlineKeyboardButton.builder()
                .text(EatingType.VEGAN.getName())
                .callbackData(EatingCallbackCommand.PREFIX + EatingType.VEGAN)
                .build()
        ))
        .build();
  }
}
