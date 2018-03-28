import java.io.*;
import java.net.*;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;

class TCPClient{
	public static void main(String[] args) throws IOException{

	String serverHostname = new String ("127.0.0.1");
	int textPort = 10007;
	int checksumPort = 20008;
	Socket textSocket = null;
	Socket checksumSocket = null;
	PrintStream outText = null;
	PrintStream outChSum = null;
	BufferedReader inText = null;

	textSocket = new Socket(serverHostname, textPort);
	checksumSocket = new Socket(serverHostname, checksumPort);
	outText = new PrintStream(textSocket.getOutputStream(), true);
	outChSum = new PrintStream(checksumSocket.getOutputStream(), true);
	CheckedOutputStream csum = new CheckedOutputStream(outText, new Adler32());
	inText = new BufferedReader(new InputStreamReader(textSocket.getInputStream()));

	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	String userInput;

	while ((userInput = stdIn.readLine()) != null) {
		userInput += "\n";
		byte[] b = userInput.getBytes();
		csum.write(b);
		csum.flush();

		outChSum.println(csum.getChecksum().getValue());
		System.out.println("echo: " + inText.readLine());
		csum.getChecksum().reset();
	}

	csum.close();
	outText.close();
	outChSum.close();
	inText.close();
	stdIn.close();
	textSocket.close();
	checksumSocket.close();
	}
}