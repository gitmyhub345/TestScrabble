/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.testng.Assert.*;
import org.testng.annotations.Test;


/**
 *
 * @author Will J
 */
public class DCScrabbleUtilsNGTest {
    
    public DCScrabbleUtilsNGTest() {
    }

    @Test
    public void testListMapToWords() {
        DCScrabbleUtils instance = new DCScrabbleUtils();
        List<Map<String,DCGenericTile>> listPlayedTiles = new ArrayList<>();
        
        Map<String,DCGenericTile> wordTiles = new HashMap<>();
        wordTiles.put("7-8", new DCGenericTile("D",1));
        wordTiles.put("7-5", new DCGenericTile("A",1));
        wordTiles.put("7-4", new DCGenericTile("S",1));
        wordTiles.put("7-6", new DCGenericTile("L",1));
        wordTiles.put("7-7", new DCGenericTile("A",1));
        Map<String,DCGenericTile> wordTiles2 = new HashMap<>();
        wordTiles2.put("7-8", new DCGenericTile("S",1));
        wordTiles2.put("6-8", new DCGenericTile("R",1));
        wordTiles2.put("3-8", new DCGenericTile("B",1));
        wordTiles2.put("5-8", new DCGenericTile("A",1));
        wordTiles2.put("4-8", new DCGenericTile("E",1));
        listPlayedTiles.add(wordTiles);
        listPlayedTiles.add(wordTiles2);
        List<String> word = instance.listMapToWords(listPlayedTiles);
        System.out.println(word.toString());
    }
    
}
