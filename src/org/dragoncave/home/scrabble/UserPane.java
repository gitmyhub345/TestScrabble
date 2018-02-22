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
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.geometry.Insets;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.util.Map;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
/**
 *
 * @author William J
 */
public class UserPane extends FlowPane{
//    private FlowPane userPane;
    private Pane boardPane;
    private double width;
    private double height;
    private String backgroundColor;
    
    private Map<String,DCTileHolder> mapUserTileHolder;
    
    public void setBackgroundColor(String color){
        this.backgroundColor = color;
        super.setStyle("-fx-background-color: "+this.backgroundColor);
    }
    public void setWidth(double width){
        this.width = width;
        super.setPrefWidth(this.width);
        super.setMinWidth(this.width);
    }
    public UserPane(){
        
//        userPane = new FlowPane();
        super.setPadding(new Insets(10.0));

//        super.setBackground( new Background(new BackgroundFill(Paint.valueOf("DARKCYAN") ,new CornerRadii(1),new Insets(10.0))));
//        super.setStyle("-fx-background-color: ff0000; -fx-opacity: 1.0;");
//        this.setStyle("-fx-background-color: ff0000");
        super.setMinWidth(46.0);
        super.setMaxWidth(46.0);
        super.setMinHeight(390.0);
        super.setVgap(5.0);
        super.setStyle("-fx-background-color : #00ff00;");
        mapUserTileHolder = new HashMap<String,DCTileHolder>();
        createHandHolder();
        this.setVisible(true);
        System.out.println("UserPane() created");
    }
    
    public void setBoardPane(Pane board,Map<String,DCTileHolder> mTH){
        boardPane = board;
        ObservableList<Node> l = this.getChildren();
        for (Node n : l){
            DCTileHolder th = (DCTileHolder)n;
            th.setBoardPane(boardPane);
            th.setPlacedTiles(mTH);
        }
//        System.out.println("setBoardPane()");
    }
    
    public DCTileHolder getTileHolder(int indexNumber){
        //return mapUserTileHolder.get(index);
        System.out.println("getting tileHolder number"+indexNumber);
        return (DCTileHolder) this.lookup("#DCTileHolderUser-"+Integer.toString(indexNumber));
    }
    
    private void createHandHolder(){
        for (int index = 0; index < 7; index++){
            DCTileHolder th = new DCTileHolder();
            th.getStyleClass().add("UserTileHolder");
            th.setBackGround("#f0f8ff");
            th.setTileHolderID("User-"+Integer.toString(index));
            mapUserTileHolder.put(th.getTileHolderID(),th);
            this.getChildren().add(th);
        }
    }
    
    public void populateHand(DCTileBuilder tb){
        List<DCTile> t = tb.getHand();
//        int numTileHolders = mapUserTileHolder.size();
        System.out.println("hand size: "+t.size());
        for (int index = 0; index < 7; index++){
            DCTileHolder th = (DCTileHolder) this.lookup("#DCTileHolderUser-"+Integer.toString(index));
            th.getChildren().add(t.get(index));
            th.setIsDroppable(false);            
            t.get(index).relocate(1.75, 1.75);
//            System.out.println("tile parent: "+t.get(index).getParent().toString() +"  :\tid:\t"+th.getTileHolderID()+"\n\ttile: "+t.get(index).getTileID());
        }
//        System.out.println("source: userPane - populateHand()\n\tchildrenlist: "+this.getChildren().size());
    }
    
    public void populateHand(String strTileList){
        /**
         * this is populating the initial hand from server. must contain 7 tiles, but repopulateHand should be able to handle 
         * adding of tiles to avoid duplicating code.
         */
        int tempNumElements = strTileList.split(",").length;
        if (tempNumElements != 7){
            System.out.println("Source: UserPane.populateHand(String) -> tileList contains the incorrect number of tiles: "+tempNumElements);
            repopulateHandFromServer(strTileList);
        } else {
            repopulateHandFromServer(strTileList);
        }
    }
    
