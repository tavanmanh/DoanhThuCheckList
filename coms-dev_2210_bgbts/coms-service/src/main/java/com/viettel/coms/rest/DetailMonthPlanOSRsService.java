package com.viettel.coms.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.viettel.coms.dto.CNTContractDTO;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.DetailMonthPlanSimpleDTO;
import com.viettel.coms.dto.DetailMonthQuantityDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserDetailCOMSDTO;

/**
 * @author HoangNH38
 */
public interface DetailMonthPlanOSRsService {
    @POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearch(DetailMonthPlanSimpleDTO obj);

    @POST
    @Path("/remove")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response remove(DetailMonthPlanSimpleDTO obj);

    //
    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response add(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response update(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/getById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getById(Long id) throws Exception;

    @POST
    @Path("/constructionTask/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchConsTask(ConstructionTaskDetailDTO obj) throws Exception;

    @POST
    @Path("/getSequence")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSequence() throws Exception;

    @POST
    @Path("/updateListTC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateListTC(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/updateListHSHC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateListHSHC(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/updateListLDT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateListLDT(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/updateListDT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateListDT(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/updateListCVK")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateListCVK(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/getWorkItemDetail")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getWorkItemDetail(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/getYearPlanDetailTarget")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getYearPlanDetailTarget(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/exportTemplateListTC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateListTC(SysUserDetailCOMSDTO obj) throws Exception;

    @POST
    @Path("/exportTemplateListHSHC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateListHSHC(SysUserDetailCOMSDTO obj) throws Exception;

    @POST
    @Path("/exportTemplateListLDT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateListLDT(SysUserDetailCOMSDTO obj) throws Exception;

    @POST
    @Path("/exportTemplateListCVK")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateListCVK(SysUserDetailCOMSDTO obj) throws Exception;

    @POST
    @Path("/exportTemplateListDT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateListDT(SysUserDetailCOMSDTO obj) throws Exception;

    @POST
    @Path("/exportTemplateListYCVT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateListYCVT(SysUserDetailCOMSDTO obj) throws Exception;

    @POST
    @Path("/importTC")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importTC(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/importHSHC")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importHSHC(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/importLDT")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importLDT(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/importCVK")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importCVK(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/importDT")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importDT(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/importYCVT")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importYCVT(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/doSearchMaterial")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchMaterial(ConstructionTaskDetailDTO obj) throws Exception;

    @POST
    @Path("/updateListDmpnOrder")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateListDmpnOrder(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/exportDetailMonthPlan")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportDetailMonthPlan(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/exportDetailMonthPlanTab1")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportDetailMonthPlanTab1(ConstructionTaskDetailDTO obj) throws Exception;

    @POST
    @Path("/exportDetailMonthPlanTab2")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportDetailMonthPlanTab2(ConstructionTaskDetailDTO obj) throws Exception;

    @POST
    @Path("/exportDetailMonthPlanTab3")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportDetailMonthPlanTab3(ConstructionTaskDetailDTO obj) throws Exception;

    @POST
    @Path("/exportDetailMonthPlanTab5")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportDetailMonthPlanTab5(ConstructionTaskDetailDTO obj) throws Exception;

    @POST
    @Path("/exportDetailMonthPlanTab6")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportDetailMonthPlanTab6(ConstructionTaskDetailDTO obj) throws Exception;

    @POST
    @Path("/exportDetailMonthPlanBTS")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportDetailMonthPlanBTS(ConstructionTaskDetailDTO obj) throws Exception;

    @POST
    @Path("/checkPermissionsAdd")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkPermissionsAdd();

    @POST
    @Path("/checkPermissionsCopy")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkPermissionsCopy();

    @POST
    @Path("/checkPermissionsDelete")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkPermissionsDelete(DetailMonthPlanSimpleDTO obj) throws Exception;

    @POST
    @Path("/checkPermissionsRegistry")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkPermissionsRegistry();

    @POST
    @Path("/checkPermissionsUpdate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkPermissionsUpdate(DetailMonthPlanSimpleDTO obj) throws Exception;
    
    @POST
    @Path("/removeRow")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response removeRow(ConstructionTaskDTO obj);
    
    @POST
    @Path("/updateRegistry")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateRegistry(DetailMonthPlanSimpleDTO obj);

    @POST
    @Path("/addTask")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addTask(DetailMonthPlanSimpleDTO obj) throws Exception;
    
    @POST
	@Path("/importThuHoiDT")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importThuHoiDT(Attachment attachments, @Context HttpServletRequest request) throws Exception;
    
	@POST
    @Path("/getRevokeCashMonthPlanByPlanId")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRevokeCashMonthPlanByPlanId(RevokeCashMonthPlanDTO obj);
	
    //tatph - start 19/12/2019
    @POST
    @Path("/doSearchManageValue")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchManageValue(RevokeCashMonthPlanDTO obj);
    
    @POST
    @Path("/updateRevokeCashMonthPlan")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateRevokeCashMonthPlan(RevokeCashMonthPlanDTO obj);
    
    @POST
    @Path("/approveRevokeCashMonthPlan")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response approveRevokeCashMonthPlan(RevokeCashMonthPlanDTO obj);
    
    
    @POST
    @Path("/getExcelTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getExcelTemplate(RevokeCashMonthPlanDTO obj) throws Exception;
    
    @POST
	@Path("/importManageValue")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importManageValue(Attachment attachments, @Context HttpServletRequest request) throws Exception;
    
    //tatph - end 19/12/2019

    //Huypq-20200113-start
    @POST
    @Path("/checkRoleTTHT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRoleTTHT();
    
    @POST
    @Path("/updateRejectRevokeCash")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateRejectRevokeCash(RevokeCashMonthPlanDTO obj);
    //Huy-end
    
    //Huypq-20200513-start
    @POST
    @Path("/exportTemplateRent")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateRent(SysUserDetailCOMSDTO obj) throws Exception;
    
  //Import thuê mặt bằng trạm
    @POST
    @Path("/importRentGround")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importRentGround(Attachment attachments, @Context HttpServletRequest request) throws Exception;
    //Huypq-end
    
    //Huypq-29062020-start
    @POST
    @Path("/exportTemplateTargetTTXD")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportTemplateTargetTTXD(SysUserDetailCOMSDTO obj) throws Exception;

    @POST
    @Path("/importTargetTTXD")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importTargetTTXD(Attachment attachments, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/doSearchResultMonthQuantityTTXD")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchResultMonthQuantityTTXD(ConstructionTaskDetailDTO obj);

    @POST
    @Path("/getListAttachmentByIdAndType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListAttachmentByIdAndType(Long id) throws Exception;
    
    @POST
    @Path("/exportResultQuantityTTXD")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportResultQuantityTTXD(ConstructionTaskDetailDTO obj) throws Exception;
    
    @POST
    @Path("/doSearchStaffByPopup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchStaffByPopup(DetailMonthQuantityDTO obj);
    
    @POST
    @Path("/doSearchContractByPopup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchContractByPopup(DetailMonthQuantityDTO obj);
    
    @POST
    @Path("/exportGiaoChiTietTTXD")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportGiaoChiTietTTXD(ConstructionTaskDetailDTO obj) throws Exception;
    //huy-end
}
