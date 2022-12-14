/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import com.viettel.coms.business.YearPlanDetailPerMonthBusinessImpl;
import com.viettel.coms.dto.YearPlanDetailPerMonthDTO;
import com.viettel.service.base.dto.DataListDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author HungLQ9
 */
public class YearPlanDetailPerMonthRsServiceImpl implements YearPlanDetailPerMonthRsService {

    protected final Logger log = Logger.getLogger(YearPlanDetailPerMonthRsServiceImpl.class);
    @Autowired
    YearPlanDetailPerMonthBusinessImpl yearPlanDetailPerMonthBusinessImpl;

    @Override
    public Response getYearPlanDetailPerMonth() {
        List<YearPlanDetailPerMonthDTO> ls = yearPlanDetailPerMonthBusinessImpl.getAll();
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
    public Response getYearPlanDetailPerMonthById(Long id) {
        YearPlanDetailPerMonthDTO obj = (YearPlanDetailPerMonthDTO) yearPlanDetailPerMonthBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(obj).build();
        }
    }

    @Override
    public Response updateYearPlanDetailPerMonth(YearPlanDetailPerMonthDTO obj) {
        Long id = yearPlanDetailPerMonthBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }

    }

    @Override
    public Response addYearPlanDetailPerMonth(YearPlanDetailPerMonthDTO obj) {
        Long id = yearPlanDetailPerMonthBusinessImpl.save(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response deleteYearPlanDetailPerMonth(Long id) {
        YearPlanDetailPerMonthDTO obj = (YearPlanDetailPerMonthDTO) yearPlanDetailPerMonthBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            yearPlanDetailPerMonthBusinessImpl.delete(obj);
            return Response.ok(Response.Status.NO_CONTENT).build();
        }
    }
}
