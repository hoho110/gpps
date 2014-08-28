package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.model.GovermentOrder;
import gpps.model.Product;
import gpps.model.ProductAction;
import gpps.model.ProductSeries;
import gpps.service.IProductService;
import gpps.service.exception.IllegalConvertException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ProductServiceImpl implements IProductService {
	@Autowired
	IGovermentOrderDao govermentOrderDao;
	@Autowired
	IProductSeriesDao productSeriesDao;
	@Autowired
	IProductDao productDao;
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
		// TODO Auto-generated method stub
	}

	@Override
	public List<ProductAction> findByProductId(Integer productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAccessory(Integer productId, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeBuyLevel(Integer productId, int buyLevel) {
		productDao.changeBuyLevel(productId,buyLevel);
	}
	
	@Override
	public void startRepaying(Integer productId) throws IllegalConvertException {
		changeState(productId, Product.STATE_REPAYING);
	}

	@Override
	public void quitFinancing(Integer productId) throws IllegalConvertException {
		changeState(productId, Product.STATE_QUITFINANCING);		
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
