import java.text.SimpleDateFormat;

public class AssignmentTest {

	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
		SubmissionHistory history = new Assignment ();
		try {
		history.add(" aaaa1234 ", df. parse ("2016/09/03 09:00:00"), 66); // submission A
		history.add(" aaaa1234 ", df. parse ("2016/09/03 16:00:00"), 86); // submission B
		history.add(" cccc1234 ", df. parse ("2016/09/03 16:00:00"), 73); // submission C
		history.add(" aaaa1234 ", df. parse ("2016/09/03 18:00:00"), 40); // submission D
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		// This will return an Integer corresponding to the number 86
		Integer example1 = history . getBestGrade (" aaaa1234 ");
		System.out.println(example1);	
		
		// This will return null
		Integer example2 = history . getBestGrade (" zzzz1234 ");
		System.out.println(example2);
		
		try {
		// This will throw new IllegalArgumentException ();
		Integer example3 = history . getBestGrade ( null );
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		// This will return a Submission corresponding to submission D
		Submission example4 = history . getSubmissionFinal (" aaaa1234 ");
		System.out.println(example4.getTime());
		
		// This will return a Submission corresponding to submission C
		Submission example5 = history . getSubmissionFinal (" cccc1234 ");
		System.out.println(example5.getTime());
		
		// This will return a Submission corresponding to submission A
		try {
		Submission example6 = history . getSubmissionBefore (" aaaa1234 ", df. parse (" 2016/09/03 13:00:00 "));
		System.out.println(example6.getTime());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

		
		// This will return null
		try {
		Submission example7 = history . getSubmissionBefore (" cccc1234 ", df. parse (" 2016/09/03 13:00:00 "));
		System.out.println(example7.getTime());
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// This will return a list containing only {" aaaa1234 "}
		// because that student 's final submission had grade 40 , but their best was 86
		//List < String > example8 = history . listRegressions ();
		// This will return a list containing only {" aaaa1234 "}
		// because that was the only student with the highest grade
		//List < String > example9 = history . listTopStudents ();
		// If we added another student with the same highest mark , they would both be returned
		// If we instead removed submission B, then {" cccc1234 "} would become the top student
		
		
		
	}
	
}
