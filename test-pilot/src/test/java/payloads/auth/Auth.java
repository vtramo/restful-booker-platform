package payloads.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Auth(
   @JsonProperty String username,
   @JsonProperty String password
) {}
