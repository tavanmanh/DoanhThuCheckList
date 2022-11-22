package com.viettel.coms.rest;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;

public interface ComplainOrdersRequestRsService {
	@POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response doSearch(ComplainOrderRequestDTO obj , @Context HttpServletRequest request) throws Exception;
	
//	@POST
//    @Path("/add")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    Response add(ComplainOrderRequestDetailLogHistoryDTO obj) throws Exception;
	
	@POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response update(ComplainOrderRequestDetailLogHistoryDTO obj) throws Exception;
	
	@POST
	@Path("/getListPerformer")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	Response getListPerformer(SysUserCOMSDTO sysUser);
	
	@POST
	@Path("/choosePerformer")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response choosePerformer(ComplainOrderRequestDetailLogHistoryDTO obj);
	
	@POST
    @Path("/checkRoleTTHTView")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRoleTTHTView(ComplainOrderRequestDTO obj ,@Context HttpServletRequest request) throws Exception;
	
	
	@POST
    @Path("/checkRoleDeployTicket")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRoleDeployTicket(ComplainOrderRequestDTO obj ,@Context HttpServletRequest request) throws Exception;
    //Duong end
	
}
