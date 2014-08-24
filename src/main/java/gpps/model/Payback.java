package gpps.model;

import java.math.BigDecimal;

public class Payback {
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
}
