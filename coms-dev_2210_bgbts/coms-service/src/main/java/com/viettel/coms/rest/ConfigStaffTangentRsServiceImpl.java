package com.viettel.coms.rest;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.business.ConfigStaffTangentBusinessImpl;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.service.base.dto.DataListDTO;

public class ConfigStaffTangentRsServiceImpl implements ConfigStaffTangentRsService{

	@Context
	HttpServletRequest request;
	
	@Autowired
	ConfigStaffTangentBusinessImpl configStaffTangentBusinessImpl;
	
	@Override
	public Response doSearch(ConfigStaffTangentDTO obj) {
		DataListDTO data = configStaffTangentBusinessImpl.doSearch(obj);
		return Response.ok(data).build();
	}
	
	@Override
	public Response doSearchProvinceInPopup(CatProvinceDTO obj) {
		return Response.ok(configStaffTangentBusinessImpl.doSearchProvinceInPopup(obj, request)).build();
	}
	
	@Override
	public Response doSearchStaffByPopup(ConfigStaffTangentDTO obj) {
		return Response.ok(configStaffTangentBusinessImpl.doSearchStaffByPopup(obj, request)).build();
	}

	@Override
	public Response saveConfig(ConfigStaffTangentDTO obj) throws Exception{
		try {
			Long id = configStaffTangentBusinessImpl.saveConfig(obj, request);
			if(id==0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.status(Response.Status.CREATED).build();
			}
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response updateConfig(ConfigStaffTangentDTO obj) {
		Long id = configStaffTangentBusinessImpl.updateConfig(obj, request);
		if(id==0l) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.status(Response.Status.CREATED).build();
		}
	}
	
	@Override
	public Response doSearchProvinceInPopupByRole(CatProvinceDTO obj) {
		return Response.ok(configStaffTangentBusinessImpl.doSearchProvinceInPopupByRole(obj, request)).build();
	}

	@Override
	public Response doSearchStaffByConfigProvinceId(ConfigStaffTangentDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(configStaffTangentBusinessImpl.doSearchStaffByConfigProvinceId(obj)).build();
	}
	
	@Override
	public Response removeConfig(ConfigStaffTangentDTO obj) throws Exception {
		try {
			Long id = configStaffTangentBusinessImpl.removeConfig(obj, request);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.status(Response.Status.CREATED).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}
}
