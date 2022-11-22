package com.viettel.coms.rest;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.coms.business.YearPlanOSBusinessImpl;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.YearPlanDTO;
import com.viettel.coms.dto.YearPlanOSDTO;
import com.viettel.coms.dto.YearPlanSimpleOSDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;

/**
 * @author HoangNH38
 */
public class YearPlanOSRsServiceImpl implements YearPlanOSRsService{

    protected final Logger log = Logger.getLogger(YearPlanOSRsServiceImpl.class);
    
    @Autowired
    YearPlanOSBusinessImpl yearPlanOSBusinessImpl;
    
    @Context
    HttpServletRequest request;

    @Override
    public Response remove(YearPlanOSDTO obj) {
        // TODO Auto-generated method stub
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DELETE_YEAR_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch năm ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        yearPlanOSBusinessImpl.remove(obj);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
        return Response.ok().build();
    }

    @Override
    public Response add(YearPlanSimpleOSDTO obj) throws Exception {
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("INSERT_YEAR_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch năm ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            obj.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
            obj.setStatus("1");
            obj.setCreatedDate(new Date());
            obj.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
            obj.setSignState("1");
            objKpiLog.setStartTime(new Date());
            Long ids = yearPlanOSBusinessImpl.add(obj);
            if (ids == 0l) {
                Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("0");
                yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("1");
                yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
                return Response.ok(Response.Status.CREATED).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response update(YearPlanSimpleOSDTO obj) throws Exception {
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("UPDATE_YEAR_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch năm ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
            obj.setUpdatedDate(new Date());
            obj.setUpdatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
            objKpiLog.setStartTime(new Date());
            Long ids = yearPlanOSBusinessImpl.updateYearPlan(obj);
            if (ids == 0l) {
                Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("0");
                yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("1");
                yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
                return Response.ok(Response.Status.CREATED).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response doSearch(YearPlanSimpleOSDTO obj) {
        // TODO Auto-generated method stub
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        //tanqn 20181109 start
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DOSEARCH_YEAR_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch năm ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        objKpiLog.setStartTime(dStart);
        
        // tam thoi chua lay theo don vi tao
        DataListDTO data = yearPlanOSBusinessImpl.doSearch(obj, null);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
        objKpiLog.setStatus("1");
        yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
      //tanqn 20181109 end
        return Response.ok(data).build();
    }

    @Override
    public Response getById(Long id) throws Exception {
        YearPlanSimpleOSDTO data = yearPlanOSBusinessImpl.getById(id);
        return Response.ok(data).build();
    }

    @Override
    public Response getSequence() throws Exception {
        // TODO Auto-generated method stub
        Long seq = yearPlanOSBusinessImpl.getSequence();
        Long a = seq;
        return Response.ok(seq).build();
    }

    @Override
    public Response exportExcelTemplate() throws Exception {
        // TODO Auto-generated method stub
        try {
            String strReturn = yearPlanOSBusinessImpl.exportExcelTemplate();
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Response exportYearPlan(YearPlanSimpleOSDTO obj) throws Exception {
    	 KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
         //tanqn 20181109 start
         KpiLogDTO objKpiLog = new KpiLogDTO();
         Date dStart = new Date();
         objKpiLog.setCreateDatetime(dStart);
         objKpiLog.setFunctionCode("EXPORT_EXEL_YEAR_PLAN");
         objKpiLog.setDescription("Kế hoạch năm");
         objKpiLog.setTransactionCode(obj.getCode());
         objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
         objKpiLog.setStartTime(new Date());
        try {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            String strReturn = yearPlanOSBusinessImpl.exportYearPlan(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response getAppParamByType(String type) throws Exception {
        // TODO Auto-generated method stub
        List<AppParamDTO> data = yearPlanOSBusinessImpl.getAppParamByType(type);
        return Response.ok(data).build();
    }
    
    @Override
    public Response updateRegistry(YearPlanOSDTO obj) {
        // TODO Auto-generated method stub
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("UPDATE_SIGNSTATE_OS");
        objKpiLog.setDescription("Kế hoạch năm ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        yearPlanOSBusinessImpl.updateRegistry(obj);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
        return Response.ok().build();
    }

}
