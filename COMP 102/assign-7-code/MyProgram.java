import javax.swing.*;
import java.io.*;

public class MyProgram {

    public static void main(String[] args){

	carFactory factory = new carFactory();
	int choice;

 	String menu = "Car menu" + 
				"\nMAIN MENU" + 
				"\n1. Load Cars" +
				"\n2. List all Cars" +
				"\n3. Exit";

		while (true) {

String s =
     JOptionPane.showInputDialog(menu);

			if (s == null ) {
				break;
			}
	
			choice = Integer.parseInt(s.trim());
			
			switch (choice) {
				case 1:  factory.loadCars();
				break;
				case 2:  factory.displayCars();
				break;
				case 3:  System.exit(0);	
			}
		}
	}
}

class carFactory {

	private Car[] cars = new Car[5];
	
	//Displays all the cars in the array (using the printCar() method in the
	//Car class.
	public void displayCars() {
		String output = "";
		for (int i = 0; i < 5 ;i++) {
			if (cars[i] != null) {
			output = output + cars[i].printCar() + "\n";
			}
		}
		JOptionPane.showMessageDialog(null,output);
	}

	public void loadCars() {
		try {
			String infile = JOptionPane.showInputDialog("Enter Filename: ");
			FileReader inStream = new FileReader(infile);
        	BufferedReader ins = new BufferedReader(inStream);
			for(int i = 0; i < 5; i++) {
				Car c = new Car();
				if(c.readFromFile(ins)) { 
					cars[i] = c; 
				
				}
			}
			ins.close();
		}
		catch(IOException ex) {
       		System.out.println(ex.getMessage());
       		ex.printStackTrace();
     	}
	}
	
}

class Car {

	private String name;
	private String brand;
	private int topSpeed;
	
	public Car(){
	}

	public String getName() {
		return name;
	}

	public String getBrand() {
		return brand;
	}

	public int getTopSpeed() {
		return topSpeed;
	}

	public String printCar() {
		String msg = "-------Car Details-------\n" +
					 "Name:      " + name + "\n" +
					 "Brand:     " + brand + "\n" +
					 "Top Speed: " + topSpeed + "\n";
		return msg;
					  
	}

	public boolean readFromFile(BufferedReader file) throws NumberFormatException, IOException {
		name = file.readLine();
	    if (name ==null) return false;
	    brand = file.readLine();
	    if (brand ==null) return false;
	    String top = file.readLine();
		if (top ==null) return false;
		topSpeed = Integer.parseInt(top);
		return true;
	}
}
