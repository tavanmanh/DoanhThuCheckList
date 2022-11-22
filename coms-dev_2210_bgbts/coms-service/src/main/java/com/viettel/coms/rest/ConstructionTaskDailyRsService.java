/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.viettel.coms.dto.ConstructionTaskDailyDTO;
import com.viettel.coms.dto.WorkItemDTO;

/**
 * @author HungLQ9
 */
public interface ConstructionTaskDailyRsService {

    @GET
    @Path("/constructionTaskDaily")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getConstructionTaskDaily();

    @GET
    @Path("/constructionTaskDaily/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getConstructionTaskDailyById(@PathParam("id") Long id);

    @PUT
    @Path("/constructionTaskDaily/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateConstructionTaskDaily(ConstructionTaskDailyDTO obj);

    @POST
    @Path("/constructionTaskDaily/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addConstructionTaskDaily(ConstructionTaskDailyDTO obj);

    @DELETE
    @Path("/constructionTaskDaily/{id}/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteConstructionTaskDaily(@PathParam("id") Long id);
    
    /**Hoangnh start 18022019**/
    @POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearch(ConstructionTaskDailyDTO obj);
    
    @POST
    @Path("/getListImage")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getListImage(ConstructionTaskDailyDTO obj) throws Exception;
    
    @POST
    @Path("/rejectQuantityByDay")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response rejectQuantityByDay(ConstructionTaskDailyDTO obj) throws Exception;
    
    @POST
    @Path("/cancelApproveQuantityByDay")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response cancelApproveQuantityByDay(ConstructionTaskDailyDTO obj);
    
    @POST
    @Path("/approveQuantityDayChecked")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response approveQuantityDayChecked(List<ConstructionTaskDailyDTO> lstObj);
    
    @POST
    @Path("/approveQuantityByDay")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response approveQuantityByDay(ConstructionTaskDailyDTO obj);
    
    @POST
    @Path("/exportConstructionTaskDaily")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response exportConstructionTaskDaily(ConstructionTaskDailyDTO obj) throws Exception;
    /**Hoangnh end 18022019**/
    
    //HuyPq-20191009-start
    @POST
    @Path("/doSearchCompleteManage")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response doSearchCompleteManage(WorkItemDTO obj);
    
    @POST
    @Path("/importCompleteWorkItem")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importCompleteWorkItem(Attachment attachments, @Context HttpServletRequest request)
            throws Exception;
    //Huy-end
}
