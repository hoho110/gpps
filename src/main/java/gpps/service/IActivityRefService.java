package gpps.service;

import gpps.model.ActivityRef;
import gpps.service.exception.IllegalOperationException;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;

import java.util.Map;

public interface IActivityRefService {
	public void applyActivity(Integer activityId) throws IllegalOperationException;
	public ActivityRef find(Integer id);
	public Map<String, Object> findByActivity(Integer activityId,int offset,int recnum);
	public void applyActivityByAnonymous(String name, String phone,String email, String messageValidateCode,Integer activityId) throws IllegalArgumentException, ValidateCodeException, LoginException;
	public boolean isApply(Integer activityId,Integer lenderId);
	public Map<String, Object> findByLender(Integer lenderId,int offset,int recnum);
}
