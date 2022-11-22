/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.coms.business.SynStockTransBusinessImpl;
import com.viettel.coms.business.YearPlanBusinessImpl;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.SynStockTransDTO;
import com.viettel.coms.dto.SynStockTransDetailDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.business.UserRoleBusinessImpl;

/**
 * @author HungLQ9
 */
public class SynStockTransRsServiceImpl implements SynStockTransRsService {

    protected final Logger log = Logger.getLogger(SynStockTransBusinessImpl.class);
    @Autowired
    SynStockTransBusinessImpl synStockTransBusinessImpl;

    @Autowired
    private UserRoleBusinessImpl userRoleBusiness;

    @Context
    private HttpServletRequest request;

    @Autowired
    YearPlanBusinessImpl yearPlanBusinessImpl;
    
    @Override
    public Response getSynStockTrans() {
        List<SynStockTransDTO> ls = synStockTransBusinessImpl.getAll();
        if (ls == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            DataListDTO data = new DataListDTO();
            data.setData(ls);
            data.setTotal(ls.size());
            data.setSize(ls.size());
            data.setStart(1);
            return Response.ok(data).build();
        }
    }

    @Override
    public Response getSynStockTransById(Long id) {
        SynStockTransDTO obj = (SynStockTransDTO) synStockTransBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(obj).build();
        }
    }

    @Override
    public Response updateSynStockTrans(SynStockTransDTO obj) {
        Long id = synStockTransBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }

    }

    @Override
    public Response addSynStockTrans(SynStockTransDTO obj) {
        Long id = synStockTransBusinessImpl.save(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response deleteSynStockTrans(Long id) {
        SynStockTransDTO obj = (SynStockTransDTO) synStockTransBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            synStockTransBusinessImpl.delete(obj);
            return Response.ok(Response.Status.NO_CONTENT).build();
        }
    }

    //VietNT_20190116_start
    private Response buildErrorResponse(String message) {
        return Response.ok().entity(Collections.singletonMap("error", message)).build();
    }

    @Override
    public Response doSearch(SynStockTransDTO obj) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DOSERACH_DISTRIBUTE_PXK_A");
        objKpiLog.setDescription("Ghi log-Điều phối PXK A cấp");
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        DataListDTO dataListDTO = synStockTransBusinessImpl.doSearch(obj);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanBusinessImpl.addKpiLog(objKpiLog);
        return Response.ok(dataListDTO).build();
    }

    @Override
    public Response doForwardGroup(SynStockTransDTO dto) {
        KttsUserSession session = userRoleBusiness.getUserSession(request);
        Long sysUserId = session.getVpsUserInfo().getSysUserId();

        try {
            synStockTransBusinessImpl.doForwardGroup(dto, sysUserId);
            return Response.ok(Response.Status.OK).build();
        } catch (BusinessException e) {
            return this.buildErrorResponse(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return this.buildErrorResponse("Có lỗi xảy ra!");
        }
    }

    //VietNT_end
	//HuyPQ-start
    @Override
	public Response reportDetailAWaitReceive(SynStockTransDTO obj) {
    	DataListDTO data = synStockTransBusinessImpl.reportDetailAWaitReceive(obj);
		return Response.ok(data).build();
	}
	//Huy-end
    
    //Huypq-20190904-start
    @Override
    public Response doSearchAcceptManage(SynStockTransDTO obj) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DOSERACH_DISTRIBUTE_A_MANAGE");
        objKpiLog.setDescription("Ghi log-Quản lý điều phối PXK A cấp");
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        DataListDTO dataListDTO = synStockTransBusinessImpl.doSearchAcceptManage(obj, request);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanBusinessImpl.addKpiLog(objKpiLog);
        return Response.ok(dataListDTO).build();
    }
    
    @Override
	public Response exportFile(SynStockTransDTO obj) throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		try {
			String fileName = synStockTransBusinessImpl.exportFile(obj, filePath, request);
			return Response.ok(Collections.singletonMap("fileName", UEncrypt.encryptFileUploadPath(fileName))).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}
    //huy-end

	@Override
	public Response updateAcceptPXK(SynStockTransDTO obj) throws Exception {
		// TODO Auto-generated method stub
		return Response.ok(synStockTransBusinessImpl.updateAcceptPXK(obj, request)).build();
	}

	@Override
	public Response updateDenyPXK(SynStockTransDTO obj) throws Exception {
		// TODO Auto-generated method stub
		return Response.ok(synStockTransBusinessImpl.updateDenyPXK(obj, request)).build();
	}

	@Override
	public Response updateAssignPXK(SynStockTransDTO obj) throws Exception {
		// TODO Auto-generated method stub
		return Response.ok(synStockTransBusinessImpl.updateAssignPXK(obj, request)).build();
	}
	
	@Override
	public Response updateAcceptAssignPXK(SynStockTransDTO obj) throws Exception {
		// TODO Auto-generated method stub
		return Response.ok(synStockTransBusinessImpl.updateAcceptAssignPXK(obj, request)).build();
	}
	
	@Override
	public Response updateAcceptAssign(SynStockTransDetailDTO obj) throws Exception {
		// TODO Auto-generated method stub
		return Response.ok(synStockTransBusinessImpl.updateAcceptAssign(obj, request)).build();
	}

	@Override
	public Response getDataFilePXK(SynStockTransDetailDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(synStockTransBusinessImpl.getDataFilePXK(obj)).build();
	}
	
	@Override
	public Response checkRolePGD() {
		List<SysUserDTO> check = synStockTransBusinessImpl.checkRolePGD(request);
		return Response.ok(check).build();
	}
}
