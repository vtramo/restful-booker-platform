package com.rbp.requests;

import com.rbp.model.booking.BookingSummaries;
import com.rbp.model.booking.Bookings;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class BookingRequests {

    private String host;

    public BookingRequests() {
        if(System.getenv("BOOKING_SERVICE_URL") == null){
            host = "http://localhost:3000/booking";
        } else {
            host = "http://" + System.getenv("BOOKING_SERVICE_URL") + ":3000/booking";
        }
    }

    public Bookings getBookings(int roomId, String token){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie", "token=" + token);

        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);

        return restTemplate.exchange(host + "/?roomid=" + roomId, HttpMethod.GET, httpEntity, Bookings.class).getBody();
    }

    public BookingSummaries getBookingSummaries(int roomId){
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(host + "/summary?roomid=" + roomId, BookingSummaries.class);
    }

}
