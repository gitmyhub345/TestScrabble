/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble;

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
 * @author William J
 */
public class DCTile extends Label{
    private boolean debug;
    private String letter;
    private int value;
    private boolean isPlayed;
    private boolean isInPool;
    private int arrayIndex;
    private boolean isDraggable;

    private HashMap<String,Integer> tileMap;
    
    public void setDebug(boolean debug){
        this.debug = debug;
    }
    
    public String getTileID(){
        return super.getId();
    }
    
    public Node getNode(){
        return this;
    }
    public boolean getInPool(){
        return isInPool;
    }
    public void setIsInPool(boolean isInPool){
        this.isInPool = isInPool;
    }
    public void removeFromPool(){
        isInPool = false;
    }
    public void returnedFromHand(){
        isInPool = true;
    }
    public void setIsPlayed(){
        isPlayed = true;
    }
    public DCTile(String letter, int index){
        super.getStyleClass().add("dcTile");
        this.letter = letter;
        this.isPlayed = false;
        this.isInPool = true;
        this.arrayIndex = index;
        this.isDraggable = true;

        super.setText(letter);
        super.setMinWidth(21);
        super.setMinHeight(21);
        super.setMaxWidth(21);
        super.setMaxHeight(21);
        super.setFont(new Font("Arial",12));
        super.setId("DCTile"+index);

        initTileMap();
        this.value = tileMap.get(this.letter);
        toggleDrag(this.isDraggable);
    }
    
    private void initTileMap(){
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
        
    }
    
    public int getValue(){
        return this.value;
    }
    
    public String getLetter(){
        return this.letter;
    }
    
    public boolean isPlayed(){
        return this.isPlayed();
    }
    public void setDraggable(boolean draggable){
        toggleDrag(draggable);
    }
    
    private void toggleDrag(boolean draggable){
        if (draggable){
            this.setOnDragDetected((MouseEvent event)->{
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
            this.setStyle("-fx-background-color: #eeee55;");
            this.setOnDragDetected((MouseEvent event)->{});
        }
    }
    public void resetTile(){
        if (debug){
            System.out.println("resetting tile "+this.getTileID()+"...");
        }
        this.isDraggable = true;
        this.isInPool = true;
        this.isPlayed = false;
        super.setStyle("-fx-background-color: #55ff99;");
        setDraggable(true);
        if (debug){
            System.out.println("done resetting tile "+this.getTileID()+".");
        }
    }
    public boolean getIsInPool(){
        return isInPool;
    }
    
    @Override
    public String toString(){
        return this.getTileID()+":"+this.letter+":"+this.arrayIndex+":"+this.isInPool+":"+this.isDraggable+":"+this.value;
    }
    
    public DCTile(String tileValues){
        String[] tileAttributes = tileValues.split(":");
        if (tileAttributes.length != 6){
            throw new IllegalArgumentException("Attempted to create object with the wrong number of arguments from parsed string.");
        }
        super.getStyleClass().add("dcTile");
        super.setId(tileAttributes[0]);
        this.letter = tileAttributes[1];
        this.arrayIndex = Integer.parseInt(tileAttributes[2]);
        this.isInPool = Boolean.parseBoolean(tileAttributes[3]);
        this.isDraggable = Boolean.parseBoolean(tileAttributes[4]);
        this.value = Integer.parseInt(tileAttributes[5]);
        initTileMap();
        super.setText(letter);
        super.setMinWidth(21);
        super.setMinHeight(21);
        super.setMaxWidth(21);
        super.setMaxHeight(21);
        super.setFont(new Font("Arial",12));
        toggleDrag(this.isDraggable);
    }
}
