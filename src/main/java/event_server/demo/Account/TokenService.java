package event_server.demo.Account;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();
    private static final long TOKEN_LIFETIME_SECONDS = 60 * 60 * 24;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final byte[] secret;

    public TokenService(@Value("${app.auth.token-secret:}") String configuredSecret) {
        if (configuredSecret == null || configuredSecret.isBlank()) {
            this.secret = new byte[32];
            RANDOM.nextBytes(this.secret);
        } else {
            byte[] configuredBytes = configuredSecret.getBytes(StandardCharsets.UTF_8);
            if (configuredBytes.length < 32) {
                throw new IllegalStateException("Configured app.auth.token-secret must be at least 32 bytes.");
            }
            this.secret = configuredBytes;
        }
    }

    public String createToken(String userId) {
        long expiresAt = Instant.now().getEpochSecond() + TOKEN_LIFETIME_SECONDS;
        String payload = userId + "." + expiresAt + "." + UUID.randomUUID();
        String encodedPayload = ENCODER.encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return encodedPayload + "." + ENCODER.encodeToString(sign(encodedPayload));
    }

    public String validateAndGetUserId(String token) {
        if (token == null) return null;
        try {
            String[] pieces = token.split("\\.", -1);
            if (pieces.length != 2) return null;

            byte[] suppliedSignature = DECODER.decode(pieces[1]);
            if (!java.security.MessageDigest.isEqual(sign(pieces[0]), suppliedSignature)) return null;

            String[] claims = new String(DECODER.decode(pieces[0]), StandardCharsets.UTF_8).split("\\.", -1);
            if (claims.length != 3 || claims[0].isBlank()) return null;
            if (Long.parseLong(claims[1]) <= Instant.now().getEpochSecond()) return null;
            UUID.fromString(claims[2]);
            return claims[0];
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private byte[] sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (java.security.GeneralSecurityException ex) {
            throw new IllegalStateException("Could not sign authentication token.", ex);
        }
    }
}
