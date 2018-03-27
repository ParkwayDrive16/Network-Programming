import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;

public class Exercise2 {

	public static void main(String[] args) throws IOException {
		
		Scanner scanner = new Scanner(System.in);
		String input;
		
		try {
			while(true) {
				//prompting the user to pick an option
				System.out.println("Enter 1 to run excercise 2a or 2 to run 2b (or exit to quit).");
				input = scanner.nextLine();
				switch(input) {
				case "1":
					exerciseA();
					break;
				case "2":
					exerciseB();
					break;
				case "exit":
					break;
				default:
					System.out.println("Incorrect input. Try again.");
					continue;
				}
				scanner.close();
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//method that runs task a in Exercise2
	public static void exerciseA() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String outputDest = "output.txt";
		String checksumDest = "checksumFile.txt";
		
		File dfile = new File(outputDest);
		File checksumFile = new File(checksumDest);
		
		FileOutputStream fos = new FileOutputStream(dfile);
		CheckedOutputStream csum = new CheckedOutputStream(fos, new Adler32());
		
		FileWriter fileWriter = new FileWriter(checksumFile);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		String line;
		
		while(true) {
			//reads 1 line at the time from the user
			line = scanner.nextLine();
			//if the input is "x" then terminates, otherwise writes the input
			if(line.equals("x")) {
				break;
			} else {
				line += "\n";
				byte[] b = line.getBytes();
				csum.write(b);
			}
			
		}
		//making sure to flush the output stream before accessing checksum
		csum.flush();
		//whiting checksum to the file
		printWriter.print(csum.getChecksum().getValue());
		
		scanner.close();
		csum.close();
		printWriter.close();
	}
	
	//method that runs task b in Exercise2
	public static void exerciseB() throws IOException {
		
		String inputFile = "output.txt";
		String checksumFile = "checksumFile.txt";
		
		File inputfile = new File(inputFile);
		FileInputStream fis = new FileInputStream(inputfile);
		CheckedInputStream csum = new CheckedInputStream(fis, new Adler32());
		
		FileReader readChecksum = new FileReader(checksumFile);
		BufferedReader br = new BufferedReader(readChecksum);
		//read the source file until the end of the file
        while (csum.read() >= 0) {}
        //reading the line from the file
      	String input = br.readLine();
        
        csum.close();
        br.close();
        //printing the results to the screen
        System.out.println("Calculated checksum of the file:	" + csum.getChecksum().getValue());
		System.out.println("Retrieved checksum from the file:	" + input);
	}

}
