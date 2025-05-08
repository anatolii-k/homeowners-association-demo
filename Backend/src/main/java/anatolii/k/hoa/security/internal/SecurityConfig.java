package anatolii.k.hoa.security.internal;

import anatolii.k.hoa.security.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        config -> {
                            configUnitEndpoint(config);
                            configResidentEndpoint(config);
                            configPersonEndpoint(config);
                            configBudgetPlanEndpoint(config);
                            configPublicEndpoints(config);

                            config.anyRequest().denyAll();
                        }
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role(Roles.ADMIN).implies(Roles.USER)
                .role(Roles.BOARD_MEMBER).implies(Roles.USER)
                .build();
    }

    private void configPublicEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
        config.requestMatchers("/login").permitAll();
        config.requestMatchers("/about").permitAll();
    }

    private void configBudgetPlanEndpoint(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
        config.requestMatchers(HttpMethod.GET, "/api/budget-plan/*").authenticated();
        config.requestMatchers("/api/budget-plan/**").hasRole(Roles.BOARD_MEMBER);
    }

    private void configPersonEndpoint(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
        config.requestMatchers("/api/person/**").hasRole(Roles.ADMIN);
    }

    private void configResidentEndpoint(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
        config.requestMatchers("/api/resident/**").hasRole(Roles.ADMIN);
    }

    private void configUnitEndpoint(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
        config.requestMatchers(HttpMethod.GET, "/api/units/*").authenticated();
        config.requestMatchers("/api/units/**").hasRole(Roles.ADMIN);
    }
}
