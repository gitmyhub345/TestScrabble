/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import static org.testng.Assert.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author Rider1
 */
public class DCGenericScrabbleBoardNGTest {
    
    @DataProvider(name="intArray")
    public Object[][] createData(){
        Random rand = new Random();
        rand.setSeed(new Date().getTime());
        int narrays = Math.abs(rand.nextInt()%50);
        Object[][][] obj= new Object[narrays][][];
        
        for (int r = 0; r < narrays; r++) {
            obj[r] = new Object[1][];
            int length = Math.abs(rand.nextInt()%25);
            obj[r][0] = new Object[length];
            for (int z = 0; z < length; z++){
                obj[r][0][z] = Math.abs(rand.nextInt()%1000);
            }
        }
        
        return obj;
    }
    
    public DCGenericScrabbleBoardNGTest() {
    }

    @Test(dataProvider="intArray")
    public void testIntArraySort(Object[] b) {
        System.out.println("Test: intArraySort-> sorting integer arrays");
        int[] a = new int[b.length];
        for (int obj = 0; obj < b.length; obj++){
            a[obj] = (Integer)b[obj];
        }
        
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        instance.intArraySort(a);
        /*
        for (int i = 0; i < a.length; i++){
            System.out.print(a[i]+",");            
        }
        */
        for (int index =0; index < a.length-1; index++){
            assert(a[index] <= a[index+1]);
        }
        
    }
    
    @Test
    public void testHorizontal(){
        System.out.println("Test: isHorizontal-> testing for horizontal hand");
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        instance.setDebug(true);
        DCGenericTileBag bag = new DCGenericTileBag();
        Map<String,DCGenericTile> playedWord = new HashMap<String,DCGenericTile>(5);
        
        List<DCGenericTile> word = bag.replacePlayedTiles(5);
        playedWord.put("3-3", word.get(0));
        playedWord.put("3-7", word.get(1));
        playedWord.put("3-5", word.get(2));
        playedWord.put("3-4", word.get(3));
        playedWord.put("3-6", word.get(4));
        boolean horizontal = instance.isHorizontal(playedWord);
        assert(horizontal);

        word.clear();
        playedWord.clear();
        word = bag.replacePlayedTiles(6);
        playedWord.put("7-2", word.get(0));
        playedWord.put("8-2", word.get(0));
        playedWord.put("1-2", word.get(0));
        playedWord.put("3-2", word.get(0));
        playedWord.put("4-2", word.get(0));
        playedWord.put("6-2", word.get(0));
        horizontal = instance.isHorizontal(playedWord);
        assert(!horizontal);

        word.clear();
        playedWord.clear();
        word = bag.replacePlayedTiles(6);
        playedWord.put("2-1", word.get(0));
        playedWord.put("2-2", word.get(0));
        playedWord.put("1-2", word.get(0));
        playedWord.put("3-2", word.get(0));
        playedWord.put("4-2", word.get(0));
        playedWord.put("6-2", word.get(0));
        horizontal = instance.isHorizontal(playedWord);
        assert(!horizontal);
    }
    @Test
    public void testVertical(){
        System.out.println("Test: isVertical-> testing for vertical hand");
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        instance.setDebug(false);
        DCGenericTileBag bag = new DCGenericTileBag();
        Map<String,DCGenericTile> playedWord = new HashMap<String,DCGenericTile>(5);
        
        List<DCGenericTile> word = bag.replacePlayedTiles(5);
        playedWord.put("7-3", word.get(0));
        playedWord.put("6-3", word.get(1));
        playedWord.put("8-3", word.get(2));
        playedWord.put("4-3", word.get(3));
        playedWord.put("5-3", word.get(4));
        boolean vertical = instance.isVertical(playedWord);
        assert(vertical);

        word.clear();
        playedWord.clear();
        word = bag.replacePlayedTiles(6);
        playedWord.put("7-2", word.get(0));
        playedWord.put("7-3", word.get(0));
        playedWord.put("7-5", word.get(0));
        playedWord.put("7-1", word.get(0));
        playedWord.put("7-8", word.get(0));
        playedWord.put("7-10", word.get(0));
        vertical = instance.isVertical(playedWord);
        assert(!vertical);

        word.clear();
        playedWord.clear();
        word = bag.replacePlayedTiles(6);
        playedWord.put("2-1", word.get(0));
        playedWord.put("2-2", word.get(0));
        playedWord.put("1-2", word.get(0));
        playedWord.put("3-2", word.get(0));
        playedWord.put("4-2", word.get(0));
        playedWord.put("6-2", word.get(0));
        vertical = instance.isVertical(playedWord);
        assert(!vertical);
    }

