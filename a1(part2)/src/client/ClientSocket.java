package client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;


import util.Connection;
import util.Util;

public class ClientSocket
{
//  final int SERVER_PORT = 19609;
//  final String SERVER_ADDR = "localhost";
  private final int NUM_OF_GUESSES = 10;
  final int SERVER_PORT = 19609;
  final String SERVER_ADDR = "m1-c13n1.csit.rmit.edu.au";
  
  public void mainClient() throws InterruptedException{
    try{
      //connecting to the server
      Socket serverSocket = new Socket(SERVER_ADDR, SERVER_PORT);
      //creating connection object
      Connection connection = new Connection(serverSocket);
      System.out.println("Connected to " + serverSocket.getInetAddress());
      //prompting the user for their name and send it to the server
      System.out.print("Please enter your name: ");
      connection.sendString(Util.getString());
      //main loop
      while(true) {
        int sizeOfGame = 0;
        int numberOfPlayers = 0;
        //checking from the server if game size needs to be set
        boolean sizeNeeded = connection.receiveBoolean();
        //if it does - ask the user for an input and send it to the server
        if(sizeNeeded) {
          while(true) {
            System.out.println("Enter a number from 3 to 8 to pick the game size ");
            sizeOfGame = Util.getInt();
            connection.sendInt(sizeOfGame);
            if (connection.receiveBoolean()){
              //if it doesn't - break out of the loop
              break;
            }
            System.out.println("Server rejected this game size. Try again.");
          }
        }
        //receiving number of players in the game and game size
        numberOfPlayers = connection.receiveInt();
        sizeOfGame = connection.receiveInt();
        System.out.println("Number of players : " + numberOfPlayers);
        System.out.println("Game size : " + sizeOfGame);

        String guess;
        //10 guesses loop
        for (int i = 0; i < NUM_OF_GUESSES; i++){
          while(true) {
            //asking the user for their guess
            System.out.print("Enter your guess : ");
            guess = Util.getString();
            //checking the size of input
            if(guess.length() == sizeOfGame) {
              connection.sendString(guess);
              break;
            } else {
              System.out.println("Incorrent size of the guess. Enter "+ sizeOfGame + " numbers.");
              Thread.sleep(1000);
            }
          }
          //if guess was correct then break out of the loop
          if (connection.receiveBoolean()){
            break;
          } else{
            //else receive correct and incorrect positions
            System.out.println("Numbers in correct position:\t" + connection.receiveInt());
            System.out.println("Numbers in incorrect position:\t" + connection.receiveInt());
          }
        }
        
        System.out.println(connection.receiveString());
        //printing info about all players in the game
        for (int i = 0; i < numberOfPlayers; i++) {
          System.out.println(connection.receiveString());
        }
        //receiving secret number from the server
        String secret = connection.receiveString();
        System.out.println(secret);
        String input;
        //asking the user if they want to play again
        while(true) {
          System.out.println("Do you with to play again (p) or quit (q)?");
          input = Util.getString();
          if(input.equals("q")) {
            //send false to the server and exit
            connection.sendBoolean(false);
            serverSocket.close();
            System.exit(0);
          }
          else if(input.equals("p")) {
            //send true to the server and start from the start of the main while loop
            connection.sendBoolean(true);
            break;
          } else
            System.out.println("Invalid input. Try again.");
        }
      }
    } catch (ConnectException e){
      System.out.println("Error: Couldn't connect to the server. Make sure it is running.");
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}