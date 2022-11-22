
package com.viettel.coms.rest;

import java.text.ParseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.DesignEstimatesDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.erp.dto.CatProvincesDTO;
import com.viettel.erp.dto.SysUserDTO;
public interface DesignEstimatesService {
	

	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(DesignEstimatesDTO obj) throws ParseException;

	@POST
	@Path("/save")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response save(DesignEstimatesDTO obj) throws Exception;

	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(DesignEstimatesDTO obj) throws Exception;
	
	@POST
	@Path("/delete")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response delete(DesignEstimatesDTO obj) throws Exception;
	
	@POST
    @Path("exportExcel")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
	public Response exportFile(DesignEstimatesDTO obj);
	
	@POST
	@Path("/checkRoleUserAssignYctx")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response checkRoleUserAssignYctx();
	
	@POST
    @Path("/doSearchArea")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchArea(CatProvincesDTO obj);

	@POST
    @Path("/doSearchUser")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchUser(SysUserDTO obj);
	
	@POST
    @Path("/doSearchStationVCC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchStationVCC(CatStationDTO obj);
	
	@POST
    @Path("/doSearchStationVTNET")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchStationVTNET(CatStationDTO obj);
	
	@POST
    @Path("/doSearchFile")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchFile(UtilAttachDocumentDTO obj);

	@POST
	@Path("/deleteFile")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteFile(UtilAttachDocumentDTO obj) throws Exception;
	
	@POST
    @Path("/getFile")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFile(String code);
	
}
