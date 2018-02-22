/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.io.StringReader;
import org.json.JSONObject;

/**
 *
 * @author Rider1
 */
public class DCGenericTileHolder {
    private DCGenericTile tile;
    private String ID;
    private String modifierType;
    private int valueModifier;
    private boolean isDroppable;
    private boolean hasTile;
    private boolean isTileHolderActive;
    
    public DCGenericTileHolder(){
        this.tile = null;
        this.valueModifier = 1;
        this.isTileHolderActive = true;
        
        this.isDroppable = true;
        this.hasTile = false;
    }
    
    public boolean isActive(){
        return isTileHolderActive;
    }
    
    public void addTile(String jsonString){
        if (this.isDroppable){
            System.out.println("adding tile to tileHolder: "+this.getID()+"\twith json string: "+jsonString);
//            JsonReader reader = Json.createReader(new StringReader(jsonString));
            JSONObject jObj = new JSONObject(jsonString);
            tile = new DCGenericTile("A",1);
            tile.fromJsonString(jObj.toString());

            this.isDroppable = false;
            this.hasTile = true;
        } else {
            // error 
            System.out.println("DCGenericTileHolder: addTile()-> trying to add tile to holder which already has a tile in it");
        }
        System.out.println("finished adding tile to tileholder");
    }
    
    public void removeTile(){
        this.tile = null;
        this.isDroppable = true;
        this.hasTile = false;
    }
    
    public DCGenericTile getTile(){
        return this.tile;
    }
    
    public boolean hasChild(){
        return this.hasTile;
    }
    
    public void resetTileHolder(){
        
    }
    
    public boolean getIsDroppable(){
        return this.isDroppable;
    }
    
    public void toggleDrop(){
        this.isDroppable = !this.isDroppable;
    }
    
    public void setID(String id){
        this.ID = id;
    }
    
    public String getID(){
        return this.ID;
    }
    
    public void setValueMultiplier(int multiplier){
        this.valueModifier = multiplier;
    }
    
    public int getValueMultiplier(){
        return this.valueModifier;
    }
    
    public String getMultiplierType(){
        return this.modifierType;
    }
    
    public void toggleActive(){
        this.isTileHolderActive = !this.isTileHolderActive;
    }
    public void setMultiplier(String multiplier){
        switch (multiplier){
            case "sls":
                this.valueModifier = 1;
                this.modifierType = "sls";
                break;
            case "dls":
                this.valueModifier = 2;
                this.modifierType = "dls";
                break;
            case "tls":
                this.valueModifier = 3;
                this.modifierType = "tls";
                break;
            case "dws":
                this.valueModifier = 2;
                this.modifierType = "dws";
                break;
            case "tws":
                this.valueModifier = 3;
                this.modifierType = "tws";
                break;
            default:
                this.valueModifier = 1;
                this.modifierType = "sls";
                break;
        }
    }
}
