package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductActionDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.dao.IStateLogDao;
import gpps.dao.ISubmitDao;
import gpps.model.Borrower;
import gpps.model.GovermentOrder;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.ProductAction;
import gpps.model.ProductSeries;
import gpps.model.StateLog;
import gpps.model.Submit;
import gpps.model.Task;
import gpps.model.ref.Accessory;
import gpps.model.ref.Accessory.MimeCol;
import gpps.model.ref.Accessory.MimeItem;
import gpps.service.IGovermentOrderService;
import gpps.service.IPayBackService;
import gpps.service.IProductService;
import gpps.service.ITaskService;
import gpps.service.exception.ExistWaitforPaySubmitException;
import gpps.service.exception.IllegalConvertException;
import gpps.tools.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyservice.xml.EasyObjectXMLTransformerImpl;
import com.easyservice.xml.IEasyObjectXMLTransformer;
import com.easyservice.xml.XMLParseException;
@Service
public class ProductServiceImpl implements IProductService {
	@Autowired
	IGovermentOrderDao govermentOrderDao;
	@Autowired
	IProductSeriesDao productSeriesDao;
	@Autowired
	IGovermentOrderService orderService;
	@Autowired
	IProductDao productDao;
	@Autowired
	ITaskService taskService;
	@Autowired
	IProductActionDao productActionDao;
	@Autowired
	IBorrowerDao borrowerDao;
	@Autowired
	IPayBackDao payBackDao;
	@Autowired
	IPayBackService payBackService;
	@Autowired
	IStateLogDao stateLogDao;
	@Autowired
	ISubmitDao submitDao;
	private static final IEasyObjectXMLTransformer xmlTransformer=new EasyObjectXMLTransformerImpl(); 
	Logger logger=Logger.getLogger(this.getClass());
	@Override
	@Transactional
	public void create(Product product) {
		checkNullObject("orderId", product.getGovermentorderId());
		GovermentOrder order=govermentOrderDao.find(product.getGovermentorderId());
		checkNullObject(GovermentOrder.class, order);
		if(order.getState()!=GovermentOrder.STATE_UNPUBLISH)
			throw new IllegalArgumentException("只能为未发布的订单添加产品");
		product.setState(Product.STATE_UNPUBLISH);
		product.setCreatetime(System.currentTimeMillis());
		checkNullObject("productseriesId", product.getProductseriesId());
		ProductSeries productSeries=productSeriesDao.find(product.getProductseriesId());
		checkNullObject(ProductSeries.class,productSeries);
		checkNullObject("expectAmount", product.getExpectAmount());
		checkNullObject("rate", product.getRate());
		product.setRealAmount(BigDecimal.ZERO);
		
		Calendar starttime=Calendar.getInstance();
		starttime.setTimeInMillis(order.getIncomeStarttime());
		
		Calendar endtime=Calendar.getInstance();
		endtime.setTimeInMillis(product.getIncomeEndtime());
		endtime.set(endtime.get(Calendar.YEAR), endtime.get(Calendar.MONTH), endtime.get(Calendar.DATE), 0, 0, 0);
		product.setIncomeEndtime(endtime.getTimeInMillis());
		
		productDao.create(product);
		Borrower borrower=borrowerDao.find(order.getBorrowerId());
		// 创建还款计划
		List<PayBack> payBacks=payBackService.generatePayBacks(PayBack.BASELINE.intValue(), product.getRate().doubleValue(),productSeries.getType(), starttime.getTimeInMillis(), endtime.getTimeInMillis());
		if(payBacks==null||payBacks.size()==0)
			throw new RuntimeException("未生成还款计划");
		for(PayBack payBack:payBacks)
		{
			payBack.setBorrowerAccountId(borrower.getAccountId());
			payBack.setProductId(product.getId());
			payBackDao.create(payBack);
		}
	}
	static int[][] validConverts={
		{Product.STATE_UNPUBLISH,Product.STATE_FINANCING},
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
				productDao.changeState(productId, state,System.currentTimeMillis());
				StateLog stateLog=new StateLog();
				stateLog.setSource(product.getState());
				stateLog.setTarget(state);
				stateLog.setType(StateLog.TYPE_PRODUCT);
				stateLog.setRefid(productId);
				stateLogDao.create(stateLog);
				return;
			}
		}
		throw new IllegalConvertException();
	}

	@Override
	public Product find(Integer productId) {
		checkNullObject("productId", productId);
		Product product=productDao.find(productId);
		product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
		return product;
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
		List<Product> products=productDao.findByState(list, offset, recnum);
		if(products==null||products.size()==0)
			return new ArrayList<Product>(0);
		for(Product product:products)
		{
			product.setGovermentOrder(govermentOrderDao.find(product.getGovermentorderId()));
		}
		return products; 
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
		productAction.setCreatetime(System.currentTimeMillis());
		productActionDao.create(productAction);
	}

	@Override
	public List<ProductAction> findByProductId(Integer productId) {
		return productActionDao.findAllByProduct(productId);
	}

	@Override
	public void addAccessory(Integer productId, String path) {
		throw new RuntimeException("The method is deprecated");
	}

	@Override
	@Transactional
	public void changeBuyLevel(Integer productId, int buyLevel) {
		checkNullObject("productId", productId);
		Product product=productDao.find(productId);
		checkNullObject(Product.class, product);
		try
		{
			product=orderService.applyFinancingProduct(productId, product.getGovermentorderId());
			productDao.changeBuyLevel(productId,buyLevel);
			if(product!=null)
				product.setLevelToBuy(buyLevel);
		}finally{
			orderService.releaseFinancingProduct(product);
		}
	}
	
	@Override
	@Transactional
	public void startRepaying(Integer productId) throws IllegalConvertException,ExistWaitforPaySubmitException {
		// 验证是否有待付款的Submit
		int count=submitDao.countByProductAndStateWithPaged(productId, Submit.STATE_WAITFORPAY);
		if(count>0)
			throw new ExistWaitforPaySubmitException("还有"+count+"个待支付的提交,请等待上述提交全部结束，稍后开始还款");
		//从竞标缓存中移除
		checkNullObject("productId", productId);
		Product product=productDao.find(productId);
		checkNullObject(Product.class, product);
		GovermentOrder order=null;
		try {
			changeState(productId, Product.STATE_REPAYING);
			order=orderService.applyFinancingOrder(product.getGovermentorderId());
			product=order.findProductById(productId);
			order.getProducts().remove(product);
			product.setState(Product.STATE_REPAYING);
			Task task=new Task();
			task.setProductId(productId);
			task.setType(Task.TYPE_PAY);
			taskService.submit(task);
		}finally
		{
			orderService.releaseFinancingOrder(order);
		}
	}

	@Override
	@Transactional
	public void quitFinancing(Integer productId) throws IllegalConvertException, ExistWaitforPaySubmitException {
		// 验证是否有待付款的Submit
		int count=submitDao.countByProductAndStateWithPaged(productId, Submit.STATE_WAITFORPAY);
		if(count>0)
			throw new ExistWaitforPaySubmitException("还有"+count+"个待支付的提交,请等待上述提交全部结束，稍后开始流标");
		//从竞标缓存中移除
		checkNullObject("productId", productId);
		Product product=productDao.find(productId);
		checkNullObject(Product.class, product);
		GovermentOrder order=null;
		try {
			changeState(productId, Product.STATE_QUITFINANCING);
			//TODO 添加退款任务
			order=orderService.applyFinancingOrder(product.getGovermentorderId());
			product=order.findProductById(productId);
			order.getProducts().remove(product);
			product.setState(Product.STATE_QUITFINANCING);
			Task task=new Task();
			task.setProductId(productId);
			task.setType(Task.TYPE_QUITFINANCING);
			taskService.submit(task);
		}finally
		{
			orderService.releaseFinancingOrder(order);
		}
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
	public static void main(String[] args)
	{
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
//		cal.add(Calendar.MONTH, 1);
		System.out.println(cal);
		
	}

	@Override
	public void addAccessory(Integer productId, int category, MimeItem item)
			throws XMLParseException {
		checkChangeProduct(productId);
		String text=productDao.findAccessory(productId);
		Accessory accessory=null;
		if(StringUtil.isEmpty(text))
			accessory=new Accessory();
		else {
			accessory=xmlTransformer.parse(text, Accessory.class);
		}
		if(accessory.getCols()==null)
			accessory.setCols(new ArrayList<Accessory.MimeCol>());
		MimeCol col=accessory.findMimeCol(category);
		if(col==null)
		{
			col=new MimeCol();
			col.setCategory(category);
			accessory.getCols().add(col);
		}
		if(col.getItems()==null)
			col.setItems(new ArrayList<Accessory.MimeItem>());
		col.getItems().add(item);
		text=xmlTransformer.export(accessory);
		productDao.updateAccessory(productId, text);
	}

	@Override
	public void delAccessory(Integer productId, int category, String path)
			throws XMLParseException {
		checkChangeProduct(productId);
		String text=productDao.findAccessory(productId);
		if(StringUtil.isEmpty(text))
			return;
		Accessory accessory=xmlTransformer.parse(text, Accessory.class);
		if(accessory.getCols()==null)
			return;
		MimeCol col=accessory.findMimeCol(category);
		if(col==null)
			return;
		List<MimeItem> items=col.getItems();
		if(items==null||items.size()==0)
			return;
		for(int i=0;i<items.size();i++)
		{
			if(items.get(i).getPath().equals(path))
			{
				items.remove(i);
				break;
			}
		}
		text=xmlTransformer.export(accessory);
		productDao.updateAccessory(productId, text);
	}

	@Override
	public List<MimeItem> findMimeItems(Integer productId, int category)
			throws XMLParseException {
		String text=productDao.findAccessory(productId);
		if(StringUtil.isEmpty(text))
			return new ArrayList<Accessory.MimeItem>(0);
		Accessory accessory=xmlTransformer.parse(text, Accessory.class);
		MimeCol col=accessory.findMimeCol(category);
		if(col==null)
			return new ArrayList<Accessory.MimeItem>(0);
		return col.getItems();
	}
	private void checkChangeProduct(Integer productId)
	{
		Product product=checkNullObject(Product.class, productDao.find(productId));
		if(product.getState()!=Product.STATE_UNPUBLISH)
			throw new RuntimeException("产品已发布，不能再修改");
	}
}
