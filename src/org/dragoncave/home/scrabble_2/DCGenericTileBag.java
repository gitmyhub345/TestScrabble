/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;
import java.io.StringReader;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import org.json.*;

/**
 *
 * @author Rider1
 */
public class DCGenericTileBag {
    
    private final String[] tiles = {"*","*","E","E","E","E","E","E","E","E","E","E","E","E",
                                    "A","A","A","A","A","A","A","A","A","I","I","I","I","I","I","I","I","I","O","O","O","O","O","O","O","O",
                                    "N","N","N","N","N","N","R","R","R","R","R","R","T","T","T","T","T","T",
                                    "L","L","L","L","S","S","S","S","U","U","U","U","D","D","D","D",
                                    "G","G","G","B","B","C","C","M","M","P","P","F","F","H","H",
                                    "V","V","W","W","Y","Y","K","J","X","Z","Q"};
//    private final String[] tiles = {"*","*","E","B","C","T","O","M","U","A","S","I","P","N"};  
//    private String gameName;
    private List<DCGenericTile> tileList;
//    private int lastPlayedTileSize;
    private int tilesInBag;
    private Random rand;
    
    private boolean debug = false;
    
    public DCGenericTileBag(){
        this.tilesInBag = 0;
//        this.lastPlayedTileSize = 0;
        this.tileList = new ArrayList<DCGenericTile>();
        rand = new Random();
        rand.setSeed(new Date().getTime());
        
        createTileList();
    }
    
    private void createTileList(){
        for(int index = 0; index < tiles.length; index++){
            DCGenericTile tile = new DCGenericTile(tiles[index],index);
            if (debug)
                System.out.println("DCGenericTileBag: createTileList()-> "+tile.toString());
            tileList.add(tile);
            tilesInBag++;
        }
    }
    
    private DCGenericTile getRandomTile(){
        DCGenericTile tile = null;
        while(tilesInBag > 0){
            int index = Math.abs(rand.nextInt()%tiles.length);
            if (tileList.get(index).isIsInPool()){
                tileList.get(index).removeFromPool();
                tile = new DCGenericTile(tileList.get(index).getLetter(),tileList.get(index).getTileNumber());
                
                tilesInBag--;
                tile.removeFromPool();
                if(debug){
                    System.out.println("DCGenericTileBag: getRandomTile()-> "+ tile.toString());
                    System.out.println(tileList.get(index).getJsonString());
                }
                break;
                
            }
        }
        return tile;
    }

    public int getTilesInBag() {
        return tilesInBag;
    }
    
    public void setDebug(boolean debug){
        this.debug = debug;        
    }
    
    public List<DCGenericTile> getHand(){
        int numTiles = Math.min(7, tilesInBag);
        return getHandTiles(numTiles);
    }
    
    public String getHandString(){
        int numTiles = Math.min(7, tilesInBag);
        return toJsonString(numTiles);
    }
    
    public List<DCGenericTile> replacePlayedTiles(int numberOfTiles){
        int numTiles = Math.min(numberOfTiles, tilesInBag);
        return getHandTiles(numTiles);
    }
    
    public String replacePlayedTilesString(int numberOfTiles){
        int numTiles = Math.min(numberOfTiles, tilesInBag);
        return toJsonString(numTiles);
    }
    private List<DCGenericTile> getHandTiles(int numberOfTiles){
        int minNumTiles = Math.min(tilesInBag, numberOfTiles);
        ArrayList<DCGenericTile> handList = new ArrayList<>(minNumTiles);
        
        for (int index = 0; index < minNumTiles; index++){
            handList.add(getRandomTile());
        }
        
        return handList;
        
    }
    
    private String toJsonString(int numTiles){
        if (debug){
            System.out.println("DCGENERICTILEBAG: toJsonString(int) ->");
        }
        JSONObject jsonHand = new JSONObject();
        jsonHand.put("hand", new JSONArray());
        if (numTiles > 0){
            List<DCGenericTile> handTiles = getHandTiles(numTiles);
            for (DCGenericTile tile: handTiles){
                if(debug){
                    System.out.println("\ttile to be added to hand: "+tile.getJsonString());
                }
                jsonHand.getJSONArray("hand").put(new JSONObject(tile.getJsonString()));
            }
        }
        return jsonHand.toString();
    }
    
    public void returnTilesToBag(List<DCGenericTile> tiles){
        if(debug){
            System.out.println("DCGenericTileBag: returnTilesToBar()->");
        }
        tiles.stream().forEach((tile)->{
            if(debug){
                System.out.println("returning tile to bag: "+tile.getJsonString());
//              System.out.println("tilebag for tile: "+tileList.get(tile.getTileNumber()).isIsInPool());
            }
            if (!this.tileList.get(tile.getTileNumber()).isIsInPool()){
                this.tileList.get(tile.getTileNumber()).resetTile();
                tilesInBag++;
            }else{
                System.out.println("DCGenericTileBag: returnTilesToBag()->\tAttempting to return a tile that is already in the bag... shame on you.");
            }
            
            if (tilesInBag > 100){
                // eeror - for some reason, there are more than 100 tiles being returned
                System.out.println("DCGenericTileBag: returnTilesToBag()->\toh oh... something went wrong with return tiles to bag.");
            }
            if (debug){
                System.out.println("DCGenericTileBag: -> returnTilesToBag");
                System.out.println(this.tileList.get(tile.getTileNumber()).getJsonString());
            }
        });
    }
    
    public void resetTileBag(){
        tilesInBag = 0;
        tileList.stream().forEach((tile) -> {
            tile.resetTile();
            tilesInBag++;
        });
    }
    
    public void loadTileBag(){
        
    }
    
    public List<DCGenericTile> getTileList(){
        return tileList;
    }
    private void toList(String tileList){
        
    }
    
    public JSONArray toJSONArray(){
        JSONArray tileArray = new JSONArray();
        for(DCGenericTile tile: tileList){
            tileArray.put(tile.getJsonString());
        }
        
        return tileArray;
    }
    public void toMap(){
        
    }
    /*
    public void setTilesInBag(int tilesInBag) {
        this.tilesInBag = tilesInBag;
    }
    */
    
}
