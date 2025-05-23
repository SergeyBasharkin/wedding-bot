package ru.wedding.weddingbot.jpa;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.wedding.weddingbot.entity.Message;
import ru.wedding.weddingbot.jpa.projection.MessageReport;

public interface MessageRepository extends JpaRepository<Message, String> {

  @Query(value =
      "select m.text as text,"
          + " m.sticker as sticker,"
          + " m.callback_trigger as callback,"
          + " count(m.callback_trigger) + count(m.text) + count(m.sticker) as count "
          + "from messages m inner join users u on u.id = m.user_id\n"
          + "where u.username = :username OR u.id = :id "
          + "group by m.callback_trigger, m.text, m.sticker", nativeQuery = true)
  List<MessageReport> findAllByUser_Username(String username, Long id);

  Message findByText(String text);

}
