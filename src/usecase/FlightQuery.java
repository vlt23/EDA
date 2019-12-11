package usecase;

import material.Position;
import material.tree.binarysearchtree.BinarySearchTree;
import material.tree.binarysearchtree.LinkedBinarySearchTree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author vlt23
 */
public class FlightQuery {
    /*
     * En cada caso solo se ordena por las claves especificadas.
     *
     * Al buscar por (date1, date1) se deberia devolver Vuelo2 pero al ser la misma fecha se compara
     * por codigo de vuelo y se excluye.
     *
     * Vuelo( date1, code=550) > Vuelo2 (date1, code=0)
     */

    private BinarySearchTree<Flight> lbstByDate = new LinkedBinarySearchTree<>((flight, t1) -> {
        if (flight.getDate().isBefore(t1.getDate())) {
            return -1;
        } else if (flight.compareDate(t1.getYear(), t1.getMonth(), t1.getDay())) {
            return 0;
        } else {
            return 1;
        }
    });

    private BinarySearchTree<Flight> lbstByDestinations = new LinkedBinarySearchTree<>(
            Comparator.comparing(Flight::getDestination)
    );

    private BinarySearchTree<Flight> lbsByCompanyAndFlightCode = new LinkedBinarySearchTree<>(
            Comparator.comparing(flight -> (flight.getCompany() + flight.getFlightCode()))
    );

    public void addFlight(Flight flight) {
        lbstByDate.insert(flight);
        lbstByDestinations.insert(flight);
    }

    public Iterable<Flight> searchByDates(int start_year, int start_month, int start_day,
                                          int end_year, int end_month, int end_day) throws RuntimeException {
        Flight start = new Flight();
        start.setDate(start_year, start_month, start_day);
        Flight end = new Flight();
        end.setDate(end_year, end_month, end_day);
        Iterable<Position<Flight>> positions = lbstByDate.findRange(start, end);
        Iterator<Position<Flight>> it = positions.iterator();
        List<Flight> flights = new ArrayList<>();
        while (it.hasNext()) {
            flights.add(it.next().getElement());
        }
        return flights;
    }

    public Iterable<Flight> searchByDestinations(String start_destination, String end_destination)
            throws RuntimeException {
        Flight start = new Flight();
        start.setDestination(start_destination);
        Flight end = new Flight();
        end.setDestination(end_destination);
        Iterable<Position<Flight>> positions = lbstByDestinations.findRange(start, end);
        Iterator<Position<Flight>> it = positions.iterator();
        List<Flight> flights = new ArrayList<>();
        while (it.hasNext()) {
            flights.add(it.next().getElement());
        }
        return flights;
    }

    public Iterable<Flight> searchByCompanyAndFLightCode(String start_company, int start_flightCode,
                                                         String end_company, int end_flightCode) {
        Flight start = new Flight();
        start.setCompany(start_company);
        start.setFlightCode(start_flightCode);
        Flight end = new Flight();
        end.setCompany(end_company);
        end.setFlightCode(end_flightCode);
        Iterable<Position<Flight>> positions = lbsByCompanyAndFlightCode.findRange(start, end);
        Iterator<Position<Flight>> it = positions.iterator();
        List<Flight> flights = new ArrayList<>();
        while (it.hasNext()) {
            flights.add(it.next().getElement());
        }
        return flights;
    }

}
