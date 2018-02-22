/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Date;

/**
 *
 * @author William J
 */
public class DCTileBuilder {
    private boolean debug;
    
    private List<DCTile> tileList;
    private Random rand;
    private int tilesInPool;
    
    private static Map<String,DCTile> mapTile;
    private static final String[] tiles = {"*","*",
                                    "E","E","E","E","E","E","E","E","E","E","E","E",
                                    "A","A","A","A","A","A","A","A","A",
                                    "I","I","I","I","I","I","I","I","I",
                                    "O","O","O","O","O","O","O","O",
                                    "N","N","N","N","N","N",
                                    "R","R","R","R","R","R",
                                    "T","T","T","T","T","T",
                                    "L","L","L","L",
                                    "S","S","S","S",
                                    "U","U","U","U",
                                    "D","D","D","D",
                                    "G","G","G",
                                    "B","B",
                                    "C","C",
                                    "M","M",
                                    "P","P",
                                    "F","F",
                                    "H","H",
                                    "V","V",
                                    "W","W",
                                    "Y","Y",
                                    "K","J","X","Z","Q"};
    public DCTileBuilder(){
        rand = new Random();
        if (tileList != null){
            return;
        } else {
            tileList = new ArrayList<DCTile>();
        }

        rand.setSeed(new Date().getTime());
        for (int index = 0; index < tiles.length; index++){
            DCTile dcTile = new DCTile(tiles[index],index);
            tileList.add(dcTile);
        }
        tilesInPool = tiles.length;
    }
    
    public void setDebug(boolean debug){
        this.debug = debug;
    }
    public List<DCTile> getTiles(){
        return tileList;
    }
    
    public DCTile getRandomTile(){
        DCTile tile = null;
        if (debug){
            System.out.println("Source: DCTileBuilder.getRandomTile() -> number of tiles remaining in list: "+tilesInPool);
            System.out.println("Source: DCTileBuilder.getRandomTile() -> getting random tile...");
        }
        while(tilesInPool>0){
            int tileIndex = Math.abs(rand.nextInt()%tiles.length);
            if (debug){
                System.out.println("Source: DCTileBuilder.getRandomTile() -> next random tile index: "+tileIndex);
            }
            if (tileList.get(tileIndex).getInPool()){
                tile = tileList.get(tileIndex);
                tileList.get(tileIndex).removeFromPool();
                tilesInPool--;
                if (debug){
                    System.out.println("Source: DCTileBuilder.getRandomTile() -> tile retrieved with index: "+tileIndex);
                }
                break;
            }
        }
        if (debug){
            if (tile != null){
                System.out.println("got random tile : "+tile.toString()+"\n\tnumber of tiles left after getting random tile: "+tilesInPool);
            } else {
                System.out.println("unable to retrieve random tile.");
            }
        }
        
        return tile;
    }
    
    public List<DCTile> getHand(){
        List<DCTile> hand = new ArrayList<>();
        if (debug){
            System.out.println("Source: DCTileBuilder.getHand() -> initial hand size: "+hand.size());
        }
        for (int index = 0; index < Math.min(7, tilesInPool); index++){
            hand.add(getRandomTile());
            if (debug){
                System.out.println("\tSource: DCTileBuilder.getHand() -> index: "+index+": size"+hand.size());
            }
        }
        if(debug){
            System.out.println("\tSource: DCTileBuilder.getHand() -> hand size: "+hand.size());
        }
        return hand;
    }
    
    public int getTilesInPool(){
        return tilesInPool;
    }
    /**
     * User is returning tiles to select others
     * @param handTiles
     * @return 
     */
    public boolean returnTilesInHand(List<DCTile> handTiles){
        boolean result = true;
        int pooltiles = tilesInPool+handTiles.size();
        for (DCTile t: handTiles){
            int tileIndex = Integer.parseInt(t.getTileID().substring(6));
            if (debug){
                System.out.println("Source: DCTileBuilder.returnTilesInHand(List) -> returning tile with index: "+tileIndex);
            }
            if (tileIndex >= 0 && tileIndex < 100){
                tileList.get(tileIndex).returnedFromHand();
                tilesInPool++;
            } else {
                result = false;
            }
        }
        if (pooltiles == tilesInPool && result){
            return result;
        }
        else{
            result = false;
            return result;
        }
    }
    
    public void saveTileList(){
        try(FileWriter out = new FileWriter("ScrabbleTileList.dat")){
            out.write(tileList.toString());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public void loadTileList(){
        if (!tileList.isEmpty())
            tileList.clear();
        
        try (BufferedReader in = new BufferedReader(new FileReader("ScrabbleTileList.dat"))){ 
            tilesInPool = 0;
            String line;
            while ((line = in.readLine()) != null){
                String subline = line.replaceAll("\\[","").replaceAll("\\]", "");
                String[] tiles = subline.split(", ");
                if(debug){
                    System.out.println("Source: DCTileBuilder.loadTileList() -> number of tiles to load: "+tiles.length);
                }
                for (String t: tiles){
                    if(debug){
                        System.out.println("\ttile: "+t);
                    }
                    DCTile tile = new DCTile(t);
                    tileList.add(tile);
                    if (tile.getInPool())
                        tilesInPool++;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        if (debug){
            System.out.println("Source: DCTileBuilder.loadTileList() -> reloaded ScrabbleTileList with "+tilesInPool+" tiles in pool");
        }
    }
    
    public void resetTileBuilder(){
        System.out.println("resetting tileBuilder...");
        for(DCTile tile: tileList){
            tile.resetTile();
        }
        tilesInPool = tiles.length;
        System.out.println("done resetting tileBuilder.");
    }
}
