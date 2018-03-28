import java.io.*;
import java.net.*;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;

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
	CheckedInputStream csum = new CheckedInputStream(clientSocket.getInputStream(), new Adler32());
	BufferedReader inText = new BufferedReader( new InputStreamReader(csum));
	BufferedReader inCheckSum = new BufferedReader( new InputStreamReader( clientSocket2.getInputStream()));
	
	String inputText;
	String inputCheckSum;

	while ((inputText = inText.readLine()) != null && (inputCheckSum = inCheckSum.readLine()) != null) {
		String calculatedCsum = Long.toString((csum.getChecksum().getValue()));
		System.out.println ("Received checksum: \t" + inputCheckSum); 
		System.out.println("Calculated checksum: \t" + calculatedCsum);

		if(inputCheckSum.equals(calculatedCsum))
			System.out.println("Matched!");
		else
			System.out.println("Oops, didn't match!");

		out.println(inputText);
		csum.getChecksum().reset();
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

	public boolean areTheSame(String string1, String string2){
		if(string1.equals(string2))
			return true;
		else return false;
	}
}