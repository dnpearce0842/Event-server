package event_server.demo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventItemRepository extends MongoRepository<EventItem, String> {
}
