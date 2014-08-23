package gpps.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import gpps.model.LenderAccount;

public interface ILenderAccountDao {
	public void create(LenderAccount lenderAccount);
	public LenderAccount find(Integer accountId);
	/**
	 * 充值
	 * total=total+amount
	 * usable=usable+amount
	 * @param accountId
	 * @param amount
	 */
	public void recharge(@Param("accountId") Integer accountId,@Param("amount") BigDecimal amount);
	/**
	 * 冻结
	 * usable=usable-amount
	 * freeze=freeze+amount
	 * @param accountId
	 * @param amount
	 */
	public void freeze(@Param("accountId") Integer accountId,@Param("amount") BigDecimal amount);//冻结
	/**
	 * 支付给借款人
	 * freeze=freeze-principalAmount
	 * used=used+principalAmount
	 * expectedincome=expectedincome+expectedIncomeAmount
	 * @param accountId 账户ID
	 * @param principalAmount 扣除本金金额
	 * @param expectedIncomeAmount 预期收益金额
	 */
	public void pay(@Param("accountId") Integer accountId,@Param("principalAmount") BigDecimal principalAmount,@Param("expectedIncomeAmount") BigDecimal expectedIncomeAmount);
	/**
	 * 借款人还款
	 * total=total+incomeAmount
	 * totalincome=totalincome+incomeAmount
	 * expectedincome=expectedincome-expectedIncomeAmount
	 * used=used-principalAmount
	 * usable=usable+principalAmount+incomeAmount
	 * @param accountId 账户ID
	 * @param principalAmount 还款本金金额
	 * @param incomeAmount 还款收益金额
	 */
	public void repay(@Param("accountId") Integer accountId,@Param("principalAmount") BigDecimal principalAmount,@Param("expectedIncomeAmount") BigDecimal expectedIncomeAmount,@Param("incomeAmount") BigDecimal incomeAmount);
	/**
	 * 取现
	 * total=total-amount
	 * usable=usable-amount
	 * @param accountId
	 * @param amount
	 */
	public void cash(@Param("accountId") Integer accountId,@Param("amount") BigDecimal amount);
}
