package anatolii.k.hoa.community.person.internal.domain;

import anatolii.k.hoa.common.domain.Email;
import anatolii.k.hoa.common.domain.PhoneNumber;
import anatolii.k.hoa.common.domain.SSN;

public class Person {
    private Long id;
    private String firstName;
    private String lastName;
    private PhoneNumber phoneNumber;
    private Email email;
    private SSN ssn;

    public Person(Long id, String firstName, String lastName, PhoneNumber phoneNumber, Email email, SSN ssn) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ssn = ssn;
    }


    public Person() { }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSsn(SSN ssn) {
        this.ssn = ssn;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public SSN getSsn() {
        return ssn;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", ssn=" + ssn +
                '}';
    }
}
