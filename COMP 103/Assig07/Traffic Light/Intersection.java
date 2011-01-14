import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Enumeration;
import jds.Queue;
import jds.collection.IndexedDeque;
import java.lang.InterruptedException;

/** The class Intersection implements a simulation of a traffic intersection, 
		it uses Objects from the class Car to implement the cars and TrafficLight 
		to implement the individual traffic lights. <br/>
		Buttons are available for restarting the simulation and	for quiting the 
		simulation.
		@see <a href="Car.html">Car</a>
		@see <a href="TrafficLight.html">TrafficLight</a>
		@author Dr. Bill Naylor */
public class Intersection implements ActionListener {

	/** array to hold the traffic Queues */
	private Queue[][] queues = new IndexedDeque[4][3];
	/** array to hold the Traffic Light objects */
	private TrafficLight[] lights;

	/** drawing canvas on which to draw the world */
	private DrawingCanvas canvas;

	/** the direction for which the traffic lights are currently green, 
			or amber */
	private String currentState = "eastwest";

	// various parameters which we may want to change to vary the behaviour of 
	// the simulation
	/** the step size to improve the smoothness of the simulation */
	private static int step = 5;
	// the step size to improve the smoothness of the simulation 
	/** the rate at which the cars are created */
	private static double creationRate = 0.2;
	// the rate at which the cars are created
	/** sleep this much every tick */
	private static int sleeptime = 50;							// sleep this much every tick
	/** change the light after this many ticks */
	private static int lightchange = 60; // change the light after this many ticks
	/** the cars respond to the amber */
	private static int lightamber = 45;					 // the cars respond to the amber

	/** a static 2-D array to hold the positions of the lights */
	private static int[][] initLightPos =	{
		{
			323, 215
				}
		,	{
			443, 305
				}
		,	{
			323, 305
				}
		,	{
			443, 215
				}
		, 
			}
	;

	/** and a 1-D array to hold their colors */
	private static Color[] initLightColor =	{
		Color.green, Color.green, Color.red, Color.red
			}
	;

	/** start the simulation */
	public static void main(String args[]) {
		Intersection t = new Intersection();
		t.reset();
		t.run();
	}

	/** create a new instance of the simulation */
	public Intersection() {
		setupInterface();
		drawSetUp();
	}

	/** reset all global variables (so that the reset button works) */
	private void reset() {

		// renew all the Queues
		for (int lane = 0; lane < 4; lane++)
			for (int i = 0; i < 3; i++)
				queues[lane][i] = new IndexedDeque();

		// create the traffic light objects
		lights = new TrafficLight[4];
		for (int i = 0; i < 4; i++)
			lights[i] = new TrafficLight(initLightPos[i][0], initLightPos[i][1], 
																	 initLightColor[i]);

		currentState = "eastwest";
		render();
	}

	/** run the simulation, a loop implements a 'clock tick' simulation which 
			go's forward one time step every iteration */
	private void run() {
		int ticks = lightamber + 1;
		while (true) {
			try {
				Thread.sleep(sleeptime);
			}
			catch (InterruptedException e) {
			}

			if (++ticks == 120)
				ticks = 0;																// advance the clock

			if (ticks == 0)
				currentState = "eastwest";
			else if (ticks == lightamber)
				currentState = "eastwestamber";
			else if (ticks == lightchange)
				currentState = "northsouth";
			else if (ticks == lightchange + lightamber)
				currentState = "northsouthamber";

			setLights();

			if (ticks % step == 0)				// every step ticks sometimes make a new car
				createNewCars();

			// move all the cars in 'driving in Queues'
			moveCarsIn();

			// when the lights are green, move a car off the end of the
			//	waiting queues and put on the driving off queues
			moveAtLights();

			// move all cars in driving off list
			moveCarsOff();

			render();
		}
	}

	/** allow the (possible) creation of a new car in each direction */
	private void createNewCars() {
		


	}


