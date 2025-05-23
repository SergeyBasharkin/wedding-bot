package ru.wedding.weddingbot.conf;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.WeddingBot;
import ru.wedding.weddingbot.bot.command.Command;

@Configuration
public class BotConfiguration {

  @Bean
  public TelegramClient telegramClient(@Value("${app.bot-token}") String token) {
    return new OkHttpTelegramClient(token);
  }

  @Bean
  public TelegramBotsLongPollingApplication app(
      @Value("${app.bot-token}") String token,
      WeddingBot bot
  ) throws TelegramApiException {
    TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
    botsApplication.registerBot(token, bot);
    return botsApplication;
  }

  @Bean
  public Set<String> commands(List<Command> commands) {
    return commands.stream().map(Command::getName).collect(Collectors.toSet());
  }
}
