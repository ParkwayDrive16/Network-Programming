import java.io.*;
import java.net.*;

class TCPClient{
	public static void main(String[] args) throws IOException{
		//creating sockets, streams and buffered reader
		String serverHostname = new String ("m1-c13n1.csit.rmit.edu.au");
		int serverPort = 19609; 
		Socket echoSocket = null;
		PrintStream out = null;
		BufferedReader in = null;

		echoSocket = new Socket(serverHostname, serverPort);
		out = new PrintStream(echoSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		//loops to get user input untill user input in null
		while ((userInput = stdIn.readLine()) != null) {
			//writes the input to the server
			out.println(userInput);
			//displays the line read
			System.out.println("echo: " + in.readLine());
			if (userInput.equals("X")) break;
		}
		//closing all streams and sockets
		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();
		}
}