import java.io.BufferedReader;
import java.io.InputStreamReader;

class InputThread implements Runnable{

  @Override
  public void run(){
    try{
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      //prompting the user for a string
      System.out.print("Enter the string: ");
      //reading line of input
      ThreadsDemo.input = in.readLine();

      synchronized (ThreadsDemo.outputTh) {
        //notifying the thread
        ThreadsDemo.outputTh.notify();
      }
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}

class OutputThread implements Runnable{

  @Override
  public void run(){
    try{
      synchronized (Thread.currentThread()) {
        //waiting for a notification from InputThread
        Thread.currentThread().wait();
        //printing the string after waiting
        System.out.println(ThreadsDemo.input);
      }
      
      synchronized (ThreadsDemo.mainTh){
        //notyfying main thred to assign a string
        ThreadsDemo.mainTh.notify();
        //waiting for main to process
        ThreadsDemo.mainTh.wait();
        //print the string received from main
        System.out.println(ThreadsDemo.inputFromMain);
      }
      
      
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}

public class ThreadsDemo {
  static Thread inputTh, outputTh, mainTh;
  static String input, inputFromMain;
  public static void main(String[] args) {
    try {
      //creating threads and starting them
      inputTh = new Thread(new InputThread());
      outputTh = new Thread(new OutputThread());
      mainTh = Thread.currentThread();
  
      inputTh.start();
      outputTh.start();
      
      synchronized (mainTh){
        //main method waiting until notified from output thread
        //which happens after user string is printed
        mainTh.wait();
        inputFromMain = "This is a string from main method and will be printed after user string.";
        //notifying that string is assigned
        mainTh.notify();
      }
      
      
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}