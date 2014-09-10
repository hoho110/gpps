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
	public void changeLevel(@Param("id") Integer id,@Param("level") int level);
	public void changePassword(@Param("id") Integer id,@Param("password") String password);
	public void changeGrade(@Param("id")Integer id,@Param("grade")int grade);
	public void delete(Integer id);
	public void registerSecondStep(@Param("id")Integer id,@Param("name")String name,@Param("identityCard")String identityCard,@Param("sex")int sex,@Param("address")String address);
}
