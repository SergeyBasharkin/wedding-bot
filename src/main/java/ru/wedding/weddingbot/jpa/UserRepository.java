package ru.wedding.weddingbot.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.wedding.weddingbot.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String userName);
}
