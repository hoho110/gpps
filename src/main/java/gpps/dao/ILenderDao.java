package gpps.dao;

import org.apache.ibatis.annotations.Param;

import gpps.model.Lender;

public interface ILenderDao {
	public int countAll();
	public Lender find(Integer id);
	public Lender findByLoginId(String loginId);
	public Lender findByLoginIdAndPassword(@Param("loginId") String loginId,@Param("password") String password);
	public Lender findByTel(String tel);
	public void create(Lender lender);
//	public void update(Lender lender);
	public void changePrivilege(@Param("id") Integer id,@Param("privilege") int privilege);
	public void changePassword(@Param("id") Integer id,@Param("password") String password);
}
