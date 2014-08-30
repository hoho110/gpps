package gpps.service;

import gpps.model.CashStream;
import gpps.model.LenderAccount;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.List;
/**
 * 账户服务
 * 该服务对外不可见，前端不可直接调用
 * 账户操作涉及到实际调用第三方平台进行操作时，需要一个预操作记录
 * 该服务中各对账户的操作只是在CashStream中记录预处理状态，在changeCashStreamState中当state=1在实际进行账户的修改
 * @author wmsdu@aliyun.com
 *
 */
public interface IAccountService {
	/**
	 * 贷款人账户充值
	 * @param amount
	 */
	public Integer rechargeLenderAccount(Integer lenderAccountId,BigDecimal amount,String description);
	/**
	 * 借款人账户充值
	 * @param amount
	 */
	public Integer rechargeBorrowerAccount(Integer borrowerAccountId,BigDecimal amount,String description);
	/**
	 * 贷款人账户冻结
	 * @param amount
	 * @exception InsufficientBalanceException 余额不足
	 */
	public Integer freezeLenderAccount(Integer lenderAccountId,BigDecimal amount,Integer submitid,String description) throws InsufficientBalanceException;
	/**
	 * 贷款人账户解冻
	 * @param lenderAccountId
	 * @param amount
	 * @param submitid
	 * @param description
	 * @return
	 */
	public Integer unfreezeLenderAccount(Integer lenderAccountId,BigDecimal amount,Integer submitid,String description);
	/**
	 * 借款人账户冻结
	 * @param amount
	 */
	public Integer freezeBorrowerAccount(Integer borrowerAccountId,BigDecimal amount,Integer paybackId,String description) throws InsufficientBalanceException;
	/**
	 * 贷款人将冻结资金支付给借款人
	 * @param amount
	 */
	public Integer pay(Integer lenderAccountId,Integer borrowerAccountId, BigDecimal chiefamount, BigDecimal interest,Integer submitid,String description);
	/**
	 * 还款
	 * @param amount
	 */
	public Integer repay(Integer lenderAccountId,Integer borrowerAccountId,BigDecimal chiefamount,BigDecimal interest,Integer submitid,Integer paybackId,String description);
	/**
	 * 取现
	 * @param amount
	 * @exception InsufficientBalanceException 余额不足
	 */
	public Integer cashLenderAccount(Integer lenderAccountId,BigDecimal amount,String description) throws InsufficientBalanceException;
	/**
	 * 取现
	 * @param amount
	 * @exception InsufficientBalanceException 余额不足
	 */
	public Integer cashBorrowerAccount(Integer borrowerAccountId,BigDecimal amount,String description) throws InsufficientBalanceException;
	/**
	 * 修改资金流的状态
	 * 在该方法中实际对账户(借款人/贷款人)进行修改
	 * @param cashStreamId 资金流ID
	 * @param state
	 * @throws IllegalStateException
	 */
	public void changeCashStreamState(Integer cashStreamId,int state) throws IllegalConvertException;
	
	/**
	 * 调用第三方平台验证
	 * @param cashStreamId
	 */
	public void checkThroughThirdPlatform(Integer cashStreamId);
	/**
	 * 找到未处理的现金流
	 * @return
	 */
	public List<CashStream> findAllDirtyCashStream();
}
