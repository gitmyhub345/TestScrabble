/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Rider1
 */
public class DCGenericScrabbleBoard {
    private final String[][] boardSpaces = {{"tws","sls","sls","dls","sls","sls","sls","tws","sls","sls","sls","dls","sls","sls","tws"},
                                        {"sls","dws","sls","sls","sls","tls","sls","sls","sls","tls","sls","sls","sls","dws","sls"},
                                        {"sls","sls","dws","sls","sls","sls","dls","sls","dls","sls","sls","sls","dws","sls","sls"},
                                        {"dls","sls","sls","dws","sls","sls","sls","dls","sls","sls","sls","dws","sls","sls","dls"},
                                        {"sls","sls","sls","sls","dws","sls","sls","sls","sls","sls","dws","sls","sls","sls","sls"},
                                        {"sls","tls","sls","sls","sls","tls","sls","sls","sls","tls","sls","sls","sls","tls","sls"},
                                        {"sls","sls","dls","sls","sls","sls","dls","sls","dls","sls","sls","sls","dls","sls","sls"},
                                        {"tws","sls","sls","dls","sls","sls","sls","dws","sls","sls","sls","dls","sls","sls","tws"},
                                        {"sls","sls","dls","sls","sls","sls","dls","sls","dls","sls","sls","sls","dls","sls","sls"},
                                        {"sls","tls","sls","sls","sls","tls","sls","sls","sls","tls","sls","sls","sls","tls","sls"},
                                        {"sls","sls","sls","sls","dws","sls","sls","sls","sls","sls","dws","sls","sls","sls","sls"},
                                        {"dls","sls","sls","dws","sls","sls","sls","dls","sls","sls","sls","dws","sls","sls","dls"},
                                        {"sls","sls","dws","sls","sls","sls","dls","sls","dls","sls","sls","sls","dws","sls","sls"},
                                        {"sls","dws","sls","sls","sls","tls","sls","sls","sls","tls","sls","sls","sls","dws","sls"},
                                        {"tws","sls","sls","dls","sls","sls","sls","tws","sls","sls","sls","dls","sls","sls","tws"}
    };
    
    private Map<String,DCGenericTileHolder> mapTileHolders;
    private Map<String,DCGenericTile> mapTiles;
    private List<Map<String,DCGenericTile>> playedWords;
    
    private boolean debug;
    
    public DCGenericScrabbleBoard(){
        mapTileHolders = new HashMap<String,DCGenericTileHolder>();
        mapTiles = new HashMap<String,DCGenericTile>();
        playedWords = new ArrayList<Map<String,DCGenericTile>>();
        debug = false;
        createBoard();
    }
    
    public void setDebug(boolean debug){
        this.debug = debug;
    }
    
    public DCGenericTileHolder getTileHolder(String tileHolderID){
        
        return mapTileHolders.keySet().contains(tileHolderID)? mapTileHolders.get(tileHolderID):null;
    }
    
    private void createBoard(){
        for (int row = 0; row < 15; row++){
            for (int col = 0; col < 15; col++){
                DCGenericTileHolder holder = new DCGenericTileHolder();
                holder.setID(Integer.toString(row)+"-"+Integer.toString(col));
                holder.setMultiplier(boardSpaces[row][col]);
                mapTileHolders.put(holder.getID(), holder);
            }
        }
    }
    
    private Map<String,DCGenericTile> cloneWord(Map<String,DCGenericTile> word){
        Map<String,DCGenericTile> clone = new HashMap<>(word.size());
        
        for(Entry entry: word.entrySet()){
            clone.put((String)entry.getKey(), (DCGenericTile) entry.getValue());
        }
        
        return clone;
        
    }
    
