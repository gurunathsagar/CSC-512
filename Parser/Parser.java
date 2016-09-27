import java.util.*;
import java.io.*;

/***
	Author: Gurunath Ashok Hanamsagar
	Unity ID: ghanams
***/

public class Parser{
	static Map<String,String[]> nonTerms = new HashMap<String, String[]>();
	
	public static void createHash(String fileName){
		
		try{
			Scanner sc = new Scanner(new File(fileName));
			while(sc.hasNext()){
				
				String str = sc.nextLine();
				if(str.length() <= 5)
					continue;
				int i=0;
				String[] sides = str.split("=");
				sides[0].trim();
				sides[1].trim();
				String[] vals = sides[1].split(",");
				System.out.println("\n" + sides[0] + ":");
				String[] list = new String[vals.length];
				for(String s: vals){
					s.trim();
					list[i] = s;
					//System.out.print(list[i]);
				}
				nonTerms.put(sides[0], list);
			}
			
		}
		catch(Exception ex){
			
		}
		
	}
	
	public static void main(String[] args){
		
		String fileName = "firsts";
		createHash(fileName);
		
	}
}
