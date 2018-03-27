import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class TCPClient{
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException{

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
	inText = new BufferedReader(new InputStreamReader(textSocket.getInputStream()));

	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	String userInput;

	while ((userInput = stdIn.readLine()) != null) {
		outText.println(userInput);
		outChSum.println("checksum would be here");
		System.out.println("echo: " + inText.readLine());
		System.out.println(getHash(userInput));
	}

	outText.close();
	outChSum.close();
	inText.close();
	stdIn.close();
	textSocket.close();
	checksumSocket.close();
	}

	public static String getHash(String text) throws NoSuchAlgorithmException{
		// MessageDigest md = MessageDigest.getInstance("MD5");
		// md.update(text.getBytes());
		// byte[] digest = md.digest();
		// String myHash = Base64.getEncoder().encodeToString(digest);
		// return myHash;
	}
}