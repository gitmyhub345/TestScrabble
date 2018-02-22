/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.comp;

import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Rider1
 */
public class intArraySortNGTest {
    
    public intArraySortNGTest() {
    }

    @Test
    public void testMaxvalue() {
    }

    @Test
    public void testSortAsc() {
    }

    @Test
    public void testSortDes() {
    }

    @Test
    public void testSortAscII() {
    }

    @Test
    public void testOrderMapKeys() {
        intArraySort instance = new intArraySort();
        List<String> keys = new ArrayList<>();
        keys.add("6-12");
        keys.add("5-12");
        keys.add("3-12");
        keys.add("7-12");
        keys.add("1-12");
        keys.add("15-12");
        keys.add("12-12");

        instance.orderMapKeys(keys);
        System.out.println(keys.toString());
        
        List<String> keys2 = new ArrayList<>();
        keys2.add("10-5");
        keys2.add("10-3");
        keys2.add("10-2");
        keys2.add("10-9");
        keys2.add("10-10");
        keys2.add("10-13");
        keys2.add("10-1");
        instance.orderMapKeys(keys2);
        System.out.println(keys2.toString());
        
    }
    
}
