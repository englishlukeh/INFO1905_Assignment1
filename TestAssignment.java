import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
public class TestAssignment {

		// This will make it a bit easier for us to make Date objects
		private static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		// This will make it a bit easier for us to make Date objects
		private static Date getDate(String s) {
			try {
				return df.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
				fail("The test case is broken, invalid SimpleDateFormat parse");
			}
			// unreachable
			return null;
		}

		// helper method to compare two Submissions using assertions
		private static void testHelperEquals(String unikey, Date timestamp, Integer grade, Submission actual) {
			assertEquals(unikey, actual.getUnikey());
			assertEquals(timestamp, actual.getTime());
			assertEquals(grade, actual.getGrade());
		}
		
		
		@Test(timeout = 1000)
		public void testEdgeCases(){
			SubmissionHistory history1 = new Assignment();
			SubmissionHistory history2 = new Assignment();
			SubmissionHistory history3 = new Assignment();
			SubmissionHistory history4 = new Assignment();
			
			// test edge case for date, most recent
			history1.add("aaaa1234", getDate("2016/09/03 09:00:00"), 66);
			history1.add("aaaa1234", getDate("2016/09/03 09:00:01"), 66);
			history1.add("aaaa1234", getDate("2016/09/03 09:00:02"), 66);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:02"), 66, history1.getSubmissionFinal("aaaa1234"));
			
			// test edge case for grade
			history2.add("aaaa1234", getDate("2016/09/03 09:00:02"), 66);
			history2.add("aaaa1234", getDate("2016/09/03 09:00:01"), 67);
			history2.add("aaaa1234", getDate("2016/09/03 09:00:00"), 68);
			assertEquals((Integer)68, history2.getBestGrade("aaaa1234"));
			
			// test edge for date, get before deadline
			history3.add("aaaa1234", getDate("2016/09/03 09:00:00"), 66);
			history3.add("aaaa1234", getDate("2016/09/03 09:00:02"), 66);
			history3.add("aaaa1234", getDate("2016/09/03 09:00:04"), 66);
			assertEquals(null, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 08:00:00")));
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:00"), 66, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:01") ));
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:02"), 66, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:03") ));
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:04"), 66, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:05") ));

			// test top students
			history4.add("aaaa1234", getDate("2016/09/03 09:00:00"), 90);
			history4.add("aaaa1234", getDate("2016/09/03 09:00:01"), 89);
			history4.add("bbbb1234", getDate("2016/09/03 09:00:02"), 89);
			history4.add("bbbb1234", getDate("2016/09/03 09:00:03"), 84);
			history4.add("cccc1234", getDate("2016/09/03 09:00:04"), 90);
			history4.add("cccc1234", getDate("2016/09/03 09:00:05"), 0);
			assertEquals(2, history4.listTopStudents().size());
			
			// test regressions
			history4.add("aaaa1234", getDate("2016/09/03 09:00:00"), 90);
			history4.add("aaaa1234", getDate("2016/09/03 09:00:01"), 89);
			history4.add("bbbb1234", getDate("2016/09/03 09:00:02"), 89);
			history4.add("bbbb1234", getDate("2016/09/03 09:00:03"), 84);
			history4.add("cccc1234", getDate("2016/09/03 09:00:04"), 0);
			history4.add("cccc1234", getDate("2016/09/03 09:00:05"), 90);
			assertEquals(2, history4.listRegressions().size());
		}
		
		@Test(timeout = 1000)
		public void testRemoveWithMethods() {
			
			SubmissionHistory history1 = new Assignment();
			SubmissionHistory history2 = new Assignment();
			SubmissionHistory history3 = new Assignment();
			SubmissionHistory history4 = new Assignment();
			
			// test case for date, most recent after removing
			history1.add("aaaa1234", getDate("2016/09/03 09:00:00"), 34);
			history1.add("aaaa1234", getDate("2016/09/03 09:00:01"), 20);
			Submission a1 = history1.add("aaaa1234", getDate("2016/09/03 09:00:02"), 38);
			history1.remove(a1);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:01"), 20, history1.getSubmissionFinal("aaaa1234"));
			
			// test case for grade after removing
			history2.add("aaaa1234", getDate("2016/09/03 09:00:02"), 46);
			history2.add("aaaa1234", getDate("2016/09/03 09:00:01"), 47);
			Submission a2 = history2.add("aaaa1234", getDate("2016/09/03 09:00:00"), 68);
			history2.remove(a2);
			assertEquals((Integer)47, history2.getBestGrade("aaaa1234"));
			
			// test for date, get before deadline after removing
			Submission a3 = history3.add("aaaa1234", getDate("2016/09/03 09:00:00"), 66);
			history3.add("aaaa1234", getDate("2016/09/03 09:00:02"), 66);
			Submission a5 = history3.add("aaaa1234", getDate("2016/09/03 09:00:04"), 66);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:00"), 66, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:01") ));
			history3.remove(a3);
			assertEquals(null, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:01")));
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:04"), 66, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:05") ));
			history3.remove(a5);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:02"), 66, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:05") ));
			
			// test for regressions after removing
			history4.add("aaaa1234", getDate("2016/09/03 09:00:00"), 90);
			Submission a6 = history4.add("aaaa1234", getDate("2016/09/03 09:00:01"), 89);
			history4.add("bbbb1234", getDate("2016/09/03 09:00:02"), 89);
			Submission a7 = history4.add("bbbb1234", getDate("2016/09/03 09:00:03"), 84);
			history4.add("cccc1234", getDate("2016/09/03 09:00:04"), 0);
			history4.add("cccc1234", getDate("2016/09/03 09:00:05"), 90);
			assertEquals(2, history4.listRegressions().size());
			history4.remove(a6);
			assertEquals(1, history4.listRegressions().size());
			history4.remove(a7);
			assertEquals(0, history4.listRegressions().size());
			
		}
		
		@Test(timeout = 1000)
		public void testAddAndRemoveWithMethods() {
			SubmissionHistory history1 = new Assignment();
			SubmissionHistory history2 = new Assignment();
			SubmissionHistory history3 = new Assignment();
			SubmissionHistory history4 = new Assignment();
			
			// test case for date, most recent after adding and removing
			history1.add("aaaa1234", getDate("2016/09/03 09:00:00"), 66);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:00"), 66, history1.getSubmissionFinal("aaaa1234"));
			history1.add("aaaa1234", getDate("2016/09/03 09:00:01"), 66);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:01"), 66, history1.getSubmissionFinal("aaaa1234"));
			Submission a1 = history1.add("aaaa1234", getDate("2016/09/03 09:00:02"), 66);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:02"), 66, history1.getSubmissionFinal("aaaa1234"));
			history1.remove(a1);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:01"), 66, history1.getSubmissionFinal("aaaa1234"));
			
			// test case for grade after adding and removing
			history2.add("aaaa1234", getDate("2016/09/03 09:00:02"), 86);
			assertEquals((Integer)86, history2.getBestGrade("aaaa1234"));
			history2.add("aaaa1234", getDate("2016/09/03 09:00:01"), 87);
			assertEquals((Integer)87, history2.getBestGrade("aaaa1234"));
			Submission a2 = history2.add("aaaa1234", getDate("2016/09/03 09:00:00"), 98);
			history2.remove(a2);
			assertEquals((Integer)87, history2.getBestGrade("aaaa1234"));
			
			// test for date, get before deadline after removing
			Submission a3 = history3.add("aaaa1234", getDate("2016/09/03 09:00:00"), 66);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:00"), 66, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:03") ));
			history3.add("aaaa1234", getDate("2016/09/03 09:00:02"), 56);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:02"), 56, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:03") ));
			Submission a5 = history3.add("aaaa1234", getDate("2016/09/03 09:00:04"), 66);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:00"), 66, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:01") ));
			history3.remove(a3);
			assertEquals(null, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:01")));
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:04"), 66, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:05") ));
			history3.remove(a5);
			testHelperEquals("aaaa1234", getDate("2016/09/03 09:00:02"), 56, history3.getSubmissionBefore("aaaa1234", getDate("2016/09/03 09:00:05") ));
			
			// test for regressions after adding and removing
			history4.add("aaaa1234", getDate("2016/09/03 09:00:00"), 80);
			assertEquals(0, history4.listRegressions().size());
			Submission a6 = history4.add("aaaa1234", getDate("2016/09/03 09:00:01"), 59);
			assertEquals(1, history4.listRegressions().size());
			history4.add("bbbb1234", getDate("2016/09/03 09:00:02"), 23);
			Submission a7 = history4.add("bbbb1234", getDate("2016/09/03 09:00:03"), 84);
			assertEquals(1, history4.listRegressions().size());
			history4.add("cccc1234", getDate("2016/09/03 09:00:04"), 82);
			history4.add("cccc1234", getDate("2016/09/03 09:00:05"), 0);
			assertEquals(2, history4.listRegressions().size());
			history4.remove(a6);
			assertEquals(1, history4.listRegressions().size());
			history4.remove(a7);
			assertEquals(1, history4.listRegressions().size());
		}
		
		@Test(timeout = 1000)
		public void testVolume() {
			SubmissionHistory history = new Assignment();
	
			for (int i = 0; i < 100; i++) {
				if (i < 10) {
					history.add("aaaa1234", getDate("2016/09/03 09:00:0" + i), i);
					history.add("bbbb1234", getDate("2016/09/03 09:00:0" + i), i);
					history.add("cccc1234", getDate("2016/09/03 09:00:0" + i), i);
					history.add("dddd1234", getDate("2016/09/03 09:00:0" + i), i);
				}
				else {
					history.add("aaaa1234", getDate("2016/09/03 09:00:" + i), i);
					history.add("bbbb1234", getDate("2016/09/03 09:00:" + i), i);
					history.add("cccc1234", getDate("2016/09/03 09:00:" + i), i);
					history.add("dddd1234", getDate("2016/09/03 09:00:" + i), i);
				}
			}
			
			assertEquals(0, history.listRegressions().size());
			assertEquals(4, history.listTopStudents().size());

		}
		
		@Test(timeout = 1000)
		public void testStrangeFields() {
			SubmissionHistory history = new Assignment();
			
			try {
			history.add("", getDate("2016/09/03 09:00:00"), 50);
			history.add(" ", getDate("2016/09/03 09:00:00"), 50);
			history.add(" ", getDate("0000/00/00 00:00:00"), 50);
			history.add("a", getDate("2016/09/03 09:00:00"), -10000);
			history.add("a", getDate("2016/09/03 09:00:00"), 100000);
			history.add("", getDate("9999/12/25 12:59:59"), 0);
			
			}catch (Exception e) {
				fail("Through exceptions for strange fields");
			}
		}
}
