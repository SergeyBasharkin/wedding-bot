package ru.wedding.weddingbot.bot;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.Command;

@Service
@RequiredArgsConstructor
public class WeddingBot implements LongPollingSingleThreadUpdateConsumer {


  private final List<Command> commands;
  private final TelegramClient telegramClient;

  @Override
  public void consume(Update update) {
    commands.stream()
        .filter(command -> command.support(update))
        .forEach(command -> command.handle(update));
  }

}
