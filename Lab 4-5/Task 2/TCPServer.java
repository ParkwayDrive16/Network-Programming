import java.io.*;
import java.net.*;

class TCPServer{
	public static void main(String[] args) throws IOException{
		//creating sockets, streams and buffered reader
		ServerSocket serverSocket = null; 
		int serverPort = 19609;
		serverSocket = new ServerSocket(serverPort); 
		Socket clientSocket = null; 
		clientSocket = serverSocket.accept(); 

		PrintStream out = new PrintStream(clientSocket.getOutputStream(), true); 
		BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream())); 
		String inputLine;
		//loop that runs unless the string received is null
		while ((inputLine = in.readLine()) != null) {
			//displays received string
			System.out.println ("Server: " + inputLine);
			//sends the same string back to the client
			out.println(inputLine);
			//breaks out of the loop if receives X
			if (inputLine.equals("X")) break; 
		}
		//closing all streams and sockets
		out.close(); 
		in.close(); 
		clientSocket.close(); 
		serverSocket.close(); 
		} 
}