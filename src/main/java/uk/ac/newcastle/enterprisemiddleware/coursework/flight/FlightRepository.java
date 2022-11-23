package uk.ac.newcastle.enterprisemiddleware.coursework.flight;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class FlightRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    List<Flight> findAllFlights() {
        TypedQuery<Flight> query = em.createNamedQuery(Flight.FIND_ALL, Flight.class);
        return query.getResultList();
    }

    Flight findById(Long id) {
        return em.find(Flight.class, id);
    }

    Flight findByFlightNumber(String flightNumber) {
        TypedQuery<Flight> query = em
                .createNamedQuery(Flight.FIND_BY_FLIGHT_NUMBER, Flight.class)
                .setParameter("flightNumber", flightNumber)
                .setMaxResults(1);
        List<Flight> resultList = query.getResultList();
        return resultList.size() == 0 ? null : resultList.get(0);
    }

    Flight create(Flight flight) {
        log.info("FlightRepository.create() - Creating " + flight.getFlightNumber());
        em.persist(flight);
        return flight;
    }

    Flight delete(Flight flight) {
        log.info("FlightRepository.delete() - Deleting " + flight.getFlightNumber());
        if (flight.getId() != null) {
            em.remove(flight);
        } else {
            log.info("FlightRepository.delete() - No ID was found so can't Delete.");
        }

        return flight;
    }
}
