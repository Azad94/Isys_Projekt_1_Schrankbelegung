package praktikum_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException {
        long openingHours = 10;
        long arrival = 5;
        Map<String, String> map = new HashMap<>();
        Map<Float, String> percentageMap = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader("res/Belegungszeiten.txt"));
        String line = "";
        DevelopingEnvironment environment;

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
        List<Float> percentageArray;
        float percentageValue = 0.0f;
        while(dummy<map.size()){
            float floatDummy = Float.parseFloat(map.get(mapKeys.get(dummy)))/(float)total;
            percentageValue += floatDummy;
            percentageMap.put(percentageValue, mapKeys.get(dummy));
            dummy++;
        }
        in.close();
        /*System.out.println(map);
        System.out.println (percentageMap);
        System.out.println(map.size());
        System.out.println(percentageMap.size());
        System.out.println(total);
        System.out.println(total2);
        System.out.println(percentageValue);*/

        /*percentageArray = new ArrayList<>(percentageMap.keySet());
        System.out.println(percentageArray);
        Collections.sort(percentageArray);
        System.out.println("sortiertes array");
        System.out.println(percentageArray);
        System.out.println("Größe" + percentageArray.size());
        Random rnd = new Random();
        float rndFloat = rnd.nextFloat();
        System.out.println("random float: " + rndFloat);
        float compare = 0.0f;
        for(int q = 0; q<percentageArray.size();q++){

          //  (rndFloat <= percentageArray.get(q)) ? System.out.println(percentageMap.get(percentageArray.get(q))) :
            if(rndFloat<=percentageArray.get(q) && rndFloat>compare){
                System.out.println("Die Aufenthaltszeit beträgt: " + percentageMap.get(percentageArray.get(q)) + " min");
            }
            compare = percentageArray.get(q);
        }*/
        environment = new DevelopingEnvironment(openingHours, arrival, percentageMap);
    }

}