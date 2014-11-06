package gpps.service.impl;

import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

import gpps.service.thirdpay.IThirdPaySupportService;
public class ThirdPaySupportServiceImpl implements IThirdPaySupportService{
	public static final String ACTION_REGISTACCOUNT="0";
	private static Map<String, String> urls=new HashMap<String, String>();
	static {
		urls.put(String.valueOf(ACTION_REGISTACCOUNT), "/loan/toloanregisterbind.action");
	}
	private String url="";
	private String platformMoneymoremore="p401";
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getBaseUrl(String action) {
		return url+urls.get(action);
	}
}
