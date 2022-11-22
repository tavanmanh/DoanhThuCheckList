package com.viettel.coms.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;

public interface ManageDataOutsideOsRsService {

	@POST
    @Path("/doSearchOS")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchOS(ManageDataOutsideOsDTO obj);
	
	@POST
    @Path("/checkRoleCNKT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRoleCNKT();
	
	@POST
    @Path("/saveAddNew")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveAddNew(ManageDataOutsideOsDTO obj);
	
	@POST
    @Path("/saveUpdateNew")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveUpdateNew(ManageDataOutsideOsDTO obj);
	
	@POST
	@Path("/getAutoCompleteConstruction")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAutoCompleteConstruction(ManageDataOutsideOsDTO obj);
	
	@POST
	@Path("/setStatus")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response setStatus(ManageDataOutsideOsDTO obj);
	
	//tatph 13/11/2019
	@GET
	@Path("/downloadFile")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@Context HttpServletRequest request)
			throws Exception;
	@POST
	@Path("/importExpertiseProposal")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importExpertiseProposal(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importExpertiseProposalCDT")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importExpertiseProposalCDT(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importInvoice")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importInvoice(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importLiquidation")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importLiquidation(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	 @GET
	 @Path("/getExcelTemplateExpertiseProposal")
	 @Produces(MediaType.APPLICATION_OCTET_STREAM)
	 public Response getExcelTemplateExpertiseProposal(@Context HttpServletRequest request) throws Exception;
	 
	 @GET
	 @Path("/exportFile")
	 @Produces(MediaType.APPLICATION_OCTET_STREAM)
	 public Response exportFile(@Context HttpServletRequest request) throws Exception;
	 
	 @GET
	 @Path("/getExcelTemplateExpertiseProposalCDT")
	 @Produces(MediaType.APPLICATION_OCTET_STREAM)
	 public Response getExcelTemplateExpertiseProposalCDT(@Context HttpServletRequest request) throws Exception;
	 
	 @GET
	 @Path("/getExcelTemplateInvoice")
	 @Produces(MediaType.APPLICATION_OCTET_STREAM)
	 public Response getExcelTemplateInvoice(@Context HttpServletRequest request) throws Exception;
	 
	 @GET
	 @Path("/getExcelTemplateLiquidation")
	 @Produces(MediaType.APPLICATION_OCTET_STREAM)
	 public Response getExcelTemplateLiquidation(@Context HttpServletRequest request) throws Exception;
	 

	  @GET
	    @Path("/downloadFileTempATTT")
	    @Produces(MediaType.APPLICATION_OCTET_STREAM)
	    public Response downloadFileTempATTT(@Context HttpServletRequest request) throws Exception;
//	tatph-end

//Huypq-20191114
	@POST
    @Path("/exportTemplateLapTienDo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateLapTienDo(ManageDataOutsideOsDTO obj) throws Exception;
	
	@POST
    @Path("/exportTemplateDNQT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateDNQT(ManageDataOutsideOsDTO obj) throws Exception;
	
	@POST
    @Path("/exportTemplateQuyetToanNC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateQuyetToanNC(ManageDataOutsideOsDTO obj) throws Exception;
	
	@POST
    @Path("/importConsContract")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importConsContract(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
    @Path("/exportTemplateConsContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateConsContract(ManageDataOutsideOsDTO obj) throws Exception;
	
	@POST
    @Path("/importSchedule")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importSchedule(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
    @Path("/importSettlementProposal")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importSettlementProposal(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
    @Path("/importProposalLabor")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importProposalLabor(Attachment attachments, @Context HttpServletRequest request) throws Exception;
//Huy-end
}
