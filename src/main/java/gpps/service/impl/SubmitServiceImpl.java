package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import gpps.dao.ICashStreamDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.ISubmitDao;
import gpps.model.CashStream;
import gpps.model.Lender;
import gpps.model.LenderAccount;
import gpps.model.Product;
import gpps.model.Submit;
import gpps.service.IAccountService;
import gpps.service.IGovermentOrderService;
import gpps.service.ILenderService;
import gpps.service.IProductService;
import gpps.service.ISubmitService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
	@Autowired
	IAccountService accountService;
	@Autowired
	ILenderAccountDao lenderAccountDao;
	@Autowired
	IGovermentOrderService orderService;
	@Override
	@Transactional
	public void buy(Integer productId, BigDecimal amount)
			throws InsufficientBalanceException {
		//判断当前账户余额是否足够购买
		checkNullObject("amount", amount);
		Lender lender=lenderService.getCurrentUser();
		LenderAccount account=lenderAccountDao.find(lender.getAccountId());
		if(amount.compareTo(account.getUsable())>0)
			throw new InsufficientBalanceException();
		Product product=productService.find(productId);
		checkNullObject(Product.class, product);
		//1.申请购买产品
		product=orderService.applyFinancingProduct(productId, product.getGovermentorderId());
		
		
		Submit submit=new Submit();
		submit.setAmount(amount);
		submit.setLenderId(lenderService.getCurrentUser().getId());
		submit.setProductId(productId);
		submit.setState(Submit.STATE_APPLY);
		submitDao.create(submit);
		//申请
		
	}

	private void changeState(Integer submitId, int state)
			throws IllegalConvertException {
		
	}

	@Override
	public Submit find(Integer id) {
		return submitDao.find(id);
	}

	@Override
	public List<Submit> findMyAllSubmits() {
		return submitDao.findAllByLender(lenderService.getCurrentUser().getId());
	}

	@Override
	public List<CashStream> findSubmitCashStream(Integer submitId) {
		return cashStreamDao.findSubmitCashStream(submitId);
	}
}
