import java.awt.Color;

/**
 * This class is for use in assignment 07. It provides objects which are models
 * for traffic lights, they have coordinates and a color.
 * @author Dr. Bill Naylor*/
public class TrafficLight {
		private Color color = Color.green;
		private int x = 0;
		private int y = 0;

		/** construct a light, parameters are the <I>x</I>, <I>y</I> coordinates
	and the color of the light */
		public TrafficLight(int X,int Y,Color col){
	color = col;
	x = X;
	y = Y;
		}

		/** method to change a traffic light, 
	the colors change in the order green -> orange -> red -> green */
		public void change() {
	if (color.equals(Color.green))
			color = Color.orange;
	else if (color.equals(Color.orange))
			color = Color.red;
	else color = Color.green;
		}

		/** method to change a traffic light, 
	the colors change in the order green -> orange -> red -> green */
		public void set(Color c) {
			color = c;
		}

		/** render the light on a DrawingCanvas */
		public void render(DrawingCanvas canvs) {
	canvs.setForeground(color);
	canvs.fillOval(x,y,10,10,false);
		}

		/**	returns the color of the traffic light
		 * @return "green", "amber" or red" */
		public String ColorIs(){
	if (color.equals(Color.green))
			return "green";
	else if (color.equals(Color.red))
			return "red";
	else return "amber";
		}
}
