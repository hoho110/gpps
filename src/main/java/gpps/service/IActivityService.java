package gpps.service;

import gpps.model.Activity;

import java.util.Map;

public interface IActivityService {
	public void create(Activity activity);
	public Activity find(Integer id);
	public Map<String,Object> findByState(int state,int offset,int recnum);
	public void update(Integer id,String name, long applyendtime,long  starttime,String url,int state);
}
