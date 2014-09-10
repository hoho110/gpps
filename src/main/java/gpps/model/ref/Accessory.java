package gpps.model.ref;

import java.util.ArrayList;
import java.util.List;

public class Accessory {
	private List<MimeCol> cols=new ArrayList<Accessory.MimeCol>();
	public List<MimeCol> getCols() {
		return cols;
	}
	public void setCols(List<MimeCol> cols) {
		this.cols = cols;
	}
	public class MimeCol{
		private int category;//逻辑类别
		private List<MimeItem> items=new ArrayList<Accessory.MimeItem>();
		public int getCategory() {
			return category;
		}
		public void setCategory(int category) {
			this.category = category;
		}
		public List<MimeItem> getItems() {
			return items;
		}
		public void setItems(List<MimeItem> items) {
			this.items = items;
		}
	}
	public class MimeItem {
		private String mimeType;//mime类型
		private long uploadTime=System.currentTimeMillis();//上传时间
		private String fileName;//文件名称
		private String showName;//展现名称
		private String path;//存储路径
		public String getMimeType() {
			return mimeType;
		}
		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}
		public long getUploadTime() {
			return uploadTime;
		}
		public void setUploadTime(long uploadTime) {
			this.uploadTime = uploadTime;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getShowName() {
			return showName;
		}
		public void setShowName(String showName) {
			this.showName = showName;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}	
	}
}