package ru.wedding.weddingbot.jpa.projection;

import java.sql.Clob;

public interface MessageReport {

  Clob getText();
  String getSticker();
  String getCallback();
  Long getCount();
}
