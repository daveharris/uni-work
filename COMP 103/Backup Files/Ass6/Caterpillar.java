import java.awt.Color;
import java.io.*;
import java.util.Enumeration;
import java.util.Comparator;


/** A Caterpillar is a sequence of segments, represented as a "Lisp type list".
		Each segment knows
		- its position on the screen,
		- its color
		- the direction it will move next
		- whether it should have a face on it
		- and the next segment in the caterpillar.
		A caterpillar can
		be created:
		grow:	(adds another segment at the end)
		shrink: (loses the segment just behing the head)
		split:	splits itself in half, returning the new caterpillar
		reverse:	completely reverses its direction, returning the new head (which
		was the tail)*/


public class Caterpillar {

	private int size = 12;											 // the radius of all the segments

	private static int westEdge = 10;
				// the edges of the region where the caterpillars should turn round
	private static int eastEdge = 490;
	private static int northEdge = 10;
	private static int southEdge = 490;

	private int x;													//the segment's location on the screen
	private int y;

	private String direction;				// Which direction the segment just moved in.
	private Color color;														// What colour to draw it in.
	private boolean face;
				// whether it is the first segment of a caterpillar and should have a 
				// face

	private Caterpillar next;							 // the next segment of the caterpillar

	// There are two constructors

	/** Constructor for the head of a new caterpillar.
			A new caterpillar should 
			be just two segments long,
			be in a random position,
			have a random direction,
			have a random color (which fades along the segments - each segment is a 
			bit more grey than the previous one)*/
	public Caterpillar() {
		x = (int)(Math.random() * (eastEdge - westEdge));
		y = (int)(Math.random() * (southEdge - northEdge));

		double n = Math.random();
		if (n < 0.25)
			direction = "north";
		else if (n < 0.5)
			direction = "east";
		else if (n < 0.75)
			direction = "south";
		else
			direction = "west";

		color = Color.getHSBColor((float)Math.random(), 1.0f, 1.0f);
		face = true;
		next = new Caterpillar(x, y, direction, color);
	}

