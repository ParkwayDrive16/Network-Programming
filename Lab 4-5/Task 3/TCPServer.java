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
		//creating server sockets, connection listeners
		communicationSocket = new ServerSocket(textPort);
		checksumSocket = new ServerSocket(checksumPort);
		clientSocket = communicationSocket.accept();
		clientSocket2 = checksumSocket.accept();
		//creating streams and buffered reader
		PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
		CheckedInputStream csum = new CheckedInputStream(clientSocket.getInputStream(), new Adler32());
		BufferedReader inText = new BufferedReader( new InputStreamReader(csum));
		BufferedReader inCheckSum = new BufferedReader( new InputStreamReader( clientSocket2.getInputStream()));
		
		String inputText;
		String inputCheckSum;
		//loop that runs unless 2 strings received are null
		while ((inputText = inText.readLine()) != null && (inputCheckSum = inCheckSum.readLine()) != null) {
			//converting checksum from long to a string
			String calculatedCsum = Long.toString((csum.getChecksum().getValue()));
			//displaying received and calculated checksums
			System.out.println ("Received checksum: \t" + inputCheckSum); 
			System.out.println("Calculated checksum: \t" + calculatedCsum);
			//compares checksums and writes a message accordingly
			if(inputCheckSum.equals(calculatedCsum))
				System.out.println("Matched!");
			else
				System.out.println("Oops, didn't match!");
			//sends received message back to the client
			out.println(inputText);
			//resets the checksum after all operations
			csum.getChecksum().reset();
			//exit if received X from the client
			if (inputText.equals("X")) break; 
		}
		//closing all streams and sockets
		out.close();
		inText.close();
		inCheckSum.close();
		clientSocket.close();
		clientSocket2.close();
		communicationSocket.close();
		checksumSocket.close();
		}
}