package gpps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpps.dao.ICashStreamDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.service.IPayBackService;
import gpps.service.PayBackDetail;
@Service
public class PayBackServiceImpl implements IPayBackService {
	@Autowired
	IPayBackDao payBackDao;
	@Autowired
	ICashStreamDao cashStreamDao;
	@Autowired
	IProductDao productDao;
	@Override
	public void create(PayBack payback) {
		payBackDao.create(payback);
	}

	@Override
	public List<PayBack> findAll(Integer productId) {
		Product product=productDao.find(productId);
		List<PayBack> payBacks=payBackDao.findAllByProduct(productId);
		if(payBacks==null||payBacks.size()==0)
			return new ArrayList<PayBack>(0);
		for(PayBack payBack:payBacks)
		{
			if(product.getState()==Product.STATE_FINANCING||product.getState()==Product.STATE_QUITFINANCING)
			{
				payBack.setChiefAmount(payBack.getChiefAmount().multiply(product.getExpectAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
				payBack.setInterest(payBack.getInterest().multiply(product.getExpectAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			}else
			{
				payBack.setChiefAmount(payBack.getChiefAmount().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
				payBack.setInterest(payBack.getInterest().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			}
		}
		return payBackDao.findAllByProduct(productId);
	}

	@Override
	public void changeState(Integer paybackId, int state) {
		payBackDao.changeState(paybackId, state);
	}
}
