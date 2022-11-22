package com.viettel.coms.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.viettel.coms.dto.ContactUnitDTO;
import com.viettel.coms.dto.ManageAnnualTargetPlanDTO;
import com.viettel.coms.dto.ManageUsedMaterialDTO;
import com.viettel.coms.dto.ManageVttbDTO;

/**
 * @author HoangNH38
 */
public interface ManageAnnualTargetPlanRsService {

	// tatph - start 19/12/2019
	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(ManageAnnualTargetPlanDTO obj);
	
	@POST
	@Path("/getById")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getById(ManageAnnualTargetPlanDTO obj);
	
	@POST
	@Path("/save")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response add(ManageAnnualTargetPlanDTO obj);
	
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(ManageAnnualTargetPlanDTO obj);
	
	@POST
	@Path("/remove")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response remove(ManageAnnualTargetPlanDTO obj);
	

	@POST
    @Path("/getExcelTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getExcelTemplate(ManageAnnualTargetPlanDTO obj) throws Exception;
	
	@POST
	@Path("/importManageAnnualTargetPlan")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importManageAnnualTargetPlan(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	
	// tatph - end 19/12/2019

}
