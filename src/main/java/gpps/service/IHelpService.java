package gpps.service;

import gpps.model.Help;

import java.util.Map;

public interface IHelpService {
	public void create(Help help);
	public Help find(Integer id);
	/**
	 * 翻页查找信息展示
	 * @param publicType -1 不限 0： 新手问题；	1： 常见问题
	 * @param offset
	 * @param recnum
	 * @return
	 */
	public Map<String,Object> findPublicHelps(int publicType,int offset,int recnum);
//	/**翻页获取Private Help
//	 * questionerType:-1为不限,0:Lender,1:borrower
//	 * questionerId:当questionerType不为-1时判断，null为不限
//	 */
	/**
	 * 翻页查找我的帮助
	 * @param offset
	 * @param recnum
	 * @return
	 */
	public Map<String,Object> findMyHelps(int offset,int recnum);
}
