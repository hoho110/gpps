package gpps.model;
public class Task {
	private Integer id;
	private int type;
	private long createTime;
	public static final int STATE_INIT=0;
	public static final int STATE_PROCESSING=1;
	public static final int STATE_FINISH=2;
	private int state=STATE_INIT;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