    @Test
    public void testIsConsecutive(){
        System.out.println("Test: isConsecutive-> testing consecutive tiles");
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        DCGenericTileBag bag = new DCGenericTileBag();
        instance.setDebug(true);
        Map<String,DCGenericTile> playedWord = new HashMap<String,DCGenericTile>(4);
        
        List<DCGenericTile> word = bag.replacePlayedTiles(4);
        playedWord.put("7-8", word.get(0));
        playedWord.put("8-8", word.get(1));
        playedWord.put("9-8", word.get(2));
        playedWord.put("10-8", word.get(3));
        
        boolean consecutive = instance.isConsecutive(playedWord);
        assert(consecutive);
        
        word.clear();
        playedWord.clear();
        
        word = bag.replacePlayedTiles(5);
        playedWord.put("8-9", word.get(0));
        playedWord.put("8-10", word.get(1));
        playedWord.put("8-11", word.get(2));
        playedWord.put("8-12", word.get(3));
        playedWord.put("8-8", word.get(4));
        
        consecutive = instance.isConsecutive(playedWord);
        assert(consecutive);
    }

    @Test
    public void testIsConsecutive2(){
        System.out.println("Test: isConsecutive2-> testing non consecutive tiles");
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        DCGenericTileBag bag = new DCGenericTileBag();
        instance.setDebug(true);
        Map<String,DCGenericTile> playedWord = new HashMap<String,DCGenericTile>(4);
        
        List<DCGenericTile> word = bag.replacePlayedTiles(4);
        playedWord.put("7-8", word.get(0));
        playedWord.put("8-8", word.get(1));
        playedWord.put("9-8", word.get(2));
        playedWord.put("11-8", word.get(3));
        
        boolean consecutive = instance.isConsecutive(playedWord);
        assert(!consecutive);
        
        word.clear();
        playedWord.clear();
        
        word = bag.replacePlayedTiles(5);
        playedWord.put("3-2", word.get(0));
        playedWord.put("3-4", word.get(1));
        playedWord.put("3-5", word.get(2));
        playedWord.put("3-6", word.get(3));
        playedWord.put("3-7", word.get(4));
        
        consecutive = instance.isConsecutive(playedWord);
        assert(!consecutive);
        word.clear();
        playedWord.clear();
        
        word = bag.replacePlayedTiles(5);
        playedWord.put("3-3", word.get(0));
        playedWord.put("3-4", word.get(1));
        playedWord.put("3-5", word.get(2));
        playedWord.put("5-5", word.get(3));
        playedWord.put("4-5", word.get(4));
        
        consecutive = instance.isConsecutive(playedWord);
        assert(!consecutive);
    }
    
    @Test
    public void testIsWordConnected(){
        System.out.println("Test: isWordConnected...");
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        instance.setDebug(true);
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-8", tiles.get(0));
        playedTiles.put("8-8", tiles.get(1));
        playedTiles.put("9-8", tiles.get(2));
        playedTiles.put("7-8", tiles.get(3));
        
        instance.addWordsToBoard(playedTiles);
        
        tiles.clear();
        playedTiles.clear();
        /**
         * skipped tile
         */
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("8-9", tiles.get(0));
        playedTiles.put("8-10", tiles.get(1));
        playedTiles.put("8-6", tiles.get(2));
        playedTiles.put("8-7", tiles.get(3));
        playedTiles.put("8-5", tiles.get(4));
        
        boolean result = instance.isWordConnected(playedTiles);
        if (result)
            System.out.println("Test isWordConnected: result -> Tiles are connected");
        else
            System.out.println("Test isWordConnected: result -> Tiles are not connected");
        assert(result);
        
        if(result)
            instance.addWordsToBoard(playedTiles);
        
        tiles.clear();
        playedTiles.clear();
        /**
         * skipped tile
         */
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("9-9", tiles.get(0));
        playedTiles.put("9-10", tiles.get(1));
        playedTiles.put("9-11", tiles.get(2));
        playedTiles.put("9-12", tiles.get(3));
        playedTiles.put("9-13", tiles.get(4));
        
        result = instance.isWordConnected(playedTiles);
        if (result)
            System.out.println("Test isWordConnected: result -> Tiles are connected");
        else
            System.out.println("Test isWordConnected: result -> Tiles are not connected");
        assert(result);
         
    }
    
