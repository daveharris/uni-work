
public class Location {

	private int row;
	private int col;

	public Location(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}

	public boolean equals(Object other) {
		if (other instanceof Location) {
			Location loc = (Location)other;
			return (row == loc.row && col == loc.col);
		}
		else
			return false;
	}

	public String toString() {
		return ("(" + row + "," + col + ")");
	}

}
