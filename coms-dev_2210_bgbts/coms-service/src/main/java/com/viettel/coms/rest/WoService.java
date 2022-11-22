package com.viettel.coms.rest;

import com.viettel.coms.dto.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.List;


public interface WoService {

    @POST
    @Path("/create")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response create(WoDTO woDto) throws Exception;

    @POST
    @Path("/createMany")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createMany(List<WoDTO> woDto) throws Exception;

    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response update(WoDTO woDto) throws Exception;

    @POST
    @Path("/delete")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response delete(long woId) throws Exception;

    @POST
    @Path("/getById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getById(long woId) throws Exception;

    @POST
    @Path("/getByRange")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getByRange(int pageNumber, int pageSize) throws Exception;

    @POST
    @Path("/doSearchReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReport(ReportWoDTO obj);

    @POST
    @Path("/exportFile")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFile(ReportWoDTO obj) throws Exception;

    @POST
    @Path("/wo/createWO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createWO(WoDTO woDto) throws ParseException;

    @POST
    @Path("/wo/createManyWO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createManyWO(List<WoDTO> woDtoList, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wo/createManyWOReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response createManyWOReport(List<WoDTO> woDtoList) throws Exception;

    @POST
    @Path("/wo/updateWO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateWO(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/deleteWO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteWO(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/getOneWODetails")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneWODetails(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getListItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListItem(String code) throws Exception;

    @POST
    @Path("/wo/getListWODetails")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListWODetails(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearch(WoDTO woDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wo/checkWoCompleteToUpdateTr")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkWoCompleteToUpdateTr(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/giveWOAssignment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response giveWOAssignment(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/acceptWO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response acceptWO(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/rejectWO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response rejectWO(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/cdAcceptAssignment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response cdAcceptAssignment(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/cdRejectAssignment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response cdRejectAssignment(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/giveAssignmentToFT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response giveAssignmentToFT(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/ftRejectAssignment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response ftRejectAssignment(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/ftAcceptAssignment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response ftAcceptAssignment(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/ftProcessingAssignment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response ftProcessing(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/ftDoneAssignment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response ftDone(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/changeStateCdOk")

    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateCdOk(WoDTO woDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wo/changeStateCdNg")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateCdNg(WoDTO woDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wo/changeStateOk")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateOk(WoDTO woDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wo/changeStateNg")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateNg(WoDTO woDto, @Context HttpServletRequest request) throws Exception;

//    @POST
//    @Path("/wo/changeStateWaitTcTct")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response changeStateWaitTcTct(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
//
//    @POST
//    @Path("/wo/tcTctChangeStateOk")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response tcTctChangeStateOk(WoDTO woDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wo/ftOpinionRequest")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response ftOpinionRequest(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getCurrentMonthPlan")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCurrentMonthPlan(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getAppWorkSrcs")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAppWorkSrcs(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getAppConstructionTypes")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAppConstructionTypes(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getConstructions")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getConstructions(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getCdLv1List")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCdLv1List(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getCdLv2List")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCdLv2List(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getCdLv3List")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCdLv3List(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getCdLv4List")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCdLv4List(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getCatWorkTypes")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCatWorkTypes(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getFtList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFtList(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getFtListFromLv2SysGroup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFtListFromLv2SysGroup(WoSimpleFtDTO ft) throws Exception;

    @POST
    @Path("/wo/getCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCheckList(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/getImportExcelTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getImportExcelTemplate(WoDTO woDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wo/checkGpon")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response woCheckGpon(WoDTO woDto) throws Exception;

//    @POST
//    @Path("/wo/getCheckListNeedAdd")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response getCheckListNeedAdd(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/addCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addCheckList(WoXdddChecklistDTO obj) throws Exception;

    @POST
    @Path("/wo/deleteCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteCheckList(WoMappingChecklistDTO woMappingChecklistDTO) throws Exception;

    @POST
    @Path("/opinionType/createOpinionType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createOpinionType(WoOpinionTypeDTO opinionTypeDto) throws Exception;

    @POST
    @Path("/opinionType/createManyOpinionType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createManyOpinionType(List<WoOpinionTypeDTO> opinionTypeDtoList) throws Exception;


    @POST
    @Path("/opinionType/updateOpinionType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateOpinionType(WoOpinionTypeDTO opinionTypeDto) throws Exception;

    @POST
    @Path("/opinionType/deleteOpinionType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteOpinionType(WoOpinionTypeDTO opinionTypeDto) throws Exception;

    @POST
    @Path("/opinionType/doSearchOpinionType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchOpinionType(WoOpinionTypeDTO opinionTypeDto) throws Exception;

    @POST
    @Path("/opinionType/getOneWOOpinionDetails")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneDetails(WoOpinionTypeDTO opinionTypeDto) throws Exception;

    @POST
    @Path("/woType/create")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createWOType(WoTypeDTO woTypeDTO) throws Exception;

    @POST
    @Path("/woType/createMany")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createManyWOType(List<WoTypeDTO> woTypeDTOList) throws Exception;

    @POST
    @Path("/woType/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateWOType(WoTypeDTO woTypeDTO) throws Exception;

    @POST
    @Path("/woType/delete")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteWOType(WoTypeDTO woTypeDTO) throws Exception;

    @POST
    @Path("/woType/getById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getWOTypeById(WoTypeDTO woTypeDTO) throws Exception;

    @POST
    @Path("/woType/getList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getList(WoTypeDTO woTypeDTO) throws Exception;

    @POST
    @Path("/woType/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWOType(WoTypeDTO woTypeDTO) throws Exception;

    //api wo type

    //start api for attachments
    @POST
    @Path("/fileAttach/createWOMappingAttach")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createWOMappingAttach(WoMappingAttachDTO dto) throws Exception;

    @POST
    @Path("/fileAttach/updateWOMappingAttach")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateWOMappingAttach(WoMappingAttachDTO dto) throws Exception;

    @POST
    @Path("/fileAttach/deleteWOMappingAttach")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteWOMappingAttach(WoMappingAttachDTO dto) throws Exception;


    @POST
    @Path("/fileAttach/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWOMappingAttach(WoMappingAttachDTO dto) throws Exception;

    //end api for attachments

    //start api for worklogs

    @POST
    @Path("/workLogs/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWorkLogs(WoDTO dto) throws Exception;

//    @POST
//    @Path("/workLogs/createWorkLogs")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response createWorkLogs(WOWorkLogsDTO dto) throws Exception;
//
//    @POST
//    @Path("/workLogs/updateWorkLogs")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response updateWorkLogs(WOWorkLogsDTO dto) throws Exception;
//
//    @POST
//    @Path("/workLogs/deleteWorkLogs")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response deleteWorkLogs(WOWorkLogsDTO dto) throws Exception;

    // end api for work logs

    //start api for opinion

    @POST
    @Path("/opinion/createOpinion")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createOpinion(WoOpinionDTO opinionDto) throws Exception;

    @POST
    @Path("/opinion/createManyOpinion")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createManyOpinion(List<WoOpinionDTO> opinionDtoList) throws Exception;

    @POST
    @Path("/opinion/updateOpinion")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateOpinion(WoOpinionDTO opinionDto) throws Exception;

    @POST
    @Path("/opinion/deleteOpinion")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteOpinion(WoOpinionDTO opinionDto) throws Exception;

    @POST
    @Path("/opinion/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchOpinion(WoOpinionDTO opinionDto) throws Exception;

    @POST
    @Path("/doSearchBaoCaoChamDiemKpi")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchBaoCaoChamDiemKpi(ReportWoDTO obj);

    @POST
    @Path("/exportFileWoKpi")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileWoKpi(ReportWoDTO obj);

    //end api for opinion

    //--------------------------
    //start api for user

    @POST
    @Path("/user/getSysUserGroup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSysUserGroup(WoDTO dto) throws Exception;

    //end api for user

    @POST
    @Path("/report/woGeneralReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response woGeneralReport(WoGeneralReportDTO obj, @Context HttpServletRequest request);

    @POST
    @Path("/report/woDetailsReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response woDetailsReport(WoDTO obj, @Context HttpServletRequest request);

    @POST
    @Path("/wo/processOpinion")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response processOpinion(WoDTO obj);

    @POST
    @Path("/wo/constructionForAutoComplete")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response constructionForAutoComplete(WoDTO obj);

    @POST
    @Path("/woName/create")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createWOName(WoNameDTO dto) throws Exception;

    @POST
    @Path("/woName/createMany")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createManyWOName(List<WoNameDTO> dtoList) throws Exception;

    @POST
    @Path("/woName/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateWOName(WoNameDTO dto) throws Exception;

    @POST
    @Path("/woName/delete")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteWOName(WoNameDTO dto) throws Exception;

    @POST
    @Path("/woName/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWOName(WoNameDTO dto) throws Exception;

    @POST
    @Path("/wo/getCatWorkName")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCatWorkName(WoDTO dto) throws Exception;

    @POST
    @Path("/wo/getAIOWoInfo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAIOWoInfo(WoDTO dto) throws Exception;

    @POST
    @Path("/woType/getWoTypeImportTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getWoTypeImportTemplate() throws Exception;

    @POST
    @Path("/report/getExcelDetailsReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getExcelDetailsReport(WoDTO dto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/report/getExcelGeneralReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getExcelGeneralReport(WoGeneralReportDTO dto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/woName/getWoNameImportTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getWoNameImportTemplate() throws Exception;

    @POST
    @Path("/doSearchReportTHDT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReportTHDT(ReportWoTHDTDTO obj);

    @POST
    @Path("/exportFileTHDT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileTHDT(ReportWoTHDTDTO obj);

    @POST
    @Path("/checklist/acceptQuantity")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response acceptChecklistQuantity(WoMappingChecklistDTO dto) throws Exception;

    @POST
    @Path("/checklist/rejectQuantity")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response rejectChecklistQuantity(WoMappingChecklistDTO dto) throws Exception;

    @POST
    @Path("/woName/exportWoNameExcel")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response exportWoNameExcel(WoNameDTO dto) throws Exception;

    @POST
    @Path("/woType/exportWoTypeExcel")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response exportWoTypeExcel(WoTypeDTO dto) throws Exception;

    @POST
    @Path("/wo/exportWoExcel")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response exportWoExcel(WoDTO dto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/doSearchReportAIO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReportAIO(ReportWoAIODTO obj);

    @POST
    @Path("/wo/getSysGroup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSysGroup(WoDTO obj);

    @POST
    @Path("/exportFileAIO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileAIO(ReportWoAIODTO obj);

    @POST
    @Path("/hcqtChecklist/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchHcqtChecklist(WoChecklistDTO obj);

    @POST
    @Path("/avgChecklist/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchAvgChecklist(WoChecklistDTO obj);

    @POST
    @Path("/hcqtChecklist/completeChecklist")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response completeHcqtChecklist(WoChecklistDTO obj);

    @POST
    @Path("/hcqtChecklist/addImage")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addImage(WoChecklistDTO obj) throws Exception;

    @POST
    @Path("/hcqtChecklist/getHcqtIssueList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getHcqtIssueList(WoChecklistDTO obj);

    @POST
    @Path("/hcqtChecklist/declareHcqtIssue")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response declareHcqtIssue(WoChecklistDTO obj);

    @POST
    @Path("/report/woHcqtFtReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response woHcqtFtReport(WoHcqtFtReportDTO dto) throws Exception;

    @POST
    @Path("/exportFileHcqtFtReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileHcqtFtReport(WoHcqtFtReportDTO obj);

    //Huypq-25082020-start
    @POST
    @Path("/report/exportReportDetailWo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportReportDetailWo(WoDTO dto, @Context HttpServletRequest request) throws Exception;
    //Huy-end

    //Huypq-04092020-start
    @POST
    @Path("/wo/checkRoleApproveHshc")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRoleApproveHshc(@Context HttpServletRequest request);
    //Huy-end

    @POST
    @Path("/doSearchReportHSHCStatus")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReportHSHCStatus(ReportHSHCQTDTO obj);

    @POST

    @Path("/exportFileHSHCStatus")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileHSHCStatus(ReportHSHCQTDTO obj);

    @POST
    @Path("/doSearchReportHSHCProvince")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReportHSHCProvince(ReportHSHCQTDTO obj);

    @POST
    @Path("/exportFileHSHCProvince")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileHSHCProvince(ReportHSHCQTDTO obj);

    @POST
    @Path("/hcqtProject/createHcqtProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createHcqtProject(WoHcqtProjectDTO dto) throws Exception;

    @POST
    @Path("hcqtProject/doSearchHcqtProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchHcqtProject(WoHcqtProjectDTO dto);

    @POST
    @Path("hcqtProject/getOneHcqtProjectDetails")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneHcqtProjectDetails(WoHcqtProjectDTO dto);

    @POST
    @Path("hcqtProject/deleteHcqtProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteHcqtProject(WoHcqtProjectDTO dto);

    @POST
    @Path("hcqtProject/updateHcqtProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateHcqtProject(WoHcqtProjectDTO dto);

    @POST
    @Path("hcqt/getImportHCQTExcelTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getImportHCQTExcelTemplate() throws Exception;

    @POST
    @Path("/wo/createManyHCQTWO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createManyHCQTWO(List<WoHcqtDTO> dtos, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wo/createManyHCQTWOReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response createManyHCQTWOReport(List<WoHcqtDTO> woDtoList) throws Exception;

    @POST
    @Path("/hcqtChecklist/resolveHcqtIssue")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response resolveHcqtIssue(WoChecklistDTO obj);

    @POST
    @Path("/exportFileDelivery")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileDelivery(WoDTO obj, @Context HttpServletRequest request);

    @POST
    @Path("/importFileDelivery")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importFileDelivery(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/hcqtProject/getHcqtProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getHcqtProject(WoHcqtProjectDTO obj);

    //Unikom start
    @POST
    @Path("/wi/doSearchWorkItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWorkItem(WoScheduleWorkItemDTO woScheduleWorkItemDTO) throws Exception;

    @POST
    @Path("/wi/createWorkItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createWorkItem(WoScheduleWorkItemDTO dto) throws Exception;

    @POST
    @Path("/wi/getOneInfoWorkItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneInfoWorkItem(WoScheduleWorkItemDTO dto) throws Exception;

    @POST
    @Path("/wi/updateWorkItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateWorkItem(WoScheduleWorkItemDTO dto) throws Exception;

    @POST
    @Path("/wi/deleteWorkItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteWorkItem(WoScheduleWorkItemDTO dto) throws Exception;

    @POST
    @Path("/wo/doSearchWICheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWICheckList(WoScheduleWorkItemDTO dto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wi/createNewWorkItemCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createNewWorkItemCheckList(WoScheduleCheckListDTO dto) throws Exception;

    @POST
    @Path("/wi/getOneDetailsCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneDetailsCheckList(WoScheduleCheckListDTO dto) throws Exception;

    @POST
    @Path("/wi/updateWorkItemCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateWorkItemCheckList(WoScheduleCheckListDTO dto) throws Exception;

    @POST
    @Path("/wi/deleteWorkItemCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteWorkItemCheckList(WoScheduleCheckListDTO dto) throws Exception;

    @POST
    @Path("/wo/doSearchWIConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWIConfig(WoScheduleConfigDTO dto) throws Exception;

    @POST
    @Path("/wi/createWIConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createWIConfig(WoScheduleConfigDTO dto) throws Exception;

    @POST
    @Path("/wi/getOneWIConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneWIConfig(WoScheduleConfigDTO dto) throws Exception;

    @POST
    @Path("/wi/updateWIConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateWIConfig(WoScheduleConfigDTO dto) throws Exception;

    @POST
    @Path("/wi/deleteWIConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteWIConfig(WoScheduleConfigDTO dto) throws Exception;

    @POST
    @Path("/importFileConfig")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importFileConfig(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/exportexcelScheduleConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportexcelScheduleConfig(WoDTO obj, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/importFileScheduleWoConfig")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importFileScheduleWoConfig(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/wo/getImportScheduleConfigResult")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getImportScheduleConfigResult(List<WoScheduleConfigDTO> woDtoList) throws Exception;

    //Unikom end

    @POST
    @Path("/workItem/getWorkItemByConstruction")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getWorkItemByConstruction(WorkItemDTO obj);

    @POST
    @Path("/wo/doSearchHcqtWo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchHcqtWo(WoDTO dto, @Context HttpServletRequest request);

    @POST
    @Path("/wo/exportHcqtWo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportHcqtWo(WoDTO dto, @Context HttpServletRequest request);

    @POST
    @Path("/wo/getVhktCdLv2VList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getVhktCdLv2VList(WorkItemDTO obj);

    @POST
    @Path("/wo/createManyHcqtWOReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response createManyHcqtWOReport(List<WoHcqtDTO> woDtoList) throws Exception;

    //Huypq-14102020-start
    @POST
    @Path("/wo/getSysGroupNameById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSysGroupNameById(Long id);

    //Huy-end
    @POST
    @Path("/wo/getImportFTResult")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getImportFTResult(List<WoDTO> woDtoList) throws Exception;

    @POST
    @Path("/hshc/autoSuggestMoneyValue")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response autoSuggestMoneyValue(WoSimpleConstructionDTO dto) throws Exception;

    @POST
    @Path("/exportApproveWo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportApproveWo(WoDTO obj, @Context HttpServletRequest request);

    @POST
    @Path("/importApproveWo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importApproveWo(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/getImportApprovreResult")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getImportApprovreResult(List<WoDTO> woDtoList) throws Exception;

    @POST
    @Path("/doSearchReportWoTr")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReportWoTr(ReportWoTrDTO obj);

    //Huypq-02112020-start

    @POST
    @Path("/woHcqt/getCheckViewHcqt")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCheckViewHcqt(@Context HttpServletRequest request);

    //huy-end
    @POST
    @Path("/exportFileTrWo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileTrWo(ReportWoTrDTO obj);

    @POST
    @Path("/xddd/acceptXdddValue")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response acceptXdddValue(WoXdddChecklistDTO obj);

    @POST
    @Path("/xddd/rejectXdddValue")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response rejectXdddValue(WoXdddChecklistDTO obj);

    @POST
    @Path("/doSearchReportWo5s")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReportWo5s(WoDTO obj);

    @POST
    @Path("/exportFileReport5s")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response exportFileReport5s(List<Report5sDTO> obj) throws Exception;

    @POST
    @Path("/wo/doSearchWoFTConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWoFTConfig(WoFTConfig5SDTO dto) throws Exception;

    @POST
    @Path("/wo/getCnktList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCnktList() throws Exception;

    @POST
    @Path("/wo/getFTList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFTList(Long sysGroupId) throws Exception;

    @POST
    @Path("/wo/getOneWO5SConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneWO5SConfig(WoFTConfig5SDTO dto) throws Exception;

    @POST
    @Path("/wo/updateWO5SConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateWO5SConfig(WoFTConfig5SDTO dto) throws Exception;

    @POST
    @Path("/wo/deleteWO5SConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteWO5SConfig(WoFTConfig5SDTO dto) throws Exception;

    @POST
    @Path("/wo/createConfigWO5s")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createConfigWO5s(WoFTConfig5SDTO dto) throws Exception;

//    @POST
//    @Path("/wo/createWO5s")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response createWO5s(WoDTO dto);

    @POST
    @Path("/woTaskDaily/doSearchWoTaskDaily")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWoTaskDaily(WoTaskDailyDTO dto);

    @POST
    @Path("/woTaskDaily/acceptQuantityByDate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response acceptQuantityByDate(WoTaskDailyDTO dto);

    @POST
    @Path("/woTaskDaily/rejectQuantityByDate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response rejectQuantityByDate(WoTaskDailyDTO dto);

    @POST
    @Path("/getCDCnkt")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCDCnkt() throws Exception;

    //Huypq-25112020-start
    @POST
    @Path("/exportDoneWo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportDoneWo(WoDTO obj, @Context HttpServletRequest request);

    @POST
    @Path("/importDoneWo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importDoneWo(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/getImportDoneResult")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getImportDoneResult(List<WoDTO> woDtoList) throws Exception;
    //Huy-end

    @POST
    @Path("/xddd/extendFinishDate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response extendFinishDate(WoDTO dto) throws Exception;

    @POST
    @Path("/doSearchDetailReportWoTr")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchDetailReportWoTr(ReportWoTrDTO obj);

    @POST
    @Path("/doSearchDetailReportTr")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchDetailReportTr(ReportWoTrDTO obj);

    @POST
    @Path("/xddd/getXdddChecklistByWorkItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getXdddChecklistByWorkItem(WoDTO dto) throws Exception;

    @POST
    @Path("/taichinh/changeStateAndAcceptTcTct")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateAndAcceptTcTct(WoDTO dto) throws Exception;

    @POST
    @Path("/woConfigContract/importConfigContract")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importConfigContract(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/woConfigContract/getWoConfigContractResult")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getWoConfigContractResult(List<WoConfigContractCommitteeDTO> woDtoList) throws Exception;

    @POST
    @Path("/wo/getFtListCdLevel5")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFtListCdLevel5(WoDTO woDto) throws Exception;

    @POST
    @Path("/wo/removeInactiveWo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response removeInactiveWo(WoDTO woDto) throws Exception;

    @POST
    @Path("/overdue/postOverdueReason")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postOverdueReason(WoDTO dto) throws Exception;

    @POST
    @Path("/overdue/getWoOverdueReason")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getWoOverdueReason(WoOverdueReasonDTO dto) throws Exception;

    @POST
    @Path("/overdue/acceptRejectOverdueReason")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response acceptRejectOverdueReason(WoDTO dto) throws Exception;

    @POST
    @Path("/getGeneralReportXDDTHT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getGeneralReportXDDTHT(WoGeneralReportDTO obj, @Context HttpServletRequest request);

    @POST
    @Path("/getWoDetailReportXDDTHT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getWoDetailReportXDDTHT(WoGeneralReportDTO obj, @Context HttpServletRequest request);

    @POST
    @Path("/exportFileReportXDDTHT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileReportXDDTHT(WoGeneralReportDTO obj);

    @POST
    @Path("/tc/tcAcceptAllSelected")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response tcAcceptAllSelected(WoTcMassApproveRejectReqDTO req) throws Exception;

    @POST
    @Path("/tc/tcRejectAllSelected")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response tcRejectAllSelected(WoTcMassApproveRejectReqDTO req) throws Exception;

    @POST
    @Path("/tc/getTcTctEmails")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getTcTctEmails() throws Exception;

    @POST
    @Path("/hshc/getConstructionWOByHSHC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getConstructionWOByHSHC(WoMappingHshcTcDTO dto) throws Exception;

    @POST
    @Path("/wo/getListGoodsByWoId")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListGoodsByWoId(WoDTO dto) throws Exception;

    @POST
    @Path("/wo/createNewTmbtWO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createNewTmbtWO(WoTrDTO trDto);

    @POST
    @Path("/wo/createNewDbhtWO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createNewDbhtWO(WoTrDTO trDto);

    @POST
    @Path("/tmbt/getLstStationsOfWo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getLstStationsOfWo(WoMappingStationDTO dto) throws Exception;

    @POST
    @Path("/tkdtChecklist/completeChecklist")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response completeTkdtChecklist(WoChecklistDTO obj);

    @POST
    @Path("/tkdtChecklist/approvedWoOk")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response approvedWoOk(WoDTO woDto, @Context HttpServletRequest request);

    @POST
    @Path("/getWoDataForChart")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getWoDataForChart(WoChartDataDto obj);
    
    //taotq 06052021 start
    @POST
    @Path("/exportConfigWorkItemList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response exportConfigWorkItemList(WoScheduleConfigDTO dto, @Context HttpServletRequest request) throws Exception;
  //taotq 06052021 end
    
    //Huypq-28062021-start
    @POST
    @Path("/exportFileImportTthq")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileImportTthq(WoDTO obj, @Context HttpServletRequest request);
    
    @POST
    @Path("/woDetails/importFileTthq")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importFileTthq(Attachment attachments, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/tthqService/getDataTableTTTHQ")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDataTableTTTHQ(EffectiveCalculationDetailsDTO obj);
    
    @POST
    @Path("/tthqService/approvedWoOkTthq")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response approvedWoOkTthq(WoDTO woDto);
    //Huy-end
    
    //HienLT56 start 27052021
    @POST
    @Path("/wo/checkRoleTTHT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRoleTTHT(@Context HttpServletRequest request);
    
    @POST
    @Path("/wo/checkRoleDeleteTTHT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRoleDeleteTTHT(@Context HttpServletRequest request);
    
    @POST
    @Path("/wo/saveChangeForTTHT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveChangeForTTHT(WoDTO woDto) throws Exception;
    
    //HienLT56 end 27052021
    //    taotq start 23082021
    @POST
    @Path("/wo/getAppWorkSource")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAppWorkSource(WoDTO woDto) throws Exception;
    
    @POST
    @Path("/wo/getListItemByWorkSrc")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListItemByWorkSrc() throws Exception;
    
    @POST
    @Path("/wo/getListItemN")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListItemN(String code) throws Exception;
    
    @POST
    @Path("/fileAttach/createFileCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createFileCheckList(WoMappingAttachDTO dto) throws Exception;
    //  taotq start 27082021

    //Duonghv13-start 14092021
    @POST
    @Path("/wo/checkRoleCDPKTCNKT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRoleCDPKTCNKT(@Context HttpServletRequest request) throws Exception;
    //Duong end
    

    //  duonghv13 start 13102021
    @POST
    @Path("/wo/checkConditionCertificate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkConditionCertificate(ManageCertificateDTO certificateDTO) throws Exception;

    
    //Huypq-22102021-start
    @POST
    @Path("/wo/changeStateWaitPqt")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateWaitPqt(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/changeStateWaitTtDtht")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateWaitTtDtht(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/changeStateWaitTcTct")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateWaitTcTct(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/workItem/getDataWorkItemByConsTypeId")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDataWorkItemByConsTypeId(CatWorkItemTypeDTO obj);
    
    @POST
    @Path("/wo/changeStateCdPause")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateCdPause(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/changeStateTthtPause")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateTthtPause(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/changeStateDthtPause")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateDthtPause(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/getDataConstructionContractByStationCode")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDataConstructionContractByStationCode(String stationCode);
    
    @POST
    @Path("/wo/changeStateCdPauseReject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateCdPauseReject(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/changeStateTthtPauseReject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateTthtPauseReject(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/changeStateDthtPauseReject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateDthtPauseReject(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/changeStateApprovedOrRejectWoHtctPQT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateApprovedOrRejectWoHtctPQT(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/wo/changeStateApprovedOrRejectWoHtctTtDtht")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateApprovedOrRejectWoHtctTtDtht(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    @POST
    @Path("/workItem/getWorkItemCompleteByConstruction")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getWorkItemCompleteByConstruction(WorkItemDTO obj);
    
    @POST
    @Path("/wo/changeStateWoReject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateWoReject(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
    
    //Huy-end
    @POST
    @Path("/wo/changeStateReProcessWoDoanhThuDTHT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateReProcessWoDoanhThuDTHT(WoDTO woDto) throws Exception;
    
    @POST
    @Path("/wo/checkRoleApproveCDLV5")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRoleApproveCDLV5(@Context HttpServletRequest request) throws Exception;
    //ducpm23 add
    @POST
    @Path("/wo/checkContractIsGpxd")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkContractIsGpxd(Long contractId, @Context HttpServletRequest request) throws Exception;

    //taotq start 15092022
    @POST
    @Path("/wo/checkAsignAdminWo")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkAsignAdminWo(@Context HttpServletRequest request);
    //taotq-end

    //kienkh start 25102022
    @POST
    @Path("/wo/getStationResource")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getStationResource(AssetHandoverList assetHandoverList);
    //kienkh end

    //kienkh start 27102022
    @POST
    @Path("/wo/rejectWoBgbtsVhkt")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response rejectWoBgbtsVhkt(WoDTO woDto) throws Exception;
    //kienkh end
}


