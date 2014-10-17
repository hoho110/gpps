package gpps.service;

import gpps.model.PayBack;
import gpps.model.ProductSeries;
import gpps.service.exception.UnSupportRepayInAdvanceException;

import java.util.List;
import java.util.Map;

public interface IPayBackService {
	public void create(PayBack payback);
	public List<PayBack> findAll(Integer productId);
	public void changeState(Integer paybackId,int state);
	public PayBack find(Integer id);
//	public List<PayBackDetail> getMyPaybackDetail(int paybackState);
	/**
	 * 申请延期
	 * @param payBackId
	 * @param delayTo
	 * @return
	 * @throws UnSupportDelayException
	 */
	public void applyRepayInAdvance(Integer payBackId) throws UnSupportRepayInAdvanceException;
	/**
	 * 设置payback 延时
	 * @param payBackId
	 */
	public void delay(Integer payBackId);
	/**
	 * 生成还款计划
	 * @param amount 贷款金额
	 * @param rate 年利率
	 * @param payBackModel 还款模型{@link ProductSeries.type}
	 * @param from 起始时间
	 * @param to 结束时间
	 * @return
	 */
	public List<PayBack> generatePayBacks(int amount,double rate,int payBackModel,
			long from, long to);
	
	public List<PayBack> generatePayBacks(Integer productId,int amount);
	/**
	 * 获取当前借款人的还款
	 * @param state 还款状态，-1为不限
	 * @param starttime 开始时间，-1为不限
	 * @param endtime 截止时间，-1为不限
	 * @return
	 */
	public Map<String, Object> findBorrowerPayBacks(int state,long starttime,long endtime,int offset,int recnum);
	
	public List<PayBack> findBorrowerCanBeRepayedPayBacks();
	
	public boolean canRepay(Integer payBackId);
	
	public boolean canRepayInAdvance(Integer payBackId);
}
