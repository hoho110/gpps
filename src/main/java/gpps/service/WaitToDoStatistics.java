package gpps.service;

public class WaitToDoStatistics {
	private int applyBorrowerCount;
	private int financingRequestCount;
	private int prepublishOrderCount;
	private int unCheckedOrderCount;
	private int waitingCloseOrderCount;
	public int getApplyBorrowerCount() {
		return applyBorrowerCount;
	}
	public void setApplyBorrowerCount(int applyBorrowerCount) {
		this.applyBorrowerCount = applyBorrowerCount;
	}
	public int getFinancingRequestCount() {
		return financingRequestCount;
	}
	public void setFinancingRequestCount(int financingRequestCount) {
		this.financingRequestCount = financingRequestCount;
	}
	public int getPrepublishOrderCount() {
		return prepublishOrderCount;
	}
	public void setPrepublishOrderCount(int prepublishOrderCount) {
		this.prepublishOrderCount = prepublishOrderCount;
	}
	public int getUnCheckedOrderCount() {
		return unCheckedOrderCount;
	}
	public void setUnCheckedOrderCount(int unCheckedOrderCount) {
		this.unCheckedOrderCount = unCheckedOrderCount;
	}
	public int getWaitingCloseOrderCount() {
		return waitingCloseOrderCount;
	}
	public void setWaitingCloseOrderCount(int waitingCloseOrderCount) {
		this.waitingCloseOrderCount = waitingCloseOrderCount;
	}
}
