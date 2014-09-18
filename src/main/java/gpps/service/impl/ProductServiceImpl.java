package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductActionDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.model.Borrower;
import gpps.model.GovermentOrder;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.ProductAction;
import gpps.model.ProductSeries;
import gpps.model.Task;
import gpps.service.IGovermentOrderService;
import gpps.service.IProductService;
import gpps.service.ITaskService;
import gpps.service.exception.IllegalConvertException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ProductServiceImpl implements IProductService {
	@Autowired
	IGovermentOrderDao govermentOrderDao;
	@Autowired
	IProductSeriesDao productSeriesDao;
	@Autowired
	IGovermentOrderService orderService;
	@Autowired
	IProductDao productDao;
	@Autowired
	ITaskService taskService;
	@Autowired
	IProductActionDao productActionDao;
	@Autowired
	IBorrowerDao borrowerDao;
	@Autowired
	IPayBackDao payBackDao;
	Logger logger=Logger.getLogger(this.getClass());
	@Override
	@Transactional
	public void create(Product product) {
		checkNullObject("orderId", product.getGovermentorderId());
		GovermentOrder order=govermentOrderDao.find(product.getGovermentorderId());
		checkNullObject(GovermentOrder.class, order);
		product.setState(Product.STATE_FINANCING);
		product.setCreatetime(System.currentTimeMillis());
		checkNullObject("productseriesId", product.getProductseriesId());
		ProductSeries productSeries=productSeriesDao.find(product.getProductseriesId());
		checkNullObject(ProductSeries.class,productSeries);
		checkNullObject("expectAmount", product.getExpectAmount());
		checkNullObject("rate", product.getRate());
		product.setRealAmount(BigDecimal.ZERO);
		productDao.create(product);
		Borrower borrower=borrowerDao.find(order.getBorrowerId());
		// 创建还款计划
		PayBack payBack=null;
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar starttime=Calendar.getInstance();
		starttime.setTimeInMillis(order.getIncomeStarttime());
		Calendar endtime=Calendar.getInstance();
		endtime.setTimeInMillis(order.getIncomeEndtime());
		int monthNum=(endtime.get(Calendar.YEAR)-starttime.get(Calendar.YEAR))*12+(endtime.get(Calendar.MONTH)-starttime.get(Calendar.MONTH));
		if(endtime.get(Calendar.DAY_OF_MONTH)>starttime.get(Calendar.DAY_OF_MONTH))
			monthNum++;
		if(productSeries.getType()==ProductSeries.TYPE_AVERAGECAPITALPLUSINTEREST)
		{
			//等额本息，息按天算;本金按月算
			for(int i=0;i<monthNum;i++)
			{
				Calendar currentMonthStart=(Calendar)(starttime.clone());
				currentMonthStart.add(Calendar.MONTH, i);
				Calendar currentMonthEnd=null;
				if(i+1==monthNum)
					currentMonthEnd=(Calendar)(endtime.clone());
				else
				{
					currentMonthEnd=(Calendar)(starttime.clone());
					currentMonthEnd.add(Calendar.MONTH, i+1);
				}
				int days=getDays(currentMonthStart, currentMonthEnd);
				payBack=new PayBack();
				payBack.setBorrowerAccountId(borrower.getAccountId());
				payBack.setChiefAmount(PayBack.BASELINE.divide(new BigDecimal(monthNum),2,BigDecimal.ROUND_UP));
				payBack.setInterest(PayBack.BASELINE.multiply(product.getRate()).multiply(new BigDecimal(days)).divide(new BigDecimal(365),2,BigDecimal.ROUND_UP));
				payBack.setProductId(product.getId());
				payBack.setState(PayBack.STATE_WAITFORREPAY);
				if(i+1==monthNum)
					payBack.setType(PayBack.TYPE_LASTPAY);
				else
					payBack.setType(PayBack.TYPE_INTERESTANDCHIEF);
				currentMonthEnd.add(Calendar.DAY_OF_YEAR, 1);
				payBack.setDeadline(currentMonthEnd.getTimeInMillis());
				payBackDao.create(payBack);
			}
		}else if(productSeries.getType()==ProductSeries.TYPE_FINISHPAYINTERESTANDCAPITAL||productSeries.getType()==ProductSeries.TYPE_FIRSTINTERESTENDCAPITAL)
		{
			for(int i=0;i<monthNum;i++)
			{
				Calendar currentMonthStart=(Calendar)(starttime.clone());
				currentMonthStart.add(Calendar.MONTH, i);
				Calendar currentMonthEnd=null;
				if(i+1==monthNum)
					currentMonthEnd=(Calendar)(endtime.clone());
				else
				{
					currentMonthEnd=(Calendar)(starttime.clone());
					currentMonthEnd.add(Calendar.MONTH, i+1);
				}
				int days=getDays(currentMonthStart, currentMonthEnd);
				payBack=new PayBack();
				payBack.setBorrowerAccountId(borrower.getAccountId());
				payBack.setInterest(PayBack.BASELINE.multiply(product.getRate()).multiply(new BigDecimal(days)).divide(new BigDecimal(365),2,BigDecimal.ROUND_UP));
				payBack.setProductId(product.getId());
				payBack.setState(PayBack.STATE_WAITFORREPAY);
				if(i+1==monthNum)
				{
					payBack.setChiefAmount(PayBack.BASELINE);
					payBack.setType(PayBack.TYPE_LASTPAY);
				}
				else
					payBack.setType(PayBack.TYPE_INTERESTANDCHIEF);
				currentMonthEnd.add(Calendar.DAY_OF_YEAR, 1);
				payBack.setDeadline(currentMonthEnd.getTimeInMillis());
				payBackDao.create(payBack);
			}
		}
	}
	private int getDays(Calendar starttime,Calendar endtime)
	{
		return endtime.get(Calendar.DAY_OF_YEAR)-starttime.get(Calendar.DAY_OF_YEAR);
	}
	static int[][] validConverts={
		{Product.STATE_FINANCING,Product.STATE_REPAYING},
		{Product.STATE_FINANCING,Product.STATE_QUITFINANCING},
		{Product.STATE_REPAYING,Product.STATE_FINISHREPAY},
		{Product.STATE_REPAYING,Product.STATE_POSTPONE},
		{Product.STATE_POSTPONE,Product.STATE_FINISHREPAY},
		{Product.STATE_FINISHREPAY,Product.STATE_APPLYTOCLOSE},
		{Product.STATE_APPLYTOCLOSE,Product.STATE_CLOSE}
		};
	private void changeState(Integer productId, int state)
			throws IllegalConvertException {
		Product product = productDao.find(productId);
		if (product == null)
			throw new RuntimeException("product is not existed");
		for(int[] validStateConvert:validConverts)
		{
			if(product.getState()==validStateConvert[0]&&state==validStateConvert[1])
			{
				productDao.changeState(productId, state);
				return;
			}
		}
		throw new IllegalConvertException();
	}

	@Override
	public Product find(Integer productId) {
		checkNullObject("productId", productId);
		Product product=productDao.find(productId);
		product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
		return product;
	}

	@Override
	public List<Product> findByGovermentOrder(Integer orderId) {
		return productDao.findByGovermentOrder(orderId);
	}

	@Override
	public List<Product> findByStates(int states, int offset, int recnum) {
		List<Integer> list=null;
		if(states!=-1)
		{
			list=new ArrayList<Integer>();
			for(int productState:productStates)
			{
				if((productState&states)>0)
					list.add(productState);
			}
			if(list.isEmpty())
				return new ArrayList<Product>(0);
		}
		return productDao.findByState(list, offset, recnum);
	}

	@Override
	public List<Product> findByProductSeriesAndStates(Integer productSeriesId,
			int states, int offset, int recnum) {
		List<Integer> list=null;
		if(states!=-1)
		{
			list=new ArrayList<Integer>();
			for(int productState:productStates)
			{
				if((productState&states)>0)
					list.add(productState);
			}
			if(list.isEmpty())
				return new ArrayList<Product>(0);
		}
		return productDao.findByProductSeriesAndState(productSeriesId, list, offset, recnum);
	}

	@Override
	public void createProductAction(ProductAction productAction) {
		productAction.setCreatetime(System.currentTimeMillis());
		productActionDao.create(productAction);
	}

	@Override
	public List<ProductAction> findByProductId(Integer productId) {
		return productActionDao.findAllByProduct(productId);
	}

	@Override
	public void addAccessory(Integer productId, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void changeBuyLevel(Integer productId, int buyLevel) {
		checkNullObject("productId", productId);
		Product product=productDao.find(productId);
		checkNullObject(Product.class, product);
		try
		{
			product=orderService.applyFinancingProduct(productId, product.getGovermentorderId());
			productDao.changeBuyLevel(productId,buyLevel);
			if(product!=null)
				product.setLevelToBuy(buyLevel);
		}finally{
			orderService.releaseFinancingProduct(product);
		}
	}
	
	@Override
	@Transactional
	public void startRepaying(Integer productId) throws IllegalConvertException {
		//从竞标缓存中移除
		checkNullObject("productId", productId);
		Product product=productDao.find(productId);
		checkNullObject(Product.class, product);
		GovermentOrder order=null;
		try {
			changeState(productId, Product.STATE_REPAYING);
			order=orderService.applyFinancingOrder(product.getGovermentorderId());
			product=order.findProductById(productId);
			order.getProducts().remove(product);
			product.setState(Product.STATE_REPAYING);
			Task task=new Task();
			task.setProductId(productId);
			task.setType(Task.TYPE_PAY);
			taskService.submit(task);
		}finally
		{
			orderService.releaseFinancingOrder(order);
		}
	}

	@Override
	@Transactional
	public void quitFinancing(Integer productId) throws IllegalConvertException {
		//从竞标缓存中移除
		checkNullObject("productId", productId);
		Product product=productDao.find(productId);
		checkNullObject(Product.class, product);
		GovermentOrder order=null;
		try {
			changeState(productId, Product.STATE_QUITFINANCING);
			//TODO 添加退款任务
			order=orderService.applyFinancingOrder(product.getGovermentorderId());
			product=order.findProductById(productId);
			order.getProducts().remove(product);
			product.setState(Product.STATE_QUITFINANCING);
			Task task=new Task();
			task.setProductId(productId);
			task.setType(Task.TYPE_QUITFINANCING);
			taskService.submit(task);
		}finally
		{
			orderService.releaseFinancingOrder(order);
		}
	}

	@Override
	public void delayRepay(Integer productId) throws IllegalConvertException {
		changeState(productId, Product.STATE_POSTPONE);
	}

	@Override
	public void finishRepay(Integer productId) throws IllegalConvertException {
		changeState(productId, Product.STATE_FINISHREPAY);		
	}

	@Override
	public void applyToClose(Integer productId) throws IllegalConvertException {
		changeState(productId, Product.STATE_APPLYTOCLOSE);		
	}

	@Override
	public void closeProduct(Integer productId) throws IllegalConvertException {
		changeState(productId, Product.STATE_CLOSE);		
	}
	public static void main(String[] args)
	{
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
//		cal.add(Calendar.MONTH, 1);
		System.out.println(cal);
		
	}
}
