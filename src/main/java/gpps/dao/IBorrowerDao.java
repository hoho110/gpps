package gpps.dao;

import java.util.List;

import gpps.model.Borrower;

public interface IBorrowerDao {
	public int countAll();
	public Borrower find(int id);
	public Borrower findByLoginId(String loginId);
	public Borrower findByLoginIdAndPassword(String loginId,String password);
	public void create(Borrower borrower);
	public void update(Borrower borrower);
	public void changePrivilege(int id,int privilege);
	public void changePassword(int id,String password);
	public List<Borrower> findByPrivilege(int privilege);
	public Borrower findByTel(String tel);
}
