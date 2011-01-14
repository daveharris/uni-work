import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Enumeration;
import jds.Queue;
import jds.collection.IndexedDeque;
import java.lang.InterruptedException;

/** The class SingleLane implements a simulation of a single lane of traffic at
		an intersection, 
		it uses Objects from the class Car to implement the cars and TrafficLight 
		to implement the individual traffic lights. <br/>
		Buttons are available for restarting the simulation and	for quiting the 
		simulation.
		@see <a href="Car.html">Car</a>
		@see <a href="TrafficLight.html">TrafficLight</a>
		@author Dr. Bill Naylor */
public class SingleLane implements ActionListener {

	/** queus to hold the traffic */
	private Queue inQueue;
	private Queue waitQueue;
	private Queue offQueue;

	/** the Traffic Light object */
	private TrafficLight light;

	/** drawing canvas on which to draw the world */
	private DrawingCanvas canvas;

	/** the direction for which the traffic lights are currently green, or amber */
	private String currentState = "eastwest";

	// various parameters which we may want to change to vary the behaviour of
	// the simulation
	/** the step size to improve the smoothness of the simulation */
	private static int step = 5;

	/** how many ticks to wait between creating cars */
	private static int createTicks = 5;

	/** the probability of creating a car */
	private static double creationRate = 1;

	/** sleep this much every tick */
	private static int sleeptime = 50;

	/** change the light to amber after this many ticks */
	private static int lightamber = 45;

	/** change the light to green after this many ticks */
	private static int lightchange = 60;

	/**	the position of the light */
	private static int lightPosx = 323;
	private static int lightPosy = 215;

	/** the initial color of the light */
	private static Color initLightColor = Color.green;

	/** start the simulation */
	public static void main(String args[]) {
		SingleLane t = new SingleLane();
		t.reset();
		t.run();
	}

	/** create a new instance of the simulation */
	public SingleLane() {
		setupInterface();
		drawSetUp();
	}

	/** reset all global variables (so that the reset button works) */
	private void reset() {
		// renew all the Queues
		inQueue = new IndexedDeque();
		waitQueue = new IndexedDeque();
		offQueue = new IndexedDeque();

		// create the traffic light objects
		light = new TrafficLight(lightPosx, lightPosy, initLightColor);

		render();
	}

	/** run the simulation, a loop implements a 'clock tick' simulation which
			goes forward one time step every iteration */
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

			setLight();

			if (ticks % createTicks == 0)				 // every createTicks, make a new car
				createNewCar();

			// move all the cars in 'driving-in Queue'
			moveCarsIn();

			// when the lights are green, move a car off the end of the
			//	waiting queue and put on the driving-off queue
			moveAtLights();

			// move all cars in driving-off queue
			moveCarsOff();

			render();
		}
	}

	/** allow the (possible) creation of a new car in the east direction */
	private void createNewCar() {
		// YOUR CODE HERE
		double random = Math.random();
		if(random <= creationRate) {
			Car newCarEast = new Car ("east");
			inQueue.addLast(newCarEast);
		}
	}

	//---------------- The methods to enable the cars to move ---------------
	/** In the east direction of flow, move each car in the driving-in 
			Queue's, unless it has reached the waiting queue.	In this case,
			remove it from the driving-in Queue	and add it to the 
			waiting queue */

	private void moveCarsIn() {
		// YOUR CODE HERE
		int carCount = 0;
		for(Enumeration eIn = inQueue.elements(); eIn.hasMoreElements(); ) {
			Car newCar = (Car)eIn.nextElement();
			if (newCar.move(step, waitQueue.size())) {
    			inQueue.removeFirst();
				waitQueue.addLast(newCar);
			}
		}
	}

	/** If the traffic lights are "eastwest" and there is a car on the waitQueue,
			move it off the waitQueue and onto the offQueue */
	private void moveAtLights() {
		// YOUR CODE HERE
		if(!waitQueue.isEmpty() && currentState.equals("eastwest")){
			//for(Enumeration eWait = waitQueue.elements(); eWait.hasMoreElements(); ) {
				//Car newCar = (Car)eWait.nextElement();
				Car newCar = (Car)waitQueue.getFirst();
				waitQueue.removeFirst();
				offQueue.addLast(newCar);
			//}
		}
	}

	/** move each car in the offQueue, unless it is no longer on the
			screen, in which case take it off the Queue */
	private void moveCarsOff() {
		// YOUR CODE HERE
		for(Enumeration eOff = offQueue.elements(); eOff.hasMoreElements(); ) {
			Car newCar = (Car)eOff.nextElement();
			if (!newCar.move(step))
				offQueue.removeFirst();
		}
	}

	/** depending on the state of the lights, set each light to the correct 
			colour */
	private void setLight() {
		if (currentState.equals("eastwest"))
			light.set(Color.green);
		if (currentState.equals("eastwestamber"))
			light.set(Color.orange);
		if (currentState.equals("northsouth"))
			light.set(Color.red);
		if (currentState.equals("northsouthamber"))
			light.set(Color.red);
	}


	// -------------- Rendering the state of the world.--------------------
	/** redisplay the intersection */
	private void render() {
		canvas.clear(false);			 // clear the canvas (but don't display this yet!)
		drawSetUp();																	// display the background

		drawCars(inQueue);														// display each Car
		drawCars(waitQueue);													// display each Car
		drawCars(offQueue);													 // display each Car

		light.render(canvas);												 // display the traffic light
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
