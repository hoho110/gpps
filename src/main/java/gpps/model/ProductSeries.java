/**
 * 
 */
package gpps.model;

import java.util.ArrayList;
import java.util.List;

import gpps.model.ref.TagCol;
import gpps.model.ref.TagCol.Item;
import gpps.tools.StringUtil;

import com.easyservice.xml.EasyObjectXMLTransformerImpl;
import com.easyservice.xml.IEasyObjectXMLTransformer;
import com.easyservice.xml.XMLParseException;

/**
 * @author wangm
 *
 */
public class ProductSeries {
	private Integer id;
	private String title;
	private String tag;//schema title description
	private String description;
	private static final IEasyObjectXMLTransformer xmlTransformer=new EasyObjectXMLTransformerImpl(); 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
		if(!StringUtil.isEmpty(tag))
		{
			try {
				this.tagCol=xmlTransformer.parse(tag, TagCol.class);
			} catch (XMLParseException e) {
				e.printStackTrace();
			}
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private TagCol tagCol;
	public TagCol getTagCol() {
		return tagCol;
	}
	public void setTagCol(TagCol tagCol) {
		this.tagCol = tagCol;
		if(tagCol!=null)
			this.tag=xmlTransformer.export(tagCol);
	}
	public static void main(String[] args)
	{
		TagCol tagCol=new TagCol();
		List<Item> tags=new ArrayList<Item>();
		tags.add(new Item("有担保","低风险"));
		tags.add(new Item("无担保","搞风险"));
		tagCol.setItems(tags);
		String text=xmlTransformer.export(tagCol);
		System.out.println(text);
		try {
			tagCol=xmlTransformer.parse(text, TagCol.class);
		} catch (XMLParseException e) {
			e.printStackTrace();
		}
		
	}
}
