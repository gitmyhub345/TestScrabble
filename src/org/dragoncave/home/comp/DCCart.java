/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.comp;

import org.dragoncave.home.comp.RandID;

import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author Rider1
 */
public class DCCart {
    
    private List<String> items;
    private long cartID;
    
    public DCCart(){
       generateID();
       items = new ArrayList<String>();
    }
    
    public void addItem(String itemID){
        this.items.add(itemID);
    }
    
    public List<String> getItems(){
        return this.items;
    }
    
    public int numItems(){
        return items.size();
    }
    
    public long getCartID(){
        return cartID;
    }
    
    private void generateID(){
        cartID = RandID.getRandAppID();
    }
}
