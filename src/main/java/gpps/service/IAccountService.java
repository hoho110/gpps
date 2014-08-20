package gpps.service;

import java.math.BigDecimal;

import gpps.model.LenderAccount;
/**
 * 账户服务
 * 该服务对外不可见，前端不可直接调用
 * @author wmsdu@aliyun.com
 *
 */
public interface IAccountService {
	/**
	 * 贷款人账户充值
	 * @param amount
	 */
	public void rechargeLenderAccount(int accountId,BigDecimal amount);
	/**
	 * 贷款人账户冻结
	 * 
	 * @param amount
	 */
	public void freezeLenderAccount(int accountId,BigDecimal amount);
	/**
	 * 扣款
	 * @param amount
	 */
	public void pay(BigDecimal amount);
	/**
	 * 还款
	 * @param amount
	 */
	public void repay(BigDecimal amount);
	/**
	 * 取现
	 * @param amount
	 */
	public void cash(BigDecimal amount);
	
}
