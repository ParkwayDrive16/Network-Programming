import java.io.*;
import java.net.*;

class TCPServer{
	public static void main(String[] args) throws IOException{

	int textPort = 10007;
	int checksumPort = 20008;
	ServerSocket communicationSocket = null;
	ServerSocket checksumSocket = null;
	Socket clientSocket = null;
	Socket clientSocket2 = null;

	communicationSocket = new ServerSocket(textPort);
	checksumSocket = new ServerSocket(checksumPort);
	clientSocket = communicationSocket.accept();
	clientSocket2 = checksumSocket.accept();

	PrintStream out = new PrintStream(clientSocket.getOutputStream(), true); 
	BufferedReader inText = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
	BufferedReader inCheckSum = new BufferedReader( new InputStreamReader( clientSocket2.getInputStream()));
	String inputText;
	String inputCheckSum;

	while ((inputText = inText.readLine()) != null && (inputCheckSum = inCheckSum.readLine()) != null) {
		System.out.println ("Server: " + inputText); 
		System.out.println("Checksum: " + inputCheckSum);
		out.println(inputText); 
		if (inputText.equals("X")) break; 
	}

	out.close(); 
	inText.close();
	inCheckSum.close();
	clientSocket.close();
	clientSocket2.close();
	communicationSocket.close();
	checksumSocket.close();
	} 
}