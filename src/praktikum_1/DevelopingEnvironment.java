package praktikum_1;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by Sheraz on 27.10.16.
 */
public class DevelopingEnvironment {

    int lockerAmount = getLockerAmount();
    List<Locker> lockers;

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


    /**
     * gets the Amount of Lockers
     * which are given as param
     * @return Amount of Lockers
     */
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

    /**
     * Counts the encounter of our
     * @return
     */
    public int encounter(){
        //TODO IMPLEMENTIEREN
        return 0;
    }

    /**
     * Initializes all Locker Objects and set their neighbours
     * finds its neighbours
     */
    private void init(){
        lockers = new LinkedList<Locker>();
        focusPersonArrived = false;
        totalEncounters = 0;

        //TODO MALTE
        //TODO die zeiten festelegenich hab das noch nivht verstanden was Jonas meinte
        //TODO ich kann aber natürlich nach fragen wenn du das nicht machen möchtest
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

        while(!focusPersonArrived){
            //TODO IMPLEMENTIEREN
        }

        while(focusPersonArrived){
            //TODO IMPLEMENTIEREN
            encounter();
        }
        end();
        frequencyScale();
    }

    public void frequencyScale(){
        //TODO IMPLEMENTIEREN Häufigkeitsverteilung
    }

    public void assignLocker(){
        //TODO Implementieren
        dummyLocker.setLocker();
    }
}