package gpps.service;

import gpps.model.News;

import java.util.Map;

public interface INewsService {
	public void create(News news);
	public News find(Integer id);
	public Map<String,Object> findAll(int offset,int recnum);
}
