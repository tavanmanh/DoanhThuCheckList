/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import com.viettel.cat.dto.CatPartnerDTO;
import com.viettel.coms.dto.DepartmentDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author HungLQ9
 */
public interface DepartmentRsService {

    @GET
    @Path("/department")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDepartment();

    @GET
    @Path("/department/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDepartmentById(@PathParam("id") Long id);

    @PUT
    @Path("/department/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateDepartment(DepartmentDTO obj);

    @POST
    @Path("/department/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addDepartment(DepartmentDTO obj);

    @DELETE
    @Path("/department/{id}/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteDepartment(@PathParam("id") Long id);

    @POST
    @Path("/department/getall")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getall(DepartmentDTO obj);

    @POST
    @Path("/department/getForAutocompleteDept")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getForAutocompleteDept(DepartmentDTO obj);

    @POST
    @Path("/department/getAutocompleteLanHan")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAutocompleteLanHan(CatPartnerDTO obj);

    @POST
    @Path("/catPartner/getForAutocompleteDept")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCatPartnerForAutocompleteDept(DepartmentDTO obj);

    @POST
    @Path("/catPartner/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchCatPartner(DepartmentDTO obj);
    
    //HuyPq-start
    @POST
    @Path("/department/getForAutoCompleteDeptCheck")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getForAutoCompleteDeptCheck(DepartmentDTO obj);
    
    @POST
    @Path("/department/getSysGroupCheck")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSysGroupCheck(DepartmentDTO obj);
    
    @POST
    @Path("/department/getSysGroupCheckTTKTDV")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSysGroupCheckTTKTDV(DepartmentDTO obj);
    
    @POST
    @Path("/department/getSysGroupCheckTTKT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSysGroupCheckTTKT(DepartmentDTO obj);
    
    @POST
    @Path("/department/getForAutoCompleteTTKV")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getForAutoCompleteTTKV(DepartmentDTO obj);
    
    @POST
    @Path("/department/getallTTKV")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getallTTKV(DepartmentDTO obj);
    //Huypq-end
    
    @POST
    @Path("/department/getForAutoCompleteDeptByDomain")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getForAutoCompleteDeptByDomain(DepartmentDTO obj);
    
    @POST
    @Path("/department/getForAutoCompleteCnkt")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getForAutoCompleteCnkt(DepartmentDTO obj);
}
