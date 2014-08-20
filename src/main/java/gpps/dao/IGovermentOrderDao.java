package gpps.dao;

import java.util.List;

import gpps.model.GovermentOrder;

public interface IGovermentOrderDao {
	public GovermentOrder create(GovermentOrder govermentOrder);
	public void changeState(int orderId,int state);
	public List<GovermentOrder> findByState(int state,int offset,int recnum);
	public int count(int state);
	public List<GovermentOrder> findByBorrowerIdAndState(int borrowerId,int state);
}
