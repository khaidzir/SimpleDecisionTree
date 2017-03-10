/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author khaidzir
 */
public class ARFFGenerator {
    
    public static void main(String [] args) {
        String filepath = "C:/Users/khaidzir/Desktop/random2.arff";
        String relationName = "random_1";
        String [] attrs = {"atr1", "atr2", "atr3", "attr4", "atr5", "atr6", "atr7", "atr8", "kelas"};
        ArrayList<String> attributeNames = new ArrayList<>(Arrays.asList(attrs));
        ArrayList<ArrayList<String>> attributeValues = new ArrayList<>();
        for(int i=0; i<attrs.length; i++) {
            String [] vals = {attrs[i]+"_a", attrs[i]+"_b"};
            attributeValues.add(new ArrayList<>(Arrays.asList(vals)));
        }
        generateRandomData(filepath, relationName, attributeNames, attributeValues);
    }
    
    public static void generateRandomData(String filepath, String relationName, ArrayList<String> attributeNames,
            ArrayList<ArrayList<String>> attributeValues) {
            BufferedWriter bw = null;
            FileWriter fw = null;

            try {
                StringBuilder sb = new StringBuilder();
                sb.append("@relation ").append(relationName).append("\n\n");
                for(int i=0; i<attributeNames.size(); i++) {
                    sb.append("@attribute ").append(attributeNames.get(i)).append(" {");
                    int j;
                    for(j=0; j<attributeValues.get(i).size()-1; j++) {
                        sb.append(attributeValues.get(i).get(j)).append(", ");
                    }
                    sb.append(attributeValues.get(i).get(j)).append("}\n");
                }
                sb.append("\n");
                
                ArrayList<ArrayList<String>> container = new ArrayList<>();
                recc(attributeValues, 0, new ArrayList<>(), container);
                
                sb.append("@data\n");
                for(ArrayList<String> instance : container) {
                    int i;
                    for(i=0; i<instance.size()-1; i++) {
                        sb.append(instance.get(i)).append(", ");
                    }
                    sb.append(instance.get(i)).append("\n");
                }
                
                fw = new FileWriter(filepath);
                bw = new BufferedWriter(fw);
                bw.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null)
                        bw.close();
                    if (fw != null)
                        fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
    }
    
    private static void recc(ArrayList<ArrayList<String>> attributeValues, int idx, 
            ArrayList<String> currIns, ArrayList<ArrayList<String>> container) {
        if (idx == attributeValues.size()-1) {
            ArrayList<String> newIns = new ArrayList<>(currIns);
            Random rand = new Random();
            int c = rand.nextInt(attributeValues.get(idx).size());
            newIns.add(attributeValues.get(idx).get(c));
            container.add(newIns);
        } else {
            for(int i=0; i<attributeValues.get(idx).size(); i++) {
                ArrayList<String> newIns = new ArrayList<>(currIns);
                newIns.add(attributeValues.get(idx).get(i));
                recc(attributeValues, idx+1, newIns, container);
            }
        }
    }
    
}
