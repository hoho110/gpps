package gpps.service;

import gpps.model.GovermentOrder;
import gpps.service.exception.IllegalConvertException;

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
	 * @param states
	 * @throws IllegalConvertException
	 */
//	public void changeState(int orderId,int states) throws IllegalConvertException;
	/**
	 * 根据状态查找
	 * @param states -1表示不限
	 * @param offset 偏移量（从0开始）
	 * @param recnum 返回的记录条数
	 * @return
	 */
	public List<GovermentOrder> findByState(int states,int offset,int recnum);
	/**
	 * 根据借款人/状态查找
	 * @param borrowerId
	 * @param states -1表示不限
	 * @return
	 */
	public List<GovermentOrder> findByBorrowerIdAndState(int borrowerId,int states);
	
	public void addAccessory(Integer orderId,String path);
	
	public void passApplying(Integer orderId)throws IllegalConvertException;//通过申请
	public void refuseApplying(Integer orderId)throws IllegalConvertException;//拒绝申请
	public void reviseApplying(Integer orderId)throws IllegalConvertException;//修订申请
	public void reApply(Integer orderId)throws IllegalConvertException;//重新申请
	public void startFinancing(Integer orderId)throws IllegalConvertException;//启动融资
	public void startRepaying(Integer orderId)throws IllegalConvertException;//启动还款
	public void quitFinancing(Integer orderId)throws IllegalConvertException;//放弃融资（流标）
	public void closeFinancing(Integer orderId)throws IllegalConvertException;//关闭融资
}
