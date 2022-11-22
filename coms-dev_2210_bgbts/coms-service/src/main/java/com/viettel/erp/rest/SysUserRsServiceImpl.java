/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.erp.rest;

import com.viettel.erp.business.SysUserBusinessImpl;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.service.base.dto.DataListDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author HungLQ9
 */
public class SysUserRsServiceImpl implements SysUserRsService {

    @Autowired
    SysUserBusinessImpl sysUserBusinessImpl;

    @Override
    public Response getSysUser() {
        List<SysUserDTO> ls = sysUserBusinessImpl.getAll();
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
    public Response getSysUserById(Long id) {
        SysUserDTO obj = (SysUserDTO) sysUserBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(obj).build();
        }
    }

    @Override
    public Response updateSysUser(SysUserDTO obj) {
        Long id = sysUserBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }

    }

    @Override
    public Response addSysUser(SysUserDTO obj) {
        Long id = sysUserBusinessImpl.save(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response deleteSysUser(Long id) {
        SysUserDTO obj = (SysUserDTO) sysUserBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            sysUserBusinessImpl.delete(obj);
            return Response.ok(Response.Status.NO_CONTENT).build();
        }
    }

    @Override
    public Response getForAutoComplete(SysUserDTO query) {
        List<SysUserDTO> list = sysUserBusinessImpl.getForAutoComplete(query);
        if (query.getIsSize()) {
            SysUserDTO moreObject = new SysUserDTO();
            moreObject.setUserId(0l);
//			moreObject.setPartnerName(0l);
            moreObject.setLoginName("Search more");

            list.add(moreObject);
        }
        return Response.ok(list).build();
    }
}
