/**
 * 
 */
package gpps.dao;

import gpps.model.Product;
import gpps.service.exception.IllegalConvertException;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author wangm
 *
 */
public interface IProductDao {
	public void create(Product product);
	public void changeState(@Param("id")Integer id,@Param("state")int state) throws IllegalConvertException;
	public Product find(Integer id);
	public List<Product> findByGovermentOrder(Integer orderId);
	public List<Product> findByState(@Param("states")List<Integer> states,@Param("offset")int offset,@Param("recnum")int recnum);
	public int countByState(@Param("states")List<Integer> states);
	public List<Product> findByProductSeriesAndState(@Param("productSeriesId")Integer productSeriesId,@Param("states")List<Integer> states,@Param("offset")int offset,@Param("recnum")int recnum);
	public int countByProductSeriesAndState(@Param("productSeriesId")Integer productSeriesId,@Param("states")List<Integer> state);
	public void changeBuyLevel(@Param("id")Integer id,@Param("levelToBuy")int levelToBuy);
	public void buy(@Param("id")Integer id,@Param("amount")int amount);
	public void delete(Integer id);
}
