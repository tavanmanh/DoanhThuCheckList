/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import com.viettel.coms.business.SignVofficeBusinessImpl;
import com.viettel.coms.dto.CommonDTO;
import com.viettel.coms.dto.SignVofficeDTO;
import com.viettel.service.base.dto.DataListDTO;

import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author HungLQ9
 */
public class SignVofficeRsServiceImpl implements SignVofficeRsService {

	// protected final Logger log = Logger.getLogger(UserRsService.class);
	@Autowired
	SignVofficeBusinessImpl signVofficeBusinessImpl;

	@Override
	public Response getSignVoffice() {
		List<SignVofficeDTO> ls = signVofficeBusinessImpl.getAll();
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
	public Response getSignVofficeById(Long id) {
		SignVofficeDTO obj = (SignVofficeDTO) signVofficeBusinessImpl
				.getOneById(id);
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(obj).build();
		}
	}

	@Override
	public Response updateSignVoffice(SignVofficeDTO obj) {
		Long id = signVofficeBusinessImpl.update(obj);
		if (id == 0l) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok().build();
		}

	}

	@Override
	public Response addSignVoffice(SignVofficeDTO obj) {
		Long id = signVofficeBusinessImpl.save(obj);
		if (id == 0l) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}

	@Override
	public Response deleteSignVoffice(Long id) {
		SignVofficeDTO obj = (SignVofficeDTO) signVofficeBusinessImpl
				.getOneById(id);
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			signVofficeBusinessImpl.delete(obj);
			return Response.ok(Response.Status.NO_CONTENT).build();
		}
	}

	@Override
	public Response getRoleByEmail(CommonDTO obj) {
		/*
		 * List<SignVofficeDTO> ls = Lists.newArrayList(); SignVofficeDTO dto =
		 * new SignVofficeDTO(); SignVofficeDTO dto1 = new SignVofficeDTO();
		 * SignVofficeDTO dto2 = new SignVofficeDTO(); dto.setAdOrgId(10000l);
		 * dto.setSysRoleId(1l); dto.setSysRoleName("GD"); ls.add(dto);
		 * 
		 * dto1.setAdOrgId(10000l); dto1.setSysRoleId(2l);
		 * dto1.setSysRoleName("NV"); ls.add(dto1);
		 * 
		 * dto2.setAdOrgId(10000l); dto2.setSysRoleId(3l);
		 * dto2.setSysRoleName("BV"); ls.add(dto2); // TODO Auto-generated
		 * method stub return Response.ok(ls).build();
		 */
		List<String> lstEmail = new ArrayList<String>();
		if (obj.getKeySearch() != null) {
			lstEmail.add(obj.getKeySearch());
		}
		return Response.ok(signVofficeBusinessImpl.getRoleByEmail(lstEmail))
				.build();
	}

	@Override
	public Response getDataSign(CommonDTO obj) {

		try {
			return Response.ok(
					signVofficeBusinessImpl.getdataSign(obj.getListId(),
							obj.getType(), obj.getReportName())).build();
		} catch (IllegalArgumentException e) {
			return Response.ok()
					.entity(Collections.singletonMap("error", e.getMessage()))
					.build();
		}
	}

	@Override
	public Response test(Long id) throws Exception {
		signVofficeBusinessImpl.updateOderChange(id);
		return null;
	}
	
	//HuyPQ-start
	@Override
	public Response getDataSignYCVT(CommonDTO obj) {

		try {
			return Response.ok(
					signVofficeBusinessImpl.getdataSignYCVT(obj.getListId(),
							obj.getType(), obj.getReportName())).build();
		} catch (IllegalArgumentException e) {
			return Response.ok()
					.entity(Collections.singletonMap("error", e.getMessage()))
					.build();
		}
	}
	//Huypq-end
}
