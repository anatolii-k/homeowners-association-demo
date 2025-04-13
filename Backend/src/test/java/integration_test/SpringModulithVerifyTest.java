package integration_test;

import anatolii.k.hoa.HoaApplication;
import org.springframework.modulith.core.ApplicationModules;
import org.junit.jupiter.api.Test;

public class SpringModulithVerifyTest {
    @Test
    void verify(){
        ApplicationModules.of(HoaApplication.class)
                .verify()
                .forEach(System.out::println);
    }

}