    public void addWordsToBoard(Map<String,DCGenericTile> word){
        if(debug){
            System.out.println("DCGenericScrabbleBoard: addWordsToBoard()->...");
            word.forEach((key,tile)->{
//                System.out.println("\tadding tile: "+key+" : "+tile.toJsonString());
            });
        }
        
        
        boolean droppable = true;
        
        /*for (String k : word.keySet()){
            // ensure all tiles addeds are droppable
            droppable = mapTileHolders.get(k).getIsDroppable();
            if (!droppable)
                break;
        }
        */
        Map<String,DCGenericTile> copyWord = cloneWord(word);
        if (droppable){
            if(debug)
                System.out.println("\tBeginning to addWord: "+word.size() + " word is: "+ word);
            word.forEach((key, tile)->{
                DCGenericTileHolder tileHolder = mapTileHolders.get(key);
                if (debug){
                    System.out.println("\ttileHolderID: "+key+"\tisDroppable: "+tileHolder.getIsDroppable()+"\tadding tile: "+tile.getJsonString());
                }
                if (tileHolder.getIsDroppable() && tileHolder.isActive()) {
                    if(debug){
                        System.out.println("\tadding tile to board...");
                    }
                    tile.setIsPlacedOnBoard(true);
                    tile.setIsDraggable(false);
                    tileHolder.addTile(tile.getJsonString());
    //                tileHolder.toggleDrop();
//                    playedWords.add(word);
                    mapTiles.put(key, tile);
                    if(debug){
                        System.out.println("adding "+tile.getLetter()+" to board completed");
                    }
                } else if (!tileHolder.getIsDroppable() && !tileHolder.isActive() && tileHolder.hasChild() && 
                        tileHolder.getTile().getTileNumber() == tile.getTileNumber() ){
                    // same tile as this one, 
                    if (debug)
                        System.out.println("\tno need to add this tile, already on board.");
                } else {
                    // tileHolder already has a tile
                    // should raise error
                    System.out.println("DCGenericScrabbleBoard: addWordsToBoard()-> error: attempting to add tile to invalid tileholder!");
                }
            });
            this.playedWords.add(copyWord);
            
            if(debug){
                System.out.println("\tfinished adding word to played words: "+playedWords.size());
                for(int ind = 0; ind < playedWords.size(); ind++){
                    System.out.println("\t\t"+ind +" item: "+playedWords.get(ind));
                }
            }
        }
    }
    
    public void removeTiles(Map<String,DCGenericTile> word){
        if(debug){
            System.out.println("DCGenericScrabbleBoard: removeTiles()->"+word.toString());
        }
        for (Entry<String,DCGenericTile> entry: word.entrySet()){
            mapTileHolders.get(entry.getKey()).removeTile();
            mapTileHolders.get(entry.getKey()).toggleActive();
        }
        if(debug){
            for (Entry<String,DCGenericTile> entry: word.entrySet()){
                System.out.println("\t results: tileholder is droppable: "+mapTileHolders.get(entry.getKey()).getIsDroppable()+"\n\ttile holder has child: " +mapTileHolders.get(entry.getKey()).hasChild());
            }
        }
    }
    public boolean isHorizontal(Map<String,DCGenericTile> word) {
        if (debug)
            System.out.println("DCSCrabbleBoard: isHorizontal()->");
        boolean result = true;
        int row=-1;
        for (String key: word.keySet()){
            if(row == -1){
                if (debug)
                    System.out.println("\t"+Integer.parseInt(key.split("-")[0]));
                row = Integer.parseInt(key.split("-")[0]);
            } else {
                if (debug)
                    System.out.println("\t"+Integer.parseInt(key.split("-")[0]));
                if ( row != Integer.parseInt(key.split("-")[0])){
                    result = false;
                    break;
                }
            }
        }
        
        return result;
    }

    public boolean isVertical(Map<String,DCGenericTile> word) {
        if (debug)
            System.out.println("DCScrabbleBoard: isVertical()->");
        boolean result = true;
        int col=-1;
        for (String key: word.keySet()){
            if(col == -1){
                if (debug)
                    System.out.println("\t"+Integer.parseInt(key.split("-")[1]));
                col = Integer.parseInt(key.split("-")[1]);
            } else {
                if (debug)
                    System.out.println("\t"+Integer.parseInt(key.split("-")[1]));
                if ( col != Integer.parseInt(key.split("-")[1])){
                    result = false;
                    break;
                }
            }
        }
        
        return result;
    }
    
    public boolean isValid(Map<String,DCGenericTile> word){
        boolean result = false;
        
        boolean horizontal = isHorizontal(word);
        boolean vertical = isVertical(word);
        int rowcol = -1;
        int orientation = -1;
        if (horizontal)
            orientation = 0;
        else if (vertical)
            orientation = 1;
        
        if (!horizontal && !vertical){
            // error - can't be both vertical and horizontal
            return result;
        } else if (horizontal) {
            // are the tiles consecutive?
            rowcol = getWordRowColumn(word,orientation);
            boolean consec = isConsecutive(word);
            
        } else if (vertical){
            boolean consec = isConsecutive(word);
        }
        return result;
    }
    
