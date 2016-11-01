package praktikum_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * CommandLine Arguments:
 * index 0: amount of hours to simulate a day                   // we used 10
 * index 1: hours after opening til focus arrives               // we used 5
 * index 2: time window to look for the focusPerson &&          // we used 300
 * also the timewindow a customer is changing in
 * the lockerroom in seconds
 * example: focusPerson arrives roughly 5h after openHours so
 * the program checks for focusperson between
 * (arrival - timewindow) and (arrival + timewindow)
 * index 3: number of days for the simulation                   // we used 10
 * index 4: amount of lockers in the studio                     // we used 150
 * index 5: probability that a person arrives at the gym        // we used 0.1
 */
public class Test {
    public static void main(String[] args) throws IOException {
        long openingHours = Long.parseLong(args[0]);
        long arrival = Long.parseLong(args[1]);
        long timewindow = Long.parseLong(args[2]);
        int daysOfSimulation = Integer.parseInt(args[3]);
        int lockerAmount = Integer.parseInt(args[4]);
        double guestProbability = Double.parseDouble(args[5]);
        Map<String, String> map = new HashMap<>();
        Map<Float, Long> percentageMap = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader("res/Belegungszeiten.txt"));
        String line = "";
        DevelopingEnvironment environment;

        int total = 0;
        int dummy = 0;
        List<String> mapKeys;

        while ((line = in.readLine()) != null) {
            if (!line.startsWith("#")) {
                String parts[] = line.split(" ");
                map.put(parts[0], parts[1]);
                total += Integer.parseInt(parts[1]);
            }
        }
        mapKeys = new ArrayList<>(map.keySet());
        float percentageValue = 0.0f;
        while (dummy < map.size()) {
            float floatDummy = Float.parseFloat(map.get(mapKeys.get(dummy))) / (float) total;
            percentageValue += floatDummy;
            percentageMap.put(percentageValue, Long.parseLong(mapKeys.get(dummy)) * 60);  // value is multiplied with 60 to convert the minutes into seconds
            dummy++;
        }
        in.close();

        for (int i = 0; i < daysOfSimulation; i++) {
            System.out.println("--- LETS START THE SIMULATION ---\n");
            environment = new DevelopingEnvironment(lockerAmount, (i + 1), openingHours, arrival, timewindow, percentageMap, guestProbability);
            environment.simulate();
        }
    }
}