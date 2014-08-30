package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import gpps.dao.ICashStreamDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.IProductDao;
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
import gpps.service.exception.InsufficientProductException;
import gpps.service.exception.ProductSoldOutException;
import gpps.service.exception.UnreachBuyLevelException;

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
	@Autowired
	IProductDao productDao;
	@Override
	@Transactional
	public void buy(Integer productId, BigDecimal amount)
			throws InsufficientBalanceException,ProductSoldOutException,InsufficientProductException,UnreachBuyLevelException {
		//判断当前账户余额是否足够购买
		checkNullObject("amount", amount);
		//TODO 验证amount格式，例如：1w起之类的
		Lender lender=lenderService.getCurrentUser();
		LenderAccount account=lenderAccountDao.find(lender.getAccountId());
		if(amount.compareTo(account.getUsable())>0)
			throw new InsufficientBalanceException();
		Product product=productService.find(productId);
		checkNullObject(Product.class, product);
		if(lender.getGrade()<product.getLevelToBuy())
			throw new UnreachBuyLevelException();
		try
		{
			product=orderService.applyFinancingProduct(productId, product.getGovermentorderId());
			if(product==null)
				throw new ProductSoldOutException();
			if(amount.compareTo(product.getExpectAmount().subtract(product.getRealAmount()))>0)
				throw new InsufficientProductException();
			
			Submit submit=new Submit();
			submit.setAmount(amount);
			submit.setLenderId(lenderService.getCurrentUser().getId());
			submit.setProductId(productId);
			submit.setState(Submit.STATE_APPLY);//TODO 确认状态
			submitDao.create(submit);
			productDao.buy(productId, amount);
			accountService.freezeLenderAccount(lender.getAccountId(), amount, submit.getId(), null);
			product.setRealAmount(product.getRealAmount().add(amount));
		}finally
		{
			orderService.releaseFinancingProduct(product);
		}
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