    private int getWordRowColumn(Map<String,DCGenericTile> word, int orientation){
        int wordRowColumn = -1;

        for(String key: word.keySet()){
            if (wordRowColumn == -1){
                wordRowColumn = Integer.parseInt(key.split("-")[orientation]);
            } else {
                if (wordRowColumn != Integer.parseInt(key.split("-")[orientation])){
                    wordRowColumn = -1; // set to -1 for error
                    break;
                }
            }
        }
        
        return wordRowColumn;
    }
    
    public int[] getRowIndexes(Map<String,DCGenericTile>word){
        int[] rowIndexes = new int[word.size()];
        int index=0;
        
        for (String key: word.keySet()){
            String[] rowcol =  key.split("-");
            rowIndexes[index] = Integer.parseInt(rowcol[0]);
            index++;
        }
        intArraySort(rowIndexes);
        return rowIndexes;
    }
    
    public int[] getColIndexes(Map<String,DCGenericTile>word){
        int[] colIndexes = new int[word.size()];
        int index=0;
        
        for (String key: word.keySet()){
            String[] rowcol =  key.split("-");
            colIndexes[index] = Integer.parseInt(rowcol[1]);
            index++;
        }
        
        intArraySort(colIndexes);
        
        return colIndexes;
    }
    
    public boolean isConsecutive(Map<String,DCGenericTile>word){
        boolean result = false;
//        int[] rowIndexes = new int[word.size()];
//        int[] colIndexes = new int[word.size()];
//        int index = 0;
//        for (String key: word.keySet()){
//            String[] rowcol =  key.split("-");
//            rowIndexes[index] = Integer.parseInt(rowcol[0]);
//            colIndexes[index] = Integer.parseInt(rowcol[1]);
//            index++;
//        }
        
        int[] rowIndexes = getRowIndexes(word);
        int[] colIndexes = getColIndexes(word);
        
        if (isHorizontal(word)){
            if (debug)
                System.out.println("DCScrabbleBoard: isConsecutive()-> horizontal");
            for (int col = 0; col < colIndexes.length-1; col++){
                if (debug)
                    System.out.println("\tcols: "+colIndexes[col]+":"+colIndexes[col+1]);
                if (colIndexes[col]+1 != colIndexes[col+1] ){
                    result = false;
                    break;
                } else {
                    result = true;
                }
            }
        } else if (isVertical(word)) {
            if (debug)
                System.out.println("DCScrabbleBoard: isConsecutive()-> vertical");
            for (int row = 0; row < rowIndexes.length-1; row++){
                if (debug)
                    System.out.println("\trows: "+rowIndexes[row]+":"+rowIndexes[row+1]);
                 if (rowIndexes[row]+1 != rowIndexes[row+1] ){
                    result = false;
                    break;
                } else {
                    result = true;
                }
            }
        }
        return result;
    }
    
    public void intArraySort(int[] array){
        int lvalue = 0, gvalue=0;
        for (int inda = 0; inda < array.length; inda++){
            for (int indb = 0; indb < array.length-1; indb++){
                if(array[indb] > array[indb+1]){
                    gvalue = array[indb];
                    array[indb] = array[indb+1];
                    array[indb+1] = gvalue;
                }
            }
        }
    }
    
    public void fillInGaps(int[] indexes, int orientation, int indexRowCol, Map<String,DCGenericTile>tiles){
        if (debug){
            System.out.println("DCGenericScrabbleBoard: fillInGaps()->...");
        }
        String tileHolderID = "";    
        for (int ind = 0; ind < indexes.length-1; ind++){
            int next = indexes[ind]+1;
            int round = 1;
            while (next < indexes[ind+1]){
                switch(orientation){
                    case 0: // horizontal, row remains the same, column changes
                        tileHolderID = Integer.toString(indexRowCol)+"-"+Integer.toString(next);
                        break;
                    case 1: // vertical, row changes, column remains the same
                        tileHolderID = Integer.toString(next)+"-"+Integer.toString(indexRowCol);
                        break;
                }
                if (mapTileHolders.get(tileHolderID).hasChild()){
                    tiles.put(tileHolderID, mapTileHolders.get(tileHolderID).getTile());
                    if (debug){
                        System.out.println("\ttile in gap: "+tileHolderID + "\ttile: "+mapTileHolders.get(tileHolderID).getTile().getJsonString());
                    }
                } else {
                    if (debug){
                        System.out.println("\ttile in gap: "+tileHolderID+" there is no tile in this gap: Error");
                    }
                    break;
                }
                next++;
            }
        }
    }
    
