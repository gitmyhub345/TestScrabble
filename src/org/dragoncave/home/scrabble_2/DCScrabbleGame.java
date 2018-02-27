/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import org.json.*;

import org.dragoncave.home.web.DCScrabbleVerifier;

/**
 *
 * @author Rider1
 */
public class DCScrabbleGame {
    private DCGenericTileBag tileBag;
    private DCGenericScrabblePlay play;
    private int numPlayers;
    private List<String> playerNames;
    private Map<String,List<DCScrabbleWordStats>> playerStats; // map, playerName, List of words played
    private String gameName;
    private int currentPlayer;
    private int numRequests;
    private Random rand;
    private DCGenericScrabbleBoard board;
    private DCScrabbleVerifier verifier;
    private List<Map<String,DCGenericTile>> playedWords;
    private List<DCGenericTile> playedTiles;
    
    private boolean debug;
    private boolean isStarted;
    
    private int numConfirmed;
    private final int modeChallenging = 2;
    private final int modeChallenge = 1;
    private final int modeGame = 0;
    private int mode;
    
    private List<String> loseATurnPlayer;
    
    public DCScrabbleGame(){
        initRand();
        this.tileBag = new DCGenericTileBag();
        this.numPlayers = 0;
        this.currentPlayer = 1;
        this.numRequests = 0;
        this.playerNames = new ArrayList<>(0);
        this.gameName = createRandomName();
        this.debug = false;
        this.play = new DCGenericScrabblePlay();
        this.board = new DCGenericScrabbleBoard();
        this.play.setBoard(this.board);
        this.isStarted = false;
        this.mode = 0;
        this.playerStats = new HashMap<>();
        this.loseATurnPlayer = new ArrayList<String>();
        verifier = new DCScrabbleVerifier();
//        this.playerNames.add(getAnotherRandomName());
    }
    
    public DCScrabbleGame(String game, String player){
        this.tileBag = new DCGenericTileBag();
        this.numPlayers = 1;
        this.currentPlayer = 1;
        this.numRequests = 0;
        this.gameName = game;
        this.playerNames = new ArrayList<>();
        playerNames.add(player);
        this.debug = false;
        this.board = new DCGenericScrabbleBoard();
        this.play = new DCGenericScrabblePlay();
        this.play.setBoard(this.board);
        this.isStarted = false;
        this.mode = 0;
        this.playerStats = new HashMap<>();
        this.playerStats.put(player, new ArrayList<>());
        this.loseATurnPlayer = new ArrayList<String>();
        verifier = new DCScrabbleVerifier();
        initRand();
    }
    
    public boolean getIsStarted(){
        return this.isStarted;
    }
    
    private void startGame(){
        this.isStarted = true;
    }
    
    private void endGame(){
        this.isStarted = false;
    }
    
    public JSONObject totalPlayersStats(){
        JSONObject jsonPlayerStandings = new JSONObject();
        int playerpoints = 0;
        for(Entry<String,List<DCScrabbleWordStats>> entry: playerStats.entrySet()){
            playerpoints = 0;
            for(DCScrabbleWordStats ws: entry.getValue()){
                playerpoints += ws.getWordValue();
            }
            jsonPlayerStandings.put(entry.getKey(), playerpoints);
        }
        
        return jsonPlayerStandings;
    }
    
    public String getWinner(){
        String winner = "";
        int highestpoints = 0;
        
        JSONObject playerStats = totalPlayersStats();
        for (String playername: playerStats.keySet()){
            int playerpoints = playerStats.getInt(playername);
            if (playerpoints > highestpoints){
                highestpoints = playerpoints;
                winner = playername;
            }
        }
        
        return winner;
    }
    public void setDebug(boolean debug){
        this.debug = debug;
        this.board.setDebug(this.debug);
        this.play.setDebug(this.debug);
        this.verifier.setDebug(this.debug);
    }
    
    private void setBoard(DCGenericScrabbleBoard board){
        this.board = board;
        play.setBoard(this.board);
    }
    
