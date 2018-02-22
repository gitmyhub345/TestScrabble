/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;

import org.dragoncave.home.scrabble.net.*;
/**
 *
 * @author William J
 */
public class DCSMenuBar extends HBox {
    private int width;
    private int height;
    private MenuItem[] menuMode;
    private MenuItem[] menuPlay;
    private Menu[] menu;
    
    private Label player;
    private int mode;
    private String playerName;
    
    private UserPane userPane;
    private DCTileBuilder tileBuilder;
    private DCScrabbleBoard scrabbleBoard;
    private ScrabblePlay scrabblePlay;
    private DCScoreBox scoreBox;
    private DCScrabbleController controller;

    private Object gamePointer;
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        player.setText(playerName);
    }
    public String getPlayerName(){
        return playerName;
    }
    public void setMode(int mode){
        this.mode = mode;
    }
    public void setController(DCScrabbleController controller) {
        this.controller = controller;
    }

    public DCScoreBox getScoreBox() {
        return scoreBox;
    }

    public void setScoreBox(DCScoreBox scoreBox) {
        this.scoreBox = scoreBox;
    }

    public UserPane getUserPane() {
        return userPane;
    }

    public void setUserPane(UserPane userPane) {
        this.userPane = userPane;
    }

    public DCTileBuilder getTileBuilder() {
        return tileBuilder;
    }

    public void setTileBuilder(DCTileBuilder tileBuilder) {
        this.tileBuilder = tileBuilder;
    }

    public DCScrabbleBoard getScrabbleBoard() {
        return scrabbleBoard;
    }

    public void setScrabbleBoard(DCScrabbleBoard scrabbleBoard) {
        this.scrabbleBoard = scrabbleBoard;
    }

    public ScrabblePlay getScrabblePlay() {
        return scrabblePlay;
    }

    public void setScrabblePlay(ScrabblePlay scrabblePlay) {
        this.scrabblePlay = scrabblePlay;
    }
    
    public void setMenuMode(int mode){
        this.mode = mode;
    }
    
    public DCSMenuBar(int mode){
        super();
//        super.setPadding(new Insets(10.0));
//        super.setSpacing(15.0);
        super.getStyleClass().add("command");
        if (mode == 1)
            playerName = "Player number 1";
        menu = new Menu[2];
        menu[0] = new Menu("Play");
        menu[1] = new Menu("Mode");
        menuPlay = new MenuItem[4];
        menuPlay[0] = new MenuItem("New");
        menuPlay[1] = new MenuItem("Load");
        menuPlay[2] = new MenuItem("Save");
        menuPlay[3] = new MenuItem("Join");
        menuMode = new MenuItem[2];
        menuMode[0] = new MenuItem("Single player");
        menuMode[1] = new MenuItem("Multi player");
        menu[0].getItems().addAll(menuPlay);
        menu[1].getItems().addAll(menuMode);
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("menubar");
        menuBar.getMenus().addAll(menu);
        initNewGame();
        if (mode == 1){
            initLoadGame();
            initSaveGame();
        } else if (mode == 2){
            initJoinGame();
        } else {
            initMultiPlayerGame();
            initSinglePlayerGame();
        }
        
        this.mode = mode;
        this.getChildren().add(menuBar);
        Label separator = new Label(" | ");
        player = new Label(playerName);
        this.getChildren().addAll(separator,player);
        inactivateMenuItem();
        
//        super.getStyleClass().add("command");
    }
    
    private void initNewGame(){
//        btnNewGame.setText("New");
//        btnNewGame.getStyleClass().add("button");
        menuPlay[0].setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                resetGame();
            }
        });
    }
    
    private void resetGame(){
        // reset scoreBox
        scoreBox.resetScore();
        // reset userPane
        userPane.resetHand();
        // reset board
        scrabbleBoard.resetScrabbleBoard();
        // reset scrabblePlay
        scrabblePlay.resetPlay();
        // reset tileBuilder
        if (mode == 1 && tileBuilder == null){
            System.out.println("tilebuilder error");
        } else{
        // repopulate userPane
            System.out.println("populating hand...");
            if (mode ==1 && tileBuilder != null){
                tileBuilder.resetTileBuilder();
                userPane.populateHand(tileBuilder);
            }
            else if (mode == 2){
                controller.dcsSendMessage(DCServerRequestType.NEWGAME+";");
            }
            System.out.println("done populating hand.");
        }

    }
    private void initJoinGame(){
        menuPlay[3].setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent e){
               controller.dcsSendMessage(DCServerRequestType.JOIN+";");
           }
        });
    }
    private void initLoadGame(){
        menuPlay[1].setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent e){
               System.out.println("trying to reset game...");
               resetGame();
               System.out.println("game reset.\nLoading...");
               tileBuilder.loadTileList();
               userPane.loadHand();
               scrabblePlay.loadPrevPlayTiles();
               scrabblePlay.loadPrevWords();
               scrabbleBoard.restorePrevPlayedTiles(scrabblePlay.getPrevPlayedTiles());
               System.out.println("finished loading.");

           }
        });
    }
    private void initSaveGame(){
        menuPlay[2].setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                tileBuilder.saveTileList();
                userPane.saveHand();
                scrabblePlay.saveGame();
            }
        });
    }
    private void initMultiPlayerGame(){
        menuMode[1].setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                mode = 2;
                inactivateMenuItem();
                System.out.println("multiplayer mode selected");
                switchGamePlay();
            }
        });
    }
    private void initSinglePlayerGame(){
        menuMode[0].setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                mode = 1;
                inactivateMenuItem();
                System.out.println("single player mode selected");
                switchGamePlay();
                
            }
        });
    }
    
    private void inactivateMenuItem(){
        if (mode == 1){
            menuPlay[3].setDisable(true);
            menuPlay[2].setDisable(false);
            menuPlay[1].setDisable(false);
        } else if (mode == 2) {
            menuPlay[3].setDisable(false);
            menuPlay[2].setDisable(true);
            menuPlay[1].setDisable(true);
            
        } else {
            // mode == 0, not selected
            menuPlay[3].setDisable(true);
            menuPlay[2].setDisable(true);
            menuPlay[1].setDisable(true);
 
        }
    }
    private void switchGamePlay(){
        if (mode == 1){
            initLoadGame();
            initSaveGame();
        } else if (mode == 2){
            initJoinGame();
        }    
    }
    
    public void setGamePointer(){
        

    };
}
