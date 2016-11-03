package praktikum_1;

import java.util.*;

public class DevelopingEnvironment {

    private Time time;
    private Statistics statistics;

    private int lockerAmount;
    private int simulationDay;
    private int vipEncounters;
    private int dailyAmount;

    private long openingHours;
    //private long closingTime;
    //private long currentTime;
    private long timeToChange;
    private long arrivalTimeVIP;
    private long timeOfArrival;

    private double guestProbability;

    private boolean vipArrived;
    private boolean vipLockerAssigned;
    private boolean vipLeft;
    private boolean encounterOnEnter;
    private boolean encounterOnExit;

    private Locker dummyLocker;
    private Locker targetLocker;

    private List<Locker> lockerList;
    private List<Integer> occupiedNeighbours;
    private List<Integer> freeNeighbours;
    private Map<Float, Long> probabilityMap;
    private List<Float> percentageArray;
    private Map<Long, Integer> dailyStats = new HashMap<>();
    List<Long> diffTimes;



    /**
     * Initializes all Lockers and sets all default values
     */
    public DevelopingEnvironment(int lockerAmount, int simulationDay, long day, long timeOfArrival, long timeToChange,
                                 Map<Float, Long> percentageMap, double guestProbability) {
        this.lockerAmount = lockerAmount;
        this.simulationDay = simulationDay;
        //this.t = new Time(day); TODO ist in init()
        this.openingHours = day;
        this.timeOfArrival = timeOfArrival;
        // this.t = new Time(openingHours);
        //this.closingTime = t.getDayTime();
        //this.arrivalTimeVIP = t.inSec(timeOfArrival); TODO ist in init
        this.timeToChange = timeToChange;
        this.guestProbability = guestProbability;
        this.probabilityMap = percentageMap;
        this.dailyAmount = 0;
        this.percentageArray = new ArrayList<>(probabilityMap.keySet());
        Collections.sort(percentageArray);
        init();
    }

    /**
     * Assignes a random locker to a Person
     */
    private void assignLocker() {
        Locker locker;

        int lockerNr = randomLockerNumber();
        locker = lockerList.get(lockerNr);
        long duration = getRandomDuration();
        locker.setOccupied(true);
        locker.setChangeOnArrival(time.getCurrentTime() + timeToChange);
        locker.setChangeOnDeparture(time.getCurrentTime() + duration - timeToChange);
        locker.setDuration(duration);
        lockerList.set(locker.getLockerNumber(), locker);
        if (vipArrived && targetLocker == null && !vipLeft) {
            targetLocker = locker;
            vipLockerAssigned = true;
        }

        dailyAmount = statistics.getMap().get(time.inMin(duration));
        dailyAmount++;
        statistics.getMap().replace(time.inMin(duration), dailyAmount);
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
        return guestTime;
    }


    private void freeLocker(int lockerNr){
        Locker locker;

        if(occupiedNeighbours.size() != 0){
            for(int i = 0; i < occupiedNeighbours.size() - 1; i++){
                if(lockerNr == occupiedNeighbours.get(i))
                    updateNeighbourList();
            }
        }

        if(targetLocker != null && targetLocker.getLockerNumber() == lockerNr)
            vipLeft = true;

        locker = lockerList.get(lockerNr);
        locker.releaseLocker();
        lockerList.set(lockerNr, locker);
    }

    /**
     * Checks if the duration of a Locker to be occupied
     * is up and frees it if so
     */
    private void updateLockers() {
        Locker locker;
        for(int i = 0; i < lockerAmount; i++) {
            locker = lockerList.get(i);
            if (locker.isOccupied() && timeUp(locker.getLockerNumber()))
                freeLocker(locker.getLockerNumber());
        }
    }

    private boolean timeUp(int lockerNr) {
        Locker locker = lockerList.get(lockerNr);
        return locker.changeOnDeparture < time.getCurrentTime();
    }

    /**
     * Checks if a Visitor is entering the studio
     *
     * @return true if a Visitor is coming
     */
    public boolean checkForVisitor() {
        double probability = Math.random();
        return probability <= guestProbability;
    }

    /**
     * @return random number of Locker to be assigned
     */
    private int randomLockerNumber() {
        int lockerNumber;
        Random rnd = new Random();
        Locker locker;

        while (true) {
            lockerNumber = rnd.nextInt(lockerAmount);
            locker = lockerList.get(lockerNumber);
            if (!locker.isOccupied())
                return lockerNumber;
        }
    }

