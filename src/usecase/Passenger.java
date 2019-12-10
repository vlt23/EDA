package usecase;

import java.util.Objects;

/**
 * @author vlt23
 */
public class Passenger {

    private String DNI;
    private String name;
    private String surname;

    public Passenger() {
        // default constructor
    }

    Passenger(String DNI, String name, String surname) {
        this.DNI = DNI;
        this.name = name;
        this.surname = surname;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String dni) {
        this.DNI = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return DNI.equals(passenger.DNI) && name.equals(passenger.name) && surname.equals(passenger.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DNI, name, surname);
    }

    @Override
    public String toString() {
        return DNI + " " + name + " " + surname;
    }

}