	//---------------- The methods to enable the cars to move ---------------
	/** In the current direction of flow, move the cars in the 'driving in 
			Queue's, unless they have reached the waiting queue's in which case 
			they must be removed from the 'driving in Q' and placed on the 
			'waiting Q' */

	private void moveCarsIn() {

	}


	/** move the cars waiting at the lights in the direction of flow */
	private void moveAtLights() {



	}


	/** move the cars in the driving off Queue */
	private void moveCarsOff() {



	}


	/** depending on the state of the lights, set each light to the correct 
			colour */
	private void setLights() {
		if (currentState.equals("eastwest")) {
			lights[0].set(Color.green);
			lights[1].set(Color.green);
			lights[2].set(Color.red);
			lights[3].set(Color.red);
		}
		if (currentState.equals("eastwestamber")) {
			lights[0].set(Color.orange);
			lights[1].set(Color.orange);
			lights[2].set(Color.red);
			lights[3].set(Color.red);
		}
		if (currentState.equals("northsouth")) {
			lights[0].set(Color.red);
			lights[1].set(Color.red);
			lights[2].set(Color.green);
			lights[3].set(Color.green);
		}
		if (currentState.equals("northsouthamber")) {
			lights[0].set(Color.red);
			lights[1].set(Color.red);
			lights[2].set(Color.orange);
			lights[3].set(Color.orange);
		}
	}


	// -------------- Rendering the state of the world.--------------------
	/** redisplay the intersection */
	private void render() {
		canvas.clear(false);			 // clear the canvas (but don't display this yet!)
		drawSetUp();																	// display the background

		/** display all the cars */

		drawTrafficLights();													// display the traffic lights
		canvas.display();													// now display the renewed canvas
	}

	/** draw each car */
	private void drawCars(Queue cars) {
		if (cars.size() != 0) {								 // if at least some cars are waiting
			for (Enumeration e = cars.elements(); e.hasMoreElements(); ) {
				Car c = (Car)e.nextElement();
				c.draw(canvas);
			}
		}
	}

	/** draw each traffic light */
	private void drawTrafficLights() {
		for (int i = 0; i < 4; i++)
			lights[i].render(canvas);
	}


	/** draw the traffic crossing background */
	private void drawSetUp() {
		canvas.setForeground(Color.black);
		canvas.drawRect(1, 1, 332, 226, false);
		canvas.fillRect(1, 1, 332, 226, false);
		canvas.drawRect(442, 1, 350, 226, false);
		canvas.fillRect(442, 1, 350, 226, false);
		canvas.drawRect(1, 305, 332, 226, false);
		canvas.fillRect(1, 305, 332, 226, false);
		canvas.drawRect(442, 305, 350, 226, false);
		canvas.fillRect(442, 305, 350, 226, false);
		canvas.drawRect(384, 1, 4, 223, false);
		canvas.drawRect(445, 264, 345, 4, false);
		canvas.drawRect(384, 308, 4, 226, false);
		canvas.drawRect(1, 264, 333, 4, false);
	}





	//--------Set up the user interface. ----------------------------------

	/** set up the user interface */
	private void setupInterface() {
		JFrame frame = new JFrame("Traffic Simulation");
		frame.setSize(800, 565);

		// The graphics area
		canvas = new DrawingCanvas();
		frame.getContentPane().add(canvas, BorderLayout.CENTER);

		// The buttons: make the pane
		JPanel buttonPanel = new JPanel();
		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

		// now add them
		addButton(buttonPanel, "Restart");
		addButton(buttonPanel, "Quit");

		frame.setVisible(true);
	}

	/** respond to the buttons */
	public void actionPerformed(ActionEvent e) {		// respond to the buttons
		if (e.getActionCommand().equals("Quit"))
			System.exit(0);
		else if (e.getActionCommand().equals("Restart"))
			reset();
	}

	/** nice an' easy button adder */
	private void addButton(JPanel panel, String name) {
		JButton button = new JButton(name);
		button.addActionListener(this);
		panel.add(button);
	}

}
