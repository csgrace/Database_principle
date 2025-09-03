package io.pubmed.command;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

public class DeserializeExample {
    public static void main(String[] args) {
        String filePath = "sustc-runner\\data\\test\\getArticleCitationsByYear.ser"; // Modify to your file path

        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            List<Map.Entry<Object[], Integer>> list = (List<Map.Entry<Object[], Integer>>) in.readObject();

            // Print deserialized objects
            for (Map.Entry<Object[], Integer> entry : list) {
                Object[] key = entry.getKey();
                Integer value = entry.getValue();
                System.out.println("Key: " + java.util.Arrays.toString(key) + ", Value: " + value);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}