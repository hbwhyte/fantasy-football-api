package fantasy_football.security;

import fantasy_football.exceptions.CallsExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static fantasy_football.security.RateLimiter.createRateLimiter;

@Component
public class RateLimitingInterceptor extends HandlerInterceptorAdapter {

    // API limit refreshes every 1 timeUnit
    // For partial units e.g. 15 minutes, you need to adjust the schedulePermitReplenishment() method
    private TimeUnit timeUnit = TimeUnit.DAYS;
    private int timeAmount = 1;
    // Number of API calls per TimeUnit
    private int limit = 200;

    public static final Logger LOG
            = Logger.getLogger(String.valueOf(RateLimitingInterceptor.class));

    private Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String apiKey = request.getParameter("api");
        // let non-API requests pass
        if (apiKey == null) {
            return true;
        }
        RateLimiter rateLimiter = getRateLimiter(apiKey);
        // Sees if this API has calls remaining or not
        boolean allowRequest = rateLimiter.tryAcquire();

        // If they exceeded their request limit, reject the request and throw CallsExceeded Exception
        if (!allowRequest) {
            rateLimiter.stop();
            rateLimiter.schedulePermitReplenishment();
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            throw new CallsExceededException("Too many calls. Maximum of " + limit +
                    " API requests per " + timeAmount +" "+ timeUnit.toString().toLowerCase() +
            ". "); // + calculateTimeRemaining(rateLimiter));
        }
        //response.addHeader("Rate-Limit-Limit", String.valueOf(limit));

        // If they have not exceeded their request limit, return true.
        return allowRequest;
    }

//    private String calculateTimeRemaining(RateLimiter rateLimiter) {
//
//        long initTime = rateLimiter.getTimeCreated();
//        long currentTime = System.currentTimeMillis();
//        long resetLength = timeUnit.toMillis(1);
//        long resetTime = resetLength - ((currentTime - initTime)%resetLength);
//
//        return "There are " + resetTime + " milliseconds remaining until you can make more calls.";
//    }

    private RateLimiter getRateLimiter(String apiKey) {
        if (limiters.containsKey(apiKey)) {
            return limiters.get(apiKey);
        } else {
            synchronized(apiKey.intern()) {
                // double-checked locking to avoid multiple-reinitializations
                if (limiters.containsKey(apiKey)) {
                    return limiters.get(apiKey);
                }
                // Allows 200 calls per day
                RateLimiter rateLimiter = createRateLimiter(limit, timeAmount, timeUnit);

                limiters.put(apiKey, rateLimiter);
                return rateLimiter;
            }
        }
    }

    @PreDestroy
    public void destroy() {
        // loop and finalize all limiters
    }
}