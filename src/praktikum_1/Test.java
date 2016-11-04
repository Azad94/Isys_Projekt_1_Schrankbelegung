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
 * index 6: random distribution or with strategy                // true for random
 */
public class Test {
    public static void main(String[] args) throws IOException {

        long openingHours = Long.parseLong(args[0]);
        if(openingHours < 1 || openingHours > 24) {
            System.out.println("Time can only be between 1 and 24 hours.");
            return;
        }
        long vipArrivalTime = Long.parseLong(args[1]);
        if(vipArrivalTime < openingHours && vipArrivalTime == 0) {
            System.out.println("The VIP can only come between the opening Hours.");
            return;
        }
        long timeToChange = Long.parseLong(args[2]);
        if(timeToChange < 120 ) {
            System.out.println("A Person needs longer than 120 seconds to change.");
            return;
        }
        int daysOfSimulation = Integer.parseInt(args[3]);
        if(daysOfSimulation < 1) {
            System.out.println("The Simulation must be simulated at least for 1 day.");
            return;
        }
        int lockerAmount = Integer.parseInt(args[4]);
        if(lockerAmount < 5) {
            System.out.println("The Amount of Lockers should be more than 5.");
            return;
        }
        double durationProbability = Double.parseDouble(args[5]);
        if(durationProbability < 0.1) {
            System.out.println("The Probability for a Person to come should be at least 0.1.");
            return;
        }
        boolean withStrategy = Boolean.valueOf(args[6]);

        int total = 0;
        int dummy = 0;
        int encounter = 0;
        double averageEncounter;
        float percentageValue = 0.0f;
        Map<String, String> map = new HashMap<>();
        Map<Float, Long> percentageMap = new HashMap<>();
        List<String> mapKeys;
        DevelopingEnvironment environment;

        BufferedReader reader = new BufferedReader(new FileReader("res/Belegungszeiten.txt"));
        String line = "";


        long startTime = System.currentTimeMillis();

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
            environment = new DevelopingEnvironment(lockerAmount, (i + 1), openingHours, vipArrivalTime, timeToChange, percentageMap, durationProbability, withStrategy);
            environment.simulate();
            encounter = encounter + environment.getEncounters();
            //  System.out.println("Simulation of Day " + i + " completed.\n");
        }

        System.out.println(encounter + " Encounters in " +daysOfSimulation + " days.");
        System.out.println("Encounter pro Tag: " + ((double) encounter/ (double) daysOfSimulation));
        averageEncounter = (double) encounter / (double) 10;
        /*long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        int seconds = (int) (totalTime / 1000) % 60 ;*/
        int month = daysOfSimulation / 10;
        double monthlyAverage = averageEncounter / (double) month;/*
        System.out.println("The Simulation took " + seconds + " seconds to simulate. For " + daysOfSimulation+" days.\n");
        System.out.println("Average Encounter in " + daysOfSimulation
                + " days of Simulation for the VIP is " + averageEncounter);*/
        System.out.println("Average Encounter per Month is " + monthlyAverage);
    }
}