import java.util.Vector;
import java.util.*;
import java.io.*;
/**
 * Implements the recursive decent parser 
 */

/**
 * @author Gurunath Ashok Hanamsagar
 *
 */
public class RecursiveParsing{
	
	public class TokenType{
		int offset;
		int size;
		
		TokenType(int off, int s){
			this.offset = off;
			this.size = s;
		}
	}
	
	public class Labels{
		int start;
		int end;
		
		Labels(int x){
			this.start=x;
			this.end=x+1;
		}
	}
	
	public class WhileLabels{
		int start;
		int mid;
		int end;
		
		WhileLabels(int x){
			this.start = x;
			this.mid = x+1;
			this.end = x+2;
		}
	}
	
	private static int numVariables;  // Keeps track of the number of variables 
	private static int numFunctions;  // Keeps track of the number of functions
	private static int numStatements; // Keeps track of the number of statements
	private static Vector<TokenNames> inputTokens; // Stores the set of input tokens 
	private static TokenNames currentToken;  // shows what the current token removed from the stack was for debug purposes 
	private static Vector<String> tokenList;	// The list of all the tokens of the program as a vector
	private static List<String> globalQueue = new ArrayList<String>();	// The global queue that comprises the token to be written
	private static List<String> localQueue = new ArrayList<String>();	// The local queue that comprises the token to be written
	private static List<String> finalQueue = new ArrayList<String>();	// The final queue that comprises the tokens to be written to file
	private static String newToken;	// The latest token evicted from the queue
	private static int globalCount;
	private static int localCount;
	private static boolean global;	// Current state
	private static Map<String, String> globalMap = new HashMap<String, String>();
	private static Map<String, String> localMap= new HashMap<String, String>();
	private static boolean declaringPhase;
	private static boolean firstGlobal;
	private static List<String> expStr = new ArrayList<String>();
	private static Stack<String> tempList = new Stack<String>();
	private static Stack<String> valStack = new Stack<String>();
	private static Stack<String> opStack = new Stack<String>();
	private static List<String> tokens = new ArrayList<String>();
	private static List<String> conditionTokens = new ArrayList<String>();
	private static Map<String, TokenType> globalArray = new HashMap<String, TokenType>();
	private static Map<String, TokenType> localArray = new HashMap<String, TokenType>();
	private static int bracketCount;
	private static StringBuilder globalString;
	private static int goToCount;
	private static Stack<Labels> goToMarkers = new Stack<Labels>();
	private static Stack<WhileLabels> goToMarkersWhile = new Stack<WhileLabels>();
	
	/**
	 * Constructor initializes the fields and get the list of input tokens
	 * @param inputTokens1
	 */
	public RecursiveParsing(Vector<TokenNames> inputTokens1, Vector<String> tokenList1) {
		numFunctions = 0;
		numVariables = 0;
		numStatements = 0;
		inputTokens = inputTokens1;
		currentToken = TokenNames.None;
		newToken = "";
		tokenList = tokenList1;
		localCount = globalCount = 0;
		global = true;
		declaringPhase = false;
		firstGlobal = true;
		bracketCount = 0;
		goToCount = 0;
		//globalMap = new HashMap<String, String>();
		//localMap = new HashMap<String, String>();
	}
	
	private void testGlobal(){
		
		for(String s: globalArray.keySet()){
			sayLine(s + " offset " + globalArray.get(s).offset + " size " +  globalArray.get(s).size);
		}
		
	}
	
	private void testLocalArray(){
		
		for(String s: localArray.keySet()){
			sayLine(s + " offset " + localArray.get(s).offset + " size " +  localArray.get(s).size);
		}
		
	}
	
	private void say(String str){
		//System.out.print(str);
	}
	
	private void sayLine(String str){
		//System.out.println(str);
	}
	
	private void mergeToLocal(){
		
		while(tokens.size()>1)
			localQueue.add(tokens.remove(0));
			
		while(tempList.size()>0)
			localQueue.add(tempList.pop());
			
		if(tokens.size()!=0)
			localQueue.add(tokens.remove(tokens.size()-1));
	}
	
	private void mergeLocalForCond(){
		
		while(tokens.size()!=0)
			localQueue.add(tokens.remove(0));
			
		while(tempList.size()!=0)
			localQueue.add(tempList.pop());
			
		while(conditionTokens.size()!=0)
			localQueue.add(conditionTokens.remove(0));
	}
	
	private void mergeConditionsToLocal(){
		while(conditionTokens.size()>0)
			localQueue.add(conditionTokens.remove(0));
	}
	
	private void storeLatestTokens(){
		
		while(! (localQueue.get(localQueue.size()-1).contains(";") || 
			localQueue.get(localQueue.size()-1).contains("return") ||
			localQueue.get(localQueue.size()-1).contains("{") || 
			localQueue.get(localQueue.size()-1).contains("}")) ){
			tempList.push(localQueue.remove(localQueue.size()-1));
		}
	}
	
