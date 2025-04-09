package anatolii.k.hoa.common.domain;


import java.util.Objects;

public class PhoneNumber {

    public static PhoneNumber fromString(String str){
        // TODO: implement validation and parsing
        return new PhoneNumber(str);
    }

    @Override
    public String toString() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    private PhoneNumber(String phoneNum){
        number = phoneNum;
    }

    private final String number;
}
