package ru.wedding.weddingbot.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.wedding.weddingbot.entity.Message;
import ru.wedding.weddingbot.jpa.MessageRepository;
import ru.wedding.weddingbot.jpa.projection.MessageReport;

@Service
@AllArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;

  public List<MessageReport> findByUsernameOrId(String username, Long id) {
    return messageRepository.findAllByUser_Username(username, id);
  }

  public void save(Message message) {
    messageRepository.save(message);
  }

  public Optional<Message> findById(String id) {
    return messageRepository.findById(id);
  }
}
