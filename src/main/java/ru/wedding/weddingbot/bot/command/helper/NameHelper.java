package ru.wedding.weddingbot.bot.command.helper;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import ru.wedding.weddingbot.entity.User;

@UtilityClass
public class NameHelper {

  public static String name(User user) {
    String name = Optional.ofNullable(user.getFirstName())
        .orElse(" ")
        + Optional.ofNullable(user.getLastName())
        .map(lastName -> " " + lastName)
        .orElse("");
    return user.getFirstName() == null ?
        user.getFirstName() : name;
  }

  public static String name(Chat chat) {
    StringBuilder sb = new StringBuilder();
    sb.append("@");
    if (StringUtils.isNotBlank(chat.getUserName())) {
      sb.append(chat.getUserName()).append(" ");
    } else {
      sb.append(chat.getId()).append(" ");
    }

    if (StringUtils.isNotBlank(chat.getFirstName())) {
      sb.append(chat.getFirstName()).append(" ");
    }
    if (StringUtils.isNotBlank(chat.getLastName())) {
      sb.append(chat.getLastName()).append(" ");
    }

    return sb.toString();
  }

  public static String nickname(User user) {
    StringBuilder sb = new StringBuilder();
    sb.append("@");
    if (StringUtils.isNotBlank(user.getUsername())) {
      sb.append(user.getUsername()).append(" ");
    } else {
      sb.append(user.getId()).append(" ");
    }

    if (StringUtils.isNotBlank(user.getFirstName())) {
      sb.append(user.getFirstName()).append(": ");
    }

    return sb.toString();
  }
}
