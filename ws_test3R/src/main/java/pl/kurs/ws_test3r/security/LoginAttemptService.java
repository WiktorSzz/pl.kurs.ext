package pl.kurs.ws_test3r.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);

    private final int MAX_ATTEMPT = 3;
    private final long ATTEMPT_RESET_DURATION = TimeUnit.MINUTES.toMillis(1);
    private final long LOCK_TIME_DURATION = TimeUnit.MINUTES.toMillis(10);

    private ConcurrentHashMap<String, UserLoginAttempt> attempts = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        UserLoginAttempt userAttempt = attempts.getOrDefault(username, new UserLoginAttempt());
        userAttempt.addFailedAttempt(System.currentTimeMillis());

        userAttempt.removeOldAttempts(System.currentTimeMillis() - ATTEMPT_RESET_DURATION);

        if (userAttempt.getFailedAttempts().size() >= MAX_ATTEMPT && !userAttempt.isLocked()) {
            userAttempt.lock(System.currentTimeMillis() + LOCK_TIME_DURATION);
            logger.warn("User {} is now locked for {} minute(s) and {} second(s).", username,
                    TimeUnit.MILLISECONDS.toMinutes(LOCK_TIME_DURATION),
                    TimeUnit.MILLISECONDS.toSeconds(LOCK_TIME_DURATION) % 60);
        } else {
            int attemptsLeft = MAX_ATTEMPT - userAttempt.getFailedAttempts().size();
            logger.warn("User {} failed login attempt. {} attempt(s) left before lock.", username, attemptsLeft);
        }

        attempts.put(username, userAttempt);
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
        logger.info("User {} login succeeded. Resetting failed attempts.", username);
    }

    public boolean isBlocked(String username) {
        UserLoginAttempt userAttempt = attempts.get(username);
        if (userAttempt == null) {
            return false;
        }

        if (userAttempt.isLocked()) {
            if (System.currentTimeMillis() > userAttempt.getLockTime()) {
                attempts.remove(username);
                logger.info("User {} is now unblocked.", username);
                return false;
            }
            return true;
        }
        return false;
    }

    public long getRemainingLockTime(String username) {
        UserLoginAttempt userAttempt = attempts.get(username);
        if (userAttempt != null && userAttempt.isLocked()) {
            return userAttempt.getLockTime() - System.currentTimeMillis();
        }
        return 0;
    }

    public int getRemainingAttempts(String username) {
        if (isBlocked(username)) {
            return 0;
        }

        UserLoginAttempt userAttempt = attempts.get(username);
        if (userAttempt == null) {
            return MAX_ATTEMPT;
        }

        userAttempt.removeOldAttempts(System.currentTimeMillis() - ATTEMPT_RESET_DURATION);
        return Math.max(MAX_ATTEMPT - userAttempt.getFailedAttempts().size(), 0);
    }

    @Scheduled(fixedRate = 60000)
    public void unlockAccounts() {
        long currentTime = System.currentTimeMillis();
        attempts.forEach((username, userAttempt) -> {
            if (userAttempt.isLocked() && currentTime > userAttempt.getLockTime()) {
                attempts.remove(username);
                logger.info("User {} is now automatically unblocked by scheduled task.", username);
            }
        });
    }

    private static class UserLoginAttempt {
        private List<Long> failedAttempts = new CopyOnWriteArrayList<>();
        private long lockTime = 0;

        public void addFailedAttempt(long timestamp) {
            failedAttempts.add(timestamp);
        }

        public void removeOldAttempts(long cutoffTime) {
            failedAttempts.removeIf(attemptTime -> attemptTime < cutoffTime);
        }

        public List<Long> getFailedAttempts() {
            return failedAttempts;
        }

        public void lock(long lockUntil) {
            this.lockTime = lockUntil;
        }

        public long getLockTime() {
            return lockTime;
        }

        public boolean isLocked() {
            return lockTime > System.currentTimeMillis();
        }
    }
}
