package gpps.service;

import java.util.List;

import com.easyservice.xml.XMLParseException;

import gpps.model.GovermentOrder;
import gpps.model.Product;
import gpps.model.ProductAction;
import gpps.model.ref.Accessory.MimeItem;
import gpps.service.exception.ExistWaitforPaySubmitException;
import gpps.service.exception.IllegalConvertException;

public interface IProductService {
	public static final int[] productStates={
		Product.STATE_FINANCING,
		Product.STATE_REPAYING,
		Product.STATE_QUITFINANCING,
		Product.STATE_FINISHREPAY,
		Product.STATE_POSTPONE,
		Product.STATE_APPLYTOCLOSE,
		Product.STATE_CLOSE
	};
	/**
	 * 创建一个产品，同时创建payback
	 * @param product
	 */
	public void create(Product product);
	/**
	 * 更新产品状态
	 * @param productId
	 * @param state
	 * @throws IllegalConvertException
	 */
//	public void changeState(Integer productId,int state) throws IllegalConvertException;
	
	public Product find(Integer productId);
	
	public List<Product> findByGovermentOrder(Integer orderId);
	/**
	 * 根据状态查找
	 * @param states 一个或几个状态并集，-1表示不限
	 * @param offset
	 * @param recnum
	 * @return
	 */
	public List<Product> findByStates(int states,int offset,int recnum);
	/**
	 * 根据产品线及状态查找
	 * @param productSeriesId
	 * @param state 一个或几个状态并集，-1表示不限
	 * @param offset
	 * @param recnum
	 * @return
	 */
	public List<Product> findByProductSeriesAndStates(Integer productSeriesId,int state,int offset,int recnum);
	
	public void createProductAction(ProductAction productAction);
	
	public List<ProductAction> findByProductId(Integer productId);
	
	public void addAccessory(Integer productId,String path);
	
	public void changeBuyLevel(Integer productId,int buyLevel);
	
	public void startRepaying(Integer productId)throws IllegalConvertException,ExistWaitforPaySubmitException;//启动还款
	public void quitFinancing(Integer productId)throws IllegalConvertException, ExistWaitforPaySubmitException;//放弃融资（流标）
	public void delayRepay(Integer productId)throws IllegalConvertException;//延期还款
	public void finishRepay(Integer productId)throws IllegalConvertException;//还款完毕
	public void applyToClose(Integer productId)throws IllegalConvertException;//申请关闭
	public void closeProduct(Integer productId)throws IllegalConvertException;//关闭产品
	
	public void addAccessory(Integer productId,int category,MimeItem item) throws XMLParseException;
	public void delAccessory(Integer productId,int category,String path) throws XMLParseException;
	public List<MimeItem> findMimeItems(Integer productId,int category)throws XMLParseException;
}
