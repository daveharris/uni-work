import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;

/* Code for Assignment 1, July 2003
 * Name: David Harris
 * Usercode: harrisdavi3
 * ID: 300069566
 */

public class MiniPaint implements ActionListener, MouseListener, MouseMotionListener{

  /* fields */
  	private DrawingCanvas canvas;
 	private String shape = "Line";  // what shape should be drawn
  	private boolean fill = false;   // whether the shape should be filled or not
	private JButton fillButton;
	private JButton colorButton;
	private JButton paintButton;
	int x1;
	int x2;
	int y1;
	int y2;
	int startX;
	int startY;
  /* You will also need fields to store where the user started dragging */

	public static void main(String args[]){
    	new MiniPaint();
	}


	public MiniPaint(){
		JFrame frame = new JFrame("MiniPaint");
		frame.setSize(650,400);

		//The graphics area
		canvas = new DrawingCanvas();
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		frame.getContentPane().add(canvas, BorderLayout.CENTER);


		//The buttons
		JPanel buttonPanel = new JPanel();

		JButton lineButton = new JButton("Line");
		JButton rectButton = new JButton("Rectangle");
		JButton ovalButton = new JButton("Oval");
		JButton clearButton = new JButton("Clear");
		fillButton = new JButton("Fill");
		colorButton = new JButton("Color");
		paintButton = new JButton("Paint");
		JButton quitButton = new JButton("Quit");

		lineButton.addActionListener(this);
		rectButton.addActionListener(this);
		ovalButton.addActionListener(this);
		clearButton.addActionListener(this);
		fillButton.addActionListener(this);
		colorButton.addActionListener(this);
		paintButton.addActionListener(this);
		quitButton.addActionListener(this);

		buttonPanel.add(lineButton);
		buttonPanel.add(rectButton);
		buttonPanel.add(ovalButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(fillButton);
		buttonPanel.add(colorButton);
		buttonPanel.add(paintButton);
		buttonPanel.add(quitButton);

		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

		frame.setVisible(true);
		
		//addMouseMotionListener(this);
	}

  /* Respond to button presses */

	public void actionPerformed(ActionEvent e){
		System.out.println("Button: " + e.getActionCommand());
		if (e.getActionCommand().equals("Line") )
			shape = "Line";
		else if (e.getActionCommand().equals("Rectangle"))
			shape = "Rectangle";
		else if (e.getActionCommand().equals("Oval"))
			shape = "Oval";
		else if (e.getActionCommand().equals("Clear"))
			canvas.clear();
		else if (e.getActionCommand().equals("Fill")) {
			fill = true;
			fillButton.setText("No Fill");
		}
		else if (e.getActionCommand().equals("No Fill")) {
			fill = false;
			fillButton.setText("Fill");
		}
		else if (e.getActionCommand().equals("Color")) {
			float value1 = (float)Math.random();
			float value2 = (float)Math.random();
			float value3 = (float)Math.random();
						
			Color random = new Color(value1, value2, value3);
			canvas.setForeground(random);
			colorButton.setBackground(random);
			colorButton.setForeground(Color.white);
		}
		else if (e.getActionCommand().equals("Paint")) {
			shape = "Paint";
		}
		else if (e.getActionCommand().equals("Quit"))
			System.exit(0);
	}


  /* Respond to mouse events */

	public void mousePressed(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
		System.out.println("Pressed at (" + x1 +" "+ y1 +")");
	}
	public void mouseReleased(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
		System.out.println("Released at ("+ x2 +" "+ y2 +")");
		DrawShapes();
	}
	public void mouseClicked(MouseEvent e) {}  //needed to satisfy interface
	public void mouseEntered(MouseEvent e) {}  //needed to satisfy interface
	public void mouseExited(MouseEvent e) {}   //needed to satisfy interface
	public void mouseDragged(MouseEvent e) {
		startX = e.getX();
		startY = e.getY();
		if (shape.equals("Paint"))
			DrawShapes();
	}
	public void mouseMoved(MouseEvent e) {}
	public void MouseMotionListener(MouseEvent e) { }

  /* Helper methods for drawing the shapes */

	public void DrawShapes() {
		if (shape.equals("Line")) {
			canvas.drawLine(x1, y1, x2, y2);
		}
		if (x2 < x1) {
			int previousX = x1;
			x1 = x2;
			x2 = previousX;
		}
		if (y2 < y1) {
			int previousY = y1;
			y1 = y2;
			y2 = previousY;
		}
		if (shape.equals("Rectangle")) {
			if(fill)
				canvas.fillRect(x1, y1, (x2-x1), (y2-y1));
			else
				canvas.drawRect(x1, y1, (x2-x1), (y2-y1));
		}
		else if (shape.equals("Oval")) {
			if(fill)
				canvas.fillOval(x1, y1, (x2-x1), (y2-y1));
			else
				canvas.drawOval(x1, y1, (x2-x1), (y2-y1));
		}
		else if (shape.equals("Clear")) {
			canvas.clear();
		}
		else if (shape.equals("Paint")) {
			canvas.fillOval(startX, startY, 10, 10);
		}
	}

}
