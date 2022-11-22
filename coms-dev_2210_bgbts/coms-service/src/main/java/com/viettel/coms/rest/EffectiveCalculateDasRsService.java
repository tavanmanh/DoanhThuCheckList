package com.viettel.coms.rest;
import com.viettel.coms.dto.EffectiveCalculateDasDTO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

public interface EffectiveCalculateDasRsService {
	
	@POST
	@Path("/getAssumptions")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAssumptions(EffectiveCalculateDasDTO obj);
	
    @POST
    @Path("/importDas")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importDas(Attachment attachments, @Context HttpServletRequest request) throws Exception;

}
