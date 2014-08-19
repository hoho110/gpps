package gpps.service;

import gpps.model.Lender;

public interface ILenderService extends ILoginService{
	/**
	 * 注册用户(贷款方),privilege默认为0
	 * 创建用户时默认为该用户创建一个初始化账户并与之关联
	 * @param lender 贷款方
	 * @return 贷款方，增加ID
	 * @throws Exception
	 */
	public Lender register(Lender lender) throws Exception;
	/**
	 * 更新用户
	 * @param lender
	 * @return
	 * @throws Exception
	 */
	public Lender update(Lender lender) throws Exception;
	/**
	 * 修改用户角色级别,修改范围为Lender定义的privilege常量
	 * 该方法只有admin有调用权限
	 * @param privilege
	 * @throws Exception
	 */
	public void changePrivilege(int privilege) throws Exception;
}
