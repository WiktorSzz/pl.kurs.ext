package pl.kurs.ws_test3r.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.*;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEventListener.class);

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        String username = event.getAuthentication().getName();

        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            logger.warn("Authentication failed for user: {}", username);
            loginAttemptService.loginFailed(username);
        } else if (event instanceof AuthenticationSuccessEvent) {
            logger.info("Authentication succeeded for user: {}", username);
            loginAttemptService.loginSucceeded(username);
        }
    }
}
