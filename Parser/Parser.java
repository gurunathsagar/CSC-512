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

	/*public createTermHash(){
		
		//terms.insert("", "")
		
	}*/
	
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
				//System.out.println("\n" + sides[0] + ":");
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
		
		else
			System.out.println("Error");
		
	}
	
	public static void printError(String str){
		
		System.out.println(" Error with message : " + str + "\n\n");
		
	}
	
	public static void loadToken(){
		if(!tokenTaken){
			if(scObj.hasMoreTokens())
				nextToken = scObj.getNextToken();
			
			else
				System.out.println(" Out of tokens !! ");
		
			tokenTaken = false;
		}
	}
	
	public static void program(){
		
		//System.out.println("Entered Program " + nonTerms.get("typename"));
		loadToken();
		
		 System.out.println("token = " + nextToken.tokenName);
		if ( nonTerms.get("typename").contains(nextToken.tokenName) ){
			
			tokenTaken=false;
			loadToken();
			 System.out.println("token = " + nextToken.tokenName);
			if( nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				
				tokenTaken=false;
				
				loadToken();
				System.out.println("token = " + nextToken.tokenName);
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
		
		if(nextToken.tokenName.equals("(")){
			tokenTaken=false;
			
			if(nonTerms.get("funcdeclz").contains(nextToken.tokenName)){
				func_decl_z();
				
				loadToken();
				if(nonTerms.get("funcz").contains(nextToken.tokenName)){
					func_z();
					
					loadToken();
					if(nonTerms.get("funclist").contains(nextToken.tokenName)){
						func_list();
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
			if(nonTerms.get("expression").contains(nextToken.tokenName)){
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
		if(nonTerms.get("typename").contains(nextToken.tokenName)){
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
						}
					}
				}
			}
		}
		else if(nextToken.tokenName.equals("$")){
			return;
		}
		else{
			printError(" Error from program_z1 ");
		}
		
	}
	
	public static void func_list(){
		
		loadToken();
		if(nonTerms.get("func").contains(nextToken.tokenName)){
			func();
			
			loadToken();
			if(nonTerms.get("func_list").contains(nextToken.tokenName)){
				func_list();
				
				return;
			}
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
		}
	}
	
	public static void func_z(){
		
		loadToken();
		if(nextToken.tokenName.equals(";")){
			tokenTaken=false;
			return;
		}
		else if(nextToken.tokenName.equals("{")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("funcz6").contains(nextToken.tokenName)){
				func_z6();	
			
				return;	
			}
		}
		else
			printError(" Error in func_z");
	}
	
	public static void func_z6(){
		
		loadToken();
		if( nonTerms.get("datadecls").contains(nextToken.tokenName)){
			data_decls();
			
			loadToken();
			if(nonTerms.get("funcz7").contains(nextToken.tokenName)){
				func_z7();
			}
		}
		else if( nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER 
			||  nonTerms.get("statements").contains(nextToken.tokenName) ){
			tokenTaken=false;
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
		if( nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER 
			||  nonTerms.get("statements").contains(nextToken.tokenName) ){
			tokenTaken=false;
			statements();
			
			loadToken();
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
		if( nonTerms.get("typename").contains(nextToken.tokenName) ){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				if(nextToken.tokenName == "("){
					tokenTaken=false;
					
					loadToken();
					if(nonTerms.get("funcdeclz").contains(nextToken.tokenName)){
						func_decl_z();
						
						return;
					}
					else{
						System.out.println("func decl error 1");
					}
				}
				else{
					System.out.println("func decl error 2");
				}
			}
			else{
				System.out.println("func decl error 3");
			}
		}
		else{
			System.out.println("func decl error 4");
		}
	}
	
	public static void func_decl_z(){
		
		loadToken();
		if(nextToken.tokenName.equals(")")){
			tokenTaken=false;
			
			return;
		}
		else if(nextToken.tokenName.equals("void")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("func_decl_z2").contains(nextToken.tokenName)){
				func_decl_z2();
				return;
			}
		}
		else if(nextToken.tokenName.equals("int")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				if(nonTerms.get("func_decl_z3").contains(nextToken.tokenName)){
					func_decl_z3();
					
					return;
				}
			}
		}
		else if(nextToken.tokenName.equals("binary")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				if(nonTerms.get("func_decl_z3").contains(nextToken.tokenName)){
					func_decl_z3();
					
					return;
				}
			}
		}
		else if(nextToken.tokenName.equals("decimal")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType == ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				if(nonTerms.get("func_decl_z3").contains(nextToken.tokenName)){
					func_decl_z3();
					
					return;
				}
			}
		}
	}
		
	public static void func_decl_z2(){
		
		loadToken();
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
		}
	}
	public static void func_decl_z3(){
		
		loadToken();
		if(nextToken.tokenName.equals(")")){
			tokenTaken=false;
			
			return;
		}
		else if(nextToken.tokenName.equals(",")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("typename").contains(nextToken.tokenName)){
				tokenTaken=false;
				
				loadToken();
				if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
					tokenTaken=false;
					
					loadToken();
					if(nonTerms.get("non_empty_list").contains(nextToken.tokenName)){
						non_empty_list();
						
						loadToken();
						if(nextToken.tokenName.equals(")")){
							return;	
						}
					}
				}
			}
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
		if(nonTerms.get("typename").contains(nextToken.tokenName)){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				tokenTaken=false;
				
				loadToken();
				if(nonTerms.get("nonemptylistz").contains(nextToken.tokenName)){
					non_empty_list_z();
					
					return;
				}
			}
		}
	}
	
	public static void non_empty_list_z(){
		
		loadToken();
		if(nonTerms.get("nonemptylistp").contains(nextToken.tokenName)){
			return;
		}
		else if(nextToken.tokenName.equals(")")){
			return;
		}
	}
	
	public static void non_empty_list_p(){
		
		loadToken();
		if(nextToken.tokenName.equals(",")){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("typename").contains(nextToken.tokenName)){
				tokenTaken=false;
				
				loadToken();
				if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
					tokenTaken=false;
					
					loadToken();
					if(nonTerms.get("non_empty_list").contains(nextToken.tokenName)){
						non_empty_list();
						
						return;
					}
				}
			}
		}
	}
	
	public static void data_decls(){
		
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
					if(nonTerms.get("datadeclsz").contains(nextToken.tokenName)){
						data_decls_z();
						
						return;
					}
				}
			}
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
					if(nonTerms.get("datadeclsz").contains(nextToken.tokenName)){
						data_decls_z();
						
						return;
					}
				}
			}
		}
		else if(nonTerms.get("datadeclsz").contains(nextToken.tokenName)){
			return;
		}
	}
	
	public static void id_list(){
		
		loadToken();
		if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			
			id();
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER 
			|| nonTerms.get("idlistz").contains(nextToken.tokenName)){
				
				id_list_z();
			}
		}
	}
	
	public static void id_list_z(){
		
		loadToken();
		if(nextToken.tokenName.equals(",")){
			id_list_p();
			
			return;
		}
		else if(nextToken.tokenName.equals(";")){
			return;
		}
	}
	
	public static void id_list_p(){
		
		loadToken();
		if(nextToken.tokenName.equals(",")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				id();
				
				loadToken();
				if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER
				|| nextToken.tokenName.equals(";")){
					id_list_p_z();
				}
			}
		}
	}
	
	public static void id_list_p_z(){
		
		loadToken();
		if(nextToken.tokenName.equals(",")){
			id_list_p();
			
			return;
		}
		else if(nextToken.tokenName.equals(";"))
			return;
	}
	
	public static void id(){
		
		loadToken();
		if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
			tokenTaken=false;
			
			loadToken();
			if(nonTerms.get("id").contains(nextToken.tokenName)){
				
				id_z();
			}
		}
	}
	
	public static void id_z(){
		
		loadToken();
		if(nextToken.tokenName.equals("[")){
			tokenTaken=false;
			
			loadToken();
			if(nextToken.tokenType==ScannerProj.TokenType.IDENTIFIER){
				expression();
				
				loadToken();
				if(nextToken.tokenName.equals("]")){
					tokenTaken=false;
					
					return;
				}
			}
		}
	}
	
	public static void expression(){
		
	}
	
	public static void statements(){
		
	}
	
	public static void main(String[] args){
		
		try{
		scObj = new ScannerProj("fileName");			
		
		String fileName = "firsts_sec";
		createHash(fileName);
		System.out.println("Created HashMap ");
		tokenTaken=false;
		//matchToken(nextToken);
		program();
		
		}
		catch(Exception e){
	
		}
	}
}
