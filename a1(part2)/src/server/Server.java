package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import logger.Logger;

public class Server {
  private static Logger logger = Logger.getLogger();
  private static Thread gameManager;
  private static Executor executor;
  
  private static PlayersManager pmanager;
  
  public void mainServer() throws Exception {
    final int SERVER_PORT = 19609;
    //creating executor with pool size 10
    executor = Executors.newFixedThreadPool(10);
    //creating player manager object
    pmanager = new PlayersManager();
    //creating new thread gamaManager
    gameManager = new Thread(new GameManager(pmanager));
    //starting game manager
    gameManager.start();
    
    logger.logComms("Server started and listening on port : " + SERVER_PORT);
    try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
      while (true)
      {
        //each new client connection starts new game thread
        executor.execute(new GameThread(serverSocket.accept(), pmanager));
      }
    }catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}