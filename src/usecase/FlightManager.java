package usecase;

import material.maps.HashTableMapDH;

import java.util.ArrayList;
import java.util.List;

public class FlightManager {

    private HashTableMapDH<Flight, Flight> flightsMap;

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
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Passenger> getPassengers(String company, int flightCode, int year, int month, int day) {
        throw new RuntimeException("Not yet implemented.");
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
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Flight> getFlightsByDestination(String destination, int year, int month, int day) {
        throw new RuntimeException("Not yet implemented.");

    }

    /*private int calculateKey(String company, int flightCode, int year, int month, int day) {
        return company.hashCode() + Integer.hashCode(flightCode)
                + Integer.hashCode(year) + Integer.hashCode(month) + Integer.hashCode(day);
    }*/

}
