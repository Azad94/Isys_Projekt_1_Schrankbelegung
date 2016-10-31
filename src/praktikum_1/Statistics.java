package praktikum_1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Statistics {

    Map<Long, Integer> durationFrequency = new HashMap<>();
    File file;

    public Statistics() {
    }

    //TODO die MAP durationFrequency muss am Anfang mit den Belegungszeiten als Key und 0 als Value initializiert werden
    /**
     * Updates the frequency of the duration
     * recently assigned.
     * @param durationTime key to search for in the map
     */
    public void updateDurationFrequency(long durationTime){
       //braucht man diese If oder gehen wir davon einfach aus das man nur Zeiten wählen
        //kann die es auch nur wirklich gibt
        // if(durationFrequency.containsKey(durationTime))
            durationFrequency.put(durationTime, durationFrequency.get(durationTime) + 1);
    }

    /**
     * Writes the collected duration frequencies into a file
     * @param simulatingDay day of simulation
     */
    public void saveData(int simulatingDay){
/*
        if(file.exists()){
            try {
                FileWriter writer = new FileWriter(file,true);
                writer.write(stringRepresentation(simulatingDay));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
           **/

            System.out.println("-----------------ENTERING SAVE DATA---------------------");
            file  = new File("SimulationsLog.txt");
            System.out.println("EXISTIERT DIE FILE --> " +file.exists() + "\n");
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(stringRepresentation(simulatingDay));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("\nDONE\n");
        //}
    }

    /**
     * Creates a String representation of the map
     * with the duration frequencies
     */
    public String stringRepresentation(int simulatingDay){
        StringBuilder builder = new StringBuilder();
        long a = 1;
        int b= 1;
        durationFrequency.put(a, b);
        durationFrequency.put(a = 2, b = 2);
        durationFrequency.put(a = 3, b = 3);
        durationFrequency.put(a = 4, b = 4);
        durationFrequency.put(a = 5, b = 5);
        durationFrequency.put(a = 6, b = 6);
        System.out.println("BUILDING THE STRING...");
        builder.append("----- SIMULATIONSTAG NR. " + simulatingDay + " -----");
        builder.append("Belegungszeit (in Minuten)");
        builder.append(", ");
        builder.append("Häufigkeit des Auftretens");
        builder.append("\r\n");
        for(Map.Entry<Long, Integer> printMap : durationFrequency.entrySet()){
            builder.append(printMap.getKey());
            builder.append(", ");
            builder.append(printMap.getValue());
            builder.append("\r\n");
        }
        builder.append("\n\n");
        System.out.println("STRING TO WRITE --> "+builder.toString().trim()+"\n");
        return builder.toString().trim();
    }

    /**public long getRandomDuration() {
    public long getRandomDuration() {

        return 1;
    }

    public void frequencyScale(){}
    **/
}

/**
 Mittelwert der häufigkeitsverteilung multipliziert mit der Wahrscheinlichkeit
    ca. 46 = 1/3 belegt
 **/