    public void addLeadingTiles(int[] indexes, int orientation, int indexRowCol, Map<String,DCGenericTile>tiles){
        if (debug){
            System.out.println("DCGenericScrabbleBoard: addLeadingTiles()->...");
        }
        int minIndex = indexes[0]-1;
        String tileHolderID = "";
        switch(orientation){
            case 0: // horizontal, row remains the same, column changes
                tileHolderID = Integer.toString(indexRowCol)+"-"+Integer.toString(minIndex);
                break;
            case 1: // vertical, row changes, column remains the same
                tileHolderID = Integer.toString(minIndex)+"-"+Integer.toString(indexRowCol);
                break;
        }
        boolean result = mapTileHolders.get(tileHolderID).hasChild();
        if (debug){
            System.out.println("\ttileHolderID: "+tileHolderID+"\thasChild: "+result);
        }
        while (result){
            tiles.put(tileHolderID, mapTileHolders.get(tileHolderID).getTile());
            // change to next tileHolderID
            minIndex--;
            switch(orientation){
                case 0: // horizontal, row remains the same, column changes
                    tileHolderID = Integer.toString(indexRowCol)+"-"+Integer.toString(minIndex);
                    break;
                case 1: // vertical, row changes, column remains the same
                    tileHolderID = Integer.toString(minIndex)+"-"+Integer.toString(indexRowCol);
                    break;
            }
            result = mapTileHolders.get(tileHolderID).hasChild();
            if (debug){
                System.out.println("\tnext tileHolderID: "+tileHolderID+"\thasChild: "+result);
            }
        }
    }
    
    public void addTrailingTiles(int[] indexes, int orientation, int indexRowCol, Map<String,DCGenericTile>tiles){
        if (debug){
            System.out.println("DCGenericScrabbleBoard: addTrailingTiles()->...");
        }
        int maxIndex = indexes[indexes.length-1]+1;
        String tileHolderID = "";
        switch(orientation){
            case 0: // horizontal, row remains the same, column changes
                tileHolderID = Integer.toString(indexRowCol)+"-"+Integer.toString(maxIndex);
                break;
            case 1: // vertical, row changes, column remains the same
                tileHolderID = Integer.toString(maxIndex)+"-"+Integer.toString(indexRowCol);
                break;
        }
        boolean result = mapTileHolders.get(tileHolderID).hasChild();
        if (debug){
            System.out.println("\ttileHolderID: "+tileHolderID+"\thasChild: "+result);
        }
        while (result){
            tiles.put(tileHolderID, mapTileHolders.get(tileHolderID).getTile());
            // change to next tileHolderID
            maxIndex++;
            switch(orientation){
                case 0: // horizontal, row remains the same, column changes
                    tileHolderID = Integer.toString(indexRowCol)+"-"+Integer.toString(maxIndex);
                    break;
                case 1: // vertical, row changes, column remains the same
                    tileHolderID = Integer.toString(maxIndex)+"-"+Integer.toString(indexRowCol);
                    break;
            }
            result = mapTileHolders.get(tileHolderID).hasChild();
            if (debug){
                System.out.println("\tnext tileHolderID: "+tileHolderID+"\thasChild: "+result);
            }
        }
        
    }
    
    public void findPerpendicularWords(){
        
    }
    
