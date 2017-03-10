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
public class Data {
    public String relationName;
    public ArrayList<String> attributeNames;
    public ArrayList<ArrayList<String>> attributeValues;
    public ArrayList<Instance> instances;
    public boolean isUnknownClass;
}
