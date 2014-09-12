/**
 * 
 */
package gpps.service.impl;

import gpps.constant.Pagination;
import gpps.dao.IBorrowerDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.IProductDao;
import gpps.model.Borrower;
import gpps.model.GovermentOrder;
import gpps.model.Product;
import gpps.service.IGovermentOrderService;
import gpps.service.ITaskService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static gpps.tools.ObjectUtil.*;

/**
 * @author wangm
 *
 */
@Service
public class GovermentOrderServiceImpl implements IGovermentOrderService{
	@Autowired
	IGovermentOrderDao govermentOrderDao;
	@Autowired
	IBorrowerDao borrowerDao;
	@Autowired
	IProductDao productDao;
	@Autowired
	ITaskService taskService;
	static int[] orderStates={
		GovermentOrder.STATE_PREPUBLISH,
		GovermentOrder.STATE_FINANCING,
		GovermentOrder.STATE_QUITFINANCING,
		GovermentOrder.STATE_REPAYING,
		GovermentOrder.STATE_CLOSE
	};
	// Order状态变化，融资金额变化需要修改下面的缓存
	Map<String,GovermentOrder> financingOrders=new HashMap<String,GovermentOrder>();  
	@PostConstruct
	public void init()
	{
		// 启动时将所有正在竞标状态的订单加载到内存中，并分配锁
		List<Integer> states=new ArrayList<Integer>();
		states.add(GovermentOrder.STATE_FINANCING);
		List<GovermentOrder> govermentOrders=govermentOrderDao.findByStates(states);
		if(govermentOrders==null||govermentOrders.size()==0)
			return;
		for(GovermentOrder order:govermentOrders)
		{
			insertGovermentOrderToFinancing(order);
		}
	}
	@Override
	public GovermentOrder create(GovermentOrder govermentOrder) {
		checkNullObject("borrowerId", govermentOrder.getBorrowerId());
		checkNullObject(Borrower.class, borrowerDao.find(govermentOrder.getBorrowerId()));
		govermentOrderDao.create(govermentOrder);
		return govermentOrder;
	}
	@Override
	public List<GovermentOrder> findByStates(int states, int offset, int recnum) {
		List<Integer> list=null;
		if(states!=-1)
		{
			list=new ArrayList<Integer>();
			for(int orderState:orderStates)
			{
				if((orderState&states)>0)
					list.add(orderState);
			}
			if(list.isEmpty())
				return new ArrayList<GovermentOrder>(0);
		}
		return govermentOrderDao.findByStatesWithPaging(list, offset, recnum);
	}
	@Override
	public List<GovermentOrder> findByBorrowerIdAndStates(int borrowerId, int states) {
		List<Integer> list=null;
		if(states!=-1)
		{
			list=new ArrayList<Integer>();
			for(int orderState:orderStates)
			{
				if((orderState&states)>0)
					list.add(orderState);
			}
			if(list.isEmpty())
				return new ArrayList<GovermentOrder>(0);
		}
		return govermentOrderDao.findByBorrowerIdAndState(borrowerId, list);
	}

	@Override
	public void addAccessory(Integer orderId, String path) {
		// TODO Auto-generated method stub
		
	}