	private void evaluateExpression(){
		
		//System.out.println(expStr);
		
		for(int i=0;i<expStr.size();i++){
			
			//System.out.println(expStr.get(i));
			
			if(expStr.get(i).equals("(")){
				opStack.push("(");
			}
			
			else if( opStack.size()==0 || (opStack.peek().equals("(")) ){
				if( expStr.get(i).equals("/") || expStr.get(i).equals("*") 
					|| expStr.get(i).equals("+") || expStr.get(i).equals("-") )
					opStack.push(String.valueOf(expStr.get(i)));
				else if(expStr.get(i).equals(")")){
					opStack.pop();
					continue;
				}
				
				else
					valStack.push(String.valueOf(expStr.get(i)));
			}
			
			else if( expStr.get(i).equals("+") || expStr.get(i).equals("-") ){
					
				String obj = opStack.pop();
				opStack.push(String.valueOf(expStr.get(i)));
				
				String v2 = valStack.pop();
				String v1 = valStack.pop();
				String eval = "local[" + localCount + "] = " + v1 + obj + v2;
				System.out.println(" eval = " + eval);
				tokens.add(eval+";");
				valStack.push("local["+localCount+"]");
				localCount++;

			}
			
			else if((expStr.get(i).equals("/") || expStr.get(i).equals("*") )
				&& (opStack.peek().equals("/") || opStack.peek().equals("*")) ){
					
				String obj = opStack.pop();
				opStack.push(String.valueOf(expStr.get(i)));
				
				String v2 = valStack.pop();
				String v1 = valStack.pop();
				String eval = "local[" + localCount + "] = " + v1 + obj + v2;
				System.out.println(" eval = " + eval);
				tokens.add(eval+";");
				valStack.push("local["+localCount+"]");
				localCount++;
					
				}
				
			else if((expStr.get(i).equals("/") || expStr.get(i).equals("*") )
				&& (opStack.peek().equals("+") || opStack.peek().equals("-")) ){
				
				opStack.push(String.valueOf(expStr.get(i)));
					
				}
			else if(expStr.get(i).equals(")")){
				String obj = opStack.pop();
				
				while( !obj.equals("(") ){
					
					String v2 = valStack.pop();
					String v1 = valStack.pop();
					
					String eval = "local[" + localCount + "] = " + v1 + obj + v2;
					obj = opStack.pop();
					System.out.println(" eval = " + eval);
					tokens.add(eval+";");
					valStack.push("local["+localCount+"]");
					localCount++;
				}
			}
			else
				valStack.push(String.valueOf(expStr.get(i)));
		}
		
		while(opStack.size()!=0){
			String obj = opStack.pop();
			String v2 = valStack.pop();
			String v1 = valStack.pop();
			
			String eval = "local[" + localCount + "] = " + v1 + obj + v2;
			tokens.add(eval+";");
			valStack.push("local["+localCount+"]");
			localCount++;
			System.out.println(eval);
			
		}
		
		if(valStack.size()!=0)
			tokens.add(valStack.pop());
		
		for(String s: tokens){
			
			//System.out.println( "In tokens" + s);
		}
	}
	
	private void printArray(List<String> arrayList){
		for(String obj:arrayList){
			
			System.out.print(obj);
			if(obj.contains(";"))
				System.out.println();
			
		}
	}
	
	private void printMap(Map<String, String> map){
		for(String obj:map.keySet()){
			System.out.println("k=" + obj + "v=" + map.get(obj));
		}
	}
	
	private void mergeLocalToGlobal(){
		
		for(String s: localQueue)
			globalQueue.add(s + " ");
			
		localQueue.clear();
		localMap.clear();
		localArray.clear();
		localCount=0;
		
	}
	
	private void errorPrint(String s){
		//System.out.println(" " + s + " ");
	}
	
	/**
	 * initialized the parsing and prints out the results when finished
	 */
	public void parse(String fileName) throws IOException{
		program();
		if(inputTokens.firstElement() == TokenNames.eof) {
			//System.out.println("Pass variable " + numVariables + " function " + numFunctions + " statement " + numStatements);
			
			try{
				String parts[] = fileName.split("\\.");
				File file = new File(parts[0]+"_gen.c");
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
				for(int i=0;i<globalQueue.size();i++){
				
					if(globalQueue.get(i).contains(";") || globalQueue.get(i).contains("}") || globalQueue.get(i).contains("{") 
					|| globalQueue.get(i).contains("if") || globalQueue.get(i).contains("while") ){
						
						bw.write(globalQueue.get(i) + "\n");
						
					}
					else{
						bw.write(globalQueue.get(i));
					}
				}
				
				bw.close();
				fw.close();
				
			}catch (IOException e) {
			     e.printStackTrace();
			} 
			finally {

			  }
			
		}
		else {
			System.out.println("error in Parsing");
		}
	}
	
	
	
