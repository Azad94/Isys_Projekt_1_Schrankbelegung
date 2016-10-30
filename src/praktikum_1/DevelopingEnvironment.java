package praktikum_1;

import java.util.List;
import java.util.Random;

public class DevelopingEnvironment {

    //Amount of Lockers intialized
    private int lockerAmount = getLockerAmount();
    //List of intialized Lockers
    private List<Locker> lockers;
    //List of occupied Neighbours of the Focus Person
    private List<Integer> occupiedNeighbours;
    //List of free Neighbours of the Focus Person
    private List<Integer> freeNeighbours;
    //time the studio opens
    private long openingHours;
    //time the studio closes
    private long closingTime;
    private long currentTime;
    //time the Focus Person is expected to come
    private long timeOfArrivalOfFocusPerson;
    //shows if the Focus Person is in the studio
    private boolean focusPersonArrived;
    //shows if the Focus Person has been assigned a Locker
    private boolean focusLockerAssigned;
    //shows if the Focus Person is gone
    private boolean focusPersonLeft;
    //shows how many encounters the Focus Person had
    private int totalEncounters;
    //Locker for multiple uses
    private Locker dummyLocker;
    //Locker of the Focus Person
    private Locker targetLocker;
    Time t = new Time();
    Statistics s = new Statistics();

    /**
     * Initializes all Lockers and sets all default values
     */
    private void init() {
       // lockers = new LinkedList<>();
       // focusPersonArrived = false;
        // focusLockerAssigned = false;
      //  focusPersonLeft = false;
       // totalEncounters = 0;
        openingHours = 10;
        closingTime = t.inSec(openingHours); //um die Sekundenanzahl zu erhalten
        timeOfArrivalOfFocusPerson = t.inSec(15);
       // targetLocker = new Locker(0,0,0,0,null);

       /* for (int i = 0; i < lockerAmount; i++) {
            lockers.add(i, dummyLocker = new Locker(i, false, 0, 0, 0, null));
            dummyLocker.setNeighbours(i, lockerAmount);
        }*/
    }

    //TODO HABE ICH TIME GEPACKT
    /**
     *
     * @param a
     * @return
     *
    private long inSec(long a){
        a = a * 60 * 60;
        return a;
    }
    */

    /**
     * Assignes a random locker to a Person
     */
    public void assignLocker() {
        dummyLocker.setLocker_number(randomLockerNumber());
        long duration = getRandomDuration();
        dummyLocker.setOccupied(true);
        if (focusPersonArrived) {
            targetLocker.setLocker_number(dummyLocker.getLockerNumber());
            dummyLocker.updateNeighbourList(dummyLocker, occupiedNeighbours,freeNeighbours);
            focusLockerAssigned = true;
        }
        dummyLocker.setChange_In(t.getCurrentTime() + 300);
        dummyLocker.setChange_Out(duration - 300);
        dummyLocker.setDuration(duration);
        lockers.set(dummyLocker.getLockerNumber(), dummyLocker);
    }

    /**
     * Frees the locker if the Person using it has left the Studio
     * @param locker locker to be freed
     */
    private void freeLocker(Locker locker){
        for(int i = 0; i < occupiedNeighbours.size() - 1; i++){
            if(locker.getLockerNumber() == occupiedNeighbours.get(i))
                dummyLocker.updateNeighbourList(dummyLocker, occupiedNeighbours,freeNeighbours);
        }

        if(focusPersonArrived && locker.getLockerNumber()
                == targetLocker.getLockerNumber())
            focusPersonLeft = true;
        locker.releaseLocker();
        lockers.set(locker.getLockerNumber(), locker);
    }

    /**
     * Checks if the duration of a Locker to be occupied
     * is up and frees it if so
     */
    private void updateLockers(){
        for(int i = 0; i < lockerAmount; i++){
            dummyLocker.setLocker_number(i);
            if (timeUp(dummyLocker)){
                freeLocker(dummyLocker);
            }
        }
    }

    /**
     * Checks if the time for a Locker to be occupied is up
     * @param locker Locker to be checked
     * @return true if the time is up
     */
    private boolean timeUp(Locker locker){
        return locker.change_Out <= currentTime;
    }

    /**
     * Checks if a Visitor is entering the studio
     * @return true if a Visitor is coming
     */
    public boolean checkForVisitor() {
        double probability = Math.random();
        return probability <= 0.1;
    }

    /**
     *
     * @return random number of Locker to be assigned
     */
    private int randomLockerNumber() {
        int lockerNumber;
        Random r = new Random();

        while (true) {
            lockerNumber = r.nextInt((lockerAmount) + 1);
            dummyLocker.setLocker_number(lockerNumber);
            if (!dummyLocker.isOccupied()) return lockerNumber;
        }
    }

    /**
     * Checks if the time is due for the Focus
     * Person to be entering the Studio
     */
    public void checkForFocusPerson() {
        if (t.getCurrentTime() > timeOfArrivalOfFocusPerson
                && focusPersonArrived
                    && focusLockerAssigned) {
             focusPersonArrived = true;
        } else  focusPersonArrived = false;

    }

    /**
     * Simulates the whole Environment/Scenario
     */
    public void simulate() {
        routine();
        t.timeInterval();
    }

    /**
     * Settles the Procedure of the Simulation
     */
    private void routine(){
        if(!checkForVisitor()){
            updateLockers();
            return;
        }
        checkForFocusPerson();
        assignLocker();
        if(focusPersonLeft)
            totalEncounters = targetLocker.encounter(targetLocker, occupiedNeighbours,freeNeighbours);
        updateLockers();
    }

    // Nur zum testen
    public long getArrival(){
        return timeOfArrivalOfFocusPerson;
    }
    // Nur zum testen
    public long getClosingTime(){
        return closingTime;
    }

    private long getRandomDuration() {
        //TODO Implementieren
        return 1;
    }

    private int getLockerAmount() {
        //TODO IMPLEMENTIEREN
        return 0;
    }


    private void updateCurrentTime(long currentTime) {
        //TODO Implementieren
        if(currentTime > openingHours && currentTime < closingTime)
        this.currentTime = currentTime;
    }
}
