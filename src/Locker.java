/**
 * Created by Sheraz on 27.10.16.
 */
public class Locker {

    int locker_number;
    boolean occupied = false;

    public Locker(int locker_number, boolean occupied) {
        this.locker_number = locker_number;
        this.occupied = occupied;
    }

    int change_In; //eig Time oder sowas
    int change_Out; //eig Time oder sowas
    int duration; //eig Time oder sowas

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.locker_number+"");
        builder.append(this.occupied+"");
        return  builder.toString();
    }
}