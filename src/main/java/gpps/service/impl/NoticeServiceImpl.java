package gpps.service.impl;

import gpps.constant.Pagination;
import gpps.dao.INoticeDao;
import gpps.model.Notice;
import gpps.service.INoticeService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class NoticeServiceImpl implements INoticeService{
	@Autowired
	INoticeDao noticeDao;
	@Override
	public void create(Notice notice) {
		noticeDao.create(notice);
	}

	@Override
	public Notice find(Integer id) {
		return noticeDao.find(id);
	}

	@Override
	public Map<String, Object> findAll(int usefor, int level, int offset,
			int recnum) {
		int count=noticeDao.count(usefor, level);
		if(count==0)
			return Pagination.buildResult(null, count, offset, recnum);
		List<Notice> notices=noticeDao.findAll(usefor, level, offset, recnum);
		return Pagination.buildResult(notices, count, offset, recnum);
	}

}
