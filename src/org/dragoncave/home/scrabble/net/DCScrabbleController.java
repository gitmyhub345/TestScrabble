/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble.net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import java.lang.invoke.MethodHandles;
import java.net.SocketException;

import org.dragoncave.home.scrabble.UserPane;
import org.dragoncave.home.scrabble.DCSMenuBar;
import org.dragoncave.home.scrabble.DCScrabbleBoard;

import javafx.scene.control.Label;

/**
 *
 * @author Rider1
 */
public class DCScrabbleController {
    private UserPane userPane;
    private DCScrabbleBoard scrabbleBoard;
    private DCSMenuBar menuBar;
    private Label message;
    
    private final static Logger LOGGER
            = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

//    private ObservableList<String> rcvdMsgsData;
//    private ObservableList<String> sentMsgsData;
//    private ListView lastSelectedListView;

    private boolean connected;
    private volatile boolean isAutoConnected;

    private static final int DEFAULT_RETRY_INTERVAL = 2000; // in milliseconds

    public enum ConnectionDisplayState {

        DISCONNECTED, ATTEMPTING, CONNECTED, AUTOCONNECTED, AUTOATTEMPTING
    }

    private FxSocketClient socket;
    
    public void setMessageLabel(Label message){
        this.message = message;
    }

    public void setUserPane(UserPane userPane) {
        this.userPane = userPane;
    }

    public void setScrabbleBoard(DCScrabbleBoard scrabbleBoard) {
        this.scrabbleBoard = scrabbleBoard;
    }
    public void setMenuBar(DCSMenuBar menuBar){
        this.menuBar = menuBar;
    }

    /*
     * Synchronized method set up to wait until there is no socket connection.
     * When notifyDisconnected() is called, waiting will cease.
     */
    private synchronized void waitForDisconnect() {
        while (connected) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /*
     * Synchronized method responsible for notifying waitForDisconnect()
     * method that it's OK to stop waiting.
     */
    private synchronized void notifyDisconnected() {
        connected = false;
        notifyAll();
    }

    /*
     * Synchronized method to set isConnected boolean
     */
    private synchronized void setIsConnected(boolean connected) {
        this.connected = connected;
    }

    /*
     * Synchronized method to check for value of connected boolean
     */
    private synchronized boolean isConnected() {
        return (connected);
    }

    public void dcsConnect(){
        connect();
    }
    private void connect() {
        try (BufferedReader in = new BufferedReader(new FileReader("hostip.dat"))){ 
            String line;
            if ((line = in.readLine()) != null){
                socket = new FxSocketClient(new FxSocketListener(),
                        line,
                        2015,
                        Constants.instance().DEBUG_NONE);
                socket.connect();
//                socket.sendMessage("requesttile");
            }
            
        } catch (IOException e){
            System.out.println("Source: DCScrabbleController.connect() -> unable to find connection file.");
        }
    }

    private void autoConnect() {
        new Thread() {
            @Override
            public void run() {
                while (isAutoConnected) {
                    if (!isConnected()) {
                        socket = new FxSocketClient(new FxSocketListener(),
                                "localhost",
                                2015,
                                Constants.instance().DEBUG_NONE);
                        socket.connect();
                    }
                    waitForDisconnect();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }.start();
    }

    public void initialize(){
        setIsConnected(false);
        isAutoConnected = false;
        
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());
    }
            
    class ShutDownThread extends Thread {

        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");
                }
                socket.shutdown();
            }
        }
    }
    public void dcsSendMessage(String msg) {
        if (!msg.equals("")) {
            socket.sendMessage(msg);
            System.out.println("sent message: "+msg);
        }
    }
    
    private void isMyTurn(String turn){
        if(turn.equals(menuBar.getPlayerName())){
            // enable tileholders for dragdrop action
            scrabbleBoard.setBoolDragDrop(true);
        } else {
            // disable dragdrop action
            scrabbleBoard.setBoolDragDrop(false);
        }
    }
    
    private void dcsProcessMessage(String msg){
        String[] msgParts = msg.split(";");
        System.out.println("Source: DCScrabbleController.dcsProcessMessage(String) -> Message received: "+msg+"\n\tnumber of segments: "+msgParts.length);
        switch (msgParts[0]){
            case "NEWGAME":
                userPane.populateHand(msgParts[1]);
                menuBar.setPlayerName(msgParts[2]);
                message.setText("Player  "+msgParts[2]+"'s turn.");
                isMyTurn(msgParts[2]);
                break;
            case "JOIN":
                userPane.populateHand(msgParts[1]);
                menuBar.setPlayerName(msgParts[2]);
//                message.setText("Player  "+msgParts[2]+"'s turn.");
                break;
            case "RESETHAND":
                userPane.populateHand(msgParts[1]);
                message.setText("Player  "+msgParts[2]+"'s turn.");
                isMyTurn(msgParts[2]);
                break;
            case "REPLAYEDTILES": 
                userPane.repopulateHandFromServer(msgParts[1]);
                message.setText("Player  "+msgParts[2]+"'s turn.");
                isMyTurn(msgParts[2]);
                break;
            case "POPULATE":
                userPane.populateHand(msgParts[1]);
                message.setText("Player  "+msgParts[2]+"'s turn.");
                isMyTurn(msgParts[2]);
                break;
            case "INITBOARD":
                scrabbleBoard.updateJoinBoard(msgParts[1]);
//                menuBar.setPlayerName(msgParts[2]);
                message.setText("Player  "+msgParts[2]+"'s turn.");
                isMyTurn(msgParts[2]);
                break;
            case "UPDATEBOARD":
                System.out.println("Update board function - still needs work.");
                scrabbleBoard.updateBoard(msgParts[1]);
                message.setText("Player  "+msgParts[2]+"'s turn.");
                isMyTurn(msgParts[2]);
                break;
            case "NEWORDER":
                message.setText(msgParts[1]);
                menuBar.setPlayerName(msgParts[2]);
                break;
            case "MESSAGE":
                message.setText(msgParts[1]+" - "+"Player "+msgParts[2]+"'s turn");
                isMyTurn(msgParts[2]);
            default:
                break;
        }
        System.out.println("Source: DCScrabbleController.dcsProcessMessage(String) -> Finished processing message");
        
    }

    class FxSocketListener implements SocketListener {

        @Override
        public void onMessage(String line) {
            if (line != null && !line.equals("")) {
                System.out.println(line);
                dcsProcessMessage(line);
            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {
            System.out.println("socket is closed?  ");
            if (isClosed) {
                notifyDisconnected();
                if (isAutoConnected) {
                    System.out.println(ConnectionDisplayState.ATTEMPTING);
                } else {
                    System.out.println(ConnectionDisplayState.DISCONNECTED);
                }
            } else {
                setIsConnected(true);
                if (isAutoConnected) {
                    System.out.println(ConnectionDisplayState.AUTOCONNECTED);
                } else {
                    System.out.println(ConnectionDisplayState.CONNECTED);
                }
            }
        }
    }
}
