/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.comp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dragoncave.home.scrabble_2.DCGenericTile;
/**
 *
 * @author Rider1
 */
public final class intArraySort {
    
    public final static int[] maxvalue(int[] b){
        int[] bb = new int[b.length];
        int maxIndex = 0, maxValue = 0;
        for (int ind = 0; ind < b.length; ind++){
            bb[ind] = b[ind];
            if (b[ind] > maxValue){
                maxValue = b[ind];
                maxIndex = ind;
            }
        }
        bb[0] = b[maxIndex];
        bb[maxIndex] = b[0];
        for(int ind: bb)
            System.out.print(ind+" ");
        System.out.print("\n");
        return bb;
    }
    
    
    public final static int[] sortAsc(int[] a){
        int[] ascArray = Arrays.copyOf(a, a.length);
        int lValue = 0, sValue = 0;
        int tempvalue = 0;
        for (int indexb = 0; indexb < a.length-1; indexb++){
            for (int indexa=0; indexa < a.length-1; indexa++){
                if( ascArray[indexa] > ascArray[indexa+1]){
                    tempvalue = ascArray[indexa];
                    ascArray[indexa] = ascArray[indexa+1];
                    ascArray[indexa+1] = tempvalue;
                }
            }
//            for( int z: ascArray)
//                System.out.print(z+" ");
//            System.out.println("\n");
        }
//        for(int ind: ascArray)
//            System.out.print(ind+" ");
//        System.out.print("\n");
        return ascArray;
    }
    
    public final static int[] sortDes(int[] a){
        int[] desArray = Arrays.copyOf(a, a.length);
        int tempvalue = 0;
        
        for (int x=0; x < a.length-1; x++){
            for (int y = 0; y < a.length-1; y++){
                if (desArray[y+1] > desArray[y]){
                    tempvalue = desArray[y];
                    desArray[y] = desArray[y+1];
                    desArray[y+1] = tempvalue;
                }
            }
        }
//        for(int ind: desArray)
//            System.out.print(ind+" ");
//        System.out.print("\n");
        return desArray;
    }
    
    public int[] sortAscII(int[] a){
        int[] ascArray = Arrays.copyOf(a, a.length);
        
        return ascArray;
    }
    
    private int[] intFromString(String a){
        String[] stringArray = a.split(" ");
        int[] intArr = new int[stringArray.length];
        for (int ind = 0; ind < stringArray.length; ind++){
            intArr[ind] = Integer.parseInt(stringArray[ind]);
        }
        return intArr;
    }    


    public List<String> orderMapKeys(List<String> keys){
//        List<String> keys = new ArrayList<>();
//        for (String key: mapTiles.keySet()){
//            keys.add(key);
//        }
        int numKeys = keys.size();
        String lvalue= "", gvalue="", tvalue="";
        for(int indexa = 0; indexa < keys.size()-1; indexa++){
            for (int indexb = 0; indexb < keys.size()-1; indexb++){
                String[] a = keys.get(indexb).split("-");
                String[] b = keys.get(indexb+1).split("-");
                if(Integer.parseInt(a[0]) > Integer.parseInt(b[0])){
                    System.out.println("keys are vertical");
                    tvalue = keys.get(indexb);
                    keys.remove(indexb);
                    keys.add(indexb+1, tvalue);
                }else if (Integer.parseInt(a[1]) > Integer.parseInt(b[1])){
                    System.out.println("keys are horizontal");
                    tvalue = keys.get(indexb);
                    keys.remove(indexb);
                    keys.add(indexb+1, tvalue);                    
                }
            }

        }
        if (numKeys != keys.size()){
            System.out.println("error");
        }
        return keys;
    }
}