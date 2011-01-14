import java.awt.Color;
import jds.collection.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

/* Code for Assignment 2, July 2003
 * Name: 
 * Usercode: 
 * ID: 
 */


public class Oval implements Shape{
	private int x;
	private int y;
	private int wd;
	private int ht;
	private Color col = Color.white;

	/** Arguments are the x and y of the top left corner, the
			width and height, and the color. */
	public Oval (int x, int y, int wd, int ht, Color col){
		this.x = x;
		this.y = y;
		this.wd = wd;
		this.ht = ht;
		this.col = col;
	}

	/** Argument is a string that contains the specification of the oval.
			The first word in the string should be the word "Oval".
			The rest of the string should be 7 integers (the x and y of the top
			left corner, and the width and height, and three ints specifying the color). */

	public Oval (String params){
		Enumeration data = new StringTokenizer(params);
		String shapeType = ((String)data.nextElement()).toLowerCase();
		try {
			if (!shapeType.equals("oval"))
	throw new Exception("Invalid shape for Oval: "+params);
			x = Integer.parseInt((String)data.nextElement());
			y = Integer.parseInt((String)data.nextElement());
			wd = Integer.parseInt((String)data.nextElement());
			ht = Integer.parseInt((String)data.nextElement());
			int red = Integer.parseInt((String)data.nextElement());
			int green = Integer.parseInt((String)data.nextElement());
			int blue = Integer.parseInt((String)data.nextElement());
			col = new Color(red, green, blue);
		}
		catch (Exception ex){
			System.out.println("Invalid parameters for Oval shape: "+params);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
		

	/** Returns true if the point (u, v) is on top of the shape */
	public boolean pointOnShape(int u, int v){
		double dx = u - (x+wd/2);		 // horizontal distance from center to point (u,v)
		double dy = v - (y+ht/2);	// vertical distance from center to point (u,v)
		double sqwd = 1.0*wd*wd;		// square of the width
		double sqht = 1.0*ht*ht;		// square of the height
		System.out.println(u+","+v+" ?on shape "+this);
		System.out.println("dx="+dx+"dy="+dy+"sqht="+sqht+"sqwd="+sqwd);
		System.out.println("dxdssqht="+(dx*dx*sqht)+"dydysqwd="+(dy*dy*sqwd));
		return (4*dx*dx*sqht + 4*dy*dy*sqwd	<= sqht*sqwd);
	}

	/** Changes the position of the shape by dx and dy.
	 If it was positioned at (x, y), it will now be at (x+dx, y+dy)*/
	public void moveBy(int dx, int dy){
		x += dx;
		y += dy;
	}

	/** Renders the oval on a canvas. It draws a black border and
			fills it with the color of the oval.
			It does not redisplay the canvas - this must be done afterwards
			in order to make the oval appear*/

	public void render (DrawingCanvas canvas){
		//System.out.println("drawing "+wd+"*"+ht+" at ("+x+","+y+")");
		canvas.setForeground(col);
		canvas.fillOval(x, y, wd, ht, false);
		canvas.setForeground(Color.black);
		canvas.drawOval(x, y, wd, ht, false);
	}

	/** Returns a string description of the oval in a form suitable for
			writing to a file in order to reconstruct the oval later */

	public String toString(){
		return ("Oval "+x+" "+y+" "+wd+" "+ht+" "+col.getRed()+" "+col.getGreen()+" "+col.getBlue());
	}

			
}
