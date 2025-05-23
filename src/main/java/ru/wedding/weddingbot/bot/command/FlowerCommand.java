package ru.wedding.weddingbot.bot.command;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Buttons;
import ru.wedding.weddingbot.utils.FileHelper;

@Component
public class FlowerCommand extends Command {

  public static final String name = Buttons.FLOWER;

  protected FlowerCommand(
      TelegramClient telegramClient) {
    super(name, telegramClient);
  }

  @Override
  @SneakyThrows
  public void handle(Update update) {
    telegramClient.execute(SendPhoto.builder()
        .chatId(update.getMessage().getChatId())
        .photo(FileHelper.getFile("flower.JPG"))
        .caption("""
            К сожалению, долго наслаждаться красотой подаренных цветов нам не удастся\\.
            
            Поэтому вместо них мы предлагаем вам подарить нам цветочную подписку\\.
            
            Вы можете написать в:
             1\\. [Телеграм](https://t.me/utaflowers)
             2\\. [Инстаграм](https://www.instagram.com/uta.flowers?igsh=MThmd2UzZmVtMjlwYQ==)
             3\\. Любой другой мессенджер по номеру 89871814437
             
            С кодовым словом: "Маша и Сережа"\\, оплатить сертификат на любую сумму, написать свое \
             пожелание и каждую неделю нам будет приходить новый букет с вашими подписями 💗
            """)
        .parseMode("MarkdownV2")
        .build());
  }
}
