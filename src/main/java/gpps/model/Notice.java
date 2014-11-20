package gpps.model;

public class Notice {
	private Integer id;
	private String title;
	private String content;
	private long publishtime = System.currentTimeMillis();
	public static final int USEFOR_ALL = -1;
	public static final int USEFOR_LENDER = 0;
	public static final int USEFOR_BORROWER = 1;
	private int usefor = -1;// -1:不限 0：lender 1：borrower',
	private int level = -1;// -1：不限，to不为-1时启用
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getPublishtime() {
		return publishtime;
	}
	public void setPublishtime(long publishtime) {
		this.publishtime = publishtime;
	}
	public int getUsefor() {
		return usefor;
	}
	public void setUsefor(int usefor) {
		this.usefor = usefor;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
