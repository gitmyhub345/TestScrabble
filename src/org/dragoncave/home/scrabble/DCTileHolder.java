/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble;

import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.Map;
import java.util.Set;
/**
 *
 * @author William J
 */
public class DCTileHolder extends Pane{
    private boolean debug;
    private final double height = 25;
    private final double width = 25;
    private boolean isDroppable;
    private String color;
    private Pane userPane;
    private UserPane userHandPane;
    private Pane boardPane;
    private Map<String,DCTileHolder> placedTileHolders;
    private int letterMultiplier;
    private int wordMultiplier;
    
    public void setDebug(boolean debug){
        this.debug = debug;
    }
    public DCTileHolder(){
        super.setMinHeight(height);
        super.setMinWidth(width);
        super.setMaxHeight(height);
        super.setMaxWidth(width);
        isDroppable = true;
        letterMultiplier = 1;
        wordMultiplier = 1;
        enableDragDrop();
    }

    protected void setLetterMultiplier(int letterMultiplier){
        this.letterMultiplier = letterMultiplier;
    }

    public int getLetterMultiplier(){
        return letterMultiplier;
    }
    protected void setWordMultiplier(int wordMultiplier){
        this.wordMultiplier = wordMultiplier;
    }
    public int getWordMultiplier(){
        return wordMultiplier;
    }
    public void setPlacedTiles(Map<String,DCTileHolder> placedTileHolders){
        this.placedTileHolders = placedTileHolders;
    }
    public void setBackGround(String color){
        this.color = color;
        super.setStyle(String.format("-fx-background-color: %s;",color));
    }

