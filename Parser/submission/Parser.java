/***
	Author: Gurunath Ashok Hanamsagar
	Unity ID: ghanams
***/
import java.util.*;
import java.io.*;


public class Parser extends ScannerProj{
	static Map<String,List<String>> nonTerms = new HashMap<String, List<String>>();
	static Map<String, String> terms = new HashMap<String, String>();
	static Token nextToken;
	static ScannerProj scObj;
	static boolean tokenTaken;
	static int count;
	static int varCount;
	static int funcCount;
	static int statementCount;

	/* Function to create a hash of all the non terminals */
	public static void createHash(String fileName){		
		
		try{
			Scanner sc = new Scanner(new File(fileName));
			while(sc.hasNext()){
				
				String str = sc.nextLine();
				if(str.length() <= 5)
					continue;
				int i=0;
				String[] sides = str.split("`");
				sides[0].trim();
				sides[1].trim();
				String[] vals = sides[1].split("@");
				////System.out.println("\n" + sides[0] + ":");
				List<String> strList = new ArrayList<String>();
				for(String s: vals){
					s.trim();
					strList.add(s);
					//System.out.print(s);
				}
				
				nonTerms.put(sides[0], strList);
				//System.out.print(nonTerms.get(sides[0]));
			}
			
		}
		catch(Exception ex){
			
		}
		
	}
	
	public static void matchToken(Token token){
		
		if(token.tokenType == ScannerProj.TokenType.NONE && token.tokenName.equals("first_one")){
			nextToken = scObj.getNextToken();
			return;
		}
		
		if ( nextToken.tokenType == token.tokenType){
			//nextToken = scObj.getNextToken().getTokenName();

			if(scObj.hasMoreTokens())
				nextToken = scObj.getNextToken();
		}
		
		else{
			//System.out.println("Error");
		}
		
	}
	
	/* This function is called whenever an error/invalid condition is met.  */
	public static void printError(String str){
		
		System.out.println("Fail");
		//System.out.println("*********ERRORRR*********");
		//System.out.println(" Error with message : " + str + "\n\n");
		//stopExecution();
		System.exit(0);
		
	}
	
	/* This function loads a new token if new one is present. Else, loads the last unused token */
	public static void loadToken(){
		if(!tokenTaken){
			if(scObj.hasMoreTokens())
				nextToken = scObj.getNextToken();
			
			else{
				//System.out.println(" Out of tokens !! ");
				nextToken.tokenName="$";
			}
			if(nextToken.tokenType==ScannerProj.TokenType.META){
				tokenTaken=false;
				loadToken();
			}
			//System.out.println("in load token token = " + nextToken.tokenName + " type = " + nextToken.tokenType);
			tokenTaken = true;
		}
	}
	
	/* The first non terminal to be called. */
	public static void program(){
		
		//System.out.println("Entered Program " + nonTerms.get("typename"));
		loadToken();
		
		 //System.out.println(count++ + "token = " + nextToken.tokenName);
		if ( nonTerms.get("typename").contains(nextToken.tokenName) ){
			
			tokenTaken=false;
			loadToken();
			statementCount++;
			 //System.out.println(count++ +"token = " + nextToken.tokenName);
			if( nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				
				tokenTaken=false;
				
				loadToken();
				//System.out.println(count++ +"token = " + nextToken.tokenName);
				if(nonTerms.get("programz").contains(nextToken.tokenName))
					program_z();
				else
					printError("typename error");
			}
			else{
				printError("ID error from program");
			}
		}
		else{
			printError("typename Error from Program");
		}
		
	}
	
