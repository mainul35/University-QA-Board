import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DateDifferenceTest {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    Date firstDate = new Date(1521105452645L);
		Date secondDate = new Date(System.currentTimeMillis());

		long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

		if (diff<60) {
			System.out.println(diff +"m");
		}else if(diff>60 && diff<1440) {
			System.out.println(diff/60 +"h");
		}else if(diff>1439 && diff<10080) {
			System.out.println(diff/1440 +"d");
		}else if(diff>10079 && diff<525600) {
			System.out.println(diff/10079 +"w");
		}else if(diff>525599) {
			System.out.println(diff/525600 +"y");
		}
	    
	}

}
