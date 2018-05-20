package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
//class that represent connection to the socket, reads and writes to it
public class Connection {
  private BufferedInputStream in;
  private BufferedOutputStream out;
  
  public Connection(Socket socket){
    try {
      this.in = new BufferedInputStream(socket.getInputStream());
      this.out = new BufferedOutputStream(socket.getOutputStream());
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void sendString(String s)
  {
     try
     {
        byte[] sizeBytes = Util.intToByteArray(s.length());
        byte[] bytes = s.getBytes();
        out.write(sizeBytes);
        out.flush();
        out.write(bytes);
        out.flush();
     }
     catch (IOException e)
     {
        e.printStackTrace();
     }
  }
  
  public String receiveString()
  {
     try
     {
       byte[] size = new byte[Integer.BYTES];
       in.read(size);
       byte[] bytes = new byte[Util.toInt(size)];
       in.read(bytes);
       return new String(bytes);
     }
     catch (IOException e)
     {
        e.printStackTrace();
        return null;
     }
  }
  
  public void sendInt(int i)
  {
     try
     {
        byte[] bytes = Util.intToByteArray(i);
        out.write(bytes);
        out.flush();
     }
     catch (IOException e)
     {
        e.printStackTrace();
     }
  }
  
  
  public int receiveInt()
  {
     try
     {
        byte[] bytes = new byte[Integer.BYTES];
        in.read(bytes);
        return Util.toInt(bytes);
     }
     catch (IOException e)
     {
        e.printStackTrace();
        return 0;
     }
  }
  
  
  public void sendBoolean(boolean b)
  {
     try
     {
        if (b == true) {
          out.write((byte) 1);
          out.flush();
        } else {
          out.write((byte) 0);
          out.flush();
        }
        
     }
     catch (IOException e)
     {
        e.printStackTrace();
     }
  }

  public boolean receiveBoolean()
  {
     try
     {
        return (in.read() == 1);
     }
     catch (IOException e)
     {
        return false;
     }
  }
  
  public void sendPosPair(int[] positions) {
    sendInt(positions[0]);
    sendInt(positions[1]);
  }

}
