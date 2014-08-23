package gpps.model;

import java.math.BigDecimal;

public class CashStream {
	private Integer id;
	/**
	 * 0:向账户充值 1:冻结(包括借款方竞标冻结/融资方还钱冻结) 2：解冻 3:借款方汇出给融资方 4:融资方:汇入借款方 5:从账户提现
	 */
	private int action;
	private long createtime = System.currentTimeMillis();
	private Integer lenderAccountId;
	private Integer borrowerAccountId;
	private Integer submitId;
	private BigDecimal chiefamount = BigDecimal.ZERO;//本金
	private BigDecimal interest = BigDecimal.ZERO;//利息
	private String description;
	private Integer paybackId;
	/**
	 * 0:预操作；1:操作完成;-1操作失败
	 */
	public static final int STATE_INIT=1;
	public static final int STATE_SUCCESS=1<<1;
	public static final int STATE_FAIL=1<<2;
	private int state=STATE_INIT;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public Integer getLenderAccountId() {
		return lenderAccountId;
	}
	public void setLenderAccountId(Integer lenderAccountId) {
		this.lenderAccountId = lenderAccountId;
	}
	public Integer getBorrowerAccountId() {
		return borrowerAccountId;
	}
	public void setBorrowerAccountId(Integer borrowerAccountId) {
		this.borrowerAccountId = borrowerAccountId;
	}
	public Integer getSubmitId() {
		return submitId;
	}
	public void setSubmitId(Integer submitId) {
		this.submitId = submitId;
	}
	public BigDecimal getChiefamount() {
		return chiefamount;
	}
	public void setChiefamount(BigDecimal chiefamount) {
		this.chiefamount = chiefamount;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getPaybackId() {
		return paybackId;
	}
	public void setPaybackId(Integer paybackId) {
		this.paybackId = paybackId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
