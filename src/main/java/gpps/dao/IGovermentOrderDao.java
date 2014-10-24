package gpps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gpps.model.GovermentOrder;
import gpps.model.ref.Accessory.MimeItem;

public interface IGovermentOrderDao {
	public void create(GovermentOrder govermentOrder);
	public void changeState(@Param("orderId")Integer orderId,@Param("state")int state,@Param("lastModifytime")long lastModifytime);
	public List<GovermentOrder> findByStatesWithPaging(@Param("states")List<Integer> states,@Param("offset")int offset,@Param("recnum")int recnum);
	public List<GovermentOrder> findByStates(@Param("states")List<Integer> states);
	public int countByState(@Param("states")List<Integer> states);
	public List<GovermentOrder> findByBorrowerIdAndState(@Param("borrowerId")Integer borrowerId,@Param("states")List<Integer> states);
	public void delete(Integer id);
	public GovermentOrder find(Integer id);
	public String findAccessory(Integer orderId);
	public void updateAccessory(@Param("orderId")Integer orderId,@Param("material")String material);
	
	public GovermentOrder findByFinancingRequest(Integer financingRequestId);
	
	public List<GovermentOrder> findAllUnpublishOrders();
}
