package gpps.model;

import java.math.BigDecimal;

public class PayBack {
	private Integer id;
	private Integer borrowerAccountId;
	private Integer productId;
	/**
	 * 0：待还款 1：正在还款 2:已还款 3：延期(待确定)
	 */
	public static int STATE_WAITFORREPAY=0;
	public static int STATE_REPAYING=1;
	public static int STATE_FINISHREPAY=2;
	public static int STATE_DELAY=3;
	private int state;
	private BigDecimal chiefAmount;
	private BigDecimal interest;
	private long deadline;//还款时间
	private long realtime;//实际还款时间
	public static final int TYPE_INTERESTANDCHIEF=0;
	public static final int TYPE_LASTPAY=1;
	private int type;//还款方式 0:利息和本息;1:一次性本金
//	private Integer paybackId;
//	/**
//	 * 0：正常还款 1：新增（指向延期）
//	 */
//	private int type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBorrowerAccountId() {
		return borrowerAccountId;
	}
	public void setBorrowerAccountId(Integer borrowerAccountId) {
		this.borrowerAccountId = borrowerAccountId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public BigDecimal getChiefAmount() {
		return chiefAmount;
	}
	public void setChiefAmount(BigDecimal chiefAmount) {
		this.chiefAmount = chiefAmount;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public long getDeadline() {
		return deadline;
	}
	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}
	public long getRealtime() {
		return realtime;
	}
	public void setRealtime(long realtime) {
		this.realtime = realtime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
