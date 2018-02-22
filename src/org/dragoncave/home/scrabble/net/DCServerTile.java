/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble.net;

import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;
import java.util.HashMap;
/**
 *
 * @author Rider1
 */
public class DCServerTile {

    private String tileID;
    private String letter;
    private int value;
    private boolean isInPool;
    private int tileIndex;
    private boolean isDraggable;
    private HashMap<String,Integer> tileMap; 
    
    public DCServerTile(String letter, int index){
        if (letter.length() > 1){
            this.letter = letter.substring(0, 1).toUpperCase();
        } else
            this.letter = letter.toUpperCase();
        
        if (index<0 || index > 99){
            throw new IndexOutOfBoundsException("Index used to create tile is out of bounds");
        } else
            this.tileIndex = index;
        
        this.tileID = "DCTile"+tileIndex;
        
        this.isDraggable = true;
        this.isInPool = true;

        tileMap = new HashMap<>();
        tileMap.put("*", 0);
        tileMap.put("E", 1);
        tileMap.put("A", 1);
        tileMap.put("I", 1);
        tileMap.put("O", 1);
        tileMap.put("N", 1);
        tileMap.put("R", 1);
        tileMap.put("T", 1);
        tileMap.put("L", 1);
        tileMap.put("S", 1);
        tileMap.put("U", 1);
        tileMap.put("D", 2);
        tileMap.put("G", 2);
        tileMap.put("B", 3);
        tileMap.put("C", 3);
        tileMap.put("M", 3);
        tileMap.put("P", 3);
        tileMap.put("F", 4);
        tileMap.put("H", 4);
        tileMap.put("V", 4);
        tileMap.put("W", 4);
        tileMap.put("Y", 4);
        tileMap.put("K", 5);
        tileMap.put("J", 8);
        tileMap.put("X", 8);
        tileMap.put("Q", 10);
        tileMap.put("Z", 10);
        
        this.value = tileMap.get(this.letter);
    }
    
    private void resetTile(){
        this.isInPool = true;
        this.isDraggable = true;
        
    }

    private void toggleDraggable(){
        this.isDraggable = !this.isDraggable;
    }
    
    public String getLetter() {
        return letter;
    }
/*
    public void setLetter(String letter) {
        this.letter = letter;
    }
*/
    public int getValue() {
        return value;
    }
/*
    public void setValue(int value) {
        this.value = value;
    }
*/
    public boolean isIsInPool() {
        return isInPool;
    }
/*
    public void setIsInPool(boolean isInPool) {
        this.isInPool = isInPool;
    }
*/
    public int getTileIndex() {
        return tileIndex;
    }
/*
    public void setTileIndex(int tileIndex) {
        this.tileIndex = tileIndex;
    }
*/
    public boolean isIsDraggable() {
        return isDraggable;
    }

    public void setIsDraggable(boolean isDraggable) {
        this.isDraggable = isDraggable;
    }
    
    public void removeFromPool(){
        this.isInPool = false;
    }
    
    public void returnToPool(){
        resetTile();
    }
    
    public String getTileID(){
        return tileID;
    }
    
    @Override
    public String toString(){
        return this.tileID+":"+this.letter+":"+this.tileIndex+":"+this.isInPool+":"+this.isDraggable+":"+this.value;
    }
    
    public DCServerTile(String tileValues){
        String[] tileAttributes = tileValues.split(":");
        if (tileAttributes.length != 6){
            throw new IllegalArgumentException("Attempted to create object with the wrong number of arguments from parsed string.");
        }
        
        this.tileID = tileAttributes[0];
        this.letter = tileAttributes[1];
        this.tileIndex = Integer.parseInt(tileAttributes[2]);
        this.isInPool = Boolean.parseBoolean(tileAttributes[3]);
        this.isDraggable = Boolean.parseBoolean(tileAttributes[4]);
        this.value = Integer.parseInt(tileAttributes[5]);
    }
}
