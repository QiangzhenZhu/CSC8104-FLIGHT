package uk.ac.newcastle.enterprisemiddleware.coursework.flight;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class FlightService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    FlightRepository flightRepository;

    public List<Flight> findAllFlights() {
        return flightRepository.findAllFlights();
    }

    public Flight findFlightById(long flight){
        return flightRepository.findById(flight);
    }

    public Flight findByFlightNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber);
    }

    public Flight delete(Flight flight) {
        log.info("delete() - Deleting " + flight.toString());

        Flight deletedFlight = null;

        if (flight.getId() != null) {
            deletedFlight = flightRepository.delete(flight);
        } else {
            log.info("delete() - No ID was found so can't Delete.");
        }

        return deletedFlight;
    }

     public Flight create(Flight flight) {
        return flightRepository.create(flight);

    }
}