    public DCGenericScrabbleBoard getBoard(){
        return this.board;
    }
    private void initRand(){
        this.rand = rand = new Random();
        rand.setSeed(new Date().getTime());
    }
    
    private String createRandomName(){
        int numGameNameChars = 15;
        String newName = ""; 
        String randChars = "%^&*()abcdefABCDEFghijklGHIJKLmnopqr01234MNOPQRstuvwxyzSTUVWXYZ56789!@#$_+";
        boolean acceptableName = false;
        while (!acceptableName){
            newName = "";
            for (int charNum = 0; charNum < numGameNameChars; charNum++){
                int nextIndex = Math.abs(rand.nextInt()%randChars.length());
                newName += randChars.substring(nextIndex, nextIndex+1);
            }
            if ((newName.contains("0") || newName.contains("1") || newName.contains("2") || newName.contains("3") || newName.contains("4") || newName.contains("5") ||
                    newName.contains("6") || newName.contains("7") || newName.contains("8") || newName.contains("9")) &&
                    (newName.contains("!") || newName.contains("@") || newName.contains("#") || newName.contains("#") || newName.contains("$") || newName.contains("%") ||
                    newName.contains("^") || newName.contains("&") || newName.contains("*") || newName.contains("("))){
                acceptableName = true;
            }
        }
        return newName;
    }
    
    public String getAnotherRandomName(){
        
        return createRandomName();
    }
    
    public String getGameName(){
        return this.gameName;
    }
    
    private boolean addPlayer(String playerName){
        boolean addedPlayer = false;

        if (!this.isStarted && this.numPlayers < 4){
            this.playerNames.add(playerName);
            this.playerStats.put(playerName, new ArrayList<>());
            addedPlayer = true;
            this.numPlayers++;
        }
        if(debug){
            System.out.println("DScrabbleGame: addPlayer()-> number of players: "+numPlayers);
        }
        return addedPlayer;
    }
    
    public boolean removePlayer(String playerName){
        boolean removedPlayer = false;
        
        if (this.playerNames.contains(playerName)){
            this.playerNames.remove(playerName);
            this.numPlayers--;
            removedPlayer = true;
        }
        
        return removedPlayer;
    }
    
    public List<String> getPlayers(){
        return this.playerNames;
    }
    
    public int getNextPlayer(){
        if(debug){
            System.out.println("DCScrabbleGame: getNextPlayer()->");
        }

        int nextPlayer = currentPlayer+1;
        
        if (nextPlayer > this.numPlayers)
            nextPlayer = 1;
        if(loseATurnPlayer.size()>0){
            if (playerNames.get(nextPlayer-1).equals(loseATurnPlayer.get(0)))
                nextPlayer++;
                if(nextPlayer > this.numPlayers)
                    nextPlayer = 1;
            }

        if(debug){
            System.out.println("\t\tnextPlayer: "+nextPlayer);
        }

        return nextPlayer;
    }
    
    public String getNextPlayerName(){
        int nextPlayer = getNextPlayer();
        return playerNames.get(nextPlayer-1);
    }
    
    private void calculateNextPlayer(){
        if (debug)
            System.out.println("DCScrabbleGame: calculateNextPlay()-> numRequest:\t"+numRequests+"\tnumPlayers:\t"+numPlayers);
        currentPlayer = numRequests%numPlayers;
    }
    
    private void incrementRequest(){
        this.numRequests++;
    }
    
    public String getHand(){
        if(debug){
            System.out.println("DCScrabbleGame: getHand()->\n\tfor player: "+getCurrentPlayer()+" with name "+getCurrentPlayerName());
        }
        JSONObject response = new JSONObject(); 
        if (isStarted){
            response.put("success", false);
            response.put("message","game already started");
            return response.toString();
        }
        String replacementTiles = "";
        if (this.tileBag.getTilesInBag() > 0) {
            incrementRequest();
//            calculateNextPlayer();
            replacementTiles = tileBag.getHandString();
//            response = new JSONObject(returnNextSteps(replacementTiles));
            response = returnNextSteps(replacementTiles);
//            jObjHand.put("replacement tiles",new JSONArray(hand));
            response.put("success", true);
            response.put("message","successfully retrieved tiles for hand");
            incrementNextPlayer();
        } else {
//            hand = "{\"error\": \"cannot get tiles for hand\"}";
            response.put("success", false);
            response.put("error","failed to get tiles");
        }
        return response.toString();
    }
    
