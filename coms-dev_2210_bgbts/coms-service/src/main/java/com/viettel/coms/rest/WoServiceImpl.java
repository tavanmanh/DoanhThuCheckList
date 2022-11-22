package com.viettel.coms.rest;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import com.viettel.coms.dto.*;
import com.viettel.erp.bo.CntContractBO;
import com.viettel.erp.business.CntContractBusinessImpl;
import com.viettel.erp.dao.CntContractDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.viettel.asset.dto.SysGroupDto;
import com.viettel.coms.bo.CatStationBO;
import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoFTConfig5SBO;
import com.viettel.coms.bo.WoHcqtProjectBO;
import com.viettel.coms.bo.WoMappingChecklistBO;
import com.viettel.coms.bo.WoMappingStationBO;
import com.viettel.coms.bo.WoNameBO;
import com.viettel.coms.bo.WoOpinionBO;
import com.viettel.coms.bo.WoOpinionTypeBO;
import com.viettel.coms.bo.WoOverdueReasonBO;
import com.viettel.coms.bo.WoScheduleCheckListBO;
import com.viettel.coms.bo.WoScheduleConfigBO;
import com.viettel.coms.bo.WoScheduleWorkItemBO;
import com.viettel.coms.bo.WoTaskDailyBO;
import com.viettel.coms.bo.WoTrBO;
import com.viettel.coms.bo.WoTypeBO;
import com.viettel.coms.bo.WoWorkLogsBO;
import com.viettel.coms.bo.WoXdddChecklistBO;
import com.viettel.coms.business.EffectiveCalculationDetailsBusinessImpl;
import com.viettel.coms.business.ManagementCertificateBusinessImpl;
import com.viettel.coms.business.WoBusinessImpl;
import com.viettel.coms.dao.CatStationDAO;
import com.viettel.coms.dao.EffectiveCalculationDetailsDAO;
import com.viettel.coms.dao.ManageCareerDAO;
import com.viettel.coms.dao.ManageCertificateDAO;
import com.viettel.coms.dao.TotalMonthPlanOSDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dao.WoChecklistDAO;
import com.viettel.coms.dao.WoConfigContractDAO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dao.WoFTConfig5SDAO;
import com.viettel.coms.dao.WoHcqtProjectDAO;
import com.viettel.coms.dao.WoMappingAttachDAO;
import com.viettel.coms.dao.WoMappingChecklistDAO;
import com.viettel.coms.dao.WoMappingStationDAO;
import com.viettel.coms.dao.WoNameDAO;
import com.viettel.coms.dao.WoOpinionDAO;
import com.viettel.coms.dao.WoOpinionTypeDAO;
import com.viettel.coms.dao.WoScheduleCheckListDAO;
import com.viettel.coms.dao.WoScheduleConfigDAO;
import com.viettel.coms.dao.WoScheduleWorkItemDAO;
import com.viettel.coms.dao.WoTaskDailyDAO;
import com.viettel.coms.dao.WoTaskMonthlyDAO;
import com.viettel.coms.dao.WoTrDAO;
import com.viettel.coms.dao.WoTrMappingStationDAO;
import com.viettel.coms.dao.WoTypeDAO;
import com.viettel.coms.dao.WoWorkLogsDAO;
import com.viettel.coms.dao.WoXdddChecklistDAO;
import com.viettel.coms.dto.CatWorkItemTypeDTO;
import com.viettel.coms.dto.EffectiveCalculationDetailsDTO;
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.Report5sDTO;
import com.viettel.coms.dto.ReportHSHCQTDTO;
import com.viettel.coms.dto.ReportWoAIODTO;
import com.viettel.coms.dto.ReportWoDTO;
import com.viettel.coms.dto.ReportWoTHDTDTO;
import com.viettel.coms.dto.ReportWoTrDTO;
import com.viettel.coms.dto.TotalMonthPlanOSDTO;
import com.viettel.coms.dto.TotalMonthPlanOSSimpleDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WoAppParamDTO;
import com.viettel.coms.dto.WoCatWorkItemTypeDTO;
import com.viettel.coms.dto.WoCdDTO;
import com.viettel.coms.dto.WoChartDataDto;
import com.viettel.coms.dto.WoChecklistDTO;
import com.viettel.coms.dto.WoConfigContractCommitteeDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoDTORequest;
import com.viettel.coms.dto.WoDTOResponse;
import com.viettel.coms.dto.WoFTConfig5SDTO;
import com.viettel.coms.dto.WoGeneralReportDTO;
import com.viettel.coms.dto.WoHcqtDTO;
import com.viettel.coms.dto.WoHcqtFtReportDTO;
import com.viettel.coms.dto.WoHcqtProjectDTO;
import com.viettel.coms.dto.WoMappingAttachDTO;
import com.viettel.coms.dto.WoMappingChecklistDTO;
import com.viettel.coms.dto.WoMappingHshcTcDTO;
import com.viettel.coms.dto.WoMappingStationDTO;
import com.viettel.coms.dto.WoNameDTO;
import com.viettel.coms.dto.WoOpinionDTO;
import com.viettel.coms.dto.WoOpinionTypeDTO;
import com.viettel.coms.dto.WoOverdueReasonDTO;
import com.viettel.coms.dto.WoScheduleCheckListDTO;
import com.viettel.coms.dto.WoScheduleConfigDTO;
import com.viettel.coms.dto.WoScheduleWorkItemDTO;
import com.viettel.coms.dto.WoSimpleConstructionDTO;
import com.viettel.coms.dto.WoSimpleFtDTO;
import com.viettel.coms.dto.WoSimpleSysGroupDTO;
import com.viettel.coms.dto.WoTaskDailyDTO;
import com.viettel.coms.dto.WoTcMassApproveRejectReqDTO;
import com.viettel.coms.dto.WoTrDTO;
import com.viettel.coms.dto.WoTrMappingStationDTO;
import com.viettel.coms.dto.WoTypeDTO;
import com.viettel.coms.dto.WoXdddChecklistDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.utils.BaseResponseOBJ;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UConfig;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.DateTimeUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import static viettel.passport.service.UserUtils.gson;

public class WoServiceImpl implements WoService {
    private final int SUCCESS_CODE = 1;
    private final int ERROR_CODE = -1;
    private final String SUCCESS_MSG = "SUCCESS";
    private final String ERROR_MSG = "Có lỗi xảy ra.";
    private final String NOTFOUND_MSG = "NOT FOUND";
    private final String CREATE_NEW_MSG = "CREATED";
    private final String UPDATED_MSG = "UPDATED";
    private final String TOO_MANY_AIO_WO = "TR từ AIO này chỉ có thể tạo 1 WO.";

    private final String UNASSIGN = "UNASSIGN";
    private final String ASSIGN_CD = "ASSIGN_CD";
    private final String ACCEPT_CD = "ACCEPT_CD";
    private final String REJECT_CD = "REJECT_CD";
    private final String ASSIGN_FT = "ASSIGN_FT";
    private final String ACCEPT_FT = "ACCEPT_FT";
    private final String REJECT_FT = "REJECT_FT";
    private final String PROCESSING = "PROCESSING";
    private final String DONE = "DONE";
    private final String CD_OK = "CD_OK";
    private final String CD_NG = "CD_NG";
    private final String OK = "OK";
    private final String NG = "NG";
    private final String OPINION_RQ = "OPINION_RQ";
    private final String WAIT_TC_BRANCH = "WAIT_TC_BRANCH";
    private final String WAIT_TC_TCT = "WAIT_TC_TCT";
    private final String WAIT_PQT = "WAIT_PQT";
    private final String WAIT_TTDTHT = "WAIT_TTDTHT";
    private final String CD_PAUSE = "CD_PAUSE";
    private final String TTHT_PAUSE = "TTHT_PAUSE";
    private final String DTHT_PAUSE = "DTHT_PAUSE";
    private final String CD_PAUSE_REJECT = "CD_PAUSE_REJECT";
    private final String TTHT_PAUSE_REJECT = "TTHT_PAUSE_REJECT";
    private final String DTHT_PAUSE_REJECT = "DTHT_PAUSE_REJECT";
    private final String RECEIVED_PQT = "RECEIVED_PQT";
    private final String REJECT_PQT = "REJECT_PQT";
    private final String RECEIVED_TTDTHT = "RECEIVED_TTDTHT";
    private final String REJECT_TTDTHT = "REJECT_TTDTHT";
    private final String PQT_NG = "PQT_NG";
    private final String TTDTHT_NG = "TTDTHT_NG";

    private final String ALL_TYPE = "ALL TYPE";
    private final String CREATED_TYPE = "CREATED TYPE";
    private final String ASSIGNED_CD_TYPE = "ASSIGNED CD TYPE";
    private final String ASSIGNED_FT_TYPE = "ASSIGNED FT TYPE";
    private final String REPORT_TYPE = "REPORT_TYPE";

    private final String AP_CONSTRUCTION_TYPE = "AP_CONSTRUCTION_TYPE";
    private final String AP_WORK_SRC = "AP_WORK_SRC";

    private final String CANNOT_DELETE = "Không thể sửa/xóa do có dữ liệu liên quan.";
    private final String CANNOT_DELETE_SCHEDULE_WI_CHECKLIST = "Không thể xóa do có dữ liệu liên quan.";

    private final String TTHT_ID = "242656";
    private final String TTVHKT_ID = "270120";
    private final String TTXDDTHT_ID = "166677";
    private final String TTGPTH_ID = "280483";
    private final String TTCNTT_ID = "280501";

    private final String NEW = "NEW";
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
    @Value("${allow.file.ext}")
    private String allowFileExt;
    @Value("${folder_upload2}")
    private String folderUpload;
    @Value("${allow.folder.dir}")
    private String allowFolderDir;
    @Value("${input_image_sub_folder_upload}")
    private String input_image_sub_folder_upload;
    @Value("${input_image_folder_upload_wo}")
    private String input_image_folder_upload_wo;

    private final String getOrderGoodsDetail = UConfig.get("ktts.ams.service") + "amsOrderGoodsRsService/doOrderGoodsDetail";

    @Autowired
    WoBusinessImpl woBusinessImpl;

    @Autowired
    WoDAO woDAO;

    @Autowired
    WoTrDAO trDAO;

    @Autowired
    WoTypeDAO woTypeDAO;

    @Autowired
    WoOpinionTypeDAO opinionTypeDAO;

    @Autowired
    WoMappingAttachDAO woMappingAttachDAO;

    @Autowired
    WoMappingChecklistDAO woMappingCheckListDAO;

    @Autowired
    WoWorkLogsDAO woWorkLogsDAO;

    @Autowired
    TotalMonthPlanOSDAO totalMonthPlanOSDAO;

    @Autowired
    WoOpinionDAO opinionDAO;

    @Autowired
    WoNameDAO woNameDAO;

    @Autowired
    WoChecklistDAO woChecklistDAO;

    @Autowired
    WoHcqtProjectDAO woHcqtProjectDAO;

    @Autowired
    WoScheduleWorkItemDAO woScheduleWorkItemDAO;

    @Autowired
    WoScheduleCheckListDAO woScheduleCheckListDAO;

    @Autowired
    WoScheduleConfigDAO woScheduleConfigDAO;

    static Logger LOGGER = LoggerFactory.getLogger(WoServiceImpl.class);

    @Autowired
    WoFTConfig5SDAO woFTConfig5SDAO;

    @Autowired
    WoTaskDailyDAO woTaskDailyDAO;

    @Autowired
    WoTaskMonthlyDAO woTaskMonthlyDAO;

    @Autowired
    WoXdddChecklistDAO woXdddChecklistDAO;

    @Autowired
    WoConfigContractDAO woConfigContractDAO;

    @Autowired
    WoMappingStationDAO woMappingStationDAO;
    
    @Autowired
    EffectiveCalculationDetailsDAO effectiveCalculationDetailsDAO;
    
    @Autowired
    EffectiveCalculationDetailsBusinessImpl effectiveCalculationDetailsBusinessImpl;
    //duonghv13 add 15102021
    @Autowired
    ManagementCertificateBusinessImpl managementCertificateBusinessImpl;

    @Autowired
    CatStationDAO catStationDAO;

    @Autowired
    WoTrMappingStationDAO woTrMappingStationDAO;
    
    //duonghv13 add 13102021
    @Autowired
    ManageCertificateDAO manageCertificateDAO;
    @Autowired
    ManageCareerDAO manageCareerDAO;

    @Autowired
    UtilAttachDocumentDAO attachDocumentDAO;

    @Autowired
    CntContractBusinessImpl cntContractBusinessImpl;

    @Autowired
    private CntContractDAO cntContractDAO;


    @Override
    public Response create(WoDTO woDto) throws Exception {
        String returnStr = woDAO.save(woDto.toModel());
        return Response.ok(returnStr).build();
    }

    @Override
    public Response createMany(List<WoDTO> woDtos) throws Exception {
        List<WoBO> woBos = new ArrayList<WoBO>();
        for (WoDTO item : woDtos) {
            woBos.add(item.toModel());
        }
        String returnStr = woDAO.saveList(woBos);
        return Response.ok(returnStr).build();
    }

    @Override
    public Response update(WoDTO woDto) throws Exception {
        String returnStr = woDAO.update(woDto.toModel());
        return Response.ok(returnStr).build();
    }

    @Override
    public Response delete(long woId) throws Exception {
//        woDao.delete(woId);
        return Response.ok("SUCCESS").build();
    }

    @Override
    public Response getById(long woId) throws Exception {
        return null;
    }

