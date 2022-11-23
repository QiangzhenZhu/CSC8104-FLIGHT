package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 出租车预定类
 */

public class TaxiBooking implements Serializable {

    private Long id;

    private Long customerId;

    private Date bookDate;

    private Long taxiId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookingDate) {
        this.bookDate = bookDate;
    }

    public Long getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(Long taxiId) {
        this.taxiId = taxiId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TaxiBooking{");
        sb.append("id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", bookDate=").append(bookDate);
        sb.append(", taxiId=").append(taxiId);
        sb.append('}');
        return sb.toString();
    }
}
