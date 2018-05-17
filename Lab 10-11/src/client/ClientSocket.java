package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import util.Util;

public class ClientSocket
{
  final int SERVER_PORT = 19609;
  final String SERVER_ADDR = "m1-c13n1.csit.rmit.edu.au";
  
  public void mainClient(){
    try{
      Scanner input = new Scanner(System.in);
      //creating selector
      Selector selector = Selector.open();
      //creating socket channel
      SocketChannel socketCh = SocketChannel.open();
      //setting non-blocking mode
      socketCh.configureBlocking(false);
      //connecting to the server
      socketCh.connect(new InetSocketAddress(SERVER_ADDR, SERVER_PORT));
      //registering all selectors
      int operations = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
      socketCh.register(selector, operations);
      
      while (true){
        if (selector.select() == 0) continue;
        //iterates through all selectors
        for (SelectionKey key : selector.selectedKeys()){
          //getting the channel from the key
          SocketChannel channel = (SocketChannel) key.channel();
          //if key is connectable then connect to the server
          if (key.isConnectable()){
            while (channel.isConnectionPending()){
              channel.finishConnect();
              // print local and server's address
              System.out.println("Local address: " + channel.getLocalAddress());
              System.out.println("Connected to: " + channel.getRemoteAddress());
            }
          }
          //if key is readable then server sent a message
          if (key.isReadable()){
            String message = Util.readFromChannel(channel);
            System.out.println(message);
            //if "x" is received, then disconnect
            if (message.equals("X")){
               channel.close();
               input.close();
               System.out.println("Disconnecting from the server.");
               System.exit(0);
            }
          }
          //if key is writable then write to the server
          if (key.isWritable()){
            //get input from the user
            String msg = input.nextLine();
            //send the message to the server
            Util.writeToChannel(msg, channel);
          }
        }
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}