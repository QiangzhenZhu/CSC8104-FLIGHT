package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 酒店预定类
 */

public class HotelBooking implements Serializable {
    private static final long serialVersionUID = 6L;
    private Long bookingId;
    private Long customerId;
    private Long hotelId;
    private Date bookingDate;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HotelBooking{");
        sb.append("bookingId=").append(bookingId);
        sb.append(", customerId=").append(customerId);
        sb.append(", hotelId=").append(hotelId);
        sb.append(", bookingDate=").append(bookingDate);
        sb.append('}');
        return sb.toString();
    }
}