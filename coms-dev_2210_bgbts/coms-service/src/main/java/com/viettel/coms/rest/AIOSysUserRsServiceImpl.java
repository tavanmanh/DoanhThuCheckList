package com.viettel.coms.rest;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.coms.business.AIOSysUserBusinessImpl;
import com.viettel.coms.dto.AIOSysGroupDTO;
import com.viettel.coms.dto.AIOSysUserDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.business.UserRoleBusinessImpl;

public class AIOSysUserRsServiceImpl implements AIOSysUserRsService {

    protected final Logger log = Logger.getLogger(AIOSysUserRsServiceImpl.class);

    @Autowired
    private AIOSysUserBusinessImpl sysUserBusiness;

    @Autowired
    private UserRoleBusinessImpl userRoleBusiness;

    @Context
    private HttpServletRequest request;

    @Override
    public Response doSearch(AIOSysUserDTO obj) {
        DataListDTO data = sysUserBusiness.doSearch(obj, request);
        return Response.ok(data).build();
    }
    
    @Override
	public Response saveRegisterCtv(AIOSysUserDTO obj) throws Exception{
		try {
			Long id = sysUserBusiness.saveRegisterCtv(obj);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}

	@Override
	public Response getSysGroupTree(AIOSysGroupDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(sysUserBusiness.getSysGroupTree(obj)).build();
	}
    
	@Override
	public Response getImageById(AIOSysUserDTO obj) {
		// TODO Auto-generated method stub
		try {
			return Response.ok(sysUserBusiness.getImageById(obj)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}
	
	@Override
	public Response updateRegisterCtv(AIOSysUserDTO obj) throws Exception{
		try {
			Long id = sysUserBusiness.updateRegisterCtv(obj);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}
    //Huy-end

	@Override
	public Response removeRecord(AIOSysUserDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(sysUserBusiness.removeRecord(obj.getSysUserId())).build();
	}

}
