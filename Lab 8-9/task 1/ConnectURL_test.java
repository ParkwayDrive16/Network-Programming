import java.net.*;
import java.io.*;

public class ConnectURL_test {
  public static void main(String[] args){
    URL url = null;
    String httpString = "http://";
    String host = "m1-c45n1.csit.rmit.edu.au";
    String filePath = "/~Course/index.php";
    StringBuilder sb = new StringBuilder();
    try {
      //try block creating URL from the string given
      sb.append(httpString);
      sb.append(host);
      sb.append(filePath);
      url = new URL(sb.toString());
    } catch (MalformedURLException e) {
      //message in case try block has failed
      System.out.println("Failed to create URL!");
    }

    //main try block
    try {
      //opening connection from the url
      URLConnection urlConnection = url.openConnection();
      //getting input stream
      InputStream input = urlConnection.getInputStream();
      //getting content type
      String contentType = urlConnection.getContentType();
      //getting content length
      int contentLength = urlConnection.getContentLength();
      //printing retrieved data
      System.out.printf("Content type: %s, content length: %d.\n", contentType, contentLength);
      
      //creating an object from url content
      Object content = url.getContent();
      //check if the object created is of instance InputStream
      if (content instanceof InputStream) {

        int data;
        input = (InputStream) content;
        //loop till there's more data
        while ((data = input.read()) != -1) {
          //print the content to the screen
          System.out.print((char) data);
        }
        //notify the user that end of the stream is reached
        System.out.println("\nInputStreamEnd");
      }
      //closing input stream
      input.close();
    } catch (IOException e) {
      //message in case try block has failed
      System.out.println("Exception was caught: " + e.getClass().getCanonicalName());
    }
  }
}