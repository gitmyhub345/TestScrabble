/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.util.HashMap;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.Node;
import javafx.scene.text.Font;

/**
 *
 * @author William Jay
 */
public class DCTile extends Label{
    public static HashMap<String,Integer> hMap = new HashMap<String,Integer>(){{
        // initialize static hashmap
        /*
            is a static member doable for single/multi player game?
        */
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
    private String letter; // this is the letter of the tile
    private boolean isPlaced; // used to determine if the tile is place on board
    private boolean isDraggable; // switch draggable feature on and off
    private int tileNumber; // this is keep track of which tile it is from the tilebag - make sure there are no duplicates
    private boolean isInPool; // used to determine if this thile is no longer in the bag
    
    private boolean debug = true;

    public DCTile(int tilenumber, String letter){
        /*
        default constructor - used to just contruct a brand new tile for tilebag
        */
        this.letter = letter;
        this.tileNumber = tilenumber;
        
        initSettings();
        setSizeLook();
        handleDraggable();
    }
    
    public DCTile(String settings){
        /*
        Overloaded constructor - this one is used to construct a tile with spcific settings.
        */
        String[] settingArray = settings.split(":");
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
        this.isPlaced = Boolean.parseBoolean(settingArray[5]);
        
        setSizeLook();
        handleDraggable();
    }
    private void initSettings(){
        isPlaced = false;
        isDraggable = true;
        isInPool = false;

        handleDraggable();
    }
    
    public int getValue(){
        return hMap.get(this.letter);
    }
    
    private void setSizeLook(){
        super.setMinWidth(21);
        super.setMinHeight(21);
        super.setMaxWidth(21);
        super.setMaxHeight(21);
        super.setFont(new Font("Arial",12));
        super.setId("DCTile"+this.tileNumber);

    }
    public void toggleDraggable(){
        this.isDraggable = !this.isDraggable;
        handleDraggable();
    }
    
    private void handleDraggable(){
        
        if (this.isDraggable){
            if (debug)
                System.out.println("DCTile: toggleDrag()->"+this.isDraggable);
            // if is Draggable - set conditions - background color,
            this.setOnDragDetected((MouseEvent event)->{
                // implement draggable
                Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                if (debug){
                    System.out.println("\n\n------------------Drag Event-----------------\n\tsource is "+event.getSource().toString());
                    System.out.println("\ttileHolderID: "+this.getParent().toString()+"\n---------------------------------------------");
                }
                content.putString(super.getId());
                db.setContent(content);
                event.consume();
            });
        } else {
            if (debug)
                System.out.println("DCTiel: toggleDrag()->"+this.isDraggable);
            this.setOnDragDetected((MouseEvent event)->{
                // empty handler for non draggable tile
            });
        }
    }
    
    private String getTileString(){
        return "DCTile:"+this.letter+":"+this.tileNumber+":"+this.isDraggable+":"+this.isInPool+":"+this.isPlaced;
    }
    
    public void resetTile() {
        initSettings();
    }
    
    public String getString(){
        return getTileString(); 
    }
    
    public boolean getIsDraggable(){
        return this.isDraggable;
    }
    
    public String getLetter(){
        return this.letter;
    }
    
    public boolean getIsInPool(){
        return this.isInPool;
    }
    
    public boolean getIsPlaced(){
        return this.isPlaced;
    }
    
    public int getTileNumber(){
        return this.tileNumber;
    }
    
    public void setDebug(boolean debug){
        this.debug = debug;
    }
}
