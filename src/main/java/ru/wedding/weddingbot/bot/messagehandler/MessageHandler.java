package ru.wedding.weddingbot.bot.messagehandler;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public interface MessageHandler<T> {

  T handle(Message message, Long chatID, String prefix);

  boolean support(Message message);


}
