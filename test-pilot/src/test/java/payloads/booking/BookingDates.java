package payloads.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingDates(
    @JsonProperty String checkin,
    @JsonProperty String checkout
) {}
