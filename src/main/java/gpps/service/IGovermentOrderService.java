package gpps.service;

import gpps.model.FinancingRequest;
import gpps.model.GovermentOrder;
import gpps.model.Product;
import gpps.model.ref.Accessory.MimeItem;
import gpps.service.exception.ExistWaitforPaySubmitException;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.easyservice.xml.XMLParseException;

public interface IGovermentOrderService {
	/**
	 * 创建订单，由管理员创建
	 * @param govermentOrder
	 * @return
	 */
	public GovermentOrder create(GovermentOrder govermentOrder);
	/**
	 * 改变状态
	 * @param orderId
	 * @param states
	 * @throws IllegalConvertException
	 */
//	public void changeState(int orderId,int states) throws IllegalConvertException;
	/**
	 * 根据状态查找
	 * @param states 一个或几个状态并集，-1表示不限
	 * @param offset 偏移量（从0开始）
	 * @param recnum 返回的记录条数
	 * @return
	 */
	public List<GovermentOrder> findByStates(int states,int offset,int recnum);
	/**
	 * 根据借款人/状态查找
	 * @param borrowerId
	 * @param states 一个或几个状态并集，-1表示不限
	 * @return
	 */
	public List<GovermentOrder> findByBorrowerIdAndStates(int borrowerId,int states);
//	public void passApplying(Integer orderId)throws IllegalConvertException;//通过申请
//	public void refuseApplying(Integer orderId)throws IllegalConvertException;//拒绝申请
//	public void reviseApplying(Integer orderId)throws IllegalConvertException;//修订申请
//	public void reApply(Integer orderId)throws IllegalConvertException;//重新申请
	
	public void publish(Integer orderId)throws IllegalConvertException;
	
	public void startFinancing(Integer orderId)throws IllegalConvertException, IllegalOperationException;//启动融资
	/**
	 * 启动还款
	 * @param orderId
	 * @throws IllegalConvertException 非法的状态转换
	 * @throws IllegalOperationException 非法操作(操作流程不合法)
	 * @throws ExistWaitforPaySubmitException 
	 */
	public void startRepaying(Integer orderId)throws IllegalConvertException, IllegalOperationException, ExistWaitforPaySubmitException;//启动还款
	public void quitFinancing(Integer orderId)throws IllegalConvertException, IllegalOperationException, ExistWaitforPaySubmitException;//放弃融资（流标）
	public void closeFinancing(Integer orderId)throws IllegalConvertException;//关闭融资
	public void closeComplete(Integer orderId)throws IllegalConvertException;
	/**
	 * 申请融资中产品，并对订单加锁,必须相应地在finally中调用releaseFinancingProduct方法
	 * @param productId
	 * @param orderId
	 * @return 融资中产品，如果为null，则表示产品已下架，不需要释放
	 */
	public Product applyFinancingProduct(Integer productId,Integer orderId);
	/**
	 * 释放融资中产品
	 * @param product
	 */
	public void releaseFinancingProduct(Product product);
	/**
	 * 申请融资中订单，并对订单加锁,必须相应地在finally中调用releaseFinancingOrder方法
	 * @param orderId
	 * @return
	 */
	public GovermentOrder applyFinancingOrder(Integer orderId);
	/**
	 * 释放融资中的order
	 * @param orderId
	 * @return
	 */
	public void releaseFinancingOrder(GovermentOrder order);
	/**
	 * 
	 * @param productSeriesId
	 * @param states
	 * @param offset
	 * @param recnum
	 * @return total:count (int),result:List<GovermentOrder>
	 */
	public Map<String, Object> findGovermentOrderByProductSeries(Integer productSeriesId,int states,int offset,int recnum);
	
	public Map<String, Object> findByStatesByPage(int states,int offset,int recnum);
	/**
	 * 返回产品所属订单以及订单的所有产品
	 * @param productId
	 * @return
	 */
	public GovermentOrder findGovermentOrderByProduct(Integer productId);
	

	public void addAccessory(Integer orderId,int category,MimeItem item) throws XMLParseException;
	public void delAccessory(Integer orderId,int category,String path) throws XMLParseException;
	public List<MimeItem> findMimeItems(Integer orderId,int category)throws XMLParseException;
	/**
	 * 返回当前借款人的订单
	 * @param states 一个或几个状态并集，-1表示不限
	 * @return
	 */
	public List<GovermentOrder> findBorrowerOrderByStates(int states);
	/**
	 * 返回当前借款人的融资请求，待关联的订单
	 * @param state -1表示不限
	 * @return
	 */
	public List<FinancingRequest> findBorrowerFinancingRequest(int state);
	
	public List<GovermentOrder> findAllUnpublishOrders();
	
	public void update(Integer id,long financingStarttime,long financingEndtime,long incomeStarttime,String description);
}
