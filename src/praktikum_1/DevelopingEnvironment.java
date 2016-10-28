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
    int openingHours;
    //time the studio closes
    int closingTime;

    int currentTime;

    boolean focusPersonArrived;


    public int getLockerAmount(){
        return 0;
    }
    /**
     * Counts the encounter of our
     * @return
     */
    public int encounter(){
        return 0;
    }

    /**
     * Initializes all Locker Objects and set their neighbours
     * finds its neighbours
     */
    private void init(){
        lockers = new LinkedList<Locker>();
        Locker dummyLocker;
        focusPersonArrived = false;

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
        if(currentTime > 1445){
            return focusPersonArrived = true;
        }
        else return focusPersonArrived = false;
    }
}