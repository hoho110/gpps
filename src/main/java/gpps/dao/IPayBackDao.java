/**
 * 
 */
package gpps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gpps.model.PayBack;

public interface IPayBackDao {
	public void create(PayBack payback);

	public List<PayBack> findAllByProduct(Integer productId);

	public void changeState(@Param("id") Integer id, @Param("state") int state);

	public PayBack find(Integer id);

	public void delete(Integer id);
}
