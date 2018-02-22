/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.comp;

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
public class TestPropFileNGTest {
    
    public TestPropFileNGTest() {
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
     * Test of openfile method, of class TestPropFile.
     */
    @Test
    public void testOpenfile() throws Exception {
        System.out.println("openfile");
        String filename = "testdict.txt";
        TestPropFile instance = new TestPropFile();
        instance.openfile(filename);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readFile method, of class TestPropFile.
     */
    @Test
    public void testReadFile_byteArr() throws Exception {
        System.out.println("readFile");
        byte[] a = new byte[500];
        TestPropFile instance = new TestPropFile();
        instance.openfile("testdict.txt");
        boolean expResult = true;
        boolean result = false;
        while (!(result = instance.readFile(a))){
            String str = new String(a).trim();
            String[] strA = str.split("\r\n");
            for(String b: strA)
                System.out.println(b);
        }
        instance.closeFile();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of closeFile method, of class TestPropFile.
     */
    @Test
    public void testCloseFile() {
        System.out.println("closeFile");
        TestPropFile instance = new TestPropFile();
        instance.closeFile();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readFile method, of class TestPropFile.
     */
    @Test
    public void testReadFile_String() throws Exception {
        System.out.println("readFile");
    //    String filename = "";
        String text = "";
        TestPropFile instance = new TestPropFile();
        instance.openfile("testdict.txt");
        boolean expResult = false;
        boolean result = instance.readFile(text);
        while (result){
            result = instance.readFile(text);
            System.out.println(text);
        }
        //assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
