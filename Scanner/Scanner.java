/**
   Author: Gurunath Ashok Hanamsagar
   Unity ID: ghanams
   Assignment: CSC 512 - Project 1
 */

import java.io.*;
import java.lang.*;

public class Scanner{

	InputStream src; // Source file stream
	InputStreamReader stream; // Source file stream reader

	OutputStream dest; // Destination file stream
	PrintStream destStream; // Destination file stream reader

	/* Enum for various types of tokens */
	public enum TokenType {
		NONE, RESERVED, IDENTIFIER, SYMBOL, NUMBER, STRING, META
	}

	/* The token class for a token object */
	public static class Token{

		public TokenType tokenType;
		public String tokenName;

		public Token(TokenType type, String name){
			this.tokenType = type;
			this.tokenName = name;
		}

		public String getTokenName(){
			return this.tokenName;
		}

		public TokenType getTokenType(){
			return this.tokenType;
		}

	}

	Token latestToken; // The latest token which will be returned.
	int remainingBuffer; // Any left over characters from the previous token matching

	/* The list of all reserved words */	
	static String[] reservedList = { "int",  "void", "if", "while", "return", "read", "write", "print", " continue", " break", "binary", "decimal" }; 

	/* Constructor to initialize the scanner object */ 
	public Scanner(String fileName) throws FileNotFoundException{

		this.src = new FileInputStream(fileName);
		this.stream = new InputStreamReader(src);
		this.latestToken = new Token(Scanner.TokenType.NONE, "");
		this.remainingBuffer = -1;

		String file = fileName.substring(0, fileName.length()-2);
		file = file + "_gen.c";

		this.dest = new FileOutputStream(file);
		this.destStream = new PrintStream(dest);
	}

	/* Reading the file character by character */
	public int readCharacter(){
		int c = -1;

		try{
			c = stream.read();
		}
		catch (IOException i){

		}
		return c;
	}

	/* Checking if the token is a hash Define */
	public boolean checkMetaStatement(char c){

		if(c == '#'){

			this.latestToken.tokenName  += Character.toString(c);
			
			while( c != '\n'){
				c = (char) readCharacter();
				this.latestToken.tokenName  += Character.toString(c);
			}
			this.latestToken.tokenType = TokenType.META;
			return true;
		}
		return false;
		//System.out.println(this.latestToken.tokenName);
	}

	// Checking if the character is a '"'. If yes, find out till the next '"' and store as a token
	public boolean checkString(char c){
		if(c == '"'){
			
			this.latestToken.tokenName  += Character.toString(c);

			c = (char) readCharacter();
			this.latestToken.tokenName  += Character.toString(c);

			while( c != '"'){
				c = (char) readCharacter();
				this.latestToken.tokenName  += Character.toString(c);
			}
			this.latestToken.tokenType = TokenType.STRING;
			return true;
		}
		return false;
		//System.out.println( 1 + this.latestToken.tokenName);
	}


	/* Check for all the symbols. */
	public boolean checkSymbols(char c){
		
		int ch;

		if(isSpecialSymbol(c)){
			this.latestToken.tokenName  += Character.toString(c);

			//System.out.println(this.latestToken.tokenName);
			this.latestToken.tokenType = TokenType.SYMBOL;

			/* If '//' is enountered , the whole line (till \n) needs to be just copied. */
			if( c == '/' ){
				ch = readCharacter();
				
				if( ch == '/' ){
					
					while( ch != '\n' ){
						this.latestToken.tokenName  += Character.toString((char)ch);
						ch = readCharacter();						
					}
					this.latestToken.tokenName  += Character.toString((char)ch);
				}
				else
					this.remainingBuffer = ch ; // The character was read but it was not '/' hence, it is stored for further use
			}
			else if( c == '-' ){ 				// Checking for a negative number
				ch = readCharacter();
				if(isDigit((char)ch)){
					return checkNumber((char)ch);
				}
				else
					this.remainingBuffer = ch ;		
			}
			return true;
		}

		return false;
	}

	// Checking if the next digit is a number
	public boolean checkNumber(char c){
		
		int ch;

		if(isDigit(c)){
			this.latestToken.tokenName  += Character.toString(c);

			while( isDigit( (char)(ch = readCharacter())) ){
				this.latestToken.tokenName  += Character.toString((char)ch);
			}

			this.remainingBuffer = ch;	// First non digit character found. Hence stored for further

			//System.out.println(this.latestToken.tokenName);
			this.latestToken.tokenType = TokenType.NUMBER;
			return true;
		}

		return false;
	}

	// Checking for <, =, >, <=, == and >=. 
	public boolean checkAssignmentOp(char c){

		int ch;

		if(isAssignmentOp(c)){
			this.latestToken.tokenName  += Character.toString(c);
			
			if( c == '=' || c == '>' || c == '<' ){
				
				ch = readCharacter();
				if((char)ch == '=')
					this.latestToken.tokenName  += Character.toString((char)ch);
				
				else
					this.remainingBuffer = (char)ch;
			this.latestToken.tokenType = TokenType.SYMBOL;
			return true;
			}
		}

		return false;
	}


