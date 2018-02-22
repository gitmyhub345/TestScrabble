/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.json.JSONArray;
import org.json.JSONObject;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Rider1
 */
public class DCGenericTileBagNGTest {
    
    public DCGenericTileBagNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of createTileBag method, of class DCGenericTileBag.
     */
    @Test
    public void testCreateTileBag(){
        DCGenericTileBag instance = new DCGenericTileBag();
        assertEquals(instance.getTilesInBag(),100);
    }
    
    @Test
    public void testGetHand() {
        DCGenericTileBag instance = new DCGenericTileBag();
        int numberHands = 100 / 7 + 1;
        int remainder = 100 % 7;
        int numberOfTiles = 0;
        int handsCreated = 0;
        int handsRemainder = 0;
        int[] issuedTiles = new int[101];
        while (instance.getTilesInBag() > 0){
            handsCreated++;
            List<DCGenericTile> hand = instance.getHand();
            numberOfTiles += hand.size();
            System.out.println("hand size: ->"+hand.size());
            hand.stream().forEach((tile)->{
                issuedTiles[tile.getTileNumber()]=1;
                System.out.println("\t"+tile.toString());
            });
            handsRemainder = hand.size();
        }
        
        assertEquals(numberHands,handsCreated);
        assertEquals(remainder,handsRemainder);
        assertEquals(numberOfTiles,100);
        for(int index = 0; index < issuedTiles.length; index++){
            System.out.println("tile index: "+index+"\t"+issuedTiles[index]);
            if(index < 100)
                assertEquals(issuedTiles[index],1);
            else
                assertEquals(issuedTiles[index],0);
        }
    }
    
    @Test
    public void testReplacePlayedTiles(){
        DCGenericTileBag instance = new DCGenericTileBag();
        
        List<DCGenericTile> hand = new ArrayList(0);
        
        hand.addAll(instance.getHand());
        Random rand = new Random();
        rand.setSeed(new Date().getTime());
        int numTilesReplaced = hand.size();
        for (int index = 0; index < 11; index++ ){
            int randNumTiles = Math.abs(rand.nextInt()%7);
            numTilesReplaced += randNumTiles;
            System.out.println("random num of Tiles: "+randNumTiles + "\tnumbTilesReplaced: "+numTilesReplaced);
            hand.addAll(instance.replacePlayedTiles(randNumTiles));
//            hand.addAll(instance.replacePlayedTiles(0));
            System.out.println("number tiles Replace: "+numTilesReplaced);
            System.out.println("hand size: "+hand.size());
            assert(hand.size()+instance.getTilesInBag() == 100);
        }
        assertEquals(numTilesReplaced,hand.size());
        
    }
    
    @Test
    public void testReturnTilesToBag(){
        DCGenericTileBag instance = new DCGenericTileBag();
        instance.setDebug(true);
        List<DCGenericTile> tiles = new ArrayList(0);
        
        for (int times =0; times < 5; times++){
            tiles.addAll(instance.getHand());
        }
        
        assert(tiles.size() == 35);
        assert(tiles.size() + instance.getTilesInBag() == 100);
        
        Random rand = new Random();
        rand.setSeed(new Date().getTime());
        
        for (int rounds = 0; rounds < 5; rounds ++){
            int nReturnToBagTiles = Math.abs(rand.nextInt()%7);
            List<DCGenericTile> returnTiles = getRandomReturnTileList(nReturnToBagTiles, tiles);
            instance.returnTilesToBag(returnTiles);
            assert(instance.getTilesInBag()+tiles.size() == 100);
        }
    }
    
    @Test
    public void testReturnTilesToBag2(){
        DCGenericTileBag instance = new DCGenericTileBag();
        instance.setDebug(true);
        
        List<DCGenericTile> tiles = new ArrayList(0);
        
        tiles.add(new DCGenericTile("A",1));
        
        instance.returnTilesToBag(tiles);
    }
    
    private List<DCGenericTile> getRandomReturnTileList(int numTiles, List<DCGenericTile> tileList){
        List<DCGenericTile> hand = new ArrayList(0);
        Random rand = new Random();
        rand.setSeed(new Date().getTime());
        for (int index = 0; index < numTiles; index++){
            int tileIndex = Math.abs(rand.nextInt()%tileList.size());
            hand.add(tileList.get(tileIndex));
            tileList.remove(tileIndex);
        }
        return hand;
    }
    
    @Test
    public void testGetJsonHand(){
        DCGenericTileBag instance = new DCGenericTileBag();
        instance.setDebug(true);
        
        String json = instance.getHandString();
        JsonReader jReader = Json.createReader(new StringReader(json));
        JsonObject jObj = jReader.readObject();
        
        JsonArray jArray = jObj.getJsonArray("hand");
        System.out.println("Json Hand: number of tiles:\t"+jArray.size());
        for (int tileNum = 0; tileNum < jArray.size(); tileNum++){
            System.out.println("tile number "+ (tileNum+1) + ":\t"+jArray.getJsonObject(tileNum));
        }
        System.out.println(jObj);
    }
    
    @Test
    public void testTileBag(){
        DCGenericTileBag bag = new DCGenericTileBag();
        
        List<DCGenericTile> tiles = bag.getTileList();
        
        tiles.stream().forEach((tile)->{
            assert((tile.isIsDraggable() == true) &&
                    (tile.isIsInPool() == true) &&
                    (tile.isIsPlacedOnBoard() == false)
                    );
        });
        
        List<DCGenericTile> hand = bag.getHand();
        hand.stream().forEach((tile)->{
            int index = tile.getTileNumber();
            
            assert(tile.isIsDraggable() == true && tiles.get(index).isIsDraggable() == true);
            assert(tile.isIsInPool() == false && tiles.get(index).isIsInPool() == false);
            assert(tile.isIsPlacedOnBoard() == false && tiles.get(index).isIsPlacedOnBoard() == false);
            assert(tile.getLetter().equals(tiles.get(index).getLetter()));
            assert(index == tiles.get(index).getTileNumber());
        });
    }
    
    @Test
    public void testReplacePlayedTilesString(){
        System.out.println("------------------TEST replacePlayedTilesString function--------------");
        DCGenericTileBag instance = new DCGenericTileBag();
        instance.setDebug(true);
        int numTilesPlayed = 3;
        String json = instance.replacePlayedTilesString(numTilesPlayed);
//        System.out.println("received json string: "+json);
//        JsonReader jReader = Json.createReader(new StringReader(json));
//        JsonObject jObj = jReader.readObject();
        JSONObject jObj = new JSONObject(json);
        JSONArray jArray = jObj.getJSONArray("hand");
        System.out.println("\tJson Hand: number of tiles:\t"+jArray.length());
        assert(jArray.length() == numTilesPlayed);
        for (int tileNum = 0; tileNum < jArray.length(); tileNum++){
            System.out.println("\t\ttile number "+ (tileNum+1) + ":\t"+jArray.get(tileNum));
        }
        System.out.println("\t"+jObj.toString());
    }
    
    @Test
    public void testToJSONArray(){
       System.out.println("----------------TEST toJSONArray----------------------");
       DCGenericTileBag tilebag = new DCGenericTileBag();
       tilebag.setDebug(true);
       
       JSONArray tileJSONArray = tilebag.toJSONArray();
       
       tileJSONArray.forEach((jobj)->{
           System.out.println(jobj);
       });
       assert(tileJSONArray.length() == 100);
    }
    
}
