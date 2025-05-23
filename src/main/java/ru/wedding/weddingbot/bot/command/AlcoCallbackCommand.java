package ru.wedding.weddingbot.bot.command;

import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Sticker;
import ru.wedding.weddingbot.entity.type.MeatType;
import ru.wedding.weddingbot.service.UserService;
import ru.wedding.weddingbot.utils.FileHelper;

@Component
public class AlcoCallbackCommand extends Command {

  public static final String PREFIX = "alco_";
  private final UserService userService;

  protected AlcoCallbackCommand(TelegramClient telegramClient, UserService userService) {
    super(PREFIX, telegramClient);
    this.userService = userService;
  }

  @Override
  @SneakyThrows
  @Transactional
  public void handle(Update update) {
    Long chatId = update.getCallbackQuery().getMessage().getChatId();

    String type = update.getCallbackQuery().getData().split("_")[1];

    Long userId = update.getCallbackQuery().getFrom().getId();
    userService.findById(userId)
        .ifPresent(user -> {
          user.setAlcoholic(type.equals("yes"));
          userService.save(user);
        });

    sendSticker(chatId, Sticker.MONKEY);
    sendMessage(chatId, "Спасибо, что ответили на вопросы, если у вас есть аллергия на какие-то продукты напишите, "
        + "пожалуйста, боту, что нам следует учесть\uD83D\uDE0A\n"
        + "Также можете дополнить свой ответ комментарием\uD83D\uDCDD");
  }

  public static SendMessage message(Long chatId){
    return SendMessage.builder()
        .chatId(chatId)
        .text("Вы пьете алкоголь?")
        .replyMarkup(InlineKeyboardMarkup.builder()
            .keyboardRow(new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("Да\uD83C\uDF7E")
                    .callbackData("alco_yes")
                    .build(),
                InlineKeyboardButton.builder()
                    .text("Нет\uD83E\uDDC3")
                    .callbackData("alco_no")
                    .build()
            ))
            .build())
        .build();
  }

  @Override
  public boolean support(Update update) {
    return Optional.ofNullable(update)
        .map(Update::getCallbackQuery)
        .map(CallbackQuery::getData)
        .filter(it -> it.contains(PREFIX))
        .isPresent();
  }
}
