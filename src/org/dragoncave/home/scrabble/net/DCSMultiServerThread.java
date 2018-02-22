/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble.net;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rider1
 */
public class DCSMultiServerThread extends Thread{
    
    private Socket socket;
    private DCServerTileBag tileBag;
    private ArrayList<DCSMultiServerThread> gamePlayers = new ArrayList<>();
    private Map<String,ArrayList> gameMap = new HashMap<>();

        
    public DCSMultiServerThread(Socket socket, DCServerTileBag tileBag,Map<String,ArrayList> gameMap, ArrayList<DCSMultiServerThread> gamePlayers){
        super("DCSMultiServerThread");
        this.socket = socket;
        this.tileBag = tileBag;
        this.gameMap = gameMap;
        this.gamePlayers = gamePlayers;
        
//        gamePlayers.add(this.socket);
        gameMap.put(tileBag.getGameName(), gamePlayers);
    }

    /**
     * main thread function which listens to requests and also processes the return message
     */
    @Override
    public void run(){
        try(
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ){
            String inputLine, outputLine="";
            // initiate protocol
            DCScrabbleProtocol dcsp = new DCScrabbleProtocol();
            dcsp.setTileBuilder(tileBag);
            System.out.println("Multiclient connection established");
            out.println("hello");
            while ((inputLine = in.readLine()) != null){
                outputLine = dcsp.processInput(inputLine);
                String[] inputParts = inputLine.split(";");
                switch (inputParts[0]){
                    case "NEWGAME":
                        out.println(outputLine+";"+tileBag.getPlayerTurn());
                        break;
                    case "JOIN":
                        // return list of new tiles for hand
                        out.println(outputLine+";"+(gamePlayers.indexOf(this)+1));  
                        // retrieve list of previously played tiles to update board
                        outputLine = DCServerRequestType.INITBOARD+";"+tileBag.getPrevPlayedTiles()+";"+tileBag.getPlayerTurn();
                        out.println(outputLine);
                        // broadcast message - DCServerRequestType.MESSAGE+";New player joined game;"+tileBag.getPlayerTurn()
                        broadcast(DCServerRequestType.MESSAGE+";New player joined game;"+tileBag.getPlayerTurn());
                        break;
                    case "PLAYEDTILES": // case where player submitted played tiles
                        // return a list of replacement tiles for played tiles
                        out.println(outputLine+";"+tileBag.getPlayerTurn());
                        // broadcast message - DCServerRequestType.UPDATEBOARD+";"+inputParts[1]+";"+tileBag.getPlayerTurn()
                        broadcast(DCServerRequestType.UPDATEBOARD+";"+inputParts[1]+";"+tileBag.getPlayerTurn());
                        break;
                    case "RESETHAND":
                        // return a list of new tiles for player
                        out.println(outputLine+";"+tileBag.getPlayerTurn());
                        // broadcast message - DCServerRequestType.MESSAGE+";Player "+(gamePlayers.indexOf(this)+1)+" replaced his/her tiles.;"+tileBag.getPlayerTurn()
                        broadcast(DCServerRequestType.MESSAGE+";Player "+(gamePlayers.indexOf(this)+1)+" replaced his/her tiles.;"+tileBag.getPlayerTurn());
                        break;
                    default:
                        break;
                }
                if (inputLine.equals("Bye")){
                    break;
                }
            }
            /**
             * delete this socket form array, but cannot delete while looping through the list of threads
             */
            int indexForRemoval = -1;
            for(DCSMultiServerThread s: gamePlayers){
                if (s == this){
                    indexForRemoval = gamePlayers.indexOf(s);
                    tileBag.removePlayer();
                    socket.close();
                }
            }
            /**
             * delete thread from array
             */
            if (indexForRemoval >= 0){
                gamePlayers.remove(indexForRemoval);
                System.out.println("deleted socket: "+indexForRemoval);
                reshufflePlayerOrder(indexForRemoval);
            }
//            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * This socket is already closed, so cannot use broadcast function.
     */
    private void reshufflePlayerOrder(int playerNumber){
        System.out.println("attempting to broadcast new player orders...");
        for (DCSMultiServerThread t: gamePlayers){
            String msg = DCServerRequestType.NEWORDER+";Player "+(playerNumber+1)+" has left the game. You are now player "+(gamePlayers.indexOf(t)+1)+";"+(gamePlayers.indexOf(t)+1);
            t.sendMessage(msg);
            msg = DCServerRequestType.MESSAGE+";Player "+(playerNumber+1)+" has left the game. You are now player "+(gamePlayers.indexOf(t)+1)+";"+tileBag.getPlayerTurn();
            t.sendMessage(msg);
        }
        System.out.println("finished broadcasting.");
    }
    /**
     * Primary function for broadcasting to other players
     * @param msg 
     */
    private void broadcast(String msg){
        System.out.println("Source: DCSMultiServerThread.broadcast(String) -> broadcasting...");
        for (DCSMultiServerThread s:gamePlayers){
            int nextPlayer = tileBag.getPlayerTurn();
            if (s != this){
                System.out.println("attempting to broadcast to other clients");
                s.sendMessage(msg);
            }
        }
        System.out.println("Source: DCSMultiServerThread.broadcast(String) -> Finished broadcasting...");
    }
    /**
     * Actual function which sends the messages out to players
     * @param msg 
     */
    public void sendMessage(String msg){
        try{
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            if (!socket.isClosed()){
                System.out.println("socket is connected:\tmessage:\t"+msg);
                out.println(msg);
                out.flush();
            }
            else{
                System.out.println("socket is closed");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    

}
