package gpps.model;

import java.math.BigDecimal;

public class Payback {
	private Integer id;
	private Integer borrowerAccountId;
	private Integer productId;
	/**
	 * 0：待还款 1：已还款 2：延期(待确定)
	 */
	private int state;
	private long datetime;
	private BigDecimal chiefAmount;
	private BigDecimal interest;
	private long deadline;
//	private Integer paybackId;
//	/**
//	 * 0：正常还款 1：新增（指向延期）
//	 */
//	private int type;
}