    public void repopulateHand(DCTileBuilder tb, int numTilesToReplace){
        int remainingTiles = numTilesToReplace;
//        for (int index = 0; index < Math.min(7, remainingTiles); index++){
//        Set keys = mapUserTileHolder.keySet();
        for (Map.Entry<String,DCTileHolder> entry: mapUserTileHolder.entrySet()){
            DCTileHolder th = entry.getValue();
//            DCTileHolder th = (DCTileHolder) this.lookup("#DCTileHolderUser-"+Integer.toString(index));
            if (th.getChildren().size() < 1 && tb.getTilesInPool() > 0 && remainingTiles > 0){
                DCTile t = tb.getRandomTile();
                if (t != null){
                    th.getChildren().add(t);
                    th.setIsDroppable(false);
                    t.relocate(1.75, 1.75);
                    remainingTiles--;
                }
            } else {
                th.setIsDroppable(true);
            }
//            if(remainingTiles == 0)
//                break;
        }
    }
    public void repopulateHand(DCTileBuilder tb){
        int remainingTiles = tb.getTilesInPool();
//        for (int index = 0; index < Math.min(7, remainingTiles); index++){
//        Set keys = mapUserTileHolder.keySet();
        for (Map.Entry<String,DCTileHolder> entry: mapUserTileHolder.entrySet()){
            DCTileHolder th = entry.getValue();
//            DCTileHolder th = (DCTileHolder) this.lookup("#DCTileHolderUser-"+Integer.toString(index));
            if (th.getChildren().size() < 1 && tb.getTilesInPool() > 0){
                DCTile t = tb.getRandomTile();
                if (t != null){
                    th.getChildren().add(t);
                    th.setIsDroppable(false);
                    t.relocate(1.75, 1.75);
                }
            }
        }
    }
    
    public void repopulateHandFromServer(String strTileList){
        /**
         * repopulate hand with the correct number of tiles. tileList should contain the correct number of tiles from server.
         * tileList is string used to create the appropriate tiles.
         */
        /** 
         * calculate the correct number of empty tileHolders.
         */
        List<DCTile> tileList = toList(strTileList);
        int emptyTileHolders = 0;
        for (Map.Entry<String,DCTileHolder> entry: mapUserTileHolder.entrySet()){
            if (entry.getValue().getChildren().isEmpty())
                emptyTileHolders++;
        }
        if (tileList.size() != emptyTileHolders){
            System.out.println("Source: UserPane.repopulateHand(List tileList) -> incorrect number of tiles to repopulate.\n"+
                    "tileList size: "+tileList.size()+"\tempty tileHolders: "+emptyTileHolders);
        }
        if (tileList.size() > 0) {
            int tileListIndex = 0;
            for (Map.Entry<String,DCTileHolder> entry: mapUserTileHolder.entrySet()){
                DCTileHolder tileHolder = entry.getValue();
                ObservableList<Node> userTiles = tileHolder.getChildren();
                if (userTiles.isEmpty()){
                    DCTile tile = tileList.get(tileListIndex);
                    tileHolder.getChildren().add(tile);
                    tileHolder.setIsDroppable(false);
                    tile.relocate(1.75, 1.75);
                    tileListIndex++;
                }
                if(tileListIndex == tileList.size())
                    break;
            }
            if (emptyTileHolders != tileListIndex)
                System.out.println("Source: UserPane.repopulateHand(List tileList) -> please double check to make sure the all tiles are add properly.");
        }
        
    }
    
