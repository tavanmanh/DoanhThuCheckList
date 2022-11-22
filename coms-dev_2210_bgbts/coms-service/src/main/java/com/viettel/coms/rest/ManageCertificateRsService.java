package com.viettel.coms.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WoDTO;
//duonghv13 end 21092021
public interface ManageCertificateRsService {
	    @POST
	    @Path("/create")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response create(ManageCertificateDTO woDto,@Context HttpServletRequest request) throws Exception;

	    @POST
	    @Path("/update")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response update(ManageCertificateDTO woDto,@Context HttpServletRequest request) throws Exception;

	    @POST
	    @Path("/delete")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response delete(ManageCertificateDTO woDto) throws Exception;

	    @POST
	    @Path("/getById")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response getById(long woId) throws Exception;

	    @POST
	    @Path("/exportFile")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response exportFile(ManageCertificateDTO obj) throws Exception;

	    @POST
	    @Path("/getOneCertificateDetails")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response getOneCertificateDetails(ManageCertificateDTO woDto) throws Exception;

	    @POST
	    @Path("/doSearch")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response doSearch(ManageCertificateDTO woDto, @Context HttpServletRequest request) throws Exception;

	    @POST
	    @Path("/fileAttach/doSearch")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response doSearchMappingAttach(UtilAttachDocumentDTO obj) throws Exception;
	    
	    @POST
	    @Path("/checkRoleVHKTApprove")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response checkRoleVHKTApprove(@Context HttpServletRequest request);

	    @POST
	    @Path("/checkRoleCNKT")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response checkRoleCNKT(@Context HttpServletRequest request);
	    
	    @POST
	    @Path("/getCertificatePopup")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response getCertificatePopup(ManageCertificateDTO woDto);
	    //  duonghv13 end 21092021
}
