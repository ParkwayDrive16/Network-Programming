import java.io.BufferedReader;
import java.io.InputStreamReader;

class InputThread implements Runnable{
  @Override
  public void run(){
    try{
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      //prompting for user input in the loop
      while(true){
        System.out.print("Enter the string: ");
        //reading line of input
        ThreadsDemo.input = in.readLine();
        //checking if input is "X" and setting global variable to false if it is
        if(ThreadsDemo.input.equals("X")){
          ThreadsDemo.keepRunning = false;
        }

        synchronized (ThreadsDemo.outputTh) {
          //notifying the thread to proceed
          ThreadsDemo.outputTh.notify();
        }
        //if global boolean if false - kill the thread
        if(!ThreadsDemo.keepRunning) return;
        //waiting for a response from output thread
        synchronized(Thread.currentThread()){
          Thread.currentThread().wait();
        }
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
      while(true){
        synchronized (Thread.currentThread()) {
          //waiting for input thread to notify
          Thread.currentThread().wait();
          //if global boolean if false - kill the thread
          if(!ThreadsDemo.keepRunning) return;
          //printing the input string
          System.out.println(ThreadsDemo.input);
        }

        synchronized(ThreadsDemo.inputTh){
          //notifying input thread that operations in output thread are done
          //to prompt the next input string
          ThreadsDemo.inputTh.notify();
        }
      }
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}

public class ThreadsDemo {
  static Thread inputTh, outputTh;
  static String input;
  //global boolean to check when to kill the threads
  static boolean keepRunning = true;
  public static void main(String[] args) {
    //creating threads and starting them
    inputTh = new Thread(new InputThread());
    outputTh = new Thread(new OutputThread());

    inputTh.start();
    outputTh.start();
   }
}