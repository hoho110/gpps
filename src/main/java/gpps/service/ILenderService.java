package gpps.service;

import gpps.model.Lender;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;

public interface ILenderService extends ILoginService{
	/**
	 * 注册用户(贷款方),privilege默认为0
	 * 创建用户时默认为该用户创建一个初始化账户并与之关联
	 * @param lender 贷款方
	 * @param messageValidateCode 短信验证码
	 * @return 贷款方，增加ID
	 * @throws LoginException 
	 * @throws Exception
	 */
	public Lender register(Lender lender,String messageValidateCode) throws ValidateCodeException,IllegalArgumentException, LoginException;
	/**
	 * 更新用户
	 * 待讨论哪些字段能够更新
	 * @param lender
	 * @return
	 * @throws Exception
	 */
//	public Lender update(Lender lender);
	/**
	 * 修改用户角色级别,修改范围为Lender定义的privilege常量
	 * 该方法只有admin有调用权限
	 * @param privilege
	 * @throws Exception
	 */
	public void changePrivilege(int id,int privilege) throws IllegalArgumentException;
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Lender find(int id);
	/**
	 * 返回所有的用户角色
	 * 供产品购买级别那里选择
	 * @return
	 */
	public int[] findAllPrivilege();
	/**
	 * 返回Session用户对象
	 * @return
	 */
	public Lender getCurrentUser();
}
