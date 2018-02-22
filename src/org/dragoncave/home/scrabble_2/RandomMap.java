/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.util.Random;
import java.util.Date;
import java.math.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Rider1
 */
public class RandomMap {
    private boolean debug = false;
    private Random rand;
    private final int maxLength = 7;
    private final int minLength = 1;
    
    private final int maxRowCol = 15;
    
    private final int horizontal = 0;
    private final int vertical = 1;
    
    private DCGenericScrabbleBoard board;
    
    public void setDebug(boolean debug){
        this.debug = debug;
    }
    public RandomMap(DCGenericScrabbleBoard board){
        rand = new Random();
        rand.setSeed(new Date().getTime());
        this.board = board;
    }
    
    private int getHandLength(){
        if(debug){
            System.out.println("RandomMap: getHandLength()->");
        }
        int length = 0;
        if (debug){
            length = Math.abs(rand.nextInt()% 4)+3;
        } else {
            length = Math.abs(rand.nextInt()% maxLength);
        }
        return length;
    }
    
    private int getRandomRowCol(){
        if(debug){
            System.out.println("RandomMap: getRandomRow()->");
        }
        int row = -1;
        if (debug){
            if (!board.getTileHolder("7-7").hasChild())
                row = 7;
            else 
                row = Math.abs(rand.nextInt()%3)+6;
        } else {
            if (!board.getTileHolder("7-7").hasChild())
                row = 7;
            else 
                row = Math.abs(rand.nextInt()%maxRowCol);
        }
        return row;
    }
        
    private String getRandomTileHolderID(int orientation,int rowcol){
        if(debug){
            System.out.println("RandomMap: getRandomTileHolderID()-> orientation: "+orientation+"\trow or column: "+rowcol);
        }
        String tileHolderID = "";
        /**
         * orientation: 0 = horizontal, 1 = vertical;
         */
        switch (orientation){
            case 0:
                tileHolderID = rowcol + "-" + getRandomRowCol();
                break;
            case 1:
                tileHolderID = getRandomRowCol() + "-" + rowcol;
                break;
        }
        boolean result = isTileHolderValid(tileHolderID);
//        while (!result){
//            tileHolderID = getRandomTileHolderID(orientation, rowcol);
//            result = isTileHolderValid(tileHolderID);
//        }
        
        return tileHolderID;
    }
    
    private boolean isTileHolderValid(String tileHolderID){
        if(debug){
            System.out.println("RandomMap: isTileHolderValid()->");
        }
        boolean isValid = false;
        
        DCGenericTileHolder holder = board.getTileHolder(tileHolderID);
        if (!holder.hasChild()){
            isValid = true;
        }
            
        
        return isValid;
    }
    
    private int getOrientation(){
        if(debug){
            System.out.println("RandomMap: getOrientation()->");
        }
        return Math.abs(rand.nextInt()%2);
    }
    
    public String getNextTileHolderID(int orientation, int rowcol){
        if(debug){
            System.out.println("RandomMap: getNextTileHolderID()->");
        }
        String tileHolderID = "";
        /**
         * orientation: 0 = horizontal, 1 = vertical;
         */
        switch (orientation){
            case 0:
                tileHolderID = rowcol + "-" + getRandomRowCol();
                break;
            case 1:
                tileHolderID = getRandomRowCol() + "-" + rowcol;
                break;
        }
        
        return tileHolderID;
    }
    public List generateRandomHand(){
        if(debug){
            System.out.println("RandomMap: generateRandomHand()->");
        }
        int orientation = getOrientation();
        
        int rowcol = getRandomRowCol();
        int handlength = getHandLength();
        
        if (debug){
            System.out.println("\tgenerated orientation: "+orientation+"\trow or column: "+rowcol+"\thand length: "+handlength);
        }
        List<String> tileMap = new ArrayList<String>();
        String nextTileID = "";
        for (int mapIndex = 0; mapIndex < handlength; mapIndex++ ){
            if (mapIndex == 0)
                nextTileID = getRandomTileHolderID(orientation,rowcol);
            else{
                switch (orientation){
                    case 0:
//                        nextTileID = getRightNeighborID(tileMap.get(mapIndex-1));
                        nextTileID = getRightNeighborID(nextTileID);
                        break;
                    case 1:
//                        nextTileID = getBottomNeighborID(tileMap.get(mapIndex-1));
                        nextTileID = getBottomNeighborID(nextTileID);
                        break;
                }
            }
            String[] indexes = nextTileID.split("-");
            if (Integer.parseInt(indexes[0]) >= 0 && Integer.parseInt(indexes[0]) < 15 &&
                    Integer.parseInt(indexes[1]) >= 0 && Integer.parseInt(indexes[1]) < 15 ){
                if (!tileHolderHasChild(nextTileID))
                    tileMap.add(nextTileID);
                else 
                    mapIndex--;
            }else {
                break;
            }
        }
        
        removeInvalidIDsFromList(tileMap);
        
        return tileMap;
    }
    
