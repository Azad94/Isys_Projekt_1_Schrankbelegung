package praktikum_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException {
        long openingHours = 1;
        long arrival = 0;
        Map<String, String> map = new HashMap<>();
        Map<Float, Long> percentageMap = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader("res/Belegungszeiten.txt"));
        String line = "";
        DevelopingEnvironment environment;
        //TODO parametrisierbar machen
        int daysOfSimulation = 1;
        //TODO parametrisierbar machen
        int lockerAmount = 20;

        int total = 0;
        float total2 = 0.0f;
        int start = 0;
        int end = 0;
        int dummy = 0;
        List<String> mapKeys;

        while ((line = in.readLine()) != null) {
            if(!line.startsWith("#")) {
                String parts[] = line.split(" ");
                map.put(parts[0], parts[1]);
                total += Integer.parseInt(parts[1]);
                total2 += Float.parseFloat(parts[1])/99999.0f;
            }
        }
        mapKeys = new ArrayList<>(map.keySet());
        float percentageValue = 0.0f;
        while(dummy<map.size()){
            float floatDummy = Float.parseFloat(map.get(mapKeys.get(dummy)))/(float)total;
            percentageValue += floatDummy;
            percentageMap.put(percentageValue, Long.parseLong(mapKeys.get(dummy))*60);
            dummy++;
        }
        in.close();

        for(int i = 0; i <= daysOfSimulation-1;i++){
            System.out.println("LETS START");
            environment = new DevelopingEnvironment(lockerAmount, daysOfSimulation, openingHours, arrival, percentageMap);
            environment.simulate();
            i++;

        }
    }
}