    @Override
    public Response getByRange(int pageNumber, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Response doSearchReport(ReportWoDTO obj) {
        DataListDTO data = woBusinessImpl.doSearchReport(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response exportFile(ReportWoDTO obj) throws Exception {
        try {
            String strReturn = woBusinessImpl.exportFile(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Response doSearchBaoCaoChamDiemKpi(ReportWoDTO obj) {
        DataListDTO ls = woBusinessImpl.doSearchBaoCaoChamDiemKpi(obj);
        return Response.ok(ls).build();
    }

    public Response exportFileWoKpi(ReportWoDTO obj) {
        try {
            String strReturn = woBusinessImpl.exportFileWoKpi(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Response createWO(WoDTO dto) {
        BaseResponseOBJ resp;
        boolean isImporting = false;
        try {
            if (woBusinessImpl.createNewWO(dto, isImporting)) {
                if ("HSHC".equalsIgnoreCase(dto.getWoTypeCode()) && dto.getCatConstructionTypeId() == 8)
                    woBusinessImpl.tryUpdateWorkItem(dto.getXdddHshcChecklistItem(), dto.getWoId());
                resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
            } else {
                resp = new BaseResponseOBJ(ERROR_CODE, dto.getCustomField(), null);
            }
        } catch (Exception e) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            e.printStackTrace();
        }

        return Response.ok(resp).build();
    }

    private void updateInfoWO(WoDTO dto) throws ParseException {
        if(dto.isAutoCreate()){
            WoXdddChecklistRequestDTO xdddChecklistRequestDTO = new WoXdddChecklistRequestDTO();
            xdddChecklistRequestDTO.setName(dto.getChecklistItemNames());
            List<WoXdddChecklistRequestDTO> xdddChecklistRequestDTOList = new ArrayList<>();
            xdddChecklistRequestDTOList.add(xdddChecklistRequestDTO);
            dto.setXdddChecklist(xdddChecklistRequestDTOList);
            // ngày tạo
            dto.setCreatedDate(new Date());
            if(!ObjectUtils.isEmpty(dto.getFinishDateStr())) {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dto.getFinishDateStr());
                Calendar c1 = Calendar.getInstance();
                c1.setTime(date);
                c1.roll(Calendar.DATE, 5);
                Date finishDate = c1.getTime();
                dto.setFinishDate(finishDate);
            }
        }
    }

    @Override
    public Response createManyWOReport(List<WoDTO> woDtos) throws Exception {
        File importResult = woBusinessImpl.genImportResultExcelFile(woDtos);
        return Response.ok(importResult).build();
    }

    @Override
    public Response createManyHcqtWOReport(List<WoHcqtDTO> woDtos) throws Exception {
        File importResult = woBusinessImpl.genImportHcqtResultExcelFile(woDtos);
        return Response.ok(importResult).build();
    }

//    @Override
//    public Response createManyWO(List<WoDTO> woDtos, HttpServletRequest request) throws Exception {
//        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
//                Constant.AdResourceKey.WOXL, request);
//        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
//        boolean isCnkt = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
//                Constant.AdResourceKey.CNKT_WOXL, request);
//
//        //Huypq-26082020-start
//        boolean checkRoleChiPhi = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
//				Constant.AdResourceKey.WOXL_CHIPHI, request);
//        //Huy-end
//
//        BaseResponseOBJ resp;
//        if (woDtos == null || woDtos.size() == 0) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        try {
//
//            int total = 0;
//            if (woBusinessImpl.validateImportData(woDtos, groupIdList, isCnkt)) {
//                boolean isImporting = true;
//                for (WoDTO item : woDtos) {
//                    if (woBusinessImpl.createNewWO(item, isImporting)) total++;
//                }
//
//                resp = new BaseResponseOBJ(SUCCESS_CODE, String.valueOf(total), woDtos);
//            } else {
//                resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, woDtos);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//        return Response.ok(resp).build();
//    }

    @Override
    public Response createManyWO(List<WoDTO> woDtos, HttpServletRequest request) throws Exception {
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.WOXL, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        boolean isCnkt = VpsPermissionChecker.hasPermission(Constant.OperationKey.CRUD,
                Constant.AdResourceKey.CNKT_WOXL, request);
        if (isCnkt) {
            String groupIdCnkt = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CRUD,
                    Constant.AdResourceKey.CNKT_WOXL, request);
            groupIdList = ConvertData.convertStringToList(groupIdCnkt, ",");
        }

        //Huypq-26082020-start
        boolean checkRoleChiPhi = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.WOXL_CHIPHI, request);
        //Huy-end

        boolean isRevenueRole = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.WOXL_DOANHTHU, request);
        if (isRevenueRole) {
            String groupIdRevenue = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                    Constant.AdResourceKey.WOXL_DOANHTHU, request);
            groupIdList = ConvertData.convertStringToList(groupIdRevenue, ",");
        }

        BaseResponseOBJ resp;
        if (woDtos == null || woDtos.size() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {

            int total = 0;
            if (woBusinessImpl.validateImportData(woDtos, groupIdList, isCnkt, isRevenueRole)) {
                boolean isImporting = true;
                for (WoDTO item : woDtos) {
                    if (woBusinessImpl.createNewWO(item, isImporting)) total++;
                }

                resp = new BaseResponseOBJ(SUCCESS_CODE, String.valueOf(total), woDtos);
            } else {
                resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, woDtos);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response updateWO(WoDTO woDto) throws Exception {
        Gson gson = new Gson();
        WoBO bo = woDAO.getOneRaw(woDto.getWoId());
        bo.setWoNameId(woDto.getWoNameId());
        bo.setWoName(woDto.getWoName());
        bo.setMoneyValue(woDto.getMoneyValue());
        bo.setTotalMonthPlanId(woDto.getTotalMonthPlanId());
        bo.setFinishDate(woDto.getFinishDate());
        bo.setQoutaTime(woDto.getQoutaTime());
        //duonghv13-start 20092021
        if ("XLSC".equalsIgnoreCase(woDto.getWoTypeCode())) 
        	bo.setDescription(woDto.getDescription());
        //duong-end
        //thu hồi dòng tiền
        if (StringUtils.isNotEmpty(woDto.getMoneyFlowBill())) bo.setMoneyFlowBill(woDto.getMoneyFlowBill());
        if (woDto.getMoneyFlowValue() != null) bo.setMoneyFlowValue(woDto.getMoneyFlowValue());
        if (woDto.getMoneyFlowRequired() != null) bo.setMoneyFlowRequired(woDto.getMoneyFlowRequired());
        if (woDto.getMoneyFlowDate() != null) bo.setMoneyFlowDate(woDto.getMoneyFlowDate());
        if (StringUtils.isNotEmpty(woDto.getMoneyFlowContent())) bo.setMoneyFlowContent(woDto.getMoneyFlowContent());

        //hoàn công quyết toán
        if (StringUtils.isNotEmpty(woDto.getStationCode())) bo.setStationCode(woDto.getStationCode());
//        {
//            WoDTO checkDto = new WoDTO();
//            checkDto.setStationCode(woDto.getStationCode());
//            checkDto.setHcqtContractCode(woDto.getHcqtContractCode());
//            List<WoDTO> resultCheck = woDAO.doSearch(checkDto, null);
//            if(resultCheck == null || resultCheck.size()==0){
//                BaseResponseOBJ resp = new BaseResponseOBJ(ERROR_CODE, "Mã trạm và hợp đồng HCQT đã tồn tai", null);
//            }
//
//        }
        if (woDto.getHcqtProjectId() != null) bo.setHcqtProjectId(woDto.getHcqtProjectId());
        if (StringUtils.isNotEmpty(woDto.getHcqtContractCode())) bo.setHcqtContractCode(woDto.getHcqtContractCode());
        if (woDto.getHshcReceiveDate() != null) bo.setHshcReceiveDate(woDto.getHshcReceiveDate());
        if (StringUtils.isNotEmpty(woDto.getCatProvinceCode())) bo.setCatProvinceCode(woDto.getCatProvinceCode());
        if (StringUtils.isNotEmpty(woDto.getCnkv())) bo.setCnkv(woDto.getCnkv());
        if (woDto.getFtId() != null) bo.setFtId(woDto.getFtId());
        if (StringUtils.isNotEmpty(woDto.getFtName())) bo.setFtName(woDto.getFtName());
        if (StringUtils.isNotEmpty(woDto.getFtEmail())) bo.setFtEmail(woDto.getFtEmail());

        if ("DOANHTHU".equalsIgnoreCase(woDto.getWoTypeCode())) bo.setState(PROCESSING);

        if (woDto.getPartner() != null) bo.setPartner(woDto.getPartner());
        if (woDto.getVtnetWoCode() != null) bo.setVtnetWoCode(woDto.getVtnetWoCode());
        if (woDto.getDescription() != null) bo.setDescription(woDto.getDescription());

        String returnStr = woDAO.update(bo);
        woBusinessImpl.logWoWorkLogs(woDto, "1", "Đã cập nhật WO", gson.toJson(woDto.toModel()), woDto.getLoggedInUser());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response deleteWO(WoDTO woDto, HttpServletRequest request) throws Exception {
        Gson gson = new Gson();
        BaseResponseOBJ resp;

        WoBO woBO = woDAO.getOneRaw(woDto.getWoId());
        String woTypeCode = woDAO.getWoTypeCodeByWoId(woDto.getWoId());
        if (woBO == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        int rowAffected = woDAO.delete(woDto.getWoId());
        if (rowAffected > 0) {
            woDto.setState("DELETED");
            woDto.setStatus(0l);
            woBusinessImpl.logWoWorkLogs(woBO.toDTO(), "1", "Xóa wo " + woBO.getWoCode(), gson.toJson(woBO), woDto.getLoggedInUser());
            //HienLT56 start 01062021
            if(StringUtils.isNotBlank(woTypeCode) && woTypeCode.equals("THICONG")) {
            	woBusinessImpl.checkWOThiCongItem(woDto.getWoId());
            }
            //Trong bảng WO_TASK_DAILY
            woBusinessImpl.checkWOCttGpon(woDto.getWoId());
            
            if(StringUtils.isNotBlank(woTypeCode) && woTypeCode.equals("THDT")) {
            	woBusinessImpl.checkWOThdt(woDto.getWoId());
            }
            woBusinessImpl.checkWOMcl(woDto.getWoId());
            woBusinessImpl.checkWoXdddCl(woDto.getWoId());
            //HienLT56 end 01062021
            
            boolean isAdminDelete = VpsPermissionChecker.hasPermission(Constant.OperationKey.DELETE, Constant.AdResourceKey.ADMIN_WO, request);
            if(isAdminDelete && StringUtils.isNotBlank(woTypeCode) && woTypeCode.equals("TMBT")) {
            	woDAO.deleteTrWhenDeleteWo(woBO.getTrId());
            }
        }

        //reset lại hạng mục của xddd để có thể tạo lại hshc
        woBusinessImpl.tryResetXdddHshc(woDto.getWoId());

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, rowAffected);

        return Response.ok(resp).build();
    }

    @Override
    public Response getOneWODetails(WoDTO woDto) throws Exception {

        Long woId = woDto.getWoId();
        BaseResponseOBJ resp;

        if (woId == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        woDto = woBusinessImpl.getOneWoDetails(woId);

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, woDto);
        return Response.ok(resp).build();
    }
    
    @Override
	public Response getListItem(String code) throws Exception {
    	BaseResponseOBJ resp;
		List<WorkItemDTO> lst = woBusinessImpl.getListItem(code);
		resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, lst);
        return Response.ok(resp).build();
	}

    @Override
    public Response getListWODetails(WoDTO woDto) throws Exception {
        if (woDto == null || woDto.getPage() <= 0 || woDto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoDTO> listWoDto = woDAO.getByRange(woDto.getPage(), woDto.getPageSize());

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listWoDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearch(WoDTO woDto, HttpServletRequest request) throws Exception {
        String groupIdViewRole = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.WOXL, request);
        List<String> groupIdListView = ConvertData.convertStringToList(groupIdViewRole, ",");

        boolean isTcTct = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.TC_TCT, request);
        boolean isTcBranch = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.TC_BRANCH, request);
        String groupIdTcBranchRole = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.APPROVED,
                Constant.AdResourceKey.TC_BRANCH, request);
        List<String> groupIdListTcBranch = ConvertData.convertStringToList(groupIdTcBranchRole, ",");

        List<String> contractBranch = makeContractBranchs(groupIdListTcBranch);

        if (StringUtils.isNotEmpty(woDto.getLoggedInUser()) && !isTcBranch && !isTcTct) {
            long sysUserId = trDAO.getSysUserId(woDto.getLoggedInUser());
            woDto.setFtId(sysUserId);
        }

        List<WoDTO> listWoDto = woDAO.doSearch(woDto, groupIdListView, isTcTct, isTcBranch, contractBranch);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listWoDto);
        resp.setTotal(woDto.getTotalRecord());
        return Response.ok(resp).build();
    }

    private List<String> makeContractBranchs(List<String> groupIdListTcBranch) {
        //từ miền dữ liệu tài chính trụ gán với contract_branch của hợp đồng xl
        List<String> contractBranchs = new ArrayList<>();
        if (groupIdListTcBranch.size() > 0) {
            for (String branch : groupIdListTcBranch) {
                if (TTGPTH_ID.equalsIgnoreCase(branch)) contractBranchs.add("1");
                if (TTHT_ID.equalsIgnoreCase(branch)) contractBranchs.add("2");
                if (TTXDDTHT_ID.equalsIgnoreCase(branch)) contractBranchs.add("3");
                if (TTVHKT_ID.equalsIgnoreCase(branch)) contractBranchs.add("4");
                if (TTCNTT_ID.equalsIgnoreCase(branch)) contractBranchs.add("5");
            }
        }

        return contractBranchs;
    }

    @Override
    public Response checkWoCompleteToUpdateTr(WoDTO woDto) throws Exception {
        List<WoDTO> listWoDto = woDAO.getListWo(woDto);
        List<WoDTO> listWoFilter = new ArrayList<>();
        boolean res;

        for (WoDTO item : listWoDto) {
            if (item.getState().equals("OK")) {
                listWoFilter.add(item);
            }
        }
        if (listWoDto.size() == listWoFilter.size()) {
            WoTrBO trBo = trDAO.getOneRaw(woDto.getTrId());
            trBo.setState("DONE");
            String returnStr = trDAO.update(trBo.toDTO().toModel());
            res = true;
        } else {
            res = false;
        }
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, res);
        return Response.ok(resp).build();
    }

//    @Override
//    public Response doSearchCreated(WoDTO woDto) throws Exception {
//        if (woDto == null || woDto.getPage() <= 0 || woDto.getPageSize() <= 0 || woDto.getLoggedInUser() == null) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        List<WoDTO> listWoDto = woDAO.doSearch(woDto, CREATED_TYPE);
//        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listWoDto);
//        return Response.ok(resp).build();
//    }
//
//    @Override
//    public Response doSearchAssignedCD(WoDTO woDto) throws Exception {
//        if (woDto == null || woDto.getPage() <= 0 || woDto.getPageSize() <= 0 || woDto.getLoggedInUser() == null) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        List<WoDTO> listWoDto = woDAO.doSearch(woDto, ASSIGNED_CD_TYPE);
//        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listWoDto);
//        return Response.ok(resp).build();
//    }
//
//    @Override
//    public Response doSearchAssignedFT(WoDTO woDto) throws Exception {
//        if (woDto == null || woDto.getPage() <= 0 || woDto.getPageSize() <= 0 || woDto.getFtId() <= 0) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        List<WoDTO> listWoDto = woDAO.doSearch(woDto, ASSIGNED_FT_TYPE);
//        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listWoDto);
//        return Response.ok(resp).build();
//    }

    @Override
    public Response giveWOAssignment(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp;
        //duonghv13 add 15102021//
        try {
        	boolean result =false;
        	if( woDto.getFtId()!= null) {
	        	if(woDto.getWoTypeCode().equalsIgnoreCase("XLSC") == true || woDto.getWoTypeCode().equalsIgnoreCase("NLMT_BDDK") || woDto.getWoTypeCode().equalsIgnoreCase("NLMT_KTDK")|| woDto.getWoTypeCode().equalsIgnoreCase("NLMT_XLSC")){   	 
	        		ManageCertificateDTO temp  = new ManageCertificateDTO();
	                temp.setSysUserId(woDto.getFtId());
	                temp.setWoIdList(woDto.getWoTypeId().toString());
	                List<ManageCareerDTO> listCareerSearch = manageCareerDAO.findByWoIdList(woDto.getWoTypeId());
		            if(listCareerSearch == null || listCareerSearch.size() ==0) {
		            	result = false;
		            }else {
		            	List<ManageCertificateDTO> ls = manageCertificateDAO.getListCertificateEnableFT(temp);
			    		if(ls.size() == 0) {
			    			result = true;
			    		}else result = false;
		            }
	        		 if(result == true) 
	        				throw new IllegalArgumentException("FT không có đủ chứng chỉ thỏa mãn hoặc chứng chỉ đã hết hạn!");
	        	 }
        	}
        	//duonghv end 15102021//
             boolean assignResult = woBusinessImpl.giveAssignment(woDto);

             if (assignResult) {
            	 if(woDto.getState() != null){
            		 woBusinessImpl.updateWoMapingPlan(woDto.getWoId());
            	 }
                 resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
             }
             else {
                 String errorMsg = woDto.getCustomField();
                 if (StringUtils.isEmpty(errorMsg)) errorMsg = ERROR_MSG;
                 resp = new BaseResponseOBJ(ERROR_CODE, errorMsg, null);
             }
             return Response.ok(resp).build();
             
        } catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
    }

    @Override
    public Response acceptWO(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp;
        if (woDto.getWoId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean acceptResult = woBusinessImpl.acceptWo(woDto);
        if (acceptResult)
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response rejectWO(WoDTO woDto) throws Exception {
        String assignState = "";
        boolean acceptResult = false;
        BaseResponseOBJ resp;

        if (ObjectUtils.isEmpty(woDto.getWoId())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        WoBO bo = woDAO.getOneRaw(woDto.getWoId());

        if (bo.getFtId() != null) {
            assignState = REJECT_FT;
            woDto.setState(assignState);
            acceptResult = woBusinessImpl.changeWoState(woDto, assignState);
        } else {
            assignState = REJECT_CD;
            woDto.setState(assignState);
            acceptResult = woBusinessImpl.cdRejectAssignment(woDto);
        }

        if (acceptResult)
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response cdAcceptAssignment(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp;

        if (woDto.getWoId() <= 0 || woDto.getLoggedInUser() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean cdAcceptResult = woBusinessImpl.cdAcceptAssignment(woDto);
        if (cdAcceptResult)
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response cdRejectAssignment(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp;

        if (woDto.getWoId() <= 0 || woDto.getLoggedInUser() == null || woDto.getAssignedCd() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean cdRejectResult = woBusinessImpl.cdRejectAssignment(woDto);
        if (cdRejectResult)
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response giveAssignmentToFT(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp;

        if (woDto.getWoId() <= 0 || woDto.getLoggedInUser() == null || woDto.getFtId() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean giveAssignmentResult = woBusinessImpl.giveAssignmentToFT(woDto);
        if (giveAssignmentResult)
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response ftRejectAssignment(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp = woBusinessImpl.changeWoStateWrapper(woDto, REJECT_FT);
        return Response.ok(resp).build();
    }

    @Override
    public Response ftAcceptAssignment(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp = woBusinessImpl.changeWoStateWrapper(woDto, ACCEPT_FT);
        return Response.ok(resp).build();
    }

    @Override
    public Response ftProcessing(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp = woBusinessImpl.changeWoStateWrapper(woDto, PROCESSING);
        return Response.ok(resp).build();
    }

    @Override
    public Response ftDone(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp = woBusinessImpl.changeWoStateWrapper(woDto, DONE);
        return Response.ok(resp).build();
    }

    @Override
//    tinh duyet wo(danh gia ket qua cv)
    public Response changeStateCdOk(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, CD_OK);

        if(woDto.getWoTypeCode().equals("THICONG") && woDto.getCdLevel1().equals("242656") && woDto.getApConstructionType() == 8) {
    		woBusinessImpl.updateConstruction(woDto.getConstructionId());
        }
        if(null != woDto.getAttachFile() && null != woDto.getLicenceName()) {
        	woBusinessImpl.updateLicenceName(woDto);

        	List<Long> idCheck = attachDocumentDAO.getAttachIdByObjectAndType(woDto.getWoId(), "LICENCE");
        	UtilAttachDocumentDTO attachFile = woDto.getAttachFile();
 	       	attachFile.setObjectId(woDto.getWoId());
 	       	attachFile.setType("LICENCE");
        	if(idCheck.size() > 0) {
        		attachFile.setUtilAttachDocumentId(idCheck.get(0));
        		Long id2 = (Long) attachDocumentDAO.updateObject(attachFile.toModel());
        	}else {
        		Long id2 = (Long) attachDocumentDAO.saveObject(attachFile.toModel());
        	}

        }
        if(null != woDto.getAttachFile() && null != woDto.getLicenceName()) {
        	woBusinessImpl.updateLicenceName(woDto);

        	List<Long> idCheck = attachDocumentDAO.getAttachIdByObjectAndType(woDto.getWoId(), "LICENCE");
        	UtilAttachDocumentDTO attachFile = woDto.getAttachFile();
 	       	attachFile.setObjectId(woDto.getWoId());
 	       	attachFile.setType("LICENCE");
        	if(idCheck.size() > 0) {
        		attachFile.setUtilAttachDocumentId(idCheck.get(0));
        		Long id2 = (Long) attachDocumentDAO.updateObject(attachFile.toModel());
        	}else {
        		Long id2 = (Long) attachDocumentDAO.saveObject(attachFile.toModel());
        	}
        }
        WoBO bo = woDAO.getWoById(woDto);
        if(Constant.WOTypeCode.THI_CONG.equals(woDto.getWoTypeCode()) && Constant.CatWorkItemTypeId.KHOI_CONG.equals(bo.getCatWorkItemTypeId())) {
            String errMessage = woBusinessImpl.isValidDeploymentDateReality(woDto.getDeploymentDateReality());
            // update ngay khoi cong thuc te cua hop dong
            if (ObjectUtils.isEmpty(errMessage)) {
                CntContractBO contractBO = cntContractBusinessImpl.getById(woDto.getContractId());
                if (!ObjectUtils.isEmpty(contractBO)) {
                    contractBO.setDeploymentDateReality(woDto.getDeploymentDateReality());
                    cntContractDAO.update(contractBO);
                    return Response.ok(woDto).build();
                }
            } else resp = new BaseResponseOBJ(ERROR_CODE, errMessage, null);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response changeStateCdNg(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, CD_NG);
        if(!Constant.WO_TYPE_CODE.BGBTS_DTHT.equalsIgnoreCase(woDto.getWoTypeCode())){
            //taotq start 16092021
            if (woDto.getWoId() == 82L || woDto.getWoId() == 101L) {
                woBusinessImpl.updateWoCheckList(woDto.getWoId());
            } else {
                woBusinessImpl.updateWoMappingCheckList(woDto.getWoId());
            }
        }
      //taotq end 16092021
        return Response.ok(resp).build();
    }

    @Override
//  tru duyet wo(ghi nhan san luong)
    public Response changeStateOk(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, OK);
//        taotq start 2001022
        if(woDto.getApConstructionType() != null && woDto.getApConstructionType() == 7 && woDto.getWoTypeCode()!=null && woDto.getWoTypeCode().equalsIgnoreCase("DBHT")) {
        	List<WoDTO> lstWo = woBusinessImpl.getCD(woDto.getConstructionId());
        	if(lstWo.size() > 0) {
        		woDto.setCdLevel1(lstWo.get(0).getCdLevel1());
            	woDto.setCdLevel1Name(lstWo.get(0).getCdLevel1Name());
            	woDto.setCdLevel2(lstWo.get(0).getCdLevel2());
            	woDto.setCdLevel2Name(lstWo.get(0).getCdLevel2Name());
        	}
        	Long id = woBusinessImpl.CreateWOHTCTDR(woDto);
        	
        }
        if(woDto.getWoTypeCode().equals("THICONG") && woDto.getCdLevel1().equals("242656") && woDto.getApConstructionType() == 8) {
        		woBusinessImpl.updateConstruction(woDto.getConstructionId());
        }
//      taotq end 2001022
        return Response.ok(resp).build();
    }

    @Override
    public Response changeStateNg(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, NG);

        return Response.ok(resp).build();
    }

    @Override
    public Response ftOpinionRequest(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp = woBusinessImpl.changeWoStateWrapper(woDto, OPINION_RQ);
        return Response.ok(resp).build();
    }

    @Override
    @Transactional
    public Response getCurrentMonthPlan(WoDTO woDto) throws Exception {
        List<Long> monthList = new ArrayList<Long>();
        Date curDate = new Date();
        Long curMonth = (long) curDate.getMonth() + 1;
        monthList.add(curMonth);
        TotalMonthPlanOSSimpleDTO monthPlanSearch = new TotalMonthPlanOSSimpleDTO();
        monthPlanSearch.setMonthList(monthList);
        monthPlanSearch.setPage((long) 1);
        monthPlanSearch.setPageSize(10);
        List<TotalMonthPlanOSDTO> result = totalMonthPlanOSDAO.doSearch(monthPlanSearch);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, result);
        return Response.ok(resp).build();
    }

    @Override
    public Response getAppWorkSrcs(WoDTO woDto) throws Exception {
        List<WoAppParamDTO> appWorkSrc = woDAO.getAppParam(AP_WORK_SRC);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, appWorkSrc);
        return Response.ok(resp).build();
    }

    @Override
    public Response getAppConstructionTypes(WoDTO woDto) throws Exception {
        List<WoAppParamDTO> appWorkSrc = woDAO.getAppParam(AP_CONSTRUCTION_TYPE);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, appWorkSrc);
        return Response.ok(resp).build();
    }

    @Override
    public Response getConstructions(WoDTO woDto) throws Exception {
        List<WoSimpleConstructionDTO> listCons = woDAO.suggestConstructions(woDto.getKeySearch());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listCons);
        return Response.ok(resp).build();
    }

    @Override
    public Response getCdLv1List(WoDTO woDto) throws Exception {
        List<WoCdDTO> cdsLv1 = woDAO.getCdLevel1();
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, cdsLv1);
        return Response.ok(resp).build();
    }

    @Override
    public Response getCdLv2List(WoDTO woDto) throws Exception {
        int level = 2;
        Long higherCd = null;

        boolean isGeoArea = false;

//        String geoArea = woDto.getGeoArea();

//
//        if (StringUtils.isNotEmpty(geoArea)) isGeoArea = true;

        List<String> geoAreaList = woDto.getGeoAreaList();
        if (geoAreaList != null) {
            if (geoAreaList.size() > 0) isGeoArea = true;
        }

        List<WoSimpleSysGroupDTO> cdsLv2 = woDAO.getCds(level, higherCd, geoAreaList, isGeoArea);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, cdsLv2);
        return Response.ok(resp).build();
    }

    @Override
    public Response getCdLv3List(WoDTO woDto) throws Exception {
        if (StringUtils.isEmpty(woDto.getCdLevel2())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoSimpleSysGroupDTO> cdsLv3 = woBusinessImpl.getCdLv3List(woDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, cdsLv3);
        return Response.ok(resp).build();
    }

    @Override
    public Response getCdLv4List(WoDTO woDto) throws Exception {
        if (StringUtils.isEmpty(woDto.getCdLevel3()) && StringUtils.isEmpty(woDto.getCdLevel2())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoSimpleSysGroupDTO> cdsLv4 = woBusinessImpl.getCdLv4List(woDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, cdsLv4);
        return Response.ok(resp).build();
    }

    @Override
    public Response getCatWorkTypes(WoDTO woDto) throws Exception {
        List<WoCatWorkItemTypeDTO> workItemTypes = woDAO.getCatWorkTypes(woDto.getCatConstructionTypeId());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, workItemTypes);
        return Response.ok(resp).build();
    }

    @Override
    public Response getFtList(WoDTO woDto) throws Exception {
        List<WoSimpleFtDTO> ftList = woBusinessImpl.getFtList(woDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, ftList);
        return Response.ok(resp).build();
    }

    @Override
    public Response getFtListFromLv2SysGroup(WoSimpleFtDTO ft) throws Exception {

        Long groupId = ft.getSysGroupId();

        List<WoSimpleFtDTO> ftList = woDAO.getFtListFromLv2SysGroup(groupId, ft.getKeySearch());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, ftList);
        return Response.ok(resp).build();
    }

    @Override
    public Response getCheckList(WoDTO dto) throws Exception {
        Long woId = dto.getWoId();
        String woTypeCode = dto.getWoTypeCode();

        if (woId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoMappingChecklistDTO> checkList = woBusinessImpl.getListChecklistsOfWoForWeb(woId, woTypeCode);


        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, checkList);
        return Response.ok(resp).build();
    }

    //end WO api

    //start opinionType api
    @Override
    public Response createOpinionType(WoOpinionTypeDTO opinionTypeDto) throws Exception {
        String returnStr = opinionTypeDAO.save(opinionTypeDto.toModel());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response createManyOpinionType(List<WoOpinionTypeDTO> opinionTypeDtoList) throws Exception {
        List<WoOpinionTypeBO> boList = new ArrayList<WoOpinionTypeBO>();
        for (WoOpinionTypeDTO item : opinionTypeDtoList) {
            boList.add(item.toModel());
        }
        String returnStr = opinionTypeDAO.saveListNoId(boList);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response updateOpinionType(WoOpinionTypeDTO opinionTypeDto) throws Exception {
        String returnStr = opinionTypeDAO.update(opinionTypeDto.toModel());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response deleteOpinionType(WoOpinionTypeDTO opinionTypeDto) throws Exception {
        opinionTypeDAO.delete(opinionTypeDto.getOpinionTypeId());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchOpinionType(WoOpinionTypeDTO dto) throws Exception {
        if (dto == null || dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoOpinionTypeDTO> listWoOpinionTypeDto = opinionTypeDAO.doSearch(dto);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listWoOpinionTypeDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response getOneDetails(WoOpinionTypeDTO opinionTypeDto) throws Exception {
        long opinionTypeId = opinionTypeDto.getOpinionTypeId();
        BaseResponseOBJ resp;

        if (opinionTypeId <= 0) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        opinionTypeDto = opinionTypeDAO.getOneDetails(opinionTypeId);

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, opinionTypeDto);
        return Response.ok(resp).build();
    }

    //end opinionType api

    //start api woType
    @Override
    public Response createWOType(WoTypeDTO dto) throws Exception {
        BaseResponseOBJ resp;
        boolean checkExistWoTypeCode = woTypeDAO.checkExistWoTypeCode(dto.getWoTypeCode());
        if (checkExistWoTypeCode) {
            String returnStr = woTypeDAO.save(dto.toModel());
            resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, "Mã loại đã tồn tại.", null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response createManyWOType(List<WoTypeDTO> woTypeDTOList) throws Exception {
        List<WoTypeBO> bos = new ArrayList<WoTypeBO>();
        for (WoTypeDTO item : woTypeDTOList) {
            bos.add(item.toModel());
        }
        String returnStr = woTypeDAO.saveListNoId(bos);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response updateWOType(WoTypeDTO woTypeDTO) throws Exception {
        BaseResponseOBJ resp;
        Long woTypeId = woTypeDTO.getWoTypeId();
        boolean isEditable = woTypeDAO.checkDeleteEditable(woTypeId);

        if (isEditable == true) {
            String returnStr = woTypeDAO.update(woTypeDTO.toModel());
            resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response deleteWOType(WoTypeDTO woTypeDTO) throws Exception {
        BaseResponseOBJ resp = null;
        Long woTypeId = woTypeDTO.getWoTypeId();

        boolean isDeletable = woTypeDAO.checkDeleteEditable(woTypeId);

        if (isDeletable == true) {
            woTypeDAO.delete(woTypeId);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getWOTypeById(WoTypeDTO woTypeDTO) throws Exception {
        long woTypeId = woTypeDTO.getWoTypeId();

        WoTypeBO woTypeBO = woTypeDAO.getOneRaw(woTypeId);
        if (woTypeBO == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        WoTypeDTO dto = woTypeBO.toDTO();
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dto);

        return Response.ok(resp).build();
    }

    @Override
    public Response getList(WoTypeDTO dto) throws Exception {
        if (dto == null || dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoTypeDTO> listWoTypeDto = woTypeDAO.getByRange(dto.getPage(), dto.getPageSize());

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listWoTypeDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchWOType(WoTypeDTO dto) throws Exception {
        if (dto == null || dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoTypeDTO> fileList = woTypeDAO.doSearch(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, fileList);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();

    }
    //end api woType


    //start api for file attachments
    @Override
    public Response createWOMappingAttach(WoMappingAttachDTO dto) throws Exception {
        Date date = new Date();
        dto.setCreatedDate(date);
        String returnStr = woMappingAttachDAO.save(dto.toModel());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response updateWOMappingAttach(WoMappingAttachDTO dto) throws Exception {
        String returnStr = woMappingAttachDAO.update(dto.toModel());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response deleteWOMappingAttach(WoMappingAttachDTO dto) throws Exception {
        Long id = dto.getId();
        woMappingAttachDAO.delete(id);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchWOMappingAttach(WoMappingAttachDTO dto) throws Exception {
        if (dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        dto.setPageSize(100);
        List<WoMappingAttachDTO> fileList = woMappingAttachDAO.doSearch(dto);
        List<WoMappingAttachDTO> finalFileList = new ArrayList<>();
        for (WoMappingAttachDTO file : fileList) {
            if (file.getChecklistId() == null) finalFileList.add(file);
        }
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, finalFileList);
        return Response.ok(resp).build();
    }

    //end api for file attachments


    //start api for worklogs

    @Override
    public Response doSearchWorkLogs(WoDTO dto) throws Exception {
        long woId = dto.getWoId();
        List<WoWorkLogsBO> logs = woWorkLogsDAO.doSearch(woId);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, logs);
        return Response.ok(resp).build();
    }


//    @Override
//    public Response createWorkLogs(WOWorkLogsDTO dto) throws Exception {
//        String returnStr = woWorkLogsDAO.save(dto.toModel());
//        return Response.ok(returnStr).build();
//    }
//
//    @Override
//    public Response updateWorkLogs(WOWorkLogsDTO dto) throws Exception {
//        String returnStr = woWorkLogsDAO.update(dto.toModel());
//        return Response.ok(returnStr).build();
//    }
//
//    @Override
//    public Response deleteWorkLogs(WOWorkLogsDTO dto) throws Exception {
//        long workLogsId = dto.getWorkLogsId();
//
//        WOWorkLogsBO workLogsBO = woWorkLogsDAO.getOneRaw(workLogsId);
//
//        if(workLogsBO == null){
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        woWorkLogsDAO.delete(workLogsId);
//        return Response.ok("SUCCESS").build();
//    }
    //end api for worklogs

    //start opinionType api
    @Override
    public Response createOpinion(WoOpinionDTO opinionDto) throws Exception {
        String returnStr = opinionDAO.save(opinionDto.toModel());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response createManyOpinion(List<WoOpinionDTO> opinionDtoList) throws Exception {
        List<WoOpinionBO> boList = new ArrayList<WoOpinionBO>();
        for (WoOpinionDTO item : opinionDtoList) {
            boList.add(item.toModel());
        }
        String returnStr = opinionDAO.saveListNoId(boList);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response updateOpinion(WoOpinionDTO opinionDto) throws Exception {
        String returnStr = opinionDAO.update(opinionDto.toModel());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response deleteOpinion(WoOpinionDTO opinionDto) throws Exception {
        opinionDAO.delete(opinionDto.getOpinionId());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchOpinion(WoOpinionDTO opinionDto) throws Exception {
        List<WoOpinionDTO> listOpinion = opinionDAO.doSearch(opinionDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listOpinion);
        return Response.ok(resp).build();
    }

    //end opinionType api

    //-------
    //start api for user

    @Override
    public Response getSysUserGroup(WoDTO dto) throws Exception {
        String loggedInUser = dto.getLoggedInUser();

        if (StringUtils.isEmpty(loggedInUser)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        WoSimpleSysGroupDTO sysGroup = trDAO.getSysUserGroup(loggedInUser);
        boolean isCdLevel1 = trDAO.checkIsCdLevel1(sysGroup.getSysGroupId());
        boolean isTrCreator = trDAO.checkIsTrCreator(sysGroup.getSysGroupId());

        if (isCdLevel1) sysGroup.setCdLevel1(true);
        if (isTrCreator) sysGroup.setTrCreator(true);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, sysGroup);

        return Response.ok(resp).build();
    }

    @Override
    public Response getImportExcelTemplate(WoDTO woDto, HttpServletRequest request) throws Exception {
        List<String> groupIdList = new ArrayList<>();
        String groupId = "";
        boolean isCnkt = false;
        boolean isRevenueRole = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.WOXL_DOANHTHU, request);
        if (isRevenueRole) {
            groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE, Constant.AdResourceKey.WOXL_DOANHTHU, request);
        } else {
            isCnkt = VpsPermissionChecker.hasPermission(Constant.OperationKey.CRUD, Constant.AdResourceKey.CNKT_WOXL, request);
            if (isCnkt) {
                groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CRUD, Constant.AdResourceKey.CNKT_WOXL, request);
            } else {
                groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE, Constant.AdResourceKey.WOXL, request);
            }
        }

        boolean isCreateHcqt = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.WO_HCQT, request);

        groupIdList = ConvertData.convertStringToList(groupId, ",");

        return Response.ok(woBusinessImpl.createImportWoExcelTemplate(woDto.getLoggedInUser(), groupIdList, isCnkt, isRevenueRole, isCreateHcqt)).build();
    }

    //end api for user

    @Override
    public Response woGeneralReport(WoGeneralReportDTO dto, HttpServletRequest request) {
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.WOXL, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");

        BaseResponseOBJ resp;

        List<WoGeneralReportDTO> report = woDAO.getGeneralReport(dto, groupIdList);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, report);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response woDetailsReport(WoDTO dto, HttpServletRequest request) {
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.WOXL, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        BaseResponseOBJ resp;

        if (StringUtils.isNotEmpty(dto.getProvinceCode())) {
            String cdLevel2 = woDAO.getCdLevel2FromProvinceCode(dto.getProvinceCode());
            dto.setCdLevel2(cdLevel2);
        }

        List<WoDTO> report = woDAO.genDetailsReport(dto, groupIdList);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, report);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response processOpinion(WoDTO dto) {
        BaseResponseOBJ resp;
        if (dto.getWoId() == null || StringUtils.isEmpty(dto.getLoggedInUser())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            woBusinessImpl.processOpinion(dto);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            e.printStackTrace();
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response constructionForAutoComplete(WoDTO obj) {
        BaseResponseOBJ resp;
        List<WoSimpleConstructionDTO> constructions = new ArrayList<>();

        if (StringUtils.isNotEmpty(obj.getKeySearch())) constructions = woDAO.suggestConstructions(obj.getKeySearch());
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, constructions);

        return Response.ok(resp).build();
    }

    @Override
    public Response createWOName(WoNameDTO dto) throws Exception {
        BaseResponseOBJ resp;
        boolean isWoTypeExist = woTypeDAO.checkExist(dto.getWoTypeId());

        if (!isWoTypeExist) {
            resp = new BaseResponseOBJ(ERROR_CODE, "Loại WO không tồn tại.", null);
            return Response.ok(resp).build();
        }

        dto.setStatus(1l);

        long saveResult = woNameDAO.saveObject(dto.toModel());

        if (saveResult == 0) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            return Response.ok(resp).build();
        }

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, saveResult);

        return Response.ok(resp).build();
    }

    @Override
    public Response createManyWOName(List<WoNameDTO> dtoList) throws Exception {
        List<WoNameBO> boList = new ArrayList<>();
        BaseResponseOBJ resp;

        for (WoNameDTO dto : dtoList) {
            boList.add(dto.toModel());
        }

        try {
            woNameDAO.saveListNoId(boList);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response updateWOName(WoNameDTO dto) throws Exception {
        Long id = dto.getId();
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            BaseResponseOBJ resp;

            long rowAffected = woNameDAO.updateObject(dto.toModel());

            if (rowAffected == 0) {
                resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
                return Response.ok(resp).build();
            }

            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, rowAffected);

            return Response.ok(resp).build();
        }
    }

    @Override
    public Response deleteWOName(WoNameDTO dto) throws Exception {
        Long id = dto.getId();
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            BaseResponseOBJ resp;

            WoNameBO bo = woNameDAO.getOneRaw(id);
            if (bo == null) {
                resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
                return Response.ok(resp).build();
            }

            int rowAffected = woNameDAO.delete(id);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, rowAffected);

            return Response.ok(resp).build();
        }
    }

    @Override
    public Response doSearchWOName(WoNameDTO dto) throws Exception {
        List<WoNameDTO> names = woNameDAO.doSearch(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, names);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response getCatWorkName(WoDTO dto) throws Exception {
        String result = "";

        if (dto.getCatWorkItemTypeId() != null && dto.getConstructionId() != null) {
            result = woDAO.getCatWorkName(dto.getCatWorkItemTypeId(), dto.getConstructionId());
        }
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, result);
        return Response.ok(resp).build();
    }

    @Override
    public Response getAIOWoInfo(WoDTO dto) throws Exception {

        //get woTypeId
        Long woTypeId = woDAO.getAIOWoTypeId();
        dto.setWoTypeId(woTypeId);

        //get woNameId
        WoNameDTO woName = null;
        if (woTypeId != null) woName = woDAO.getAIOWoName(woTypeId);
        dto.setWoNameId(woName.getId());
        dto.setWoName(woName.getName());

        //get cdLv2
        if (dto.getContractId() != null) {
            WoSimpleSysGroupDTO cdLv2 = woDAO.getCdLevel2FromAIOContract(dto.getContractId());
            if (cdLv2 != null) {
                dto.setCdLevel2(String.valueOf(cdLv2.getSysGroupId()));
                dto.setCdLevel2Name(cdLv2.getGroupName());
            }
        }

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dto);
        return Response.ok(resp).build();
    }

    @Override
    public Response getWoTypeImportTemplate() throws Exception {
        return Response.ok(woBusinessImpl.importWoTypeExcelTemplate()).build();
    }

    @Override
    public Response getExcelDetailsReport(WoDTO dto, HttpServletRequest request) throws Exception {
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.WOXL, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");

        if (StringUtils.isNotEmpty(dto.getProvinceCode())) {
            String cdLevel2 = woDAO.getCdLevel2FromProvinceCode(dto.getProvinceCode());
            dto.setCdLevel2(cdLevel2);
        }

        return Response.ok(woBusinessImpl.getExcelDetailsReport(dto, groupIdList)).build();
    }

    @Override
    public Response getExcelGeneralReport(WoGeneralReportDTO dto, HttpServletRequest request) throws Exception {
        try {
            String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                    Constant.AdResourceKey.WOXL, request);
            List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
            String strReturn = woBusinessImpl.getExcelGeneralReport(dto, groupIdList);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Response getWoNameImportTemplate() throws Exception {
        return Response.ok(woBusinessImpl.importWoNameExcelTemplate()).build();
    }

    @Override
    public Response woCheckGpon(WoDTO dto) {
        BaseResponseOBJ resp;
        if (dto.getConstructionCode() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean value = woBusinessImpl.woCheckGpon(dto);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, value);

        return Response.ok(resp).build();
    }

//    @Override
//    public Response getCheckListNeedAdd(WoDTO woDto) throws Exception {
//        List<WoMappingChecklistBO> checkList;
//        Long woId = woDto.getWoId();
//        Long catConsTypeId = woDto.getCatConstructionTypeId();
//        Long catWorkItemTypeId = woDto.getCatWorkItemTypeId();
//        Long constructionId = woDto.getConstructionId();
//
//        if (catConsTypeId != null && catConsTypeId == 3) {
//            //is GPON
//            checkList = woMappingCheckListDAO.getCheckListForCreateWo(catWorkItemTypeId, woId, true, constructionId);
//        } else {
//            checkList = woMappingCheckListDAO.getCheckListForCreateWo(catWorkItemTypeId, woId, false, null);
//        }
//
//        List<WoMappingChecklistDTO> lstChecklists = woBusinessImpl.getListChecklistsOfWoForWeb(woDto.getWoId());
//
//        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, lstChecklists);
//        return Response.ok(resp).build();
//    }

    @Override
    public Response addCheckList(WoXdddChecklistDTO obj) throws Exception {
        BaseResponseOBJ resp = new BaseResponseOBJ(ERROR_CODE, "Có lỗi xảy ra!", null);

        if (obj.getWoId() == null || StringUtils.isEmpty(obj.getName())) {
            resp = new BaseResponseOBJ(ERROR_CODE, "Dữ liệu không hợp lệ!", null);
        } else {
            if ("XDDD".equalsIgnoreCase(obj.getCustomField())) {
                List<WoXdddChecklistDTO> searchResult = woXdddChecklistDAO.doSearch(obj);
                if (searchResult.size() > 0) {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Đã tồn tại! Tên đâu việc không được trùng nhau! ", null);
                } else {
                    WoXdddChecklistBO item = new WoXdddChecklistBO();
                    item.setName(obj.getName());
                    item.setWoId(obj.getWoId());
                    item.setId(woXdddChecklistDAO.getNextSeqVal());
                    item.setState("NEW");
                    item.setHshc(0l);
                    item.setConfirm(0l);
                    item.setStatus(1l);
                    item.setUserCreated(obj.getLoggedInUser());
                    item.setCreateDate(new Date());
                    woXdddChecklistDAO.saveObject(item);

                    logAddChecklistItem(obj.getWoId(), obj.getName(), obj.getLoggedInUser());
                    resp = new BaseResponseOBJ(SUCCESS_CODE, "Thêm mới thành công! ", null);
                }
            }

            if ("5S".equalsIgnoreCase(obj.getCustomField())) {
                WoMappingChecklistDTO searchObj = new WoMappingChecklistDTO();
                searchObj.setWoId(obj.getWoId());

                List<WoMappingChecklistDTO> searchResult = woMappingCheckListDAO.doSearch(searchObj);

                boolean hasNameCheck = checkItemHasName(searchResult, obj.getName());

                if (hasNameCheck) {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Đã tồn tại! Tên đâu việc không được trùng nhau! ", null);
                } else {
                    WoMappingChecklistBO bo = new WoMappingChecklistBO();
                    bo.setWoId(obj.getWoId());
                    bo.setState("NEW");
                    bo.setStatus(1l);
                    bo.setCheckListId(searchResult.size() + 1l);
                    bo.setName(obj.getName());
                    woMappingCheckListDAO.saveObject(bo);

                    logAddChecklistItem(obj.getWoId(), obj.getName(), obj.getLoggedInUser());
                    resp = new BaseResponseOBJ(SUCCESS_CODE, "Thêm mới thành công! ", null);
                }
            }
        }

        return Response.ok(resp).build();
    }

    private boolean checkItemHasName(List<WoMappingChecklistDTO> listItem, String name) {
        boolean res = false;

        for (WoMappingChecklistDTO item : listItem) {
            if (name.equalsIgnoreCase(item.getName())) return true;
        }

        return res;
    }

    private void logAddChecklistItem(Long woId, String name, String loggedInUser) throws Exception {
        WoDTO woDTO = new WoDTO();
        woDTO.setWoId(woId);
        woDTO.setLoggedInUser(loggedInUser);
        woDTO.setDontSendMocha(true);
        woBusinessImpl.logWoWorkLogs(woDTO, "1", "Thêm đầu việc " + name, "", loggedInUser);
    }

    @Override
    public Response deleteCheckList(WoMappingChecklistDTO dto) throws Exception {
        BaseResponseOBJ resp;

        if (dto.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            //xóa đầu việc của wo xddd - loại công trình 8 -> ct xddd
            if (dto.getCatConstructionTypeId() != null && dto.getCatConstructionTypeId() == 8) {
                woXdddChecklistDAO.delete(dto.getId());
            } else {
                woMappingCheckListDAO.deleteCheckList(dto.getId());
            }

            WoDTO woDTO = new WoDTO();
            woDTO.setWoId(dto.getWoId());
            woDTO.setLoggedInUser(dto.getLoggedInUser());
            woDTO.setDontSendMocha(true);
            woBusinessImpl.logWoWorkLogs(woDTO, "1", "Xóa đầu việc " + dto.getName(), "", dto.getLoggedInUser());

            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchReportTHDT(ReportWoTHDTDTO obj) {
        DataListDTO data = woBusinessImpl.doSearchReportTHDT(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response exportFileTHDT(ReportWoTHDTDTO obj) {
        try {
            String strReturn = woBusinessImpl.exportFileTHDT(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Response acceptChecklistQuantity(WoMappingChecklistDTO dto) throws Exception {
        BaseResponseOBJ resp;
        try {
            woBusinessImpl.acceptChecklistQuantity(dto);
            resp = new BaseResponseOBJ(SUCCESS_CODE, dto.getCustomField(), null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response rejectChecklistQuantity(WoMappingChecklistDTO dto) throws Exception {
        BaseResponseOBJ resp;
        try {
            woBusinessImpl.rejectChecklistQuantity(dto);
            resp = new BaseResponseOBJ(SUCCESS_CODE, dto.getCustomField(), null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response exportWoNameExcel(WoNameDTO dto) throws Exception {
        dto.setPage(1l);
        dto.setPageSize(9999);

        List<WoNameDTO> listWoNameDto = woNameDAO.doSearch(dto);

        return Response.ok(woBusinessImpl.exportExcelWoNameList(listWoNameDto)).build();
    }

    @Override
    public Response exportWoTypeExcel(WoTypeDTO dto) throws Exception {
        dto.setPage(1l);
        dto.setPageSize(9999);

        List<WoTypeDTO> listWoTypeDto = woTypeDAO.doSearch(dto);

        return Response.ok(woBusinessImpl.exportExcelWoTypeList(listWoTypeDto)).build();
    }

    @Override
    public Response exportWoExcel(WoDTO dto, HttpServletRequest request) throws Exception {
        String groupIdViewRole = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.WOXL, request);
        List<String> groupIdListView = ConvertData.convertStringToList(groupIdViewRole, ",");

        boolean isTcTct = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.TC_TCT, request);
        boolean isTcBranch = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.TC_BRANCH, request);
        String groupIdTcBranchRole = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.APPROVED,
                Constant.AdResourceKey.TC_BRANCH, request);
        List<String> groupIdListTcBranch = ConvertData.convertStringToList(groupIdTcBranchRole, ",");

        if (StringUtils.isNotEmpty(dto.getLoggedInUser()) && !isTcBranch && !isTcTct) {
            long sysUserId = trDAO.getSysUserId(dto.getLoggedInUser());
            dto.setFtId(sysUserId);
        }

        dto.setPage(1l);
        dto.setPageSize(99999);
        dto.setCustomField(REPORT_TYPE);

        List<String> contractBranchs = makeContractBranchs(groupIdListTcBranch);

        List<WoDTO> listWoDto = woDAO.doSearch(dto, groupIdListView, isTcTct, isTcBranch, contractBranchs);

        return Response.ok(woBusinessImpl.exportExcelWoList(listWoDto)).build();
    }

    @Override
    public Response doSearchReportAIO(ReportWoAIODTO obj) {
        DataListDTO data = woBusinessImpl.doSearchReportAIO(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response getSysGroup(WoDTO obj) {
        WoSimpleSysGroupDTO group = trDAO.getSysGroupById(obj.getSysGroupId());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, group);
        return Response.ok(resp).build();
    }

    @Override
    public Response exportFileAIO(ReportWoAIODTO obj) {
        try {
            String strReturn = woBusinessImpl.exportFileAIO(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Response doSearchHcqtChecklist(WoChecklistDTO obj) {
        List<WoChecklistDTO> data = woBusinessImpl.getHcqtChecklist(obj);
        data.get(0).setAttachmentLst(new ArrayList<>());
        WoMappingAttachDTO dto = new WoMappingAttachDTO();
        dto.setWoId(obj.getWoId());
        dto.setPageSize(100);
        dto.setPage(1l);
        List<WoMappingAttachDTO> fileList = woMappingAttachDAO.searchFileCheckList(dto);
        data.get(1).setAttachmentLst(fileList);
        data.get(2).setAttachmentLst(new ArrayList<>());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchAvgChecklist(WoChecklistDTO obj) {
        List<WoChecklistDTO> data = woBusinessImpl.getAvgChecklist(obj);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
        return Response.ok(resp).build();
    }

    @Override
    public Response completeHcqtChecklist(WoChecklistDTO obj) {
        BaseResponseOBJ resp;
        try {
            woBusinessImpl.completeHcqtChecklist(obj);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response addImage(WoChecklistDTO obj) throws Exception {
        BaseResponseOBJ resp;
        if (obj.getLstImgs() == null || obj.getLstImgs().size() == 0)
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        if (woBusinessImpl.addImageToChecklist(obj)) {
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getHcqtIssueList(WoChecklistDTO obj) {
        BaseResponseOBJ resp;

        try {
            List<WoAppParamDTO> hcqtIssues = woDAO.getAppParam("HCQT_ISSUE");
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, hcqtIssues);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response declareHcqtIssue(WoChecklistDTO obj) {
        BaseResponseOBJ resp;
        try {
            woBusinessImpl.declareHcqtProblem(obj);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response exportReportDetailWo(WoDTO dto, HttpServletRequest request) throws Exception {
        try {
            String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                    Constant.AdResourceKey.WOXL, request);
            List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");

            if (StringUtils.isNotEmpty(dto.getProvinceCode())) {
                String cdLevel2 = woDAO.getCdLevel2FromProvinceCode(dto.getProvinceCode());
                dto.setCdLevel2(cdLevel2);
            }

            String strReturn = woBusinessImpl.exportReportDetailWo(dto, groupIdList);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response woHcqtFtReport(WoHcqtFtReportDTO dto) throws Exception {
        List<WoHcqtFtReportDTO> records = woDAO.genHcqtFtReport(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, records);
        ;
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response exportFileHcqtFtReport(WoHcqtFtReportDTO obj) {
        try {
            String strReturn = woBusinessImpl.exportFileHcqtFtReport(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
    }

    //Huypq-04092020-start
    @Override
    public Response checkRoleApproveHshc(HttpServletRequest request) {
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request)) {
            return Response.ok(Response.Status.OK).build();
        }
        return Response.ok(Response.Status.UNAUTHORIZED).build();
    }
    //Huy-end

    @Override
    public Response doSearchReportHSHCStatus(ReportHSHCQTDTO obj) {
        DataListDTO data = woBusinessImpl.doSearchReportHSHCStatus(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response exportFileHSHCStatus(ReportHSHCQTDTO obj) {
        try {
            String strReturn = woBusinessImpl.exportFileHSHCStatus(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Response doSearchReportHSHCProvince(ReportHSHCQTDTO obj) {
        DataListDTO data = woBusinessImpl.doSearchReportHSHCProvince(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response exportFileHSHCProvince(ReportHSHCQTDTO obj) {
        try {
            String strReturn = woBusinessImpl.exportFileHSHCProvince(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
    }

    public Response createHcqtProject(WoHcqtProjectDTO dto) throws Exception {
        dto.setCreatedDate(new Date());
        Long result = woHcqtProjectDAO.saveObject(dto.toModel());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, result);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchHcqtProject(WoHcqtProjectDTO dto) {
        List<WoHcqtProjectDTO> projects = woHcqtProjectDAO.doSearch(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, projects);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }


    @Override
    public Response getOneHcqtProjectDetails(WoHcqtProjectDTO dto) {
        WoHcqtProjectDTO project = woHcqtProjectDAO.getOneRaw(dto.getHcqtProjectId()).toDTO();
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, project);
        return Response.ok(resp).build();
    }

    @Override
    public Response deleteHcqtProject(WoHcqtProjectDTO dto) {
        int result = woHcqtProjectDAO.delete(dto.getHcqtProjectId());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, result);
        return Response.ok(resp).build();
    }

    @Override
    public Response updateHcqtProject(WoHcqtProjectDTO dto) {
        Long result = woHcqtProjectDAO.updateObject(dto.toModel());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, result);
        return Response.ok(resp).build();
    }

    @Override
    public Response getImportHCQTExcelTemplate() throws Exception {
        return Response.ok(woBusinessImpl.createImportHCQTWoExcelTemplate()).build();
    }

    @Override
    public Response createManyHCQTWO(List<WoHcqtDTO> dtos, HttpServletRequest request) throws Exception {
        boolean hasCreatePermission = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.WO_HCQT, request);
        Long loggedInUserId = VpsPermissionChecker.getUser(request).getSysUserId();
        BaseResponseOBJ resp;
        try {
            int totalInsert = 0;
            int totalUpdate = 0;
            if (woBusinessImpl.validateHCQTImportData(dtos)) {
                boolean isImporting = true;

                Long hcqtWoTypeId = woTypeDAO.getIdByCode("HCQT");
                List<WoDTO> allHcqtWo = woDAO.getAllWoByTypeId(hcqtWoTypeId);

                Map<String, WoDTO> hcqtWoMap = new HashMap<>();

                for (WoDTO wo : allHcqtWo) {
                    String uniqueKey = woBusinessImpl.createUniqueHcqtKey(wo);
                    hcqtWoMap.put(uniqueKey, wo);
                }

                //nếu không có quyền create thì chỉ cho update & chỉ update wo được giao
                if (!hasCreatePermission) {
                    boolean invalidPermission = false;

                    for (WoHcqtDTO item : dtos) {
                        String uniqueKey = woBusinessImpl.createUniqueHcqtKey(item);
                        WoDTO existedWo = hcqtWoMap.get(uniqueKey);

                        //đánh dấu wo mới
                        if (existedWo == null) {
                            item.setCustomField("Phát hiện dữ liệu mới - không có quyền tạo wo.");
                            invalidPermission = true;
                        }

                        if (!item.getFtId().equals(loggedInUserId)) {
                            item.setCustomField("Không có quyền sửa wo này.");
                            invalidPermission = true;
                        }
                    }

                    //nếu vi phạm thì trả về cả file
                    if (invalidPermission) {
                        resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, dtos);
                        return Response.ok(resp).build();
                    }
                }


                for (WoHcqtDTO item : dtos) {
                	item.setWoTypeCode("HCQT");
                    String uniqueKey = woBusinessImpl.createUniqueHcqtKey(item);
                    WoDTO existedWo = hcqtWoMap.get(uniqueKey);

                    //nếu chưa tồn tại thì thêm mới, đã tồn tại thì update
                    if (existedWo == null) {
                        if (woBusinessImpl.createNewWO(item, isImporting)) totalInsert++;
                    } else {
                        boolean result = woBusinessImpl.updateHcqtWo(existedWo.getWoId(), item);
                        if (result) totalUpdate++;
                    }

                }

                String message = "Import: " + totalInsert + "; Update: " + totalUpdate;

                resp = new BaseResponseOBJ(SUCCESS_CODE, message, dtos);
            } else {
                resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, dtos);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response createManyHCQTWOReport(List<WoHcqtDTO> dtoList) throws Exception {
        File importResult = woBusinessImpl.genHcqtImportResultExcelFile(dtoList);
        return Response.ok(importResult).build();
    }

    @Override
    public Response resolveHcqtIssue(WoChecklistDTO obj) {
        BaseResponseOBJ resp;
        try {
            woBusinessImpl.resolveHcqtProblem(obj);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response exportFileDelivery(WoDTO obj, HttpServletRequest request) {
        String strReturn = "";
        try {
            strReturn = woBusinessImpl.exportFileDeliveryFT(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Response importFileDelivery(Attachment attachments, HttpServletRequest request) throws Exception {
        BaseResponseOBJ resp;
        boolean resul = true;
        String filePath;
        List<WoDTO> result = new ArrayList<>();
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
        Calendar c = Calendar.getInstance();
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParam)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }
        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);

        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }

        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }
        try {
            result = woBusinessImpl.importFileDelivery(filePath, request);
            if (result.size() > 0) {
                for (WoDTO woDTO : result) {
                    if (woDTO.getCustomField() == null) {
                        resul = true;
                    } else {
                        resul = false;
                    }
                }
                if (resul == true) {
                    for (WoDTO woDTO : result) {
                        if (woDTO.getCustomField() == null) {
                            resul = woBusinessImpl.giveAssignmentToFT(woDTO);
                        } else {
                            resul = false;
                        }
                    }
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
                if (resul = true) {
                    resp = new BaseResponseOBJ(SUCCESS_CODE, "Import thành công", result);
                    return Response.ok(resp).build();
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
            return Response.ok(resp).build();
        }
        resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
        return Response.ok(resp).build();
    }

    private boolean isExtendAllowSave(String fileName) {
        return UString.isExtendAllowSave(fileName, allowFileExt);
    }

    private boolean isFolderAllowFolderSave(String folderDir) {
        return UString.isFolderAllowFolderSave(folderDir, allowFolderDir);
    }


    @Override
    public Response getHcqtProject(WoHcqtProjectDTO obj) {
        WoHcqtProjectBO project = woHcqtProjectDAO.getOneRaw(obj.getHcqtProjectId());
        BaseResponseOBJ resp;

        if (project == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
        } else {
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, project);
        }

        return Response.ok(resp).build();
    }

    //Huypq-14102020-start
    @Override
    public Response getSysGroupNameById(Long id) {
        SysGroupDto project = woBusinessImpl.getSysGroupNameById(id);
        BaseResponseOBJ resp;

        if (project == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
        } else {
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, project);
        }

        return Response.ok(resp).build();
    }

    //Huypq-14102020-end
    @Override
    public Response getWorkItemByConstruction(WorkItemDTO obj) {
        BaseResponseOBJ resp;
        Long consId = obj.getConstructionId();
        if (consId == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        List<WorkItemDTO> workItems = new ArrayList<>();
        if(consId!=null && obj.getWoType()!=null && obj.getWoType().equals("TKDT")) {
        	//Lấy hạng mục của wo TKDT
        	workItems = woDAO.getWorkItemHtctByConstructionId(consId);
        } else {
        	workItems = woDAO.getDoneWorkItemByConstructionId(consId);
        }
        
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, workItems);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchHcqtWo(WoDTO dto, HttpServletRequest request) {
        Boolean isCreatorHcqt = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.WO_HCQT, request);
        //Huypq-02112020-start
        Boolean isViewWoHcqt = VpsPermissionChecker.hasPermission(Constant.OperationKey.VIEW, Constant.AdResourceKey.WO_HCQT, request);
        //Huy-end
        if (StringUtils.isNotEmpty(dto.getLoggedInUser())) {
            long sysUserId = trDAO.getSysUserId(dto.getLoggedInUser());
            dto.setSysUserId(sysUserId);
        }

        if (dto.getChecklistStep() != null) {
            if (dto.getChecklistStep() == -1) {
                dto.setChecklistStep(null);
                dto.setState(NG);
            }
        }

        List<WoHcqtDTO> listWoDto = woDAO.doSearchHcqtWo(dto, isCreatorHcqt, true, isViewWoHcqt);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listWoDto);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response exportHcqtWo(WoDTO dto, HttpServletRequest request) {
        Boolean isCreatorHcqt = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.WO_HCQT, request);
        Boolean isViewWoHcqt = VpsPermissionChecker.hasPermission(Constant.OperationKey.VIEW, Constant.AdResourceKey.WO_HCQT, request);

        if (StringUtils.isNotEmpty(dto.getLoggedInUser())) {
            long sysUserId = trDAO.getSysUserId(dto.getLoggedInUser());
            dto.setSysUserId(sysUserId);
        }

        if (dto.getChecklistStep() != null) {
            if (dto.getChecklistStep() == -1) {
                dto.setChecklistStep(null);
                dto.setState(NG);
            }
        }

        List<WoHcqtDTO> listWoDto = woDAO.doSearchHcqtWo(dto, isCreatorHcqt, true, isViewWoHcqt);

        try {
            String strReturn = woBusinessImpl.exportExcelHcqtWo(listWoDto);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Response getVhktCdLv2VList(WorkItemDTO obj) {

        List<WoSimpleSysGroupDTO> cdsLv2 = woDAO.getCnktCdLevel2();

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, cdsLv2);
        return Response.ok(resp).build();
    }


    @Override
    public Response doSearchWorkItem(WoScheduleWorkItemDTO dto) throws Exception {

//        if (dto == null || dto.getPage() <= 0 || dto.getPageSize() <= 0) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }

        List<WoScheduleWorkItemDTO> listTrDto = woScheduleWorkItemDAO.doSearch(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTrDto);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response createWorkItem(WoScheduleWorkItemDTO dto) throws Exception {
        BaseResponseOBJ resp;

        boolean checkExistTrTypeCode = woScheduleWorkItemDAO.checkExistWorkItemCode(dto.getWorkItemCode());

        if (checkExistTrTypeCode) {
            dto.setCreatedDate(new Date());
            String returnStr = woScheduleWorkItemDAO.save(dto.toModel());
            resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, "Mã công việc định kỳ đã tồn tại.", null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getOneInfoWorkItem(WoScheduleWorkItemDTO dto) throws Exception {
        Long woWorkItemId = dto.getWoWorkItemId();
        BaseResponseOBJ resp;

        if (woWorkItemId <= 0) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        dto = woScheduleWorkItemDAO.getOneInfoWorkItem(woWorkItemId);

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dto);
        return Response.ok(resp).build();
    }

    @Override
    public Response updateWorkItem(WoScheduleWorkItemDTO dto) throws Exception {

        BaseResponseOBJ resp;
//        boolean checkExistTrTypeCode = woScheduleWorkItemDAO.checkExistWorkItemCode(dto.getWorkItemCode());

        String returnStr = woScheduleWorkItemDAO.update(dto.toModel());
        resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);


        return Response.ok(resp).build();

    }

    @Override
    public Response deleteWorkItem(WoScheduleWorkItemDTO dto) throws Exception {
        Long woWorkItemId = dto.getWoWorkItemId();
        BaseResponseOBJ resp;

        WoScheduleWorkItemBO woScheduleWorkItemBO = woScheduleWorkItemDAO.getOneRaw(woWorkItemId);
        if (woScheduleWorkItemBO == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        boolean isDeletable = woScheduleWorkItemDAO.checkDeletable(woWorkItemId);

        if (isDeletable == true) {
            woScheduleWorkItemDAO.deleteWorkItem(woWorkItemId);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE_SCHEDULE_WI_CHECKLIST, null);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchWICheckList(WoScheduleWorkItemDTO dto, HttpServletRequest request) throws Exception {
//        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
//                Constant.AdResourceKey.WOXL, request);
//        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");

        List<WoScheduleCheckListDTO> listWoDto = woScheduleWorkItemDAO.doSearchWICheckList(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listWoDto);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response createNewWorkItemCheckList(WoScheduleCheckListDTO dto) throws Exception {
        BaseResponseOBJ resp;

        boolean checkExistTrTypeCode = woScheduleWorkItemDAO.checkExistWICheckListCode(dto);

        if (checkExistTrTypeCode) {
            String returnStr = woScheduleCheckListDAO.save(dto.toModel());
            resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, "Mã công việc đã tồn tại.", null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getOneDetailsCheckList(WoScheduleCheckListDTO dto) throws Exception {
        Long scheduleCheckListId = dto.getScheduleCheckListId();
        BaseResponseOBJ resp;

        if (scheduleCheckListId <= 0) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        dto = woScheduleWorkItemDAO.getOneDetailsCheckList(scheduleCheckListId);

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dto);
        return Response.ok(resp).build();
    }

    @Override
    public Response updateWorkItemCheckList(WoScheduleCheckListDTO dto) throws Exception {
        BaseResponseOBJ resp;
        boolean checkExistTrTypeCode = woScheduleWorkItemDAO.checkExistWICheckListCode(dto);

        if (checkExistTrTypeCode) {
            String returnStr = woScheduleCheckListDAO.update(dto.toModel());
            resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, "Mã công việc đã tồn tại.", null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response deleteWorkItemCheckList(WoScheduleCheckListDTO dto) throws Exception {
        Long scheduleCheckListId = dto.getScheduleCheckListId();
        BaseResponseOBJ resp;

        WoScheduleCheckListBO woScheduleCheckListDTO = woScheduleCheckListDAO.getOneRaw(scheduleCheckListId);
        if (woScheduleCheckListDTO == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        boolean isDeletable = woScheduleWorkItemDAO.checkDeletable(dto.getScheduleWorkItemId());

        if (isDeletable == true) {
            woScheduleCheckListDAO.deleteWorkItemCheckList(scheduleCheckListId);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE_SCHEDULE_WI_CHECKLIST, null);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response exportexcelScheduleConfig(WoDTO woDto, HttpServletRequest request) throws Exception {

        List<String> groupIdList = new ArrayList<>();
        String strReturn = " ";
        String groupIdDomain = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.WOXL, request);
        groupIdList = ConvertData.convertStringToList(groupIdDomain, ",");
        try {
            strReturn = woBusinessImpl.exportexcelScheduleConfig(woDto.getLoggedInUser(), groupIdList);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;

    }

    @Override
    public Response doSearchWIConfig(WoScheduleConfigDTO dto) throws Exception {

        if (dto == null || dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoScheduleConfigDTO> listDto = woScheduleConfigDAO.doSearch(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listDto);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response createWIConfig(WoScheduleConfigDTO dto) throws Exception {
        BaseResponseOBJ resp;

        boolean checkExistTrTypeCode = woScheduleConfigDAO.checkExistScheduleConfigCode(dto.getScheduleConfigCode());

        if (checkExistTrTypeCode) {
            dto.setCreatedDate(new Date());
            String returnStr = woScheduleConfigDAO.save(dto.toModel());
            resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, "Mã cấu hình công việc định kỳ đã tồn tại.", null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getOneWIConfig(WoScheduleConfigDTO dto) throws Exception {
        Long scheduleConfigId = dto.getScheduleConfigId();
        BaseResponseOBJ resp;

        if (scheduleConfigId <= 0) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        dto = woScheduleConfigDAO.getOneWIConfig(scheduleConfigId);

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dto);
        return Response.ok(resp).build();
    }

    @Override
    public Response updateWIConfig(WoScheduleConfigDTO dto) throws Exception {

        BaseResponseOBJ resp;

        boolean checkExistCode = woScheduleConfigDAO.checkExistScheduleConfigCode(dto.getScheduleConfigCode());

//        if(checkExistCode){
        String returnStr = woScheduleConfigDAO.update(dto.toModel());
        resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
//        }
//        else{
//            resp = new BaseResponseOBJ(ERROR_CODE, "Mã cấu hình công việc định kỳ đã tồn tại.", null);
//        }

        return Response.ok(resp).build();

    }

    @Override
    public Response deleteWIConfig(WoScheduleConfigDTO dto) throws Exception {
        Long scheduleConfigId = dto.getScheduleConfigId();
        BaseResponseOBJ resp;

        WoScheduleConfigBO woScheduleConfigBO = woScheduleConfigDAO.getOneRaw(scheduleConfigId);
        if (woScheduleConfigBO == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        boolean isDeletable = woScheduleConfigDAO.checkDeletable(scheduleConfigId);

        if (isDeletable == true) {
            woScheduleConfigDAO.deleteWIConfig(scheduleConfigId);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE_SCHEDULE_WI_CHECKLIST, null);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response getImportScheduleConfigResult(List<WoScheduleConfigDTO> woDtos) throws Exception {
        File importResult = woBusinessImpl.getImportScheduleConfigResult(woDtos);
        return Response.ok(importResult).build();
    }

    @Override
    public Response autoSuggestMoneyValue(WoSimpleConstructionDTO dto) throws Exception {
        BaseResponseOBJ resp;
        if (dto.getConstructionId() == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        Double totalMoneyValue = woDAO.getHshcMoneyValueByConstructionId(dto.getConstructionId());
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, totalMoneyValue);
        return Response.ok(resp).build();
    }

    @Override
    public Response importFileConfig(Attachment attachments, HttpServletRequest request) throws Exception {
        BaseResponseOBJ resp;
        boolean resul = true;
        String filePath;
        List<WoDTO> result = new ArrayList<>();
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
//        String typeImport = UString.getSafeFileName(request.getParameter("typeImport"));

        Calendar c = Calendar.getInstance();
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParam)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }
        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);

        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }

        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }
        try {
            result = woBusinessImpl.importFileDelivery(filePath, request);
            if (result.size() > 0) {
                for (WoDTO woDTO : result) {
                    if (woDTO.getCustomField() == null) {
                        resul = true;
                    } else {
                        resul = false;
                    }
                }
                if (resul == true) {
                    for (WoDTO woDTO : result) {
                        if (woDTO.getCustomField() == null) {
                            resul = woBusinessImpl.giveAssignmentToFT(woDTO);
                        } else {
                            resul = false;
                        }
                    }
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
                if (resul = true) {
                    resp = new BaseResponseOBJ(SUCCESS_CODE, "Import thành công", result);
                    return Response.ok(resp).build();
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
            return Response.ok(resp).build();
        }
        resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
        return Response.ok(resp).build();
    }

    @Override
    public Response exportApproveWo(WoDTO obj, HttpServletRequest request) {
        String strReturn = "";
        try {
            strReturn = woBusinessImpl.exportApproveWo(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response importApproveWo(Attachment attachments, HttpServletRequest request) throws Exception {
        BaseResponseOBJ resp;
        boolean resul = true;
        String filePath;
        List<WoDTO> result = new ArrayList<>();
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
        String listDomainData = UString.getSafeFileName(request.getParameter("cdDomainDataList"));
        List<String> groupIdList = ConvertData.convertStringToList(listDomainData, ",");
        Calendar c = Calendar.getInstance();
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParam)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }
        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);

        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }

        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }
        try {
            result = woBusinessImpl.importApproveWo(filePath, groupIdList, request);
            if (result.size() > 0) {
                for (WoDTO woDTO : result) {
                    if (woDTO.getCustomField() != null) {
                        resul = false;
                        break;
                    }
                }
                if (resul == true) {
                    for (WoDTO woDTO : result) {
                        if (woDTO.getCustomField() == null) {
                            resul = woBusinessImpl.changeWoState(woDTO, woDTO.getState());
                        } else {
                            resul = false;
                        }
                    }
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
                if (resul = true) {
                    resp = new BaseResponseOBJ(SUCCESS_CODE, "Import thành công", result);
                    return Response.ok(resp).build();
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
            return Response.ok(resp).build();
        }
        resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
        return Response.ok(resp).build();
    }

    @Override
    public Response getImportApprovreResult(List<WoDTO> woDtos) throws Exception {
        File importResult = woBusinessImpl.getImportApprovreResult(woDtos);
        return Response.ok(importResult).build();
    }

    @Override
    public Response importFileScheduleWoConfig(Attachment attachments, HttpServletRequest request) throws Exception {
        BaseResponseOBJ resp;
        boolean resul = true;
        String filePath;
        List<WoScheduleConfigDTO> result = new ArrayList<>();
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
        Calendar c = Calendar.getInstance();
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParam)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }
        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);

        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }

        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }
        try {
            result = woBusinessImpl.importFileScheduleConfig(filePath, request);
            if (result.size() > 0) {
                for (WoScheduleConfigDTO woDTO : result) {
                    if (woDTO.getCustomField() == null) {
                        resul = true;
                    } else {
                        resul = false;
                        break;
                    }
                }
                if (resul == true) {
                    for (WoScheduleConfigDTO woDTO : result) {
                        resul = woBusinessImpl.createScheduleWoConfig(woDTO);
                    }
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
                if (resul = true) {
                    resp = new BaseResponseOBJ(SUCCESS_CODE, "Import thành công", result);
                    return Response.ok(resp).build();
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
            return Response.ok(resp).build();
        }
        resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
        return Response.ok(resp).build();
    }

    @Override
    public Response getImportFTResult(List<WoDTO> woDtos) throws Exception {
        File importResult = woBusinessImpl.getImportFTResult(woDtos);
        return Response.ok(importResult).build();
    }

    @Override
    public Response doSearchReportWoTr(ReportWoTrDTO obj) {
        DataListDTO data = woBusinessImpl.doSearchReportWoTr(obj);
        return Response.ok(data).build();
    }

    //Huypq-02112020-start

    @Override
    public Response getCheckViewHcqt(HttpServletRequest request) {
        Boolean isViewWoHcqt = VpsPermissionChecker.hasPermission(Constant.OperationKey.VIEW, Constant.AdResourceKey.WO_HCQT, request);
        return Response.ok(isViewWoHcqt).build();
    }

    //Huy-end
    @Override
    public Response exportFileTrWo(ReportWoTrDTO obj) {
        try {
            String strReturn = woBusinessImpl.exportFileTrWo(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Huypq-25112020-start
    @Override
    public Response exportDoneWo(WoDTO obj, HttpServletRequest request) {
        String strReturn = "";
        try {
            strReturn = woBusinessImpl.exportDoneWo(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response importDoneWo(Attachment attachments, HttpServletRequest request) throws Exception {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        BaseResponseOBJ resp;
        boolean resul = true;
        String filePath;
        List<WoDTO> result = new ArrayList<>();
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
        String listDomainData = UString.getSafeFileName(request.getParameter("cdDomainDataList"));
        List<String> groupIdList = ConvertData.convertStringToList(listDomainData, ",");
        Calendar c = Calendar.getInstance();
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParam)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }
        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);

        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }

        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            result = woBusinessImpl.importDoneWo(filePath, groupIdList, request);
            if (result.size() > 0) {
                for (WoDTO woDTO : result) {
                    if (!StringUtils.isNotBlank(woDTO.getCustomField())) {
                        resul = true;
                    } else {
                        resul = false;
                        break;
                    }
                }
                if (resul == true) {
                    for (WoDTO woDTO : result) {
                        if (!StringUtils.isNotBlank(woDTO.getCustomField())) {
                            if (StringUtils.isNotBlank(woDTO.getDoneStateWo()) && woDTO.getDoneStateWo().equals("1")) {
                                woDTO.setFinishDate(dateFormat.parse(woDTO.getFinishDateStr()));
                                Long id = woBusinessImpl.updateStateWoImportDone(woDTO, user, null);
                                if (id == 0) {
                                    resul = false;
                                    throw new BusinessException("Có lỗi xảy ra khi cập nhật dữ liệu " + woDTO.getWoCode());
                                }
                            }
                        } else {
                            resul = false;
                        }
                    }
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
                if (resul = true) {
                    resp = new BaseResponseOBJ(SUCCESS_CODE, "Import thành công", result);
                    return Response.ok(resp).build();
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
            return Response.ok(resp).build();
        }
        resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
        return Response.ok(resp).build();
    }

    @Override
    public Response getImportDoneResult(List<WoDTO> lstData) throws Exception {
        String strReturn = "";
        try {
            strReturn = woBusinessImpl.getImportDoneResult(lstData);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//        File importResult = woBusinessImpl.getImportDoneResult(woDtos);
//        return Response.ok(importResult).build();
    }
    //Huy-end

    //unikom start
    @Override
    public Response doSearchReportWo5s(WoDTO obj) {
        BaseResponseOBJ resp;
        if ("1".equalsIgnoreCase(obj.getType())) { // 5s report
            List<WoFTConfig5SDTO> data = woFTConfig5SDAO.doSearchReportWo5s(obj);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
            resp.setTotal(data.size());
            return Response.ok(resp).build();
        } else if ("2".equalsIgnoreCase(obj.getType())) { // Approved
            List<Report5sDTO> data = woFTConfig5SDAO.doSearchReportWo5sApproved(obj);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
            resp.setTotal(data.size());
            return Response.ok(resp).build();
        } else if ("3".equalsIgnoreCase(obj.getType())) { // Detail
            List<Report5sDTO> data = woFTConfig5SDAO.doSearchReportWo5sDetail(obj);
            List<String> lstStateDone = new ArrayList<>();
            lstStateDone.add("DONE");
            lstStateDone.add("CD_OK");
            lstStateDone.add("CD_NG");
            lstStateDone.add("OK");
            lstStateDone.add("NG");
            for (Report5sDTO iWo : data) {
                if (!lstStateDone.contains(iWo.getWoState())) {
                    iWo.setExecuteState("Chưa thực hiện");
                    iWo.setApprovedState("Chưa thực hiện");
                    continue;
                }

                // Trang thai thuc hien
                if (iWo.getEndTime()!=null && iWo.getFinishDate()!=null && DateTimeUtils.convertStringToDate(iWo.getEndTime(), "dd/MM/yyyy").getTime() < DateTimeUtils.convertStringToDate(iWo.getFinishDate(), "dd/MM/yyyy").getTime()) {
                    iWo.setExecuteState("Thực hiện đúng  hạn");
                } else {
                    iWo.setExecuteState("Thực hiện quá hạn");
                }

                // Trang thai phe duyet
                if (iWo.getUpdateCdApproveWoStr()!=null && iWo.getFinishDate()!=null && DateTimeUtils.convertStringToDate(iWo.getUpdateCdApproveWoStr(), "dd/MM/yyyy").getTime() < DateTimeUtils.convertStringToDate(iWo.getFinishDate(), "dd/MM/yyyy").getTime()) {
                    iWo.setApprovedState("Phê duyệt đúng  hạn");
                } else {
                    iWo.setApprovedState("Phê duyệt quá hạn");
                }
            }
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
            resp.setTotal(data.size());
            return Response.ok(resp).build();
        }
        return Response.serverError().build();
    }

    @Override
    public Response exportFileReport5s(List<Report5sDTO> obj) throws Exception {
        File importResult = woBusinessImpl.exportFileReport5s(obj);
        return Response.ok(importResult).build();

    }

    @Override
    public Response doSearchWoFTConfig(WoFTConfig5SDTO dto) throws Exception {

        if (dto == null || dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoFTConfig5SDTO> listDto = woFTConfig5SDAO.doSearch(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listDto);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response getCnktList() throws Exception {

        List<WoSimpleSysGroupDTO> cdsLv2 = woDAO.getCnkt();

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, cdsLv2);
        return Response.ok(resp).build();
    }

    @Override
    public Response getFTList(Long sysGroupId) throws Exception {

        List<WoSimpleFtDTO> ftList = woDAO.getFtList(sysGroupId, true, null);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, ftList);
        return Response.ok(resp).build();
    }

    @Override
    public Response getOneWO5SConfig(WoFTConfig5SDTO dto) throws Exception {
        Long ftConfigId = dto.getFtConfigId();
        BaseResponseOBJ resp;

        if (ftConfigId <= 0) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        dto = woFTConfig5SDAO.getOneWO5sConfig(ftConfigId);

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dto);
        return Response.ok(resp).build();
    }

    @Override
    public Response updateWO5SConfig(WoFTConfig5SDTO dto) throws Exception {

        BaseResponseOBJ resp;

        boolean checkExistCode = woFTConfig5SDAO.checkExistConfigCodeId(dto.getFtConfigCode(), dto.getFtConfigId());

        if (checkExistCode) {
            dto.setUserUpdated(dto.getLoggedInUser());
            String returnStr = woFTConfig5SDAO.update(dto.toModel());
            resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, "Mã cấu hình đã tồn tại.", null);
        }

        return Response.ok(resp).build();

    }

    @Override
    public Response deleteWO5SConfig(WoFTConfig5SDTO dto) throws Exception {
        Long ftConfigId = dto.getFtConfigId();
        BaseResponseOBJ resp;

        WoFTConfig5SBO woFTConfig5SBO = woFTConfig5SDAO.getOneRaw(ftConfigId);
        if (woFTConfig5SBO == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
//        boolean isDeletable = woScheduleConfigDAO.checkDeletable(ftConfigId);

//        if (isDeletable == true) {
        //woFTConfig5SDAO.deleteWO5sConfig(ftConfigId);
//        } else {
//            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE_SCHEDULE_WI_CHECKLIST, null);
//        }
        WoFTConfig5SBO bo = woFTConfig5SDAO.getOneRaw(ftConfigId);
        bo.setStatus(0);
        bo.setUserUpdated(dto.getLoggedInUser());
        woFTConfig5SDAO.updateObject(bo);

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        return Response.ok(resp).build();
    }

    @Override
    public Response createConfigWO5s(WoFTConfig5SDTO dto) throws Exception {
        BaseResponseOBJ resp;

        boolean checkExistCode = woFTConfig5SDAO.checkExistConfigCode(dto.getFtConfigCode());
        boolean isCNKTExisted = woFTConfig5SDAO.checkIsCNKTExisted(dto.getCdLevel2());

        if (checkExistCode) {
            if (isCNKTExisted) {
                resp = new BaseResponseOBJ(ERROR_CODE, "Đã tồn tại cấu hình công việc 5s cho CNKT này.", null);
            } else {
                dto.setCreatedDate(new Date());
                String returnStr = woFTConfig5SDAO.save(dto.toModel());
                resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
            }
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, "Mã cấu hình công việc 5s đã tồn tại. ", null);
        }

        return Response.ok(resp).build();
    }

    public Response acceptXdddValue(WoXdddChecklistDTO obj) {
        BaseResponseOBJ resp;
        boolean result = woBusinessImpl.acceptXdddChecklistItemValue(obj.getId(), obj.getLoggedInUser());

        if (result) resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    public Response rejectXdddValue(WoXdddChecklistDTO obj) {
        BaseResponseOBJ resp;
        try {
            WoXdddChecklistBO checklistItem = woXdddChecklistDAO.getOneRaw(obj.getId());
            if (checklistItem != null) {
                checklistItem.setValue(null);
                checklistItem.setState("REJECTED");
                checklistItem.setConfirmDate(new Date());
                checklistItem.setConfirmBy(obj.getLoggedInUser());
                woXdddChecklistDAO.updateObject(checklistItem);
            }

            Long woId = checklistItem.getWoId();
            WoBO wo = woDAO.getOneRaw(woId);
            wo.setState(PROCESSING);
            woDAO.updateObject(wo);

            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            e.printStackTrace();
        }

        return Response.ok(resp).build();
    }

//    @Override
//    public Response createWO5s(WoDTO dto){
//        BaseResponseOBJ resp;
//
//        if(StringUtils.isEmpty(dto.getCdLevel2())){
//            resp = new BaseResponseOBJ(ERROR_CODE,"Không có CD level 2",null);
//            return Response.ok(resp).build();
//        }
//
//        WoFTConfig5SDTO searchObj = new WoFTConfig5SDTO();
//        searchObj.setCdLevel2(dto.getCdLevel2());
//        List<WoFTConfig5SDTO> configFt5s = woFTConfig5SDAO.doSearch(searchObj);
//
//        if(configFt5s.size() == 0){
//            resp = new BaseResponseOBJ(ERROR_CODE,"Chưa chỉ định người thực hiện 5S cho cnkt này",null);
//            return Response.ok(resp).build();
//        }
//
//        WoFTConfig5SDTO ftConfig = configFt5s.get(0);
//
//        boolean result = woBusinessImpl.createWo5s(dto, ftConfig);
//
//        if(result) resp = new BaseResponseOBJ(SUCCESS_CODE,SUCCESS_MSG,null);
//        else resp = new BaseResponseOBJ(ERROR_CODE,ERROR_MSG,null);
//
//        return Response.ok(resp).build();
//    }

    @Override
    public Response doSearchWoTaskDaily(WoTaskDailyDTO dto) {
        BaseResponseOBJ resp;
        if (dto.getWoMappingChecklistId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        WoTaskDailyDTO searchObj = new WoTaskDailyDTO();
        searchObj.setWoMappingChecklistId(dto.getWoMappingChecklistId());
        List<WoTaskDailyBO> data = woTaskDailyDAO.doSearch(searchObj);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
        resp.setTotal(searchObj.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response acceptQuantityByDate(WoTaskDailyDTO dto) {
        if (dto.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean result = woBusinessImpl.acceptQuantityByDate(dto);
        BaseResponseOBJ resp;
        if (result) resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response rejectQuantityByDate(WoTaskDailyDTO dto) {
        if (dto.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        WoTaskDailyBO task = woTaskDailyDAO.getOneRaw(dto.getId());
        woTaskDailyDAO.updateWoMappingCheckList(task.getWoMappingChecklistId(), "NEW");
        task.setStatus(0l);
        task.setAmount(0d);
        task.setQuantity(0d);
        woTaskDailyDAO.updateObject(task);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response getCDCnkt() throws Exception {
        List<WoSimpleSysGroupDTO> fileList = woDAO.getCDCnkt();
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, fileList);
        return Response.ok(resp).build();
    }

    @Override
    public Response extendFinishDate(WoDTO dto) throws Exception {
        Gson gson = new Gson();
        BaseResponseOBJ resp;
        if (dto.getWoId() == null || dto.getFinishDate() == null || StringUtils.isEmpty(dto.getText())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            WoBO wo = woDAO.getOneRaw(dto.getWoId());

            Date oldFinishDate = wo.getFinishDate();
            Date newFinishDate = dto.getFinishDate();

            wo.setFinishDate(newFinishDate);
            woDAO.updateObject(wo);

            //tính toán sinh thêm 5s hoặc xóa bớt
            woBusinessImpl.calculateRelated5S(wo, oldFinishDate);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String logContent = "Chỉnh sửa hạn hoàn thành từ " + df.format(oldFinishDate) + " sang thời gian mới: " + df.format(dto.getFinishDate());
            logContent += " Lý do: " + dto.getText();
            WoDTO logDto = wo.toDTO();
            logDto.setDontSendMocha(true);
            woBusinessImpl.logWoWorkLogs(logDto, "1", logContent, gson.toJson(wo), dto.getLoggedInUser());

            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getXdddChecklistByWorkItem(WoDTO dto) throws Exception {
        BaseResponseOBJ resp;
        if (dto.getConstructionId() == null || dto.getCatWorkItemTypeId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoXdddChecklistDTO> listChecklistItem = woXdddChecklistDAO.getXdddChecklistByWorkItem(dto.getConstructionId(), dto.getCatWorkItemTypeId(), dto.getWoId());
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listChecklistItem);

        return Response.ok(resp).build();
    }

    @Override
    public Response changeStateAndAcceptTcTct(WoDTO dto) throws Exception {
        BaseResponseOBJ resp;
        if (dto.getWoId() == null || StringUtils.isEmpty(dto.getState())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean result = woBusinessImpl.changeStateAndAcceptTcTct(dto);
        if (result) {
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchDetailReportWoTr(ReportWoTrDTO obj) {
        DataListDTO data = woBusinessImpl.doSearchDetailReportWoTr(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response doSearchDetailReportTr(ReportWoTrDTO obj) {
        DataListDTO data = woBusinessImpl.doSearchDetailReportTr(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response importConfigContract(Attachment attachments, HttpServletRequest request) throws Exception {
        BaseResponseOBJ resp;
        boolean resul = true;
        String filePath;
        List<WoConfigContractCommitteeDTO> result = new ArrayList<>();
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
        Calendar c = Calendar.getInstance();
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParam)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }
        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);

        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }

        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }
        try {
            result = woBusinessImpl.importConfigContract(filePath, request);
            if (result.size() > 0) {
                for (WoConfigContractCommitteeDTO woDTO : result) {
                    if (woDTO.getCustomField() != null && !"".equals(woDTO.getCustomField())) {
                        resul = false;
                        break;
                    }
                }
                if (resul == true) {
                    for (WoConfigContractCommitteeDTO woDTO : result) {
                        if (woDTO.getCustomField().equals("")) {
                            String returnStr = woConfigContractDAO.save(woDTO.toModel());
                        } else {
                            resul = false;
                        }
                    }
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
                if (resul = true) {
                    resp = new BaseResponseOBJ(SUCCESS_CODE, "Import thành công", result);
                    return Response.ok(resp).build();
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                    return Response.ok(resp).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
            return Response.ok(resp).build();
        }
        resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
        return Response.ok(resp).build();
    }

    @Override
    public Response getWoConfigContractResult(List<WoConfigContractCommitteeDTO> woDtos) throws Exception {
        File importResult = woBusinessImpl.getWoConfigContractResult(woDtos);
        return Response.ok(importResult).build();
    }

    @Override
    public Response getFtListCdLevel5(WoDTO woDto) throws Exception {
        List<WoSimpleFtDTO> ftList = woBusinessImpl.getFtListCdLevel5(woDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, ftList);
        return Response.ok(resp).build();
    }

    @Override
    public Response removeInactiveWo(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp;

        if (woDto.getWoId() == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, "Có lỗi xảy ra!", null);
        } else {
            WoBO wo = woDAO.getOneRaw(woDto.getWoId());
            if (wo.getStatus() > 0) {
                resp = new BaseResponseOBJ(ERROR_CODE, "Không được phép xóa WO đã được kích hoạt!", null);
            } else {
                woDAO.removeInactiveWo(woDto.getWoId());
                resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
            }
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response postOverdueReason(WoDTO dto) throws Exception {
        BaseResponseOBJ resp;

        if (dto.getSysUserId() == null || dto.getWoId() == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, "Bad request!", null);
        } else {
            woBusinessImpl.insertOverdueReason(dto);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getWoOverdueReason(WoOverdueReasonDTO dto) throws Exception {
        BaseResponseOBJ resp;

        if (dto.getWoId() == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, "Bad request!", null);
        } else {
            WoOverdueReasonBO data = woBusinessImpl.getWoOverdueReason(dto);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response acceptRejectOverdueReason(WoDTO dto) throws Exception {
        BaseResponseOBJ resp;

        if (dto.getWoId() == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, "Bad request!", null);
        } else {
            woBusinessImpl.acceptRejectOverdueReason(dto);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getGeneralReportXDDTHT(WoGeneralReportDTO dto, HttpServletRequest request) {
        BaseResponseOBJ resp;
        List<WoGeneralReportDTO> report = woDAO.getGeneralReportXDDTHT(dto);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, report);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response getWoDetailReportXDDTHT(WoGeneralReportDTO dto, HttpServletRequest request) {
        BaseResponseOBJ resp;
        List<WoDTO> report = woDAO.getWoDetailReportXDDTHT(dto);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, report);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }


    @Override
    public Response exportFileReportXDDTHT(WoGeneralReportDTO obj) {
        try {
            String strReturn = woBusinessImpl.exportFileReportXDDTHT(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response tcAcceptAllSelected(WoTcMassApproveRejectReqDTO req) throws Exception {
        BaseResponseOBJ resp;
        try {
            woBusinessImpl.tcAcceptAllSelected(req);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response tcRejectAllSelected(WoTcMassApproveRejectReqDTO req) throws Exception {
        BaseResponseOBJ resp;
        try {
            woBusinessImpl.tcRejectAllSelected(req);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response getListGoodsByWoId(WoDTO dto) throws Exception {
        BaseResponseOBJ resp;
        Long woId = dto.getWoId();
        WoDTO woDTO = woDAO.getOneDetails(woId);

        WoDTORequest request = new WoDTORequest();
        request.setWoId(woId);
        WoDTOResponse response = woBusinessImpl.getListGoodsByWoId(request);
        if ("DONE".equalsIgnoreCase(woDTO.getState()) && !"CMC".equalsIgnoreCase(woDTO.getPartner()) && !"AVG".equalsIgnoreCase(woDTO.getWoTypeCode())) {
            request.setOutsideApiRequest("{\"woId\": " + request.getWoId() + "}");
            WoDTOResponse res = woBusinessImpl.getOrderGoodsDetail(request, getOrderGoodsDetail);
            response.setLstGoods(res.getLstData());
            response.setResultInfo(res.getResultInfo());
        }
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, response.getLstGoods());

        return Response.ok(resp).build();
    }

    @Override
    public Response getTcTctEmails() throws Exception {
        List<WoAppParamDTO> tcTctEmails = woDAO.getAppParam("ROLE_TC_TCT");
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, tcTctEmails);
        return Response.ok(resp).build();
    }

    @Override
    public Response getConstructionWOByHSHC(WoMappingHshcTcDTO dto) throws Exception {
        if (dto == null || dto.getWoHshcId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoDTO> woList = woBusinessImpl.getListWoByWoHshcId(dto);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, woList);
        return Response.ok(resp).build();
    }

    @Transactional
    @Override
    public Response createNewTmbtWO(WoTrDTO dto) {
        BaseResponseOBJ resp;
        Gson gson = new Gson();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
            Long trId = dto.getTrId();
            WoTrDTO trDTO = trDAO.getOneDetails(trId);
            // Lay danh sach tram giao thuê theo TR trong bảng WO_TR_MAPPING_STATION
            List<WoTrMappingStationDTO> lstTrMapStations = woTrMappingStationDAO.getStationsOfTr(trId);

            // Nếu có trạm thì sinh wo theo danh sách đó
            if (lstTrMapStations.size() > 0) {
                for (WoTrMappingStationDTO iCsr : lstTrMapStations) {
                    WoSimpleSysGroupDTO sysGroupDTO = woDAO.getSysGroup("" + iCsr.getSysGroupId());
                    // Generate TMBT follow trDTO
                    WoBO woBO = new WoBO();
                    // Wo code
                    String code = "VNM_PMXL_1_";
                    Long woTypeId = woTypeDAO.getIdByCode("TMBT");
                    Long nextSq = woDAO.getNextSeqVal();
                    Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
                    code += woTypeId + "_" + countIdType + "_" + nextSq;
                    woBO.setWoCode(code);
                    woBO.setTrCode(trDTO.getTrCode());
                    // Wo name
                    WoNameDTO dtoName = new WoNameDTO();
                    dtoName.setWoTypeId(woTypeId);
                    List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
                    if (lstNames != null && lstNames.size() > 0) {
                        woBO.setWoName(lstNames.get(0).getName());
                    }
                    woBO.setUserCreated(trDTO.getUserCreated());
                    woBO.setCdLevel1(trDTO.getCdLevel1());
                    woBO.setCdLevel1Name(trDTO.getCdLevel1Name());
                    woBO.setCdLevel2("" + iCsr.getSysGroupId());
                    woBO.setCdLevel2Name(sysGroupDTO.getGroupName());
                    woBO.setTrId(trDTO.getTrId());
                    woBO.setConstructionId(trDTO.getConstructionId());
                    woBO.setConstructionCode(trDTO.getConstructionCode());
                    woBO.setContractId(trDTO.getContractId());
                    woBO.setContractCode(trDTO.getContractCode());
                    woBO.setMoneyValue(0.0);
                    woBO.setCreatedDate(today);
                    woBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
                    woBO.setWoTypeId(woTypeId);
                    woBO.setQoutaTime(trDTO.getQoutaTime());
                    // Finish date
                    cal.add(Calendar.MONTH, 1);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.add(Calendar.DATE, -1);
                    woBO.setFinishDate(cal.getTime());
                    woBO.setState(ASSIGN_CD);
                    woBO.setStatus(1l);
                    woDAO.saveObject(woBO);

                    // Worklog tu sinh wo
                    woBusinessImpl.logWoWorkLogs(woBO.toDTO(), "1", "Tự động sinh WO TMBT khi TTHT tiếp nhận TR.", gson.toJson(woBO), dto.getLoggedInUser());

                    // Create checklist
                    WoMappingChecklistBO woMappingChecklistBO = new WoMappingChecklistBO();
                    woMappingChecklistBO.setWoId(woBO.getWoId());
                    woMappingChecklistBO.setCheckListId(1l);
                    woMappingChecklistBO.setState("NEW");
                    woMappingChecklistBO.setStatus(1l);
                    woMappingChecklistBO.setName(lstNames.get(0).getName());
                    woMappingCheckListDAO.saveObject(woMappingChecklistBO);

                    // Generate station code and insert wo_mapping_station
                    List<WoMappingStationBO> lstSaves = new ArrayList<>();
                    List<CatStationBO> lstCatStations = new ArrayList<>();
                    // List cat station new
                    String[] lstStations = iCsr.getLstStations().split(",");
                    for (String iStationId : lstStations) {
                        WoMappingStationBO woMappingStationBO = new WoMappingStationBO();
                        woMappingStationBO.setWoId(woBO.getWoId());
                        woMappingStationBO.setStatus(2l);
                        CatStationBO catStationBO = catStationDAO.getByStationId(Long.parseLong(iStationId));
                        if (catStationBO == null) {
                            woMappingStationBO.setReason("Mã trạm không tồn tại !");
                        } else {
                            woMappingStationBO.setCatStationId(catStationBO.getCatStationId());
                            catStationBO.setRentStatus(3l);
                            lstCatStations.add(catStationBO);
                        }
                        lstSaves.add(woMappingStationBO);
                    }
                    woMappingStationDAO.saveList(lstSaves);
                    catStationDAO.saveList(lstCatStations);
                }
            }
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            e.printStackTrace();
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response createNewDbhtWO(WoTrDTO dto) {
        BaseResponseOBJ resp;
        Gson gson = new Gson();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
            WoTrDTO trDTO = trDAO.getOneDetails(dto.getTrId());
            // Get AP_WORK_SRC
            List<WoAppParamDTO> lstApWorkSrcs = woDAO.getAppParam("AP_WORK_SRC");
            Long apWorkSrc = null;
            for (WoAppParamDTO iAp : lstApWorkSrcs) {
                if (iAp.getName().contains("HTCTHT")) {
                    apWorkSrc = Long.parseLong(iAp.getCode());
                    break;
                }
            }
            // Get AP_CONSTRUCTION_TYPE
            List<WoAppParamDTO> lstApConstructionTypes = woDAO.getAppParam("AP_CONSTRUCTION_TYPE");
            Long apConstructionType = null;
            for (WoAppParamDTO iAp : lstApConstructionTypes) {
                if (iAp.getName().contains("Hạ tầng cho thuê")) {
                    apConstructionType = Long.parseLong(iAp.getCode());
                    break;
                }
            }
            // Generate 7 wo DBHT follow trDTO
            List<WoAppParamDTO> lstDbhtWos = woDAO.getAppParam("DBHT_WO");
            for (WoAppParamDTO iWo : lstDbhtWos) {
                WoBO woBO = new WoBO();
                woBO.setCatWorkItemTypeId(Long.parseLong(iWo.getCode()));
                woBO.setApWorkSrc(apWorkSrc);
                woBO.setApConstructionType(apConstructionType);
                woBO.setStationCode(trDTO.getStationCode());
                // Wo code
                String code = "VNM_PMXL_1_";
                Long woTypeId = iWo.getParOrder() == 7l ? woTypeDAO.getIdByCode("PHATSONG") : woTypeDAO.getIdByCode("THICONG");
                Long nextSq = woDAO.getNextSeqVal();
                Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
                code += woTypeId + "_" + countIdType + "_" + nextSq;
                woBO.setWoCode(code);
                woBO.setTrCode(trDTO.getTrCode());
                // Wo name
                WoNameDTO dtoName = new WoNameDTO();
                dtoName.setWoTypeId(woTypeId);
                List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
                if (lstNames != null && lstNames.size() > 0) {
                    woBO.setWoName(lstNames.get(0).getName());
                }
                woBO.setUserCreated(trDTO.getUserCreated());
                woBO.setCdLevel1(trDTO.getCdLevel1());
                woBO.setCdLevel1Name(trDTO.getCdLevel1Name());
                woBO.setTrId(trDTO.getTrId());
                woBO.setConstructionId(trDTO.getConstructionId());
                woBO.setConstructionCode(trDTO.getConstructionCode());
                woBO.setContractId(trDTO.getContractId());
                woBO.setContractCode(trDTO.getContractCode());
                woBO.setMoneyValue(0.0);
                woBO.setCreatedDate(today);
                woBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
                woBO.setWoTypeId(woTypeId);
                woBO.setQoutaTime(trDTO.getQoutaTime());
                // Finish date
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.DATE, -1);
                woBO.setFinishDate(cal.getTime());
                woBO.setState(ASSIGN_CD);
                woBO.setStatus(1l);
                woDAO.saveObject(woBO);

                woBusinessImpl.logWoWorkLogs(woBO.toDTO(), "1", "Tự động sinh khi TTHT tiếp nhận TR.", gson.toJson(woBO), dto.getLoggedInUser());

                // Create checklist
                woBusinessImpl.tryCreateChecklistForNewWo(woBO.toDTO());
            }
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            e.printStackTrace();
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getLstStationsOfWo(WoMappingStationDTO dto) throws Exception {
        List<WoMappingStationDTO> lstStations = woMappingStationDAO.getStationsOfWo(dto.getWoId());

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, lstStations);
        return Response.ok(resp).build();
    }

    @Override
    public Response completeTkdtChecklist(WoChecklistDTO obj) {
        BaseResponseOBJ resp;
        try {
            woBusinessImpl.completeTkdtChecklist(obj);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response approvedWoOk(WoDTO dto, HttpServletRequest request) {
        BaseResponseOBJ resp;
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
            woBusinessImpl.approvedWoOk(dto, request);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);

        } catch (Exception e) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            e.printStackTrace();
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response getWoDataForChart(WoChartDataDto obj) {
        BaseResponseOBJ resp = null;
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
                Map<String,List<WoChartDataDto>> dataForChart = woBusinessImpl.getWoDataForChart(obj);
                resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dataForChart);
        } catch (Exception e) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            e.printStackTrace();
        }

        return Response.ok(resp).build();
    }
	@Override
	public Response exportConfigWorkItemList(WoScheduleConfigDTO dto, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
	        dto.setPage(1l);
	        dto.setPageSize(9999);
	        List<WoScheduleConfigDTO> listDto = woScheduleConfigDAO.doSearch(dto);
	        return Response.ok(woBusinessImpl.exportConfigWorkItemList(listDto)).build();
	}
	
	//Huypq-28062021-start
	@Override
    public Response exportFileImportTthq(WoDTO obj, HttpServletRequest request) {
        String strReturn = "";
        try {
            strReturn = woBusinessImpl.exportFileImportTthq(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
	
	@Override
    public Response importFileTthq(Attachment attachments, HttpServletRequest request) throws Exception {
        BaseResponseOBJ resp;
        boolean resul = true;
        String filePath;
        String filePathTthqReturn;
        List<EffectiveCalculationDetailsDTO> result = new ArrayList<>();
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
        String folderParamRequest = request.getParameter("folderParam");
        Long woId = Long.parseLong(UString.getSafeFileName(request.getParameter("woId")));
        String woCode = request.getParameter("woCode");
        String userCreateCode = request.getParameter("userCreate");
		String nameFile = request.getParameter("nameFile");
        Calendar c = Calendar.getInstance();
        Session session = effectiveCalculationDetailsBusinessImpl.gettDAO().getSessionFactory().openSession();
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParam)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }
        if (UString.isNullOrWhitespace(folderParamRequest)) {
        	folderParamRequest = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParamRequest)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParamRequest);
            }
        }
        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);

        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }

        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
//            String filePathTthq = UFile.writeToFileServerATTTNotSafeFileName(inputStream, fileName, folderParamRequest, folderUpload);
//            filePathTthqReturn = UEncrypt.encryptFileUploadPath(filePathTthq);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }
        try {
        	Long id = 0l;
            result = woBusinessImpl.importFileTthq(filePath, woId, request);
            
            if (result != null && !result.isEmpty()	&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
//            	effectiveCalculationDetailsDAO.deleteRecordEffective(session, woId);
//                for (EffectiveCalculationDetailsDTO dto : result) {
//                	dto.setStatus("1");
//                	dto.setWoCode(woCode);
//                	id = effectiveCalculationDetailsDAO.saveObject(dto.toModel());
//                }
//                effectiveCalculationDetailsDAO.updateTthqResultWoMappingChecklist(session, woId, result.get(0).getConclude());
//                Date date = new Date();
//                WoMappingAttachDTO dto = new WoMappingAttachDTO();
//                dto.setWoId(woId);
//                dto.setFilePath(filePathTthqReturn);
//                dto.setFileName(nameFile);
//                dto.setCreatedDate(date);
//                dto.setUserCreated(userCreateCode);
//                dto.setStatus(1l);
//                woMappingAttachDAO.save(dto.toModel());
            	resp = new BaseResponseOBJ(SUCCESS_CODE, "Import thành công", result);
                return Response.ok(resp).build();
            } else {
            	resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
                return Response.ok(resp).build();
            }
//            if (id != 0l) {
//                resp = new BaseResponseOBJ(SUCCESS_CODE, "Import thành công", result);
//                return Response.ok(resp).build();
//            } else {
//                resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
//                return Response.ok(resp).build();
//            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", result);
            return Response.ok(resp).build();
        }
    }
	
	@Override
	public Response getDataTableTTTHQ(EffectiveCalculationDetailsDTO obj) {
		DataListDTO data = effectiveCalculationDetailsBusinessImpl.getDataTableTTTHQ(obj);
        return Response.ok(data).build();
	}
	
	@Override
    public Response approvedWoOkTthq(WoDTO dto) {
        BaseResponseOBJ resp;
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
            woBusinessImpl.approvedWoOkTthq(dto);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);

        } catch (Exception e) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            e.printStackTrace();
        }

        return Response.ok(resp).build();
    }
	//Huy-end


	//HienLT56 start 27052021
	@Override
	public Response checkRoleTTHT(HttpServletRequest request) {
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.EDIT,
                Constant.AdResourceKey.ADMIN_WO, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(groupIdList.contains("242656")) {
        	return Response.ok(Response.Status.OK).build();
        } else {
        	return Response.ok(Response.Status.UNAUTHORIZED).build();
        }
	}

	@Override
	public Response checkRoleDeleteTTHT(HttpServletRequest request) {
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.DELETE,
                Constant.AdResourceKey.ADMIN_WO, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(groupIdList.contains("242656")) {
        	return Response.ok(Response.Status.OK).build();
        } else {
        	return Response.ok(Response.Status.UNAUTHORIZED).build();
        }
	}

	@Override
	public Response saveChangeForTTHT(WoDTO woDto) throws Exception {
		Gson gson = new Gson();
		Long id = woBusinessImpl.saveChangeForTTHT(woDto);
       	if(id != null && id != 0) {
       		woBusinessImpl.logWoWorkLogEditTTHT(woDto, "1", "Sửa wo " + woDto.getWoCode(), gson.toJson(woDto.toModel()), woDto.getLoggedInUser());
       		return Response.ok(woDto).build();
       	} else {
       		return Response.ok(Response.Status.UNAUTHORIZED).build();
       	}
	}
	//HienLT56 end 27052021
//	taotq start 23082021
	 @Override
	    public Response getAppWorkSource(WoDTO woDto) throws Exception {
	        List<WoAppParamDTO> appWorkSrc = woDAO.getAppWorkSource(AP_WORK_SRC);
	        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, appWorkSrc);
	        return Response.ok(resp).build();
	    }
	 
	 @Override
		public Response getListItemByWorkSrc() throws Exception {
	    	BaseResponseOBJ resp;
			List<WorkItemDTO> lst = woBusinessImpl.getListItemByWorkSrc();
			resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, lst);
	        return Response.ok(resp).build();
		}
	 @Override
		public Response getListItemN(String code) throws Exception {
	    	BaseResponseOBJ resp;
			List<WorkItemDTO> lst = woBusinessImpl.getListItemN(code);
			resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, lst);
	        return Response.ok(resp).build();
		}
	 
	 @Override
	    public Response createFileCheckList(WoMappingAttachDTO dto) throws Exception {
	        Date date = new Date();
	        dto.setCreatedDate(date);
	        woMappingAttachDAO.deleteFile(dto);
	        String returnStr = woMappingAttachDAO.save(dto.toModel());
	        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

	        return Response.ok(resp).build();
	    }
//		taotq end 27082021

	 //Duonghv13- start 14092021
	@Override
	public Response checkRoleCDPKTCNKT(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Boolean checkRole = woBusinessImpl.checkRoleCDPKTCNKT(request);
		if (checkRole) {
			return Response.ok(Status.OK).build();
		}
		return Response.ok().build();
		
	}
	//Duong end//

//	duonghv13 start 13102021
	@Override
	public Response checkConditionCertificate(ManageCertificateDTO certificateDTO) throws Exception {
		// TODO Auto-generated method stub
		List<ManageCertificateDTO> listCertificateDto = woBusinessImpl.getListCertificateEnableFT(certificateDTO);
	    boolean res;
	    if (listCertificateDto.size() >0) {
	        res = true;
	    } else {
	        res = false;
	    }
	    BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, res);
	    return Response.ok(resp).build();
	}
	
	 
	 //Huypq-22102021-start
	@Override
	public Response changeStateWaitPqt(WoDTO woDto, HttpServletRequest request) throws Exception {
		boolean checkRoleApproveHshcHtct = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.HTCT_HSHC, request);
		BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshcHtct, WAIT_PQT);

		return Response.ok(resp).build();
	}

	@Override
	public Response changeStateWaitTtDtht(WoDTO woDto, HttpServletRequest request) throws Exception {
		boolean checkRoleApproveHshcHtct = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.REVENUE_SALARY, request);
		BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshcHtct, WAIT_TTDTHT);

		return Response.ok(resp).build();
	}
	
	@Override
	public Response changeStateWaitTcTct(WoDTO woDto, HttpServletRequest request) throws Exception {
		boolean checkRoleApproveHshcHtct = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVE_DTHT,
				Constant.AdResourceKey.HTCT_HSHC, request);
		BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshcHtct, WAIT_TC_TCT);

		return Response.ok(resp).build();
	}
	 //Huy-end

	//Huypq-06112021-start
	@Override
	public Response getDataWorkItemByConsTypeId(CatWorkItemTypeDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(woBusinessImpl.getDataWorkItemByConsTypeId(obj)).build();
	}
	
	@Override
    public Response changeStateCdPause(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, CD_PAUSE);
        
        return Response.ok(resp).build();
    }
	
	@Override
    public Response changeStateTthtPause(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, TTHT_PAUSE);
        
        return Response.ok(resp).build();
    }
	
	@Override
    public Response changeStateDthtPause(WoDTO woDto, HttpServletRequest request) throws Exception {
		BaseResponseOBJ resp = null;
		try {
			boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
	        resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, DTHT_PAUSE);


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Response.ok(resp).build();
    }
	
	@Override
    public Response getDataConstructionContractByStationCode(String stationCode) {
        return Response.ok(woBusinessImpl.getDataConstructionContractByStationCode(stationCode)).build();
    }
	
	@Override
    public Response changeStateCdPauseReject(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, CD_PAUSE_REJECT);
        
        return Response.ok(resp).build();
    }
	
	@Override
    public Response changeStateTthtPauseReject(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, TTHT_PAUSE_REJECT);
        
        return Response.ok(resp).build();
    }
	
	@Override
    public Response changeStateDthtPauseReject(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, DTHT_PAUSE_REJECT);
        
        return Response.ok(resp).build();
    }
	
	@Override
    public Response changeStateApprovedOrRejectWoHtctPQT(WoDTO woDto, HttpServletRequest request) throws Exception {
		boolean checkRoleApproveHshc = false;
		if(PQT_NG.equalsIgnoreCase(woDto.getState()) || RECEIVED_PQT.equalsIgnoreCase(woDto.getState())) {
			checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
		} else {
			checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVE_DTHT, Constant.AdResourceKey.HTCT_HSHC, request);
		}
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, woDto.getState());
        return Response.ok(resp).build();
    }
	
	@Override
    public Response changeStateApprovedOrRejectWoHtctTtDtht(WoDTO woDto, HttpServletRequest request) throws Exception {
        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVE_DTHT, Constant.AdResourceKey.HTCT_HSHC, request);
        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, woDto.getState());
        return Response.ok(resp).build();
    }
	
	@Override
    public Response getWorkItemCompleteByConstruction(WorkItemDTO obj) {
        BaseResponseOBJ resp;
        Long consId = obj.getConstructionId();
        if (consId == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        List<WoDTO> workItems = woDAO.getWorkItemCompleteByConstruction(consId);
        
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, workItems);
        return Response.ok(resp).build();
    }
	
	 @Override
	    public Response changeStateWoReject(WoDTO woDto, HttpServletRequest request) throws Exception {
	        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
	        BaseResponseOBJ resp = woBusinessImpl.approvedWo(woDto, checkRoleApproveHshc, NG);
	        return Response.ok(resp).build();
	    }
	//Huy-end
	 
	@Override
	public Response changeStateReProcessWoDoanhThuDTHT(WoDTO dto) {
		BaseResponseOBJ resp;
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		try {
			woBusinessImpl.changeStateReProcessWoDoanhThuDTHT(dto);
			resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);

		} catch (Exception e) {
			resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
			e.printStackTrace();
		}

		return Response.ok(resp).build();
	}
	@Override
	public Response checkRoleApproveCDLV5(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Boolean checkRole = woBusinessImpl.checkRoleApproveCDLV5(request);
		if (checkRole) {
			return Response.ok(Status.OK).build();
		}
		return Response.ok().build();
		
	}

	@Override
	public Response checkContractIsGpxd(Long contractId, HttpServletRequest request) throws Exception {

        if(woDAO.checkCntContractIsGpxd(contractId)) {
        	return Response.ok(Response.Status.OK).build();
        } else {
        	return Response.ok(Response.Status.UNAUTHORIZED).build();
        }
	}
	@Override
    public Response checkAsignAdminWo(HttpServletRequest request) {
        if (VpsPermissionChecker.hasPermission(Constant.OperationKey.ASSIGN , Constant.AdResourceKey.ADMIN_WO, request)) {
            return Response.ok(Response.Status.OK).build();
        }
        return Response.ok(Response.Status.UNAUTHORIZED).build();
    }

    @Override
    public Response getStationResource(AssetHandoverList assetHandoverList) {
        BaseResponseOBJ resp;
        if (ObjectUtils.isEmpty(assetHandoverList.getStationCode())) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        List<AssetHandoverList> assetHandoverLists = woBusinessImpl.getDataAssetHandoverList(assetHandoverList.getStationCode());
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, assetHandoverLists);
        return Response.ok(resp).build();
    }

    @Override
    public Response rejectWoBgbtsVhkt(WoDTO woDto) throws Exception {
        BaseResponseOBJ resp;
        try {
            if (ObjectUtils.isEmpty(woDto.getWoId())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            WoBO bo = woDAO.getOneRaw(woDto.getWoId());
            // chuyển trạng thái wo hiện tại
            woBusinessImpl.updateAndWriteLogWo(bo, woDto.getLoggedInUser(),woDto);

            // chuyển trạng thái wo DTHT
            woBusinessImpl.updateAndWriteLogWoBtsDHDT(woDto, bo);

            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        }catch (Exception e){
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }
        return Response.ok(resp).build();
    }

//Huy-end
}
