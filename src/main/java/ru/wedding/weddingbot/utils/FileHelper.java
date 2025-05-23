package ru.wedding.weddingbot.utils;

import java.io.InputStream;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@UtilityClass
public class FileHelper {

  @SneakyThrows
  public static InputFile getFile(String fileName) {
    return new InputFile(FileHelper.class.getResourceAsStream("/static/"+fileName), fileName);
  }
  @SneakyThrows
  public static InputStream getInputStream(String fileName) {
    return FileHelper.class.getResourceAsStream("/static/"+fileName);
  }

}
