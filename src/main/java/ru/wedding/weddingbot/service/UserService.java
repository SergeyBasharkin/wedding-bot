package ru.wedding.weddingbot.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.jpa.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public void save(User user) {
    userRepository.save(user);
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  public Optional<User> findByUsername(String userName) {
    return userRepository.findByUsername(userName);
  }
}
