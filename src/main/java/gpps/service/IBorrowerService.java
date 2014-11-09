package gpps.service;

import gpps.model.Borrower;
import gpps.model.FinancingRequest;
import gpps.model.ref.Accessory.MimeItem;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.easyservice.xml.XMLParseException;

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
	public List<Borrower> findFinancingBorrower(int privilege);
	
	/**
	 * 查找申请融资的借款方
	 * 
	 * @return
	 * */
	public List<Borrower> findRequestBorrower();

//	public void addAccessory(Integer borrowerId, String path);

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
	
	public void addAccessory(Integer borrowerId,int category,MimeItem item) throws XMLParseException;
	public void delAccessory(Integer borrowerId,int category,String itemId) throws XMLParseException;
	public List<MimeItem> findMimeItems(Integer borrowerId,int category)throws XMLParseException;
	
	public void applyFinancing(FinancingRequest financingRequest);
	public void passFinancingRequest(Integer financingRequestId) throws IllegalOperationException;
	public void refuseFinancingRequest(Integer financingRequestId);
	
	public FinancingRequest findFinancingRequest(Integer id);
	/**
	 * 找到borrower的所有申请
	 * @param state -1为不限
	 * @return
	 */
	public List<FinancingRequest> findMyFinancingRequests(int state);
	/**
	 * 找到所有申请
	 * @param state -1为不限
	 * @param offset
	 * @param recnum
	 * @return
	 */
	public Map<String, Object> findAllFinancingRequests(int state,int offset, int recnum);
	
	public void registerThirdPartyAccount(Integer id,String thirdPartyAccount);
	public boolean isThirdPartyAuthentication();
	/**
	 * 查找借款人
	 * @param privilege -1表示不限
	 * @param offset
	 * @param recnum
	 * @return
	 */
	public Map<String, Object> findByPrivilegeWithPaging(int privilege,int offset,int recnum);
	public void update(Integer id,String corporationName,String corporationAddr,String brange);
}
