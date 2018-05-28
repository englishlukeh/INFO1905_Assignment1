import java.util.Date;

public class SubmissionFile implements Submission {

	private String unikey;
	private Date timestamp;
	private Integer grade;
	
	
	public SubmissionFile(String unikey, Date timestamp, Integer grade) {
		this.unikey = unikey;
		this.timestamp = timestamp;
		this.grade = grade;
	}
	
	@Override
	public String getUnikey() {
		return unikey;
	}

	@Override
	public Date getTime() {
		return timestamp;
	}

	@Override
	public Integer getGrade() {
		return grade;
	}

}
