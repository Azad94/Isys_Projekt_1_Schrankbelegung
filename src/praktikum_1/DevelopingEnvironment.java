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
    private int exitCode = -1;

    private long openingHours;
    private long timeToChange;
    private long arrivalTimeVip;
    private long timeOfArrival;

    private double guestProbability;

    private boolean vipArrived;
    private boolean vipLockerAssigned;
    private boolean vipLeft;
    private boolean encounterOnEnter;
    private boolean encounterOnExit;
    //random or strategy distribution of Lockers
    private boolean withRandom;
    private boolean stats;

    private Random rnd;
    private Locker vipLocker;

    private List<Locker> lockerList;
    private List<Locker> freeLockers = new ArrayList<>();
    private List<Integer> occupiedNeighbours;
    private List<Integer> freeNeighbours;
    private List<Float> percentageArray;
    private Map<Float, Long> probabilityMap;
    private Map<Long, Integer> dailyStats = new HashMap<>();

    /**
     * Initializes all Lockers and sets all default values
     */
    public DevelopingEnvironment(int lockerAmount, int simulationDay, long day, long timeOfArrival, long timeToChange,
                                 Map<Float, Long> percentageMap, double guestProbability, boolean withRandom, boolean stats) {
        this.lockerAmount = lockerAmount;
        this.simulationDay = simulationDay;
        this.openingHours = day;
        this.timeOfArrival = timeOfArrival;
        this.rnd = new Random();
        this.sendHome = 0;
        this.withRandom = withRandom;
        this.stats = stats;
        this.timeToChange = timeToChange;
        this.guestProbability = guestProbability;
        this.probabilityMap = percentageMap;
        this.dailyAmount = 0;
        this.percentageArray = new ArrayList<>(probabilityMap.keySet());
        Collections.sort(percentageArray);
        init();
    }


    public int getEncounters() {
        return vipEncounters;
    }

    public int getSendHome() {
        return sendHome;
    }

    /**
     * Initializes all parameters for the Simulation
     */
    public void init() {
        Locker locker;
        List<Long> diffTimes;
        statistics = new Statistics(dailyStats);
        time = new Time(openingHours);

        vipEncounters = 0;
        arrivalTimeVip = time.inSec(timeOfArrival);

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
            locker = new Locker(i, false, 0, 0, 0, null);
            locker.setNeighbours(locker.getLockerNumber(), lockerAmount);
            lockerList.add(i, locker);
        }
    }

    /**
     * Simulates the whole Environment/Scenario
     */
    public void simulate() {
        while (time.getCurrentTime() < time.getDayTime()) {
            routine();
            time.timeInterval();
        }

        if (stats) {
            statistics.saveDailyData(simulationDay, sendHome);
        }
    }

    /**
     * Settles the Procedure of the Simulation
     */
    private void routine() {

        if (checkForVisitor()) {
            assignLocker();
        }
        if (!vipArrived) {
            checkForFocusPerson();
        }
        if (vipLocker != null && !vipLeft) {
            updateNeighbourList();
            if ((time.getCurrentTime() >= (vipLocker.changeOnArrival - timeToChange)) && (time.getCurrentTime() <= vipLocker.changeOnArrival)) {
                vipEncounters = encounter();
            } else if ((time.getCurrentTime() >= vipLocker.changeOnDeparture) && (time.getCurrentTime() <= (vipLocker.changeOnDeparture + timeToChange))) {
                vipEncounters = encounter();
            }
        }
        updateLockers();
    }

    /**
     * Checks if the time is due for the Focus
     * Person to be entering the Studio
     */
    private void checkForFocusPerson() {
        if ((time.getCurrentTime() >= arrivalTimeVip - timeToChange) && (time.getCurrentTime() <= arrivalTimeVip + timeToChange)
                && !vipArrived && !vipLockerAssigned) {
            vipArrived = true;
        }
    }

    /**
     * Checks if a Visitor is entering the studio
     *
     * @return true if a Visitor is coming
     */
    private boolean checkForVisitor() {
        double probability = Math.random();
        return probability <= guestProbability;
    }

    /**
     * Assigns a random locker to a Person
     */
    private void assignLocker() {
        Locker locker;
        int lockerNr;
        if (withRandom) {
            lockerNr = randomLockerNumber();
        } else {
            lockerNr = strategyLockerNumber();
        }
        if (lockerNr == exitCode) {
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
        if (vipArrived && vipLocker == null && !vipLeft) {
            vipLocker = locker;
            vipLockerAssigned = true;
        }

        dailyAmount = statistics.getMap().get(time.inMin(duration));
        dailyAmount++;
        statistics.getMap().replace(time.inMin(duration), dailyAmount);
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
            return exitCode;
        } else {
            lockerNumber = rnd.nextInt(freeLockers.size());
            return freeLockers.get(lockerNumber).lockerNumber;
        }
    }

    private int strategyLockerNumber() {
        int addFive = 5;
        int addThree = 3;
        int indexZero = 0;
        int indexOne = 1;

        boolean fiveAdded = false;

        while (indexZero < lockerList.size()) {
            if (!lockerList.get(indexZero).isOccupied()) {
                return lockerList.get(indexZero).getLockerNumber();
            }
            if (!fiveAdded) {
                indexZero += addFive;
                fiveAdded = true;
            } else {
                indexZero += addThree;
                fiveAdded = false;
            }
        }

        while (indexOne < lockerList.size()) {
            if (!lockerList.get(indexOne).isOccupied()) {
                return lockerList.get(indexOne).getLockerNumber();
            }
            if (!fiveAdded) {
                indexOne += addFive;
                fiveAdded = true;
            } else {
                indexOne += addThree;
                fiveAdded = false;
            }
        }
        return randomLockerNumber();
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

    private void updateNeighbourList() {
        Locker locker;
        occupiedNeighbours.clear();
        freeNeighbours.clear();

        for (int i = 0; i < vipLocker.neighbours.size(); i++) {
            locker = lockerList.get(vipLocker.neighbours.get(i));
            if (locker.isOccupied()) {
                occupiedNeighbours.add(locker.getLockerNumber());
            } else {
                freeNeighbours.add(locker.getLockerNumber());
            }
        }
    }

    private void freeLocker(int lockerNr) {
        Locker locker;

        if (occupiedNeighbours.size() != 0) {
            for (int i = 0; i < occupiedNeighbours.size() - 1; i++) {
                if (lockerNr == occupiedNeighbours.get(i))
                    updateNeighbourList();
            }
        }

        if (vipLocker != null && vipLocker.getLockerNumber() == lockerNr)
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
     * checks for encounters
     * when the changing time of the focus person and some other guest, who uses a neighbour locker, is overlapping
     *
     * @return number of encounters (max 2)
     */
    private int encounter() {
        Locker locker;
        if (encounterOnEnter && encounterOnExit) return 2;

        long guestArrivalStart;
        long guestArrivalEnd;
        long guestDepartureStart;
        long guestDepartureEnd;

        long vipArrivalStart;
        long vipArrivalEnd;
        long vipDepartureStart;
        long vipDepartureEnd;


        for (int i = 0; i < occupiedNeighbours.size(); i++) {
            locker = lockerList.get(occupiedNeighbours.get(i));
            guestArrivalStart = locker.changeOnArrival - timeToChange;
            guestArrivalEnd = locker.changeOnArrival;
            guestDepartureStart = locker.changeOnDeparture;
            guestDepartureEnd = locker.changeOnDeparture + timeToChange;

            vipArrivalStart = vipLocker.changeOnArrival - timeToChange;
            vipArrivalEnd = vipLocker.changeOnArrival;
            vipDepartureStart = vipLocker.changeOnDeparture;
            vipDepartureEnd = vipLocker.changeOnDeparture + timeToChange;


            if (!encounterOnEnter) {
                if ((guestArrivalStart <= vipArrivalStart && guestArrivalEnd >= vipArrivalStart && guestArrivalEnd <= vipArrivalEnd) ||
                        (guestArrivalStart >= vipArrivalStart && guestArrivalStart <= vipArrivalEnd && guestArrivalEnd >= vipArrivalEnd) ||
                        (guestDepartureStart <= vipArrivalStart && guestDepartureEnd >= vipArrivalStart && guestDepartureEnd <= vipArrivalEnd) ||
                        (guestDepartureStart >= vipArrivalStart && guestDepartureStart <= vipArrivalEnd && guestDepartureEnd >= vipArrivalEnd)) {
                    encounterOnEnter = true;
                }
            }
            if (!encounterOnExit) {
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
}