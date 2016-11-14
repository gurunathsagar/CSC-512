Author: Gurunath Ashok Hanamsagar
Unity ID: ghanams

Project: Intermediate Code Generation.

Previous projects required: Scanner, Parser. 

File Requirements: Scanner.java, Parser.java, RecursiveParsing.java, RegularExpressions.java, Scanner.java, Scan.java, Token.java

How to Compile:

$ javac Parser.java 

This will compile all the necessary classes. The Scanner, Tkens etc will be automatically generated and 
the pwd will have all the necessary .class files. 

The file that is to be generated must be present in the same directory and it must be of the format:
"filename.c". The resultant file generated will be "filename_gen.c". 

How to Execute:

$ java Parser filename.c

All the class files will be generated and a new file named filename_gen.c will be generated that follows the modified grammar. 

******** 

About the Scanner and Parser. I have used the Scanner and Parser as provided by the Professor. I have made minor modifications as per 
requirement for my convenience.

********

About the IR Code Generation.

IR code is generated so that the system can comprehend in an easy fashion. All the assignments are changed to 
3 adress code. 

Implementation:

The IR code follows the Parser control flow. Each time a valid token is obtained, either it is added to the queue or it is
queued in another queue to be put into the main queue later.  There are two major counts: localCount (number of lcoal variables required for each function)
globalCount(Which tells the number of total global variables). The variables are used to mark the amount of memory to be allocated for each memory.

Data Structures (Most important):

I have maintained 4 maps (localMap, localArray, globalMap, globalArray) and 2 queues(localQueue, globalQueue). 

Maps:

localMap: This comprises the identifiers of all the variables local to a function.
localArray: This comprises the array idenitifiers of all the variables local to a function.
globalMap: This comprises the identifiers of all the variables global to a function.
globalArray: This comprises the array idenitifiers of all the variables global to a function.


Queues:

localQueue: Each token obtained by the parser is verified, modified and then copied on to the localQueue.
globalQueue: At the end of each function, the the localQueue is emptied on to the globalQueue.
tokens: This is a list used extensively whenever an expression is evaluated and comprises the tokens that are to be queued but not immediately.

Stack:

tempList: When we come across expressions, we need to store the already added tokens in a stack so that the 
evaluations of expressions can be reloaded later as and when needed.
