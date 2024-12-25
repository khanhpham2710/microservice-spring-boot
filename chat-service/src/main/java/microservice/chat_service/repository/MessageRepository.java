package microservice.chat_service.repository;

import microservice.chat_service.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    public List<Message> findByChatId(@Param("chatId") String chatId);
}