	/**
	 * <program> --> <type name> ID <data decls> <func list> | empty
	 * @return A boolean indicating pass or error 
	 */
	private boolean program() {
		// check if we are at the eof
		
		while(inputTokens.firstElement() == TokenNames.MetaStatements){
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			
			globalQueue.add(newToken);
		}
		
		firstGlobal = true;
		localQueue.clear();
		if(inputTokens.firstElement() == TokenNames.eof) {
			return true;
		}
		else if(type_name()) {
			if(inputTokens.firstElement() == TokenNames.ID) {
				currentToken = inputTokens.remove(0); // get the ID token
				localQueue.add(tokenList.firstElement());
				//newToken = tokenList.remove(0);
				
				if(data_decls() && func_list()) {
					//check to see if the remaining token is eof is so this is a legal syntax
					if(inputTokens.firstElement() == TokenNames.eof) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * <func list> --> empty | left_parenthesis <parameter list> right_parenthesis <func Z> <func list Z> 
	 * @return A boolean indicating if the rule passed or failed 
	 */
	private boolean func_list() {
		if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
			declaringPhase = false;
			firstGlobal = false;
			
			if(globalCount>0)
				globalQueue.add("int global[" + globalCount + "];");
			
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			
			if(parameter_list()) {
				if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					localQueue.add(newToken);
					if(func_Z()) {
						return func_list_Z();
					}
					return false;
				}
				return false;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * <func Z> --> semicolon | left_brace <data decls Z> <statements> right_brace 
	 * @return A boolean indicating if the rule passed or failed 
	 */
	private boolean func_Z() {
		// checks if the next token is a semicolon
		if(inputTokens.firstElement() == TokenNames.semicolon) {
			currentToken = inputTokens.remove(0); // remove the token from the stack
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			
			mergeLocalToGlobal();
			
			return true;
		}
		
		if(inputTokens.firstElement() == TokenNames.left_brace) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			global = false;
			declaringPhase = true;
			//printArray(globalQueue);
			localQueue.add("int localCount;");
			
			if(data_decls_Z()) {
				if(statements()) {
					if(inputTokens.firstElement() == TokenNames.right_brace) {
						currentToken = inputTokens.remove(0);
						// Count the number of function definitions
						newToken = tokenList.remove(0);
						localQueue.add(newToken);
						numFunctions += 1;
						
						//sayLine("lCount" + localQueue.size());
						if(localCount > 0){
							
							for(int i=0;i<localQueue.size();i++){
								if(localQueue.get(i).contains("localCount")){
									localQueue.remove(i);
									localQueue.add(i, "int local[" + localCount + "];");
									break;
								}
							}
						}
						//printArray(localQueue);
						
						mergeLocalToGlobal();
						//printArray(globalQueue);
						localQueue.clear();
						localMap.clear();
						localArray.clear();
						return true;
					}
					return false;
				}
				return false;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * <func list Z> --> empty | <type name> ID left_parenthesis <parameter list> right_parenthesis <func Z> <func list Z>
	 * @return a boolean 
	 */
	private boolean func_list_Z() {
		if(type_name()) {
			if(inputTokens.firstElement() == TokenNames.ID) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				localQueue.add(newToken);
				
				if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					localQueue.add(newToken);
					if(parameter_list()) {
						if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
							currentToken = inputTokens.remove(0);
							newToken = tokenList.remove(0);
							localQueue.add(newToken);
							if(func_Z()) {
								return func_list_Z();
							}
						}						
					}					
				}				
			}
			return false;
		}
		// return true for the empty rule
		return true;		
	}
	
	/**
	 * <type name> --> int | void | binary | decimal 
	 * @return A boolean indicating if the rule passed or failed
	 */
	private boolean type_name() {
		if(inputTokens.firstElement() == TokenNames.Int || inputTokens.firstElement() == TokenNames.Void 
				|| inputTokens.firstElement() == TokenNames.binary || inputTokens.firstElement() == TokenNames.decimal) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			//System.out.println(newToken);
			return true;
		}
		return false;
	}
	
	/**
	 * <parameter list> --> empty | void <parameter list Z> | <non-empty list> 
	 * @return a boolean
	 */
	private boolean parameter_list() {
		// void <parameter list Z>
		if(inputTokens.firstElement() == TokenNames.Void) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			errorPrint("parameter List new token = " + newToken);
			return parameter_list_Z();
		}
		// <non-empty list>
		else if(non_empty_list()) {
			return true;
		}
		// empty
		return true;
	}
	
	/**
	 * <parameter list Z> --> empty | ID <non-empty list prime>
	 * @return a boolean
	 */
	private boolean parameter_list_Z() {
		if(inputTokens.firstElement() == TokenNames.ID) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			localMap.put(newToken, "local[" + localCount++ + "]");
			System.out.println(" Param List z " + newToken);
			
			return non_empty_list_prime();
		}
		return true;
	}
	
	/**
	 * <non-empty list> --> int ID <non-empty list prime> | binary ID <non-empty list prime> | 
	 * decimal ID <non-empty list prime>
	 * @return a boolean
	 */
	private boolean non_empty_list() {
		// check for int, binary, decimal
		if(inputTokens.firstElement() == TokenNames.Int || inputTokens.firstElement() == TokenNames.binary || 
				inputTokens.firstElement() == TokenNames.decimal) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			if(inputTokens.firstElement() == TokenNames.ID) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				localQueue.add(newToken);
				localMap.put(newToken, "local[" + localCount++ + "]");
				System.out.println(" Non empty list " + newToken);
				return non_empty_list_prime();
			}
		}
		return false;
	}
	
	/**
	 * <non-empty list prime> --> comma <type name> ID <non-empty list prime> | empty
	 * @return a boolean
	 */
	private boolean non_empty_list_prime() {
		if(inputTokens.firstElement() == TokenNames.comma) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			
			if(type_name()) {
				if(inputTokens.firstElement() == TokenNames.ID) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					localQueue.add(newToken);
					localMap.put(newToken, "local[" + localCount++ + "]");
					sayLine(" Putting in ID list prime 1 " + newToken);
					return non_empty_list_prime();
				}
				return false;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * <data decls> --> empty | <id list Z> semicolon <program> | <id list prime> semicolon <program>
	 * @return a boolean
	 */
	private boolean data_decls() {
		if(id_list_Z()) {
			if(inputTokens.firstElement() == TokenNames.semicolon) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				// count variable 
				numVariables += 1;
				return program(); //data_decls_Z();
			}
			return false;
		}
		if(id_list_prime()) {
			if(inputTokens.firstElement() == TokenNames.semicolon) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				declaringPhase = false;
				// since we consume the first id before we get here count this as a variable
				numVariables += 1;
				return program(); //data_decls_Z();
			}
			//return false;
		}
		return true;
	}
	
	/**
	 * <data decls Z> --> empty | int <id list> semicolon <data decls Z> | 
	 * 				     void <id list> semicolon <data decls Z> | 
	 * 			         binary <id list> semicolon <data decls Z> | decimal <id list> semicolon <data decls Z> 
	 * @return A boolean indicating if the rule passed or failed
	 */
	private boolean data_decls_Z() {
		if(type_name()) {
			localQueue.remove(localQueue.size()-1);
			if(id_list()) {
				if(inputTokens.firstElement() == TokenNames.semicolon) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					//localQueue.add(newToken);
					return data_decls_Z();
				}
				return false;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * <id list> --> <id> <id list prime>
	 * @return a boolean
	 */
	private boolean id_list() {
		if(id()) {
			return id_list_prime();
		}
		return false;
	}
	
	/**
	 * <id list Z> --> left_bracket <expression> right_bracket <id list prime>
	 * @return a boolean indicating if the rule passed or failed
	 */
	private boolean id_list_Z() {
		if(inputTokens.firstElement() == TokenNames.left_bracket) {
			firstGlobal = false;
			currentToken = inputTokens.remove(0);
			String idToken = tokenList.remove(0);
			newToken = tokenList.remove(0);
			if(inputTokens.firstElement() == TokenNames.NUMBER) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				if(global){
					if(globalArray.containsKey(idToken)){
						System.out.println("Redeclaration error");
						return false;
					}
					int num = Integer.parseInt(newToken);
					TokenType obj = new TokenType(globalCount, num);
					//sayLine("Putting in " + idToken);
					globalArray.put(idToken, obj);
					globalCount += num;
				}
				if(inputTokens.firstElement() == TokenNames.right_bracket) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					return id_list_prime();
				}
			}
		}
		return false;
	}
	
	/**
	 * <id list prime> --> comma <id> <id list prime> | empty
	 * @return a boolean indicating if the rule passed or failed
	 */
	private boolean id_list_prime() {
		if(inputTokens.firstElement() == TokenNames.comma) {
			
			declaringPhase = true;
			
			if(global && !tokenList.get(1).equals("[")){
				if(firstGlobal){
					firstGlobal = false;
					if(globalMap.containsKey(tokenList.get(0))){
						System.out.println("Redeclaration error");
						return false;
					}
					globalMap.put(tokenList.get(0), "global[" + globalCount++ + "]");
					System.out.println(" Putting in ID list prime 2 " + tokenList.get(0));
					newToken = tokenList.remove(0);
				}
				/*else{
					System.out.println(" Putting in ID list prime -- " + tokenList.get(1));
					if(globalMap.containsKey(tokenList.get(1))){
						System.out.println("Redeclaration error");
						return false;
					}
					globalMap.put(tokenList.get(1), "global[" + globalCount++ + "]");
				}*/
			}
			newToken = tokenList.remove(0);
			currentToken = inputTokens.remove(0);
			if(id()) {
				return id_list_prime();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * <id> --> ID <id Z>
	 * @return a boolean
	 */
	private boolean id() {
		if(inputTokens.firstElement() == TokenNames.ID) {
			currentToken = inputTokens.remove(0);
			
			if(global && declaringPhase){
				if(globalMap.containsKey(tokenList.firstElement()) || globalArray.containsKey(tokenList.firstElement())){
					sayLine("Redeclaration error");
					return false;
				}
				
				if(!(tokenList.get(1).equals("[")) ){
					globalMap.put(tokenList.firstElement(), "global[" + globalCount++ + "]");
					sayLine(" Putting in ID new= " + tokenList.firstElement());
				}
			}
			
			if(!global && declaringPhase){
				if(localMap.containsKey(tokenList.firstElement()) || localArray.containsKey(tokenList.firstElement())){
					sayLine("Redeclaration error");
					return false;
				}
				if(!(tokenList.get(1).equals("[")) ){
					localMap.put(tokenList.firstElement(), "local[" + localCount++ + "]");
					sayLine(" Putting local in ID new=" + tokenList.firstElement());
				}
			}
			
			newToken = tokenList.remove(0);
			return id_Z();
		}
		return false;
	}
	
	/**
	 * <id Z> --> left_bracket <expression> right_bracket | empty
	 * @return a boolean
	 */
	private boolean id_Z() {
		if(inputTokens.firstElement() == TokenNames.left_bracket) {
			String tokenId = newToken;
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			if(declaringPhase){
				if(inputTokens.firstElement() == TokenNames.NUMBER){
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					if(global){
						TokenType obj = new TokenType(globalCount, Integer.parseInt(newToken));
						globalArray.put(tokenId, obj);
						globalCount += Integer.parseInt(newToken);
					}
					else{
						TokenType obj = new TokenType(localCount, Integer.parseInt(newToken));
						localArray.put(tokenId, obj);
						localCount += Integer.parseInt(newToken);
					}
					if(inputTokens.firstElement() == TokenNames.right_bracket) {
						currentToken = inputTokens.remove(0);
						newToken = tokenList.remove(0);
						// count the number of variables 
						numVariables += 1;
						return true;
					}
					return false;
				}
				else{
					return false;	// In declaring phase, expressions not allowed.
				}
			}
			else if(expression()) {
				if(inputTokens.firstElement() == TokenNames.right_bracket) {
					currentToken = inputTokens.remove(0);
					evaluateExpression();
					expStr.clear();
					mergeToLocal();
					//printArray(localQueue);
					
					newToken = tokenList.remove(0);
					localQueue.add(newToken);
					
					// count the number of variables 
					numVariables += 1;
					return true;
				}
				return false;
			}
			return false;
		}
		// count the number of variables 
		numVariables += 1;
		return true;
	}
	
	/**
	 * <block statements> --> left_brace <statements> right_brace 
	 * @return a boolean
	 */
	private boolean block_statements() {
		if(inputTokens.firstElement() == TokenNames.left_brace) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			//localQueue.add(newToken);
			if(statements()) {
				if(inputTokens.firstElement() == TokenNames.right_brace) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					//localQueue.add(newToken);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * <statements> --> empty | <statement> <statements> 
	 * @return a boolean
	 */
	private boolean statements() {
		
		declaringPhase = false;
		//printMap(localMap);
		if(statement()) {
			numStatements += 1;
			return statements();
		}
		return true;
	}
	
	/**
	 * <statement> --> ID <statement Z> | <if statement> | <while statement> | 
	 *	<return statement> | <break statement> | <continue statement> | 
	 *	read left_parenthesis  ID right_parenthesis semicolon | 
	 *  write left_parenthesis <expression> right_parenthesis semicolon | 
	 *  print left_parenthesis  STRING right_parenthesis semicolon 
	 * @return a boolean indicating if the rule passed or failed 
	 */
	private boolean statement() {
		if(inputTokens.firstElement() == TokenNames.ID) {
			currentToken = inputTokens.remove(0);
			if(localMap.containsKey(tokenList.firstElement())){
				localQueue.add(localMap.get(tokenList.firstElement()));
				newToken = tokenList.remove(0);
			}
			else if(localArray.containsKey(tokenList.firstElement())){
				
			}
			else if(globalArray.containsKey(tokenList.firstElement())){
				
			}
			else if(globalMap.containsKey(tokenList.firstElement())){
				localQueue.add(globalMap.get(tokenList.firstElement()));
				newToken = tokenList.remove(0);
			}
			else{
				System.out.println("Undeclared variable" + tokenList.firstElement());
			}
			bracketCount = 0;
			
			return statement_Z();
		}
		if(if_statement()) {
			return true;
		}
		if(while_statement()) {
			return true;
		}
		if(return_statement()) {
			return true;
		}
		if(break_statement()) {
			return true;
		}
		if(continue_statement()) {
			return true;
		}
		if(inputTokens.firstElement() == TokenNames.read) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				localQueue.add(newToken);
				if(inputTokens.firstElement() == TokenNames.ID) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					
					if(localMap.containsKey(newToken))
						localQueue.add(localMap.get(newToken));
					else if(globalMap.containsKey(newToken))
						localQueue.add(globalMap.get(newToken));
					else
						System.out.println("Undeclared Variable");
						
					if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
						currentToken = inputTokens.remove(0);
						newToken = tokenList.remove(0);
						localQueue.add(newToken);
						if(inputTokens.firstElement() == TokenNames.semicolon) {
							currentToken = inputTokens.remove(0);
							newToken = tokenList.remove(0);
							localQueue.add(newToken);
							return true;
						}
					}
				}
			}
			return false;
		}
		
		// write left_parenthesis <expression> right_parenthesis semicolon
		if(inputTokens.firstElement() == TokenNames.write) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				localQueue.add(newToken);
				if(expression()) {
					if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
						currentToken = inputTokens.remove(0);
						evaluateExpression();
						expStr.clear();
						mergeToLocal();
						newToken = tokenList.remove(0);
						localQueue.add(newToken);
						if(inputTokens.firstElement() == TokenNames.semicolon) {
							currentToken = inputTokens.remove(0);
							newToken = tokenList.remove(0);
							localQueue.add(newToken);
							return true;
						}
					}
				}
			}
			return false;
		}
		
		// print left_parenthesis  STRING right_parenthesis semicolon
		if(inputTokens.firstElement() == TokenNames.print) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				localQueue.add(newToken);
				if(inputTokens.firstElement() == TokenNames.STRING) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					localQueue.add(newToken);
					if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
						currentToken = inputTokens.remove(0);
						newToken = tokenList.remove(0);
						localQueue.add(newToken);
						if(inputTokens.firstElement() == TokenNames.semicolon) {
							currentToken = inputTokens.remove(0);
							newToken = tokenList.remove(0);
							localQueue.add(newToken);
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;
	}
	
	/**
	 * <statement Z> --> <assignment Z> | <func call>
	 * @return a boolean indicating if the rule passed or failed
	 */
	private boolean statement_Z() {
		if(assignment_Z()) {
			return true;
		}
		else if(func_call()) {
			return true;
		}
		return false;
	}
	
	/**
	 * <assignment Z> --> equal_sign <expression> semicolon | 
	 * left_bracket <expression> right_bracket equal_sign <expression> semicolon
	 * @return a boolean
	 */
	private boolean assignment_Z() {
		if(inputTokens.firstElement() == TokenNames.equal_sign) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			if(expression()) {
				if(inputTokens.firstElement() == TokenNames.semicolon) {
					currentToken = inputTokens.remove(0);
					evaluateExpression();
					//System.out.println(tokens);
					expStr.clear();
					if(tokens.size()!=0){
						tokens.add("local[" + localCount + "] = " + tokens.remove(tokens.size()-1) + ";");
						localCount++;
					}
					//mergeToLocal();
					//printArray(localQueue);
					
					while(tokens.size()!=0)
						localQueue.add(tokens.remove(0));
						
					while(tempList.size()!=0)
						localQueue.add(tempList.pop());
					localQueue.add("local[" + (localCount-1) + "]");
					newToken = tokenList.remove(0);
					localQueue.add(newToken);
					return true;
				}
			}
			return false;
		}
		if(inputTokens.firstElement() == TokenNames.left_bracket) {
			currentToken = inputTokens.remove(0);
			String idToken = tokenList.remove(0);	// The identifier
			newToken = tokenList.remove(0);			// The [
			/*if(inputTokens.firstElement() == TokenNames.NUMBER) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				int index = Integer.parseInt(newToken);
				if(localArray.containsKey(idToken)){
					if(localArray.get(idToken).size > index ){
						int finalIndex = index + localArray.get(idToken).offset;
						String addToken = "local[" +  Integer.toString(finalIndex) + "]";
						localQueue.add(addToken);
					}
					else{
						System.out.println(" Array out of bounds");
						return false;
					}
				}
				else if(globalArray.containsKey(idToken)){
					if(globalArray.get(idToken).size > index ){
						int finalIndex = index + globalArray.get(idToken).offset;
						String addToken = "global[" +  Integer.toString(finalIndex) + "]";
						localQueue.add(addToken);
					}
					else{
						System.out.println(" Array out of bounds");
						return false;
					}
				}
				
				if(inputTokens.firstElement() == TokenNames.right_bracket) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					if(inputTokens.firstElement() == TokenNames.equal_sign) {
						currentToken = inputTokens.remove(0);
						newToken = tokenList.remove(0);
						localQueue.add(newToken);
						if(expression()) {
							if(inputTokens.firstElement() == TokenNames.semicolon) {
								currentToken = inputTokens.remove(0);
								evaluateExpression();
								expStr.clear();
								mergeToLocal();
								//printArray(localQueue);
								newToken = tokenList.remove(0);
								localQueue.add(newToken);
								return true;
							}
						}
					}
				}
			}*/
			{
				String addToken = "";
				boolean flag = false;
				if(localArray.containsKey(idToken)){
					addToken = "local[";
					flag = true;
				}
				else if(globalArray.containsKey(idToken)){
					addToken = "global[";
					flag = false;
				}
				localQueue.add(addToken);
				if(expression()) {
					if(inputTokens.firstElement() == TokenNames.right_bracket) {
						currentToken = inputTokens.remove(0);
						evaluateExpression();
						sayLine("tokens = " + tokens);
						
						if(flag){
							tokens.add("local[" + localCount + "] = " + tokens.remove(tokens.size()-1) + "+" + localArray.get(idToken).offset + ";");
							localCount++;
						}
						else{
							tokens.add("local[" + localCount + "] = " + tokens.remove(tokens.size()-1) + "+" + globalArray.get(idToken).offset + ";" );
							localCount++;
						}
						
						tokens.add("local[" + Integer.toString(localCount-1) + "]");
						
						expStr.clear();
						mergeToLocal();
						
						//printArray(localQueue);
						
						newToken = tokenList.remove(0);
						localQueue.add(newToken);
						if(inputTokens.firstElement() == TokenNames.equal_sign) {
							currentToken = inputTokens.remove(0);
							newToken = tokenList.remove(0);
							localQueue.add(newToken);
							if(expression()) {
								if(inputTokens.firstElement() == TokenNames.semicolon) {
									currentToken = inputTokens.remove(0);
									evaluateExpression();
									expStr.clear();
									mergeToLocal();
									//printArray(localQueue);
									newToken = tokenList.remove(0);
									localQueue.add(newToken);
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}
		return false;
	}
	
	/**
	 * <func call> --> left_parenthesis <expr list> right_parenthesis semicolon 
	 * @return a boolean
	 */
	private boolean func_call() {
		if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);	// ID
			//sayLine("Inside func call. Adding this token to localQueue"  + newToken);
			localQueue.add(newToken);
			newToken = tokenList.remove(0);	// (
			//sayLine("2 Inside func call. Adding this token to localQueue"  + newToken);
			localQueue.add(newToken);
			
			if(expr_list()) {
				if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
					currentToken = inputTokens.remove(0);
					
					evaluateExpression();
					//tokens.add("local[" + localCount + "] = " + tokens.remove(tokens.size()-1) + ";");
					//localCount++;
					expStr.clear();
					mergeToLocal();
					
					newToken = tokenList.remove(0);
					localQueue.add(newToken);
					if(inputTokens.firstElement() == TokenNames.semicolon) {
						currentToken = inputTokens.remove(0);
						
						//evaluateExpression();
						//mergeToLocal();
						//printArray(localQueue);

						newToken = tokenList.remove(0);
						localQueue.add(newToken);
						return true;
					}
				}
			}
		}		
		return false;
	}
	
	/**
	 * <expr list> --> empty | <non-empty expr list> 
	 * @return a boolean
	 */
	private boolean expr_list() {
		if(non_empty_expr_list()) {
			return true;
		}
		return true;
	}
	
	/**
	 * <non-empty expr list> --> <expression> <non-empty expr list prime>
	 * @return a boolean
	 */
	private boolean non_empty_expr_list() {
		if(expression()) {
			return non_empty_expr_list_prime();
		}
		return false;
	}
	
	/**
	 * <non-empty expr list prime> --> comma <expression> <non-empty expr list prime> | empty
	 * @return a boolean
	 */
	private boolean non_empty_expr_list_prime() {
		if(inputTokens.firstElement() == TokenNames.comma) {
			currentToken = inputTokens.remove(0);
			
			newToken = tokenList.remove(0);
			evaluateExpression();
			
			
			if(expression()) {
				return non_empty_expr_list_prime();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * <if statement> --> if left_parenthesis <condition expression> right_parenthesis <block statements> 
	 * @return a boolean
	 */
	private boolean if_statement() {
		if(inputTokens.firstElement() == TokenNames.If) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				localQueue.add(newToken);
				if(condition_expression()) {
					if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
						currentToken = inputTokens.remove(0);
						evaluateExpression();
						
						conditionTokens.add(tokens.remove(tokens.size()-1));
						
						if(conditionTokens.size()==1){
							//tokens.add("local[" + localCount + "] = " + conditionTokens.remove(0) + ";");
						}
						else if(conditionTokens.size()==3){
							//tokens.add("local[" + localCount + "] = " + conditionTokens.remove(0) + conditionTokens.remove(0) + conditionTokens.remove(0) + ";" );
						}
						else if(conditionTokens.size()==5){
							String val = conditionTokens.remove(2);
							val += conditionTokens.remove(2);
							val += conditionTokens.remove(2);
							tokens.add("local[" + localCount + "] = " + val + ";");
							localCount++;
							conditionTokens.add("local["+Integer.toString(localCount-1)+"]");
						}
						expStr.clear();
						mergeLocalForCond();
						//printArray(localQueue);
						mergeConditionsToLocal();
						//printArray(localQueue);
						newToken = tokenList.remove(0);
						localQueue.add(newToken);
						
						Labels marker = new Labels(goToCount);
						goToMarkers.push(marker);
						goToCount += 2;
						
						String label = "goto l" + goToMarkers.peek().start + ";";
						
						localQueue.add(label);
						
						label = "goto l" + goToMarkers.peek().end + ";";
						
						localQueue.add(label);
						localQueue.add("l" + goToMarkers.peek().start + ":");
						
						boolean res = block_statements();
						
						label = "l" + goToMarkers.pop().end + ":";
						localQueue.add(label);
						
						return res;
						
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * <condition expression> -->  <condition> <condition expression Z>
	 * @return a boolean
	 */
	private boolean condition_expression() {
		if(condition()) {
			return condition_expression_Z();
		}
		return false;
	}
	
	/**
	 * <condition expression Z> --> <condition op> <condition> | empty
	 * @return a boolean
	 */
	private boolean condition_expression_Z() {
		if(condition_op()) {
			return condition();
		}
		return true;
	}
	
	/**
	 * <condition op> --> double_end_sign | double_or_sign 
	 * @return a boolean
	 */
	private boolean condition_op() {
		if(inputTokens.firstElement() == TokenNames.double_and_sign || inputTokens.firstElement() == TokenNames.double_or_sign) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			evaluateExpression();
			conditionTokens.add(tokens.remove(tokens.size()-1));
			String str="";
			for(String obj : conditionTokens)
				str += obj;
			tokens.add("local["+localCount+"] = " + str + ";" );
			localCount++;
			conditionTokens.clear();
			conditionTokens.add("local["+(Integer.toString(localCount-1))+"]");
			conditionTokens.add(newToken);
			expStr.clear();
			return true;
		}
		return false;
	}
	
	/**
	 * <condition> --> <expression> <comparison op> <expression> 
	 * @return a boolean
	 */
	private boolean condition() {
		if(expression()) {
			if(comparison_op()) {
				return expression();
			}
		}
		return false;
	}
	
	/**
	 * <comparison op> --> == | != | > | >= | < | <=
	 * @return a boolean
	 */
	private boolean comparison_op() {
		if(inputTokens.firstElement() == TokenNames.doubleEqualSign || inputTokens.firstElement() == TokenNames.notEqualSign ||
				inputTokens.firstElement() == TokenNames.greaterThenSign || inputTokens.firstElement() == TokenNames.greaterThenOrEqualSign ||
				inputTokens.firstElement() == TokenNames.lessThenSign || inputTokens.firstElement() == TokenNames.lessThenOrEqualSign) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			evaluateExpression();
			conditionTokens.add(tokens.remove(tokens.size()-1));
			conditionTokens.add(newToken);
			expStr.clear();
			return true;
		}
		return false;
	}
	
	/**
	 * <while statement> --> while left_parenthesis <condition expression> right_parenthesis <block statements> 
	 * @return
	 */
	private boolean while_statement() {
		if(inputTokens.firstElement() == TokenNames.While) {
			currentToken = inputTokens.remove(0);
			WhileLabels mark = new WhileLabels(goToCount);
			goToMarkersWhile.push(mark);
			goToCount += 3;
			String l = "w" + goToMarkersWhile.peek().start + ":;";
			localQueue.add(l);
			
			newToken = tokenList.remove(0);
			localQueue.add("if");
			if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				localQueue.add(newToken);
				if(condition_expression()) {
					if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
						currentToken = inputTokens.remove(0);
						evaluateExpression();
						
						conditionTokens.add(tokens.remove(tokens.size()-1));
						
						if(conditionTokens.size()==1){
							//tokens.add("local[" + localCount + "] = " + conditionTokens.remove(0) + ";");
						}
						else if(conditionTokens.size()==3){
							//tokens.add("local[" + localCount + "] = " + conditionTokens.remove(0) + conditionTokens.remove(0) + conditionTokens.remove(0) + ";" );
						}
						else if(conditionTokens.size()==5){
							String val = conditionTokens.remove(2);
							val += conditionTokens.remove(2);
							val += conditionTokens.remove(2);
							tokens.add("local[" + localCount + "] = " + val + ";");
							localCount++;
							conditionTokens.add("local["+Integer.toString(localCount-1)+"]");
						}
						expStr.clear();
						mergeLocalForCond();
						//printArray(localQueue);
						mergeConditionsToLocal();
						//printArray(localQueue);
						newToken = tokenList.remove(0);
						localQueue.add(newToken);
						
						l = "goto w" + goToMarkersWhile.peek().mid + ";";
						localQueue.add(l);
						
						l = "goto w" + goToMarkersWhile.peek().end + ";";
						localQueue.add(l);
						
						localQueue.add("w" + goToMarkersWhile.peek().mid + ";");
						
						boolean result = block_statements();
						
						localQueue.add("goto w" + goToMarkersWhile.peek().start + ";");
						
						l = "w" + goToMarkersWhile.pop().end + ":;";
						localQueue.add(l);
						
						return result;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * <return statement> --> return <return statement Z>
	 * @return a boolean
	 */
	private boolean return_statement() {
		if(inputTokens.firstElement() == TokenNames.Return) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			
			// printArray(localQueue);
			return return_statement_Z();
		}
		return false;
	}
	
	/**
	 * <return statement Z> --> <expression> semicolon | semicolon 
	 * @return a boolean
	 */
	private boolean return_statement_Z() {
		
		//sayLine("Hi. Printing nextToken here" + tokenList.firstElement());
		
		if(expression()) {
			if(inputTokens.firstElement() == TokenNames.semicolon) {
				currentToken = inputTokens.remove(0);
				evaluateExpression();
				expStr.clear();
				sayLine("Return top of stack = tempList " + tempList);
				mergeToLocal();
				newToken = tokenList.remove(0);
				localQueue.add(newToken);
				//printArray(localQueue);
				return true;
			}
			return false;
		}
		if(inputTokens.firstElement() == TokenNames.semicolon) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			localQueue.add(newToken);
			return true;
		}
		return false;
	}
	
	/**
	 * <break statement> ---> break semicolon
	 * @return a boolean
	 */
	private boolean break_statement() {
		if(inputTokens.firstElement() == TokenNames.Break) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			//localQueue.add(newToken);
			if(inputTokens.firstElement() == TokenNames.semicolon) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				//localQueue.add(newToken);
				
				String label = "goto w" + goToMarkersWhile.peek().end + ";";
				localQueue.add(label);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <continue statement> ---> continue semicolon
	 * @return a boolean
	 */
	private boolean continue_statement() {
		if(inputTokens.firstElement() == TokenNames.Continue) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			//localQueue.add(newToken);
			if(inputTokens.firstElement() == TokenNames.semicolon) {
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				//localQueue.add(newToken);
				
				String label = "goto w" + goToMarkersWhile.peek().start + ";";
				localQueue.add(label);
				
				return true;
				
				
			}
		}
		return false;
	}
	
	/**
	 * <expression> --> <term> <expression prime>
	 * @return a boolean
	 */
	private boolean expression() {
		
		storeLatestTokens();
		if(term()) {
			return expression_prime();
		}
		return false;
	}
	
	/**
	 * <expression prime> --> <addop> <term> <expression prime> | empty
	 * @return
	 */
	private boolean expression_prime() {
		if(addop()) {
			if(term()) {
				return expression_prime();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * <addop> --> plus_sign | minus_sign 
	 * @return a boolean
	 */
	private boolean addop() {
		if(inputTokens.firstElement() == TokenNames.plus_sign || inputTokens.firstElement() == TokenNames.minus_sign) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			expStr.add(newToken);
			return true;
		}
		return false;
	}
	
	/**
	 * <term> --> <factor> <term prime>
	 * @return a boolean
	 */
	private boolean term() {
		if(factor()) {
			return term_prime();
		}
		return false;
	}
	
	/**
	 * <term prime> --> <mulop> <factor> <term prime> | empty
	 * @return
	 */
	private boolean term_prime() {
		if(mulop()) {
			if(factor()) {
				return term_prime();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * <mulop> --> star_sign | forward_slash 
	 * @return a boolean
	 */
	private boolean mulop() {
		if(inputTokens.firstElement() == TokenNames.star_sign || inputTokens.firstElement() == TokenNames.forward_slash) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			expStr.add(newToken);
			return true;
		}
		return false;
	}
	
	/**
	 * <factor> --> ID <factor Z> | NUMBER | minus_sign NUMBER | left_parenthesis <expression>right_parenthesis 
	 * @return
	 */
	private boolean factor() {
		if(inputTokens.firstElement() == TokenNames.ID) {
			currentToken = inputTokens.remove(0);
			
			if(localMap.containsKey(tokenList.firstElement())){
				expStr.add(localMap.get(tokenList.firstElement()));
				newToken = tokenList.remove(0);
			}
			else if(localArray.containsKey(tokenList.firstElement())){
				
			}
			else if(globalArray.containsKey(tokenList.firstElement())){
				
			}
			else if(globalMap.containsKey(tokenList.firstElement())){
				expStr.add(globalMap.get(tokenList.firstElement()));
				newToken = tokenList.remove(0);
			}
			else
				System.out.println("Undeclared variable" + tokenList.firstElement());
			return factor_Z();
		}
		// NUMBER
		if(inputTokens.firstElement() == TokenNames.NUMBER) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			expStr.add(newToken);
			return true;
		}
		
		// minus_sign NUMBER
		if(inputTokens.firstElement() == TokenNames.minus_sign) {
			currentToken = inputTokens.remove(0);
			newToken = tokenList.remove(0);
			String str = newToken;
			if(inputTokens.firstElement() == TokenNames.NUMBER) {
				currentToken = inputTokens.remove(0);
				newToken = str+tokenList.remove(0);
				expStr.add(newToken);
				return true;
			}
			return false;
		}
		
		// left_parenthesis <expression>right_parenthesis
		if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
			if(currentToken == TokenNames.ID){
				bracketCount++;
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				expStr.add(newToken);
				if(expression()) {
					if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
						bracketCount--;
						currentToken = inputTokens.remove(0);
						newToken = tokenList.remove(0);
						expStr.add(newToken);
						return true;
					}
				}
				return false;
			}
			else{
				currentToken = inputTokens.remove(0);
				newToken = tokenList.remove(0);
				expStr.add(newToken);
				if(expression()) {
					if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
						currentToken = inputTokens.remove(0);
						newToken = tokenList.remove(0);
						expStr.add(newToken);
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}
	
	/**
	 * <factor Z> --> left_bracket <expression> right_bracket | left_parenthesis <expr list> right_parenthesis | empty
	 * @return
	 */
	private boolean factor_Z() {
		// left_bracket <expression> right_bracket
		if(inputTokens.firstElement() == TokenNames.left_bracket) {
			currentToken = inputTokens.remove(0);
			String idToken = tokenList.remove(0);	// ID
			newToken = tokenList.remove(0);			// [
			String addToken = "";
			boolean flag=true;;
			if(localArray.containsKey(idToken)){
				addToken = "local[";
				flag = true;
			}
			else if(globalArray.containsKey(idToken)){
				addToken = "local[";
				flag = false;
			}
			List<String> tempStrings = new ArrayList<String>();
			for(String s: expStr){
				tempStrings.add(s);
			}
			expStr.clear();
			if(expression()) {
				if(inputTokens.firstElement() == TokenNames.right_bracket) {
					//say("** expStr array = " + expStr + "tokens" + tokens +"\n");
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					evaluateExpression();
					//sayLine("Printing tokens:" + tokens);
					tokens.add("local[" + localCount + "] = " + tokens.remove(tokens.size()-1) + ";");
					localCount++;
					if(flag){
						tokens.add("local["+localCount+"] = " + addToken 
							+ Integer.toString(localCount-1)+"] + " + Integer.toString(localArray.get(idToken).offset) + ";");
						localCount++;
						tokens.add("local["+ localCount + "] = " + "local[local["+Integer.toString(localCount-1)+"]];" );
						localCount++;
					}
					else{
						tokens.add("local["+localCount+"] = " + addToken 
							+ Integer.toString(localCount-1)+"] + " + Integer.toString(globalArray.get(idToken).offset) + ";");
						localCount++;
						tokens.add("local["+ localCount + "] = " + "global[local["+Integer.toString(localCount-1)+"]];" );
						localCount++;
					}
					
					expStr.clear();
					for(String s:tempStrings)
						expStr.add(s);
					tempStrings.clear();
					expStr.add("local[" + Integer.toString(localCount-1)+"]");
					//expStr.add(addToken+Integer.toString(localCount-1)+"]");
					//printArray(localQueue);
					return true;
				}
			}
			return false;
		}
		// left_parenthesis <expr list> right_parenthesis
		if(inputTokens.firstElement() == TokenNames.left_parenthesis) {
			currentToken = inputTokens.remove(0);
			String idToken = tokenList.remove(0); // ID
			newToken = tokenList.remove(0);	// (
			List<String> tempStrings = new ArrayList<String>();
			for(String s: expStr){
				tempStrings.add(s);
			}
			expStr.clear();
			if(expr_list()) {
				if(inputTokens.firstElement() == TokenNames.right_parenthesis) {
					currentToken = inputTokens.remove(0);
					newToken = tokenList.remove(0);
					
					evaluateExpression();
					
					if(tokens.size()!=0){
						String temp = tokens.remove(tokens.size()-1);
						tokens.add("local[" + localCount + "] = " + temp + ";");
						localCount++;
						tokens.add("local["+ localCount + "] = " + idToken + "(local["+Integer.toString(localCount-1)+"]);" );
						tokens.add("local["+localCount+"]");
						localCount++;
					}
					else{
						tempStrings.add(idToken+"()");
					}
					expStr.clear();
					for(String s:tempStrings)
						expStr.add(s);
					tempStrings.clear();
					if(tokens.size()!=0){
						expStr.add(tokens.remove(tokens.size()-1));
					}

					return true;
				}
			}
			return false;
		}
		// empty
		return true;
	}
	

}