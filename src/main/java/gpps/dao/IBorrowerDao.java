package gpps.dao;

import gpps.model.Borrower;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized.Parameters;

public interface IBorrowerDao {
	public int countAll();
	public Borrower find(Integer id);
	public Borrower findByLoginId(String loginId);
	public Borrower findByLoginIdAndPassword(@Param("loginId") String loginId,@Param("password") String password);
	public List<Borrower> findByPrivilege(int privilege);
	public Borrower findByTel(String tel);
	public Borrower findByIdentityCard(String identityCard);
	public List<Borrower> findByState(int state);
	public void create(Borrower borrower);
//	public void update(Borrower borrower);
	public void changePrivilege(@Param("id") Integer id,@Param("privilege") int privilege,@Param("lastModifyTime")long lastModifyTime);
	public void changePassword(@Param("id") Integer id,@Param("password") String password);
//	public void changeCreditValue(@Param("id")Integer id,@Param("creditValue")int creditValue);
	public void delete(Integer id);
//	public void addCreditValue(@Param("id")Integer id,@Param("creditValue")int creditValue);
	public void changeCreditValueAndLevel(@Param("id")Integer id,@Param("creditValue")int creditValue,@Param("level")int level);
	
	public String findAccessory(Integer borrowerId);
	public void updateAccessory(@Param("borrowerId")Integer borrowerId,@Param("material")String material);
	
	public void registerThirdPartyAccount(@Param("id")Integer id,@Param("thirdPartyAccount")String thirdPartyAccount);
	/**
	 * @param state -1 不限
	 * @return
	 */
	public int countByPrivilege(@Param("privilege")int privilege);
	/**
	 * @param state -1 不限
	 * @return
	 */
	public List<Borrower> findByPrivilegeWithPaging(@Param("privilege")int privilege,@Param("offset")int offset,@Param("recnum")int recnum);
}
