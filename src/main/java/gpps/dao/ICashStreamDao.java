package gpps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gpps.model.CashStream;
import gpps.model.PayBack;
import gpps.service.PayBackDetail;

public interface ICashStreamDao {
	public void create(CashStream cashStream);
	public void changeCashStreamState(@Param("cashStreamId") Integer cashStreamId,@Param("state") int state);
	public List<CashStream> findSubmitCashStream(Integer submitId);
	public CashStream find(Integer id);
	public void delete(Integer id);
	public List<CashStream> findByState(int state);
	public void deleteByLenderAccountId(Integer accountId);//测试使用
	public void deleteByBorrowerAccountId(Integer accountId);//测试使用
	/**
	 * 
	 * @param action -1为不限
	 * @param state  -1为不限
	 * @return
	 */
	public List<CashStream> findByActionAndState(@Param("lenderAccountId")Integer lenderAccountId,@Param("borrowerAccountId")Integer borrowerAccountId,@Param("action")int action,@Param("state")int state,@Param("offset")int offset,@Param("recnum")int recnum);
	public int countByActionAndState(@Param("lenderAccountId")Integer lenderAccountId,@Param("borrowerAccountId")Integer borrowerAccountId,@Param("action")int action,@Param("state")int state);
	public PayBackDetail sumLenderRepayed(@Param("lenderAccountId")Integer lenderAccountId,@Param("starttime")long starttime,@Param("endtime")long endtime);
	/**
	 * 找到订单的所有还款
	 * @param submitId
	 * @param payBackId null为不限
	 * @return
	 */
	public List<CashStream> findRepayCashStream(@Param("submitId")Integer submitId,@Param("payBackId")Integer payBackId);
}
