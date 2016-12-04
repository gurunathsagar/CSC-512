import java.io.*;
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
		Vector<Pair<TokenNames,String>> scannedTokens1 = new Vector<Pair<TokenNames,String>>();
		Map<String, Integer> varCount;
		List<String> main = new ArrayList<String>();
		List<String> code = new ArrayList<String>();
		List<String> last = new ArrayList<String>();
		boolean mainStarted = false;
		// run initialize and run the scanner
		Scanner scanner = new Scanner(args[0]);
		scannedTokens = scanner.runScanner();
		scannedTokens1 = scanner.runScanner();
		
		// initialize and run the parser and get the Variable Count in varCount
		InitialRecursiveParsing IRP = new InitialRecursiveParsing(scannedTokens);
		IRP.parse();
		varCount = IRP.getVarCount();
		//System.out.println(varCount);
		
		
		RecursiveParsing RP = new RecursiveParsing(scannedTokens1, varCount);
		String fileName = "temp.c";
		PrintWriter pw = new PrintWriter(new File(fileName));
		//scanner.printMetaStatements(pw);
		
		scanner.addMetaStatements(main);
		
		RP.parse(pw, main, last);
		
		pw.close();
		FileInputStream fstream = null;
		BufferedReader br = null;
		
		
		try {
			File file = new File("temp.c");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
					
				  // Print the content on the console
			  if(line.contains("mainFunc"))
				mainStarted=true;
					
				if(mainStarted)
					main.add(line);
				else
					code.add(line);
					
				}
				fileReader.close();
			} catch (IOException e) {
			e.printStackTrace();
		}
		
		PrintWriter pw3 = new PrintWriter(new File("out.c"));
		
		for(String s: main)
			pw3.print(s + "\n");
		for(String s: code)
			pw3.print(s + "\n");
		for(String s: last)
			pw3.print(s + "\n");
		pw3.close();
	}
	
	
	
	

}
