package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import logger.Logger;
import util.Connection;

public class Server {
  //getting logger instance
  private static Logger logger = Logger.getLogger();
  
  public void mainServer() throws Exception {
    final int SERVER_PORT = 19609;
    //starting server
    try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
      logger.logComms("Starting server on port " + SERVER_PORT);
      //main server while loop
      while(true) {
        logger.logComms("Listening for connections...");
        //waiting for connections here
        Socket clientSocket = serverSocket.accept();
        logger.logComms("Client connected", clientSocket);
        //creating connection object which contains streams and deals with them
        Connection connection = new Connection(clientSocket);
        //creating game object
        Game game = new Game();
        while (true){
          //asking for an int from the client to set game size
          int gameSize = connection.receiveInt();
          if (game.setSize(gameSize)){
            logger.logGame("Game size is " + gameSize);
            //send true if valid
            connection.sendBoolean(true);
            break;
          }
          //otherwise send false
          connection.sendBoolean(false);
        }
        //generates random numbers of the given size
        game.generateNumbers();
        logger.logGame("The generated number is " + game.getNumbers());
        //value which reflects if the number was guessed correctly
        boolean guessed = false;
        int numGuesses;
        //10 guesses loop
        for(numGuesses = 1; numGuesses <= 10; numGuesses++){
          //asking for a guess from the client
          String guess = connection.receiveString();
          logger.logGame("User guessed " + guess);
          //if user guessed correct - send true
          if((game.getNumbers()).equals(guess)){
            logger.logGame("Player guess the number after " + numGuesses + " guesses.");
            connection.sendBoolean(true);
            guessed = true;
            break;
          }else {
            //if guess was not correct - send false
            connection.sendBoolean(false);
            //compare the guess to the secret number
            game.compare(guess);
            //send back to the client correct and incorrect positions
            connection.sendPosPair(game.getPosPair());
          }
        }
        //if the number was guessed correctly
        if (guessed){
          //send number of guessed taken
          connection.sendInt(numGuesses);
        }else{
          //otherwise send secret number
          connection.sendString(game.getNumbers());
        }
        logger.logGame("Game finished.");
        logger.logComms("Closing socket.", clientSocket);
        //closing client socket
        clientSocket.close();
      }
      
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}