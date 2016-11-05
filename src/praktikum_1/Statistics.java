package praktikum_1;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Statistics {

    private Map<Long, Integer> durationFrequency = new HashMap<>();

    /**
     * Constructor
     *
     * @param map stored the amount of people who stayed a certain amount of time
     *            key: amount of time
     *            value: number of people
     */
    public Statistics(Map<Long, Integer> map) {
        this.durationFrequency = map;
    }

    public Statistics() {
    }

    public Map<Long, Integer> getMap() {
        return this.durationFrequency;
    }

    /**
     * Writes the collected duration frequencies into a file
     *
     * @param simulatingDay day of simulation
     */
    public void saveDailyData(int simulatingDay, int sendHome) {
        try (FileWriter fw = new FileWriter("res/statistics.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(toString(simulatingDay, sendHome));

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void saveData(int daysOfSimulation, int totalEncounter, int sendHome, boolean withRandom) {
        try (FileWriter fw = new FileWriter("res/statistics.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            if (withRandom) {
                out.println("\nrandom distribution\n");
            } else {
                out.println("\nwith strategy\n");
            }
            out.println(daysOfSimulation + " days simulated");
            out.println(sendHome + " people send home");
            out.println(totalEncounter + " encounters in " + daysOfSimulation + " days.\n");
            out.println("average encounter a day: " + ((double) totalEncounter / (double) daysOfSimulation));
            out.println("average encounter in a month: " + (double) totalEncounter / ((double) daysOfSimulation / (double) 10) + "\n");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Creates a String representation of the map
     * with the duration frequencies
     */
    private String toString(int simulatingDay, int sendHome) {
        StringBuilder builder = new StringBuilder();
        int numOfPeople = 0;
        builder.append("----- SIMULATIONDAY NR. " + simulatingDay + " -----\n");
        builder.append("Belegungszeit (in Minuten)");
        builder.append("\t");
        builder.append("Häufigkeit des Auftretens");
        builder.append("\r\n");
        for (Map.Entry<Long, Integer> printMap : durationFrequency.entrySet()) {
            builder.append(printMap.getKey());
            builder.append("\t");
            builder.append(printMap.getValue());
            builder.append("\r\n");
        }

        for (int i : durationFrequency.values()) {
            numOfPeople += i;
        }
        builder.append("\nNumber of Persons: " + numOfPeople + "\n");
        builder.append("People send home: " + sendHome);
        return builder.toString().trim();
    }
}