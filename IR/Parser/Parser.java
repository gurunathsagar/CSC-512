import java.util.Vector;
import java.io.*;
/**
 * implements the main function that gets the file name and calls the scanner and parser 
 */

/**
 * @author Gurunath Ashok Hanamsagar
 *
 */
public class Parser {

	
	/**
	 * starting point for the program
	 * @param args The file name to read in and parse
	 */
	public static void main(String[] args) throws IOException {
		// checks to see if we are given any arguments
		if(args.length < 1) {
			System.out.println("Please provide an input file to process");
			System.exit(0);
		}
		Vector<Pair<TokenNames, String>> allTokens = new Vector<Pair<TokenNames, String>>();
		// run initialize and run the scanner
		Scanner scanner = new Scanner(args[0]);
		allTokens = scanner.runScanner();
		// initialize and run the parser
		Vector<TokenNames> scannedTokens = new Vector<TokenNames>();
		Vector<String> tokenList = new Vector<String>();
		for(Pair<TokenNames, String> object: allTokens){
			scannedTokens.add(object.getKey());
			tokenList.add(object.getValue());
		}

		RecursiveParsing RP = new RecursiveParsing(scannedTokens, tokenList);
		//System.out.println("RP" + args[0]);
		try {
			RP.parse(args[0]);
		}
		catch(IOException e){
			
		}

	}
	
	
	
	

}
