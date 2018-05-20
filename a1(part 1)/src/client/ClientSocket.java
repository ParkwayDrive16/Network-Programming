package client;

import java.io.IOException;
import java.net.Socket;


import util.Connection;
import util.Util;

public class ClientSocket
{
//  final int SERVER_PORT = 19609;
//  final String SERVER_ADDR = "localhost";
  final int SERVER_PORT = 19609;
  final String SERVER_ADDR = "m1-c13n1.csit.rmit.edu.au";
  
  public void mainClient() throws InterruptedException{
    try{
      //connecting to the server socket
      Socket serverSocket = new Socket(SERVER_ADDR, SERVER_PORT);
      //creating connection object which contains streams and deals with them
      Connection connection = new Connection(serverSocket);
      System.out.println("Connected to " + serverSocket.getInetAddress());
      
      int sizeOfGame;
      //loop asking for valid game size
      while(true) {
        System.out.println("Enter a number from 3 to 8 to pick the game size ");
        sizeOfGame = Util.getInt();
        //send it to the server
        connection.sendInt(sizeOfGame);
        
        if (connection.receiveBoolean()){
          //if valid game size then break out of the loop
           break;
        }
        //if invalid - ask the user again
        System.out.println("Server rejected this game size. Try again.");
      }
      //value which reflects if the number was guessed correctly
      boolean guessed = false;
      String guess;
      //10 guesses loop
      for (int i = 0; i < 10; i++)
      {
        
        while(true) {
          //prompts the user to enter the guess
          System.out.print("Enter your guess : ");
          guess = Util.getString();
          //checks the size of the guess and sends to the server if correct
          if(guess.length() == sizeOfGame) {
            connection.sendString(guess);
            break;
          } else {
            System.out.println("Incorrent size of the guess. Enter "+ sizeOfGame + " numbers.");
            Thread.sleep(1000);
          }
          
        }
        
        if (connection.receiveBoolean()){
          //if received true from the server then the number was guessed correctly
          guessed = true;
          break;
        } else{
          //otherwise get correct and incorrect positions from the server
          System.out.println("Numbers in correct position:\t" + connection.receiveInt());
          System.out.println("Numbers in incorrect position:\t" + connection.receiveInt());
        }
      }
      //if number was guessed correctly
      if (guessed){
        //receive number of guessed from the server
        int numGuesses = connection.receiveInt();
        //inform the user about number of guesses
        System.out.println("Your guess was correct");
        System.out.println("It took you " + numGuesses + " guesses.");
      }
      //otherwise number wasn't guessed correctly
      else {
        //receive secret number from the server
        String number = connection.receiveString();
        //show the client the correct number
        System.out.println("Oops, you ran out of guesses!");
        System.out.printf("Secret number was " + number);
      }
      //closing server socket
      serverSocket.close();
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}