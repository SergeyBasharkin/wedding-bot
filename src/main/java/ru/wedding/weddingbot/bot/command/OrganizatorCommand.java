package ru.wedding.weddingbot.bot.command;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Buttons;
import ru.wedding.weddingbot.utils.FileHelper;

@Component
public class OrganizatorCommand extends Command{

  public static final String name = Buttons.ORG;

  protected OrganizatorCommand(TelegramClient telegramClient) {
    super(name, telegramClient);
  }

  @Override
  @SneakyThrows
  public void handle(Update update) {
    telegramClient.execute(SendPhoto.builder()
            .chatId(update.getMessage().getChatId())
            .photo(FileHelper.getFile("org.JPG"))
            .caption("Организатор Кристина @kristinn_a. "
                + "К ней вы можете обращаться по поводу мероприятия и организации сюрпризов")
        .build());
  }
}
