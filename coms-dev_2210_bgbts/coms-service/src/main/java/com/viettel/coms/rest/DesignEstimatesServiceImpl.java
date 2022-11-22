package com.viettel.coms.rest;

import java.text.ParseException;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.coms.business.DesignEstimatesBusinessImpl;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.DesignEstimatesDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.erp.dto.CatProvincesDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.service.base.dto.DataListDTO;

public class DesignEstimatesServiceImpl implements DesignEstimatesService {

	@Context
	HttpServletRequest request;
	
	static Logger LOGGER = LoggerFactory.getLogger(DesignEstimatesServiceImpl.class);

	@Autowired
	DesignEstimatesBusinessImpl designEstimatesBusinessImpl;

	@Value("${folder_upload2}")
	private String folderUpload;
	
	@Override
	public Response doSearch(DesignEstimatesDTO obj) throws ParseException {
		
		try {
			DataListDTO data;
			data = designEstimatesBusinessImpl.doSearch(obj, request);
			return Response.ok(data).build();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Response save(DesignEstimatesDTO obj) throws Exception {
		try {	
			Long id = designEstimatesBusinessImpl.save(obj, request);
			if (id == 0) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else 
				return Response.ok(Response.Status.CREATED).build();
		}catch (BusinessException e) {
			//e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}catch (Exception e) {
			//e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}

	@Override
	public Response update(DesignEstimatesDTO obj) throws Exception {
		try {
			Long id = designEstimatesBusinessImpl.update(obj, request);
			if (id == 0) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}
	
	@Override
	public Response delete(DesignEstimatesDTO obj) throws Exception {
		try {
			Long id = designEstimatesBusinessImpl.delete(obj, request);
			if (id == 0) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}
	
	@Override
	public Response checkRoleUserAssignYctx() {
		Long result = 0l;
		Boolean check = designEstimatesBusinessImpl.checkRoleUserAssignYctx(request);
		if(check) {
			result = 1l;
		}
		return Response.ok(result).build();
	}
	
	@Override
    public Response doSearchArea(CatProvincesDTO obj) {
        return Response
                .ok(designEstimatesBusinessImpl.doSearchArea(obj))
                .build();
    }
	
	@Override
    public Response doSearchUser(SysUserDTO obj) {
        return Response
                .ok(designEstimatesBusinessImpl.doSearchUser(obj))
                .build();
    }
	
	@Override
    public Response doSearchStationVCC(CatStationDTO obj) {
        return Response
                .ok(designEstimatesBusinessImpl.doSearchStationVCC(obj))
                .build();
    }
	
	@Override
    public Response doSearchStationVTNET(CatStationDTO obj) {
        return Response
                .ok(designEstimatesBusinessImpl.doSearchStationVTNET(obj))
                .build();
    }

	@Override
	public Response exportFile(DesignEstimatesDTO obj) {
		try {
			String strReturn = designEstimatesBusinessImpl.exportFile(obj, request);
			return Response.ok(strReturn).build();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Response doSearchFile(UtilAttachDocumentDTO obj) {
		// TODO Auto-generated method stub
		  try {
			return Response
			            .ok(designEstimatesBusinessImpl.doSearchFile(obj))
			            .build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void deleteFile(UtilAttachDocumentDTO obj) {
		// TODO Auto-generated method stub
			            designEstimatesBusinessImpl.deleteFile(obj);
	}
	
	@Override
	public Response getFile(String code) {
		// TODO Auto-generated method stub
		  try {
			return Response
			            .ok(designEstimatesBusinessImpl.getFile(code))
			            .build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
