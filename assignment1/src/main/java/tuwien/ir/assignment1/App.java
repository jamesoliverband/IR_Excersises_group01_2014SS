package tuwien.ir.assignment1;

/**
 * Hello world!!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	// The CLI allows the user to enter provide
    	// search parameters and a file with a topic to search for.
    	// bag-of-words or the bi-gram index, scoring method)
    	
    	if (args.length == 0){
    		printHelp();
    	}else{
    		// TODO run through the parameters and choose the appropriate action
    		for(int i = 0; i < args.length ;i++){
    			String s = args[i];
    			
    		}
    	}
    	
        System.out.println( "Hello World!" );
    }

	private static void printHelp() {
		System.out.println("Help\n----------");
		System.out.print("Your input should be looking like this: ");
		System.out.println("assignment1 -i bow -f file.txt\n");
		System.out.println("-i(index): \tbow ...bag of words index\n\t\tbig ...bigram-index\n");
		System.out.println("-sm(scoring method): \t TODO ");
	}
}