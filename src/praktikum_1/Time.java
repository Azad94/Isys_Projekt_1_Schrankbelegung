package praktikum_1;

public class Time {

    long time;
    long currentTime;

    public Time(long dayTime) {
        this.currentTime = 0;
        this.time = inSec(dayTime);
    }

    public void timeInterval(){
        if(currentTime<time) {
            currentTime += 10;
        }
    }

    public long getCurrentTime() {
        return currentTime;
    }
    public long getDayTime() {
        return time;
    }

    public long inSec(long a){
        a = a * 60 * 60;
        return a;
    }

}
