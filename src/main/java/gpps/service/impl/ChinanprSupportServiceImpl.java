package gpps.service.impl;

import gpps.service.IChinanprSupportService;
public class ChinanprSupportServiceImpl implements IChinanprSupportService{
	private String url="";
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
