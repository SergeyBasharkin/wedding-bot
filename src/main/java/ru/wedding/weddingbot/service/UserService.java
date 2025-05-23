package ru.wedding.weddingbot.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wedding.weddingbot.entity.User;
import ru.wedding.weddingbot.jpa.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public void save(User user) {
    userRepository.save(user);
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  public List<User> findAdmins() {
    return userRepository.findAllByIsAdminIsTrue();
  }

  public Optional<User> findByChatId(Long chatId) {
    return userRepository.findByChatId(chatId);
  }

  public Optional<User> findByUsernameOrId(String username, Long id) {
    return userRepository.findByUsernameOrId(username, id);
  }
}
