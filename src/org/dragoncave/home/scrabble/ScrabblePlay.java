/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import javafx.scene.layout.Pane;
/**
 *
 * @author William J
 */
public class ScrabblePlay {
    
    private String playerName;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    private Pane boardPane;
    private boolean isValid;
    private Map<String,String> handTiles;
    private List<Map<String,DCTile>> prevWords;
    private List<Map<String,DCTile>> prevPlayTiles;
    private Map<String,DCTileHolder> allBoardTileHolders;
    private boolean isConnected;
//    private DCScrabbleBoard sb;
//    private DCTileBuilder tb;
//    public DCTileBuilder getTileBuilder(){
//        return new DCTileBuilder();
//    }
//    public Pane getBoard(){
//        return sb.getBoard();
//    }
    public ScrabblePlay(){
        isConnected = false;
        isValid = false;
        prevWords = new ArrayList<Map<String,DCTile>>();
        prevPlayTiles = new ArrayList<Map<String,DCTile>>();
        handTiles = new HashMap<>();
//        sb = new DCScrabbleBoard();
    }
    public void setTileMap(Map<String,DCTileHolder> map){
        this.allBoardTileHolders = map;
    }
    public DCSPlayResponse isWordValid(Map<String,DCTileHolder> charList){
        DCSPlayResponse response = new DCSPlayResponse();
        int beginningWithNumWords = prevWords.size();
        isConnected = false;
        boolean valid = true;
        boolean horizontal = isHorizontal(charList);
        boolean vertical = isVertical(charList);
        if (charList.size()==0){
            response.setValid(false);
            response.setMessage("You need to place a tile on the board to make a play.");
            return response;
        }
        if (!horizontal && !vertical){
            valid = false;
            response.setValid(false);
            response.setMessage("You need to play tiles in a straight line.");
            return response;
        }
        if (prevWords.isEmpty()){
            if (allBoardTileHolders.get("DCTileHolderBoard-7-7").getChildren().size() < 1){
                valid = false;
                System.out.println("no prewords found and tileholder at 7,7 is null");
                response.setValid(false);
                response.setMessage("This first play must have a tile in the center square;");
                return response;
            } else if(charList.size() == 1){
                valid = false;
                System.out.println("only one character played for the first word.");
                response.setValid(valid);
                response.setMessage("The first word placed on the board must be more than 1 character.");
            } else {
                response.setValid(true);
                isConnected = true;
            }
        } 
//        if (!isConnected){
//            valid = false;
//            return valid;
//        }
        int searchIndex = getSearchIndex(horizontal); 
        // reorderlist
        ArrayList<String> playedTileList = reorderCharList(charList,searchIndex);
        // split each ID into respective indexes
        ArrayList<String[]> dStrArray = new ArrayList<String[]>();
        ArrayList<String[]> dStrArray2 = new ArrayList<String[]>();
        for(String tileHolderID: playedTileList){
//            String[] tileIndexes = tileHolderID.split("-");
            dStrArray.add(splitString(tileHolderID));
            dStrArray2.add(splitString(tileHolderID));

        }
        
        
        insertSkippedTileHolders(dStrArray,searchIndex);
        boolean consecutive = isConsecutive(dStrArray,searchIndex);
        if (!consecutive){
            valid = false;
            System.out.println("filled hand is still not consecutive. trying to play two words. no cheating!");
            response.setValid(false);
            response.setMessage("Played tiles are not consecutive. Are you trying to play two words? \nThere is no cheating in this game");
            return response;
        }
//        System.out.println("array after inserted items:");

        // search for words made perpendicular to this word;
        int lettersInHand = dStrArray2.size();
        for (int letterNum = 0; letterNum < lettersInHand; letterNum++){
            int newSearchIndex = (searchIndex == 1 ?  2 : 1);
//            System.out.println("search index: "+searchIndex+"\tnewsearchIndex: "+newSearchIndex);
            ArrayList<String[]> perpendicular = new ArrayList<String[]>();
            String[] cString = Arrays.copyOf(dStrArray2.get(letterNum),3);
            perpendicular.add(cString);
            insertSkippedTileHolders(perpendicular, newSearchIndex);
            if (perpendicular.size() > 1 && isConnected){
                    addHandToPrevWordList(perpendicular);
            }
        }
        if(isConnected && dStrArray.size() > 0){
            addHandToPrevWordList(dStrArray);
            addToPlayedTiles(playedTileList);
        }
        System.out.println("there are now "+ prevWords.size() + " words played.");
        for (int pwIndex = beginningWithNumWords; pwIndex < prevWords.size(); pwIndex++) {
            Map m = prevWords.get(pwIndex);
            Set keys =  m.keySet();
            for(Object key: keys){
                DCTile t = (DCTile) m.get((String)key);
                System.out.print(t.getText());
            }
            System.out.print("\n");
        }

        
        if (!consecutive){
            // find holes - need to insert missing tileholders to plug holes;
            System.out.println("played tiles are not consecutive");
            
        } 
        
        if (!isConnected){
            System.out.println("word played is not connected.");
            response.setValid(false);
            response.setMessage("Tiles play are not adjacent to other tiles.");
            return response;
            
        }
        // save game action.
//        saveGame(playedTileList);
    
        // calculate points for hand
        
        int playScore = calculatePlayValue(charList,beginningWithNumWords);
        response.setValid(true);
        response.setScore(playScore);
        if (playScore > 30)
            response.setMessage("Magnificent!!!");
        if (playScore >20 && playScore <= 30)
            response.setMessage("Awesome!!!");
        if (playScore >10 && playScore <= 20)
            response.setMessage("Great job!");
        if (playScore > 0 && playScore <= 10)
            response.setMessage("Good job!");
        return response;
    }
    
