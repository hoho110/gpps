package gpps.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpps.dao.ICashStreamDao;
import gpps.dao.ISubmitDao;
import gpps.model.CashStream;
import gpps.model.Product;
import gpps.model.Submit;
import gpps.service.ILenderService;
import gpps.service.IProductService;
import gpps.service.ISubmitService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.InsufficientBalanceException;
import static gpps.tools.ObjectUtil.*;
@Service
public class SubmitServiceImpl implements ISubmitService {
	@Autowired
	ISubmitDao submitDao;
	@Autowired
	IProductService productService;
	@Autowired
	ILenderService lenderService;
	@Autowired
	ICashStreamDao cashStreamDao;
	@Override
	public void buy(Integer productId, BigDecimal amount)
			throws InsufficientBalanceException {
		checkNullObject(Product.class, productService.find(productId));
		checkNullObject("amount", amount);
		Submit submit=new Submit();
		submit.setAmount(amount);
		submit.setLenderId(lenderService.getCurrentUser().getId());
		submit.setProductId(productId);
		submit.setState(Submit.STATE_APPLY);
		submitDao.create(submit);
		//TODO 加入竞标队列
	}

	@Override
	public void changeState(Integer submitId, int state)
			throws IllegalConvertException {
		// TODO Auto-generated method stub

	}

	@Override
	public Submit find(Integer id) {
		return submitDao.find(id);
	}

	@Override
	public List<Submit> findAll() {
		return submitDao.findAllByLender(lenderService.getCurrentUser().getId());
	}

	@Override
	public List<CashStream> findSubmitCashStream(Integer submitId) {
		return cashStreamDao.findSubmitCashStream(submitId);
	}

}
