package gpps.service;

import gpps.model.Activity;
import gpps.model.ref.Accessory.MimeItem;

import java.util.Map;

import com.easyservice.xml.XMLParseException;

public interface IActivityService {
	public void create(Activity activity);
	public Activity find(Integer id);
	public Map<String,Object> findByState(int state,int offset,int recnum);
	public void update(Integer id,String name, long applyendtime,long  starttime,String url,int state, String description);
	public void addAccessory(Integer id,MimeItem item) throws XMLParseException;
	public void delAccessory(Integer id,String itemId) throws XMLParseException;
	public MimeItem findMimeItem(Integer id)throws XMLParseException;
}
