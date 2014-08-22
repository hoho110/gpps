package gpps.service;

import gpps.model.GovermentOrder;
import gpps.service.exception.IllegalStateConvertException;

import java.util.List;

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
	 * @param state
	 * @throws IllegalStateConvertException
	 */
	public void changeState(int orderId,int state) throws IllegalStateConvertException;
	/**
	 * 根据状态查找
	 * @param state
	 * @param offset 偏移量（从0开始）
	 * @param recnum 返回的记录条数
	 * @return
	 */
	public List<GovermentOrder> findByState(int state,int offset,int recnum);
	/**
	 * 根据借款人/状态查找
	 * @param borrowerId
	 * @return
	 */
	public List<GovermentOrder> findByBorrowerIdAndState(int borrowerId,int state);
	
	public void addAccessory(Integer orderId,String path);
}
