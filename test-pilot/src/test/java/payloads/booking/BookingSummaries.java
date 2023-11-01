package payloads.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BookingSummaries(@JsonProperty List<BookingSummary> bookings) {}
