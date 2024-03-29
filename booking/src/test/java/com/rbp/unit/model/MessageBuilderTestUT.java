package com.rbp.unit.model;

import com.rbp.model.db.Booking;
import com.rbp.model.db.Message;
import com.rbp.service.MessageBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MessageBuilderTestUT {

    @Test
    public void messageBuiltFromBookingTest(){
        LocalDate checkindate = LocalDate.of(1990, Month.FEBRUARY, 1);
        LocalDate checkoutdate = LocalDate.of(1990, Month.FEBRUARY, 2);

        Booking booking = new Booking.BookingBuilder()
                .setFirstname("Mark")
                .setLastname("Winteringham")
                .setDepositpaid(true)
                .setCheckin(checkindate)
                .setCheckout(checkoutdate)
                .setEmail("mark@mwtestconsultancy.co.uk")
                .setPhone("01392123928")
                .build();

        MessageBuilder messageBuilder = new MessageBuilder();
        Message message = messageBuilder.build(booking);

        assertThat(message.toString(), is("Message{, name='Mark Winteringham', email='mark@mwtestconsultancy.co.uk', phone='01392123928', subject='You have a new booking!', description='You have a new booking from Mark Winteringham. They have booked a room for the following dates: 1990-02-01 to 1990-02-02'}"));
    }

}
