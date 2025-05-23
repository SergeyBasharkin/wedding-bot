package ru.wedding.weddingbot.bot.command.helper;

import static ru.wedding.weddingbot.bot.constants.Buttons.DRESS;
import static ru.wedding.weddingbot.bot.constants.Buttons.FLOWER;
import static ru.wedding.weddingbot.bot.constants.Buttons.INFO;
import static ru.wedding.weddingbot.bot.constants.Buttons.MENU;
import static ru.wedding.weddingbot.bot.constants.Buttons.ORG;
import static ru.wedding.weddingbot.bot.constants.Buttons.WEDDING_PROGRAM;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@UtilityClass
public class ButtonsHelper {

  public static ReplyKeyboardMarkup buttons(){
    return ReplyKeyboardMarkup.builder()
        .keyboardRow(new KeyboardRow(WEDDING_PROGRAM))
        .keyboardRow(new KeyboardRow(ORG))
        .keyboardRow(new KeyboardRow(DRESS))
        .keyboardRow(new KeyboardRow(FLOWER))
        .keyboardRow(new KeyboardRow(INFO))
        .keyboardRow(new KeyboardRow(MENU))
        .build();
  }
}
