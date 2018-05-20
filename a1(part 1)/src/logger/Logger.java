package logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;

public class Logger {
  
  private static Logger logger = null;
  private PrintWriter game;
  private PrintWriter comms;
  
  private Logger() {
    try{
      game = new PrintWriter("gameLog.log");
      comms = new PrintWriter("communicationLog.log");
    } catch (IOException e){
      System.err.println("Provided files don't exist");
      e.printStackTrace();
      System.exit(1);
    }
  }
  
  //get instance of the logger
  public static Logger getLogger() {
    if (logger == null){
      logger = new Logger();
    }
    return logger;
  }
  //logging game info
  public void logGame(String msg){
    System.out.println("[GAME] " + msg);
    String timeStamp = Instant.now().toString();
    String logMessage = String.format("[%s] %s", timeStamp, msg);
    
    game.println(logMessage);
    game.flush();
  }
  //logging communication info with the socket
  public void logComms(String msg, Socket socket) {
    String timeStamp = Instant.now().toString();
    System.out.println("[COMM] " + msg);
    String logMessage = String.format("[%s] %s", timeStamp, msg);
    comms.println(String.format("%s (%s:%d)", logMessage, socket.getInetAddress(), socket.getPort()));
    comms.flush();
  }
  //logging communication info with no sockets
  public void logComms(String msg) {
    String timeStamp = Instant.now().toString();
    System.out.println("[COMM] " + msg);
    String logMessage = String.format("[%s] %s", timeStamp, msg);
    comms.println(logMessage);
    comms.flush();
  }
}
