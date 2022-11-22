package com.viettel.coms.webservice;

import com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider.App;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.ManageMEBusiness;
import com.viettel.coms.business.ManageMEBusinessImpl;
import com.viettel.coms.dto.DeviceStationElectricalDTO;
import com.viettel.coms.dto.StationElectricalDTO;
import com.viettel.coms.dto.StationElectricalRequest;
import com.viettel.coms.dto.StationElectricalResponse;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.dto.AppParamDTO;

@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
@Path("/service")
public class StationElectricalWsService {
	
	private Logger LOGGER = Logger.getLogger(StationElectricalWsService.class);
	
	@Autowired
	private ManageMEBusiness manageMEBusiness;
	
	@Autowired
	ManageMEBusinessImpl manageMEBusinessImpl;
	
	//Lấy danh sách trạm
	@POST
    @Path("/getAllStationElectrical/")
    public StationElectricalResponse getAllStationElectrical(StationElectricalRequest request) {
		StationElectricalResponse response = new StationElectricalResponse();
        try {
        	request.getStationElectricalDTO().setPage(1l);
        	request.getStationElectricalDTO().setPageSize(100);
        	List<StationElectricalDTO> data = manageMEBusiness.doSearch(request.getStationElectricalDTO(), request.getSysUserRequest().getSysUserId());
            response.setLstStationElectricalDTO(data);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }
	
	//Lấy loại thiết bị
	@POST
    @Path("/getDeviceType/")
    public StationElectricalResponse getDeviceType() {
		StationElectricalResponse response = new StationElectricalResponse();
        try {
        	List<AppParamDTO> data = manageMEBusiness.getAppParamByParType("DIVICE_ELECTRIC");
            response.setLstDeviceType(data);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }
	
	//Lấy danh sách thiết bị
	@POST
    @Path("/getDeviceStationElectrical/")
    public StationElectricalResponse getDeviceStationElectrical(StationElectricalRequest request) {
		StationElectricalResponse response = new StationElectricalResponse();
        try {
        	request.getStationElectricalDTO().setPage(1l);
        	request.getStationElectricalDTO().setPageSize(100);
        	DeviceStationElectricalDTO device = new DeviceStationElectricalDTO();
        	device.setType(request.getStationElectricalDTO().getDeviceType());
        	device.setStatus("1");
        	device.setStationId(request.getStationElectricalDTO().getStationId());
        	List<DeviceStationElectricalDTO> data = manageMEBusiness.getDevices(device);
            response.setLstDeviceStationElectricalDTO(data);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }
	
	//Lưu thông tin chi tiết thiết bị
	@POST
    @Path("/saveDeviceDetail/")
    public StationElectricalResponse saveDeviceDetail(StationElectricalRequest request) {
		StationElectricalResponse response = new StationElectricalResponse();
        try {
        	manageMEBusiness.saveDeviceDetail(request);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }
	
	@POST
    @Path("/getDetailDeviceById/")
    public StationElectricalResponse getDetailDeviceById(StationElectricalRequest request) {
		StationElectricalResponse response = new StationElectricalResponse();
        try {
        	Object data = manageMEBusinessImpl.getDeviceDetails(request.getDeviceStationElectricalDTO(), true).getData();
            response.setData(data);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }


    // todo
    @POST
    @Path("/getBrokenDeviceReason/")
    public StationElectricalResponse getBrokenDeviceReason(AppParamDTO request) {
        StationElectricalResponse response = new StationElectricalResponse();
        try {
            List<AppParamDTO> data = manageMEBusiness.getAppParamByParType(request.getParType());
            response.setData(data);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("/createBroken/")
    public StationElectricalResponse createBroken(DeviceStationElectricalDTO obj) {
        StationElectricalResponse response = new StationElectricalResponse();
        try {
            manageMEBusiness.createBroken(obj);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

}
