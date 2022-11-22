package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.RpBTSDTO;
import com.viettel.coms.dto.StationConstructionDTO;

public interface StationConstructionRsService {
	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(StationConstructionDTO obj);
	
	@POST
	@Path("/exportExcel")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportExcel(StationConstructionDTO obj) throws Exception;

}
