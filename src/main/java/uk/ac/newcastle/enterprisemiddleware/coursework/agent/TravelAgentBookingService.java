package uk.ac.newcastle.enterprisemiddleware.coursework.agent;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author jos
 * @date 2022/11/21 22:09:24
 * @description 旅游预订服务类
 */

@Singleton
public class TravelAgentBookingService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    TravelAgentBookingRepository bookingRepository;

    public List<TravelAgentBooking> findAllBookings() {
        return bookingRepository.findAllBookings();
    }

    public TravelAgentBooking findById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<TravelAgentBooking> findAllByCustomerId(Long customerId) {
        return bookingRepository.findAllByCustomerId(customerId);
    }

    public TravelAgentBooking createTravelAgentBooking(TravelAgentBooking booking) throws Exception {
        return bookingRepository.createTravelAgentBooking(booking);
    }

    TravelAgentBooking deleteTravelAgentBooking(TravelAgentBooking booking) throws Exception {
        return bookingRepository.deleteTravelAgentBooking(booking);
    }

}
