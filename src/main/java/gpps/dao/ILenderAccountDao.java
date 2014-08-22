package gpps.dao;

import java.math.BigDecimal;

import gpps.model.LenderAccount;

public interface ILenderAccountDao {
	public void create(LenderAccount lenderAccount);
	public LenderAccount find(int accountId);
	/**
	 * 充值
	 * total=total+amount
	 * usable=usable+amount
	 * @param accountId
	 * @param amount
	 */
	public void recharge(int accountId,BigDecimal amount);
	/**
	 * 冻结
	 * usable=usable-amount
	 * freeze=freeze+amount
	 * @param accountId
	 * @param amount
	 */
	public void freeze(int accountId,BigDecimal amount);//冻结
	/**
	 * 支付给借款人
	 * @param accountId 账户ID
	 * @param principalAmount 扣除本金金额
	 * @param expectedIncomeAmount 预期收益金额
	 */
	public void pay(int accountId,BigDecimal principalAmount,BigDecimal expectedIncomeAmount);
	/**
	 * 借款人还款
	 * @param accountId 账户ID
	 * @param principalAmount 还款本金金额
	 * @param incomeAmount 还款收益金额
	 */
	public void repay(int accountId,BigDecimal principalAmount,BigDecimal incomeAmount);
	/**
	 * 取现
	 * @param accountId
	 * @param amount
	 */
	public void cash(int accountId,BigDecimal amount);
}
