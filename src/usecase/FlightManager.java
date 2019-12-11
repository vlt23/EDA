package usecase;

import material.maps.Entry;
import material.maps.HashTableMapDH;
import material.maps.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vlt23
 */
public class FlightManager {

    private Map<Flight, Flight> flightsMap = new HashTableMapDH<>();
    private Map<String, Passenger> passengersMap = new HashTableMapDH<>();
    private Map<Flight, List<Passenger>> flightWithAllPassengersMap = new HashTableMapDH<>();
    private Map<Passenger, List<Flight>> passengerWithAllFlightsMap = new HashTableMapDH<>();

    public Flight addFlight(String company, int flightCode, int year, int month, int day) {
        Flight flight = new Flight(company, flightCode, year, month, day);
        if (flightsMap.get(flight) != null) {
            throw new RuntimeException("The flight already exists.");
        }
        flightsMap.put(flight, flight);

        flightWithAllPassengersMap.put(flight, new ArrayList<>());

        return flight.copyFlight();
    }

    public Flight getFlight(String company, int flightCode, int year, int month, int day) {
        Flight flight = flightsMap.get(new Flight(company, flightCode, year, month, day));
        if (flight == null) {
            throw new RuntimeException("Flight not found.");
        }
        return flight.copyFlight();
    }

    public void updateFlight(String company, int flightCode, int year, int month, int day, Flight updatedFlightInfo) {
        Flight currentFlight = flightsMap.get(new Flight(company, flightCode, year, month, day));
        if (currentFlight == null) {
            throw new RuntimeException("The flight doesn't exists and can't be updated.");
        }

        Flight oldFlight = currentFlight.copyFlight();
        if (!currentFlight.equals(updatedFlightInfo)) {
            if (flightsMap.get(updatedFlightInfo) != null) {
                throw new RuntimeException("The new flight identifiers are already in use.");
            }
            flightsMap.remove(currentFlight);
            currentFlight = new Flight(updatedFlightInfo.getCompany(), updatedFlightInfo.getFlightCode(),
                    updatedFlightInfo.getYear(), updatedFlightInfo.getMonth(), updatedFlightInfo.getDay());
        }
        currentFlight.setTime(updatedFlightInfo.getHours(), updatedFlightInfo.getMinutes());
        currentFlight.setCapacity(updatedFlightInfo.getCapacity());
        currentFlight.setOrigin(updatedFlightInfo.getOrigin());
        currentFlight.setDestination(updatedFlightInfo.getDestination());
        currentFlight.setDelay(updatedFlightInfo.getDelay());
        Iterable<String> properties = updatedFlightInfo.getAllAttributes();
        for (String property : properties) {
            currentFlight.setProperty(property, updatedFlightInfo.getProperty(property));
        }
        flightsMap.put(currentFlight, currentFlight);

        List<Passenger> passengers = flightWithAllPassengersMap.remove(oldFlight);
        flightWithAllPassengersMap.put(currentFlight, passengers);

        Iterable<Entry<Passenger, List<Flight>>> entries = passengerWithAllFlightsMap.entries();
        for (Entry<Passenger, List<Flight>> passengerListFlight : entries) {
            boolean isRemoved = passengerListFlight.getValue().remove(oldFlight);
            if (isRemoved) {
                passengerWithAllFlightsMap.remove(passengerListFlight.getKey());
                passengerListFlight.getValue().add(currentFlight);
                passengerWithAllFlightsMap.put(passengerListFlight.getKey(), passengerListFlight.getValue());
            }
        }
    }

    public void addPassenger(String dni, String name, String surname, Flight flight) {
        Flight currentFlight = flightsMap.get(flight);
        if (currentFlight == null) {
            throw new RuntimeException("The flight doesn't exits.");
        }

        if (flightWithAllPassengersMap.get(flight) != null) {
            if (currentFlight.getCapacity() == flightWithAllPassengersMap.get(flight).size()) {
                throw new RuntimeException("This flight doesn't have capacity for more passengers.");
            }
        }

        Passenger passenger = new Passenger(dni, name, surname);
        Passenger currentPassenger = passengersMap.get(dni);
        if (currentPassenger != null) {  // old passenger
            if (!currentPassenger.equals(passenger)) {
                passengersMap.remove(dni);
                passengersMap.put(dni, passenger);
            }
            if (flightWithAllPassengersMap.get(flight) != null) {
                flightWithAllPassengersMap.get(flight).remove(currentPassenger);
            }
            List<Flight> flights = passengerWithAllFlightsMap.remove(currentPassenger);
            passengerWithAllFlightsMap.put(passenger, flights);
        } else {  // new passenger
            passengersMap.put(dni, passenger);
            passengerWithAllFlightsMap.put(passenger, new ArrayList<>());
        }

        flightWithAllPassengersMap.get(flight).add(passenger);

        passengerWithAllFlightsMap.get(passenger).add(flight);
    }

    public Iterable<Passenger> getPassengers(String company, int flightCode, int year, int month, int day) {
        Flight flight = flightsMap.get(new Flight(company, flightCode, year, month, day));
        if (flight == null) {
            throw new RuntimeException("The flight doesn't exists.");
        }
        return flightWithAllPassengersMap.get(flight) != null
                ? flightWithAllPassengersMap.get(flight) : new ArrayList<>();
    }

    public Iterable<Flight> flightsByDate(int year, int month, int day) {
        Iterable<Flight> flights = flightsMap.values();
        List<Flight> toReturnFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.compareDate(year, month, day)) {
                toReturnFlights.add(flight.copyFlight());
            }
        }
        return toReturnFlights;
    }

    public Iterable<Flight> getFlightsByPassenger(Passenger passenger) {
        Passenger currentPassenger = passengersMap.get(passenger.getDNI());
        if (currentPassenger != null) {
            List<Flight> flights = new ArrayList<>(passengerWithAllFlightsMap.get(currentPassenger).size());
            for (Flight flight : passengerWithAllFlightsMap.get(currentPassenger)) {
                flights.add(flight.copyFlight());
            }
            return flights;
        }
        // The DNI is invalid or the passenger doesn't exists in the map
        return new ArrayList<>();
    }

    public Iterable<Flight> getFlightsByDestination(String destination, int year, int month, int day) {
        List<Flight> flights = new ArrayList<>();
        boolean existDest = false;
        for (Flight flight : flightsMap.values()) {
            if (flight.getDestination().equals(destination)) {
                existDest = true;
                if (flight.compareDate(year, month, day)) {
                    flights.add(flight.copyFlight());
                }
            }
        }
        if (!existDest) {
            throw new RuntimeException("The destination doesn't exists.");
        }
        return flights;
    }

    /*private int calculateKey(String company, int flightCode, int year, int month, int day) {
        return company.hashCode() + Integer.hashCode(flightCode)
                + Integer.hashCode(year) + Integer.hashCode(month) + Integer.hashCode(day);
    }*/

}
