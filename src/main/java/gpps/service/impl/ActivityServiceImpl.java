package gpps.service.impl;

import gpps.constant.Pagination;
import gpps.dao.IActivityDao;
import gpps.model.Activity;
import gpps.service.IActivityService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ActivityServiceImpl implements IActivityService{
	@Autowired
	IActivityDao activityDao;
	@Override
	public void create(Activity activity) {
		activity.setCreatetime(System.currentTimeMillis());
		activityDao.create(activity);
	}

	@Override
	public Activity find(Integer id) {
		return activityDao.find(id);
	}

	@Override
	public Map<String, Object> findByState(int state, int offset, int recnum) {
		int count=activityDao.countByState(state);
		if(count==0)
			return Pagination.buildResult(null, count, offset, recnum);
		List<Activity> activities=activityDao.findByState(state, offset, recnum);
		return Pagination.buildResult(activities, count, offset, recnum);
	}

	@Override
	public void update(Integer id, String name, long applyendtime,
			long starttime, String url, int state) {
		activityDao.update(id, name, applyendtime, starttime, url, state);
	}

}
