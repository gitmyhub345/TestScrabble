/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Rider1
 */
public class RandomMapNGTest {
    
    public RandomMapNGTest() {
    }

    @Test
    public void testGetNextTileHolderID() {
    }

    @Test
    public void testGenerateRandomHand() {
        System.out.println("TEST: TESTGENERATERANDROMHAND()->");
        DCGenericScrabbleBoard board = new DCGenericScrabbleBoard();
        RandomMap map = new RandomMap(board);
        map.setDebug(true);
        List<String> tileHolderID = map.generateRandomHand3();
        
        for (int index = 0; index < tileHolderID.size(); index++){
            System.out.println(tileHolderID.get(index));
        }
    }
    
}
