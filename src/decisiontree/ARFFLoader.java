/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author khaidzir
 */
public class ARFFLoader {
    
    private static final String RELATION = "@RELATION";
    private static final String ATTRIBUTE = "@ATTRIBUTE";
    private static final String DATA = "@DATA";
    
    public static final int OK = 0,
                            WRONG_FORMAT = 1,
                            DATA_ERROR = 2;
    
    public static int readARFF(String path, Data data, boolean isForTrain) {
        // Construct BufferedReader from FileReader
	BufferedReader br;        
        ArrayList<Instance> instances = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line = null;
            instances = new ArrayList<>();
            String relation = null;
            ArrayList<String> attributeNames = new ArrayList<>();
            ArrayList<ArrayList<String>> attributes = new ArrayList<>();
            boolean bRel=false, bAttr=false;
            boolean isUnknownClass = false;
            int i;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) continue;
                if (line.charAt(0) == '%') continue;
                String[] arrStr = line.split("\\s+");
                
                if (arrStr[0].compareToIgnoreCase(RELATION)==0) {
                    if (bRel) {
                        return WRONG_FORMAT;
                    } else {
                        bRel = true;
                        relation = arrStr[1];
                        continue;
                    }
                }
                
                if (arrStr[0].compareToIgnoreCase(ATTRIBUTE)==0) {
                    if (!bRel) {
                        return WRONG_FORMAT;
                    }
                    bAttr = true;
                    int code = readAttribute(arrStr, attributeNames, attributes);
                    if (code != OK) return code;
                    continue;
                }
                
                if (arrStr[0].compareToIgnoreCase(DATA)==0) {
                    if (!bAttr) return WRONG_FORMAT;
                    ArrayList<HashMap<String, Boolean>> mapAttr = new ArrayList<>();
                    for(ArrayList<String> arr : attributes) {
                        HashMap<String, Boolean> hashMap = new HashMap<>();
                        for(String s : arr) {
                            hashMap.put(s, true);
                        }
                        mapAttr.add(hashMap);
                    }
                    boolean firstData=true;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.charAt(0) == '%') continue;
                        String[] arrS = line.split(",+\\s*");
                        if (arrS.length != attributes.size()) {
                            return WRONG_FORMAT;
                        }
                        
                        for(i=0; i<arrS.length-1; i++) {
                            if (!mapAttr.get(i).containsKey(arrS[i])) {
                                return DATA_ERROR;
                            }
                        }
                        if (isForTrain) {
                            if (!mapAttr.get(i).containsKey(arrS[i])) {
                                return DATA_ERROR;
                            }
                        } else {
                            if (firstData) {
                                if (arrS[i].equals("?")) isUnknownClass = true;
                                else isUnknownClass = false;
                                firstData = false;
                            } 
                            if (isUnknownClass) {
                                if (arrS[i].equals("?")) {
                                    return DATA_ERROR;
                                }
                            } else {
                                if (!mapAttr.get(i).containsKey(arrS[i])) {
                                    return DATA_ERROR;
                                }
                            }
                        }
                        instances.add(new Instance(new ArrayList<>(Arrays.asList(arrS))));
                    }
                }
            }
            
            data.relationName = relation;
            data.attributeNames = attributeNames;
            data.attributeValues = attributes;
            data.instances = instances;
            data.isUnknownClass = isUnknownClass;
            
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ARFFLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(ARFFLoader.class.getName()).log(Level.SEVERE, null, e);
        }
        return OK;
    }
    
    private static int readAttribute(String[] arrLine, ArrayList<String> attrNames, ArrayList<ArrayList<String>> attrValues) {
        if (arrLine[2].charAt(0) == '{') {
            if (arrLine[2].charAt(arrLine[2].length()-1) == '}') {
                String [] arrAttr = arrLine[2].split(",");
                ArrayList<String> attrVals = new ArrayList<>();
                attrVals.add(arrAttr[0].substring(1));
                for(int i=1; i<arrAttr.length-1; i++) {
                    if (arrAttr[i].equals("?")) return WRONG_FORMAT;
                    attrVals.add(arrAttr[i]);
                }
                String last = arrAttr[arrAttr.length-1].substring(0, arrAttr[arrAttr.length-1].length()-1);
                if (last.equals("?")) return WRONG_FORMAT;
                attrVals.add(last);
                attrValues.add(attrVals);
                attrNames.add(arrLine[1]);
                return OK;
            } else if (arrLine[arrLine.length-1].charAt(arrLine[arrLine.length-1].length()-1) == '}') {
                ArrayList<String> attrVals = new ArrayList<>();
                String [] arrAttr = arrLine[2].split(",+");                
                String attr = arrAttr[0].substring(1);
                if (attr.length() > 0) {
                    attrVals.add(attr);
                    for(int i=1; i<arrAttr.length; i++) {
                        if (arrAttr[i].equals("?")) return WRONG_FORMAT;
                        attrVals.add(arrAttr[i]);
                    }
                }
                for(int i=3; i<arrLine.length-1; i++) {
                    arrAttr = arrLine[i].split(",+");
                    for(int j=0; j<arrAttr.length; j++) {
                        if (arrAttr[j].equals("?")) return WRONG_FORMAT;
                        attrVals.add(arrAttr[j]);
                    }
                }
                arrAttr = arrLine[arrLine.length-1].split(",+");
                for(int i=0; i<arrAttr.length-1; i++) {
                    if (arrAttr[i].equals("?")) return WRONG_FORMAT;
                    attrVals.add(arrAttr[i]);
                }
                attr = arrAttr[arrAttr.length-1].substring(0, arrAttr[arrAttr.length-1].length()-1);
                if (attr.length() > 0) {
                    if(attr.equals("?"));
                    attrVals.add(attr);
                }
                attrValues.add(attrVals);
                attrNames.add(arrLine[1]);
                return OK;
            } else {
                return WRONG_FORMAT;
            }
        } else {
            return WRONG_FORMAT;
        }
    }
    
}
