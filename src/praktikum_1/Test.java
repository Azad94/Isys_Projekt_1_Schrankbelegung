package praktikum_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * CommandLine Arguments:
 * index 0: amount of hours to simulate a day
 *          the arrival of the focusPerson is set to half the amount of the open hours
 * index 1: number of days for the simulation
 * index 2: amount of lockers in the studio
 */
public class Test {
    public static void main(String[] args) throws IOException {
        long openingHours = Long.parseLong(args[0]);
        long arrival = openingHours/2l;
        Map<String, String> map = new HashMap<>();
        Map<Float, Long> percentageMap = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader("res/Belegungszeiten.txt"));
        String line = "";
        DevelopingEnvironment environment;
        //TODO parametrisierbar machen
        int daysOfSimulation = Integer.parseInt(args[1]);
        //TODO parametrisierbar machen
        int lockerAmount = Integer.parseInt(args[2]);

        int total = 0;
        int dummy = 0;
        List<String> mapKeys;

        while ((line = in.readLine()) != null) {
            if(!line.startsWith("#")) {
                String parts[] = line.split(" ");
                map.put(parts[0], parts[1]);
                total += Integer.parseInt(parts[1]);
            }
        }
        mapKeys = new ArrayList<>(map.keySet());
        float percentageValue = 0.0f;
        while(dummy<map.size()){
            float floatDummy = Float.parseFloat(map.get(mapKeys.get(dummy)))/(float)total;
            percentageValue += floatDummy;
            percentageMap.put(percentageValue, Long.parseLong(mapKeys.get(dummy))*60);  // value is multiplied with 60 to convert the minutes into seconds
            dummy++;
        }
        in.close();

        for(int i = 0; i <= daysOfSimulation-1;i++){
            System.out.println("--- LETS START THE SIMULATION ---\n");
            environment = new DevelopingEnvironment(lockerAmount, daysOfSimulation, openingHours, arrival, percentageMap);
            environment.simulate();
            i++;

        }
    }
}