	/** Constructor for a segment to be attached to a previous segment:
			- position is one radius over from the x and y of the previous segment
			(in the opposite direction from the prevDir),
			- moving in the same direction as the previous segment
			- color ought to be the next shade through the spectrum from the 
			previous segment*/
	public Caterpillar(int prevX, int prevY, String prevDir, Color prevColor) {
		color = nextShade(prevColor);
		x = prevX;
		y = prevY;
		if (prevDir.equals("north"))
			y = y + size;
		else if (prevDir.equals("south"))
			y = y - size;
		else if (prevDir.equals("east"))
			x = x - size;
		else if (prevDir.equals("west"))
			x = x + size;
		direction = prevDir;
		next = null;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean near(int otherX, int otherY) {
		return (Math.abs(x - otherX) < size && Math.abs(y - otherY) < size);
	}


	/** return the size (number of segments) of the caterpillar */
	public int size() {
		// Your Code Here
		int ans = 0;
		for (Caterpillar rest = this; rest != null; rest = rest.next)
			ans++;
		return ans;
	}

	/** move: the caterpillar should move along, with each segement following the 
		one in front of it. That means that each segment must move in the
		direction the previous segment moved the last time.
		The version of move with no arguments works out a new direction
		to move (most commonly the same as its current direction) then calls
		move(newDirection).
		It will turn if it is over the edge, or with a 0.1 probability
		The version of move with a new direction first tells the semgent after
		it to move in this segement's current direction, then it moves itself in
		the new direction, and remembers this new direction.*/

	public void move() {
		String newDir = getNewDirection();
		move(newDir);
	}


	public void move(String newDir) {
		if (next != null)
			next.move(direction);
		direction = newDir;
		if (direction.equals("north"))
			y = y - size;
		else if (direction.equals("south"))
			y = y + size;
		else if (direction.equals("east"))
			x = x + size;
		else if (direction.equals("west"))
			x = x - size;
	}


	/**	redraw should first redraw the rest of the caterpillar,
		then it should draw this segment.
		The segment should draw itself as a colored circle, centered at its
		position. If it is a face, It should draw two eyes just above its
		center */
	public void redraw(DrawingCanvas canvas) {
		// Your Code Here
		if (next != null)
			next.redraw(canvas);

		canvas.setForeground(color);
		canvas.fillOval(x - size, y - size, size * 2, size * 2, false);
		canvas.setForeground(Color.black);
		canvas.drawOval(x - size, y - size, size * 2, size * 2, false);

		if (face) {
			canvas.fillOval(x-4, y-3, 4, 4, false);
			canvas.fillOval(x+4, y-3, 4, 4, false);
		}
	}

	/* Jump will move the whole caterpillar over by (dx, dy)*/
	public void jump(int dx, int dy) {
		// Your Code Here
		if (next != null)
			next.jump(dx, dy);

		x += dx;
		y += dy;
		

	}

	/* Grow will add a new segment onto the end of the caterpillar
	It should have the appropriate position and direction.*/
	public void grow() {
		// Your Code Here
		if (next == null)
			next = new Caterpillar(x, y, direction, color);
		else
			next.grow();
	}

	/** As long as there are at least 3 segments in the caterpillar,
		shrink will make the caterpillar shorter by removing the second segment.
		It has to make all the rest of the caterpillar move one step to close up
		the gap.*/
	public void shrink() {
		// Your Code Here
		if (size() > 2){
			if (next != null)
				next.move(direction);
			next=next.next;
		}
  	}

	/* As long as there are at least 4 segments in the caterpillar, split
	 will break the caterpillar in half, and return the head of the second
	 caterpillar (the one starting half way down the existing caterpillar)*/
	public Caterpillar split() {
		// Your Code Here
		if(size() >= 4) {
			int mid  = size()/2;
			int count = 0;
			for (Caterpillar rest = this; rest.next != null; rest = rest.next)
				if(count++ == mid-1) {
					rest.next.face = true;
					Caterpillar tmp = rest.next;
					rest.next = null;
					return tmp;
				}
		}
		return null;
	}


	/** Reverse will reverse the caterpillar, turning the head into the tail and
		the tail into the head, reversing all the links along the way, and
		returning a reference to the new head
		Note, it is also necessary to replace the direction of travel
		in each segment by its opposite .*/

	public Caterpillar reverse() {
		//	EXTENSION ONLY

		// Your Code Here

		return this;																	//this is wrong!
	}





	// Utility methods, used by the methods above.

	private Color nextShade(Color col) {
		float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(),
																 null);
		return Color.getHSBColor(hsb[0] + 5.0f / 360, 1.0f, 1.0f);
	}


	private String getNewDirection() {
		// if caterpillar is over the edge, turn round
		if (x <= westEdge)
			return"east";
		else if (x >= eastEdge)
			return"west";
		else if (y <= northEdge)
			return"south";
		else if (y >= southEdge)
			return"north";
		else if (Math.random() < 0.9)
			return direction;
		else																					//with low probability, turn
			if (Math.random() < 0.6) {									// usually turn right
				if (direction.equals("north"))
					return"east";
				else if (direction.equals("south"))
					return"west";
				else if (direction.equals("east"))
					return"south";
				else
					return"north";
			}
			else {																			// but sometimes turn left
				if (direction.equals("north"))
					return"west";
				else if (direction.equals("south"))
					return"east";
				else if (direction.equals("east"))
					return"north";
				else
					return"south";
			}
	}

	private String opposite(String dir) {
		if (dir.equals("north"))
			return"south";
		else if (dir.equals("south"))
			return"north";
		else if (dir.equals("east"))
			return"west";
		else
			return"east";
	}


	//Please leave this method here for testing and marking.
	public static void main(String[] args) {
		CaterpillarSimulation.main(null);
	}

}
