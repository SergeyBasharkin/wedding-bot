package ru.wedding.weddingbot.bot.command;

import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class Command {

  private final String name;
  protected final TelegramClient telegramClient;

  protected Command(String name, TelegramClient telegramClient) {
    this.name = name;
    this.telegramClient = telegramClient;
  }

  public abstract void handle(Update update);

  public boolean support(Update update) {
    return Optional.ofNullable(update)
        .map(Update::getMessage)
        .map(Message::getText)
        .filter(it -> it.equals(name))
        .isPresent();
  }
}
