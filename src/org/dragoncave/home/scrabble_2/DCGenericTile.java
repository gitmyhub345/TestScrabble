/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.io.StringReader;
import java.util.HashMap;
import org.json.*;

/**
 *
 * @author Rider1
 */
public class DCGenericTile {
    private final static HashMap<String,Integer> genericTileMap = new HashMap<String,Integer>() {{
            put("*", 0);
            put("E", 1);
            put("A", 1);
            put("I", 1);
            put("O", 1);
            put("N", 1);
            put("R", 1);
            put("T", 1);
            put("L", 1);
            put("S", 1);
            put("U", 1);
            put("D", 2);
            put("G", 2);
            put("B", 3);
            put("C", 3);
            put("M", 3);
            put("P", 3);
            put("F", 4);
            put("H", 4);
            put("V", 4);
            put("W", 4);
            put("Y", 4);
            put("K", 5);
            put("J", 8);
            put("X", 8);
            put("Q", 10);
            put("Z", 10);
        }};
    private String letter;
    private boolean isPlacedOnBoard;
    private boolean isInPool;
    private boolean isDraggable;
    private int tileNumber;
    private boolean debug = false;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public boolean isIsPlacedOnBoard() {
        return isPlacedOnBoard;
    }

    public void setIsPlacedOnBoard(boolean isPlacedOnBoard) {
        this.isPlacedOnBoard = isPlacedOnBoard;
    }

    public boolean isIsInPool() {
        return isInPool;
    }

    public void setIsInPool(boolean isInPool) {
        this.isInPool = isInPool;
    }

    public boolean isIsDraggable() {
        return isDraggable;
    }

    public void setIsDraggable(boolean isDraggable) {
        this.isDraggable = isDraggable;
    }

    public int getTileNumber() {
        return tileNumber;
    }

    public void setTileNumber(int tileNumber) {
        this.tileNumber = tileNumber;
    }
    

    public DCGenericTile(String letter, int tileNumber) {
        this.letter = letter;
        this.tileNumber = tileNumber;
        initSettings();
        
    }

    public DCGenericTile(String tileSpecifics){
        /*
        Overloaded constructor - this one is used to construct a tile with spcific settings.
        */
        String[] settingArray = tileSpecifics.split(":");
        if (settingArray.length != 6){
            if (debug)
                System.out.println("DCTile: DCTile(String) -> not enough parameters to create a tile.");
        }
        // no need to iterate over the settings array
        //"DCTile:"+this.letter+":"+this.tileNumber+":"+this.isDraggable+":"+this.isInPool+":"+this.isPlaced;
        this.letter = settingArray[1];
        this.tileNumber = Integer.parseInt(settingArray[2]);
        this.isDraggable = Boolean.parseBoolean(settingArray[3]);
        this.isInPool = Boolean.parseBoolean(settingArray[4]);
        this.isPlacedOnBoard = Boolean.parseBoolean(settingArray[5]);
    }
    
    private int calculateTileValue(){
        return genericTileMap.get(this.letter);
    }
    
    public int getValue(){
        return calculateTileValue();
    }
    
    public void setDebug(boolean debug){
        this.debug = debug;
    }
    
    private void initSettings(){
        this.isDraggable = true;
        this.isPlacedOnBoard = false;
        this.isInPool = true;
    }
    
    public void resetTile(){
//        System.out.println("resetting tile");
        initSettings();
    }
    
    public void toggleDraggable(){
        this.isDraggable = !this.isDraggable;
    }
    
    public void removeFromPool(){
        this.isInPool = false;
    }
    
    @Override
    public String toString(){
        return "DCGenericTile:"+this.tileNumber+":"+this.getLetter()+":"+this.getValue()+":"+this.isDraggable+":"+this.isInPool+":"+this.isPlacedOnBoard;
//        return getJsonString();
    }
    
    public String getJsonString(){
        return toJson().toString();
    }

    private JSONObject toJson(){
        JSONObject jobj = new JSONObject();
            jobj.put("letter", this.letter);
            jobj.put("tileNumber", this.tileNumber);
            jobj.put("isInPool", this.isInPool);
            jobj.put("isDraggable", this.isDraggable);
            jobj.put("isPlacedOnBoard", this.isPlacedOnBoard);
//            jobj.put("value",getValue());
        return jobj;
    }
    
    public boolean fromJsonString(String jsonString){
        if (debug){
            System.out.println("DCGENERICTILE: fromJsonString(String) ->");
        }
        boolean result = false;
        JSONObject jobj = new JSONObject(jsonString);
        if (genericTileMap.get(jobj.getString("letter"))!= null &&
                jobj.getInt("tileNumber") > -1 && jobj.getInt("tileNumber") < 100 ) {
            this.letter = jobj.getString("letter");
            this.tileNumber = jobj.getInt("tileNumber");
            this.isDraggable = jobj.getBoolean("isDraggable");
            this.isInPool = jobj.getBoolean("isInPool");
            this.isPlacedOnBoard = jobj.getBoolean("isPlacedOnBoard");
            result = true;
        }
        return result;
    } 
}
