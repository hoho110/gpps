package gpps.service;

import gpps.model.ActivityRef;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;

import java.util.List;

public interface IActivityRefService {
	public void applyActivity(Integer activityId);
	public ActivityRef find(Integer id);
	public List<ActivityRef> findByActivity(Integer activityId);
	public void applyActivityByAnonymous(String name, String phone,String email, String messageValidateCode,Integer activityId) throws IllegalArgumentException, ValidateCodeException, LoginException;
}
