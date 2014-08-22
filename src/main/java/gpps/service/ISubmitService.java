package gpps.service;

import gpps.model.CashStream;
import gpps.model.Submit;
import gpps.service.exception.IllegalStateConvertException;
import gpps.service.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.List;
public interface ISubmitService {
	/**
	 * 购买产品,贷款人调用，
	 * 贷款人信息从Session中获取
	 * @param projuctId 产品名称
	 * @param amount 购买数量
	 * @throws InsufficientBalanceException 余额不足
	 */
	public void buy(Integer projuctId,BigDecimal amount)throws InsufficientBalanceException;
	/**
	 * 系统任务调用
	 * @param submitId 订单ID
	 * @param state 订单状态
	 * @throws IllegalStateConvertException
	 */
	public void changeState(Integer submitId,int state)throws IllegalStateConvertException;
	
	public Submit find(Integer id);
	/**
	 * 返回用户购买的所有订单，按时间倒序排序
	 * @return
	 */
	public List<Submit> findAll();
	/**
	 * 找到该订单的资金流,倒序排列返回
	 * @param submit
	 * @return
	 */
	public List<CashStream> findSubmitCashStream(Integer submitId);
}
