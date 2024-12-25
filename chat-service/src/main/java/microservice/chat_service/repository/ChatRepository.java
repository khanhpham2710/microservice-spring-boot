package microservice.chat_service.repository;

import microservice.chat_service.model.Chat;
import microservice.chat_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByUsersContaining(User user);

    @Query("{'users': {$all: [?0, ?1]}, 'isGroup': false}")
    Optional<Chat> findSingleChatByUserIds(String userId1, String userId2);
}