    public String submitTiles(Map<String,DCGenericTile> playedTiles){
        if(debug){
            System.out.println("DCScrabbleGame: submitTiles()-> \n\tplayedTiles size: "+playedTiles.size()+"\n\tplayer name: "+getCurrentPlayerName());
            play.setDebug(this.debug);
        }
        JSONObject response = new JSONObject();
        if (!isStarted){
            response = new JSONObject();
            response.put("success", false);
            response.put("message","please start the game before submitting tiles");
            return response.toString();
//            return response;
        }
        incrementRequest();
//        calculateNextPlayer();
        int playsize = playedTiles.size();
        boolean result = play.isValid(playedTiles);
        numConfirmed = 0;
        int totalpoints = play.getPlayValue();
        
        if (result){
//            addPlayedStats();
//            String replacementTiles = tileBag.replacePlayedTilesString(playsize);
//            response = new JSONObject(returnNextSteps(replacementTiles));
//            response = returnNextSteps(replacementTiles);
//            response.put("tiles remaining",tileBag.getTilesInBag());
            response.put("success", true);
            response.put("points gained",totalpoints);
            response.put("message",getCurrentPlayerName()+ " successfully submitted tiles");
            response.put("number of played words", play.getPlayWords().size());
            response.put("played words",getJSONPlayWords());
            response.put("total words", play.getWords().size());
            response.put("current player",getCurrentPlayerName());
//            response.put("player stats",mapPlayerStatsToJSONObject());
//            return response.toString();
        }else{
            response = getNextSteps();
            response.put("error",getCurrentPlayerName()+ " played invalid tiles");
            response.put("success", false);
            incrementNextPlayer(); // this ends the play, moves on to the next player 
//            return response.toString();
        }
        
        return response.toString();
//        return response;
    }

    private JSONObject acceptPlay(){
        if(debug){
            System.out.println("DCScrabbleGame: acceptPlay()->");
        }
        JSONObject response;
        /**
         * originally from submitTiles() function above.
         */
        int playsize = play.getLastPlayedMap().size();
        int totalpoints = play.getPlayValue();
            addPlayedStats();
            String replacementTiles = tileBag.replacePlayedTilesString(playsize);
//            response = new JSONObject(returnNextSteps(replacementTiles));
            response = returnNextSteps(replacementTiles);
            response.put("tiles remaining",tileBag.getTilesInBag());
            response.put("success", true);
            response.put("points gained",totalpoints);
            response.put("message",getCurrentPlayerName()+ "'s submitted words are valid");
            response.put("number of played words", play.getPlayWords().size());
            response.put("played words",getJSONPlayWords());
            response.put("total words", play.getWords().size());
            response.put("player stats",mapPlayerStatsToJSONObject());
            response.put("challenged player name", getCurrentPlayerName());
            response.put("verified",true);
            incrementNextPlayer();
        if(debug){
            System.out.println("\tresults from challengeLastPlay: "+response.toString());
        }
            
        return response;
    }
    
    private JSONArray getJSONPlayWords(){
        if(debug){
            System.out.println("DCSrabbleGame: getPlayWords()->");
        }
        List<Map<String,DCGenericTile>> words = play.getPlayWords();
        List<String> listWords = orderPlayedWords(words);
        JSONArray jArrayWords = new JSONArray();
        for (int index = 0; index < listWords.size(); index++){
            jArrayWords.put(listWords.get(index));
        }
/*        for(Map<String,DCGenericTile> map: words){
            for (Entry<String,DCGenericTile> entry: map.entrySet()){
                DCGenericTile tile = entry.getValue();
                JSONObject jObjTile = new JSONObject(tile.getJsonString());
                jArrayWords.put(jObjTile);
            }
         }
*/        if(debug)
            System.out.println("\t ListToJSONArray: "+jArrayWords);
        return jArrayWords;
    }

