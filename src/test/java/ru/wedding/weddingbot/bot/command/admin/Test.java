package ru.wedding.weddingbot.bot.command.admin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
  private static final Pattern regex = Pattern.compile("^(/sendAll) (.*)$", Pattern.DOTALL);

  public static void main(String[] args) {
    Matcher matcher = regex.matcher("/sendAll Привет, мы выбираем салаты для нашего праздничного меню, хотим спросить у вас с каким белком вы предпочитаете салаты? (креветки, сыр, рыба, мясо и т.д.) \n"
        + "\n"
        + "Напишите прямо в бот свои предпочтения по белку или скажите какой обычно салат вы заказываете \uD83E\uDD57");
    System.out.println(matcher.matches());

  }

}
