package ru.wedding.weddingbot.bot.command.admin;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.helper.NameHelper;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.entity.type.EatingType;
import ru.wedding.weddingbot.entity.type.MeatType;
import ru.wedding.weddingbot.service.UserService;

@Component
public class UsersCommand extends AdminCommand {

  public static final String name = "/users";

  protected UsersCommand(TelegramClient telegramClient, UserService userService) {
    super(name, telegramClient, userService);
  }

  @Override
  @SneakyThrows
  protected void handleInternal(Update update) {
    String users = userService.findAll().stream()
        .map(this::msg)
        .collect(Collectors.joining("\n\n"));
    telegramClient.execute(SendMessage.builder()
        .chatId(update.getMessage().getChatId())
        .text(users)
        .build());
  }

  private String msg(User it) {
    String eatingType = Optional.ofNullable(it.getEatingType())
        .map(EatingType::getName)
        .orElse("-");
    if (EatingType.MEAT.getName().equals(eatingType)) {
      eatingType = Optional.ofNullable(it.getMeatType())
          .map(MeatType::getName)
          .orElse(eatingType);
    }
    return "@" + Optional.ofNullable(it.getUsername()).orElse(String.valueOf(it.getId())) + " " +
        NameHelper.name(it) + " " +
        (it.isICome() ? "Придёт" : "Не придёт") + " " +
        eatingType + " " +
        (it.isAlcoholic() ? "Пьёт": "Не пьёт") + " " +
        (it.isAdmin() ? "АДМИН" : "");

  }
}