    public int calculatePlayValue(Map<String,DCTileHolder> tileHolderID,int prevWordCount){
        int numPlayedTiles = tileHolderID.size();
        int wordScore = 0;
//            DCTileHolder holder = (DCTileHolder) allBoardTileHolders.get(tileHolderID.get(tileNum));
        for (int thisPlayWords = prevWordCount; thisPlayWords < prevWords.size(); thisPlayWords++){
            Map m = prevWords.get(thisPlayWords);
            Set keys = m.keySet();
            int wordValue = 0;
            int wordMultiplier = 1;
            for (Object key: keys){
                String k = (String) key;
                DCTileHolder th = (DCTileHolder) allBoardTileHolders.get(k);
                int lMultiplier = 1;
                if (tileHolderID.get(k)!= null){
                    lMultiplier = th.getLetterMultiplier();
                    wordMultiplier *= th.getWordMultiplier();
                }
                DCTile t = (DCTile) m.get(key);
                int letterValue = t.getValue();
                if (lMultiplier > 1){
                    letterValue*=lMultiplier;
                }
                wordValue+=letterValue;
            }
            wordScore += wordValue*wordMultiplier;
        }
        System.out.println("your score for this hand is: "+wordScore);
        return wordScore;
    }
    
    
    private void addHandToPrevWordList(ArrayList<String[]> dStrArray){
        Map<String,DCTile> mapHand = new HashMap<String,DCTile>();
        for (int index=0; index < dStrArray.size(); index++){
            String reconTileHolderID = reconstructTileHolderID(dStrArray.get(index));
//            System.out.println("\t"+reconTileHolderID);
            ObservableList<Node> observableNode = allBoardTileHolders.get(reconTileHolderID).getChildren();
//            System.out.println("\treturned node list contains "+observableNode.size()+" nodes");
            for (Node n: observableNode){
                DCTile t = (DCTile)n;
                mapHand.put(reconTileHolderID, t);
            }
        }
        prevWords.add(mapHand);
    }
    
    private String reconstructTileHolderID(String[] arrTileHolderID){
        String tileHolderID = "";
        for (int ind = 0; ind < arrTileHolderID.length; ind++){
            if (ind < arrTileHolderID.length-1)
                tileHolderID+=arrTileHolderID[ind]+"-";
            else
                tileHolderID+=arrTileHolderID[ind];
        }
        return tileHolderID;
    }
    
