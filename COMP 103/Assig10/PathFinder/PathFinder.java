import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.io.*;
import java.util.Enumeration;
import jds.FindMin;
import java.util.Comparator;
import java.awt.Color;

/* Code for Assignment 10, October 2003
 * Name:  David Harris
 * Usercode: harrisdavi3
 * ID: 300069566
 */

public class PathFinder implements ActionListener, MouseListener {
	private DrawingCanvas canvas;

	private World world;
	private Location start;
	private Location goal;
	private boolean raise;

	public static void main(String args[]) {
		new PathFinder();
	}


	public PathFinder() {
		JFrame frame = new JFrame("Path Finder");
		frame.setSize(700, 700);

		//The graphics area
		canvas = new DrawingCanvas();
		canvas.addMouseListener(this);
		frame.getContentPane().add(canvas, BorderLayout.CENTER);


		//The buttons
		JPanel buttonPanel = new JPanel();

		// I made a method below to make it easier to add the buttons
		addButton(buttonPanel, "New");
		addButton(buttonPanel, "Load");
		addButton(buttonPanel, "Save");
		addButton(buttonPanel, "Raise/Lower");
		addButton(buttonPanel, "Quit");

		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
		frame.setVisible(true);

		world = new World(30);
		world.render(canvas);
	}

	public void addButton(JPanel panel, String name) {
		JButton button = new JButton(name);
		button.addActionListener(this);
		panel.add(button);
	}


	/* Respond to button presses */

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("New"))
			createNewWorld();
		else if (cmd.equals("Load"))
			loadWorld();
		else if (cmd.equals("Save"))
			saveWorld();
		else if (cmd.equals("Raise/Lower"))
			raise = !raise;
		else if (cmd.equals("Quit"))
			System.exit(0);
	}


	/* Respond to mouse events */

	public void mousePressed(MouseEvent e) {
		world.render(canvas);
		start = world.getLocation(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		goal = world.getLocation(e.getX(), e.getY());
		findPath();
	}

	public void mouseClicked(MouseEvent e) {
		Location loc = world.getLocation(e.getX(), e.getY());
		world.edit(loc, raise, canvas);
	}

	public void mouseEntered(MouseEvent e) {
	}																							 //needed to satisfy interface
	public void mouseExited(MouseEvent e) {
	}																							 //needed to satisfy interface


	//===================================================================
	/*find a path from the start location to the goal location,
	by a priority first search.
	Represents the paths that it is searching by linked lists of PathNodes.
	The PathNodes are what goes on the priority queue.*/
	private void findPath() {
		if (start.equals(goal))
			return;
		world.reset();

		// make a new priority queue
		//FindMin queue = new ArrayPQueue();
		HeapPQueue queue = new HeapPQueue();

		// put a new Pathnode to the start location on the queue.
		PathNode newNode = new PathNode(start, world.getCost(start), null);
		queue.addElement(newNode);

		// while the queue has any pathnodes on it
		while(!queue.isEmpty()) {

			// get the highest priority pathnode.
			PathNode highest = (PathNode)queue.getFirst();
			Location location = highest.getLocation();
			queue.removeFirst();

			// if it is already marked, then don't do anything with it
			if(world.isMarked(location)) {
			}
			// otherwise
			else{
				//mark the location of the pathnode so we don't look at it again.
				world.mark(location);
				// see if this pathnode is at the goal.
				//If so, display the path to this pathnode and return
				if(location.equals(goal)) {
					displayPathTo(highest);
					return;
				}
				// if we aren't at the goal yet, consider each neighbouring location

				for(int i = 0; i<=3; i++) {
					// make a new pathnode to this neighbouring location,
					// building on the current pathnode
					Location neighbour = world.getNeighbour(location, i);

					// and add it to the queue
					queue.addElement(new PathNode(neighbour, highest.getPathCost() + world.getCost(location), highest));
				}
			}
		}
	}


	/** Display the path that ends at this path node, by drawing
			lines between the successive locations on the path.
			Needs to work back along the parent pointers of the path nodes*/
	private void displayPathTo(PathNode node) {
		world.render(canvas);
		PathNode nextNode = node;
		Location nextLocation = nextNode.getLocation();
		while(!nextLocation.equals(start)) {
			PathNode parentNode = nextNode.getParent();
			Location parentLocation = parentNode.getLocation();
			world.drawLine(nextLocation, parentLocation, canvas);
			nextNode = parentNode;
			nextLocation = parentLocation;
		}
	}

	//===================================================================


	public void loadWorld() {
		world = new World(FileDialog.open());
		world.render(canvas);
	}


	public void createNewWorld() {
		int size =
				Integer.parseInt(JOptionPane.showInputDialog("What size (20..80)?"));
		world = new World(size);
		world.render(canvas);
	}

	public void saveWorld() {
		if (world != null) {
			world.save();
		}
	}

}


