package praktikum_1;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DevelopingEnvironment {

    private int lockerAmount = getLockerAmount();
    private List<Locker> lockers;
    private List<Integer> occupiedNeighbours;
    private List<Integer> freeNeighbours;
    //time the studio opens
    private long openingHours;
    //time the studio closes
    private long closingTime;
    private long currentTime;
    private long timeOfArrivalOfFocusPerson;
    //tells if the focus person is in the studio
    private boolean focusPersonArrived;
    private boolean focusPersonLeft;
    //tells how many encouters the focus person had
    private int totalEncounters;
    private Locker dummyLocker;
    //Locker of the focus person
    private Locker targetLocker;

    public int encounter(Locker focusLocker) {
        updateNeighbourList(focusLocker);

        for (int i = 0; i < occupiedNeighbours.size() - 1; i++) {
            dummyLocker.setLocker_number(occupiedNeighbours.get(i));
            if (dummyLocker.change_In >= focusLocker.change_In - 300
                    && dummyLocker.change_Out <= focusLocker.change_In) {
                totalEncounters++;
            } else if (dummyLocker.change_Out >= focusLocker.change_Out - 300
                    && dummyLocker.change_Out <= focusLocker.change_Out) {
                totalEncounters++;
            }
        }
        dummyLocker = null;
        return totalEncounters;
    }

    private void updateNeighbourList(Locker focusLocker) {
        occupiedNeighbours = new LinkedList<>();
        freeNeighbours = new LinkedList<>();
        for (int i = 0; i < focusLocker.neighbours.size() - 1; i++) {
            dummyLocker.setLocker_number(focusLocker.neighbours.get(i));
            if (dummyLocker.isOccupied())
                occupiedNeighbours.add(dummyLocker.getLockerNumber());
            else
                freeNeighbours.add(dummyLocker.getLockerNumber());
        }
    }

    private void init() {
        lockers = new LinkedList<>();
        focusPersonArrived = false;
        focusPersonLeft = false;
        totalEncounters = 0;
        openingHours = 10;
        closingTime = 22;
        timeOfArrivalOfFocusPerson = 15;
        targetLocker = new Locker(0,0,0,0,null);

        for (int i = 0; i < lockerAmount; i++) {
            lockers.add(i, dummyLocker = new Locker(i, 0, 0, 0, null));
            dummyLocker.setNeighbours(i, lockerAmount);
        }
    }

    public void assignLocker() {
        dummyLocker.setLocker_number(randomLockerNumber());
        long duration = getRandomDuration();
        dummyLocker.setOccupied(true);
        if (focusPersonArrived) {
            targetLocker.setLocker_number(dummyLocker.getLockerNumber());
            dummyLocker.setFocusPerson(true);
        }
        else dummyLocker.setFocusPerson(false);
        dummyLocker.setChange_In(getCurrentTime() + 300);
        dummyLocker.setChange_Out(duration - 300);
        dummyLocker.setDuration(duration);
        lockers.set(dummyLocker.getLockerNumber(), dummyLocker);
    }

    private void freeLocker(Locker locker){
        for(int i = 0; i < occupiedNeighbours.size() - 1; i++){
            if(locker.getLockerNumber() == occupiedNeighbours.get(i))
                updateNeighbourList(targetLocker);
        }

        if(focusPersonArrived && locker.getLockerNumber()
                == targetLocker.getLockerNumber())
            focusPersonLeft = true;
        locker.setOccupied(false);
        locker.setFocusPerson(false);
        locker.setChange_In(0);
        locker.setChange_Out(0);
        locker.setDuration(0);
        lockers.set(locker.getLockerNumber(), locker);
    }

    private void updateLockers(){
        for(int i = 0; i < lockerAmount; i++){
            dummyLocker.setLocker_number(i);
            if (timeUp(dummyLocker)){
                freeLocker(dummyLocker);
            }
        }
    }

    private boolean timeUp(Locker locker){
        return locker.change_Out <= currentTime;
    }

    public boolean checkForVisitor() {
        double probability = Math.random();
        return probability <= 0.1;
    }


    private int randomLockerNumber() {
        int lockerNumber;
        Random r = new Random();

        while (true) {
            lockerNumber = r.nextInt((lockerAmount) + 1);
            dummyLocker.setLocker_number(lockerNumber);
            if (!dummyLocker.isOccupied()) return lockerNumber;
        }
    }

    public boolean checkForFocusPerson() {

        //TODO focusPersonArrived ist in INIT mit false intialisiert muss ich hier also auf true prüfen oder trotzdem auf false prüfen?
        if (getCurrentTime() > timeOfArrivalOfFocusPerson && !focusPersonArrived) {
            return focusPersonArrived = true;
        } else return focusPersonArrived = false;
    }


    public void simulate() {

        //TODO einfach so reingeschrieben erstmal damit da nicht überall steht never used
        init();
        start();
        encounter(targetLocker);
        checkForFocusPerson();
        checkForVisitor();
        updateLockers();
        assignLocker();
        updateCurrentTime(0);

        do {
            //TODO Implementieren
        }
        while (currentTime != closingTime);

        end();
        frequencyScale();
    }


    private void start() {
        //TODO Implementieren
    }

    private void end() {
        //TODO Implementieren
    }

    private long getRandomDuration() {
        //TODO Implementieren
        return 1;
    }

    private int getLockerAmount() {
        //TODO IMPLEMENTIEREN
        return 0;
    }

    private long getCurrentTime() {
        //TODO Implementieren
        return currentTime;
    }

    private void updateCurrentTime(long currentTime) {
        //TODO Implementieren
        if(currentTime > openingHours && currentTime < closingTime)
        this.currentTime = currentTime;
    }

    private void frequencyScale() {
        //TODO IMPLEMENTIEREN Häufigkeitsverteilung
    }
}