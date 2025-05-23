package ru.wedding.weddingbot.bot;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.Command;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeddingBot implements LongPollingSingleThreadUpdateConsumer {


  private final List<Command> commands;

  @Override
  public void consume(Update update) {
    commands.stream()
        .filter(command -> command.support(update))
        .forEach(command -> {
          try {
            command.handle(update);
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        });
  }

}
