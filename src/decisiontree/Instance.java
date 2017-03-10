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
public class Instance {
    
    ArrayList<String> attribute;
    
    public Instance() {}
    public Instance(ArrayList<String> attribute) {
        this.attribute = attribute;
    }
    public void setAttribute(ArrayList<String> attribute) {
        this.attribute = attribute;
    }
    public void setAttribute(int idx, String val) {
        if (idx < attribute.size())
            attribute.set(idx, val);
    }
    
}