    private List<String> orderPlayedWords(List<Map<String,DCGenericTile>> words){
        if(debug){
            System.out.println("DCScrabbleGame: orderPlayedWords()->"+"\n\t"+words.toString());
        }
        List<Map<String,DCGenericTile>> mapPlayedWords = words;
        List<String> playedWords = new ArrayList<>();
        for(Map<String,DCGenericTile> wordMap: mapPlayedWords){
            List<String> wordKeys = orderMapKeys(wordMap);
            String Word = "";
            for (int index = 0; index < wordKeys.size(); index++){
                Word +=wordMap.get(wordKeys.get(index)).getLetter();
            }
            playedWords.add(Word);
        }
        return playedWords;
    }
    private List<String> orderMapKeys(Map<String,DCGenericTile> mapTiles){
        if(debug){
            System.out.println("DCScrabbleGame: orderMapKeys()->"+"\n\t"+mapTiles.toString());
        }
        List<String> keys = new ArrayList<>();
        mapTiles.keySet().stream().forEach((key) -> {
            keys.add(key);
        });
        int numKeys = keys.size();
        String tvalue="";
        for(int indexa = 0; indexa < keys.size()-1; indexa++){
            for (int indexb = 0; indexb < keys.size()-1; indexb++){
                String[] a = keys.get(indexb).split("-");
                String[] b = keys.get(indexb+1).split("-");
                if(Integer.parseInt(a[0]) > Integer.parseInt(b[0])){
                    if(debug){
                        System.out.println("\tkeys are vertical");
                    }
                    tvalue = keys.get(indexb);
                    keys.remove(indexb);
                    keys.add(indexb+1, tvalue);
                }else if (Integer.parseInt(a[1]) > Integer.parseInt(b[1])){
                    if(debug){
                    System.out.println("\tkeys are horizontal");
                    }
                    tvalue = keys.get(indexb);
                    keys.remove(indexb);
                    keys.add(indexb+1, tvalue);                    
                }
            }

        }
        if (numKeys != keys.size()){
            System.out.println("error");
        }
        return keys;
    }

    public String returnTilesToBag(Map<String,DCGenericTile> returnTiles){
        if (!isStarted){
            JSONObject response = new JSONObject();
            response.put("success", false);
            response.put("message","please start the game before returning tiles");
            return response.toString();
        }
//        calculateNextPlayer();
        tileBag.returnTilesToBag(mapToList(returnTiles));
        incrementRequest();
        String replacementTiles = tileBag.replacePlayedTilesString(returnTiles.size());
//        JSONObject response = new JSONObject(returnNextSteps(replacementTiles));
        JSONObject response = returnNextSteps(replacementTiles);
        response.put("success", true);
        response.put("message", "successfully returned tiles to tilebag");
        incrementNextPlayer();
        return response.toString();
    }
    private JSONObject joinGame(String playerName){
        if(debug){
            System.out.println("DCScrabbleGame: joinGame()->");
        }
            
        String jsonString = "";
        JSONObject response = new JSONObject();
        response.put("player name",playerName);
        response.put("game name", this.gameName);
        if (addPlayer(playerName)){
//            response = getNextSteps();
            response.put("message", "successfully joined game, waiting for game to start.");
            response.put("success", true);
            jsonString = "{\"message\":\"successfully joined game\"}";
        } else {
            response.put("message","unable to join game");
            response.put("success",false);
            jsonString = "{\"message\":\"unable to join this game\"}";;
        }
        return response;
    }
    
    public boolean isTileBagEmpty(){
        return tileBag.getTilesInBag() <= 0;
    }
        
