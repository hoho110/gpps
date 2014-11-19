package gpps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gpps.model.Activity;

public interface IActivityDao {
	public void create(Activity activity);
	public Activity find(Integer id);
	public int countByState(int state);
	public List<Activity> findByState(@Param("state")int state,@Param("offset")int offset,@Param("recnum")int recnum);
}