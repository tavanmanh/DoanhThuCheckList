/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.erp.rest;

import com.viettel.erp.business.DistanceUnloadListBusinessImpl;
import com.viettel.erp.dto.DistanceUnloadListDTO;
import com.viettel.service.base.dto.DataListDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author HungLQ9
 */
public class DistanceUnloadListRsServiceImpl implements DistanceUnloadListRsService {

    @Autowired
    DistanceUnloadListBusinessImpl distanceUnloadListBusinessImpl;

    @Override
    public Response getDistanceUnloadList() {
        List<DistanceUnloadListDTO> ls = distanceUnloadListBusinessImpl.getAll();
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
    public Response getDistanceUnloadListById(Long id) {
        DistanceUnloadListDTO obj = (DistanceUnloadListDTO) distanceUnloadListBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(obj).build();
        }
    }

    @Override
    public Response updateDistanceUnloadList(DistanceUnloadListDTO obj) {
        Long id = distanceUnloadListBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }

    }

    @Override
    public Response addDistanceUnloadList(DistanceUnloadListDTO obj) {
        Long id = distanceUnloadListBusinessImpl.save(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response deleteDistanceUnloadList(Long id) {
        DistanceUnloadListDTO obj = (DistanceUnloadListDTO) distanceUnloadListBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            distanceUnloadListBusinessImpl.delete(obj);
            return Response.ok(Response.Status.NO_CONTENT).build();
        }
    }

    @Override
    public Response doSearchByDisUnloadConsMinId(Long disUnloadConsMinId) {
        List<DistanceUnloadListDTO> ls = distanceUnloadListBusinessImpl.doSearchByDisUnloadConsMinId(disUnloadConsMinId);
        if (ls == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(ls).build();
        }
    }
}
