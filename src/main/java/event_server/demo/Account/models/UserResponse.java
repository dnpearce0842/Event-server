package event_server.demo.Account.models;

public record UserResponse(String id, String username, String email, String token) {
    public static UserResponse from(User user, String token) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), token);
    }
}
