package finalyear.officeme.model;

import java.io.Serializable;

/**
 * Created by College on 19/04/2016.
 */
public class BookingRequest implements Serializable {

    int bookingRequestID, bookingRequestDay, bookingRequestMonth, bookingRequestYear, bookingRequestHour, bookingRequestMinute, bookingRequestVisitorID, bookingRequestHostID, bookingRequestListingID;

    public BookingRequest(int bookingRequestDay, int bookingRequestMonth, int bookingRequestYear, int bookingRequestHour, int bookingRequestMinute, int bookingRequestVisitorID, int bookingRequestHostID, int bookingRequestListingID) {
        this.bookingRequestDay = bookingRequestDay;
        this.bookingRequestMonth = bookingRequestMonth;
        this.bookingRequestYear = bookingRequestYear;
        this.bookingRequestHour = bookingRequestHour;
        this.bookingRequestMinute = bookingRequestMinute;
        this.bookingRequestVisitorID = bookingRequestVisitorID;
        this.bookingRequestHostID = bookingRequestHostID;
        this.bookingRequestListingID = bookingRequestListingID;
    }

    public BookingRequest(int bookingRequestID, int bookingRequestDay, int bookingRequestMonth, int bookingRequestYear, int bookingRequestHour, int bookingRequestMinute, int bookingRequestVisitorID, int bookingRequestHostID, int bookingRequestListingID) {
        this.bookingRequestID = bookingRequestID;
        this.bookingRequestDay = bookingRequestDay;
        this.bookingRequestMonth = bookingRequestMonth;
        this.bookingRequestYear = bookingRequestYear;
        this.bookingRequestHour = bookingRequestHour;
        this.bookingRequestMinute = bookingRequestMinute;
        this.bookingRequestVisitorID = bookingRequestVisitorID;
        this.bookingRequestHostID = bookingRequestHostID;
        this.bookingRequestListingID = bookingRequestListingID;
    }

    public int getBookingRequestID() {
        return bookingRequestID;
    }

    public void setBookingRequestID(int bookingRequestID) {
        this.bookingRequestID = bookingRequestID;
    }

    public int getBookingRequestDay() {
        return bookingRequestDay;
    }

    public void setBookingRequestDay(int bookingRequestDay) {
        this.bookingRequestDay = bookingRequestDay;
    }

    public int getBookingRequestMonth() {
        return bookingRequestMonth;
    }

    public void setBookingRequestMonth(int bookingRequestMonth) {
        this.bookingRequestMonth = bookingRequestMonth;
    }

    public int getBookingRequestYear() {
        return bookingRequestYear;
    }

    public void setBookingRequestYear(int bookingRequestYear) {
        this.bookingRequestYear = bookingRequestYear;
    }

    public int getBookingRequestHour() {
        return bookingRequestHour;
    }

    public void setBookingRequestHour(int bookingRequestHour) {
        this.bookingRequestHour = bookingRequestHour;
    }

    public int getBookingRequestMinute() {
        return bookingRequestMinute;
    }

    public void setBookingRequestMinute(int bookingRequestMinute) {
        this.bookingRequestMinute = bookingRequestMinute;
    }

    public int getBookingRequestVisitorID() {
        return bookingRequestVisitorID;
    }

    public void setBookingRequestVisitorID(int bookingRequestVisitorID) {
        this.bookingRequestVisitorID = bookingRequestVisitorID;
    }

    public int getBookingRequestHostID() {
        return bookingRequestHostID;
    }

    public void setBookingRequestHostID(int bookingRequestHostID) {
        this.bookingRequestHostID = bookingRequestHostID;
    }

    public int getBookingRequestListingID() {
        return bookingRequestListingID;
    }

    public void setBookingRequestListingID(int bookingRequestListingID) {
        this.bookingRequestListingID = bookingRequestListingID;
    }
}
