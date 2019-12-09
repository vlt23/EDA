package usecase;

import material.maps.HashTableMapDH;
import material.maps.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vlt23
 */
public class FlightManager {

    private Map<Flight, Flight> flightsMap = new HashTableMapDH<>();
    private Map<Passenger, Passenger> passengersMap = new HashTableMapDH<>();
    private Map<Flight, List<Passenger>> flightWithAllPassengersMap = new HashTableMapDH<>();
    private Map<Passenger, List<Flight>> passengerWithAllFlightsMap = new HashTableMapDH<>();

    public Flight addFlight(String company, int flightCode, int year, int month, int day) {
        Flight flight = new Flight(company, flightCode, year, month, day);
        flightsMap.put(flight, flight);
        return flight;
    }

    public Flight getFlight(String company, int flightCode, int year, int month, int day) {
        return flightsMap.get(new Flight(company, flightCode, year, month, day));
    }

    public void updateFlight(String company, int flightCode, int year, int month, int day, Flight updatedFlightInfo) {
        Flight currentFlight = flightsMap.get(new Flight(company, flightCode, year, month, day));
        if (!currentFlight.equals(updatedFlightInfo)) {
            flightsMap.remove(currentFlight);
            currentFlight = new Flight(company, flightCode, year, month, day);
        }
        currentFlight.setCapacity(updatedFlightInfo.getCapacity());
        currentFlight.setOrigin(updatedFlightInfo.getOrigin());
        currentFlight.setDestination(updatedFlightInfo.getDestination());
        currentFlight.setDelay(updatedFlightInfo.getDelay());
        Iterable<String> properties = updatedFlightInfo.getAllAttributes();
        for (String property : properties) {
            currentFlight.setProperty(property, updatedFlightInfo.getProperty(property));
        }
    }

    public void addPassenger(String dni, String name, String surname, Flight flight) {
        Passenger passenger = new Passenger(dni, name, surname);
        Passenger currentPassenger = passengersMap.get(passenger);
        if (currentPassenger != null) {  // old passenger
            if (!currentPassenger.equals(passenger)) {
                passengersMap.remove(currentPassenger);
                passengersMap.put(passenger, passenger);
            }
        } else {  // new passenger
            passengersMap.put(passenger, passenger);
        }
        flightWithAllPassengersMap.get(flight).add(passenger);
        List<Flight> allFlights = passengerWithAllFlightsMap.get(passenger);
        allFlights.add(flight);
        passengerWithAllFlightsMap.put(passenger, allFlights);
    }

    public Iterable<Passenger> getPassengers(String company, int flightCode, int year, int month, int day) {
        Flight flight = new Flight(company, flightCode, year, month, day);
        return flightWithAllPassengersMap.get(flight);
    }

    public Iterable<Flight> flightsByDate(int year, int month, int day) {
        Iterable<Flight> flights = flightsMap.values();
        List<Flight> toReturnFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.compareDate(year, month, day)) {
                toReturnFlights.add(flight);
            }
        }
        return toReturnFlights;
    }

    public Iterable<Flight> getFlightsByPassenger(Passenger passenger) {
        return passengerWithAllFlightsMap.get(passenger);
    }

    public Iterable<Flight> getFlightsByDestination(String destination, int year, int month, int day) {
        List<Flight> flights = new ArrayList<>();
        for (Flight flight : flightsMap.values()) {
            if (flight.getDestination().equals(destination) && flight.compareDate(year, month, day)) {
                flights.add(flight);
            }
        }
        return flights;
    }

    /*private int calculateKey(String company, int flightCode, int year, int month, int day) {
        return company.hashCode() + Integer.hashCode(flightCode)
                + Integer.hashCode(year) + Integer.hashCode(month) + Integer.hashCode(day);
    }*/

}
