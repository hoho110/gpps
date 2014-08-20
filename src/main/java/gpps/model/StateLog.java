package gpps.model;

public class StateLog {
	private Integer id;
	/**
	 * 0：submit 1：product 2：Govermentorder
	 */
	private int type;
	private long createtime = System.currentTimeMillis();
	private int from;
	private int to;
	private Integer refid;
}
