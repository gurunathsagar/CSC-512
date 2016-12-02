import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * implements the main function that gets the file name and calls the scanner and parser 
 */

/**
 * @author Danny Reinheimer
 *
 */
public class MainCodeGenerator {

	
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
		Map<String, Integer> varCount;
		// run initialize and run the scanner
		Scanner scanner = new Scanner(args[0]);
		scannedTokens = scanner.runScanner();
		
		// initialize and run the parser and get the Variable Count in varCount
		InitialRecursiveParsing IRP = new InitialRecursiveParsing(scannedTokens);
		IRP.parse();
		varCount = IRP.getVarCount();
		//System.out.println(varCount);
		
		
		RecursiveParsing RP = new RecursiveParsing(scannedTokens, varCount);
		String fileName = "out.c";
		PrintWriter pw = new PrintWriter(new File(fileName));
		scanner.printMetaStatements(pw);
		RP.parse(pw);
		pw.close();
	}
	
	
	
	

}
