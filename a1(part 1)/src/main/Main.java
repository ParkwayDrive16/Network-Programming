package main;

import java.util.Scanner;

import client.ClientSocket;
import server.Server;

public class Main {
  //main app that launches clients and servers
  static private Server server;
  static private ClientSocket client;
  public static void main(String[] args) throws Exception {
    Scanner keyboard = new Scanner(System.in);
    String input;
    
    while(true) {
      System.out.println("Enter 1 to start the server, "
          + "2 to start the client.");
      input = keyboard.nextLine();
      if(input.equals("1") || input.equals("2"))
        break;
      else {
        System.out.println("Invalid input. Try again.");
        Thread.sleep(1000);
      }
    }
    
    switch(input) {
    case "1":
      server = new Server();
      server.mainServer();
      break;
    case "2":
      client = new ClientSocket();
      client.mainClient();
      break;
    }
    keyboard.close();
  }

}
