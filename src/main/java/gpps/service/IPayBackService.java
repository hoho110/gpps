package gpps.service;

import gpps.model.PayBack;
import gpps.service.exception.UnSupportDelayException;

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
	public PayBack applyToDelay(Integer payBackId,long delayTo) throws UnSupportDelayException;
}
