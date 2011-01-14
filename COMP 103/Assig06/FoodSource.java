import java.awt.Color;
import java.io.*;
import java.util.Enumeration;
import java.util.Comparator;


/** A Food Source is a (never ending) food source stuck on the ground.
		It comes in two types - growth food ("grow") and reproductive food ("split
		")It can draw itself, and return its position and type.
		It takes 10 ticks to regrow after it has been eaten.
		This is represented by its age, and mature() is only true when its age > 
		10;*/


public class FoodSource {

	private int size = 4;													 // the size of the food source
	private int x;											 //the FoodSource's location on the screen
	private int y;
	private String type;														// the kind of food
	private Color color;

	private static int timeToGrow = 20;
	private int age = timeToGrow;									 // is it old enough to eat?.

	private static int westEdge = 0;
				// the edges of the region beyond which the food can not exist
	private static int eastEdge = 500;
	private static int northEdge = 0;
	private static int southEdge = 500;



	public FoodSource() {
		x = (int)(Math.random() * (eastEdge - westEdge));
		y = (int)(Math.random() * (southEdge - northEdge));
		double r = Math.random();
		if (r < 0.15) {
			type = "poison";
			color = Color.magenta;
		}
		else if (r < 0.3) {
			type = "divisive";
			color = Color.blue;
		}
		else if (r < 0.4) {
			type = "badtaste";
			color = Color.black;
		}
		else if (r < 0.5) {
			type = "spicy";
			color = Color.red;
		}
		else {
			type = "nutritious";
			color = Color.green;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getType() {
		return type;
	}

	public boolean near(int otherX, int otherY) {
		return (Math.abs(x - otherX) < size && Math.abs(y - otherY) < size);
	}

	public boolean mature() {
		return (age >= timeToGrow);
	}

	public void grow() {
		if (age < timeToGrow)
			age++;
	}

	public void eaten() {
		age = 0;
	}

	public void redraw(DrawingCanvas canvas) {
		if (age < timeToGrow)
			canvas.setForeground(Color.lightGray);
		else
			canvas.setForeground(color);
		canvas.fillRect(x - size, y - size, size * 2, size * 2, false);
	}


}
