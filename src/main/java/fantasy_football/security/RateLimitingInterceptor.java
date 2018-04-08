package fantasy_football.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static fantasy_football.security.SimpleRateLimiter.createRateLimiter;

@Component
public class RateLimitingInterceptor extends HandlerInterceptorAdapter {

    public static final Logger LOG
            = Logger.getLogger(String.valueOf(RateLimitingInterceptor.class));

    @Value("${rate.limit.enabled}")
    private boolean enabled;

    @Value("${rate.limit.hourly.limit}")
    private int hourlyLimit;

    private Map<String, Optional<SimpleRateLimiter>> limiters = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!enabled) {
            return true;
        }
        String apiKey = request.getHeader("api"); // NOT ACTUALLY IN THE HEADER. ONE OF 2 QUERY PARAMS
        // let non-API requests pass
        if (apiKey == null) {
            return true;
        }
        SimpleRateLimiter rateLimiter = getRateLimiter(apiKey);
        boolean allowRequest = rateLimiter.tryAcquire(); // probably rateLimiter, possibly limiter

        if (!allowRequest) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        response.addHeader("Rate-Limit-Limit", String.valueOf(hourlyLimit));
        return allowRequest;
    }

    private SimpleRateLimiter getRateLimiter(String apiKey) {
        if (limiters.containsKey(apiKey)) {
            return limiters.get(apiKey);
        } else {
            synchronized(apiKey.intern()) {
                // double-checked locking to avoid multiple-reinitializations
                if (limiters.containsKey(apiKey)) {
                    return limiters.get(apiKey);
                }

                SimpleRateLimiter rateLimiter = createRateLimiter(apiKey);

                limiters.put(apiKey, rateLimiter);
                return rateLimiter;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }

    @PreDestroy
    public void destroy() {
        // loop and finalize all limiters
    }
}