
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;

public class Assignment implements SubmissionHistory {
	
	/** 
	 * Declare dataStructure object and gradesStructure
	 * 
	 * dataStructure is a TreeMap with keys of unikeys (String), and values of an ArrayList with two TreeMaps:
	 * TreeMap 0 holds keys of timestamp (Date) mapping to values of the corresponding submission files (Submission)
	 *  TreeMap 0 is hereinafter referred to as dateSubmission
	 * 
	 * TreeMap 1 holds keys of grade (Integer) mapping to values of the count of submissions which scored that grade (Integer) for the given unikey
	 *  TreeMap1 is hereinafter referred to as gradeCount
	 * 
	 * gradesStructure is a TreeMap with keys of Grades (Integer), and values of an ArrayList of Strings
	 * The ArrayList holds the unikeys (String) which have at least one submission which scored the corresponding grade
    */
    private TreeMap<String, ArrayList<TreeMap<Object, Object>>> dataStructure;
    private TreeMap<Integer, TreeSet<String>> gradesStructure;
    
    /**
     * Helper method to return dateSubmission TreeMap for given Unikey
     */
    private TreeMap<Object, Object> getDateSubmission(String unikey){
    	return dataStructure.get(unikey).get(0);
    }

    /**
     * Helper method to return gradeCount TreeMap for given Unikey
     */
    private TreeMap<Object, Object> getGradeCount(String unikey){
    	return dataStructure.get(unikey).get(1);
    }
    
	/**
	 * Initialize blank data structures for dataStructure and gradesStructure
	 */
	public Assignment() {
		
        dataStructure = new TreeMap<String, ArrayList<TreeMap<Object, Object>>>();
        
        // reverse order is used so highest Integer is firstKey, i.e. so that finding the highest grade can be completed in O(1) time
        gradesStructure = new TreeMap<Integer, TreeSet<String>>(Collections.reverseOrder());
        
    }

	/**
	 * If unikey is null, throw IllegalArgumentException
	 * If dataStructure doesn't contain the unikey (i.e. no submissions), return null
	 * Else, return gradeCount's highest key (i.e. best grade) for corresponding unikey
	 */
    @Override
    public Integer getBestGrade(String unikey) throws IllegalArgumentException {
    	if (unikey == null)
    		throw new IllegalArgumentException();
    	
    	if (!dataStructure.containsKey(unikey))
    		return null;
    	
    	// return max key (i.e. grade) from second TreeMap (gradeCount)
    	return (Integer) getGradeCount(unikey).firstKey();
    }

    /**
     * If Unikey is null, throw IllegalArgumentException
     * If dataStructure doesn't contain the unikey (i.e. no submissions), return null
     * Else, return the dateSubmission's highest key (i.e. latest submission) for corresponding unikey
     */
    @Override
    public Submission getSubmissionFinal(String unikey) throws IllegalArgumentException{
        if (unikey == null)
        	throw new IllegalArgumentException();
        
        if (!dataStructure.containsKey(unikey))
        	return null;
        
        // return value (Submission) for max key (i.e. date) from first TreeMap (dateSubmission)
        return (Submission) getDateSubmission(unikey).firstEntry().getValue();
    }

    /**
     * If any parameters are null, throw IllegalArgumentException
     * If dataStructure doesn't contain the unikey (i.e. no submissions), return null
     * 
     * If unikey's dateSubmission doesn't contain a key less than or equal to deadline (i.e. before or on the given date), return null
     * Else, return unikey's dateSubmission's Submission corresponding to Date key less than or equal to deadline
     */
    @Override
    public Submission getSubmissionBefore(String unikey, Date deadline) throws IllegalArgumentException {
    	if (unikey == null || deadline == null) {
    		throw new IllegalArgumentException();
    	}
    	
    	if (!dataStructure.containsKey(unikey)) {
    		return null;
    	}
    	
    	// return ceilingEntry (entry less than or equal to deadline; ceilingEntry used since order is reversed), function returns null if no such key exists
    	else {
    		// this check is necessary as null cannot be typecast as Submission
    		if (getDateSubmission(unikey).ceilingEntry(deadline) == null)
    			return null;
    		else
    			return (Submission) getDateSubmission(unikey).ceilingEntry(deadline).getValue();
    	}
    }