    private void incrementNextPlayer(){
        if(debug){
            System.out.println("DCScrabbleGame: incrementNextPlayer()->");
        }
        currentPlayer++;
        
        if (currentPlayer > (this.numPlayers )){
            currentPlayer = 1;
        }
        
        if(loseATurnPlayer.size() > 0 && getCurrentPlayerName().equals(loseATurnPlayer.get(0))){
            loseATurnPlayer.remove(0);
            currentPlayer++;
            if (currentPlayer > (this.numPlayers )){
                currentPlayer = 1;
            }
        }

        
        if(debug){
            System.out.println("\t\tresults after incrementing player: "+currentPlayer);
        }
            
    }
    
    private void decrementNextPlayer(){
        currentPlayer--;
        if(currentPlayer < 1){
            currentPlayer = this.numPlayers;
        }
        
    }
    
    private JSONObject returnNextSteps(String jsonStrReplacements){
//        String nextPlayer = "\"next player\":";
//        String prevPlayer = "\"previous player\":";
//        String replacedTiles = "\"replacement tiles\":";
//        
//        String strReturnString = "{"+nextPlayer+getNextPlayer()+","+
//                replacedTiles + jsonStrReplacements+"}";
        
        JSONObject jObj = getNextSteps();
        JSONObject jObjReplacements = new JSONObject(jsonStrReplacements);
//        JSONArray jArray = jObjReplacements.getJSONArray("hand");
        
        jObj.put("replacement tiles", jObjReplacements);
        
        if(debug){
            System.out.println("DCScrabbleGame: returnNextSteps() -> jsonString: "+ jObj);
//            System.out.println("DCScrabbleGame: returnNextSteps() -> jsonString2: "+strReturnString);
        }
//        return strReturnString;
        return jObj;
    }
    
    
    private JSONObject getNextSteps(){
        
        JSONObject response = new JSONObject();
        response.put("next player", getNextPlayer());
        response.put("next player name",getNextPlayerName());
        response.put("current player",getCurrentPlayer());
        response.put("current player name",getCurrentPlayerName());
        response.put("number of players",numPlayers);
        return response;
    }
    
    private int getPreviousPlayer(){
        int previousPlayer = getCurrentPlayer()-1;
        if (previousPlayer < 1)
            previousPlayer = numPlayers;
        
        return previousPlayer;
    }
    
    private String getPreviousPlayerName(){
        return playerNames.get(getPreviousPlayer()-1);
    }
    public int getCurrentPlayer(){
        return currentPlayer;
    }
    public String getCurrentPlayerName(){
        if(debug){
            System.out.println("DCScrabbleGame: getCurrentPlayerName()-> with current player number: "+getCurrentPlayer());
        }
        int currentPlayer = getCurrentPlayer();
        return playerNames.get(currentPlayer-1);

    }
    
