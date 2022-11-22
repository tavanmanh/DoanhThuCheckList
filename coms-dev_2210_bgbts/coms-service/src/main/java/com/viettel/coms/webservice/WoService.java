package com.viettel.coms.webservice;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.viettel.coms.bo.WoBO;
import com.viettel.coms.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.bo.WoTaskDailyBO;
import com.viettel.coms.bo.WoWorkLogsBO;
import com.viettel.coms.business.WoBusiness;
import com.viettel.coms.business.WoBusinessImpl;
import com.viettel.coms.business.WoDashboardBusiness;
import com.viettel.coms.business.WoPlanBusiness;
import com.viettel.coms.business.WoTrBusiness;
import com.viettel.coms.dao.WoClassQoutaDAO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dao.WoMappingChecklistDAO;
import com.viettel.coms.dao.WoMappingGoodsDAO;
import com.viettel.coms.dao.WoMappingStationDAO;
import com.viettel.coms.dao.WoTrDAO;
import com.viettel.coms.dao.WoWorkLogsDAO;
import com.viettel.coms.dto.avg.GetWoAvgOutputDto;
import com.viettel.coms.dto.avg.WoAvgResponseDTO;
import com.viettel.coms.dto.avg.WoAvgVhktInputDTO;
import com.viettel.coms.dto.vttb.VoGoodsDTO;
import com.viettel.coms.utils.BaseResponseOBJ;
import com.viettel.ktts2.common.UConfig;
import com.viettel.utils.Constant;
import com.viettel.wms.business.AppParamBusinessImpl;
import com.viettel.wms.dto.AppParamDTO;
import org.springframework.util.ObjectUtils;

@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
@Path("/")
public class WoService {
    private final int SUCCESS_CODE = 1;
    private final int ERROR_CODE = -1;
    private final String SUCCESS_MSG = "SUCCESS";
    private final String FAIL_MSG = "FAILD";
    private final String ERROR_MSG = "Có lỗi xảy ra.";

    private final String getListStockUrl = UConfig.get("ktts.wms.service") + "stockRsServiceRest/woGetForAutoCompleteStockDomain";
    private final String getMerStockUrl = UConfig.get("ktts.wms.service") + "merEntityServiceRest/merEntity/getMerStock";
    private final String woSaveImportOrderUrl = UConfig.get("ktts.wms.service") + "orderServiceRest/order/woSaveImportOrder";
    private final String getLstUserRoleUrl = UConfig.get("ktts.wms.service") + "userRoleServiceRest/userRole/getLstUserRole";
    private final String getRoleByEmailUrl = UConfig.get("ktts.wms.service") + "userRoleServiceRest/userRole/getRoleByEmail";
    private final String woSignVofficeUrl = UConfig.get("ktts.wms.service") + "reportServiceRest/woSignVoffice";
    private final String checkVofficeInfoUrl = UConfig.get("ktts.wms.service") + "orderServiceRest/order/APIDoSearchExportRequirement";
    private final String getOrderGoodsDetail = UConfig.get("ktts.ams.service") + "amsOrderGoodsRsService/doOrderGoodsDetail";
    private final String saveVofficepassMB = UConfig.get("ktts.wms.service") + "commonServiceRest/saveVofficepassMB";
    private final String getListGoodsForWO = UConfig.get("ktts.wms.service") + "vsmartWorkOrderWebService/service/getListGoodsForWO";
    private final String reduceGoodsInStock = UConfig.get("ktts.wms.service") + "vsmartWorkOrderWebService/service/reduceGoodsInStock";

    private Logger LOGGER = Logger.getLogger(WoService.class);
    @Autowired
    WoBusiness woBusiness;
    @Autowired
    AuthenticateWsBusiness authenticateWsBusiness;
    @Autowired
    AppParamBusinessImpl appParamBusinessImpl;
    @Autowired
    WoWorkLogsDAO woWorkLogsDAO;
    @Autowired
    WoDAO woDAO;
    @Autowired
    WoPlanBusiness planBusiness;
    @Autowired
    WoMappingChecklistDAO woMappingChecklistDAO;
    @Autowired
    WoDashboardBusiness woDashboardBusiness;
    @Autowired
    WoTrBusiness woTrBusiness;
    @Autowired
    WoTrDAO woTrDAO;
    @Autowired
    WoMappingStationDAO woMappingStationDAO;
    @Autowired
    WoClassQoutaDAO woClassQoutaDAO;
    @Autowired
    WoMappingGoodsDAO woMappingGoodsDAO;
    
    @Autowired
    WoBusinessImpl woBusinessImpl;

    Gson gson = new Gson();

