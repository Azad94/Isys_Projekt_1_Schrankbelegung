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
    Statistics s = new Statistics();
    Map<Float, String> probabilityMap;
    List<Float> percentageArray;
    /**
     * Initializes all Lockers and sets all default values
     */
    public DevelopingEnvironment(int lockerAmount, int simulationDay, Long day, Long arrival, Map<Float, String> percentageMap){
        this.lockerAmount = lockerAmount;
        this.simulationDay = simulationDay;
        this.openingHours = day;
        this.t = new Time(openingHours);

        this.closingTime = t.time;
        this.timeOfArrivalOfFocusPerson = t.inSec(arrival);
        //TODO Fehler vom mergen ?
        this.probabilityMap = percentageMap;
        //keys(wahrscheinlichkeiten eine bestimmte zeit zu bleiben) der map als liste und die sortiert um besser vergleichen zu können
        this.percentageArray = new ArrayList<>(probabilityMap.keySet());
        Collections.sort(percentageArray);
        init();
    }

    //TODO das auskommentierte muss alles mitrein
    /**
     * Assignes a random locker to a Person
     */
    public void assignLocker() {
        int number = randomLockerNumber();
        dummyLocker = lockers.get(number);
        dummyLocker.setLocker_number(randomLockerNumber());
        //TODO MUSS WIEDER EINGEFÜGT WERDEN
        //long duration = s.getRandomDuration();
        dummyLocker.setOccupied(true);
        if (focusPersonArrived) {
            targetLocker.setLocker_number(dummyLocker.getLockerNumber());
            dummyLocker.updateNeighbourList(dummyLocker, occupiedNeighbours,freeNeighbours);
            focusLockerAssigned = true;
        }
        System.out.println("FOCUS PERSON ARRIVED: " + focusPersonArrived);
        dummyLocker.setChange_In(t.getCurrentTime() + 300);
        //dummyLocker.setChange_Out(duration - 300);
        //dummyLocker.setDuration(duration);

        //TODO das kann dann raus
        dummyLocker.setNeighbours(dummyLocker.getLockerNumber(), lockerAmount);

        //TODO hier war eine array out of Bound siehe screenshot
        lockers.set(dummyLocker.getLockerNumber(), dummyLocker);
        //s.updateDurationFrequency(duration);
        System.out.println("\nASSIGNED LOCKER IS " + dummyLocker.toString());
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
        System.out.println("random float: " + rndFloat);
        float compare = 0.0f;
        for(int q = 0; q<percentageArray.size();q++){

            //  (rndFloat <= percentageArray.get(q)) ? System.out.println(percentageMap.get(percentageArray.get(q))) :
            if(rndFloat<=percentageArray.get(q) && rndFloat>compare){
               guestTime =  Long.parseLong(probabilityMap.get(percentageArray.get(q)));
            }
            compare = percentageArray.get(q);
        }
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
        return probability <= 1;
    }

    /**
     *
     * @return random number of Locker to be assigned
     */
    private int randomLockerNumber() {
        int lockerNumber;
        Random r = new Random();
        dummyLocker = new Locker(0, false, 0, 0, 0, null);

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
            System.out.println("FOCUS PERSON INCOMING");
        } else  {
            focusPersonArrived = false;
            System.out.println("FOCUS PERSON NOT COMING YET");
        }
    }

    /**
     * Initializes all parameters for the Simulation
     */
    public void init() {
        System.out.println("INTIALIZING ...");
        lockers = new LinkedList<>();
        occupiedNeighbours = new LinkedList<>();
        freeNeighbours = new LinkedList<>();
        focusPersonArrived = false;
        focusLockerAssigned = false;
        focusPersonLeft = false;
        totalEncounters = 0;
        targetLocker = new Locker(0, false, 0, 0, 0, null);

       for (int i = 0; i < lockerAmount; i++) {
           dummyLocker = new Locker(i, false, 0, 0, 0, null);
           dummyLocker.setNeighbours(dummyLocker.getLockerNumber(), lockerAmount);
           lockers.add(i, dummyLocker);
           System.out.println("LOCKER "+ dummyLocker.getLockerNumber()+ " ERSTELLT MIT DEN NACHBARN " + dummyLocker.neighbours.toString());
       }

       dummyLocker.releaseLocker();
/**
        for (int i = 0; i < lockers.size(); i++) {
            if(dummyLocker.neighbours == null){
                System.out.println("NR -- " + i + " ist leer");
            }
        }
         **/
    }

    /**
     * Simulates the whole Environment/Scenario
     */
    public void simulate() {
        //TODO hier war eine array out of Bound siehe screenshot
        System.out.println("ENTER SIMULATE");
        routine();
        t.timeInterval();
        //TODO wenn de Tag vorbei ist soll diese Methode aufgerufen werden
        s.saveData(simulationDay);
    }

    /**
     * Settles the Procedure of the Simulation
     */
    private void routine(){
        System.out.println("ENTER ROUTINE");
        System.out.println("DER FOCUS LOCKER " + targetLocker.toString());
        System.out.println("OCCUPIED NEIGHBOURS " + occupiedNeighbours.toString());
        System.out.println("FREE NEIGHBOURS" + freeNeighbours.toString());
        if(!checkForVisitor()){
            updateLockers();
            System.out.println("NO ONE IS COMING");
            return;
        }
        System.out.println("INCOMING PERSON");
        //TODO hier war eine array out of Bound siehe screenshot
        checkForFocusPerson();
        assignLocker();
        if(focusPersonLeft)
            totalEncounters = targetLocker.encounter(targetLocker, occupiedNeighbours,freeNeighbours);
        updateLockers();
    }

    /**
     * Resets necessary parameters
     *
    public void reset(){
        //save statistics before reset
        s.saveData(simulationDay);
        lockers.clear();
        lockers = null;
        targetLocker = null;
    }*/
}