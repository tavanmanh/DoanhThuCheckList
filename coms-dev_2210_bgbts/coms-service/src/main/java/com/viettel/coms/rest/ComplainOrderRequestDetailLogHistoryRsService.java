package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;

public interface ComplainOrderRequestDetailLogHistoryRsService {
	@POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response doSearch(ComplainOrderRequestDetailLogHistoryDTO obj);
	
//	@POST
//    @Path("/add")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    Response add(ComplainOrderRequestDetailLogHistoryDTO obj) throws Exception;

}
