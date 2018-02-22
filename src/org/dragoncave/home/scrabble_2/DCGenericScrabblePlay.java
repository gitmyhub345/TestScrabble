/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
/**
 *
 * @author Rider1
 */
public class DCGenericScrabblePlay {
    private List<Map<String,DCGenericTile>>words;
    private boolean debug;
    private int playValue;
    private DCGenericScrabbleBoard board;
    private int initialPlayTilesLength;
    
    private List<Map<String,DCGenericTile>> playedWords;  // list of words played after adding any leading, middle, and trailing characters not on original map
    private List<DCScrabbleWordStats> playStats;
    private static DCScrabbleUtils utils;
    
    private Map<String,DCGenericTile> mapLastPlayedTiles; // copy of last played tiles - just in case play is invalid after challenge, so board and reverse plays.
    
    public void setDebug(boolean debug){
        this.debug = debug;
    }
    
    public void setBoard(DCGenericScrabbleBoard board){
        this.board = board;
    }
    public DCGenericScrabblePlay(){
        words = new ArrayList<Map<String,DCGenericTile>>(0);
        playedWords = new ArrayList<Map<String,DCGenericTile>>(0);
        playStats = new ArrayList<>();
        utils = new DCScrabbleUtils();
        initialPlayTilesLength = 0;
        mapLastPlayedTiles = new HashMap<>();
        this.playValue = 0;
    }
    
    public DCGenericScrabblePlay(DCGenericScrabbleBoard board){
        this.board = board;
        words = new ArrayList<Map<String,DCGenericTile>>(0);
        playedWords = new ArrayList<Map<String,DCGenericTile>>(0);
        this.playValue = 0;
        playStats = new ArrayList<>();
        utils = new DCScrabbleUtils();
        initialPlayTilesLength = 0;
        mapLastPlayedTiles = new HashMap<>();
    }
    
    public int getPlayValue(){
        return this.playValue;
    }
    
    public boolean isHorizontal(Map<String,DCGenericTile> word) {
        if (debug)
            System.out.println("DCGenericScrabblePlay: isHorizontal()->");
        boolean result = false;
        int row=-1;
        for (String key: word.keySet()){
            if(row == -1){
                if (debug)
                    System.out.println("\t"+Integer.parseInt(key.split("-")[0]));
                row = Integer.parseInt(key.split("-")[0]);
            } else {
                if (debug)
                    System.out.println("\t"+Integer.parseInt(key.split("-")[0]));
                if ( row == Integer.parseInt(key.split("-")[0])){
                    result = true;
                } else {
                    result = false;
                    break;
                }
            }
        }
        if(debug)
            System.out.println("\tisHorizontal = "+result);
        return result;
    }

    public boolean isVertical(Map<String,DCGenericTile> word) {
        if (debug)
            System.out.println("DCGenericScrabblePlay: isVertical()->");
        boolean result = false;
        int col=-1;
        for (String key: word.keySet()){
            if(col == -1){
                if (debug)
                    System.out.println("\t"+Integer.parseInt(key.split("-")[1]));
                col = Integer.parseInt(key.split("-")[1]);
            } else {
                if (debug)
                    System.out.println("\t"+Integer.parseInt(key.split("-")[1]));
                if ( col == Integer.parseInt(key.split("-")[1])){
                    result = true;
                } else {
                    result = false;
                    break;
                }
            }
        }
        if (debug)
            System.out.println("\tisVertical = "+result);
        return result;
    }
    
