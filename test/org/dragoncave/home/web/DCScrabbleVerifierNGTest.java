/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.web;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Will J
 */
public class DCScrabbleVerifierNGTest {
    
    public DCScrabbleVerifierNGTest() {
    }

    @Test
    public void testVerifyWord() throws Exception {
        DCScrabbleVerifier v = new DCScrabbleVerifier();
        v.setDebug(true);
        boolean result = v.verifyWord("DOT");
        assert(result);
    }
    
}
