package com.viettel.coms.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.ApiConstructionBusiness;
import com.viettel.coms.dto.ApiConstructionRequest;
import com.viettel.coms.dto.ApiConstructionResponse;
import com.viettel.coms.dto.WoDTO;

@Consumes({ MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML })
@Path("/service")
public class RestApiConstructionService {

	private Logger LOGGER = Logger.getLogger(RestApiConstructionService.class);
	
	@Autowired
	private ApiConstructionBusiness apiConstructionBusiness;
	
	@POST
	@Path("/getSolarSystemBusinessInfo")
	public ApiConstructionResponse getSolarSystemBussinessInfo(ApiConstructionRequest request) {
		ApiConstructionResponse response = new ApiConstructionResponse();
		try {
			ResultInfo resultInfo = new ResultInfo();
			List<ApiConstructionResponse> res = apiConstructionBusiness.getSolarSystemBussinessInfo(request);
			if(res!=null) {
				response.setListConstructionResponse(res);
			} else {
				resultInfo.setMessage("Mã công trình không tồn tại");
			}
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		
		return response;
	}
	
	@POST
	@Path("/getConstructionAttachFile")
	public ApiConstructionResponse getConstructionAttachFile(ApiConstructionRequest request) {
		ApiConstructionResponse response = new ApiConstructionResponse();
		try {
			ResultInfo resultInfo = new ResultInfo();
			ApiConstructionResponse res = apiConstructionBusiness.getConstructionAttachFile(request);
			if(res.getLstFileTrVhkt().size()>0) {
				response = res;
			} else {
				resultInfo.setMessage("Không có file đính kèm tương ứng");
			}
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		
		return response;
	}
	
	@POST
	@Path("/getWOInfoByAlert")
	public ApiConstructionResponse getWOInfoByAlert(ApiConstructionRequest request) {
		ApiConstructionResponse response = new ApiConstructionResponse();
		try {
			ResultInfo resultInfo = new ResultInfo();
			ApiConstructionResponse res = apiConstructionBusiness.getWOInfoByAlert(request);
			if(res.getLstWOInfoVhkt().size()>0) {
				response = res;
			} else {
				resultInfo.setMessage("Không có WO tương ứng");
			}
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		
		return response;
	}
	
	@POST
	@Path("/getTRWOBySystem")
	public ApiConstructionResponse getTRWOBySystem(ApiConstructionRequest request) {
		ApiConstructionResponse response = new ApiConstructionResponse();
		try {
			ResultInfo resultInfo = new ResultInfo();
			ApiConstructionResponse res = apiConstructionBusiness.getTRWOBySystem(request);
			if(res.getLstWOInfoVhkt().size()>0) {
				response = res;
			} else {
				resultInfo.setMessage("Không có WO và TR tương ứng");
			}
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		
		return response;
	}
	
	@POST
	@Path("/getQuickGISBySystem")
	public ApiConstructionResponse getQuickGISBySystem(ApiConstructionRequest request) {
		ApiConstructionResponse response = new ApiConstructionResponse();
		try {
			response = apiConstructionBusiness.getQuickGISBySystem(request);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setMessage(e.getMessage());
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			response.setResultInfo(resultInfo);
		}
		
		return response;
	}
	
	@POST
	@Path("/getDataReportMaintainPeriodic")
	public ApiConstructionResponse getDataReportMaintancePeriodic(ApiConstructionRequest request) {
		ApiConstructionResponse response = new ApiConstructionResponse();
		try {
			response = apiConstructionBusiness.getDataReportMaintancePeriodic(request);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setMessage(e.getMessage());
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			response.setResultInfo(resultInfo);
		}
		
		return response;
	}
	
	@POST
	@Path("/createWO")
	public ResultInfo createWO(WoDTO request) {
		ResultInfo response = new ResultInfo();
		try {
			String mess = apiConstructionBusiness.createWO(request);
			if(StringUtils.isNotBlank(mess)) {
				response.setStatus(ResultInfo.RESULT_OK);
			} else {
				response.setStatus(ResultInfo.RESULT_NOK);
			}
			response.setMessage(mess);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(e.getMessage());
			response.setStatus(ResultInfo.RESULT_NOK);
			response.setMessage("");
		}
		
		return response;
	}
}
