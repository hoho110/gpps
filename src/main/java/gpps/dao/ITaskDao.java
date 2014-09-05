package gpps.dao;

import gpps.model.Task;

import java.util.List;

public interface ITaskDao {
	public void create(Task task);
	public Task find(Integer id);
	public List<Task> findByState(int state);
	public void changeState(Integer id,int state);
	public void delete(Integer id);
}
