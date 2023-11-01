package requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import payloads.booking.Booking;

import static io.restassured.RestAssured.given;

public class BookingApi extends BaseApi {
    public static final String BOOKING_SERVICE_URL_ENV_VAR_NAME = "BOOKING_SERVICE_URL";

    static {
        RestAssured.defaultParser = Parser.JSON;
    }

    public BookingApi(String url) { super(url); }

    public Response postBooking(Booking booking) {
        return given()
            .contentType(ContentType.JSON)
            .body(booking)
            .post(url() + "booking/");
    }

    public Response getSummaries(int roomid) {
        return given()
            .queryParam("roomid", roomid)
            .get(url() + "booking/summary");
    }
}
