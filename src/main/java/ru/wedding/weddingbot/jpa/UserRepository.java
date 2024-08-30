package ru.wedding.weddingbot.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.wedding.weddingbot.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
