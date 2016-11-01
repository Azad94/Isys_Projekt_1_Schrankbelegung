package praktikum_1;

import java.util.*;

public class DevelopingEnvironment {

    //Amount of Lockers intialized
    private int lockerAmount;
    private int simulationDay;
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
    Time t;
    Statistics s;
    Map<Float, Long> probabilityMap;
    List<Float> percentageArray;
    Map<Long, Integer> dailyStats = new HashMap<>();
    /**
     * Initializes all Lockers and sets all default values
     */
    public DevelopingEnvironment(int lockerAmount, int simulationDay, Long day, Long arrival, Map<Float, Long> percentageMap){
        this.lockerAmount = lockerAmount;
        this.simulationDay = simulationDay;
        this.openingHours = day;
        this.t = new Time(openingHours);

        this.closingTime = t.getDayTime();
        this.timeOfArrivalOfFocusPerson = t.inSec(arrival);
        this.probabilityMap = percentageMap;
        //keys(wahrscheinlichkeiten eine bestimmte zeit zu bleiben) der map als liste und die sortiert um besser vergleichen zu können
        this.percentageArray = new ArrayList<>(probabilityMap.keySet());
        Collections.sort(percentageArray);
        init();
    }

    /**
     * Assignes a random locker to a Person
     */
    public void assignLocker() {
        Locker l;
        int number = randomLockerNumber();
        l = lockers.get(number);
        l.setLocker_number(number);
        long duration = getRandomDuration();
        System.out.println("TEST:DURATION: " + duration);
        System.out.println("TEST:MAP.KEY: " + duration/60);
        System.out.println("TEST:MAP.VALUE: " + dailyStats.get(duration/60)+1);
        dailyStats.replace(duration/60,dailyStats.get(duration/60)+1);
        l.setOccupied(true);
        if (focusPersonArrived) {
            targetLocker.setLocker_number(l.getLockerNumber());
            l.updateNeighbourList(l, occupiedNeighbours,freeNeighbours);
            focusLockerAssigned = true;
        }
        l.setChange_In(t.getCurrentTime() + 300);
        l.setChange_Out(t.getCurrentTime() + duration - 300);
        l.setDuration(duration);
        lockers.set(l.getLockerNumber(), l);
        l.releaseLocker();
        //s.updateDurationFrequency(duration);
        System.out.println("STATISTICS: ");
        System.out.println("------------------------------------------------------------ ");
        System.out.println(dailyStats);
    }

    /**
     * Noch mega hässlich aber wählt uns eine zufällige zeit aus.
     *
     * @return
     */
    private long getRandomDuration() {
        Long guestTime = 0l;
        Random rnd = new Random();
        float rndFloat = rnd.nextFloat();
        float compare = 0.0f;
        for(int q = 0; q<percentageArray.size();q++){

            //  (rndFloat <= percentageArray.get(q)) ? System.out.println(percentageMap.get(percentageArray.get(q))) :
            if(rndFloat<=percentageArray.get(q) && rndFloat>compare){
               guestTime = probabilityMap.get(percentageArray.get(q));
            }
            compare = percentageArray.get(q);
        }
        System.out.println("GUESTTIME: " + guestTime);
        return guestTime;
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
        Locker l;

        while (true) {
            lockerNumber = r.nextInt(lockerAmount);
            l = lockers.get(lockerNumber);
            if (!l.isOccupied()) {
                return lockerNumber;
            }
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
        } else  {
            focusPersonArrived = false;
        }
    }

    /**
     * Initializes all parameters for the Simulation
     */
    public void init() {
        System.out.println("- INTIALIZING ...");
        lockers = new LinkedList<>();
        occupiedNeighbours = new LinkedList<>();
        freeNeighbours = new LinkedList<>();
        focusPersonArrived = false;
        focusLockerAssigned = false;
        focusPersonLeft = false;
        totalEncounters = 0;
        targetLocker = new Locker(0, false, 0, 0, 0, null);
        List<Long> diffTimes = new ArrayList<>(probabilityMap.values());
        for(long l : diffTimes){
            dailyStats.put(l/60, 0);
        }
       for (int i = 0; i < lockerAmount; i++) {
           dummyLocker = new Locker(i, false, 0, 0, 0, null);
           dummyLocker.setNeighbours(dummyLocker.getLockerNumber(), lockerAmount);
           lockers.add(i, dummyLocker);
       }

       dummyLocker.releaseLocker();
    }

    /**
     * Simulates the whole Environment/Scenario
     */
    public void simulate() {
        System.out.println("- ENTER simulate()\n");
        while(t.currentTime<t.time){
            routine();
            t.timeInterval();
        }
        System.out.println("ENDING DAY");
        s = new Statistics(dailyStats);
        s.saveData(simulationDay);
    }

    /**
     * Settles the Procedure of the Simulation
     */
    private void routine(){
        System.out.println("- ENTER routine()");
        if(!checkForVisitor()){
            updateLockers();
            System.out.println("- No customer is coming ...\n");
            return;
        }
        System.out.println("- Customer is coming ...\n");
        checkForFocusPerson();
        assignLocker();
        if(focusPersonLeft)
            totalEncounters = targetLocker.encounter(targetLocker, occupiedNeighbours,freeNeighbours);
        updateLockers();
    }
}