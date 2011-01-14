import java.awt.Color;
import java.util.Enumeration;
import java.util.StringTokenizer;

/* Code for Assignment 4, July 2003
 * Represents information about different types of atoms for drawing them.
 *	- type of atom
 *	- size (radius) that this type of atom should be rendered
 *	- color that this type of atom should be rendered
 */


public class AtomTypeInfo {
	private String type;
	private int size;	 // the radius of the atom
	private Color col;

	/** info is a string with the atom size (diameter) and color (three ints). */
	public AtomTypeInfo (String params){
		try{
			Enumeration data = new StringTokenizer(params);
			type = (String)data.nextElement();
			size = Integer.parseInt((String)data.nextElement());
			int red = Integer.parseInt((String)data.nextElement());
			int green = Integer.parseInt((String)data.nextElement());
			int blue = Integer.parseInt((String)data.nextElement());
			col = new Color(red, green, blue);
		}
		catch (Exception ex){
			System.out.println("Invalid parameters for atom information: "+params);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
		

	public String getType(){
		return type;
	}

	public int getSize(){
		return size;
	}

	public Color getColor(){
		return col;
	}

}
