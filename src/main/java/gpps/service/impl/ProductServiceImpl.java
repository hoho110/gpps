package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.IProductActionDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.model.GovermentOrder;
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
import java.util.List;

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
	Logger logger=Logger.getLogger(this.getClass());
	static int[] productStates={
		Product.STATE_FINANCING,
		Product.STATE_REPAYING,
		Product.STATE_QUITFINANCING,
		Product.STATE_FINISHREPAY,
		Product.STATE_POSTPONE,
		Product.STATE_APPLYTOCLOSE,
		Product.STATE_CLOSE
	};
	@Override
	public void create(Product product) {
		checkNullObject("orderId", product.getGovermentorderId());
		checkNullObject(GovermentOrder.class, govermentOrderDao.find(product.getGovermentorderId()));
		product.setState(Product.STATE_FINANCING);
		product.setCreatetime(System.currentTimeMillis());
		checkNullObject("productseriesId", product.getProductseriesId());
		checkNullObject(ProductSeries.class,productSeriesDao.find(product.getProductseriesId()));
		checkNullObject("expectAmount", product.getExpectAmount());
		checkNullObject("rate", product.getRate());
		product.setRealAmount(BigDecimal.ZERO);
		productDao.create(product);
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
		return productDao.find(productId);
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

}
