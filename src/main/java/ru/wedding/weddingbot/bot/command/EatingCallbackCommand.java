package ru.wedding.weddingbot.bot.command;

import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.entity.type.EatingType;
import ru.wedding.weddingbot.entity.type.MeatType;
import ru.wedding.weddingbot.service.UserService;

@Component
public class EatingCallbackCommand extends Command {

  public static final String PREFIX = "eating_";
  private final UserService userService;

  protected EatingCallbackCommand(TelegramClient telegramClient, UserService userService) {
    super(PREFIX, telegramClient);
    this.userService = userService;
  }

  @Override
  @SneakyThrows
  @Transactional
  public void handle(Update update) {
    Long chatId = update.getCallbackQuery().getMessage().getChatId();

    String type = update.getCallbackQuery().getData().split("_")[1];
    EatingType eatingType = EatingType.valueOf(type);

    Long userId = update.getCallbackQuery().getFrom().getId();
    userService.findById(userId)
        .ifPresent(user -> {
          user.setEatingType(eatingType);
          userService.save(user);
        });

    if (!EatingType.MEAT.equals(eatingType)) {
      finalMsg(chatId);
      telegramClient.execute(
          AlcoCallbackCommand.message(chatId)
      );
    } else {
      telegramClient.execute(SendMessage.builder()
          .chatId(chatId)
          .text("Какое мясо вы предпочитаете?")
          .replyMarkup(replyMarkup())
          .build());
    }
  }

  private void finalMsg(Long chatId) {
    sendMessage(
        chatId,
        "Хорошо, мы учтем это при составлении меню\uD83E\uDEF6\uD83C\uDFFB"
    );
  }

  @Override
  public boolean support(Update update) {
    return Optional.ofNullable(update)
        .map(Update::getCallbackQuery)
        .map(CallbackQuery::getData)
        .filter(it -> it.contains(PREFIX))
        .isPresent();
  }

  public static InlineKeyboardMarkup replyMarkup() {
    return InlineKeyboardMarkup.builder()
        .keyboardRow(new InlineKeyboardRow(
            InlineKeyboardButton.builder()
                .text(MeatType.CHICKEN.getName())
                .callbackData(MeatTypeCallbackCommand.PREFIX + MeatType.CHICKEN)
                .build(),
            InlineKeyboardButton.builder()
                .text(MeatType.BEEF.getName())
                .callbackData(MeatTypeCallbackCommand.PREFIX + MeatType.BEEF)
                .build(),
            InlineKeyboardButton.builder()
                .text(MeatType.PORK.getName())
                .callbackData(MeatTypeCallbackCommand.PREFIX + MeatType.PORK)
                .build()
        ))
        .build();
  }
}
