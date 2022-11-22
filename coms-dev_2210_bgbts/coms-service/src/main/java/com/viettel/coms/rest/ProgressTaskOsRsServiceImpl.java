package com.viettel.coms.rest;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.coms.business.ProgressTaskOsBusinessImpl;
import com.viettel.coms.dto.ProgressTaskOsDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.service.base.dto.DataListDTO;

/**
 * @author hailh10
 */

public class ProgressTaskOsRsServiceImpl implements ProgressTaskOsRsService {

	protected final Logger log = Logger.getLogger(ProgressTaskOsRsService.class);
	@Autowired
	ProgressTaskOsBusinessImpl progressTaskOsBusinessImpl;

	@Context
	HttpServletRequest request;
	
	@Override
	public Response doSearch(ProgressTaskOsDTO obj) {
		DataListDTO ls = progressTaskOsBusinessImpl.doSearch(obj, request);
		return Response.ok(ls).build();
	}

	@Override
	public Response getById(ProgressTaskOsDTO obj) {
		DataListDTO ls = progressTaskOsBusinessImpl.getById(obj);
		return Response.ok(ls).build();
	}

	@Override
	public Response saveAdd(ProgressTaskOsDTO obj) throws Exception{
		try {
			Long id = progressTaskOsBusinessImpl.saveAdd(obj, request);
			return Response.ok(Response.Status.CREATED).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
}
	}

	@Override
	public Response getDataTaskByProvince(ProgressTaskOsDTO obj) {
		DataListDTO ls = progressTaskOsBusinessImpl.getDataTaskByProvince(obj);
		return Response.ok(ls).build();
	}
	
	@Override
	public Response doSearchMain(ProgressTaskOsDTO obj) {
		DataListDTO ls = progressTaskOsBusinessImpl.doSearchMain(obj, request);
		return Response.ok(ls).build();
	}

	@Override
	public Response deleteRecord(ProgressTaskOsDTO obj) {
		Long id = progressTaskOsBusinessImpl.deleteRecord(obj, request);
		return Response.ok(id).build();
	}
	
	@Override
	public Response saveUpdate(ProgressTaskOsDTO obj) {
		Long id = progressTaskOsBusinessImpl.saveUpdate(obj, request);
		if(id==0l) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}
	
	@Override
    public Response exportCompleteProgress(ProgressTaskOsDTO obj) throws Exception {
        try {
            String strReturn = progressTaskOsBusinessImpl.exportCompleteProgress(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

	@Override
	public Response updateProgressTaskOs(ProgressTaskOsDTO obj) {
		Long ids = progressTaskOsBusinessImpl.updateProgressTaskOs(obj, request);
		if(ids==0l) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}
	
	@Override
	public Response doSearchBaoCaoTienDoOs(ProgressTaskOsDTO obj) {
		DataListDTO ls = progressTaskOsBusinessImpl.doSearchBaoCaoTienDoOs(obj, request);
		return Response.ok(ls).build();
	}
	
	@Override
    public Response exportFileBaoCaoKHOs(ProgressTaskOsDTO obj) throws Exception {
        try {
            String strReturn = progressTaskOsBusinessImpl.exportFileBaoCaoKHOs(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
	public Response doSearchBaoCaoChamDiemKpi(ProgressTaskOsDTO obj) {
		DataListDTO ls = progressTaskOsBusinessImpl.doSearchBaoCaoChamDiemKpi(obj, request);
		return Response.ok(ls).build();
	}
	
	@Override
    public Response exportFileBaoCaoChamDiemKpi(ProgressTaskOsDTO obj) throws Exception {
        try {
            String strReturn = progressTaskOsBusinessImpl.exportFileBaoCaoChamDiemKpi(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
}
