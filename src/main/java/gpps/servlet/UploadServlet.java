package gpps.servlet;

import gpps.model.ref.Accessory.MimeItem;
import gpps.service.IGovermentOrderService;
import gpps.service.IProductService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UploadServlet{
	private Resource officalDir;//上传文件存在根目录
	private Resource tempDir;//临时目录
	private static final long MAXFILESIZE=1024L*1024*1024;//1G
	@Autowired
	IGovermentOrderService orderService;
	@Autowired
	IProductService productService;
	public Resource getOfficalDir() {
		return officalDir;
	}

	public void setOfficalDir(Resource officalDir) {
		this.officalDir = officalDir;
	}

	public Resource getTempDir() {
		return tempDir;
	}

	public void setTempDir(Resource tempDir) {
		this.tempDir = tempDir;
	}

	Logger log=Logger.getLogger(this.getClass());
	public static final String TYPE_ORDER="order";
	public static final String TYPE_PRODUCT="product";
	public static final String FILESEPARATOR ="/";

	@RequestMapping(value={"/upload/{type}/{id}/{category}"})
	protected void service(HttpServletRequest request, HttpServletResponse response,@PathVariable("type") String type,@PathVariable("id") Integer id,@PathVariable("category") int category)
			throws ServletException, IOException {
	    // 解析 request，判断是否有上传文件  
	    boolean isMultipart = ServletFileUpload.isMultipartContent(request);  
	    if(!isMultipart){
	    	response.sendError(400, "非法的multipart配置");
	    	return;
	    }
	   if(!tempDir.exists())
		   tempDir.getFile().mkdirs();
	    	
	    FileItemFactory factory=new DiskFileItemFactory(2048,tempDir.getFile());
        ServletFileUpload upload = new ServletFileUpload(factory);  
         // 设置路径、文件名的字符集  
        upload.setHeaderEncoding("UTF-8");  
        // 设置允许用户上传文件大小,单位:字节  
        upload.setSizeMax(MAXFILESIZE);  //最大限制1G
        // 得到所有的表单域，它们目前都被当作FileItem  
         List<FileItem> fileItems;
		try {
			fileItems = upload.parseRequest(request);
			if(fileItems==null||fileItems.size()==0)
				throw new Exception("fileItems项为空");
			for (FileItem item : fileItems) {
				if (item.isFormField())
					continue;
				// 如果item是文件上传表单域
				// 获得文件名及路径
				String fileName = item.getName();
				String path=new StringBuilder().append(FILESEPARATOR).append(type)
											   .append(FILESEPARATOR).append(id)
											   .append(FILESEPARATOR).append(category)
											   .append(FILESEPARATOR).append(UUID.randomUUID().toString()).toString();
				if(!officalDir.exists())
					officalDir.getFile().mkdirs();
				File uploadFile=new File(officalDir.getFile(),path);
				if(!uploadFile.getParentFile().exists())
					uploadFile.getParentFile().mkdirs();
				uploadFile.createNewFile();
				item.write(uploadFile);
				MimeItem mimeItem=new MimeItem();
				mimeItem.setMimeType(request.getHeader(""));
				mimeItem.setFileName(fileName);
				mimeItem.setPath(path);
				if(type.equals(TYPE_ORDER))
					orderService.addAccessory(id, category, mimeItem);
				else if(type.equals(TYPE_PRODUCT))
					productService.addAccessory(id, category, mimeItem);
				else {
					response.sendError(400, "不支持的上传类型:"+type);
					return;
				}
				System.out.println("文件" + uploadFile.getName()+ "上传成功");
			}
			response.setStatus(200);
			ServletUtils.write(response, "上传成功");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			response.sendError(403, e.getMessage());
		}  
	}
}
