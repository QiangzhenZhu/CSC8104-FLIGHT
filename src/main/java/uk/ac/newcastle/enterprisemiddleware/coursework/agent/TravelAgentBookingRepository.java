package uk.ac.newcastle.enterprisemiddleware.coursework.agent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description 旅游社预定信息持久类
 */

@Singleton
public class TravelAgentBookingRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    public List<TravelAgentBooking> findAllBookings() {
        TypedQuery<TravelAgentBooking> query = em.createNamedQuery(TravelAgentBooking.FIND_ALL, TravelAgentBooking.class);
        return query.getResultList();
    }

    public TravelAgentBooking findById(Long id) {
        return em.find(TravelAgentBooking.class, id);
    }

    public List<TravelAgentBooking> findAllByCustomerId(Long customerId) {
        TypedQuery<TravelAgentBooking> query = em
                .createNamedQuery(TravelAgentBooking.FIND_BY_CUSTOMER_ID, TravelAgentBooking.class)
                .setParameter("customerId", customerId);
        return query.getResultList();
    }

    public TravelAgentBooking createTravelAgentBooking(TravelAgentBooking booking) throws Exception {
        em.persist(booking);
        return booking;
    }

    TravelAgentBooking deleteTravelAgentBooking(TravelAgentBooking booking) throws Exception {
        em.remove(booking);
        return booking;
    }

}