    public boolean isWordConnected(Map<String,DCGenericTile>word){
        boolean result = false;
        boolean horizontal = isHorizontal(word);
        boolean vertical = isVertical(word);
        int orientation;
        int[] indexes;  // indexes to sort the row/columns for faster sorting and comparison
        if(horizontal){
            orientation = 0;
            indexes = getColIndexes(word);
        } else if (vertical){
            orientation = 1;
            indexes = getRowIndexes(word);
        } else {
            if(debug)
                System.out.println("DCGenericScrabbleBoard: isWordConnected()-> word is neither horizontal or vertical.");
            return result;
        }
        
        /**
         * Sort indexes 
         */
        intArraySort(indexes);
        if (debug){
            System.out.println("DCGenericScrabbleBoard: isWordConnected()-> sorted indexes array...");
            for (int a = 0; a < indexes.length; a++){
                System.out.println("\tSorted indexes values: "+indexes[a]);
            }
        }
        
        /**
         * get the row/column the tiles are played on
         */
//        int rowColIndex = -1;
        int rowColIndex = getWordRowColumn(word,orientation);
        /**
         * code moved to procedure: dummyFunc_1
         */
        /**
         * end code movement: dummyFunc_1
         */
        if(debug)
            System.out.println("the played tiles are on row/column: "+rowColIndex);
        
        boolean consec = isConsecutive(word);
        int gapTiles = 0;
        if (!consec){
            /**
             * checking to see if skipped tiles exists, if not, then playing two words
             */
            gapTiles = hasGapTiles(indexes, orientation, rowColIndex );
            result = gapTiles > 0? true: false;
        }
        
        if(debug){
            System.out.println("Skipped tiles have child(ren)"+"\tResult from previous: "+gapTiles);
        }
        
        if (!result){
            /**
             * check for tiles at the beginning and end of tiles
             */
            int startIndex, endIndex;
            startIndex = indexes[0]-1;
            endIndex = indexes[indexes.length-1]+1;
            String mapStartID = "";
            String mapEndID = "";
            if (horizontal){
                mapStartID = Integer.toString(rowColIndex)+"-"+Integer.toString(startIndex);
                mapEndID = Integer.toString(rowColIndex)+"-"+Integer.toString(endIndex);
                
            } else if (vertical){
                mapStartID = Integer.toString(startIndex)+"-"+Integer.toString(rowColIndex);
                mapEndID = Integer.toString(endIndex)+"-"+Integer.toString(rowColIndex);
                
            }
            result = mapTileHolders.get(mapStartID).hasChild();
            if (!result){
                result = mapTileHolders.get(mapEndID).hasChild();
            }
        }
        
        if(!result){
            if(debug)
                System.out.println("DCGenericScrabbleBorad: isWordConnected()-> checking for perpendicular words...");
            int connectedTiles = hasPerpendicular(indexes,orientation,rowColIndex);
            if (connectedTiles > 0)
                result = true;
        }
        
        return result;
    }
    
    private int hasPerpendicular(int[] indexes, int orientation, int indexRowCol){
        // orientation: 0 = horizonal; 1 = vertical
        // indexRowCol = row/column of word
        int result = 0;
        String startTileHolderID = "";
        String endTileHolderID = "";
        for (int ind = 0; ind < indexes.length; ind++){
            switch (orientation){
                case 1: // word is vertical, checking for horizontal (row is constant while column changes)
                    startTileHolderID = Integer.toString(indexes[ind])+"-"+Integer.toString(indexRowCol-1);
                    endTileHolderID = Integer.toString(indexes[ind])+"-"+Integer.toString(indexRowCol+1);
                    break;
                case 0: // word is horizontal, checking for vertical (row changes while column is constant)
                    startTileHolderID = Integer.toString(indexRowCol-1)+"-"+Integer.toString(indexes[ind]);
                    endTileHolderID = Integer.toString(indexRowCol-1)+"-"+Integer.toString(indexes[ind]);
                    break;
            }
            if(debug)
                System.out.println("DCGenericScrabbleBoard: hasPerpendicular() -> checking tileHolderID "+startTileHolderID + " and "+endTileHolderID);
            if (mapTileHolders.get(startTileHolderID).hasChild() || mapTileHolders.get(endTileHolderID).hasChild()) {
                result++;
            }
            
        }
        
        if (debug)
            System.out.println("DCGenericScrabbleBoard: hasPerpendicular()-> has "+ result+" perpendicular word(s)");
        return result;
    }
    
