import java.awt.Color;

/** This class has been created for use with the first part of assignment 07.
    It allows one to represent the different types of actions that may be 
    performed in the MiniDraw program.  */

public class Action {

    private String actionType;
    private int index;
    private Shape shape;
	private Color col;

    private int mvx;
    private int mvy;


    /** The action type, this is a string describing the type of action. i.e.
       "Move", "Delete", "Line", etc. */
    public String getActionType(){
      return actionType;
    }

    /** The index (in the shapes collection) of the shape which the
	action is being performed on.  (Needed for the Delete actions) */
    public int getIndex(){
      return index;
    }

    /** The shape which the action is being performed on.
        (Needed for the Delete and Move actions)  */
    public Shape getShape(){
      return shape;
    }

	/** The color of the action.
	(Needed for the Color actions)  */
	public Color getColor() {
		return col;
	}
	
    /** The x component of the amount the shape was moved
        (Need only for the Move action) */
    public int getMvx(){
      return mvx;
    }

    /** The y component of the amount the shape was moved
        (Need only for the Move action) */
    public int getMvy(){
      return mvy;
    }

    /** This constructor may be used for constructing <tt>Action</tt> objects 
	corresponding to the action of creating a new shape "Oval", "Rect",
	"Line", "PolyLine" or "Text". 
	The argument is the action type. */
    public Action(String at) {
	actionType = at;
    }

    /** This constructor may be used for constructing <tt>Action</tt> objects 
	corresponding to the Delete action.
	The first argument is the action type ("Delete")
	the second argument is the index of the shape in the Vector,
	the third argument is the shape). */
    public Action(String at, int i, Shape s) {
	actionType = at;
	index = i;
	shape = s;
    }

    /** This constructor may be used for constructing <tt>Action</tt> objects 
	corresponding to the "Move" action.
	The first argument is the action type ("Move").
	the second argument is the shape, 
	and the last two arguments are the
	change in x and y positions (in that order) */
    public Action(String at, Shape s, int x, int y) {
	actionType = at;
	shape = s;
	mvx = x;
	mvy = y;
    }

	/** This constructor may be used for constructing <tt>Action</tt> objects
    corresponding to the action of changing the colour. The first argument
    should be the previous colour, the second should be a describing string
    viz. "Colour" */
	public Action(Color c, String at) {
		col = c;
		actionType = at;
	}
}
