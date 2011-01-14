import java.awt.Color;

/* Code for Assignment 2, July 2003
 */


public interface Shape{

  /** Returns true if the point (x, y) is on top of the shape */
  public boolean pointOnShape(int x, int y);


  /** Changes the position of the shape by dx and dy.
   If it was positioned at (x, y), it will now be at (x+dx, y+dy)*/
  public void moveBy(int dx, int dy);

  /** Renders the shape on a canvas. 
      It does not redisplay the canvas - this must be done afterwards
      in order to make the rectangle appear*/

  public void render (DrawingCanvas canvas);

  /** Returns a string description of the shape in a form suitable for
      writing to a file in order to reconstruct the shape later */

  public String toString();
      
}
