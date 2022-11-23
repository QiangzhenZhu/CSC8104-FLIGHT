package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description 飞机预订服务类
 */

@Singleton
public class FlightBookingService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    FlightBookingRepository flightBookingRepository;

    public FlightBooking create(FlightBooking booking) throws Exception {
        log.info("BookingService.create() - Creating " + booking.toString());

        // Write the booking to the database.
        return flightBookingRepository.createFlightBooking(booking);
    }


    public List<FlightBooking> findByCustomerId(Long customerId) {
        return flightBookingRepository.findByCustomerId(customerId);
    }

    public List<FlightBooking> findAll() {
        return flightBookingRepository.findAll();
    }

    public FlightBooking delete(FlightBooking booking) throws Exception {
        log.info("delete() - Deleting " + booking.toString());

        FlightBooking deletedBooking = null;

        if (booking.getId() != 0) {
            deletedBooking = flightBookingRepository.deleteFlightBooking(booking);
        } else {
            log.info("delete() - No Booking was found so can't Delete.");
        }

        return deletedBooking;
    }

    public FlightBooking findById(Long id) {
        return flightBookingRepository.findById(id);
    }

    public FlightBooking findByFlightIdAndDate(Long flightId, Date date) {
        return flightBookingRepository.findByFlightIdAndDate(flightId, date);
    }


}
