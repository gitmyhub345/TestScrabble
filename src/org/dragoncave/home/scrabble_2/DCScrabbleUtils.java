/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Will J
 */
public class DCScrabbleUtils {

    public DCScrabbleUtils(){
        
    }
    
    public List<String> listMapToWords(List<Map<String,DCGenericTile>> words){
        return Utils.orderPlayedWords(words);
    }
    
    public String mapToWord(Map<String,DCGenericTile> mapWord){
        return Utils.getWordFromMap(mapWord);
    }
    
    public boolean mappedTilesEqual(Map<String,DCGenericTile> map1, Map<String,DCGenericTile> map2){
        return Utils.compareMappedTiles(map1,map2);
    }
    static class Utils{
        private static boolean compareMappedTiles(Map<String,DCGenericTile> map1, Map<String,DCGenericTile> map2){
            boolean result = false;
            boolean sizeequivalence = map1.size() == map2.size()? true : false;
            
            for (Entry<String,DCGenericTile> entry: map1.entrySet()){
                if (!map2.keySet().contains(entry.getKey())){
                    result = false;
                    break;
                }
                if (!entry.getValue().toString().equals(map2.get(entry.getKey()).toString())){
                    result = false;
                    break;
                }
                result = true;
            }
            
            
            
            return result;
        }
        
        public static List<String> orderPlayedWords(List<Map<String,DCGenericTile>> words){
//            if(debug){
//                System.out.println("DCScrabbleGame: orderPlayedWords()->"+"\n\t"+words.toString());
//            }
            List<Map<String,DCGenericTile>> mapPlayedWords = words;
            List<String> playedWords = new ArrayList<>();
            for(Map<String,DCGenericTile> wordMap: mapPlayedWords){
                /* thi has been replaced by function getWordFromMap
                List<String> wordKeys = orderMapKeys(wordMap);
                String Word = "";
                for (int index = 0; index < wordKeys.size(); index++){
                    Word +=wordMap.get(wordKeys.get(index)).getLetter();
                }
                */
                String word = getWordFromMap(wordMap);
                //System.out.println("word returned from new function: "+word);
                playedWords.add(word);
            }
            return playedWords;
        }
        private static List<String> orderMapKeys(Map<String,DCGenericTile> mapTiles){
//            if(debug){
                System.out.println("DCScrabbleGame: orderMapKeys()->"+"\n\t"+mapTiles.toString());
//            }
            List<String> keys = new ArrayList<>();
            mapTiles.keySet().stream().forEach((key) -> {
                keys.add(key);
            });
            int numKeys = keys.size();
            String tvalue="";
            for(int indexa = 0; indexa < keys.size()-1; indexa++){
                for (int indexb = 0; indexb < keys.size()-1; indexb++){
                    String[] a = keys.get(indexb).split("-");
                    String[] b = keys.get(indexb+1).split("-");
                    if(Integer.parseInt(a[0]) > Integer.parseInt(b[0])){
//                        if(debug){
                            System.out.println("\tkeys are vertical");
//                        }
                        tvalue = keys.get(indexb);
                        keys.remove(indexb);
                        keys.add(indexb+1, tvalue);
                    }else if (Integer.parseInt(a[1]) > Integer.parseInt(b[1])){
//                        if(debug){
                        System.out.println("\tkeys are horizontal");
//                        }
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
        
        public static String getWordFromMap(Map<String,DCGenericTile> mapTiles){
            String word = "";
//                for(Map<String,DCGenericTile> wordMap: mapTiles){
                    List<String> wordKeys = orderMapKeys(mapTiles);
                    String Word = "";
                    for (int index = 0; index < wordKeys.size(); index++){
                        word +=mapTiles.get(wordKeys.get(index)).getLetter();
                    }
//                    playedWords.add(Word);
//                }
            return word;
        }
        
    }
}
