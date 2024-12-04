package pl.kurs.ws_test3r.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        logger.error("AuthenticationException: {}", authException.getClass().getName());
        logger.error("Exception message: {}", authException.getMessage());
        String username = extractUsernameFromRequest(request);
        logger.info("Handling authentication exception for user: {}", username);
        String errorMessage = "Authentication failed.";
        AuthenticationException rootException = getRootCause(authException);
        logger.info("Root exception type: {}", rootException.getClass().getName());

        if (rootException instanceof LockedException) {
            long remainingLockTime = loginAttemptService.getRemainingLockTime(username);
            String time = formatTime(remainingLockTime);
            errorMessage = "Your account is locked due to too many failed login attempts. Please try again after " + time + ".";
            logger.warn("User {} is blocked. Remaining lock time: {}", username, time);
        } else if (rootException instanceof BadCredentialsException || rootException instanceof org.springframework.security.authentication.InsufficientAuthenticationException) {
            int attemptsLeft = loginAttemptService.getRemainingAttempts(username);
            if (attemptsLeft > 0) {
                errorMessage = "Invalid username or password. You have " + attemptsLeft + " attempt(s) left.";
            } else if (authException instanceof org.springframework.security.authentication.InsufficientAuthenticationException) {
                if (loginAttemptService.isBlocked(username)) {
                    long remainingLockTime = loginAttemptService.getRemainingLockTime(username);
                    String time = formatTime(remainingLockTime);
                    errorMessage = "Your account is locked due to too many failed login attempts. Please try again after " + time + ".";
                    logger.warn("User {} is blocked. Remaining lock time: {}", username, time);
                    logger.warn("User {} has {} remaining login attempt(s).", username, attemptsLeft);
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
    }

    private AuthenticationException getRootCause(AuthenticationException exception) {
        Throwable cause = exception.getCause();
        while (cause != null && cause instanceof AuthenticationException) {
            exception = (AuthenticationException) cause;
            cause = cause.getCause();
        }
        return exception;
    }


    private String extractUsernameFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);
            if (values.length == 2) {
                return values[0];
            }
        }
        return "Unknown";
    }


    private String formatTime(long millis) {
        if (millis <= 0) {
            return "0 second(s)";
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        if (minutes > 0) {
            return minutes + " minute(s) and " + seconds + " second(s)";
        } else {
            return seconds + " second(s)";
        }
    }
}