    public void setUserPane(Pane userPane){
        this.userPane = userPane;
    }
    public void setUserHandPane(UserPane userHandPane){
        this.userHandPane = userHandPane;
    }
    public void setBoardPane(Pane boardPane){
        this.boardPane = boardPane;
    }
    public void setTileHolderID(String id){
        super.setId(this.getClass().getSimpleName()+id);
    }
    public String getTileHolderID(){
        return super.getId();
    }
    private void enableDragDrop(){
        if (isDroppable && this.getChildren().size() < 1){
            this.setOnDragDropped((DragEvent event)->{
                Dragboard db = event.getDragboard();
                boolean success = false;
                String source = event.getSource().toString();
                String[] target = event.getTarget().toString().split("-");
                if(debug){
                    System.out.println("\t------------------Drop Event-----------------");
                    System.out.println("\tSource: DCTileHolder.enableDragDrop() -> source: "+source);
                    System.out.println("\tSource: DCTileHolder.enableDragDrop() -> target: "+target[0]);
                    System.out.println("\tSource: DCTileHolder.enableDragDrop() -> TileHolder Index 1: "+target[1]);
                    if (target.length>2)
                        System.out.println("\tSource: DCTileHolder.enableDragDrop() -> TileHolder Index 2: "+target[2]);
                    System.out.println("\tSource: DCTileHolder.enableDragDrop() -> number of childer: "+this.getChildren().size());
                    System.out.println("\t---------------------------------------------");
                }
                if (db.hasString() && this.getChildren().size() < 1){
                   String nodeId = "#"+db.getString();
                    DCTile t = null;
//                    DCTileHolder userHandTileHolder = null; // to store the tile which is the parent of source
                    DCTileHolder sourceTileHolder = lookupSourceNode(nodeId);
                    
                    // ToDo: add label to hand
                    
                    if(sourceTileHolder != null){
                        t = getSourceTile(sourceTileHolder,nodeId);
                        if (t!=null){
//                            sourceTileHolder.getChildren().remove(t);
//                            sourceTileHolder.toggleDrop();
                            this.getChildren().add(t);
                            if (debug){
                                System.out.println("\tSource: DCTileHolder.enableDragDrop() -> Tile played: TileID:\t"+t.getTileID()+"\t:"+t.getLetter()+"\t"+t.getText());
                                System.out.println("\tSource: DCTileHolder.enableDragDrop() -> source tile id: "+sourceTileHolder.getTileHolderID().substring(0,13));
                                System.out.println("\tSource: DCTileHolder.enableDragDrop() -> target tile id: "+this.getTileHolderID().substring(0, 13));
                            }
                            if ( this.getTileHolderID().substring(0, 13).equalsIgnoreCase("DCTileHolderU") &&
                                    sourceTileHolder.getTileHolderID().substring(0,13).equalsIgnoreCase("DCTileHolderU")){
                                // do nothing in this case
                            } else {
                                placedTileHolders.remove(sourceTileHolder.getTileHolderID());
                                if (this.getTileHolderID().substring(0, 13).equalsIgnoreCase("DCTileHolderB")){
                                    placedTileHolders.put(this.getTileHolderID(),this);
                                }
                            }
                            t.relocate(1.75, 1.75);
                            success = true;
                        }
                        this.toggleDrop();
                        sourceTileHolder.toggleDrop();
                    }
                    // sourceTileHolder not found in any of the tileHolders
                    
                }
                if (!success){
                    System.out.println("attempting to drop into occupied space");
                }
                event.setDropCompleted(success);
                event.consume();
                if(debug){
                    System.out.println("\t------------- End Drop Event ----------------");
                    System.out.println("\t---------------------------------------------");
                }
            });
            this.setOnDragOver((DragEvent event)->{
                if (event.getGestureSource() != this && event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.MOVE);
                    super.setStyle("-fx-background-color: #EEEEEE;");
                }
                event.consume();
            });
            this.setOnDragExited((DragEvent event)->{
                super.setStyle("-fx-background-color: "+this.color);
            });
        } else {
            this.setOnDragDropped((DragEvent event)->{});
            this.setOnDragOver((DragEvent event)->{});
            this.setOnDragExited((DragEvent event)->{});
        }
    }
    
    
    public void disableDrop(){
        this.isDroppable = false;
        this.enableDragDrop();
        ObservableList<Node> observableList = this.getChildren();
        for (Node n: observableList){
            DCTile t = (DCTile)n;
            t.setDraggable(false);
            if (debug){
                System.out.println("source: DCTileHolder.disableDrop() -> disableDrop ");
            }
        }
    }
    
    private void toggleDrop(){
        isDroppable = !isDroppable;
        super.setStyle("-fx-background-color: "+this.color);
        this.enableDragDrop();
    }
    
    public boolean getIsDroppable(){
        return this.isDroppable;
    }
    
    private DCTileHolder lookupSourceNode(String nodeId){
        String tileNodeID = nodeId;
        boolean sourceNodeFound = false;
        DCTileHolder sourceTile = null;
        ObservableList<Node> sourceNodeList;
        Node sNode = null;
        // which ever pane this tileHolder belongs to, get parent, and search all children.
        sourceNodeList = this.getParent().getChildrenUnmodifiable();
        sNode = searchNodeList(sourceNodeList,tileNodeID);
        if (sNode != null){
            sourceTile = (DCTileHolder)sNode;
            sourceNodeFound = true;
            if (debug){
                System.out.println("Source: DCTileHolder.lookupSourceNode(String) -> source and targert tileholders are in the same component.");
            }
        }
        // if source is not in this same pane
        if (!sourceNodeFound && boardPane!= null){
            // target tileHolder is userPane 
            sourceNodeList = boardPane.getChildren();
            sNode = searchNodeList(sourceNodeList,tileNodeID);
            if (sNode != null){
                sourceTile = (DCTileHolder)sNode;
                sourceNodeFound = true;
                if (debug){
                    System.out.println("Source: DCTileHolder.lookupSourceNode(String) -> source and targert tileholders are in different components. Target tileholder is in the UserPane.");
                }
            }
        } else if (!sourceNodeFound && userHandPane!= null){
            // target tileHolder is boardPane
            sourceNodeList = userHandPane.getChildren();
            sNode = searchNodeList(sourceNodeList,tileNodeID);
            if (sNode != null){
                sourceTile = (DCTileHolder)sNode;
                sourceNodeFound = true;
                if (debug){
                    System.out.println("Source: DCTileHolder.lookupSourceNode(String) -> source and targert tileholders are in different components. Target tileholder is in the Scrabble Board.");
                }
            }
        }
        
        return sourceTile;
    }
    
    private DCTile getSourceTile(DCTileHolder tileHolder,String strTileID){
        DCTile tile = (DCTile)tileHolder.lookup(strTileID);
        return tile;
    }
    
    private Node searchNodeList(ObservableList<Node> sourceNodeList,String tileNodeID){
        Node sourceNode = null;
        for (Node sNode: sourceNodeList){
            if (sNode.lookup(tileNodeID)!=null){
                sourceNode = sNode;
                break;
            }
        }
        return sourceNode;
    }
    public void setIsDroppable(boolean droppable){
        this.isDroppable=droppable;
        this.enableDragDrop();
    }
    @Override
    public String toString(){
        return this.getTileHolderID();
    }
}
