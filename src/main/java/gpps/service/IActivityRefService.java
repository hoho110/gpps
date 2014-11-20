package gpps.service;

import gpps.model.ActivityRef;

import java.util.List;

public interface IActivityRefService {
	public void applyActivity(Integer activityId);
	public ActivityRef find(Integer id);
	public List<ActivityRef> findByActivity(Integer activityId);
}
