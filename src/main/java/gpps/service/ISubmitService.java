package gpps.service;

import gpps.model.CashStream;
import gpps.model.PayBack;
import gpps.model.Submit;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.InsufficientBalanceException;
import gpps.service.exception.InsufficientProductException;
import gpps.service.exception.ProductSoldOutException;
import gpps.service.exception.UnreachBuyLevelException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ISubmitService {
	/**
	 * 购买产品,贷款人调用， 贷款人信息从Session中获取
	 * 
	 * @param productId
	 *            产品名称
	 * @param amount
	 *            购买数量
	 * @throws InsufficientBalanceException
	 *             余额不足
	 * @throws ProductSoldOutException
	 *             产品已售完
	 * @throws InsufficientProductException
	 *             产品余额不足
	 * @throws UnreachBuyLevelException
	 *             未达购买级别
	 *             
	 * @return 返回订单号
	 */
	public Integer buy(Integer productId, int amount) throws InsufficientBalanceException, ProductSoldOutException, InsufficientProductException, UnreachBuyLevelException;
	public void confirmBuy(Integer submitId);

	/**
	 * 系统任务调用
	 * 
	 * @param submitId
	 *            订单ID
	 * @param state
	 *            订单状态
	 * @throws IllegalConvertException
	 */
	// public void changeState(Integer submitId,int state)throws
	// IllegalConvertException;

	public Submit find(Integer id);

	/**
	 * 返回用户购买的所有订单，按时间倒序排序
	 * 
	 * @return
	 */
	public Map<String, Object> findMyAllSubmits(int offset, int recnum);

	public Map<String, Object> findMyAllWaitforPayingSubmits(int offset, int recnum);

	public Map<String, Object> findMyAllSubmitsByProductState(int productState);

	/**
	 * 找到该订单的资金流,倒序排列返回
	 * 
	 * @param submit
	 * @return
	 */
	public List<CashStream> findSubmitCashStream(Integer submitId);
	// public void refuseApply(Integer submitId)throws
	// IllegalConvertException;//拒绝申请
	// public void passApply(Integer submitId)throws
	// IllegalConvertException;//通过申请进行待支付

}
