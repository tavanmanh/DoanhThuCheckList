package com.viettel.coms.webservice;

import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.WoPlanBusiness;
import com.viettel.coms.dto.*;
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
@Path("/")
public class WoPlanService {
    private Logger LOGGER = Logger.getLogger(WoPlanService.class);
    @Autowired
    WoPlanBusiness woPlanBusiness;
    @Autowired
    AuthenticateWsBusiness authenticateWsBusiness;

    @POST
    @Path("list")
    public WoPlanDTOResponse list(SysUserRequest request) {
        WoPlanDTOResponse response = new WoPlanDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request);
            List<WoPlanDTO> listWoPlans = woPlanBusiness.list(request.getSysUserId());
            response.setListWoPlans(listWoPlans);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response = exceptionResponse(response, e.getMessage());
        }
        return response;
    }

    @POST
    @Path("/getListWoByPlanId/")
    public WoPlanDTOResponse getListWoByPlanId(WoPlanDTORequest request) {
        WoPlanDTOResponse response = new WoPlanDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<WoDTO> data = woPlanBusiness.getListWosByPlanId(request.getWoPlanDTO());
            response.setLstWosOfPlan(data);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response = exceptionResponse(response, e.getMessage());
        }
        return response;
    }

    @POST
    @Path("/insert/")
    public WoPlanDTOResponse insertWoPlan(WoPlanDTORequest request) {
        WoPlanDTOResponse response = new WoPlanDTOResponse();
        try {
            SysUserRequest sysUserRequest = request.getSysUserRequest();
            authenticateWsBusiness.validateRequest(sysUserRequest);
            request.getWoPlanDTO().setFtId(sysUserRequest.getSysUserId());
            woPlanBusiness.insert(request.getWoPlanDTO(), request.getLstWosOfPlan());
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response = exceptionResponse(response, e.getMessage());
        }

        return response;
    }

    @POST
    @Path("/update/")
    public WoPlanDTOResponse updateWoPlan(WoPlanDTORequest request) {
        WoPlanDTOResponse response = new WoPlanDTOResponse();
        try {
            SysUserRequest sysUserRequest = request.getSysUserRequest();
            authenticateWsBusiness.validateRequest(sysUserRequest);
            request.getWoPlanDTO().setFtId(sysUserRequest.getSysUserId());
            woPlanBusiness.update(request.getWoPlanDTO(), request.getLstWosOfPlan());
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response = exceptionResponse(response, e.getMessage());
        }
        return response;
    }

    @POST
    @Path("/delete/")
    public WoPlanDTOResponse deleteWoPlan(WoPlanDTORequest request) {
        WoPlanDTOResponse response = new WoPlanDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            boolean isDeleted = woPlanBusiness.deletePlan(request.getWoPlanDTO());
            ResultInfo resultInfo = new ResultInfo();
            if(isDeleted){
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            }
            else{
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Không được xóa kế hoạch khi có WO đang thực hiện");
                response.setResultInfo(resultInfo);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response = exceptionResponse(response, e.getMessage());
        }
        return response;
    }

    private WoPlanDTOResponse exceptionResponse(WoPlanDTOResponse response, String message) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setStatus(ResultInfo.RESULT_NOK);
        resultInfo.setMessage(message);
        response.setResultInfo(resultInfo);

        return response;
    }
}