    private void removeInvalidIDsFromList(List<String> listTileHolders){
        
        if (debug){
            System.out.println("RandomMap: removeInvalidIDsFromList()->");
            System.out.println("\tlist tileHolderID: "+listTileHolders.toString());
        }
        for (int index = listTileHolders.size()-1; index >= 0; index--){
            if (tileHolderHasChild(listTileHolders.get(index))){
                listTileHolders.remove(index);
                if(debug)
                    System.out.println("\t\tremoving index: "+index);
            }
        }
        if(debug){
            System.out.println("\tfinal list of tileHolderIDs: "+listTileHolders.toString());
        }
    }
    private String getNeighborTileID(String tileHolderID){
        String[] rowAndCol = tileHolderID.split("-");
        String tempID = "";
         
        return tempID;
    }
    private String getLeftNeighborID(String tileHolderID){
        if(debug){
            System.out.println("RandomMap: getLeftNeighbor()->");
        }
        String[] rowAndCol = tileHolderID.split("-");
        
        int leftIndex = Integer.parseInt(rowAndCol[1])-1;
//        if (leftIndex < 0)
//            leftIndex = 14;
        String tempID = rowAndCol[0] + "-" + leftIndex;
        
        return tempID;
    }
    private String getRightNeighborID(String tileHolderID){
        if(debug){
            System.out.println("RandomMap: getRightNeighbor()->");
        }
        String[] rowAndCol = tileHolderID.split("-");
        int rightIndex = Integer.parseInt(rowAndCol[1])+1;
//        if (rightIndex > 14)
//            rightIndex = 0;
        String tempID = rowAndCol[0] + "-" + rightIndex;
        
        return tempID;
    }
    private String getTopNeighborID(String tileHolderID){
        if(debug){
            System.out.println("RandomMap: getTopNeighbor()->");
        }
        String[] rowAndCol = tileHolderID.split("-");
        int topIndex = Integer.parseInt(rowAndCol[0])-1;
//        if (topIndex < 0)
//            topIndex = 14;
        String tempID = topIndex + "-" + rowAndCol[1];
        
        return tempID;
    }
    private String getBottomNeighborID(String tileHolderID){
        if(debug){
            System.out.println("RandomMap: getBottomNeighbor()->");
        }
        String[] rowAndCol = tileHolderID.split("-");
        int bottomIndex = Integer.parseInt(rowAndCol[0])+1;
//        if (bottomIndex > 14)
//            bottomIndex = 0;
        String tempID = bottomIndex + "-" + rowAndCol[1];
        
        return tempID;
    }
    
    private boolean hasNeighbor(String tileHolderID){
        boolean result = false;
        
        if (tileHolderHasChild(getLeftNeighborID(tileHolderID)) ||
                (tileHolderHasChild(getRightNeighborID(tileHolderID))) ||
                (tileHolderHasChild(getTopNeighborID(tileHolderID))) ||
                (tileHolderHasChild(getBottomNeighborID(tileHolderID)))){
            result = true;
        }
        return result;
    }
    
    private DCGenericTileHolder getTileHolder(String tileHolderID){
        DCGenericTileHolder holder = board.getTileHolder(tileHolderID);
        if (debug){
            System.out.println("RandomMap: getTileHolder()->");
        }
        return holder;
        
    }
    
    private boolean tileHolderHasChild(String tileHolderID){
        return board.getTileHolder(tileHolderID).hasChild();
    }
    
    private void searchIndexes(){
        if(debug){
            System.out.println("RandomMap: stub function for now.");
        }
        ArrayList<Integer> wordIndexes = new ArrayList<Integer>(0);
    }
    
