import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Enumeration;
import jds.Indexed;
import jds.collection.Vector;

/**
 * MiniDraw - a simple drawing program for making simple diagrams
 * @author:David Harris
 * @ID:300069566
 * @Assignment 2
 * @see jds.Collection
 *
 * MiniDraw allows the user to draw lines, rectangles, ovals, and text strings on the screen
 * It also allows the user to delete (and maybe move) the objects, and to save the drawing
 * to a file and open again it later.
 */

public class MiniDraw implements ActionListener, MouseListener{

	private DrawingCanvas canvas;

  // Fields to store the current state of the drawing (the collection of shapes, the brush, colors, etc)
  // YOUR CODE HERE
	private Color currentColor = Color.black;
	private String brush = "line";
	private Vector v;


	public static void main(String args[]){
		new MiniDraw();
	}

	public MiniDraw(){
		v = new Vector();
		JFrame frame = new JFrame("MiniDraw");
		frame.setSize(800,600);

		//Create the graphics area, set it to listen to the mouse, and add it to the frame
		// YOUR CODE HERE
		JPanel buttonPanel = new JPanel();
		canvas = new DrawingCanvas();
		canvas.addMouseListener(this);
		//canvas.addMouseMotionListener(this);
		frame.getContentPane().add(canvas, BorderLayout.CENTER);

			//Create the buttons and add to the frame
		// YOUR CODE HERE
		JButton newButton = new JButton("New");
		JButton openButton = new JButton("Open");
		JButton saveButton = new JButton("Save");
		JButton deleteButton = new JButton("Delete");
		JButton lineButton = new JButton("Line");
		JButton rectButton = new JButton("Rect");
		JButton ovalButton = new JButton("Oval");
		JButton textButton = new JButton("Text");
		JButton colorButton = new JButton("Color");
		JButton quitButton = new JButton("Quit");

 		/* Respond to button presses */
		newButton.addActionListener(this);
		openButton.addActionListener(this);
		saveButton.addActionListener(this);
		deleteButton.addActionListener(this);
		lineButton.addActionListener(this);
		rectButton.addActionListener(this);
		ovalButton.addActionListener(this);
		textButton.addActionListener(this);
		colorButton.addActionListener(this);
		quitButton.addActionListener(this);

		buttonPanel.add(newButton);
		buttonPanel.add(openButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(lineButton);
		buttonPanel.add(rectButton);
		buttonPanel.add(ovalButton);
		buttonPanel.add(textButton);
		buttonPanel.add(colorButton);
		buttonPanel.add(quitButton);

		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

		frame.setVisible(true);
	}


	public void actionPerformed(ActionEvent e){
		//System.out.println("Button: " + e.getActionCommand());

		// YOUR CODE HERE
		if (e.getActionCommand().equals("New"))
			newDrawing();
		else if (e.getActionCommand().equals("Open"))
			openDrawing();
		else if (e.getActionCommand().equals("Save"))
			saveDrawing();
		else if (e.getActionCommand().equals("Delete"))
			brush = "delete";
		else if (e.getActionCommand().equals("Line"))
			brush = "line";
		else if (e.getActionCommand().equals("Rect"))
			brush = "rect";
		else if (e.getActionCommand().equals("Oval"))
			brush = "oval";
		else if (e.getActionCommand().equals("Text"))
			brush = "text";
		else if (e.getActionCommand().equals("Color"))
			selectColor();
		else if (e.getActionCommand().equals("Quit"))
			System.exit(0);
	}

  /** Use the Swing color chooser dialog box to allow the user to choose the current color */
	private void selectColor(){
		Color newColor = JColorChooser.showDialog(null,"Choose Color for new shapes", null);
		if (newColor!=null){
			currentColor = newColor;
			canvas.setForeground(currentColor);
		}
  }

  /* Respond to mouse events */

	private int lastX;
	private int lastY;

	public void mousePressed(MouseEvent e) {
		// YOUR CODE HERE
		lastX = e.getX();
		lastY = e.getY();
		//System.out.println("Pressed at ("+ lastX +" "+ lastY +")");
		
  }

	public void mouseReleased(MouseEvent e) {
		//Action depends on whether currently deleting shapes or drawing a shape.
		// YOUR CODE HERE
		//
		int x = e.getX();
		int y = e.getY();
		//System.out.println("Released at ("+ e.getX() +" "+ e.getY() +")");
		if (brush.equals("delete")) {
			deleteShape(x, y);
		}
		else {
			drawShape(x, y);
		}
	}

	public void mouseClicked(MouseEvent e) {}  //needed to satisfy interface
	public void mouseEntered(MouseEvent e) {}  //needed to satisfy interface
	public void mouseExited(MouseEvent e) {}   //needed to satisfy interface

  /* Helper methods for implementing the button actions */
  
  /** Clear the canvas and render all the shapes in the drawing */
  public void render(){
		// YOUR CODE HERE
		canvas.clear();
		for (Enumeration e = v.elements(); e.hasMoreElements(); ) {
			Shape s = (Shape) e.nextElement();
			s.render(canvas);
			canvas.display();
		}
  }

  /** Start a new drawing. */
	public void newDrawing(){
		// make a new, empty collection of shapes for the drawing and clear the graphics area
		// YOUR CODE HERE
		canvas.clear();
		v = new Vector();
	}


  /** Open a drawing file, and read all the shape descriptions into the current drawing. */
  public void openDrawing(){
		//Extension!  Read all the shape descriptions from the file,
		//create the appropriate shapes, add them to the drawing, close the file, and render the drawing.
		try {
		String fname = FileDialog.open();
		BufferedReader f = new BufferedReader(new FileReader(fname));

		// YOUR CODE HERE,  Extension only.

		f.close();
		}
		catch(IOException ex) {
		System.out.println(ex.getMessage());
		ex.printStackTrace();
		}
  }

	public void saveDrawing(){
		//Print out descriptions of all the shapes to a file,
		try {
			String fname = FileDialog.save();
			PrintWriter f = new PrintWriter(new FileWriter(fname));

		// YOUR CODE HERE
			for (Enumeration e = v.elements(); e.hasMoreElements(); ) {
				Shape s = (Shape) e.nextElement();
				f.println(s.toString());
				System.out.println(s.toString());
			}
			f.close();
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}


  /* Helper methods for implementing the mouse actions */

  // You do not need to use these methods!!!
  // I chose to use the following helper methods.  You may use the same design or
  // you may wish to do it differently. 

  /** Deletes the shape that was under the mouseReleased position (x, y) */
  public void deleteShape(int x, int y){
		//Hint: check the methods in the Shape interface.
		//Find the index of the shape that (lastX, lastY) is on.
		//and remove the shape from the drawing, then render the drawing again
		//If not pressed on any shape, then do nothing.
		int counter = 0;
		System.out.println("vector is :" + v.size());
		for (int i = v.size()-1; i >= 0; i--) {
			counter++;
			Shape s = (Shape) v.elementAt(i);
			System.out.println(x + " : " + y);
			if (s.pointOnShape(x,y)) {
				v.removeElementAt(i);
				render();
				return;
			}
		System.out.println("counter is : " + counter);
		}


  }

  /** Draws a shape of the current kind at the positions where the mouse was pressed and released */
  public void drawShape(int x, int y){
		//construct a new Shape object of the appropriate kind
		//Use the appropriate constructor of the Line, Rectangle, Oval, or TextShape classes.
		//render the shape on the canvas
		//add it to the collection of shapes in the drawing
		//v= new Vector();

		if(brush.equals("line")) {
			Line L = new Line(x, y, lastX, lastY , currentColor);
			v.addLast(L);
			L.render(canvas);
			canvas.display();

		}
		else if(brush.equals("rect")) {
			int left= Math.min(lastX, x);
			int top= Math.min(lastY, y);
			int width= Math.abs(lastX - x);
			int height= Math.abs(lastY - y);
			Rectangle R = new Rectangle(left, top, width, height, currentColor);
			v.addLast(R);
			R.render(canvas);
			canvas.display();

		}
		else if (brush.equals("oval")) {
			int left= Math.min(lastX, x);
			int top= Math.min(lastY, y);
			int width= Math.abs(lastX - x);
			int height= Math.abs(lastY - y);
			Oval O = new Oval(left, top, width, height, currentColor);
			v.addLast(O);
			O.render(canvas);
			canvas.display();

		}
		else if (brush.equals("text")) {
			String input = JOptionPane.showInputDialog("Enter the string to be shown");
			TextShape T = new TextShape(input, lastX, lastY, currentColor);
			v.addLast(T);
			T.render(canvas);
			canvas.display();

		}
  }

  /** Make a new Rectangle object between (lastX, lastY) and (x,y) */
	public Shape newRectangle(int x, int y){
		return null;
	}

  /** Make a new Oval object between (lastX, lastY) and (x,y) */
	public Shape newOval(int x, int y){
		return null;
	}

  /** Make a new text shape object at (x,y), prompting the user for the string */
	public Shape newTextShape(int x, int y){
		return null;
	}

	/*public void vectorTester() {
		Enumeration test = v.elements();
		while (test.hasMoreElements()) {
			Shape stemp = (Shape) test.nextElement();
			System.out.println(stemp.toString());
		}
	}*/


}

