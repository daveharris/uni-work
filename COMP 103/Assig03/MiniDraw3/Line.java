import java.awt.Color;
import jds.collection.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

/* The Line shape for Assignment 3, July 2003
   Represents a straight line.
   Implements the Shape interface.
   Has four additional methods that return the positions of each end point
   Some of these may be needed by the PolyLine class
 */


public class Line implements Shape{
  private int x1;
  private int y1;
  private int x2;
  private int y2;
  private Color col = Color.white;
  // extra fields to help with determining the distance of a point from a line
  private int wd;
  private int ht;
  private double ln;



  /** Arguments are the position (x1,y1) of one end and the position (x2,y2) of the other end,
      and the color. */
  public Line (int x1, int y1, int x2, int y2, Color col){
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.col = col;
    //precompute numbers to help compute distance of a point from the line
    wd = x2-x1;
    ht = y2-y1;
    ln = Math.sqrt(wd*wd+ht*ht);
  }

  /** Argument is a string that contains the specification of the line.
      The first word in the string should be the word "line".
      The rest of the string should be 7 integers (the x and y of the first end
      the x and y of the other end, and three integers that specify the color). */

  public Line (String params){
    Enumeration data = new StringTokenizer(params);
    String shapeType = ((String)data.nextElement()).toLowerCase();
    try {
      if (!shapeType.equals("line"))
	throw new Exception("Invalid shape for Line: "+params);
      x1 = Integer.parseInt((String)data.nextElement());
      y1 = Integer.parseInt((String)data.nextElement());
      x2 = Integer.parseInt((String)data.nextElement());
      y2 = Integer.parseInt((String)data.nextElement());
      int red = Integer.parseInt((String)data.nextElement());
      int green = Integer.parseInt((String)data.nextElement());
      int blue = Integer.parseInt((String)data.nextElement());
      col = new Color(red, green, blue);
      //precompute numbers to help compute distance of a point from the line
      wd = x2-x1;
      ht = y2-y1;
      ln = Math.sqrt(wd*wd+ht*ht);
    }
    catch (Exception ex){
      System.out.println("Invalid parameters for Line shape: "+params);
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }
    
  /** Returns true if the point (u, v) is on top of the shape */
  public boolean pointOnShape(int u, int v){
    double dist = distFromLine(u,v);
    //System.out.println("distance to ("+u+","+v+") = "+ dist);
    boolean ans = (u>=Math.min(x1,x2) && u<=Math.max(x1,x2) &&
		   v>=Math.min(y1,y2) && v<=Math.max(y1,y2) &&
		   dist <=3.0);
    //System.out.println("("+u+","+v+") "+(ans?"":"not")+ " on "+this);
    return ans;     
  }

  // Returns the distance of (u,v) from the line
  private double distFromLine(int u, int v){
    int du = u-x1;
    int dv = v-y1;
    if (ht==0) return Math.abs(dv);
    else if (wd==0) return Math.abs(du);
    else return Math.abs((dv*wd - du*ht)/ln);
  }
      

    /** Changes the position of the shape by dx and dy.
   If it was positioned at (x, y), it will now be at (x+dx, y+dy)*/
  public void moveBy(int dx, int dy){
    x1 += dx;
    y1 += dy;
    x2 += dx;
    y2 += dy;
  }

  /** Renders the line on a canvas. 
      It does not redisplay the canvas - this must be done afterwards
      in order to make the line appear*/

  public void render (DrawingCanvas canvas){
    //System.out.println("drawing "+wd+"*"+ht+" at ("+x+","+y+")");
    canvas.setForeground(col);
    canvas.drawLine(x1, y1, x2, y2, false);
  }

  /** Returns a string description of the line in a form suitable for
      writing to a file in order to reconstruct the line later */

  public String toString(){
    return ("Line "+x1+" "+y1+" "+x2+" "+y2+" "+col.getRed()+" "+col.getGreen()+" "+col.getBlue());
  }

  /** Additional methods may be required for the PolyLine class*/
  public int getX1(){
    return x1;
  }
  public int getY1(){
    return y1;
  }
  public int getX2(){
    return x2;
  }
  public int getY2(){
    return y2;
  }
      
}
