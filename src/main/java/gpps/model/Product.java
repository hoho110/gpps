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
	private String schema;
	private Integer productseriesId;
	private int levelToBuy;
}
