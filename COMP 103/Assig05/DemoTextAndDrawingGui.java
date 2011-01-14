import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.io.*;

/* Demo Code for Assignment 5, Aug 2003
 * Illustrates a program that puts up a window with
 * - some buttons,
 * - a drawing canvas,
 * - and a scrollable text area.
 */

public class DemoTextAndDrawingGui implements ActionListener{

	private DrawingCanvas canvas;	
	private JTextArea textArea; 

	public static void main(String args[]){
		new DemoTextAndDrawingGui();
	}
	

	public DemoTextAndDrawingGui(){
		JFrame frame = new JFrame("Demo Text and Graphics");
		frame.setSize(800,600);


		//The graphics area (but inside a scrollable pane)
		canvas = new DrawingCanvas();
		frame.getContentPane().add(new JScrollPane(canvas), BorderLayout.CENTER);

		//The text area
		textArea = new JTextArea(25, 30); // 25 lines long, 30 characters across
		frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.WEST);
		textArea.setTabSize(6);	// Set the tab width to 6, so the columns aren't too wide.

		
		//The buttons
		JPanel buttonPanel = new JPanel();
		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

		// I made a method below to make it easier to add the buttons
		addButton(buttonPanel, "Show Table");
		addButton(buttonPanel, "Clear Table");
		addButton(buttonPanel, "Output Table");
		addButton(buttonPanel, "Plot");
		addButton(buttonPanel, "Clear Plot");
		addButton(buttonPanel, "Quit");

		frame.setVisible(true);
	}

	private void addButton(JPanel panel, String name){
		JButton button = new JButton(name);
		button.addActionListener(this);
		panel.add(button);
	}

	/* Respond to button presses */

	public void actionPerformed(ActionEvent e){
		System.out.println("Button: " + e.getActionCommand());
		if (e.getActionCommand().equals("Show Table") )
			showTable();
		else if (e.getActionCommand().equals("Clear Table") )
			textArea.setText("Cleared");
		else if (e.getActionCommand().equals("Output Table") )
			System.out.println(textArea.getText());
		else if (e.getActionCommand().equals("Plot") )
			plot();
		else if (e.getActionCommand().equals("Clear Plot") )
			canvas.clear();
		else if (e.getActionCommand().equals("Quit") )
			System.exit(0);
	}

	/** Resets the map of counts of words to an empty map */
	public void showTable (){
		textArea.setText("");
		textArea.append("Num\tSq\tprime\n");
		textArea.append("====================\n");
		for (int i=0; i<50; i++){
			textArea.append(i+"\t"+ (i*i) +"\t"+isPrime(i)+"\n");
		}
	}

	public boolean isPrime(int n){
		int max = (int)Math.sqrt(n);
		for (int i=2; i<=max; i++){
			if (n%i == 0)
	return false;
		}
		return true;
	}

	public void plot(){
		int left = 20;
		int base = 500;
		int lastY = 250;
		canvas.drawString("250:", 0,250,false);
		canvas.drawString("	0:", 0,500,false);
		for (int i=0; i<400; i+=2){
			int thisY = lastY + (int)(Math.random()*20-10);
			canvas.drawLine(i-2+left, base-lastY, i+left, base-thisY, false);
			lastY = thisY;
		}
		canvas.display();
	}

}



