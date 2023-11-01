package payloads.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingSummary(@JsonProperty BookingDates bookingDates) {}
