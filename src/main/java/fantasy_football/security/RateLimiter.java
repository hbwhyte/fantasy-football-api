package fantasy_football.security;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Implements Java's Semaphore utility to implement API throttling
 */
public class RateLimiter {
    // variable used to manage API calls and control access
    private Semaphore semaphore;
    // max number of calls allowed per time period
    private int maxCalls;
    // How frequently the allowance resets (e.g. daily, hourly etc.)
    private TimeUnit timePeriod;
    //ExecutorService that resets semaphore to run after a set timePeriod
    private ScheduledExecutorService scheduler;
    private long timeCreated;
    private int timeAmount;

    // Constructor
    private RateLimiter(int maxCalls, int timeAmount, TimeUnit timeUnit) {
        this.semaphore = new Semaphore(maxCalls);
        this.maxCalls = maxCalls;
        this.timeAmount = timeAmount;
        this.timePeriod = timeUnit;
        this.timeCreated = System.currentTimeMillis();
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * Creates a RateLimiter object based on the rates and time period
     * passed into the params, and sets the scheduler with when it should refresh
     *
     * @param maxCalls Maximum number of calls and API can make before being reset
     * @param timePeriod How frequently the limit is reset
     * @return RateLimiter Object
     */
    public static RateLimiter createRateLimiter(int maxCalls, int timeAmount, TimeUnit timePeriod) {
        // Creates new RateLimiter object
        RateLimiter rateLimiter = new RateLimiter(maxCalls, timeAmount, timePeriod);
        // Schedules reset timeline, based on given params
        rateLimiter.schedulePermitReplenishment();
        return rateLimiter;
    }

    /**
     * Boolean check if rate limit has yet been exceeded
     *
     * @return boolean False if call limit has been exceeded, true if
     * calls still remain
     */
    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    /**
     * Sets the reset schedule for the API Limit. If you want to create
     * a limit that is an irregular time period (e.g. not per minute
     * or per hour, but say, every 15 minutes), you can adjust the delay
     * to be 15 instead of 1. However the RateLimitingInterceptor class
     * is designed to work with a delay of 1.
     */
    public void schedulePermitReplenishment() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            semaphore.release(maxCalls - semaphore.availablePermits());
        }, timeAmount, timePeriod);
    }

}
