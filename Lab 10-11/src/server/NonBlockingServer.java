package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import util.Util;

public class NonBlockingServer {
  final int SERVER_PORT = 19609;
  public void mainNonBlocking() throws Exception {
    try{
      System.out.println("Listening on " + SERVER_PORT);
       
      //creating selector
      Selector selector = Selector.open();
      //creating server socket channel
      ServerSocketChannel serverSocketCh = ServerSocketChannel.open();
      serverSocketCh.socket().bind(new InetSocketAddress(SERVER_PORT));
      //setting blocking to false
      serverSocketCh.configureBlocking(false);
      //registering selector
      serverSocketCh.register(selector, SelectionKey.OP_ACCEPT);
       
      while (true){
        if (selector.select() == 0) continue;
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        //iterates through all selectors
        while (keys.hasNext()){
          //remove the key to prevent repeating
          SelectionKey key = keys.next();
          keys.remove();

          if (key.isAcceptable()){
            //if key is connectable then client can connect
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            if (socketChannel != null){
              //register read key for the client connected
              System.out.println("Connected to: " + socketChannel.getRemoteAddress());
              socketChannel.configureBlocking(false);
              socketChannel.register(key.selector(), SelectionKey.OP_READ);
            }
          }
          //if key is readable then client has sent the message
          if (key.isReadable()){
            //read from the client socket
            SocketChannel socketChannel = (SocketChannel) key.channel();
            String message = Util.readFromChannel(socketChannel);

            if (message.length() == 0){
              continue;
            }
            System.out.println("Received from " + socketChannel.getRemoteAddress() + " : " + message);
            //change the string to upper case
            message = message.toUpperCase();
            // send it back
            Util.writeToChannel(message, socketChannel);

            //check if we need to exit
            if (message.equals("X")){
              //close channel is string sent was "X"
              System.out.println("Client " + socketChannel.getRemoteAddress() + " disconnected.");
              socketChannel.close();
            }
          }
        }
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}