/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Rider1
 */
public class DCGenericScrabblePlayTestNGTest {
    
    public DCGenericScrabblePlayTestNGTest() {
    }
    @Test
    public void testHorizontal(){
        System.out.println("TESTING: DCGENERICSCRABBLEPLAY: ISHORIZONTAL() FUNCTION...");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay play = new DCGenericScrabblePlay();
        play.setBoard(board);
        
        play.setDebug(true);
        board.setDebug(true);
        DCGenericTileBag bag = new DCGenericTileBag();
        Map<String,DCGenericTile> playedWord = new HashMap<String,DCGenericTile>(5);
        
        List<DCGenericTile> word = bag.replacePlayedTiles(5);
        playedWord.put("3-3", word.get(0));
        playedWord.put("3-7", word.get(1));
        playedWord.put("3-5", word.get(2));
        playedWord.put("3-4", word.get(3));
        playedWord.put("3-6", word.get(4));
        boolean horizontal = play.isHorizontal(playedWord);
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
        horizontal = play.isHorizontal(playedWord);
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
        horizontal = play.isHorizontal(playedWord);
        assert(!horizontal);

        word.clear();
        playedWord.clear();
        word = bag.replacePlayedTiles(1);
        playedWord.put("7-7", word.get(0));
        horizontal = play.isHorizontal(playedWord);
        assert(!horizontal);
    }
    
    @Test
    public void testVertical(){
        System.out.println("TESTING: DCGENERICSCRABBLEPLAY: ISVERTICAL() FUNCTION...");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay instance = new DCGenericScrabblePlay();
        instance.setBoard(board);
        instance.setDebug(true);
        board.setDebug(true);
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

        word.clear();
        playedWord.clear();
        word = bag.replacePlayedTiles(1);
        playedWord.put("7-7", word.get(0));
        vertical = instance.isVertical(playedWord);
        assert(!vertical);
    }

    @Test
    public void testIsConsecutive(){
        System.out.println("TESTING: DCGENERICSCRABBLEPLAY: ISCONSECUTIVE() FUNCTION...");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay instance = new DCGenericScrabblePlay();
        instance.setBoard(board);
        DCGenericTileBag bag = new DCGenericTileBag();
        instance.setDebug(true);
        board.setDebug(true);
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

        word.clear();
        playedWord.clear();
        
        word = bag.replacePlayedTiles(5);
        playedWord.put("8-9", word.get(0));
        playedWord.put("8-10", word.get(1));
        playedWord.put("8-11", word.get(2));
        playedWord.put("8-13", word.get(3));
        playedWord.put("8-8", word.get(4));
        
        consecutive = instance.isConsecutive(playedWord);
        assert(!consecutive);

        word.clear();
        playedWord.clear();
        
        word = bag.replacePlayedTiles(5);
        playedWord.put("7-5", word.get(0));
        playedWord.put("8-5", word.get(1));
        playedWord.put("11-5", word.get(2));
        playedWord.put("12-5", word.get(3));
        playedWord.put("3-5", word.get(4));
        
        consecutive = instance.isConsecutive(playedWord);
        assert(!consecutive);

        word.clear();
        playedWord.clear();
        word = bag.replacePlayedTiles(1);
        playedWord.put("7-7", word.get(0));
        consecutive = instance.isConsecutive(playedWord);
        assert(!consecutive);
    }

    @Test
    public void testIsWordConnected(){
        System.out.println("TESTING: DCGENERICSCRABBLEPLAY: ISWORDCONNECTED() FUNCTION...");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay instance = new DCGenericScrabblePlay();
        board.setDebug(true);
        instance.setBoard(board);
        instance.setDebug(true);
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-8", tiles.get(0));
        playedTiles.put("8-8", tiles.get(1));
        playedTiles.put("9-8", tiles.get(2));
        playedTiles.put("7-8", tiles.get(3));
        
        board.addWordsToBoard(playedTiles);
        
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
        
        board.printBoard();
        boolean result = instance.isValid(playedTiles);
        if (result)
            System.out.println("Test isWordConnected: result -> Tiles are connected");
        else
            System.out.println("Test isWordConnected: result -> Tiles are not connected");
        assert(result);
        
//        if(result)
//            board.addWordsToBoard(playedTiles);
//        
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
        
        result = instance.isValid(playedTiles);
        board.printBoard();
        if (result)
            System.out.println("Test isWordConnected: result -> Tiles are connected");
        else
            System.out.println("Test isWordConnected: result -> Tiles are not connected");
        assert(result);
        
    }

