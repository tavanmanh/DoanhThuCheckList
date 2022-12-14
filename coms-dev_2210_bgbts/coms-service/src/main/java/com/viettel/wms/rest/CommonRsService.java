/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.wms.rest;

import com.viettel.coms.dto.CommonDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;

/**
 * @author HungLQ9
 */
public interface CommonRsService {

    @POST
    @Path("/genCode")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN})
    public Response genCode(CommonDTO obj);


    @POST
    @Path("/getCharFour")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getCharFour(CommonDTO obj);

    @POST
    @Path("/getCharThree")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getCharThree(CommonDTO obj);

    @POST
    @Path("/getCharOneAmount")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getCharOneAmount(CommonDTO obj);

    @POST
    @Path("/getCharOneTimes")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getCharOneTimes(CommonDTO obj);

    @POST
    @Path("/getCharTwoWeek")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getCharTwoWeek(CommonDTO obj);

    @POST
    @Path("/getCharTwoMonth")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getCharTwoMonth(CommonDTO obj) throws ParseException;

    @POST
    @Path("/exportExcelTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportExcelTemplate(String fileName) throws Exception;


    //phuc vu chuyen tab menu khi chuyen sang tomcat khac
    @POST
    @Path("/requestMenuCode")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response requestMenuCode(CommonDTO obj) throws Exception;
    //end

    @POST
    @Path("/exportExcelTemplate2")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportExcelTemplate2(String fileName) throws Exception;
}
