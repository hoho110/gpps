package gpps.service;

import gpps.model.PayBack;

import java.util.List;

public interface IPayBackService {
	public void create(PayBack payback);
	public List<PayBack> findAll(Integer productId);
	public void changeState(Integer paybackId,int state);
	public List<PayBackDetail> getPaybackDetail(Integer paybackId);
}
