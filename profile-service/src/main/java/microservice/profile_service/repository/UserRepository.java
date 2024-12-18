package microservice.profile_service.repository;

import microservice.profile_service.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, String> {
    User findByEmail(String email);

    List<User> findByFriendsEmail(String email);

    List<User> findByFollowersEmail(String email);

    List<User> findByFollowingEmail(String email);
}
