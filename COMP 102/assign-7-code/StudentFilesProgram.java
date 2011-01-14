
import javax.swing.JOptionPane;
import java.io.*;

public class StudentFilesProgram{
	public static void main(String[] args){

		try{
			String inFileName = JOptionPane.showInputDialog("Input file name");
			FileReader inStream = new FileReader(inFileName);
			BufferedReader ins = new BufferedReader(inStream);
			String outFileName = JOptionPane.showInputDialog("Enter output file");
			FileWriter outStream = new FileWriter(outFileName);
			PrintWriter outs = new PrintWriter(outStream);

			String dataline = ins.readLine();

			while(dataline != null) {
				int space = dataline.indexOf(" ");
				String name = dataline.substring(0, space);
				int space2 = dataline.indexOf(" ", space+1);
				int space3 = dataline.indexOf(" ", space2+1);
				double mark1 = Double.parseDouble(dataline.substring(space, space2));
				double mark2 = Double.parseDouble(dataline.substring(space2+1, space3));
				double mark3 = Double.parseDouble(dataline.substring(space3));
				double markTotal = mark1 + mark2 + mark3;

				String msg = name + "   " + markTotal;
				System.out.println(msg);
				outs.println(msg);
				dataline = ins.readLine();
			}

			ins.close();
			outs.close();
			return;
		}

			catch (IOException ex) {
				System.out.println("IO Error:  " + ex.getMessage());
				ex.printStackTrace();
			}

	}
}
