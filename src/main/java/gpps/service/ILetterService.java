package gpps.service;

import gpps.model.Letter;

import java.util.Map;

public interface ILetterService {
	public void create(Letter letter);
	public Letter find(Integer id);
	/**
	 * 
	 * @param markRead -1为不限 0:未读；1：已读
	 * @param offset
	 * @param recnum
	 * @return
	 */
	public Map<String,Object> findMyLetters(int markRead,int offset,int recnum);
	public Map<String,Object> findAll(int offset,int recnum);
	public void alreadyRead(Integer id);
}
