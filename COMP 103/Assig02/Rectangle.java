 import java.awt.Color;
import jds.collection.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

/* Code for Assignment 2, July 2003
 * Name: 
 * Usercode: 
 * ID: 
 */


public class Rectangle implements Shape{
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
      left corner, and the width and height) and three ints specifying the color. */

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
      int red = Integer.parseInt((String)data.nextElement());
      int green = Integer.parseInt((String)data.nextElement());
      int blue = Integer.parseInt((String)data.nextElement());
      col = new Color(red, green, blue);
    }
    catch (Exception ex){
      System.out.println("Invalid parameters for Rectangle shape: "+params);
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }
    

  /** Returns true if the point (u, v) is on top of the shape */
  public boolean pointOnShape(int u, int v){
    return (u>=x && v>=y && (u-x)<= wd && (v-y)<=ht);
  }

  /** Changes the position of the shape by dx and dy.
   If it was positioned at (x, y), it will now be at (x+dx, y+dy)*/
  public void moveBy(int dx, int dy){
    x += dx;
    y += dy;
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
    return ("Rectangle "+x+" "+y+" "+wd+" "+ht+" "+col.getRed()+" "+col.getGreen()+" "+col.getBlue());
  }

      
}
