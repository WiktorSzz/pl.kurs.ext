package pl.kurs.ws_test3r.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;
    private final LoginAttemptService loginAttemptService;
    private final PasswordEncoder passwordEncoder;



    public CustomAuthenticationProvider(UserDetailsService userDetailsService,
                                        LoginAttemptService loginAttemptService,
                                        PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.loginAttemptService = loginAttemptService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        if (loginAttemptService.isBlocked(username)) {
            logger.warn("User {} is locked.", username);
            throw new LockedException("Your account is locked due to too many failed login attempts. Please try again later.");
        }

        String password = authentication.getCredentials().toString();
        UserDetails user;

        try {
            user = userDetailsService.loadUserByUsername(username);
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid username or password.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
