import java.io.*;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.awt.Color;


public class World {


	/** Load a world from a file. */
	public World(String fname) {
		loadWorld(fname);
	}

	/** Make a new smoothed, random world. */
	public World(int sz) {
		createRandomWorld(sz);
	}

	/** Returns the cost of traversing the cell at the location */
	public double getCost(Location loc) {
		return cells[loc.getRow()][loc.getCol()].cost;
	}

	/** Return neighbour location in specified direction
			direction:	0 = north, 1 = east, 2=south, 3=west.
			Guarantees not to return a location off the edge.*/
	public Location getNeighbour(Location loc, int dir) {
		int r = loc.getRow();
		int c = loc.getCol();
		if (dir == 0 && r > 0)
			r--;
		else if (dir == 1 && c < size - 1)
			c++;
		else if (dir == 2 && r < size - 1)
			r++;
		else if (dir == 3 && c > 0)
			c--;

		return cells[r][c].loc;
	}

	/** Mark the location */
	public void mark(Location loc) {
		cells[loc.getRow()][loc.getCol()].mark = true;
	}

	/** Returns true iff the location has been marked */
	public boolean isMarked(Location loc) {
		return cells[loc.getRow()][loc.getCol()].mark;
	}

	/** Resets the world by removing all marks */
	public void reset() {
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
				cells[row][col].mark = false;
	}

	/** Returns an estimate of the cost of getting from a to b.
			Guaranteed to be lower than the actual cost.
			Only needed for the challenge. */
	public double getEstimate(Location a, Location b) {
		return (Math.abs(a.getRow() - b.getRow()) +
						Math.abs(a.getCol() - b.getCol())) * minCost;
	}

