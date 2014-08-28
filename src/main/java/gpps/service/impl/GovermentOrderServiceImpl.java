/**
 * 
 */
package gpps.service.impl;

import gpps.dao.IBorrowerDao;
import gpps.dao.IGovermentOrderDao;
import gpps.model.Borrower;
import gpps.model.GovermentOrder;
import gpps.service.IGovermentOrderService;
import gpps.service.exception.IllegalConvertException;

import java.util.ArrayList;
import java.util.List;

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
		return govermentOrderDao.findByState(list, offset, recnum);
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
}
