package event_server.demo.Account.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import event_server.demo.Account.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
