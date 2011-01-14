import java.awt.Color;
/** class for use in assignment 07. It provides objects which are models for a 
		car they have a x,y coordinates, a direction which should be "north", 
		"south", "east" or "west" and a color */
public class Car {
	private int x;
	private int y;
	private String direction = ""; // should be "north", "south", "east" or "west"
	private Color color = Color.red;

	public Car(String dir) {
		direction = dir;
		color = new Color((float)Math.random(), (float)Math.random(), (float)
											Math.random());
		if (dir.equals("east")) {
			x = 1;
			y = 240;
		}
		else if (dir.equals("west")) {
			x = 800;
			y = 280;
		}
		else if (dir.equals("south")) {
			x = 410;
			y = 1;
		}
		else if (dir.equals("north")) {
			x = 350;
			y = 530;
		}
		else
			throw new RuntimeException("direction may not be " + dir);
	}

	/** draw the car on a Drawing Canvas */
	public void draw(DrawingCanvas cvs) {
		cvs.setForeground(color);
		if (direction.equals("north")) {
			cvs.fillRect(x, y + 5, 10, 15, false);
			cvs.setForeground(color.brighter());
			cvs.fillRect(x, y, 10, 5, false);
		}
		else if (direction.equals("south")) {
			cvs.fillRect(x, y, 10, 15, false);
			cvs.setForeground(color.brighter());
			cvs.fillRect(x, y + 15, 10, 5, false);
		}
		else if (direction.equals("east")) {
			cvs.fillRect(x, y, 15, 10, false);
			cvs.setForeground(color.brighter());
			cvs.fillRect(x + 15, y, 5, 10, false);
		}
		else {
			cvs.fillRect(x + 5, y, 15, 10, false);
			cvs.setForeground(color.brighter());
			cvs.fillRect(x, y, 5, 10, false);
		}
	}

	public boolean move(int step) {
		boolean stillOnScr;
		if (direction.equals("east"))
			stillOnScr = (x < 770);
		else if (direction.equals("west"))
			stillOnScr = (x > 1);
		else if (direction.equals("south"))
			stillOnScr = (y < 520);
		else
			stillOnScr = (y > 1);											 // direction must be north

		if (stillOnScr) {														 // car is still on screen
			if (direction.equals("east"))
				x += step;
			else if (direction.equals("west"))
				x -= step;
			else if (direction.equals("south"))
				y += step;
			else
				y -= step;
		}
		return stillOnScr;
	}

	public boolean move(int step, int queSize) {
		boolean reachedWaitingQ = false;	 // a flag which tells us whether car has 
		// reached the traffic light queue yet

		int lengthWaitingCars = queSize * 20;	// how far back the waiting Q reaches

		if (direction.equals("east"))
			reachedWaitingQ = !(x < (300 - lengthWaitingCars));
		else if (direction.equals("west"))
			reachedWaitingQ = !(x > (440 + lengthWaitingCars));
		else if (direction.equals("south"))
			reachedWaitingQ = !(y < (190 - lengthWaitingCars));
		else
			reachedWaitingQ = !(y > (310 + lengthWaitingCars));
					// direction must be north

		if (!reachedWaitingQ) {
						// we haven't reached the waiting Q yet, so need to move the car 
						// forward
			if (direction.equals("east"))
				x = x + step;
			else if (direction.equals("west"))
				x = x - step;
			else if (direction.equals("south"))
				y = y + step;
			else
				y = y - step;
		}
		return reachedWaitingQ;
	}


	/** return the <I>x<I> coordinate of the top corner of the car*/
	public int getX() {
		return x;
	}

	/** return the <I>x<I> coordinate of the top corner of the car*/
	public int getY() {
		return y;
	}

	/** return a String rendering of the Car Object */
	public String toString() {
		return"Car: x is " + x + " y is " + y;
	}
}

