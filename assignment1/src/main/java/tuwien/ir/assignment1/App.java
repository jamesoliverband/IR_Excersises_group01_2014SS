package tuwien.ir.assignment1;

import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class of assignment.
 * 
 */
public class App {
	/*
	 * BOW = bag of words
	 * BIG = bigram
	 */
	enum IndexFormat {
		BOW, BIG
	}

	@Option(name = "-i", aliases = { "--index-format" }, usage = "index format", required = true)
	public IndexFormat indexFormat = IndexFormat.BOW;

	@Option(name = "-f", aliases = { "--file" }, usage = "input filename", required = true, metaVar = "INPUT")
	public File inputFile = new File("in.txt");

	/*
	 * M1 = TODO
	 * M2 = TODO
	 */
	enum ScoringMethod {
		M1, M2
	}

	@Option(name = "-s", aliases = { "--scoring-method" }, usage = "scoring method", required = true)
	public ScoringMethod scoringMethod;

	/*
	 * @Option(name="-r",usage="recursively run something") private boolean
	 * recursive;
	 * 
	 * @Option(name="-o",usage="output to this file",metaVar="OUTPUT") private
	 * File out = new File(".");
	 * 
	 * @Option(name="-str") // no usage private String str = "(default value)";
	 * 
	 * @Option(name="-hidden-str2",hidden=true,usage="hidden option") private
	 * String hiddenStr2 = "(default value)";
	 * 
	 * @Option(name="-n",usage=
	 * "repeat <n> times\nusage can have new lines in it and also it can be verrrrrrrrrrrrrrrrrry long"
	 * ) private int num = -1;
	 * 
	 * // using 'handler=...' allows you to specify a custom OptionHandler //
	 * implementation class. This allows you to bind a standard Java type //
	 * with a non-standard option syntax
	 * 
	 * @Option(name="-custom",handler=BooleanOptionHandler.class,usage=
	 * "boolean value for checking the custom handler") private boolean data;
	 */ 
	 // receives other command line parameters than options
	 
	@Argument private List<String> arguments = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		new App().doMain(args);
		/*
		 * // The CLI allows the user to enter provide // search parameters and
		 * a file with a topic to search for. // bag-of-words or the bi-gram
		 * index, scoring method)
		 * 
		 * if (args.length == 0){ printHelp(); }else{ // TODO run through the
		 * parameters and choose the appropriate action for(int i = 0; i <
		 * args.length ;i++){ String s = args[i];
		 * 
		 * } }
		 */
		System.out.println("Exiting.");

	}

	private static void printHelp() {
		System.out.println("Help\n----------");
		System.out.print("Your input should be looking like this: ");
		System.out.println("assignment1 -i bow -f file.txt\n");
		System.out.println("-i(index): \tbow ...bag of words index\n\t\tbig ...bigram-index\n");
		System.out.println("-sm(scoring method): \t TODO ");
	}

	public void doMain(String[] args) throws IOException {
		CmdLineParser parser = new CmdLineParser(this);

		// if you have a wider console, you could increase the value;
		// here 80 is also the default
		parser.setUsageWidth(80);

		try {
			// parse the arguments.
			parser.parseArgument(args);

			// you can parse additional arguments if you want.
			// parser.parseArgument("more","args");

			// after parsing arguments, you should check
			// if enough arguments are given.
			//if (arguments.isEmpty())
			//	throw new CmdLineException(parser, "No argument is given");

		} catch (CmdLineException e) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.err.println(e.getMessage());
			System.err.println("java App [options...] arguments...");
			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();

			// print option sample. This is useful some time
			System.err.println(" Example: java App" + parser.printExample(ALL));

			return;
		}

		// this will redirect the output to the specified output (metaVar=OUTPUT)
		//System.out.println(out);

		/*
		 * if (recursive) System.out.println("-r flag is set"); if (data)
		 * System.out.println("-custom flag is set");
		 * System.out.println("-str was "+str); if (num>=0)
		 * System.out.println("-n was "+num); // access non-option arguments
		 * System.out.println("other arguments are:"); for (String s :
		 * arguments) System.out.println(s);
		 */
		System.out.println("--- Begin cmdline options:");
		System.out.println("input file is " + inputFile);
		System.out.println("index format is " + indexFormat);
		System.out.println("scoring method is " + scoringMethod);
		System.out.println("--- End of options.");
	}
}