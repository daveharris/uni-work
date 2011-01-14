import java.io.*;
import java.util.Enumeration;
import jds.Set;

/** Writes out all the distinct lines in a file, eliminating all the duplicates.
 *The input file may be specified as a command line argument, othewise, the program
 *will allow the user to select a file.
 *The output file is written to a file of the same name with ".uniq" appended.*/

public class Unique{

	public static void main(String args[]){
		Set distinctLines = new ArraySet();
		int count = 0;
		String fname="";
		if (args.length <1) 
			fname = FileDialog.open();
		else 
			fname =args[0];
		try {
			BufferedReader fin = new BufferedReader(new FileReader(fname));
			System.out.println("reading from "+fname);
			while (true){
				String line = fin.readLine();
				if (line==null) break;
				count++;
				distinctLines.addElement(line);
				System.out.println(count);
			}
			fin.close();
			System.out.println("Writing to "+fname+".uniq");
			PrintWriter fout = new PrintWriter(new FileWriter(fname+".uniq"));
			for (Enumeration e = distinctLines.elements(); e.hasMoreElements();){
				fout.println((String) e.nextElement());
			}
			fout.close();
			System.out.println(distinctLines.size() +" distinct lines out of " + count + " total lines");
			System.exit(0);
		}
		catch(IOException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

}
