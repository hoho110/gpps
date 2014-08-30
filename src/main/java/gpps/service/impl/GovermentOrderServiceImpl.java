/**
 * 
 */
package gpps.service.impl;

import gpps.dao.IBorrowerDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.IProductDao;
import gpps.model.Borrower;
import gpps.model.GovermentOrder;
import gpps.model.Product;
import gpps.service.IGovermentOrderService;
import gpps.service.exception.IllegalConvertException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	static int[] orderStates={
		GovermentOrder.STATE_APPLY,
		GovermentOrder.STATE_MODIFY,
		GovermentOrder.STATE_REAPPLY,
		GovermentOrder.STATE_REFUSE,
		GovermentOrder.STATE_PASS,
		GovermentOrder.STATE_FINANCING,
		GovermentOrder.STATE_QUITFINANCING,
		GovermentOrder.STATE_REPAYING,
		GovermentOrder.STATE_CLOSE
	};
	//TODO Order状态变化，融资金额变化需要修改下面的缓存
	Map<String,GovermentOrder> financingOrders=new HashMap<String,GovermentOrder>();  
	@PostConstruct
	public void init()
	{
		//TODO 启动时将所有正在竞标状态的订单加载到内存中，并分配锁
		List<Integer> states=new ArrayList<Integer>();
		states.add(GovermentOrder.STATE_FINANCING);
		List<GovermentOrder> govermentOrders=govermentOrderDao.findByStates(states);
		if(govermentOrders==null||govermentOrders.size()==0)
			return;
		for(GovermentOrder order:govermentOrders)
		{
			order.lock=new ReentrantLock();
			order.setMaterial(null);
			financingOrders.put(order.toString(), order);
			
			List<Product> products=productDao.findByGovermentOrder(order.getId());
			if(products==null||products.size()==0)
				continue;
			for(Product product:products)
			{
				if(product.getState()==Product.STATE_FINANCING)
				{
					product.setAccessory(null);
					order.getProducts().add(product);
				}
			}
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
		{GovermentOrder.STATE_APPLY,GovermentOrder.STATE_MODIFY},
		{GovermentOrder.STATE_APPLY,GovermentOrder.STATE_REFUSE},
		{GovermentOrder.STATE_APPLY,GovermentOrder.STATE_PASS},
		{GovermentOrder.STATE_MODIFY,GovermentOrder.STATE_REAPPLY},
		{GovermentOrder.STATE_REAPPLY,GovermentOrder.STATE_MODIFY},
		{GovermentOrder.STATE_REAPPLY,GovermentOrder.STATE_REFUSE},
		{GovermentOrder.STATE_REAPPLY,GovermentOrder.STATE_PASS},
		{GovermentOrder.STATE_PASS,GovermentOrder.STATE_FINANCING},
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
	@Override
	public void passApplying(Integer orderId) throws IllegalConvertException {
		changeState(orderId, GovermentOrder.STATE_PASS);
	}

	@Override
	public void refuseApplying(Integer orderId) throws IllegalConvertException {
		changeState(orderId, GovermentOrder.STATE_REFUSE);
	}

	@Override
	public void reviseApplying(Integer orderId) throws IllegalConvertException {
		changeState(orderId, GovermentOrder.STATE_MODIFY);	
	}

	@Override
	public void reApply(Integer orderId) throws IllegalConvertException {
		changeState(orderId, GovermentOrder.STATE_REAPPLY);
	}
	@Override
	public void startFinancing(Integer orderId) throws IllegalConvertException {
		changeState(orderId, GovermentOrder.STATE_FINANCING);
	}
	@Override
	public void startRepaying(Integer orderId) throws IllegalConvertException {
		changeState(orderId, GovermentOrder.STATE_REPAYING);
	}
	@Override
	public void quitFinancing(Integer orderId) throws IllegalConvertException {
		changeState(orderId, GovermentOrder.STATE_QUITFINANCING);
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
}
