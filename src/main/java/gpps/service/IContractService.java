package gpps.service;

import java.util.List;

public interface IContractService {
	public List<ContractItem> list(Integer pid) throws Exception;
	public String getProductName(Integer pid);
}
