package praktikum_1;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sheraz on 27.10.16.
 */

public class Locker {

    int locker_number;
    boolean occupied = false;
    boolean isFocusPerson = false;
    long change_In; //eig Time oder sowas
    long change_Out; //eig Time oder sowas
    long duration; //eig Time oder sowas
    List<Integer> neighbours;


    public Locker(int locker_number, long change_In, long change_Out, long duration, List<Integer> neighbours) {
        this.locker_number = locker_number;
        this.occupied = false;
        this.isFocusPerson = false;
        this.change_In = change_In;
        this.change_Out = change_Out;
        this.duration = duration;
        this.neighbours = neighbours;
    }

    public int getLockerNumber() {
        return this.locker_number;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public boolean isFocusPerson() {
        return this.isFocusPerson;
    }

    public long getChange_In() {
        return this.change_In;
    }

    public long getChange_Out() {
        return change_Out;
    }

    public long getDuration() {
        return duration;
    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }

    public void setLocker_number(int locker_number) {
        this.locker_number = locker_number;
    }

    public void setChange_In(long change_In) {
        this.change_In = change_In;
    }

    public void setChange_Out(long change_Out) {
        this.change_Out = change_Out;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setFocusPerson(boolean focusPerson) {
        isFocusPerson = focusPerson;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void setNeighbours(int locker_number, int amountOfLockers) {
        List<Integer> n = new LinkedList<Integer>();

        //checks if the Locker has 5 neighbours
        if (locker_number >= 2 && locker_number <= amountOfLockers - 2) {
            n.add(locker_number - 2);
            n.add(locker_number - 1);
            n.add(locker_number + 1);
            n.add(locker_number + 2);
            n.add(locker_number + 3);
        }
        //checks if the Locker is at the beginning
        else if (locker_number < 3) {
            n.add(locker_number + 1);
            n.add(locker_number + 2);
            n.add(locker_number + 3);
        }
        //checks if the Locker is at the end
        else if (locker_number > amountOfLockers - 2) {
            n.add(locker_number - 3);
            n.add(locker_number - 2);
            n.add(locker_number - 1);
        }

        //TODO was wenn Lockcer number nicht existiert

        this.neighbours = n;
    }

    public void releaseLocker() {
        this.occupied = false;
        this.isFocusPerson = false;
    }

    public void setLocker() {
        this.occupied = true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.locker_number + "");
        builder.append(this.occupied + "");
        builder.append(this.isFocusPerson + "");
        builder.append(this.change_In + "");
        builder.append(this.change_Out + "");
        builder.append(this.duration + "");
        builder.append(this.neighbours + "");
        return builder.toString();
    }
}

/**
 * LOCKER STRUKTUR BEI 20 LOCKER SIEHT WIE FOLGT AUS
 * <p>
 * 1 3 5 7  9 11 13 16 17 19
 * 2 4 6 8 10 12 14 16 18 20
 **/