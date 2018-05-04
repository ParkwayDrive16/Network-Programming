import java.net.*;
import java.util.Collections;
import java.util.jar.*;
import java.io.*;

public class ConnectJAR_test {
  public static void main(String[] args){
    URL url = null;

    //Strings that create full jar url
    String jarString = "jar:";
    String httpString = "http://";
    String host = "m1-c45n1.csit.rmit.edu.au";
    String filePath = "/~Course/";
    String fileName = "HelloWorld.jar!/";
    StringBuilder sb = new StringBuilder(host);
    //try block in case any exceptions occur
    try {
      //getting ip address of the host
      String ip = InetAddress.getByName(sb.toString()).getHostAddress();
      System.out.println("Connecting to: " + ip);

      //creating full jar url
      sb.insert(0, httpString);
      sb.insert(0, jarString);
      sb.append(filePath);
      sb.append(fileName);
      System.out.println(sb.toString());

      //creating url from the string builder (converted to a String first)
      url = new URL(sb.toString());
      JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();

      //getting content type
      String contentType = jarURLConnection.getContentType();
      //getting content length
      int contentLength = jarURLConnection.getContentLength();
      //printing retrieved data
      System.out.printf("Content type: %s, content length: %d.\n\n", contentType, contentLength);

      //creating jar file from the jar url
      JarFile jarFile = jarURLConnection.getJarFile();

      //iterating through the entries in the jar file and printing their names and sizes
      for(JarEntry entry : Collections.list(jarFile.entries())){
        System.out.println("Entry: " + entry.getName() + "(" + entry.getSize() + ")");
      }

    } catch (IOException e) {
      //message in case try block has failed
      System.out.println("Exception was caught: " + e.getClass().getCanonicalName());
    }
  }
}