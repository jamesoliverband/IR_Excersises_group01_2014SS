package tuwien.ir.assignment1;

import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import tuwien.ir.assignment1.index.BigramIndex;
import tuwien.ir.assignment1.index.BowIndex;
import tuwien.ir.assignment1.index.Index;
import tuwien.ir.assignment1.index.SearchResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class of assignment.
 */
public class App {
	/*
	 * BOW = bag of words BIG = bigram
	 */
	enum IndexFormat {
		BOW, BIG
	}

	@Option(name = "-f", aliases = { "--index-format" }, usage = "index format", required = true)
	public IndexFormat indexFormat = IndexFormat.BOW;

	@Option(name = "-i", aliases = { "--index" }, usage = "index filename", required = true, metaVar = "INDEX", depends = { "-f" })
	public File indexFile = new File("index.txt");

	@Option(name = "-d", aliases = { "--docs" }, usage = "documents directory", required = false, metaVar = "DOCS", depends = { "-i" })
	public File documentsDir = null;	//new File("20_newsgroups_subset/");

	@Option(name = "-t", aliases = { "--search-topic" }, usage = "search topic", required = false, metaVar = "TOPIC", depends = { "-i", "-s", "-tid", "-rn" })
	public File searchTopic = null;	//new File("topics/topic1");

	@Option(name = "-tid", aliases = { "--topic-id" }, usage = "search topic id", required = false)
	public String searchTopicId = null;
	
	@Option(name = "-rn", aliases = { "--run-name" }, usage = "search run name", required = false)
	public String sarchRunName = null;

	/*
	 * M1 = TODO M2 = TODO
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

	@Argument
	private List<String> arguments = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		App app = new App();
		app.doMain(args);
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

		// new index depending with correct type, according to cmdline
		Index index = null;
		switch (app.indexFormat) {
		case BOW:
			index = new BowIndex();
			break;
		case BIG:
			index = new BigramIndex();
			break;
		default:
			System.out
					.println("ERROR: initialize index: Unrecognized index format. Exiting.");
			System.exit(1);
		}
		// either make index or search index
		if (app.searchTopic != null) {
			// search mode
			// load index
			index.load(app.indexFile);
			// perform search
			ArrayList<SearchResult> results = index.getSimilarDocuments(app.searchTopic, app.searchTopicId, app.sarchRunName);
			// print results
			for (int i=0; i < results.size(); i++) {
				// get result
				SearchResult result = results.get(i);
				// print this result
				System.out.println(result.toString());
			}
		} else if (app.documentsDir != null) {
			// index creation mode
			// get file list
			// source:
			// http://stackoverflow.com/questions/2056221/recursively-list-files-in-java
			// source:
			// http://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html
			// source:
			// http://www.riedquat.de/blog/2011-02-26-01
			Find finder = app.new Find();
			Files.walkFileTree(app.documentsDir.toPath(), finder);	//Paths.get( System.getProperty( "user.home" ) )
			System.out.format("Found %d documents to index.\n", finder.foundFiles.size());
			System.out.println("Indexing all files...");
			// index all documents
			for (int i=0; i < finder.foundFiles.size(); i++) {
				// get file
				File file = finder.foundFiles.get(i);
				// generate documentId
				String lastpath = file.getParentFile().getName();
				String filename = file.getName();
				String documentId = lastpath + file.pathSeparator + filename;
				// index the document
				//System.out.println(file.getAbsolutePath());
				index.indexDocument(file, documentId);
			}
			// save index
			index.save(app.indexFile.toPath());
			// print summary
			System.out.println(index.toString());
		}
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
			// if (arguments.isEmpty())
			// throw new CmdLineException(parser, "No argument is given");

			if (searchTopic == null && documentsDir == null) {
				throw new CmdLineException(
						parser,
						"One out of --search-topic (perform search) or --docs (scan docs and generate index) must be given");
			} else if (searchTopic != null && documentsDir != null) {
				System.out.println("searchTopic: " + searchTopic);
				System.out.println("searchTopic len: " + new Long(searchTopic.length()).toString());
				System.out.println("documentsDir: " + documentsDir.getCanonicalPath());
				throw new CmdLineException(
						parser,
						"Only one out of --search-topic (perform search) or --docs (scan docs and generate index) can be given");
			}

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

			System.exit(1);
		}

		// this will redirect the output to the specified output
		// (metaVar=OUTPUT)
		// System.out.println(out);

		/*
		 * if (recursive) System.out.println("-r flag is set"); if (data)
		 * System.out.println("-custom flag is set");
		 * System.out.println("-str was "+str); if (num>=0)
		 * System.out.println("-n was "+num); // access non-option arguments
		 * System.out.println("other arguments are:"); for (String s :
		 * arguments) System.out.println(s);
		 */
		System.out.println("--- Begin cmdline options:");
		System.out.println("index file is " + indexFile);
		System.out.println("index format is " + indexFormat);
		System.out.println("scoring method is " + scoringMethod);
		System.out.println("--- End of options.");
	}
	
	class Find extends SimpleFileVisitor<Path> {
		public ArrayList<File> foundFiles;
		
		public Find() {
			this.foundFiles = new ArrayList<File>();
		}
	    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
	        if (".svn".equals(dir.getFileName().toString())) {
	            return FileVisitResult.SKIP_SUBTREE;
	        }
	        //System.out.println(dir);
	        return FileVisitResult.CONTINUE;
	    }
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
	        //System.out.println(file);
	        this.foundFiles.add(file.toFile());
	        return FileVisitResult.CONTINUE;
	    }
	}
}