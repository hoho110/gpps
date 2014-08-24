package gpps.model;

import java.math.BigDecimal;

public class Product {
	private Integer id;
	/**
	 * 0：投标中 1：还款中 -1：申请关闭 -2：延期 2：已关闭 3：还款完成 4：流标
	 */
	private int state;
	private Integer govermentorderId;
	private BigDecimal expectAmount = BigDecimal.ZERO;
	private BigDecimal realAmount = BigDecimal.ZERO;
	private BigDecimal rate = BigDecimal.ZERO;
	/**
	 * 0： 等额本息 1：先还利息，到期还本 2：到期还本付息
	 * 
	 * 0:按日 1：按周 2：按半月 3：按月 4：按指定日期 9：未指定
	 */
	private int paybackmodel;
	private String accessory;
	private Integer productseriesId;
	private int levelToBuy;
	private long createtime=System.currentTimeMillis();
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
	public Integer getGovermentorderId() {
		return govermentorderId;
	}
	public void setGovermentorderId(Integer govermentorderId) {
		this.govermentorderId = govermentorderId;
	}
	public BigDecimal getExpectAmount() {
		return expectAmount;
	}
	public void setExpectAmount(BigDecimal expectAmount) {
		this.expectAmount = expectAmount;
	}
	public BigDecimal getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public int getPaybackmodel() {
		return paybackmodel;
	}
	public void setPaybackmodel(int paybackmodel) {
		this.paybackmodel = paybackmodel;
	}
	public String getAccessory() {
		return accessory;
	}
	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}
	public Integer getProductseriesId() {
		return productseriesId;
	}
	public void setProductseriesId(Integer productseriesId) {
		this.productseriesId = productseriesId;
	}
	public int getLevelToBuy() {
		return levelToBuy;
	}
	public void setLevelToBuy(int levelToBuy) {
		this.levelToBuy = levelToBuy;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
}
