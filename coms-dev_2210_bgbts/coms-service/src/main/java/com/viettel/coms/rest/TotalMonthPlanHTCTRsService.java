package com.viettel.coms.rest;
import javax.servlet.http.HttpServletRequest;
//Duonghv13 start-16/08/2021//
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TotalMonthPlanHTCTDTO;
import com.viettel.coms.dto.TotalMonthPlanOSSimpleDTO;

public interface TotalMonthPlanHTCTRsService {
	@POST
	@Path("/monthReport/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(TotalMonthPlanHTCTDTO obj);

	@POST
	@Path("/monthReport/remove")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response remove(TotalMonthPlanHTCTDTO obj);

	
	@POST
	@Path("/monthReport/add")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response add(TotalMonthPlanHTCTDTO obj) throws Exception;

	@POST
	@Path("/monthReport/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(TotalMonthPlanHTCTDTO obj) throws Exception;
	

	@GET
	@Path("/monthReport/getById")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getById(@QueryParam("id")Long id) throws Exception;

	@POST
	@Path("/monthReport/exportExcelTemplate")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response exportExcelTemplate() throws Exception;
	
	@POST
    @Path("/monthReport/importReport")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importReport(Attachment attachments) throws Exception;
	
	 @POST
	 @Path("/monthReport/exportMonthPlan")
	 @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	 @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	 public Response exportMonthPlan(TotalMonthPlanHTCTDTO obj) throws Exception;
	 
	 @POST
	 @Path("/monthReport/exportPDFReport")
	 @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	 @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	 public Response exportPDFReport(TotalMonthPlanHTCTDTO obj) throws Exception;
	 
	 @GET
	 @Path("/monthReport/downloadFileTempATTT")
	 @Produces(MediaType.APPLICATION_OCTET_STREAM)
	 public Response downloadFileTempATTT(@Context HttpServletRequest request) throws Exception;
	 
	//Duonghv13 end-16/08/2021//
	 
	//Duonghv13-start 17092021
	 @POST
	 @Path("/monthReport/checkRoleCreateMonthPlan")
	 @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	 @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	 public Response checkRoleCreateMonthPlan(@Context HttpServletRequest request) throws Exception;
	 
	//Duong end
	 
	//Duonghv13-start 17092021
	 @POST
	 @Path("/monthReport/getAllStationVCCHTCT")
     @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	 @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
     public Response getAllStationVCCHTCT(TotalMonthPlanHTCTDTO obj) ;
     //Duong end
}
