/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author khaidzir
 */
public class DecisionTree {
    
    public Node root;
    public String relationName;
    public ArrayList<String> attributeNames;
    public ArrayList<ArrayList<String>> attributeValues;
    
    public String log;
    private StringBuilder sb;
    private int currId;
    private String mostCommonClass;
    
    public void train(Data data) {
        this.relationName = data.relationName;
        this.attributeNames = data.attributeNames;
        this.attributeValues = data.attributeValues;
        
        // Cari kelas yang paling banyak
        HashMap<String, Integer> mapClass = new HashMap<>();
        int idxClass = data.instances.get(0).attribute.size()-1;
        for(Instance ins : data.instances) {
            if (mapClass.containsKey(ins.attribute.get(idxClass))) {
                mapClass.put(ins.attribute.get(idxClass), mapClass.get(ins.attribute.get(idxClass))+1);
            } else {
                mapClass.put(ins.attribute.get(idxClass), 1);
            }
        }
        int max = 0;
        for(String s : attributeValues.get(idxClass)) {
            if (mapClass.containsKey(s)) {
                if (mapClass.get(s) > max) {
                    max = mapClass.get(s);
                    mostCommonClass = s;
                }
            }
        }
        
        root = new Node();
        ArrayList<Boolean> remainingAttr = new ArrayList<>();
        for(int i=0; i<attributeNames.size()-1; i++) {
            remainingAttr.add(true);
        }
        root.isClass = false;
        sb = new StringBuilder();
        currId = 0;
        buildTree(root, remainingAttr, data.instances, currId);
        log = sb.toString();
    }
    
    private void buildTree(Node currNode, ArrayList<Boolean> remainingAttr, ArrayList<Instance> remainingIns, int id) {
        currNode.id = id;
        double classEntropy = calculateClassEntropy(remainingIns);
        double maxGain = Integer.MIN_VALUE;
        int selectedAttr = -1;        
        
        // Log
        sb.append("[").append(id).append("] Atribut tersedia : {");
        int i;
        for(i=0; i<remainingAttr.size(); i++) {
            if (remainingAttr.get(i)) {
                sb.append(attributeNames.get(i)).append(", ");
            }
        }
        sb.delete(sb.length()-2, sb.length()).append("}\n");
        sb.append("[").append(id).append("] Entropi kelas = ").append(classEntropy).append("\n");
        
        // Cari atribut terbaik
        for(i=0; i<remainingAttr.size(); i++) {
            if (remainingAttr.get(i)) {
                double gain = 0.0;
                for(int j=0; j<attributeValues.get(i).size(); j++) {
                    gain += calculateAttrEntropy(remainingIns, i, attributeValues.get(i).get(j));
                }
                
                // Log
                sb.append("[").append(id).append("] E(").append(attributeNames.get(i)).append(") = ")
                  .append(gain);
                        
                gain = classEntropy-gain;
                
                // Log
                sb.append(" Gain(").append(attributeNames.get(i)).append(") = ").append(gain).append("\n");
                
                if (gain > maxGain) {
                    maxGain = gain;
                    selectedAttr = i;
                }
            }
        }
        
        // Bentuk node
        currNode.attributeIdx = selectedAttr;
        currNode.attributeName = attributeNames.get(selectedAttr);
        currNode.edges = new ArrayList<>();
        for(i=0; i<attributeValues.get(selectedAttr).size(); i++) {
            Edge e = new Edge();
            e.nextNode = new Node();
            e.value = attributeValues.get(selectedAttr).get(i);
            currNode.edges.add(e);
            currNode.id = id;
        }
        
        // Log
        sb.append("[").append(id).append("] Atribut yang dipilih : ")
          .append(currNode.attributeName).append("\n\n");
        
        // Rekursif
        boolean process;
        String sClass;
        int idxClass = remainingIns.get(0).attribute.size()-1;
        Instance ins;
        for (Edge e : currNode.edges) {
            process = false;
            sClass = null;
            int counter = 0;
            // Cek apakah sudah sama semua            
            for(i=0; i<remainingIns.size()&&!process; i++) {
                ins = remainingIns.get(i);
                if (ins.attribute.get(selectedAttr).equals(e.value)) {
                    counter++;
                    if (sClass == null) {
                        sClass = ins.attribute.get(idxClass);
                    } else {
                        if (!sClass.equals(ins.attribute.get(idxClass))) {
                            process = true;
                        }
                    }
                }
            }
            if (counter == 0) {
                e.nextNode.attributeName = mostCommonClass;
                e.nextNode.isClass = true;
                continue;
            }
            if (process) {
                // Eliminasi attribute
                ArrayList<Boolean> remAttr = new ArrayList<>(remainingAttr);
                remAttr.set(selectedAttr, false);
                e.nextNode.isClass = false;
                currId++;
                buildTree(e.nextNode, remAttr, filter(remainingIns, selectedAttr, e.value), currId);
            } else {
                e.nextNode.attributeName = sClass;
                e.nextNode.isClass = true;
//                System.out.println(e.value + " " + sClass);
            }
        }
    }
    
