import jds.Indexed;
import jds.collection.Vector;
import java.util.Comparator;
import java.util.Enumeration;

public class ShowSort
{
	public static void main(String[] args)
	{
		new ShowSort();
	}
	
	public ShowSort()
	{
		Vector elements = new Vector();
		elements.addLast("tui");
		elements.addLast("bee");
		elements.addLast("gnu");
		elements.addLast("ant");
		elements.addLast("pig");
		elements.addLast("hen");
		elements.addLast("yak");
		elements.addLast("kit");
		String msg ="";
		for (int i=0; i<elements.size(); i++ )
			msg +=elements.elementAt(i) + "  ";
		System.out.println(msg + "\n");

		
		sort(elements);
	}

	public void sort(Indexed array)
	{
		Comparator test = new Compare();
		for(int i = 1; i < array.size(); i++)
		{
			Object element = array.elementAt(i);
			int j = i-1;
			while(j >= 0 && (test.compare(element, array.elementAt(j)) < 0))
			{
				array.setElementAt(array.elementAt(j), j+1);
				j = j-1;
			}
			array.setElementAt(element, j+1);
			
			System.out.println(i);
			Enumeration e = array.elements();
			while(e.hasMoreElements())
			{
				System.out.print(e.nextElement()+"  ");
			}
			System.out.println("\n");
		}
	}
	
	public class Compare implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			return ((String)o1).compareTo((String)o2);
		}
	}
}