	public static void program_z(){
		
		loadToken();
		//System.out.println(count++ + " prog z token = " + nextToken.tokenName);
		//System.out.println(count++ +"token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals("(")){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ +"program z token = " + nextToken.tokenName);
			if(nonTerms.get("funcdeclz").contains(nextToken.tokenName)){
				func_decl_z();
				
				loadToken();
				//System.out.println(count++ +" token = " + nextToken.tokenName);
				if(nonTerms.get("funcz").contains(nextToken.tokenName)){
					func_z();
					
					loadToken();
					//System.out.println(count++ +"token = " + nextToken.tokenName);
					if(nonTerms.get("funclistz").contains(nextToken.tokenName)){
						func_list_z();
					}
					else{
						printError(" programz. funclist");
					}
				}
				else{
					printError(" programz. funcz");
				}
			}
			else{
				printError(" programz. funcdeclz");
			}
		}
		else if(nextToken.tokenName.equals(",")){
			tokenTaken=false;
			
			loadToken();
			if( nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;

				loadToken();
				if(nonTerms.get("idlistp").contains(nextToken.tokenName)){
					func_z();
					
					loadToken();
					if(nextToken.tokenName.equals(";")){
						tokenTaken=false;
						
						loadToken();
						if(nonTerms.get("datadecls").contains(nextToken.tokenName)){
							data_decls();
							
							loadToken();
							if(nonTerms.get("programz1").contains(nextToken.tokenName)){
								program_z1();
							}
							else{
								printError(" programz. programz1");
							}
						}
						else{
							printError("programz. datadecls");
						}
					}
					else{
						printError(" programz. semicolon");
					}
				}
				else{
					printError(" programz. idlistp");
				}
			}
			else{
				printError(" programz. id");
			}
		}
		else if(nextToken.tokenName.equals(";")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("datadecls").contains(nextToken.tokenName)){
				data_decls();
				
				loadToken();
				if(nonTerms.get("programz1").contains(nextToken.tokenName)){
					program_z1();
				}
				else{
					printError("Func programz. In ; In programz1");
				}
			}
			else{
				printError("Func programz. In ; In data_decls");
			}
		}
		else if(nextToken.tokenName.equals("[")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("expression").contains(nextToken.tokenName)
			|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
			|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				expression();
				
				loadToken();
				if(nextToken.tokenName.equals("]")){
					tokenTaken=false;
					
					loadToken();
					if(nonTerms.get("programz7").contains(nextToken.tokenName)){
						program_z7();
					}
					else{
						printError("In programz. In [. In programz7");
					}
				}
				else{
					printError("In programz. In [. In ]");
				}
			}
			else{
				printError("In programz. In [. In expression");
			}
			
		}else{
			printError(" Error at last else of programz");
		}
	}
	
	public static void program_z7(){
		
		loadToken();
		//System.out.println(count++ + " prog z7 token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals(";")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("datadecls").contains(nextToken.tokenName)){
				data_decls();
				
				loadToken();
				if(nonTerms.get("programz1").contains(nextToken.tokenName)){
					program_z1();
				}
				else{
					printError("Func programz7. In ; In programz1");
				}
			}
		}
		else if(nextToken.tokenName.equals(",")){
			tokenTaken=false;
			
			loadToken();
			if( nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;

				loadToken();
				if(nonTerms.get("idlistp").contains(nextToken.tokenName)){
					func_z();
					
					loadToken();
					if(nextToken.tokenName.equals(";")){
						tokenTaken=false;
						
						loadToken();
						if(nonTerms.get("datadecls").contains(nextToken.tokenName)){
							data_decls();
							
							loadToken();
							if(nonTerms.get("programz1").contains(nextToken.tokenName)){
								program_z1();
							}
							else{
								printError(" programz. programz1");
							}
						}
						else{
							printError("programz. datadecls");
						}
					}
					else{
						printError(" programz. semicolon");
					}
				}
				else{
					printError(" programz. idlistp");
				}
			}
		}
		
	}
	
	public static void program_z1(){
		
		loadToken();
		//System.out.println(count++ + " prog z1 token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals("$")){
			return;
		}
		else if(nonTerms.get("typename").contains(nextToken.tokenName)){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				if(nextToken.tokenName.equals("(")){
					tokenTaken=false;
					
					loadToken();
					if(nonTerms.get("funcdeclz").contains(nextToken.tokenName)){
						func_decl_z();
						
						loadToken();
						if(nonTerms.get("funcz").contains(nextToken.tokenName)){
							func_z();
							
							loadToken();
							if(nonTerms.get("funclist").contains(nextToken.tokenName)){
								func_list();
								
								return;
							}
							else{
								printError(" Error from program_z1 ");
							}
						}
						else{
							printError(" Error from program_z1 ");
						}
					}
					else{
						printError(" Error from program_z1 ");
					}
				}
				else{
					printError(" Error from program_z1 ");
				}
			}
			else{
				printError(" Error from program_z1 ");
			}
		}
		else{
			printError(" Error from program_z1 ");
		}
		
	}
	
	public static void func_list(){
		
		loadToken();
		//System.out.println(count++ + " func list 1 token=" + nextToken.tokenName);
		if(nonTerms.get("func").contains(nextToken.tokenName)){
			func();
			
			loadToken();
			//System.out.println(count++ + " func list 2 token=" + nextToken.tokenName);
			if(nonTerms.get("funclistz").contains(nextToken.tokenName)){
				func_list_z();
				
				return;
			}
			else{
				printError("Error in func_list");
			}
		}
		else{
			printError("Error in func_list");
		}
	}
	
	public static void func_list_z(){
		
		loadToken();
		if(nextToken.tokenName.equals("$")){
			return;
		}
		else if(nonTerms.get("funclist").contains(nextToken.tokenName)){
			func_list();
			
			return;
		}
		else{
			printError("Error func list z");
		}
		
	}
	
	public static void func(){
		
		loadToken();
		if(nonTerms.get("funcdecl").contains(nextToken.tokenName)){
			func_decl();
			
			loadToken();
			if(nonTerms.get("funcz").contains(nextToken.tokenName)){
				func_z();
				
				return;
			}
			else{
				printError(" error func");
			}
		}
		else{
			printError(" error func");
		}
	}
	
	public static void func_z(){
		
		loadToken();
		//System.out.println(count++ + "funcz" + nextToken.tokenName);
		if(nextToken.tokenName.equals(";")){
			tokenTaken=false;
			return;
		}
		else if(nextToken.tokenName.equals("{")){
			tokenTaken=false;
			funcCount++;
			
			loadToken();
			//System.out.println(count++ + "funcz ()" + nextToken.tokenName);
			if(nonTerms.get("funcz6").contains(nextToken.tokenName)
			|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				func_z6();	
			
				return;	
			}
			else{
				printError(" error func z");
			}
		}
		else
			printError(" Error in func_z");
	}
	
	public static void func_z6(){
		
		loadToken();
		//System.out.println(count++ + "funcz6" + nextToken.tokenName);
		if( nonTerms.get("datadecls").contains(nextToken.tokenName)){
			data_decls();
			
			loadToken();
			////System.out.println(count++ + "funcz6 2" + nextToken.tokenName);
			if(nonTerms.get("funcz7").contains(nextToken.tokenName)
			|| nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				func_z7();
			}
			else
				printError("Error is func z6. Call to z7");
		}
		else if( nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER 
			||  nonTerms.get("statements").contains(nextToken.tokenName) ){
			//tokenTaken=false;
			statements();
			
			loadToken();
			if(nextToken.tokenName.equals("}")){
				tokenTaken=false;
				return;
			}
			else
				printError(" Error in func_z6 1");
		}	
		else if(nextToken.tokenName.equals("}"))
			return;
		else
			printError(" Error in func_z6 2");
			
	}
	public static void func_z7(){
		
		loadToken();
		//System.out.println(count++ + " func z7 token = " + nextToken.tokenName);
		if( nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER 
			||  nonTerms.get("statements").contains(nextToken.tokenName) ){
			statements();
			
			loadToken();
			//System.out.println(count++ + " func z7 2 token = " + nextToken.tokenName);
			if(nextToken.tokenName.equals("}")){
				tokenTaken=false;
				return;
			}
			else
				printError("func z7 1");
		}
		else if(nextToken.tokenName.equals("}")){
			tokenTaken=false;
			return;
		}
		else
			printError(" Error in func_z");
			
	}
	
	public static void func_decl(){
		
		loadToken();
		//System.out.println(count++ + "funcdecl" + nextToken.tokenName);
		if( nonTerms.get("typename").contains(nextToken.tokenName) ){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ + "token = " + nextToken.tokenName);
			if(nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				//System.out.println(count++ + "token = " + nextToken.tokenName);
				if(nextToken.tokenName.equals("(")){
					tokenTaken=false;
					
					loadToken();
					if(nonTerms.get("funcdeclz").contains(nextToken.tokenName)){
						func_decl_z();
						
						return;
					}
					else{
						printError("func decl error 1");
					}
				}
				else{
					printError("func decl error 2");
				}
			}
			else{
				printError("func decl error 3");
			}
		}
		else{
			printError("func decl error 4");
		}
	}
	
	public static void func_decl_z(){
		
		loadToken();
		//System.out.println(count++ + " /*/* token in funcdeclz = " + nextToken.tokenName);
		if(nextToken.tokenName.equals(")")){
			tokenTaken=false;
			
			return;
		}
		else if(nextToken.tokenName.equals("void")){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ + " token in 1 funcdeclz = " + nextToken.tokenName );
			if(nonTerms.get("funcdeclz2").contains(nextToken.tokenName)){

				func_decl_z2();
				return;
			}
			else{
				printError("func decl z");
			}
		}
		else if(nextToken.tokenName.equals("int")){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ +"fdz int token = " + nextToken.tokenName);
			if(nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				//System.out.println(count++ +"|||fdz int token = " + nextToken.tokenName);
				if(nonTerms.get("funcdeclz3").contains(nextToken.tokenName)){
					//System.out.println("weeeee");
					func_decl_z3();
					
					return;
				}
				else{
					printError("func decl z");
				}
			}
			else{
				printError("func decl z");
			}
		}
		else if(nextToken.tokenName.equals("binary")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				if(nonTerms.get("funcdeclz3").contains(nextToken.tokenName)){
					func_decl_z3();
					
					return;
				}
				else{
					printError("func decl z");
				}
			}
			else{
				printError("func decl z");
			}
		}
		else if(nextToken.tokenName.equals("decimal")){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ +"fdz dec token = " + nextToken.tokenName);
			if(nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				//System.out.println(count++ +"fdz dec token = " + nextToken.tokenName);
				if(nonTerms.get("funcdeclz3").contains(nextToken.tokenName)){
					func_decl_z3();
					
					return;
				}
				else{
					printError("func decl z");
				}
			}
			else{
				printError("func decl z");
			}
		}
		else{
			printError("func decl z");
		}
	}
		
	public static void func_decl_z2(){
		
		loadToken();
		//System.out.println(count++ + " in func decl z2 token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals(")")){
			tokenTaken=false;
			
			return;
		}
		else if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("funcdeclz6").contains(nextToken.tokenName)){
				func_decl_z6();
				
				return;
			}
			else{
				printError("func decl z 2");
			}
		}
		else{
			printError("func decl z2");
		}
	}
	public static void func_decl_z6(){
		
		loadToken();
		if(nextToken.tokenName.equals(")")){
			tokenTaken=false;
			
			return;
		}
		else if(nonTerms.get("non-emptylistp").contains(nextToken.tokenName)){
			non_empty_list_p();
			
			loadToken();
			if(nextToken.tokenName.equals(")")){
				tokenTaken=false;
				
				return;
			}
			else{
				printError("func decl z6");
			}
		}
		else{
			printError("func decl z6");
		}
	}
	public static void func_decl_z3(){
		
		loadToken();
		//System.out.println(count++ +"fdz3 token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals(")")){
			tokenTaken=false;
			
			return;
		}
		else if(nextToken.tokenName.equals(",")){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ +"fdz3 token = " + nextToken.tokenName);
			if(nonTerms.get("typename").contains(nextToken.tokenName)){
				tokenTaken=false;
				
				loadToken();
				//System.out.println(count++ +"fdz3 token = " + nextToken.tokenName);
				if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
					tokenTaken=false;
					
					loadToken();
					//System.out.println(count++ +"/*/*/*fdz3 token = " + nextToken.tokenName);
					if(nonTerms.get("non-emptylistp").contains(nextToken.tokenName)){
						non_empty_list_p();
						
						loadToken();
						if(nextToken.tokenName.equals(")")){
							tokenTaken=false;
							
							return;	
						}
						else{
							printError("func decl z 3");
						}
					}
					else{
						printError("func decl z 3");
					}
				}
				else{
					printError("func decl z 3");
				}
			}
			else{
				printError("func decl z 3");
			}
		}
		else{
			printError("func decl z 3");
		}
	}
	
	public static void type_name(){
		
		loadToken();
		if(nextToken.tokenName.equals("int") || nextToken.tokenName.equals("void") 
			|| nextToken.tokenName.equals("binary") || nextToken.tokenName.equals("decimal")){
				tokenTaken=false;
				
				return;
			}
		else{
			printError("Error in type name");
		}
	}
	
	public static void non_empty_list(){
		
		loadToken();
		//System.out.println(count++ +"nelptoken = " + nextToken.tokenName);
		if(nonTerms.get("typename").contains(nextToken.tokenName)){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ +"nelptoken = " + nextToken.tokenName);
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				//System.out.println(count++ +"nelptoken = " + nextToken.tokenName);
				if(nonTerms.get("non-emptylistz").contains(nextToken.tokenName)){
					non_empty_list_z();
					
					return;
				}
				else{
					printError("non empty list");
				}
			}
			else{
				printError("non empty list");
			}
		}
		else{
			printError("non empty list");
		}
	}
	
	public static void non_empty_list_z(){
		
		loadToken();
		if(nonTerms.get("non-emptylistp").contains(nextToken.tokenName)){
			return;
		}
		else if(nextToken.tokenName.equals(")")){
			return;
		}
		else{
			printError("non empty list z");
		}
	}
	
	public static void non_empty_list_p(){
		
		loadToken();
		//System.out.println(count++ +"nelptoken = " + nextToken.tokenName);
		if(nextToken.tokenName.equals(",")){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ +"nelptoken = " + nextToken.tokenName);
			if(nonTerms.get("typename").contains(nextToken.tokenName)){
				tokenTaken=false;
				
				loadToken();
				//System.out.println(count++ +"nelptoken = " + nextToken.tokenName);
				if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
					tokenTaken=false;
					
					loadToken();
					//System.out.println(count++ +"nelptoken = " + nextToken.tokenName);
					if(nonTerms.get("non-emptylist").contains(nextToken.tokenName)){
						non_empty_list_p();
						
						return;
					}
					else{
						printError("non empty list p");
					}
				}
				else{
					printError("non empty list p");
				}
			}
			else{
				printError("non empty list p");
			}
		}
		else if(nonTerms.get("non-emptylistp").contains(nextToken.tokenName)){
			return;
		}
		else{
			printError("non empty list p");
		}
	}
	
	public static void data_decls(){
		
		loadToken();
		//System.out.println(count++ + " data decls token = " + nextToken.tokenName);
		if(nonTerms.get("typename").contains(nextToken.tokenName)){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				
				id_list();
				loadToken();
				//System.out.println(count++ + " data decls ; token = " + nextToken.tokenName);
				if(nextToken.tokenName.equals(";")){
					tokenTaken=false;
					
					loadToken();
					//System.out.println(count++ + " data decls 3 ; token = " + nextToken.tokenName);
					if(nonTerms.get("datadeclsz").contains(nextToken.tokenName)
					|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
					|| nonTerms.get("funcz7").contains(nextToken.tokenName)){
						data_decls_z();
						
						return;
					}
					else{
						printError("data decls 1");
					}
				}
				else{
					printError("data decls 2");
				}
			}
			else{
				printError("data decls 3");
			}
		}
		else{
			printError("data decls 4");
		}
	}
	
	public static void data_decls_z(){
		
		loadToken();
		if(nonTerms.get("typename").contains(nextToken.tokenName)){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				
				id_list();
				loadToken();
				if(nextToken.tokenName.equals(";")){
					tokenTaken=false;
					
					loadToken();
					if(nonTerms.get("datadeclsz").contains(nextToken.tokenName)
					|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
					|| nonTerms.get("funcz7").contains(nextToken.tokenName)){
						data_decls_z();
						
						return;
					}
					else{
						printError("data decls z 1");
					}
				}
				else{
					printError("data decls z-2");
				}
			}
			else{
				printError("data decls z-3");
			}
		}
		else if(nonTerms.get("funcz7").contains(nextToken.tokenName)
		|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			return;
		}
		else{
			printError("data decls z-4");
		}
	}
	
	public static void id_list(){
		
		loadToken();
		//System.out.println(count++ + " id list token = " + nextToken.tokenName);
		if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			
			id();
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER 
			|| nonTerms.get("idlistz").contains(nextToken.tokenName)){
				
				id_list_z();
			}
			else{
				printError("id list");
			}
		}
		else{
			printError("id list");
		}
	}
	
	public static void id_list_z(){
		
		loadToken();
		//System.out.println(count++ + " id list z token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals(",")){
			id_list_p();
			
			return;
		}
		else if(nextToken.tokenName.equals(";")){
			return;
		}
		else
			printError("Error in id_list_z");
	}
	
	public static void id_list_p(){
		
		loadToken();
		//System.out.println(count++ + " id list p token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals(",")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				id();
				
				loadToken();
				if(nextToken.tokenName.equals(",")
				|| nextToken.tokenName.equals(";")){
					id_list_p_z();
				}
				else{
					printError("id list p");
				}
			}
			else{
				printError("id list p");
			}
		}
		else{
			printError("id list p");
		}
	}
	
	public static void id_list_p_z(){
		
		loadToken();
		//System.out.println(count++ + " id list p z token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals(",")){
			id_list_p();
			
			return;
		}
		else if(nextToken.tokenName.equals(";"))
			return;
		
		else{
			printError("id list p z");
		}
	}
	
	public static void id(){
		
		loadToken();
		//System.out.println(count++ + " id token = " + nextToken.tokenName);
		if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ + " id 2 token = " + nextToken.tokenName + "bool=" + nonTerms.get("idz").contains(nextToken.tokenName));
			if(nonTerms.get("idz").contains(nextToken.tokenName)){
				
				id_z();
				return;
			}
			else{
				printError("id");
			}
		}
		else{
			printError("id");
		}
	}
	
	public static void id_z(){
		
		loadToken();
		//System.out.println(count++ + " id z token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals("[")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
			|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				expression();
				
				loadToken();
				if(nextToken.tokenName.equals("]")){
					tokenTaken=false;
					
					return;
				}
				else{
					printError("id z");
				}
			}
			else{
				printError("id z");
			}
		}
		else if(nonTerms.get("idz").contains(nextToken.tokenName)){
			
			return;
		}
		else{
			printError("Error in id z");
		}
	}
	
	public static void block_statements(){
		
		loadToken();
		if(nextToken.tokenName.equals("{")){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ + "in block_statements token = " + nextToken.tokenName);
			if(nonTerms.get("blockstatementsz").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				block_statements_z();
				
				return;
			}
			else{
				printError("block statements");
			}
		}
		else{
			printError("Error from block statements");
		}
		
	}
	
	public static void block_statements_z(){
		
		loadToken();
		if(nextToken.tokenName.equals("}")){
			tokenTaken=false;
			
			return;
		}
		else if(nonTerms.get("statements").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			statements();
			
			loadToken();
			if(nextToken.tokenName.equals("}")){
				tokenTaken=false;
				
				return;
			}
			else{
				printError("block statements z");
			}
		}
		else
			printError("block statements z");
	}
	
	public static void statements(){
		
		loadToken();
		//System.out.println(count++ +"statements token = " + nextToken.tokenName);
		if(nonTerms.get("statement").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			
			statement();
			
			loadToken();
			if(nonTerms.get("statementsz").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				statements_z();
				
				return;
			}
			else{
				printError("statements ");
			}
		}
		else if(nextToken.tokenName.equals("}")){
			return;
		}
		else{
			printError("statements error");
		}
	}
	
	public static void statements_z(){
		
		loadToken();
		if(nonTerms.get("statement").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			
			statement();
			
			loadToken();
			if(nonTerms.get("statementsz").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				statements_z();
				
				return;
			}
			else{
				printError("statements z-1");
			}
		}
		else if(nonTerms.get("statements").contains(nextToken.tokenName))
			return;
			
		else
			printError("Error in statements_z-2");
	}
	
	public static void statement(){
		
		loadToken();
		//System.out.println(count++ +"statement token = " + nextToken.tokenName);
		if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ +"statement 2 token = " + nextToken.tokenName);
			if(nonTerms.get("statementz").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				statement_z();
				
				return;
			}
			else{
				printError("statement");
			}
		}
		else if(nextToken.tokenName.equals("if")){
			
			if_statement();
			return;
		}
		else if(nextToken.tokenName.equals("while")){
			
			while_statement();
			return;
		}
		else if(nextToken.tokenName.equals("break")){
			
			break_statement();
			return;
		}
		else if(nextToken.tokenName.equals("return")){
			
			return_statement();
			return;
		}
		else if(nextToken.tokenName.equals("continue")){
			
			continue_statement();
			return;
		}
		else if(nextToken.tokenName.equals("read")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenName.equals("(")){
				tokenTaken=false;
				
				loadToken();
				if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
					tokenTaken=false;
					
					loadToken();
					if(nextToken.tokenName.equals(")")){
						tokenTaken=false;
						
						loadToken();
						if(nextToken.tokenName.equals(";")){
							tokenTaken=false;
							
							return;
						}
						else{
							printError("statement");
						}
					}
					else{
							printError("statement");
						}
				}
				else{
							printError("statement");
						}
			}
			else{
							printError("statement");
						}
		}
		else if(nextToken.tokenName.equals("write")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenName.equals("(")){
				tokenTaken=false;
				
				loadToken();
				if(nonTerms.get("expression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
					expression();
					
					loadToken();
					if(nextToken.tokenName.equals(")")){
						tokenTaken=false;
						
						loadToken();
						if(nextToken.tokenName.equals(";")){
							tokenTaken=false;
							statementCount++;
							
							return;
						}
						else{
							printError("statement");
						}
					}
					else{
							printError("statement");
						}
				}
				else{
							printError("statement");
						}
			}
			else{
							printError("statement");
						}
		}
		else if(nextToken.tokenName.equals("print")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenName.equals("(")){
				tokenTaken=false;
				
				loadToken();
				if(nextToken.tokenType==ScannerProj.TokenType.STRING){
					tokenTaken=false;
					
					loadToken();
					if(nextToken.tokenName.equals(")")){
						tokenTaken=false;
						
						loadToken();
						if(nextToken.tokenName.equals(";")){
							tokenTaken=false;
							statementCount++;
							
							return;
						}
						else{
							printError("statement");
						}
					}
					else{
							printError("statement");
						}
				}
				else{
							printError("statement");
						}
			}
			else{
							printError("statement");
						}
		}
		else if(nonTerms.get("statementsz").contains(nextToken.tokenName))
			return;
		else{
			printError(" Error from Statement");
		}
	}
	
	public static void statement_z(){
		
		loadToken();
		//System.out.println(count++ +"statement z token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals("[")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("expression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				expression();
				
				loadToken();
				if(nextToken.tokenName.equals("]")){
					tokenTaken=false;
					
					loadToken();
					if(nextToken.tokenName.equals("=")){
						tokenTaken=false;
						
						loadToken();
						if(nonTerms.get("expression").contains(nextToken.tokenName)
						|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
						|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
							expression();
							
							loadToken();
							if(nextToken.tokenName.equals(";")){
								tokenTaken=false;
								statementCount++;
								
								return;
							}
							else{
								printError("statement z");
							}
						}
						else{
								printError("statement z");
							}
					}
					else{
								printError("statement z");
							}
				}
				else{
								printError("statement z");
							}
			}
			else{
								printError("statement z");
							}
		}
		else if(nextToken.tokenName.equals("=")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("expression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				expression();
				
				loadToken();
				if(nextToken.tokenName.equals(";")){
					tokenTaken=false;
					statementCount++;
					
					return;
				}
				else{
								printError("statement z");
							}
			}
			else{
								printError("statement z");
							}
		}
		else if(nextToken.tokenName.equals("(")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("statementz1").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				statement_z1();
				
				return;
			}
			else{
								printError("statement z");
							}
		}
		else
			printError("Last else in statement z");
	}
	
	public static void statement_z1(){
		
		loadToken();
		if(nextToken.tokenName.equals(")")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenName.equals(";")){
				tokenTaken=false;
				statementCount++;
				
				return;
			}
			else{
				printError("statement z1");
			}
		}
		else if(nonTerms.get("exprlist").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			expr_list();
			
			loadToken();
			if(nextToken.tokenName.equals(")")){
				tokenTaken=false;
				
				loadToken();
				if(nextToken.tokenName.equals(";")){
					tokenTaken=false;
					statementCount++;
					
					return;
				}
				else{
					printError("statement z1");
				}
			}
			else{
				printError("statement z1");
			}
		}
		else
			printError("Error from statement z1");
	}
	
	public static void expr_list(){
		
		loadToken();
		//System.out.println(count++ + "Expr list , token = " + nextToken.tokenName);
		if(nonTerms.get("expression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			expression();
			
			loadToken();
			if(nonTerms.get("non-emptyexprlistp").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				non_empty_expr_list_p();
				
				return;
			}
			else{
				printError("expr-list");
			}
		}
		else
			printError("expr_list error");
	}
	
	public static void non_empty_expr_list_p(){
		
		loadToken();
		if(nextToken.tokenName.equals(",")){
			tokenTaken=false;
			
			if(nonTerms.get("expression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				expression();
				
				loadToken();
				if(nonTerms.get("non-emptyexprlistp").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
					non_empty_expr_list_p();
					
					return;
				}
				else{
					printError("non empty expr-list p");
				}
			}
			else{
				printError("non empty expr-list p");
			}
		}
		else if(nonTerms.get("non-emptyexprlistp").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			
			return;
		}
		else
			printError("Error in non empty expr list p");
	}
	
	public static void if_statement(){
		
		loadToken();
		if(nextToken.tokenName.equals("if")){
			statementCount++;
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenName.equals("(")){
				tokenTaken=false;
			
				loadToken();
				if(nonTerms.get("conditionexpression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
					condition_expression();
					
					loadToken();
					if(nextToken.tokenName.equals(")")){
						tokenTaken=false;
						
						loadToken();
						if(nonTerms.get("blockstatements").contains(nextToken.tokenName)
						|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
							block_statements();
							
							return;
						}
						else{
							printError("if statement");
						}
					}
					else{
							printError("if statement");
						}
				}
				else{
							printError("if statement");
						}
			}
			else{
							printError("if statement");
						}
		}
		else
			printError("****Error in if statement");
	}
	
	public static void condition_expression(){
		
		loadToken();
		//System.out.println(count++ +" cond_expression token = " + nextToken.tokenName);
		if(nonTerms.get("condition").contains(nextToken.tokenName)
		|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
		|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			condition();	
			
			loadToken();
			//System.out.println(count++ +" cond_expression 2 token = " + nextToken.tokenName);
			if(nonTerms.get("conditionexpressionz").contains(nextToken.tokenName)
			|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
			|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				condition_expression_z();
				
				return;
			}
			else{
				printError("condition expression");
			}
		}
		else{
				printError("condition expression");
			}
	}
	
	public static void condition_expression_z(){
		
		loadToken();
		//System.out.println(count++ +"cond_expression z token = " + nextToken.tokenName);
		if(nonTerms.get("conditionop").contains(nextToken.tokenName)){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("condition").contains(nextToken.tokenName)
			|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
			|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				condition();
				
				return;
			}
			else{
				printError("condition expression z");
			}
		}
		else if(nonTerms.get("conditionexpressionz").contains(nextToken.tokenName))
			return;
		else
			printError("Error in cond expr z");
	}
	
	public static void condition_op(){
		
		loadToken();
		if(nextToken.tokenName.equals("&&")){
			tokenTaken=false;
			
			return;
		}
		else if(nextToken.tokenName.equals("||")){
			tokenTaken=false;
			
			return;
		}
		else
			printError("Error in condition_op");
	}
	
	public static void condition(){
		
		loadToken();
		//System.out.println(count++ +" condition token = " + nextToken.tokenName);
		if(nonTerms.get("expression").contains(nextToken.tokenName)
			|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
			|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			expression();
		
			loadToken();
			if(nonTerms.get("comparisonop").contains(nextToken.tokenName)){
				comparison_op();
				
				loadToken();
				if(nonTerms.get("expression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
					expression();
					
					return;
				}
				else{
					printError("condition ");
				}
			}
			else{
				printError("condition ");
			}
		}
		else
			printError("Error condition");
	}
	
	public static void comparison_op(){
		
		loadToken();
		//System.out.println(count++ + "token in comp op = " + nextToken.tokenName);
		if(nextToken.tokenName.equals("==") || nextToken.tokenName.equals("!=") 
		|| nextToken.tokenName.equals(">") || nextToken.tokenName.equals(">=")
		|| nextToken.tokenName.equals("<") || nextToken.tokenName.equals("<=")){
			tokenTaken=false;
			
			return;
		}
		else	
			printError("Error in comparison op");
	}
	
	public static void while_statement(){
		
		loadToken();
		if(nextToken.tokenName.equals("while")){
			tokenTaken=false;
			statementCount++;
			
			loadToken();
			if(nextToken.tokenName.equals("(")){
				tokenTaken=false;
			
				loadToken();
				if(nonTerms.get("conditionexpression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
					condition_expression();
					
					loadToken();
					if(nextToken.tokenName.equals(")")){
						tokenTaken=false;
						
						loadToken();
						//System.out.println(count++ + "token in while statement = " + nextToken.tokenName);
						if(nonTerms.get("blockstatements").contains(nextToken.tokenName)
						|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
							block_statements();
							
							return;
						}
						else{
							printError("while statement");
						}
					}
					else{
							printError("while statement");
						}
				}
				else{
							printError("while statement");
						}
			}
			else{
							printError("while statement");
						}
		}
		else
			printError("---Error in while statement");
	}
	
	public static void return_statement(){
		
		loadToken();
		//System.out.println(count++ +" token in return statement = " + nextToken.tokenName);
		if(nextToken.tokenName.equals("return")){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ +" token in return statement 2 = " + nextToken.tokenName);
			if(nonTerms.get("returnstatementz").contains(nextToken.tokenName)
			|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
			|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			return_statement_z();
			
			return;
			}
			else
				printError("Error in return 1 statement");
		}
		else
			printError("Error in return 2 statement");
	}
	
	public static void return_statement_z(){
		
		loadToken();
		if(nonTerms.get("expression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			expression();
			
			loadToken();
			if(nextToken.tokenName.equals(";")){
				tokenTaken=false;
				statementCount++;
				
				return;
			}
			else{
				printError("return statement z");
			}
		}
		else if(nextToken.tokenName.equals(";")){
			tokenTaken=false;
			statementCount++;
			
			return;
		}
		else	
			printError("Error in returns tatement z");
	}
	
	public static void break_statement(){
		
		loadToken();
		if(nextToken.tokenName.equals("break")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenName.equals(";")){
				tokenTaken=false;
				statementCount++;
				
				return;
			}
			else{
				printError("break statement");
			}
		}
		else	
			printError("Error in break tatement z");
	}
	
	public static void continue_statement(){
		
		loadToken();
		if(nextToken.tokenName.equals("continue")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenName.equals(";")){
				tokenTaken=false;
				statementCount++;
				
				return;
			}
			else{
				printError("contiue statement");
			}
		}
		else	
			printError("Error in continue tatement z");
	}
	
	public static void expression(){
		
		loadToken();
		//System.out.println(count++ +"expression token = " + nextToken.tokenName);
		if(nonTerms.get("term").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			term();
			
			loadToken();
			//System.out.println(count++ +"//expression token = " + nextToken.tokenName);
			if(nonTerms.get("expressionp").contains(nextToken.tokenName)){
				expression_p();
				
				return;
			}
			else{
				printError("expression");
			}
		}
		else
			printError("Error in expression");
	}
	
	public static void expression_p(){
		
		loadToken();
		if(nonTerms.get("addop").contains(nextToken.tokenName)){
			addop();
			
			loadToken();
			if(nonTerms.get("term").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				term();
				
				loadToken();
				//System.out.println(count++ +"expression p 2 token = " + nextToken.tokenName);
				if(nonTerms.get("expressionp").contains(nextToken.tokenName)){
					expression_p();
					
					return;
				}
				else{
					printError("expression p");
				}
			}
			else{
				printError("expression p");
			}
		}
		else if(nonTerms.get("expression").contains(nextToken.tokenName)){
			
			return;
		}
		else
			printError("Error in expressionp");
	}
	
	public static void addop(){
		
		loadToken();
		if(nextToken.tokenName.equals("+")){
			tokenTaken=false;
			return;
		}
		else if(nextToken.tokenName.equals("-")){
			tokenTaken=false;
			return;
		}
		else
			printError("Error in addop");
	}
	
	public static void term(){
		
		loadToken();
		//System.out.println(count++ +"term token = " + nextToken.tokenName);
		if(nonTerms.get("factor").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			factor();
			
			loadToken();
			//System.out.println(count++ +"term  1 token = " + nextToken.tokenName);
			if(nonTerms.get("termp").contains(nextToken.tokenName)
			|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				term_p();
				
				return;
			}
			else{
				printError("1. term");
			}
		}
		else
			printError("Error in term");
	}
	
	public static void term_p(){
		
		loadToken();
		//System.out.println(count++ + "Token in term_p =  " + nextToken.tokenName + nextToken.tokenType);
		if(nonTerms.get("mulop").contains(nextToken.tokenName)){
			mulop();
			
			loadToken();
			if(nonTerms.get("factor").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				factor();
				
				loadToken();
				if(nonTerms.get("termp").contains(nextToken.tokenName)){
					term_p();
					
					return;
				}
				else{
					printError("term p");
				}
			}
			else{
				printError("term p");
			}
		}
		else if(nonTerms.get("termp").contains(nextToken.tokenName)
		|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
		|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			
			return;
		}
		else
			printError("Error in term p");
	}
	
	public static void mulop(){
		
		loadToken();
		if(nextToken.tokenName.equals("*")){
			tokenTaken=false;
			return;
		}
		else if(nextToken.tokenName.equals("/")){
			tokenTaken=false;
			return;
		}
		else
			printError("Error in mulop");
	}
	
	public static void factor(){
		
		loadToken();
		//System.out.println(count++ +"factor token = " + nextToken.tokenName);
		if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ +"factor 1 token = " + nextToken.tokenName + nonTerms.get("factorz").contains(nextToken.tokenName));
			if(nonTerms.get("factorz").contains(nextToken.tokenName)){
				factor_z();
				
				return;
			}
			else{
				printError("factor");
			}
		}
		else if(nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			tokenTaken=false;
			
			return;
		}
		else if(nextToken.tokenName.equals("-")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				tokenTaken=false;
				
				return;
			}
			else{
				printError("factor");
			}
		}
		else if(nextToken.tokenName.equals("(")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("expression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				expression();
				
				loadToken();
				if(nextToken.tokenName.equals(")")){
					tokenTaken=false;
					
					return;
				}
				else{
					printError("factor");
				}
			}
			else{
				printError("factor");
			}
		}
		else if(nonTerms.get("termp").contains(nextToken.tokenName))
		
			return;
		else
			printError("Error in factor");
	}
	
	public static void factor_z(){
		
		loadToken();
		//System.out.println(count++ + " factor z token = " + nextToken.tokenName);
		if(nextToken.tokenName.equals("[")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("expression").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
				expression();
				
				loadToken();
				if(nextToken.tokenName.equals("]")){
					tokenTaken=false;
					
					return;
				}
				else{
					printError("factor z");
				}
			}
			else{
				printError("factor z");
			}
		}
		else if(nextToken.tokenName.equals("(")){
			tokenTaken=false;
			
			loadToken();
			//System.out.println(count++ + " 1. factor z token = " + nextToken.tokenName);
			if(nonTerms.get("factorz1").contains(nextToken.tokenName)
			|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				factor_z1();
				
				return;
			}
			else
				printError("Error in factor z");
		}
		else if(nonTerms.get("factor").contains(nextToken.tokenName))
			return;
		else
			printError("Error in factor z");
	}
	
	public static void factor_z1(){
		
		loadToken();
		//System.out.println(count++ + " token in factor z1 = " + nextToken.tokenName);
		if(nextToken.tokenName.equals(")")){
 			tokenTaken=false;
			
			return;
		}
		
		else if(nonTerms.get("exprlist").contains(nextToken.tokenName)
				|| nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenType==ScannerProj.TokenType.NUMBER){
			expr_list();
		
			loadToken();
			if(nextToken.tokenName.equals(")")){
				tokenTaken=false;
				
				return;
			}
			else{
				printError("factor z1");
			}
		}
		else
			printError("Error in factor z1");
	}
	
	public static void main(String[] args){
		
		try{
	
			scObj = new ScannerProj(args[0]);		// File name of the file ot be parsed
			
			String file = "firsts_sec";				// Necessary for creating the hashmap
			createHash(file);						// Creating the Hash
			tokenTaken=false;						// So that the first token is fetched
			varCount=statementCount=funcCount=count=0;	// Initializing the counts to 0
			
			program();									// Actual execution begins
			
			System.out.println("PASS. Variables = " + varCount 
			+ " Functions = " + funcCount + " Statements = " + statementCount);	// Printing
	
			
		}
		catch(Exception e){
	
		}
	}
}
