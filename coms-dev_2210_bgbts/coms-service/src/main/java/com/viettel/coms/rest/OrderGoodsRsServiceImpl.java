/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import com.viettel.coms.business.OrderGoodsBusinessImpl;
import com.viettel.coms.dto.OrderGoodsDTO;
import com.viettel.service.base.dto.DataListDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author HungLQ9
 */
public class OrderGoodsRsServiceImpl implements OrderGoodsRsService {

    protected final Logger log = Logger.getLogger(OrderGoodsRsServiceImpl.class);
    @Autowired
    OrderGoodsBusinessImpl orderGoodsBusinessImpl;

    @Override
    public Response getOrderGoods() {
        List<OrderGoodsDTO> ls = orderGoodsBusinessImpl.getAll();
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
    public Response getOrderGoodsById(Long id) {
        OrderGoodsDTO obj = (OrderGoodsDTO) orderGoodsBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(obj).build();
        }
    }

    @Override
    public Response updateOrderGoods(OrderGoodsDTO obj) {
        Long id = orderGoodsBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }

    }

    @Override
    public Response addOrderGoods(OrderGoodsDTO obj) {
        Long id = orderGoodsBusinessImpl.save(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response deleteOrderGoods(Long id) {
        OrderGoodsDTO obj = (OrderGoodsDTO) orderGoodsBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            orderGoodsBusinessImpl.delete(obj);
            return Response.ok(Response.Status.NO_CONTENT).build();
        }
    }
}
