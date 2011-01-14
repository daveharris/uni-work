import java.util.Comparator;

class StringComparator implements Comparator{

	public StringComparator(){
	}

	public int compare(Object s1, Object s2){
		return ((String) s1).compareTo((String) s2);
	}

}
