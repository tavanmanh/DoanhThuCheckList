/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import com.viettel.coms.business.KpiLogBusinessImpl;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.service.base.dto.DataListDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;

/**
 * @author HungLQ9
 */
public class KpiLogRsServiceImpl implements KpiLogRsService {

    protected final Logger log = Logger.getLogger(KpiLogBusinessImpl.class);
    @Autowired
    KpiLogBusinessImpl kpiLogBusinessImpl;

    @Override
    public Response getKpiLog() {
        List<KpiLogDTO> ls = kpiLogBusinessImpl.getAll();
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
    public Response getKpiLogById(Long id) {
        KpiLogDTO obj = (KpiLogDTO) kpiLogBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(obj).build();
        }
    }

    @Override
    public Response updateKpiLog(KpiLogDTO obj) {
        Long id = kpiLogBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }

    }

    @Override
    public Response addKpiLog(KpiLogDTO obj) {
        Long id = kpiLogBusinessImpl.save(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response deleteKpiLog(Long id) {
        KpiLogDTO obj = (KpiLogDTO) kpiLogBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            kpiLogBusinessImpl.delete(obj);
            return Response.ok(Response.Status.NO_CONTENT).build();
        }
    }

	@Override
	public Response doSearch(KpiLogDTO obj) {
		DataListDTO ls = kpiLogBusinessImpl.doSearch(obj);
		return Response.ok(ls).build();
	}

	@Override
	public Response exportExcelDataLog(KpiLogDTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
            String strReturn = kpiLogBusinessImpl.exportExcelDataLog(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
	}
}
