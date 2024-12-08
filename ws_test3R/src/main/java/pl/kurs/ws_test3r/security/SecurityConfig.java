package pl.kurs.ws_test3r.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final LoginAttemptService loginAttemptService;

    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          LoginAttemptService loginAttemptService) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.loginAttemptService = loginAttemptService;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN", "IMPORTER", "EMPLOYEE")
                .build();

        UserDetails importer = User.withUsername("importer")
                .password(passwordEncoder.encode("importer"))
                .roles("IMPORTER", "EMPLOYEE")
                .build();

        UserDetails employee = User.withUsername("employee")
                .password(passwordEncoder.encode("employee"))
                .roles("EMPLOYEE")
                .build();

        return new InMemoryUserDetailsManager(admin, importer, employee);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider customAuthenticationProvider) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/persons").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/persons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/import/**").hasAnyRole("ADMIN", "IMPORTER")
                        .requestMatchers(HttpMethod.POST, "/api/employee/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/account/status").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .authenticationProvider(customAuthenticationProvider)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }




    @Bean
    public AuthenticationProvider customAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(userDetailsService, loginAttemptService, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
