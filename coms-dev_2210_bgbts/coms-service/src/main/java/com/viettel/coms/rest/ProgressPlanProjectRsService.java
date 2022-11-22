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

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.dto.ProgressPlanProjectDTO;
import com.viettel.coms.dto.SynStockTransDTO;

public interface ProgressPlanProjectRsService {

	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(ProgressPlanProjectDTO obj);
	
	@POST
    @Path("/catProvince/doSearchProvinceInPopup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchProvinceInPopup(CatProvinceDTO obj);
	
	@POST
    @Path("/getCntContractInHtct")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCntContractInHtct(ProgressPlanProjectDTO obj);
	
	@POST
    @Path("/saveProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveProject(ProgressPlanProjectDTO obj) throws Exception;
	
	@POST
    @Path("/updateProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateProject(ProgressPlanProjectDTO obj) throws Exception;
	
	@POST
    @Path("/importProject")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importProject(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
    @Path("/saveImportProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveImportProject(ProgressPlanProjectDTO obj) throws Exception;
	
	@POST
    @Path("/checkDomainUser")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkDomainUser();
	
	@POST
    @Path("/exportExcelProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportExcelProject(ProgressPlanProjectDTO obj) throws Exception;
	
	@POST
    @Path("/getListFile")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListFile(Long objId);
}
