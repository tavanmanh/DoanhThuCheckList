package com.viettel.coms.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.viettel.coms.dto.AssignHandoverDTO;

//VietNT_20181210_created
public interface AssignHandoverRsService {

	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response doSearch(AssignHandoverDTO obj);

	@POST
	@Path("/addNewAssignHandover")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response addNewAssignHandover(AssignHandoverDTO obj);

	@POST
	@Path("/removeAssignHandover")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response removeAssignHandover(AssignHandoverDTO obj);

	@POST
	@Path("/attachDesignFileEdit")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response attachDesignFileEdit(AssignHandoverDTO dto) throws Exception;

	@POST
	@Path("/importExcel")
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	Response importExcel(Attachment attachments, @Context HttpServletRequest request) throws Exception;

	@POST
	@Path("/downloadTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	Response downloadTemplate() throws Exception;

    @POST
    @Path("/getById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getById(Long id) throws Exception;

	//VietNT_20181218_start
	@POST
	@Path("/readFileConstructionCode")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	Response readFileConstructionCode(Attachment attachments, @Context HttpServletRequest request);

	@POST
	@Path("/doAssignHandover")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response doAssignHandover(AssignHandoverDTO obj);

	@POST
	@Path("/doSearchNV")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response doSearchNV(AssignHandoverDTO obj);

	@POST
	@Path("/getListImageHandover")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response getListImageHandover(Long handoverId);

	@POST
	@Path("/getConstructionProvinceByCode")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response getConstructionProvinceByCode(String constructionCode);

	@POST
	@Path("/getForSysUserAutoComplete")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response getForSysUserAutoComplete(SysUserCOMSDTO obj);

	@POST
	@Path("/updateWorkItemPartner")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response updateWorkItemPartner(ConstructionTaskDetailDTO dto);
	//VietNT_end
	//VietNT_20180225_start
	@POST
	@Path("/exportHandoverNV")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response exportHandoverNV(AssignHandoverDTO dto);
	//VietNT_end
	
	//Huypq-20190315
	@POST
	@Path("/checkStationContractBGMB")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response checkStationContractBGMB(AssignHandoverDTO dto);
	
	@POST
	@Path("/doSearchTTKT")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response doSearchTTKT(AssignHandoverDTO obj, @Context HttpServletRequest request);
	
	@POST
	@Path("/updateSysGroupInAssignHandover")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response updateSysGroupInAssignHandover(AssignHandoverDTO dto);
	
	@POST
	@Path("/removeAssignById")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response removeAssignById(Long id);
	
	@POST
	@Path("/getCheckDataWorkItem")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response getCheckDataWorkItem(AssignHandoverDTO dto);
	
	@POST
	@Path("/checkStationContract")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response checkStationContract(AssignHandoverDTO dto);
	
	@POST
	@Path("/removeAssignHandoverById")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response removeAssignHandoverById(AssignHandoverDTO dto);
	
	@POST
	@Path("/getListHouseType")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response getListHouseType(AssignHandoverDTO dto);
	
	@POST
	@Path("/getListGroundingType")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response getListGroundingType(AssignHandoverDTO dto);
	
	@POST
	@Path("/updateEditDataAssignHandoverNv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response updateEditDataAssignHandoverNv(AssignHandoverDTO dto);
	
	@POST
	@Path("/updateAssignHandoverVuong")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response updateAssignHandoverVuong(AssignHandoverDTO dto) throws Exception;
	
	@POST
	@Path("/updateAssignHandoverVtmd")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response updateAssignHandoverVtmd(AssignHandoverDTO dto) throws Exception;
	
	@POST
	@Path("/updateAssignHandoverVuongVtmd")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response updateAssignHandoverVuongVtmd(AssignHandoverDTO dto) throws Exception;
	
	@POST
	@Path("/updateAssignHandoverNotVuongVtmd")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response updateAssignHandoverNotVuongVtmd(AssignHandoverDTO dto) throws Exception;
	
	@POST
	@Path("/importExcelTTKT")
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	Response importExcelTTKT(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/downloadTemplateTTKT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	Response downloadTemplateTTKT() throws Exception;
	
	@POST
	@Path("/checkWorkItemConsTask")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void checkWorkItemConsTask(AssignHandoverDTO dto);
	
	@POST
	@Path("/updateWorkItem")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void updateWorkItem(AssignHandoverDTO dto);
	
	@POST
	@Path("/checkStationBGMB")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	Response checkStationBGMB(AssignHandoverDTO list);
	
	@POST
	@Path("/downloadTemplateBGMB")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	Response downloadTemplateBGMB() throws Exception;
	
	@POST
	@Path("/importExcelBGMB")
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	Response importExcelBGMB(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
    @Path("/exportAssignHandoverCN")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportAssignHandoverCN(AssignHandoverDTO obj) throws Exception;
	
	@POST
    @Path("/exportAssignHandoverTTKT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportAssignHandoverTTKT(AssignHandoverDTO obj) throws Exception;
	
	@POST
    @Path("/findSignStateGoodsPlanByConsId")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response findSignStateGoodsPlanByConsId(Long id);
	
	@POST
	@Path("/updateConstructionTuyen")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void updateConstructionTuyen(AssignHandoverDTO dto);
	//Huy-end
	
	//Huypq-20190828-start
	@POST
	@Path("/getDataReportStartInMonth")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response getDataReportStartInMonth(AssignHandoverDTO obj);
	//Huy-end
}