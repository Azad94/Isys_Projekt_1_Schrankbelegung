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
        file  = new File("Simulationstag_Nr." + simulatingDay + ".txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(stringRepresentation());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a String representation of the map
     * with the duration frequencies
     */
    public String stringRepresentation(){

        StringBuilder builder = new StringBuilder();
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
        return builder.toString().trim();
    }

    public long getRandomDuration() {

        return 1;
    }

    public void frequencyScale(){

    }
}