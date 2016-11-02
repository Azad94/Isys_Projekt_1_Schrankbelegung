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
    //time window for people changing in the locker room && timeWindow for vip arrvial
    private long timeWindow;
    //probability that a guest arrvies at the gym
    private double guestProbabilty;
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
    //amount of persons for a certain time
    private int dailyAmount;
    //Locker for multiple uses
    private Locker dummyLocker;
    //Locker of the Focus Person
    private Locker targetLocker;
    Time t;
    Statistics s;

    Map<Float, Long> probabilityMap;

    List<Float> percentageArray;
    //Map for the daily time distribution for all guests
    Map<Long, Integer> dailyStats = new HashMap<>();

    /**
     * Initializes all Lockers and sets all default values
     */
    public DevelopingEnvironment(int lockerAmount, int simulationDay, long day, long arrival, long timewindow, Map<Float, Long> percentageMap, double guestProbabilty) {
        this.lockerAmount = lockerAmount;
        this.simulationDay = simulationDay;
        this.openingHours = day;
        this.t = new Time(openingHours);

        this.closingTime = t.getDayTime();
        this.timeOfArrivalOfFocusPerson = t.inSec(arrival);
        this.timeWindow = timewindow;
        this.guestProbabilty = guestProbabilty;
        System.out.println("EXPECTED TIME FOR FOCUSPERSON: " + timeOfArrivalOfFocusPerson);
        this.probabilityMap = percentageMap;
        this.dailyAmount = 0;
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
        dailyAmount = s.getMap().get(t.inMin(duration));
        System.out.println(dailyAmount);
        dailyAmount++;
        s.getMap().replace(t.inMin(duration), dailyAmount);
        l.setOccupied(true);
        l.setChange_In(t.getCurrentTime() + timeWindow);
        l.setChange_Out(t.getCurrentTime() + duration - timeWindow);
        l.setDuration(duration);
        lockers.set(l.getLockerNumber(), l);
        if (focusPersonArrived && targetLocker == null) {
            targetLocker = l;
            updateNeighbourList();
            focusLockerAssigned = true;
            System.out.println("FocusPerson arrived his locker number is ---------" + targetLocker.getLockerNumber());
        }
        l.releaseLocker();
        //s.updateDurationFrequency(duration);
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
        for (int q = 0; q < percentageArray.size(); q++) {
            if (rndFloat <= percentageArray.get(q) && rndFloat > compare) {
                guestTime = probabilityMap.get(percentageArray.get(q));
            }
            compare = percentageArray.get(q);
        }
        System.out.println("GUESTTIME: " + guestTime);
        return guestTime;
    }

    /**
     * Frees the locker if the Person using it has left the Studio
     *
     * @param locker locker to be freed
     */
    private void freeLocker(Locker locker){
        for(int i = 0; i < occupiedNeighbours.size() - 1; i++){
            if(locker.getLockerNumber() == occupiedNeighbours.get(i))
                updateNeighbourList();
        }

        locker.releaseLocker();
        lockers.set(locker.getLockerNumber(), locker);
    }

    /**
     * Checks if the duration of a Locker to be occupied
     * is up and frees it if so
     */
    private void updateLockers() {
        for(int i = 0; i < lockerAmount; i++) {
            dummyLocker.setLocker_number(i);
            if (timeUp(dummyLocker)) {
                freeLocker(dummyLocker);
            }
        }
    }

    /**
     * Checks if the time for a Locker to be occupied is up
     *
     * @param locker Locker to be checked
     * @return true if the time is up
     */
    private boolean timeUp(Locker locker) {
        return locker.change_Out <= currentTime;
    }

    /**
     * Checks if a Visitor is entering the studio
     *
     * @return true if a Visitor is coming
     */
    public boolean checkForVisitor() {
        double probability = Math.random();
        return probability <= guestProbabilty;
    }

    /**
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
        if (t.getCurrentTime() > timeOfArrivalOfFocusPerson - timeWindow && t.getCurrentTime() < timeOfArrivalOfFocusPerson + timeWindow
                && !focusPersonArrived && !focusLockerAssigned) {
             focusPersonArrived = true;
            System.out.println("FOKUS PERSON KOMMT JETZT!!!!!!");
            System.out.println("AKTUELLE ZEIT " + t.getCurrentTime());
            System.out.println("ARRIVAL ZEIT " + timeOfArrivalOfFocusPerson);
        }

    }

    public void updateNeighbourList(){
        Locker dummyLocker;

        for(int i = 0; i < targetLocker.neighbours.size(); i++){
            dummyLocker = lockers.get(targetLocker.neighbours.get(i));
            if (dummyLocker.isOccupied()) {
                occupiedNeighbours.add(dummyLocker.getLockerNumber());
            }
            else
            {
                freeNeighbours.add(dummyLocker.getLockerNumber());
            }
            dummyLocker.releaseLocker();
        }
    }


    public int encounter(){
        Locker dummy;
        int encounter = 0;

        for(int i = 0; i < occupiedNeighbours.size(); i++){
            dummy = lockers.get(occupiedNeighbours.get(i));
            //subtracting the time of change
            if(dummy.change_In >= targetLocker.change_In - 300 && dummy.change_In <= targetLocker.change_In){
                encounter++;
            }else
                //subtracting the time of change
                if(dummy.change_Out >= targetLocker.change_Out && dummy.change_Out <= targetLocker.change_Out + 300){
                    encounter++;
                }
        }
        return encounter;
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
        List<Long> diffTimes = new ArrayList<>(probabilityMap.values());
        for (long l : diffTimes) {
            dailyStats.put(t.inMin(l), 0);
        }
        s = new Statistics(dailyStats);
        System.out.println("MAAAPPP: " + s.getMap());
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
        while (t.getCurrentTime()< t.getDayTime()) {
            routine();
            t.timeInterval();
            System.out.println("Aktuelle Zeit: " + t.getCurrentTime());
        }
        System.out.println("ENDING DAY");
        s.saveData(simulationDay);
    }

    /**
     * Settles the Procedure of the Simulation
     */
    private void routine() {
        System.out.println("- ENTER routine()");
        if (!checkForVisitor()) {
            updateLockers();
            System.out.println("- No customer is coming ...\n");
            return;
        }
        System.out.println("- Customer is coming ...\n");
        checkForFocusPerson();
        assignLocker();
        if(targetLocker != null) {
            updateNeighbourList();
            totalEncounters = encounter();
        }
        updateLockers();

        if(targetLocker!=null){
            System.out.println("----------------- END OF ROTUINE LOOP ---------");
            System.out.println("**********TARGETLOCKER DETAILS**********");
            System.out.println(targetLocker.toString()+"\n");
            System.out.println("ENCOUNTERS TODAY: "+totalEncounters + " \n");
            System.out.println("DIE BESETZTEN NACHABRN" + occupiedNeighbours.toString()+"\n");
            System.out.println("DIE FREIEN NACHABRN" + freeNeighbours.toString()+"\n\n\n");
        }
    }
}