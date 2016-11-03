package praktikum_1;

import java.util.LinkedList;
import java.util.List;


/**
 * Implements the class Locker, that represents
 * each Locker in a Fitness Studio, which is used
 * for Software developing purposes
 */
public class Locker {

    int locker_number;
    boolean occupied = false;
    long changeOnArrival;
    long changeOnDeparture;
    long duration;
    List<Integer> neighbours;


    /**
     * @param locker_number number of this Locker
     * @param occupied      shows if this Locker is in use
     * @param changeOnArrival     the time he'll be finished changing at the beginning
     * @param changeOnDeparture    the time he'll be finished changing at the end
     * @param duration      time this locker is occupied
     * @param neighbours    list of neighbours from this locker
     */
    public Locker(int locker_number, boolean occupied, long changeOnArrival, long changeOnDeparture, long duration, List<Integer> neighbours) {
        this.locker_number = locker_number;
        this.occupied = occupied;
        this.changeOnArrival = changeOnArrival;
        this.changeOnDeparture = changeOnDeparture;
        this.duration = duration;
        this.neighbours = neighbours;
    }

    /**
     * @return locker number
     */
    public int getLockerNumber() {
        return this.locker_number;
    }

    /**
     * @return true if this Locker is occupied
     */
    public boolean isOccupied() {
        return this.occupied;
    }

    /**
     * @param changeOnArrival change in time to set
     */
    public void setChangeOnArrival(long changeOnArrival) {
        this.changeOnArrival = changeOnArrival;
    }

    /**
     * @param changeOnDeparture change out time to set
     */
    public void setChangeOnDeparture(long changeOnDeparture) {
        this.changeOnDeparture = changeOnDeparture;
    }

    /**
     * @param duration duration time to set
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * @param occupied occupied status to set
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    /**
     * Searches for the neighbours of the Locker from the Focus Person.
     * Regarding which Locker the Focus Person has it's neighbours
     * are either three or five.
     *
     * @param locker_number   number of the locker which neighbours are supposed to be found
     * @param amountOfLockers number of lockers initialized
     */
    public void setNeighbours(int locker_number, int amountOfLockers) {
        List<Integer> neighbours = new LinkedList<>();
        //checks if the Locker has 5 neighbours
        if (locker_number >= 2 && locker_number <= amountOfLockers - 3) {
            if (locker_number % 2 == 0) {
                neighbours.add(locker_number - 2);
                neighbours.add(locker_number - 1);
                neighbours.add(locker_number + 1);
                neighbours.add(locker_number + 2);
                neighbours.add(locker_number + 3);
            } else {
                neighbours.add(locker_number - 3);
                neighbours.add(locker_number - 2);
                neighbours.add(locker_number - 1);
                neighbours.add(locker_number + 1);
                neighbours.add(locker_number + 2);
            }
        }
        //checks if the Locker is at the beginning
        else if (locker_number < 3) {
            if (locker_number == 0) {
                neighbours.add(locker_number + 1);
                neighbours.add(locker_number + 2);
                neighbours.add(locker_number + 3);
            } else {
                neighbours.add(locker_number - 1);
                neighbours.add(locker_number + 1);
                neighbours.add(locker_number + 2);
            }
        }
        //checks if the Locker is at the end
        else if (locker_number > amountOfLockers - 3) {
            if (locker_number == amountOfLockers - 2) {
                neighbours.add(locker_number - 2);
                neighbours.add(locker_number - 1);
                neighbours.add(locker_number + 1);
            }
            if (locker_number == amountOfLockers - 1) {
                neighbours.add(locker_number - 3);
                neighbours.add(locker_number - 2);
                neighbours.add(locker_number - 1);
            }

        }
        this.neighbours = neighbours;
    }

    /**
     * sets the Locker to its default values
     */
    public void releaseLocker() {
        this.occupied = false;
        this.changeOnArrival = 0;
        this.changeOnDeparture = 0;
        this.duration = 0;
    }

    /**
     * @return String representation of a Locker
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Locker Number : " + this.locker_number + "\n");
        builder.append("Occupied : " + this.occupied + "\n");
        builder.append("Change In: " + this.changeOnArrival + "\n");
        builder.append("Change Out : " + this.changeOnDeparture + "\n");
        builder.append("Duration : " + this.duration + "\n");
        builder.append("Neighbours : " + this.neighbours + "\n");
        return builder.toString();
    }
}