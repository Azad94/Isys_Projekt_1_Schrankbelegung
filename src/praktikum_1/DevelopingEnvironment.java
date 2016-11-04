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

    //test
    private long a = 10;
    private double guestProbability;

    private boolean vipArrived;
    private boolean vipLockerAssigned;
    private boolean vipLeft;
    private boolean encounterOnEnter;
    private boolean encounterOnExit;
    //random or strategy
    private boolean random;

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
                                 Map<Float, Long> percentageMap, double guestProbability, boolean random) {
        this.lockerAmount = lockerAmount;
        this.simulationDay = simulationDay;
        //this.t = new Time(day); TODO ist in init()
        this.openingHours = day;
        this.timeOfArrival = timeOfArrival;
        this.rnd = new Random();
        this.sendHome = 0;
        this.random=random;
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
        int lockerNr;
        if(random) {
            lockerNr = randomLockerNumber();
        }else{
            lockerNr = strategyLockerNumber();
        }
        if (lockerNr == -1) {
            //System.out.println("!!\nDas Studio ist leider voll\n!!");
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
          /*  System.out.println("VIP arrived at " + time.getCurrentTime());
            System.out.println("His number is: " + locker.getLockerNumber());
            System.out.println("VIP duration is " + locker.duration);
            System.out.println("He arrived at " + (locker.changeOnArrival-timeToChange));
            System.out.println("His changeIn is " + locker.changeOnArrival);
            System.out.println("his changeOut is " + locker.changeOnDeparture);
            System.out.println("he is gone at " + (locker.changeOnDeparture+timeToChange));
            System.out.println("endtime - starttime : " + ((locker.changeOnDeparture+timeToChange)-(locker.changeOnArrival-timeToChange)));
            System.out.println("His Lockernumber is: " +locker.locker_number );
            System.out.println("His neighbours are: " + locker.neighbours.toString());*/
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
        // System.out.println("\nLOCKERSIZE: " + freeLockers.size());
        for (int i = 0; i < lockerList.size(); i++) {
            if (!lockerList.get(i).isOccupied()) {
                freeLockers.add(lockerList.get(i));
            }
        }
        //System.out.println("LISTE IST: " + freeLockers);
        if (freeLockers.size() == 0) {
            return -1;
        } else {
            // System.out.println("SCHEINT NICHT NULL ZU SEIN");
            lockerNumber = rnd.nextInt(freeLockers.size());
            // System.out.println("RANDOMNUMBER " + lockerNumber);
            // System.out.println("SPINT IST " + lockerList.get(lockerNumber));
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
        System.out.println("---------------------------------------------\nENCOUNTER2:");
        System.out.println("TargetNR :" + targetLocker.locker_number);
        System.out.println("Occupied?:" +targetLocker.isOccupied());
        System.out.println("changingOut: " + targetLocker.isChangingOut(time.getCurrentTime()));
        System.out.println("encounter?" + !encounterOnEnter);
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
                    // System.out.println("ENCOUNTER ENTER");
                }
            }
            if(!encounterOnExit) {
                if ((dIn1 <= tOut1 && dIn2 >= tOut1 && dIn2 <= tOut2) ||
                        (dIn1 >= tOut1 && dIn1 <= tOut2 && dIn2 >= tOut2) ||
                        (dOut1 <= tOut1 && dOut2 >= tOut1 && dOut2 <= tOut2) ||
                        (dOut1 >= tOut1 && dOut1 <= tOut2 && dOut2 >= tOut2)) {
                    //System.out.println("ENCOUNTER EXIT");
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

    /**
     * Simulates the whole Environment/Scenario
     */
    public void simulate() {
        while (time.getCurrentTime() < time.getDayTime()) {
            int i = 0;
            routine();
            time.timeInterval();
           /* for(Locker l: lockerList){
               if(l.isOccupied()){
                   i++;
               }
            }
            if(time.getCurrentTime() == a){
               // System.out.println("LOCKER BELEGT: " + i);
                a += 3600;
            }*/
            // System.out.println("ENCOUNTERS: " + vipEncounters);
        }
        //TODO MUSS WIEDER REIN
        //statistics.saveData(simulationDay, sendHome);
        //  System.out.println("ENCOUNTERS TODAY: "+ vipEncounters);
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
        /*if (!checkForVisitor()) {
            updateLockers();
            return;
        }
        checkForFocusPerson();
        assignLocker();
        if (targetLocker != null && !vipLeft) {
            updateNeighbourList();
            if ((time.getCurrentTime() >= (targetLocker.changeOnArrival - timeToChange)) && (time.getCurrentTime() <= targetLocker.changeOnArrival)) {
                vipEncounters = encounter();
            } else if ((time.getCurrentTime() >= targetLocker.changeOnDeparture) && (time.getCurrentTime() <= (targetLocker.changeOnDeparture + timeToChange))) {
                vipEncounters = encounter();
            }
        }
        updateLockers();*/
    }
}