    private JSONObject submitRequest(JSONObject jsonRequest){
        if(debug){
            System.out.println("DCSrabbleGame: submitRequest()->"+jsonRequest.toString()+"\ncurrent player: "+getCurrentPlayer());
        }
        JSONObject requestObject = jsonRequest;
        JSONObject responseObject = new JSONObject();
        JSONObject jsonHand = new JSONObject();
        Map<String,DCGenericTile> mapHand = new HashMap<>(); 
        if (requestObject.getString("request type") != null){
            switch(requestObject.getString("request type")){
                case "return tiles":
                    jsonHand = requestObject.getJSONObject("tiles");
                    mapHand = jsonObjectToMap(jsonHand);
                    responseObject = new JSONObject(returnTilesToBag(mapHand));
                    responseObject.put("mode",modeGame);
                    break;
                case "submit tiles":
                    jsonHand = requestObject.getJSONObject("tiles");
                    mapHand = jsonObjectToMap(jsonHand);
                    responseObject = new JSONObject(submitTiles(mapHand));
                    if (responseObject.getBoolean("success")){
                        responseObject.put("update board", requestObject.getJSONObject("tiles"));
                        responseObject.put("challenge","in progress");
                        responseObject.put("mode",modeChallenge);
                        mode = modeChallenge;
//                        mode = 1; // submitted tiles are valid accord -- switch modes to challenge
                    /**
                     * The below section should be reserved for finalized play
                     */
                        int userRemainingTiles = jsonRequest.getInt("remaining");
                        System.out.println("user tiles remaining: "+userRemainingTiles);
                        if (userRemainingTiles == 0 && responseObject.getInt("tiles remaining") == 0){
                            endGame();
                            play.getPlayStats().add(new DCScrabbleWordStats("bonus",50));
                            responseObject.put("players standings", totalPlayersStats());
                            responseObject.put("message",responseObject.getString("message")+"\nThis game is ended");
                            responseObject.put("winner",getWinner());
//                            requestObject.put("request type","end game"); //this is added at the end then also for all returning messages. updating this to make the change so it doesn't get orverridden.
                        }
                    /**
                     * end above section
                     */
                    } else{
                        mode = 0; // submitted tles are invalid -- back to game mode, player loses turn
                        responseObject.put("mode", mode);
                        if(debug)
                            System.out.println("DCScrabbleGame: submitRequest()-> processing submit tiles case error");
                    }
                    break;
                case "challengeYes":
                    
                    break;
                case "challengeNo":
                    
                    break;
                case "new game": // this scenario is handled outside of the game
                    break;
                case "start game": // 
//                    decrementNextPlayer(); // call this enable sending the current message
                    responseObject = getNextSteps();
//                    incrementNextPlayer(); // call this after getting the correct settings for message.
                    responseObject.put("mode", modeGame);
                    if (!isStarted){
                        startGame();
                        responseObject.put("success", true);
                        responseObject.put("message","game started");
                    } else {
                        responseObject.put("success",false);
                        responseObject.put("message","this game has already started");
                    }
                    break;
                case "end game":
                    endGame();
                    responseObject = getNextSteps();
                    responseObject.put("success",true);
                    responseObject.put("message","game ended");
                    responseObject.put("mode",modeGame);
                    responseObject.put("winner",getWinner());
                    break;
                case "join game":
                    String playerName = requestObject.getString("player name");
                    responseObject = joinGame(playerName);
                    responseObject.put("mode", modeGame);
                    break;
                case "get hand":
                    responseObject = new JSONObject(getHand());
                    responseObject.put("mode",modeGame);
                    break;
                case "create game": // this scenario is handled outside of the game also
                    break;
                case "challenge":
                    responseObject.put("mode", modeGame);
                    responseObject.put("message","last play has already been verified!");
                    break;
                default:
                    break;
            }
            
        }
        responseObject.put("request type", requestObject.getString("request type"));
        if(debug){
            System.out.println("\tsubmitRequest()->"+responseObject.toString());
        }
        return responseObject;
    }
    
    private Map<String,DCGenericTile> jsonObjectToMap(JSONObject jsonObjectHand){
        if(debug){
            System.out.println("DCScrabbleGame: jsonObjectToMap()->");
        }
        Map<String,DCGenericTile> hand = new HashMap<>();
        // iterate over the JSONArray to each entry
        for (String key: jsonObjectHand.keySet()){
            DCGenericTile tile = new DCGenericTile("A",1);
            tile.fromJsonString(jsonObjectHand.getJSONObject(key).toString());
            hand.put(key, tile);
        }
        if(debug){
            System.out.println("\tmap: "+hand);
        }
        return hand;
    }
    
    private List<DCGenericTile> jsonArrayToList(JSONArray jsonArrayTiles){
        if (debug){
            System.out.println("DCScrabbleGame: jsonArrayToList()->");
        }
        List<DCGenericTile> listTiles = new ArrayList<>();
        for (int index = 0; index < jsonArrayTiles.length(); index++){
            DCGenericTile tile = new DCGenericTile("A",1);
            tile.fromJsonString(jsonArrayTiles.getJSONObject(index).toString());
            listTiles.add(tile);
        }
        if (debug){
            System.out.println("\t list: "+listTiles.toString());
        }
        return listTiles;
    }
    
    private List<DCGenericTile> mapToList(Map<String,DCGenericTile> mapTiles){
        List<DCGenericTile> listTiles = new ArrayList<>(0);
        for (Entry<String,DCGenericTile> set: mapTiles.entrySet()){
            boolean add = listTiles.add(set.getValue());
        }
        return listTiles;
    }
    
