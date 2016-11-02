package praktikum_1;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Statistics {

    Map<Long, Integer> durationFrequency = new HashMap<>();
    int day;

    /**
     * Constructor
     *
     * @param map stored the amount of people who stayed a certain amount of time
     *            key: amount of time
     *            value: number of people
     */
    public Statistics(Map<Long, Integer> map) {
        this.durationFrequency = map;
        this.day = 0;
    }
    public Map<Long, Integer> getMap(){
        return this.durationFrequency;
    }

   /* /**
     * Updates the frequency of the duration
     * recently assigned.
     *
     * @param durationTime key to search for in the map
     */
   /* public void updateDurationFrequency(long durationTime) {
        //braucht man diese If oder gehen wir davon einfach aus das man nur Zeiten wählen
        //kann die es auch nur wirklich gibt
        // if(durationFrequency.containsKey(durationTime))
        durationFrequency.put(durationTime, durationFrequency.get(durationTime) + 1);
    }*/

    /**
     * Writes the collected duration frequencies into a file
     *
     * @param simulatingDay day of simulation
     */
    public void saveData(int simulatingDay) {
           try{

                Logger logger = Logger.getLogger("Simulations_Log");
                FileHandler fh = new FileHandler("res/SimulationsLog_"+ simulatingDay++ +".log" , true);
                logger.addHandler(fh);

                SimpleFormatter f = new SimpleFormatter();
                fh.setFormatter(f);
                logger.setUseParentHandlers(false);

                logger.info("\n\n" + stringRepresentation(simulatingDay) + "\n");
                int gesamt = 0;
                for(int i: durationFrequency.values()){
                    gesamt += i;
                }
                System.out.println("Personen Gesamt: " + gesamt);

            }catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Creates a String representation of the map
     * with the duration frequencies
     */
    public String stringRepresentation(int simulatingDay) {
        StringBuilder builder = new StringBuilder();
        int numOfPeople = 0;
        builder.append("\n----- SIMULATIONSTAG NR. " + simulatingDay + " -----\n");
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
        for(int i : durationFrequency.values()){
            numOfPeople+=i;
        }
        builder.append("\nNumber of Persons: " + numOfPeople);
        builder.append("\n\n");
       // System.out.println("STRING TO WRITE --> " + builder.toString().trim() + "\n");
        return builder.toString().trim();
    }
}

/**
 * Mittelwert der häufigkeitsverteilung multipliziert mit der Wahrscheinlichkeit
 * ca. 46 = 1/3 belegt
 **/