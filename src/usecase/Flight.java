package usecase;

import java.util.Calendar;

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

    public Flight() {

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
        throw new RuntimeException("Not yet implemented.");
    }

    public String getProperty(String attribute) {
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<String> getAllAttributes() {
        throw new RuntimeException("Not yet implemented.");
    }

}
