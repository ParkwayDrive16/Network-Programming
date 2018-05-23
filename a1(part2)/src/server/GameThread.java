package server;

import java.io.IOException;
import java.net.Socket;

import logger.Logger;
import util.Connection;
// this class is the logic of the game thread
public class GameThread implements Runnable {
  private static Logger logger = Logger.getLogger();
  private Socket client;
  private PlayersManager pmanager;
  private Game game;
  private String playerName;
  Connection connection;
  private final int NUM_OF_GUESSES = 10;
  
  public GameThread(Socket sc, PlayersManager pmanager) {
    this.client = sc;
    this.pmanager = pmanager;
  }

  @Override
  public void run() {
    //creating connection object
    connection = new Connection(client);
    logger.logComms("Client connected", client);
    //receiving name from the player
    playerName = connection.receiveString();

    logger.logComms("Received name " + playerName + " from the client.", client);
    //main while loop
    while(true) {
      Object o = new Object();
     //add this player to the queue
      try{
        pmanager.queue(o);
        logger.logGame(playerName + " added to the queue");
        //call wait on the object until the game wakes it up
        synchronized (o)
        {
          o.wait();
        }
      } catch (InterruptedException e){
        e.getStackTrace();
      }
     
      logger.logGame(playerName + " joined the game");
      //creating game object
      game = pmanager.getGame();
      
      //prompting the first player joined to set game size if null
      int gameSize = 0;
      if(game.getNumbers() == null) {
        connection.sendBoolean(true);
         while (true){
           //receiving game size
           gameSize = connection.receiveInt();
           if (game.setSize(gameSize)){
             //if successful - send confirmation to the player
             logger.logGame("Game size set to " + game.getSize());
             connection.sendBoolean(true);
             break;
           }
         }
         //generating random numbers
         game.generateNumbers();
         logger.logGame("Random number generated : " + game.getNumbers());
         //notifying the rest of the players
         synchronized (o)
         {
           o.notify();
         }
      } else 
        connection.sendBoolean(false);
      
      logger.logComms("Number of players and game size is sent to " + playerName, client);;
      connection.sendInt(pmanager.getNumOfPlayers());
      connection.sendInt(game.getSize());
     
      boolean guessed = false;
      int numGuesses = 0;
      //10 guesses loop
      while (numGuesses < NUM_OF_GUESSES) {
        numGuesses++;
        //getiing a guess from the player
        String guess = connection.receiveString();
        
        logger.logGame(playerName + " guesses " + guess +  " (guess count " + numGuesses + ")");
        
        if((game.getNumbers()).equals(guess)){
          logger.logGame(playerName + " guessed the number!");
          connection.sendBoolean(true);
          guessed = true;
          break;
        }else {
          connection.sendBoolean(false);
          //compare the guess to the secret number
          game.compare(guess);
          //send back to the client correct and incorrect positions
          connection.sendPosPair(game.getPosPair());
        }  
      }
      //saving all results in SingleResult object
      SingleResult result = new SingleResult(numGuesses, guessed, playerName);
      //adding this result to array list
      pmanager.addResult(result);
      logger.logGame("Player " + playerName + " finished.");
      //sending appropriate message to the player depending when they finished
      if(pmanager.getEndGame().getCount() > 1) {
        connection.sendString("Please wait for other players to finish");
      } else {
        connection.sendString("You and all other players have finished.");
      }
      //player that finished first waiting for the EndGame count to reach 0
      pmanager.getEndGame().countDown();
      try {
        pmanager.getEndGame().await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      
      logger.logComms("Sending results to all the players");
      for (SingleResult sr : pmanager.getResults()) {
        connection.sendString(sr.getPlayerName() + (sr.isCorrect ? " guessed " : "didn't guess ") + "the number and used " + sr.getNumOfGuesses() + " guesses.");
      }
      logger.logGame("Revealing secret number : " + game.getNumbers());
      connection.sendString("Secret number is " + game.getNumbers());
      //asking player to play again or exit
      boolean playAgain = connection.receiveBoolean();
      if(!playAgain) {
        try {
          logger.logComms("Client disconnecting", client);
          client.close();
          break;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
