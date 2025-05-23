package ru.wedding.weddingbot.bot.command.admin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.Command;
import ru.wedding.weddingbot.service.UserService;

@Component
public class AdminInfoCommand extends AdminCommand {

  public static final String name = "/info";


  private final Set<String> commands;

  protected AdminInfoCommand(
      TelegramClient telegramClient,
      UserService userService,
      @Lazy Set<String> commands) {
    super(name, telegramClient, userService);
    this.commands = commands;
  }

  @Override
  @SneakyThrows
  protected void handleInternal(Update update) {
    String message = String.join("\n", commands);
    sendMessage(update.getMessage().getChatId(), message);
  }
}
