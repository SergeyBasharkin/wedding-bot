package ru.wedding.weddingbot.bot.command;

import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.entity.type.EatingType;
import ru.wedding.weddingbot.entity.type.MeatType;
import ru.wedding.weddingbot.service.UserService;

@Component
public class MeatTypeCallbackCommand extends Command{

  public static final String PREFIX = "meat_";
  private final UserService userService;

  protected MeatTypeCallbackCommand(TelegramClient telegramClient, UserService userService) {
    super(PREFIX, telegramClient);
    this.userService = userService;
  }

  @Override
  @SneakyThrows
  @Transactional
  public void handle(Update update) {
    Long chatId = update.getCallbackQuery().getMessage().getChatId();

    String type = update.getCallbackQuery().getData().split("_")[1];
    MeatType meatType = MeatType.valueOf(type);

    Long userId = update.getCallbackQuery().getFrom().getId();
    userService.findById(userId)
        .ifPresent(user -> {
          user.setMeatType(meatType);
          userService.save(user);
        });

    telegramClient.execute(
        AlcoCallbackCommand.message(chatId)
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
}