    @Test
    public void testHasPerpendicular(){
        System.out.println("TESTING: DC GENERICSCRABBLEPLAY: HASPERPENDICULAR() FUNCTION...");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay instance = new DCGenericScrabblePlay(board);
//        instance.setBoard(board);
        board.setDebug(true);
        instance.setDebug(true);
        
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-7", tiles.get(0));
        playedTiles.put("8-7", tiles.get(1));
        playedTiles.put("9-7", tiles.get(2));
        playedTiles.put("7-7", tiles.get(3));
//        board.addWordsToBoard(playedTiles);
        instance.isValid(playedTiles);
        tiles.clear();
        playedTiles.clear();
        
        
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("9-8", tiles.get(0));
        playedTiles.put("9-6", tiles.get(1));
        playedTiles.put("9-9", tiles.get(2));
        playedTiles.put("9-10", tiles.get(3));
        playedTiles.put("9-11", tiles.get(4));
        
//        int[] indexes = instance.getRowIndexes(playedTiles);
//        int crossWords = instance.hasPerpendicular(indexes, 1, 7);
        
        boolean result = instance.isValid(playedTiles);
        tiles.clear();
        playedTiles.clear();
        
        
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("9-12", tiles.get(0));
        playedTiles.put("10-12", tiles.get(1));
        playedTiles.put("11-12", tiles.get(2));
        playedTiles.put("12-12", tiles.get(3));
        playedTiles.put("13-12", tiles.get(4));
        result = instance.isValid(playedTiles);
        
        tiles.clear();
        playedTiles.clear();
        
        
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("14-8", tiles.get(0));
        playedTiles.put("14-12", tiles.get(1));
        playedTiles.put("14-9", tiles.get(2));
        playedTiles.put("14-10", tiles.get(3));
        playedTiles.put("14-11", tiles.get(4));
        result = instance.isValid(playedTiles);
        
        board.printBoard();
//        System.out.println("number of perpendicular words: "+crossWords);
//        assertEquals(crossWords,4);
        assert(result);
        
        List instanceWords = instance.getWords();
        instanceWords.forEach((map)->{
            System.out.println(map.toString());
        });
    }
    
    @Test
    public void testAddLeadingTiles(){
        System.out.println("TESTING: DCGENERICSCRABBLEPLAY: ADDLEADINGTILES() FUNCTION -> CASE 1: VERTICAL");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay instance = new DCGenericScrabblePlay(board);
//        instance.setBoard(board);
        board.setDebug(true);
        instance.setDebug(true);
        
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-8", tiles.get(0));
        playedTiles.put("8-8", tiles.get(1));
        playedTiles.put("9-8", tiles.get(2));
        playedTiles.put("7-8", tiles.get(3));
        board.addWordsToBoard(playedTiles);
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
        board.addWordsToBoard(playedTiles);
        
        
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
        board.addWordsToBoard(playedTiles);
    }

