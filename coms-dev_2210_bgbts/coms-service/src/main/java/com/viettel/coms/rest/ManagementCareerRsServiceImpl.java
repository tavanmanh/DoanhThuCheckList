package com.viettel.coms.rest;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.coms.business.ManagementCareerBusinessImpl;
import com.viettel.coms.business.ManagementCertificateBusinessImpl;
import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.erp.dto.DataListDTO;
import com.viettel.ktts2.dto.KttsUserSession;

//Duonghv13-start 16092021
public class ManagementCareerRsServiceImpl implements ManagementCareerRsService {

	protected final Logger log = Logger.getLogger(ManagementCareerRsService.class);
	@Context
	HttpServletRequest request;

	@Autowired
	ManagementCareerBusinessImpl managementCareerBusinessImpl;
	@Autowired
	ManagementCertificateBusinessImpl managementCertificateBusinessImpl;

	@Override
	public Response doSearch(ManageCareerDTO obj) {
		// TODO Auto-generated method stub
		List<ManageCareerDTO> ls = managementCareerBusinessImpl.doSearch(obj);
		if (ls == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			DataListDTO data = new DataListDTO();
			data.setData(ls);
			data.setTotal(obj.getTotalRecord());
			data.setSize(obj.getPageSize());
			data.setStart(1);
			return Response.ok(data).build();
		}
	}

	@Override
	public Response remove(ManageCareerDTO obj) {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			
			ManageCertificateDTO one = new ManageCertificateDTO();
	        one.setCareerId(obj.getCareerId());
	        one.setPage(1l);
	        one.setPageSize(10); 
	        List<ManageCertificateDTO> listSearch = managementCertificateBusinessImpl.doSearch(one);
	        if(listSearch.size() == 0) {
	        	obj.setUpdatedDate(new Date());
				obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
				obj.setStatus("0");
				Long id = managementCareerBusinessImpl.updateCareer(obj);
				if (id == 0l) {
					return Response.status(Response.Status.BAD_REQUEST).build();
				} else {
					return Response.ok().build();
				}
	        }else {
	        	throw new IllegalArgumentException("Không được xóa .Ngành nghề đã được gán vào chứng chỉ của nhân viên!");
	        }
			
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response add(ManageCareerDTO obj) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			obj.setCreatedDate(new Date());
			String s = objUser.getSysUserId().toString();
			obj.setCreatedUserId(Long.parseLong(s));
			obj.setStatus("1");
			Long id = managementCareerBusinessImpl.createCareer(obj);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response update(ManageCareerDTO obj) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			obj.setUpdatedDate(new Date());
			obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
			obj.setStatus("1");
			Long id = managementCareerBusinessImpl.updateCareer(obj);

			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response getById(Long id) throws Exception {
		// TODO Auto-generated method stub
		ManageCareerDTO obj = (ManageCareerDTO) managementCareerBusinessImpl.getById(id);
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(obj).build();
		}
	}

	@Override
	public Response exportCareer(ManageCareerDTO obj) throws Exception {
		String strReturn = managementCareerBusinessImpl.exportCareer(obj);
		return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
	}

	@Override
	public Response getCareerPopup(ManageCareerDTO obj) throws Exception{
		// TODO Auto-generated method stub
		return Response.ok(managementCareerBusinessImpl.getForAutoCompleteInSign(obj)).build();
	}

	
	// Duonghv13 end-16/09/2021//
}

