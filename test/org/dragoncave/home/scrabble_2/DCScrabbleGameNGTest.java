/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble_2;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Rider1
 */
public class DCScrabbleGameNGTest {
    
    public DCScrabbleGameNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getGameName method, of class DCScrabbleGame.
     */
    @Test
    public void testGetGameName() {
        System.out.println("getGameName");
        DCScrabbleGame instance = new DCScrabbleGame();
        int expResult = 15;
        String result = instance.getGameName();
        System.out.println("gameName:\t"+result);
        assertEquals(result.length(), expResult);
        assert(instance.getPlayers().size() == 0);
//        assert(instance.getPlayers().get(0) == null);
//        System.out.println("players:\t"+instance.getPlayers().get(0));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getAnotherRandomName method, of class DCScrabbleGame.
     */
    @Test
    public void testGetAnotherRandomName() {
        System.out.println("getAnotherRandomName");
        DCScrabbleGame instance = new DCScrabbleGame();
        List<String> randomNames = new ArrayList<>();
        int expResult = 15;
        for (int name = 0; name < 10000; name++){
            String result = instance.getAnotherRandomName();
            //System.out.println("another random name:\t"+result);
            if (!randomNames.contains(result)){
                randomNames.add(result);
                assertEquals(result.length(), expResult);
            }
        }
        System.out.println("number of names added:\t"+randomNames.size());
        randomNames.stream().forEach((name)->{
            assert (name.contains("0") || name.contains("1") || name.contains("2") || name.contains("3") || name.contains("4") || name.contains("5") ||
                    name.contains("6") || name.contains("7") || name.contains("8") || name.contains("9"));
            assert (name.contains("!") || name.contains("@") || name.contains("#") || name.contains("#") || name.contains("$") || name.contains("%") ||
                    name.contains("^") || name.contains("&") || name.contains("*") || name.contains("("));
                    
        });
        randomNames.clear();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    
    @Test
    public void testAddPlayer(){
        System.out.println("Testing AddPlayer()");
        DCScrabbleGame instance = new DCScrabbleGame();
        JSONObject jsonRequest1 = new JSONObject();
        jsonRequest1.put("request type", "join game");
        jsonRequest1.put("player name","player1");
        JSONObject add1 = instance.submitMainRequest(jsonRequest1);
        JSONObject jsonRequest2 = new JSONObject();
        jsonRequest2.put("request type", "join game");
        jsonRequest2.put("player name","player2");
        JSONObject add2 = instance.submitMainRequest(jsonRequest2);
        JSONObject jsonRequest3 = new JSONObject();
        jsonRequest3.put("request type", "join game");
        jsonRequest3.put("player name","player3");
        JSONObject add3 = instance.submitMainRequest(jsonRequest3);
        JSONObject jsonRequest4 = new JSONObject();
        jsonRequest4.put("request type", "join game");
        jsonRequest4.put("player name","player4");
        JSONObject add4 = instance.submitMainRequest(jsonRequest4);
        JSONObject jsonRequest5 = new JSONObject();
        jsonRequest5.put("request type", "join game");
        jsonRequest5.put("player name","player5");
        JSONObject add5 = instance.submitMainRequest(jsonRequest5);
        
        assert((add1.getBoolean("success") == true) && (add3.getBoolean("success") == true) && (add2.getBoolean("success") == true) && (add4.getBoolean("success") == true) &&  (add5.getBoolean("success") == false));
        instance.getPlayers().stream().forEach((name)->{
            System.out.println(name);
        });
        
        assert(instance.getPlayers().size() <= 4);
    }
    
    @Test
    public void testRemovePlayer(){
        System.out.println("Testing RemovePlayer()");
        DCScrabbleGame instance = new DCScrabbleGame();
        JSONObject jsonRequest1 = new JSONObject();
        jsonRequest1.put("request type", "join game");
        jsonRequest1.put("player name","player1");
        JSONObject add1 = instance.submitMainRequest(jsonRequest1);
        JSONObject jsonRequest2 = new JSONObject();
        jsonRequest2.put("request type", "join game");
        jsonRequest2.put("player name","player2");
        JSONObject add2 = instance.submitMainRequest(jsonRequest2);
        JSONObject jsonRequest3 = new JSONObject();
        jsonRequest3.put("request type", "join game");
        jsonRequest3.put("player name","player3");
        JSONObject add3 = instance.submitMainRequest(jsonRequest3);
        JSONObject jsonRequest4 = new JSONObject();
        jsonRequest4.put("request type", "join game");
        jsonRequest4.put("player name","player4");
        JSONObject add4 = instance.submitMainRequest(jsonRequest4);
        JSONObject jsonRequest5 = new JSONObject();
        jsonRequest5.put("request type", "join game");
        jsonRequest5.put("player name","player5");
        JSONObject add5 = instance.submitMainRequest(jsonRequest5);
        assert((add1.getBoolean("success") == true) && (add3.getBoolean("success") == true) && (add2.getBoolean("success") == true) && (add4.getBoolean("success") == true) &&  (add5.getBoolean("success") == false));
        boolean remove5 = instance.removePlayer("player6");
        assert((remove5 == false) && (instance.getPlayers().size() == 4));
        boolean remove3 = instance.removePlayer("player4");
        assert((remove3 == true) && (instance.getPlayers().size()==3));
        assert(!instance.getPlayers().contains("player4"));
        boolean remove2 = instance.removePlayer("player3");
        assert((remove2 == true) && (instance.getPlayers().size()==2));
        assert(!instance.getPlayers().contains("player3"));
        boolean remove4 = instance.removePlayer("player5");
        assert((remove4 == false) && (instance.getPlayers().size()==2));
        assert(!instance.getPlayers().contains("player5"));

    }    
    
    @Test
    public void playGame(){
        System.out.println("------------Test playGame()------------");
        Random rand = new Random();
        rand.setSeed(new Date().getTime());
        DCScrabbleGame game = new DCScrabbleGame();
        DCGenericScrabbleBoard gameBoard = game.getBoard();
        
        RandomMap rMap = new RandomMap(gameBoard);
        rMap.setDebug(false);
        Map<String,List<JSONObject>> playerHands = new HashMap<String,List<JSONObject>>();
        game.setDebug(true);
        
        String[] players = new String[4];
        players[0] = "player1";
        players[1] = "player2";
        players[2] = "player3";
        players[3] = "player4";
        
        JSONObject jsonRequest1 = new JSONObject();
        jsonRequest1.put("request type", "join game");
        jsonRequest1.put("player name",players[0]);
        JSONObject jsonRequest2 = new JSONObject();
        jsonRequest2.put("request type", "join game");
        jsonRequest2.put("player name",players[1]);
        JSONObject jsonRequest3 = new JSONObject();
        jsonRequest3.put("request type", "join game");
        jsonRequest3.put("player name",players[2]);
        JSONObject jsonRequest4 = new JSONObject();
        jsonRequest4.put("request type", "join game");
        jsonRequest4.put("player name",players[3]);
        JSONObject jsonRequest5 = new JSONObject();

        JSONObject p1 = game.submitMainRequest(jsonRequest1);
        JSONObject p2 = game.submitMainRequest(jsonRequest2);
        JSONObject p3 = game.submitMainRequest(jsonRequest3);
        JSONObject p4 = game.submitMainRequest(jsonRequest4);
        
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);
        
        playerHands.put(players[0] ,new ArrayList<JSONObject>());
        playerHands.put(players[1] , new ArrayList<JSONObject>());
        playerHands.put(players[2] , new ArrayList<JSONObject>());
        playerHands.put(players[3] , new ArrayList<JSONObject>());

        
        JSONObject jsonRequestGetHand = new JSONObject();
        jsonRequestGetHand.put("request type","get hand");
        playerHands.get(players[0] ).add(game.submitMainRequest(jsonRequestGetHand));
        playerHands.get(players[1] ).add(game.submitMainRequest(jsonRequestGetHand));
        playerHands.get(players[2] ).add(game.submitMainRequest(jsonRequestGetHand));
        playerHands.get(players[3] ).add(game.submitMainRequest(jsonRequestGetHand));
        
        System.out.println("/***************************************/\n\tprinting results from gettting hand....");
        for (int playerIndex = 0; playerIndex < 4; playerIndex++){
            JSONObject jObj = playerHands.get(players[playerIndex]).get(0);
            System.out.println("player"+(playerIndex+1)+" hand: "+jObj.toString());
            assert(jObj.getBoolean("success") == true);
        }
        
        JSONObject jsonRequestStartGame = new JSONObject();
        jsonRequestStartGame.put("request type","start game");
        JSONObject jsonStartGame = game.submitMainRequest(jsonRequestStartGame);
        System.out.println("\n\n\n/************************************************/\n\tmessage from start game: "+jsonStartGame.toString()+"\n/********************************************/\n\n");


        int round = 0;
        boolean playerStillHasTiles = true;
        while (playerStillHasTiles){
//        while(!game.isTileBagEmpty()){
            List<String> randTileMap = rMap.generateRandomHand3();
            int playerNumber = game.getCurrentPlayer()-1;
            int lastPlayNumber = playerHands.get(players[playerNumber]).size()-1;
//            System.out.println("number of tiles to submit/return: "+randTileMap.size()+"\n\t\tprocessing the following for player number "+playerNumber+" and play number "+lastPlayNumber+": "+playerHands.get(players[playerNumber]));
            ArrayList<Integer> indexArray = new ArrayList<>();
            Map<String,DCGenericTile> playTiles = new HashMap<>();
            JSONObject replacements = (JSONObject)playerHands.get(players[playerNumber]).get(lastPlayNumber).get("replacement tiles");
            JSONArray jarray = (JSONArray)replacements.getJSONArray("hand");
//            JSONArray jarray = (JSONArray)playerHands.get(players[playerNumber]).get(lastPlayNumber).get("replacement tiles");
//            JSONObject jObjReplacement = (JSONObject)playerHands.get(players[playerNumber]).get(lastPlayNumber).get("replacement tiles");
//            JSONArray jarray = (JSONArray)jObjReplacement.get("hand");
            for(int playIndex = 0; playIndex < Math.min(randTileMap.size(), jarray.length()); playIndex++){
//                int randIndex = Math.abs(rand.nextInt()%7);
                if (!indexArray.contains(playIndex)){
                    indexArray.add(playIndex);
//                    System.out.println("\t"+jarray.get(playIndex).toString());
                    jarray.get(playIndex);
                    DCGenericTile newTile = new DCGenericTile("A",1);
                    newTile.fromJsonString(jarray.get(playIndex).toString());
                    playTiles.put(randTileMap.get(playIndex), newTile);
                }
            }
            JSONArray remainingTiles = new JSONArray();
            System.out.println("\t"+players[playerNumber]+" played tiles: "+playTiles.toString());
            
            for (int remainingIndex = 0; remainingIndex < jarray.length(); remainingIndex++ ){
                if (!indexArray.contains(remainingIndex)){
                    System.out.println("\t"+"remaining tiles: "+jarray.get(remainingIndex));
                    remainingTiles.put(new JSONObject(jarray.get(remainingIndex).toString()));
                }
            }
            JSONObject playResponse = new JSONObject();
            int action = Math.abs(rand.nextInt()%24);
            switch (action){
                case 15:
                    playResponse = new JSONObject(game.returnTilesToBag(playTiles));
                    break; 
                default:
                    playResponse = new JSONObject(game.submitTiles(playTiles));
            }
            
            if (remainingTiles.length() == 0){
                playerStillHasTiles = false;
            }
                
            Set keys = playResponse.keySet();
            if (keys.contains("replacement tiles")){
                JSONArray replacedTiles = playResponse.getJSONObject("replacement tiles").getJSONArray("hand");
//                System.out.println("\treplacement tiles received: "+replacedTiles);
//                System.out.println("\treplaced Tiles: "+playResponse.getJSONObject("replacement tiles"));
//                System.out.println("replacedTiles size: "+replacedTiles.length()+"\tremainingTiles size: "+remainingTiles.length());
                for (int index = 0; index < remainingTiles.length(); index++){
                    replacedTiles.put(remainingTiles.get(index));
                }
//                System.out.println("final new Hand: "+replacedTiles);
                if(!game.isTileBagEmpty())
                    assert(replacedTiles.length() == 7);
                else 
                    assert(replacedTiles.length() <= 7);
//                JSONObject finalJObj = new JSONObject();
//                finalJObj.put("hand", replacedTiles);
//                finalJObj.put("hand", playResponse);
//                playerHands.get(players[playerNumber]).add(finalJObj);
                playerHands.get(players[playerNumber]).add(playResponse);
            }else if(keys.contains("error")){
                System.out.println("error Tiles: "+playResponse);
            }
            System.out.println("\tFINAL playResponse: "+playResponse);
        }
        

//        for (int player = 0; player < 4; player++){
//            System.out.println(players[player]+" : "+playerHands.get(players[player]).size());
//            for (int hand = 0; hand < playerHands.get(players[player]).size(); hand++){
//                System.out.println( "\t"+players[player]+" \thand "+ hand +": "+playerHands.get(players[player]).get(hand));
//            }
//        }
        
        game.getBoard().printBoard();
    }

    @Test
    public void testSubmitRequest(){
        DCScrabbleGame game = new DCScrabbleGame("testgame","player1");
        game.setDebug(true);
        RandomMap rMap = new RandomMap(game.getBoard());
        JSONObject jsonRequest1 = new JSONObject();
        jsonRequest1.put("request type", "join game");
        jsonRequest1.put("player name","player2");
        JSONObject jsonRequest2 = new JSONObject();
        jsonRequest2.put("request type", "join game");
        jsonRequest2.put("player name","player3");
        JSONObject player2 = game.submitMainRequest(jsonRequest1);
        System.out.println("response after joining game: "+player2.toString());
        JSONObject player3 = game.submitMainRequest(jsonRequest2);
        System.out.println("response after joining game: "+player3.toString());
        
        System.out.println("player 1 submitting getHand request ->....");
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("game name", "testgame");
        jsonRequest.put("request type", "get hand");
        jsonRequest.put("player name","player1");
        JSONObject jsonResponse = game.submitMainRequest(jsonRequest);
        System.out.println("response from player1 getHand()->\n\t"+jsonResponse.toString());
        
        System.out.println("\n\n\nplayer 2 submitting getHand request ->....");
        JSONObject jsonRequest4 = new JSONObject();
        jsonRequest4.put("game name", "testgame");
        jsonRequest4.put("request type", "get hand");
        jsonRequest4.put("player name","player2");
        JSONObject jsonResponse2 = game.submitMainRequest(jsonRequest4);
        System.out.println("response from player2 getHand()->\n\t"+jsonResponse2.toString());

        System.out.println("\n\n\nplayer 3 submitting getHand request ->....");
        JSONObject jsonRequest3 = new JSONObject();
        jsonRequest3.put("game name", "testgame");
        jsonRequest3.put("request type", "get hand");
        jsonRequest3.put("player name","player3");
        JSONObject jsonResponse3 = game.submitMainRequest(jsonRequest3);
        System.out.println("response from player3 getHand()->\n\t"+jsonResponse3.toString());

        System.out.println("\n\n\n/**********************************************/");
        JSONObject startRequest = new JSONObject();
        startRequest.put("request type","start game");
        JSONObject startResponse = game.submitMainRequest(startRequest);
        System.out.println("response from start game: "+startResponse.toString());
        System.out.println("/*********************************************/\n\n\n");
        
        
        List<String> listTileMap = rMap.generateRandomHand3();
//        
//        JSONObject jsonHand = new JSONObject(game.getHand());
//        System.out.println("player1 response from getHand()-> "+jsonHand.toString());
//        JSONObject jsonHand2 = new JSONObject(game.getHand());
//        System.out.println("player2 response from getHand()-> "+jsonHand2.toString());
        JSONArray jsonHandTiles = jsonResponse.getJSONObject("replacement tiles").getJSONArray("hand");
        JSONObject mapJsonTiles = new JSONObject();
        for(int index = 0; index < listTileMap.size(); index++){
            mapJsonTiles.put(listTileMap.get(index), jsonHandTiles.getJSONObject(index));
        }
        jsonRequest.put("tiles", mapJsonTiles);
        jsonRequest.put("request type", "submit tiles");
        JSONObject jsonResponse1 = game.submitMainRequest(jsonRequest);
        
        System.out.println("response : "+jsonResponse1);
        JSONObject replacementTiles = jsonResponse1.getJSONObject("replacement tiles");
        JSONArray jsonArrayNewTiles = replacementTiles.getJSONArray("hand");
        game.getBoard().printBoard();
        assert(jsonArrayNewTiles.length() == listTileMap.size());
    }
}
