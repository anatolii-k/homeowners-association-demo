package anatolii.k.hoa.common.config;

import anatolii.k.hoa.common.domain.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberInitializer {

    @Value("${hoa.phone-number.country-code.default}")
    private String defaultCountryCode;

    @PostConstruct
    void setDefaultCountryCode(){
        PhoneNumber.setDefaultCountryCode(defaultCountryCode);
    }
}
