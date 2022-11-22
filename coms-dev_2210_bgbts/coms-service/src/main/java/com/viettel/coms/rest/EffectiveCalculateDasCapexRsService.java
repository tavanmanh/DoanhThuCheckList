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
import com.viettel.coms.dto.EffectiveCalculateDasCapexDTO;

public interface EffectiveCalculateDasCapexRsService {
	@POST
	@Path("/getAssumptionsCapex")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAssumptionsCapex(EffectiveCalculateDasCapexDTO obj);
	
    @POST
    @Path("/importDasCapex")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importDasCapex(Attachment attachments, @Context HttpServletRequest request) throws Exception;
}