    @Test
    public void testAddLeadingTiles2(){
        System.out.println("TESTING: DCGENERICSCRABBLEPLAY: ADDLEADINGTILES() FUNCTION -> CASE 2: HORIZONTAL");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay instance = new DCGenericScrabblePlay(board);
//        instance.setBoard(board);
        board.setDebug(true);
        instance.setDebug(true);
        
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("7-5", tiles.get(0));
        playedTiles.put("7-6", tiles.get(1));
        playedTiles.put("7-7", tiles.get(2));
        playedTiles.put("7-8", tiles.get(3));
        board.addWordsToBoard(playedTiles);
        tiles.clear();
        playedTiles.clear();
        
        
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("7-9", tiles.get(0));
        playedTiles.put("7-10", tiles.get(1));
        playedTiles.put("7-11", tiles.get(2));
        playedTiles.put("7-12", tiles.get(3));
        playedTiles.put("7-13", tiles.get(4));
        int[] indexes = instance.getColIndexes(playedTiles);
        int rowColIndex = 7; // tiles on column 8
        int orientation = 0; // vertical
        instance.addLeadingTiles(indexes, orientation, rowColIndex, playedTiles);
        assert(playedTiles.size() == 9);
        board.addWordsToBoard(playedTiles);
        
        
        tiles.clear();
        playedTiles.clear();
        
        tiles = bag.replacePlayedTiles(3);
        playedTiles.put("8-12", tiles.get(0));
        playedTiles.put("9-12", tiles.get(1));
        playedTiles.put("10-12", tiles.get(2));
        
        int[] indexesb = instance.getRowIndexes(playedTiles);
        rowColIndex = 12;
        orientation = 1;
        instance.addLeadingTiles(indexesb, orientation, rowColIndex, playedTiles);
        assert(playedTiles.size() == 4);
        board.addWordsToBoard(playedTiles);
    }
    @Test
    public void testAddTrailingTiles(){
        System.out.println("TESTING: DCGENERICSCRABBLEPLAY: ADDTRAILINGTILES() FUNCTION...");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay instance = new DCGenericScrabblePlay(board);
//        instance.setBoard(board);
        board.setDebug(true);
        instance.setDebug(true);
        
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-8", tiles.get(0));
        playedTiles.put("8-8", tiles.get(1));
        playedTiles.put("9-8", tiles.get(2));
        playedTiles.put("7-8", tiles.get(3));
        board.addWordsToBoard(playedTiles);
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
        board.addWordsToBoard(playedTiles);
        assert(playedTiles.size() == 9);
        
    }
    
    @Test
    public void testFillInGap(){
        System.out.println("TESTING: DCGENERICSCRABBLEPLAY: FILLINGAP() FUNCTION...");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay instance = new DCGenericScrabblePlay(board);
//        instance.setBoard(board);
        board.setDebug(true);
        instance.setDebug(true);
        
        DCGenericTileBag bag = new DCGenericTileBag();
        List<DCGenericTile> tiles = bag.replacePlayedTiles(4);
        Map<String,DCGenericTile> playedTiles = new HashMap<String,DCGenericTile>(4);
        playedTiles.put("6-7", tiles.get(0));
        playedTiles.put("8-7", tiles.get(1));
        playedTiles.put("9-7", tiles.get(3));
        playedTiles.put("7-7", tiles.get(2));
        instance.isValid(playedTiles);
        System.out.println("word value is: "+instance.getPlayValue());
//        board.addWordsToBoard(playedTiles);
        tiles.clear();
        playedTiles.clear();
        
        
        tiles = bag.replacePlayedTiles(5);
        playedTiles.put("10-7", tiles.get(0));
        playedTiles.put("11-7", tiles.get(1));
        playedTiles.put("3-7", tiles.get(2));
        playedTiles.put("4-7", tiles.get(3));
        playedTiles.put("5-7", tiles.get(4));
        
        int[] indexes = instance.getRowIndexes(playedTiles);
        int rowColIndex = 8; // tiles on column 8
        int orientation = 1; // vertical
//        instance.fillInGaps(indexes, orientation, rowColIndex, playedTiles);
        boolean valid = instance.isValid(playedTiles);
        assert(playedTiles.size() == 9 && valid);
        System.out.println("word value is: "+instance.getPlayValue());
        
        int[] row = new int[9];
        int rowIndex = 0;
        for(String key: playedTiles.keySet()){
            String[] column = key.split("-");
            assert(Integer.parseInt(column[1]) == 7);
            row[rowIndex] = Integer.parseInt(column[0]);
            rowIndex++;
        }
        
        for(int a =0; a < row.length; a++){
            for (int b = 0; b < row.length-1; b++){
                if ( row[b] > row[b+1]){
                    int temp = row[b];
                    row[b] = row[b+1];
                    row[b+1] = temp;
                } 
            }
        }
        
        for (int c = 0; c < row.length-1; c++){
//            System.out.println("row: "+row[c] + "\tnext: "+row[c+1]);
            assert (row[c] == row[c+1] -1 );
        }
    }
    
