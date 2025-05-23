package ru.wedding.weddingbot.bot.command.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Clob;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.jpa.projection.MessageReport;
import ru.wedding.weddingbot.service.MessageService;
import ru.wedding.weddingbot.service.UserService;

@Component
public class MessageCommand extends AdminCommand {

  public static final String name = "/message";
  private static final Pattern regex = Pattern.compile("^(/message) (@\\S+)$");
  private final MessageService messageService;

  protected MessageCommand(TelegramClient telegramClient, MessageService messageService,
      UserService userService) {
    super(name, telegramClient, userService);
    this.messageService = messageService;
  }


  @Override
  @SneakyThrows
  protected void handleInternal(Update update) {
    Long chatId = update.getMessage().getChatId();
    String msg = update.getMessage().getText();
    Matcher matcher = regex.matcher(msg);
    if (matcher.matches()) {
      String username = matcher.group(2).substring(1);
      Long id;
      try {
        id = Long.valueOf(username);
      } catch (Exception ignore){
        id = -1L;
      }
      List<MessageReport> messages = messageService.findByUsernameOrId(username, id);

      messages.stream()
          .filter(it -> StringUtils.isNotBlank(it.getSticker()))
          .forEach(it -> sendSticker(it, chatId));

      String fullMessage = messages.stream()
          .filter(it -> StringUtils.isBlank(it.getSticker()))
          .map(this::buildText)
          .collect(Collectors.joining("\n\n"));

      StringReader reader = new StringReader(fullMessage);
      char[] buffer = new char[4000];
      int charsRead;
      while ((charsRead = reader.read(buffer)) != -1) {
        sendMessage(chatId, new String(buffer, 0, charsRead));
      }
    } else {
      telegramClient.execute(SendMessage.builder()
          .chatId(update.getMessage().getChatId())
          .text("Ошибка формата. Формат команды /message @user 1")
          .build());
    }
  }

  @SneakyThrows
  private void sendText(String text, Long chatId) {
    telegramClient.execute(SendMessage.builder()
        .chatId(chatId)
        .text(text)
        .build());
  }

  @SneakyThrows
  private void sendSticker(MessageReport it, Long chatId) {
    telegramClient.execute(SendSticker.builder()
        .sticker(new InputFile(it.getSticker()))
        .chatId(chatId)
        .build());
  }

  @SneakyThrows
  private String buildText(MessageReport report) {
    Clob text = report.getText();
    String slash = "\nкол-во: ";
    String callback = report.getCallback();
    Long count = report.getCount();
    StringBuilder result = new StringBuilder();
    if (text != null) {
      result.append(IOUtils.toString(text.getCharacterStream())).append(slash);
    }
    if (StringUtils.isNotBlank(callback)) {
      result.append(callback).append(slash);
    }
    result.append(count);
    return result.toString();
  }

  @Override
  public boolean support(Update update) {
    return Optional.ofNullable(update)
        .map(Update::getMessage)
        .map(Message::getText)
        .map(it -> it.contains(name))
        .orElse(false);
  }

}
