package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.ProgressTaskOsDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;

/**
 * @author hailh10
 */

public interface ProgressTaskOsRsService {

	@POST
	@Path("/getById")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getById(ProgressTaskOsDTO obj);

	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(ProgressTaskOsDTO obj);

	@POST
	@Path("/saveAdd")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveAdd(ProgressTaskOsDTO obj) throws Exception;
	
	@POST
	@Path("/getDataTaskByProvince")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDataTaskByProvince(ProgressTaskOsDTO obj);
	
	@POST
	@Path("/doSearchMain")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchMain(ProgressTaskOsDTO obj);
	
	@POST
	@Path("/deleteRecord")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteRecord(ProgressTaskOsDTO obj);
	
	@POST
	@Path("/saveUpdate")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveUpdate(ProgressTaskOsDTO obj);
	
	@POST
    @Path("/exportCompleteProgress")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportCompleteProgress(ProgressTaskOsDTO obj) throws Exception;
	
	@POST
	@Path("/updateProgressTaskOs")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateProgressTaskOs(ProgressTaskOsDTO obj);
	
	@POST
	@Path("/doSearchBaoCaoTienDoOs")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchBaoCaoTienDoOs(ProgressTaskOsDTO obj);
	
	@POST
    @Path("/exportFileBaoCaoKHOs")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileBaoCaoKHOs(ProgressTaskOsDTO obj) throws Exception;
	
	@POST
	@Path("/doSearchBaoCaoChamDiemKpi")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchBaoCaoChamDiemKpi(ProgressTaskOsDTO obj);
	
	@POST
    @Path("/exportFileBaoCaoChamDiemKpi")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileBaoCaoChamDiemKpi(ProgressTaskOsDTO obj) throws Exception;
}
