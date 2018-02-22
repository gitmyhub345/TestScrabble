/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble.net;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
/**
 *
 * @author Rider1
 */
public class DCServerTileBag {
    
    private int numPlayers;
    private int numRequest;
    
    private String gameName;
    private List<Map<String,DCServerTile>> prevPlayedTiles;
    private List<DCServerTile> tileList;
    private int lastPlayedTileSize;
    private int tilesInBag;
    private Random rand;
    private final String[] tiles = {"*","*","E","E","E","E","E","E","E","E","E","E","E","E",
                                    "A","A","A","A","A","A","A","A","A","I","I","I","I","I","I","I","I","I","O","O","O","O","O","O","O","O",
                                    "N","N","N","N","N","N","R","R","R","R","R","R","T","T","T","T","T","T",
                                    "L","L","L","L","S","S","S","S","U","U","U","U","D","D","D","D",
                                    "G","G","G","B","B","C","C","M","M","P","P","F","F","H","H",
                                    "V","V","W","W","Y","Y","K","J","X","Z","Q"};

    public List<Map<String, DCServerTile>> getPrevPlayedTiles() {
        return prevPlayedTiles;
    }
    public String getGameName(){
        return gameName;
    }
    
    public void addPlayer(){
        numPlayers++;
    }
    public void removePlayer(){
        numPlayers--;
    }
    public int getNumPlayers(){
        return numPlayers;
    }
    
    public int getPlayerTurn(){
        System.out.println("Source: DCServerTileBag.getPlayerTurn() -> numRequest: "+numRequest+" numPlayers: "+numPlayers);
        return numRequest%numPlayers + 1;
    }
    
    private void calculateTurn(){
        
    }
    public void setGameName(String gameName){
        this.gameName = gameName;
    }
    public DCServerTileBag(){
        /**
         * create the list of Tiles to issue send out to players
         */
        gameName="DCScrabbleGame";
        numPlayers = 1;
        numRequest = 0;
        tilesInBag = 0;
        tileList = new ArrayList<>();
        prevPlayedTiles = new ArrayList<Map<String,DCServerTile>>();
        lastPlayedTileSize = 0;
        for (int index = 0; index < tiles.length; index++){
            DCServerTile tile = new DCServerTile(tiles[index],index);
            tileList.add(tile);
            tilesInBag++;
        }
        
        /**
         * initiate rand for use in function getRandomTile()
         */
        
        rand = new Random();
        rand.setSeed(new Date().getTime());
    }
    public DCServerTile getTile(){
        return getRandomTile();
    }
    public String repopulateHand(int size){
//        int size=0;
        int minSize = Math.min(size,tilesInBag);
        return getHandTiles(minSize).toString();
    }
    public String populateHane(){
//        numRequest++;
        int size = Math.min(tilesInBag, 7);
        return getHandTiles(size).toString();
    }
    private List<DCServerTile> getHandTiles(int size){
        List<DCServerTile> handTiles = new ArrayList<>();
        for (int index = 0; index < size; index++){
            handTiles.add(getRandomTile());
        }
        return handTiles;
    }
    private DCServerTile getRandomTile(){
        DCServerTile tile = null;
        System.out.println("Source: DCServerTileBag.getRandomTile() -> beginning tiles remaining " + tilesInBag);
        /**
         * only return a valid tile if there are tiles in bag
         */
        while (tilesInBag > 0){
            int index = Math.abs(rand.nextInt()%tiles.length);
            System.out.println("next index: "+index);
            tile = tileList.get(index);
            if (tile.isIsInPool()){
                /**
                 * if tile is in bag, remove from bag, decrement the number of available tiles leftover, and exit loop
                 */
                tilesInBag--;
                tile.removeFromPool();
                break;
            }
        }
        
        System.out.println("Source: DCServerTileBag.getRandomTile() -> tiles remaining " + tilesInBag);
        return tile;
    }
    
    private boolean returnTileToPool(DCServerTile tile){
        boolean result = false;
        
        if (!tile.isIsInPool()){
            tile.returnToPool();
            result = true;
            tilesInBag++;
            System.out.println("Source: DCServerTileBag.returnTileToPool(DCServerTile) -> returned to pool: "+tile.toString());
        } else {
            System.out.println("Source: DCServerTileBag.returnTileToPool(DCServerTile) -> error: tile already in pool, should not be allowed");
            
        }
        
        return result;
    }

    public boolean returnTilesToPool(String strTilesList){
        List<DCServerTile> tiles = toList(strTilesList);
        boolean result = false;
        int currentTilesInBag = tilesInBag;
        for (DCServerTile tile: tiles){
            result = returnTileToPool(tile);
            if(!result){
                System.out.println("Source: DCServerTileBag.returnTilesToPool(String) -> error returning tile to pool");
                break;
            }
        }
        if (result){
            /** all tiles returned to bag **/
            if (currentTilesInBag + tiles.size() != tilesInBag){
                result = false;
                System.out.println("Source: DCServerTileBag.returnTilesToPool(String)-> number of tiles returned does not match tile list");
            }
        }
        if (result)
            numRequest++;
        return result;
    }
    
    public int submitPlayedTiles(String strMapPlayedTiles){
        int numOfTiles = 0;
        Map<String,DCServerTile> map = toMap(strMapPlayedTiles);
        prevPlayedTiles.add(map);
        numRequest++;
        numOfTiles = map.size();
        return numOfTiles;
    }
    
    public void resetTileBag(){
        tileList.stream().forEach((tile) -> {
            tile.returnToPool();
        });
        tilesInBag = tiles.length;
        prevPlayedTiles.clear();
        lastPlayedTileSize=0;
        numPlayers = 1;
        numRequest = 0;
    }
    
    public void loadTileBag(){
        
    }
    
    public void saveTileBag(){
       tileList.stream().forEach((tile)->{
           System.out.println(tile.toString());
       });
    }
    
    public DCServerTile getTile(int tileIndex){
        return tileList.get(tileIndex);
    }
    
    private List<DCServerTile> toList(String strList){
        List<DCServerTile> tList = new ArrayList<>();
        String modifiedString = strList.replaceAll("\\[","").replaceAll("\\]", "");
        String[] strTileArray = modifiedString.split(", ");
        for(String strTile: strTileArray){
            System.out.println("Source: DCServerTileBag.toList(String) -> DCServerTile: "+strTile);
//            DCServerTile serverTile = new DCServerTile(strTile);
            String[] tileAtt = strTile.split(":");
            System.out.println("\tnumber of attibutes: "+tileAtt.length+"\t"+tileAtt.toString());
            DCServerTile serverTile = this.tileList.get(Integer.parseInt(tileAtt[2]));
            tList.add(serverTile);
        }
        return tList;
    }
    
    private Map<String,DCServerTile> toMap(String strMap){
        Map<String,DCServerTile> playMap = new HashMap<>();
        String modifiedString = strMap.replaceAll("\\{","").replaceAll("\\}", "");
        String[] strTileMap = modifiedString.split(", ");
        for (String mapEntry: strTileMap){
            String[] entrySet = mapEntry.split("=");
//            DCServerTile tile = new DCServerTile(entrySet[1]);
            String[] tileAtt = entrySet[1].split(":");
            DCServerTile tile = tileList.get(Integer.parseInt(tileAtt[2]));
            if (tile != null)
                playMap.put(entrySet[0], tile);
            else
                System.out.println("Source: DCServerTileBag.toMap(String) -> error creating DCSeverTile from string for "+entrySet[1]);
        }
        return playMap;
    }
}
