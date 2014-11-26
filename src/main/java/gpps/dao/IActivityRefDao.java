package gpps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gpps.model.ActivityRef;

public interface IActivityRefDao {
	public void create(ActivityRef activityRef);
	public ActivityRef find(Integer id);
	public List<ActivityRef> findByActivity(@Param("activityId")Integer activityId,@Param("offset")int offset,@Param("recnum")int recnum);
	public int countByActivity(Integer activityId);
}