    public String test(Instance instance) {
        Node currNode = root;
        while(!currNode.isClass) {
            for(Edge edge : currNode.edges) {
                if (edge.value.equals(instance.attribute.get(currNode.attributeIdx))) {
                    currNode = edge.nextNode;
                    break;
                }
            }
        }
        
        return currNode.attributeName;
    }
    
    private ArrayList<Instance> filter(ArrayList<Instance> input, int attrIdx, String attrVal) {
        ArrayList<Instance> ret = new ArrayList<>();
        for(Instance ins : input) {
            if (ins.attribute.get(attrIdx).equals(attrVal)) {
                ret.add(ins);
            }
        }
        return ret;
    }
    
    private double calculateClassEntropy(ArrayList<Instance> instances) {
        if (instances.isEmpty()) return 0.0;
        
        int classIdx = instances.get(0).attribute.size()-1;
        HashMap<String, Integer> classMap = new HashMap<>();
        String classVal;
        ArrayList<String> classes = new ArrayList<>();
        for(Instance ins : instances) {
            classVal = ins.attribute.get(classIdx);
            if (classMap.containsKey(classVal)) {
                classMap.put(classVal, classMap.get(classVal)+1);
            } else {
                classes.add(classVal);
                classMap.put(classVal, 1);
            }
        }
        
        double entropy = 0.0;
        double totalIns = (double)instances.size();
        double p;
        for(String c : classes) {
            p = ((double)classMap.get(c)/totalIns);
            entropy += ( (-1) * p * (Math.log(p)/Math.log(2)) );
        }
        
        return entropy;
    }
    
    private double calculateAttrEntropy(ArrayList<Instance> instances, int attrIdx, String attrVal) {
        int classIdx = instances.get(0).attribute.size()-1;
        HashMap<String, Integer> classMap = new HashMap<>();
        String classVal;
        ArrayList<String> classes = new ArrayList<>();
        double totalIns = 0.0;
        for(Instance ins : instances) {
            if (ins.attribute.get(attrIdx).equals(attrVal)) {
                totalIns += 1.0;
                classVal = ins.attribute.get(classIdx);
                if (classMap.containsKey(classVal)) {
                    classMap.put(classVal, classMap.get(classVal)+1);
                } else {
                    classes.add(classVal);
                    classMap.put(classVal, 1);
                }
            }
        }
        if (totalIns == 0.0) return 0.0;
        
        double entropy = 0.0;        
        double p;
        for(String c : classes) {
            p = ((double)classMap.get(c)/totalIns);
            entropy += ( (-1) * p * (Math.log(p)/Math.log(2)) );
        }
        
        return (totalIns/instances.size())*entropy;
    }
    
    @Override
    public String toString() {
        if (root == null) {
            return "NULL Tree";
        } else {
            StringBuilder sb = new StringBuilder();
            Queue<Node> q = new LinkedList<>();
            sb.append(root.attributeName).append("\n");
            q.add(root);
            while(!q.isEmpty()) {
                Node n = q.poll();
                for(Edge e : n.edges) {
                    sb.append(e.value).append("\t");
                }
                sb.append("\n");
                for(Edge e : n.edges) {
                    sb.append(e.nextNode.attributeName).append("\t");
                    if (e.nextNode != null)
                        q.add(e.nextNode);
                }
                sb.append("\n");
            }
            
            return sb.toString();
        }
    }
    
}
