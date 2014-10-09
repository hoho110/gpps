package gpps.service;

import gpps.model.PayBack;
import gpps.model.ProductSeries;
import gpps.service.exception.UnSupportRepayInAdvanceException;

import java.util.List;

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
	
}
