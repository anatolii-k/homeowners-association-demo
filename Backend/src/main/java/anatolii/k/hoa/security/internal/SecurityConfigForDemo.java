package anatolii.k.hoa.security.internal;

import anatolii.k.hoa.security.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@Profile("demo")
public class SecurityConfigForDemo {

    @Bean
    public UserDetailsService userDetailsService(){
        return new InMemoryUserDetailsManager(
                User.builder().username("user1")
                        .password("{noop}pwd1")
                        .roles(Roles.USER)
                        .build(),
                User.builder().username("admin")
                        .password("{noop}pwd2")
                        .roles(Roles.ADMIN)
                        .build(),
                User.builder().username("board1")
                        .password("{noop}pwd3")
                        .roles(Roles.BOARD_MEMBER)
                        .build()
        );
    }
}