    /**
     * Checks if the time is due for the Focus
     * Person to be entering the Studio
     */
    private void checkForFocusPerson() {
        if (time.getCurrentTime() > arrivalTimeVIP - timeToChange && time.getCurrentTime() < arrivalTimeVIP + timeToChange
                && !vipArrived && !vipLockerAssigned)
            vipArrived = true;
    }

    private void updateNeighbourList(){
        Locker locker;
        occupiedNeighbours.clear();
        freeNeighbours.clear();

        for(int i = 0; i < targetLocker.neighbours.size(); i++){
            locker = lockerList.get(targetLocker.neighbours.get(i));
            if (locker.isOccupied())
                occupiedNeighbours.add(locker.getLockerNumber());
            else
                freeNeighbours.add(locker.getLockerNumber());
        }
    }

    private int encounter(){
        Locker locker;

        if(encounterOnEnter && encounterOnExit) return 2;

        for(int i = 0; i < occupiedNeighbours.size(); i++){
            locker = lockerList.get(occupiedNeighbours.get(i));
            long dIn1 = locker.changeOnArrival - timeToChange;
            long dIn2 = locker.changeOnArrival;
            long dOut1 = locker.changeOnDeparture;
            long dOut2 = locker.changeOnDeparture + timeToChange;

            long tIn1 = targetLocker.changeOnArrival - timeToChange;
            long tIn2 = targetLocker.changeOnArrival;
            long tOut1 = targetLocker.changeOnDeparture;
            long tOut2 = targetLocker.changeOnDeparture + timeToChange;


            if(!encounterOnEnter) {
                if ((dIn1 <= tIn1 && dIn2 >= tIn1 && dIn2 <= tIn2) ||
                        (dIn1 >= tIn1 && dIn1 <= tIn2 && dIn2 >= tIn2) ||
                        (dOut1 <= tIn1 && dOut2 >= tIn1 && dOut2 <= tIn2) ||
                        (dOut1 >= tIn1 && dOut1 <= tIn2 && dOut2 >= tIn2)) {
                    encounterOnEnter = true;
                }
            }
            if(!encounterOnExit) {
                if ((dIn1 <= tOut1 && dIn2 >= tOut1 && dIn2 <= tOut2) ||
                        (dIn1 >= tOut1 && dIn1 <= tOut2 && dIn2 >= tOut2) ||
                        (dOut1 <= tOut1 && dOut2 >= tOut1 && dOut2 <= tOut2) ||
                        (dOut1 >= tOut1 && dOut1 <= tOut2 && dOut2 >= tOut2)) {
                    encounterOnExit = true;
                }
            }
        }

        if(encounterOnEnter && !encounterOnExit || !encounterOnEnter && encounterOnExit) return 1;
        return 0;
    }

    /**
     * Initializes all parameters for the Simulation
     */
    public void init() {

        statistics = new Statistics(dailyStats);
        time = new Time(openingHours);

        vipEncounters = 0;
        arrivalTimeVIP = time.inSec(timeOfArrival);

        vipArrived = false;
        vipLockerAssigned = false;
        vipLeft = false;
        encounterOnEnter = false;
        encounterOnExit = false;

        diffTimes = new ArrayList<>(probabilityMap.values());
        lockerList = new LinkedList<>();
        occupiedNeighbours = new LinkedList<>();
        freeNeighbours = new LinkedList<>();

        for (long l : diffTimes) {
            dailyStats.put(time.inMin(l), 0);
        }

        for (int i = 0; i < lockerAmount; i++) {
            dummyLocker = new Locker(i, false, 0, 0, 0, null);
            dummyLocker.setNeighbours(dummyLocker.getLockerNumber(), lockerAmount);
            lockerList.add(i, dummyLocker);
        }
   }

   public int getEncounters(){return vipEncounters;}

    /**
     * Simulates the whole Environment/Scenario
     */
    public void simulate() {
        while (time.currentTime < time.time) {
            routine();
            time.timeInterval();
        }
        //TODO MUSS WIEDER REIN
        //statistics.saveData(simulationDay);
        //System.out.println("ENCOUNTERS TODAY: "+ vipEncounters);
    }

    /**
     * Settles the Procedure of the Simulation
     */
    private void routine() {

       if (!checkForVisitor()) {
            updateLockers();
            return;
       }
       checkForFocusPerson();
       assignLocker();
       if(targetLocker != null && !vipLeft) {
           updateNeighbourList();
           if(targetLocker.changeOnArrival >= time.currentTime){
               vipEncounters = encounter();
           }else if((targetLocker.changeOnDeparture + timeToChange) <= time.currentTime){
               vipEncounters = encounter();
           }
       }
       updateLockers();
    }
}