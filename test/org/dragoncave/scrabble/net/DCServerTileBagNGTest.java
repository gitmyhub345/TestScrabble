/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.scrabble.net;

import org.dragoncave.home.scrabble.net.DCServerTileBag;
import org.dragoncave.home.scrabble.net.DCServerTile;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author Rider1
 */
public class DCServerTileBagNGTest {
    
    public DCServerTileBagNGTest() {
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
     * Test of getRandomTile method, of class DCServerTileBag.
     */
    @Test
    public void testGetRandomTile() {
        System.out.println("getRandomTile");
        DCServerTileBag instance = new DCServerTileBag();
        Map<String,DCServerTile> tileMap = new HashMap<>();
        for (int index = 0; index < 103; index++){
            DCServerTile result = instance.getTile();
            if (result != null){
                if(tileMap.containsKey(result.getTileID())){
                    fail("tileID already exists");
                }
                tileMap.put(result.getTileID(), result);
                System.out.println(result.toString());
            }
            else
            {
                assert(result == null);
                System.out.println("\ttileIndex = "+index+" generated null tile.");
//                fail("generated an invalid tile");
            }
        }
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of returnTilesToPool method, of class DCServerTileBag.
     */
    @Test
    public void testReturnTilesToPool() {
        System.out.println("returnTilesToPool");
        List<DCServerTile> tiles = new ArrayList<>();
        DCServerTileBag instance = new DCServerTileBag();
        Random rand = new Random();
        rand.setSeed(new java.util.Date().getTime());
        int numTile = Math.abs(rand.nextInt()%100);
        System.out.println("this round will generate and return "+numTile+" tile(s).");
        for (int index = 0; index < numTile; index++){
            tiles.add(instance.getTile());
        }
        assert(tiles.size() == numTile);
        boolean result = instance.returnTilesToPool(tiles.toString());
        assert(result == true);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of resetTileBag method, of class DCServerTileBag.
     */
    @Test
    public void testResetTileBag() {
        System.out.println("resetTileBag");
        List<DCServerTile> tiles = new ArrayList<>();
        Random rand = new Random();
        rand.setSeed(new java.util.Date().getTime());
        int numTile = Math.abs(rand.nextInt()%100);
        System.out.println("this round will generate and return "+numTile+" tile(s).");
        DCServerTileBag instance = new DCServerTileBag();
        for (int index = 0; index < numTile; index++){
            DCServerTile tile = instance.getTile();
            tiles.add(new DCServerTile(tile.toString()));
        }
        instance.resetTileBag();
        for (DCServerTile tile: tiles){
            DCServerTile t = instance.getTile(tile.getTileIndex());
            System.out.println("\t"+tile.toString());
            System.out.println("\t"+t.toString());
            assert(t.getTileIndex() == tile.getTileIndex());
            assert((t.isIsInPool() != tile.isIsInPool()) == true);
            assert(t.getTileID().equals(tile.getTileID()));
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of loadTileBag method, of class DCServerTileBag.
     */
//    @Test
//    public void testLoadTileBag() {
//        System.out.println("loadTileBag");
//        DCServerTileBag instance = new DCServerTileBag();
//        instance.loadTileBag();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of saveTileBag method, of class DCServerTileBag.
     */
//    @Test
//    public void testSaveTileBag() {
//        System.out.println("saveTileBag");
//        DCServerTileBag instance = new DCServerTileBag();
//        instance.saveTileBag();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