    private void insertSkippedTileHolders(ArrayList<String[]> dStrArray, int index){
        int searchIndex = index;
        
        // insert any prior fields first
        boolean hasPrior = true;
        while (hasPrior){
            hasPrior = insertPriorTileHolders(dStrArray, index);
        }
        int minRowCol = -1;
        int arraySize = dStrArray.size();
        
        for(int sIndex = 0; sIndex < arraySize; sIndex++){
            if (sIndex == 0){
                minRowCol = Integer.parseInt(dStrArray.get(sIndex)[searchIndex]);
            }
            if (sIndex < arraySize && sIndex > 0){
//                System.out.println("sIndex: "+sIndex+"\tminRowCol: "+minRowCol+"\tcurrent value: "+Integer.parseInt(dStrArray.get(sIndex)[searchIndex])+"\tArraySize: "+arraySize);
                if (minRowCol+1 < Integer.parseInt(dStrArray.get(sIndex)[searchIndex])){
                    String tileHolderID = dStrArray.get(sIndex)[0]+"-";
                    if (searchIndex == 1)
                        tileHolderID += Integer.toString(minRowCol+1)+"-"+ dStrArray.get(sIndex)[2];
                    else
                        tileHolderID += dStrArray.get(sIndex)[1] + "-" + Integer.toString(minRowCol+1) ;
                    
                    if ( allBoardTileHolders.get(tileHolderID).getChildren().size() == 1 ){
                        String[] insertedTileHolder = splitString(tileHolderID);
                        dStrArray.add(sIndex, insertedTileHolder);
                        isConnected = true;
//                        System.out.println("inserted array with tileholderid: "+tileHolderID);
//                        sIndex--;
                        arraySize++;
                    }
                }
                minRowCol++;
            }
        }
        
        // add trailing tiles
        boolean hasTrail = true;
        while (hasTrail){
            hasTrail = addTrailTileHolders(dStrArray, index);
        }
    }
    
    private boolean insertPriorTileHolders(ArrayList<String[]> dStrArray, int index){
        int searchIndex = index;
        int sIndex = 0;
        boolean hasPrior = false;
//        System.out.println("value of search index: "+Integer.parseInt(dStrArray.get(sIndex)[searchIndex]) );
        if (Integer.parseInt(dStrArray.get(sIndex)[searchIndex]) > 0 ){
            String tileHolderID = dStrArray.get(sIndex)[0]+"-";
            if (searchIndex == 1)
                tileHolderID += Integer.toString(Integer.parseInt(dStrArray.get(sIndex)[1])-1)+"-"+ dStrArray.get(sIndex)[2];
            else
                tileHolderID += dStrArray.get(sIndex)[1] + "-" + Integer.toString(Integer.parseInt(dStrArray.get(sIndex)[2])-1) ;
//            System.out.println("first tileHolderId: "+Arrays.toString(dStrArray.get(sIndex)));
//            System.out.println("reconstructed prior tileHolderID: "+tileHolderID);
//            System.out.println("prior tileholder has "+allBoardTileHolders.get(tileHolderID).getChildren().size()+" child(ren).");
            if ( allBoardTileHolders.get(tileHolderID).getChildren().size() == 1 ){
                String[] insertedTileHolder = splitString(tileHolderID);
                dStrArray.add(sIndex, insertedTileHolder);
                isConnected = true;
                hasPrior = true;
//                    arraySize++;
            }
        }        
        return hasPrior;
    }
    
    private boolean addTrailTileHolders(ArrayList<String[]> dStrArray, int index){
        int searchIndex = index;
        int sIndex = dStrArray.size()-1;
        boolean hasTrail = false;
        if (Integer.parseInt(dStrArray.get(sIndex)[searchIndex]) < 14 ){
            String tileHolderID = dStrArray.get(sIndex)[0]+"-";
            if (searchIndex == 1)
                tileHolderID += Integer.toString(Integer.parseInt(dStrArray.get(sIndex)[1])+1)+"-"+ dStrArray.get(sIndex)[2];
            else
                tileHolderID += dStrArray.get(sIndex)[1] + "-" + Integer.toString(Integer.parseInt(dStrArray.get(sIndex)[2])+1) ;
//            System.out.println("last tileHolderId: "+Arrays.toString(dStrArray.get(sIndex)));
//            System.out.println("reconstructed last tileHolderID: "+tileHolderID);
//            System.out.println("last tileholder has "+allBoardTileHolders.get(tileHolderID).getChildren().size()+" child(ren).");
            if ( allBoardTileHolders.get(tileHolderID).getChildren().size() == 1 ){
                String[] insertedTileHolder = splitString(tileHolderID);
                dStrArray.add(sIndex+1, insertedTileHolder);
                hasTrail = true;
                isConnected = true;
            }
        }
        return hasTrail;
    }
    
