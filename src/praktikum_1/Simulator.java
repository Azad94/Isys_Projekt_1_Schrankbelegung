package praktikum_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * CommandLine Arguments:
 * index 0: amount of hours to simulate a day
 * index 1: hour the VIP arrives
 * index 2: time a visitor changes
 * index 3: number of days for the simulation
 * index 4: amount of lockers in the studio
 * index 5: probability that a Visitor arrives at the gym
 * index 6: true if random distribution
 * index 7; true if youn want to print the daily duration frequency distribution
 */
public class Simulator {
    public static void main(String[] args) throws IOException {

        Statistics statistics = new Statistics();

        long openingHours = Long.parseLong(args[0]);
        long vipArrivalTime = Long.parseLong(args[1]);
        long timeToChange = Long.parseLong(args[2]);
        int daysOfSimulation = Integer.parseInt(args[3]);
        int lockerAmount = Integer.parseInt(args[4]);
        double arrivalProbability = Double.parseDouble(args[5]);
        boolean withRandom = Boolean.valueOf(args[6]);
        boolean dailyStats = Boolean.valueOf(args[7]);

        int total = 0;
        int dummy = 0;
        int encounter = 0;
        int sendHome = 0;
        float percentageValue = 0.0f;


        Map<String, String> map = new HashMap<>();
        Map<Float, Long> percentageMap = new HashMap<>();
        List<String> mapKeys;
        DevelopingEnvironment environment;
        List<Integer> variance = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader("res/Belegungszeiten.txt"));
        String line;

        while ((line = reader.readLine()) != null) {
            if (!line.startsWith("#")) {
                String parts[] = line.split(" ");
                map.put(parts[0], parts[1]);
                total += Integer.parseInt(parts[1]);
            }
        }

        mapKeys = new ArrayList<>(map.keySet());

        while (dummy < map.size()) {
            float floatDummy = Float.parseFloat(map.get(mapKeys.get(dummy))) / (float) total;
            percentageValue += floatDummy;
            percentageMap.put(percentageValue, Long.parseLong(mapKeys.get(dummy)) * 60);  // value is multiplied with 60 to convert the minutes into seconds
            dummy++;
        }
        reader.close();

        for (int i = 0; i < daysOfSimulation; i++) {
            environment = new DevelopingEnvironment(lockerAmount, (i + 1), openingHours, vipArrivalTime, timeToChange, percentageMap, arrivalProbability, withRandom, dailyStats);
            environment.simulate();
            encounter = encounter + environment.getEncounters();
            sendHome += environment.getSendHome();
            variance.add(environment.getEncounters());
        }

        statistics.saveData(daysOfSimulation, encounter, sendHome, withRandom, variance);
    }
}