    private String findRandomEmptyTileHolder(){
        if (debug)
            System.out.println("RandomMap: findRandomEmptyTileHolder()->");
        int orientation = getOrientation();
        String nextTileHolderID = "";
        
        int minRowColIndex = 0;
        int maxRowColIndex = 15;
        int middleRowColIndex = 7;
        int row = 0,col=0;
        while (nextTileHolderID.equals("")){
            row = Math.abs(rand.nextInt()%(maxRowColIndex));
            col = Math.abs(rand.nextInt()%(maxRowColIndex));
            if (row < 14 && row > 0 && col < 14 && col > 0){
                DCGenericTileHolder th = board.getTileHolder(row+"-"+col);
                if (th.hasChild()){
                    // get an adjacent empty tileholder to be the beginning of the map of tiles
                    int adjacentTileHolder = Math.abs(rand.nextInt()%4);
                    switch (adjacentTileHolder){
                        case 0:
                            // get top tileholder
                            if (!board.getTileHolder(getTopNeighborID(row+"-"+col)).hasChild()){
                                nextTileHolderID = getTopNeighborID(row+"-"+col);
                                break;
                            }
                        case 1:
                            // get bottom tileholder
                            if (!board.getTileHolder(getBottomNeighborID(row+"-"+col)).hasChild()){
                                nextTileHolderID = getBottomNeighborID(row+"-"+col);
                                break;
                            }
                        case 2:
                            // get left tileholder
                            if (!board.getTileHolder(getLeftNeighborID(row+"-"+col)).hasChild()){
                                nextTileHolderID = getLeftNeighborID(row+"-"+col);
                                break;
                            }
                        case 3:
                            // get right tileholder
                            if (!board.getTileHolder(getRightNeighborID(row+"-"+col)).hasChild()){
                                nextTileHolderID = getRightNeighborID(row+"-"+col);
                                break;
                            }
                    }
                }
            }
        }
        if(debug)
            System.out.println("RandomMap: findRandomEmptyTileHolder() -> tileHolderID: "+nextTileHolderID);
        return nextTileHolderID;
    }
    
    private List<String> generateTileMap(int row, int col){
        if(debug)
            System.out.println("RandomMap: generateTileMap()->");
        
        int orientation = getOrientation();
        int rowcol = getRandomRowCol();
        int handlength = getHandLength();
        List<String> tileMap = new ArrayList<String>();
        String nextTileID = "";

        for (int mapIndex = 0; mapIndex < handlength; mapIndex++ ){
            if (mapIndex == 0)
                nextTileID = row+"-"+col;
            else{
                switch (orientation){
                    case 0:
//                        nextTileID = getRightNeighborID(tileMap.get(mapIndex-1));
                        nextTileID = getRightNeighborID(nextTileID);
                        break;
                    case 1:
//                        nextTileID = getBottomNeighborID(tileMap.get(mapIndex-1));
                        nextTileID = getBottomNeighborID(nextTileID);
                        break;
                }
            }
            String[] indexes = nextTileID.split("-");
            if (Integer.parseInt(indexes[0]) >= 0 && Integer.parseInt(indexes[0]) < 15 &&
                    Integer.parseInt(indexes[1]) >= 0 && Integer.parseInt(indexes[1]) < 15 ){
                if (!tileHolderHasChild(nextTileID))
                    tileMap.add(nextTileID);
                else 
                    mapIndex--;
            }else {
                break;
            }
        }
        
        removeInvalidIDsFromList(tileMap);
        
        return tileMap;
    }
    
    public List<String> generateRandomHand3(){
        if(debug)
            System.out.println("RandomMap: generateRandomHand3()->");
        List<String> tileHolderList = new ArrayList<String>();
        int row = -1, col = -1;
        if (!board.getTileHolder("7-7").hasChild()) {
            /**
             * does board already have first word (center tile)? is not generate map with center tile
             */
            row = 7;
            col = 7;
        } else {
            /**
             * find a empty tile on board next to an occupied tileHolder, then generate map with empty tileHolder
             */
            String EmptyTileHolder = findRandomEmptyTileHolder();
            String[] rowcol = EmptyTileHolder.split("-");
            row = Integer.parseInt(rowcol[0]);
            col = Integer.parseInt(rowcol[1]);
        }
        tileHolderList = generateTileMap(row,col);
        
        return tileHolderList;
    }
}
