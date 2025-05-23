package ru.wedding.weddingbot.entity.type;

public enum EatingType {
  FISH("Рыба \uD83D\uDC20"),
  MEAT("Мясо \uD83E\uDD69"),
  VEGAN("Веган \uD83C\uDF31");

  private String name;

  EatingType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