    private void enableDragDrop(){
        this.setOnDragDropped((DragEvent event)->{
            Dragboard db = event.getDragboard();
            boolean success = false;
            
            if(db.hasString()){
                String nodeID = "#"+db.getString();
                
                DCTile tile = (DCTile)boardPane.lookup(nodeID);
                
                if(tile != null ){
                    boardPane.getChildren().remove(tile);
                    this.getChildren().add(tile);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
        this.setOnDragOver((DragEvent event)->{
            if(event.getGestureSource()!= this && event.getDragboard().hasString()){
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        
    }
    
    public void returnTilesToPool(DCTileBuilder tb){
        /** should not be able to return a partial hand **/
        
        System.out.println("Source: UserPane.returnTilesToPool(DCTileBuilder) -> returning tiles to pool...");
        ObservableList<Node> observableList = this.getChildren();
        List<DCTile> returnTiles = new ArrayList<DCTile>();
        boolean result = true;
        for (Node node: observableList){
            DCTileHolder tileHolder = (DCTileHolder)node;
            ObservableList<Node> childList = tileHolder.getChildren();
            for(Node n: childList){
                DCTile t = (DCTile)n;
                returnTiles.add(t);
            }
            if (childList.size()> 1) {
                System.out.println("Source: UserPane.returnTilesToPool(DCTileBuilder) -> too many children in tileHolder");
                result = false;
                break;
            }
            tileHolder.getChildren().removeAll(childList);

        }
//        System.out.println("number of tiles to return: "+returnTiles.size());
        if (result){
            result = tb.returnTilesInHand(returnTiles);
        }
//        System.out.println("UserPane: returnTilesToPool: number of tile in pool: "+ tb.getTilesInPool());
        if (result)
            repopulateHand(tb,returnTiles.size());
        System.out.println("Source: UserPane.returnTilesToPool(DCTileBuilder) -> Finished getting new tiles");
    }
    
    public String returnHandToServer(){
        /**
         * returning tiles to server - but server only understands strings.
         */
        List<String> tileList = new ArrayList<>();
        for (Map.Entry<String,DCTileHolder> entry:mapUserTileHolder.entrySet()){
            ObservableList<Node> tiles = entry.getValue().getChildren();
            if (tiles.size() == 1){
                for(Node n: tiles){
                    DCTile tile = (DCTile)n;
                    if (tile != null){
                        tileList.add(tile.toString());
//                        entry.getValue().getChildren().remove(tile);
                    } else {
                        System.out.println("Source:  UserPane.returnHandToServer() -> unable to capture tile to return.");
                        break;
                    }
                }
                entry.getValue().getChildren().removeAll(tiles);
            } else if (entry.getValue().getChildren().size() > 1){
                System.out.println("Source: UserPane.returnHandToServer() -> too many children. removing all children.");
                entry.getValue().getChildren().removeAll(tiles);
                break;
            }
        }
        System.out.println("Source: UserPane.returnHandToServer() ->returning hand with "+tileList.size()+" tiles:\n\t"+tileList.toString());
        return tileList.toString();
    }
    
    public void saveHand(){
        try(FileWriter out = new FileWriter("ScrabbleUserPaneTiles.dat")){
            Map<String,DCTile> saveMap = new HashMap<>();
            ObservableList<Node> observableList = this.getChildren();
            for (Node n: observableList) {
                DCTileHolder tileHolder = (DCTileHolder)n;
                String key = tileHolder.getTileHolderID();
                ObservableList<Node> tiles = tileHolder.getChildren();
                for (Node t: tiles){
                    DCTile tile = (DCTile)t;
//                    out.write(key+":"+tile.getTileID()+":"+tile.getText()+";");
                    saveMap.put(key, tile);
                }
//                out.write("\n");
            }
            out.write(saveMap.toString());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public void loadHand(){
        resetHand();
        try (BufferedReader in = new BufferedReader(new FileReader("ScrabbleUserPaneTiles.dat"))){ 
            String line;
            while ((line = in.readLine()) != null && (line.length()>2)){
//                Map<String,DCTile> m = new HashMap<>();
                String subline = line.replaceAll("\\{", "").replaceAll("\\}", "");
                String[] mapEntries = subline.split(", ");
                System.out.println("number of mapEntries: "+mapEntries.length);
                for (String entryMap:mapEntries){
                    String[] tileMap = entryMap.split("=");
                    System.out.println("tileHolderID: "+tileMap[0]+"\tTile String: "+tileMap[1]);
                    DCTile tile = new DCTile(tileMap[1]);
                    DCTileHolder tileHolder = (DCTileHolder)this.lookup("#"+tileMap[0]);
                    if(tileHolder != null && tile != null){
                        System.out.println("adding tile to user hand.");
                        tileHolder.getChildren().add(tile);
                        tile.relocate(1.75, 1.75);
                    }
                }

//                String[] word = line.split(";");
//                for (String c: word){
//                    String[] th = c.split(":"); 
//                    int index = Integer.parseInt(th[1].substring(6));
//                    DCTile tile = new DCTile(th[2],index);
////                    m.put(th[0], tile);
//                    System.out.println("userPane loadHand: tileHolderID: "+th[0]);
//                    DCTileHolder tileHolder = (DCTileHolder) this.lookup("#"+th[0]);
//                    if (tileHolder != null){
//                        tileHolder.getChildren().add(tile);
//                        tile.relocate(1.75, 1.75);
//                    }
//                            
//                }
//                prevPlayTiles.add(m);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
    
    public void resetHand(){
        System.out.println("resetting hand...");
        for (Map.Entry<String,DCTileHolder> entry: mapUserTileHolder.entrySet()){
            DCTileHolder tileHolder = entry.getValue();
            ObservableList<Node> nodes = tileHolder.getChildren();
            if (nodes.size()>0){
                tileHolder.getChildren().removeAll(nodes);
            }
            tileHolder.setIsDroppable(false);
        }
        System.out.println("done resetting hand.");
    }
    
    private List<DCTile> toList(String strListTiles){
        List<DCTile> tileList = new ArrayList<>();
        String modifiedString = strListTiles.replaceAll("\\[","").replaceAll("\\]", "");
        String[] strTileArray = modifiedString.split(", ");

        for(String strTile: strTileArray){
            System.out.println("Source: UserPane.toList(String) -> DCServerTile: "+strTile);
            if (!strTile.isEmpty()){
            DCTile tile = new DCTile(strTile);
            boolean result = tileList.add(tile);
            if(!result)
                System.out.println("Source: UserPane.toList(String) -> cannot add tile to list.");
            }
        }
        return tileList;
        
    }
}
