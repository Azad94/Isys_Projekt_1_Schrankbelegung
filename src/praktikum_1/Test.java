package praktikum_1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import static java.lang.Integer.valueOf;

/**
 * Created by Malte on 27.10.2016.
 */
public class Test {
    public static void main(String[] args) throws IOException {

        Map<String, String> map = new HashMap<String, String>();
        BufferedReader in = new BufferedReader(new FileReader("res/Belegungszeiten.txt"));
        String line = "";

        int gesamt = 0;
        int start = 0;
        int end = 0;

        while ((line = in.readLine()) != null) {
            if(!line.startsWith("#")) {
                String parts[] = line.split(" ");
                map.put(parts[0], parts[1]);
                gesamt += Integer.parseInt(parts[1]);
            }
        }
        in.close();
        System.out.println(gesamt);
        System.out.println(map.toString());
    }

}
