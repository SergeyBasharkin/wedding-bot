package ru.wedding.weddingbot.bot.command;

import java.io.FileInputStream;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Buttons;
import ru.wedding.weddingbot.utils.FileHelper;


@Component
public class WhatDresscode extends Command {

  public static final String name = Buttons.DRESS;

  protected WhatDresscode(TelegramClient telegramClient) {
    super(name, telegramClient);
  }

  @Override
  @SneakyThrows
  public void handle(Update update) {
    List<InputMediaPhoto> inputFiles = List.of(
        InputMediaPhoto.builder()
            .media(FileHelper.getInputStream("dressCode.JPG"),
                "dressCode.JPG")
            .caption(
                "Мы очень просим вас прийти, учитывая дресс-код. Это важно для нас, чтобы передать настроение вечера и получились красивые фотографии<3")
            .build(),
        InputMediaPhoto.builder()
            .media(FileHelper.getInputStream("dressCircle.JPG"),
                "dressCircle.JPG")
            .build()
    );
    telegramClient.execute(SendMediaGroup.builder()
        .medias(inputFiles)
        .chatId(update.getMessage().getChatId())
        .build());

    List<InputMediaPhoto> examples = List.of(
        InputMediaPhoto.builder()
            .media(FileHelper.getInputStream("example/1.jpg"),
                "1.jpg")
            .caption(
                "Референсы для луков \uD83C\uDFF9")
            .build(),
        InputMediaPhoto.builder()
            .media(FileHelper.getInputStream("example/2.jpg"),
                "2.jpg")
            .build(),
        InputMediaPhoto.builder()
            .media(FileHelper.getInputStream("example/3.jpg"),
                "3.jpg")
            .build(),
        InputMediaPhoto.builder()
            .media(FileHelper.getInputStream("example/4.jpg"),
                "4.jpg")
            .build(),
        InputMediaPhoto.builder()
            .media(FileHelper.getInputStream("example/5.jpg"),
                "5.jpg")
            .build()
    );

    telegramClient.execute(SendMediaGroup.builder()
        .medias(examples)
        .chatId(update.getMessage().getChatId())
        .build());


  }
}
