package ru.wedding.weddingbot.bot.command;

import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.wedding.weddingbot.bot.command.helper.ComeCommandHelper;
import ru.wedding.weddingbot.bot.constants.Effect;
import ru.wedding.weddingbot.bot.constants.Sticker;
import ru.wedding.weddingbot.service.UserService;

@Component
public class IDidntComeCommand extends Command {

  public static final String name = "i_didnt_come";
  private final UserService userService;

  protected IDidntComeCommand(TelegramClient telegramClient, UserService userService) {
    super(name, telegramClient);
    this.userService = userService;
  }

  public static SendSticker badSticker(Long chatId) {
    return SendSticker.builder()
        .chatId(chatId)
        .sticker(new InputFile(Sticker.BAD))
        .build();
  }

  public static SendMessage badMessage(Long chatId) {
    return SendMessage.builder()
        .chatId(chatId)
        .messageEffectId(Effect.DISLIKE)
        .text("""
        Нам очень жаль\uD83D\uDE14 Мы надеемся, что у тебя все же получится присутствовать и твое решение поменяется!
        
        Если вдруг твое решение поменяется, сообщи об этом нам, нажав на кнопку снизу.
        """)
        .replyMarkup(IComeCommand.replyMarkup())
        .build();
  }

  @Override
  public void handle(Update update) {
    Optional.ofNullable(update)
        .map(Update::getCallbackQuery)
        .map(CallbackQuery::getFrom)
        .map(org.telegram.telegrambots.meta.api.objects.User::getId)
        .flatMap(userService::findById)
        .ifPresent(it -> {
          it.setICome(false);
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
    telegramClient.execute(badSticker(replyMsg.getChatId()));
    telegramClient.execute(badMessage(replyMsg.getChatId()));
  }

  @Override
  public boolean support(Update update) {
    return callback(update);
  }

}
