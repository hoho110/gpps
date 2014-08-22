package gpps.dao;

import gpps.model.Lender;

public interface ILenderDao {
	public int countAll();
	public Lender find(int id);
	public Lender findByLoginId(String loginId);
	public Lender findByLoginIdAndPassword(String loginId,String password);
	public Lender findByTel(String tel);
	public void create(Lender lender);
	public void update(Lender lender);
	public void changePrivilege(int id,int privilege);
	public void changePassword(int id,String password);
}
