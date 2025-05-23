package ru.wedding.weddingbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.wedding.weddingbot.entity.type.EatingType;
import ru.wedding.weddingbot.entity.type.MeatType;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  private Long id;

  private Long chatId;

  private String username;

  private String firstName;
  private String lastName;
  private boolean iCome = false;
  private boolean isAdmin = false;

  @Enumerated(EnumType.STRING)
  private EatingType eatingType;

  @Enumerated(EnumType.STRING)
  private MeatType meatType;

  private boolean isAlcoholic = true;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