    /**
     * If any parameters are null, throw IllegalArgumentException
     * If unikey not in dataStructure yet, create new entry and put first submission into corresponding TreeMaps.
     * If unikey already in dataStructure's keyset, append new submission into unikey's values.
     */
    @Override
    public Submission add(String unikey, Date timestamp, Integer grade) throws IllegalArgumentException {

    	if (unikey == null || timestamp == null || grade == null)
    		throw new IllegalArgumentException();
    	
        Submission newSubmission = new SubmissionFile(unikey, timestamp, grade);
        
        // if unikey not in dataStructure yet, create new entry and put first submission into corresponding TreeMaps
        if (!dataStructure.containsKey(unikey)) {
        	ArrayList<TreeMap<Object, Object>> unikeyData = new ArrayList<TreeMap<Object, Object>>();
        	
        	// reverseOrder is used so that highest key is first; firstKey runs in O(1) time
 
        	TreeMap<Object, Object> dateSubmission = new TreeMap<Object, Object>(Collections.reverseOrder());
        	dateSubmission.put(timestamp, newSubmission);
        	TreeMap<Object, Object> gradeCount = new TreeMap<Object, Object>(Collections.reverseOrder());
        	gradeCount.put(grade, (Integer)1);
        	
        	
            unikeyData.add(dateSubmission);
            unikeyData.add(gradeCount);
    		
            dataStructure.put(unikey, unikeyData);  
        }
        
        // else, append new submission into unikey's values
        else {
        	// put dateSubmission into first TreeMap (dateSubmission) and update grade count if necessary in second TreeMap (gradeCount)
        	getDateSubmission(unikey).put(timestamp, newSubmission);
        	if (getGradeCount(unikey).containsKey(grade)){
        		getGradeCount(unikey).put(grade, (Integer) getGradeCount(unikey).get(grade) + 1);
        	}
        	// gradeCount doesn't contain grade in keyset, so put key of grade mapped to value of 1
        	else {
        		getGradeCount(unikey).put(grade, 1);
        	}
        }
        
        // if grade not in gradesStructure, create new TreeMap for it
        if (!gradesStructure.containsKey(grade)) {
        	TreeSet<String> tmpTreeSet = new TreeSet<String>();
        	tmpTreeSet.add(unikey);
        	gradesStructure.put(grade, tmpTreeSet);
        }
        
        // else grade exists, so add unikey to grade's value's TreeSet, if the unikey is already present replaces (since duplicates not allowed in TreeSet)
        else {
            gradesStructure.get(grade).add(unikey);
        }
            
        return newSubmission;
        
    }

    /**
     * If submission or any of its fields are null, throw IllegalArgumentException
     * If submission is not contained in dataStructure, don't do anything
     * Else, remove submission from dataStructure (i.e. remove from dateSubmission and update values in gradeCount and also gradeStructure)
     */
    @Override
    public void remove(Submission submission) throws IllegalArgumentException {
    	// if submission is null, or any of its fields are null, throw IllegalArgumentException
        if (submission == null || submission.getGrade() == null || submission.getTime() == null || submission.getUnikey() == null)
        	throw new IllegalArgumentException();
       
       // Declare and initialize fields of submission for local use
       String unikey = submission.getUnikey();
       Date timestamp = submission.getTime();
       Integer grade = submission.getGrade();
       
       // if submission doesn't exist in dataStructure, return
       if (!(getDateSubmission(unikey).get(timestamp) == submission)) {
    	   return;
       }
       
       // else remove the submission from first TreeMap (dateSubmission)
       getDateSubmission(unikey).remove(timestamp);
       
       // if more than 1 count of grade in first TreeMap (gradeCount), subtract 1 from count
       if ((Integer) getGradeCount(unikey).get(grade) > 1){
    	   // looks yikes, but really its just subtracting 1 from the count in the second TreeMap (gradeCount) with key of submission's grade
    	   getGradeCount(unikey).put(grade, (Integer) getGradeCount(unikey).get(grade) - 1);
       }
       
       // else, there is only 1 count of the grade of submission, so delete key and remove unikey from gradesStructure's TreeSet for grade key
       else {
    	   getGradeCount(unikey).remove(grade);
    	   gradesStructure.get(grade).remove(unikey);
    	   
    	   // if no more unikeys have that grade, delete the key
    	   if (gradesStructure.get(grade).size() == 0) {
    		   gradesStructure.remove(grade);
    	   }
       }
       
       // if there are no more submissions for the corresponding unikey, delete Unikey's key from dataStructure
       if (getDateSubmission(unikey).size() == 0)
    	   dataStructure.remove(unikey);

    }

    /**
     * If no submissions, return empty ArrayList,
     * Else, return ArrayList containing all unikeys in TreeSet corresponding to top grade (i.e. first key in key set) in gradeStructure
     */
    @Override
    public List<String> listTopStudents() {
    	List<String> list = new ArrayList<String>();
    	
    	// if empty gradesStructure, return empty list
    	if (gradesStructure.size() == 0)
    		return list;
    	
    	// else add each of the unikey's which have achieved the highest grade in the gradesStructure to list
        for (String unikey : gradesStructure.get(gradesStructure.firstKey())) {
        	list.add(unikey);
        }
        
        return list;
    }

    /**
     * If no submissions, return empty ArrayList
     * Else, iterate through each entry in dataStructure (i.e. each unikey) and check if most recent submission is lower than best submission and add to ArrayList unikeys for which this is true
     * Then, return ArrayList
     */
    @Override
    public List<String> listRegressions() {
        List<String> list = new ArrayList<String>();
        
        // if empty gradesStrucutre, return empty list
        if (dataStructure.size() == 0)
        	return list;
        
        // iterate through each entry in dataStructure to check if grade of most recent submission is lower than best submission 
        for(String unikey : dataStructure.keySet()) {
        	if (getSubmissionFinal(unikey).getGrade() < getBestGrade(unikey))
        		list.add(unikey);
        }
        
        return list;
    }
}
