/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.comp;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author Rider1
 */

public final class RandID {
    
    private static Random rand;
    
    private static void generateRandID(){
        rand = new Random(new Date().getTime());
    }
    
    public final static long getRandAppID(){
        if (rand == null){
            generateRandID();
        }
        
        return getRandomNumber(100000000);
    }
    
    public final static long getRandFieldID(){
        if (rand == null){
            generateRandID();
        }

        return getRandomNumber(10000000);
    }
    
    public final static long getRandPageID(){
        if (rand == null){
            generateRandID();
        }

        return getRandomNumber(100000);
    }
    private static long getRandomNumber(long number){
        long l = rand.nextLong()%number;
        while (l < number/10 || l > number){
            l = Math.abs(rand.nextLong()%number);
        }
        return l;
    }
}
