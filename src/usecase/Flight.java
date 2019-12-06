package usecase;

import material.maps.HashTableMapDH;

import java.util.Calendar;
import java.util.Objects;

/**
 * @author vlt23
 */
public class Flight {

    // Flight's identification
    private String company;
    private int flightCode;
    private Calendar flightDate;

    private int capacity;

    private String origin;
    private String destination;

    // in minutes
    private int delay;

    private HashTableMapDH<String, String> properties;

    public Flight() {
        properties = new HashTableMapDH<>();
    }

    Flight(String company, int flightCode, int year, int month, int day) {
        this.company = company;
        this.flightCode = flightCode;
        this.setDate(year, month, day);
        properties = new HashTableMapDH<>();
    }

    public void setTime(int hours, int minutes) {
        flightDate.set(flightDate.get(Calendar.YEAR), flightDate.get(Calendar.MONTH),
                flightDate.get(Calendar.DAY_OF_MONTH), hours, minutes);
    }

    public int getHours() {
        return flightDate.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes() {
        return flightDate.get(Calendar.MINUTE);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(int flightCode) {
        this.flightCode = flightCode;
    }

    public void setDate(int year, int month, int day) {
        flightDate = Calendar.getInstance();
        flightDate.set(year, month, day);
    }

    public int getYear() {
        return flightDate.get(Calendar.YEAR);
    }

    public int getMonth() {
        return flightDate.get(Calendar.MONTH);
    }

    public int getDay() {
        return flightDate.get(Calendar.DAY_OF_MONTH);
    }

    boolean compareDate(int year, int month, int day) {
        return flightDate.get(Calendar.YEAR) == year && flightDate.get(Calendar.MONTH) == month
                && flightDate.get(Calendar.DAY_OF_MONTH) == day;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setProperty(String attribute, String value) {
        properties.put(attribute, value);
    }

    public String getProperty(String attribute) {
        return properties.get(attribute);
    }

    public Iterable<String> getAllAttributes() {
        return properties.keys();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return flightCode == flight.flightCode &&
                company.equals(flight.company) &&
                flightDate.equals(flight.flightDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, flightCode, flightDate);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "company='" + company + '\'' +
                ", flightCode=" + flightCode +
                ", flightDate=" + flightDate +
                ", capacity=" + capacity +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", delay=" + delay +
                ", properties=" + properties +
                '}';
    }

}
