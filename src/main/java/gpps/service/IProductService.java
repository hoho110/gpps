package gpps.service;

import java.util.List;

import gpps.model.Product;
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
}
