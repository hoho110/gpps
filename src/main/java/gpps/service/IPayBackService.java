package gpps.service;

import gpps.model.Payback;

import java.util.List;

public interface IPayBackService {
	public void create(Payback payback);
	public List<Payback> findAll(Integer productId);
	public void changeState(Integer paybackId,int state);
	public List<PayBackDetail> getPaybackDetail(Integer paybackId);
}
