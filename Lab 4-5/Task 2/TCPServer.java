import java.io.*;
import java.net.*;

class TCPServer{
	public static void main(String[] args) throws IOException{

	ServerSocket serverSocket = null; 
	int serverPort = 10007;
	serverSocket = new ServerSocket(serverPort); 
	Socket clientSocket = null; 
	clientSocket = serverSocket.accept(); 

	PrintStream out = new PrintStream(clientSocket.getOutputStream(), true); 
	BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream())); 
	String inputLine; 

	while ((inputLine = in.readLine()) != null) {
		System.out.println ("Server: " + inputLine); 
		out.println(inputLine); 
		if (inputLine.equals("X")) break; 
	} 
	out.close(); 
	in.close(); 
	clientSocket.close(); 
	serverSocket.close(); 
	} 
}