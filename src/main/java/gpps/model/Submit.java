package gpps.model;

import java.math.BigDecimal;

public class Submit {
	private Integer id;
	/**
	 * 0:申请竞标-> 1:待支付 （支付） 2:竞标中 (融资审核成功) 3:还款中 4：还款完毕
	 * 
	 * 6:流标(融资审核不成功) 7:退订（未支付） 8：异常（额度不足）申请不成功
	 */
	public static final int STATE_APPLY=1;
	public static final int STATE_WAITFORPAY=1<<1;//2
	public static final int STATE_INBIDDING=1<<2;//4
	public static final int STATE_REPAYING=1<<3;//8
	public static final int STATE_COMPLETEPAY=1<<4;//16
	public static final int STATE_FAILBIDDING=1<<5;//32
	public static final int STATE_UNSUBSCRIBE=1<<6;//64
	public static final int STATE_FAILAPPLY=1<<7;//128
	private int state=STATE_APPLY;
	private long createtime = System.currentTimeMillis();
	private Integer lenderId;
	private Integer productId;
	private long lastmodifytime = System.currentTimeMillis();
	private BigDecimal amount = BigDecimal.ZERO;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public Integer getLenderId() {
		return lenderId;
	}
	public void setLenderId(Integer lenderId) {
		this.lenderId = lenderId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public long getLastmodifytime() {
		return lastmodifytime;
	}
	public void setLastmodifytime(long lastmodifytime) {
		this.lastmodifytime = lastmodifytime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
