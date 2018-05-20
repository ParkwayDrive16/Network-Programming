package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class Util {
  
  public static int getInt() throws IOException
  {
     BufferedReader br =  new BufferedReader(new InputStreamReader(System.in));
     String line;
     int nextInt;
     
     while (true)
     {
        line = br.readLine();
        try
        {
           nextInt = Integer.parseInt(line);
        }
        catch (NumberFormatException e)
        {
           System.out.println("Invalid input");
           continue;
        }
        return nextInt;
     }
   }
     
     public static String getString() throws IOException
     {
        BufferedReader br =  new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        return line;
     }
  
  public static final byte[] intToByteArray(int value) {
    return new byte[] {
            (byte)(value >>> 24),
            (byte)(value >>> 16),
            (byte)(value >>> 8),
            (byte)value};
  }
  
  public static int toInt(byte[] bytes)
  {
      return ByteBuffer.wrap(bytes).getInt();
  }
}
