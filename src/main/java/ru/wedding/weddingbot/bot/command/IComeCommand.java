package ru.wedding.weddingbot.bot.command;

import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.helper.ComeCommandHelper;
import ru.wedding.weddingbot.bot.constants.Effect;
import ru.wedding.weddingbot.bot.constants.Sticker;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.service.UserService;

@Component
public class IComeCommand extends Command {

  public static final String name = "i_come";

  public static final String iComeButton = "Я приду ✅";
  public static final String iDidntComeButton = "Я не приду \uD83D\uDE14";
  private final UserService userService;

  protected IComeCommand(TelegramClient telegramClient, UserService userService) {
    super(name, telegramClient);
    this.userService = userService;
  }

  public static SendSticker salutSticker(Long chatId) {
    return SendSticker.builder()
        .chatId(chatId)
        .messageEffectId(Effect.SALUT)
        .sticker(new InputFile(Sticker.SALUT))
        .build();
  }

  public static SendMessage salutMessage(Long chatId) {
    return SendMessage.builder()
        .chatId(chatId)
        .text("""
        Мы будем тебя ждать!
        
        Все интересующие тебя вопросы, по месту/времени/программе/контактам организатора, можешь увидеть в меню внизу.
        
        Если вдруг твое решение поменяется, сообщи об этом нам, нажав на кнопку снизу.
        """)
        .replyMarkup(IComeCommand.replyMarkup())
        .build();
  }

  public static SendMessage buildMessage(User user) {
    return SendMessage.builder()
        .chatId(user.getChatId())
        .text(ComeCommandHelper.iComeMessage)
        .replyMarkup(replyMarkup())
        .messageEffectId(Effect.SALUT)
        .build();
  }

  public static InlineKeyboardMarkup replyMarkup() {
    return InlineKeyboardMarkup.builder()
        .keyboardRow(new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .callbackData(IComeCommand.name)
                    .text(iComeButton)
                    .build(),
                InlineKeyboardButton.builder()
                    .callbackData(IDidntComeCommand.name)
                    .text(iDidntComeButton)
                    .build()
            )
        )
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
          it.setICome(true);
          userService.save(it);
          MaybeInaccessibleMessage replyMsg = update.getCallbackQuery().getMessage();
          sendMessage(replyMsg);
        });
  }

  @SneakyThrows
  private void sendMessage(MaybeInaccessibleMessage replyMsg) {
    EditMessageText message = EditMessageText.builder()
        .chatId(replyMsg.getChatId())
        .text(ComeCommandHelper.msg(replyMsg))
        .messageId(replyMsg.getMessageId())
        .build();
    telegramClient.execute(message);
    telegramClient.execute(salutSticker(replyMsg.getChatId()));
    telegramClient.execute(salutMessage(replyMsg.getChatId()));
  }

  @Override
  public boolean support(Update update) {
    return callback(update);
  }
}
