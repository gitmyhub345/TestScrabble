/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble.net;

/**
 *
 * @author Rider1
 */
public interface SocketListener {
    public void onMessage(String line);
    public void onClosedStatus(boolean isClosed);    
}
