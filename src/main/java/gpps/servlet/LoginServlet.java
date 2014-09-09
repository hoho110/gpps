package gpps.servlet;

import gpps.service.ILenderService;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class LoginServlet {
	@Autowired
	ILenderService lenderService;
	@RequestMapping(value={"/login/graphValidateCode"})
	public void thirdPartyRegist(HttpServletRequest req, HttpServletResponse resp)
	{
		resp.setContentType("image/jpeg");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		try {
			lenderService.writeGraphValidateCode(resp.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
