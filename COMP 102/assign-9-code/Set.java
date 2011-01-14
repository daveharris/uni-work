
//======================================================================
/** Generic Set class, as discussed in lectures.
 *   This class contains Objects using an array.
 *   This version also has the "traversal" methods getInit, getNext
 *   and getElement. You first call getInit, and then
 *   each call to getNext positions you at the next element,
 *   which you can obtain with getElement. getNext returns true when there
 *   is a valid element, and false when no more are left.
 */           
//======================================================================


class Set {
    private Object elements[];
    private int maxsize = 100;
    private int pos = 0;
    
    public Set(int setsize) {
	maxsize = setsize;
	elements=new Object[maxsize];
    }
    
    public Set() {
	elements=new Object[maxsize];
    }
    
    public boolean insert(Object x) {
	if (Check(x)) return true;
	int i; 
	i = 0;
	while (i < maxsize) {
	    if (elements[i] == null) {
                elements[i] = x;
		return true;
	    }
	    i++;
	}
	return false;  
    }
    
    public boolean Check(Object x) {
	int i; 
	i = 0;
	while (i < maxsize) {
	    if (elements[i] != null && elements[i].equals(x)) {
		return true;
	    }
	    i++;
	}
	return false;  
    }
    
    public boolean Delete(Object x) {
	int i = 0;
	while (i < maxsize) {
	    if (elements[i] != null && elements[i].equals(x)) {
		elements[i] = null;
		return true;
	    }
	    i++;
	}
	return false;
    }
        
    public void getInit() {
        pos = -1;
    }
    

    public boolean getNext() {
	pos++;
        while (pos < maxsize) {
	    if (elements[pos] != null) {
                return true;
            }
	    pos++;
        }
        return false;
    }

    public Object getElement() {
	return elements[pos];
    }

}
