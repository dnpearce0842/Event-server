package event_server.demo.Events.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_items")
public class EventItem {
    @Id
    private String id;
    private Instant dateCreated;
    private Instant eventDate;
    private Integer capacity;
    private String title;
    private String description;
    private Double fee;
    private List<Map<String, Object>> people = new ArrayList<>();
    private String ownerId;
    private boolean atMun;
    private String address;
    private String room;
    private String locationDetails;

    public EventItem() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Instant getDateCreated() { return dateCreated; }
    public void setDateCreated(Instant dateCreated) { this.dateCreated = dateCreated; }
    public Instant getEventDate() { return eventDate; }
    public void setEventDate(Instant eventDate) { this.eventDate = eventDate; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getFee() { return fee; }
    public void setFee(Double fee) { this.fee = fee; }
    public List<Map<String, Object>> getPeople() { return people; }
    public void setPeople(List<Map<String, Object>> people) { this.people = people; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public boolean isAtMun() { return atMun; }
    public void setAtMun(boolean atMun) { this.atMun = atMun; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getLocationDetails() { return locationDetails; }
    public void setLocationDetails(String locationDetails) { this.locationDetails = locationDetails; }
}
