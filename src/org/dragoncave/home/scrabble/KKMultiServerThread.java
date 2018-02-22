/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble;

import java.net.*;
import java.io.*;

/**
 *
 * @author Rider1
 */
public class KKMultiServerThread extends Thread {
    private Socket socket = null;
    
    public KKMultiServerThread(Socket socket){
        super("KKMultiServerThread");
        this.socket = socket;
    }
    
    public void run(){
        try(
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ){
            String theInput, theOutput;
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            theOutput = kkp.processInput(null);
            out.println(theOutput);
            
            while((theInput = in.readLine()) != null){
                theOutput = kkp.processInput(theInput);
                out.println(theOutput);
                if (theOutput.equalsIgnoreCase("Bye.")){
                    break;
                }
            }
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
