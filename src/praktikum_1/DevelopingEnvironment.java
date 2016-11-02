package praktikum_1;

import java.util.*;

public class DevelopingEnvironment {

    //Amount of Lockers intialized haben
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
    Locker l1,l2,l3,l4,l5,l6,l7,l8,l9,l10;
    List<Locker> lockerss = new LinkedList<>();


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
    //    System.out.println("EXPECTED TIME FOR FOCUSPERSON: " + timeOfArrivalOfFocusPerson);
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
        long duration = getRandomDuration();
        dailyAmount = s.getMap().get(t.inMin(duration));
        //System.out.println(dailyAmount);
        dailyAmount++;
        s.getMap().replace(t.inMin(duration), dailyAmount);
        l.setOccupied(true);
        l.setChange_In(t.getCurrentTime() + timeWindow);
        l.setChange_Out(t.getCurrentTime() + duration - timeWindow);
        l.setDuration(duration);
        lockers.set(l.getLockerNumber(), l);
        if (focusPersonArrived && targetLocker == null && !focusPersonLeft) {
            targetLocker = l;
            focusLockerAssigned = true;
    //        System.out.println("FocusPerson arrived his locker number is ---------" + targetLocker.getLockerNumber());
        }
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
    //    System.out.println("GUESTTIME: " + guestTime);
        return guestTime;
    }


    private void freeLocker(int lockerNr){
        Locker l;
        if(occupiedNeighbours.size() != 0){
            for(int i = 0; i < occupiedNeighbours.size() - 1; i++){
                if(lockerNr == occupiedNeighbours.get(i))
                    updateNeighbourList();
            }
        }

        if(targetLocker != null && targetLocker.getLockerNumber() == lockerNr){
    //        System.out.println("FOCUS PERSON IS NOW GONE...");
            focusPersonLeft = true;
        }

        //l = lockers.get(lockerNr);
        l = lockerss.get(lockerNr);
        l.releaseLocker();
        //lockers.set(lockerNr, l);
        lockerss.set(lockerNr, l);
    }

    /**
     * Checks if the duration of a Locker to be occupied
     * is up and frees it if so
     */
    private void updateLockers() {
        Locker l;
        for(int i = 0; i < lockerAmount; i++) {
            //l = lockers.get(i);
            l = lockerss.get(i);
            if (l.isOccupied() && timeUp(l.getLockerNumber())){
    //            System.out.println("------------------------------\nES WIRD JETZT FREI GESETZT" + l.getLockerNumber()+ "------------------------------\n");
                freeLocker(l.getLockerNumber());
            }
        }
    }

    private boolean timeUp(int lockerNr) {
        //Locker l = lockers.get(lockerNr);
        Locker l = lockerss.get(lockerNr);
        return l.change_Out < t.getCurrentTime();
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
            //l = lockers.get(lockerNumber);
            l = lockerss.get(lockerNumber);
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
    //        System.out.println("AKTUELLE ZEIT " + t.getCurrentTime());
    //        System.out.println("ARRIVAL ZEIT " + timeOfArrivalOfFocusPerson);
        }

    }

    public void updateNeighbourList(){
        Locker dummyLocker;

        occupiedNeighbours.clear();
        freeNeighbours.clear();

        for(int i = 0; i < targetLocker.neighbours.size(); i++){
            //dummyLocker = lockers.get(targetLocker.neighbours.get(i));
            dummyLocker = lockerss.get(targetLocker.neighbours.get(i));
            if (dummyLocker.isOccupied()) {
                occupiedNeighbours.add(dummyLocker.getLockerNumber());
    //            System.out.println("Besetzte Neighbours sind jetzt\n" + occupiedNeighbours.toString());
            }
            else
            {
                freeNeighbours.add(dummyLocker.getLockerNumber());
    //            System.out.println("Free Neighbours sind jetzt\n" + freeNeighbours.toString());
            }
        }
    }

    public int encounter(){
        Locker dummy;

        System.out.println("BESETZTE NACHBARN " + occupiedNeighbours.toString());
        for(int i = 0; i < occupiedNeighbours.size(); i++){
            //dummy = lockers.get(occupiedNeighbours.get(i));
            dummy = lockerss.get(occupiedNeighbours.get(i));
            //subtracting the time of change
            if(dummy.change_In >= targetLocker.change_In - timeWindow && dummy.change_In <= targetLocker.change_In){
                totalEncounters++;
                System.out.println("ENCOUNTERED NEIGHBOUR IN CHANGE IN " + dummy.getLockerNumber() + " und " + totalEncounters);
            }
            //subtracting the time of change
            if(dummy.change_Out >= targetLocker.change_Out && dummy.change_Out <= targetLocker.change_Out + timeWindow){
                totalEncounters++;
                System.out.println("ENCOUNTERED NEIGHBOUR IN CHANGE OUT" + dummy.getLockerNumber()+ " und " + totalEncounters);
            }

        }
        return totalEncounters;
    }

    /**
     * Initializes all parameters for the Simulation
     */
    public void init() {
    //    System.out.println("- INTIALIZING ...");
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
/*        for (int i = 0; i < lockerAmount; i++) {
            dummyLocker = new Locker(i, false, 0, 0, 0, null);
            dummyLocker.setNeighbours(dummyLocker.getLockerNumber(), lockerAmount);
            lockers.add(i, dummyLocker);
        }
*/
        //0
        lockerss.add(0, l1 = new Locker(0,true, 20, 40, 60, null));
        l1.setNeighbours(0, 10);

        //1
        lockerss.add(1, l2 = new Locker(1,true, 20, 40, 60, null));
        l2.setNeighbours(1, 10);

        //2
        lockerss.add(2, l3 = new Locker(2,true, 60, 90, 70, null));
        l3.setNeighbours(2, 10);

        //3
        lockerss.add(3, l4 = new Locker(3,true, 40, 80, 80, null));
        l4.setNeighbours(3, 10);

        //4
        lockerss.add(4, l5 = new Locker(4,true, 50, 90, 80, null));
        l5.setNeighbours(4, 10);

        //5
        lockerss.add(5, l6 = new Locker(5,true, 70, 120, 90, null));
        l6.setNeighbours(5, 10);

        //6
        lockerss.add(6, l7 = new Locker(6,true, 100, 130, 70, null));
        l7.setNeighbours(6, 10);

        //7
        lockerss.add(7, l8 = new Locker(7,true, 20, 110, 130, null));
        l8.setNeighbours(7, 10);

        //8
        lockerss.add(8, l9 = new Locker(8,true, 90, 150, 100, null));
        l9.setNeighbours(8, 10);

        //09
        lockerss.add(9, l10 = new Locker(9,true, 130, 160, 70, null));
        l10.setNeighbours(9, 10);

        targetLocker = lockerss.get(l6.getLockerNumber());
    }

    /**
     * Simulates the whole Environment/Scenario
     */
    public void simulate() {
     //   System.out.println("- ENTER simulate()\n");
        while (t.currentTime < t.time) {
            routine();
            t.timeInterval();
        }
    //    System.out.println("ENDING DAY");
    //    System.out.println("DAAAAYYY: " + simulationDay);
        s.saveData(simulationDay);
        System.out.println("ENCOUNTERS TODAY: "+totalEncounters + " \n");
    }
    int i = 0;
    /**
     * Settles the Procedure of the Simulation
     */
    private void routine() {
    //    System.out.println("- ENTER routine()");
/*        if (!checkForVisitor()) {
            updateLockers();
    //        System.out.println("- No customer is coming ...\n");
            return;
        }
    //    System.out.println("- Customer is coming ...\n");
        checkForFocusPerson();
        assignLocker();
*/        if(targetLocker != null && !focusPersonLeft) {
            updateNeighbourList();
            totalEncounters = encounter();
        }
        updateLockers();

        if(targetLocker!=null && !focusPersonLeft && i==0){
            System.out.println("----------------- END OF ROTUINE LOOP ---------");
            System.out.println("**********TARGETLOCKER DETAILS**********");
            System.out.println(targetLocker.toString()+"\n");
            System.out.println("DIE BESETZTEN NACHABRN" + occupiedNeighbours.toString());
            System.out.println("DIE FREIEN NACHABRN" + freeNeighbours.toString()+"\n\n");
            i++;
        }
    }
}