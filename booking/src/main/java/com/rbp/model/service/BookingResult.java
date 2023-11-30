package com.rbp.model.service;

import com.rbp.model.db.Booking;
import com.rbp.model.db.Bookings;
import com.rbp.model.db.CreatedBooking;
import org.springframework.http.HttpStatus;

public class BookingResult {

    private Booking booking;

    private CreatedBooking createdBooking;

    private Bookings bookings;

    private HttpStatus result;

    public BookingResult(Booking booking, HttpStatus result) {
        this.booking = booking;
        this.result = result;
    }

    public BookingResult(CreatedBooking createdBooking, HttpStatus result) {
        this.createdBooking = createdBooking;
        this.result = result;
    }

    public BookingResult(Bookings bookings, HttpStatus result){
        this.bookings = bookings;
        this.result= result;
    }

    public BookingResult(HttpStatus result) {
        this.result = result;
    }

    public HttpStatus getStatus() {
        return result;
    }

    public Booking getBooking() {
        return booking;
    }

    public CreatedBooking getCreatedBooking() {
        return createdBooking;
    }

    public Bookings getBookings() {
        return bookings;
    }
}
