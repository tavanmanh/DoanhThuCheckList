package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.dto.ReportDTO;
import com.viettel.coms.dto.ReportErpAmsWoDTO;
import com.viettel.coms.dto.RpOrderlyWoDTO;
import com.viettel.coms.dto.SysGroupDTO;

public interface RpOrderlyWoRsService {

	@POST
	@Path("/getDataReceiveWoSynthetic")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDataReceiveWoSynthetic(RpOrderlyWoDTO obj);
	
	@POST
	@Path("/getDataReceiveWoDetail")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDataReceiveWoDetail(RpOrderlyWoDTO obj);
	
	@POST
    @Path("/exportFile")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFile(RpOrderlyWoDTO obj) throws Exception;
	
	@POST
	@Path("/getForAutoCompleteByGroupLv2")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getForAutoCompleteByGroupLv2(SysGroupDTO obj);
	
	@POST
	@Path("/doSearchGeneralCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchGeneralCtv(ReportDTO obj);
	
	@POST
	@Path("/doSearchDetailCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchDetailCtv(ReportDTO obj);
	
	@POST
	@Path("/doSearchZoningCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchZoningCtv(ReportDTO obj);
	
	@POST
	@Path("/doSearchRevenueCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchRevenueCtv(ReportDTO obj);
	
	@POST
    @Path("/exportFileCtv")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileCtv(ReportDTO obj) throws Exception;
	
	@POST
    @Path("/catProvince/doSearchProvinceInPopup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchProvinceInPopup(CatProvinceDTO obj);
	
	//Huypq-07062021-start
	@POST
	@Path("/doSearchReportErpAmsWo")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchReportErpAmsWo(ReportErpAmsWoDTO obj);
	//Huy-end
}
