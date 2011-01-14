import javax.swing.JOptionPane;

public class NumberAnalyser{
  public static void main (String[] args) {
    NumberArray n = new NumberArray();
    n.read();
    n.display();
    System.out.println("Print and count all numbers above 50");
    n.above50();
    System.out.println("Display in format");
    n.formatDisplay();
    System.out.println("Average Step is " + n.averageStep());
  }
}

class NumberArray {
  //data fields
  //Declare and create an array that can hold 5 integers
  private int[] nums = new int[5];

  public void read() {
	int i;
	for (i=0; i<5; i++) {
		nums [i] = Integer.parseInt(JOptionPane.showInputDialog("Enter an integer"));
	}
}
  public void display() {
	int j;
	for (j=0; j<5; j++) {
		System.out.println(nums[j]);
	}
  }
  public void above50() {
	int k;
	int number = 0;

	for (k=0; k<5; k++) {
		if (nums[k]>=50) {
			number+=1;
			System.out.println(nums[k]);
		}
	}
	System.out.println("There are " + number + " number(s) above 50");
  }

  public void formatDisplay( ) {
	int l;
	String msg="";
	for (l=0; l<5; l++) {
		if (l%2==0) {
			msg += l+": " + nums[l] + "        ";
		}
			else {
			msg += l+": " + nums[l] + "\n";
			}
	}
	System.out.println(msg);
}
  public double averageStep() {
	//int m;
	//	for (m=0; m<5; m++) {


	int firstGap = Math.abs(nums[0] - nums[1]);
	int secondGap = Math.abs(nums[1] - nums[2]);
	int thirdGap = Math.abs(nums[2] - nums[3]);
	int forthGap = Math.abs(nums[3] - nums[4]);

	String msg = (""+(firstGap + secondGap + thirdGap + forthGap)/4);
	double msgD = Double.parseDouble(msg);
   return msgD;
   }

}

