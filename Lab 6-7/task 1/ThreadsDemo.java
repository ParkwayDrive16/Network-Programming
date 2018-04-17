import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class MyThread implements Runnable{   
  @Override
  public void run(){
    try{
      synchronized (Thread.currentThread()) {
      //waiting for a notification from the main method
      Thread.currentThread().wait();
      //printing the string after waiting
      System.out.println(ThreadsDemo.input);
      }       
    } catch(InterruptedException e){
      e.printStackTrace();
    }
  }
}

public class ThreadsDemo {
  static String input;
  static Thread th;
   
  public static void main(String[] args) {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      //creating a thred and starting it
      th = new Thread(new MyThread());
      th.start();
      //prompting the user for a string
      System.out.print("Enter the string: ");
      //reding line of input
      input = in.readLine();

      synchronized (th) {
        //notifying the thread
        th.notify();
      }
      //closing buffer
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}