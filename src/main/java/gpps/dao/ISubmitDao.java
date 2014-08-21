package gpps.dao;

import gpps.model.Submit;

public interface ISubmitDao {
	public Submit findAll(Integer lenderId);
	public Submit find(Integer id);
	public void changeState(Integer submitId,int state);
}
