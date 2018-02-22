/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble.net;

/**
 *
 * @author Rider1
 */
public enum DCServerRequestType {
    RESETHAND("RESETHAND"), 
    PLAYEDTILES("PLAYEDTILES"), 
    POPULATE("POPULATE"),
    GETTILE("GETTILE"),
    REPLAYEDTILES("REPLAYEDTILES"),
    NEWGAME("NEWGAME"),
    UPDATEBOARD("UPDATEBOARD"),
    JOIN("JOIN"),
    INITBOARD("INITBOARD"),
    MESSAGE("MESSAGE"),
    NEWORDER("NEWORDER");
    
    private final String name;
    
    private DCServerRequestType(String s){
        name = s;
    }
    
    public boolean equals(String othername){
        return name.equals(othername);
    }
    
    public String toString(){
        return this.name;    
    }
}
