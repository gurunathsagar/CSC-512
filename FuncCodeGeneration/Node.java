import java.io.PrintWriter;
import java.util.*;

/**
 * 
 * @author Kannan
 *
 */
public class Node {
	private boolean leaf;	//indicates whether the node is a leaf node or has children
	private String value;	//in case of a leaf node, contains value to be printed.
							//in case of datadecl node, contains the function name
	private Vector<Node> children; //list of child nodes
	private boolean datadecl;	// indicates whether this is a datadecl node
	int index;					//used only in case of expression nodes, contains the index of the local array containing the value
	boolean local;				//used only in case of id nodes, indicates whether id belongs to local or global array
	private Vector<Integer> indexList;	//used only in case of expr list, contains indexes of parameters
	
	/**
	 * Constructor
	 */
	public Node()
	{
		leaf = true;
		value = "";
		children = new Vector<Node>();
		datadecl = false;
		index = -1;
		indexList = new Vector<Integer>();
		local = true;
	}
	
	/**
	 * Set node as a leaf node
	 * @param value
	 */
	public void setTerminalValue(String value)
	{
		leaf = true;
		this.value = value;
	}
	
	/**
	 * Set node as a data declaration node
	 * @param value = function Name
	 */
	public void setDataDeclValue(String value)
	{
		leaf = false;
		datadecl = true;
		this.value = value;
	}
	
	/**
	 * Set as a non-leaf node
	 */
	public void setAsIntermediate()
	{
		leaf = false;
	}
	
	/**
	 * Add child to list
	 * @param n <-child
	 * @return
	 */
	public boolean addChild(Node n)
	{
		if(n == null)
			return false;
		setAsIntermediate();
		children.add(n);
		return true;
	}
	
	/**
	 * Add parameter index to list
	 * @param i <- index of local array containing parameter value
	 */
	public void addToIndexList(int i)
	{
		indexList.add(i);
	}
	
	/**
	 * @return List of local array indexes containing paramter values
	 */
	public Vector<Integer> getIndexList(){
		return indexList;
	}
	
	
	public void concatIndexList(Vector<Integer> vec){
		for(int i = 0; i< vec.size(); i++)
		{
			addToIndexList(vec.elementAt(i));
		}
	}
	
	/**
	 * Prints the node to file
	 * @param pw -> printWriter to write to file
	 * @param vHMap -> Hashmap containing ids
	 */
	public void print(PrintWriter pw, VariableHashmap vHMap)
	{
		//leaf node
		if(leaf == true)
		{
			//print value
			if(value.contains("\n"))
				pw.print(value);
			else if (!value.equals(""))
			pw.print(value + " ");
		}
		//data declaration node
		else if(datadecl == true)
		{
			//get total number of variables for function
			int num = vHMap.getNumVariables(value);
			if(num > 0){
				if(value.equals("global"))
				{
					//int global[num];
					//pw.print("int global["+num+"];\n");
				}
				else
				{
					//int local[num];
					//pw.print("int local["+num+"];\n");
					Vector<String> paramsList = vHMap.getParameterList(value);
					for(int i= 0; i<paramsList.size(); i++)
					{
						int index = vHMap.get(value, paramsList.elementAt(i));
						//pw.print("local[" + index +"] = " + paramsList.elementAt(i)+";\n");
					}
				}
			}
		}
		else
		{
			//non-leaf node
			for(int i=0; i< children.size(); i++)
			{
				//print children
				children.elementAt(i).print(pw, vHMap);
			}
		}
	}
	
	/**
	 * Prints the node to file
	 * @param pw -> printWriter to write to file
	 * @param vHMap -> Hashmap containing ids
	 */
	/*public void finalPrint(List<String> main, List<String> code)
	{
		//leaf node
		if(leaf == true)
		{
			
			if(value.contains("StartOfMain###"))
				mainStarted = true;
			//print value
			
			if(mainStarted){
				if(value.contains("StartOfMain###"))
					main.add("mainFunc: ;" + "\n");
				else if(value.contains("EndOFMain@@@"))
					main.add("\n" + "goto jumpTable");
				else
					main.add(value);
			}
			else
				code.add(value);
			
			if(value.contains("EndOFMain@@@"))
				mainStarted = false;
			
		}
		else
		{
			//non-leaf node
			for(int i=0; i< children.size(); i++)
			{
				//print children
				children.elementAt(i).finalPrint(main, code);
			}
		}
	}*/
	
	public void updateVariableCount(Map<String, Integer> varCount, VariableHashmap vHMap){
		if(leaf == true)
		{
			
		}
		//data declaration node
		else if(datadecl == true)
		{
			//get total number of variables for function
			int num = vHMap.getNumVariables(value);
			if(num > 0){
				if(value.equals("global"))
				{
					//int global[num];
					//pw.print("int global["+num+"];\n");
					varCount.put("global", num);
					//System.out.println("global" + num);
				}
				else
				{
					//int local[num];
					//pw.print("int local["+num+"];\n");
					varCount.put(value, num);
					//System.out.println(value + num);
					Vector<String> paramsList = vHMap.getParameterList(value);
					for(int i= 0; i<paramsList.size(); i++)
					{
						// For all the input parameters to the function
						int index = vHMap.get(value, paramsList.elementAt(i));
						//pw.print("local[" + index +"] = " + paramsList.elementAt(i)+";\n");
					}
				}
			}
		}
		else
		{
			//non-leaf node
			for(int i=0; i< children.size(); i++)
			{
				//print children
				children.elementAt(i).updateVariableCount(varCount, vHMap);
			}
		}
	}
}
