/**
 * 
 */
package gpps.model;

/**
 * @author wangm
 *
 */
public class ProductSeries {
	private Integer id;
	private String title;
	private String message;
	private String decstription;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDecstription() {
		return decstription;
	}
	public void setDecstription(String decstription) {
		this.decstription = decstription;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
