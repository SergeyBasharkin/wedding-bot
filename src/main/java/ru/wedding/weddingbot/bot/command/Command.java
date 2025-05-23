package ru.wedding.weddingbot.bot.command;

import java.util.Optional;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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
        .filter(it -> it.equals(getName()))
        .isPresent();
  }

  public boolean callback(Update update) {
    return Optional.ofNullable(update)
        .map(Update::getCallbackQuery)
        .map(CallbackQuery::getData)
        .filter(it -> it.equals(getName()))
        .isPresent();
  }


  @SneakyThrows
  public void sendMessage(Long chatId, String text) {
    telegramClient.execute(SendMessage.builder()
        .chatId(chatId)
        .text(text)
        .build());
  }


  @SneakyThrows
  public void sendSticker(Long chatId, String sticker) {
    telegramClient.execute(SendSticker.builder()
        .chatId(chatId)
        .sticker(new InputFile(sticker))
        .build());
  }

  @SneakyThrows
  public void sendPhoto(Long chatId, InputFile photo, String caption){
    telegramClient.execute(SendPhoto.builder()
            .chatId(chatId)
            .photo(photo)
            .caption(caption)
        .build());
  }
  public String getName() {
    return name;
  }
}