    private String[] splitString(String tileHolderID){
        String[] tileHolderParts = tileHolderID.split("-");
        return tileHolderParts;
    }
    
    private boolean isConsecutive(ArrayList<String[]> dStrArray,int ind ){
        boolean consecutive = true;
        int searchIndex = ind;
        
//        ArrayList<String[]> dStrArray = new ArrayList<String[]>();
        int minRowCol = -1;
        
        for(int index=0; index<dStrArray.size();index++){
            String[] tileIndexes = dStrArray.get(index);
            if (index == 0)
                minRowCol = Integer.parseInt(tileIndexes[searchIndex]);
            else {
                
                if (Integer.parseInt(tileIndexes[searchIndex]) != minRowCol+1){
                    consecutive = false;
                    break;
                }
                minRowCol = Integer.parseInt(tileIndexes[searchIndex]);
            }
        }
        return consecutive;
    }
    
    private int getSearchIndex(boolean horizontal){
        if (horizontal){
            return 2;
        } else {
            return 1;
        }
    }
    
    public ArrayList<String> reorderCharList(Map<String,DCTileHolder> charList,int index){
        ArrayList<String> newList = new ArrayList<String>();
        Set keys = charList.keySet();
        for(Object key: keys){
            newList.add((String)key);
        }
        int reorderingIndex = index;
        
        if (reorderingIndex > 0){
            int min = 0;
            String tempString = new String();
//            String[] orderedCharList = new String[charList.size()];
            for (int charListIndex = 0; charListIndex < charList.size()-1; charListIndex++){
                for(int charListIndex2 = 0; charListIndex2 < charList.size()-1; charListIndex2++){
                    String[] tileHolderIndexes = newList.get(charListIndex2).split("-");
                    String[] tileHolder2Indexes = newList.get(charListIndex2+1).split("-");
//                    System.out.println("index 1: "+tileHolderIndexes[reorderingIndex] + " index 2: "+tileHolder2Indexes[reorderingIndex]);
                    if ( Integer.parseInt(tileHolderIndexes[reorderingIndex]) > Integer.parseInt(tileHolder2Indexes[reorderingIndex])){
                        tempString = newList.get(charListIndex2+1);
                        newList.remove(charListIndex2+1);
                        newList.add(charListIndex2+1, newList.get(charListIndex2));
                        newList.remove(charListIndex2);
                        newList.add(charListIndex2,tempString);
                    }
                }
            }
        } else {
            System.out.println("cannot reorder list - is neither horizontal or vertical");
        }
//        System.out.println("printing reordered array:");
//        for (int nlIndex = 0; nlIndex < newList.size(); nlIndex++){
//            System.out.println("\t"+newList.get(nlIndex));
//        }
        return newList;
    }
    
    private boolean isHorizontal(Map<String,DCTileHolder> charList){
        boolean horizontal = true;
        int row = -1;
        Set<String> keys = charList.keySet();
        for(String key: keys){
            String[] tileHolderIndexes = key.split("-");
            if (row == -1){
                row = Integer.parseInt(tileHolderIndexes[1]);
            }else{
                if (Integer.parseInt(tileHolderIndexes[1]) != row){
                    horizontal = false;
                    break;
                }
            }
        }
//        System.out.println(horizontal ? "hand is horizontal":"hand is not horizontal");
        return horizontal;
    }
    
