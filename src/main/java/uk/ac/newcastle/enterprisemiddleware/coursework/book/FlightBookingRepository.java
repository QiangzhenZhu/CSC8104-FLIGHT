package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description 飞机预订持久类
 */

@Singleton
public class FlightBookingRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    FlightBooking createFlightBooking(FlightBooking booking) {
        em.persist(booking);
        return booking;
    }

    FlightBooking deleteFlightBooking(FlightBooking booking) {
        if (booking.getFlightId() != null && booking.getFlightId() > 0) {
            em.remove(booking);
        } else {
            log.info("FlightBookingRepository.deleteFlightBooking() - No ID was found so can't Delete.");
        }
        return booking;
    }

    List<FlightBooking> findByCustomerId(Long customerId) {
        TypedQuery<FlightBooking> namedQuery = em
                .createNamedQuery(FlightBooking.FIND_BY_CUSTOMER_ID, FlightBooking.class)
                .setParameter("customerId", customerId);
        return namedQuery.getResultList();
    }

    List<FlightBooking> findAll() {
        TypedQuery<FlightBooking> query =
                em.createNamedQuery(FlightBooking.FIND_ALL, FlightBooking.class);
        return query.getResultList();
    }

    FlightBooking findById(Long id) {
        return em.find(FlightBooking.class, id);
    }

    FlightBooking findByFlightIdAndDate(Long flightId, Date date) {
        TypedQuery<FlightBooking> namedQuery = em.createNamedQuery(FlightBooking.FIND_BY_FLIGHT_ID_AND_DATE, FlightBooking.class)
                .setParameter("flightId", flightId)
                .setParameter("bookingDate", date)
                .setMaxResults(1);
        List<FlightBooking> resultList = namedQuery.getResultList();
        return resultList.size() == 0? null: resultList.get(0);
    }
}
