package praktikum_1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {

    Map<Long, Integer> durationFrequency = new HashMap<>();
    File file;
    StringBuilder builder = new StringBuilder();

    public Statistics() {
    }

    public void frequencyScale(){

    }

    public void updateDurationFrequency(long durationTime){
       //braucht man diese If oder gehen wir davon einfach aus das man nur Zeiten wählen
        //kann die es auch nur wirklich gibt
        // if(durationFrequency.containsKey(durationTime))
            durationFrequency.put(durationTime, durationFrequency.get(durationTime) + 1);
    }

    public void recordData(int simulatingDay){

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
        logFile(simulatingDay);
    }

    public void logFile(int simulatingDay){
        file  = new File("Belegungszeiten_" + simulatingDay + ".txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(builder.toString().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getRandomDuration() {
        //TODO Implementieren
        return 1;
    }
}