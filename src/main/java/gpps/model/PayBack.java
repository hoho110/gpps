package gpps.model;

import java.math.BigDecimal;

public class PayBack {
	private Integer id;
	private Integer borrowerAccountId;
	private Integer productId;
	/**
	 * 0：待还款 1：正在还款 2:已还款 3：延期(待确定)
	 */
	private int state;
	private BigDecimal chiefAmount;
	private BigDecimal interest;
	private long deadline;//还款时间
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
}
