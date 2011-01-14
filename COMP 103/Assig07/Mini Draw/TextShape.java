import java.awt.Color;
import jds.collection.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

/* Code for Assignment 2, July 2003
 * Name: 
 * Usercode: 
 * ID: 
 */


public class TextShape implements Shape{
  private String str = "";
  private int x;
  private int y;
  private Color col = Color.white;

  /** Arguments are the string, the position, and the color. */
  public TextShape (String str, int x, int y, Color col){
    this.str = str;
    this.x = x;
    this.y = y;
    this.col = col;
  }

  /** Argument is a string that contains the specification of the TextShape.
      The first word in the string should be the word "TextShape".
      The rest of the string should be 5 ints (the x and y of the bottom 
      left corner and three ints that specify the color) followed by the text 
      The trickiness here is that the text may have spaces in it.*/

  public TextShape (String params){
    Enumeration data = new StringTokenizer(params);
    String shapeType = ((String)data.nextElement()).toLowerCase();
    try {
      if (!shapeType.equals("textshape"))
	throw new Exception("Invalid shape for TextShape: "+params);
      x = Integer.parseInt((String)data.nextElement());
      y = Integer.parseInt((String)data.nextElement());
      int red = Integer.parseInt((String)data.nextElement());
      int green = Integer.parseInt((String)data.nextElement());
      int blue = Integer.parseInt((String)data.nextElement());
      col = new Color(red, green, blue);
      int textStart=0; 
      for (int i=0; i<6; i++){textStart = 1+params.indexOf(" ",textStart);}
      str=params.substring(textStart);
    }
    catch (Exception ex){
      System.out.println("Invalid parameters for TextShape shape: "+params);
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }

  /** Returns true if the point (u, v) is on top of the shape */
  public boolean pointOnShape(int u, int v){
    //doing this properly would be nasty because we would have to find out the size of the font
    // and the size of the string when displayed in the font currently used by the canvas
    // Instead, this uses an approximation, based on the default font of the canvas
    return (u>=x && u<=x+(str.length()*10) && v>=y-13 && v<=y+3);
  }

  /** Changes the position of the shape by dx and dy.
   If it was positioned at (x, y), it will now be at (x+dx, y+dy)*/
  public void moveBy(int dx, int dy){
    x += dx;
    y += dy;
  }

  /** Renders the textshape on a canvas. It draws a black border and
      fills it with the color of the textshape.
      It does not redisplay the canvas - this must be done afterwards
      in order to make the textshape appear*/

  public void render (DrawingCanvas canvas){
    //System.out.println("drawing "+wd+"*"+ht+" at ("+x+","+y+")");
    canvas.setForeground(col);
    canvas.drawString(str, x, y, false);
    canvas.setForeground(Color.lightGray);
    canvas.drawRect(x,y-13,(str.length()*8),16);
  }

  /** Returns a string description of the textshape in a form suitable for
      writing to a file in order to reconstruct the textshape later */

  public String toString(){
    return ("TextShape "+x+" "+y+" "+col.getRed()+" "+col.getGreen()+" "+col.getBlue()+" "+str);
  }

      
}