    @POST
    @Path("list")
    public WoDTOResponse list(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<WoDTO> lstWos = woBusiness.list(request, null, request.getFilter());
            response.setLstWos(lstWos);
//            response.setTotalRecord(request.getWoDTO().getTotalRecord());
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("getById")
    public WoDTOResponse getById(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            WoDTO woDTO = woBusiness.getById(request.getSysUserRequest().getSysUserId(), request.getWoDTO().getWoId());
            response.setWoDTO(woDTO);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("update")
    public WoDTOResponse update(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        SysUserRequest sysU = request.getSysUserRequest();
        ResultInfo resultInfo = new ResultInfo();
        try {
            authenticateWsBusiness.validateRequest(sysU);
            //Huypq-07082021-start ko cho duyệt wo TTHQ
            if ("DONE".equalsIgnoreCase(request.getWoDTO().getState())) {
            	WoDTO woDTO = woDAO.getOneDetails(request.getWoDTO().getWoId());
                String messageValidate = validateInfoAssetExist(request,woDTO);
                if(!ObjectUtils.isEmpty(messageValidate)){
                    resultInfo.setStatus(ResultInfo.RESULT_NOK);
                    resultInfo.setMessage(messageValidate);
                    response.setResultInfo(resultInfo);
                    return response;
                }

            	if("TTHQ".equalsIgnoreCase(woDTO.getWoTypeCode())) {
            		resultInfo.setStatus(ResultInfo.RESULT_NOK);
                    resultInfo.setMessage("Chưa đính kèm file tính toán hiệu quả");
                    response.setResultInfo(resultInfo);
                    return response;
            	}
            }
            //Huy-end
            
            WoDTO wo = request.getWoDTO();

            //ghi nhận thêm trường USER_FT_RECEIVE_WO và UPDATE_FT_RECEIVE_WO
            if ("ACCEPT_FT".equalsIgnoreCase(wo.getState())) {
                wo.setUpdateFtReceiveWo(new Date());
                wo.setAcceptTime(new Date());
                if (sysU != null) wo.setUserFtReceiveWo(String.valueOf(sysU.getSysUserId()));
            }

            WoDTO woDTO = woDAO.getOneDetails(wo.getWoId());
            wo.setCreatedDate(woDTO.getCreatedDate());
            wo.setStartTime(woDTO.getStartTime());
            
            woBusiness.update(wo, request.getOpinionContent(), sysU);

            resultInfo.setStatus(ResultInfo.RESULT_OK);
            resultInfo.setMessage(wo.getCustomField());

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }

        response.setResultInfo(resultInfo);
        return response;
    }

    private String validateInfoAssetExist(WoDTORequest request, WoDTO woDTO) {
        String messageValidate = "";
        if(Constant.WO_STATE.DONE.equalsIgnoreCase(request.getWoDTO().getState())
                && Constant.WO_TYPE_CODE.BGBTS_DTHT.equalsIgnoreCase(request.getWoDTO().getWoTypeCode())
                && Constant.WO_STATE.PROCESSING.equalsIgnoreCase(woDTO.getState())
        ) {
            List<AssetHandoverList> assetHandoverLists = woBusinessImpl.getDataAssetHandoverList(request.getWoDTO().getStationCode());
            if(ObjectUtils.isEmpty(assetHandoverLists)){
                messageValidate = "Bạn chưa đối soát tài sản trên PMQLTS";
                return messageValidate;
            }
        }

        return null;
    }

    @POST
    @Path("updateOpinion")
    public WoDTOResponse updateOpinion(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        SysUserRequest sysU = request.getSysUserRequest();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            String content = "- Loại ý kiến: " + request.getOpinionType() + "\n- Nội dung: " + request.getOpinionContent() + "\n"
                    + "- Đối tượng: " + request.getOpinionObject();
            //Huypq-02112021-start
            if(request.getOpinionType()!=null && (request.getOpinionType().equalsIgnoreCase("Đề xuất gia hạn") || request.getOpinionType().equalsIgnoreCase("Đề xuất hủy"))) {
            	request.getWoDTO().setOpinionType(request.getOpinionType());
            	request.getWoDTO().setState("PAUSE");
            }
            //Huy-end
            woBusiness.update(request.getWoDTO(), content, sysU);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("listChecklists")
    public WoDTOResponse listChecklists(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            if ("HCQT".equalsIgnoreCase(request.getWoDTO().getWoTypeCode())) {
                List<WoChecklistDTO> lstChecklists = woBusiness.getListChecklistsOfWoHcqt(request.getWoDTO().getWoId());
                response.setLstChecklistsOfWoHcqt(lstChecklists);
            } else if ("AVG".equalsIgnoreCase(request.getWoDTO().getWoTypeCode())) {
                List<WoChecklistDTO> lstChecklists = woBusiness.getListChecklistsOfWoAvg(request.getWoDTO().getWoId());
                response.setLstChecklistsOfAvg(lstChecklists);
            } else {
                List<WoMappingChecklistDTO> lstChecklists = woBusiness.getListChecklistsOfWo(request.getWoDTO().getWoId(), request.getWoDTO().getWoTypeCode());
                response.setLstChecklistsOfWo(lstChecklists);
            }
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("listWorklogs")
    public WoDTOResponse listWorklogs(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<WoWorkLogsBO> logs = woBusiness.getListWorklogsOfWo(request.getWoDTO().getWoId());
            response.setLogs(logs);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("updateChecklist")
    public WoDTOResponse updateChecklist(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        WoDTO woDTO = request.getWoDTO();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());

            if ("HCQT".equalsIgnoreCase(woDTO.getWoTypeCode())) {
                for (WoChecklistDTO woChecklistDTO : request.getLstChecklistsOfWoHcqt()) {
                    woBusiness.updateChecklistHcqt(woDTO, woChecklistDTO);
                }
            } else {
                for (WoMappingChecklistDTO woMappingChecklistDTO : request.getLstChecklistsOfWo()) {
                    // nếu là wo bts thì check số lượng ảnh
                    String messageValidate = validateSizeImgWoBts(woMappingChecklistDTO,woDTO);
                    if(!ObjectUtils.isEmpty(messageValidate)){
                        resultInfo.setStatus(ResultInfo.RESULT_NOK);
                        resultInfo.setMessage(messageValidate);
                        response.setResultInfo(resultInfo);
                        return response;
                    }

                    String quantityByDate = woMappingChecklistDTO.getQuantityByDate();

                    // Cập nhật sản lượng theo ngày vào bảng wo_task_daily
                    if (woMappingChecklistDTO.getAddedQuantityLength() != null && "1".equalsIgnoreCase(quantityByDate)) {
                        boolean result = woBusiness.insertUpdateDailyChecklist(woMappingChecklistDTO);
                        if (!result) {
                            resultInfo.setStatus(ResultInfo.RESULT_NOK);
                            resultInfo.setMessage("Sản lượng theo ngày hôm nay đã được duyệt. Không thể thêm!");
                            response.setResultInfo(resultInfo);
                            return response;
                        }
                    }

                    //cập nhật sản lượng theo tháng vào bảng wo_task_monthly
                    if (woMappingChecklistDTO.getAddedQuantityLength() != null && "2".equalsIgnoreCase(quantityByDate)) {
                        boolean result = woBusiness.declareThdtAddedValue(woMappingChecklistDTO);

                        if (!result) {
                            resultInfo.setStatus(ResultInfo.RESULT_NOK);
                            resultInfo.setMessage(woMappingChecklistDTO.getCustomField());
                            response.setResultInfo(resultInfo);
                            return response;
                        }
                    }

                    // Cap nhat san luong xddd wo_xddd_checklist
                    if ("THICONG".equalsIgnoreCase(woDTO.getWoTypeCode())
                            && woMappingChecklistDTO.getValue() != null 
                            && woDTO.getApWorkSrc() != null && woDTO.getApWorkSrc() == 6
                            && woDTO.getCatConstructionTypeId() != null && woDTO.getCatConstructionTypeId() == 8) {
                    	if(woDTO.getCatWorkItemTypeName()!=null && woDTO.getCatWorkItemTypeName().equalsIgnoreCase("Khởi công")) {
                    		woMappingChecklistDTO.setValue(0d);
                    		woBusiness.updateChecklistXddd(woDTO, woMappingChecklistDTO);
                            resultInfo.setStatus(ResultInfo.RESULT_OK);
                            response.setResultInfo(resultInfo);
                            return response;
                    	} else if(woMappingChecklistDTO.getValue() > 0) {
                    		woBusiness.updateChecklistXddd(woDTO, woMappingChecklistDTO);
                            resultInfo.setStatus(ResultInfo.RESULT_OK);
                            response.setResultInfo(resultInfo);
                            return response;
                    	}
                    	
                    }
                    woBusiness.updateChecklist(woDTO, woMappingChecklistDTO);
                }
            }
            resultInfo.setStatus(ResultInfo.RESULT_OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }
        response.setResultInfo(resultInfo);
        return response;
    }

    private String validateSizeImgWoBts(WoMappingChecklistDTO woMappingChecklistDTO, WoDTO woDTO) {
        String messageValidate = "";
        if(Constant.WO_TYPE_CODE.BGBTS_DTHT.equalsIgnoreCase(woDTO.getWoTypeCode()) && Constant.STATE_CHECK_LIST.DONE.equals(woMappingChecklistDTO.getState())){
            List<ImgChecklistDTO> lstImgs = woMappingChecklistDTO.getLstImgs();
            if(Constant.CHECK_LIST_ID.WO_BGBTS_1.equals(woMappingChecklistDTO.getChecklistId())){
                // nếu check list id = 1 thì size ảnh ít nhât là 4
                if(lstImgs.size() <4){
                    messageValidate = "Bạn cần chọn ít nhất 4 ảnh";
                    return messageValidate;
                }
            }else if(Constant.CHECK_LIST_ID.WO_BGBTS_2.equals(woMappingChecklistDTO.getChecklistId())){
                // nếu check list id = 2 thì size ảnh ít nhât là 6
                if(lstImgs.size() <6){
                    messageValidate = "Bạn cần chọn ít nhất 6 ảnh";
                    return messageValidate;
                }
            }else if(Constant.CHECK_LIST_ID.WO_BGBTS_3.equals(woMappingChecklistDTO.getChecklistId())){
                // nếu check list id = 3 thì size ảnh ít nhât là 4
                if(lstImgs.size() <4){
                    messageValidate = "Bạn cần chọn ít nhất 4 ảnh";
                    return messageValidate;
                }
            }else if(Constant.CHECK_LIST_ID.WO_BGBTS_4.equals(woMappingChecklistDTO.getChecklistId())){
                // nếu check list id = 4 thì size ảnh ít nhât là 6
                if(lstImgs.size() <6){
                    messageValidate = "Bạn cần chọn ít nhất 6 ảnh";
                    return messageValidate;
                }
            }else if(Constant.CHECK_LIST_ID.WO_BGBTS_5.equals(woMappingChecklistDTO.getChecklistId())){
                // nếu check list id = 5 thì size ảnh ít nhât là 4
                if(lstImgs.size() <4){
                    messageValidate = "Bạn cần chọn ít nhất 4 ảnh";
                    return messageValidate;
                }
            }else if(Constant.CHECK_LIST_ID.WO_BGBTS_6.equals(woMappingChecklistDTO.getChecklistId())){
                // nếu check list id = 6 thì size ảnh ít nhât là 1
                if(lstImgs.size() <1){
                    messageValidate = "Bạn cần chọn ít nhất 1 ảnh";
                    return messageValidate;
                }
            }
        }
        return null;
    }

    @POST
    @Path("getForComboBox")
    public WoDTOResponse getForComboBox(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        AppParamDTO obj = new AppParamDTO();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            obj.setStatus("1");
            obj.setParType((String) request.getFilter().get("parType"));
            response.setLstDataForComboBox(appParamBusinessImpl.getForComboBox(obj));
            resultInfo.setStatus(ResultInfo.RESULT_OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }
        response.setResultInfo(resultInfo);
        return response;
    }

    @POST
    @Path("getListFtToAssign")
    public WoDTOResponse getListFtToAssign(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<WoSimpleFtDTO> listFtToAssign = woBusiness.getListFtToAssign(request.getGroupId());
            response.setListFtToAssign(listFtToAssign);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }
        response.setResultInfo(resultInfo);
        return response;
    }

    @POST
    @Path("createTRContractAIO")
    public String createTRContractAIO(AIOWoTrDTO aioWoTrDTO) {

        String result = "";
        try {
            result = woBusiness.createTRContractAIO(aioWoTrDTO);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return result;
    }

    @POST
    @Path("updateTRContractAIO")
    public String updateTRContractAIO(AIOWoTrDTO aioWoTrDTO) {
        String result = "";
        try {
            result = woBusiness.updateTRContractAIO(aioWoTrDTO);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return result;
    }

    @POST
    @Path("getDataForChart")
    public WoDTOResponse getDataForChart(SysUserRequest request) {
        Long loginUserId = request.getSysUserId();
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            // Get domain list
            List<DomainDTO> lstDoamins = woDAO.getByAdResource(loginUserId, "VIEW WOXL");
            List<String> lstGroupIds = new ArrayList<>();
            for (DomainDTO iDomain : lstDoamins) {
                lstGroupIds.add("" + iDomain.getDataId());
            }

            List<WoGeneralReportDTO> lstDataForChart = woDAO.getDataForChart(loginUserId, lstGroupIds);
            if (lstDataForChart.size() == 0) {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("No data");
                response.setResultInfo(resultInfo);
                return response;
            }

            WoGeneralReportDTO report = lstDataForChart.get(0);
            int totalDone = 0;
            int totalUndone = 0;
            List<WoPlanDTO> listWoPlans = planBusiness.getDataForPlanChart(request.getSysUserId());
            for (WoPlanDTO plan : listWoPlans) {
                if (plan.getNumWoOfPlan() == plan.getWoOk()) {
                    totalDone += 1;
                } else {
                    totalUndone += 1;
                }
            }

            Map<String, Integer> mapDataWoForChart = new HashMap<>();
            // WO
            mapDataWoForChart.put("ASSIGN_FT", report.getTotalAssignFt().intValue());
            mapDataWoForChart.put("ACCEPT_FT", report.getTotalAcceptFt().intValue());
            mapDataWoForChart.put("REJECT_FT", report.getTotalRejectFt().intValue());
            mapDataWoForChart.put("ASSIGN_CD", report.getTotalAssignCd().intValue());
            mapDataWoForChart.put("REJECT_CD", report.getTotalRejectCd().intValue());
            mapDataWoForChart.put("PROCESSING", report.getTotalProcessing().intValue());
            mapDataWoForChart.put("DONE", report.getTotalDone().intValue());
            mapDataWoForChart.put("OK", report.getTotalOk().intValue());
            mapDataWoForChart.put("NG", report.getTotalNotGood().intValue());
            response.setMapDataWoForChart(mapDataWoForChart);

            // Plan
            Map<String, Integer> mapDataWoPlanForChart = new HashMap<>();
            mapDataWoPlanForChart.put("DONE", totalDone);
            mapDataWoPlanForChart.put("UNDONE", totalUndone);
            response.setMapDataWoPlanForChart(mapDataWoPlanForChart);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }

        // TR
        try {
            List<String> lstTrGroupIds = new ArrayList<>();
            List<DomainDTO> lstTrDoamins = woDAO.getByAdResource(loginUserId, "VIEW WOXL_TR");
            for (DomainDTO iDomain : lstTrDoamins) {
                lstTrGroupIds.add("" + iDomain.getDataId());
            }

            List<DomainDTO> lstTrDoamins2 = woDAO.getByAdResource(loginUserId, "VIEW WOXL_TR_XDDTHT");
            for (DomainDTO iDomain : lstTrDoamins2) {
                lstTrGroupIds.add("" + iDomain.getDataId());
            }

            List<DomainDTO> lstTrDoamins3 = woDAO.getByAdResource(loginUserId, "VIEW WOXL_TR_GPTH");
            for (DomainDTO iDomain : lstTrDoamins3) {
                lstTrGroupIds.add("" + iDomain.getDataId());
            }
            List<WoGeneralReportDTO> lstDataTrForChart = woTrDAO.getDataTrForChart(loginUserId, lstTrGroupIds);
            if (lstDataTrForChart.size() == 0) {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("No data");
                response.setResultInfo(resultInfo);
                return response;
            }
            WoGeneralReportDTO reportTr = lstDataTrForChart.get(0);

            Map<String, Integer> mapDataTrForChart = new HashMap<>();
            mapDataTrForChart.put("OK", reportTr.getTotalTrOk().intValue());
            mapDataTrForChart.put("OPINION_RQ", reportTr.getTotalTrOpinionRq().intValue());
            mapDataTrForChart.put("PROCESSING", reportTr.getTotalTrProcessing().intValue());
            mapDataTrForChart.put("ASSIGN_CD", reportTr.getTotalTrAssignCd().intValue());
            mapDataTrForChart.put("ASSIGN_FT", reportTr.getTotalTrAssignFt().intValue());
            mapDataTrForChart.put("NOK", reportTr.getTotalTrNotOk().intValue());
            mapDataTrForChart.put("DONE", reportTr.getTotalTrDone().intValue());
            mapDataTrForChart.put("REJECT_CD", reportTr.getTotalTrRejectCd().intValue());
            mapDataTrForChart.put("ACCEPT_CD", reportTr.getTotalTrAcceptCd().intValue());
            mapDataTrForChart.put("OTHER", reportTr.getTotalTR().intValue() - reportTr.getTotalTrOk().intValue()
                    - reportTr.getTotalTrAssignCd().intValue()
                    - reportTr.getTotalTrNotOk().intValue()
                    - reportTr.getTotalTrRejectCd().intValue()
                    - reportTr.getTotalTrAcceptCd().intValue());
            response.setMapDataWoTrForChart(mapDataTrForChart);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }

        resultInfo.setStatus(ResultInfo.RESULT_OK);
        response.setResultInfo(resultInfo);
        return response;
    }

    @POST
    @Path("listChecklistsDetail")
    public WoDTOResponse listChecklistsDetail(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        long woId = request.getWoId();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            if (request.getWoDTO() != null && "AVG".equalsIgnoreCase(request.getWoDTO().getWoTypeCode())) {
                WoChecklistDTO woChecklistDTO = woBusiness.getChecklistsDetailAvg(woId, request.getCheckListId());
                response.setLstChecklistsOfAvg(Arrays.asList(woChecklistDTO));

                // Lay danh sach vat tu da luu trong DB
                WoMappingGoodsDTO searchObj = new WoMappingGoodsDTO();
                searchObj.setWoId(woId);
                List<WoMappingGoodsDTO> goodsFromDB = woMappingGoodsDAO.doSearch(searchObj);

                // Lay danh sach vat tu thuoc kho ca nhan cua user
                Long loginUserId = request.getSysUserRequest().getSysUserId();
                String employeeCode = woTrDAO.getSysUser(loginUserId).getEmployeeCode();
                String apiResult = woBusiness.callOutsideApi("{\"employeeCode\": \"" + employeeCode + "\"}", getListGoodsForWO);
                JSONObject jsonObject = new JSONObject(apiResult);

                Type typeResultInfo = new TypeToken<ResultInfo>() {
                }.getType();
                ResultInfo ListGoodsResult = gson.fromJson(jsonObject.getJSONObject("resultInfo").toString(), typeResultInfo);

                if ("OK".equalsIgnoreCase(ListGoodsResult.getStatus())) {
                    Type typeListMerEntity = new TypeToken<List<VoGoodsDTO>>() {
                    }.getType();
                    List<VoGoodsDTO> listMerEntity = gson.fromJson(jsonObject.getJSONArray("listMerEntity").toString(), typeListMerEntity);
                    for (VoGoodsDTO iGoods : listMerEntity) {
                        iGoods.setName(StringUtils.isNotEmpty(iGoods.getSerial()) ? iGoods.getGoodsName() + " - " + iGoods.getSerial() : iGoods.getGoodsName());
                    }
                    response.setListMerEntity(listMerEntity);

                    // Vat tu co serial dang duoc tra ra thanh nhieu ban ghi -> thuc hien gop lai thanh 1 thoi
                    List<VoGoodsDTO> listGoodsOfWo = new ArrayList<>();
                    Map<Long, List<String>> mapSerials = new HashMap<>();
                    for (VoGoodsDTO iEntity : listMerEntity) {
                        String currentSerial = iEntity.getSerial();
                        iEntity.setSerial("");
                        // Set name cho mobile hien thi
                        iEntity.setName(StringUtils.isNotEmpty(iEntity.getCode()) ? iEntity.getCode() + " - " + iEntity.getName() : iEntity.getName());
                        // KTTS tra ve null thi cap nhat thanh 0 cho mobile ko bi null pointer
                        if (iEntity.getIsUsed() == null) {
                            iEntity.setIsUsed(0l);
                        }
                        // Lay thong tin da cap nhat tu DB
                        for (WoMappingGoodsDTO item : goodsFromDB) {
                            if (item.getGoodsId().equals(iEntity.getGoodsId())) {
                                iEntity.setAmount(item.getAmount());
                                iEntity.setAmountNeed(item.getAmountNeed());
                                iEntity.setAmountReal(item.getAmountReal());
                                iEntity.setIsSerial(item.getIsSerial());
                                iEntity.setIsUsed(item.getIsUsed());
                                iEntity.setSerial(item.getSerial());
                                break;
                            }
                        }

                        Long goodId = iEntity.getGoodsId();
                        if (iEntity.getIsSerial() == 1) {
                            if (mapSerials.get(goodId) == null) {
                                List<String> lstSerials = new ArrayList<>();
                                lstSerials.add("-- Chọn serial --");
                                lstSerials.add(currentSerial);
                                mapSerials.put(goodId, lstSerials);
                                listGoodsOfWo.add(iEntity);
                            } else {
                                List<String> lstSerials = mapSerials.get(goodId);
                                lstSerials.add(currentSerial);
                                mapSerials.put(goodId, lstSerials);
                            }
                            iEntity.setListSerials(mapSerials.get(goodId));
                        } else {
                            listGoodsOfWo.add(iEntity);
                        }
                    }
                    for (VoGoodsDTO iGood : listGoodsOfWo) {
                        if (iGood.getIsSerial() == 1) {
                            iGood.setAmount((double) iGood.getListSerials().size() - 1);
                        }
                    }
                    response.setLstGoodsOfWo(listGoodsOfWo);
                }
            } else {
                //authenticateWsBusiness.validateRequest(request.getSysUserRequest());
                WoMappingChecklistDTO woMappingChecklistDTO = woBusiness.listChecklistsDetail(woId, request.getCheckListId());
                response.setWoMappingChecklist(woMappingChecklistDTO);
            }


            // TMBT thi lay them danh sach tram
            WoDTO woDTO = woDAO.getOneDetails(woId);
            if ("TMBT".equalsIgnoreCase(woDTO.getWoTypeCode())) {
                List<CatStationDTO> lstCatStations = woDAO.getCatStationOfWoTmbt(woId, false);
                response.setLstCatStation(lstCatStations);
            }

            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    // test
    @POST
    @Path("listChecklistsTest")
    public WoDTOResponse listChecklistsTest(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            if ("HCQT".equalsIgnoreCase(request.getWoDTO().getWoTypeCode())) {
                List<WoChecklistDTO> lstChecklists = woBusiness.getListChecklistsOfWoHcqt(request.getWoDTO().getWoId());
                response.setLstChecklistsOfWoHcqt(lstChecklists);
            } else {
                List<WoMappingChecklistDTO> lstChecklists = woBusiness.getListChecklistsOfWoTest(request.getWoDTO().getWoId());
                response.setLstChecklistsOfWo(lstChecklists);
            }
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    // test
    @POST
    @Path("declareHcqtIssue")
    public BaseResponseOBJ declareHcqtIssue(WoChecklistDTO obj) {
        BaseResponseOBJ resp;
        try {
            woBusiness.declareHcqtProblem(obj);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return resp;
    }

    @POST
    @Path("resolveHcqtIssue")
    public BaseResponseOBJ resolveHcqtIssue(WoChecklistDTO obj) {
        BaseResponseOBJ resp;
        try {
            woBusiness.resolveHcqtProblem(obj);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        return resp;
    }

    @POST
    @Path("getFtListFromLv2SysGroup")
    public BaseResponseOBJ getFtListFromLv2SysGroup(WoSimpleFtDTO ft) {
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        if (StringUtils.isEmpty(ft.getKeySearch())) return resp;

        Long groupId = ft.getSysGroupId();
        List<WoSimpleFtDTO> ftList = woDAO.getFtListFromLv2SysGroup(groupId, ft.getKeySearch());
        resp.setData(ftList);
        return resp;
    }

    @POST
    @Path("giveWOAssignment")
    public BaseResponseOBJ giveWOAssignment(WoDTO woDto) {
        BaseResponseOBJ resp;
        
      //Huypq-05082021-start
        if(woDto.getWoTypeCode().equalsIgnoreCase("THICONG") && woDto.getState().equalsIgnoreCase("ACCEPT_CD") && "XDDD".equalsIgnoreCase(woDto.getApWorkSrcName().split("-")[0].trim())) {
        	List<Long> lstXdddCheckList = woDAO.checkWoXdddCl(woDto.getWoId());
        	if(lstXdddCheckList.isEmpty()) {
        		String errorMsg = "Cần tạo đầu việc XDDD trước khi giao WO xuống !";
            	return new BaseResponseOBJ(ERROR_CODE, errorMsg, null);
        	}
        }
        //huy-end
        
        boolean assignResult = woBusiness.giveAssignment(woDto);

        if (assignResult)
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else {
            String errorMsg = woDto.getCustomField();
            if (StringUtils.isEmpty(errorMsg)) errorMsg = ERROR_MSG;
            resp = new BaseResponseOBJ(ERROR_CODE, errorMsg, null);
        }

        return resp;
    }

    @POST
    @Path("completeHcqtChecklist")
    public BaseResponseOBJ completeHcqtChecklist(WoChecklistDTO obj) {
        BaseResponseOBJ resp;
        try {
            woBusiness.completeHcqtChecklist(obj);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }
        return resp;
    }

    @POST
    @Path("declareThdtAddedValue")
    public BaseResponseOBJ declareThdtAddedValue(WoMappingChecklistDTO obj) {
        BaseResponseOBJ resp;
        Long checklistId = obj.getId();
        Double addValue = obj.getAddedQuantityLength();

        if (checklistId == null || addValue == null || addValue <= 0) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
            return resp;
        }

        try {
            boolean result = woBusiness.declareThdtAddedValue(obj);
            if (result) resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
            else resp = new BaseResponseOBJ(ERROR_CODE, obj.getCustomField(), null);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, "Lỗi hệ thống!", null);
        }

        return resp;
    }

    @POST
    @Path("acceptWo")
    public WoDTOResponse acceptWo(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            WoDTO woRequest = request.getWoDTO();
            woRequest.setLoggedInUser("" + request.getSysUserRequest().getAuthenticationInfo().getUsername());
            woBusiness.acceptWo(woRequest);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("getCdLv3List")
    public WoDTOResponse getCdLv3List(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<WoSimpleSysGroupDTO> cdsLv3 = woBusiness.getCdLv3List(request.getWoDTO());
            response.setCdsLv3(cdsLv3);

            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("getCdLv4List")
    public WoDTOResponse getCdLv4List(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<WoSimpleSysGroupDTO> cdsLv4 = woBusiness.getCdLv4List(request.getWoDTO());
            response.setCdsLv4(cdsLv4);

            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("getFtList")
    public WoDTOResponse getFtList(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<WoSimpleFtDTO> ftList = woBusiness.getFtList(request.getWoDTO());
            response.setFtList(ftList);

            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    /**
     * getDataDashboardWoMngt: thong tin dieu hanh wo
     *
     * @param request
     * @return
     */
    @POST
    @Path("getDataDashboardWoMngt")
    public WoDTOResponse getDataDashboardWoMngt(WoDashboardRequest request) {
        WoDTOResponse response = new WoDTOResponse();
        String fromDate = request.getFromDate();
        String toDate = request.getToDate();
        try {
            Assert.hasText(fromDate, "FromDate can't be null or empty");
            Assert.hasText(toDate, "ToDate can't be null or empty");

            // List all Dashboard Information
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            WoDashboardDTO dataDashboardWoMngt = woDashboardBusiness
                    .getDataDashboardWoMngt(request.getSysUserRequest().getSysUserId(), fromDate, toDate);
            response.setDataDashboardWoMngt(dataDashboardWoMngt);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    /**
     * getDataDashboardExecuteWo: thong tin thuc hien wo
     *
     * @param request
     * @return
     */
    @POST
    @Path("getDataDashboardWoExecute")
    public WoDTOResponse getDataDashboardExecuteWo(WoDashboardRequest request) {
        WoDTOResponse response = new WoDTOResponse();
        String fromDate = request.getFromDate();
        String toDate = request.getToDate();
        try {
            Assert.hasText(fromDate, "FromDate can't be null or empty");
            Assert.hasText(toDate, "ToDate can't be null or empty");

            // List all Implementation Information
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            WoDashboardDTO dataDashboardWoExecute = woDashboardBusiness
                    .getDataDashboardExecuteWo(request.getSysUserRequest().getSysUserId(), fromDate, toDate);
            response.setDataDashboardWoExecute(dataDashboardWoExecute);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("detailTotalWoDasboard")
    public WoDTOResponse detailTotalWoDasboard(WoDashboardRequest request) {
        WoDTOResponse response = new WoDTOResponse();
        //Huypq-28072021-start
//      String fromDate = request.getFromDate();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, 1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String fromDate = dateFormat.format(cal.getTime());
        //Huy-end
        String toDate = request.getToDate();
        try {
            Assert.hasText(fromDate, "FromDate can't be null or empty");
            Assert.hasText(toDate, "ToDate can't be null or empty");

            // List all Implementation Information
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<WoDTO> woDTOList = woDashboardBusiness
                    .detailTotalWoDasboard(request, fromDate, toDate, request.getType());
            response.setLstWos(woDTOList);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("approvedWo")
    public WoDTOResponse approvedWo(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
        	
        	WoDTO woDTO = woDAO.getOneDetails(request.getWoDTO().getWoId());
        	if("DONE".equals(request.getWoDTO().getState()) && "TMBT".equalsIgnoreCase(woDTO.getWoTypeCode())) {
        		ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Chưa đính kèm file hợp đồng và BB khảo sát");
                response.setResultInfo(resultInfo);
                return response;
        	}
        	
            SysUserRequest sysUserRequest = request.getSysUserRequest();
            authenticateWsBusiness.validateRequest(sysUserRequest);

            WoDTO woDto = request.getWoDTO();
            boolean checkRoleApproveHshc = woDAO.hasRole(sysUserRequest.getSysUserId(), Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY);
            BaseResponseOBJ resp = woBusiness.approvedWo(woDto, checkRoleApproveHshc, request.getApprovedState());

            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            resultInfo.setMessage(resp.getMessage());
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("acceptChecklistQuantity")
    public WoDTOResponse acceptChecklistQuantity(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            SysUserRequest sysUserRequest = request.getSysUserRequest();
            authenticateWsBusiness.validateRequest(sysUserRequest);

            WoMappingChecklistDTO dto = request.getAcceptChecklistObj();
            woBusiness.acceptChecklistQuantity(dto);

            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("rejectChecklistQuantity")
    public WoDTOResponse rejectChecklistQuantity(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            SysUserRequest sysUserRequest = request.getSysUserRequest();
            authenticateWsBusiness.validateRequest(sysUserRequest);

            WoMappingChecklistDTO dto = request.getAcceptChecklistObj();
            woBusiness.rejectChecklistQuantity(dto);

            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("doSearchWoTaskDaily")
    public WoDTOResponse doSearchWoTaskDaily(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            SysUserRequest sysUserRequest = request.getSysUserRequest();
            authenticateWsBusiness.validateRequest(sysUserRequest);

            WoTaskDailyDTO searchObj = new WoTaskDailyDTO();
            searchObj.setWoMappingChecklistId(request.getAcceptChecklistObj().getChecklistId());
            List<WoTaskDailyBO> lstTaskDaily = woBusiness.doSearchWoTaskDaily(searchObj);
            response.setLstTaskDaily(lstTaskDaily);

            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("listTr")
    public WoDTOResponse listTr(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());

            Long loginUserId = request.getSysUserRequest().getSysUserId();
            String employeeCode = woTrDAO.getSysUser(loginUserId).getEmployeeCode();
            List<DomainDTO> lstDoamins = woDAO.getByAdResource(loginUserId, "VIEW WOXL_TR");
            List<String> lstGroupIds = new ArrayList<>();
            for (DomainDTO iDomain : lstDoamins) {
                lstGroupIds.add("" + iDomain.getDataId());
            }
            List<WoTrDTO> lstWoTrs = woTrBusiness.list(employeeCode, lstGroupIds);

            for (WoTrDTO iTr : lstWoTrs) {
                iTr.setCd(false);
                if (!"ASSIGN_CD".equalsIgnoreCase(iTr.getState()) || lstGroupIds.size() == 0) {
                    continue;
                }
                if (StringUtils.isNotEmpty(iTr.getCdLevel2()) && lstGroupIds.contains(iTr.getCdLevel2())) {
                    iTr.setCd(true);
                } else if (StringUtils.isNotEmpty(iTr.getCdLevel1()) && lstGroupIds.contains(iTr.getCdLevel1())) {
                    iTr.setCd(true);
                }
            }

            response.setLstWoTrs(lstWoTrs);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("changeStateTr")
    public WoDTOResponse changeStateTr(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            WoTrDTO woTrRequest = request.getWoTrDTO();
            woTrRequest.setLoggedInUser("" + request.getSysUserRequest().getAuthenticationInfo().getUsername());

            woTrBusiness.changeStateTr(woTrRequest);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("getListStocks")
    public WoDTOResponse getListStocks(WoDTORequest request) {
        Long loginUserId = request.getSysUserRequest().getSysUserId();

        ListStocksDtoRequest listStocksDtoRequest = new ListStocksDtoRequest();
        List<String> listTypes = new ArrayList<>();

        String partner = request.getWoDTO().getPartner();

        String type = "19";
        String levelStock = "2";
        if ("CMC".equalsIgnoreCase(partner) || "AVG".equalsIgnoreCase(request.getWoDTO().getWoTypeCode())) {
            type = "16";
            levelStock = "4";
        }

        listTypes.add(type);
        listStocksDtoRequest.setListType(listTypes);
        listStocksDtoRequest.setLevelStock(levelStock);
        listStocksDtoRequest.setName(request.getKeysearch());
        if (!"AVG".equalsIgnoreCase(request.getWoDTO().getWoTypeCode())) {
            listStocksDtoRequest.setSysGroupId(woDAO.getUserSysGroupLevel2ByUserId(loginUserId));
        }

        request.setOutsideApiRequest(listStocksDtoRequest);
        WoDTOResponse res = woBusiness.callOutsideApiWraper(request, getListStockUrl);

        res.setLstStocks(res.getLstData());
        return res;
    }

    @POST
    @Path("getListGoods")
    public WoDTOResponse getListMerStocks(WoDTORequest request) {
        List<Long> lstId = new ArrayList<>();
        lstId.add(request.getCatStockId());
        String partner = request.getWoDTO().getPartner();

        String type = "19";
        if ("CMC".equalsIgnoreCase(partner) || "AVG".equalsIgnoreCase(request.getWoDTO().getWoTypeCode())) type = "16";

        WoGetListGoodsDTORequest req = new WoGetListGoodsDTORequest();
        req.setType(type);
        req.setLstId(lstId);
        request.setOutsideApiRequest(req);

        WoDTOResponse res = woBusiness.callOutsideApiWraper(request, getMerStockUrl);
//        res.setLstGoods(res.getLstData());

        List<Object> lstGoods = new ArrayList<>();
        Type typeListMerEntity = new TypeToken<List<VoGoodsDTO>>() {
        }.getType();
        List<VoGoodsDTO> listMerEntity = gson.fromJson(gson.toJson(res.getLstData()), typeListMerEntity);
        for (VoGoodsDTO iGoods : listMerEntity) {
            iGoods.setName(StringUtils.isNotEmpty(iGoods.getCode()) ? iGoods.getCode() + " - " + iGoods.getName() : iGoods.getName());
            if (iGoods.getIsUsed() == null) {
                iGoods.setIsUsed(0l);
            }
            lstGoods.add(iGoods);
        }
        res.setLstGoods(lstGoods);

        return res;
    }

    @POST
    @Path("woSaveImportOrder")
    public WoDTOResponse woSaveImportOrder(WoDTORequest request) {
        return woBusiness.woSaveImportOrder(request, woSaveImportOrderUrl);
    }

    @POST
    @Path("getLstUserRole")
    public WoDTOResponse getLstUserRole(WoDTORequest request) {
        Map<String, String> outsideReq = new HashMap<>();
        String keysearch = request.getKeysearch();
        if ("a".equalsIgnoreCase(request.getKeysearch())) keysearch = "datnv";
        outsideReq.put("name", keysearch);
        request.setOutsideApiRequest(outsideReq);
        WoDTOResponse res = woBusiness.callOutsideApiWraper(request, getLstUserRoleUrl);
        res.setLstSignUsers(res.getLstData());

        return res;
    }

    @POST
    @Path("getRoleByEmail")
    public WoDTOResponse getRoleByEmail(WoDTORequest request) {
        request.setOutsideApiRequest(request.getMapEmails());
        WoDTOResponse res = woBusiness.callOutsideApiWraper(request, getRoleByEmailUrl);
        res.setLstSignUserRoles(res.getLstData());
        return res;
    }

    @POST
    @Path("woSignVoffice")
    public WoDTOResponse woSignVoffice(WoDTORequest request) {
        return woBusiness.woSignVoffice(request, woSignVofficeUrl);
    }

    @POST
    @Path("getListGoodsByWoId")
    public WoDTOResponse getListGoodsByWoId(WoDTORequest request) {
        WoDTOResponse response = woBusiness.getListGoodsByWoId(request);
        try {
        	response.setLstGoodsOffline(response.getLstGoods());
            WoDTO woDTO = woDAO.getOneDetails(request.getWoId());
            if ("DONE".equalsIgnoreCase(woDTO.getState()) && !"CMC".equalsIgnoreCase(woDTO.getPartner())) {
                request.setOutsideApiRequest("{\"woId\": " + request.getWoId() + "}");
                WoDTOResponse res = woBusiness.getOrderGoodsDetail(request, getOrderGoodsDetail);
                response.setLstGoods(res.getLstData());
                response.setResultInfo(res.getResultInfo());
            }
        } catch(Exception e) {
        	e.printStackTrace();
        }
        return response;
    }

    @POST
    @Path("reduceGoods")
    public WoDTOResponse reduceGoods(WoDTORequest request) {
        return woBusiness.woUpdateAmountReal(request);
    }

    @POST
    @Path("getVofficeInfo")
    public WoDTOResponse getVofficeInfo(WoDTORequest request) {
        return woBusiness.checkVofficeInfo(request, checkVofficeInfoUrl);
    }

    @POST
    @Path("saveVofficepassMB")
    public WoDTOResponse saveVofficepassMB(WoDTORequest request) {
        return woBusiness.saveVofficepassMB(request, saveVofficepassMB);
    }

    @POST
    @Path("getListGoodsForWO")
    public WoDTOResponse getListGoodsForWO(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        Gson gson = new Gson();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            Long loginUserId = request.getSysUserRequest().getSysUserId();
            String employeeCode = woTrDAO.getSysUser(loginUserId).getEmployeeCode();
            String apiResult = woBusiness.callOutsideApi("{\"employeeCode\": \"" + employeeCode + "\"}", getListGoodsForWO);
            JSONObject jsonObject = new JSONObject(apiResult);

            Type typeResultInfo = new TypeToken<ResultInfo>() {
            }.getType();
            ResultInfo resultInfo = gson.fromJson(jsonObject.getJSONObject("resultInfo").toString(), typeResultInfo);

            if ("OK".equalsIgnoreCase(resultInfo.getStatus())) {
                Type typeListMerEntity = new TypeToken<List<VoGoodsDTO>>() {
                }.getType();
                List<VoGoodsDTO> listMerEntity = gson.fromJson(jsonObject.getJSONArray("listMerEntity").toString(), typeListMerEntity);
                for (VoGoodsDTO iGoods : listMerEntity) {
                    iGoods.setName(StringUtils.isNotEmpty(iGoods.getSerial()) ? iGoods.getGoodsName() + " - " + iGoods.getSerial() : iGoods.getGoodsName());
                }
                response.setListMerEntity(listMerEntity);
            }
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
        	e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("getListCatStationForTmbt")
    public WoDTOResponse getListCatStationForTmbt(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            // Get catstation for wo TMBT
            if ("TMBT".equalsIgnoreCase(request.getWoDTO().getWoTypeCode())) {
                List<CatStationDTO> lstCatStation = woDAO.getCatStationForTmbt(request.getKeysearch());
                response.setLstCatStation(lstCatStation);
            }
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("updateChecklistTmbt")
    public WoDTOResponse updateChecklistTmbt(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            woBusiness.updateChecklistTmbt(request);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("updateChecklistTthq")
    public WoDTOResponse updateChecklistTthq(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            woBusiness.updateChecklistTthq(request);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("getLstClassDetails")
    public WoDTOResponse getLstClassDetails(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            List<WoAppParamDTO> lstClassDetails = woDAO.getAppParam("CLASS_DETAIL");
            response.setLstClassDetails(lstClassDetails);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("getDefaultClassValue")
    public WoDTOResponse getDefaultClassValue(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            List<WoClassQoutaDTO> lstWoClassQoutaDTOs = woClassQoutaDAO.getByClassIdAndChecklistName(request.getClassId(), request.getChecklistName());
            response.setDefaultClassValue(lstWoClassQoutaDTOs != null && lstWoClassQoutaDTOs.size() > 0 ? lstWoClassQoutaDTOs.get(0).getQoutaValue() : 0l);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("updateChecklistDbht")
    public WoDTOResponse updateChecklistDbht(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            woBusiness.updateChecklistDbht(request);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }

    @POST
    @Path("create-wo")
    @Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
    public WoAvgResponseDTO createWO(String request) {
    	System.out.println("Create WO from AVG");
    	WoAvgResponseDTO response = new WoAvgResponseDTO();
    	Gson gson = new Gson();
        try {
        	WoAvgVhktInputDTO woAvgRequestVhktDTO = gson.fromJson(request, WoAvgVhktInputDTO.class);
            response = woBusiness.createWoAvg(woAvgRequestVhktDTO);
            /*if (id == null) {
                response.setErrorCode("201");
                response.setErrorDesc("Mã hàng hóa đã được giao WO !");
            }*/
        } catch (Exception e) {
            LOGGER.error("Fail to create AVG wo" + e.getMessage(), e);
            response.setErrorDesc("Fail to create AVG work order");
            response.setErrorCode("500");
            response.setStatus(1);
            response.setPath("/coms-service/service/woService/create-wo");
        }
        return response;
    }

    @POST
    @Path("get-wo-info")
    public WoAvgResponseDTO getWO(String request) {
    	System.out.println("get WO info from AVG");
    	WoAvgVhktInputDTO req = gson.fromJson(request, WoAvgVhktInputDTO.class);
        WoAvgResponseDTO response = new WoAvgResponseDTO();
        ResultInfo resultInfo = new ResultInfo();
        try {
            List<GetWoAvgOutputDto> data = woBusiness.getWoAvg(req);
            response.setData(data);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setErrorDesc(e.getMessage());
            response.setErrorCode("500");
            response.setStatus(1);
            response.setPath("coms-service/service/woService/get-wo-info");
        }
        return response;
    }

    @GET
    @Path("get-list-device")
    public WoAvgResponseDTO getAvglistDevice(WoAvgVhktInputDTO request) {
        WoAvgResponseDTO response = new WoAvgResponseDTO();
        ResultInfo resultInfo = new ResultInfo();
        try {
            List<GetWoAvgOutputDto> data = woBusiness.getWoAvg(request);
            response.setData(data);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setErrorDesc(e.getMessage());
            response.setErrorCode("500");
            response.setStatus(1);
            response.setPath("/woService/v1.0/get-wo-info");
        }
        return response;
    }

    @POST
    @Path("updateChecklistAvg")
    public WoDTOResponse updateChecklistAvg(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            boolean updateStatus = false;
            for (WoChecklistDTO woChecklistDTO : request.getLstChecklistsOfWoAvg()) {
                updateStatus = woBusiness.updateChecklistAvg(request, woChecklistDTO);
            }
            if (updateStatus) {
                resultInfo.setStatus(ResultInfo.RESULT_OK);
            } else {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Failed to update AVG order status");
            }
            response.setResultInfo(resultInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;
    }
    
    @POST
    @Path("overdue/acceptRejectOverdueReason")
    public ResultInfo acceptRejectOverdueReason(WoDTO request) {
    	ResultInfo resultInfo = new ResultInfo();
        try {
            if (request.getWoId() == null) {
                resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
                resultInfo.setMessage("Dữ liệu không hợp lê");
            } else {
                woBusinessImpl.acceptRejectOverdueReason(request);
                resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
                resultInfo.setMessage(SUCCESS_MSG);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
            resultInfo.setMessage(e.getMessage());
        }
        return resultInfo;
    }


    @POST
    @Path("/wo/getStationResource")
    public ResultInfo getStationResource(AssetHandoverList assetHandoverList) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            authenticateWsBusiness.validateRequest(assetHandoverList.getSysUserRequest());
            if (ObjectUtils.isEmpty(assetHandoverList.getStationCode())) {
                resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
                resultInfo.setMessage("Dữ liệu không hợp lê");
            } else {
                List<AssetHandoverList> assetHandoverLists = woBusinessImpl.getDataAssetHandoverList(assetHandoverList.getStationCode());
                resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
                resultInfo.setMessage(SUCCESS_MSG);
                resultInfo.setData(assetHandoverLists);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
            resultInfo.setMessage(e.getMessage());

        }
        return resultInfo;
    }

    @POST
    @Path("/wo/rejectWoBgbtsVhkt")
    public ResultInfo rejectWoBgbtsVhkt(WoDTO woDTO) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            authenticateWsBusiness.validateRequest(woDTO.getSysUserRequest());
            if (ObjectUtils.isEmpty(woDTO.getWoId())) {
                resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
                resultInfo.setMessage("Dữ liệu không hợp lê");
            } else {
                WoBO bo = woDAO.getOneRaw(woDTO.getWoId());
            // chuyển trạng thái wo hiện tại
            woBusinessImpl.updateAndWriteLogWo(bo, woDTO.getLoggedInUser(), woDTO);

            // chuyển trạng thái wo DTHT
            woBusinessImpl.updateAndWriteLogWoBtsDHDT(woDTO, bo);
                resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
                resultInfo.setMessage(SUCCESS_MSG);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
            resultInfo.setMessage(e.getMessage());

        }
        return resultInfo;


//        BaseResponseOBJ resp;
//        try {
//            if (ObjectUtils.isEmpty(woDto.getWoId())) {
//                return Response.status(Response.Status.BAD_REQUEST).build();
//            }
//            WoBO bo = woDAO.getOneRaw(woDto.getWoId());
//            // chuyển trạng thái wo hiện tại
//            woBusinessImpl.updateAndWriteLogWo(bo, woDto.getLoggedInUser());
//
//            // chuyển trạng thái wo DTHT
//            woBusinessImpl.updateAndWriteLogWoBtsDHDT(woDto, bo);
//
//            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
//        }
//        return Response.ok(resp).build();
    }
}
