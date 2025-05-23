package ru.wedding.weddingbot.entity.type;

public enum MeatType {
  CHICKEN("Курица \uD83C\uDF57"),
  BEEF("Говядина \uD83E\uDD69"),
  PORK("Свинина \uD83C\uDF56"),
  ALL("Без разницы, главное вкусно\uD83E\uDEC4\uD83C\uDFFB");

  private String name;

  MeatType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
