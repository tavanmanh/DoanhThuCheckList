package com.viettel.coms.webservice;


import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.HolidayBusinessImpl;
import com.viettel.coms.dto.AppVersionDTOResponse;
import com.viettel.coms.dto.AppVersionWorkItemDTO;
import com.viettel.coms.dto.HolidayRequest;
import com.viettel.coms.dto.HolidayResponse;
import com.viettel.coms.dto.StockTransRequest;
import com.viettel.coms.dto.StockTransResponse;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.UserHolidayDTO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;

@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
@Path("/service")
public class HolidayWsRsService {
    private Logger LOGGER = Logger.getLogger(HolidayWsRsService.class);

    @Autowired
    HolidayBusinessImpl holidayBusiness;
    @Autowired
    AuthenticateWsBusiness authenticateWsBusiness;
    @POST
    @Path("/getListUserHoliday/")
    public HolidayResponse getListUserHoliday(SysUserRequest request) {
    	HolidayResponse response = new HolidayResponse();
        try {
            List<UserHolidayDTO> data = holidayBusiness.getListUserHoliday(request);
            response.setLstUserHolidayDto(data);
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
    @Path("/addHoliday/")
    public HolidayResponse addHoliday(HolidayRequest request) throws Exception {
    	HolidayResponse response = new HolidayResponse();
        try {
            Long result = holidayBusiness.insertHoliday(request);     
            if (result == -1L) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Thời gian bạn đăng ký đi làm đã tồn tại");
                response.setResultInfo(resultInfo);
            } else if (result == -2L) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Đăng ký vượt quá số ngày nghỉ cho phép");
                response.setResultInfo(resultInfo);
            } else if (result == 1L) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Có lỗi xảy ra");
                response.setResultInfo(resultInfo);
            }
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage("Có lỗi xảy ra");
            response.setResultInfo(resultInfo);
        }
        return response;
    }
    @POST
    @Path("/getListManagerHoliday/")
    public HolidayResponse getListManagerHoliday(SysUserRequest request) {
    	HolidayResponse response = new HolidayResponse();
        try {
            List<UserHolidayDTO> data = holidayBusiness.getListManagerHoliday(request);
            response.setLstUserHolidayDto(data);
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
    @Path("/approveRejectHoliday/")
    public HolidayResponse approveRejectHoliday(HolidayRequest request) {
    	HolidayResponse response = new HolidayResponse();
        try {
            ResultInfo resultInfo = new ResultInfo();
            int result = holidayBusiness.approveRejectHoliday(request, resultInfo);
            if (result > 0) {
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Có lỗi xảy ra");
                response.setResultInfo(resultInfo);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage("Có lỗi xảy ra");
            response.setResultInfo(resultInfo);
        }
        return response;

    }
    @POST
    @Path("/getAppVersion/")
    public HolidayResponse getAppVersion() {
    	HolidayResponse response = new HolidayResponse();
        try {
            List<UserHolidayDTO> data = holidayBusiness.getAppVersion();
            response.setLstUserHolidayDto(data);
//            UserHolidayDTO dataDetail = data.get(0);
//            response.setUserHolidayDto(dataDetail);
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
