import java.awt.Color;
//import jds.collection.Vector;
import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

/* Code for Assignment 1, July 2003
 * Name: 
 * Usercode: 
 * ID: 
 */


public class Rectangle{
	private int x;
	private int y;
	private int wd;
	private int ht;
	private Color col = Color.white;

	/** Arguments are the x and y of the top left corner, the
			width and height, and the color. */
	public Rectangle (int x, int y, int wd, int ht, Color col){
		this.x = x;
		this.y = y;
		this.wd = wd;
		this.ht = ht;
		this.col = col;
	}

	/** Argument is a string that contains the specification of the rectangle.
			The first word in the string should be the word "rectangle".
			The rest of the string should be 4 integers (the x and y of the top
			left corner, and the width and height) and a string that is the name
			of the color. */

	public Rectangle (String params){
		Enumeration data = new StringTokenizer(params);
		String shapeType = ((String)data.nextElement()).toLowerCase();
		try {
			if (!shapeType.equals("rectangle"))
	throw new Exception("Invalid shape for Rectangle: "+params);
			x = Integer.parseInt((String)data.nextElement());
			y = Integer.parseInt((String)data.nextElement());
			wd = Integer.parseInt((String)data.nextElement());
			ht = Integer.parseInt((String)data.nextElement());
			String colorName = ((String)data.nextElement()).toLowerCase();
			if (colorName.equals("red"))				 col = Color.red;
			else if (colorName.equals("yellow")) col = Color.yellow;
			else if (colorName.equals("green"))	col = Color.green;
			else if (colorName.equals("blue"))	 col = Color.blue;
			else if (colorName.equals("black"))	col = Color.black;
			else if (colorName.equals("white"))	col = Color.white;
			else throw new Exception("Invalid colour: "+ colorName);
		}
		catch (Exception ex){
			System.out.println("Invalid parameters for Rectangle shape: "+params);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
		

	/** Returns the color of the rectangle as a value of the class Color */
	public Color getColor(){
		return col;
	}

	/** Returns the area of the rectangle as an integer */
	public int getArea(){
		return wd*ht;
	}


	/** Renders the rectangle on a canvas. It draws a black border and
			fills it with the color of the rectangle.
			It does not redisplay the canvas - this must be done afterwards
			in order to make the rectangle appear*/

	public void render (DrawingCanvas canvas){
		//System.out.println("drawing "+wd+"*"+ht+" at ("+x+","+y+")");
		canvas.setForeground(col);
		canvas.fillRect(x, y, wd, ht, false);
		canvas.setForeground(Color.black);
		canvas.drawRect(x, y, wd, ht, false);
	}

	/** Returns a string description of the rectangle in a form suitable for
			writing to a file in order to reconstruct the rectangle later */

	public String toString(){
		String colorName = "unknown";
		if (col == Color.red) colorName ="red";
		else if (col == Color.yellow) colorName ="yellow";
		else if (col == Color.green) colorName ="green";
		else if (col == Color.blue) colorName ="blue";
		else if (col == Color.black) colorName ="black";
		else if (col == Color.white) colorName ="white";
		return ("Rectangle "+x+" "+y+" "+wd+" "+ht+" "+colorName);
	}

			
}
