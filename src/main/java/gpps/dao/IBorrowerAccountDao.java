package gpps.dao;

import gpps.model.BorrowerAccount;

import java.math.BigDecimal;

public interface IBorrowerAccountDao {
	public void create(BorrowerAccount borrowerAccount);
	public BorrowerAccount find(int accountId);
	/**
	 * 充值
	 * @param accountId
	 * @param amount
	 */
	public void recharge(int accountId,BigDecimal amount);
	/**
	 * 冻结
	 * @param accountId
	 * @param amount
	 */
	public void freeze(int accountId,BigDecimal amount);
	/**
	 * 支付给借款人
	 * @param accountId 账户ID
	 * @param amount 支付金额
	 */
	public void pay(int accountId,BigDecimal amount);
	/**
	 * 借款人还款
	 * @param accountId 账户ID
	 * @param amount 还款金额
	 */
	public void repay(int accountId,BigDecimal amount);
	/**
	 * 取现
	 * @param accountId
	 * @param amount
	 */
	public void cash(int accountId,BigDecimal amount);
}
