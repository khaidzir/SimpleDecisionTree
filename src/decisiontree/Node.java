/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontree;

import java.util.ArrayList;

/**
 *
 * @author khaidzir
 */
public class Node {
    boolean isClass;
    public String attributeName;
    public int attributeIdx;
    public ArrayList<Edge> edges;
    
    // Dibutuhin buat GUI
    public int nLeaf;
    public int nodeLevel;
    public int xCoorArea, widthArea;
    public int id;
    
    public int calculateNLeaf() {
        if (isClass) {
            nLeaf = 1;
        } else {
            nLeaf = 0;
            for(Edge e : edges) {
                nLeaf += e.nextNode.calculateNLeaf();
            }
        }
        return nLeaf;
    }
    
}
