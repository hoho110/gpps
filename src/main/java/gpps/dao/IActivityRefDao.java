package gpps.dao;

import java.util.List;

import gpps.model.ActivityRef;

public interface IActivityRefDao {
	public void create(ActivityRef activityRef);
	public ActivityRef find(Integer id);
	public List<ActivityRef> findByActivity(Integer activityId);
}