    private JSONObject mapToJSONObject(Map<String,DCGenericTile> mapTiles){
        if (debug){
            System.out.println("DCScrabbleGame: mapToJSONObject()->");
        }
        JSONObject jObj = new JSONObject();
        for(Entry<String,DCGenericTile> entry: mapTiles.entrySet()){
//            DCGenericTile tile = entry.getValue();
            jObj.put(entry.getKey(), new JSONObject(entry.getValue().getJsonString()));
        }
        if (debug){
            System.out.println("\tJSONObject: "+jObj.toString());
        }
        
        return jObj;
    }
    
    private JSONObject mapPlayerStatsToJSONObject(){
        JSONObject jObj = new JSONObject();
        playerStats.forEach((key,listWords)->{
            jObj.put(key, listStatsToJSONArray(listWords));
        });
        return jObj;
    }
    private JSONArray listToJSONArray(List<DCGenericTile> listTiles){
        if (debug){
            System.out.println("DCScrabbleGame: listToJSONArray()->");
        }
            
        JSONArray jObjArray = new JSONArray();
        for(DCGenericTile tile: listTiles){
            jObjArray.put(new JSONObject(tile.getJsonString()));
        }
        if (debug){
            System.out.println("\tJSONArray: "+jObjArray.toString());
        }
        return jObjArray;
    }
    
    private JSONArray listStatsToJSONArray(List<DCScrabbleWordStats> listWords){
        if (debug){
            System.out.println("DCScrabbleGame: listStatsToJSONArray()->");
        }
        JSONArray jObjArray = new JSONArray();
        for (DCScrabbleWordStats stat: listWords){
            jObjArray.put(stat.toJSONObject());
        }
        return jObjArray;
    }

    public boolean isLastPlayConfirmed(){
        boolean result = false;
        if (numConfirmed == numPlayers-1 ){
            result = true;
            numConfirmed = 0;
        }
        return result;
    }
    
    public JSONObject challengeLastPlay(String player){
        if(debug){
            System.out.println("DCScrabbleGame: challengeLastPlay()->");
        }
        mode = modeChallenging;
        JSONObject jsonResponse = new JSONObject();
        boolean verified = challengePlay();
        mode = modeGame;
        if(verified){
            loseATurnPlayer.add(player);
            jsonResponse = acceptPlay();
            String previousMessage = jsonResponse.getString("message");
            jsonResponse.put("message", previousMessage + " "+player+" will lose a turn.");
        } else {
            board.removeTiles(play.getLastPlayedMap());
            play.removeLastPlayedTilesWords();
            jsonResponse = getNextSteps();
            jsonResponse.put("success",false);
            jsonResponse.put("challenged player name",getCurrentPlayerName());
            jsonResponse.put("message","word(s) played are invalid, "+getCurrentPlayerName()+"'s tiles have been return to "+getCurrentPlayerName());
            jsonResponse.put("verified",false);
            incrementNextPlayer();
            
        }
        jsonResponse.put("mode",mode);
//        jsonResponse.put("challenge result",(verified? "verified":"not verified"));
        jsonResponse.put("request type", "submit tiles");
        if(debug){
            System.out.println("\tresults from challengeLastPlay: "+jsonResponse.toString());
        }
        return jsonResponse;
    }
    
