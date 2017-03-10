/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontree;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author khaidzir
 */
public class Coba {
    
    public static void main(String [] args) {
        String s = "{sunny,";
        String [] a = s.split(",+");
        for(String q : a) {
            System.out.println(q);
        }
    }
    
    public static void tesPerformansi() {
        int N = 1000000;
        
        int [] arrbiasa = new int[N];
        ArrayList<Integer> arr = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();        
        
        double exec;
       
        /* Coba add */
        exec = System.nanoTime();
        for(int i=0; i<N; i++) {
            arrbiasa[i] = i;
        }
        exec = (System.nanoTime()-exec)/1000;
        System.out.println("Waktu eksekusi penambahan arraybiasa : " + exec + " ms");
        
        exec = System.nanoTime();
        for(int i=0; i<N; i++) {
            arr.add(i);
        }
        exec = (System.nanoTime()-exec)/1000;
        System.out.println("Waktu eksekusi penambahan arraylist : " + exec + " ms");
        
        exec = System.nanoTime();
        for(int i=0; i<N; i++) {
            map.put(i, i);
        }
        exec = (System.nanoTime()-exec)/1000;
        System.out.println("Waktu eksekusi penambahan hashmap : " + exec + " ms");
        
        /* Coba akses */
        int a;
        exec = System.nanoTime();
        for(int i=0; i<N; i++) {
            a = arrbiasa[i];
        }
        exec = (System.nanoTime()-exec)/1000;
        System.out.println("Waktu eksekusi pengaksesan arraybiasa : " + exec + " ms");
        
        exec = System.nanoTime();
        for(int i=0; i<N; i++) {
            a = arr.get(i);
        }
        exec = (System.nanoTime()-exec)/1000;
        System.out.println("Waktu eksekusi pengaksesan arraylist : " + exec + " ms");
        
        exec = System.nanoTime();
        for(int i=0; i<N; i++) {
            a = map.get(i);
        }
        exec = (System.nanoTime()-exec)/1000;
        System.out.println("Waktu eksekusi pengaksesan hashmap : " + exec + " ms");
    }
    
}