    public boolean isValid(Map<String,DCGenericTile> pword){
        if (debug){
            System.out.println("DCGenericScrabblePlay: isValid()->");
        }
        initialPlayTilesLength = pword.size();
        this.playValue = 0;
        this.playedWords.clear();
        playStats.clear();
        mapLastPlayedTiles.clear();
        boolean result = false;
        if (words.isEmpty() && pword.size() <= 1){
            if (debug)
                System.out.println("\tnumber of words on played: "+words.size()+"\tnumber of characters played: "+pword.size()+"\n\tfirst word played cannot be only 1 character in length... try agan.");
            return result;
        }
        
        Map<String,DCGenericTile> dupWord = cloneWord(pword);
        Map<String,DCGenericTile> word = cloneWord(pword);
        mapLastPlayedTiles = cloneWord(pword);
//        dupWord.putAll(word);
        
        if (!utils.mappedTilesEqual(word, dupWord))
            return false;
        
        boolean horizontal = isHorizontal(word);
        boolean vertical = isVertical(word);

        int orientation=-1;
        int[] indexes;  // indexes to sort the row/columns for faster sorting and comparison

        if(horizontal || (!horizontal && !vertical && !words.isEmpty() && pword.size() == 1)){
            orientation = 0;
            indexes = getColIndexes(word);
        } else if (vertical){
            orientation = 1;
            indexes = getRowIndexes(word);
//        } else if (!horizontal && !vertical && !words.isEmpty()){
//            orientation = 0;
//            indexes = getColIndexes(word);
        } else {
            if(debug)
                System.out.println("\tword is neither horizontal or vertical.");
//            if (words.isEmpty())
                return result;
        }
        
        int indexRowCol = getWordRowColumn(word,orientation);
        if (words.isEmpty()){
        /**
         * special handling for first word.... needs to be consecutive and has middle tileholder.
         */
            if(debug){
                System.out.println("\tfirst word.");
            }
            result = isConsecutive(word);
            if (result){
                DCGenericTile tile = word.get("7-7");
                result = tile == null? false : true;
                if (debug){
                    System.out.println("\tconsecutive. on middle tileholder? "+result);
                }
                if (result){
                    board.addWordsToBoard(word);
                    playValue = calculateWordValue(word);
                    if (initialPlayTilesLength == 7)
                        playValue += 50;
                    words.add(word);
                    playedWords.add(word);
                    String w = utils.mapToWord(word);
                    playStats.add(new DCScrabbleWordStats(w,playValue));
                    // toggle tileholder so won't add modifiers for next play.
                    board.toggleActiveTileHolders(dupWord);
                    if(debug){
                        System.out.println("\tadded word to playedWords list. "+word.toString());
                        System.out.println("\ttoggling tileholders");
                    }
                }
            } else {
                if (debug)
                    System.out.println("\tnot consecutive.");
            }
        } else {
            result = isWordConnected(indexes, orientation, word);
            if(result){
                fillInGaps(indexes, orientation, indexRowCol, word);
                addLeadingTiles(indexes, orientation, indexRowCol, word);
                addTrailingTiles(indexes, orientation, indexRowCol, word);
                board.addWordsToBoard(word);

                if (!words.isEmpty() && word.size() > 1){
                    words.add(word);
                    playedWords.add(word);
                    if(debug){
                        System.out.println("\tadded word to playedWords list."+word.toString());
                    }
                }

                if (word.size() > 1) {
                    playValue = calculateWordValue(word);
                    if (initialPlayTilesLength == 7)
                        playValue += 50;
                }
                
                String w = utils.mapToWord(word);
                playStats.add(new DCScrabbleWordStats(w,playValue));

                
                if (debug)
                    System.out.println("\tDCGenericScrabblePlay: isValid()-> calculated word value..." + playValue);

                int perpendicularWords = hasPerpendicular(indexes, orientation,indexRowCol);
                if (debug){
                    System.out.println("\tWord has "+ perpendicularWords+" perpendicular words");
                    System.out.println("\tfinding perpendicular words...");
                }
                if (perpendicularWords > 0){
                    findPerpendicularWords(indexes, orientation, indexRowCol, dupWord);
                }
                // need to toggle tileholders to inactive for playedTiles
                board.toggleActiveTileHolders(dupWord);
            }
        }
        
        if (debug){
            System.out.println("\tisValid() result = "+result);
            System.out.println("\n\n/**************************************/\n\tcloned dupword: "+dupWord.toString()+"\n\toriginal word: "+pword.toString());
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
        if (debug)
            System.out.println("DCGenericScrabblePlay: isConsecutive()->");
        boolean result = false;
        
        int[] rowIndexes = getRowIndexes(word);
        int[] colIndexes = getColIndexes(word);
        if(word.size() == 1)
            return true;
        
        if (isHorizontal(word)){
            if (debug)
                System.out.println("\tword is horizontal");
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
                System.out.println("\tword is vertical");
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
        
        if (debug)
            System.out.println("\tisConsecutive()-> "+result);
        return result;
    }
    
    private void intArraySort(int[] array){
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
    
    private void fillInGaps(int[] indexes, int orientation, int indexRowCol, Map<String,DCGenericTile>tiles){
        if (debug){
            System.out.println("DCGenericScrabblePlay: fillInGaps()->...");
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
                if (board.getTileHolder(tileHolderID).hasChild()){
                    tiles.put(tileHolderID, board.getTileHolder(tileHolderID).getTile());
                    if (debug){
                        System.out.println("\tadding tile in gap: "+tileHolderID + "\ttile: "+board.getTileHolder(tileHolderID).getTile().getJsonString());
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
            System.out.println("DCGenericScrabblePlay: addLeadingTiles()->...");
        }
        int minIndex = indexes[0]-1;
        if (minIndex >= 0) {
            String tileHolderID = "";
            switch(orientation){
                case 0: // horizontal, row remains the same, column changes
                    tileHolderID = Integer.toString(indexRowCol)+"-"+Integer.toString(minIndex);
                    break;
                case 1: // vertical, row changes, column remains the same
                    tileHolderID = Integer.toString(minIndex)+"-"+Integer.toString(indexRowCol);
                    break;
            }
            boolean result = board.getTileHolder(tileHolderID).hasChild();
            if (debug){
                System.out.println("\tfirst tileHolderID: "+tileHolderID+"\thasChild: "+result);
            }
            while (result){
                tiles.put(tileHolderID, board.getTileHolder(tileHolderID).getTile());
                // change to next tileHolderID
                minIndex--;
                if(minIndex < 0)
                    break;
                switch(orientation){
                    case 0: // horizontal, row remains the same, column changes
                        tileHolderID = Integer.toString(indexRowCol)+"-"+Integer.toString(minIndex);
                        break;
                    case 1: // vertical, row changes, column remains the same
                        tileHolderID = Integer.toString(minIndex)+"-"+Integer.toString(indexRowCol);
                        break;
                }
                result = board.getTileHolder(tileHolderID).hasChild();
                if (debug){
                    System.out.println("\tnext tileHolderID: "+tileHolderID+"\thasChild: "+result);
                }
            }
        } else {
            if (debug)
                System.out.println("\tbeginning tile is already at the first row/column, no leading characters can be added.");
        }
    }
    
    public void addTrailingTiles(int[] indexes, int orientation, int indexRowCol, Map<String,DCGenericTile>tiles){
        if (debug){
            System.out.println("DCGenericScrabblePlay: addTrailingTiles()->...orientation: "+orientation+"\tindexRowCol: "+indexRowCol);
        }
        int maxIndex = indexes[indexes.length-1]+1;
        if (maxIndex <= 14){
            String tileHolderID = "";
            switch(orientation){
                case 0: // horizontal, row remains the same, column changes
                    tileHolderID = Integer.toString(indexRowCol)+"-"+Integer.toString(maxIndex);
                    break;
                case 1: // vertical, row changes, column remains the same
                    tileHolderID = Integer.toString(maxIndex)+"-"+Integer.toString(indexRowCol);
                    break;
            }
            boolean result = board.getTileHolder(tileHolderID).hasChild();
            if (debug){
                System.out.println("\ttileHolderID: "+tileHolderID+"\thasChild: "+result);
            }
            while (result){
                tiles.put(tileHolderID, board.getTileHolder(tileHolderID).getTile());
                // change to next tileHolderID
                maxIndex++;
                if (maxIndex > 14)
                    break;
                switch(orientation){
                    case 0: // horizontal, row remains the same, column changes
                        tileHolderID = Integer.toString(indexRowCol)+"-"+Integer.toString(maxIndex);
                        break;
                    case 1: // vertical, row changes, column remains the same
                        tileHolderID = Integer.toString(maxIndex)+"-"+Integer.toString(indexRowCol);
                        break;
                }
                result = board.getTileHolder(tileHolderID).hasChild();
                if (debug){
                    System.out.println("\tnext tileHolderID: "+tileHolderID+"\thasChild: "+result);
                }
            }
        } else {
            if (debug)
                System.out.println("\ttrailing tile is already at the last row/column, no trailing characters can be added.");
        }
        
    }
    
    public void findPerpendicularWords(int[] indexes, int orientation, int indexRowCol, Map<String,DCGenericTile>tiles){
        if(debug){
            System.out.println("DCGenericScrabblePlay: findPerpendicularWords()->...orientation: "+orientation+"\tindexRowCol: "+indexRowCol);
            for (int a = 0;a < indexes.length; a++){
                System.out.print(indexes[a]+", ");
            }
            System.out.println("");
        }
        List<Map<String,DCGenericTile>>perpendicularWords = new ArrayList<Map<String,DCGenericTile>>();
        String thID = "";
        String sthID = "";
        String ethID = "";
        int newIndexRowCol=-1;
        int porientation = -1;
        int[] newIndex = new int[1];
        for (int index = 0; index < indexes.length; index++){
            porientation = orientation == 0 ? 1 : 0; 
            switch(orientation){
                case 0: // horizontal - row remains constant while column changes
                    sthID = (indexRowCol-1)+"-"+(indexes[index]);
                    thID = indexRowCol+"-"+indexes[index];
                    ethID = (indexRowCol+1)+"-"+(indexes[index]);
                    newIndexRowCol = indexes[index];
                    newIndex[0] = indexRowCol;
                    break;
                case 1: // vertical - row changes while column remains constant
                    sthID = (indexes[index])+"-"+(indexRowCol-1);
                    thID = indexes[index]+"-"+indexRowCol;
                    ethID = (indexes[index])+"-"+(indexRowCol+1);
                    newIndexRowCol = indexes[index];
                    newIndex[0] = indexRowCol;
                    break;
            }
            if (debug)
                System.out.println("\tsthID: "+sthID+"\tthID: "+thID+"\tethID: "+ethID+"\tnew oriention:"+porientation+"\tnew indexRowCol: "+newIndexRowCol+"\tnew beginning and ending index: "+newIndex[0]);
            if (board.getTileHolder(sthID)!= null){
                if(board.getTileHolder(sthID).hasChild()){
                    Map<String,DCGenericTile>pword = new HashMap<>(0);
                    pword.put(thID, tiles.get(thID));
                    addLeadingTiles(newIndex,porientation,newIndexRowCol,pword);
                    addTrailingTiles(newIndex,porientation,newIndexRowCol,pword);
                    if(pword.size() > 1 ){
                        perpendicularWords.add(pword);
                    }
                    if(debug){
                        System.out.println("\tleading perpendicular tile found for current tileholder: "+thID+"\n\t\tnew word tiles: "+pword.toString());
                    }
                }else{
                    if(board.getTileHolder(ethID) != null && board.getTileHolder(ethID).hasChild()){
                        Map<String,DCGenericTile>pword = new HashMap<>(0);
                        pword.put(thID, tiles.get(thID));
                        addTrailingTiles(newIndex,porientation,newIndexRowCol,pword);
                        if(pword.size() > 1) {
                            perpendicularWords.add(pword);
                        }
                        if(debug)
                            System.out.println("\ttrailing perpendicular tile found for current tileholder: "+thID+"\n\t\tnew word tiles: "+pword.toString());
                    }    
                }
            } else {
                
                if (board.getTileHolder(ethID) != null){
                    
                    if(board.getTileHolder(ethID).hasChild()){
                        Map<String,DCGenericTile>pword = new HashMap<>(0);
                        pword.put(thID, tiles.get(thID));
//                        addLeadingTiles(newIndex,porientation,newIndexRowCol,pword);
                        addTrailingTiles(newIndex,porientation,newIndexRowCol,pword);
                        if(pword.size() > 1){
                            perpendicularWords.add(pword);
                        }
                        if(debug)
                            System.out.println("\ttrailing perpendicular tile found for current tileholder: "+thID+"\n\t\tnew word tiles: "+pword.toString());
                    }    
                }
                
            }
            
        }
        if(debug)
            System.out.println("perpendicular words: "+perpendicularWords.toString());
        
        int pWordValue = 0;
        for (Map<String,DCGenericTile> word: perpendicularWords){
            if(debug){
                System.out.println("\tcalculating value of perpendicular word...");
            }
            pWordValue = calculateWordValue(word);
            String w = utils.mapToWord(word);
            playStats.add(new DCScrabbleWordStats(w,pWordValue));

            this.playValue += pWordValue;
        }
        
        if (debug)
            System.out.println("\tTotal value of played word is: "+this.playValue);
        
        words.addAll(perpendicularWords);
        playedWords.addAll(perpendicularWords);
        if(debug){
            System.out.println("\tadded "+perpendicularWords.size()+" perpendicular words to playedWords list.");
        }
        
    }
    
    private boolean isWordConnected(int[] indexes, int orientation, Map<String,DCGenericTile>word){
        boolean connected = false;
        /**
         * Sort indexes 
         */
        intArraySort(indexes);
        if (debug){
            System.out.println("DCGenericScrabblePlay: isWordConnected()-> sorted indexes array...");
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
            System.out.println("\tthe played tiles are on row/column: "+rowColIndex);
        if (rowColIndex < 0){
            return false;
        }
        
        boolean consec = isConsecutive(word);

        int gapTiles = 0;
        if (!consec){
            /**
             * checking to see if skipped tiles exists, if not, then playing two words
             */
            if(debug)
                System.out.println("\tPlayed tiles are not consecutive -> Checking for tiles in gap(s)...");
            gapTiles = hasGapTiles(indexes, orientation, rowColIndex );
            connected = gapTiles > 0? true: false;
            if(debug){
                System.out.println("\tSkipped tiles have child(ren)"+"\tResult from previous: "+gapTiles);
            }
            return connected;
        } else {
        
        
            /**
             * check for tiles at the beginning and end of tiles
             */
            if(debug)
                System.out.println("\tPlayed tiles are consecutive -> Checking for leading and trailing characters...");
            int startIndex, endIndex;
            startIndex = indexes[0]-1;
            endIndex = indexes[indexes.length-1]+1;
            String mapStartID = "";
            String mapEndID = "";
            if (orientation == 0){
                mapStartID = Integer.toString(rowColIndex)+"-"+Integer.toString(startIndex);
                mapEndID = Integer.toString(rowColIndex)+"-"+Integer.toString(endIndex);
                
            } else if (orientation == 1){
                mapStartID = Integer.toString(startIndex)+"-"+Integer.toString(rowColIndex);
                mapEndID = Integer.toString(endIndex)+"-"+Integer.toString(rowColIndex);
                
            }
            if (startIndex >= 0) {
                connected = board.getTileHolder(mapStartID).hasChild();
                if(debug)
                 System.out.println("\tleading characters found: "+connected);
            } else {
                if (debug)
                    System.out.println("\tleading character cannot be found because startIndex < 0");
            }
            if (endIndex <= 14){
                if (!connected){
                    connected = board.getTileHolder(mapEndID).hasChild();
                    if(debug)
                        System.out.println("\ttrailing characters found: "+connected);
                }
            }else{
                if (debug)
                    System.out.println("\ttrailing character cannot be found because endIndex > 14");
            }
        }
        
        if(!connected){
            if(debug)
                System.out.println("\tPlayed Tiles are not still not connected -> checking for perpendicular words...");
            int connectedTiles = hasPerpendicular(indexes,orientation,rowColIndex);
            if (connectedTiles > 0)
                connected = true;
        }
        
        return connected;
    }
    
    private int hasPerpendicular(int[] indexes, int orientation, int indexRowCol){
        if (debug)
            System.out.println("DCGenericScrabblePlay: hasPerpendicular()-> orientation: "+orientation+"\tindexRowCol: "+indexRowCol);
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
                    endTileHolderID = Integer.toString(indexRowCol+1)+"-"+Integer.toString(indexes[ind]);
                    
                    break;
            }
            if (indexRowCol+1 <= 14 && indexRowCol-1 >= 0){
                if(debug)
                    System.out.print("DCGenericScrabblePlay: hasPerpendicular() -> checking tileHolderID "+startTileHolderID + " and "+endTileHolderID);
                if (board.getTileHolder(startTileHolderID).hasChild() || board.getTileHolder(endTileHolderID).hasChild()) {
                    result++;
                    if (debug)
                        System.out.println("\tFound a perpendicular word for tile between "+startTileHolderID+" and "+endTileHolderID);
                } else {
                    if (debug)
                        System.out.println("\tDid not find a perpendicular word for tile between "+startTileHolderID+" and "+endTileHolderID);
                }
            }else{
                if (debug)
                    System.out.println("\tedge case: start ID: "+startTileHolderID+"\t: end ID: "+endTileHolderID);
                if (board.getTileHolder(startTileHolderID) != null){
                    if(debug)
                        System.out.println("\t\tstart ID exists: start ID "+startTileHolderID);
                    if(board.getTileHolder(startTileHolderID).hasChild())
                        result++;
                } else if (board.getTileHolder(endTileHolderID)!=null){
                    if(debug)
                        System.out.println("\t\tstart ID exists: start ID "+endTileHolderID);
                    if(board.getTileHolder(endTileHolderID).hasChild())
                        result++;
                    
                }
            }
        }
        
        if (debug)
            System.out.println("\tFound "+ result+" perpendicular word(s).");
        return result;
    }
    
    private int hasGapTiles(int[] indexes, int orientation, int rowColIndex){
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
        int gapTiles = 0, nSkipped = 0, nextIndex = 0;

        if(debug)
            System.out.println("DCGenericScrabblePlay: hasGapTiles()-> tiles are not consecutive, testing for skipped tiles");
        for (int r = 0; r < indexes.length-1; r++){
            // skipped indexes
            nSkipped = indexes[r+1] - indexes[r];
            nextIndex = indexes[r]+1;
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
                DCGenericTileHolder holder = board.getTileHolder(tileHolder);
                if (!holder.hasChild()){
                    /**
                     * Skipped tileHolder does not have a tile, so playing two words - invalid play, must return false.
                     */
                    if (debug){
                        System.out.println("\ttileHolder ID: "+tileHolder+" does not have a tile... skipping multiple spaces without tiles means playing multiple words.");
                    }
                    result = false;
                    gapTiles = -1;
                    break;
//                        return result;
                } else {
                    if (debug){
                        System.out.println("\ttileHolder ID: "+tileHolder+"\tTile:"+holder.getTile().getJsonString() + "\tHasChild(): "+holder.hasChild());
                    }
                    gapTiles++;
                    result = true;
                }
                nextIndex++;
                nSkipped--;
            }
            if(gapTiles == -1)
                break;
        }
        
        return gapTiles;
    }
    
    public void wordPlay(Map<String,DCGenericTile>tiles){
        int[] rowIndexes = getRowIndexes(tiles);
        int[] colIndexes = getColIndexes(tiles);
        
        boolean horizontal = isHorizontal(tiles);
        boolean vertical = isVertical(tiles);
        
        boolean consecutive = isConsecutive(tiles);
        
    }
    
    private void dummyFunc_1(Map<String,DCGenericTile>word, int rowColIndex, boolean horizontal, boolean vertical, int[] indexes, int orientation){

        int ind=0;
        for (String key: word.keySet()){
            String[] rowCol = key.split("-");
            if (debug){
                if (vertical)
                    System.out.println("DCGenericScrabblePlay: isWordConnected()-> recreating tileHolder row-column: "+indexes[ind]+":"+rowCol[orientation]);
                else if (horizontal)
                    System.out.println("DCGenericScrabblePlay: isWordConnected()-> recreating tileHolder row-column: "+rowCol[orientation]+":"+indexes[ind]);
            }
            if (rowColIndex == -1)
                rowColIndex = Integer.parseInt(rowCol[orientation]);
            else {
                if (rowColIndex != Integer.parseInt(rowCol[orientation])){
                    if (debug)
                        System.out.println("DCGenericScrabblePlay: isWordConnected()-> ERROR: the tiles are not in a straight line");
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
    
    private int calculateWordValue(Map<String,DCGenericTile>word){
        if (debug)
            System.out.println("DCGenericScrabblePlay: calculateWordValue()->"+word.toString());
        int value = 0;
        int wordMultiplier = 1;
        for (String key: word.keySet()){
            DCGenericTileHolder holder = board.getTileHolder(key);
            if(debug){
                System.out.println("\ttileHolderID: "+key+"\tvalue: "+holder.getTile().getValue());
            }
            int tilevalue = holder.getTile().getValue();
            if ((holder.getMultiplierType().equals("dls") || holder.getMultiplierType().equals("tls")) && holder.isActive() ){
                if(debug)
                    System.out.println("\ttileHolder type: "+holder.getMultiplierType()+"\tmultiplier value: "+holder.getValueMultiplier());
                tilevalue *= holder.getValueMultiplier(); 
            } else if ((holder.getMultiplierType().equals("dws") || holder.getMultiplierType().equals("tws")) &&
                    holder.isActive()){
                if(debug)
                    System.out.println("\ttileHolder type: "+holder.getMultiplierType()+"\tmultiplier value: "+holder.getValueMultiplier());
                wordMultiplier *= holder.getValueMultiplier();
            }
            value += tilevalue;
        }
        value *= wordMultiplier;
        if(debug)
            System.out.println("\tword value is "+value);
        return value;
    }
    
    public Map<String,DCGenericTile> getLastPlayedMap(){
        return mapLastPlayedTiles;
    }
    public void challengePlay(String playerName){
        /**
         * challenge play function
         * play cannot be finalized until no challenges.
         */
    }
    
    public void finalizePlay(){
        /**
         * this is finally add played words to the final list.
         */
    }
    
    public List<Map<String,DCGenericTile>> getWords(){
        return words;
    }
    
    public List<Map<String,DCGenericTile>> getPlayWords(){
        return playedWords;
    }
    private Map<String,DCGenericTile> cloneWord(Map<String,DCGenericTile> word){
        Map<String,DCGenericTile> clone = new HashMap<>(word.size());
        for(Entry<String,DCGenericTile> entry: word.entrySet()){
            DCGenericTile tile = new DCGenericTile("A",1); // just to create a generic tile
            tile.fromJsonString(entry.getValue().getJsonString());
            clone.put(entry.getKey(), tile);
        }
//        clone.putAll(word);

        boolean cloneIsEqual = utils.mappedTilesEqual(word, clone);
        if (debug){
            System.out.println("DCGenericScrabblePlay: cloneWord()-> cloned map is equal: "+cloneIsEqual);
        }
        return clone;
    }
    
    public List<DCScrabbleWordStats> getPlayStats(){
        return playStats;
    }
    
    public void removeLastPlayedTilesWords(){
        int numMappingEntries = words.size();
        if(debug){
            System.out.println("DCGenericScrabblePlay: removeLastPlayedTilesWords()-> "+words.get(numMappingEntries-1));
        }
        words.remove(numMappingEntries-1);
    }
}
