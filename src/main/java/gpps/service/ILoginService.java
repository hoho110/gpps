package gpps.service;

public interface ILoginService {
	/**
	 * 登录
	 * @param loginId 登录名
	 * @param password 密码
	 * @param validateCode 验证码
	 * @throws Exception
	 */
	public void login(String loginId,String password,String validateCode) throws Exception;
	/**
	 * 为当前用户修改密码
	 * @param source 原密码
	 * @param target 目标密码
	 * @throws Exception
	 */
	public void changePassword(String source,String target) throws Exception;
	/**
	 * 当前用户登出
	 */
	public void loginOut();
}
