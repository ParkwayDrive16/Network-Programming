package main;

import java.util.Scanner;

import client.ClientSocket;
import server.BlockingServer;
import server.NonBlockingServer;

public class Main {
  //main app that launches clients and servers
  static private BlockingServer blockingServer;
  static private NonBlockingServer nonBlockingServer;
  static private ClientSocket client;
  public static void main(String[] args) throws Exception {
    Scanner keyboard = new Scanner(System.in);
    String input;
    
    while(true) {
      System.out.println("Enter 1 to start the blocking server, "
          + "2 to start the non-blocking server and 3 to start the client.");
      input = keyboard.nextLine();
      if(input.equals("1") || input.equals("2") || input.equals("3"))
        break;
      else {
        System.out.println("Invalid input. Try again.");
        Thread.sleep(1000);
      }
    }
    
    switch(input) {
    case "1":
      blockingServer = new BlockingServer();
      blockingServer.mainBlocking();
      break;
    case "2":
      nonBlockingServer = new NonBlockingServer();
      nonBlockingServer.mainNonBlocking();
    case "3":
      client = new ClientSocket();
      client.mainClient();
      break;
    }
    keyboard.close();
  }

}
