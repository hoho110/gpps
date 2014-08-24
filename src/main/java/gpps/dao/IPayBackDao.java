/**
 * 
 */
package gpps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gpps.model.Payback;

/**
 * @author wangm
 *
 */
public interface IPayBackDao {
	public void create(Payback payback);
	public List<Payback> findAllByProduct(Integer productId);
	public void changeState(@Param("id")Integer id,@Param("state")int state);
	public Payback find(Integer id);
	public void delete(Integer id);
}
