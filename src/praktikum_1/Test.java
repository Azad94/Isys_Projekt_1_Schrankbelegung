package praktikum_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException {
        long openingHours = 30;
        long arrival = 10;
        Map<String, String> map = new HashMap<>();
        Map<String, Float> percentageMap = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader("res/Belegungszeiten.txt"));
        String line = "";
        DevelopingEnvironment environment;
        //TODO parametrisierbar machen
        int daysOfSimulation = 1;
        //TODO parametrisierbar machen
        int lockerAmount = 20;

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
        while(dummy<map.size()){
            percentageMap.put(mapKeys.get(dummy) , (Float.parseFloat(map.get(mapKeys.get(dummy)))*100.0f)/(float)total);
            dummy++;
        }
        in.close();

        //System.out.println (percentageMap);
        //System.out.println(percentageMap.size());


        for(int i = 0; i <= daysOfSimulation-1;i++){
            System.out.println("LETS START");
            environment = new DevelopingEnvironment(lockerAmount, daysOfSimulation, openingHours, arrival, percentageMap);
            environment.simulate();
            i++;

        }
    }
}