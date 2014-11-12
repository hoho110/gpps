package gpps.service.thirdpay;

import gpps.service.exception.InsufficientBalanceException;

import java.util.List;

public interface IThirdPaySupportService {
	/**
	 * 根据行为获取url
	 * @param action
	 * @return
	 */
	public String getBaseUrl(String action);
	
	public RegistAccount getRegistAccount();
	
	public Recharge getRecharge(String amount);
	
	public Transfer getTransferToBuy(Integer submitId,String pid) throws InsufficientBalanceException;
	
	public String getPrivateKey();
	/**
	 * 后台审核服务
	 * @param loanNos 所有流水号用英文逗号(,)连成一个字符串
	 * @param auditType 1.通过
						2.退回
						3.二次分配同意
						4.二次分配不同意
						5.提现通过
						6.提现退回
	 */
	public void check(List<String> loanNos,int auditType);
	
	public CardBinding getCardBinding();
	
	public Cash getCash(String amount) throws InsufficientBalanceException;
	
}
