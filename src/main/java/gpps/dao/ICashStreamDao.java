package gpps.dao;

import java.util.List;

import gpps.model.CashStream;

public interface ICashStreamDao {
	public void create(CashStream cashStream);
	public void changeCashStreamState(Integer cashStreamId,int state);
	public List<CashStream> findSubmitCashStream(Integer submitId);
}
