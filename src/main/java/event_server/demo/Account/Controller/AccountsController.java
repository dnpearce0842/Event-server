package event_server.demo.Account.Controller;

import java.util.UUID;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import event_server.demo.Account.Repository.UserRepository;
import event_server.demo.Account.TokenService;
import event_server.demo.Account.models.LoginRequest;
import event_server.demo.Account.models.SignupRequest;
import event_server.demo.Account.models.User;
import event_server.demo.Account.models.UserResponse;


@RestController
@RequestMapping("api/account")
@CrossOrigin(origins = "${app.cors.allowed-origin:http://localhost:3000}")
public class AccountsController {
    private final UserRepository accounts;
    private final TokenService tokens;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public AccountsController(UserRepository accounts, TokenService tokens) {
        this.accounts = accounts;
        this.tokens = tokens;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createAccount(@RequestBody SignupRequest request) {
        if (request == null || request.username() == null || request.username().isBlank()
                || request.email() == null || !request.email().contains("@") || !request.email().contains(".")
                || request.password() == null || request.password().length() < 12) {
            return ResponseEntity.badRequest().body("Provide a username, valid email, and password of at least 12 characters.");
        }

        String email = normalizeEmail(request.email());
        if (accounts.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with that email already exists.");
        }

        User user = new User(request.username().trim(), email, passwordEncoder.encode(request.password()));
        user.setId(UUID.randomUUID().toString());
        User savedUser = accounts.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(savedUser, tokens.createToken(savedUser.getId())));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request == null || request.email() == null || request.password() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }

        return accounts.findByEmail(normalizeEmail(request.email()))
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(UserResponse.from(user, tokens.createToken(user.getId()))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password."));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

}
