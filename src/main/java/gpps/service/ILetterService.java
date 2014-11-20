package gpps.service;

import gpps.model.Letter;

import java.util.Map;

public interface ILetterService {
	public void create(Letter letter);
	public Letter find(Integer id);
	public Map<String,Object> findMyLetters(int offset,int recnum);
	public void alreadyRead(Integer id);
}