    public int hasGapTiles(int[] indexes, int orientation, int rowColIndex){
        /**
         * checking to see if skipped tiles exists, if not, then playing two words
         */
        boolean result = false;
        boolean horizontal = false, vertical=false;
        
        switch (orientation){
            case 0:
                horizontal = true;
                vertical = false;
                break;
            case 1:
                vertical = true;
                horizontal = false;
                break;
        }
        int gapTiles = 0;

        if(debug)
                System.out.println("DCGenericScrabbleBoard: isWordConnected()-> tiles are not consecutive, testing for skipped tiles");
            for (int r = 0; r < indexes.length-1; r++){
                // skipped indexes
                int nSkipped = indexes[r+1] - indexes[r];
                int nextIndex = indexes[r]+1;
                while (nSkipped > 1){
                    String tileHolder = Integer.toString(nextIndex);
                    if (debug){
                        if(horizontal)
                            System.out.println("\tskipped tiles: "+nSkipped+".\tSkipped index:"+rowColIndex+"-"+tileHolder);
                        else if (vertical)
                            System.out.println("\tskipped tiles: "+nSkipped+".\tSkipped index:"+tileHolder+"-"+rowColIndex);
                    }
                    if(horizontal){
                        tileHolder = Integer.toString(rowColIndex)+"-"+tileHolder;
                    }else if (vertical){
                        tileHolder = tileHolder+"-"+Integer.toString(rowColIndex);
                    }
                    DCGenericTileHolder holder = mapTileHolders.get(tileHolder);
                    System.out.println("tileHolder ID: "+tileHolder+"\tTile:"+holder.getTile().getJsonString() + "\tHasChild(): "+holder.hasChild());
                    if (!holder.hasChild()){
                        /**
                         * Skipped tileHolder does not have a tile, so playing two words - invalid play, must return false.
                         */
                        result = false;
//                        return result;
                    } else {
                        gapTiles++;
                        result = true;
                    }
                    nextIndex++;
                    nSkipped--;
                }
            }
        
        return gapTiles;
    }
    
    public void toggleActiveTileHolders(Map<String,DCGenericTile>tiles){
        if(debug){
            System.out.println("DCGenericScrabbleBoard: toggleActiveTileHolders()->");
        }
        for (String ID: tiles.keySet()){
            DCGenericTileHolder holder = mapTileHolders.get(ID);
            if (holder.hasChild() && holder.isActive()){
                holder.toggleActive();
                if (debug){
                    System.out.println("\ttoggling tileholder "+ID);
                }
            } else {
                if(debug)
                    System.out.println("\tattempting tileholder "+ID+", but it is either already inactive or does not have a tile on it.");
            }
        }
        
    }
    
    private void dummyFunc_1(Map<String,DCGenericTile>word, int rowColIndex, boolean horizontal, boolean vertical, int[] indexes, int orientation){

        int ind=0;
        for (String key: word.keySet()){
            String[] rowCol = key.split("-");
            if (debug){
                if (vertical)
                    System.out.println("DCScrabbleBoard: isWordConnected()-> recreating tileHolder row-column: "+indexes[ind]+":"+rowCol[orientation]);
                else if (horizontal)
                    System.out.println("DCScrabbleBoard: isWordConnected()-> recreating tileHolder row-column: "+rowCol[orientation]+":"+indexes[ind]);
            }
            if (rowColIndex == -1)
                rowColIndex = Integer.parseInt(rowCol[orientation]);
            else {
                if (rowColIndex != Integer.parseInt(rowCol[orientation])){
                    if (debug)
                        System.out.println("DCScrabbleBoard: isWordConnected()-> ERROR: the tiles are not in a straight line");
//                    return result; commented out for moved code.
                }
            }
            ind++;
        }
        if (debug){
            if (rowColIndex == getWordRowColumn(word,orientation))
                System.out.println("testing getWordRowColumn: success!");
            else
                System.out.println("testing getWordRowColumn: fail :(");
        }
    }
    
    public void printBoard(){
        for (int row = -1; row < 15; row++){
            for (int col = -1; col < 15; col++){
                if (row == -1 && col == -1){
                    System.out.print("   ");
                } else if (row == -1) {
                    if (col >= 10)
                        System.out.print (" "+col);
                    else
                        System.out.print(" "+col+" ");
                } else if (col == -1) {
                    if (row >= 10)
                        System.out.print(" "+row);
                    else 
                        System.out.print(" "+row+" ");
                } else {
                    if(mapTileHolders.get(row+"-"+col).hasChild())
                        System.out.print(" "+mapTileHolders.get(row+"-"+col).getTile().getLetter()+" ");
                    else
                        System.out.print(" _ ");
                }
            }
            System.out.println("");
        }
    }
}
