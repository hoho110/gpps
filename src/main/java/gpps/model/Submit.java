package gpps.model;

import java.math.BigDecimal;

public class Submit {
	private Integer id;
	/**
	 * 0:申请竞标-> 1:待支付 （支付） 2:竞标中 (融资审核成功) 3:还款中 4：还款完毕
	 * 
	 * 6:流标(融资审核不成功) 7:退订（未支付） 8：异常（额度不足）申请不成功
	 */
	private int state;
	private long createtime = System.currentTimeMillis();
	private Integer lenderId;
	private Integer productId;
	private long lastmodifytime = System.currentTimeMillis();
	private BigDecimal amount = BigDecimal.ZERO;
}
