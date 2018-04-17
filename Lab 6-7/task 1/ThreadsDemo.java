import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class MyThread implements Runnable{
   private Thread mythread;
   
   MyThread()
   { 
      mythread = new Thread(this, "my runnable thread");
      mythread.start();
   }

   @Override
   public void run(){
      synchronized (this) {
         try{
            this.wait();
         } catch(InterruptedException e){
            e.printStackTrace();
         }
         System.out.println("Printing the string from the thread: " + ThreadsDemo.input);
      }

      System.out.println("MyThread run is finished." );
   }

}

public class ThreadsDemo {
   static String input;
   
   public static void main(String[] args) {
      try {
         BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
         MyThread th = new MyThread();
   
         System.out.print("Enter the string: ");
         
         input = in.readLine();
         
   
         synchronized (th) {
            th.notify();
         }
   
         in.close();
         System.out.println("Main method is finished.");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}