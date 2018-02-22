/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import org.json.JSONObject;
/**
 *
 * @author Will J
 */
public class DCScrabbleWordStats {
    boolean debug;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getWordValue() {
        return wordValue;
    }

    public void setWordValue(int wordValue) {
        this.wordValue = wordValue;
    }
    private int wordValue;
    
    public DCScrabbleWordStats(String word, int value){
        this.word = word;
        this.wordValue = value;
    }
    
    public DCScrabbleWordStats(){
        wordValue = 0;
        word = "";
    }
    
    public JSONObject toJSONObject(){
        JSONObject jObj = new JSONObject();
        jObj.put("word", this.word);
        jObj.put("value", wordValue);
        
        return jObj;
    }
}
