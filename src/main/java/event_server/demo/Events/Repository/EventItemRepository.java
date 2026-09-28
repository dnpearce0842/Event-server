package event_server.demo.Events.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import event_server.demo.Events.models.EventItem;

public interface EventItemRepository extends MongoRepository<EventItem, String> {
}
