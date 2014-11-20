package gpps.service.impl;

import gpps.dao.IActivityRefDao;
import gpps.model.ActivityRef;
import gpps.model.Borrower;
import gpps.model.Lender;
import gpps.service.IActivityRefService;
import gpps.service.ILoginService;

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
		ref.setApplyState(ActivityRef.APPLYSTATE_YES);
		ref.setApplyTime(System.currentTimeMillis());
		if(user instanceof Lender)
		{
			ref.setParticipatorType(ActivityRef.PARTICIPATORTYPE_LENDER);
			ref.setParticipatorId(((Lender)user).getId());
		}else if(user instanceof Borrower)
		{
			ref.setParticipatorType(ActivityRef.PARTICIPATORTYPE_BORROWER);
			ref.setParticipatorId(((Borrower)user).getId());
		}
		else
			throw new RuntimeException("不支持的用户类型");
		activityRefDao.create(ref);
	}

}
