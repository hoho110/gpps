package gpps.service.impl;

import gpps.dao.IActivityRefDao;
import gpps.model.ActivityRef;
import gpps.model.Lender;
import gpps.service.IActivityRefService;
import gpps.service.ILenderService;
import gpps.service.ILoginService;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;
import gpps.tools.StringUtil;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Service
public class ActivityRefServiceImpl implements IActivityRefService{
	@Autowired
	IActivityRefDao activityRefDao;
	@Autowired
	ILenderService lenderService;
	@Override
	public ActivityRef find(Integer id) {
		return activityRefDao.find(id);
	}

	@Override
	public List<ActivityRef> findByActivity(Integer activityId) {
		return activityRefDao.findByActivity(activityId);
	}

	@Override
	public void applyActivity(Integer activityId) {
		HttpSession session=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Object user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		ActivityRef ref=new ActivityRef();
		ref.setActivityId(activityId);
		ref.setParticipate(ActivityRef.PARTICIPATE_YES);
		ref.setApplyTime(System.currentTimeMillis());
		if(user instanceof Lender)
		{
			ref.setParticipatorType(ActivityRef.PARTICIPATORTYPE_LENDER);
			ref.setParticipatorId(((Lender)user).getId());
		}
		else
			throw new RuntimeException("不支持的用户类型");
		activityRefDao.create(ref);
	}

	@Override
	public void applyActivityByAnonymous(String name, String phone, String email, String messageValidateCode, Integer activityId) throws IllegalArgumentException, ValidateCodeException, LoginException {
		Lender lender=new Lender();
		lender.setName(StringUtil.checkNullAndTrim("name", name));
		lender.setTel(StringUtil.checkNullAndTrim("tel", phone));
		lender.setEmail(StringUtil.checkNullAndTrim("email", email));
		lender.setLoginId("L"+lender.getTel());
		lender.setPassword("L"+lender.getTel());
		lender=lenderService.register(lender, messageValidateCode);
		applyActivity(activityId);
	}

}
