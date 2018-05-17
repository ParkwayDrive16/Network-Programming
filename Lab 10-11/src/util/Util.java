package util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Util {
  public static String readFromChannel(SocketChannel channel) throws IOException{
    // read into a buffer
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    channel.read(buffer);
    // flip buffer and get the string string
    buffer.flip();
    Charset charset = Charset.forName("UTF-8");
    CharsetDecoder decoder = charset.newDecoder();
    CharBuffer charBuffer = decoder.decode(buffer);
    return charBuffer.toString();
  }
  
  public static void writeToChannel(String message, SocketChannel channel) throws IOException{
    //create buffer from the message
    ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
    //write to the channel provided
    channel.write(buffer);
  }

}
