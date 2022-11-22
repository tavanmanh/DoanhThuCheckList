package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.ManageUsedMaterialDTO;
import com.viettel.coms.dto.ManageVttbDTO;

/**
 * @author HoangNH38
 */
public interface ManageVttbRsService {

	// tatph - start 19/12/2019
	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(ManageVttbDTO obj);
	
	@POST
	@Path("/doSearchUsedMaterial")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchUsedMaterial(ManageVttbDTO obj);

	@POST
	@Path("/saveUsedMaterial")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveUsedMaterial(ManageUsedMaterialDTO obj);

	@POST
    @Path("/getExcelTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getExcelTemplate(ManageVttbDTO obj) throws Exception;
	
	
	// tatph - end 19/12/2019

}
