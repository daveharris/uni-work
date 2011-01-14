import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Enumeration;
import jds.Stack;
import jds.Indexed;
import jds.collection.Vector;

public class MiniDrawExt implements ActionListener, MouseListener,
		MouseMotionListener {

	private DrawingCanvas canvas;
	private String brushShape = "Line";
	private Color currentColor = Color.black;
	private JButton colorButton;

	private Indexed shapes = new Vector();
	private PolyLine currentPoly = null;						// PolyLine

	private Stack history = new Vector();// The history of all the actions the user has done


	public static void main(String args[]) {
		new MiniDrawExt();
	}

	public MiniDrawExt() {
		JFrame frame = new JFrame("MiniDraw");
		frame.setSize(800, 600);

		//The graphics area
		canvas = new DrawingCanvas();
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		frame.getContentPane().add(canvas, BorderLayout.CENTER);


		//The buttons
		JPanel buttonPanel = new JPanel();
		JPanel shapePanel = new JPanel();
		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
		frame.getContentPane().add(shapePanel, BorderLayout.SOUTH);


		addButton(buttonPanel, "New");
		addButton(buttonPanel, "Open");
		addButton(buttonPanel, "Save");
		addButton(buttonPanel, "Move");
		addButton(buttonPanel, "Delete");
		colorButton = addButton(buttonPanel, "Color");
		addButton(buttonPanel, "Undo");
		addButton(buttonPanel, "Quit");


		addButton(shapePanel, "Line");
		addButton(shapePanel, "Poly");				// PolyLine
		addButton(shapePanel, "Rect");
		addButton(shapePanel, "Oval");
		addButton(shapePanel, "Text");
		
		frame.setVisible(true);
	}

	private JButton addButton(JPanel panel, String name) {
		JButton button = new JButton(name);
		button.addActionListener(this);
		panel.add(button);
		return button;
	}

	/* Respond to button presses */

	public void actionPerformed(ActionEvent e) {
		debug("Button: " + e.getActionCommand());
		if (currentPoly != null) {
						// PolyLine: end the current polyline, if there is one
			currentPoly = null;												 // PolyLine
		}
		if (e.getActionCommand().equals("New"))
			newDrawing();
		else if (e.getActionCommand().equals("Open"))
			openDrawing();
		else if (e.getActionCommand().equals("Save"))
			saveDrawing();
		else if (e.getActionCommand().equals("Color"))
			selectColor();
		else if (e.getActionCommand().equals("Quit"))
			System.exit(0);
		else if (e.getActionCommand().equals("Undo"))
			undo();
		else
			brushShape = e.getActionCommand();
	}

	private void selectColor() {
		Color newColor =
				JColorChooser.showDialog(null, "Choose Color for new shapes",
																 currentColor);
		if (newColor != null) {
			currentColor = newColor;
			colorButton.setBackground(currentColor);
			canvas.setForeground(currentColor);
		}
	}

	/* Respond to mouse events */

	private int lastX;										//where the mouse was pressed
	private int lastY;										//where the mouse was pressed

	public void mousePressed(MouseEvent e) {
		lastX = e.getX();
		lastY = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		debug("Released at (" + e.getX() + " " + e.getY() + ")");
		if (brushShape.equals("Move")) {
			moveShape(e.getX(), e.getY());
		}
		else if (brushShape.equals("Delete")) {
			deleteShape(e.getX(), e.getY());
		}
		else
			drawShape(e.getX(), e.getY());
	}

	public void mouseClicked(MouseEvent e) {
	}																 //needed to satisfy interface
	public void mouseEntered(MouseEvent e) {
	}																 //needed to satisfy interface
	public void mouseExited(MouseEvent e) {
	}																 //needed to satisfy interface

	/* Rubberbanding: Respond to mouse motion */
	public void mouseDragged(MouseEvent e) {
	}
	public void mouseMoved(MouseEvent e) {
	}


	/* Helper methods for implementing the button actions */

	/** Clear the canvas and render all the shapes in the drawing */
	public void render() {
		canvas.clear(false);
		for (Enumeration e = shapes.elements(); e.hasMoreElements(); ) {
			Shape s = (Shape)e.nextElement();
			s.render(canvas);
		}
		canvas.display();
	}

	/** Start a new drawing. */
	public void newDrawing() {
		shapes = new Vector();
		render();
	}


	/** Open a drawing file, and read all the shape descriptions into the current 
		* drawing. 
		*/
	public void openDrawing() {
		try {
			shapes = new Vector();
			String fname = FileDialog.open();
			BufferedReader f = new BufferedReader(new FileReader(fname));
			debug("Opening file " + fname);
			while (true) {
				String params = f.readLine();
				if (params == null)
					break;											//end of file
				Shape s;
				if (params.startsWith("Line"))
					s = new Line(params);
				else if (params.startsWith("Rectangle"))
					s = new Rectangle(params);
				else if (params.startsWith("Oval"))
					s = new Oval(params);
				else if (params.startsWith("TextShape"))
					s = new TextShape(params);
				else if (params.startsWith("PolyLine"))
					s = new PolyLine(params);					 // PolyLine
				else
					throw new RuntimeException("Unrecognised shape in drawing file");
				shapes.addElementAt(s, shapes.size());
			}
			f.close();
			render();
			debug("Read " + shapes.size() + " shapes");
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void saveDrawing() {
		try {
			String fname = FileDialog.save();
			PrintWriter f = new PrintWriter(new FileWriter(fname));
			for (Enumeration e = shapes.elements(); e.hasMoreElements(); ) {
				Shape s = (Shape)e.nextElement();
				f.println(s.toString());
				debug("Saving " + s + " to " + fname);
			}
			f.close();
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}


	/* Helper methods for implementing the mouse actions */

	/** Moves the shape that was under the mousePressed position (lastX, lastY)
			to where the mouse was released.	Ie, move it by (newX-lastX) and (newY-
			lastY)*/
	public void moveShape(int newX, int newY) {
		debug("Moving shape under (" + lastX + "," + lastY + ") to (" + lastX +
					"," + lastY + ")");
		// Run down all the shapes in the drawing to find the shape that (lastX, 
		// lastY) is on.
		// Change the position of the shape,	then render the drawing again
		for (int i = shapes.size() - 1; i >= 0; i--) {
			Shape s = (Shape)shapes.elementAt(i);
			if (s.pointOnShape(lastX, lastY)) {
				s.moveBy((newX - lastX), (newY - lastY));
				// Add the required action to the action stack
				Action newAction = new Action(i, s, brushShape,(newX - lastX),(newY - lastY));
				history.addLast(newAction);
				render();
				break;
			}
		}
	}


	/** Deletes the shape that was under the mouseReleased position (x, y)*/
	public void deleteShape(int x, int y) {
		debug("Deleting shape under (" + x + "," + y + ")");
		//Find the index of the shape that (lastX, lastY) is on.
		//and remove the shape from the drawing, then render the drawing again
		for (int i = shapes.size() - 1; i >= 0; i--) {
			Shape s = (Shape)shapes.elementAt(i);
			if (s.pointOnShape(x, y)) {
				shapes.removeElementAt(i);
				Action newAction = new Action(i, s, brushShape);
				history.addLast(newAction);
				render();
				break;
			}
		}
	}


	public void drawShape(int x, int y) {
		int polyCounter = 0;
		debug("Drawing shape " + brushShape + " at " + lastX + " " + lastY + " " +
					x + " " + y);
		//construct a new Shape object of the appropriate kind (depending on 
		// brushShape)
		//If making a polyline, then either start a new polyline shape (if there is 
		// not a current one)
		// or add a new point to the current polyline and redisplay it
		//Else use the appropriate constructor of the Line, Rectangle, Oval, or 
		// TextShape classes.
		Shape shape = null;
		if (brushShape.equals("Poly")) {							// PolyLine
			if (currentPoly == null) {								// start a polyline
				currentPoly = new PolyLine(x, y, currentColor);		// PolyLine
				shapes.addElementAt(currentPoly, shapes.size());	// PolyLine
				history.addLast(new Action(polyCounter, currentPoly, brushShape));
			}														// PolyLine
			else {													// PolyLine
				currentPoly.addSegmentTo(x, y);					 	// PolyLine
				history.addLast(new Action(++polyCounter, currentPoly, brushShape));
				currentPoly.render(canvas);							// PolyLine
			}
		}
		else {
			if (brushShape.equals("Line"))
				shape = new Line(lastX, lastY, x, y, currentColor);
			else if (brushShape.equals("Rect"))
				shape = newRectangle(x, y);
			else if (brushShape.equals("Oval"))
				shape = newOval(x, y);
			else if (brushShape.equals("Text")) {
				String str = JOptionPane.showInputDialog("String:");
				shape = new TextShape(str, x, y, currentColor);
			}
			else
				throw new RuntimeException("Unknown brush shape: " + brushShape);
			// Add it to the collection of shapes in the drawing
			shapes.addElementAt(shape, shapes.size());
			shape.render(canvas);
			Action newAction = new Action(shapes.size()-1, shape, brushShape);
			history.addLast(newAction);
		}
		// render the shape on the canvas
		canvas.display();
	}

	/** Make a new Rectangle object between (lastX, lastY) and (x,y) */
	public Shape newRectangle(int x, int y) {
		int left = Math.min(lastX, x);
		int top = Math.min(lastY, y);
		int width = Math.abs(lastX - x);
		int height = Math.abs(lastY - y);
		return new Rectangle(left, top, width, height, currentColor);
	}

	/** Make a new Oval object between (lastX, lastY) and (x,y) */
	public Shape newOval(int x, int y) {
		int left = Math.min(lastX, x);
		int top = Math.min(lastY, y);
		int width = Math.abs(lastX - x);
		int height = Math.abs(lastY - y);
		return new Oval(left, top, width, height, currentColor);
	}


	private void debug(String s) {
		//System.out.println(s);
	}

	private void undo () {
		if(!history.isEmpty()) {
			Action top = (Action)history.getLast();
			String str = top.actionType;
			if(str.equals("Line") || str.equals("Oval")
					|| str.equals("Rect") || str.equals("Text"))
				shapes.removeElementAt(shapes.size()-1);
			else if (str.equals("Poly")) {
				PolyLine last = (PolyLine)shapes.elementAt(shapes.size()-1);
				last.removeSegmentFrom();
			}
			else if(str.equals("Move")) {
				Shape last = (Shape)shapes.elementAt(shapes.size()-1);
				last.moveBy(-top.mvx, -top.mvy);
				shapes.setElementAt(last, shapes.size()-1);
			}
			else if(str.equals("Delete")) {
				Action newAction = (Action)history.getLast();
				shapes.addElementAt(newAction.shape, shapes.size());
			}
			render();
			history.removeLast();
		}
	}
}

