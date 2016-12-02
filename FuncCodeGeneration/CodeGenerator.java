import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * implements the main function that gets the file name and calls the scanner and parser 
 */

/**
 * @author Danny Reinheimer
 *
 */
public class CodeGenerator {

	
	/**
	 * starting point for the program
	 * @param args The file name to read in and parse
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// checks to see if we are given any arguments
		if(args.length < 1) {
			System.out.println("Please provide an input file to process");
			System.exit(0);
		}
		Vector<Pair<TokenNames,String>> scannedTokens = new Vector<Pair<TokenNames,String>>();
		// run initialize and run the scanner
		Scanner scanner = new Scanner(args[0]);
		scannedTokens = scanner.runScanner();
		// initialize and run the parser
		RecursiveParsing RP = new RecursiveParsing(scannedTokens);
		String fileName = "out.c";
		PrintWriter pw = new PrintWriter(new File(fileName));
		scanner.printMetaStatements(pw);
		RP.parse(pw);
		pw.close();
		System.out.println("The generated output file is "+fileName);

	}
	
	
	
	

}
