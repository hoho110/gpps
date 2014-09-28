package gpps.servlet;

import gpps.model.ref.Accessory.MimeItem;
import gpps.service.IBorrowerService;
import gpps.service.IGovermentOrderService;
import gpps.service.IProductService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import com.easyservice.xml.XMLParseException;

@Controller
public class AccessoryTransferServlet {
	private Resource officalDir;// 上传文件存在根目录
	private Resource tempDir;// 临时目录
	private static final long MAXFILESIZE = 1024L * 1024 * 1024;// 1G
	@Autowired
	IGovermentOrderService orderService;
	@Autowired
	IProductService productService;
	@Autowired
	IBorrowerService borrowerService;

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

	Logger log = Logger.getLogger(this.getClass());
	public static final String TYPE_ORDER = "order";
	public static final String TYPE_PRODUCT = "product";
	public static final String TYPE_BORROWER = "borrower";
	public static final String FILESEPARATOR = "/";

	/**
	 * 上传路径："/upload/{type}/{id}/{category}" type取值："order"、"product"、"borrower"
	 * id:上传实体对应ID category：附件类型，int
	 */
	@RequestMapping(value = { "/upload/{type}/{id}/{category}" })
	protected void upload(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("type") String type,
			@PathVariable("id") Integer id,
			@PathVariable("category") int category) throws ServletException,
			IOException {
		// 解析 request，判断是否有上传文件
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			response.sendError(400, "非法的multipart配置");
			return;
		}
		if (!tempDir.exists())
			tempDir.getFile().mkdirs();

		FileItemFactory factory = new DiskFileItemFactory(2048,
				tempDir.getFile());
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置路径、文件名的字符集
		upload.setHeaderEncoding("UTF-8");
		// 设置允许用户上传文件大小,单位:字节
		upload.setSizeMax(MAXFILESIZE); // 最大限制1G
		// 得到所有的表单域，它们目前都被当作FileItem
		List<FileItem> fileItems;
		try {
			fileItems = upload.parseRequest(request);
			if (fileItems == null || fileItems.size() == 0)
				throw new Exception("fileItems项为空");
			for (FileItem item : fileItems) {
				if (item.isFormField())
					continue;
				// 如果item是文件上传表单域
				// 获得文件名及路径
				String fileName = item.getName();
				String uuid = UUID.randomUUID().toString();
				String path = new StringBuilder().append(FILESEPARATOR)
						.append(type).append(FILESEPARATOR).append(id)
						.append(FILESEPARATOR).append(category)
						.append(FILESEPARATOR).append(uuid).toString();
				if (!officalDir.exists())
					officalDir.getFile().mkdirs();
				File uploadFile = new File(officalDir.getFile(), path);
				if (!uploadFile.getParentFile().exists())
					uploadFile.getParentFile().mkdirs();
				uploadFile.createNewFile();
				item.write(uploadFile);
				MimeItem mimeItem = new MimeItem();
				mimeItem.setId(uuid);
				mimeItem.setMimeType(request.getHeader(""));
				mimeItem.setFileName(fileName);
				mimeItem.setPath(path);
				if (type.equals(TYPE_ORDER))
					orderService.addAccessory(id, category, mimeItem);
				else if (type.equals(TYPE_PRODUCT))
					productService.addAccessory(id, category, mimeItem);
				else if (type.equals(TYPE_BORROWER))
					borrowerService.addAccessory(id, category, mimeItem);
				else {
					response.sendError(400, "不支持的上传类型:" + type);
					return;
				}
				System.out.println("文件" + uploadFile.getName() + "上传成功");
			}
			response.setStatus(200);
			ServletUtils.write(response, "上传成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.sendError(403, e.getMessage());
		}
	}

	@RequestMapping(value = { "/download/{type}/{id}/{category}/{itemID}" })
	protected void service(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("type") String type,
			@PathVariable("id") Integer id,
			@PathVariable("category") int category,
			@PathVariable("itemID") String itemID) throws ServletException,
			IOException {
		MimeItem item = null;
		List<MimeItem> items = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			if (type.equals(TYPE_ORDER))
				items = orderService.findMimeItems(id, category);
			else if (type.equals(TYPE_PRODUCT))
				items = productService.findMimeItems(id, category);
			else if (type.equals(TYPE_BORROWER))
				items = borrowerService.findMimeItems(id, category);
			else {
				response.sendError(400, "不支持的下载类型:" + type);
				return;
			}
			item = findItem(items, itemID);
			File downloadFile = new File(officalDir.getFile(), item.getPath());
			if (item == null || !downloadFile.exists()) {
				response.sendError(404, "未找到下载文件");
				return;
			}
			response.setContentType(item.getMimeType());
			response.setHeader("Content-Disposition", "attachment; filename="+ item.getFileName());
			response.addHeader("Content-Length",String.valueOf(downloadFile.length()));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(downloadFile);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, i);
			}
			outputStream.flush();
		} catch (XMLParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
			}
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
			}
		}
	}

	private MimeItem findItem(List<MimeItem> items, String itemID) {
		if (items == null || items.size() == 0)
			return null;
		for (MimeItem item : items) {
			if (item.getId().equals(itemID))
				return item;
		}
		return null;
	}

	private void writeFile(String contentType, String fileName,
			HttpServletResponse response, File file) {
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "attachment; filename="
					+ file.getName());
			response.addHeader("Content-Length", String.valueOf(file.length()));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, i);
			}
			outputStream.flush();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
			}
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
			}
		}
	}
}