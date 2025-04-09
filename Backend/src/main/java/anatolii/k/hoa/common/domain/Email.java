package anatolii.k.hoa.common.domain;

import java.util.Objects;

public class Email {

    public static Email fromString(String email){
        //TODO add validation
        return new Email(email);
    }

    @Override
    public String toString() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

    private Email(String email) {
        this.email = email;
    }

    private final String email;
}
