package ru.wedding.weddingbot.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.wedding.weddingbot.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsernameOrId(String userName, Long id);
  List<User> findAllByIsAdminIsTrue();

  Optional<User> findByChatId(Long chatId);

  Optional<User> findByUsername(String userName);
}
