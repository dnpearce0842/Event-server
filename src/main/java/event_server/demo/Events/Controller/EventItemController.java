package event_server.demo.Events.Controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import event_server.demo.Events.Repository.EventItemRepository;
import event_server.demo.Events.models.EventItem;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "${app.cors.allowed-origin:http://localhost:3000}")
public class EventItemController {
    private final EventItemRepository eventItems;

    public EventItemController(EventItemRepository eventItems) {
        this.eventItems = eventItems;
    }

    @GetMapping
    public List<EventItem> getEvents() {
        return eventItems.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventItem> getEvent(@PathVariable String id) {
        return eventItems.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventItem> createEvent(@RequestBody EventItem eventItem) {
        eventItem.setId(UUID.randomUUID().toString());
        if (eventItem.getDateCreated() == null) {
            eventItem.setDateCreated(Instant.now());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(eventItems.save(eventItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        if (!eventItems.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventItems.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
