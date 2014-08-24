package gpps.dao;

import gpps.model.Submit;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ISubmitDao {
	public void create(Submit submit);
	public List<Submit> findAllByLender(Integer lenderId);
	/**
	 * 
	 * @param lenderId
	 * @param states null为不限
	 * @return
	 */
	public List<Submit> findAllByLenderAndStates(@Param("lenderId") Integer lenderId,@Param("states")List<Integer> states);
	public List<Submit> findAllByProduct(Integer productId);
	public Submit find(Integer id);
	public void changeState(@Param("id")Integer id,@Param("state")int state);
	public void delete(Integer id);
}
