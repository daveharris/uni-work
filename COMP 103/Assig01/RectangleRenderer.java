import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.*;
import jds.collection.Vector;
import java.util.Enumeration;

/* Code for Assignment 1, July 2003
 * Name: David Harris
 * Usercode: harrisdavi3
 * ID: 300069566
 */

public class RectangleRenderer implements ActionListener{
	private DrawingCanvas canvas;
	private Vector rectangles = new Vector();


	public static void main(String args[]){
		new RectangleRenderer();
	}

	public RectangleRenderer(){
		JFrame frame = new JFrame("Render Rectangles");
		frame.setSize(600,400);

		//The graphics area
		canvas = new DrawingCanvas();
		frame.getContentPane().add(canvas, BorderLayout.CENTER);


		//The buttons
		JPanel buttonPanel = new JPanel();

		JButton readButton = new JButton("Read Data");
		JButton renderButton = new JButton("Render");
		JButton reverseButton = new JButton("Reverse");
		JButton blueButton = new JButton("Blue only");
		JButton quitButton = new JButton("Quit");

		readButton.addActionListener(this);
		renderButton.addActionListener(this);
		reverseButton.addActionListener(this);
		blueButton.addActionListener(this);
		quitButton.addActionListener(this);

		buttonPanel.add(readButton);
		buttonPanel.add(renderButton);
		buttonPanel.add(reverseButton);
		buttonPanel.add(blueButton);
		buttonPanel.add(quitButton);

		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

		frame.setVisible(true);
	}

	/* Respond to button presses */

	public void actionPerformed(ActionEvent e){
		System.out.println("Button: " + e.getActionCommand());
		if (e.getActionCommand().equals("Read Data")) {
			readRectangleFile();
		renderRects();
	}
		else if (e.getActionCommand().equals("Render"))
			renderRects();
		else if (e.getActionCommand().equals("Blue only"))
			renderColor(Color.blue);
		else if (e.getActionCommand().equals("Reverse"))
			renderReverse();
		else if (e.getActionCommand().equals("Quit"))
			System.exit(0);
	}

	/* Read the data from a file into the rectangles field */

	public void readRectangleFile(){
	String fname = JOptionPane.showInputDialog("File name:");
		 rectangles= new Vector();
		try {
			BufferedReader f = new BufferedReader(new FileReader(fname));
			while (true){
			String line = f.readLine();
			if (line==null) break;	//end of file
			rectangles.addLast(line);
				}
		f.close();
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/* Render the rectangles in forward order */
	public void renderRects(){
	canvas.clear(false);
		Enumeration e = rectangles.elements();
		while(e.hasMoreElements()){
			String size = (String) e.nextElement();
		Rectangle temp = new Rectangle(size);
		temp.render(canvas);
		}
	canvas.display();
	}

	/* Render the rectangles in reverse order */
	public void renderReverse(){
		for (int i = rectangles.size()-1;	 i >= 0;	i--) {
				String size = (String)rectangles.elementAt(i);
			Rectangle temp = new Rectangle(size);
			temp.render(canvas);
		}
	canvas.display();
	}

	/* Render just the blue rectangles in forward order */
	public void renderColor(Color col){
	canvas.clear(false);
		Enumeration e = rectangles.elements();
		while(e.hasMoreElements()){
			String size = (String) e.nextElement();
		Rectangle temp = new Rectangle(size);
		if(temp.getColor() == col)
			temp.render(canvas);
		}
	canvas.display();
	}

}






	