    @Test
    public void playGame(){
        System.out.println("------------------TEST playGame()------------------");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        DCGenericScrabblePlay instance = new DCGenericScrabblePlay(board);
        instance.setBoard(board);
        instance.setDebug(true);
        DCGenericTileBag tileBag = new DCGenericTileBag();
        
        List<DCGenericTile> listTiles = tileBag.replacePlayedTiles(4);
        Map<String,DCGenericTile> mapPlay = new HashMap<>();
        mapPlay.put("7-5", listTiles.get(0));
        mapPlay.put("7-6", listTiles.get(1));
        mapPlay.put("7-7", listTiles.get(2));
        mapPlay.put("7-8", listTiles.get(3));
        System.out.println("\tTesting 1st play: \n\t\ttiles played: "+mapPlay.toString());
        boolean blnPlay = instance.isValid(mapPlay);
        System.out.println("\t\tplayed words: "+instance.getPlayWords().toString());
        board.printBoard();
        List<DCScrabbleWordStats> stats = instance.getPlayStats();
        for(DCScrabbleWordStats stat: stats){
            System.out.println("played word: "+stat.getWord()+"\t, score: "+stat.getWordValue());
        }
        assert(blnPlay);
        assert(instance.getPlayWords().size() == 1);
        
        
        listTiles.clear();
        mapPlay.clear();
        listTiles = tileBag.replacePlayedTiles(2);
        mapPlay.put("7-9", listTiles.get(0));
        mapPlay.put("6-9", listTiles.get(1));
        System.out.println("\tTesting 2nd play: \n\t\ttiles played: "+mapPlay.toString());
        blnPlay = instance.isValid(mapPlay);
        System.out.println("\t\tplayed words: "+instance.getPlayWords().toString());
        board.printBoard();
        assert(blnPlay);
        assert(instance.getPlayWords().size() == 2);
        stats = instance.getPlayStats();
        for(DCScrabbleWordStats stat: stats){
            System.out.println("played word: "+stat.getWord()+"\t, score: "+stat.getWordValue());
        }
        
        listTiles.clear();
        mapPlay.clear();
        listTiles = tileBag.replacePlayedTiles(7);
        mapPlay.put("8-7", listTiles.get(0));
        mapPlay.put("8-8", listTiles.get(1));
        mapPlay.put("8-9", listTiles.get(2));
        mapPlay.put("8-10", listTiles.get(3));
        mapPlay.put("8-11", listTiles.get(4));
        mapPlay.put("8-12", listTiles.get(5));
        mapPlay.put("8-13", listTiles.get(6));
        System.out.println("\tTesting 3rd play: \n\t\ttiles played: "+mapPlay.toString());
        blnPlay = instance.isValid(mapPlay);
        System.out.println("\t\tplayed words: "+instance.getPlayWords().toString());
        board.printBoard();
        assert(blnPlay);
        assert(instance.getPlayWords().size() == 4);
        stats = instance.getPlayStats();
        for(DCScrabbleWordStats stat: stats){
            System.out.println("played word: "+stat.getWord()+"\t, score: "+stat.getWordValue());
        }
        
        listTiles.clear();
        mapPlay.clear();
        listTiles = tileBag.replacePlayedTiles(3);
        mapPlay.put("7-12", listTiles.get(0));
        mapPlay.put("9-12", listTiles.get(1));
        mapPlay.put("10-12", listTiles.get(2));
        System.out.println("\tTesting 4th play: \n\t\ttiles played: "+mapPlay.toString());
        blnPlay = instance.isValid(mapPlay);
        System.out.println("\t\tplayed words: "+instance.getPlayWords().toString());
        board.printBoard();
        assert(blnPlay);
        assert(instance.getPlayWords().size() == 1);
        stats = instance.getPlayStats();
        for(DCScrabbleWordStats stat: stats){
            System.out.println("played word: "+stat.getWord()+"\t, score: "+stat.getWordValue());
        }

        listTiles.clear();
        mapPlay.clear();
        listTiles = tileBag.replacePlayedTiles(1);
        mapPlay.put("7-13", listTiles.get(0));
        System.out.println("\tTesting 5th play: \n\t\ttiles played: "+mapPlay.toString());
        blnPlay = instance.isValid(mapPlay);
        System.out.println("\t\tplayed words: "+instance.getPlayWords().toString());
        board.printBoard();
        assert(blnPlay);
        assert(instance.getPlayWords().size() == 2);
        stats = instance.getPlayStats();
        for(DCScrabbleWordStats stat: stats){
            System.out.println("played word: "+stat.getWord()+"\t, score: "+stat.getWordValue());
        }

        listTiles.clear();
        mapPlay.clear();
        listTiles = tileBag.replacePlayedTiles(1);
        mapPlay.put("11-12", listTiles.get(0));
        System.out.println("\tTesting 6th play: \n\t\ttiles played: "+mapPlay.toString());
        blnPlay = instance.isValid(mapPlay);
        System.out.println("\t\tplayed words: "+instance.getPlayWords().toString());
        board.printBoard();
        assert(blnPlay);
        assert(instance.getPlayWords().size() == 1);
        stats = instance.getPlayStats();
        for(DCScrabbleWordStats stat: stats){
            System.out.println("played word: "+stat.getWord()+"\t, score: "+stat.getWordValue());
        }

        listTiles.clear();
        mapPlay.clear();
        listTiles = tileBag.replacePlayedTiles(1);
        mapPlay.put("7-4", listTiles.get(0));
        System.out.println("\tTesting 7th play: \n\t\ttiles played: "+mapPlay.toString());
        blnPlay = instance.isValid(mapPlay);
        System.out.println("\t\tplayed words: "+instance.getPlayWords().toString());
        board.printBoard();
        assert(blnPlay);
        assert(instance.getPlayWords().size() == 1);
        stats = instance.getPlayStats();
        for(DCScrabbleWordStats stat: stats){
            System.out.println("played word: "+stat.getWord()+"\t, score: "+stat.getWordValue());
        }
        
        listTiles.clear();
        mapPlay.clear();
        listTiles = tileBag.replacePlayedTiles(3);
        mapPlay.put("7-3", listTiles.get(0));
        mapPlay.put("8-4", listTiles.get(1));
        mapPlay.put("8-5", listTiles.get(2));
        System.out.println("\tTesting 8th play: \n\t\ttiles played: "+mapPlay.toString());
        blnPlay = instance.isValid(mapPlay);
        System.out.println("\t\tplayed words: "+instance.getPlayWords().toString());
        assert(!blnPlay);
        assert(instance.getPlayWords().size() == 0);
        board.printBoard();
        stats = instance.getPlayStats();
        for(DCScrabbleWordStats stat: stats){
            System.out.println("played word: "+stat.getWord()+"\t, score: "+stat.getWordValue());
        }

        listTiles.clear();
        mapPlay.clear();
        listTiles = tileBag.replacePlayedTiles(3);
        mapPlay.put("5-9", listTiles.get(0));
        mapPlay.put("9-9", listTiles.get(1));
        mapPlay.put("10-9", listTiles.get(2));
        System.out.println("\tTesting 8th play: \n\t\ttiles played: "+mapPlay.toString());
        blnPlay = instance.isValid(mapPlay);
        System.out.println("\t\tplayed words: "+instance.getPlayWords().toString());
        assert(blnPlay);
        assert(instance.getPlayWords().size() == 1);
        board.printBoard();
        stats = instance.getPlayStats();
        for(DCScrabbleWordStats stat: stats){
            System.out.println("played word: "+stat.getWord()+"\t, score: "+stat.getWordValue());
        }
    }
}
