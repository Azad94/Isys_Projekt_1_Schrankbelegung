package praktikum_1;

public class Time {

    private long time;
    private long currentTime;

    /**
     * Constructor
     * currentTime : time
     * time : overall time the simulation is running in s
     *
     * @param dayTime the amount of time the studio is open in hours
     */
    public Time(long dayTime) {
        this.currentTime = 0;
        this.time = inSec(dayTime);
    }

    /**
     * updates the current time in a 10s interval
     */
    public void timeInterval() {
        if (currentTime < time) {
            currentTime += 10;
        }
    }

    /**
     * Getter for the currentTime (startet counting from 0 -> time the studio closes)
     *
     * @return the currentTime
     */
    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * Getter for the amount of hours the studio is open
     *
     * @return the time the studio is open
     */
    public long getDayTime() {
        return time;
    }

    /**
     * Time in hours is required!
     * Calculates the time in seconds
     *
     * @param timeInHours time in hours
     * @return time in seconds
     */
    public long inSec(long timeInHours) {
        timeInHours = timeInHours * 60 * 60;
        return timeInHours;
    }

    /**
     * Time in seconds is required!
     * Calculates the time in min
     *
     * @param timeInSeconds time in seconds
     * @return time in minutes
     */
    public long inMin(long timeInSeconds) {
        timeInSeconds = timeInSeconds / 60;
        return timeInSeconds;
    }
}