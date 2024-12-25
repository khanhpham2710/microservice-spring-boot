package microservice.chat_service.repository;

import microservice.chat_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    public User findByEmail(String email);

    public List<User> findByEmailContaining(@Param("query") String query);

}
