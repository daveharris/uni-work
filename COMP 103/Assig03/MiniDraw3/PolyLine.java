import java.awt.Color;
import jds.collection.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

/* Code for Assignment 3, July 2003
 * Name: David Harris
 * Usercode: harrisdavi3
 * ID:300069566
 *
 * Represents a PolyLine that consists of a collection of straight line segments.
 * It must have fields to store
 *	 - a collection (an Indexed or a Bag) of Line objects (one for each line segment)
 *	 - the initial point of the first line segment
 *		 (needed for the string representation of the PolyLine for writing to a file)
 *	 - the end point of the lastest line segment to have been added
 *		 (needed when adding a new line segment to the PolyLine.
 *	 - the color
 * It implements the Shape interface, just like all the other shapes, but must have
 *	one additional method:
 *			addSegmentTo(int x, int y)
 *	which will add an additional line segment, starting at the end point of the latest
 *	line segment, and ending at (x, y).
 * 
 */


public class PolyLine implements Shape{
	// FIELDS
	//Create a new vector, the x, y co-ordinates
	// and the colour and the canvas
	private Vector lineVector;
	private int x1, y1, x2, y2;
	private Color currentColor;
	private DrawingCanvas canvas = new DrawingCanvas();
	
	/** Constructor 1: Arguments are the position (x,y) of the start of the
	 *	first line and the color.
	 *	It should set all the fields:
	 *	 the collection of Lines should be an empty collection
	 *	 both the starting point of the polyline, and the end point of the latest line should
	 *	 be set to (x, y)	.*/
	public PolyLine (int x, int y, Color col){
		//PolyLine constructor
		//Initializes the x, y points and uses the Line class
		//Adds the points to the vector
		lineVector = new Vector();
		currentColor = col;
		x1 = x;
		y1 = y;
		x2 = x;
		y2 = y;
		Line newPoly = new Line(x1, y1, x2, y2, currentColor);
		lineVector.addLast(newPoly);
	}

	/** Constructor 2: Argument is a string that contains the specification
	of the PolyLine.
	The first word in the string should be the word "PolyLine".
	The rest of the string should be two integers (the x and y of the first point)
	three integers specifying the color, followed by any even number of integers
	specifying the later points. eg, for a red polyline with four line segments
	"PolyLine 10 10 255 0 0 100 100 100 10 200 100 100 0"
	(this is a simple "sawtooth" polyline shape):
	 */
	public PolyLine (String params){
		//PolyLine constructor allowing the shape to be drawn from file
		lineVector = new Vector();
		Enumeration data = new StringTokenizer(params);
		String shapeType = ((String)data.nextElement()).toLowerCase();
		try {
			if (!shapeType.equals("polyline"))
				throw new Exception("Invalid shape for PolyLine: "+params);
			x1 = Integer.parseInt((String)data.nextElement());
			y1 = Integer.parseInt((String)data.nextElement());
			int red = Integer.parseInt((String)data.nextElement());
			int green = Integer.parseInt((String)data.nextElement());
			int blue = Integer.parseInt((String)data.nextElement());
			currentColor = new Color(red, green, blue);
			x2 = x1;
			y2 = y1;
			Line newPoly = new Line(x1, y1, x2, y2, currentColor);
			lineVector.addLast(newPoly);
			while(data.hasMoreElements()) {
				int u = Integer.parseInt((String)data.nextElement());
				int v = Integer.parseInt((String)data.nextElement());
				//Actually adds the new line part to existing line
				addSegmentTo(u, v);
				//Updates the x, y co-ords
				x2 = u;
				y2 = v;
			}
		}
		catch (Exception ex){
			//catches all expections
			System.out.println("Invalid parameters for PolyLine shape: "+params);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
	 
	/** Adds a new segment to the polyline, from the current end point to (u,v)
	 *	This should construct a new Line object and add it to the collection of lines
	 *	and also remember (u, v) as the position of the end of the latest line */
	public void addSegmentTo(int u, int v){
		//Adds a new line object to the vector
		Line L = new Line(x2, y2, u, v, currentColor);
		lineVector.addLast(L);
		//Updates the x, y co-ords
		x2 = u;
		y2 = v;
	}

	/** Returns true if the point (u, v) is on top of the shape
	 *	Works by checking whether the point is on any of the Lines in the collection */
	public boolean pointOnShape(int u, int v){
		//Used for delete and move shape, to find out if the 
		//mouse was clicked on the shape 
		for (Enumeration lines = lineVector.elements(); lines.hasMoreElements(); ) {
			Line l = (Line) lines.nextElement();
			if (l.pointOnShape(u, v))
				return true;
		}
		return false;
	}

	/** Changes the position of the shape by dx and dy.
	 *	If it was positioned at (x, y), it will now be at (x+dx, y+dy)
	 *	Should change the positions of the start and end points, and also change
	 *	the positions of each Line in the collection */
	public void moveBy(int dx, int dy){
		//Move the shape by the amount passed as arguments
		//and updates x, y co-ords.
		for (Enumeration lines = lineVector.elements(); lines.hasMoreElements(); ) {
			Line L = (Line) lines.nextElement();
			L.moveBy(dx, dy);
		}
		x1 += dx;
		y1 += dy;
		x2 += dx;
		y2 += dy;
	}

	/** Renders the polyline on a canvas by rendering each Line in the collection
	 *	on the canvas.
	 *	It does not redisplay the canvas - this must be done afterwards
	 *	in order to make the polyline appear
			*/
	public void render (DrawingCanvas canvas){
		//Adding the shape to the canvas
		for (Enumeration lines = lineVector.elements(); lines.hasMoreElements(); ) {
			Line L = (Line) lines.nextElement();
			L.render(canvas);
		}
	}

	/** Returns a string description of the polyline in a form suitable for
	writing to a file in order to reconstruct the polyline later
	See the description of the second constructor above */
	public String toString(){
		//Converts the shape object to a string, 
		//so can be read back into the program.  Has to be stored in a special way
		String saveString = ("PolyLine "+x1+" "+y1+" "+currentColor.getRed()+" "+
			currentColor.getGreen()+" "+currentColor.getBlue());
		for (Enumeration lines = lineVector.elements(); lines.hasMoreElements(); ) {
			Line L = (Line) lines.nextElement();
			saveString += (" "+  L.getX2() + " " + L.getY2());
		}
		return saveString;
	}
}