package anatolii.k.hoa.community.person.infrastructure.dto;


import anatolii.k.hoa.common.domain.Email;
import anatolii.k.hoa.common.domain.PhoneNumber;
import anatolii.k.hoa.common.domain.SSN;
import anatolii.k.hoa.community.person.domain.Person;
import jakarta.persistence.*;

@Entity
@Table(name="person")
public class PersonDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="first_name")
    private String firstName;

    @Column(name ="last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;
    private String ssn;

    public PersonDTO(Long id, String firstName, String lastName, String phoneNumber, String email, String ssn) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ssn = ssn;
    }

    public PersonDTO() {}

    public static PersonDTO fromDomain(Person domainObj){
        return new PersonDTO(domainObj.getId(),
                domainObj.getFirstName(),
                domainObj.getLastName(),
                domainObj.getPhoneNumber().toString(),
                domainObj.getEmail().toString(),
                domainObj.getSsn().toString());
    }

    public Person toDomain(){
        return new Person( id,
                firstName,
                lastName,
                PhoneNumber.fromString(phoneNumber),
                Email.fromString(email),
                SSN.fromString(ssn));
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSsn() {
        return ssn;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
