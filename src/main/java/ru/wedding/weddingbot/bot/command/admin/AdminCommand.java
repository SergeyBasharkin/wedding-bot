package ru.wedding.weddingbot.bot.command.admin;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.Command;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.service.UserService;

public abstract class AdminCommand extends Command {

  protected final UserService userService;

  protected AdminCommand(String name, TelegramClient telegramClient, UserService userService) {
    super(name, telegramClient);
    this.userService = userService;
  }

  @Override
  @SneakyThrows
  public void handle(Update update) {
    Chat chat = update.getMessage().getChat();
    Boolean isAdmin = userService.findById(chat.getId())
        .map(User::isAdmin)
        .orElse(false);
    if (isAdmin) {
      handleInternal(update);
    }
  }

  protected abstract void handleInternal(Update update);
}
