import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.*;
import java.util.Enumeration;
import java.util.Comparator;
import jds.Set;

/* Code for Assignment 6, Sept 2003
 * Name: 
 * Usercode: 
 * ID: 
 */

/* Simulation of a collection of caterpillars, squirming around the screen
	 looking for food sources.	If they find a food source, they eat it. 
	 Different kinds of food will have different effects.
	 grow or split into two caterpillars. 
	 
	 This program is related to some of the earlier computer games
	 (now appearing again on a cellphone near you), but doesn't have
	 a game element. (Of course, you could add one).
	 
	 The center of the program is the run() method that loops forever,
	 controlling the simulation.
	 On each cycle ("clock tick") it
	 - moves the caterpillars,
	 - makes them eat,
	 - redraws them, and
	 - waits for the next clock tick.
	 
	 Several of the buttons (split, grow, shrink, reverse)	are just for
	 testing your code - they will perform the operation on the first
	 caterpillar, regardless of the food.
	 
	 Note: Because the run() method is called from the main method,
	 it is in a different thread from the buttons.	That is why the
	 buttons still work, even while the simulation is running.*/

public class CaterpillarSimulation implements ActionListener {
	private DrawingCanvas canvas;

	private Set caterpillars;
	private Set foodSources;							// the size and color of each atom type.
	private int jumpSize = 40;

	public static void main(String args[]) {
		CaterpillarSimulation cs = new CaterpillarSimulation();
		cs.reset(80);
		cs.run();
	}


	public CaterpillarSimulation() {
		JFrame frame = new JFrame("Caterpillar Frenzy");
		frame.setSize(700, 700);

		//The graphics area
		canvas = new DrawingCanvas();
		frame.getContentPane().add(canvas, BorderLayout.CENTER);


		//The buttons
		JPanel buttonPanel = new JPanel();

		// I made a method below to make it easier to add the buttons
		addButton(buttonPanel, "Reset");
		addButton(buttonPanel, "New");
		addButton(buttonPanel, "Jump");
		addButton(buttonPanel, "Grow");
		addButton(buttonPanel, "Shrink");
		addButton(buttonPanel, "Split");
		addButton(buttonPanel, "Reverse");
		addButton(buttonPanel, "Quit");

		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
		frame.setVisible(true);
	}

	public void addButton(JPanel panel, String name) {
		JButton button = new JButton(name);
		button.addActionListener(this);
		panel.add(button);
	}


	/* Respond to button presses */

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		Caterpillar c = (Caterpillar)caterpillars.elements().nextElement();
		if (cmd.equals("Reset"))
			reset();

		else if (cmd.equals("New"))
			caterpillars.addElement(new Caterpillar());

		else if (cmd.equals("Quit"))
			System.exit(0);
		// The buttons to test the actions
		else if (cmd.equals("Jump"))
			c.jump(jumpSize, jumpSize);

		else if (cmd.equals("Grow"))
			c.grow();

		else if (cmd.equals("Split")) {
			Caterpillar newCat = c.split();
			if (newCat != null)
				caterpillars.addElement(newCat);
		}

		else if (cmd.equals("Shrink"))
			c.shrink();

		else if (cmd.equals("Reverse")) {
			Caterpillar newHead = c.reverse();
			caterpillars.removeElement(c);
			caterpillars.addElement(newHead);
		}
		else
			throw new RuntimeException("No such button: " + cmd);
	}



	/** Creates a new Set of food items at random places, and displays them.
			Creates a new Set with two caterpillars, and displays them. */
	public void reset() {
		reset(Integer.parseInt(JOptionPane.showInputDialog("Number of Food Sources:")));
	}

	public void reset(int num) {
		foodSources = new ArraySet();
		caterpillars = new ArraySet();
		for (int i = 0; i < num; i++)
			foodSources.addElement(new FoodSource());
		caterpillars.addElement(new Caterpillar());
		redraw();
	}


	/** A never ending simulation loop that waits for the next tick of the 
			simulation,then
			- moves each caterpillar, possibly changing its direction
			- checks each caterpillar to see if it is next to some food
			if so, makes it eat the food, possibly adding a new caterpillar
			to the bag of caterpillars.*/
	public void run() {
		while (true) {
			// move each of the caterpillars
			for (Enumeration e = caterpillars.elements(); e.hasMoreElements(); )
				((Caterpillar)e.nextElement()).move();

			// grow each of the food sources
			for (Enumeration e = foodSources.elements(); e.hasMoreElements(); )
				((FoodSource)e.nextElement()).grow();

			eat();									// check each caterpillar against each food supply

			// redraw everything
			redraw();
			try {
				Thread.sleep(100);
			}

			catch (Exception e) {
			}
		}
	}


	/** For each caterpillar and each food source, check if the caterpillar
			is at the food source. If so, perform the appropriate action on the
			caterpillar, depending on the type of the food source.*/
	public void eat() {
		for (Enumeration cs = caterpillars.elements(); cs.hasMoreElements(); ) {
			Caterpillar c = (Caterpillar)cs.nextElement();
			for (Enumeration fs = foodSources.elements(); fs.hasMoreElements(); ) {
				FoodSource food = (FoodSource)fs.nextElement();
				if (c.near(food.getX(), food.getY()) && food.mature()) {
					String foodType = food.getType();
					food.eaten();
					if (foodType.equals("spicy"))
						c.jump(jumpSize, jumpSize);
					if (foodType.equals("nutritious"))
						c.grow();
					else if (foodType.equals("poison"))
						c.shrink();
					else if (foodType.equals("divisive")) {
						Caterpillar newCat = c.split();
						if (newCat != null)
							caterpillars.addElement(newCat);
					}
					if (foodType.equals("badtaste")) {
						caterpillars.removeElement(c);
						caterpillars.addElement(c.reverse());
					}
				}
			}
		}
	}


	public void redraw() {
		canvas.clear(false);
		for (Enumeration fs = foodSources.elements(); fs.hasMoreElements(); )
			((FoodSource)fs.nextElement()).redraw(canvas);
		for (Enumeration cs = caterpillars.elements(); cs.hasMoreElements(); )
			((Caterpillar)cs.nextElement()).redraw(canvas);
		canvas.display();
	}

}





