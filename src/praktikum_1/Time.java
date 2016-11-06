package praktikum_1;

/**
 * Implements the class Time, which is needed
 *
 */
public class Time {

    private long totalTime;
    private long currentTime;

    /**
     * Constructor for Initialization of a Time object.
     *
     * totalTime       overall time the simulation is running in seconds
     * currentTime     value of time at the moment
     * @param dayTime   the amount of time the studio is opened in hours
     */
    public Time(long dayTime) {
        this.currentTime = 0;
        this.totalTime = inSec(dayTime);
    }

    /**
     * updates the current time in a 10s interval
     */
    public void timeInterval() {
        if (currentTime < totalTime) {
            currentTime += 10;
        }
    }

    /**
     * (started counting from 0 till the time the studio closes)
     * @return the currentTime
     */
    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * @return the time the studio is open
     */
    public long getDayTime() {
        return totalTime;
    }

    /**
     * @param timeInHours   time in hours
     * @return              time in seconds
     */
    public long inSec(long timeInHours) {
        timeInHours = timeInHours * 60 * 60;
        return timeInHours;
    }

    /**
     * @param timeInSeconds time in seconds
     * @return              time in minutes
     */
    public long inMin(long timeInSeconds) {
        timeInSeconds = timeInSeconds / 60;
        return timeInSeconds;
    }
}