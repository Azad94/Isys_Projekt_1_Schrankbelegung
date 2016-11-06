package praktikum_1;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates all necessary Statistics for a single Simulation Day
 * or all days of Simulation, as set.
 *
 */
public class Statistics {


    private Map<Long, Integer> durationFrequency = new HashMap<>();
    double encounterVariance = 0;
    /**
     * Constructor for Initialization of a Static object.
     *
     * @param map stores the amount of people who stayed a certain amount of time
     *            key: amount of time
     *            value: number of people
     */
    public Statistics(Map<Long, Integer> map) {
        this.durationFrequency = map;
    }

    /**
     * Constructor for Initialization of a Static object.
     */
    public Statistics() {
    }

    public Map<Long, Integer> getMap() {
        return this.durationFrequency;
    }

    /**
     * Writes the collected duration frequencies of one day into a file
     *
     * @param simulatingDay day of simulation
     * @param sendHome      number of Visitors send Home
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

    /**
     * Writes the collected duration frequencies of
     * all simulated days into a file.
     * @param daysOfSimulation  days the simulation is running
     * @param totalEncounter    vip encounters after all days
     * @param sendHome          visitors send Home
     * @param withRandom        tells if the random strategy is used
     * @param variance          List of amount of Encounters per day
     */
    public void saveData(int daysOfSimulation, int totalEncounter, int sendHome, boolean withRandom, List<Integer> variance) {
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
            double monthlyAvg = (double) totalEncounter / ((double) daysOfSimulation / (double) 10);
            out.println("average encounter in a month: " + monthlyAvg + "\n");
            double var = calculateVariance(variance, monthlyAvg, daysOfSimulation);
            out.println("the variance is: " + var);
            out.println("the standard deviation is: " + Math.sqrt(var));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Calculates the Variance of Encounters after the whole simulation.
     *
     * @param var               List of Encounters per day
     * @param monthlyAvg        Average Encounters per month
     * @param daysOfSimulation  Days the Simulation is going to run
     * @return                  Variance of Encounters
     */
    private double calculateVariance(List<Integer> var, double monthlyAvg, int daysOfSimulation){
        double dummy = 0;
        for(int i: var){
            dummy += ((double)i - monthlyAvg) * ((double)i - monthlyAvg);
        }
        encounterVariance = dummy / (double) daysOfSimulation;
        return encounterVariance;
    }
    /**
     * Creates a String representation of the map
     * with the duration frequencies
     *
     * @param simulatingDay     day the simulation is currently running
     * @param sendHome          amount of visitors sent home
     * @return                  String representation of all Simulation Statistics
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