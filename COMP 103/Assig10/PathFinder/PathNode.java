import java.lang.Comparable;

public class PathNode implements Comparable {
	private Location location;								// the location that we have got to
	private PathNode parent;								// the PathNode that we came from
	private double pathCost;					 			// the cost of the path from start to here.

	public PathNode(Location loc, double cost, PathNode cameFrom) {
		location = loc;
		pathCost = cost;
		parent = cameFrom;
	}


	public Location getLocation() {
		return location;
	}

	public double getPathCost() {
		return pathCost;
	}

	public PathNode getParent() {
		return parent;
	}

	public int compareTo(Object other) {
		double othercost = ((PathNode)other).pathCost;
		if (pathCost < othercost)
			return - 1;
		else if (pathCost == othercost)
			return 0;
		else
			return 1;
	}


	public String toString() {
		return (location + " pathcost: " + (((int)(pathCost * 100)) / 100.0) + 
						((parent == null)? "": " from: " + parent.location));
	}

}
