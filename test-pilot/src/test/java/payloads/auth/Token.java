package payloads.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record Token(
    @JsonProperty String token,
    @JsonProperty ZonedDateTime expiry
) {}