    private boolean isVertical(Map<String,DCTileHolder> charList){
        boolean vertical = true;
        int col = -1;
        Set<String> keys = charList.keySet();
        for(String key: keys){
            String[] tileHolderIndexes = key.split("-");
            if (col == -1){
                col = Integer.parseInt(tileHolderIndexes[2]);
            }else{
                if (Integer.parseInt(tileHolderIndexes[2]) != col){
                    vertical = false;
                    break;
                }
            }
        }
//        System.out.println(vertical ? "hand is vertical":"hand is not vertical");
        return vertical;
    }
    public void saveGame(){
        savePrevWords();
        savePlayedTiles();
        
    }
    public Map<String,String> getPlayedTiles(){
        return handTiles;
    }
    private void addToPlayedTiles(ArrayList<String> listTileHolderID){
        int initialPrevPlayTilesSize = prevPlayTiles.size();
        for (String tileHolderID: listTileHolderID){
            Map<String,DCTile> m = new HashMap<>();
            DCTileHolder tileHolder = (DCTileHolder) allBoardTileHolders.get(tileHolderID);
            ObservableList<Node> observableList = tileHolder.getChildren();
            for (Node n: observableList){
                DCTile tile = (DCTile) n;
                m.put(tileHolderID, tile);
            }
            prevPlayTiles.add(m);
        }
        // create a new mapping of tiles to send to server.
        handTiles.clear();
//        System.out.println("handTiles cleared, new size: "+handTiles.size());
        for (int index = initialPrevPlayTilesSize; index < prevPlayTiles.size(); index++){
            for (Map.Entry<String,DCTile> entry: prevPlayTiles.get(index).entrySet()){
//                System.out.println("\tgetting next tile: "+prevPlayTiles.get(index).toString());
                DCTile t = entry.getValue();
                String key = entry.getKey();
                String tileID = t.getTileID();
                handTiles.put(key, t.toString());
            }
        }
        
        //need to communicate this with server
        
    }
    private void savePlayedTiles(){
        try(FileWriter out = new FileWriter("ScrabblePlayedTiles.dat")){
            out.write(prevPlayTiles.toString());
//            for (Map m: prevPlayTiles) {
//                Set keys = m.keySet();
//                for (Object key: keys){
//                    DCTile tile = (DCTile)m.get(key);
//                    out.write((String)key+":"+tile.getTileID()+":"+tile.getText()+";");
//                }
//                out.write("\n");
//            }
        } catch (IOException e){
            e.printStackTrace();
        }
        
//        System.out.println(prevPlayTiles.toString());
    }
    
    private void savePrevWords(){
        try(FileWriter out = new FileWriter("ScrabblePrevWords.dat")) {
            out.write(prevWords.toString());
//            for (int pwIndex = 0; pwIndex < prevWords.size(); pwIndex++){
//                Map m = prevWords.get(pwIndex);
//                Set keys = m.keySet();
//                for(Object key: keys){
//                    String k = (String)key;
//                    DCTile tile =(DCTile) m.get(key);
//                    String tileID = tile.getTileID();
//                    out.write(k+":"+tileID+":"+tile.getText()+";");
//                }
//                out.write("\n");
//            }
        } catch (IOException f){
            System.out.println("File not found exception.");
        }
        
//        System.out.println(prevWords.toString());
        
        
    }
    
    private void loadSaved(List<Map<String,DCTile>> listMap,String filename){
        try (BufferedReader in = new BufferedReader(new FileReader(filename))){ 
            String line;
            while ((line = in.readLine()) != null){
                String subline = line.replaceAll("\\}, ", ";").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\[", "").replaceAll("\\]", "");
                String[] words = subline.split(";");
                for (String word: words){
                    String[] characters = word.split(", ");
                    Map<String,DCTile> map = new HashMap<>();
                    for (String charmap: characters){
                        String[] comp = charmap.split("=");
                        map.put(comp[0],new DCTile(comp[1]));
                    }
                    listMap.add(map);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
    public void loadPrevWords(){
        if (prevWords.size()>0)
            prevWords.clear();
        System.out.println("loading prevWords...");
        loadSaved(prevWords,"ScrabblePrevWords.dat");
        System.out.println("finished loading prevWords.");
    }
    public void loadPrevPlayTiles(){
        if (prevPlayTiles.size() > 0)
            prevPlayTiles.clear();
        System.out.println("loading prevPlayedTiles...");
        loadSaved(prevPlayTiles,"ScrabblePlayedTiles.dat");
        System.out.println("finished loading prevPlayedTiles.");
    }
    
    public List<Map<String,DCTile>> getPrevPlayedTiles(){
        return prevPlayTiles;
    }
    
    public void resetPlay(){
        System.out.println("resetting scrabble play:\n resetting prevPlayTiles...");
        prevPlayTiles.clear();
        System.out.println("done restting prevPlayTiles. Resetting prevWords...");
        prevWords.clear();
        System.out.println("done resetting prevWords.");
    }
}
