import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * This is the main class for the Scanner
 */

/**
 * @author Danny Reinheimer
 *
 */
public class Scanner {
	
	private String fileName;
	
	public Scanner(String fileName) {
		this.fileName = fileName;
	}


	public Vector<Pair<TokenNames, String>> runScanner() {
		// checks to see if we are given any arguments
//		if(args.length < 1) {
//			System.out.println("Please provide an input file to process");
//			System.exit(0);
//		}
		
		//String fileName = args[0];
		Scan scan = new Scan(fileName);
		Vector<TokenNames> outputTokens = new Vector<TokenNames>();
		Pair<TokenNames,String> tokenPair;
		Vector<Pair<TokenNames, String>> allTokens = new Vector<Pair<TokenNames, String>>();
		
		
		// get the name of the file minus the dot 
//			int pos = fileName.lastIndexOf(".");
//			String newFileName = fileName.substring(0, pos) + "_gen.c";
//			PrintWriter writer = new PrintWriter(newFileName,"UTF-8");
		
		// keep getting the next token until we get a null
		while((tokenPair = scan.getNextToken()) != null) {
			if(tokenPair.getKey() != TokenNames.Space && tokenPair.getKey() != TokenNames.MetaStatements) {
				outputTokens.addElement(tokenPair.getKey());
				allTokens.addElement(tokenPair);
			}
			
		}
		outputTokens.add(TokenNames.eof);
		Pair<TokenNames, String> fin = new Pair<TokenNames, String>(TokenNames.eof, "eof");
		allTokens.add(fin);
		
		return allTokens;
		
		
		
		

	}

}