	static int[][] validConverts={
		{GovermentOrder.STATE_PREPUBLISH,GovermentOrder.STATE_FINANCING},
		{GovermentOrder.STATE_FINANCING,GovermentOrder.STATE_QUITFINANCING},
		{GovermentOrder.STATE_FINANCING,GovermentOrder.STATE_REPAYING},
		{GovermentOrder.STATE_REPAYING,GovermentOrder.STATE_CLOSE}};
	private void changeState(int orderId, int state) throws IllegalConvertException {
		GovermentOrder order = govermentOrderDao.find(orderId);
		if (order == null)
			throw new RuntimeException("order is not existed");
		for(int[] validStateConvert:validConverts)
		{
			if(order.getState()==validStateConvert[0]&&state==validStateConvert[1])
			{
				govermentOrderDao.changeState(orderId, state);
				return;
			}
		}
		throw new IllegalConvertException();
	}
//	@Override
//	public void passApplying(Integer orderId) throws IllegalConvertException {
//		changeState(orderId, GovermentOrder.STATE_PREPUBLISH);
//	}

//	@Override
//	public void refuseApplying(Integer orderId) throws IllegalConvertException {
//		changeState(orderId, GovermentOrder.STATE_REFUSE);
//	}
//
//	@Override
//	public void reviseApplying(Integer orderId) throws IllegalConvertException {
//		changeState(orderId, GovermentOrder.STATE_MODIFY);	
//	}
//
//	@Override
//	public void reApply(Integer orderId) throws IllegalConvertException {
//		changeState(orderId, GovermentOrder.STATE_REAPPLY);
//	}
	@Override
	@Transactional
	public void startFinancing(Integer orderId) throws IllegalConvertException {
		checkNullObject("orderId", orderId);
		GovermentOrder order=checkNullObject(GovermentOrder.class, govermentOrderDao.find(orderId));
		changeState(orderId, GovermentOrder.STATE_FINANCING);
		order.setState(GovermentOrder.STATE_FINANCING);
		insertGovermentOrderToFinancing(order);
	}
	@Override
	@Transactional
	public void startRepaying(Integer orderId) throws IllegalConvertException,IllegalOperationException {
		GovermentOrder order=null;
		try
		{
			order=applyFinancingOrder(orderId);
			if(order!=null)
			{
				List<Product> products=order.getProducts();
				if(products!=null&&products.size()>0)
					throw new IllegalOperationException("还有竞标中的产品，请先修改产品状态");
			}
			changeState(orderId, GovermentOrder.STATE_REPAYING);
			order=financingOrders.remove(orderId.toString());
			order.setState(GovermentOrder.STATE_REPAYING);
		}finally
		{
			releaseFinancingOrder(order);
		}
	}
	@Override
	public void quitFinancing(Integer orderId) throws IllegalConvertException, IllegalOperationException {
		GovermentOrder order=null;
		try
		{
			order=applyFinancingOrder(orderId);
			if(order!=null)
			{
				List<Product> products=order.getProducts();
				if(products!=null&&products.size()>0)
					throw new IllegalOperationException("还有竞标中的产品，请先修改产品状态");
			}
			changeState(orderId, GovermentOrder.STATE_QUITFINANCING);
			order=financingOrders.remove(orderId.toString());
			order.setState(GovermentOrder.STATE_QUITFINANCING);
		}finally
		{
			releaseFinancingOrder(order);
		}
	}
	@Override
	public void closeFinancing(Integer orderId) throws IllegalConvertException {
		changeState(orderId, GovermentOrder.STATE_CLOSE);		
	}
	@Override
	public Product applyFinancingProduct(Integer productId, Integer orderId) {
		GovermentOrder order=financingOrders.get(orderId.toString());
		if(order==null)
			return null;
		order.lock.lock();
		if(order.getState()!=GovermentOrder.STATE_FINANCING)
		{
			order.lock.unlock();
			return null;
		}
		List<Product> products=order.getProducts();
		if(products==null||products.size()==0)
		{
			order.lock.unlock();
			return null;
		}
		for(Product product:products)
		{
			//Integer对象的==操作需注意 ,如:10==new Integer(10)为true,new Integer(10)==new Integer(10)为false
			if((int)productId==(int)(product.getId()))
			{
				if(product.getState()==Product.STATE_FINANCING)
					return product;
				else
				{
					order.lock.unlock();
					return null;
				}
			}
		}
		order.lock.unlock();
		return null;
	}
	@Override
	public void releaseFinancingProduct(Product product) {
		if(product==null)
			return;
		GovermentOrder order=financingOrders.get(product.getGovermentorderId().toString());
		order.lock.unlock();
		
	}
	public static void main(String[] args)
	{
		Integer a=new Integer(5);
		Integer b=new Integer(5);
		System.out.println(a==(int)b);
		a=new Integer(1000);
		b=new Integer(1000);
		System.out.println(a==(int)b);
	}
	@Override
	public GovermentOrder applyFinancingOrder(Integer orderId) {
		GovermentOrder order=financingOrders.get(orderId.toString());
		if(order==null)
			return null;
		order.lock.lock();
		return order;
	}
	@Override
	public void releaseFinancingOrder(GovermentOrder order) {
		if(order!=null)
			order.lock.unlock();
	}
	private void insertGovermentOrderToFinancing(GovermentOrder order)
	{
		order.lock=new ReentrantLock(true);
		order.setMaterial(null);
		financingOrders.put(order.getId().toString(), order);
		
		List<Product> products=productDao.findByGovermentOrder(order.getId());
		if(products==null||products.size()==0)
			return;
		for(Product product:products)
		{
			if(product.getState()==Product.STATE_FINANCING)
			{
				product.setAccessory(null);
				order.getProducts().add(product);
			}
		}
	}
	@Override
	public Map<String, Object> findGovermentOrderByProductSeries(
			Integer productSeriesId,int states, int offset, int recnum) {
		List<Integer> list=null;
		if(states!=-1)
		{
			list=new ArrayList<Integer>();
			for(int orderState:orderStates)
			{
				if((orderState&states)>0)
					list.add(orderState);
			}
			if(list.isEmpty())
				return Pagination.buildResult(null, 0, offset, recnum);
		}
		int count=productDao.countByProductSeriesAndState(productSeriesId, null);
		if(count==0)
			return Pagination.buildResult(null, 0, offset, recnum);
		List<Product> products=productDao.findByProductSeriesAndState(productSeriesId, list, offset, recnum);
		List<GovermentOrder> orders=new ArrayList<GovermentOrder>();
		for(Product product:products)
		{
			GovermentOrder order=govermentOrderDao.find(product.getGovermentorderId());
			order.getProducts().add(product);
			orders.add(order);
		}
		return Pagination.buildResult(orders, count, offset, recnum);
	}
	@Override
	public Map<String, Object> findByStatesByPage(int states, int offset,
			int recnum) {
		List<Integer> list=null;
		if(states!=-1)
		{
			list=new ArrayList<Integer>();
			for(int orderState:orderStates)
			{
				if((orderState&states)>0)
					list.add(orderState);
			}
			if(list.isEmpty())
				return Pagination.buildResult(null, 0, offset, recnum);
		}
		int count=govermentOrderDao.countByState(list);
		List<GovermentOrder> orders=govermentOrderDao.findByStatesWithPaging(list, offset, recnum);
		if(orders!=null&&orders.size()>0)
		{
			for(GovermentOrder order:orders)
			{
				order.setProducts(productDao.findByGovermentOrder(order.getId()));
			}
		}
		return Pagination.buildResult(orders, count, offset, recnum);
	}
}
