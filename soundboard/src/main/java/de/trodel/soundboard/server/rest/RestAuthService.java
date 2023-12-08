package de.trodel.soundboard.server.rest;

import static java.time.Instant.now;
import static java.util.Optional.ofNullable;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class RestAuthService {
    public record TokenData(String token, Instant creationTime, Instant expireTime, String source) {};

    private final ObservableMap<String, TokenData> tokens        = FXCollections.observableHashMap();
    private final Duration                         tokenLiveTime = Duration.ofSeconds(60);
    private final SecureRandom                     random        = new SecureRandom();
    private final byte[]                           tempKeyHolder = new byte[64];
    private final ObservableStringValue            secret;

    public RestAuthService(ObservableStringValue secret) {
        this.secret = secret;
        secret.addListener((observable, oldV, newV) -> {
            clearAllTokens();
        });
    }

    public void clearAllTokens() {
        synchronized (tokens) {
            tokens.clear();
        }
    }

    public synchronized TokenData createToken(String secret, String remoteAddress) throws Exception {
        clearExpiredTokens();

        if (tokens.size() >= 100) {
            throw new Exception("Max number of active tokens is 100.");
        }

        if (!Objects.equals(secret, this.secret.get())) {
            throw new Exception("Invalid secret");
        }

        random.nextBytes(tempKeyHolder);
        Instant creation = Instant.ofEpochSecond(Instant.now().getEpochSecond());
        Instant expires = creation.plus(tokenLiveTime);

        TokenData token = new TokenData(
            Base64.getEncoder().encodeToString(tempKeyHolder),
            creation,
            expires,
            remoteAddress
        );

        synchronized (tokens) {
            tokens.put(token.token(), token);
        }
        return token;
    }

    public boolean removeToken(String token) {
        synchronized (tokens) {
            return tokens.remove(token) != null;
        }
    }

    public boolean isTokenValid(String token) {
        synchronized (tokens) {
            return ofNullable(tokens.get(token)).map(t -> !t.expireTime().isBefore(now())).orElse(false);
        }
    }

    private void clearExpiredTokens() {
        synchronized (tokens) {
            var iterator = tokens.entrySet().iterator();
            Instant now = Instant.now();

            while (iterator.hasNext()) {
                if (iterator.next().getValue().expireTime().isBefore(now)) {
                    iterator.remove();
                }
            }
        }
    }

    public ObservableMap<String, TokenData> getTokens() {
        return tokens;
    }
}