	public void render(DrawingCanvas canvas) {
		canvas.clear(false);
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				int x = left + col * cellSize;
				int y = top + row * cellSize;
				double c = cells[row][col].cost - minCost;
				Color color = Color.black;
				if (c < 1.0)
					color = new Color((float)(c * 0.3), (float)((1.3 - c) / 1.3), 0.0f);

				canvas.setForeground(color);
				canvas.fillRect(x, y, cellSize, cellSize, false);

				canvas.setForeground(Color.black);
				canvas.drawRect(x, y, cellSize, cellSize, false);
				if (cells[row][col].mark) {
					canvas.setForeground(Color.blue);
					canvas.fillRect(x + cellSize / 2, y + cellSize / 2, 1, 1, false);
				}
			}
		}
		canvas.display();
	}


	public void drawLine(Location a, Location b, DrawingCanvas canvas) {
		canvas.setForeground(Color.yellow);
		int x1 = left + a.getCol() * cellSize + cellSize / 2;
		int y1 = top + a.getRow() * cellSize + cellSize / 2;
		int x2 = left + b.getCol() * cellSize + cellSize / 2;
		int y2 = top + b.getRow() * cellSize + cellSize / 2;
		canvas.drawLine(x1, y1, x2, y2);
		canvas.drawLine(x1 + 1, y1, x2 + 1, y2);
		canvas.drawLine(x1 - 1, y1, x2 - 1, y2);
		canvas.drawLine(x1, y1 + 1, x2, y2 + 1);
		canvas.drawLine(x1, y1 - 1, x2, y2 - 1);
	}

	// -----------------------------------------------------------
	// You do not need to use any of the methods below this point.
	// -----------------------------------------------------------


	// fields

	private Cell[][] cells;
	private int size;
	private int cellSize = 20;
	private static int left = 10;
	private static int top = 10;
	private static int width = 600;

	private static double minCost = 0.1;			// minimum cost of traversing a cell
	private static double maxCost = 100000;				 // cost of an impassible cell


	/** Save the world map to a file */
	public void save() {
		try {
			String fname = FileDialog.save();
			PrintWriter f = new PrintWriter(new FileWriter(fname));
			f.println(size);
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					int cost = (int)(cells[row][col].cost * 100.0);
					f.print(cost / 100.0 + " ");
				}
				f.println();
			}
			f.close();
		}
		catch (Exception e) {
			System.out.println("World File not saved correctly");
		}

	}


	/** Returns the location corresponding to the point (x, y) on the window */
	public Location getLocation(int x, int y) {
		if (cells == null)
			return null;
		else {
			int row = (y - top) / cellSize;
			int col = (x - left) / cellSize;
			row = Math.max(0, Math.min(size - 1, row));
			col = Math.max(0, Math.min(size - 1, col));
			return cells[row][col].loc;
		}
	}

	/** Raise or lower the terrain at loc, and the cells immediately around it */
	public void edit(Location loc, boolean raise, DrawingCanvas canvas) {
		double change = -0.05;
		if (raise)
			change = 0.1;
		int row = loc.getRow();
		int col = loc.getCol();
		cells[row][col].setCost(cells[row][col].cost + change);
		for (int r = row - 1; r <= row + 1; r++)
			if (r >= 0 && r < size)
				for (int c = col - 1; c <= col + 1; c++)
					if (c >= 0 && c < size)
						cells[r][c].setCost(cells[r][c].cost + change);

		render(canvas);
	}


	/** create random world.
			YOU DO NOT NEED TO UNDERSTAND THIS METHOD!!
			It attempts to create a world with reasonably nice distribution
			of difficulties, but it is by no means perfect. In particular, it
			doesn't generate large scale features like mountains, ridges, and
			valleys.	*/

	private void createRandomWorld(int sz) {
		size = Math.min(80, Math.max(20, sz));
		cellSize = width / size;
		cells = new Cell[size][size];
		// put easy border border around the edge
		for (int i = 0; i < size; i++) {
			cells[0][i] = new Cell(0, 0, i);
			cells[i][0] = new Cell(0, i, 0);
			cells[size - 1][i] = new Cell(0, size - 1, i);
			cells[i][size - 1] = new Cell(0, i, size - 1);
		}
		// create random terrain
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				double cost = Math.random() * 1.7;
				cost = (cost * cost) / 2;								 // more low cost cells
				cells[row][col] = new Cell(cost, row, col);
			}
		}
		//now smooth it out,
		for (int i = 0; i < 2; i++) {
			for (int row = 1; row < size - 1; row++) {
				for (int col = 1; col < size - 1; col++) {
					double av = cells[row][col].cost * 3;
					int n = 7;
					for (int j = 0; j < 2; j++)
						for (int k = 0; k < 2; k++) {
							double c = cells[row + j][col + k].cost;
							if (c <= 1.0)
								av += c;
							else
								n--;
						}
					double newcost = av / n;								// + cells[row][col].cost)/2;
					cells[row][col].setCost(newcost);
				}
			}
		}
		//now make the border impassible 
		for (int i = 0; i < size; i++) {
			cells[0][i].setCost(1000);
			cells[i][0].setCost(1000);
			cells[size - 1][i].setCost(1000);
			cells[i][size - 1].setCost(1000);
		}
	}

	/** Load a world from a file 
			Assumes that the world file has the dimension of the world on the first 
			line,and difficulties for each cell on the remaining lines, one row per 
			line. Costs must be at least 0.1.	If over 1.0, then they are treated as
			almost infinite. */

	private void loadWorld(String fname) {
		try {
			BufferedReader worldFile = new BufferedReader(new FileReader(fname));
			int row = 0;
			size = Integer.parseInt(worldFile.readLine());
			cellSize = width / size;
			cells = new Cell[size][size];
			while (row < size) {
				String line = worldFile.readLine();
				if (line == null)
					break;
				int col = 0;
				Enumeration e = new StringTokenizer(line);
				while (e.hasMoreElements() & col < size) {
					double cost = Double.parseDouble((String)e.nextElement());
					cells[row][col] = new Cell(cost, row, col);
					col++;
				}
				row++;
			}
			worldFile.close();
		}
		catch (Exception e) {
			System.out.println("World File not read correctly");
		}
	}


	/** Records the difficulty of traversing a cell of the world, and whether it 
		* has been marked 
		*/
	private class Cell {

		public double cost;
		public boolean mark = false;
		public Location loc;

		public Cell(double c, int row, int col) {
			setCost(c);
			loc = new Location(row, col);
		}


		public void setCost(double c) {
			cost = c;
			if (cost > 1.0)
				cost = maxCost;
			else if (cost < minCost)
				cost = minCost;
		}


	}



}
