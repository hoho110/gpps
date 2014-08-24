package gpps.service;

import gpps.model.Borrower;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;

import java.util.List;

public interface IBorrowerService extends ILoginService {
	/**
	 * 注册用户(借款方),privilege默认为10 创建用户时默认为该用户创建一个初始化账户并与之关联
	 * 
	 * @param borrower
	 *            借款方
	 * @param messageValidateCode
	 *            短信验证码
	 * @return 借款方，增加ID
	 * @throws LoginException
	 * @throws Exception
	 */
	public Borrower register(Borrower borrower, String messageValidateCode) throws ValidateCodeException, IllegalArgumentException, LoginException;

	/**
	 * 更新用户 待讨论哪些字段能够更新
	 * 
	 * @param borrower
	 * @return
	 * @throws Exception
	 */
	// public Borrower update(Borrower borrower);
	/**
	 * 修改用户角色级别,修改范围为borrower定义的privilege常量 该方法只有admin有调用权限
	 * 
	 * @param id
	 * @param privilege
	 * @throws Exception
	 */
//	public void changePrivilege(int id, int privilege) throws IllegalArgumentException;

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Borrower find(int id);

	/**
	 * 查找可融资的借款方
	 * 
	 * @return
	 */
	public List<Borrower> findFinancingBorrower();

	public void addAccessory(Integer borrowerId, String path);

	public boolean isPhoneNumberExist(String phoneNumber);
	/**
	 * 返回Session用户对象
	 * @return
	 */
	public Borrower getCurrentUser();
	/**
	 * 申请拥有融资权限
	 * @throws IllegalConvertException
	 */
	public void applyForFunding() throws IllegalConvertException;
	/**
	 * 通过融资权限申请
	 * @throws IllegalConvertException
	 */
	public void passFundingApplying(Integer borrowerId) throws IllegalConvertException;
	/**
	 * 拒绝融资权限申请
	 * @throws IllegalConvertException
	 */
	public void refuseFundingApplying(Integer borrowerId)throws IllegalConvertException;
}
