package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.YearPlanDTO;
import com.viettel.coms.dto.YearPlanOSDTO;
import com.viettel.coms.dto.YearPlanSimpleOSDTO;

/**
 * @author HoangNH38
 */
public interface YearPlanOSRsService {

    @POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearch(YearPlanSimpleOSDTO obj);

    @POST
    @Path("/remove")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response remove(YearPlanOSDTO obj);

    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response add(YearPlanSimpleOSDTO obj) throws Exception;

    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response update(YearPlanSimpleOSDTO obj) throws Exception;

    @POST
    @Path("/getById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getById(Long id) throws Exception;

    @POST
    @Path("/getSequence")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSequence() throws Exception;

    @POST
    @Path("/exportExcelTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportExcelTemplate() throws Exception;

    @POST
    @Path("/exportYearPlan")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportYearPlan(YearPlanSimpleOSDTO obj) throws Exception;

    @POST
    @Path("/getAppParamByType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAppParamByType(String type) throws Exception;
    
    @POST
    @Path("/updateRegistry")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateRegistry(YearPlanOSDTO obj);

}
