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
	private int state=0;
}
