package ru.wedding.weddingbot.bot.command.admin;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.Command;
import ru.wedding.weddingbot.service.UserService;

@Component
public class SetAdminCommand extends Command {

  private static final String name = "/admin 3336754";

  private final UserService userService;

  protected SetAdminCommand(TelegramClient telegramClient, UserService userService) {
    super(name, telegramClient);
    this.userService = userService;
  }

  @Override
  public void handle(Update update) {
    Long userId = update.getMessage().getFrom().getId();
    userService.findById(userId)
        .ifPresent(it -> {
          it.setAdmin(true);
          userService.save(it);
        });
  }
}
