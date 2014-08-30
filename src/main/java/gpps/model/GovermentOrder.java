package gpps.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class GovermentOrder {
	private Integer id;
	private Integer borrowerId;
	private String material;// 记录相关资料附件的路径
	/**
	 * 1：申请融资 ,2：驳回重填, 4：重新申请 ,8：拒绝 ,16：审核通过,32：融资中,64:流标, 128：还款中 ,256：已关闭
	 */
	public static final int STATE_APPLY=1;
	public static final int STATE_MODIFY=1<<1;
	public static final int STATE_REAPPLY=1<<2;
	public static final int STATE_REFUSE=1<<3;
	public static final int STATE_PASS=1<<4;
	public static final int STATE_FINANCING=1<<5;
	public static final int STATE_QUITFINANCING=1<<6;
	public static final int STATE_REPAYING=1<<7;
	public static final int STATE_CLOSE=1<<8;
	private int state=STATE_APPLY;
	private long financingStarttime;
	private long financingEndtime;
	private long createtime=System.currentTimeMillis();
//	private String auditor;//审计员
	private List<Product> products=new ArrayList<Product>();//非持久化字段
	public Lock lock=null;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(Integer borrowerId) {
		this.borrowerId = borrowerId;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getFinancingStarttime() {
		return financingStarttime;
	}
	public void setFinancingStarttime(long financingStarttime) {
		this.financingStarttime = financingStarttime;
	}
	public long getFinancingEndtime() {
		return financingEndtime;
	}
	public void setFinancingEndtime(long financingEndtime) {
		this.financingEndtime = financingEndtime;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	
	
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
