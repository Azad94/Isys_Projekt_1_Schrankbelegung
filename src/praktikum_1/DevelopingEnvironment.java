package praktikum_1;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * Created by Sheraz on 27.10.16.
 */
public class DevelopingEnvironment {

    int lockerAmount = getLockerAmount();
    List<Locker> lockers;
    List<Integer> occupiedNeighbours;
    List<Integer> freeNeighbours;

    //time the studio opens
    long openingHours;
    //time the studio closes
    long closingTime;

    long currentTime;

    //tells if the focus person is in the studio
    boolean focusPersonArrived;

    //tells how many encouters the focus person had
    int totalEncounters;

    Locker dummyLocker;
    //Locker of the focus person
    Locker targetLocker;


    public int getLockerAmount(){
        //TODO IMPLEMENTIEREN
        return 0;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }


    public int encounter(Locker focusLocker){
        for(int i = 0; i < occupiedNeighbours.size() - 1; i++){
            dummyLocker.setLocker_number(occupiedNeighbours.get(i));
            if(dummyLocker.change_In >= focusLocker.change_In - 300
                    && dummyLocker.change_Out <= focusLocker.change_In){
                totalEncounters++;
            }else if(dummyLocker.change_Out >= focusLocker.change_Out - 300
                    && dummyLocker.change_Out <= focusLocker.change_Out){
                totalEncounters++;
            }
        }
        dummyLocker = null;
        return totalEncounters;
    }

    public void updateNeighbourList(Locker focusLocker){
        for(int i = 0; i < focusLocker.neighbours.size()-1; i++){
            dummyLocker.setLocker_number(focusLocker.neighbours.get(i));
            if(dummyLocker.isOccupied())
                occupiedNeighbours.add(dummyLocker.getLockerNumber());
            else
                freeNeighbours.add(dummyLocker.getLockerNumber());
        }
    }

    private void init(){
        lockers = new LinkedList<Locker>();
        focusPersonArrived = false;
        totalEncounters = 0;
        openingHours = 10;
        closingTime = 22;

        for(int i = 0; i < lockerAmount; i++){
            lockers.add(i, dummyLocker = new Locker(i,0, 0, 0, null));
            dummyLocker.setNeighbours(i, lockerAmount);
        }

    }

    public boolean checkForFocusPerson(){

        //TODO die Zeit muss hier angepasst werden auf den richtigen Datentyp
        if(getCurrentTime() > 1445){
            return focusPersonArrived = true;
        }
        else return focusPersonArrived = false;
    }

    //TODO starten des Taktes
    public void start(){
        setCurrentTime(currentTime + 10);
    }

    //TODO enden des Taktes
    public void end(){

    }

    public void simulate(){

        init();
        start();

        do{
            //TODO Implementieren
        }
        while(currentTime != closingTime);

        end();
        frequencyScale();
    }

    public void frequencyScale(){
        //TODO IMPLEMENTIEREN Häufigkeitsverteilung
    }

    public void assignLocker(){
        dummyLocker.setLocker_number(randomLockerNumber());
        long duration = getDurationOfStay();
        dummyLocker.setChange_In(getCurrentTime() + 300);
        dummyLocker.setChange_Out(duration - 300);
        dummyLocker.setDuration(duration);
        dummyLocker.setNeighbours(dummyLocker.getLockerNumber(), lockerAmount);
        if(focusPersonArrived) targetLocker.setLocker_number(dummyLocker.getLockerNumber());
    }

    public long getDurationOfStay(){
        return 1;
    }

    public void checkForCustom(){
        //TODO Implementieren
    }

    public void timeInterval(){
        //TODO Implementieren
    }

    public int randomLockerNumber(){
        int lockerNumber;
        Random r = new Random();

        while(true){
            lockerNumber = r.nextInt((lockerAmount) + 1);
            dummyLocker.setLocker_number(lockerNumber);
            if(!dummyLocker.isOccupied()) return lockerNumber;
        }
    }
}