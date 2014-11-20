package gpps.dao;

import gpps.model.News;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface INewsDao {
	public void create(News news);
	public News find(Integer id);
	public int count();
	public List<News> findAll(@Param("offset")int offset,@Param("recnum")int recnum);
}
