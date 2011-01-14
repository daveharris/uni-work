import java.awt.Color;
import jds.collection.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

/* Code for Assignment 3, July 2003
 * Name: 
 * Usercode: 
 * ID:
 *
 * Represents a PolyLine that consists of a collection of straight line segments.
 * It must have fields to store
 *	 - a collection (an Indexed or a Bag) of Line objects (one for each line segment)
 *	 - the initial point of the first line segment
 *		 (needed for the string representation of the PolyLine for writing to a file)
 *	 - the end point of the latest line segment to have been added
 *		 (needed when adding a new line segment to the PolyLine.
 *	 - the color
 * It implements the Shape interface, just like all the other shapes, but must have
 *	one additional method:
 *			addSegmentTo(int x, int y)
 *	which will add an additional line segment, starting at the end point of the latest
 *	line segment, and ending at (x, y).
 */


public class PolyLine implements Shape{
	private Vector lines = new Vector();
	private int x;		//initial point
	private int y;		//initial point
	private int endX;	//latest point to be added
	private int endY;	//latest point to be added
	private Color col = Color.white;


	/** Arguments are the position (x,y) of the start of the first line and the color.
	 *	It should set all the fields:
	 *	 the collection of Lines should be an empty collection
	 *	 both the starting point of the polyline, and the end point of the latest line should
	 *	 be set to (x, y)	.*/
	public PolyLine (int x, int y, Color col){
		this.x = x;
		this.y = y;
		this.col = col;
		endX = x;
		endY = y;
	}

	/** Argument is a string that contains the specification of the PolyLine.
			The first word in the string should be the word "PolyLine".
			The rest of the string should be two integers (the x and y of the first point)
			three integers specifying the color, followed by any even number of integers
			specifying the later points. eg, for a red polyline with four line segments
			"PolyLine 10 10 255 0 0 100 100 100 10 200 100 100 0"
			(this is a simple "sawtooth" polyline shape):
	 */
	public PolyLine (String params){
		Enumeration data = new StringTokenizer(params);
		String shapeType = ((String)data.nextElement()).toLowerCase();
		try {
			if (!shapeType.equals("polyline"))
	throw new Exception("Invalid shape for PolyLine: "+params);
			int x = Integer.parseInt((String)data.nextElement());
			int y = Integer.parseInt((String)data.nextElement());
			int red = Integer.parseInt((String)data.nextElement());
			int green = Integer.parseInt((String)data.nextElement());
			int blue = Integer.parseInt((String)data.nextElement());
			col = new Color(red, green, blue);
			while (data.hasMoreElements()){
	int x2= Integer.parseInt((String)data.nextElement());
	int y2= Integer.parseInt((String)data.nextElement());
	lines.addLast(new Line(x, y, x2, y2, col));
	x=x2;
	y=y2;
			}
		}
		catch (Exception ex){
			System.out.println("Invalid parameters for Line shape: "+params);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
	 
	/** Adds a new segment to the polyline, from the current end point to (u,v)
	 *	This should construct a new Line object and add it to the collection of lines
	 *	and also remember (u, v) as the position of the end of the latest line */
	public void addSegmentTo(int u, int v){
		lines.addLast(new Line(endX, endY, u, v, col));
		endX=u;
		endY=v;
	}

		/** Remove a segment from a polyline, this uses the removeLast method from 
	the Stack API implemented by Vector **/
		public void removeSegmentFrom() {
	if (!(lines.isEmpty()))
			lines.removeLast();
		}
 
	/** Returns true if the point (u, v) is on top of the shape
	 *	Works by checking whether the point is on any of the Lines in the collection */
	public boolean pointOnShape(int u, int v){
		for (Enumeration e = lines.elements(); e.hasMoreElements(); ){
			Line ln = (Line) e.nextElement();
			if (ln.pointOnShape(u,v))
	return true;
		}
		return false;
	}


	/** Changes the position of the shape by dx and dy.
	 *	If it was positioned at (x, y), it will now be at (x+dx, y+dy)
	 *	Should change the positions of the start and end points, and also change
	 *	the positions of each Line in the collection */
	public void moveBy(int dx, int dy){
		x += dx;
		y += dy;
		endX += dx;
		endY += dy;
		for (Enumeration e = lines.elements(); e.hasMoreElements(); ){
			Line ln = (Line) e.nextElement();
			ln.moveBy(dx, dy);
		}
	}

	/** Renders the polyline on a canvas by rendering each Line in the collection
	 *	on the canvas.
	 *	It does not redisplay the canvas - this must be done afterwards
	 *	in order to make the polyline appear
			*/
	public void render (DrawingCanvas canvas){
		canvas.setForeground(col);
		for (Enumeration e = lines.elements(); e.hasMoreElements(); ){
			Line ln = (Line) e.nextElement();
			ln.render(canvas);
		}
	}

	/** Returns a string description of the polyline in a form suitable for
			writing to a file in order to reconstruct the polyline later
			See the description of the second constructor above */
	public String toString(){
		String ans = "PolyLine "+x+" "+y+" "+col.getRed()+" "+col.getGreen()+" "+col.getBlue();
		for (Enumeration e = lines.elements(); e.hasMoreElements(); ){
			Line ln = (Line) e.nextElement();
			ans += " "+ln.getX2()+" "+ln.getY2();
		}
		return ans;
	}
			
}
