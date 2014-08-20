package gpps.dao;

import gpps.model.Lender;

public interface ILenderDao {
	public int countAll();
	public Lender find(int id);
	public Lender findByLoginId(String loginId);
	public Lender findByLoginIdAndPassword(String loginId,String password);
	public Lender create(Lender lender);
	public Lender update(Lender lender);
	public void changePrivilege(int id,int privilege);
	public void changePassword(int id,String password);
}
