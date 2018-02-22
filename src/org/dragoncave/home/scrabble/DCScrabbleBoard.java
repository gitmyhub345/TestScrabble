/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
/**
 *
 * @author William J
 */
public class DCScrabbleBoard {
    private Pane userPane;
    private UserPane userHandPane;
    private final Pane boardPane;
    
    private Map<String,DCTileHolder> allTileHolders;
    private Map<String,DCTile> hand;
    private DCTileHolder tile;
    
    private Map<String,DCTileHolder> placedTileHolders;
    
    private final String[][] board = {{"tws","sls","sls","dls","sls","sls","sls","tws","sls","sls","sls","dls","sls","sls","tws"},
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
    public Pane getBoard(){
        return boardPane;
    }
    
    public Pane getUserPane(){
        return userPane;
    }
    
    public void setHand(Pane userPane, Map<String,DCTile> hand){
        this.userPane = userPane;
        this.hand = hand;
//        tile.setUserPane(userPane);
        ObservableList<Node> tl = boardPane.getChildren();
        for(Node n: tl){
//            System.out.println(n.getClass().toString());
            DCTileHolder th = (DCTileHolder)n;
            th.setUserPane(userPane);
        }
    }
    public void setUserHand(UserPane userPane, Map<String,DCTile> hand){
        this.userHandPane = userPane;
        this.hand = hand;
//        tile.setUserPane(userPane);
        ObservableList<Node> tl = boardPane.getChildren();
        for(Node n: tl){
//            System.out.println(n.getClass().toString());
            DCTileHolder th = (DCTileHolder)n;
            th.setUserHandPane(userHandPane);
        }
    }
    public DCScrabbleBoard(){
        boardPane = new Pane();

        boardPane.setMinWidth(455);
        boardPane.setMaxWidth(455);
        boardPane.setMinHeight(455);
        boardPane.setMaxHeight(455);
//        boardPane.setStyle("-fx-background-color: #bbbbff");
        placedTileHolders = new HashMap<String,DCTileHolder>();
        allTileHolders = new HashMap<String,DCTileHolder>();
        /**
         * for testing purposed only
         */
//            enableDragDrop();
//            addTileHolder();    
        /**
         * end testing 
         */

        drawBoard();
    }
    
    private void addTileHolder(){
        tile = new DCTileHolder();
        if(userPane == null)
            System.out.println("userPane is null");
        else
            tile.setUserPane(userPane);
//        boardPane.getChildren().add(tile.getNode());
        boardPane.getChildren().add(tile);
        tile.relocate(0, 0);
        
    }
    
    private void drawBoard(){
        System.out.println("board rows: "+board.length);
        System.out.println("board columns: "+board[0].length);
        
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length; col++){
                String tileHolderType = board[row][col];
                DCTileHolder th = new DCTileHolder();
                th.setTileHolderID("Board-"+Integer.toString(row)+"-"+Integer.toString(col));
                th.setPlacedTiles(placedTileHolders);
                switch (tileHolderType){
                    case "dls":
                        th.setBackGround("#22ddff");
                        th.setLetterMultiplier(2);
                        break;
                    case "dws":
                        th.setBackGround("#AA55AA");
                        th.setWordMultiplier(2);
                        break;
                    case "tls":
                        th.setBackGround("#0000FF");
                        th.setLetterMultiplier(3);
                        break;
                    case "tws":
                        th.setBackGround("#FF0000");
                        th.setWordMultiplier(3);
                        break;
                    default:
                        th.setBackGround("#DDDDDD");
                        break;
                }
                boardPane.getChildren().add(th);
                allTileHolders.put(th.getTileHolderID(), th);
                th.getStyleClass().add("BoardTileHolder");
                th.relocate (10+col*25+(col*4),10+row*25+(4*row));
            }
        }
        
    }
    public Node getNode(){
        return boardPane;
    }
    
    public DCTileHolder getTileHolderByID(String tileHolderID){
        return allTileHolders.get(tileHolderID);
    }
    
    public Map<String,DCTileHolder> getAllTileHolderMap(){
        return allTileHolders;
    }
    
    public void inactivatePlayedTileHolders(){
//        ObservableList<Node> observableList = boardPane.getChildren();
//        int disabledTileHolders = 0;
//        for(Node nTH: observableList){
//            DCTileHolder dcTileHolder = (DCTileHolder) nTH;
//            if (!dcTileHolder.getChildren().isEmpty() && dcTileHolder.getIsDroppable()){
//                // this tileholder has a played tile. should disable drag feature for this TileHolder
//                dcTileHolder.disableDrop();
//                System.out.println("disabling TileHolder: "+dcTileHolder.getTileHolderID());
//                disabledTileHolders++;
//            }
//        }
//        System.out.println("disabled "+disabledTileHolders+" TileHolders.");
        
        // comparing placedTileHolders
        placedTileHolders.forEach((String st,DCTileHolder v)->{
            v.disableDrop();
//            System.out.println("disabled tileholder: "+v.getTileHolderID()+" with key: "+st);
        });
        placedTileHolders.clear();
    }
    
    public Map<String,DCTileHolder> getPlayedHandMap(){
        return placedTileHolders;
    }
    
    public List<String> getPlayedHandList(){
        Set<String> setTileHolderID = placedTileHolders.keySet();
        List<String> listTileHolderID = new ArrayList<String>();
        for(String thID: setTileHolderID){
            listTileHolderID.add(thID);
        }
        return listTileHolderID;
    }
    public void updateBoard(String strMap){
        Map<String,DCTile> map = new HashMap<>();
        String modifiedStrMap = strMap.replaceAll("\\{", "").replaceAll("\\}", "");
        String[] strMapEntries = modifiedStrMap.split(", ");
        for (String mapEntry: strMapEntries){
            String[] mapParts = mapEntry.split("=");
            if(mapParts.length == 2){
                DCTile tile = new DCTile(mapParts[1]);
                if (tile != null){
                    map.put(mapParts[0], tile);
                } else {
                    System.out.println("Source: DCScrabbleBoard.updateBoard(String) -> error recreating tile");
                }
            } else {
                System.out.println("Source: DCScrabbleBoard.updateBoard(String) -> cannot create mapping entry.");
            }
        }
        ArrayList<Map<String,DCTile>> listMapTile = new ArrayList<>();
        listMapTile.add(map);
        restorePrevPlayedTiles(listMapTile);
    }
    public void updateJoinBoard(String strListMapTiles){
        List<Map<String,DCTile>> listMap = toListMap(strListMapTiles);
        restorePrevPlayedTiles(listMap);
    }
    
    private List<Map<String,DCTile>> toListMap(String strListMapTiles){
        List<Map<String,DCTile>> listMap = new ArrayList<>();
        System.out.println(strListMapTiles);
        String strModifiedStr = strListMapTiles.replaceAll("\\}, ", ";").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\[", "").replaceAll("\\]","");
//        System.out.println("Source: DCSrabbleBoard.toListMap(String) -> strModifiedStr: "+strModifiedStr);
        if (!strModifiedStr.isEmpty()) {
        String[] maps = strModifiedStr.split(";");
            for (String map: maps){
                String[] mapEntries = map.split(", ");
                Map<String,DCTile> mapTile = new HashMap<>();
                for(String mapEntry: mapEntries){
//                    System.out.println("Source: DCScrabbleBoard.toListMap(String) -> mapEntry: "+mapEntry);
                    String[] mapParts = mapEntry.split("=");
                    DCTile tile = new DCTile(mapParts[1]);
                    if(tile != null){
                        mapTile.put(mapParts[0], tile);
                    }
                }
                listMap.add(mapTile);
            }
        } else if(strModifiedStr.isEmpty()){
            System.out.println("Source: DCScrabbleBoard.toListMap(String)-> No previously played tiles to update.");            
        } else {
            System.out.println("Source: DCScrabbleBoard.toListMap(String)-> error creating List<Map<String,DCTile>>");
        }
        System.out.println(listMap.toString());
        return listMap;
    }
    public void restorePrevPlayedTiles(List<Map<String,DCTile>> listMapPlayedTiles){
        System.out.println("restoring prevPlayedTiles...");
        listMapPlayedTiles.stream().forEach((map) -> {
            Set keys = map.keySet();
            for(Object key: keys){
                String tileHolderID = (String) key;
                DCTileHolder tileHolder = allTileHolders.get(key);
                if (tileHolder != null && tileHolder.getChildren().size() == 0){
                    DCTile tile = map.get(tileHolderID);
                    tileHolder.getChildren().add(tile);
                    tile.relocate(1.75,1.75);
                    tileHolder.disableDrop();
                }
            }
        });
        System.out.println("finished restoring prevPlayedTiles.");
    }
    
    public void addPrevPlayedTilesToBoard(List<Map<String,String>> listMapPlayedTiles){
        /**
         * this should be adding tiles to the board to update tiles added by other players for this game.
         * listMapPlayedTiles is from server - should reflect other players moves.
         */
        for (Map<String,String> mapPlayedTiles: listMapPlayedTiles){
            for(Map.Entry<String,String> entry: mapPlayedTiles.entrySet()){
                DCTileHolder tileHolder = allTileHolders.get(entry.getKey());
                if (tileHolder.getChildren().isEmpty()){
                    DCTile tile = new DCTile(entry.getValue());
                    tileHolder.getChildren().add(tile);
                    tile.relocate(1.75, 1.75);
                    tileHolder.disableDrop();
                } else {
                    System.out.println("Sourec: DCScrabbleBoard.addPrevPlayedTilesToBoard(Lis<Map<>>) -> tileHolder is already occupied. tileHolderID: "+entry.getKey());
                }
            }
        }
    }
    
    public void resetScrabbleBoard(){
        System.out.println("resetting board tiles");
        for (Map.Entry<String,DCTileHolder> entry : allTileHolders.entrySet()){
            DCTileHolder tileHolder = entry.getValue();
            ObservableList<Node> nodes = tileHolder.getChildren();
            if (nodes.size() > 0)
                entry.getValue().getChildren().removeAll(nodes);
            tileHolder.setIsDroppable(true);
        }
        System.out.println("done restting board.\tclearing placedTileHolders list");
        placedTileHolders.clear();
        System.out.println("done clearing list");
    }
    
    private boolean boolDragDrop = true;

    /**
     * Get the value of boolDragDrop
     *
     * @return the value of boolDragDrop
     */
    public boolean isBoolDragDrop() {
        return boolDragDrop;
    }

    /**
     * Set the value of boolDragDrop
     *
     * @param boolDragDrop new value of boolDragDrop
     */
    public void setBoolDragDrop(boolean boolDragDrop) {
        this.boolDragDrop = boolDragDrop;
        enableDragDrop();
    }

    public void toggleAllDragDrop(){
        boolDragDrop = !boolDragDrop;
        enableDragDrop();
    }
    
    private void enableDragDrop(){
        for (Map.Entry<String,DCTileHolder> entry: allTileHolders.entrySet()){
            DCTileHolder th = entry.getValue();
            if (th.getChildren().size() == 0){
                // no children, so need to toggle the tileholder
                th.setIsDroppable(boolDragDrop);
                
            }
        }
    }
}
