package payloads.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record BookingDates(
    @JsonProperty String checkin,
    @JsonProperty String checkout
) {}
