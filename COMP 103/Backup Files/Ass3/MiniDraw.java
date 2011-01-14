import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Enumeration;
import jds.Indexed;
import jds.collection.Vector;

public class MiniDraw implements ActionListener, MouseListener, MouseMotionListener{

	private DrawingCanvas canvas;
	private String brushShape = "Line";
	private Color currentColor = Color.black;
	private JButton colorButton;
	private PolyLine currentPoly;

	private Indexed shapes = new Vector();
	
	public static void main(String args[]){
		new MiniDraw();
	}

	public MiniDraw(){
		JFrame frame = new JFrame("MiniDraw");
		frame.setSize(900,600);

		//The graphics area
		canvas = new DrawingCanvas();
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		frame.getContentPane().add(canvas, BorderLayout.CENTER);


		//The buttons
		JPanel buttonPanelTop = new JPanel();
		JPanel buttonPanelBott = new JPanel();

		JButton newButton = new JButton("New");
		JButton openButton = new JButton("Open");
		JButton saveButton = new JButton("Save");
		JButton moveButton = new JButton("Move");
		JButton deltButton = new JButton("Delete");
		JButton lineButton = new JButton("Line");
		JButton rectButton = new JButton("Rect");
		JButton ovalButton = new JButton("Oval");
		JButton textButton = new JButton("Text");
		JButton polyButton = new JButton("PolyLine");
		JButton colrButton = new JButton("Color");
		JButton quitButton = new JButton("Quit");

		newButton.addActionListener(this);
		openButton.addActionListener(this);
		saveButton.addActionListener(this);
		moveButton.addActionListener(this);
		deltButton.addActionListener(this);
		lineButton.addActionListener(this);
		rectButton.addActionListener(this);
		ovalButton.addActionListener(this);
		textButton.addActionListener(this);
		polyButton.addActionListener(this);
		colrButton.addActionListener(this);
		quitButton.addActionListener(this);

		buttonPanelTop.add(newButton);
		buttonPanelTop.add(openButton);
		buttonPanelTop.add(saveButton);
		buttonPanelTop.add(moveButton);
		buttonPanelTop.add(deltButton);
		buttonPanelBott.add(lineButton);
		buttonPanelBott.add(rectButton);
		buttonPanelBott.add(ovalButton);
		buttonPanelBott.add(textButton);
		buttonPanelBott.add(polyButton);
		buttonPanelTop.add(colrButton);
		buttonPanelTop.add(quitButton);

		frame.getContentPane().add(buttonPanelTop, BorderLayout.NORTH);
		frame.getContentPane().add(buttonPanelBott, BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	/* Respond to button presses */

	public void actionPerformed(ActionEvent e){
		//System.out.println("Button: " + e.getActionCommand());
		currentPoly = null;
		if (e.getActionCommand().equals("New") )
			newDrawing();
		else if (e.getActionCommand().equals("Open") )
		 openDrawing();
		else if (e.getActionCommand().equals("Save") )
			saveDrawing();
		// the move action is extension
		else if (e.getActionCommand().equals("Color") )
			selectColor();
		else if (e.getActionCommand().equals("Quit") )
			System.exit(0);
		else
			brushShape = e.getActionCommand();
	}

	private void selectColor(){
		Color newColor = JColorChooser.showDialog(null,"Choose Color for new shapes", currentColor);
		if (newColor!=null){
			currentColor=newColor;
			colorButton.setBackground(currentColor);
			canvas.setForeground(currentColor);
		}
	}

	/* Respond to mouse events */

	private int lastX;
	private int lastY;

	private int movingX;	// Rubberbanding
	private int movingY;	// Rubberbanding

	public void mousePressed(MouseEvent e) {
		//System.out.println("Pressed at ("+ e.getX() +" "+ e.getY() +")");
		lastX = e.getX();
		lastY = e.getY();
		movingX = lastX;	 // Rubberbanding
		movingY = lastY;	 // Rubberbanding
		invertRubberBand(movingX, movingY);	 // Rubberbanding
	}

	public void mouseReleased(MouseEvent e) {
		//System.out.println("Released at ("+ e.getX() +" "+ e.getY() +")");
		invertRubberBand(movingX, movingY);	 // Rubberbanding
		// Move is extension
		if (brushShape.equals("Move")){
			moveShape(e.getX(), e.getY());
		}
		else if (brushShape.equals("Delete")){
			deleteShape(e.getX(), e.getY());
		}
		else 
			drawShape(e.getX(), e.getY()); 
	}

	public void mouseClicked(MouseEvent e) {}	//needed to satisfy interface
	public void mouseEntered(MouseEvent e) {}	//needed to satisfy interface
	public void mouseExited(MouseEvent e) {}	 //needed to satisfy interface

	/* Rubberbanding: Respond to mouse motion */
	public void mouseDragged(MouseEvent e) {
		//System.out.println("Dragged to ("+ e.getX() +" "+ e.getY() +")");
		invertRubberBand(movingX, movingY);
		movingX = e.getX();
		movingY = e.getY();
		invertRubberBand(movingX, movingY);
	}
		public void mouseMoved(MouseEvent e) {}	 //needed to satisfy interface
	
	private void invertRubberBand(int x, int y){
		if (brushShape.equals("Line"))
			canvas.invertLine(lastX, lastY, x, y);
		else if (brushShape.equals("Rect") || brushShape.equals("Oval")){
			int left= Math.min(lastX, x);
			int top= Math.min(lastY, y);
			int width= Math.abs(lastX - x);
			int height= Math.abs(lastY - y);
			canvas.invertRect(left, top, width, height);
		}
	}

	
	/* Helper methods for implementing the button actions */

	/** Clear the canvas and render all the shapes in the drawing */
	public void render(){
		canvas.clear(false);
		for (Enumeration e = shapes.elements(); e.hasMoreElements(); ){
			Shape s = (Shape) e.nextElement();
			s.render(canvas);
		}
		canvas.display();
	}

	/** Start a new drawing. */
	public void newDrawing(){
		shapes = new Vector();
		render();
	}


	/** Open a drawing file, and read all the shape descriptions into the current drawing. */
	public void openDrawing(){
		try {
			shapes = new Vector();
			String fname = FileDialog.open();
			BufferedReader f = new BufferedReader(new FileReader(fname));
			//System.out.println("Opening file "+fname);
			while (true){
				String params = f.readLine();
				if (params==null) break;	//end of file
				Shape s;
				if (params.startsWith("Line")) s = new Line(params);
				else if (params.startsWith("Rectangle")) s = new Rectangle(params);
				else if (params.startsWith("Oval")) s = new Oval(params);
				else if (params.startsWith("TextShape")) s = new TextShape(params);
				else if (params.startsWith("PolyLine")) s = new PolyLine(params);
				else throw new RuntimeException("Unrecognised shape in drawing file");
					shapes.addElementAt(s, shapes.size());
			}
			f.close();
			render();
			//System.out.println("Read "+shapes.size()+" shapes");
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void saveDrawing(){
		try {
			String fname = FileDialog.save();
			PrintWriter f = new PrintWriter(new FileWriter(fname));
			for (Enumeration e = shapes.elements(); e.hasMoreElements(); ){
				Shape s = (Shape) e.nextElement();
				f.println(s.toString());
				//System.out.println("Saving "+s+" to "+fname);
			}
			f.close();
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/* Helper methods for implementing the mouse actions */

	/** Moves the shape that was under the mousePressed position (lastX, lastY)
			to where the mouse was released.	Ie, move it by (newX-lastX) and (newY-lastY)
	*/
	public void moveShape(int newX, int newY){
		//System.out.println("Moving shape under ("+lastX+","+lastY+") to ("+lastX+","+lastY+")");
		//Hint: check the methods in the Shape interface.

		//Run down all the shapes in the drawing (looking at the ones on top first!) to find
		//	the shape that (lastX, lastY) is on.	Change the position of the shape,
		//	then render the drawing again
		//If not pressed on any shape, then do nothing.
		for (int i=shapes.size()-1; i>=0; i--){
			Shape s = (Shape) shapes.elementAt(i);
			if (s.pointOnShape(lastX, lastY)){
				s.moveBy((newX-lastX), (newY-lastY));
				render();
				break;
			}
		}
	}


	/** Deletes the shape that was under the mouseReleased position (x, y)
	*/
	public void deleteShape(int x, int y){
		//System.out.println("Deleting shape under ("+x+","+y+")");
		//Hint: check the methods in the Shape interface.
		//Find the index of the shape that (lastX, lastY) is on.
		//and remove the shape from the drawing, then render the drawing again
		for (int i=shapes.size()-1; i>=0; i--){
			Shape s = (Shape)shapes.elementAt(i);
			if (s.pointOnShape(x, y)){
				shapes.removeElementAt(i);
				render();
				break;
			}
		}
		//If not pressed on any shape, then do nothing.
	}

	public void drawShape(int x, int y){
		//System.out.println("Drawing shape "+brushShape+" at "+lastX+" "+lastY+" "+x+" "+y);
		//construct a new Shape object of the appropriate kind (depending on brushShape)
		//Use the appropriate constructor of the Line, Rectangle, Oval, or TextShape classes.
		Shape shape = null;
		if (brushShape.equals("Line"))
			shape = new Line(lastX, lastY, x, y, currentColor);
		else if (brushShape.equals("Rect"))
			shape = newRectangle(x, y);
		else if (brushShape.equals("Oval"))
			shape = newOval(x, y);
		else if (brushShape.equals("Text"))
			shape = newTextShape(x, y);
		else if (brushShape.equals("PolyLine"))
			shape = newPolyLine(x,y);
		else
			throw new RuntimeException("Unknown brush shape: "+brushShape);
		//render the shape on the canvas
		shape.render(canvas);
		canvas.display();
		//add it to the collection of shapes in the drawing
		shapes.addElementAt(shape, shapes.size());
	}

	/** Make a new Rectangle object between (lastX, lastY) and (x,y) */
	public Shape newRectangle(int x, int y){
		int left= Math.min(lastX, x);
		int top= Math.min(lastY, y);
		int width= Math.abs(lastX - x);
		int height= Math.abs(lastY - y);
		return new Rectangle(left, top, width, height, currentColor);
	}

	/** Make a new Oval object between (lastX, lastY) and (x,y) */
	public Shape newOval(int x, int y){
		int left= Math.min(lastX, x);
		int top= Math.min(lastY, y);
		int width= Math.abs(lastX - x);
		int height= Math.abs(lastY - y);
		return new Oval(left, top, width, height, currentColor);
	}
	public Shape newPolyLine(int x, int y){
		if(currentPoly == null)
			currentPoly = new PolyLine(x, y, currentColor);
		else {
			shapes.removeElementAt(shapes.size()-1);
			currentPoly.addSegmentTo(x,y);
		}
		return currentPoly;
	}
	/** Make a new text shape object at (x,y), prompting the user for the string */
	public Shape newTextShape(int x, int y){
		String str = JOptionPane.showInputDialog("string:");
		return new TextShape(str, x, y, currentColor);
	}


}

