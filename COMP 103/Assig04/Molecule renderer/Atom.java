import java.awt.Color;
import java.util.Enumeration;
import java.util.StringTokenizer;

/* Code for Assignment 4, Aug 2003
 * Represents information about a particular atom in a molecule:
 *	 - type of atom (which kind of chemical element)
 *	 - position (x, y, z) coordinates
 */


public class Atom {

	private String type; // the type of atom 

	// coordinates of center of atom, relative to top left front corner
	private int x;			 // distance to the right (when looking from the front)
	private int y;			 // distance down	(when looking from the front)
	private int z;			 // distance away	(when looking from the front)



	/** params is a string containing type of atom
			and its position (x, y, z) coordinates, as above */

	public Atom (String params ){
		try{
			Enumeration info = new StringTokenizer(params);
			type = (String)info.nextElement();
			x = Integer.parseInt((String)info.nextElement());
			y = Integer.parseInt((String)info.nextElement());
			z = Integer.parseInt((String)info.nextElement());
		}
		catch (Exception ex){
			System.out.println("Invalid parameters for Atom: "+params);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public String getType(){
		return type;
	}

	/** Returns -ve number if this atom is left of (smaller x) than other,
	0 if have same x, and +ve if it is to the right	*/
	public int leftOf(Atom other){
		return (x - other.x);
	}

	/** Returns -ve number if this atom is above (larger y) than other,
	0 if have same y, and +ve if it is below	*/
	public int above(Atom other){
		return (y - other.y);
	}

	/** Returns -ve number if this atom is behind (larger z) than other,
	0 if have same z, and +ve if it is in front	*/
	public int behind(Atom other){
		return (other.z - z);
	}


	/** Render the atom on the canvas.
	 *	Parameters specify
	 *	 -	the canvas,
	 *	 -	the radius of the circle to draw
	 *	 -	the color
	 *	 -	the direction (a string) to render it from
	 *			
	 */

	public void render(DrawingCanvas canvas, int radius, Color col, String dir){
		int left=0;
		int top=0;
		int diam= radius*2;

		// The (x,y) coordinates on the canvas depends on the direction we
		if (dir.equals("Front")){
			left = x-radius;
			top = y-radius;
		}
		else if (dir.equals("Right")){
			left = z-radius;
			top = y-radius;
		}
		else if (dir.equals("Bottom")){
			left = x-radius;
			top = z-radius;
		}
		
		canvas.setForeground(col);
		canvas.fillOval(left, top, diam, diam);
		canvas.setForeground(Color.black);
		canvas.drawOval(left, top, diam, diam);
	}



}
