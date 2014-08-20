package gpps.model;

public class GovermentOrder {
	private Integer id;
	private Integer borrowerId;
	private String material;// 记录相关资料附件的路径
	/**
	 * 0：申请融资 -1：驳回重填 -2：重新申请 -100：拒绝 1：审核通过 2：产品已发布 3：融资中 4：还款中 5：已关闭
	 */
	public static final int STATE_APPLY=0;
	public static final int STATE_PASS=1;
	public static final int STATE_PUBLISH=2;
	public static final int STATE_FINANCING=3;
	public static final int STATE_REPAYING=4;
	public static final int STATE_CLOSE=5;
	public static final int STATE_MODIFY=-1;
	public static final int STATE_REAPPLY=-2;
	public static final int STATE_REFUSE=-100;
	private int state=STATE_APPLY;
	private long financingStarttime;
	private long financingEndtime;
//	private String auditor;//审计员
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(Integer borrowerId) {
		this.borrowerId = borrowerId;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getFinancingStarttime() {
		return financingStarttime;
	}
	public void setFinancingStarttime(long financingStarttime) {
		this.financingStarttime = financingStarttime;
	}
	public long getFinancingEndtime() {
		return financingEndtime;
	}
	public void setFinancingEndtime(long financingEndtime) {
		this.financingEndtime = financingEndtime;
	}
}
