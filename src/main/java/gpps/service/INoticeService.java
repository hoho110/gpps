package gpps.service;

import gpps.model.Notice;

import java.util.Map;

public interface INoticeService {
	public void create(Notice notice);
	public Notice find(Integer id);
	/**
	 * 
	 * @param usefor  -1:不限 0：lender 1：borrower',
	 * @param level -1：不限，to不为-1时启用
	 * @param offset
	 * @param recnum
	 * @return
	 */
	public Map<String,Object> findAll(int usefor,int level,int offset,int recnum);
}
