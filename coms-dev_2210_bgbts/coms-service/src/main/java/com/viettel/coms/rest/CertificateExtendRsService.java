package com.viettel.coms.rest;

import javax.servlet.http.HttpServletRequest;
//Duonghv13 start-22/09/2021//
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;

public interface CertificateExtendRsService {
	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(CertificateExtendDTO obj);

	@POST
	@Path("/remove")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response remove(CertificateExtendDTO obj);

	
	@POST
	@Path("/add")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response add(CertificateExtendDTO obj) throws Exception;

	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(CertificateExtendDTO obj) throws Exception;
	
	@POST
	@Path("/accept")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response accept(CertificateExtendDTO obj) throws Exception;
	
	@POST
	@Path("/reject")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response reject(CertificateExtendDTO obj) throws Exception;
	
	@GET
	@Path("/getById")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getById(@QueryParam("id")Long id) throws Exception;
	
	@POST
    @Path("/fileAttach/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchMappingAttach(UtilAttachDocumentDTO obj) throws Exception;
	
	@POST
    @Path("/getResultFileByExtendId")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getResultFileByExtendId(CertificateExtendDTO obj);

	//Duonghv13 end-22/09/2021//
}
