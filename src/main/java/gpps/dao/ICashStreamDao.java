package gpps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gpps.model.CashStream;

public interface ICashStreamDao {
	public void create(CashStream cashStream);
	public void changeCashStreamState(@Param("cashStreamId") Integer cashStreamId,@Param("state") int state);
	public List<CashStream> findSubmitCashStream(Integer submitId);
}
