package microservice.profile_service.repository;

import microservice.profile_service.model.Address;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AddressRepository extends Neo4jRepository<Address, Long> {
}
