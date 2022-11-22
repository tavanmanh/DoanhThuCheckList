package com.viettel.coms.rest;

import com.viettel.coms.dto.AssignHandoverDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//hienvd: 3/7/2019
public interface AssignHandoverKPIRsService {
	@POST
	@Path("/doSearchKPI")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response doSearch(AssignHandoverDTO obj);
}
//hienvd: end