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
	
	public void check(List<String> loanNos,int auditType);
	
	public CardBinding getCardBinding();
	
	public Cash getCash(String amount) throws InsufficientBalanceException;
	
}
