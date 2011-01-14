import java.io.*;
import javax.swing.JOptionPane;

public class FileTest{
	public static void main(String[] args) {
		try{
			String inFileName = JOptionPane.showInputDialog("InputFile Name:");
			FileReader inStream = new FileReader(inFileName);
			BufferedReader ins = new BufferedReader(inStream);

			String dataLine = ins.readLine();
			int counter = 0;
			while (dataLine != null) {
				System.out.println(dataLine + "  " + counter++);
				dataLine = ins.readLine();
			}
			ins.close();
		}
		catch(IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
		}
	}
}