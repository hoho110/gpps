package gpps.service;

import java.util.List;

import gpps.model.GovermentOrder;
import gpps.model.Product;
import gpps.model.ProductAction;
import gpps.service.exception.IllegalStateConvertException;

public interface IProductService {
	/**
	 * 创建一个产品，同时创建payback
	 * @param product
	 */
	public void create(Product product);
	/**
	 * 更新产品状态
	 * @param productId
	 * @param state
	 * @throws IllegalStateConvertException
	 */
	public void changeState(Integer productId,int state) throws IllegalStateConvertException;
	
	public Product find(Integer productId);
	
	public List<Product> findByGovermentOrder(Integer orderId);
	
	public List<Product> findByState(int state,int offset,int recnum);
	
	public List<Product> findByProductSeriesAndState(Integer productSeriesId,int state,int offset,int recnum);
	
	public void createProductAction(ProductAction productAction);
	
	public List<ProductAction> findByProductId(Integer productId);
	
	public void addAccessory(Integer productId,String path);
	
	public void changeBuyLevel(Integer productId,int buyLevel);
}
