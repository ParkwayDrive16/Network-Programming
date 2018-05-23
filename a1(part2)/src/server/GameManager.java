package server;

import java.util.concurrent.TimeUnit;

import logger.Logger;

public class GameManager implements Runnable{
  private final int WAIT_TIME = 20;
  private PlayersManager pmanager;
  private static Logger logger = Logger.getLogger();
  
  //this thread is responsible of managing delays in the game
  public GameManager(PlayersManager o) {
    this.pmanager = o;
  }

  @Override
  public void run() {
    while (true) {
      try {
//        logger.logGame("Game created. Waiting for players to connect.");
        pmanager.getStartCount().await();
        logger.logGame("Player connected, waiting 20 second on other possible players");
        pmanager.getStartGame().await(WAIT_TIME, TimeUnit.SECONDS);
        logger.logGame("Starting the game");
        pmanager.main();
        pmanager.getEndGame().await();
        logger.logGame("Game finished!");
        
      } catch (InterruptedException e) {
        
      }
      pmanager.init();
    }
    
  }

}
