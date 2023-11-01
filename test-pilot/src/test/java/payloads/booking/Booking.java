package payloads.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Booking(
    @JsonProperty int roomid,
    @JsonProperty String firstname,
    @JsonProperty String lastname,
    @JsonProperty boolean depositpaid,
    @JsonProperty BookingDates bookingdates
) {}
