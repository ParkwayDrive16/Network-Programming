package server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

//import logger.Logger;

public class PlayersManager {
//  private Logger logger = Logger.getLogger();
  
  private Queue<Object> playerQueue = new LinkedList<Object>();
  private CountDownLatch startCount, startGameNow, endGame;
  private ArrayList<SingleResult> results;
  
  private Game game;
  private int numOfPlayers;

  public PlayersManager() {
    init();
  }
  
  public synchronized void init() {
    startGameNow = new CountDownLatch(3);
    startCount = new CountDownLatch(1);
    results = new ArrayList<SingleResult>();
    
    this.numOfPlayers = 0;
    this.game = new Game();
    
    for (int i = 0; i < playerQueue.size(); i++){
       startCount.countDown();
       startGameNow.countDown();
    }
  }
  
  public Game getGame(){
    return game;
  }
  
  
  public synchronized void queue(Object lock){
    playerQueue.add(lock);
    startGameNow.countDown();
    startCount.countDown();
  }
  
  public synchronized void main(){
    if (playerQueue.size() >= 3)
      numOfPlayers = 3;
   else
     numOfPlayers = playerQueue.size();
    
    endGame = new CountDownLatch(numOfPlayers);
    Object lock = playerQueue.poll();
    synchronized (lock){
      lock.notify();
      try{
        lock.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    for (int i = 0; i < numOfPlayers - 1; i++){
      lock = playerQueue.poll();
      synchronized (lock){
        lock.notify();
      }
    }
  }
  
  public synchronized void addResult(SingleResult result)
  {
    results.add(result);
  }
  
  public synchronized List<SingleResult> getResults()
  {
     return results;
  }

  public CountDownLatch getStartCount() {
    return startCount;
  }

  public CountDownLatch getStartGame() {
    return startGameNow;
  }

  public CountDownLatch getEndGame() {
    return endGame;
  }

  public int getNumOfPlayers() {
    return numOfPlayers;
  }
  
}
