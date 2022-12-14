/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import com.viettel.cat.dto.CatPartnerDTO;
import com.viettel.coms.business.DepartmentBusinessImpl;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.service.base.dto.DataListDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author HungLQ9
 */
public class DepartmentRsServiceImpl implements DepartmentRsService {

    // protected final Logger log = Logger.getLogger(UserRsService.class);
    @Autowired
    DepartmentBusinessImpl departmentBusinessImpl;
    
    @Context
    HttpServletRequest request;

    @Override
    public Response getDepartment() {
        List<DepartmentDTO> ls = departmentBusinessImpl.getAll();
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
    public Response getDepartmentById(Long id) {
        DepartmentDTO obj = (DepartmentDTO) departmentBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(obj).build();
        }
    }

    @Override
    public Response updateDepartment(DepartmentDTO obj) {
        Long id = departmentBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }

    }

    @Override
    public Response addDepartment(DepartmentDTO obj) {
        Long id = departmentBusinessImpl.save(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response deleteDepartment(Long id) {
        DepartmentDTO obj = (DepartmentDTO) departmentBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            departmentBusinessImpl.delete(obj);
            return Response.ok(Response.Status.NO_CONTENT).build();
        }
    }

    @Override
    public Response getall(DepartmentDTO obj) {
        return Response.ok(departmentBusinessImpl.getall(obj)).build();
    }

    @Override
    public Response getForAutocompleteDept(DepartmentDTO obj) {
        return Response.ok(departmentBusinessImpl.getDeptForAutocomplete(obj)).build();
    }

    @Override
    public Response getAutocompleteLanHan(CatPartnerDTO obj) {
        return Response.ok(departmentBusinessImpl.getAutocompleteLanHan(obj)).build();
    }

    @Override
    public Response getCatPartnerForAutocompleteDept(DepartmentDTO obj) {
        // TODO Auto-generated method stub
        return Response.ok(departmentBusinessImpl.getCatPartnerForAutocompleteDept(obj)).build();
    }

    @Override
    public Response doSearchCatPartner(DepartmentDTO obj) {
        // TODO Auto-generated method stub
        DataListDTO data = departmentBusinessImpl.doSearchCatPartner(obj);
        return Response.ok(data).build();
    }
    //HuyPQ-start
	@Override
	public Response getForAutoCompleteDeptCheck(DepartmentDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(departmentBusinessImpl.getForAutoCompleteDeptCheck(obj,request)).build();
	}
	
	@Override
    public Response getSysGroupCheck(DepartmentDTO obj) {
        return Response.ok(departmentBusinessImpl.getSysGroupCheck(obj,request)).build();
    }
    
    @Override
    public Response getSysGroupCheckTTKTDV(DepartmentDTO obj) {
        return Response.ok(departmentBusinessImpl.getSysGroupCheckTTKTDV(obj,request)).build();
    }
    
    @Override
    public Response getSysGroupCheckTTKT(DepartmentDTO obj) {
        return Response.ok(departmentBusinessImpl.getSysGroupCheckTTKT(obj,request)).build();
    }
    
    @Override
    public Response getForAutoCompleteTTKV(DepartmentDTO obj) {
        return Response.ok(departmentBusinessImpl.getForAutoCompleteTTKV(obj)).build();
    }
    
    @Override
    public Response getallTTKV(DepartmentDTO obj) {
        return Response.ok(departmentBusinessImpl.getallTTKV(obj)).build();
    }
	//Huy-end
    
    @Override
    public Response getForAutoCompleteDeptByDomain(DepartmentDTO obj) {
        return Response.ok(departmentBusinessImpl.getForAutoCompleteDeptByDomain(obj, request)).build();
    }
    
    @Override
    public Response getForAutoCompleteCnkt(DepartmentDTO obj) {
        return Response.ok(departmentBusinessImpl.getForAutoCompleteCnkt(obj)).build();
    }
}
