package gpps.dao;

import gpps.model.Borrower;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	public void changePrivilege(@Param("id") Integer id,@Param("privilege") int privilege);
	public void changePassword(@Param("id") Integer id,@Param("password") String password);
	public void changeCreditValue(@Param("id")Integer id,@Param("creditValue")int creditValue);
	public void delete(Integer id);
	public void addCreditValue(@Param("id")Integer id,@Param("creditValue")int creditValue);
	
	
	public String findAccessory(Integer borrowerId);
	public void updateAccessory(@Param("borrowerId")Integer borrowerId,@Param("material")String material);
}
