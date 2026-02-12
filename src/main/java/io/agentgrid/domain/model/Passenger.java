package io.agentgrid.domain.model;



import io.agentgrid.domain.valueObject.Email;
import io.agentgrid.domain.valueObject.PassportNumber;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static io.agentgrid.domain.util.DomainValidator.*;

public final class Passenger {

    private final UUID id;
    private final PassportNumber passportNumber; // stable
    private Email email;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber; // optionnel

    private Passenger(UUID id,
                      String firstName,
                      String lastName,
                      Email email,
                      PassportNumber passportNumber,
                      LocalDate dateOfBirth,
                      String phoneNumber) {

        this.id = nn(id, "Passenger id is required");
        setName(firstName, lastName);
        setEmail(email);
        this.passportNumber = nn(passportNumber, "Passport number is required");
        setDateOfBirth(dateOfBirth);
        setPhoneNumber(phoneNumber);
    }

    public static Passenger create(String firstName,
                                   String lastName,
                                   Email email,
                                   PassportNumber passportNumber,
                                   LocalDate dateOfBirth,
                                   String phoneNumber) {

        requireAdult(dateOfBirth, 16); // règle “raisonnable”
        return new Passenger(UUID.randomUUID(), firstName, lastName, email, passportNumber, dateOfBirth, phoneNumber);
    }

    public void setName(String firstName, String lastName) {
        requireNotBlank(firstName, "First name is required");
        requireNotBlank(lastName, "Last name is required");
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    public void setEmail(Email email) {
        this.email = nn(email, "Email is required");
    }

    public void setDateOfBirth(LocalDate dob) {
        nn(dob, "Date of birth is required");
        requireAdult(dob, 16);
        this.dateOfBirth = dob;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = (phoneNumber == null || phoneNumber.isBlank()) ? null : phoneNumber.trim();
    }

    public UUID id() { return id; }
    public String firstName() { return firstName; }
    public String lastName() { return lastName; }
    public Email email() { return email; }
    public PassportNumber passportNumber() { return passportNumber; }
    public LocalDate dateOfBirth() { return dateOfBirth; }
    public String phoneNumber() { return phoneNumber; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passenger other)) return false;
        return id.equals(other.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}

