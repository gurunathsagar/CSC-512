import java.util.*;
import java.io.*;

/***
	Author: Gurunath Ashok Hanamsagar
	Unity ID: ghanams
***/

public class Parser{
	Map<String,String[]> nonTerms = new HashMap<String, String[]>();
	
	public void createHash(String fileName){
		
		try{
			Scanner sc = new Scanner(new File(fileName));
			while(sc.hasNext()){
				
				String str = sc.nextLine();
				if(str.length <= 5)
					continue;
					
				String[] sides = str.split("=");
				sides[0].trim();
				sides[1].trim();
				String[] vals = sides[1].split(",");
				System.out.println(sides[0]);
				for(String s: vals){
					nonTerms.put(sides[0], s);
					System.out.println(s);
				}

			}
			
		}
		catch(Exception ex)
		
	}
	
	public static void main(String[] args){
		
		String fileName = "firsts";
		createHash(fileName);
		
	}
}
