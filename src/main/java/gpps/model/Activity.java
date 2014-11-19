package gpps.model;

public class Activity {
	private Integer id;
	private String name;
	private long applystarttime;
	private long applyendtime;
	private long starttime;
	private String question;
	private String answer;
	private long createtime;
	// 0:未发布;1：报名;2：进行;3：结束
	public static final int STATE_UNPUBLISHED = 0;
	public static final int STATE_APPLY = 1;
	public static final int STATE_FINISH = 2;
	private int state = STATE_UNPUBLISHED;
	private String url;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getApplystarttime() {
		return applystarttime;
	}
	public void setApplystarttime(long applystarttime) {
		this.applystarttime = applystarttime;
	}
	public long getApplyendtime() {
		return applyendtime;
	}
	public void setApplyendtime(long applyendtime) {
		this.applyendtime = applyendtime;
	}
	public long getStarttime() {
		return starttime;
	}
	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
