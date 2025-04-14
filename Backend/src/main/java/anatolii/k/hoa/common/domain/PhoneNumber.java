package anatolii.k.hoa.common.domain;

import java.util.Objects;

public class PhoneNumber {

    public enum ErrorCode{
        INVALID_PHONE_NUMBER,
        INVALID_COUNTRY_CODE
    }

    private static final String invalidNumberMsg = "Phone number [%s] is invalid";
    private static String defaultCountryCode;
    private static final int numberOfDigits = 12;

    public static PhoneNumber fromString(String str){
        if (str == null || str.isBlank()) {
            return null;
        }
        validatePreliminary(str);

        String cleanedStr = str.replaceAll("[^\\d]", "");

        boolean isNumberWithoutCountryCode = cleanedStr.length() == (numberOfDigits - 2);
        if(isNumberWithoutCountryCode){
            cleanedStr = defaultCountryCode + cleanedStr;
        }
        if( cleanedStr.length() != numberOfDigits){
            throw new CommonException(ErrorCode.INVALID_PHONE_NUMBER.toString(),
                    invalidNumberMsg.formatted(str));
        }
        return new PhoneNumber(cleanedStr);
    }

    public static void setDefaultCountryCode(String countryCode){
        validateCountryCode(countryCode);
        defaultCountryCode = countryCode;
    }

    private static void validateCountryCode(String countryCode) {
        if(!countryCode.matches("^\\d\\d$")){
            throw new CommonException(ErrorCode.INVALID_COUNTRY_CODE.toString(),
                    "Country code [%s] is invalid".formatted(countryCode));
        }
    }

    private static void validatePreliminary(String str) {
        if(!str.matches("^\\+?[\\d\\s()-]{6,}$")){
            throw new CommonException(ErrorCode.INVALID_PHONE_NUMBER.toString(),
                    invalidNumberMsg.formatted(str));
        }
    }

    private static void validate(String str) {
    }

    @Override
    public String toString() {
        return number;
    }

    public String getFormatedString(){
        return String.format("+%s(%s)%s-%s-%s",
                number.substring(0, 2),
                number.substring(2, 5),
                number.substring(5, 8),
                number.substring(8, 10),
                number.substring(10));
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
