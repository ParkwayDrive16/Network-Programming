import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Exercise1 {

	public static void main(String[] args) {
		int intch;
		Reader input = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			//checking for the end of the stream
			while((intch = input.read()) != -1) {
				//checks if character read was a space of a tab
				if(intch == (int)' ' || intch == (int)'\t'){
					intch = (int)'_';
				}
				
				System.out.print((char)intch);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
