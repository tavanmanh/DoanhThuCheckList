package com.viettel.coms.business;

import java.util.List;

import com.viettel.coms.dto.ApiConstructionRequest;
import com.viettel.coms.dto.ApiConstructionResponse;
import com.viettel.coms.dto.WoDTO;

public interface ApiConstructionBusiness {

	public List<ApiConstructionResponse> getSolarSystemBussinessInfo(ApiConstructionRequest request);
	
	public ApiConstructionResponse getConstructionAttachFile(ApiConstructionRequest request);
	
	public ApiConstructionResponse getWOInfoByAlert(ApiConstructionRequest request);
	
	public ApiConstructionResponse getTRWOBySystem(ApiConstructionRequest request);
	
	public ApiConstructionResponse getQuickGISBySystem(ApiConstructionRequest request);
	
	public ApiConstructionResponse getDataReportMaintancePeriodic(ApiConstructionRequest request);
	
	public String createWO(WoDTO request);

}
