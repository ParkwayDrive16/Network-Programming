package logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Logger {
  
  private static Logger logger = null;
  
  DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
      .withLocale( Locale.ENGLISH )
        .withZone( ZoneId.systemDefault() );
  private PrintWriter game;
  private PrintWriter comms;
  
  private Logger()
  {
     try
     {
        game = new PrintWriter("gameLog.log");
        comms = new PrintWriter("communicationLog.log");
     }
     catch (IOException e)
     {
        System.err.println("Provided files don't exist");
        e.printStackTrace();
        System.exit(1);
     }
  }
  
  
  public static Logger getLogger()
  {
     if (logger == null)
     {
       logger = new Logger();
     }
     return logger;
  }
  
  public void logGame(String msg)
  {
    System.out.println("[GAME] " + msg);
    String logMessage = String.format("[%s] %s", getTime(), msg);
    game.println(logMessage);
    game.flush();
  }
  
  public void logComms(String msg, Socket socket)
  {
    System.out.println("[COMM] " + msg);
    String logMessage = String.format("[%s] %s", getTime(), msg);
    comms.println(String.format("%s (%s:%d)", logMessage, socket.getInetAddress(), socket.getPort()));
    comms.flush();
  }
  
  public void logComms(String msg)
  {
    System.out.println("[COMM] " + msg);
    String logMessage = String.format("[%s] %s", getTime(), msg);
    comms.println(logMessage);
    comms.flush();
  }
  
  private String getTime() {
    return formatter.format(Instant.now());
  }
}
