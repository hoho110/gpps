package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import gpps.constant.Pagination;
import gpps.dao.ICashStreamDao;
import gpps.dao.IGovermentOrderDao;
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
import java.util.Map;

import org.apache.log4j.Logger;
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
	IGovermentOrderDao govermentOrderDao;
	@Autowired
	IProductDao productDao;
	Logger logger=Logger.getLogger(this.getClass());
	@Override
	@Transactional
	public Integer buy(Integer productId, double num)
			throws InsufficientBalanceException,ProductSoldOutException,InsufficientProductException,UnreachBuyLevelException {
		//TODO 验证amount格式，例如：1w起之类的
		Lender lender=lenderService.getCurrentUser();
		LenderAccount account=lenderAccountDao.find(lender.getAccountId());
		BigDecimal amount=new BigDecimal(num);
		//判断当前账户余额是否足够购买
		if(amount.compareTo(account.getUsable())>0)
			throw new InsufficientBalanceException();
		Product product=productService.find(productId);
		checkNullObject(Product.class, product);
		//判断用户购买级别
		if(lender.getLevel()<product.getLevelToBuy())
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
			submit.setState(Submit.STATE_WAITFORPAY);
			submitDao.create(submit);
			productDao.buy(productId, amount);
//			Integer cashStreamId=accountService.freezeLenderAccount(lender.getAccountId(), amount, submit.getId(), null);
			product.setRealAmount(product.getRealAmount().add(amount));
			return submit.getId();
		}finally
		{
			orderService.releaseFinancingProduct(product);
		}
	}
	static int[][] validConverts={
		};
	private void changeState(Integer submitId, int state)
			throws IllegalConvertException {
		Submit submit = submitDao.find(submitId);
		if (submit == null)
			throw new RuntimeException("submit is not existed");
		for(int[] validStateConvert:validConverts)
		{
			if(submit.getState()==validStateConvert[0]&&state==validStateConvert[1])
			{
				submitDao.changeState(submitId, state);
				return;
			}
		}
		throw new IllegalConvertException();
	}

	@Override
	public Submit find(Integer id) {
		return submitDao.find(id);
	}

	@Override
	public Map<String,Object> findMyAllSubmits(int offset,int recnum) {
		Lender lender=lenderService.getCurrentUser();
		int count=submitDao.countByLender(lender.getId());
		if(count==0)
			return Pagination.buildResult(null, count, offset, recnum);
		List<Submit> submits=submitDao.findAllByLender(lender.getId(), offset, recnum);
		for(Submit submit:submits)
		{
			submit.setProduct(productDao.find(submit.getProductId()));
			submit.getProduct().setGovermentOrder(govermentOrderDao.find(submit.getProduct().getGovermentorderId()));
		}
		return Pagination.buildResult(submits,count,offset, recnum);
	}

	@Override
	public List<CashStream> findSubmitCashStream(Integer submitId) {
		return cashStreamDao.findSubmitCashStream(submitId);
	}

	@Override
	public Map<String, Object> findMyAllWaitforPayingSubmits(int offset, int recnum) {
		return null;
	}

	@Override
	public Map<String, Object> findMyAllSubmitsByProductState(int productState) {
		return null;
	}
}
