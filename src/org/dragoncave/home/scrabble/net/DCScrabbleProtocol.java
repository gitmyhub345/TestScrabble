/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble.net;

import org.dragoncave.home.scrabble.*;
/**
 *
 * @author Rider1
 */
public class DCScrabbleProtocol {
    
    private static final int WAITING = 1;
    private static final int READYFORINPUT = 2;
    private static final int RESPONSESENT = 3;
    private static final int AWAITINGACKNOWLEDGEMENT = 4;
    
    private int state = WAITING;
    
    private DCServerTileBag tileBag;
    
    public void setTileBuilder(DCServerTileBag tileBag){
        this.tileBag = tileBag;
    }
    
    /**
     * client can request for a new tile multiple times - 
     * client can sent data packets as objects?/string representations
     * if client sends data packets, will have to notify send data to other clients for updates
     */
    
    /**
     * client sends request for new tile - 
     * 
     */
    public String processInput(String input){
        String output = null;
        
        /**
         * client request for new tile -
         */
        
        /**
         * check to see what is sent to server - if it is only a list of tiles, then the client wants a new set of tiles in return.
         * otherwise, should be a map of tiles and tileholders, in which case, server will return a list of new tiles with the same 
         * number of tiles as played, then broadcast to other players of tile placed on the boards so they can update their view.
         */
        String[] requestType = input.split(";");
        switch(requestType[0]){
            case "RESETHAND":
                tileBag.returnTilesToPool(requestType[1]);
                output = DCServerRequestType.RESETHAND+";"+tileBag.populateHane();
                break;
            case "PLAYEDTILES":
                int n = tileBag.submitPlayedTiles(requestType[1]);
                System.out.println("PlayedTiles: "+requestType[1]);
                output = DCServerRequestType.REPLAYEDTILES+";"+tileBag.repopulateHand(n);
                break;
            case "JOIN":
                tileBag.addPlayer();
                output = DCServerRequestType.JOIN+";"+tileBag.populateHane();
                break;
            case "GETTILE":
                output = DCServerRequestType.GETTILE+";"+tileBag.getTile().toString();
                break;
            case "NEWGAME":
                tileBag.resetTileBag();
                output = DCServerRequestType.NEWGAME+";"+tileBag.populateHane();
                break;
            default:
                break;
        }

        return output;
    }
    
}
