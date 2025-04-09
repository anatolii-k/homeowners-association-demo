package anatolii.k.hoa.common.domain;

import java.util.Objects;

public class SSN {

    public static SSN fromString(String ssn){
        // TODO: add SSN validation
        return new SSN(ssn);
    }

    @Override
    public String toString() {
        return ssn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SSN ssn1 = (SSN) o;
        return Objects.equals(ssn, ssn1.ssn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ssn);
    }

    private SSN(String ssn) {
        this.ssn = ssn;
    }

    private String ssn;
}