    @Test
    public void testHasPerpendicular(){
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        instance.setDebug(true);
        
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-8", tiles.get(0));
        playedTiles.put("8-8", tiles.get(1));
        playedTiles.put("9-8", tiles.get(2));
        playedTiles.put("7-8", tiles.get(3));
        instance.addWordsToBoard(playedTiles);
        tiles.clear();
        playedTiles.clear();
        
        
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("6-7", tiles.get(0));
        playedTiles.put("7-7", tiles.get(1));
        playedTiles.put("8-7", tiles.get(2));
        playedTiles.put("9-7", tiles.get(3));
        playedTiles.put("5-7", tiles.get(4));
        
        int[] indexes = instance.getRowIndexes(playedTiles);
//        int crossWords = instance.hasPerpendicular(indexes, 1, 7);
        
        boolean result = instance.isWordConnected(playedTiles);
        
//        System.out.println("number of perpendicular words: "+crossWords);
//        assertEquals(crossWords,4);
        assert(result);
        
    }
    
    @Test
    public void testAddLeadingTiles(){
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        instance.setDebug(true);
        
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-8", tiles.get(0));
        playedTiles.put("8-8", tiles.get(1));
        playedTiles.put("9-8", tiles.get(2));
        playedTiles.put("7-8", tiles.get(3));
        instance.addWordsToBoard(playedTiles);
        tiles.clear();
        playedTiles.clear();
        
        
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("10-8", tiles.get(0));
        playedTiles.put("11-8", tiles.get(1));
        playedTiles.put("12-8", tiles.get(2));
        playedTiles.put("13-8", tiles.get(3));
        playedTiles.put("14-8", tiles.get(4));
        int[] indexes = instance.getRowIndexes(playedTiles);
        int rowColIndex = 8; // tiles on column 8
        int orientation = 1; // vertical
        instance.addLeadingTiles(indexes, orientation, rowColIndex, playedTiles);
        assert(playedTiles.size() == 9);
        instance.addWordsToBoard(playedTiles);
        
        
        tiles.clear();
        playedTiles.clear();
        
        tiles = bag.replacePlayedTiles(3);
        playedTiles.put("12-9", tiles.get(0));
        playedTiles.put("12-10", tiles.get(1));
        playedTiles.put("12-11", tiles.get(2));
        
        int[] indexesb = instance.getColIndexes(playedTiles);
        rowColIndex = 12;
        orientation = 0;
        instance.addLeadingTiles(indexesb, orientation, rowColIndex, playedTiles);
        assert(playedTiles.size() == 4);
        instance.addWordsToBoard(playedTiles);
        instance.printBoard();
        
    }

    @Test
    public void testAddTrailingTiles(){
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        instance.setDebug(true);
        
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-8", tiles.get(0));
        playedTiles.put("8-8", tiles.get(1));
        playedTiles.put("9-8", tiles.get(2));
        playedTiles.put("7-8", tiles.get(3));
        instance.addWordsToBoard(playedTiles);
        tiles.clear();
        playedTiles.clear();
        
        
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("1-8", tiles.get(0));
        playedTiles.put("2-8", tiles.get(1));
        playedTiles.put("3-8", tiles.get(2));
        playedTiles.put("4-8", tiles.get(3));
        playedTiles.put("5-8", tiles.get(4));
        
        int[] indexes = instance.getRowIndexes(playedTiles);
        int rowColIndex = 8; // tiles on column 8
        int orientation = 1; // vertical
        instance.addTrailingTiles(indexes, orientation, rowColIndex, playedTiles);
        assert(playedTiles.size() == 9);
        
    }
    
    @Test
    public void testFillInGap(){
        DCGenericScrabbleBoard instance = new DCGenericScrabbleBoard();
        instance.setDebug(true);
        
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-8", tiles.get(0));
        playedTiles.put("8-8", tiles.get(1));
        playedTiles.put("9-8", tiles.get(3));
        playedTiles.put("7-8", tiles.get(2));
        instance.addWordsToBoard(playedTiles);
        tiles.clear();
        playedTiles.clear();
        
        
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("10-8", tiles.get(0));
        playedTiles.put("11-8", tiles.get(1));
        playedTiles.put("3-8", tiles.get(2));
        playedTiles.put("4-8", tiles.get(3));
        playedTiles.put("5-8", tiles.get(4));
        
        int[] indexes = instance.getRowIndexes(playedTiles);
        int rowColIndex = 8; // tiles on column 8
        int orientation = 1; // vertical
        instance.fillInGaps(indexes, orientation, rowColIndex, playedTiles);
        assert(playedTiles.size() == 9);
        instance.addWordsToBoard(playedTiles);
        
        instance.printBoard();
    }
}
