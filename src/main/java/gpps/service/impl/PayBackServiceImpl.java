package gpps.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpps.dao.ICashStreamDao;
import gpps.dao.IPayBackDao;
import gpps.model.PayBack;
import gpps.service.IPayBackService;
import gpps.service.PayBackDetail;
@Service
public class PayBackServiceImpl implements IPayBackService {
	@Autowired
	IPayBackDao payBackDao;
	@Autowired
	ICashStreamDao cashStreamDao;
	@Override
	public void create(PayBack payback) {
		payBackDao.create(payback);
	}

	@Override
	public List<PayBack> findAll(Integer productId) {
		return payBackDao.findAllByProduct(productId);
	}

	@Override
	public void changeState(Integer paybackId, int state) {
		payBackDao.changeState(paybackId, state);
	}

	@Override
	public List<PayBackDetail> getMyPaybackDetail(int paybackState) {
		if(paybackState==PayBack.STATE_FINISHREPAY)
		{
			
		}else
		{
			
		}
		return null;
	}

}
