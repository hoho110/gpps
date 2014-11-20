package gpps.service.impl;

import gpps.constant.Pagination;
import gpps.dao.ILetterDao;
import gpps.model.Borrower;
import gpps.model.Lender;
import gpps.model.Letter;
import gpps.service.ILetterService;
import gpps.service.ILoginService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Service
public class LetterServiceImpl implements ILetterService{
	@Autowired
	ILetterDao letterDao;
	@Override
	public void create(Letter letter) {
		letterDao.create(letter);
	}

	@Override
	public Letter find(Integer id) {
		return letterDao.find(id);
	}

	@Override
	public Map<String, Object> findMyLetters(int offset, int recnum) {
		Integer receiverId=null;
		int receivertype=Letter.RECEIVERTYPE_LENDER;
		HttpSession session=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Object user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if(user instanceof Lender)
		{
			receiverId=((Lender)user).getId();
			receivertype=Letter.RECEIVERTYPE_LENDER;
		}else if(user instanceof Borrower)
		{
			receiverId=((Borrower)user).getId();
			receivertype=Letter.RECEIVERTYPE_BORROWER;
		}
		else
			throw new RuntimeException("不支持的用户类型");
		int count=letterDao.countByReceiver(receivertype, receiverId);
		if(count==0)
			return Pagination.buildResult(null, count, offset, recnum);
		List<Letter> letters=letterDao.findByReceiver(receivertype, receiverId, offset, recnum);
		return Pagination.buildResult(letters, count, offset, recnum);
	}

	@Override
	public void alreadyRead(Integer id) {
		letterDao.changeMarkRead(id, Letter.MARKREAD_YES);
	}

}