	// Checking for &&, ||
	public boolean checkBitWiseOp(char c){

		int ch;

		if(isAssignmentOp(c)){
			this.latestToken.tokenName  += Character.toString(c);
			
			ch = readCharacter();
			if( c == '&' || c == '|' ){
				
				if((char)ch == c)
					this.latestToken.tokenName  += Character.toString((char)ch);
				
				else
					this.remainingBuffer = (char)ch;
			//System.out.println( "1" + this.latestToken.tokenName);	
			this.latestToken.tokenType = TokenType.SYMBOL;
			return true;
			}
		}

		return false;
	}

	// Checking if the token is a reserved identifier
	public boolean checkReserved(){

		for(int i=0; i < reservedList.length; i++){
			if(reservedList[i].equals(this.latestToken.tokenName ))
				return true;
		}

		return false;

	}

	// Checking if the token is an identifier or a reserved literal
	public boolean checkIdentifierOrReserved(char c){

		int ch;

		if (isCharacter(c)){
			this.latestToken.tokenName  += Character.toString(c);
			
			ch = readCharacter();
			while( isDigit ((char)(ch)) || isCharacter( (char)(ch)))
				{
					this.latestToken.tokenName  += Character.toString((char)ch);
					ch = readCharacter();
				}//System.out.println( "2" + (char)ch);	
		
			this.remainingBuffer = ch;

			if(checkReserved())
				this.latestToken.tokenType = TokenType.RESERVED;	
			
			else
				this.latestToken.tokenType = TokenType.IDENTIFIER;	
			
			return true;
		}

		return false;
	}

	// Evaluating if the file has more tokens.
	public boolean hasMoreTokens(){

		// Already a token exists in lastToken
		if( this.latestToken.tokenType != TokenType.NONE )
			return true;

		// New token is to be obtained
		else{
			// Call to match new token
			startNewtoken();
				//System.out.println(this.latestToken.tokenName);
		}

		// Token Found
		if( this.latestToken.tokenType != TokenType.NONE )
			return true;

		// No token found. Hence, end of file reached.
		return false;
	}

	public void startNewtoken(){

		int c;
		
		/* Any remaining characters. Say extra character matched after checking >m */
		if(remainingBuffer == -1)
			c=readCharacter();
		
		else{
			c = remainingBuffer;
			remainingBuffer = -1;
		}

		this.latestToken.tokenName = ""; // Initializing the token to null.
		
		/*
			We check all the possible tokens one by one. only one will match because it returns in each of if conditions/
		*/

		while(isWhiteSpace((char)c)){
			c = readCharacter();
		}
		
		if (checkMetaStatement((char) c)){
			return;
		}

		else if (checkString((char) c)){
			return;
		}

		else if (checkSymbols((char) c)){
			return;
		}
		
		else if (checkAssignmentOp((char) c)){
			return;
		}

		else if (checkNumber((char) c)){
			return;
		}

		else if (checkIdentifierOrReserved((char) c)){
			return;
		}	

		// Only token that contains '!' is '!=' Hence, this is explicitly matched.
		else if ( (char)c == '!'){
			int ch = readCharacter();

			if( '=' == (char)ch){
				this.latestToken.tokenType = TokenType.SYMBOL ;
				this.latestToken.tokenName += "!=" ;
			}
		}
		else{
			/* End of file or illegal token reached. */
			//System.out.println("No token found.");
		}
	}

	// Returns the next token.
	public Token getNextToken(){

		Token tempToken = new Token(this.latestToken.tokenType, this.latestToken.tokenName);

		this.latestToken.tokenType = TokenType.NONE;

		return tempToken;
	}


	// Functions to check what token could a character be a part of.


	public boolean isCharacter(char ch){
		if( (ch >= 'a' && ch <= 'z') ||  (ch >= 'A' && ch <= 'Z') || ch == '_')
			return true;

		return false;
	}

	public boolean isDigit(char ch){
		if (ch >= '0' && ch <= '9')
			return true;

		return false;
	}

	public boolean isAssignmentOp(char ch){
		if (ch == '=' || ch == '<' || ch == '>' )
			return true;

		return false;
	}

	public boolean isBitWiseOp(char ch){
		if (ch == '&' || ch == '|' )
			return true;

		return false;
	}

	public boolean isSpecialSymbol(char ch){
		if (ch == '(' || ch == ')' || ch == '{' || ch == '}' || ch == '['|| ch == ']' || ch == ',' 
			|| ch == ';' || ch == '{' || ch == '+' || ch == '-' || ch == '*' || ch == '/')
			return true;

		return false;	
	}

	public boolean isWhiteSpace(char ch){
		if (ch == ' ' || ch == '\n')
			return true;
		return false;
	}

	public static void main(String[] args){
		try{
			Scanner sc = new Scanner(args[0]); // Scanner object with file name from command line
			
			while(sc.hasMoreTokens()){ // Loop will run till more tokens are present.
				
				// Token modified and written if matches condition
				if (sc.getNextToken().getTokenType() == TokenType.IDENTIFIER && !( "main".equals(sc.getNextToken().getTokenName())) )
					sc.destStream.print("cs512" + sc.getNextToken().getTokenName() + " "); 
               
               // Token simply written to the file.
        		else 
               		sc.destStream.print(sc.getNextToken().getTokenName() + " "); 				
			}
			sc.destStream.println();
		}
		catch (FileNotFoundException f){

		}
	}

}
