package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import util.Util;

public class BlockingServer {
  
  public void mainBlocking() throws Exception {
    final int SERVER_PORT = 1212;
  
    try{
      //printing the port number
      System.out.println("Listening on " + SERVER_PORT);
      // creating server socket channel
      ServerSocketChannel serverSocketCh = ServerSocketChannel.open();
      serverSocketCh.socket().bind(new InetSocketAddress(SERVER_PORT));
      //setting blocking to true
      serverSocketCh.configureBlocking(true);
      
      while (true){
        //blocks socket until connected to the client
        SocketChannel socketChannel = serverSocketCh.accept();
        
        //creating new thread to deal with each socket
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
            //print client's address
              System.out.println("Connected to: " + socketChannel.getRemoteAddress());
              //running server loop
              while (true){
                //get message from the client
                String message = Util.readFromChannel(socketChannel);
                 
                // check that message isn't empty
                if (message.length() == 0){
                  // if it is, the client may have disconnected
                  continue;
                }
                 
                // convert to upper case
                System.out.println("Received from " + socketChannel.getRemoteAddress() + " : " + message);
                String newMessage = message.toUpperCase();
                // send new string to client
                Util.writeToChannel(newMessage, socketChannel);
                 
                // check if message was "x"
                if (message.equals("X")){
                  // if it was, the client will disconnect
                  System.out.println("Client " + socketChannel.getRemoteAddress() + " disconnected.");
                  socketChannel.close();
                  break;
                }
              }
            }catch (IOException e) {
              e.printStackTrace();
            }
          }
        }).start();
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}