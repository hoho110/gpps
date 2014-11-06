package gpps.service.thirdpay;

public interface IThirdPaySupportService {
	/**
	 * 根据行为获取url
	 * @param action
	 * @return
	 */
	public String getBaseUrl(String action);
}
