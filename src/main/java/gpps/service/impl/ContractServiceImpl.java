package gpps.service.impl;

import gpps.dao.IGovermentOrderDao;
import gpps.dao.ILenderDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.dao.ISubmitDao;
import gpps.model.Product;
import gpps.model.Submit;
import gpps.service.ContractItem;
import gpps.service.IContractService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
public class ContractServiceImpl implements IContractService {
	private Resource contractDir; //合同保存目录
	
	
	@Autowired
	ISubmitDao submitDao;
	@Autowired
	IGovermentOrderDao orderDao;
	@Autowired
	IProductDao productDao;
	@Autowired
	IProductSeriesDao seriesDao;
	@Autowired
	ILenderDao lenderDao;
	
	@Override
	public String getProductName(Integer pid){
		Product product = productDao.find(pid);
		String orderName = orderDao.find(product.getGovermentorderId()).getTitle();
		String seriesName = seriesDao.find(product.getProductseriesId()).getTitle();
		return orderName+"["+seriesName+"]";
	}
	
	@Override
	public List<ContractItem> list(Integer pid) throws Exception{
		List<Submit> submits = submitDao.findAllByProductAndState(pid, Submit.STATE_COMPLETEPAY);
		List<ContractItem> items = new ArrayList<ContractItem>();
		Product product = productDao.find(pid);
		String orderName = orderDao.find(product.getGovermentorderId()).getTitle();
		String seriesName = seriesDao.find(product.getProductseriesId()).getTitle();
		for(Submit submit:submits){
			ContractItem item = new ContractItem();
			item.setSubmitID(submit.getId());
			item.setLenderName(lenderDao.find(submit.getLenderId()).getName());
			item.setOrderName(orderName);
			item.setSeriesName(seriesName);
			item.setAmount(submit.getAmount());
			item.setExist(false);
			File ff = new File(contractDir.getFile().getAbsolutePath()+File.separator+pid+File.separator+pid+item.getSubmitID()+".pdf");
			if(ff.exists()){
				item.setExist(true);
			}
			
			items.add(item);
		}
		return items;
	}
	
	public Resource getContractDir() {
		return contractDir;
	}
	public void setContractDir(Resource contractDir) {
		this.contractDir = contractDir;
	}

}
