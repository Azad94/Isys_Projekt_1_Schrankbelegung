package praktikum_1;

import java.util.*;

public class DevelopingEnvironment {

    private Time time;
    private Statistics statistics;

    private int lockerAmount;
    private int simulationDay;
    private int vipEncounters;
    private int dailyAmount;
    private int sendHome;

    private long openingHours;
    private long timeToChange;
    private long arrivalTimeVIP;
    private long timeOfArrival;

    private double guestProbability;

    private boolean vipArrived;
    private boolean vipLockerAssigned;
    private boolean vipLeft;
    private boolean encounterOnEnter;
    private boolean encounterOnExit;
    //random or strategy
    private boolean random;
    private boolean stats;
    private Random rnd;
    private Locker dummyLocker;
    private Locker targetLocker;

    private List<Locker> lockerList;
    private List<Integer> occupiedNeighbours;
    private List<Integer> freeNeighbours;
    private Map<Float, Long> probabilityMap;
    private List<Float> percentageArray;
    private Map<Long, Integer> dailyStats = new HashMap<>();
    List<Long> diffTimes;
    private List<Locker> freeLockers = new ArrayList<>();


    /**
     * Initializes all Lockers and sets all default values
     */
    public DevelopingEnvironment(int lockerAmount, int simulationDay, long day, long timeOfArrival, long timeToChange,
                                 Map<Float, Long> percentageMap, double guestProbability, boolean random, boolean stats) {
        this.lockerAmount = lockerAmount;
        this.simulationDay = simulationDay;
        this.openingHours = day;
        this.timeOfArrival = timeOfArrival;
        this.rnd = new Random();
        this.sendHome = 0;
        this.random=random;
        this.stats = stats;
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
        int lockerNr;
        if(random) {
            lockerNr = randomLockerNumber();
        }else{
            lockerNr = strategyLockerNumber();
        }
        if (lockerNr == -1) {
            sendHome++;
            return;
        }
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


    private void freeLocker(int lockerNr) {
        Locker locker;

        if (occupiedNeighbours.size() != 0) {
            for (int i = 0; i < occupiedNeighbours.size() - 1; i++) {
                if (lockerNr == occupiedNeighbours.get(i))
                    updateNeighbourList();
            }
        }

        if (targetLocker != null && targetLocker.getLockerNumber() == lockerNr)
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
        for (int i = 0; i < lockerAmount; i++) {
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
        freeLockers.clear();
        for (int i = 0; i < lockerList.size(); i++) {
            if (!lockerList.get(i).isOccupied()) {
                freeLockers.add(lockerList.get(i));
            }
        }
        if (freeLockers.size() == 0) {
            return -1;
        } else {
            lockerNumber = rnd.nextInt(freeLockers.size());
            return freeLockers.get(lockerNumber).locker_number;
        }
    }

    private int strategyLockerNumber() {
        int indexOne = 0;
        int indexTwo = 1;
        boolean plus5 = false;
        while(indexOne < lockerList.size()) {
            if (!lockerList.get(indexOne).isOccupied()) {
                return lockerList.get(indexOne).getLockerNumber();
            }
            if(!plus5){
                indexOne += 5;
                plus5 = true;
            }
            else{
                indexOne+=3;
                plus5 =false;
            }
        }

        while(indexTwo < lockerList.size()) {
            if (!lockerList.get(indexTwo).isOccupied()) {
                return lockerList.get(indexTwo).getLockerNumber();
            }
            if(!plus5){
                indexTwo += 5;
                plus5 = true;
            }
            else{
                indexTwo+=3;
                plus5 =false;
            }
        }

        return randomLockerNumber();
    }
    /**
     * Checks if the time is due for the Focus
     * Person to be entering the Studio
     */
    private void checkForFocusPerson() {
        if ((time.getCurrentTime() >= arrivalTimeVIP - timeToChange) && (time.getCurrentTime() <= arrivalTimeVIP + timeToChange)
                && !vipArrived && !vipLockerAssigned) {
            vipArrived = true;
        }
    }

    private void updateNeighbourList() {
        Locker locker;
        occupiedNeighbours.clear();
        freeNeighbours.clear();

        for (int i = 0; i < targetLocker.neighbours.size(); i++) {
            locker = lockerList.get(targetLocker.neighbours.get(i));
            if (locker.isOccupied()) {
                occupiedNeighbours.add(locker.getLockerNumber());
            } else {
                freeNeighbours.add(locker.getLockerNumber());
            }
        }
    }

    /**
     * checks for encounters
     * when the chaning time of the focus person and some other guest, who uses a neighbour locker, is overlapping
     * @return number of encounters (max 2)
     */
    private int encounter() {
        Locker locker;
        if (encounterOnEnter && encounterOnExit) return 2;
        /*System.out.println("---------------------------------------------\nENCOUNTER:");
        System.out.println("TargetNR :" + targetLocker.locker_number);
        System.out.println("Occupied?:" +targetLocker.isOccupied());
        System.out.println("changingIn: " + targetLocker.isChangingIn(time.getCurrentTime()));
        System.out.println("encounter?" + !encounterOnEnter);
        if (targetLocker.isOccupied() && targetLocker.isChangingIn(time.getCurrentTime()) && !encounterOnEnter) {
           // System.out.println("ERSTE IF\n");
            for (int i : occupiedNeighbours) {
                locker = lockerList.get(i);
                if (locker.isChangingIn(time.getCurrentTime()) || locker.isChangingOut(time.getCurrentTime())) {
                    encounterOnEnter = true;
                    System.out.println("ENCOUNTER ON ENTER\n");
                }
            }
        }
        else if (targetLocker.isOccupied() && targetLocker.isChangingOut(time.getCurrentTime()) && !encounterOnExit) {
         //   System.out.println("--------------------------------------------------------------");
         //   System.out.println("ZWEITE IF \n");
            for (int i : occupiedNeighbours) {
                locker = lockerList.get(i);
             //   System.out.println("OCCUPIEDNR: " + i);
             //   System.out.println("PICKED LOCKER: " + lockerList.get(i));
                if (locker.isChangingIn(time.getCurrentTime()) || locker.isChangingOut(time.getCurrentTime())) {
                    encounterOnExit = true;
                    System.out.println("ENCOUNTER ON EXIT");
                }
            }
        }*/
        long guestArrivalStart;
        long guestArrivalEnd;
        long guestDepartureStart;
        long guestDepartureEnd;

        long vipArrivalStart = targetLocker.changeOnArrival - timeToChange;
        long vipArrivalEnd = targetLocker.changeOnArrival;
        long vipDepartureStart = targetLocker.changeOnDeparture;
        long vipDepartureEnd = targetLocker.changeOnDeparture + timeToChange;

        for(int i = 0; i < occupiedNeighbours.size(); i++){
            locker = lockerList.get(occupiedNeighbours.get(i));

            guestArrivalStart = locker.changeOnArrival - timeToChange;
            guestArrivalEnd = locker.changeOnArrival;
            guestDepartureStart = locker.changeOnDeparture;
            guestDepartureEnd = locker.changeOnDeparture + timeToChange;

            if(!encounterOnEnter) {
                if ((guestArrivalStart <= vipArrivalStart && guestArrivalEnd >= vipArrivalStart && guestArrivalEnd <= vipArrivalEnd) ||
                        (guestArrivalStart >= vipArrivalStart && guestArrivalStart <= vipArrivalEnd && guestArrivalEnd >= vipArrivalEnd) ||
                        (guestDepartureStart <= vipArrivalStart && guestDepartureEnd >= vipArrivalStart && guestDepartureEnd <= vipArrivalEnd) ||
                        (guestDepartureStart >= vipArrivalStart && guestDepartureStart <= vipArrivalEnd && guestDepartureEnd >= vipArrivalEnd)) {
                    encounterOnEnter = true;
                }
            }
            if(!encounterOnExit) {
                if ((guestArrivalStart <= vipDepartureStart && guestArrivalEnd >= vipDepartureStart && guestArrivalEnd <= vipDepartureEnd) ||
                        (guestArrivalStart >= vipDepartureStart && guestArrivalStart <= vipDepartureEnd && guestArrivalEnd >= vipDepartureEnd) ||
                        (guestDepartureStart <= vipDepartureStart && guestDepartureEnd >= vipDepartureStart && guestDepartureEnd <= vipDepartureEnd) ||
                        (guestDepartureStart >= vipDepartureStart && guestDepartureStart <= vipDepartureEnd && guestDepartureEnd >= vipDepartureEnd)) {
                    encounterOnExit = true;
                }
            }
        }

        if ((encounterOnEnter && !encounterOnExit) || (!encounterOnEnter && encounterOnExit)) return 1;
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

    public int getEncounters() {
        return vipEncounters;
    }
    public int getSendHome(){
        return sendHome;
    }

    /**
     * Simulates the whole Environment/Scenario
     */
    public void simulate() {
        while (time.getCurrentTime() < time.getDayTime()) {
            routine();
            time.timeInterval();
        }

        if(stats) {
            statistics.saveDailyData(simulationDay, sendHome);
        }
    }

    /**
     * Settles the Procedure of the Simulation
     */
    private void routine() {

        if( checkForVisitor()){
            assignLocker();
        }
        if(!vipArrived){
            checkForFocusPerson();
        }
        if (targetLocker != null && !vipLeft) {
            updateNeighbourList();
            if ((time.getCurrentTime() >= (targetLocker.changeOnArrival - timeToChange)) && (time.getCurrentTime() <= targetLocker.changeOnArrival)) {
                vipEncounters = encounter();
            } else if ((time.getCurrentTime() >= targetLocker.changeOnDeparture) && (time.getCurrentTime() <= (targetLocker.changeOnDeparture + timeToChange))) {
                vipEncounters = encounter();
            }
        }
        updateLockers();
    }
}