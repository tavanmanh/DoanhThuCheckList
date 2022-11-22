package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.SettlementDebtADTO;
import com.viettel.coms.dto.SettlementDebtARpDTO;

public interface SettlementDebtARsService {

	@POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearch(SettlementDebtADTO obj);
	
	@POST
    @Path("/exportExcelTonACap")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportExcelTonACap(SettlementDebtADTO obj) throws Exception;
	
	@POST
    @Path("/doSearchThreeMonth")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchThreeMonth(SettlementDebtARpDTO obj);
	
	@POST
    @Path("/exportExcelACapThreeMonth")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportExcelACapThreeMonth(SettlementDebtARpDTO obj) throws Exception;
}