    public JSONObject confirmLastPlay(){
        if(debug){
            System.out.println("DCScrabbleGame: confirmLastPlay()-> game mode: "+mode);
        }
        JSONObject jsonResponse = new JSONObject();
        
        if (mode == modeGame){
            // already in game mode, no need to go through all of this
            jsonResponse.put("request type", "submit tiles");
            jsonResponse.put("mode", mode);
            jsonResponse.put("message","words played already verified");
            jsonResponse.put("success", true);
        }
        
        numConfirmed++;
        if (numConfirmed >= numPlayers){
            mode = modeGame;
        }else{
            mode = modeChallenge;
        }
        
        if(mode == modeGame){
            jsonResponse = acceptPlay();
            jsonResponse.put("mode",modeGame);
            jsonResponse.put("request type", "submit tiles");
        }else{
            jsonResponse.put("mode",modeChallenge);
            jsonResponse.put("message","waiting for others to either accept or challenge words played");
            jsonResponse.put("request type", "challenge");
            jsonResponse.put("challenged player name", getCurrentPlayerName());
            jsonResponse.put("verified",false);
                    
        }
        
        if(debug){
            System.out.println("\tresults from confirmLastPlay: "+jsonResponse.toString());
        }
        return jsonResponse;
    }
    
    public JSONObject submitMainRequest(JSONObject jsonRequest){
        if(debug){
            System.out.println("\n\n\nDCScrabbleGame: submitMainRequest()->..."+jsonRequest.toString());
        }
//        JSONObject jsonRequest = new JSONObject(jsonRequestString);
        JSONObject jsonResponse = new JSONObject();
        if(mode == modeGame){
            jsonResponse = submitRequest(jsonRequest);
        }else if (mode == modeChallenge){
            switch (jsonRequest.getString("challenge")){
                case "yes":
                    jsonResponse = challengeLastPlay(jsonRequest.getString("player name"));
                    jsonResponse.put("request type",(jsonResponse.getBoolean("success")?"submit tiles":"challenge"));
//                    jsonResponse.put("mode", modeGame);
//                    jsonResponse.put("success", true);
//                    jsonResponse.put("challenged player name", getCurrentPlayerName());
                    break;
                case "no":
                    jsonResponse = confirmLastPlay();
//                    jsonResponse.put("request type", "submit tiles");
                    jsonResponse.put("mode",mode);
                    jsonResponse.put("success", true);
//                    jsonResponse.put("challenged player name", getCurrentPlayerName());
                    break;
                default:
                    break;
            }
        }
        
        if(debug){
            System.out.println("\t\tDCSrabbleGame: submitMainRequest()-> results"+jsonResponse.toString());
        }
        return jsonResponse;
    }

    private void addPlayedStats(){
        if (debug){
            System.out.println("DCScrabbleGame: addPlayedStats()->");
        }
        String currentPlayer = getCurrentPlayerName();
        if (playerStats.keySet().contains(currentPlayer)){
            if(debug){
                System.out.println("\tadding play stats for player "+currentPlayer+" with words: "+play.getPlayStats().toString());
            }
            playerStats.get(currentPlayer).addAll(play.getPlayStats());
        } else {
            if (debug){
                System.out.println("\tadding play stats for new player "+currentPlayer+" with words: "+play.getPlayStats().toString());
            }
            playerStats.put(currentPlayer, play.getPlayStats());
        }
        if(debug){
            System.out.println("\t\tfinished adding played stats");
        }
    }
    
    private List<String> getPlayWords(){
        List<DCScrabbleWordStats> playedWordsStats = play.getPlayStats();
        List<String> playedWords = new ArrayList<>(playedWordsStats.size());
        for (DCScrabbleWordStats ws: playedWordsStats){
            playedWords.add(ws.getWord());
        }
        return playedWords;
    }
    
    private void getStartGameMessage(){
        
    }
    
    public int getMode(){
        return mode;
    }
    
    private boolean challengePlay(){
        if(debug){
            System.out.println("DCScrabbleGame: challengePlay()->");
        }
        boolean result = false;
        
        System.out.println("DCScrabbleGame: challengePlay()-> stubbed function" + "\n\tCurrent player: "+getCurrentPlayer()+"\tCurrent player name: "+getCurrentPlayerName());
        String currentPlayer = getCurrentPlayerName();
        
        List<String> listWords = getPlayWords();
        for(String word: listWords){
            try{
                result = verifier.verifyWord(word);
            } catch (IOException e){
                System.out.println("\n\n\terror with Verifying word: "+word+"\n\t"+e.getMessage());
            }
            if(!result)
                break;
        }
        
        return result;
    }
    
}
