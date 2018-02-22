/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble;

/**
 *
 * @William J
 */
public class Body {
    private static DCScrabbleBoard body;
    public static DCScrabbleBoard getBody() {
        if (body == null)
            body = new DCScrabbleBoard();
        return body;
    }    
}
