package ru.wedding.weddingbot.bot.command;

import static ru.wedding.weddingbot.bot.command.BadCommand.badCommand;
import static ru.wedding.weddingbot.bot.command.SalutCommand.salutCommand;

import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.constants.Effect;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.service.UserService;

@Component
public class IComeCommand extends Command {

  public static final String name = "i_come";
  public static final String iComeMessage = """
      P.S.
      Используй кнопку "Я приду :smile:", чтобы мы знали, что ты будешь :heart_hands:
        """;
  public static final String iComeButton = "Я приду ✅";
  public static final String iDidntComeButton = "Я не приду \uD83D\uDE14";
  private final UserService userService;

  protected IComeCommand(TelegramClient telegramClient, UserService userService) {
    super(name, telegramClient);
    this.userService = userService;
  }

  public static SendMessage buildMessage(User user) {
    return SendMessage.builder()
        .chatId(user.getChatId())
        .text(IComeCommand.iComeMessage)
        .replyMarkup(replyMarkup(user))
        .messageEffectId(Effect.SALUT)
        .build();
  }

  public static InlineKeyboardMarkup replyMarkup(User user) {
    return InlineKeyboardMarkup.builder()
        .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton.builder()
            .callbackData(IComeCommand.name)
            .text(user.isICome() ? iDidntComeButton : iComeButton)
            .build()))
        .build();
  }

  @Override
  @Transactional
  public void handle(Update update) {
    Optional.ofNullable(update)
        .map(Update::getCallbackQuery)
        .map(CallbackQuery::getFrom)
        .map(org.telegram.telegrambots.meta.api.objects.User::getId)
        .flatMap(userService::findById)
        .ifPresent(it -> {
          it.setICome(!it.isICome());
          userService.save(it);
          MaybeInaccessibleMessage replyMsg = update.getCallbackQuery().getMessage();
          sendMessage(replyMsg, it);
        });
  }

  @SneakyThrows
  private void sendMessage(MaybeInaccessibleMessage replyMsg, User user) {
    EditMessageText message = EditMessageText.builder()
        .chatId(replyMsg.getChatId())
        .text(iComeMessage)
        .messageId(replyMsg.getMessageId())
        .replyMarkup(replyMarkup(user))
        .build();
    telegramClient.execute(message);
    telegramClient.execute(user.isICome() ? salutCommand(replyMsg.getChatId()) : badCommand(replyMsg.getChatId()));
  }

  @Override
  public boolean support(Update update) {
    return callback(update);
  }
}
