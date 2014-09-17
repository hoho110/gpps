package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import gpps.constant.Pagination;
import gpps.dao.ICashStreamDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.IProductDao;
import gpps.dao.ISubmitDao;
import gpps.model.CashStream;
import gpps.model.GovermentOrder;
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
import java.util.ArrayList;
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
	public Integer buy(Integer productId, int num)
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
			//计算已还款
			if(submit.getState()!=Submit.STATE_COMPLETEPAY)
				continue;
			List<CashStream> cashStreams=findSubmitCashStream(submit.getId());
			if(cashStreams==null||cashStreams.size()==0)
				continue;
			for(CashStream cashStream:cashStreams)
			{
				if(cashStream.getAction()==CashStream.ACTION_REPAY&&cashStream.getState()==CashStream.STATE_SUCCESS)
				{
					submit.getRepayedAmount().add(cashStream.getChiefamount());
				}
			}
		}
		return Pagination.buildResult(submits,count,offset, recnum);
	}

	@Override
	public List<CashStream> findSubmitCashStream(Integer submitId) {
		return cashStreamDao.findSubmitCashStream(submitId);
	}

	@Override
	public List<Submit> findMyAllWaitforPayingSubmits() {
		Lender lender=lenderService.getCurrentUser();
		List<Integer> states=new ArrayList<Integer>();
		states.add(Submit.STATE_WAITFORPAY);
		List<Submit> submits=submitDao.findAllByLenderAndStates(lender.getId(), states);
		if(submits==null||submits.size()==0)
			return new ArrayList<Submit>(0);
		for(Submit submit:submits)
		{
			submit.setProduct(productDao.find(submit.getProductId()));
			submit.getProduct().setGovermentOrder(govermentOrderDao.find(submit.getProduct().getGovermentorderId()));
			submit.setPayExpiredTime(submit.getCreatetime()+Submit.PAYEXPIREDTIME);
		}
		return submits;
//		List<Submit> submits=new ArrayList<Submit>();
//		for(int i=0;i<100;i++)
//		{
//			Submit submit=new Submit();
//			submit.setId(i);
//			submit.setAmount(new BigDecimal(10000));
//			submit.setProduct(new Product());
//			submit.getProduct().setId(i);
//			submit.getProduct().setGovermentOrder(new GovermentOrder());
//			submit.getProduct().getGovermentOrder().setTitle("淘宝借款三期");
//			submit.setPayExpiredTime(System.currentTimeMillis()+Submit.PAYEXPIREDTIME);
//			submits.add(submit);
//		}
//		return submits;
	}

	@Override
	public Map<String, Object> findMyAllSubmitsByProductStates(int productStates,int offset,int recnum) {
		Lender lender=lenderService.getCurrentUser();
		List<Integer> stateList=null;
		if(productStates!=-1)
		{
			stateList=new ArrayList<Integer>();
			for(int productState:IProductService.productStates)
			{
				if((productState&productStates)>0)
					stateList.add(productState);
			}
		}
		int count=submitDao.countByLenderAndProductStates(lender.getId(), stateList);
		if(count==0)
			return Pagination.buildResult(null, count, offset, recnum);
		List<Submit> submits=submitDao.findAllPayedByLenderAndProductStates(lender.getId(), stateList, offset, recnum);
		for(Submit submit:submits)
		{
			submit.setProduct(productDao.find(submit.getProductId()));
			submit.getProduct().setGovermentOrder(govermentOrderDao.find(submit.getProduct().getGovermentorderId()));
			//计算已还款
			if(submit.getState()!=Submit.STATE_COMPLETEPAY)
				continue;
			List<CashStream> cashStreams=findSubmitCashStream(submit.getId());
			if(cashStreams==null||cashStreams.size()==0)
				continue;
			for(CashStream cashStream:cashStreams)
			{
				if(cashStream.getAction()==CashStream.ACTION_REPAY&&cashStream.getState()==CashStream.STATE_SUCCESS)
				{
					submit.getRepayedAmount().add(cashStream.getChiefamount());
				}
			}
		}
		return Pagination.buildResult(submits,count,offset, recnum);
	}

	@Override
	public void confirmBuy(Integer submitId) {
		submitDao.changeState(submitId, Submit.STATE_COMPLETEPAY);
	}
}
