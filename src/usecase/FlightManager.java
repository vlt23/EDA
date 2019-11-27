package usecase;

import material.maps.HashTableMapDH;

public class FlightManager {

    private HashTableMapDH<Integer, Flight> flightsMap;

    public Flight addFlight(String company, int flightCode, int year, int month, int day) {
        Flight flight = new Flight();
        flight.setCompany(company);
        flight.setFlightCode(flightCode);
        flight.setDate(year, month, day);
        flightsMap.put(calculateKey(company, flightCode, year, month, day), flight);
        return flight;
    }

    public Flight getFlight(String company, int flightCode, int year, int month, int day) {
        return flightsMap.get(calculateKey(company, flightCode, year, month, day));
    }

    public void updateFlight(String company, int flightCode, int year, int month, int day, Flight updatedFlightInfo) {
        Flight flight = flightsMap.get(calculateKey(company, flightCode, year, month, day));
        flight.setCapacity(updatedFlightInfo.getCapacity());
        flight.setOrigin(updatedFlightInfo.getOrigin());
        flight.setDestination(updatedFlightInfo.getDestination());
        flight.setDelay(updatedFlightInfo.getDelay());
        Iterable<String> properties = updatedFlightInfo.getAllAttributes();
        for (String property : properties) {
            flight.setProperty(property, updatedFlightInfo.getProperty(property));
        }
    }

    public void addPassenger(String dni, String name, String surname, Flight flight) {
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Passenger> getPassengers(String company, int flightCode, int year, int month, int day) {
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Flight> flightsByDate(int year, int month, int day) {
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Flight> getFlightsByPassenger(Passenger passenger) {
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Flight> getFlightsByDestination(String destination, int year, int month, int day) {
        throw new RuntimeException("Not yet implemented.");

    }

    private int calculateKey(String company, int flightCode, int year, int month, int day) {
        return company.hashCode() + Integer.hashCode(flightCode)
                + Integer.hashCode(year) + Integer.hashCode(month) + Integer.hashCode(day);
    }

}
