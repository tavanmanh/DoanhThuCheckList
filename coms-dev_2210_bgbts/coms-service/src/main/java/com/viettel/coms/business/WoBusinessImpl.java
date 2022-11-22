package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import com.viettel.coms.bo.*;
import com.viettel.coms.dao.*;
import com.viettel.coms.dto.*;
import com.viettel.erp.bo.CntContractBO;
import com.viettel.wms.dao.AppParamDAO;
import com.viettel.wms.dto.AppParamDTO;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.joda.time.DateTimeComparator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.asset.dto.SysGroupDto;
import com.viettel.coms.dto.avg.AvgGetTokenInputDTO;
import com.viettel.coms.dto.avg.AvgResponse;
import com.viettel.coms.dto.avg.AvgUpdateActiveDeviceRequestInput;
import com.viettel.coms.dto.avg.GetWoAvgOutputDto;
import com.viettel.coms.dto.avg.WoAvgResponseDTO;
import com.viettel.coms.dto.avg.WoAvgVhktInputDTO;
import com.viettel.coms.dto.vttb.VoGoodsDTO;
import com.viettel.coms.utils.BaseResponseOBJ;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.erp.dao.CntContractDAO;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.UConfig;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.utils.StringUtils;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.wms.utils.ValidateUtils;

import static viettel.passport.service.UserUtils.gson;

@Service("woBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WoBusinessImpl implements WoBusiness {
    private final int SUCCESS_CODE = 1;
    private final int ERROR_CODE = -1;
    private final String SUCCESS_MSG = "SUCCESS";
    private final String ERROR_MSG = "ERROR";
    private final String CREATE_NEW_MSG = "CREATED";

    private final String UNASSIGN = "UNASSIGN";
    private final String ASSIGN_CD = "ASSIGN_CD";
    private final String ACCEPT_CD = "ACCEPT_CD";
    private final String REJECT_CD = "REJECT_CD";
    private final String ASSIGN_FT = "ASSIGN_FT";
    private final String ACCEPT_FT = "ACCEPT_FT";
    private final String REJECT_FT = "REJECT_FT";
    private final String PROCESSING = "PROCESSING";
    private final String DONE = "DONE";
    private final String OK = "OK";
    private final String CD_OK = "CD_OK";
    private final String CD_NG = "CD_NG";
    private final String NG = "NG";
    private final String TC_TCT_REJECTED = "TC_TCT_REJECTED";
    private final String OPINION_RQ = "OPINION_RQ";
    private final String OPINION_RQ_1 = "OPINION_RQ_1";
    private final String OPINION_RQ_2 = "OPINION_RQ_2";
    private final String OPINION_RQ_3 = "OPINION_RQ_3";
    private final String OPINION_RQ_4 = "OPINION_RQ_4";
    private final String AP_CONSTRUCTION_TYPE = "AP_CONSTRUCTION_TYPE";
    private final String AP_WORK_SRC = "AP_WORK_SRC";
    private final String WAIT_TC_BRANCH = "WAIT_TC_BRANCH";
    private final String WAIT_TC_TCT = "WAIT_TC_TCT";
    private final String NEW = "NEW";
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
    private final String AVAILABLE_TYPE = "AVAILABLE_TYPE";

    private final String ACCEPTED = "ACCEPTED";
    private final String REJECTED = "REJECTED";
    private final String OPINION_ACCEPTED = "Đã chấp thuận ý kiến.";
    private final String OPINION_REJECTED = "Đã từ chối ý kiến.";
    private final String OPINION_PASS_UP = "Đã chuyển ý kiến lên trên.";
    private final String TR_NAME = "Hợp đồng AIO.";
    private final String WO_TR_XL_STATE = "WO_TR_XL_STATE";
    private final String WO_XL_STATE = "WO_XL_STATE";
    private final String AIO = "AIO";
    private final String BDDK = "BDDK";
    private final String THICONG = "THICONG";
    private final String AVG = "AVG";

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final String SAME_WO = "Hạng mục của công trình đã được tạo WO, nhập lại Hạng mục.";
    private final String AIO_ONLY_1_WO = "TR từ phần mềm AIO chỉ được tạo 1 WO duy nhất.";
    private final String SAME_STATION = "Trạm này đã tồn tại WO thuê trạm tương ứng.";
    private final String STATION_COMPLETE = "Trạm này đã hoàn thành thuê mặt bằng";
    private final String SAME_CONSTRUCTION_SALARY_FUND = "Công trình này đã có WO quỹ lương tương ứng.";
    private final String SAME_CONSTRUCTION_MONEY_INCOME = "Công trình này đã có WO doanh thu tương ứng.";
    private final String SAME_WO_HSHC = "Công trình này đã có WO hồ sơ hoàn công tương ứng.";
    private final String SAME_HCQT_WO = "Công trình này đã có WO hoàn công quyết toán tương ứng.";

    private final long GPON_TYPE = 3;
    private final long TUYEN_TYPE = 2;
    private final String SINGLE_TYPE = "SINGLE TYPE";
    private final String IMPORT_TYPE = "IMPORT TYPE";

    private final String WO_XL_MSG_SUBJECT = "Thông báo công việc Work Order";
    private final String DAILY_QUANTITY_ACCEPTED = "Ghi nhận sản lượng theo ngày";

    private final String UPLOAD_SUB_FOLDER = "input/mobile_images";
    private final String HCQT_PROBLEM = "HCQT_PROBLEM";
    private final String PROBLEM = "PROBLEM";
    private final String HCQT_CHECKLIST = "HCQT_CHECKLIST";
    private final String XLSC_CHECKLIST = "XLSC_CHECKLIST";
    private final String CODIEN_CHECKLIST = "Codien_HTCT_CHECKLIST";

    private final String TTHT_GROUP_ID = "242656";
    private final String VHKT_GROUP_ID = "270120";
    private final int XDDD_AP_WORK_SRC = 6;
    private final long WO_TYPE_5S_ID = 142;
    private final long WO_NAME_5S_ID = 62;

    private final String TTHT_ID = "242656";
    private final String TTVHKT_ID = "270120";
    private final String TTXDDTHT_ID = "166677";
    private final String TTGPTH_ID = "280483";
    private final String TTCNTT_ID = "280501";
    private final String TT_XDDD_ID = "9006003";
    private final String PKD_ID = "275062";
    private final String PDT_ID = "271149";
    private final String PHC_ID = "275062";

    private final String woConfirmAmsAssetReqUrl = UConfig.get("ktts.ams.service")
            + "confirmStockTransServiceRest/woComfirmAmsAssetReq";
    private final String getListGoodsForWO = UConfig.get("ktts.wms.service")
            + "vsmartWorkOrderWebService/service/getListGoodsForWO";
    private final String reduceGoodsInStock = UConfig.get("ktts.wms.service")
            + "vsmartWorkOrderWebService/service/reduceGoodsInStock";
    private String avgTokenStr = null;
    private static final String AVG_ERR_CODE_TOKEN_EXPIRE = "COM.CRM.EXCEPTION.INVALIDTOKENEXCEPTION";
    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    @Value("${avg.checksum.key}")
    private String checkSumKey;

    @Autowired
    WoDAO woDAO;

    @Autowired
    WoTrDAO trDAO;

    @Autowired
    WoMappingAttachDAO woMappingAttachDAO;

    @Autowired
    WoMappingChecklistDAO woMappingChecklistDAO;

    @Autowired
    WoXdddChecklistDAO woXdddChecklistDAO;

    @Autowired
    WoWorkLogsDAO woWorkLogsDAO;

    @Autowired
    WoTypeDAO woTypeDAO;

    @Autowired
    TotalMonthPlanOSDAO totalMonthPlanOSDAO;

    @Autowired
    WoMappingPlanDAO woMappingPlanDAO;

    @Autowired
    ConstructionDAO constructionDAO;

    @Value("${folder_upload2}")
    private String folderUpload;

    @Autowired
    WoTrBusinessImpl woTrBusinessImpl;

    @Autowired
    WoNameDAO woNameDAO;

    @Autowired
    WoTaskDailyDAO woTaskDailyDAO;

    @Autowired
    WoTaskMonthlyDAO woTaskMonthlyDAO;

    @Autowired
    ConstructionBusinessImpl constructionBusinessImpl;

    @Autowired
    WoChecklistDAO woChecklistDAO;

    @Autowired
    WoHcqtProjectDAO woHcqtProjectDAO;
    private String str;

    @Autowired
    WoScheduleWorkItemDAO woScheduleWorkItemDAO;

    @Autowired
    WoScheduleConfigDAO woScheduleConfigDAO;

    @Autowired
    WoFTConfig5SDAO woFTConfig5SDAO;

    @Autowired
    WoConfigContractDAO woConfigContractDAO;

    @Autowired
    WoOverdueReasonDAO woOverdueReasonDAO;

    @Autowired
    AuthenticateWsBusiness authenticateWsBusiness;

    @Autowired
    WoMappingGoodsDAO woMappingGoodsDAO;

    @Autowired
    CntContractDAO cntContractDAO;

    @Autowired
    WoMappingHshcTcDAO woMappingHshcTcDAO;

    @Autowired
    WoMappingStationDAO woMappingStationDAO;

    @Autowired
    WoMappingChecklistDAO woMappingCheckListDAO;

    // Huypq-28062021-start
    @Autowired
    SysGroupDAO sysGroupDAO;
    // Huy-end

    @Autowired
    WoAvgMappingDAO woAvgMappingDAO;

    @Autowired
    CatStationDAO catStationDAO;

    @Autowired
    WoTrMappingStationDAO woTrMappingStationDAO;

    @Autowired
    ManageCertificateDAO manageCertificateDAO;

    @Autowired
    ManageCareerDAO manageCareerDAO;
    // Avg api information
    @Value("${avg.update-status.url}")
    private String avgUpdateStatusUrl;
    @Value("${avg.active-device.url}")
    private String avgActiveDeviceUrl;
    @Value("${avg.get-token.url}")
    private String avgGetTokenUrl;
    @Value("${avg.username}")
    private String avgUsername;
    @Value("${avg.password}")
    private String avgPassword;

    @Autowired
    WoMappingWorkItemHtctDAO woMappingWorkItemHtctDAO;

    @Autowired
    EffectiveCalculationDetailsDAO effectiveCalculationDetailsDAO;

    @Autowired
    WorkItemDAO workItemDAO;

    @Autowired
    AppParamDAO appParamDAO;


    static Logger LOGGER = LoggerFactory.getLogger(WoBusinessImpl.class);

    public String getAvgTokenStr() {
        return avgTokenStr;
    }

    public void setAvgTokenStr(String avgTokenStr) {
        this.avgTokenStr = avgTokenStr;
    }

    @Transactional
    @Override
    public List<WoDTO> list(WoDTORequest request, List<Long> lstWoId, Map<String, Object> filter) {
        long loginUserId = request.getSysUserRequest().getSysUserId();
        List<DomainDTO> lstDoamins = woDAO.getByAdResource(loginUserId, "VIEW WOXL");

        List<String> lstGroupIds = new ArrayList<>();
        for (DomainDTO iDomain : lstDoamins) {
            lstGroupIds.add("" + iDomain.getDataId());
        }

        WoDTO woDTOSearch = new WoDTO();
        woDTOSearch.setLoggedInUser(trDAO.getSysUser(loginUserId).getEmployeeCode());
        woDTOSearch.setFtId(loginUserId);
        woDTOSearch.setMobile(true);
        woDTOSearch.setPage(request.getPage() == null || request.getPage() == 0l ? null : request.getPage());
        woDTOSearch.setPageSize(
                request.getPageSize() == null || request.getPageSize() == 0 ? null : request.getPageSize());
        if (filter != null) {
            if (filter.get("state") != null) {
                woDTOSearch.setState(filter.get("state").toString());
            }
            if (filter.get("apWorkSrc") != null) {
                woDTOSearch.setApWorkSrc(Long.parseLong(filter.get("apWorkSrc").toString()));
            }
            if (filter.get("constructionType") != null) {
                woDTOSearch.setConstructionType(filter.get("constructionType").toString());
            }
        }
        woDTOSearch.setKeySearch(request.getKeysearch());
        List<WoDTO> lst = woDAO.doSearch(woDTOSearch, lstGroupIds, false, false, new ArrayList());
        // Set total record
        // request.getWoDTO().setTotalRecord(woDTOSearch.getTotalRecord());

        return lst;
    }

    @Transactional
    @Override
    public List<WoDTO> doSearch(WoDTO woDto, List<String> groupIdList) throws Exception {
        List<WoDTO> listWoDto = woDAO.doSearch(woDto, groupIdList, false, false, new ArrayList<>());
        return listWoDto;
    }

    @Transactional
    @Override
    public WoDTO getById(Long loginUserId, Long woId) {
        List<Long> lstWoId = new ArrayList<>();
        lstWoId.add(woId);
        WoDTO woDto = woDAO.getOneDetails(woId);
        // if (lst.size() == 0) {
        // return null;
        // }
        // WoDTO woDto = lst.get(0);

        // WO_TYPE HCQT
        // Add information for AVG
        if (woDto.getWoTypeCode() != null && "AVG".equals(woDto.getWoTypeCode())) {
            WoAvgMappingEntityBO avgWoDto = woAvgMappingDAO.getWoAvgInformation(woId);
            try {
                woDto.setOrderCodeTgdd(avgWoDto.getOrderCodeTgdd());
                woDto.setOrderCodeAvg(avgWoDto.getOrderCodeAvg());
                woDto.setCustomerName(avgWoDto.getCustomerName());
                woDto.setPhoneNumber(avgWoDto.getPhoneNumber());
                woDto.setPersonalId(avgWoDto.getPersonalId());
                woDto.setAddress(avgWoDto.getAddress());
                woDto.setProductCode(avgWoDto.getProductCode());
                woDto.setPaymentStatus(avgWoDto.getPaymentStatus());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (woDto.getWoTypeCode() != null && "HCQT".equalsIgnoreCase(woDto.getWoTypeCode())) {
            woDto.setCanFinish(false);
            woDto.setInPlan(true);
            woDto.setRoleForWo(0);
            List<WoChecklistDTO> lstChecklistOfWo = getListChecklistsOfWoHcqt(woDto.getWoId());
            int countWosDone = 0;
            for (WoChecklistDTO i : lstChecklistOfWo) {
                if ("DONE".equalsIgnoreCase(i.getState())) {
                    countWosDone++;
                }
            }
            woDto.setDoneChecklistNumber(countWosDone + "/" + lstChecklistOfWo.size());
        } else if ("AVG".equalsIgnoreCase(woDto.getWoTypeCode())) {
            woDto.setCanFinish(false);
            woDto.setInPlan(false);
            woDto.setRoleForWo(0);
            List<WoChecklistDTO> lstChecklistOfWo = getListChecklistsOfWoAvg(woDto.getWoId());
            int countWosDone = 0;
            for (WoChecklistDTO i : lstChecklistOfWo) {
                if ("DONE".equalsIgnoreCase(i.getState())) {
                    countWosDone++;
                }
            }
            woDto.setDoneChecklistNumber(countWosDone + "/" + lstChecklistOfWo.size());
            if (countWosDone > 0 && countWosDone == lstChecklistOfWo.size()) {
                woDto.setCanFinish(true);
            }
            // Check in plan
            if (woMappingPlanDAO.countPlanOfWo(woDto.getWoId()) > 0) {
                woDto.setInPlan(true);
            }
        } else { // OTHER WO_TYPE
            // Check wo can finish
            woDto.setCanFinish(true);

            List<WoMappingChecklistDTO> lstChecklists = getListChecklistsOfWo(woDto.getWoId(), woDto.getWoTypeCode());
            for (WoMappingChecklistDTO iCl : lstChecklists) {
                // Neu co checklist xddd new hoac reject thi ko duoc dong
                if ("NEW".equalsIgnoreCase(iCl.getState()) || "REJECTED".equalsIgnoreCase(iCl.getState())) {
                    woDto.setCanFinish(false);
                    break;
                }
            }

            // Check in plan
            if (woMappingPlanDAO.countPlanOfWo(woDto.getWoId()) > 0) {
                woDto.setInPlan(true);
            }

            // Checklist done number
            List<WoMappingChecklistDTO> lstChecklistOfWo = getListChecklistsOfWo(woDto.getWoId(), woDto.getWoTypeCode());
            int countWosDone = 0;
            for (WoMappingChecklistDTO i : lstChecklistOfWo) {
                if ("DONE".equalsIgnoreCase(i.getState())) {
                    countWosDone++;
                }
            }
            woDto.setDoneChecklistNumber(countWosDone + "/" + lstChecklistOfWo.size());

            // Remain amount
            if (woDto.getConstructionId() != null) {
                long constructionId = woDto.getConstructionId();
                double amount = woDAO.getAmmountOfConstruction(constructionId);
                double currentAmount = woDAO.getCurrentAmount(constructionId);
                woDto.setRemainAmount(amount - currentAmount);
            }

            if ("THDT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                Double approvedCollected = woTaskMonthlyDAO.getApprovedCollected(woId);
                Double unapprovedCollected = woTaskMonthlyDAO.getUnapprovedCollected(woId);
                woDto.setApprovedCollected(approvedCollected);
                woDto.setUnapprovedCollected(unapprovedCollected);
            }
        }

        // Check role for wo
        List<String> lstRolesOfWo = new ArrayList<>();
        // Get domain list
        List<DomainDTO> lstDoamins = woDAO.getByAdResource(loginUserId, "CD WOXL");
        List<String> lstGroupIds = new ArrayList<>();
        for (DomainDTO iDomain : lstDoamins) {
            lstGroupIds.add("" + iDomain.getDataId());
        }

        if (woDto.getCdLevel5() != null && woDto.getCdLevel5().equalsIgnoreCase("" + loginUserId)) {
            lstRolesOfWo.add("5");
        } else if (woDto.getCdLevel1() != null && lstGroupIds.contains("" + woDto.getCdLevel1())) {
            lstRolesOfWo.add("1");
        }

        if (woDto.getCdLevel4() != null && lstGroupIds.contains("" + woDto.getCdLevel4())) {
            lstRolesOfWo.add("4");
        }

        if (woDto.getCdLevel3() != null && lstGroupIds.contains("" + woDto.getCdLevel3())) {
            lstRolesOfWo.add("3");
        }

        if (woDto.getCdLevel2() != null && lstGroupIds.contains("" + woDto.getCdLevel2())) {
            lstRolesOfWo.add("2");
        }
        woDto.setLstRolesOfWo(lstRolesOfWo);

        // boolean isCd = woDAO.isCd(loginUserId);
        // if (woDto.getFtId() == null || 0L == woDto.getFtId()) { // Chua giao FT -->
        // CD
        // if (isCd) {
        // woDto.setRoleForWo(2);
        // }
        // } else { // Da giao FT
        // if (loginUserId.equals(woDto.getFtId())) { // User login chinh la FT
        // woDto.setRoleForWo(1); // Quyen FT
        // if (isCd) { // Co them quyen CD --> Co 2 quyen
        // woDto.setRoleForWo(0);
        // }
        // } else { // User login ko phai FT
        // if (isCd) { // Co quyen CD --> CD
        // woDto.setRoleForWo(2); // chua giao FT -> CD co quyen
        // }
        // }
        // }

        return woDto;
    }

    @Override
    public void update(WoDTO dto, String content, SysUserRequest sysU) throws Exception {
        Gson gson = new Gson();
        if (dto != null) {
            WoBO incomingBo = dto.toModel();
            WoTypeBO woTypeBO = woTypeDAO.getOneRaw(incomingBo.getWoTypeId());
            // Update WO
            if ("PROCESSING".equalsIgnoreCase(dto.getState())) {
                if (dto.getConstructionId() != null && "THICONG".equalsIgnoreCase(woTypeBO.getWoTypeCode())) {
                    String consStatus = woDAO.getStatusConstructionById(dto.getConstructionId());
                    if (consStatus != null && Long.parseLong(consStatus) < 5) {
                        woDAO.updateStartConstruction(dto.getConstructionId());
                        if (dto.getCatWorkItemTypeId() != null) {
                            woDAO.updateStartWorkItiem(dto.getConstructionId(), dto.getCatWorkItemTypeId());
                        }
                    }
                }
            }

            if (CD_OK.equalsIgnoreCase(dto.getState())) {
                String employeeCode = dto.getLoggedInUser();
                Long sysUserId = sysU.getSysUserId();
                incomingBo.setUserCdApproveWo(employeeCode);
                incomingBo.setUpdateCdApproveWo(new Date());
                if ("THICONG".equalsIgnoreCase(woTypeBO.getWoTypeCode())) {
                    incomingBo.setApproveDateReportWo(new Date());

                    // duyệt hết sản lượng theo ngày nếu là công trình tuyến/GPON
                    if (incomingBo.getCatConstructionTypeId() == 2 || incomingBo.getCatConstructionTypeId() == 3) {
                        confirmAllQuantityByDate(incomingBo.getWoId(), sysUserId, employeeCode);
                    }

                    // chuyển trạng thái work item = 3
                    woDAO.tryCompleteWorkItem(incomingBo);

                    // chuyển trạng thái công trình thành đã thi công nếu tất cả các hạng mục của
                    // công trình đã xong
                    tryCompleteConstruction(incomingBo.getConstructionId());

                    // Lay thong tin hop dong
                    Long woId = incomingBo.getWoId();
                    WoBO bo = woDAO.getOneRaw(woId);
                    CntContractDTO cntContractDTO = cntContractDAO.getCntContractByCode(bo.getContractCode());
                    // Tao wo HSHC khi dong wo thi cong (nguon dau tu: CONTRACT_TYPE_O = 1)
                    // Lay danh sach tat ca wo thi cong thuoc nha tram (cua wo hien tai)
                    WoDTO newDto = bo.toDTO();
                    newDto.setWoTypeCode(woTypeBO.getWoTypeCode());

                    // Nguon dau tu thi phai check cac wo thuoc ma nha tram da duoc duyet OK
                    if (cntContractDTO != null && cntContractDTO.getContractTypeO() == 1) {
                        List<WoDTO> lstWoOfStationHouses = woDAO.getLstWoOfStationHouses(woId);
                        boolean checkOkAll = lstWoOfStationHouses.size() > 0 ? true : false;
                        List<WoDTO> lstWoTcs = new ArrayList<>();
                        for (WoDTO iWo : lstWoOfStationHouses) {
                            lstWoTcs.add(iWo);
                            if (!"OK".equalsIgnoreCase(iWo.getState()) && !"CD_OK".equalsIgnoreCase(iWo.getState())
                                    && !iWo.getWoId().equals(bo.getWoId())) {
                                checkOkAll = false;
                                break;
                            }
                        }
                        if (checkOkAll) {
                            tryCreateHshc(newDto, lstWoTcs);
                        }
                    } else { // Con lai thi tao HSHC luon
                        tryCreateHshc(newDto, null);
                    }
                }

                // duyệt hết sản lượng theo tháng nếu là wo thu hồi dòng tiền
                if ("THDT".equalsIgnoreCase(dto.getWoTypeCode())) {
                    confirmAllQuantityByMonth(incomingBo.getWoId(), sysUserId, employeeCode);
                }

            }

            WoBO bo = woDAO.getOneRaw(incomingBo.getWoId());

            // hủy hiệu lực sản lượng đã ghi nhận (theo ngày / tháng)
            if (NG.equalsIgnoreCase(incomingBo.getState()) || CD_NG.equalsIgnoreCase(incomingBo.getState())) {
                tryRejectAllRelatedQuantity(bo.getWoId());
                if ("THICONG".equalsIgnoreCase(dto.getWoTypeCode()) && incomingBo.getCatConstructionTypeId() != null
                        && incomingBo.getCatConstructionTypeId() == 8) {
                    incomingBo.setMoneyValue(0d);
                }
            }

            // hủy trạng thái đã thực hiện của công trình và hạng mục nếu CD lv 1 duyệt NG
            if (NG.equalsIgnoreCase(incomingBo.getState()) && "THICONG".equalsIgnoreCase(dto.getWoTypeCode())) {
                woDAO.tryUncompleteWorkItem(incomingBo);
                woDAO.tryUncompleteConstruction(incomingBo.getConstructionId());
            }

            // nếu 5S thì OK luôn
            // if (DONE.equalsIgnoreCase(incomingBo.getState()) && incomingBo.getWoTypeId()
            // == 241) {
            // incomingBo.setState(OK);
            // }

            // Neu FT tu choi thi cap nhat ve null
            if (REJECT_FT.equalsIgnoreCase(incomingBo.getState())) {
                bo.setFtId(null);
                bo.setFtName(null);
                bo.setFtEmail(null);
            }

            tranferBoData(incomingBo, bo);

            woDAO.update(bo);

            // FT tiep nhan wo thi cong (nguon viec XDDD_AP_WORK_SRC = 6, va loai cong trinh
            // = 8) thi sinh WO5S
            if ("THICONG".equalsIgnoreCase(woTypeBO.getWoTypeCode())) {
                if (ACCEPT_FT.equalsIgnoreCase(bo.getState()) && XDDD_AP_WORK_SRC == bo.getApWorkSrc()
                        && bo.getCatConstructionTypeId() != null && bo.getCatConstructionTypeId() == 8) {
                    tryCreate5SForXDDD(bo, new Date());
                }
            }

            // Update TR
            if (dto.getTrId() != null) {
                // WoTrDTO woTrDTO = trDAO.getOneDetails(dto.getTrId());
                // if (woTrDTO != null && !woTrDTO.getState().contains("OPINION_RQ")) {
                // if ("PROCESSING".equalsIgnoreCase(dto.getState())) {
                // woTrDTO.setState("PROCESSING");
                // } else if (dto.getState().contains("OPINION_RQ")) {
                // woTrDTO.setState("OPINION_RQ");
                // }
                // trDAO.update(woTrDTO.toModel());
                // }
                WoTrBO trBO = trDAO.getOneRaw(dto.getTrId());
                if (trBO != null && !trBO.getState().contains("OPINION_RQ")) {
                    if ("PROCESSING".equalsIgnoreCase(dto.getState())) {
                        trBO.setState("PROCESSING");
                    } else if (dto.getState().contains("OPINION_RQ")) {
                        trBO.setState("OPINION_RQ");
                    }
                    trDAO.update(trBO);
                }
            }
            if (Constant.WO_TYPE_CODE.BGBTS_VHKT.equalsIgnoreCase(woTypeBO.getWoTypeCode())
                    && Constant.WO_STATE.ASSIGN_FT.equals(bo.getState())) {
                String loggedInUser = dto.getLoggedInUser();
                bo.setState(CD_OK);
                bo.setAcceptTime(new Date());
                bo.setUserFtReceiveWo(loggedInUser);
                bo.setUpdateFtReceiveWo(new Date());
                bo.setUserCdApproveWo(loggedInUser);
                bo.setUpdateCdApproveWo(new Date());
                bo.setBgbtsResult(null);
                woDAO.update(bo);
                content = woDAO.getNameAppParam(ACCEPT_CD, WO_XL_STATE);
                logWoWorkLogs(dto, "1", content, gson.toJson(bo), loggedInUser);
            } else {
                // Worklogs
                String logContent = "Trạng thái WO: " + woDAO.getNameAppParam(bo.getState(), WO_XL_STATE);
                if (!StringUtils.isNullOrEmpty(content)) {
                    logContent += "\n " + content;
                }
                logWoWorkLogs(dto, "1", logContent, gson.toJson(bo), dto.getLoggedInUser());
            }
        }
    }

    private WoBO tranferBoData(WoBO incomingBo, WoBO dbBo) {

        if (!StringUtils.isNullOrEmpty(incomingBo.getWoCode()))
            dbBo.setWoCode(incomingBo.getWoCode());
        if (!StringUtils.isNullOrEmpty(incomingBo.getWoName()))
            dbBo.setWoName(incomingBo.getWoName());
        if (incomingBo.getWoTypeId() != null)
            dbBo.setWoTypeId(incomingBo.getWoTypeId());
        if (incomingBo.getTrId() != null)
            dbBo.setTrId(incomingBo.getTrId());
        if (!StringUtils.isNullOrEmpty(incomingBo.getState()))
            dbBo.setState(incomingBo.getState());
        if (incomingBo.getConstructionId() != null)
            dbBo.setConstructionId(incomingBo.getConstructionId());
        if (incomingBo.getCatWorkItemTypeId() != null)
            dbBo.setCatWorkItemTypeId(incomingBo.getCatWorkItemTypeId());
        if (!StringUtils.isNullOrEmpty(incomingBo.getStationCode()))
            dbBo.setStationCode(incomingBo.getStationCode());
        if (!StringUtils.isNullOrEmpty(incomingBo.getUserCreated()))
            dbBo.setUserCreated(incomingBo.getUserCreated());
        if (incomingBo.getCreatedDate() != null)
            dbBo.setCreatedDate(incomingBo.getCreatedDate());
        if (incomingBo.getFinishDate() != null)
            dbBo.setFinishDate(incomingBo.getFinishDate());
        if (incomingBo.getQoutaTime() != null)
            dbBo.setQoutaTime(incomingBo.getQoutaTime());
        if (incomingBo.getAcceptTime() != null)
            dbBo.setAcceptTime(incomingBo.getAcceptTime());
        if (incomingBo.getStartTime() != null)
            dbBo.setStartTime(incomingBo.getStartTime());
        if (incomingBo.getEndTime() != null)
            dbBo.setEndTime(incomingBo.getEndTime());
        if (!StringUtils.isNullOrEmpty(incomingBo.getExecuteMethod()))
            dbBo.setExecuteMethod(incomingBo.getExecuteMethod());
        if (!StringUtils.isNullOrEmpty(incomingBo.getQuantityValue()))
            dbBo.setQuantityValue(incomingBo.getQuantityValue());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCdLevel1()))
            dbBo.setCdLevel1(incomingBo.getCdLevel1());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCdLevel2()))
            dbBo.setCdLevel2(incomingBo.getCdLevel2());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCdLevel3()))
            dbBo.setCdLevel3(incomingBo.getCdLevel3());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCdLevel4()))
            dbBo.setCdLevel4(incomingBo.getCdLevel4());
        if (incomingBo.getFtId() != null)
            dbBo.setFtId(incomingBo.getFtId());
        if (!StringUtils.isNullOrEmpty(incomingBo.getExecuteLat()))
            dbBo.setExecuteLat(incomingBo.getExecuteLat());
        if (!StringUtils.isNullOrEmpty(incomingBo.getExecuteLong()))
            dbBo.setExecuteLong(incomingBo.getExecuteLong());
        if (incomingBo.getStatus() != null)
            dbBo.setStatus(incomingBo.getStatus());
        if (incomingBo.getTotalMonthPlanId() != null)
            dbBo.setTotalMonthPlanId(incomingBo.getTotalMonthPlanId());
        if (incomingBo.getMoneyValue() != null)
            dbBo.setMoneyValue(incomingBo.getMoneyValue());
        if (!StringUtils.isNullOrEmpty(incomingBo.getMoneyFlowBill()))
            dbBo.setMoneyFlowBill(incomingBo.getMoneyFlowBill());
        if (incomingBo.getMoneyFlowDate() != null)
            dbBo.setMoneyFlowDate(incomingBo.getMoneyFlowDate());
        if (incomingBo.getMoneyFlowValue() != null)
            dbBo.setMoneyFlowValue(incomingBo.getMoneyFlowValue());
        if (incomingBo.getMoneyFlowRequired() != null)
            dbBo.setMoneyFlowRequired(incomingBo.getMoneyFlowRequired());
        if (!StringUtils.isNullOrEmpty(incomingBo.getMoneyFlowContent()))
            dbBo.setMoneyFlowContent(incomingBo.getMoneyFlowContent());
        if (incomingBo.getApConstructionType() != null)
            dbBo.setApConstructionType(incomingBo.getApConstructionType());
        if (incomingBo.getApWorkSrc() != null)
            dbBo.setApWorkSrc(incomingBo.getApWorkSrc());
        if (!StringUtils.isNullOrEmpty(incomingBo.getOpinionResult()))
            dbBo.setOpinionResult(incomingBo.getOpinionResult());
        if (incomingBo.getContractId() != null)
            dbBo.setContractId(incomingBo.getContractId());
        if (incomingBo.getExecuteChecklist() != null)
            dbBo.setExecuteChecklist(incomingBo.getExecuteChecklist());
        if (incomingBo.getWoNameId() != null)
            dbBo.setWoNameId(incomingBo.getWoNameId());
        if (incomingBo.getQuantityByDate() != null)
            dbBo.setQuantityByDate(incomingBo.getQuantityByDate());
        if (incomingBo.getClosedTime() != null)
            dbBo.setClosedTime(incomingBo.getClosedTime());
        if (!StringUtils.isNullOrEmpty(incomingBo.getConstructionCode()))
            dbBo.setConstructionCode(incomingBo.getConstructionCode());
        if (!StringUtils.isNullOrEmpty(incomingBo.getContractCode()))
            dbBo.setContractCode(incomingBo.getContractCode());
        if (incomingBo.getProjectId() != null)
            dbBo.setProjectId(incomingBo.getProjectId());
        if (!StringUtils.isNullOrEmpty(incomingBo.getProjectCode()))
            dbBo.setProjectCode(incomingBo.getProjectCode());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCdLevel1Name()))
            dbBo.setCdLevel1Name(incomingBo.getCdLevel1Name());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCdLevel2Name()))
            dbBo.setCdLevel2Name(incomingBo.getCdLevel2Name());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCdLevel3Name()))
            dbBo.setCdLevel3Name(incomingBo.getCdLevel3Name());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCdLevel4Name()))
            dbBo.setCdLevel4Name(incomingBo.getCdLevel4Name());
        if (!StringUtils.isNullOrEmpty(incomingBo.getFtName()))
            dbBo.setFtName(incomingBo.getFtName());
        if (!StringUtils.isNullOrEmpty(incomingBo.getFtEmail()))
            dbBo.setFtEmail(incomingBo.getFtEmail());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCreatedUserFullName()))
            dbBo.setCreatedUserFullName(incomingBo.getCreatedUserFullName());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCreatedUserEmail()))
            dbBo.setCreatedUserEmail(incomingBo.getCreatedUserEmail());
        if (!StringUtils.isNullOrEmpty(incomingBo.getTrCode()))
            dbBo.setTrCode(incomingBo.getTrCode());
        if (incomingBo.getCatConstructionTypeId() != null)
            dbBo.setCatConstructionTypeId(incomingBo.getCatConstructionTypeId());
        if (incomingBo.getChecklistStep() != null)
            dbBo.setChecklistStep(incomingBo.getChecklistStep());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCatProvinceCode()))
            dbBo.setCatProvinceCode(incomingBo.getCatProvinceCode());
        if (!StringUtils.isNullOrEmpty(incomingBo.getUserCdLevel2ReceiveWo()))
            dbBo.setUserCdLevel2ReceiveWo(incomingBo.getUserCdLevel2ReceiveWo());
        if (incomingBo.getUpdateCdLevel2ReceiveWo() != null)
            dbBo.setUpdateCdLevel2ReceiveWo(incomingBo.getUpdateCdLevel2ReceiveWo());
        if (!StringUtils.isNullOrEmpty(incomingBo.getUserCdLevel3ReceiveWo()))
            dbBo.setUserCdLevel3ReceiveWo(incomingBo.getUserCdLevel3ReceiveWo());
        if (incomingBo.getUpdateCdLevel3ReceiveWo() != null)
            dbBo.setUpdateCdLevel3ReceiveWo(incomingBo.getUpdateCdLevel3ReceiveWo());
        if (!StringUtils.isNullOrEmpty(incomingBo.getUserCdLevel4ReceiveWo()))
            dbBo.setUserCdLevel4ReceiveWo(incomingBo.getUserCdLevel4ReceiveWo());
        if (incomingBo.getUpdateCdLevel4ReceiveWo() != null)
            dbBo.setUpdateCdLevel4ReceiveWo(incomingBo.getUpdateCdLevel4ReceiveWo());
        if (!StringUtils.isNullOrEmpty(incomingBo.getUserFtReceiveWo()))
            dbBo.setUserFtReceiveWo(incomingBo.getUserFtReceiveWo());
        if (incomingBo.getUpdateFtReceiveWo() != null)
            dbBo.setUpdateFtReceiveWo(incomingBo.getUpdateFtReceiveWo());
        if (!StringUtils.isNullOrEmpty(incomingBo.getUserCdApproveWo()))
            dbBo.setUserCdApproveWo(incomingBo.getUserCdApproveWo());
        if (incomingBo.getUpdateCdApproveWo() != null)
            dbBo.setUpdateCdApproveWo(incomingBo.getUpdateCdApproveWo());
        if (!StringUtils.isNullOrEmpty(incomingBo.getUserTthtApproveWo()))
            dbBo.setUserTthtApproveWo(incomingBo.getUserTthtApproveWo());
        if (incomingBo.getUpdateTthtApproveWo() != null)
            dbBo.setUpdateTthtApproveWo(incomingBo.getUpdateTthtApproveWo());
        if (incomingBo.getApproveDateReportWo() != null)
            dbBo.setApproveDateReportWo(incomingBo.getApproveDateReportWo());
        if (incomingBo.getHcqtProjectId() != null)
            dbBo.setHcqtProjectId(incomingBo.getHcqtProjectId());
        if (incomingBo.getHshcReceiveDate() != null)
            dbBo.setHshcReceiveDate(incomingBo.getHshcReceiveDate());
        if (!StringUtils.isNullOrEmpty(incomingBo.getType()))
            dbBo.setType(incomingBo.getType());
        if (!StringUtils.isNullOrEmpty(incomingBo.getHcqtContractCode()))
            dbBo.setHcqtContractCode(incomingBo.getHcqtContractCode());
        if (!StringUtils.isNullOrEmpty(incomingBo.getCnkv()))
            dbBo.setCnkv(incomingBo.getCnkv());
        //Huypq-02112021-start
        if (!StringUtils.isNullOrEmpty(incomingBo.getOpinionType())) {
            dbBo.setOpinionType(incomingBo.getOpinionType());
        }
        //Huy-end
        return dbBo;
    }

    public DataListDTO doSearchReport(ReportWoDTO obj) {
        List<ReportWoDTO> ls = woDAO.doSearchReport(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public String exportFile(ReportWoDTO obj) throws Exception {
        obj.setPage(null);
        obj.setPageSize(null);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_BaoCao_Wo_NgoaiOS.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Export_BaoCao_Wo_NgoaiOS.xlsx");
        List<RpWoDetailOsDTO> data = woDAO.getDataExportFile(obj);

        List<ReportWoDTO> datatonghop = woDAO.doSearchReport(obj);

        XSSFSheet sheet0 = workbook.getSheetAt(0);
        if (datatonghop != null && !datatonghop.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet0);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet0);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet0);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));
            int i = 2;
            for (ReportWoDTO dto : datatonghop) {
                Row row = sheet0.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getMonth() != null) ? dto.getMonth() : 0l);
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getYear() != null) ? dto.getYear() : 0l);
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getName() != null) ? dto.getName() : "");
                cell.setCellStyle(style);
                // Sản lượng
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getQuantityMonth() != null) ? dto.getQuantityMonth() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getQuantityWo() != null) ? dto.getQuantityWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getQuantityPercent() != null) ? dto.getQuantityPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // Quỹ lương
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getSalaryMonth() != null) ? dto.getSalaryMonth() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getSalaryWo() != null) ? dto.getSalaryWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getSalaryPercent() != null) ? dto.getSalaryPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // HSHC hoanm1_20200828_start
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getFileMonth() != null) ? dto.getFileMonth() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getProvinceTH() != null) ? dto.getProvinceTH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getProvinceTHPercent() != null) ? dto.getProvinceTHPercent() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getFileWo() != null) ? dto.getFileWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue((dto.getFilePercent() != null) ? dto.getFilePercent() : 0d);
                cell.setCellStyle(styleNumber);
                // hoanm1_20200828_end
                // Dòng tiền
                cell = row.createCell(15, CellType.STRING);
                cell.setCellValue((dto.getMoneyMonth() != null) ? dto.getMoneyMonth() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(16, CellType.STRING);
                cell.setCellValue((dto.getMoneyWo() != null) ? dto.getMoneyWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(17, CellType.STRING);
                cell.setCellValue((dto.getMoneyPercent() != null) ? dto.getMoneyPercent() : 0d);
                cell.setCellStyle(styleNumber);
            }
        }
        XSSFSheet sheet1 = workbook.getSheetAt(1);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet1);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet1);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet1);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            int i = 2;
            for (RpWoDetailOsDTO dto : data) {
                Row row = sheet1.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getMonth() != null) ? dto.getMonth() + "/" + dto.getYear() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getName() != null) ? dto.getName() : "");
                cell.setCellStyle(style);
                // Quỹ lương
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getSalaryMonth() != null) ? dto.getSalaryMonth() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getSalaryWo() != null) ? dto.getSalaryWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getSalaryPercent() != null) ? dto.getSalaryPercent() : 0d);
                cell.setCellStyle(styleNumber);

                // HSHC xây lắp
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getHshcXayLapKH() != null) ? dto.getHshcXayLapKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getHshcXayLapWo() != null) ? dto.getHshcXayLapWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getHshcXayLapPercent() != null) ? dto.getHshcXayLapPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // Doanh thu nguồn CP
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getQtKH() != null) ? dto.getQtKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getQtWo() != null) ? dto.getQtWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getQtPercent() != null) ? dto.getQtPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // DOANH THU NTĐ (GPDN)
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getQtGPDNKH() != null) ? dto.getQtGPDNKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getQtGPDNWo() != null) ? dto.getQtGPDNWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue((dto.getQtGPDNPercent() != null) ? dto.getQtGPDNPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // DOANH THU NTĐ (XDDD)
                cell = row.createCell(15, CellType.STRING);
                cell.setCellValue((dto.getQtXDDDKH() != null) ? dto.getQtXDDDKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(16, CellType.STRING);
                cell.setCellValue((dto.getQtXDDDWo() != null) ? dto.getQtXDDDWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(17, CellType.STRING);
                cell.setCellValue((dto.getQtXDDDPercent() != null) ? dto.getQtXDDDPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // HSHC HẠ TẦNG CHO THUÊ
                cell = row.createCell(18, CellType.STRING);
                cell.setCellValue((dto.getQtHTCTKH() != null) ? dto.getQtHTCTKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(19, CellType.STRING);
                cell.setCellValue((dto.getQtHTCTWo() != null) ? dto.getQtHTCTWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(20, CellType.STRING);
                cell.setCellValue((dto.getQtHTCTPercent() != null) ? dto.getQtHTCTPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // SẢN LƯỢNG XÂY LẮP
                cell = row.createCell(21, CellType.STRING);
                cell.setCellValue((dto.getQtSLXLKH() != null) ? dto.getQtSLXLKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(22, CellType.STRING);
                cell.setCellValue((dto.getQtSLXLWo() != null) ? dto.getQtSLXLWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(23, CellType.STRING);
                cell.setCellValue((dto.getQtSLXLPercent() != null) ? dto.getQtSLXLPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // SẢN LƯỢNG Ngoài OS
                cell = row.createCell(24, CellType.STRING);
                cell.setCellValue((dto.getSlKH() != null) ? dto.getSlKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(25, CellType.STRING);
                cell.setCellValue((dto.getSlWo() != null) ? dto.getSlWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(26, CellType.STRING);
                cell.setCellValue((dto.getSlPercent() != null) ? dto.getSlPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // SẢN LƯỢNG NTĐ (GPDN)
                cell = row.createCell(27, CellType.STRING);
                cell.setCellValue((dto.getSlGPDNKH() != null) ? dto.getSlGPDNKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(28, CellType.STRING);
                cell.setCellValue((dto.getSlGPDNWo() != null) ? dto.getSlGPDNWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(29, CellType.STRING);
                cell.setCellValue((dto.getSlGPDNPercent() != null) ? dto.getSlGPDNPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // SẢN LƯỢNG NTĐ (XDDD)
                cell = row.createCell(30, CellType.STRING);
                cell.setCellValue((dto.getSlXDDDKH() != null) ? dto.getSlXDDDKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(31, CellType.STRING);
                cell.setCellValue((dto.getSlXDDDWo() != null) ? dto.getSlXDDDWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(32, CellType.STRING);
                cell.setCellValue((dto.getSlXDDDPercent() != null) ? dto.getSlXDDDPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // Tìm kiếm việc XDDD
                cell = row.createCell(33, CellType.STRING);
                cell.setCellValue((dto.getTkvKH() != null) ? dto.getTkvKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(34, CellType.STRING);
                cell.setCellValue((dto.getTkvWo() != null) ? dto.getTkvWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(35, CellType.STRING);
                cell.setCellValue((dto.getTkvPercent() != null) ? dto.getTkvPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // Thu hồi dòng tiền
                cell = row.createCell(36, CellType.STRING);
                cell.setCellValue((dto.getThdtKH() != null) ? dto.getThdtKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(37, CellType.STRING);
                cell.setCellValue((dto.getThdtWo() != null) ? dto.getThdtWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(38, CellType.STRING);
                cell.setCellValue((dto.getThdtPercent() != null) ? dto.getThdtPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // Tổng triển khai trạm
                cell = row.createCell(39, CellType.STRING);
                cell.setCellValue((dto.getTtkKH() != null) ? dto.getTtkKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(40, CellType.STRING);
                cell.setCellValue((dto.getTtkWo() != null) ? dto.getTtkWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(41, CellType.STRING);
                cell.setCellValue((dto.getTtkPercent() != null) ? dto.getTtkPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // Xong móng
                cell = row.createCell(42, CellType.STRING);
                cell.setCellValue((dto.getXdXMKH() != null) ? dto.getXdXMKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(43, CellType.STRING);
                cell.setCellValue((dto.getXdXMWo() != null) ? dto.getXdXMWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(44, CellType.STRING);
                cell.setCellValue((dto.getXdXMPercent() != null) ? dto.getXdXMPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // Xong ĐB
                cell = row.createCell(45, CellType.STRING);
                cell.setCellValue((dto.getxDBKH() != null) ? dto.getxDBKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(46, CellType.STRING);
                cell.setCellValue((dto.getxDBWo() != null) ? dto.getxDBWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(47, CellType.STRING);
                cell.setCellValue((dto.getxDBPercent() != null) ? dto.getxDBPercent() : 0d);
                cell.setCellStyle(styleNumber);
                // Thuê trạm
                cell = row.createCell(48, CellType.STRING);
                cell.setCellValue((dto.getTmbKH() != null) ? dto.getTmbKH() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(49, CellType.STRING);
                cell.setCellValue((dto.getTmbWo() != null) ? dto.getTmbWo() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(50, CellType.STRING);
                cell.setCellValue((dto.getTmbPercent() != null) ? dto.getTmbPercent() : 0d);
                cell.setCellStyle(styleNumber);

            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Export_BaoCao_Wo_NgoaiOS.xlsx");
        return path;
    }

    @Override
    public List<WoMappingChecklistDTO> getListChecklistsOfWo(Long woId, String woTypeCode) {
        WoBO bo = woDAO.getOneRaw(woId);
        boolean isAIO = woDAO.checkWoIsAIO(bo.getWoTypeId());
        boolean isCVDK = woDAO.checkWoIsCVDK(bo.getWoTypeId()); // wo định kỳ
        WoDTO dto = bo.toDTO();
        if (!StringUtils.isNullOrEmpty(woTypeCode))
            dto.setWoTypeCode(woTypeCode);
        if (isAIO)
            dto.setWoTypeCode(AIO);
        if (isCVDK)
            dto.setWoTypeCode(BDDK);

        List<WoMappingChecklistDTO> lstData = getCheckListOfWo(dto);
        // 20200903_hoanm1_comment
        // for (WoMappingChecklistDTO iData : lstData) {
        // // Get list file attach
        // List<ImgChecklistDTO> lstImgs = getImageList(iData.getWoId(),
        // iData.getChecklistId());
        // iData.setLstImgs(lstImgs);
        // }
        // 20200903_hoanm1_comment
        return lstData;
    }

    @Override
    public List<WoMappingChecklistDTO> getListChecklistsOfWoForWeb(Long woId, String woTypeCode) {
        WoBO bo = woDAO.getOneRaw(woId);
        WoDTO dto = bo.toDTO();
        dto.setWoTypeCode(woTypeCode);

        // boolean isAIO = woDAO.checkWoIsAIO(bo.getWoTypeId());
        // boolean isCVDK = woDAO.checkWoIsCVDK(bo.getWoTypeId()); //wo định kỳ
        // if (isAIO) dto.setWoTypeCode(AIO);
        // if (isCVDK) dto.setWoTypeCode(BDDK);

        List<WoMappingChecklistDTO> lstData = getCheckListOfWo(dto);
        for (WoMappingChecklistDTO iData : lstData) {
            // Get list file attach
            Long checklistId = iData.getChecklistId();
            if (bo.getCatConstructionTypeId() != null) {
                if (bo.getCatConstructionTypeId() != null && bo.getCatConstructionTypeId() == 8
                        && "THICONG".equalsIgnoreCase(woTypeCode))
                    checklistId = iData.getId();
            }
            List<ImgChecklistDTO> lstImgs = getImageList(iData.getWoId(), checklistId);
            iData.setLstImgs(lstImgs);
        }
        return lstData;
    }

    private List<ImgChecklistDTO> getImageList(Long woId, Long checklistId) {
        WoMappingAttachDTO searchObj = new WoMappingAttachDTO();
        searchObj.setWoId(woId);
        searchObj.setChecklistId(checklistId);
        searchObj.setPage(1l);
        searchObj.setPageSize(9999);
        List<WoMappingAttachDTO> lstAttachs = woMappingAttachDAO.doSearch(searchObj);
        List<ImgChecklistDTO> lstImgs = new ArrayList<>();
        for (WoMappingAttachDTO iAttach : lstAttachs) {
            // Parse image to base 64
            File imgFile = new File(folderUpload + "/" + iAttach.getFilePath());
            byte[] content = new byte[0];
            try {
                content = FileUtils.readFileToByteArray(imgFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String imgBase64 = Base64.getEncoder().encodeToString(content);
            ImgChecklistDTO imgChecklistDTO = new ImgChecklistDTO();
            imgChecklistDTO.setImgBase64(imgBase64);
            imgChecklistDTO.setId(iAttach.getId());
            lstImgs.add(imgChecklistDTO);
        }
        return lstImgs;
    }

    @Override
    public List<WoChecklistDTO> getHcqtChecklist(WoChecklistDTO checklistDTO) {
        List<WoChecklistDTO> hcqtChecklist = woChecklistDAO.doSearch(checklistDTO);
        for (WoChecklistDTO item : hcqtChecklist) {
            List<ImgChecklistDTO> lstImgs = getImageList(item.getWoId(), item.getChecklistId());
            item.setLstImgs(lstImgs);
        }
        return hcqtChecklist;
    }

    @Override
    public List<WoChecklistDTO> getAvgChecklist(WoChecklistDTO checklistDTO) {
        List<WoChecklistDTO> lstAvgChecklists = woChecklistDAO.doSearch(checklistDTO);
        for (WoChecklistDTO item : lstAvgChecklists) {
            List<ImgChecklistDTO> lstImgs = getImageList(item.getWoId(), item.getChecklistId());
            item.setLstImgs(lstImgs);
        }
        return lstAvgChecklists;
    }

    @Override
    public List<WoChecklistDTO> getListChecklistsOfWoHcqt(Long woId) {
        WoChecklistDTO searchDto = new WoChecklistDTO();
        searchDto.setWoId(woId);
        List<WoChecklistDTO> lstData = woChecklistDAO.doSearch(searchDto);
        for (WoChecklistDTO iData : lstData) {
            // Get list file attach
            WoMappingAttachDTO searchObj = new WoMappingAttachDTO();
            searchObj.setWoId(woId);
            searchObj.setChecklistId(iData.getChecklistId());
            searchObj.setPage(1l);
            searchObj.setPageSize(9999);
            List<WoMappingAttachDTO> lstAttachs = woMappingAttachDAO.doSearch(searchObj);
            // List<ImgChecklistDTO> lstImgs = new ArrayList<>();
            // for (WoMappingAttachDTO iAttach : lstAttachs) {
            // // Parse image to base 64
            // File imgFile = new File(folderUpload + "/" + iAttach.getFilePath());
            // byte[] content = new byte[0];
            // try {
            // content = FileUtils.readFileToByteArray(imgFile);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            // String imgBase64 = Base64.getEncoder().encodeToString(content);
            // ImgChecklistDTO imgChecklistDTO = new ImgChecklistDTO();
            // imgChecklistDTO.setImgBase64(imgBase64);
            // lstImgs.add(imgChecklistDTO);
            // }
            // iData.setLstImgs(lstImgs);
        }
        return lstData;
    }

    @Transactional
    @Override
    public void updateChecklist(WoDTO woDTO, WoMappingChecklistDTO woMappingChecklistDTO) {
        Long checklistId = woMappingChecklistDTO.getChecklistId();
        List<ImgChecklistDTO> lstImgs = woMappingChecklistDTO.getLstImgs();

        // Delete WO_MAPPING_ATTACH
        woDAO.removeAttachOfChecklist(woDTO.getWoId(), checklistId);

        // Update WO_MAPPING_CHECKLIST
        WoMappingChecklistBO incomingBO = woMappingChecklistDTO.toModel();
        WoMappingChecklistBO dbBO = woMappingChecklistDAO.getOneRaw(incomingBO.getId());
        dbBO = transferWoMappingChecklistBoData(incomingBO, dbBO);
        woMappingChecklistDAO.updateObject(dbBO);

        // Insert image
        insertWoMappingAttach(woDTO, checklistId, lstImgs);

        // cap nhat trang thai hang muc - WORK_ITEM
        updateStateWorkItem(dbBO.getWoId());

    }



    private WoMappingChecklistBO transferWoMappingChecklistBoData(WoMappingChecklistBO incomingBO,
                                                                  WoMappingChecklistBO dbBO) {
        if (incomingBO.getState() != null)
            dbBO.setState(incomingBO.getState());
        if (incomingBO.getStatus() != null)
            dbBO.setStatus(incomingBO.getStatus());
        if (incomingBO.getQuantityLength() != null)
            dbBO.setQuantityLength(incomingBO.getQuantityLength());
        if (incomingBO.getAddedQuantityLength() != null)
            dbBO.setAddedQuantityLength(incomingBO.getAddedQuantityLength());
        if (incomingBO.getDbhtVuong() != null)
            dbBO.setDbhtVuong(incomingBO.getDbhtVuong());
        return dbBO;
    }

    @Transactional
    @Override
    public void updateChecklistHcqt(WoDTO woDTO, WoChecklistDTO woChecklistDTO) {
        Long checklistId = woChecklistDTO.getChecklistId();
        List<ImgChecklistDTO> lstImgs = woChecklistDTO.getLstImgs();

        // Delete WO_MAPPING_ATTACH
        woDAO.removeAttachOfChecklist(woDTO.getWoId(), checklistId);

        // Update WO_MAPPING_CHECKLIST
        WoChecklistBO woChecklistBO = woChecklistDTO.toModel();
        woChecklistDAO.updateObject(woChecklistBO);

        // Insert image
        insertWoMappingAttach(woDTO, checklistId, lstImgs);
    }

    @Transactional
    @Override
    public void updateChecklistXddd(WoDTO woDTO, WoMappingChecklistDTO inputChecklistItem) {
        // woMappingChecklistDAO.requestValue(checklistDto);

        // cập nhật checklist item của xddd (bảng wo_xddd_checklist)
        WoXdddChecklistBO xdddChecklistItem = woXdddChecklistDAO.getOneRaw(inputChecklistItem.getId());
        xdddChecklistItem.setValue(inputChecklistItem.getValue());
        xdddChecklistItem.setState(inputChecklistItem.getState());
        if (xdddChecklistItem != null) {
            woXdddChecklistDAO.updateObject(xdddChecklistItem);
        }

        // cập nhật ảnh
        Long checklistId = inputChecklistItem.getId();
        List<ImgChecklistDTO> lstImgs = inputChecklistItem.getLstImgs();

        // Delete WO_MAPPING_ATTACH
        woDAO.removeAttachOfChecklist(woDTO.getWoId(), checklistId);

        // Insert image
        insertWoMappingAttach(woDTO, checklistId, lstImgs);

        // update work_item
        updateStateWorkItemXddd(xdddChecklistItem.getWoId());
    }

    public void updateStateWorkItemXddd(Long woId) {
        List<WoXdddChecklistDTO> woXdddChecklistDTOS = woXdddChecklistDAO.findByWoID(woId);
        if(ObjectUtils.isEmpty(woXdddChecklistDTOS)){return;}
        // neu có 1
        // > cap nhat trang thai work_item = trang thai của Checklist
        if(woXdddChecklistDTOS.size() == 1){
            // nếu check list đang ở trạng thái done thì update thành done > cap nhat trang thai work_item = trang thai của Checklist
            if(Constant.STATE_CHECK_LIST.DONE.equals(woXdddChecklistDTOS.get(0).getState())){
                workItemDAO.updateStatusWorkItem(woId,Constant.STATE_WORK_ITEM.DONE);
            }
        }else {
            Boolean create = false;
            Boolean done = false;
            for (WoXdddChecklistDTO woXdddChecklistDTO : woXdddChecklistDTOS){
                if(Constant.STATE_CHECK_LIST.NEW.equals(woXdddChecklistDTO.getState())){
                    create = true;
                }
                if(Constant.STATE_CHECK_LIST.DONE.equals(woXdddChecklistDTO.getState())){
                    done = true;
                }
            }
//           + có ít nhát 1 STATE = DONE => WORK_ITEM.STATUS = 3
            if(create && done){
                workItemDAO.updateStatusWorkItem(woId,Constant.STATE_WORK_ITEM.PROCESSING);
            }
            // + tat ca STATE = DONE => WORK_ITEM.STATUS = 5
            if(!create && done){
                workItemDAO.updateStatusWorkItem(woId,Constant.STATE_WORK_ITEM.DONE);
            }
        }
    }

    public void updateStateWorkItem(Long woId){
        // lay danh sách checlist
        List<WoMappingChecklistDTO> woMappingChecklistDTOS = woMappingChecklistDAO.findByWoId(woId);

        if(ObjectUtils.isEmpty(woMappingChecklistDTOS)){return;}
        // neu có 1
        // > cap nhat trang thai work_item = trang thai của Checklist
        if(woMappingChecklistDTOS.size() == 1){
            // nếu check list đang ở trạng thái done thì update thành done > cap nhat trang thai work_item = trang thai của Checklist
            if(Constant.STATE_CHECK_LIST.DONE.equals(woMappingChecklistDTOS.get(0).getState())){
                workItemDAO.updateStatusWorkItem(woId,Constant.STATE_WORK_ITEM.DONE);
            }
        }else {
            Boolean create = false;
            Boolean done = false;
            for (WoMappingChecklistDTO woMappingChecklist : woMappingChecklistDTOS){
                if(Constant.STATE_CHECK_LIST.NEW.equals(woMappingChecklist.getState())){
                    create = true;
                }
                if(Constant.STATE_CHECK_LIST.DONE.equals(woMappingChecklist.getState())){
                    done = true;
                }
            }
//           + có ít nhát 1 STATE = DONE => WORK_ITEM.STATUS = 3
            if(create && done){
                workItemDAO.updateStatusWorkItem(woId,Constant.STATE_WORK_ITEM.PROCESSING);
            }
            // + tat ca STATE = DONE => WORK_ITEM.STATUS = 5
            if(!create && done){
                workItemDAO.updateStatusWorkItem(woId,Constant.STATE_WORK_ITEM.DONE);
            }
        }
    }

    private void insertWoMappingAttach(WoDTO woDTO, Long checklistId, List<ImgChecklistDTO> lstImgs) {
        if (lstImgs != null && lstImgs.size() > 0) {
            int imgIdx = 1;
            String dateString = new SimpleDateFormat("yyyymmdd_hhMMss").format(new Date());
            for (ImgChecklistDTO iImg : lstImgs) {
                WoMappingAttachBO woMappingAttachBO = new WoMappingAttachBO();
                woMappingAttachBO.setWoId(woDTO.getWoId());
                woMappingAttachBO.setUserCreated(woDTO.getUserCreated());
                woMappingAttachBO.setChecklistId(checklistId);

                String fileName = dateString + "_" + imgIdx + ".jpg";
                byte[] decodedBytes = com.itextpdf.text.pdf.codec.Base64.decode(iImg.getImgBase64());
                InputStream is = new ByteArrayInputStream(decodedBytes);
                String path = null;
                try {
                    // path = UFile.writeToFileServerATTT2(is, fileName, "/" + woDTO.getWoId() + "/"
                    // + checklistId + "/", folderUpload);
                    path = UFile.writeToFileServerATTT2(is, fileName, UPLOAD_SUB_FOLDER, folderUpload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                woMappingAttachBO.setFileName(fileName);
                woMappingAttachBO.setFilePath(path);
                woMappingAttachBO.setStatus(1l);
                woMappingAttachBO.setLatitude(iImg.getLatitude());
                woMappingAttachBO.setLongtitude(iImg.getLongtitude());

                woMappingAttachDAO.save(woMappingAttachBO);

                imgIdx++;
            }
        }
    }

    @Override
    public boolean insertUpdateDailyChecklist(WoMappingChecklistDTO checklistDto) {
        Date currentDate = new Date();
        WoTaskDailyBO bo = woTaskDailyDAO.getOneRawByDate(currentDate, checklistDto.getId());
        if (bo != null && "1".equalsIgnoreCase(bo.getConfirm()))
            return false;

        WoBO wo = woDAO.getOneRaw(checklistDto.getWoId());
        Long ftId = wo.getFtId();
        Long constructionId = wo.getConstructionId();
        Long catConstructionId = woDAO.getCatConstructionId(constructionId);
        Long checkListId = checklistDto.getChecklistId();
        Double price = woTaskDailyDAO.getPriceOfChecklist(checkListId, constructionId, catConstructionId);

        // đơn vị lưu price trong db là triệu vnd
        price = price * 1000000;

        Double quantity = checklistDto.getAddedQuantityLength() * price;
        if (bo == null) {
            bo = new WoTaskDailyBO();
            bo.setWoId(checklistDto.getWoId());
            bo.setAmount(checklistDto.getAddedQuantityLength());
            bo.setCreatedDate(currentDate);
            bo.setConfirm("0");
            bo.setCreatedUserId(ftId);
            bo.setStatus(1l);
            bo.setWoMappingChecklistId(checklistDto.getId());
            bo.setQuantity(quantity);
            woTaskDailyDAO.saveObject(bo);
        } else {
            bo.setQuantity(quantity);
            bo.setAmount(checklistDto.getAddedQuantityLength());
            woTaskDailyDAO.updateQtyAndAmount(bo);
        }

        return true;
    }

    @Override
    public boolean giveAssignment(WoDTO woDto) {
        Gson gson = new Gson();

        String assignState = "";

        String cd2 = woDto.getCdLevel2();
        String cd3 = woDto.getCdLevel3();
        String cd4 = woDto.getCdLevel4();
        String cd5 = woDto.getCdLevel5();
        Long ftId = woDto.getFtId();

        if (woDto.getLoggedInUser() == null
                || (cd2 == null && cd3 == null && cd4 == null && cd5 == null && ftId == null)) {
            return false;
        }

        long woId = woDto.getWoId();
        WoBO bo = woDAO.getOneRaw(woId);

        if (ftId != null) {
            if ("HCQT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                if (PROCESSING.equalsIgnoreCase(bo.getState()))
                    assignState = PROCESSING;
                if (ACCEPT_FT.equalsIgnoreCase(bo.getState()))
                    assignState = ACCEPT_FT;
            } else
                assignState = ASSIGN_FT;

            // kiểm tra công nợ của FT
            boolean hasDebt = this.checkDebt(woDto);

            if (hasDebt) {
                woDto.setCustomField("FT còn công nợ chưa thanh toán. Không thể giao WO!");
                return false;
            }

        } else {
            assignState = ASSIGN_CD;
        }

        woDto.setState(assignState);
        String loggedInUser = woDto.getLoggedInUser();
        String assignedUnitName = "";

        if (woDto.getCdLevel1() != null)
            bo.setCdLevel1(woDto.getCdLevel1());
        if (woDto.getCdLevel2() != null)
            bo.setCdLevel2(woDto.getCdLevel2());
        if (woDto.getCdLevel3() != null)
            bo.setCdLevel3(woDto.getCdLevel3());
        if (woDto.getCdLevel4() != null)
            bo.setCdLevel4(woDto.getCdLevel4());
        if (woDto.getCdLevel5() != null)
            bo.setCdLevel5(woDto.getCdLevel5());

        if (!StringUtils.isNullOrEmpty(woDto.getCdLevel1Name())) {
            bo.setCdLevel1Name(woDto.getCdLevel1Name());
            assignedUnitName = woDto.getCdLevel1Name();
        }

        if (!StringUtils.isNullOrEmpty(woDto.getCdLevel2Name())) {
            bo.setCdLevel2Name(woDto.getCdLevel2Name());
            assignedUnitName = woDto.getCdLevel2Name();
        }
        if (!StringUtils.isNullOrEmpty(woDto.getCdLevel3Name())) {
            bo.setCdLevel3Name(woDto.getCdLevel3Name());
            assignedUnitName = woDto.getCdLevel3Name();
        }
        if (!StringUtils.isNullOrEmpty(woDto.getCdLevel4Name())) {
            bo.setCdLevel4Name(woDto.getCdLevel4Name());
            assignedUnitName = woDto.getCdLevel4Name();
        }

        if (!StringUtils.isNullOrEmpty(woDto.getCdLevel5Name())) {
            bo.setCdLevel5Name(woDto.getCdLevel5Name());
            assignedUnitName = woDto.getCdLevel5Name();
        }

        if (woDto.getFtId() != null)
            bo.setFtId(woDto.getFtId());
        if (!StringUtils.isNullOrEmpty(woDto.getFtName())) {
            bo.setFtName(woDto.getFtName());
            assignedUnitName = woDto.getFtName();
        }
        if (!StringUtils.isNullOrEmpty(woDto.getFtEmail())) {
            bo.setFtEmail(woDto.getFtEmail());
            assignedUnitName += " " + woDto.getFtEmail();
        }

        if (woDto.getState() != null)
            bo.setState(woDto.getState());

        // reassign to ft2 must delete cdlv3
        if (woDto.getFtId() != null && woDto.getCdLevel3() == null && bo.getCdLevel4() == null) {
            bo.setCdLevel3(null);
            bo.setCdLevel3Name(null);
        }

        try {
            // Update WO
            woDAO.update(bo);
            woDto = bo.toDTO();
            // Write worklogs
            String content = "";
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(assignedUnitName.trim())) {
                content += "Giao cho: " + assignedUnitName + " ";
            }
            content += "Trạng thái: " + woDAO.getNameAppParam(woDto.getState(), WO_XL_STATE);

            logWoWorkLogs(woDto, "1", content, gson.toJson(bo), loggedInUser);

            // Update AIO
            WoTypeDTO woTypeDTO = woDAO.getWoTypeAio("AIO");
            if (bo.getWoTypeId() == woTypeDTO.getWoTypeId()) {
                if (bo.getFtId() != null) {
                    SysUserCOMSDTO sysUserFT = woDAO.getUserInfoById(bo.getFtId());
                    SysUserCOMSDTO sysUserLogin = woDAO.getUserInfoByLoginname(loggedInUser);
                    if (sysUserFT != null && sysUserLogin != null) {
                        AIOWoTrDTO aioWoTrDTO = new AIOWoTrDTO();
                        aioWoTrDTO.setUserCreated(sysUserLogin.getSysUserId());
                        aioWoTrDTO.setPerformerCode(sysUserFT.getEmployeeCode());
                        aioWoTrDTO.setPerformerName(sysUserFT.getFullName());
                        aioWoTrDTO.setContractId(bo.getContractId());
                        aioWoTrDTO.setPerformerId(bo.getFtId());
                        aioWoTrDTO.setPerformerGroupId(woDAO.getUserSysGroupLevel2ByUserId(bo.getFtId()));
                        Long statusAio = woDAO.getStatusContractAIO(aioWoTrDTO.getContractId());
                        if (statusAio == 3l) {
                            return false;
                        } else {
                            woDAO.updateAioAcceptance(aioWoTrDTO);
                            woDAO.updateAioContract(aioWoTrDTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean cdAcceptAssignment(WoDTO woDto) {
        Gson gson = new Gson();
        long woId = woDto.getWoId();
        String loggedInUser = woDto.getLoggedInUser();

        WoBO bo = woDAO.getOneRaw(woId);
        bo.setState(ACCEPT_CD);

        WoDTO woDetail = woDAO.getOneDetails(woDto.getWoId());
        if ("DOANHTHU_DTHT".equalsIgnoreCase(woDetail.getWoTypeCode())) {
            WoSimpleSysUserDTO user = trDAO.getSysUser(loggedInUser);
            Date changeStateTime = new Date();
            // Cập nhật trạng thái thành PROCESSING, giao FT là người đăng nhập
            bo.setFtId(user.getSysUserId());
            bo.setFtName(user.getFullName());
            bo.setFtEmail(user.getEmail());
            bo.setState(PROCESSING);
            bo.setAcceptTime(changeStateTime);
            bo.setStartTime(changeStateTime);
            bo.setUpdateFtReceiveWo(changeStateTime);
            bo.setUserFtReceiveWo(user.getEmployeeCode());
            bo.setUpdateCdLevel2ReceiveWo(changeStateTime);
            bo.setUserCdLevel2ReceiveWo(user.getEmployeeCode());
        }
        recordAcceptPerson(bo, loggedInUser, new Date());

        try {
            woDAO.update(bo);
            woDto = bo.toDTO();
            String content = woDAO.getNameAppParam(ACCEPT_CD, WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(bo), loggedInUser);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean cdRejectAssignment(WoDTO woDto) {
        Gson gson = new Gson();
        long woId = woDto.getWoId();
        String loggedInUser = woDto.getLoggedInUser();
        WoSimpleSysGroupDTO sysGroup = trDAO.getSysUserGroup(woDto.getLoggedInUser());
        String sysGroupId = String.valueOf(sysGroup.getSysGroupId());

        WoBO bo = woDAO.getOneRaw(woId);
        bo.setState(REJECT_CD);

        if (sysGroupId.equalsIgnoreCase(bo.getCdLevel2()))
            bo.setCdLevel2(null);
        if (sysGroupId.equalsIgnoreCase(bo.getCdLevel3()))
            bo.setCdLevel3(null);

        try {
            woDAO.update(bo);
            woDto = bo.toDTO();
            String content = woDAO.getNameAppParam(REJECT_CD, WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(bo), loggedInUser);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean giveAssignmentToFT(WoDTO woDto) {
        Gson gson = new Gson();
        long woId = woDto.getWoId();
        long ftId = woDto.getFtId();
        String loggedInUser = woDto.getLoggedInUser();

        WoBO bo = woDAO.getOneRaw(woId);
        if (bo.getFtId() != null) {
            if (bo.getFtId() > 0)
                return false;
        }
        bo.setFtId(ftId);
        bo.setFtEmail(woDto.getFtEmail());
        bo.setFtName(woDto.getFtName());
        bo.setState(ASSIGN_FT);

        try {
            woDAO.update(bo);
            woDto = bo.toDTO();
            String content = woDAO.getNameAppParam(ASSIGN_FT, WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content + " : " + String.valueOf(woDto.getFtName()), gson.toJson(bo),
                    loggedInUser);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean changeWoState(WoDTO woDto, String state) {
        Gson gson = new Gson();
        long woId = woDto.getWoId();
        String loggedInUser = woDto.getLoggedInUser();
        WoSimpleSysUserDTO user = trDAO.getSysUser(loggedInUser);
        String commentText = woDto.getText();
        String invoicePeriod = woDto.getInvoicePeriod();
        Date stationRevenueDate = woDto.getStationRevenueDate();
        Long rejectedFtId = null;
        Date changeStateTime = new Date();

        WoBO bo = woDAO.getOneRaw(woId);

        // Huypq-12082021-start
        // Khi TTHT duyệt/từ chối wo thi công thì chỉ cho duyệt trong tháng hiện tại
        // (trừ loại wo XDDD Khởi công)
        if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode()) && "CD_OK".equalsIgnoreCase(bo.getState())
                && ("OK".equalsIgnoreCase(state) || "NG".equalsIgnoreCase(state))) {
            DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
            String currentDate = dateFormat.format(new Date());
            // Huypq-01112021-start open comment theo yêu cầu
            String logTime = woDAO.getLogTimeByWoId(woId, "Điều phối duyệt OK");

            if (!currentDate.equalsIgnoreCase(logTime)) {
//				if (bo.getApWorkSrc() != null && (bo.getApWorkSrc() != 6l || (bo.getApWorkSrc() == 6l
//						&& !woDto.getCatWorkItemTypeName().equalsIgnoreCase("Khởi công")))) {
                if (woDto.getCatWorkItemTypeName() != null && !woDto.getCatWorkItemTypeName().equalsIgnoreCase("Khởi công trạm HTCT") && !woDto.getCatWorkItemTypeName().equalsIgnoreCase("Khởi công")) {
                    woDto.setCustomField(
                            "Chỉ được duyệt hoặc từ chối WO trong cùng tháng mà CD CNKT đã duyệt điều phối OK ! ");
                    return false;
                }
            }
            // Huypq-end comment
            // Với loại Khởi công khi TTHT duyệt thì update CheckWoKcXDDD của cntContract
            if ("OK".equalsIgnoreCase(state) && bo.getApWorkSrc() == 6l
                    && woDto.getCatWorkItemTypeName().equalsIgnoreCase("Khởi công")) {
                woXdddChecklistDAO.updateCheckWoKcXDDDInContract(bo.getContractId());
            }
        }
        // Huy-end

        // Thêm thông tin người nhận wo
        if (ACCEPT_FT.equalsIgnoreCase(state)) {
            bo.setUserFtReceiveWo(loggedInUser);
            bo.setUpdateFtReceiveWo(changeStateTime);
        } else if (ACCEPT_CD.equalsIgnoreCase(state)) {
            recordAcceptPerson(bo, loggedInUser, changeStateTime);
        } else if (REJECT_FT.equalsIgnoreCase(state)) {
            rejectedFtId = bo.getFtId();
            bo.setFtId(null);
            bo.setFtName(null);
            bo.setFtEmail(null);
        }

        String logContent = woDAO.getNameAppParam(state, WO_XL_STATE);

        //Huypq-11112021-start
        if (bo.getOpinionType() != null) {
            if (bo.getOpinionType().equalsIgnoreCase("Đề xuất gia hạn")) {
                logContent += " đề xuất gia hạn";
            }
            if (bo.getOpinionType().equalsIgnoreCase("Đề xuất hủy")) {
                logContent += " đề xuất hủy";
            }
        }
        //Huy-end

        if (!StringUtils.isNullOrEmpty(woDto.getText())) {
            logContent += " - " + woDto.getText();
        }

        try {
            // Lay thong tin hop dong
            CntContractDTO cntContractDTO = cntContractDAO.getCntContractByCode(bo.getContractCode());

            //Huypq-10122021-start check hợp đồng chia sẻ doanh thu
//			Long contractShare = 0l;
//			if(woDto.getContractId()!=null) {
//				contractShare = catStationDAO.getContractShareRevenueById(woDto.getContractId());
//			}
            //Huy-end

            int checkContractRevenue = 0;

            if (CD_OK.equalsIgnoreCase(state)) { // CD duyet OK
                bo.setUserCdApproveWo(woDto.getLoggedInUser());
                bo.setUpdateCdApproveWo(new Date());
                bo.setPmtStatus(woDto.getPmtStatus());
                bo.setBgbtsResult(null);
                if (woDto.getEndTime() != null)
                    bo.setEndTime(woDto.getEndTime());

                if ("AIO".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    bo.setApproveDateReportWo(new Date());
                } else if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    bo.setApproveDateReportWo(new Date());
                    boolean check = false;
                    Double quantity = 0d;
                    List<WoTaskDailyDTO> listWoTaskDaily = woDAO.getCheckWoTaskDaily(woId);
                    if (listWoTaskDaily != null && listWoTaskDaily.size() > 0) {
                        check = true;
                        woDAO.updateWoTaskDaily(woDto.getLoggedInUser(), woId);
                        for (WoTaskDailyDTO woTaskDailyDTO : listWoTaskDaily) {
                            woDAO.updateWoMapingCheckList(woTaskDailyDTO.getQuantity(),
                                    woTaskDailyDTO.getWoMappingChecklistId());
                            quantity += woTaskDailyDTO.getQuantity();
                        }
                    }

                    if (quantity > 0) {
                        logWoWorkLogs(bo.toDTO(), "1", "Sản lượng: " + quantity / 1000000 + " triệu.",
                                gson.toJson(bo.toDTO()), woDto.getLoggedInUser());
                    }

                    // confirm hết sản lượng xddd nếu điều phối duyệt CD OK
                    if (bo.getCatConstructionTypeId() != null && bo.getCatConstructionTypeId() == 8) {
                        WoXdddChecklistDTO searchObj = new WoXdddChecklistDTO();
                        searchObj.setWoId(bo.getWoId());
                        List<WoXdddChecklistDTO> checklistXddd = woXdddChecklistDAO.doSearch(searchObj);

                        double addedValue = 0;
                        for (WoXdddChecklistDTO item : checklistXddd) {
                            if (item.getConfirm() == null || item.getConfirm() != 1) {
                                if (item.getValue() != null) {
                                    WoXdddChecklistBO itemBo = woXdddChecklistDAO.getOneRaw(item.getId());
                                    itemBo.setConfirm(1l);
                                    itemBo.setConfirmBy(loggedInUser);
                                    itemBo.setConfirmDate(new Date());
                                    itemBo.setState("DONE");
                                    woXdddChecklistDAO.updateObject(itemBo);

                                    addedValue += itemBo.getValue();
                                } else {
                                    woDto.setCustomField("Chưa khai báo sản lượng cho tất cả các đầu việc! ");
                                    return false;
                                }
                            }
                        }

                        // thêm sản lượng xddd đã ghi nhận vào tổng sl của wo
                        bo.setMoneyValue(bo.getMoneyValue() + addedValue);
                    }

                    if (check == true) {
                        woDAO.updatePercentConstructionCDOK(bo.toDTO(), quantity, false, state);
                    } else {
                        // if(!StringUtils.isStringNullOrEmpty(woDto.getCheckHTCT()) &&
                        // woDto.getCheckHTCT().equals("1")) {
                        // woDAO.updatePercentConstructionCDOK(bo.toDTO(), bo.getMoneyValue(), true);
                        // } else {
                        // Nếu là wo thi công theo luồng mới của ĐTHT thì update cả bảng
                        // WO_MAPPING_WORK_ITEM_HTCT
                        List<WoMappingWorkItemHtctBO> lstWoMapping = woDAO.checkExistWoMappingWiHtct(bo);
//						WoDTO woDtoSearch = new WoDTO();
//						woDtoSearch.setTrId(bo.getTrId());
//						List<WoDTO> lstWosOfTr = new ArrayList<>();
//						for (WoDTO iWo : woDAO.doSearch(woDtoSearch, null, false, false, null)) {
//							String woOrder = iWo.getWoOrder();
//							if (!StringUtils.isNullOrEmpty(woOrder) && Long.parseLong(woOrder) > 1
//									&& Long.parseLong(woOrder) <= 7) {
//								lstWosOfTr.add(iWo);
//							}
//						}

                        if (lstWoMapping.size() > 0) {
                            // Update giá trị, trạng thái hạng mục, công trình
                            Long result = woDAO.updatePercentWoMappingWiHtct(bo, 11l);

                            // Nếu công trình hoàn thành thì sinh wo HSHC, doanh thu ĐTHT
                            if (result == 2) {
//								WoDTO woDetail = woDAO.getOneDetails(bo.getWoId());
//								// Tu dong sinh wo HSHC
//								woDetail.setUserCreated(loggedInUser);
//								Long woHshcId = tryCreateHshc(woDetail, lstWosOfTr);
//
//								// Tu dong sinh wo Doanh thu
//								Long checkDoanhThuDTHTWoId = woDAO
//										.getDoanhThuDTHTWoByConstruction(woDetail.getConstructionId());
//								if (checkDoanhThuDTHTWoId == null) {
//									Double completeValue = woDAO
//											.getHshcMoneyValueByConstructionId(bo.getConstructionId());
//									createNewDoanhThuWO(woDetail, completeValue);
//								}
//
//								ConstructionDTO constructionDTO = new ConstructionDTO();
//								constructionDTO.setConstructionId(bo.getConstructionId());
//								constructionDTO.setCode(bo.getConstructionCode());
//								createWoByParOrder(bo, constructionDTO, cntContractDTO, "7");

                                //Tạo wo DBHT khi công trình về trạng thái Chờ duyệt ĐBHT
                                // check wo tồn tại
                                Long woDbhtId = woDAO.getWoDBHTByConstruction(bo.getConstructionId());
                                if (woDbhtId != null) {
                                    woDAO.updateWoDbhtWhenReApprove(woDbhtId, "CD_OK", 1l);
                                } else {
                                    ConstructionDTO constructionDTO = new ConstructionDTO();
                                    constructionDTO.setConstructionId(bo.getConstructionId());
                                    constructionDTO.setCode(bo.getConstructionCode());
                                    createWoDbht(bo, constructionDTO, cntContractDTO, loggedInUser);
                                }
                            }
                        } else {
                            checkContractRevenue = woDAO.updatePercentConstructionCDOK(bo.toDTO(), bo.getMoneyValue(), false, state);
                        }
                        // }
                    }
                }

                // ghi nhận hết sản lượng theo tháng nếu chưa ghi nhận
                if ("THDT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    tryAcceptAllMonthlyQuantity(woDto);
                }

                // gọi sang tài sản để giảm trừ vật tư trong kho
                if ("UCTT".equalsIgnoreCase(woDto.getWoTypeCode()) || "AVG".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    trySubmitReduceGoods(woDto);
                }

                // Tao wo HSHC khi dong wo thi cong (nguon dau tu: CONTRACT_TYPE_O = 1)
                // Lay danh sach tat ca wo thi cong thuoc nha tram (cua wo hien tai)
                if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    WoDTO newDto = bo.toDTO();
                    newDto.setWoTypeCode(woDto.getWoTypeCode());

                    // Nguon dau tu thi phai check cac wo thuoc ma nha tram da duoc duyet OK
                    if (cntContractDTO != null && cntContractDTO.getContractTypeO() != null) {
                        if (cntContractDTO != null && cntContractDTO.getContractTypeO() == 1) {
                            List<WoDTO> lstWoOfStationHouses = woDAO.getLstWoOfStationHouses(woId);
                            boolean checkOkAll = lstWoOfStationHouses.size() > 0 ? true : false;
                            List<WoDTO> lstWoTcs = new ArrayList<>();
                            for (WoDTO iWo : lstWoOfStationHouses) {
                                if (iWo.getContractCode() != null
                                        && !iWo.getContractCode().equalsIgnoreCase(bo.getContractCode())) {
                                    continue;
                                }
                                lstWoTcs.add(iWo);
                                if (!"OK".equalsIgnoreCase(iWo.getState()) && !"CD_OK".equalsIgnoreCase(iWo.getState())
                                        && !iWo.getWoId().equals(bo.getWoId())) {
                                    checkOkAll = false;
                                    break;
                                }
                            }
                            if (checkOkAll) {
                                tryCreateHshc(newDto, lstWoTcs);
                            }
                        } else { // Con lai thi tao HSHC luon
                            tryCreateHshc(newDto, null);
                        }
                    }
//					else {
//						if(cntContractDTO.getCntContractRevenue()!=null && cntContractDTO.getCntContractRevenue().equals("1") && checkContractRevenue==2) {
//							tryCreateHshc(newDto, null);
//						}
//					}
                }

                // Dong bo ha tang
                // Neu loai TR la dong bo ha tang, check xem neu 5 hang muc
                // Khởi công trạm HTCT, Thi công điện AC, XD móng cột dưới đất, Lắp dựng cột
                // dưới đất, Thi công tiếp địa lập là
                // hoàn thành thì: 1. Hoàn thành công trình, 2: Sinh WO HSHC
                // Huypq-comment luồng cũ wo ĐTHT
                WoDTO woDetail = woDAO.getOneDetails(bo.getWoId());
                if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode())
                        && "TR_DONG_BO_HA_TANG".equalsIgnoreCase(woDetail.getTrTypeCode())) {
                    List<WoMappingWorkItemHtctBO> lstWoMapping = woDAO.checkExistWoMappingWiHtct(bo);
                    if (lstWoMapping.size() == 0) {
                        WoDTO woDtoSearch = new WoDTO();
                        woDtoSearch.setTrId(bo.getTrId());
                        // List<WoDTO> lstWosOfTr = woDAO.doSearch(woDtoSearch, null, false, false,
                        // null);
                        List<WoDTO> lstWosOfTr = new ArrayList<>();
                        for (WoDTO iWo : woDAO.doSearch(woDtoSearch, null, false, false, null)) {
                            String woOrder = iWo.getWoOrder();
                            if (!StringUtils.isNullOrEmpty(woOrder) && Long.parseLong(woOrder) >= 1
                                    && Long.parseLong(woOrder) <= 7) {
                                lstWosOfTr.add(iWo);
                            }
                        }
                        boolean canCreateHshc = true;
                        Double completeValue = woDto.getMoneyValue() == null ? 0 : woDto.getMoneyValue();

                        // Neu wo thu 6 thi can xong 5 wo thi cong dau tien
                        if ("6".equalsIgnoreCase(bo.getWoOrder())) {
                            for (WoDTO iWoOfTr : lstWosOfTr) {
                                if (iWoOfTr.getWoOrder() != null && Long.parseLong(iWoOfTr.getWoOrder()) < 6
                                        && !"OK".equalsIgnoreCase(iWoOfTr.getState())
                                        && !"CD_OK".equalsIgnoreCase(iWoOfTr.getState())) { // Huypq-05072021-edit
                                    // check
                                    // null
                                    woDto.setCustomField("Chưa hoàn thành 5 đầu việc thi công !");
                                    return false;
                                }
                            }
                        }

                        // Neu wo thu 7 thi can xong 6 wo thi cong dau tien
                        if ("7".equalsIgnoreCase(bo.getWoOrder())) {
                            for (WoDTO iWoOfTr : lstWosOfTr) {
                                if (iWoOfTr.getWoOrder() != null && Long.parseLong(iWoOfTr.getWoOrder()) < 7
                                        && !"OK".equalsIgnoreCase(iWoOfTr.getState())
                                        && !"CD_OK".equalsIgnoreCase(iWoOfTr.getState())) { // Huypq-05072021-edit
                                    // check
                                    // null
                                    woDto.setCustomField("Chưa hoàn thành 6 đầu việc nên không thể thực duyệt phát sóng !");
                                    return false;
                                }
                            }
                        }

                        // Xong 5 đầu việc đầu thì sinh HSHC
                        if (Long.parseLong(bo.getWoOrder()) <= 5) {
                            for (WoDTO iWoOfTr : lstWosOfTr) {
                                // Check những wo khac
                                if (iWoOfTr.getWoOrder() != null
                                        && Long.parseLong(iWoOfTr.getWoOrder()) != Long.parseLong(bo.getWoOrder())
                                        && Long.parseLong(iWoOfTr.getWoOrder()) <= 5
                                        && !"OK".equalsIgnoreCase(iWoOfTr.getState())
                                        && !"CD_OK".equalsIgnoreCase(iWoOfTr.getState())) { // Huypq-05072021-edit
                                    // check
                                    // null
                                    canCreateHshc = false;
                                    break;
                                }
                                completeValue += (iWoOfTr.getMoneyValue() == null ? 0 : iWoOfTr.getMoneyValue());
                            }
                        }

                        if (Long.parseLong(bo.getWoOrder()) <= 5) {
                            if (canCreateHshc) {
                                // Tu dong sinh wo HSHC
                                woDetail.setUserCreated(loggedInUser);
                                Long woHshcId = tryCreateHshc(woDetail, lstWosOfTr);
                                // Hoan thanh cong trinh
                                WoDTO woHshc = woDAO.getOneDetails(woHshcId);

                                woDAO.updatePercentConstructionCDOK(woHshc, completeValue, true, state);
                                // Tu dong sinh wo Doanh thu
                                Long checkDoanhThuDTHTWoId = woDAO
                                        .getDoanhThuDTHTWoByConstruction(woDetail.getConstructionId());
                                if (checkDoanhThuDTHTWoId == null) {
                                    createNewDoanhThuWO(woDetail, completeValue);
                                }
                            }
                        }

                        if (woDto.getFinishDate() != null) {
                            bo.setFinishDate(woDto.getFinishDate());
                        }
                    }
                }
                ConstructionDTO constructionDTO = new ConstructionDTO();
                constructionDTO.setConstructionId(bo.getConstructionId());
                constructionDTO.setCode(bo.getConstructionCode());
                createWoBtsVHKT(bo, constructionDTO, cntContractDTO, loggedInUser,woDto,gson);
            } else if (WAIT_TC_BRANCH.equalsIgnoreCase(state)) {
                bo.setUserTthtApproveWo(woDto.getLoggedInUser());
                bo.setUpdateTthtApproveWo(new Date());
            } else if (WAIT_TC_TCT.equalsIgnoreCase(state)) {
                bo.setUserTcBranchApproveWo(woDto.getLoggedInUser());
                bo.setUpdateTcBranchApproveWo(new Date());
                bo.setEmailTcTct(woDto.getEmailTcTct());
            } else if (OK.equalsIgnoreCase(state)) { // Duyet dong WO
                // Neu trang thai truoc khi duyet la CD_OK -> luong binh thuong
                // Nguoc lai thi la TC TCT duyet va dong WO
                if (bo.getState().equalsIgnoreCase(CD_OK)) {
                    bo.setUserTthtApproveWo(woDto.getLoggedInUser());
                    bo.setUpdateTthtApproveWo(new Date());
                    // Don vi duyet luc nay la TTHT
                    // Neu loai wo la HSHC
                    // Neu cong trinh co don vi quyet toan
                } else {
                    bo.setUserTcTctApproveWo(woDto.getLoggedInUser());
                    bo.setUpdateTcTctApproveWo(new Date());
                }

                // Ngay ghi nhan duyet WO
                if (!"THICONG".equalsIgnoreCase(woDto.getWoTypeCode()) && !"AIO".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    if (!"HSHC".equalsIgnoreCase(woDto.getWoTypeCode()) || ("HSHC".equalsIgnoreCase(woDto.getWoTypeCode())
                            && !(bo.getApWorkSrc() == 4l && bo.getTrId() != null))) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        if (day < 4) {
                            Date date = new Date(cal.getTime().getTime() - (day) * 24 * 60 * 60 * 1000);
                            bo.setApproveDateReportWo(date);
                        } else {
                            bo.setApproveDateReportWo(new Date());
                        }
                    }
                }
                //duonghv13 add 13122021
                if ("BDMPD".equalsIgnoreCase(woDto.getWoTypeCode())
                        || "Codien_HTCT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    bo.setApproveDateReportWo(new Date());

                }

                // Thoi gian dong wo
                bo.setClosedTime(new Date());

                // Hoàn thành nghiệp vụ liên quan đến công trình
                WoDTO newDto = bo.toDTO();
                newDto.setWoTypeCode(woDto.getWoTypeCode());
                woDAO.updatePercentConstruction(newDto);

                // Cap nhat checklist HCQT
                if ("HCQT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    woDAO.updateWoCheckListOK(bo.getMoneyValue(), user.getSysUserId(), user.getFullName(),
                            bo.getWoId());
                    bo.setChecklistStep(5L);
                }

                // Tự động giao wo HCQT sau khi phê duyệt wo HSHC đối với các hợp đồng có đơn vị
                // quyết toán là TTHT
                if ("HSHC".equalsIgnoreCase(woDto.getWoTypeCode()) && cntContractDTO != null
                        && "2".equalsIgnoreCase(cntContractDTO.getUnitSettlement())
                        && cntContractDTO.getContractTypeO() != null && cntContractDTO.getContractTypeO() == 1l
                        && !(bo.getApWorkSrc() == 4l && bo.getTrId() != null)) { // Huypq-thêm điều kiện để bỏ
                    // loại wo HSHC ĐTHT
                    tryCreateHcqt(woDto, cntContractDTO);
                }

                // Tu dong sinh wo TTHQ khi duyet dong wo TMBT
                if ("TMBT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    bo.setApproveDateReportWo(new Date());
                    WoDTO woTmbt = bo.toDTO();
                    woTmbt.setUserCreated(loggedInUser);
                    tryCreateTthq(woTmbt);
                }

                // Tu dong sinh wo TKDT khi duyet dong TTHQ va wo TTHQ co ke qua la "Hieu qua"
//				List<WoMappingChecklistDTO> lstChecklists = woMappingChecklistDAO.getMappingCheclistByWoId(woId);
//				boolean isHq = true;
//				for (WoMappingChecklistDTO iChecklist : lstChecklists) {
//					if (StringUtils.isNullOrEmpty(iChecklist.getTthqResult())
//							|| "Không hiệu quả".equalsIgnoreCase(iChecklist.getTthqResult())) {
//						isHq = false;
//						break;
//					}
//				}

                if ("DOANHTHU".equalsIgnoreCase(woDto.getWoTypeCode()) && "WAIT_TC_TCT".equalsIgnoreCase(bo.getState())
                        && "OK".equalsIgnoreCase(state)) {
                    woMappingChecklistDAO.updateStateMappingCheckList(woId);
                }

                //Huypq-29112021-start comment
//				if ("TTHQ".equalsIgnoreCase(woDto.getWoTypeCode()) && isHq) {
//					WoDTO woTthq = bo.toDTO();
//					woTthq.setUserCreated(loggedInUser);
//					tryCreateTkdt(woTthq);
//				}
                //Huypq-29112021-end comment
                // Trạm phát sóng xong
                if ("PHATSONG".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    WoDTO woDetail = getOneWoDetails(bo.getWoId());
                    woDAO.tryUpdateConstructionPs(woDetail.getConstructionId(), woDetail.getCatWorkItemTypeId());
                }

                // Huypq-22102021-start
                if ("HSHC".equalsIgnoreCase(woDto.getWoTypeCode()) && bo.getApWorkSrc() == 4l && bo.getTrId() != null
                        && "WAIT_TC_TCT".equalsIgnoreCase(bo.getState())) {
                    bo.setMoneyValueHtct(bo.getMoneyValueHtct());
//					bo.setState(OK);
                    bo.setStateHtct(OK);
                }
                // Huy-end

                // Đóng wo khởi công HTCT sinh wo thi công ứng với hạng mục đã gán bước TKDT
                if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode()) && "CD_OK".equalsIgnoreCase(bo.getState())) {
                    //Cập nhật ngày hoàn thành khởi công của công trình
                    woDAO.updateConstrWhenCloseWoKhoiCong(bo.getConstructionId());
                    Long id = woDAO.getWorkItemTypeIdByCode("HTCT_02");
                    if (id.compareTo(bo.getCatWorkItemTypeId()) == 0) {
                        ConstructionDTO constructionDTO = new ConstructionDTO();
                        constructionDTO.setConstructionId(bo.getConstructionId());
                        constructionDTO.setCode(bo.getConstructionCode());
                        createWoThiCongWhenAccessWoKhoiCong(bo, constructionDTO, cntContractDTO);
                    }
                }
                // Huy-end

                //kienkh-start 31-10-2022
                if(Constant.WO_TYPE_CODE.BGBTS_VHKT.equalsIgnoreCase(woDto.getWoTypeCode()) && OK.equalsIgnoreCase(state) && CD_OK.equalsIgnoreCase(bo.getState())){
                    bo.setBgbtsResult(null);
                    Long woTypeId = woTypeDAO.getIdByCode("BGBTS_DTHT");
                    WoBO woBO = woDAO.findByWoIdAndWoTypeIdAndStatus(bo.getTrId(),woTypeId,Constant.STATUS.ACTIVE);
                    if(!ObjectUtils.isEmpty(woBO)){
                        woBO.setState(OK);
                        woDAO.update(woBO);
                        woDto = woBO.toDTO();
                        String content = woDAO.getNameAppParam(OK, WO_XL_STATE);
                        logWoWorkLogs(woDto, "1", content, gson.toJson(woBO), loggedInUser);
                    }
//                    truy vấn bảng WO với TR_ID = <tr của WO hiện tại>, WO_TYPE_ID = woTypeId, STATUS = 1
                }
                //kienkh-end 31-10-2022


            } else if (NG.equalsIgnoreCase(state)) {
                bo.setApproveDateReportWo(null);

                //Nếu từ chối wo thi công ở chi tiết wo DBHT
                if (woDto.getWoParentId() != null) {
                    bo.setState("NG");
                    woDAO.updatePercentWiConstrNG(bo.toDTO());
                    woDAO.updateWoMappingStateById(woId, "NEW");
                    WoBO woParent = woDAO.getOneRaw(woDto.getWoParentId());
                    woParent.setState("NG");
                    woDAO.update(woParent);
                    logWoWorkLogs(woParent.toDTO(), "1", logContent, gson.toJson(woParent), loggedInUser);
                } else {
                    if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode())) {
                        woDAO.updatePercentConstructionNG(bo.toDTO());
                        // tryDeleteHshc(bo); //Huypq-31052021-comment không xóa wo HSHC khi từ chối wo
                        // Thi công
                    }
                    //kienkh-start 31-10-2022
                    if(Constant.WO_TYPE_CODE.BGBTS_VHKT.equalsIgnoreCase(woDto.getWoTypeCode()) && NG.equalsIgnoreCase(state) && CD_OK.equalsIgnoreCase(bo.getState())) {
                        bo.setBgbtsResult(null);
                        bo.setUserCdApproveWo(woDto.getLoggedInUser());
                        bo.setUpdateCdApproveWo(new Date());
                        Long woTypeId = woTypeDAO.getIdByCode("BGBTS_DTHT");
                        WoBO woBO = woDAO.findByWoIdAndWoTypeIdAndStatus(bo.getTrId(),woTypeId,Constant.STATUS.ACTIVE);
//                        SysUser sysUser = sysGroupDAO.
                        if(!ObjectUtils.isEmpty(woBO)){
                            woBO.setState(CD_NG);
                            woBO.setBgbtsResult(woDto.getBgbtsResult());
                            woBO.setUserCdApproveWo(woDto.getLoggedInUser());
                            woBO.setUpdateCdApproveWo(new Date());
                            woDAO.update(woBO);
                            WoDTO woDTO = woBO.toDTO();
                            woDTO.setListChecklistId(woDto.getListChecklistId());
                            woDTO.setBgbtsResult(woDto.getBgbtsResult());
                            updateCheckListStateNG(woDTO,bo);
//                            woDto = bo.toDTO();
//                            woDto.setText(null);
//                            woDto.setRejectedFtId(null);
//                            String Content = woDAO.getNameAppParam(state, WO_XL_STATE);
//                            if (!StringUtils.isNullOrEmpty(woDto.getText())) {
//                                Content += " - " + woDto.getText();
//                            }
//                            logWoWorkLogs(woDto, "1", Content, gson.toJson(bo), loggedInUser);
                        }
                    }
                    //kienkh-end 31-10-2022
                    }
            }

            if (org.apache.commons.lang3.StringUtils.isNotBlank(state)) {
                bo.setState(state);
            }

            // Neu TC TCT tu choi HCQT thi se chuyen thanh NG
            if (TC_TCT_REJECTED.equalsIgnoreCase(state)) {
                if ("HCQT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    bo.setState(NG);
                }
            }

            // Neu la wo UCTT, trang thai CD duyet OK thi dong luon
            if ("UCTT".equalsIgnoreCase(woDto.getWoTypeCode()) && CD_OK.equalsIgnoreCase(state)) {
                bo.setState("OK");
            }

            // if (OK.equalsIgnoreCase(bo.getState())) {
            // WoDTO newDto = bo.toDTO();
            // newDto.setWoTypeCode(woDto.getWoTypeCode());
            // if ("HCQT".equalsIgnoreCase(woDto.getWoTypeCode())) {
            // WoSimpleSysUserDTO user = trDAO.getSysUser(loggedInUser);
            // woDAO.updateWoCheckListOK(bo.getMoneyValue(), user.getSysUserId(),
            // user.getFullName(), bo.getWoId());
            // bo.setChecklistStep(5L);
            // }
            // //hoàn thành nghiệp vụ liên quan đến công trình
            // woDAO.updatePercentConstruction(newDto);
            // }

            // TTHT khi duyệt HSHC có thể sửa giá trị sản lượng
            if (WAIT_TC_BRANCH.equalsIgnoreCase(state) || OK.equalsIgnoreCase(state)) {
                if ("HSHC".equalsIgnoreCase(woDto.getWoTypeCode()) && woDto.getMoneyValue() != null
                        && !(bo.getApWorkSrc() == 4l && bo.getTrId() != null)) {
                    bo.setMoneyValue(woDto.getMoneyValue() * 1000000);
                    //trung-start
                    // thay doi trang thai thanh toan wo
                    bo.setPmtStatus(woDto.getPmtStatus());
                    //trung-end
                }
            }

            if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode())) {
                // hủy hiệu lực sản lượng đã ghi nhận (theo ngày / tháng)
                if (NG.equalsIgnoreCase(bo.getState()) || CD_NG.equalsIgnoreCase(bo.getState())) {
                    tryRejectAllRelatedQuantity(bo.getWoId());
                    if (bo.getCatConstructionTypeId() != null && bo.getCatConstructionTypeId() == 8) {
                        bo.setMoneyValue(0d);
                    }
                }

                // hủy trạng thái đã thực hiện của công trình và hạng mục nếu CD level 1 duyệt
                // NG
                if (NG.equalsIgnoreCase(bo.getState())) {
                    woDAO.tryUncompleteWorkItem(bo);
                    woDAO.tryUncompleteConstruction(bo.getConstructionId());
                }
                // Huypq-30072021 duyệt OK có sửa giá trị
                if (OK.equalsIgnoreCase(state)
                        && (bo.getCatConstructionTypeId() == null || (bo.getCatConstructionTypeId() != null
                        && bo.getCatConstructionTypeId() != 2l && bo.getCatConstructionTypeId() != 3l))) {
                    bo.setMoneyValue(woDto.getMoneyValue() * 1000000);
                    woDAO.tryCompleteWorkItem(bo);
                }
                // Huypq-end
            }

            // nếu điều phối duyệt CD_OK wo 5s thì thành OK luôn
            if ("5S".equalsIgnoreCase(woDto.getWoTypeCode()) && "CD_OK".equalsIgnoreCase(state)) {
                bo.setState(OK);
                logContent = woDAO.getNameAppParam(OK, WO_XL_STATE);
            }

            if ("DOANHTHU_DTHT".equalsIgnoreCase(woDto.getWoTypeCode()) && "CD_OK".equalsIgnoreCase(state)) {
                woDAO.updateWoMappingStateById(bo.getWoId(), DONE);
            }

            if ("DOANHTHU_DTHT".equalsIgnoreCase(woDto.getWoTypeCode()) && "NG".equalsIgnoreCase(state)) {
                woDAO.updateWoMappingStateById(bo.getWoId(), NEW);
            }

            // Huypq-22102021-start WO HSHC trụ ĐTHT
            if ("HSHC".equalsIgnoreCase(woDto.getWoTypeCode()) && bo.getApWorkSrc()!= null && bo.getApWorkSrc() == 4l && bo.getTrId() != null) {
                // Phòng điều hành TTHT duyệt
                if (WAIT_PQT.equalsIgnoreCase(state)) {
                    Calendar cal = Calendar.getInstance();
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    if (day < 4) {
                        Date date = new Date(cal.getTime().getTime() - (day) * 24 * 60 * 60 * 1000);
                        bo.setApproveDateReportWo(date);
                    } else {
                        bo.setApproveDateReportWo(new Date());
                    }
                    bo.setMoneyValue(woDto.getMoneyValue() * 1000000);
                    bo.setUpdateTthtApproveWo(new Date());
                    bo.setUserTthtApproveWo(loggedInUser);
                    bo.setState(OK);
                    bo.setStateHtct(WAIT_PQT);
                }

                // Phòng quyết toán duyệt wo
                if (RECEIVED_PQT.equalsIgnoreCase(state)) {
//					bo.setMoneyValueHtct(woDto.getMoneyValueHtct() * 1000000);
                    bo.setUpdateTcBranchApproveWo(new Date());
                    bo.setUserTcBranchApproveWo(loggedInUser);
//					bo.setState(RECEIVED_PQT);
                    bo.setState(OK);
                    bo.setStateHtct(RECEIVED_PQT);
                }

                if (PQT_NG.equalsIgnoreCase(state)) {
                    bo.setUpdateTcBranchApproveWo(new Date());
                    bo.setUserTcBranchApproveWo(loggedInUser);
                    bo.setState(OK);
                    bo.setStateHtct(PQT_NG);
                }

                if (WAIT_TTDTHT.equalsIgnoreCase(state)) {
                    bo.setUpdateTcBranchApproveWo(new Date());
                    bo.setUserTcBranchApproveWo(loggedInUser);
                    bo.setMoneyValueHtct(woDto.getMoneyValueHtct() * 1000000);
//					bo.setState(WAIT_TTDTHT);
                    bo.setState(OK);
                    bo.setStateHtct(WAIT_TTDTHT);
                }

                // TT ĐTHT duyệt WO
                if (RECEIVED_TTDTHT.equalsIgnoreCase(state)) {
                    bo.setUpdateDthtApprovedWo(new Date());
                    bo.setUserDthtApprovedWo(loggedInUser);
//					bo.setMoneyValueHtct(woDto.getMoneyValueHtct() * 1000000);
//					bo.setState(RECEIVED_TTDTHT);
                    bo.setState(OK);
                    bo.setStateHtct(RECEIVED_TTDTHT);
                }

                if (TTDTHT_NG.equalsIgnoreCase(state)) {
                    bo.setUpdateDthtApprovedWo(new Date());
                    bo.setUserDthtApprovedWo(loggedInUser);
                    bo.setState(OK);
                    bo.setStateHtct(TTDTHT_NG);
                }

                if (WAIT_TC_TCT.equalsIgnoreCase(state)) {
                    bo.setUpdateDthtApprovedWo(new Date());
                    bo.setUserDthtApprovedWo(loggedInUser);
                    bo.setMoneyValueHtct(woDto.getMoneyValueHtct() * 1000000);
//					bo.setState(WAIT_TC_TCT);
                    bo.setStateHtct(WAIT_TC_TCT);
                    bo.setState(OK);
                    bo.setEmailTcTct(woDto.getEmailTcTct());
                }

                if (NG.equalsIgnoreCase(state)) {
                    bo.setMoneyValue(woDto.getMoneyValue() * 1000000);
                    bo.setState(NG);
                }
            }
            // Huy-end
            //Huypq-02112021-start
            if (bo.getOpinionType() != null) {
                if (CD_PAUSE.equalsIgnoreCase(state)) {
                    bo.setState(CD_PAUSE);
                    bo.setUserCdApproveWo(woDto.getLoggedInUser());
                    bo.setUpdateCdApproveWo(new Date());
                }
                if (TTHT_PAUSE.equalsIgnoreCase(state)) {
                    bo.setState(TTHT_PAUSE);
                    bo.setUserTthtApproveWo(woDto.getLoggedInUser());
                    bo.setUpdateTthtApproveWo(new Date());
                    if (bo.getOpinionType().equalsIgnoreCase("Đề xuất gia hạn")) {
                        bo.setEndTime(woDto.getFinishDate());
                    }
                    if (bo.getOpinionType().equalsIgnoreCase("Đề xuất hủy")) {
                        bo.setMoneyValueHtct(woDto.getMoneyValue());
                    }
                }
                if (DTHT_PAUSE.equalsIgnoreCase(state)) {
                    bo.setOpinionResult(ACCEPTED);
                    if (bo.getOpinionType().equalsIgnoreCase("Đề xuất gia hạn")) {
                        bo.setFinishDate(woDto.getFinishDate());
                        bo.setState(DTHT_PAUSE);
                        bo.setUserDthtApprovedWo(woDto.getLoggedInUser());
                        bo.setUpdateDthtApprovedWo(new Date());
                    }

                    if (bo.getOpinionType().equalsIgnoreCase("Đề xuất hủy")) {
                        bo.setState(OK);
                        bo.setUserDthtApprovedWo(woDto.getLoggedInUser());
                        bo.setUpdateDthtApprovedWo(new Date());
                        bo.setMoneyValue(woDto.getMoneyValue() * 1000000);
                        List<WoBO> lstWo = woDAO.getWoByConsId(bo);
                        for (WoBO wod : lstWo) {
                            wod.setState(OK);
                            wod.setMoneyValue(0d);
                            wod.setUserDthtApprovedWo(woDto.getLoggedInUser());
                            wod.setUpdateDthtApprovedWo(new Date());
                            woDAO.updateObject(wod);
                            String content = "Hệ thống cập nhật hoàn thành, TT ĐTHT duyệt WO đề xuất hủy";
                            logWoWorkLogs(wod.toDTO(), "1", content, gson.toJson(wod), loggedInUser);
                        }

                        Long resul = woDAO.updatePercentConsWorkItem(bo);

                        // Nếu ct hoàn thành thì sinh wo hshc, doanh thu
                        if (resul == 2l && !woDto.getCatWorkItemTypeName().equalsIgnoreCase("Khởi công trạm HTCT")) {
                            WoDTO woDtoSearch = new WoDTO();
                            woDtoSearch.setTrId(bo.getTrId());
                            List<WoDTO> lstWosOfTr = new ArrayList<>();
                            for (WoDTO iWo : woDAO.doSearch(woDtoSearch, null, false, false, null)) {
                                String woOrder = iWo.getWoOrder();
                                if (!StringUtils.isNullOrEmpty(woOrder) && Long.parseLong(woOrder) > 1
                                        && Long.parseLong(woOrder) <= 7) {
                                    lstWosOfTr.add(iWo);
                                }
                            }

                            WoDTO woDetail = woDAO.getOneDetails(bo.getWoId());
                            // Tu dong sinh wo HSHC
                            woDetail.setUserCreated(loggedInUser);
                            Long woHshcId = tryCreateHshc(woDetail, lstWosOfTr);

                            // Tu dong sinh wo Doanh thu
                            //Luồng hủy wo không sinh wo Doanh thu và phát sóng
//							Long checkDoanhThuDTHTWoId = woDAO
//									.getDoanhThuDTHTWoByConstruction(woDetail.getConstructionId());
//							if (checkDoanhThuDTHTWoId == null) {
//								Double completeValue = woDAO.getHshcMoneyValueByConstructionId(bo.getConstructionId());
//								createNewDoanhThuWO(woDetail, completeValue);
//							}
//
//							ConstructionDTO constructionDTO = new ConstructionDTO();
//							constructionDTO.setConstructionId(bo.getConstructionId());
//							constructionDTO.setCode(bo.getConstructionCode());
//							createWoByParOrder(bo, constructionDTO, cntContractDTO, "7");
                        }
                    }
                }
                if (CD_PAUSE_REJECT.equalsIgnoreCase(state)) {
                    bo.setState(CD_PAUSE_REJECT);
                    bo.setOpinionResult(REJECTED);
                    bo.setUserCdApproveWo(woDto.getLoggedInUser());
                    bo.setUpdateCdApproveWo(new Date());
                }
                if (TTHT_PAUSE_REJECT.equalsIgnoreCase(state)) {
                    bo.setState(TTHT_PAUSE_REJECT);
                    bo.setUserTthtApproveWo(woDto.getLoggedInUser());
                    bo.setUpdateTthtApproveWo(new Date());
                }
                if (DTHT_PAUSE_REJECT.equalsIgnoreCase(state)) {
                    bo.setOpinionResult(REJECTED);
                    bo.setState(DTHT_PAUSE_REJECT);
                    bo.setUserDthtApprovedWo(woDto.getLoggedInUser());
                    bo.setUpdateDthtApprovedWo(new Date());
                }
            }

            if (OK.equalsIgnoreCase(state) && "DBHT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                bo.setState("OK");
                woDAO.updatePercentWoMappingWiHtct(bo, 5l);
                bo.setUserTthtApproveWo(loggedInUser);
                bo.setUpdateTthtApproveWo(new Date());
                bo.setApproveDateReportWo(new Date());
                //Update thông tin wo thi công khi hoàn thành wo DBHT
                List<WoBO> lstWoThiCong = woDAO.getWoThiCongCompleteByConsId(bo.getConstructionId());
                for (WoBO woThiCong : lstWoThiCong) {
                    woThiCong.setState("OK");
                    woThiCong.setUserTthtApproveWo(loggedInUser);
                    woThiCong.setUpdateTthtApproveWo(new Date());
//					woThiCong.setApproveDateReportWo(new Date()); // taotq bo update ApproveDateReportWo khi ghi nhan san luong
                    woDAO.update(woThiCong);
                    logWoWorkLogs(woThiCong.toDTO(), "1", logContent + " khi đóng WO đồng bộ hạ tầng", gson.toJson(woThiCong), loggedInUser);
                }

                WoDTO woDtoSearch = new WoDTO();
                woDtoSearch.setTrId(bo.getTrId());
                List<WoDTO> lstWosOfTr = new ArrayList<>();
                for (WoDTO iWo : woDAO.doSearch(woDtoSearch, null, false, false, null)) {
                    String woOrder = iWo.getWoOrder();
                    if (!StringUtils.isNullOrEmpty(woOrder) && Long.parseLong(woOrder) > 1
                            && Long.parseLong(woOrder) <= 7) {
                        lstWosOfTr.add(iWo);
                    }
                }

                //Lấy cdlv2 theo trạm
                List<WoTrMappingStationDTO> lstTrMapStations = woTrMappingStationDAO.getStationsOfTr(bo.getTrId());
                WoSimpleSysGroupDTO sysGroupDTO = woDAO.getSysGroup(lstTrMapStations.get(0).getSysGroupId().toString());

                WoDTO woDetail = woDAO.getOneDetails(bo.getWoId());
                // Tu dong sinh wo HSHC
                woDetail.setUserCreated(loggedInUser);
                woDetail.setCdLevel2(lstTrMapStations.get(0).getSysGroupId().toString());
                woDetail.setCdLevel2Name(sysGroupDTO.getGroupName());
                Long woHshcId = tryCreateHshc(woDetail, lstWosOfTr);

                // Tu dong sinh wo Doanh thu
                Long checkDoanhThuDTHTWoId = woDAO
                        .getDoanhThuDTHTWoByConstruction(woDetail.getConstructionId());
                if (checkDoanhThuDTHTWoId == null) {
                    Double completeValue = woDAO
                            .getHshcMoneyValueByConstructionId(bo.getConstructionId());
                    createNewDoanhThuWO(woDetail, completeValue);
                }

                ConstructionDTO constructionDTO = new ConstructionDTO();
                constructionDTO.setConstructionId(bo.getConstructionId());
                constructionDTO.setCode(bo.getConstructionCode());
                createWoByParOrder(bo, constructionDTO, cntContractDTO, "7");
                createWoBts(bo, constructionDTO, cntContractDTO, loggedInUser);
            }

            //Huy-end
            if(bo.getMoneyValue()!=null && bo.getMoneyValue() > 10000000000d) {
                woDto.setCustomField("Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!");
                return false;
            }

            if(Constant.WO_TYPE_CODE.BGBTS_DTHT.equalsIgnoreCase(woDto.getWoTypeCode()) && CD_NG.equalsIgnoreCase(state)){
                bo.setBgbtsResult(woDto.getBgbtsResult());
                updateCheckListStateNG(woDto,bo);
                woDAO.update(bo);
            }

            bo.setInvoicePeriod(invoicePeriod);
            bo.setStationRevenueDate(stationRevenueDate);
            woDAO.update(bo);
            woDto = bo.toDTO();
            woDto.setText(commentText);
            woDto.setRejectedFtId(rejectedFtId);
            logWoWorkLogs(woDto, "1", logContent, gson.toJson(bo), loggedInUser);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void updateCheckListStateNG(WoDTO woDto, WoBO bo) {
        List<WoMappingChecklistBO> woMappingChecklistBOList = woMappingChecklistDAO.findByWoIdEntity(woDto.getWoId());
        if(Constant.BGBTS_RESULT.MISSING_PHOTO.equals(woDto.getBgbtsResult())) {
            if (!CollectionUtils.isEmpty(woDto.getListChecklistId())) {
                if (!CollectionUtils.isEmpty(woMappingChecklistBOList)) {
                    for (WoMappingChecklistBO woMappingChecklistBO : woMappingChecklistBOList) {
                        if (woDto.getListChecklistId().contains(woMappingChecklistBO.getCheckListId())) {
                            woMappingChecklistBO.setState(NEW);
                            woMappingChecklistDAO.updateObject(woMappingChecklistBO);
                        }
                    }
                }
            }
        }else {
            updateWoMappingCheckList(woDto.getWoId());
        }
    }


    private void tryAcceptAllMonthlyQuantity(WoDTO dto) {
        WoSimpleSysUserDTO user = trDAO.getSysUser(dto.getLoggedInUser());
        WoTaskMonthlyDTO searchObj = new WoTaskMonthlyDTO();
        searchObj.setWoId(dto.getWoId());
        List<WoTaskMonthlyDTO> listTaskMonthly = woTaskMonthlyDAO.doSearch(searchObj);

        // chuyển confirm 1 và ghi vết người chấp thuận
        Double totalValue = 0d;
        for (WoTaskMonthlyDTO item : listTaskMonthly) {
            totalValue += item.getQuantity();
            if (!"1".equalsIgnoreCase(item.getConfirm())) {
                item.setConfirm("1");
                item.setApproveBy(dto.getLoggedInUser());
                item.setApproveDate(new Date());
                item.setConfirmUserId(user.getSysUserId());
                woTaskMonthlyDAO.updateObject(item.toModel());
            }
        }

        // thay đổi số tiền đã ghi nhận vào checklist
        WoMappingChecklistDTO searchObj2 = new WoMappingChecklistDTO();
        searchObj2.setWoId(dto.getWoId());
        List<WoMappingChecklistDTO> listMappingChecklist = woMappingChecklistDAO.doSearch(searchObj2);
        if (listMappingChecklist.size() == 0)
            return;
        WoMappingChecklistDTO mainItem = listMappingChecklist.get(0);
        mainItem.setQuantityLength(totalValue);
        woMappingChecklistDAO.updateObject(mainItem.toModel());
    }

    private void tryDeleteHshc(WoBO inputWo) {
        Long checkHshcWoId = woDAO.getHshcWoByConstruction(inputWo.getConstructionId());
        if (checkHshcWoId == null)
            return;

        WoBO hshcWo = woDAO.getOneRaw(checkHshcWoId);
        if (OK.equalsIgnoreCase(hshcWo.getState()))
            return;

        hshcWo.setStatus(0l);
        woDAO.updateObject(hshcWo);
    }

    private Long tryCreateHshc(WoDTO inputWo, List<WoDTO> lstWoTcs) throws Exception {
        Long woId = 0l;
        try {
            if (!"TR_DONG_BO_HA_TANG".equalsIgnoreCase(inputWo.getTrTypeCode())
                    && !"TR_THUE_MAT_BANG_TRAM".equalsIgnoreCase(inputWo.getTrTypeCode())) {
                if (!checkConstructionDone(inputWo)) {
                    return null;
                }
                if (!isContractCanCreateHSHC(inputWo.getContractId())) {
                    return null;
                }
            }

            if ("TR_THUE_MAT_BANG_TRAM".equalsIgnoreCase(inputWo.getTrTypeCode())) {
                if (!checkContractCanCreateHSHC(inputWo.getContractId())) {
                    return null;
                }
            }

            // nếu đã có hshc return
            Long checkHshcWoId = woDAO.getHshcWoByConstruction(inputWo.getConstructionId());
            if (checkHshcWoId != null)
                return checkHshcWoId;

            Integer defaultQuotaTime = 24 * 30; // 30 days in hours

            // get quota time config from app_parram
            List<WoAppParamDTO> hshcQuotaTimeParam = woDAO.getAppParam("HSHC_QUOTA_TIME");
            if (hshcQuotaTimeParam.size() > 0) {
                defaultQuotaTime = Integer.parseInt(hshcQuotaTimeParam.get(0).getCode());
            }

            // get hshc wo type
            long woTypeId = 3l;
            WoTypeDTO searchObj = new WoTypeDTO();
            searchObj.setWoTypeCode("HSHC");
            List<WoTypeDTO> searchResult = woTypeDAO.doSearch(searchObj);
            if (searchResult == null || searchResult.size() == 0)
                return null;
            else {
                woTypeId = searchResult.get(0).getWoTypeId();
            }

            // calculate finish date
            Integer quotaInDays = Math.round(defaultQuotaTime / 24);
            Date createdDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(createdDate);
            c.add(Calendar.DATE, quotaInDays);
            Date finishDate = c.getTime();

            // tạo wo
            WoBO hshcWo = new WoBO();
            hshcWo.setWoId(0l);
            hshcWo.setWoName("Hồ sơ hoàn công");
            hshcWo.setWoTypeId(woTypeId);
            hshcWo.setTrId(inputWo.getTrId());
            hshcWo.setTrCode(inputWo.getTrCode());
            hshcWo.setState(ASSIGN_CD);

            hshcWo.setStationCode(inputWo.getStationCode());
            hshcWo.setCreatedDate(createdDate);
            hshcWo.setFinishDate(finishDate);
            hshcWo.setQoutaTime(defaultQuotaTime);
//			taotq start 270142022 chuyển CDLV1 về ĐTHT
            if (inputWo.getApWorkSrc() == 4) {
                hshcWo.setCdLevel1("166677");
                hshcWo.setCdLevel1Name("TTDTHT - Trung tâm đầu tư hạ tầng");
            } else {
                hshcWo.setCdLevel1(inputWo.getCdLevel1());
                hshcWo.setCdLevel1Name(inputWo.getCdLevel1Name());
            }
//			taotq end 270142022
            hshcWo.setCdLevel2(inputWo.getCdLevel2());
            hshcWo.setCdLevel2Name(inputWo.getCdLevel2Name());

            hshcWo.setStatus(1l);

            Double moneyValue = woDAO.getHshcMoneyValueByConstructionId(inputWo.getConstructionId());
            hshcWo.setMoneyValue(moneyValue);

            hshcWo.setApConstructionType(inputWo.getApConstructionType());
            hshcWo.setApWorkSrc(inputWo.getApWorkSrc());

            hshcWo.setConstructionId(inputWo.getConstructionId());
            hshcWo.setConstructionCode(inputWo.getConstructionCode());
            hshcWo.setCatConstructionTypeId(inputWo.getCatConstructionTypeId());
            hshcWo.setContractId(inputWo.getContractId());
            hshcWo.setContractCode(inputWo.getContractCode());
            hshcWo.setProjectId(inputWo.getProjectId());
            hshcWo.setProjectCode(inputWo.getProjectCode());

            String woCode = this.generateWoCode(hshcWo.toDTO());
            hshcWo.setWoCode(woCode);
            Long totalMonthPlanId = finishDate.getMonth() + 1l;
            hshcWo.setTotalMonthPlanId(totalMonthPlanId);
            hshcWo.setUserCreated(inputWo.getUserCreated());

            woId = woDAO.saveObject(hshcWo);

            // tạo checklist
            List<WoAppParamDTO> checklistItems = woDAO.getAppParam("HSHC_CHECKLIST");
            createChecklistByAppParam(woId, checklistItems);

            String content = "Tự động tạo WO Hồ sơ hoàn công do công trình đã hoàn thành. ";
            if (lstWoTcs != null) {
                Double totalMoneyValue = 0.0;
                // Insert table WO_MAPPING_HSHC_TC
                List<WoMappingHshcTcBO> lstMappings = new ArrayList<>();
                for (WoDTO iWoTc : lstWoTcs) {
                    WoMappingHshcTcBO woMappingHshcTcBO = new WoMappingHshcTcBO();
                    woMappingHshcTcBO.setWoHshcId(woId);
                    woMappingHshcTcBO.setWoTcId(iWoTc.getWoId());
                    woMappingHshcTcBO.setStatus(1l);
                    woMappingHshcTcBO.setCatStationHouseId(iWoTc.getCatStationHouseId());
                    woMappingHshcTcBO.setContractCode(iWoTc.getContractCode());
                    lstMappings.add(woMappingHshcTcBO);

                    // San luong
                    if (iWoTc.getMoneyValue() == null) {
                        iWoTc.setMoneyValue(0d);
                    }
                    totalMoneyValue += iWoTc.getMoneyValue();
                    hshcWo.setConstructionId(null);
                    hshcWo.setConstructionCode(null);
                }
                hshcWo.setMoneyValue(totalMoneyValue);
                woMappingHshcTcDAO.saveList(lstMappings);
                content = "Tự động tạo WO Hồ sơ hoàn công do các wo thi công của nhà trạm đã hoàn thành. ";
            }

            WoDTO woDto = hshcWo.toDTO();
            if (woDto.getApWorkSrc() != null && woDto.getApWorkSrc().compareTo(4l) == 0) {
                woDto.setIsCreateNew(true);
            }
            Gson gson = new Gson();
            logWoWorkLogs(woDto, "1", content, gson.toJson(woDto), inputWo.getLoggedInUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return woId;
    }

    private boolean isContractCanCreateHSHC(Long contractId) {
        if (contractId == null)
            return false;
        return woDAO.isContractCanCreateHSHC(contractId);
    }

    private void createChecklistByAppParam(Long woId, List<WoAppParamDTO> checklistItems) {
        for (WoAppParamDTO item : checklistItems) {
            WoMappingChecklistBO checklistItem = new WoMappingChecklistBO();
            checklistItem.setId(0l);
            checklistItem.setWoId(woId);
            checklistItem.setCheckListId(item.getParOrder());
            checklistItem.setStatus(1l);
            checklistItem.setState("NEW");
            checklistItem.setName(item.getName());
            woMappingChecklistDAO.saveObject(checklistItem);
        }
    }

    private boolean isAbleToCreateHshc(WoDTO dto) {
        WoDTO searchObj = new WoDTO();
        searchObj.setWoTypeCode("HSHC");
        searchObj.setConstructionCode(dto.getConstructionCode());
        List<WoDTO> searchResult = woDAO.doSearch(searchObj, new ArrayList<>(), false, false, new ArrayList<>());
        if (searchResult.size() == 0)
            return true;
        return false;
    }

    private void tryRejectAllRelatedQuantity(Long woId) {
        // reset sản lượng đã ghi nhận
        WoMappingChecklistDTO searchObj = new WoMappingChecklistDTO();
        searchObj.setWoId(woId);
        List<WoMappingChecklistDTO> listChecklist = woMappingChecklistDAO.doSearch(searchObj);
        for (WoMappingChecklistDTO item : listChecklist) {
            if (item.getQuantityByDate() != null && "1".equalsIgnoreCase(item.getQuantityByDate())
                    || "2".equalsIgnoreCase(item.getQuantityByDate())) {
                item.setQuantityLength(null);
            }
            if (item.getQuantityByDate() == null || "1".equalsIgnoreCase(item.getQuantityByDate())) {
                item.setState("NEW");
            }
            woMappingChecklistDAO.updateObject(item.toModel());
        }

        // reset sản lượng theo ngày
        woTaskDailyDAO.tryUnconfirmAllQuantity(woId);
        // reset sản lượng theo tháng
        woTaskMonthlyDAO.tryUnconfirmAllQuantity(woId);
        // reset sản lượng xây dựng dân dụng
        woXdddChecklistDAO.tryUnconfirmAllQuantity(woId);
    }

    private void recordAcceptPerson(WoBO bo, String loggedInUser, Date changeStateTime) {
        if (bo.getCdLevel5() != null) {
            bo.setUserCdLevel5ReceiveWo(loggedInUser);
            bo.setUpdateCdLevel5ReceiveWo(changeStateTime);
        } else if (bo.getCdLevel4() != null) {
            bo.setUserCdLevel4ReceiveWo(loggedInUser);
            bo.setUpdateCdLevel4ReceiveWo(changeStateTime);
        } else if (bo.getCdLevel3() != null) {
            bo.setUserCdLevel3ReceiveWo(loggedInUser);
            bo.setUpdateCdLevel3ReceiveWo(changeStateTime);
        } else if (bo.getCdLevel2() != null) {
            bo.setUserCdLevel2ReceiveWo(loggedInUser);
            bo.setUpdateCdLevel2ReceiveWo(changeStateTime);
        }
    }

    @Override
    public BaseResponseOBJ changeWoStateWrapper(WoDTO woDto, String state) {
        BaseResponseOBJ resp;

        if (woDto.getWoId() <= 0 || woDto.getLoggedInUser() == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        boolean result = changeWoState(woDto, state);
        if (result) {
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } else
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, woDto);

        return resp;
    }

    @Transactional
    @Override
    public void logWoWorkLogs(WoDTO dto, String logType, String content, String contentDetail, String loggedInUser)
            throws Exception {
        WoWorkLogsBO workLogs = new WoWorkLogsBO();
        workLogs.setWoId(dto.getWoId());
        workLogs.setContent(content);
        workLogs.setContentDetail(contentDetail);
        workLogs.setLogTime(new Date());
        workLogs.setLogType(logType);
        workLogs.setStatus(1);

        String loggedInUserStr = "Hệ thống";
        if (!StringUtils.isNullOrEmpty(loggedInUser)) {
            WoSimpleSysUserDTO sysUser = trDAO.getSysUser(loggedInUser);
            WoSimpleSysGroupDTO sysGroup = trDAO.getSysUserGroup(loggedInUser);
            if (sysUser != null) {
                loggedInUserStr = sysUser.getFullName() + " - " + sysUser.getEmployeeCode() + " - ";
            }
            if (sysGroup.getGroupLevel() == 1)
                loggedInUserStr += sysGroup.getGroupNameLevel1();
            if (sysGroup.getGroupLevel() == 2)
                loggedInUserStr += sysGroup.getGroupNameLevel2();
            if (sysGroup.getGroupLevel() == 3)
                loggedInUserStr += sysGroup.getGroupNameLevel3();
        }
        workLogs.setUserCreated(loggedInUserStr);

        if ("UCTT".equalsIgnoreCase(dto.getWoTypeCode()) && !StringUtils.isNullOrEmpty(dto.getCustomField())) {
            WoWorkLogsBO workLogsUctt = workLogs;
            workLogsUctt.setContent(dto.getCustomField());
            woWorkLogsDAO.saveObject(workLogsUctt);
        }

        // Write worklogs
        woWorkLogsDAO.saveObject(workLogs);

        try {
            sendMocha(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMocha(WoDTO dto) {
        if (true == dto.isDontSendMocha())
            return;

        String woState = dto.getState();
        String content = "";
        String woCode = dto.getWoCode();
        String constructionCode = dto.getConstructionCode();
        String catWorkName = "";
        WoTypeBO woType = woTypeDAO.getOneRaw(dto.getWoTypeId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formatedFinishDate = dateFormat.format(dto.getFinishDate());
        if ("THICONG".equalsIgnoreCase((woType.getWoTypeCode()))) {
            if (dto.getApWorkSrc() != XDDD_AP_WORK_SRC) {
                WoCatWorkItemTypeDTO catWork = woDAO.getCatWorkTypeById(dto.getCatWorkItemTypeId());
                if (catWork != null)
                    catWorkName = catWork.getName();
            } else {
                catWorkName = "Xây dựng dân dụng";
            }
        }
        // lấy sysgroup được giao
        if (ASSIGN_CD.equalsIgnoreCase(woState) || REJECT_FT.equalsIgnoreCase(woState)) {
            String receiveGroupId = getLowestCdLevel(dto);

            if (ASSIGN_CD.equalsIgnoreCase(woState)) {
                content = "Đồng chí được giao điều phối WO: " + woCode + ".";
                if (!StringUtils.isNullOrEmpty(constructionCode))
                    content += " Công trình " + constructionCode + ".";
                if ("THICONG".equalsIgnoreCase((woType.getWoTypeCode())))
                    content += " Hạng mục " + catWorkName + ".";
                content += " Hạn hoàn thành: " + formatedFinishDate + "!";
            }

            if (REJECT_FT.equalsIgnoreCase(woState) && dto.getRejectedFtId() != null) {
                WoSimpleFtDTO ft = woDAO.getFtById(dto.getRejectedFtId());
                content = "Nhân viên " + ft.getEmployeeCode() + "-" + ft.getFullName() + " đã từ chối WO " + woCode;
                if (!StringUtils.isNullOrEmpty(constructionCode))
                    content += " của công trình " + constructionCode;
                if ("THICONG".equalsIgnoreCase((woType.getWoTypeCode())))
                    content += " với hạng mục " + catWorkName;
                content += " với lý do " + dto.getText();
            }
            if (StringUtils.isNullOrEmpty(dto.getCdLevel5())) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(receiveGroupId));
            } else {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, Long.parseLong(dto.getCdLevel5()), null);
            }
        }

        // gửi msg cho ft
        if (ASSIGN_FT.equalsIgnoreCase(woState) || CD_NG.equalsIgnoreCase(woState)) {
            if (ASSIGN_FT.equalsIgnoreCase(woState) && StringUtils.isNullOrEmpty(dto.getCustomField())) {
                content = "Đồng chí được giao thực hiện WO: " + woCode + ".";
                if (!StringUtils.isNullOrEmpty(constructionCode))
                    content += " Công trình " + constructionCode + ".";
                if ("THICONG".equalsIgnoreCase((woType.getWoTypeCode())))
                    content += " Hạng mục " + catWorkName + ".";
                content += " Hạn hoàn thành: " + formatedFinishDate + "!";
            }

            if (CD_NG.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " được điều phối đánh giá là chưa hoàn thành tốt cần thực hiện lại!";
            }

            woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, dto.getFtId(), null);
        }

        // xin ý kiến cd4
        if (OPINION_RQ_4.equalsIgnoreCase(woState))
            if (OPINION_RQ_4.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " có ý kiến cần xin điều phối cấp 4.";
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(dto.getCdLevel4()));
            }

        // xin ý kiến cd3
        if (OPINION_RQ_3.equalsIgnoreCase(woState))
            if (OPINION_RQ_3.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " có ý kiến cần xin điều phối cấp 3.";
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(dto.getCdLevel3()));
            }

        // xin ý kiến cd2
        if (OPINION_RQ_2.equalsIgnoreCase(woState))
            if (OPINION_RQ_2.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " có ý kiến cần xin điều phối cấp 2.";
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(dto.getCdLevel2()));
            }

        // gửi msg cho cd1 (nếu group level = 2 thì gửi cho các phòng ban)
        if ((OPINION_RQ_1.equalsIgnoreCase(woState) || CD_OK.equalsIgnoreCase(woState))
                && StringUtils.isNullOrEmpty(dto.getCdLevel5())) {
            if (OPINION_RQ_1.equalsIgnoreCase(woState))
                content = "WO " + woCode + " có ý kiến cần xin điều phối cấp 2.";

            if (CD_OK.equalsIgnoreCase(woState)) {

                // lấy tên cnkt tỉnh từ phòng hạ tầng tỉnh
                String cdLv2 = dto.getCdLevel2();
                String cnktName = "";
                WoSimpleSysGroupDTO cnkt = woDAO.getParentGroup(cdLv2);
                if (cnkt != null)
                    cnktName = cnkt.getGroupName();

                content = "WO " + woCode + " đã được điều phối " + cnktName + " duyệt OK.";
            }

            // ở dưới báo vướng HCQT
            if (HCQT_PROBLEM.equalsIgnoreCase(dto.getCustomField())) {
                content = "WO hoàn công quyết toán " + woCode + " báo vướng. Mời xem chi tiết trên hệ thống! ";
            }

            WoSimpleSysGroupDTO cd1Group = woDAO.getSysGroup(dto.getCdLevel1());

            if (cd1Group.getGroupLevel() > 2) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, cd1Group.getSysGroupId());
            } else {
                List<WoSimpleSysGroupDTO> childrenGroup = woDAO.getChildrenGroup(dto.getCdLevel1());
                for (WoSimpleSysGroupDTO group : childrenGroup) {
                    woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, group.getSysGroupId());
                }
            }
        }

        // ft hoàn thành
        if (DONE.equalsIgnoreCase(woState) && DAILY_QUANTITY_ACCEPTED.equalsIgnoreCase(dto.getCustomField()) == false
                && "HCQT".equalsIgnoreCase(dto.getWoTypeCode()) == false) {
            WoSimpleFtDTO ft = woDAO.getFtById(dto.getFtId());
            content = "Nhân viên " + ft.getEmployeeCode() + " " + ft.getFullName() + " đã hoàn thành WO " + woCode;
            if (!StringUtils.isNullOrEmpty(constructionCode))
                content += " của công trình " + constructionCode;
            if ("THICONG".equalsIgnoreCase((woType.getWoTypeCode())))
                content += " với hạng mục " + catWorkName;
            content += ". Yêu cầu đồng chí vào xác nhận! ";
            if (!StringUtils.isNullOrEmpty(dto.getCdLevel5())) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, Long.parseLong(dto.getCdLevel5()), null);
            }
            if (!StringUtils.isNullOrEmpty(dto.getCdLevel4())) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(dto.getCdLevel4()));
            }

            if (!StringUtils.isNullOrEmpty(dto.getCdLevel3())) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(dto.getCdLevel3()));
            }

            if (!StringUtils.isNullOrEmpty(dto.getCdLevel2())) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(dto.getCdLevel2()));
            }
        }

        // ghi nhận sản lượng theo ngày
        if (DONE.equalsIgnoreCase(woState) && DAILY_QUANTITY_ACCEPTED.equalsIgnoreCase(dto.getCustomField()) == true) {
            content = "Sản lượng theo ngày của WO " + woCode + " đã được ghi nhận!";

            woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, dto.getFtId(), null);
        }

        // cd1 từ chối cả wo
        // cd level1 từ chối cả wo
        if (NG.equalsIgnoreCase(woState)) {
            content = "WO " + woCode + " được điều phối cấp 1 đánh giá là chưa hoàn thành tốt cần thực hiện lại.";
            if (!StringUtils.isNullOrEmpty(dto.getCdLevel5())) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, Long.parseLong(dto.getCdLevel5()), null);
            }
            if (!StringUtils.isNullOrEmpty(dto.getCdLevel4())) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(dto.getCdLevel4()));
            }

            if (!StringUtils.isNullOrEmpty(dto.getCdLevel3())) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(dto.getCdLevel3()));
            }

            if (!StringUtils.isNullOrEmpty(dto.getCdLevel2()))
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, Long.parseLong(dto.getCdLevel2()));
            woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, dto.getFtId(), null);
        }

        if ("HSHC".equalsIgnoreCase(woType.getWoTypeCode())) {
            if (WAIT_TC_TCT.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " đã được Tài chính Trụ duyệt";
            }

            if (OK.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " đã được Tài chính TCT duyệt";
            }

            WoSimpleSysGroupDTO cd1Group = woDAO.getSysGroup(dto.getCdLevel1());
            List<WoSimpleSysGroupDTO> childrenGroup = woDAO.getChildrenGroup(dto.getCdLevel1());
            for (WoSimpleSysGroupDTO group : childrenGroup) {
                woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, null, group.getSysGroupId());
            }
        }

        if (CD_PAUSE.equalsIgnoreCase(woState) || CD_PAUSE_REJECT.equalsIgnoreCase(woState) || TTHT_PAUSE.equalsIgnoreCase(woState) || TTHT_PAUSE_REJECT.equalsIgnoreCase(woState)
                || DTHT_PAUSE.equalsIgnoreCase(woState) || DTHT_PAUSE_REJECT.equalsIgnoreCase(woState)) {
            if (CD_PAUSE.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " được điều phối duyệt đề xuất gia hạn.";
            }
            if (CD_PAUSE_REJECT.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " được điều phối từ chối đề xuất gia hạn.";
            }
            if (TTHT_PAUSE.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " được TTHT duyệt đề xuất gia hạn.";
            }
            if (TTHT_PAUSE_REJECT.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " được TTHT từ chối đề xuất gia hạn.";
            }
            if (DTHT_PAUSE.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " được TT ĐTHT duyệt đề xuất gia hạn.";
            }
            if (DTHT_PAUSE_REJECT.equalsIgnoreCase(woState)) {
                content = "WO " + woCode + " được TT ĐTHT từ chối đề xuất gia hạn.";
            }

            woDAO.createSendSmsEmailForMobile(content, WO_XL_MSG_SUBJECT, dto.getFtId(), null);
        }

        if (dto.getIsCreateNew() != null && dto.getIsCreateNew()) {
            WoSimpleSysGroupDTO cd1Group = woDAO.getSysGroup(dto.getCdLevel1());

            if (cd1Group.getGroupLevel() > 2) {
                woDAO.createSendEmailForDTHT(content, WO_XL_MSG_SUBJECT, null, cd1Group.getSysGroupId());
            } else {
                List<WoSimpleSysGroupDTO> childrenGroup = woDAO.getChildrenGroup(dto.getCdLevel1());
                for (WoSimpleSysGroupDTO group : childrenGroup) {
                    woDAO.createSendEmailForDTHT(content, WO_XL_MSG_SUBJECT, null, group.getSysGroupId());
                }
            }
        }
    }

    private String getLowestCdLevel(WoDTO dto) {
        String lowestCdLevel = dto.getCdLevel4() != null ? dto.getCdLevel4()
                : dto.getCdLevel3() != null ? dto.getCdLevel3()
                : dto.getCdLevel2() != null ? dto.getCdLevel2()
                : dto.getCdLevel1() != null ? dto.getCdLevel1() : null;

        return lowestCdLevel;
    }

    @Override
    public List<WoWorkLogsBO> getListWorklogsOfWo(Long woId) {
        return woWorkLogsDAO.doSearch(woId);
    }

    @Override
    public void mappingChecklistToWO(long catWorkItemTypeId, long woId, Long catConsTypeId, Long constructionId) {
        List<WoMappingChecklistBO> checkList;

        if (GPON_TYPE == catConsTypeId) {
            // is GPON
            checkList = woMappingChecklistDAO.getCheckListForCreateWo(catWorkItemTypeId, woId, true, constructionId);
        } else {
            checkList = woMappingChecklistDAO.getCheckListForCreateWo(catWorkItemTypeId, woId, false, null);
        }
        woMappingChecklistDAO.saveListNoId(checkList);
    }

    @Override
    public void mappingChecklistAIOToWO(long contractId, long woId) {
        List<WoMappingChecklistBO> checkList = woMappingChecklistDAO.getAIOChecklistItemByContract(contractId, woId);
        woMappingChecklistDAO.saveListNoId(checkList);
    }

    @Override
    public String generateWoCode(WoDTO woDto) {
        String code = "VNM_";
        String apWorkSrcCode = String.valueOf(woDto.getApWorkSrc());

        WoDTO apWorkSrc = woDAO.getAppParam(apWorkSrcCode, AP_WORK_SRC);

        String apWorkSrcName = "";
        if (apWorkSrc != null)
            apWorkSrcName = apWorkSrc.getApWorkSrcName();

        Long ftId = woDto.getFtId();

        String ftWoCode = "2";

        if (ftId != null)
            ftWoCode = "1";

        String prefix = apWorkSrcName.split("-")[0].trim();

        String software = "PMXL";
        if ("AIO".equalsIgnoreCase(woDto.getWoTypeCode()))
            software = "AIO";

        Long woTypeId = woDto.getWoTypeId();

        // Long nextSq = woDAO.getNextValSequence("WO_SEQ");
        Long nextSq = woDAO.getNextSeqVal();
        Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);

        if (prefix != "")
            code += prefix + "_";

        code += software + "_" + ftWoCode + "_" + woTypeId + "_" + countIdType + "_" + nextSq;
        return code;
    }

    public DataListDTO doSearchBaoCaoChamDiemKpi(ReportWoDTO obj) {
        List<RpWoDetailOsDTO> ls = woDAO.doSearchBaoCaoChamDiemKpi(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public String exportFileWoKpi(ReportWoDTO obj) throws Exception {
        obj.setPage(null);
        obj.setPageSize(null);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_BC_ChamDiemWO_KPI.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Export_BC_ChamDiemWO_KPI.xlsx");
        List<RpWoDetailOsDTO> data = woDAO.exportFileWoKpi(obj);
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            // HuyPQ-22/08/2018-start
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            // HuyPQ-end
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            int i = 3;
            for (RpWoDetailOsDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 3));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getProCode() != null) ? dto.getProCode() : "");
                cell.setCellStyle(style);
                // Tổng điểm
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getDiemDatTong() != null) ? dto.getDiemDatTong() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getDiemThuongTong() != null) ? dto.getDiemThuongTong() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getTongDiem() != null) ? dto.getTongDiem() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getQuyDoiDiem() != null) ? dto.getQuyDoiDiem() : 0d);
                cell.setCellStyle(style);
                // Quỹ lương
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getSalaryMonth() != null) ? dto.getSalaryMonth() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getSalaryWo() != null) ? dto.getSalaryWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getSalaryPercent() != null) ? dto.getSalaryPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getSalaryDat() != null) ? dto.getSalaryDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getSalaryThuong() != null) ? dto.getSalaryThuong() : 0d);
                cell.setCellStyle(style);
                // HSHC xây lắp
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getHshcXayLapKH() != null) ? dto.getHshcXayLapKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getHshcXayLapWo() != null) ? dto.getHshcXayLapWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue((dto.getHshcXayLapPercent() != null) ? dto.getHshcXayLapPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(15, CellType.STRING);
                cell.setCellValue((dto.getHshcXayLapDat() != null) ? dto.getHshcXayLapDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(16, CellType.STRING);
                cell.setCellValue((dto.getHshcXayLapThuong() != null) ? dto.getHshcXayLapThuong() : 0d);
                cell.setCellStyle(style);
                // Doanh thu nguồn CP
                cell = row.createCell(17, CellType.STRING);
                cell.setCellValue((dto.getQtKH() != null) ? dto.getQtKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(18, CellType.STRING);
                cell.setCellValue((dto.getQtWo() != null) ? dto.getQtWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(19, CellType.STRING);
                cell.setCellValue((dto.getQtPercent() != null) ? dto.getQtPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(20, CellType.STRING);
                cell.setCellValue((dto.getQtDat() != null) ? dto.getQtDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(21, CellType.STRING);
                cell.setCellValue((dto.getQtThuong() != null) ? dto.getQtThuong() : 0d);
                cell.setCellStyle(style);
                // DOANH THU NTĐ (XDDD)
                cell = row.createCell(22, CellType.STRING);
                cell.setCellValue((dto.getQtGPDNKH() != null) ? dto.getQtGPDNKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(23, CellType.STRING);
                cell.setCellValue((dto.getQtGPDNWo() != null) ? dto.getQtGPDNWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(24, CellType.STRING);
                cell.setCellValue((dto.getQtGPDNPercent() != null) ? dto.getQtGPDNPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(25, CellType.STRING);
                cell.setCellValue((dto.getQtGPDNDat() != null) ? dto.getQtGPDNDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(26, CellType.STRING);
                cell.setCellValue((dto.getQtGPDNThuong() != null) ? dto.getQtGPDNThuong() : 0d);
                cell.setCellStyle(style);
                // SẢN LƯỢNG XÂY LẮP
                cell = row.createCell(27, CellType.STRING);
                cell.setCellValue((dto.getQtSLXLKH() != null) ? dto.getQtSLXLKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(28, CellType.STRING);
                cell.setCellValue((dto.getQtSLXLWo() != null) ? dto.getQtSLXLWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(29, CellType.STRING);
                cell.setCellValue((dto.getQtSLXLPercent() != null) ? dto.getQtSLXLPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(30, CellType.STRING);
                cell.setCellValue((dto.getQtSLXLDat() != null) ? dto.getQtSLXLDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(31, CellType.STRING);
                cell.setCellValue((dto.getQtSLXLThuong() != null) ? dto.getQtSLXLThuong() : 0d);
                cell.setCellStyle(style);
                // Sản lượng nguồn chi phí
                cell = row.createCell(32, CellType.STRING);
                cell.setCellValue((dto.getSlKH() != null) ? dto.getSlKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(33, CellType.STRING);
                cell.setCellValue((dto.getSlWo() != null) ? dto.getSlWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(34, CellType.STRING);
                cell.setCellValue((dto.getSlPercent() != null) ? dto.getSlPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(35, CellType.STRING);
                cell.setCellValue((dto.getSlDat() != null) ? dto.getSlDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(36, CellType.STRING);
                cell.setCellValue((dto.getSlThuong() != null) ? dto.getSlThuong() : 0d);
                cell.setCellStyle(style);
                // SẢN LƯỢNG NTĐ (XDDD)
                cell = row.createCell(37, CellType.STRING);
                cell.setCellValue((dto.getSlGPDNKH() != null) ? dto.getSlGPDNKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(38, CellType.STRING);
                cell.setCellValue((dto.getSlGPDNWo() != null) ? dto.getSlGPDNWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(39, CellType.STRING);
                cell.setCellValue((dto.getSlGPDNPercent() != null) ? dto.getSlGPDNPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(40, CellType.STRING);
                cell.setCellValue((dto.getSlGPDNDat() != null) ? dto.getSlGPDNDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(41, CellType.STRING);
                cell.setCellValue((dto.getSlGPDNThuong() != null) ? dto.getSlGPDNThuong() : 0d);
                cell.setCellStyle(style);
                // Tìm kiếm việc XDDD
                cell = row.createCell(42, CellType.STRING);
                cell.setCellValue((dto.getTkvKH() != null) ? dto.getTkvKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(43, CellType.STRING);
                cell.setCellValue((dto.getTkvWo() != null) ? dto.getTkvWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(44, CellType.STRING);
                cell.setCellValue((dto.getTkvPercent() != null) ? dto.getTkvPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(45, CellType.STRING);
                cell.setCellValue((dto.getTkvDat() != null) ? dto.getTkvDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(46, CellType.STRING);
                cell.setCellValue((dto.getTkvThuong() != null) ? dto.getTkvThuong() : 0d);
                cell.setCellStyle(style);
                // Thu hồi dòng tiền
                cell = row.createCell(47, CellType.STRING);
                cell.setCellValue((dto.getThdtKH() != null) ? dto.getThdtKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(48, CellType.STRING);
                cell.setCellValue((dto.getThdtWo() != null) ? dto.getThdtWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(49, CellType.STRING);
                cell.setCellValue((dto.getThdtPercent() != null) ? dto.getThdtPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(50, CellType.STRING);
                cell.setCellValue((dto.getThdtDat() != null) ? dto.getThdtDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(51, CellType.STRING);
                cell.setCellValue((dto.getThdtThuong() != null) ? dto.getThdtThuong() : 0d);
                cell.setCellStyle(style);
                // Tổng triển khai trạm HTCT
                cell = row.createCell(52, CellType.STRING);
                cell.setCellValue((dto.getTtkKH() != null) ? dto.getTtkKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(53, CellType.STRING);
                cell.setCellValue((dto.getTtkWo() != null) ? dto.getTtkWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(54, CellType.STRING);
                cell.setCellValue((dto.getTtkPercent() != null) ? dto.getTtkPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(55, CellType.STRING);
                cell.setCellValue((dto.getTtkDat() != null) ? dto.getTtkDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(56, CellType.STRING);
                cell.setCellValue((dto.getTtkThuong() != null) ? dto.getTtkThuong() : 0d);
                cell.setCellStyle(style);
                // Xd móng
                cell = row.createCell(57, CellType.STRING);
                cell.setCellValue((dto.getXdXMKH() != null) ? dto.getXdXMKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(58, CellType.STRING);
                cell.setCellValue((dto.getXdXMWo() != null) ? dto.getXdXMWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(59, CellType.STRING);
                cell.setCellValue((dto.getXdXMPercent() != null) ? dto.getXdXMPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(60, CellType.STRING);
                cell.setCellValue((dto.getXdXMDat() != null) ? dto.getXdXMDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(61, CellType.STRING);
                cell.setCellValue((dto.getXdXMThuong() != null) ? dto.getXdXMThuong() : 0d);
                cell.setCellStyle(style);
                // Xong đb trạm
                cell = row.createCell(62, CellType.STRING);
                cell.setCellValue((dto.getxDBKH() != null) ? dto.getxDBKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(63, CellType.STRING);
                cell.setCellValue((dto.getxDBWo() != null) ? dto.getxDBWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(64, CellType.STRING);
                cell.setCellValue((dto.getxDBPercent() != null) ? dto.getxDBPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(65, CellType.STRING);
                cell.setCellValue((dto.getxDBDat() != null) ? dto.getxDBDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(66, CellType.STRING);
                cell.setCellValue((dto.getxDBThuong() != null) ? dto.getxDBThuong() : 0d);
                cell.setCellStyle(style);
                //
                cell = row.createCell(67, CellType.STRING);
                cell.setCellValue((dto.getTmbKH() != null) ? dto.getTmbKH() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(68, CellType.STRING);
                cell.setCellValue((dto.getTmbWo() != null) ? dto.getTmbWo() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(69, CellType.STRING);
                cell.setCellValue((dto.getTmbPercent() != null) ? dto.getTmbPercent() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(70, CellType.STRING);
                cell.setCellValue((dto.getTmbDat() != null) ? dto.getTmbDat() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(71, CellType.STRING);
                cell.setCellValue((dto.getTmbThuong() != null) ? dto.getTmbThuong() : 0d);
                cell.setCellStyle(style);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Export_BC_ChamDiemWO_KPI.xlsx");
        return path;
    }

    // kiem tra truoc khi cap nhat cong trinh
    public void checkUpdateConstruction(Long constructionId) {
        Double statusWorkItem = woDAO.checkStatusWo(constructionId);
        if (statusWorkItem.compareTo(0d) == 0) {
            woDAO.updateConstructionComplete(constructionId);
        }
    }

    @Transactional
    public String createTRContractAIO(AIOWoTrDTO aioWoTrDTO) {
        String resut = "";
        String trCode = "";
        Gson gson = new Gson();
        try {

            WoTypeDTO woTypeDTO = woDAO.getWoTypeAio("AIO");
            WoAppParamDTO groupCreated = woDAO.getCodeAppParam("TR_CREATOR", "2");
            SysUserCOMSDTO sysUser = woDAO.getUserInfoById(aioWoTrDTO.getUserCreated());
            Long trTypeId = woDAO.getAIOWoTRTypeId();
            String serviceCode = woDAO.getConfigFromAioConfigService(aioWoTrDTO.getServiceCode());
            // giao cho nhan viên
            if (aioWoTrDTO.getType().equals("1")) {
                String name = "";
                String codeWoTR = woDAO.getCodeAIOWOTR("1");
                // String cdLevel1 = woDAO.getCodeAppParam("CD_LEVEL_1","2").getCode();
                trCode = codeWoTR + "_" + aioWoTrDTO.getContractId();
                WoTrDTO woTrDTO = new WoTrDTO();
                woTrDTO.setTrCode(trCode);
                woTrDTO.setTrName(TR_NAME);
                woTrDTO.setContractCode(aioWoTrDTO.getContractCode());
                woTrDTO.setContractId(aioWoTrDTO.getContractId());
                woTrDTO.setTrTypeId(trTypeId);
                woTrDTO.setUserCreated(sysUser.getLoginName());
                woTrDTO.setCreatedDate(new Date());
                woTrDTO.setStartDate(aioWoTrDTO.getStartDate());
                woTrDTO.setFinishDate(aioWoTrDTO.getEndDate());
                woTrDTO.setQuantityValue(aioWoTrDTO.getContractAmount());
                woTrDTO.setCustomerType(aioWoTrDTO.getCustomerType());
                woTrDTO.setContractType(aioWoTrDTO.getContractType());
                woTrDTO.setQoutaTime(aioWoTrDTO.getQoutaTime());
                woTrDTO.setState(ASSIGN_FT);
                woTrDTO.setStatus(1);
                woTrDTO.setGroupCreated(groupCreated.getCode());
                woTrDTO.setGroupCreatedName(groupCreated.getDescription());
                Long idtr = trDAO.saveObject(woTrDTO.toModel());
                woTrDTO.setTrId(idtr);
                String contentr = "Trạng thái Tr: " + woDAO.getNameAppParam(woTrDTO.getState(), WO_TR_XL_STATE);
                woTrBusinessImpl.logTrWorkLogs(woTrDTO, "1", contentr, gson.toJson(woTrDTO), sysUser.getLoginName(),
                        false);
                WoDTO wo = new WoDTO();

                String codeWO = woDAO.getCodeAIOWO("1");
                if (aioWoTrDTO.getPerformerId() != null) {
                    SysUserCOMSDTO user = woDAO.getUserInfoById(aioWoTrDTO.getPerformerId());
                    if (user != null) {
                        WoDTO aioWoTrDTO2 = woDAO.getCDLevel(user.getSysGroupId());
                        if (aioWoTrDTO2 != null) {
                            wo.setCdLevel2(aioWoTrDTO2.getCdLevel2());
                            wo.setCdLevel2Name(aioWoTrDTO2.getCdLevel2Name());
                            wo.setCdLevel3(aioWoTrDTO2.getCdLevel3());
                            wo.setCdLevel3Name(aioWoTrDTO2.getCdLevel3Name());
                        }
                        wo.setCdLevel4(user.getSysGroupId().toString());
                        wo.setCdLevel4Name(user.getSysGroupName());
                        wo.setFtId(aioWoTrDTO.getPerformerId());
                        wo.setFtName(user.getFullName());
                        wo.setFtEmail(user.getEmail());
                        name = user.getFullName() + "-" + user.getEmail();
                    }
                }
                wo.setWoCode(codeWO + "_" + aioWoTrDTO.getContractId());
                wo.setWoTypeId(woTypeDTO.getWoTypeId());
                wo.setState(ASSIGN_FT);
                wo.setApWorkSrc(8l);
                WoNameDTO woName = woDAO.getAIOWoName(woTypeDTO.getWoTypeId());
                wo.setWoNameId(woName.getId());
                wo.setWoName(woName.getName());
                wo.setTrCode(trCode);
                // wo.setCdLevel1(cdLevel1);
                wo.setMoneyValue(aioWoTrDTO.getContractAmount());
                wo.setTrId(idtr);
                wo.setFinishDate(aioWoTrDTO.getEndDate());
                wo.setCreatedDate(new Date());
                wo.setStatus(1l);
                wo.setContractId(aioWoTrDTO.getContractId());
                wo.setUserCreated(sysUser.getLoginName());
                wo.setQoutaTime(aioWoTrDTO.getQoutaTime());
                wo.setContractCode(aioWoTrDTO.getContractCode());
                wo.setCreatedUserEmail(sysUser.getEmail());
                wo.setCreatedUserFullName(sysUser.getFullName());
                // Huypq-09012021-start
                if (serviceCode != null && serviceCode.equals("2")) {
                    wo.setCdLevel1(null);
                    wo.setCdLevel1Name(null);
                }
                // Huy-end
                Long woID = woDAO.saveObject(wo.toModel());
                List<AIOWoTrDetailDTO> aioWoTrDetailDTO = aioWoTrDTO.getAioWoTrDetailDTO();
                List<WoMappingChecklistBO> listwoMappingCheck = new ArrayList<>();
                for (AIOWoTrDetailDTO detailDTOX : aioWoTrDetailDTO) {
                    WoMappingChecklistDTO woMappingChecklist = new WoMappingChecklistDTO();
                    woMappingChecklist.setStatus(detailDTOX.getStatus());
                    woMappingChecklist.setWoId(woID);
                    woMappingChecklist.setChecklistId(detailDTOX.getContractDetailId());
                    woMappingChecklist.setState("NEW");
                    woMappingChecklist.setStatus(1l);
                    listwoMappingCheck.add(woMappingChecklist.toModel());
                }
                woMappingChecklistDAO.saveList(listwoMappingCheck);
                wo.setWoId(woID);
                String content = "Giao cho" + name + "Trạng thái WO: "
                        + woDAO.getNameAppParam(wo.getState(), WO_XL_STATE);
                logWoWorkLogs(wo, "1", content, gson.toJson(wo), sysUser.getLoginName());
            }
            // giao cho ttht
            if (aioWoTrDTO.getType().equals("2")) {
                String codeWoTR = woDAO.getCodeAIOWOTR("2");
                String cdLevel1 = woDAO.getCodeAppParam("CD_LEVEL_1", "1").getCode();
                WoSimpleSysGroupDTO cd1 = trDAO.getSysGroupById(Long.parseLong(cdLevel1));
                trCode = codeWoTR + "_" + aioWoTrDTO.getContractId();
                WoTrDTO woTrDTO = new WoTrDTO();
                woTrDTO.setTrCode(trCode);
                woTrDTO.setTrName(TR_NAME);
                woTrDTO.setContractCode(aioWoTrDTO.getContractCode());
                woTrDTO.setContractId(aioWoTrDTO.getContractId());
                // Huypq-09012021-start
                if (serviceCode != null && !serviceCode.equals("2")) {
                    if (aioWoTrDTO.getFieldAio() != null && aioWoTrDTO.getFieldAio().equals("TT.CNTT")) {
                        woTrDTO.setCdLevel1("280501");
                        woTrDTO.setCdLevel1Name("Trung tâm Công nghệ thông tin");
                    } else {
                        woTrDTO.setCdLevel1(cdLevel1);
                        if (cd1 != null) {
                            woTrDTO.setCdLevel1Name(cd1.getGroupName());
                        }
                    }
                } else {
                    woTrDTO.setCdLevel1(null);
                    woTrDTO.setCdLevel1Name(null);
                }
                // Huy-end
                woTrDTO.setTrTypeId(trTypeId);
                woTrDTO.setCreatedDate(new Date());
                woTrDTO.setStartDate(aioWoTrDTO.getStartDate());
                woTrDTO.setFinishDate(aioWoTrDTO.getEndDate());
                woTrDTO.setUserCreated(sysUser.getLoginName());
                // Huypq-28122020-start
                if (aioWoTrDTO.getCustomerType() == 1l || aioWoTrDTO.getCustomerType() == 2l) {
                    woTrDTO.setState(PROCESSING);
                } else {
                    woTrDTO.setState(ASSIGN_CD);
                }
                woTrDTO.setUserReceiveTr("1");
                woTrDTO.setUpdateReceiveTr(new Date());
                woTrDTO.setStatus(1);
                woTrDTO.setQuantityValue(aioWoTrDTO.getContractAmount());
                woTrDTO.setCustomerType(aioWoTrDTO.getCustomerType());
                woTrDTO.setContractType(aioWoTrDTO.getContractType());
                woTrDTO.setQoutaTime(aioWoTrDTO.getQoutaTime());
                woTrDTO.setGroupCreated(groupCreated.getCode());
                woTrDTO.setGroupCreatedName(groupCreated.getDescription());
                Long idtr = trDAO.saveObject(woTrDTO.toModel());
                woTrDTO.setTrId(idtr);
                String contentr = "Giao cho" + cd1.getGroupName() + "Trạng thái Tr: "
                        + woDAO.getNameAppParam(woTrDTO.getState(), WO_TR_XL_STATE);
                woTrBusinessImpl.logTrWorkLogs(woTrDTO, "1", contentr, gson.toJson(woTrDTO), sysUser.getLoginName(),
                        false);

                // Huypq-28122020-start
                if (aioWoTrDTO.getCustomerType() == 1l || aioWoTrDTO.getCustomerType() == 2l) {
                    String name = "";
                    WoDTO wo = new WoDTO();
                    String codeWO = woDAO.getCodeAIOWO("1");
                    // if (sysUser.getSysUserId() != null) {
                    // SysUserCOMSDTO user = woDAO.getUserInfoById(sysUser.getSysUserId());
                    // if (user != null) {
                    Long provinceId = woDAO.getProvinceByAreaId(aioWoTrDTO.getProvinceId());
                    WoDTO aioWoTrDTO2 = woDAO.getCdLevel2FromProvinceId(provinceId);
                    if (aioWoTrDTO2 != null) {
                        wo.setCdLevel2(aioWoTrDTO2.getCdLevel2());
                        wo.setCdLevel2Name(aioWoTrDTO2.getCdLevel2Name());
                        name = aioWoTrDTO2.getCdLevel2Name();
                    }

                    // }
                    // }
                    wo.setWoCode(codeWO + "_" + aioWoTrDTO.getContractId());
                    wo.setWoTypeId(woTypeDTO.getWoTypeId());
                    wo.setState(ASSIGN_CD);
                    wo.setApWorkSrc(8l);
                    WoNameDTO woName = woDAO.getAIOWoName(woTypeDTO.getWoTypeId());
                    wo.setWoNameId(woName.getId());
                    wo.setWoName(woName.getName());
                    wo.setTrCode(trCode);
                    // Huypq-09012021-start
                    if (serviceCode != null && !serviceCode.equals("2")) {
                        if (aioWoTrDTO.getFieldAio() != null && aioWoTrDTO.getFieldAio().equals("TT.CNTT")) {
                            wo.setCdLevel1("280501");
                            wo.setCdLevel1Name("Trung tâm Công nghệ thông tin");
                        } else {
                            wo.setCdLevel1(cdLevel1);
                            if (cd1 != null) {
                                wo.setCdLevel1Name(cd1.getGroupName());
                            }
                        }
                    } else {
                        wo.setCdLevel1(null);
                        wo.setCdLevel1Name(null);
                    }
                    // Huy-end
                    wo.setMoneyValue(aioWoTrDTO.getContractAmount());
                    wo.setTrId(idtr);
                    wo.setFinishDate(aioWoTrDTO.getEndDate());
                    wo.setCreatedDate(new Date());
                    wo.setStatus(1l);
                    wo.setContractId(aioWoTrDTO.getContractId());
                    wo.setUserCreated(sysUser.getLoginName());
                    wo.setQoutaTime(aioWoTrDTO.getQoutaTime());
                    wo.setContractCode(aioWoTrDTO.getContractCode());
                    wo.setCreatedUserEmail(sysUser.getEmail());
                    wo.setCreatedUserFullName(sysUser.getFullName());

                    Long woID = woDAO.saveObject(wo.toModel());
                    List<AIOWoTrDetailDTO> aioWoTrDetailDTO = aioWoTrDTO.getAioWoTrDetailDTO();
                    List<WoMappingChecklistBO> listwoMappingCheck = new ArrayList<>();
                    for (AIOWoTrDetailDTO detailDTOX : aioWoTrDetailDTO) {
                        WoMappingChecklistDTO woMappingChecklist = new WoMappingChecklistDTO();
                        woMappingChecklist.setStatus(detailDTOX.getStatus());
                        woMappingChecklist.setWoId(woID);
                        woMappingChecklist.setChecklistId(detailDTOX.getContractDetailId());
                        woMappingChecklist.setState("NEW");
                        woMappingChecklist.setStatus(1l);
                        listwoMappingCheck.add(woMappingChecklist.toModel());
                    }
                    woMappingChecklistDAO.saveList(listwoMappingCheck);
                    wo.setWoId(woID);
                    String content = "Giao cho" + name + "Trạng thái WO: "
                            + woDAO.getNameAppParam(wo.getState(), WO_XL_STATE);
                    logWoWorkLogs(wo, "1", content, gson.toJson(wo), sysUser.getLoginName());
                }
                // Huy-end
            }
            resut = "SUCCESS";

        } catch (Exception e) {
            e.printStackTrace();
            resut = "FAIL";
        }
        return resut;

    }

    public String updateTRContractAIO(AIOWoTrDTO aioWoTrDTO) {
        String resut = "";
        Gson gson = new Gson();
        String contentTR = "";
        String contentWo = "";
        Long chekMapping = 1l;
        try {
            if (aioWoTrDTO.getContractId() != null) {
                WoTrDTO woTrDTO = woDAO.getWoTrByConTract(aioWoTrDTO.getContractId());
                SysUserCOMSDTO sysUser = woDAO.getUserInfoById(aioWoTrDTO.getUserCreated());
                if (woTrDTO != null) {
                    WoDTO wo = woDAO.getWoByTR(woTrDTO.getTrId());
                    if (wo != null) {
                        if (aioWoTrDTO.getPerformerId() != null && !aioWoTrDTO.getPerformerId().equals(wo.getFtId())
                                && (aioWoTrDTO.getIsDieuPhoi() != null ? aioWoTrDTO.getIsDieuPhoi() : false)) {
                            SysUserCOMSDTO user = woDAO.getUserInfoById(aioWoTrDTO.getPerformerId());
                            if (user != null && !aioWoTrDTO.getType().equals("0")) {
                                WoDTO aioWoTrDTO2 = woDAO.getCDLevel(user.getSysGroupId());
                                if (aioWoTrDTO2 != null) {
                                    wo.setCdLevel2(aioWoTrDTO2.getCdLevel2());
                                    wo.setCdLevel2Name(aioWoTrDTO2.getCdLevel2Name());
                                    wo.setCdLevel3(aioWoTrDTO2.getCdLevel3());
                                    wo.setCdLevel3Name(aioWoTrDTO2.getCdLevel3Name());
                                }
                                wo.setCdLevel4(user.getSysGroupId().toString());
                                wo.setCdLevel4Name(user.getSysGroupName());
                                wo.setFtId(aioWoTrDTO.getPerformerId());
                                wo.setFtName(user.getFullName());
                                wo.setFtEmail(user.getEmail());
                            }
                        }
                        // NV từ chối
                        if (aioWoTrDTO.getType().equals("0")) {
                            wo.setState(REJECT_FT);
                            wo.setFtId(null);
                            wo.setFtName(null);
                            wo.setFtEmail(null);
                            if (aioWoTrDTO.getContractDetailId() != null) {
                                woDAO.updateWoMappingCheckList(wo.getWoId(), aioWoTrDTO.getContractDetailId(),
                                        REJECT_FT);
                            }
                            contentWo = aioWoTrDTO.getReasonName();
                            woDAO.update(wo.toModel());
                        }

                        // huypq-08012021-start
                        if (aioWoTrDTO.getContractAmount() != null) {
                            wo.setMoneyValue(aioWoTrDTO.getContractAmount());
                        }
                        // Huy-end

                        if (aioWoTrDTO.getType().equals("1")) {
                            if (aioWoTrDTO.getStatus() == 1l) {
                                wo.setState(ASSIGN_FT);
                                if (aioWoTrDTO.getContractAmount() != null) {
                                    wo.setMoneyValue(aioWoTrDTO.getContractAmount());
                                }
                                if (aioWoTrDTO.getEndDate() != null) {
                                    wo.setFinishDate(aioWoTrDTO.getEndDate());
                                }
                            }
                            if (aioWoTrDTO.getStatus() == 2l) {
                                if (aioWoTrDTO.getContractDetailId() != null) {
                                    woDAO.updateWoMappingCheckList(wo.getWoId(), aioWoTrDTO.getContractDetailId(),
                                            PROCESSING);
                                }
                                wo.setState(PROCESSING);
                                if (wo.getAcceptTime() == null) {
                                    Date datew = new Date();
                                    wo.setAcceptTime(datew);
                                    wo.setStartTime(datew);
                                    wo.setUserFtReceiveWo(sysUser.getSysUserId().toString());
                                    wo.setUpdateFtReceiveWo(datew);
                                }
                            }
                            if (aioWoTrDTO.getStatus() == 3l) {
                                if (aioWoTrDTO.getContractDetailId() != null) {
                                    List<AIOWoImageDTO> lstImgs = aioWoTrDTO.getListImage();
                                    if (lstImgs != null && lstImgs.size() > 0) {
                                        int imgIdx = 1;
                                        String dateString = new SimpleDateFormat("yyyymmdd_hhMMss").format(new Date());
                                        for (AIOWoImageDTO iImg : lstImgs) {
                                            WoMappingAttachBO woMappingAttachBO = new WoMappingAttachBO();
                                            woMappingAttachBO.setWoId(wo.getWoId());
                                            woMappingAttachBO.setUserCreated(aioWoTrDTO.getUserCreated().toString());
                                            woMappingAttachBO.setChecklistId(aioWoTrDTO.getContractDetailId());
                                            String fileName = dateString + "_" + imgIdx + ".jpg";
                                            byte[] decodedBytes = com.itextpdf.text.pdf.codec.Base64
                                                    .decode(iImg.getBase64String());
                                            InputStream is = new ByteArrayInputStream(decodedBytes);
                                            String path = null;
                                            try {
                                                // path = UFile.writeToFileServerATTT2(is, fileName, "/" + wo.getWoId()
                                                // + "/" + aioWoTrDTO.getContractDetailId() + "/", folderUpload);
                                                path = UFile.writeToFileServerATTT2(is, fileName, UPLOAD_SUB_FOLDER,
                                                        folderUpload);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            woMappingAttachBO.setFileName(fileName);
                                            woMappingAttachBO.setFilePath(path);
                                            woMappingAttachBO.setStatus(1l);
                                            woMappingAttachBO.setLatitude(iImg.getLatitude());
                                            woMappingAttachBO.setLongtitude(iImg.getLongtitude());
                                            woMappingAttachDAO.save(woMappingAttachBO);
                                            imgIdx++;
                                        }
                                    }
                                    Double ax = woDAO.getValueAcceptane(aioWoTrDTO.getContractId(),
                                            aioWoTrDTO.getContractDetailId());
                                    woDAO.updateWoMappingDone(wo.getWoId(), aioWoTrDTO.getContractDetailId(), DONE, ax);
                                    chekMapping = woDAO.checkWoMapping(wo.getWoId());
                                    if (chekMapping == 0l) {
                                        wo.setEndTime(new Date());
                                        if (wo.getCdLevel1() == null) {
                                            wo.setState(OK);
                                        } else {
                                            wo.setState(DONE);
                                        }
                                    }
                                }
                            }
                            if (aioWoTrDTO.getStatus() == 4l) {
                                wo.setStatus(0l);
                            }
                            if (aioWoTrDTO.getStatus() > 4l) {
                                wo.setState(OPINION_RQ);
                                if (aioWoTrDTO.getContractDetailId() != null) {
                                    woDAO.updateWoMappingCheckList(wo.getWoId(), aioWoTrDTO.getContractDetailId(),
                                            OPINION_RQ);
                                }
                            }
                            woDAO.update(wo.toModel());
                        }
                        // GIAO THTH
                        if (aioWoTrDTO.getType().equals("2")) {
                            // wo
                            if (aioWoTrDTO.getStatus() == 1l) {
                                wo.setCdLevel1(woDAO.getCodeAppParam("CD_LEVEL_1", "1").getCode());
                            }
                            if (aioWoTrDTO.getStatus() == 2l) {
                                if (aioWoTrDTO.getContractDetailId() != null) {
                                    woDAO.updateWoMappingCheckList(wo.getWoId(), aioWoTrDTO.getContractDetailId(),
                                            PROCESSING);
                                }
                                wo.setState(PROCESSING);
                                wo.setAcceptTime(new Date());
                            }
                            if (aioWoTrDTO.getStatus() == 3l) {
                                if (aioWoTrDTO.getContractDetailId() != null) {
                                    woDAO.updateWoMappingCheckList(wo.getWoId(), aioWoTrDTO.getContractDetailId(),
                                            DONE);
                                }
                                if (chekMapping == 0l) {
                                    wo.setState(OK);
                                    wo.setFinishDate(new Date());
                                    wo.setEndTime(new Date());
                                }
                            }
                            if (aioWoTrDTO.getStatus() == 4l) {
                                wo.setStatus(0l);
                            }
                            if (aioWoTrDTO.getStatus() > 4l) {
                                wo.setState(OPINION_RQ);
                                if (aioWoTrDTO.getContractDetailId() != null) {
                                    woDAO.updateWoMappingCheckList(wo.getWoId(), aioWoTrDTO.getContractDetailId(),
                                            OPINION_RQ);
                                }
                            }

                            woDAO.update(wo.toModel());
                        }
                        if (aioWoTrDTO.getAioWoTrDetailDTO() != null && aioWoTrDTO.getContractDetailId() == null) {
                            woDAO.deleteWoMappingChecklist(wo.getWoId());
                            List<AIOWoTrDetailDTO> aioWoTrDetailDTO = aioWoTrDTO.getAioWoTrDetailDTO();
                            List<WoMappingChecklistBO> listwoMappingCheck = new ArrayList<>();
                            for (AIOWoTrDetailDTO detailDTOX : aioWoTrDetailDTO) {
                                WoMappingChecklistDTO woMappingChecklist = new WoMappingChecklistDTO();
                                woMappingChecklist.setStatus(detailDTOX.getStatus());
                                woMappingChecklist.setWoId(wo.getWoId());
                                woMappingChecklist.setChecklistId(detailDTOX.getContractDetailId());
                                if (detailDTOX.getStatus() == 1) {
                                    woMappingChecklist.setState("NEW");
                                }
                                if (detailDTOX.getStatus() == 2) {
                                    woMappingChecklist.setState("PROCESSING");
                                }
                                if (detailDTOX.getStatus() == 3) {
                                    woMappingChecklist.setState("DONE");
                                }
                                woMappingChecklist.setStatus(1l);
                                listwoMappingCheck.add(woMappingChecklist.toModel());
                            }
                            woMappingChecklistDAO.saveList(listwoMappingCheck);
                        }
                        String content = "Trạng thái WO:" + woDAO.getNameAppParam(wo.getState(), WO_XL_STATE);
                        logWoWorkLogs(wo, "1", content + '-' + contentWo, gson.toJson(wo), sysUser.getLoginName());
                    }
                    if (aioWoTrDTO.getContractAmount() != null) {
                        woTrDTO.setQuantityValue(aioWoTrDTO.getContractAmount());
                    }
                    if (aioWoTrDTO.getStartDate() != null) {
                        woTrDTO.setStartDate(aioWoTrDTO.getStartDate());

                    }
                    if (aioWoTrDTO.getEndDate() != null) {
                        woTrDTO.setFinishDate(aioWoTrDTO.getEndDate());
                    }
                    if (aioWoTrDTO.getContractType() != null) {
                        woTrDTO.setContractType(aioWoTrDTO.getContractType());
                    }
                    if (aioWoTrDTO.getQoutaTime() != null) {
                        woTrDTO.setQoutaTime(aioWoTrDTO.getQoutaTime());
                    }
                    if (aioWoTrDTO.getType().equals("1")) {
                        if (aioWoTrDTO.getStatus() == 1l) {
                            woTrDTO.setState(ASSIGN_FT);
                        }
                    }
                    if (aioWoTrDTO.getStatus() == 4l) {
                        woTrDTO.setStatus(0);
                    }
                    // NV từ chối
                    if (aioWoTrDTO.getType().equals("0")) {
                        // wo
                        // woTrDTO.setState(REJECT_FT);
                        contentTR = aioWoTrDTO.getReasonName();
                        // trDAO.update(woTrDTO.toModel());
                    } else {
                        if (aioWoTrDTO.getStatus() == 2l) {
                            woTrDTO.setState(PROCESSING);
                        }
                        if (aioWoTrDTO.getStatus() == 3l) {
                            if (woTrDTO.getCdLevel1() == null) {
                                if (chekMapping == 0l) {
                                    woTrDTO.setState(OK);
                                }
                            } else {
                                if (chekMapping == 0l) {
                                    woTrDTO.setState(DONE);
                                }
                            }
                        }
                    }
                    trDAO.update(woTrDTO.toModel());
                    String contentr = "Trạng thái Tr: " + woDAO.getNameAppParam(woTrDTO.getState(), WO_TR_XL_STATE);
                    woTrBusinessImpl.logTrWorkLogs(woTrDTO, "1", contentr + '-' + contentTR, gson.toJson(woTrDTO),
                            sysUser.getLoginName(), false);

                    resut = "SUCCESS";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resut = "FAIL";
        }
        return resut;

    }

    @Override
    public File createImportWoExcelTemplate(String loggedInUser, List<String> groupIdList, boolean isCnkt,
                                            boolean isRevenueRole, boolean isCreateHcqt) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_CreateWo_3107.xlsx";
        String outFileName = "Template_WO_XL_CreateWo.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                Long sysGroupIdOfUser = woDAO.getGroupLevel2ByUserId(Long.parseLong(loggedInUser)).getSysGroupId();
                String groupCode = woDAO.getSysGroup("" + sysGroupIdOfUser).getCode();
                String woCreatorDomainId = groupIdList.get(0);

                if (!StringUtils.isNullOrEmpty(groupCode)) {
                    // LOẠI WO
                    List<WoTypeDTO> listWoTypeFilterd = new ArrayList<>();
                    if (isRevenueRole) {
                        WoTypeDTO searchObj = new WoTypeDTO();
                        searchObj.setWoTypeCode("DOANHTHU");
                        listWoTypeFilterd.addAll(woTypeDAO.doSearch(searchObj));
                    }

                    if (isCreateHcqt) {
                        WoTypeDTO searchObj = new WoTypeDTO();
                        searchObj.setWoTypeCode("HCQT");
                        listWoTypeFilterd.addAll(woTypeDAO.doSearch(searchObj));
                    }

                    List<WoTypeDTO> listWoType = woTypeDAO.getListWoType();
                    if (groupCode.contains("VHKT")) {
                        listWoTypeFilterd
                                .addAll(listWoType.stream()
                                        .filter(t -> (t.getWoTypeCode().equalsIgnoreCase("XLSC")
                                                || t.getWoTypeCode().equalsIgnoreCase("UCTT")
                                                //duonghv13 add 13122021
                                                || t.getWoTypeCode().equalsIgnoreCase("Codien_HTCT")
                                                || t.getWoTypeCode().equalsIgnoreCase("BDMPD")))
                                        //duonghv13 end 13122021
                                        .collect(Collectors.toList()));
                    } else if (groupCode.contains("P.HT") || groupCode.contains("CNKT")) {
                        listWoTypeFilterd.addAll(listWoType.stream()
                                .filter(t -> (t.getWoTypeCode().equalsIgnoreCase("QUYLUONG")
                                        || t.getWoTypeCode().equalsIgnoreCase("THICONG")
                                        || t.getWoTypeCode().equalsIgnoreCase("HSHC")
                                        || t.getWoTypeCode().equalsIgnoreCase("THDT")
                                        || t.getWoTypeCode().equalsIgnoreCase("TMBTHTTC")))
                                .collect(Collectors.toList()));
                    } else if (groupCode.contains("XDDD")) {
                        listWoTypeFilterd.addAll(listWoType.stream()
                                .filter(t -> (t.getWoTypeCode().equalsIgnoreCase("DOANHTHU")
                                        || t.getWoTypeCode().equalsIgnoreCase("THICONG")
                                        || t.getWoTypeCode().equalsIgnoreCase("HSHC")))
                                .collect(Collectors.toList()));
                    } else if (groupCode.contains("GPTH") || groupCode.contains("ĐTHT")) {
                        listWoTypeFilterd.addAll(
                                listWoType.stream().filter(t -> (t.getWoTypeCode().equalsIgnoreCase("DOANHTHU")))
                                        .collect(Collectors.toList()));
                    } else {
                        listWoTypeFilterd.addAll(listWoType.stream()
                                .filter(t -> (!t.getWoTypeCode().equalsIgnoreCase("XLSC")
                                        && !t.getWoTypeCode().equalsIgnoreCase("BDDK")
                                        && !t.getWoTypeCode().equalsIgnoreCase("UCTT")
                                        && !t.getWoTypeCode().equalsIgnoreCase("DOANHTHU")
                                        && !t.getWoTypeCode().equalsIgnoreCase("HCQT")))
                                .collect(Collectors.toList()));
                    }

                    writeWoType(workbook, listWoTypeFilterd);

                    // NGUỒN VIỆC
                    List<WoAppParamDTO> appWorkSrcs = woDAO.getAppParam("AP_WORK_SRC");
                    List<WoAppParamDTO> lstApWorkSrcs = new ArrayList<>();
                    if (groupCode.contains("TTHT")) { // TTHT tao duoc wo voi tat ca nguon viec
                        writeAppWorkSrc(workbook, appWorkSrcs);
                    } else if (groupCode.contains("P.HT") || groupCode.contains("CNKT")) { // CNKT tao wo voi nguon viec
                        // chi phi va xddd
                        for (WoAppParamDTO iAp : appWorkSrcs) {
                            if (iAp.getName().contains("CP") || iAp.getName().contains("XDDD")) {
                                lstApWorkSrcs.add(iAp);
                            }
                        }
                        writeAppWorkSrc(workbook, lstApWorkSrcs);
                    } else if (groupCode.contains("XDDD")) { // XDDD chi tao wo voi nguon viec ngoai tap doan vao xddd
                        for (WoAppParamDTO iAp : appWorkSrcs) {
                            if (iAp.getName().contains("NTĐ") || iAp.getName().contains("XDDD")) {
                                lstApWorkSrcs.add(iAp);
                            }
                        }
                        writeAppWorkSrc(workbook, lstApWorkSrcs);
                    }
                    // Cac tru khac ko co nguon viec
                }

                // LOẠI CÔNG TRÌNH
                List<WoAppParamDTO> apConsType = woDAO.getAppParam("AP_CONSTRUCTION_TYPE");
                writeConstructionType(workbook, apConsType);

                // TÊN WO
                WoNameDTO nameDto = new WoNameDTO();
                List<WoNameDTO> woNames = woNameDAO.doSearch(nameDto);
                List<WoNameDTO> woNameFiltered = new ArrayList<>();
                if (VHKT_GROUP_ID.equalsIgnoreCase(woCreatorDomainId)) {
                    woNameFiltered = woNames.stream().filter(t -> (t.getWoTypeCode().equalsIgnoreCase("XLSC")
                                    //duonghv13 edit 13122021
                                    || t.getWoTypeCode().equalsIgnoreCase("UCTT")
                                    || t.getWoTypeCode().equalsIgnoreCase("Codien_HTCT")
                                    || t.getWoTypeCode().equalsIgnoreCase("BDMPD")))
                            //duonghv13 end 13122021
                            .collect(Collectors.toList());
                } else {
                    woNameFiltered = woNames.stream()
                            .filter(t -> (!t.getWoTypeCode().equalsIgnoreCase("XLSC")
                                    && !t.getWoTypeCode().equalsIgnoreCase("BDDK")
                                    && !t.getWoTypeCode().equalsIgnoreCase("HCQT")))
                            .collect(Collectors.toList());
                }
                writeWoName(workbook, woNameFiltered);

                // MÃ TR
                WoTrDTO trDTO = new WoTrDTO();
                trDTO.setLoggedInUser(loggedInUser);
                trDTO.setPage((long) 1);
                trDTO.setPageSize(99999);
                List<WoTrDTO> listWoTr = trDAO.doSearch(trDTO, groupIdList, AVAILABLE_TYPE, true, true);
                writeTr(workbook, listWoTr);

                List<WorkItemDetailDTO> workItemLst = constructionBusinessImpl.getCatWorkItemType();
                writeWorkItemList(workbook, workItemLst);

                // KẾ HOẠCH THÁNG
                List<String> monthPlans = Arrays.asList("Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5",
                        "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12");
                writeBookCurrentMonthPlan(workbook, monthPlans);

                // CD LEVEL 2
                List<WoSimpleSysGroupDTO> cdsLv2;
                if (VHKT_GROUP_ID.equalsIgnoreCase(woCreatorDomainId)) {
                    cdsLv2 = woDAO.getCnktCdLevel2();
                } else {
                    if (isCnkt) {
                        Long sysGroupId = Long.parseLong(groupIdList.get(0));
                        WoSimpleSysGroupDTO cdLv2Group = trDAO.getSysGroupById(sysGroupId);
                        cdsLv2 = new ArrayList<>();
                        cdsLv2.add(cdLv2Group);
                    } else
                        cdsLv2 = woDAO.getCds(2, null, null, false);
                }
                writeCdLevel2(workbook, cdsLv2);

                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }

        return outFile;
    }

    private void writeWoType(SXSSFWorkbook workbook, List<WoTypeDTO> listWoType) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(1);
        for (WoTypeDTO record : listWoType) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(record.getWoTypeId());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(record.getWoTypeCode());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(record.getWoTypeName());
            Cell cell3 = row.createCell(3);
            if (record.getHasApWorkSrc() != null)
                cell3.setCellValue("Có");
            Cell cell4 = row.createCell(4);
            if (record.getHasConstruction() != null)
                cell4.setCellValue("Có");
            Cell cell5 = row.createCell(5);
            if (record.getHasWorkItem() != null)
                cell5.setCellValue("Có");
        }
    }

    private void writeAppWorkSrc(SXSSFWorkbook workbook, List<WoAppParamDTO> appWorkSrcs) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(2);
        for (WoAppParamDTO record : appWorkSrcs) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(record.getCode());
            Cell cell1 = row.createCell(1);

            if (!StringUtils.isNullOrEmpty(record.getName())) {
                int ind = record.getName().indexOf('-');
                if (ind == -1)
                    cell1.setCellValue(record.getName());
                else
                    cell1.setCellValue(record.getName().substring(ind + 1).trim());
            }

        }
    }

    private void writeConstructionType(SXSSFWorkbook workbook, List<WoAppParamDTO> apConsType) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(3);
        for (WoAppParamDTO record : apConsType) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(record.getCode());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(record.getName());
        }
    }

    private void writeWoName(SXSSFWorkbook workbook, List<WoNameDTO> woNames) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(4);
        for (WoNameDTO record : woNames) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(record.getId());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(record.getName());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(record.getWoTypeName());
        }
    }

    private void writeTr(SXSSFWorkbook workbook, List<WoTrDTO> trs) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(5);
        for (WoTrDTO record : trs) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(record.getTrId());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(record.getTrCode());
        }
    }

    private void writeWorkItemList(SXSSFWorkbook workbook, List<WorkItemDetailDTO> workItemLst) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(6);
        for (WorkItemDetailDTO item : workItemLst) {
            if (item != null) {
                Row row = getOrCreateRow(sheet, ++rowNum);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(item.getCatConstructionTypeName());
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(item.getName());
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(item.getCatWorkItemTypeId());
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(item.getCatConstructionTypeId());
            }
        }
    }

    private void writeBookCurrentMonthPlan(SXSSFWorkbook workbook, List<String> monthPlans) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(8);
        for (String record : monthPlans) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(record);
        }
    }

    private void writeCdLevel2(SXSSFWorkbook workbook, List<WoSimpleSysGroupDTO> cdLv2s) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(7);
        for (WoSimpleSysGroupDTO record : cdLv2s) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(record.getSysGroupId());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(record.getGroupName());
        }
    }

    private Row getOrCreateRow(Sheet sheet, int rowNum) {
        Row row = sheet.getRow(rowNum);
        if (Objects.isNull(row)) {
            row = sheet.createRow(rowNum);
        }
        return row;
    }

    private boolean checkAIOTrHasWO(Long trId) {
        if (trId == null)
            return false;
        return trDAO.checkAIOTrHasWO(trId);
    }

    @Transactional
    @Override
    public boolean createNewWO(WoDTO woDto, boolean isImporting) throws Exception {
        Gson gson = new Gson();

        if (!isImporting) {
            if (checkSameWo(woDto) || checkConstructionDone(woDto) || checkWorkItemDone(woDto) || !checkStationConstruction(woDto))
                return false;
        }

        // generate wo code
        woDto.setWoCode(generateWoCode(woDto));

        //Huypq-28012022-start
        if (woDto.getStationCode() != null) {
            if (woDto.getWoTypeCode() != null && !"HCQT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                CatStationDTO station = woDAO.checkStationByCode(woDto.getStationCode());
                if (station == null) {
                    woDto.setCustomField("Mã trạm không tồn tại");
                    return false;
                }
            }
        }
        //Huypq-end

        // set assign state if already assigned
        if (woDto.getFtId() != null) {
            woDto.setState(ASSIGN_FT);
            if ("HCQT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                woDto.setState(ACCEPT_FT);
                woDto.setAcceptTime(new Date());
                String userFtReceiveWo = woDto.getFtId().toString();
                woDto.setUserFtReceiveWo(userFtReceiveWo);
                woDto.setUpdateFtReceiveWo(new Date());
            }
        } else if (woDto.getCdLevel2() != null || woDto.getCdLevel3() != null || woDto.getCdLevel5() != null) {
            woDto.setState(ASSIGN_CD);
        } else {
            woDto.setState(UNASSIGN);
            if ("DOANHTHU".equalsIgnoreCase(woDto.getWoTypeCode()))
                woDto.setState(WAIT_TC_TCT);
        }

        // check AIO tr (with customer type = 1) already has a WO
        if (checkAIOTrHasWO(woDto.getTrId())) {
            woDto.setCustomField(AIO_ONLY_1_WO);
            return false;
        }

        // add work item for construction
        if (woDto.getCatWorkItemTypeId() != null && woDto.getConstructionId() != null) {
            //Huypq10122021-start Check hợp đồng chia sẻ doanh thu
            Long contractShare = 0l;
            if (woDto.getContractId() != null) {
                contractShare = catStationDAO.getContractShareRevenueById(woDto.getContractId());
            }
            if (!woDAO.checkExistConstructionWorkItem(woDto.getConstructionId(), woDto.getCatWorkItemTypeId())) {
                if (contractShare.compareTo(1l) == 0) {
                    woDto.setWorkItemBranch(woDto.getCdLevel1());
                }
                woDAO.insertWorkItem(woDto);
            }
            //update trụ hạng mục với hợp đồng chia doanh thu
            else {
                if (contractShare.compareTo(1l) == 0) {
                    catStationDAO.updateBranchWorkItem(woDto.getConstructionId(), woDto.getCatWorkItemTypeId(), woDto.getCdLevel1());
                }

            }
            //Huy-end
        }

        if (woDto.getCdLevel5() != null) {
            woDto.setType("3");
        }

        // thêm thông tin tỉnh theo trạm (dành cho HCQT)
        // if(!StringUtils.isNullOrEmpty(woDto.getStationCode()) &&
        // "HCQT".equalsIgnoreCase(woDto.getWoTypeCode())){
        // WoSimpleStationDTO station = woDAO.getStationByCode(woDto.getStationCode());
        // woDto.setCatProvinceCode(station.getCatProvinceCode());
        // }
        // woDto.setCatProvinceCode(woDto.getCatProvinceCode());

        // nếu wo từ cnkt: type = 1 thì CD level 2 tự động tiếp nhận
        if ("1".equalsIgnoreCase(woDto.getType())) {
            woDto.setState(ACCEPT_CD);
            woDto.setUserCdLevel2ReceiveWo(woDto.getLoggedInUser());
            woDto.setUpdateCdLevel2ReceiveWo(new Date());
        }

        // thêm cd level 3 khi import wo xử lý sự cố
        if ("XLSC".equalsIgnoreCase(woDto.getWoTypeCode())) {
            woDto.setCdLevel3(woDto.getCdLevel2());
            woDto.setCdLevel3Name(woDto.getCdLevel2Name());
        }

        // tự động tính sản lượng HSHC cho import
        if ("HSHC".equalsIgnoreCase(woDto.getWoTypeCode()) && isImporting) {
            Double totalMoneyValue = woDAO.getHshcMoneyValueByConstructionId(woDto.getConstructionId());
            woDto.setMoneyValue(totalMoneyValue);
        }

        // nếu thiếu cd name sẽ điền thêm cd name
        tryGetCdNames(woDto);

        // nếu là wo xddd thì sản lượng ban đầu = 0
        if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode()) && Integer.valueOf(XDDD_AP_WORK_SRC).equals(woDto.getApWorkSrc()))
            woDto.setMoneyValue(0d);

        // create new wo
        woDto.setStatus((long) 1);
        woDto.setCreatedDate(new Date());
        woDto.setChecklistStep(0l);
        Long newWoId = woDAO.saveObject(woDto.toModel());
        woDto.setWoId(newWoId);

        // log creation
        // logWoWorkLogs(woDto, "1", CREATE_NEW_MSG, gson.toJson(woDto),
        // woDto.getLoggedInUser());

        // log assignment if there's any
        String content = "Tạo mới; ";
        if (woDto.getFtId() != null) {
            content += "Giao cho nhân viên: " + woDto.getFtName() + ";";
            if ("HCQT".equalsIgnoreCase(woDto.getWoTypeCode()))
                content += "Trạng thái: " + woDAO.getNameAppParam(ACCEPT_FT, WO_XL_STATE);
            else
                content += "Trạng thái: " + woDAO.getNameAppParam(ASSIGN_FT, WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(woDto), woDto.getLoggedInUser());
        } else if (woDto.getCdLevel2() != null) {
            content += "Giao cho CD: " + woDto.getCdLevel2Name() + ";";
            content += "Trạng thái: " + woDAO.getNameAppParam(woDto.getState(), WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(woDto), woDto.getLoggedInUser());
        } else if (woDto.getCdLevel5() != null) {
            content += "Giao cho CD: " + woDto.getCdLevel5Name() + ";";
            content += "Trạng thái: " + woDAO.getNameAppParam(woDto.getState(), WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(woDto), woDto.getLoggedInUser());
        } else if (PROCESSING.equalsIgnoreCase(woDto.getState())) {
            content += "Trạng thái: " + woDAO.getNameAppParam(PROCESSING, WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(woDto), woDto.getLoggedInUser());
        } else {
            content += "Trạng thái: " + woDAO.getNameAppParam(woDto.getState(), WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(woDto), woDto.getLoggedInUser());
        }

        // create checklist
        tryCreateChecklistForNewWo(woDto);
        woDto.setCustomField("Thành công. ");
        return true;
    }

    @Override
    public void tryCreateChecklistForNewWo(WoDTO woDto) {
        if ("HSHC".equalsIgnoreCase(woDto.getWoTypeCode())) { // tạo checlist cho wo hồ sơ hoàn công
            List<WoAppParamDTO> checklistItems = woDAO.getAppParam("HSHC_CHECKLIST");

            // bỏ đầu việc tài duyệt nếu là hợp đồng nguồn đtư hoặc hđ hạ tầng cho thuê
            // boolean isHdNdt = woDAO.checkContractIsNDT(woDto.getContractId());
            // boolean isHdHtct = woDAO.checkContractIsHTCT(woDto.getContractId());
            // if(isHdHtct || isHdNdt){
            // WoAppParamDTO checklist1 = checklistItems.get(0);
            // checklistItems.clear();
            // checklistItems.add(checklist1);
            // }

            createChecklistByAppParam(woDto.getWoId(), checklistItems);
        } else if ("DOANHTHU".equalsIgnoreCase(woDto.getWoTypeCode())) { // tạo checlist cho wo doanh thu
            List<WoAppParamDTO> checklistItems = woDAO.getAppParam("WO_DOANHTHU_CHECKLIST");
            createChecklistByAppParam(woDto.getWoId(), checklistItems);
        } else if (woDto.getCatWorkItemTypeId() == null) {
            Double dnqtValue = woDto.getDnqtValue();
            if ("HCQT".equalsIgnoreCase(woDto.getWoTypeCode())) { // tạo checklist cho wo hoàn công qt
                List<WoChecklistBO> hcqtChecklist = createHcqtChecklistSet(woDto.getWoId());
                woChecklistDAO.saveListNoId(hcqtChecklist);
            } else if ("XLSC".equalsIgnoreCase(woDto.getWoTypeCode())) { // tạo checlist cho wo xử lý sự cố
                List<WoMappingChecklistBO> xlscChecklist = createXlscChecklistSet(woDto.getWoId());
                woMappingChecklistDAO.saveListNoId(xlscChecklist);
            } else if ("Codien_HTCT".equalsIgnoreCase(woDto.getWoTypeCode())) { // tạo checlist cho wo cơ điện
                List<WoMappingChecklistBO> CodienChecklist = createMechanicalElectricChecklistSet(woDto.getWoId());
                woMappingChecklistDAO.saveListNoId(CodienChecklist);
            } else {
                WoMappingChecklistBO checkListItem = new WoMappingChecklistBO();
                checkListItem.setWoId(woDto.getWoId());
                checkListItem.setState("NEW");
                checkListItem.setStatus(1l);
                checkListItem.setId(0l);

                // nếu là wo thu hồi dòng tiền thì quantity by date = 2 (sản lượng theo tháng)
                if ("THDT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                    checkListItem.setQuantityByDate("2");
                }

                woMappingChecklistDAO.save(checkListItem);
            }

        } else if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode()) && Long.valueOf(XDDD_AP_WORK_SRC).equals(woDto.getApWorkSrc())
                && !StringUtils.isNullOrEmpty(woDto.getChecklistItemNames())) {
            List<WoXdddChecklistBO> xdddChecklist = new ArrayList<>();
            if (woDto.getIsIOC() != null && woDto.getIsIOC()) {
                for (WoXdddChecklistRequestDTO xddd : woDto.getXdddChecklist()) {
                    woXdddChecklistDAO.saveObject(xddd.toModel());
                }
            } else {
                String itemNames = woDto.getChecklistItemNames();
                String[] itemNamesArr = itemNames.split("\\|");

                // tạo checlist cho wo xây dựng dân dụng

                for (String name : itemNamesArr) {
                    name = name.trim();
                    if (StringUtils.isNullOrEmpty(name))
                        continue;
                    WoXdddChecklistBO item = new WoXdddChecklistBO();
                    item.setName(name);
                    item.setState("NEW");
                    item.setStatus(1l);
                    item.setWoId(woDto.getWoId());
                    item.setHshc(0l);
                    item.setConfirm(0l);
                    item.setCreateDate(new Date());
                    item.setUserCreated(woDto.getLoggedInUser());
                    xdddChecklist.add(item);
                }
            }
            woXdddChecklistDAO.saveListNoId(xdddChecklist);

        } else if (woDto.getCatWorkItemTypeId() > 0) {
            Long catConsTypeId = woDto.getCatConstructionTypeId();
            Long constructionId = woDto.getConstructionId();
            if (catConsTypeId == null && constructionId != null)
                catConsTypeId = woDAO.getCatConstructionId(constructionId);
            mappingChecklistToWO(woDto.getCatWorkItemTypeId(), woDto.getWoId(), catConsTypeId, constructionId);
        } else {
            // catWorkItemTypeId < 0 is AIO wo
            Long contractId = woDto.getContractId();
            if (contractId != null)
                mappingChecklistAIOToWO(contractId, woDto.getWoId());
        }
    }

    //duonghv13 add 08122021
    private List<WoMappingChecklistBO> createMechanicalElectricChecklistSet(Long woId) {
        List<WoMappingChecklistBO> newSet = new ArrayList<>();
        List<WoAppParamDTO> hcqtChecklistName = woDAO.getAppParam(CODIEN_CHECKLIST);
        for (WoAppParamDTO param : hcqtChecklistName) {
            WoMappingChecklistBO item = new WoMappingChecklistBO();
            item.setStatus(1l);
            item.setWoId(woId);
            item.setName(param.getName());
            item.setState("NEW");
            item.setCheckListId(param.getParOrder());
            newSet.add(item);
        }

        return newSet;
    }

    @Override
    public List<WoSimpleFtDTO> getListFtToAssign(Long parentGroupId) {
        return woDAO.getFtList(parentGroupId, false, null);
    }

    @Override
    public void processOpinion(WoDTO dto) throws Exception {
        Gson gson = new Gson();
        Long woId = dto.getWoId();
        String loggedInUser = dto.getLoggedInUser();

        WoBO bo = woDAO.getOneRaw(woId);
        String opResult = dto.getOpinionResult();

        if (dto.getState() != null)
            bo.setState(dto.getState());

        if (opResult != null)
            bo.setOpinionResult(dto.getOpinionResult());

        try {
            woDAO.updateObject(bo);
            dto = bo.toDTO();
            String logContent = "";
            String opinionComment = dto.getOpinionComment();
            if (dto.getState() != null)
                logContent = OPINION_PASS_UP;
            if (opResult != null && opResult.equalsIgnoreCase(ACCEPTED))
                logContent = OPINION_ACCEPTED;
            else if (opResult != null && opResult.equalsIgnoreCase(REJECTED))
                logContent = OPINION_REJECTED;
            logContent += " - Ý kiến: " + opinionComment;

            String logContentDetails = gson.toJson(bo);
            logWoWorkLogs(dto, "2", logContent, logContentDetails, loggedInUser);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public List<WoMappingChecklistDTO> getCheckListOfWo(WoDTO dto) {

        Long woId = dto.getWoId();
        String woTypeCode = dto.getWoTypeCode();

        boolean isGPONTuyen = false;
        if (dto.getConstructionId() != null) {
            WoSimpleConstructionDTO construction = trDAO.getConstructionById(dto.getConstructionId());
            if (construction != null && (GPON_TYPE == construction.getCatConstructionTypeId()
                    || TUYEN_TYPE == construction.getCatConstructionTypeId()))
                isGPONTuyen = true;
        }

        boolean isAIO = false;
        boolean isCVDK = false;
        boolean isXDDD = false;
        if (THICONG.equalsIgnoreCase(woTypeCode) && dto.getApWorkSrc() != null && dto.getApWorkSrc() == 6)
            isXDDD = true;
        if (AIO.equalsIgnoreCase(woTypeCode))
            isAIO = true;
        if (BDDK.equalsIgnoreCase(woTypeCode) || woTypeCode.contains("NLMT") || ("BDTHTCT").equals(woTypeCode))
            isCVDK = true;

        if (isAIO)
            return woDAO.getListAIOChecklistsOfWo(woId);
        else if (isCVDK)
            return woDAO.getListBDDKChecklistOfWo(woId);
        else if (isXDDD) {
            WoXdddChecklistDTO searchObj = new WoXdddChecklistDTO();
            searchObj.setWoId(dto.getWoId());

            List<WoXdddChecklistDTO> xdddListChecklist = woXdddChecklistDAO.doSearch(searchObj);
            List<WoMappingChecklistDTO> checkList = new ArrayList<>();

            // convert xddd checklist to normal checklist
            for (WoXdddChecklistDTO item : xdddListChecklist) {
                WoMappingChecklistDTO convertedItem = item.toMappingChecklist();
                checkList.add(convertedItem);
            }

            return checkList;
        } else {
            List<WoMappingChecklistDTO> checkList = woDAO.getListChecklistsOfWo(woId);

            if (isGPONTuyen) {

                WoSimpleConstructionDTO construction = trDAO.getConstructionById(dto.getConstructionId());
                Double totalAmount = construction.getAmount();

                for (WoMappingChecklistDTO item : checkList) {
                    if ("1".equalsIgnoreCase(item.getQuantityByDate())) {
                        Double completedAmount = woTaskDailyDAO.sumAmountOfWo(item.getWoId());
                        item.setRemainAmount(totalAmount - completedAmount);
                        item.setCompletedAmount(completedAmount);
                    }
                }
            }

            for (WoMappingChecklistDTO item : checkList) {
                if ("1".equalsIgnoreCase(item.getQuantityByDate())) {
                    item.setUnapprovedQuantity(woTaskDailyDAO.sumUnapprovedQuantity(item.getId()));
                }

                if ("2".equalsIgnoreCase(item.getQuantityByDate())) {
                    Double unapprovedCollectedMoney = woTaskMonthlyDAO.getUnapprovedCollected(woId);
                    item.setUnapprovedQuantity(unapprovedCollectedMoney);
                }
            }

            return checkList;
        }
    }

    @Override
    public boolean woCheckGpon(WoDTO dto) {
        String constructionCode = dto.getConstructionCode();
        boolean isGPON = false;
        if (!StringUtils.isNullOrEmpty(constructionCode)) {
            WoSimpleConstructionDTO construction = trDAO.getConstructionByCode(constructionCode);
            if (construction.getCatConstructionTypeId() == 3 || construction.getCatConstructionTypeId() == 2)
                isGPON = true;
        }
        return isGPON;
    }

    @Override
    public List<WoMappingChecklistDTO> getCheckListNeedAdd(WoDTO dto) {

        Long woId = dto.getWoId();
        String woTypeCode = dto.getWoTypeCode();

        String constructionCode = dto.getConstructionCode();

        boolean isGPON = false;
        if (!StringUtils.isNullOrEmpty(constructionCode)) {
            WoSimpleConstructionDTO construction = trDAO.getConstructionByCode(constructionCode);
            if (construction.getCatConstructionTypeId() == 3 || construction.getCatConstructionTypeId() == 2)
                isGPON = true;
        }

        boolean isAIO = false;
        if (woTypeCode.equalsIgnoreCase(AIO))
            isAIO = true;

        if (isGPON)
            return woDAO.getListGPONCheckListNeedAdd(dto);
        else if (isAIO)
            return woDAO.getListAIOCheckListNeedAdd(dto);
        else
            return woDAO.getListChecklistsOfWo(woId);
    }

    @Override
    public List<WoMappingChecklistDTO> getAIOCheckListOfWo(WoDTO dto) {
        return woDAO.getListAIOChecklistsOfWo(dto.getWoId());
    }

    @Override
    public WoDTO getOneWoDetails(long woId) {
        WoDTO dto = woDAO.getOneDetails(woId);

        if (dto.getTrId() == null && dto.getContractId() != null) {
            String contractCode = woDAO.getCNTContractCode(dto.getContractId());
            dto.setContractCode(contractCode);
        }

        // ThanhPT - WO BGBTS - Start
        if ("BGBTS_DTHT".equalsIgnoreCase(dto.getWoTypeCode())) {
            List<AppParamDTO> lstCheclists = appParamDAO.getAppParamByParType("WO_BGBTS_CHECKLIST");
            dto.setLstAppParams(lstCheclists);
        }
        // ThanhPT - WO BGBTS - End

        return dto;
    }

    public File importWoTypeExcelTemplate() throws IOException {
        String fileName = "WO_XL_CreateWoType.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        File templateFile = new File(filePath + fileName);

        return templateFile;
    }

    @Override
    public File exportExcelWoNameList(List<WoNameDTO> names) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_WoNameList.xlsx";
        String outFileName = "Template_WO_XL_WoNameList.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 100)) {
                SXSSFSheet sheet = workbook.getSheetAt(0);

                int rowNum = 0;
                for (WoNameDTO name : names) {
                    Row row = sheet.createRow(++rowNum);
                    row.createCell(0).setCellValue(rowNum);
                    row.createCell(1).setCellValue(name.getName());
                    row.createCell(2).setCellValue(name.getWoTypeName());
                    row.createCell(3).setCellValue(name.getWoTypeCode());
                }

                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }

        return outFile;
    }

    @Override
    public File exportExcelWoTypeList(List<WoTypeDTO> types) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_WoTypeList.xlsx";
        String outFileName = "Template_WO_XL_WoTypeList.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 100)) {
                SXSSFSheet sheet = workbook.getSheetAt(0);

                int rowNum = 0;
                for (WoTypeDTO type : types) {
                    Row row = sheet.createRow(++rowNum);
                    row.createCell(0).setCellValue(rowNum);
                    row.createCell(1).setCellValue(type.getWoTypeCode());
                    row.createCell(2).setCellValue(type.getWoTypeName());
                    row.createCell(3).setCellValue(returnYesNo(type.getHasApWorkSrc()));
                    row.createCell(4).setCellValue(returnYesNo(type.getHasConstruction()));
                    row.createCell(5).setCellValue(returnYesNo(type.getHasWorkItem()));
                }

                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }

        return outFile;
    }

    @Override
    public File exportExcelWoList(List<WoDTO> wos) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_WoList.xlsx";
        String outFileName = "Template_WO_XL_WoList.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        List<WoAppParamDTO> stateList = woDAO.getAppParam("WO_XL_STATE");
        Map<String, String> stateMap = new HashMap<>();
        for (WoAppParamDTO item : stateList) {
            stateMap.put(item.getCode(), item.getName());
        }
        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 100)) {
                SXSSFSheet sheet = workbook.getSheetAt(0);

                int rowNum = 0;
                for (WoDTO wo : wos) {
                    Row row = sheet.createRow(++rowNum);
                    row.createCell(0).setCellValue(rowNum);
                    row.createCell(1).setCellValue(wo.getWoTypeName());
                    row.createCell(2).setCellValue(wo.getWoName());
                    row.createCell(3).setCellValue(wo.getWoCode());
                    row.createCell(4).setCellValue(wo.getTrCode());
                    row.createCell(5).setCellValue(wo.getApWorkSrcName());
                    row.createCell(6).setCellValue(wo.getApConstructionTypeName());
                    row.createCell(7).setCellValue(wo.getProjectCode());

                    if (StringUtils.isNullOrEmpty(wo.getHcqtContractCode()))
                        row.createCell(8).setCellValue(wo.getContractCode());
                    else
                        row.createCell(8).setCellValue(wo.getHcqtContractCode());

                    row.createCell(9).setCellValue(wo.getConstructionCode());
                    row.createCell(10).setCellValue(wo.getMoneyValue() != null ? wo.getMoneyValue() / 1000000 : 0d); // HienLT56
                    // add
                    // 29102020
                    row.createCell(11).setCellValue(wo.getCdLevel1Name());
                    row.createCell(12).setCellValue(wo.getCdLevel2Name());
                    row.createCell(13).setCellValue(wo.getCdLevel3Name());
                    row.createCell(14).setCellValue(wo.getCdLevel4Name());
                    row.createCell(15).setCellValue(wo.getFtName());
                    row.createCell(16).setCellValue(wo.getItemName());
                    if (wo.getOpinionType() != null) {
                        if (wo.getOpinionType().equalsIgnoreCase("Đề xuất gia hạn")) {
                            row.createCell(17).setCellValue(stateMap.get(wo.getState()) + " đề xuất gia hạn");
                        }
                        if (wo.getOpinionType().equalsIgnoreCase("Đề xuất hủy")) {
                            row.createCell(17).setCellValue(stateMap.get(wo.getState()) + " đề xuất hủy");
                        }
                    } else {
                        row.createCell(17).setCellValue(stateMap.get(wo.getState()));
                    }

                    if (wo.getCreatedDate() != null)
                        row.createCell(18).setCellValue(dateFormat.format(wo.getCreatedDate()));
                    if (wo.getFinishDate() != null)
                        row.createCell(19).setCellValue(dateFormat.format(wo.getFinishDate()));
                    if (wo.getEndTime() != null) {
                        row.createCell(20).setCellValue(wo.getEndTimeStr());
                    }
                    row.createCell(21).setCellValue(wo.getMoneyFlowBill());
                    if (wo.getMoneyFlowDate() != null)
                        row.createCell(22).setCellValue(dateFormat.format(wo.getMoneyFlowDate()));
                    if (wo.getMoneyFlowValue() != null)
                        row.createCell(23).setCellValue(wo.getMoneyFlowValue());
                    if (wo.getMoneyFlowRequired() != null)
                        row.createCell(24).setCellValue(wo.getMoneyFlowRequired());
                    // duonghv13 edit 04112021
                    if (wo.getWoTypeCode().equals("XLSC"))
                        row.createCell(25).setCellValue(wo.getDescription());
                    else
                        row.createCell(25).setCellValue(wo.getMoneyFlowContent());
                    // duonghv13 end 04112021
                    row.createCell(26).setCellValue(wo.getStationCode()); // Huypq-14102020-add mã trạm
                    row.createCell(27).setCellValue(wo.getVtNetStationCode());
                    row.createCell(28).setCellValue(wo.getFtName() != null ? wo.getFtName() : "");
                    row.createCell(29).setCellValue(wo.getFtEmailSysUser() != null ? wo.getFtEmailSysUser() : "");
                    row.createCell(30).setCellValue(wo.getFtPositionName() != null ? wo.getFtPositionName() : "");
                    if (wo.getApproveDateReportWo() != null)
                        row.createCell(31).setCellValue(dateFormat.format(wo.getApproveDateReportWo()));
                }
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }

        return outFile;
    }

    private String returnYesNo(Long val) {
        if (val != null && val == 1)
            return "Có";
        return "Không";
    }

    public File importWoNameExcelTemplate() throws IOException {
        File outFile = null;
        File templateFile = null;
        String fileName = "WO_XL_CreateWoName.xlsx";
        String outFileName = "Template_WO_XL_CreateWoName.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                SXSSFSheet sheet = workbook.getSheetAt(1);

                CellStyle cellStyle2 = sheet.getWorkbook().createCellStyle();
                CellStyle cellStyleHeader = sheet.getWorkbook().createCellStyle();
                CellStyle cellStyleInfo = sheet.getWorkbook().createCellStyle();

                Font fontContent = sheet.getWorkbook().createFont();
                fontContent.setFontName("Times New Roman");
                fontContent.setFontHeightInPoints((short) 11);

                cellStyle2.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                cellStyle2.setFillPattern(FillPatternType.NO_FILL);
                cellStyle2.setBorderTop(BorderStyle.THIN);
                cellStyle2.setBorderRight(BorderStyle.THIN);
                cellStyle2.setBorderBottom(BorderStyle.THIN);
                cellStyle2.setBorderLeft(BorderStyle.THIN);
                cellStyle2.setFont(fontContent);
                cellStyleInfo.setFont(fontContent);

                Font fontHeader2 = sheet.getWorkbook().createFont();
                fontHeader2.setBold(true);
                fontHeader2.setFontName("Times New Roman");
                fontHeader2.setFontHeightInPoints((short) 11);
                cellStyleHeader.setFont(fontHeader2);
                cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
                cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);

                WoTypeDTO typeDto = new WoTypeDTO();
                typeDto.setPage(1l);
                typeDto.setPageSize(99999);
                List<WoTypeDTO> woTypes = woTypeDAO.doSearch(typeDto);
                int rowApWorkSrc = 0;
                for (WoTypeDTO record : woTypes) {
                    Row row = getOrCreateRow(sheet, ++rowApWorkSrc);
                    Cell cell = row.createCell(0);
                    cell.setCellStyle(cellStyle2);
                    cell.setCellValue(record.getWoTypeId() + " - " + record.getWoTypeName());
                }
                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }

            }
        }
        return outFile;
    }

    public File getExcelDetailsReport(WoDTO dto, List<String> groupIdList) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_DetailsReport.xlsx";
        String outFileName = "WO_XL_DetailsReportResult.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 999999)) {
                SXSSFSheet sheet = workbook.getSheetAt(0);

                CellStyle cellStyle2 = sheet.getWorkbook().createCellStyle();
                CellStyle cellStyleHeader = sheet.getWorkbook().createCellStyle();
                CellStyle cellStyleInfo = sheet.getWorkbook().createCellStyle();

                Font fontContent = sheet.getWorkbook().createFont();
                fontContent.setFontName("Times New Roman");
                fontContent.setFontHeightInPoints((short) 11);

                cellStyle2.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                cellStyle2.setFillPattern(FillPatternType.NO_FILL);
                cellStyle2.setBorderTop(BorderStyle.THIN);
                cellStyle2.setBorderRight(BorderStyle.THIN);
                cellStyle2.setBorderBottom(BorderStyle.THIN);
                cellStyle2.setBorderLeft(BorderStyle.THIN);
                cellStyle2.setFont(fontContent);
                cellStyleInfo.setFont(fontContent);

                Font fontHeader2 = sheet.getWorkbook().createFont();
                fontHeader2.setBold(true);
                fontHeader2.setFontName("Times New Roman");
                fontHeader2.setFontHeightInPoints((short) 11);
                cellStyleHeader.setFont(fontHeader2);
                cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
                cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);

                CellStyle cellStyle3 = cellStyle2;

                List<WoDTO> reportRecords = woDAO.genDetailsReport(dto, groupIdList);
                List<WoAppParamDTO> woStates = woDAO.getAppParam("WO_XL_STATE");
                Map<String, String> woStateMap = new HashMap<>();
                for (WoAppParamDTO state : woStates)
                    woStateMap.put(state.getCode(), state.getName());

                for (int i = 0; i < reportRecords.size(); i++) {
                    WoDTO record = reportRecords.get(i);
                    Row row = getOrCreateRow(sheet, i + 1);
                    writeValueToCell(row, 0, cellStyle2, String.valueOf(i + 1)); // stt
                    writeValueToCell(row, 1, cellStyle2, "VN"); // market
                    String cdLv2Name = record.getCdLevel2Name();
                    String cnkt = "";
                    if (!StringUtils.isNullOrEmpty(cdLv2Name))
                        cnkt = cdLv2Name.split("-")[0].trim();
                    writeValueToCell(row, 2, cellStyle2, cnkt); // branch
                    writeValueToCell(row, 3, cellStyle2, record.getCdLevel4Name()); // processing unit
                    writeValueToCell(row, 4, cellStyle2, record.getFtName()); // ft name
                    writeValueToCell(row, 5, cellStyle2, record.getTrCode()); // tr code
                    writeValueToCell(row, 6, cellStyle2, record.getWoCode()); // wo code
                    writeValueToCell(row, 7, cellStyle2, record.getWoName()); // wo name

                    writeValueToCell(row, 8, cellStyle2, woStateMap.get(record.getState())); // state
                    writeValueToCell(row, 9, cellStyle2, record.getContractCode()); // contract code
                    // //Huypq-03092020-add
                    writeValueToCell(row, 10, cellStyle2, record.getStationCode()); // station code
                    writeValueToCell(row, 11, cellStyle2, record.getConstructionCode()); // cons code
                    writeValueToCell(row, 12, cellStyle2, record.getCatWorkItemTypeName()); // wi code
                    // //Huypq-03092020-add
                    writeValueToCell(row, 13, cellStyle2, record.getApConstructionTypeName()); // cons type name
                    writeValueToCell(row, 14, cellStyle2, record.getApWorkSrcName()); // ap work src name
                    writeValueToCell(row, 15, cellStyle2, record.getStartTime()); // start time
                    writeValueToCell(row, 16, cellStyle2, record.getEndTime()); // end time
                    writeValueToCell(row, 17, cellStyle2, record.getQoutaTime()); // quota time
                    writeValueToCell(row, 18, cellStyle2, record.getCalculateMethod());

                    Double moneyValue = record.getMoneyValue();
                    String moneyValueStr = "";
                    if (moneyValue != null) {
                        moneyValue = moneyValue / 1000000;
                        moneyValueStr = String.format("%.3f", moneyValue);
                    }

                    Double acceptedMoneyValue = record.getAcceptedMoneyValue();
                    String acceptedMoneyValueStr = "";
                    if (acceptedMoneyValue != null) {
                        acceptedMoneyValue = acceptedMoneyValue / 1000000;
                        acceptedMoneyValueStr = String.format("%.3f", acceptedMoneyValue);
                    }

                    // writeValueToCell(row, 16, cellStyle2, moneyValueStr); //money value
                    Cell cell = row.createCell(19, CellType.NUMERIC);
                    cell.setCellStyle(cellStyle2);
                    cell.setCellValue(moneyValueStr);

                    Cell cell2 = row.createCell(20, CellType.NUMERIC);
                    cell2.setCellStyle(cellStyle2);
                    cell2.setCellValue(acceptedMoneyValueStr);

                    // writeValueToCell(row, 16, cellStyle2, record.getTotalChecklistItem());
                    // //total chklist item
                    // writeValueToCell(row, 17, cellStyle2, record.getCompletedChecklistItem());
                    // //completed chklist item
                    // writeValueToCell(row, 18, cellStyle2, record.getNotCompletedChecklistItem());
                    // //not completed chklist item
                }

                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }

            }
        }

        return outFile;
    }

    public String getExcelGeneralReport(WoGeneralReportDTO dtox, List<String> groupIdList) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "WO_XL_GeneralReport.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        NumberFormat formatter = new DecimalFormat("#,###");
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "WO_XL_GeneralReport.xlsx");
        List<WoGeneralReportDTO> data = woDAO.getGeneralReport(dtox, groupIdList);

        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleRight = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.CENTER);
            styleRight.setAlignment(HorizontalAlignment.RIGHT);
            int i = 1;
            for (WoGeneralReportDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue("VN");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getGeoArea() != null) ? dto.getGeoArea() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getAreaName() != null) ? dto.getAreaName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getSectionName() != null) ? dto.getSectionName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getFtName() != null) ? dto.getFtName() : "");
                cell.setCellStyle(styleNumber);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getTotalWO() != null) ? formatter.format(dto.getTotalWO()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getTotalUnassign() != null) ? formatter.format(dto.getTotalUnassign()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getTotalAssignCd() != null) ? formatter.format(dto.getTotalAssignCd()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getTotalAcceptCd() != null) ? formatter.format(dto.getTotalAcceptCd()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getTotalRejectCd() != null) ? formatter.format(dto.getTotalRejectCd()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getTotalAssignFt() != null) ? formatter.format(dto.getTotalAssignFt()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getTotalAcceptFt() != null) ? formatter.format(dto.getTotalAcceptFt()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getTotalRejectFt() != null) ? formatter.format(dto.getTotalRejectFt()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue(
                        (dto.getTotalProcessing() != null) ? formatter.format(dto.getTotalProcessing()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(15, CellType.STRING);
                cell.setCellValue((dto.getTotalDone() != null) ? formatter.format(dto.getTotalDone()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(16, CellType.STRING);
                cell.setCellValue((dto.getTotalCdOk() != null) ? formatter.format(dto.getTotalCdOk()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(17, CellType.STRING);
                cell.setCellValue((dto.getTotalCdNotGood() != null) ? formatter.format(dto.getTotalCdNotGood()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(18, CellType.STRING);
                cell.setCellValue((dto.getTotalOk() != null) ? formatter.format(dto.getTotalOk()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(19, CellType.STRING);
                cell.setCellValue((dto.getTotalNotGood() != null) ? formatter.format(dto.getTotalNotGood()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(20, CellType.STRING);
                cell.setCellValue(
                        (dto.getTotalOpinionRequest() != null) ? formatter.format(dto.getTotalOpinionRequest()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(21, CellType.STRING);
                cell.setCellValue((dto.getTotalOverDue() != null) ? formatter.format(dto.getTotalOverDue()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(22, CellType.STRING);
                cell.setCellValue(
                        (dto.getTotalFinishOverDue() != null) ? formatter.format(dto.getTotalFinishOverDue()) : "0");
                cell.setCellStyle(styleRight);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "WO_XL_GeneralReport.xlsx");
        return path;
    }

    private void writeValueToCell(Row row, int column, CellStyle style, Object obj) {
        String value = "";
        if (obj != null)
            value = String.valueOf(obj);

        Cell cell = row.createCell(column);
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }

    public DataListDTO doSearchReportTHDT(ReportWoTHDTDTO obj) {
        List<ReportWoTHDTDTO> ls = woDAO.doSearchReportTHDT(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public String exportFileTHDT(ReportWoTHDTDTO obj) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "WO_XL_THDT.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        NumberFormat formatter = new DecimalFormat("#,###");
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "WO_XL_THDT.xlsx");
        List<ReportWoTHDTDTO> data = woDAO.exportFileTHDT(obj);

        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleRight = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.CENTER);
            styleRight.setAlignment(HorizontalAlignment.RIGHT);
            int i = 1;
            for (ReportWoTHDTDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(dto.getProCode());
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getSignerPartner() != null) ? dto.getSignerPartner() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getMoneyFlowBill() != null) ? dto.getMoneyFlowBill() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getMoneyFlowDate() != null) ? dto.getMoneyFlowDate() : "");
                cell.setCellStyle(styleNumber);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getMoneyFlowValue() != null) ? formatter.format(dto.getMoneyFlowValue()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue(
                        (dto.getMoneyFlowRequired() != null) ? formatter.format(dto.getMoneyFlowValue()) : "0");
                cell.setCellStyle(styleRight);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "WO_XL_THDT.xlsx");
        return path;
    }

    @Override
    public void acceptChecklistQuantity(WoMappingChecklistDTO dto) throws Exception {
        Gson gson = new Gson();
        if (dto.getId() != null && dto.getQuantityLength() != null && dto.getUnapprovedQuantity() != null
                && dto.getWoId() != null) {
            WoBO wo = woDAO.getOneRaw(dto.getWoId());
            WoMappingChecklistBO checklistBO = woMappingChecklistDAO.getOneRaw(dto.getId());
            if (wo.getQuantityByDate() == null) {
                wo.setQuantityByDate(Long.valueOf(checklistBO.getQuantityByDate()));
                woDAO.updateObject(wo);
            }

            woMappingChecklistDAO.acceptChecklistQuantity(dto);
            String logStr = "Cập nhật sản lượng theo ";

            // sản lượng theo ngày
            if ("1".equalsIgnoreCase(checklistBO.getQuantityByDate())) {
                List<WoTaskDailyBO> taskDailyBOs = woTaskDailyDAO.getUnapproved(dto.getId());
                logStr += "ngày. ";
                for (WoTaskDailyBO item : taskDailyBOs) {
                    item.setConfirm("1");
                    item.setApproveDate(new Date());
                    item.setApproveBy(dto.getLoggedInUser());
                    item.setConfirmUserId(dto.getSysUserId());
                    woTaskDailyDAO.updateObject(item);
                }
            }

            // sản lượng theo tháng
            if ("2".equalsIgnoreCase(checklistBO.getQuantityByDate())) {
                List<WoTaskMonthlyBO> taskMonthlyBOs = woTaskMonthlyDAO.getUnapproved(dto.getId());
                logStr += "tháng. ";
                for (WoTaskMonthlyBO item : taskMonthlyBOs) {
                    item.setConfirm("1");
                    item.setApproveDate(new Date());
                    item.setApproveBy(dto.getLoggedInUser());
                    item.setConfirmUserId(dto.getSysUserId());
                    woTaskMonthlyDAO.updateObject(item);
                }
            }

            Double approvedQuantity = dto.getUnapprovedQuantity() / 1000000;
            WoDTO woDTO = wo.toDTO();
            woDTO.setCustomField(DAILY_QUANTITY_ACCEPTED);
            logWoWorkLogs(woDTO, "1", logStr + "Sản lượng: " + approvedQuantity + " triệu.", gson.toJson(wo),
                    dto.getLoggedInUser());
            woDTO.setLoggedInUser(dto.getLoggedInUser());
            dto.setCustomField("Ghi nhận thành công!");

            // tự động CD_OK nếu đã thu đủ số tiền cần thu
            // if ("2".equalsIgnoreCase(checklistBO.getQuantityByDate())) {
            // WoMappingChecklistBO checklistAfterChanged =
            // woMappingChecklistDAO.getOneRaw(dto.getId());
            // Double woMoneyValue = woDTO.getMoneyValue();
            // Double acceptedChecklistValue = checklistAfterChanged.getQuantityLength();
            // if (woMoneyValue.equals(acceptedChecklistValue)) {
            // changeWoState(woDTO, CD_OK);
            // dto.setCustomField("Ghi nhận thành công! Tự động hoàn thành WO do đã thu hồi
            // đủ số tiền.");
            // }
            // }

        }
    }

    @Override
    public void rejectChecklistQuantity(WoMappingChecklistDTO dto) throws Exception {
        WoBO wo = woDAO.getOneRaw(dto.getWoId());
        WoMappingChecklistBO checklistBO = woMappingChecklistDAO.getOneRaw(dto.getId());
        if (wo.getQuantityByDate() == null) {
            wo.setQuantityByDate(Long.valueOf(checklistBO.getQuantityByDate()));
            woDAO.updateObject(wo);
        }

        // sản lượng theo tháng
        if ("2".equalsIgnoreCase(checklistBO.getQuantityByDate())) {
            List<WoTaskMonthlyBO> taskMonthlyBOs = woTaskMonthlyDAO.getUnapproved(dto.getId());
            for (WoTaskMonthlyBO item : taskMonthlyBOs) {
                item.setStatus(0l);
                woTaskMonthlyDAO.updateObject(item);
            }
        }

        WoDTO woDTO = wo.toDTO();
        woDTO.setDontSendMocha(true);
        Gson gson = new Gson();
        logWoWorkLogs(woDTO, "1", "Từ chối sản lượng theo tháng", gson.toJson(wo), dto.getLoggedInUser());
    }

    public File genImportResultExcelFile(List<WoDTO> dtos) throws Exception {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_CreateWo_Result.xlsx";
        String outFileName = "Template_WO_XL_CreateWo.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                Sheet importSheet = workbook.getSheetAt(0);
                CellStyle errorStyle = importSheet.getWorkbook().createCellStyle();
                errorStyle.setFillForegroundColor(IndexedColors.RED.index);
                errorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

                int rowNo = 0;
                for (WoDTO wo : dtos) {
                    Row row = importSheet.createRow(++rowNo);

                    if (!StringUtils.isNullOrEmpty(wo.getCustomField())
                            && !"Thành công. ".equalsIgnoreCase(wo.getCustomField())) {
                        row.setRowStyle(errorStyle);
                    }

                    if (wo.getWoTypeName() != null)
                        row.createCell(0).setCellValue(wo.getWoTypeName());
                    if (wo.getApWorkSrcName() != null)
                        row.createCell(1).setCellValue(wo.getApWorkSrcName());
                    if (wo.getApConstructionTypeName() != null)
                        row.createCell(2).setCellValue(wo.getApConstructionTypeName());
                    if (wo.getWoNameFromConfig() != null)
                        row.createCell(3).setCellValue(wo.getWoName());
                    if (wo.getTotalMonthPlanId() != null)
                        row.createCell(4).setCellValue(wo.getTotalMonthPlanId());
                    if (wo.getTrCode() != null)
                        row.createCell(5).setCellValue(wo.getTrCode());
                    if (wo.getContractCode() != null)
                        row.createCell(6).setCellValue(wo.getContractCode());
                    if (wo.getConstructionCode() != null)
                        row.createCell(7).setCellValue(wo.getConstructionCode());
                    if (wo.getStationCode() != null)
                        row.createCell(8).setCellValue(wo.getStationCode());
                    if (wo.getCatWorkItemTypeName() != null)
                        row.createCell(9).setCellValue(wo.getCatWorkItemTypeName());
                    if (wo.getMoneyValue() != null)
                        row.createCell(10).setCellValue(wo.getMoneyValue());
                    if (wo.getFinishDate() != null)
                        row.createCell(11).setCellValue(dateFormat.format(wo.getFinishDate()));
                    if (wo.getQoutaTime() != null)
                        row.createCell(12).setCellValue(wo.getQoutaTime());
                    if (wo.getCdLevel2Name() != null)
                        row.createCell(13).setCellValue(wo.getCdLevel2Name());
                    if (wo.getMoneyFlowBill() != null)
                        row.createCell(14).setCellValue(wo.getMoneyFlowBill());
                    if (wo.getMoneyFlowDate() != null)
                        row.createCell(15).setCellValue(dateFormat.format(wo.getMoneyFlowDate()));
                    if (wo.getMoneyFlowValue() != null)
                        row.createCell(16).setCellValue(wo.getMoneyFlowValue());
                    if (wo.getMoneyFlowRequired() != null)
                        row.createCell(17).setCellValue(wo.getMoneyFlowRequired());
                    if (wo.getMoneyFlowContent() != null)
                        row.createCell(18).setCellValue(wo.getMoneyFlowContent());
                    if (wo.getChecklistItemNames() != null)
                        row.createCell(19).setCellValue(wo.getChecklistItemNames());
                    if (wo.getCdLevel5Email() != null)
                        row.createCell(20).setCellValue(wo.getCdLevel5Email());
                    if (wo.getCustomField() != null)
                        row.createCell(21).setCellValue(wo.getCustomField());
                }

                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }

            }
        }

        return outFile;
    }

    public File genImportHcqtResultExcelFile(List<WoHcqtDTO> dtos) throws Exception {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_CreateHcqtWo_Result.xlsx";
        String outFileName = "Template_WO_XL_CreateHcqtWo.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                Sheet importSheet = workbook.getSheetAt(0);
                CellStyle errorStyle = importSheet.getWorkbook().createCellStyle();
                errorStyle.setFillForegroundColor(IndexedColors.RED.index);
                errorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

                int rowNo = 1;
                for (WoHcqtDTO wo : dtos) {
                    Row row = importSheet.createRow(++rowNo);

                    if (!StringUtils.isNullOrEmpty(wo.getCustomField())
                            && !"Thành công. ".equalsIgnoreCase(wo.getCustomField())) {
                        row.setRowStyle(errorStyle);
                    }

                    if (wo.getFtName() != null)
                        row.createCell(0).setCellValue(wo.getFtName());
                    // taptq start 23082021
                    if (wo.getApWorkSource() != null)
                        row.createCell(1).setCellValue(wo.getApWorkSource());
                    // taptq start 23082021
                    if (wo.getStationCode() != null)
                        row.createCell(2).setCellValue(wo.getStationCode());
                    if (wo.getCatProvinceCode() != null)
                        row.createCell(3).setCellValue(wo.getCatProvinceCode());
                    if (wo.getHcqtProjectName() != null)
                        row.createCell(4).setCellValue(wo.getHcqtProjectName());
                    if (wo.getCnkv() != null)
                        row.createCell(5).setCellValue(wo.getCnkv());
                    if (wo.getHcqtContractCode() != null)
                        row.createCell(6).setCellValue(wo.getHcqtContractCode());
                    if (wo.getMoneyValue() != null)
                        row.createCell(7).setCellValue(wo.getMoneyValue());
                    if (wo.getHshcReceiveDate() != null)
                        row.createCell(8).setCellValue(dateFormat.format(wo.getHshcReceiveDate()));
                    if (wo.getFinishDate() != null)
                        row.createCell(9).setCellValue(dateFormat.format(wo.getFinishDate()));

                    if (wo.getDnqtDate() != null)
                        row.createCell(10).setCellValue(dateFormat.format(wo.getDnqtDate()));
                    if (wo.getDnqtValue() != null)
                        row.createCell(11).setCellValue(wo.getDnqtValue());
                    if (wo.getDnqtPerson() != null)
                        row.createCell(12).setCellValue(wo.getDnqtPerson());
                    if (wo.getVtnetSendDate() != null)
                        row.createCell(13).setCellValue(dateFormat.format(wo.getVtnetSendDate()));
                    if (wo.getVtnetConfirmDate() != null)
                        row.createCell(14).setCellValue(dateFormat.format(wo.getVtnetConfirmDate()));
                    if (wo.getVtnetConfirmPerson() != null)
                        row.createCell(15).setCellValue(wo.getVtnetConfirmPerson());
                    if (wo.getAprovedDocDate() != null)
                        row.createCell(16).setCellValue(dateFormat.format(wo.getAprovedDocDate()));
                    if (wo.getVtnetConfirmValue() != null)
                        row.createCell(17).setCellValue(wo.getVtnetConfirmValue());
                    if (wo.getAprovedPerson() != null)
                        row.createCell(18).setCellValue(wo.getAprovedPerson());
                    if (wo.getCustomField() != null)
                        row.createCell(19).setCellValue(wo.getCustomField());
                }

                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }

            }
        }

        return outFile;
    }

    public DataListDTO doSearchReportAIO(ReportWoAIODTO obj) {
        List<ReportWoAIODTO> ls = woDAO.doSearchReportAIO(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public String exportFileAIO(ReportWoAIODTO obj) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BC_Quan_Tri_AIO.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        NumberFormat formatter = new DecimalFormat("#,###");
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BC_Quan_Tri_AIO.xlsx");
        List<ReportWoAIODTO> data = woDAO.exportFileAIO(obj);

        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleRight = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.CENTER);
            styleRight.setAlignment(HorizontalAlignment.RIGHT);
            int i = 4;
            for (ReportWoAIODTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getCdLevel2() != null) ? dto.getCdLevel2() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getCdLevel4() != null) ? dto.getCdLevel4() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getTrCode() != null) ? dto.getTrCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getWoCode() != null) ? dto.getWoCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getInsdustryName() != null) ? dto.getInsdustryName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getMoneyValue() != null) ? formatter.format(dto.getMoneyValue()) : "0");
                cell.setCellStyle(styleRight);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getState() != null) ? dto.getState() : "");
                cell.setCellStyle(style);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getUserCreated() != null) ? dto.getUserCreated() : "");
                cell.setCellStyle(style);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getUserFt() != null) ? dto.getUserFt() : "");
                cell.setCellStyle(style);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getAcceptTime() != null) ? dto.getAcceptTime() : "");
                cell.setCellStyle(styleCenter);
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getEndTime() != null) ? dto.getEndTime() : "");
                cell.setCellStyle(styleCenter);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getReason() != null) ? dto.getReason() : "0");
                cell.setCellStyle(style);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BC_Quan_Tri_AIO.xlsx");
        return path;
    }

    private List<WoChecklistBO> createHcqtChecklistSet(Long woId) {
        List<WoChecklistBO> newSet = new ArrayList<>();
        List<WoAppParamDTO> hcqtChecklistName = woDAO.getAppParam(HCQT_CHECKLIST);
        for (WoAppParamDTO param : hcqtChecklistName) {
            WoChecklistBO item = new WoChecklistBO();
            item.setStatus(1l);
            item.setWoId(woId);
            item.setChecklistName(param.getName());
            item.setState("NEW");
            item.setChecklistOrder(param.getParOrder());
            newSet.add(item);
        }

        return newSet;
    }

    private List<WoMappingChecklistBO> createXlscChecklistSet(Long woId) {
        List<WoMappingChecklistBO> newSet = new ArrayList<>();
        List<WoAppParamDTO> hcqtChecklistName = woDAO.getAppParam(XLSC_CHECKLIST);
        for (WoAppParamDTO param : hcqtChecklistName) {
            WoMappingChecklistBO item = new WoMappingChecklistBO();
            item.setStatus(1l);
            item.setWoId(woId);
            item.setName(param.getName());
            item.setState("NEW");
            item.setCheckListId(param.getParOrder() - 3);
            newSet.add(item);
        }

        return newSet;
    }

    // kiem tra cong trinh da hoan thanh doi voi loai wo thi cong
    private boolean checkConstructionDone(WoDTO dto) {
        if ("THICONG".equalsIgnoreCase(dto.getWoTypeCode()) && dto.getConstructionId() != null) {
            WoSimpleConstructionDTO construction = trDAO.getConstructionById(dto.getConstructionId());
            if (construction != null) {
                if (construction.getStatus() == 5) {
                    dto.setCustomField("Không được giao công trình đã hoàn thành cho loại wo thi công; ");
                    return true;
                }
            }
        }

        return false;
    }

    // kiem tra hang muc da hoan thanh doi voi loai wo thi cong
    private boolean checkWorkItemDone(WoDTO dto) {
        if ("THICONG".equalsIgnoreCase(dto.getWoTypeCode()) && dto.getCatWorkItemTypeId() != null
                && dto.getConstructionId() != null) {
            boolean checkResult = woDAO.checkWorkItemDone(dto.getConstructionId(), dto.getCatWorkItemTypeId());

            if (checkResult) {
                dto.setCustomField("Không được giao hạng mục đã hoàn thành cho loại wo thi công; ");
                return true;
            }

        }

        return false;
    }

    public DataListDTO doSearchReportHSHCStatus(ReportHSHCQTDTO obj) {
        List<ReportHSHCQTDTO> ls = new ArrayList<>();
        if (obj.getType() == 1) {
            ls = woDAO.doSearchReportHSHCStatus(obj);
        } else {
            ls = woDAO.doSearchReportHSHCProject(obj);
        }
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public String exportFileHSHCStatus(ReportHSHCQTDTO obj) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_BCHSHCQT_Vuong.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Export_BCHSHCQT_Vuong.xlsx");
        List<ReportHSHCQTDTO> data = new ArrayList<>();
        if (obj.getType() == 1) {
            data = woDAO.exportFileHSHCStatus(obj);
        } else {
            data = woDAO.exportFileHSHCProject(obj);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
            int i = 2;
            for (ReportHSHCQTDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getProblemName() != null) ? dto.getProblemName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getValueTotal() != null) ? dto.getValueTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getQuantityAproved() != null) ? dto.getQuantityAproved() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getAprovedValue() != null) ? dto.getAprovedValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getQuantityVtnet() != null) ? dto.getQuantityVtnet() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getVtnetValue() != null) ? dto.getVtnetValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getQuantityDnqt() != null) ? dto.getQuantityDnqt() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getDnqtValue() != null) ? dto.getDnqtValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getQuantityMoney() != null) ? dto.getQuantityMoney() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getMoneyValue() != null) ? dto.getMoneyValue() : 0d);
                cell.setCellStyle(styleNumber);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_BCHSHCQT_Vuong.xlsx");
        return path;
    }

    @Override
    public boolean addImageToChecklist(WoChecklistDTO dto) throws Exception {
        WoMappingAttachBO woMappingAttachBO = new WoMappingAttachBO();
        woMappingAttachBO.setWoId(dto.getWoId());
        woMappingAttachBO.setUserCreated(dto.getUserCreated());
        woMappingAttachBO.setChecklistId(dto.getChecklistId());

        String dateString = new SimpleDateFormat("yyyymmdd_hhMMss").format(new Date());

        String fileName = dateString + ".jpg";
        ImgChecklistDTO image = dto.getLstImgs().get(0);
        String base64String = image.getImgBase64();
        byte[] decodedBytes = com.itextpdf.text.pdf.codec.Base64.decode(base64String);
        InputStream is = new ByteArrayInputStream(decodedBytes);
        String path = null;
        try {
            // path = UFile.writeToFileServerATTT2(is, fileName, "/" + dto.getWoId() + "/" +
            // dto.getChecklistId() + "/", folderUpload);
            path = UFile.writeToFileServerATTT2(is, fileName, UPLOAD_SUB_FOLDER, folderUpload);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        woMappingAttachBO.setFileName(fileName);
        woMappingAttachBO.setFilePath(path);
        woMappingAttachBO.setStatus(1l);

        // try update related wo processing if state is accept ft
        tryUpdateWoProcessing(dto.getWoId(), dto.getLoggedInUser());

        woMappingAttachDAO.save(woMappingAttachBO);
        return true;
    }

    @Override
    public void tryUpdateWoProcessing(Long woId, String loggedInUser) throws Exception {
        Gson gson = new Gson();
        WoBO bo = woDAO.getOneRaw(woId);
        String currentState = bo.getState();

        if (ACCEPT_FT.equalsIgnoreCase(currentState)) {
            bo.setState(PROCESSING);
            bo.setStartTime(new Date());
            woDAO.updateObject(bo);
            logWoWorkLogs(bo.toDTO(), "1", "Cập nhật trạng thái: Đang thực hiện", gson.toJson(bo), loggedInUser);
        }
    }

    // Huypq-25082020-start
    public String exportReportDetailWo(WoDTO obj, List<String> groupIdList) throws Exception {
        obj.setPage(null);
        obj.setPageSize(null);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "WO_XL_DetailsReport.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "WO_XL_DetailsReport.xlsx");
        List<WoDTO> reportRecords = woDAO.genDetailsReport(obj, groupIdList);
        List<WoAppParamDTO> woStates = woDAO.getAppParam("WO_XL_STATE");
        Map<String, String> woStateMap = new HashMap<>();
        for (WoAppParamDTO state : woStates)
            woStateMap.put(state.getCode(), state.getName());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (reportRecords != null && !reportRecords.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
            styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
            int i = 1;
            for (WoDTO record : reportRecords) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue("VN");
                cell.setCellStyle(style);
                String cdLv2Name = record.getCdLevel2Name();
                String cnkt = "";
                if (!StringUtils.isNullOrEmpty(cdLv2Name))
                    cnkt = cdLv2Name.split("-")[0].trim();
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(cnkt);
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                // cell.setCellValue((record.getCdLevel4Name() != null) ?
                // record.getCdLevel4Name() : "");
                cell.setCellValue((record.getCdLevel1Name() != null) ? record.getCdLevel1Name() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((record.getFtName() != null) ? record.getFtName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((record.getTrCode() != null) ? record.getTrCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((record.getWoCode() != null) ? record.getWoCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((record.getWoName() != null) ? record.getWoName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((woStateMap.get(record.getState()) != null) ? woStateMap.get(record.getState()) : "");
                cell.setCellStyle(style);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((record.getContractCode() != null) ? record.getContractCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((record.getCatStationHouseTxt() != null) ? record.getCatStationHouseTxt() : "");
                cell.setCellStyle(style);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((record.getStationCode() != null) ? record.getStationCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((record.getConstructionCode() != null) ? record.getConstructionCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((record.getCatWorkItemTypeName() != null) ? record.getCatWorkItemTypeName() : "");
                cell.setCellStyle(style);

                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue((record.getChecklistItemNames() != null) ? record.getChecklistItemNames() : "");
                cell.setCellStyle(style);

                cell = row.createCell(15, CellType.STRING);
                cell.setCellValue(
                        (record.getApConstructionTypeName() != null) ? record.getApConstructionTypeName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(16, CellType.STRING);
                cell.setCellValue((record.getApWorkSrcName() != null) ? record.getApWorkSrcName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(17, CellType.STRING);
                cell.setCellValue((record.getFinishDateStr() != null) ? record.getFinishDateStr() : "");
                cell.setCellStyle(style);
                cell = row.createCell(18, CellType.STRING);
                cell.setCellValue((record.getEndTimeStr() != null) ? record.getEndTimeStr() : "");
                cell.setCellStyle(style);
                cell = row.createCell(19, CellType.STRING);
                cell.setCellValue((record.getUserTthtApproveWoStr() != null) ? record.getUserTthtApproveWoStr() : "");
                cell.setCellStyle(style);
                cell = row.createCell(20, CellType.STRING);
                cell.setCellValue((record.getUserTthtApproveWo() != null) ? record.getUserTthtApproveWo() : "");
                cell.setCellStyle(style);
                cell = row.createCell(21, CellType.STRING);
                cell.setCellValue(
                        (record.getApproveDateReportWoStr() != null) ? record.getApproveDateReportWoStr() : "");
                cell.setCellStyle(style);
                cell = row.createCell(22, CellType.STRING);
                cell.setCellValue((record.getQoutaTime() != null) ? record.getQoutaTime() : 0);
                cell.setCellStyle(style);
                cell = row.createCell(23, CellType.STRING);
                cell.setCellValue((record.getCalculateMethod() != null) ? record.getCalculateMethod() : "");
                cell.setCellStyle(style);
                cell = row.createCell(24, CellType.STRING);
                cell.setCellValue((record.getMoneyValue() != null) ? record.getMoneyValue() / 1000000d : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(25, CellType.STRING);
                cell.setCellValue(
                        (record.getAcceptedMoneyValue() != null) ? record.getAcceptedMoneyValue() / 1000000d : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(26, CellType.STRING);
                cell.setCellValue(
                        (record.getAcceptedMoneyValueDaily() != null) ? record.getAcceptedMoneyValueDaily() / 1000000d
                                : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(27, CellType.STRING);
                cell.setCellValue((record.getAcceptedMoneyValueMonthly() != null)
                        ? record.getAcceptedMoneyValueMonthly() / 1000000d
                        : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(28, CellType.STRING);
                cell.setCellValue((record.getPartner() != null) ? record.getPartner() : "");
                cell.setCellStyle(style);
                cell = row.createCell(29, CellType.STRING);
                cell.setCellValue((record.getVtnetWoCode() != null) ? record.getVtnetWoCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(30, CellType.STRING);
                cell.setCellValue((record.getWoOrder() != null) ? record.getWoOrder() : "");
                cell.setCellStyle(style);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "WO_XL_DetailsReport.xlsx");
        return path;
    }
    // Huy-end

    @Override
    public WoMappingChecklistDTO listChecklistsDetail(Long woId, Long checkListID) {
        WoMappingChecklistDTO woMappingChecklistDTO = new WoMappingChecklistDTO();
        woMappingChecklistDTO.setChecklistId(checkListID);
        List<ImgChecklistDTO> lstImgs = getImageList(woId, checkListID);
        woMappingChecklistDTO.setLstImgs(lstImgs);

        WoDTO woDetail = woDAO.getOneDetails(woId);
        if ("THICONG".equalsIgnoreCase(woDetail.getWoTypeCode())
                && "TR_DONG_BO_HA_TANG".equalsIgnoreCase(woDetail.getTrTypeCode())) {
            WoMappingChecklistDTO iMappingChecklist = woMappingChecklistDAO.getDetail(woId, checkListID);
            if (woMappingChecklistDTO != null) {
                woMappingChecklistDTO.setClassId(iMappingChecklist.getClassId());
                woMappingChecklistDTO.setClassName(iMappingChecklist.getClassName());
                woMappingChecklistDTO.setDefaultClassValue(iMappingChecklist.getDefaultClassValue());
                woMappingChecklistDTO.setActualValue(iMappingChecklist.getActualValue());
            }
        }

        return woMappingChecklistDTO;
    }

    @Override
    public WoChecklistDTO getChecklistsDetailAvg(Long woId, Long checkListID) {
        WoChecklistDTO woChecklistDTO = new WoChecklistDTO();

        WoChecklistBO detail = woChecklistDAO.getOneRaw(checkListID);
        if (detail != null) {
            woChecklistDTO = detail.toDTO();
        }
        woChecklistDTO.setChecklistId(checkListID);
        List<ImgChecklistDTO> lstImgs = getImageList(woId, checkListID);
        woChecklistDTO.setLstImgs(lstImgs);

        return woChecklistDTO;
    }

    @Override
    public List<WoMappingChecklistDTO> getListChecklistsOfWoTest(Long woId) {
        WoBO bo = woDAO.getOneRaw(woId);
        boolean isAIO = woDAO.checkWoIsAIO(bo.getWoTypeId());
        WoDTO dto = bo.toDTO();
        if (isAIO)
            dto.setWoTypeCode(AIO);

        List<WoMappingChecklistDTO> lstData = getCheckListOfWo(dto);
        // for (WoMappingChecklistDTO iData : lstData) {
        // // Get list file attach
        // List<ImgChecklistDTO> lstImgs = getImageList(iData.getWoId(),
        // iData.getChecklistId());
        // iData.setLstImgs(lstImgs);
        // }
        return lstData;
    }

    @Override
    public boolean checkDoneCTTuyen(Long woId) {
        WoDTO wo = woDAO.getOneDetails(woId);
        String woTypeCode = wo.getWoTypeCode();

        // không phải wo thi công
        if ("THICONG".equalsIgnoreCase(woTypeCode) == false)
            return true;

        // không phải công trình tuyến, gpon
        Long catConType = wo.getCatConstructionTypeId();
        if (catConType == null)
            return true;
        if (catConType != 2 && catConType != 3)
            return true;

        Double unapproveQty = woTaskDailyDAO.sumWoUnapprovedQuantity(woId);
        if (unapproveQty == 0)
            return true;

        return false;
    }

    public DataListDTO doSearchReportHSHCProvince(ReportHSHCQTDTO obj) {
        List<ReportHSHCQTDTO> ls = woDAO.doSearchReportHSHCProvince(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public String exportFileHSHCProvince(ReportHSHCQTDTO obj) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_BCHSHCQT_Tinh.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Export_BCHSHCQT_Tinh.xlsx");
        List<ReportHSHCQTDTO> data = woDAO.exportFileHSHCProvince(obj);

        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
            int i = 3;
            for (ReportHSHCQTDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 3));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getQuantityTotal() != null) ? dto.getQuantityTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getValueTotal() != null) ? dto.getValueTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getQuantityAprovedTotal() != null) ? dto.getQuantityAprovedTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getAprovedValueTotal() != null) ? dto.getAprovedValueTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getQuantityVtnetTotal() != null) ? dto.getQuantityVtnetTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getVtnetValueTotal() != null) ? dto.getVtnetValueTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getQuantityDnqtTotal() != null) ? dto.getQuantityDnqtTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getDnqtValueTotal() != null) ? dto.getDnqtValueTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getQuantityMoneyTotal() != null) ? dto.getQuantityMoneyTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getMoneyValueTotal() != null) ? dto.getMoneyValueTotal() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getQuantityAproved() != null) ? dto.getQuantityAproved() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getAprovedValue() != null) ? dto.getAprovedValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue((dto.getQuantityVtnet() != null) ? dto.getQuantityVtnet() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(15, CellType.STRING);
                cell.setCellValue((dto.getVtnetValue() != null) ? dto.getVtnetValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(16, CellType.STRING);
                cell.setCellValue((dto.getQuantityDnqt() != null) ? dto.getQuantityDnqt() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(17, CellType.STRING);
                cell.setCellValue((dto.getDnqtValue() != null) ? dto.getDnqtValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(18, CellType.STRING);
                cell.setCellValue((dto.getQuantityMoney() != null) ? dto.getQuantityMoney() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(19, CellType.STRING);
                cell.setCellValue((dto.getMoneyValue() != null) ? dto.getMoneyValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(20, CellType.STRING);
                cell.setCellValue((dto.getQuantityAprovedPoblem() != null) ? dto.getQuantityAprovedPoblem() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(21, CellType.STRING);
                cell.setCellValue((dto.getAprovedValuePoblem() != null) ? dto.getAprovedValuePoblem() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(22, CellType.STRING);
                cell.setCellValue((dto.getQuantityVtnetPoblem() != null) ? dto.getQuantityVtnetPoblem() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(23, CellType.STRING);
                cell.setCellValue((dto.getVtnetValuePoblem() != null) ? dto.getVtnetValuePoblem() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(24, CellType.STRING);
                cell.setCellValue((dto.getQuantityDnqtPoblem() != null) ? dto.getQuantityDnqtPoblem() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(25, CellType.STRING);
                cell.setCellValue((dto.getDnqtValuePoblem() != null) ? dto.getDnqtValuePoblem() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(26, CellType.STRING);
                cell.setCellValue((dto.getQuantityMoneyPoblem() != null) ? dto.getQuantityMoneyPoblem() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(27, CellType.STRING);
                cell.setCellValue((dto.getMoneyValuePoblem() != null) ? dto.getMoneyValuePoblem() : 0d);
                cell.setCellStyle(styleNumber);

            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_BCHSHCQT_Tinh.xlsx");
        return path;
    }

    @Override
    public File createImportHCQTWoExcelTemplate() throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_HCQT_WO_Template.xlsx";
        String outFileName = "WO_HCQT_WO_Template_out.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {

                // lấy các sys user của trung tâm hạ tầng?
                Long TTHTSysGroupId = 242656l;
                List<WoSimpleFtDTO> ftList = woDAO.getFtListFromLv2SysGroup(TTHTSysGroupId, null);
                writeHCQTFtList(workbook, ftList);

                WoHcqtProjectDTO searchDto = new WoHcqtProjectDTO();
                searchDto.setPage(1l);
                searchDto.setPageSize(999999);
                List<WoHcqtProjectDTO> projects = woHcqtProjectDAO.doSearch(searchDto);
                writeHcqtProjectList(workbook, projects);

                List<WoAppParamDTO> hcqtIssuesAppParam = woDAO.getAppParam("HCQT_ISSUE");
                writeHCQTIssues(workbook, hcqtIssuesAppParam);

                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }

        return outFile;
    }

    private void writeHCQTIssues(SXSSFWorkbook workbook, List<WoAppParamDTO> hcqtIssuesAppParam) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(3);
        for (WoAppParamDTO appParram : hcqtIssuesAppParam) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(appParram.getCode());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(appParram.getName());
        }
    }

    private void writeHCQTFtList(SXSSFWorkbook workbook, List<WoSimpleFtDTO> ftList) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(1);
        for (WoSimpleFtDTO record : ftList) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(record.getFtId());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(record.getEmployeeCode());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(record.getFullName());
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(record.getEmail());
        }
    }

    private void writeHcqtProjectList(SXSSFWorkbook workbook, List<WoHcqtProjectDTO> projects) {
        int rowNum = 0;
        SXSSFSheet sheet = workbook.getSheetAt(2);
        for (WoHcqtProjectDTO record : projects) {
            Row row = getOrCreateRow(sheet, ++rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(record.getHcqtProjectId());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(record.getName());
        }
    }

    public boolean validateHCQTImportData(List<WoHcqtDTO> dtos) {
        List<String> lstStationCode = new ArrayList<>();
        List<String> stationAndContractList = new ArrayList<>();
        List<Long> hcqtProjectIdList = new ArrayList<>();
        List<String> contractCodeList = new ArrayList<>();

        boolean isValid = true;

        for (WoHcqtDTO wo : dtos) {
            if (!StringUtils.isNullOrEmpty(wo.getStationCode()))
                lstStationCode.add(wo.getStationCode());
            if (wo.getHcqtProjectId() != null && !hcqtProjectIdList.contains(wo.getHcqtProjectId()))
                hcqtProjectIdList.add(wo.getHcqtProjectId());

            if (wo.getHcqtContractCode() != null && !contractCodeList.contains(wo.getHcqtContractCode()))
                contractCodeList.add(wo.getHcqtContractCode());

        }

        // prepare contractMap
        Map<String, WoSimpleContractDTO> contractMap = new HashMap<>();
        if (contractCodeList.size() > 0) {
            WoTrDTO trDto = new WoTrDTO();
            trDto.setContractCodeRange(contractCodeList);
            List<WoSimpleContractDTO> contractList = trDAO.doSearchContracts(trDto);
            for (WoSimpleContractDTO contract : contractList)
                contractMap.put(contract.getContractCode(), contract);
        }

        // prepare station
        WoSimpleStationDTO searchDto = new WoSimpleStationDTO();
        searchDto.setCodeRange(lstStationCode);
        List<WoSimpleStationDTO> stations = trDAO.doSearchStationsIgnoreStatusComplete(searchDto);
        Map<String, WoSimpleStationDTO> stationMap = new HashMap<>();
        for (WoSimpleStationDTO station : stations)
            stationMap.put(station.getStationCode(), station);

        // prepare contract match station str
        List<String> contractMatchStationList = woDAO.getContractStationList(contractCodeList);

        WoHcqtProjectDTO searchProjectDto = new WoHcqtProjectDTO();
        searchProjectDto.setIdRange(hcqtProjectIdList);
        List<WoHcqtProjectDTO> woHcqtProjectList = woHcqtProjectDAO.doSearch(searchProjectDto);
        Map<Long, WoHcqtProjectDTO> hcqtProjectMap = new HashMap<>();
        for (WoHcqtProjectDTO p : woHcqtProjectList)
            hcqtProjectMap.put(p.getHcqtProjectId(), p);

        for (WoHcqtDTO wo : dtos) {
            String errorStr = "";

            if (wo.getFtId() == null) {
                errorStr += "Người phụ trách không được để trống hoặc không có quyền; ";
                isValid = false;
            }

            if (StringUtils.isNullOrEmpty(wo.getStationCode())) {
                errorStr += "Mã trạm không được để trống; ";
                isValid = false;
            } else {
                // WoSimpleStationDTO station = stationMap.get(wo.getStationCode());
                // if (station == null) {
                // errorStr += "Mã trạm không tồn tại; ";
                // isValid = false;
                // } else {
                // String checkStr = wo.getContractCode() + "-" + wo.getStationCode();
                // if (!contractMatchStationList.contains(checkStr)) {
                // errorStr += "Mã trạm không thuộc hợp đồng; ";
                // isValid = false;
                // }
                // }
            }

            if (StringUtils.isNullOrEmpty(wo.getCatProvinceCode())) {
                errorStr += "Mã tỉnh không được để trống; ";
                isValid = false;
            }

            if (wo.getHcqtProjectId() == null) {
                errorStr += "Dự án HCQT không được để trống; ";
                isValid = false;
            } else {
                WoHcqtProjectDTO checkProject = hcqtProjectMap.get(wo.getHcqtProjectId());
                if (checkProject == null) {
                    errorStr += "Dự án HCQT không tồn tại hoặc đã xóa; ";
                    isValid = false;
                }
            }

            if (StringUtils.isNullOrEmpty(wo.getCnkv())) {
                errorStr += "CNKV không được để trống; ";
                isValid = false;
            }

            if (StringUtils.isNullOrEmpty(wo.getHcqtContractCode())) {
                errorStr += "Mã hợp đồng HCQT không được để trống; ";
                isValid = false;
            } else {
                String contractCode = wo.getHcqtContractCode();
                WoSimpleContractDTO contract = contractMap.get(contractCode);
                if (contract == null) {
                    errorStr += "Mã hợp đồng không tồn tại; ";
                    isValid = false;
                }
            }

            if (wo.getMoneyValue() == null) {
                errorStr += "Giá trị sản lượng không được để trống; ";
                isValid = false;
            }

            if (wo.getHshcReceiveDate() == null) {
                errorStr += "Ngày nhận HSHC không được để trống; ";
                isValid = false;
            }

            if (wo.getFinishDate() == null) {
                errorStr += "Hạn hoàn thành không được để trống; ";
                isValid = false;
            }

            String stationAndContract = wo.getStationCode() + "-" + wo.getHcqtContractCode();

            if (stationAndContractList.contains(stationAndContract)) {
                errorStr += "Trùng lặp dữ liệu: Cùng một hợp đồng, một trạm chỉ có thể tạo một WO HCQT; ";
                isValid = false;
            } else
                stationAndContractList.add(stationAndContract);

            if (wo.getDnqtValue() != null && wo.getDnqtDate() == null) {
                errorStr += "Nếu có giá trị đề nghị quyết toán thì phải có ngày đề nghị; ";
                isValid = false;
            }

            if (wo.getVtnetSendDate() != null && wo.getDnqtValue() == null) {
                errorStr += "Phải có đề nghị quyết toán trước khi gửi VTNET; ";
                isValid = false;
            }

            if ((wo.getVtnetConfirmDate() != null
                    && (wo.getAprovedPerson() == null || wo.getVtnetConfirmValue() == null))
                    || (wo.getAprovedPerson() != null
                    && (wo.getVtnetConfirmValue() == null || wo.getVtnetConfirmDate() == null))
                    || (wo.getVtnetConfirmValue() != null
                    && (wo.getAprovedPerson() == null || wo.getVtnetConfirmDate() == null))) {
                errorStr += "Thiếu thông tin để hoàn thành bước chốt với VTNET; ";
                isValid = false;
            } else {
                if ((wo.getVtnetSendDate() == null || wo.getDnqtValue() == null)
                        && (wo.getVtnetConfirmDate() != null || wo.getVtnetConfirmValue() != null)) {
                    errorStr += "Phải có đề nghị quyết toán và gửi trước khi chốt; ";
                    isValid = false;
                }
            }

            if (wo.getAprovedDocDate() != null) {
                if (wo.getVtnetSendDate() == null || wo.getDnqtValue() == null || wo.getVtnetConfirmValue() == null) {
                    errorStr += "Phải hoàn thành các bước khác trước khi bảng thẩm về; ";
                    isValid = false;
                }
            }

            wo.setCustomField(errorStr);

        }

        return isValid;
    }

    public File genHcqtImportResultExcelFile(List<WoHcqtDTO> dtos) throws Exception {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_CreateHcqtWo_Result.xlsx";
        String outFileName = "Template_WO_XL_CreateHcqtWo.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                Sheet importSheet = workbook.getSheetAt(0);
                CellStyle errorStyle = importSheet.getWorkbook().createCellStyle();
                errorStyle.setFillForegroundColor(IndexedColors.RED.index);
                errorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

                int rowNo = 1;
                for (WoHcqtDTO wo : dtos) {
                    Row row = importSheet.createRow(++rowNo);

                    if (!StringUtils.isNullOrEmpty(wo.getCustomField())
                            && !"Thành công. ".equalsIgnoreCase(wo.getCustomField())) {
                        row.setRowStyle(errorStyle);
                    }

                    if (wo.getFtName() != null)
                        row.createCell(0).setCellValue(wo.getFtName());
                    if (wo.getStationCode() != null)
                        row.createCell(1).setCellValue(wo.getStationCode());
                    if (wo.getCatProvinceCode() != null)
                        row.createCell(2).setCellValue(wo.getCatProvinceCode());
                    if (wo.getHcqtProjectName() != null)
                        row.createCell(3).setCellValue(wo.getHcqtProjectName());
                    if (wo.getCnkv() != null)
                        row.createCell(4).setCellValue(wo.getCnkv());
                    if (wo.getHcqtContractCode() != null)
                        row.createCell(5).setCellValue(wo.getHcqtContractCode());
                    if (wo.getMoneyValue() != null)
                        row.createCell(6).setCellValue(wo.getMoneyValue());

                    if (StringUtils.isNullOrEmpty(wo.getHshcReceiveDateStr())) {
                        if (wo.getHshcReceiveDate() != null)
                            row.createCell(7).setCellValue(dateFormat.format(wo.getHshcReceiveDate()));
                    } else {
                        row.createCell(7).setCellValue(wo.getHshcReceiveDateStr());
                    }

                    if (StringUtils.isNullOrEmpty(wo.getFinishDateStr())) {
                        if (wo.getFinishDate() != null)
                            row.createCell(8).setCellValue(dateFormat.format(wo.getFinishDate()));
                    } else {
                        row.createCell(8).setCellValue(wo.getFinishDateStr());
                    }

                    if (StringUtils.isNullOrEmpty(wo.getDnqtDateStr())) {
                        if (wo.getDnqtDate() != null)
                            row.createCell(9).setCellValue(dateFormat.format(wo.getDnqtDate()));
                    } else {
                        row.createCell(9).setCellValue(wo.getDnqtDateStr());
                    }

                    if (wo.getDnqtValue() != null)
                        row.createCell(10).setCellValue(wo.getDnqtValue());
                    if (wo.getDnqtPerson() != null)
                        row.createCell(11).setCellValue(wo.getDnqtPerson());

                    if (StringUtils.isNullOrEmpty(wo.getVtnetSendDateStr())) {
                        if (wo.getVtnetSendDate() != null)
                            row.createCell(12).setCellValue(dateFormat.format(wo.getVtnetSendDate()));
                    } else {
                        row.createCell(12).setCellValue(wo.getVtnetSendDateStr());
                    }

                    if (StringUtils.isNullOrEmpty(wo.getVtnetConfirmDateStr())) {
                        if (wo.getVtnetConfirmDate() != null)
                            row.createCell(13).setCellValue(dateFormat.format(wo.getVtnetConfirmDate()));
                    } else {
                        row.createCell(13).setCellValue(wo.getVtnetConfirmDateStr());
                    }

                    if (wo.getVtnetConfirmPerson() != null)
                        row.createCell(14).setCellValue(wo.getVtnetConfirmPerson());

                    if (StringUtils.isNullOrEmpty(wo.getAprovedDocDateStr())) {
                        if (wo.getAprovedDocDate() != null)
                            row.createCell(15).setCellValue(dateFormat.format(wo.getAprovedDocDate()));
                    } else {
                        row.createCell(15).setCellValue(wo.getAprovedDocDateStr());
                    }

                    if (wo.getVtnetConfirmValue() != null)
                        row.createCell(16).setCellValue(wo.getVtnetConfirmValue());
                    if (wo.getAprovedPerson() != null)
                        row.createCell(17).setCellValue(wo.getAprovedPerson());
                    if (wo.getCustomField() != null)
                        row.createCell(18).setCellValue(wo.getCustomField());
                }

                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }

            }
        }

        return outFile;
    }

    public String exportFileHcqtFtReport(WoHcqtFtReportDTO obj) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BC_HCQT_FT.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BC_HCQT_FT.xlsx");
        List<WoHcqtFtReportDTO> data = new ArrayList<>();
        data = woDAO.exportFileHcqtFtReport(obj);
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
            int i = 2;
            for (WoHcqtFtReportDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getFtName() != null) ? dto.getFtName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getTotalQuantityByPlan() != null) ? dto.getTotalQuantityByPlan() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getTotalValueByPlan() != null) ? dto.getTotalValueByPlan() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getTotalHasDnqtQuantity() != null) ? dto.getTotalHasDnqtQuantity() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getTotalDnqtValue() != null) ? dto.getTotalDnqtValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getDnqtPercent() != null) ? dto.getDnqtPercent() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getTotalSendVtnetQuantity() != null) ? dto.getTotalSendVtnetQuantity() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getTotalSendVtnetValue() != null) ? dto.getTotalSendVtnetValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue(
                        (dto.getTotalVtnetConfirmedQuantity() != null) ? dto.getTotalVtnetConfirmedQuantity() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getTotalVtnetConfirmedValue() != null) ? dto.getTotalVtnetConfirmedValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getVtnetPercent() != null) ? dto.getVtnetPercent() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getTotalApprovedQuantity() != null) ? dto.getTotalApprovedQuantity() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getTotalApprovedValue() != null) ? dto.getTotalApprovedValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue((dto.getApprovedPercent() != null) ? dto.getApprovedPercent() : 0d);
                cell.setCellStyle(styleNumber);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BC_HCQT_FT.xlsx");
        return path;
    }

    @Override
    public int insertUpdateManyHcqtWo(List<WoHcqtDTO> dtos) throws Exception {
        int total = 0;
        boolean isImporting = true;

        WoDTO searchObj = new WoDTO();
        Long hcqtWoTypeId = woTypeDAO.getIdByCode("HCQT");
        List<WoDTO> allHcqtWo = woDAO.getAllWoByTypeId(hcqtWoTypeId);

        Map<String, WoDTO> hcqtWoMap = new HashMap<>();

        for (WoDTO wo : allHcqtWo) {
            String uniqueKey = createUniqueHcqtKey(wo);
            hcqtWoMap.put(uniqueKey, wo);
        }

        List<String> uniqueKeyList = new ArrayList<>();
        for (WoHcqtDTO item : dtos) {
            String uniqueKey = createUniqueHcqtKey(item);
            if (uniqueKeyList.contains(uniqueKey)) {
                item.setCustomField("Bỏ qua (trùng dữ liệu);");
            } else {
                WoDTO existedWo = hcqtWoMap.get(uniqueKey);
                // nếu chưa tồn tại thì thêm mới, đã tồn tại thì update
                if (existedWo == null)
                    if (createNewWO(item, isImporting))
                        total++;
                    else
                        updateHcqtWo(existedWo.getWoId(), item);
            }

        }
        return total;
    }

    public String createUniqueHcqtKey(WoDTO wo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // String uniqueKey = wo.getFtName() + "-";
        String uniqueKey = wo.getStationCode() + "-";
        // uniqueKey += wo.getCatProvinceCode() + "-";
        // uniqueKey += wo.getHcqtProjectId() + "-";
        // uniqueKey += wo.getCnkv() + "-";
        uniqueKey += wo.getHcqtContractCode() + "-";
        // uniqueKey += wo.getMoneyValue() + "-";
        // if (wo.getHshcReceiveDate() != null) uniqueKey +=
        // dateFormat.format(wo.getHshcReceiveDate());

        return uniqueKey;
    }

    public boolean updateHcqtWo(Long woId, WoHcqtDTO updates) throws Exception {
        Gson gson = new Gson();
        WoChecklistDTO searchObj = new WoChecklistDTO();
        searchObj.setWoId(woId);
        List<WoChecklistDTO> lstChecklist = woChecklistDAO.doSearch(searchObj);
        WoBO bo = woDAO.getOneRaw(woId);

        if (bo.getChecklistStep() != null && bo.getChecklistStep() == 5) {
            updates.setCustomField("Không thể cập nhật! WO đã được tài chính phê duyệt!;");
            return false;
        }

        Date now = new Date();

        long updateStep = 0;
        if (updates.getDnqtValue() != null)
            updateStep = 1;
        if (updates.getVtnetSendDate() != null)
            updateStep = 2;
        if (updates.getVtnetConfirmValue() != null)
            updateStep = 3;
        if (updates.getAprovedDocDate() != null)
            updateStep = 4;

        Long woStep = bo.getChecklistStep();

        if (woStep != null && woStep > updateStep) {
            updates.setCustomField("Không thể cập nhật do thiếu dữ liệu. WO đã thực hiện đến bước " + woStep);
            return false;
        }

        bo.setFtId(updates.getFtId());
        bo.setFtName(updates.getFtName());
        bo.setFtEmail(updates.getFtEmail());
        bo.setCatProvinceCode(updates.getCatProvinceCode());
        bo.setHcqtProjectId(updates.getHcqtProjectId());
        bo.setCnkv(updates.getCnkv());
        bo.setMoneyValue(updates.getMoneyValue());
        bo.setHshcReceiveDate(updates.getHshcReceiveDate());
        bo.setFinishDate(updates.getFinishDate());
        bo.setApWorkSrc(updates.getApWorkSrc());

        for (WoChecklistDTO item : lstChecklist) {
            if (item.getChecklistOrder() == 1) {
                boolean dataChangeStep1 = false;
                if (updates.getDnqtDate() != null) {
                    if (compareDateIgnoreTime(updates.getDnqtDate(), item.getDnqtDate()) != 0) {
                        item.setDnqtDate(updates.getDnqtDate());
                        dataChangeStep1 = true;
                    }
                }

                if (updates.getDnqtValue() != null) {
                    if (!updates.getDnqtValue().equals(item.getDnqtValue())) {
                        item.setDnqtValue(updates.getDnqtValue());
                        dataChangeStep1 = true;
                    }
                }

                if (updates.getDnqtValue() != null)
                    item.setState("DONE");

                if (dataChangeStep1) {
                    item.setCompletedDate(now);
                    item.setCompletePersonId(updates.getFtId());
                    item.setCompletePersonName(updates.getFtName());
                }
            }

            if (item.getChecklistOrder() == 2 && updates.getVtnetSendDate() != null) {
                boolean dataChangeStep2 = false;
                if (compareDateIgnoreTime(updates.getVtnetSendDate(), item.getVtnetSendDate()) != 0) {
                    item.setVtnetSendDate(updates.getVtnetSendDate());
                    item.setVtnetSentValue(updates.getDnqtValue());
                    dataChangeStep2 = true;
                }

                if (updates.getVtnetSendDate() != null)
                    item.setState("DONE");

                if (dataChangeStep2) {
                    item.setCompletedDate(now);
                    item.setCompletePersonId(updates.getFtId());
                    item.setCompletePersonName(updates.getFtName());
                }

            }

            if (item.getChecklistOrder() == 3) {
                boolean dataChangeStep3 = false;

                if (updates.getVtnetConfirmDate() != null) {
                    if (compareDateIgnoreTime(updates.getVtnetConfirmDate(), item.getVtnetConfirmDate()) != 0) {
                        item.setVtnetConfirmDate(updates.getVtnetConfirmDate());
                        dataChangeStep3 = true;
                    }
                }

                if (updates.getVtnetConfirmValue() != null) {
                    if (!updates.getVtnetConfirmValue().equals(item.getVtnetConfirmValue())) {
                        item.setVtnetConfirmValue(updates.getVtnetConfirmValue());
                        dataChangeStep3 = true;
                    }
                }

                if (updates.getAprovedPerson() != null) {
                    if (!updates.getAprovedPerson().equals(item.getAprovedPerson())) {
                        item.setAprovedPerson(updates.getAprovedPerson());
                        dataChangeStep3 = true;
                    }
                }

                if (updates.getVtnetConfirmValue() != null)
                    item.setState("DONE");

                if (dataChangeStep3) {
                    item.setCompletedDate(now);
                    item.setCompletePersonId(updates.getFtId());
                    item.setCompletePersonName(updates.getFtName());
                }
            }

            if (item.getChecklistOrder() == 4 && updates.getAprovedDocDate() != null
                    && updates.getVtnetConfirmValue() != null) {
                boolean dataChangeStep4 = false;
                if (compareDateIgnoreTime(updates.getAprovedDocDate(), item.getAprovedDocDate()) != 0) {
                    item.setAprovedDocDate(updates.getAprovedDocDate());
                    dataChangeStep4 = true;
                }

                if (updates.getAprovedDocDate() != null)
                    item.setState("DONE");

                if (dataChangeStep4) {
                    item.setAprovedDocValue(updates.getVtnetConfirmValue());
                    item.setCompletedDate(now);
                    item.setCompletePersonId(updates.getFtId());
                    item.setCompletePersonName(updates.getFtName());
                }
            }

            // if (item.getChecklistOrder() == 5) {
            // if ("DONE".equalsIgnoreCase(item.getState()) &&
            // updates.getVtnetConfirmValue() != null)
            // item.setFinalValue(updates.getVtnetConfirmValue());
            // }

            woChecklistDAO.updateObject(item.toModel());
        }

        bo.setChecklistStep(updateStep);
        if (updateStep == 4)
            bo.setState(DONE);
        woDAO.updateObject(bo);
        updates.setCustomField("Thành công! ");
        WoDTO woDto = bo.toDTO();
        woDto.setDontSendMocha(true);
        logWoWorkLogs(woDto, "1", "Cập nhật từ Excel", gson.toJson(bo), updates.getLoggedInUser());
        return true;
    }

    // bieu mau giao cho FT
    public String exportFileDeliveryFT(WoDTO woDto, HttpServletRequest request) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "WO_giaoFT.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "WO_giaoFT.xlsx");
        List<String> groupIdList = ConvertData.convertStringToList(woDto.getCdDomainDataList(), ",");
        List<WoCdDTO> listWOcd = woDAO.getCdLevel1();
        boolean checkCD = false;
        boolean checkCDLevel5 = false;
        // duonghv13 add 08102021
        boolean checkcreatedWOUCTT = false;
        // duonghv13 end 08102021
        String type = "";
        for (String domainData : groupIdList) {
            if (Constant.AdResourceKey.WOXL_XDDTHT.equals(domainData)) {
                checkCDLevel5 = true;
            }
            if (Constant.AdResourceKey.WOXL_UCTTROLE.equals(domainData)) {
                checkcreatedWOUCTT = true;
            }
            for (WoCdDTO woCdDTO : listWOcd) {
                if (domainData.equals(woCdDTO.getSysGroupId().toString())) {
                    checkCD = true;
                }

            }
        }
        if (checkCD == false) {
            List<WoDTO> data = new ArrayList<>();
            if (checkCDLevel5 == true) {
                List<String> cdLevl5 = new ArrayList<>();
                cdLevl5.add(VpsPermissionChecker.getUser(request).getSysUserId().toString());
                data = woDAO.exportApproveWo("3", cdLevl5);
            } else if (checkcreatedWOUCTT == true) {
                data = woDAO.exportApproveWoUCTTRole("0", groupIdList);
            } else {
                data = woDAO.exportApproveWo("0", groupIdList);
            }

            XSSFSheet sheet = workbook.getSheetAt(0);
            if (data != null && !data.isEmpty()) {
                XSSFCellStyle style = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
                XSSFCreationHelper createHelper = workbook.getCreationHelper();
                styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                styleNumber.setAlignment(HorizontalAlignment.RIGHT);
                // styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
                int i = 2;
                for (WoDTO dto : data) {
                    Row row = sheet.createRow(i++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue("" + (i - 2));
                    cell.setCellStyle(styleNumber);
                    cell.setCellStyle(style);
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue((dto.getWoCode() != null) ? dto.getWoCode() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue((dto.getApWorkSrcName() != null) ? dto.getApWorkSrcName() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellValue((dto.getMoneyValue() != null) ? dto.getMoneyValue() : 0d);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellValue((dto.getFinishDateStr() != null) ? dto.getFinishDateStr() : null);
                    cell.setCellStyle(styleCenter);
                }
            }
        }
        XSSFSheet sheet2 = workbook.getSheetAt(1);
        List<WoSimpleFtDTO> ftList = new ArrayList<>();
        if (checkCDLevel5 == true) {
            ftList = woDAO.getFtListLevel5();
        } else {
            ftList = woDAO.getFtList(woDto.getSysGroupId(), true, null);
        }
        if (ftList != null && !ftList.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet2);
            int i = 1;
            for (WoSimpleFtDTO record : ftList) {
                Row row = sheet2.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((record.getEmployeeCode() != null) ? record.getEmployeeCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((record.getEmail() != null) ? record.getEmail() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((record.getFullName() != null) ? record.getFullName() : "");
                cell.setCellStyle(style);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "WO_giaoFT.xlsx");
        return path;
    }

    public String exportApproveWo(WoDTO woDto, HttpServletRequest request) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "WO_Approve.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "WO_Approve.xlsx");
        List<String> groupIdList = ConvertData.convertStringToList(woDto.getCdDomainDataList(), ",");
        List<WoCdDTO> listWOcd = woDAO.getCdLevel1();
        boolean isTcTct = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
                Constant.AdResourceKey.TC_TCT, request);
        boolean isTcBranch = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
                Constant.AdResourceKey.TC_BRANCH, request);
        // Huypq-22102021-start
        boolean is_APPROVED_HTCT_HSHC = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
                Constant.AdResourceKey.HTCT_HSHC, request);
        boolean is_APPROVE_DTHT_HTCT_HSHC = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVE_DTHT,
                Constant.AdResourceKey.HTCT_HSHC, request);
        boolean is_APPROVED_REVENUE_SALARY = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
                Constant.AdResourceKey.REVENUE_SALARY, request);
        // Huy-end
        boolean checkCD = false;
        boolean checkCDLevel5 = false;
        String type = "";
        if (!isTcTct && !isTcBranch) {
            for (String domainData : groupIdList) {
                if (Constant.AdResourceKey.WOXL_XDDTHT.equals(domainData)) {
                    checkCDLevel5 = true;
                }
                for (WoCdDTO woCdDTO : listWOcd) {
                    if (domainData.equals(woCdDTO.getSysGroupId().toString())) {
                        checkCD = true;
                    }
                }
            }
            if (checkCD == true) {
                type = "2";
            } else {
                type = "1";
            }
        }
        if (isTcTct) {
            type = "TCTCT";
        }
        if (isTcBranch) {
            type = "TCBRANCH";
        }
        // Huypq-22102021-start
        if (is_APPROVED_HTCT_HSHC) {
            type = "PĐH_TTHT";
        }
        if (is_APPROVE_DTHT_HTCT_HSHC) {
            type = "TTĐTHT";
        }
        if (is_APPROVED_REVENUE_SALARY) {
            type = "PQT";
        }
        // Huy-end
        if (checkCDLevel5 == true) {
            // type = "4";
            if (checkCD != true) {
                type = "4";
                groupIdList.add(VpsPermissionChecker.getUser(request).getSysUserId().toString());
            }
        }
        List<WoDTO> data = woDAO.exportApproveWo(type, groupIdList);
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            // styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
            int i = 2;
            for (WoDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getWoCode() != null) ? dto.getWoCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getWoTypeName() != null) ? dto.getWoTypeName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getApWorkSrcName() != null) ? dto.getApWorkSrcName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getCatWorkItemTypeName() != null) ? dto.getCatWorkItemTypeName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getMoneyValue() != null) ? dto.getMoneyValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getFinishDateStr() != null) ? dto.getFinishDateStr() : null);
                cell.setCellStyle(style);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getEndTimeStr() != null) ? dto.getEndTimeStr() : null);
                cell.setCellStyle(style);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getCdLevel2Name() != null) ? dto.getCdLevel2Name() : null);
                cell.setCellStyle(style);
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getFtName() != null) ? dto.getFtName() : null);
                cell.setCellStyle(style);
            }

            // Nếu là tài chính trụ thì fill danh sách TC tổng công ty để chọn
            if ("TCBRANCH".equalsIgnoreCase(type) || "PQT".equalsIgnoreCase(type)) {
                // Lay danh sach TC TCT
                List<WoAppParamDTO> lstTcTcts = woDAO.getAppParam("ROLE_TC_TCT");
                XSSFSheet sheetData = workbook.getSheetAt(1);
                for (WoAppParamDTO iTcTct : lstTcTcts) {
                    Row row = sheetData.createRow(lstTcTcts.indexOf(iTcTct));
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue(iTcTct.getCode());
                    cell.setCellStyle(style);
                }
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "WO_Approve.xlsx");
        return path;
    }

    public List<WoDTO> importFileDelivery(String fileInput, HttpServletRequest request) throws Exception {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        List<WoDTO> workLst = Lists.newArrayList();
        List<WoDTO> listWoDTOCheck = Lists.newArrayList();
        String error = "";
        boolean resul = true;
        try {
            File f = new File(fileInput);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<String> listCodeWO = Lists.newArrayList();
            int count = 0;
            for (Row row : sheet) {
                WoDTO obj = new WoDTO();
                count++;
                if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                    String woCode = formatter.formatCellValue(row.getCell(1));
                    String apWorkSrcName = formatter.formatCellValue(row.getCell(2));
                    String contractCode = formatter.formatCellValue(row.getCell(3));
                    String stationCode = formatter.formatCellValue(row.getCell(4));
                    String constructionCode = formatter.formatCellValue(row.getCell(5));
                    String moneyValue = formatter.formatCellValue(row.getCell(6));
                    String finishDate = formatter.formatCellValue(row.getCell(7));
                    String ft = formatter.formatCellValue(row.getCell(8));
                    obj.setFtName(ft.trim());
                    obj.setWoCode(woCode);
                    obj.setApWorkSrcName(apWorkSrcName);
                    obj.setContractCode(contractCode);
                    obj.setStationCode(stationCode);
                    obj.setConstructionCode(constructionCode);
                    obj.setFinishDateStr(finishDate);
                    obj.setMoneyValueString(moneyValue);
                    obj.setUserCreated(user.getVpsUserInfo().getEmployeeCode());
                    obj.setLoggedInUser(user.getVpsUserInfo().getEmployeeCode());
                    obj.setState(ASSIGN_FT);
                    workLst.add(obj);
                    listCodeWO.add(woCode);
                }
            }
            if (listCodeWO.size() > 0) {
                listWoDTOCheck = woDAO.getWoIDByCode(listCodeWO, "0");
            }
            if (workLst.size() > 0) {
                for (WoDTO woDTO : workLst) {
                    boolean isCheck = false;
                    String code = woDTO.getWoCode();
                    for (WoDTO woDTOCheck : listWoDTOCheck) {
                        if (woDTOCheck.getWoCode().equals(code)) {
                            isCheck = true;
                            woDTO.setWoId(woDTOCheck.getWoId());
                            woDTO.setWoTypeCode(woDTOCheck.getWoTypeCode());
                            if (woDTOCheck.getApWorkSrc() != null && woDTOCheck.getApConstructionType() != null
                                    && woDTOCheck.getApWorkSrc() == 6 && woDTOCheck.getApConstructionType() == 8
                                    && woDTOCheck.getWoTypeCode().equals("THICONG")) {
                                Double sl = woDAO.getCheckListCountXDDD(woDTOCheck.getWoId());
                                if (sl.equals(0D)) {
                                    woDTO.setCustomField("Mã WO chưa có đầu việc thì không được giao WO!");
                                }
                            }
                        }
                    }
                    if (isCheck == false) {
                        woDTO.setCustomField("Mã WO không tồn tại!");
                    }
                    if (!StringUtils.isNullOrEmpty(woDTO.getFtName())) {
                        SysUserDTO sysUserDTO = woDAO.checkUserFT(woDTO.getFtName(),
                                user.getVpsUserInfo().getSysGroupId());
                        if (sysUserDTO != null) {
                            woDTO.setFtName(sysUserDTO.getFullName());
                            woDTO.setFtId(sysUserDTO.getUserId());
                            woDTO.setFtEmail(sysUserDTO.getEmail());
                            if (checkDebt(woDTO)) {
                                woDTO.setCustomField("Nhân viên còn công nợ không được giao WO");
                            }
                            if (checkCertificate(woDTO) == true) {
                                woDTO.setCustomField(
                                        "Nhân viên không có chứng chỉ thỏa mãn hoặc chứng chỉ đã hết hạn. ");
                            }

                        } else {
                            woDTO.setCustomField("Nhân viên thực hiện không tồn tại!");
                        }
                    } else {
                        woDTO.setCustomField("Nhân viên thực hiện không được để trống");
                    }

                }
            }

            return workLst;

        } catch (NullPointerException pointerException) {
            pointerException.printStackTrace();
            throw new Exception(error);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(error);
        }
    }

    private ExcelErrorDTO createError(int row, int column, String detail) {
        ExcelErrorDTO err = new ExcelErrorDTO();
        err.setColumnError(String.valueOf(column));
        err.setLineError(String.valueOf(row));
        err.setDetailError(detail);
        return err;
    }

    public boolean validateString(String str) {
        this.str = str;
        return (str != null && str.length() > 0);
    }

    @Override
    public void declareHcqtProblem(WoChecklistDTO obj) throws Exception {
        Gson gson = new Gson();
        WoChecklistBO bo = woChecklistDAO.getOneRaw(obj.getChecklistId());

        // add 9 days to due date if declare issue
        WoBO wo = woDAO.getOneRaw(bo.getWoId());
        Date woFinishDate = wo.getFinishDate();
        Calendar c = Calendar.getInstance();
        c.setTime(woFinishDate);
        Long totalProblemOfThisWo = woDAO.sumProblemHcqtWo(bo.getWoId());

        Long daysAdded = 9 * (totalProblemOfThisWo + 1);
        c.add(Calendar.DATE, daysAdded.intValue());
        Date dueDate = c.getTime();

        bo.setResolveDueDate(dueDate);
        bo.setProblemName(obj.getProblemName());
        bo.setProblemCode(obj.getProblemCode());
        bo.setProblemDate(new Date());
        Long hasProblem = bo.getHasProblem();

        if (hasProblem == null)
            hasProblem = 0l;
        hasProblem += 1;
        bo.setHasProblem(hasProblem);
        bo.setContent(obj.getContent());
        bo.setProblemDeclarePersonId(obj.getProblemDeclarePersonId());
        bo.setProblemDeclarePersonName(obj.getProblemDeclarePersonName());
        bo.setState(PROBLEM);

        // if current state is accept ft then change to processing
        tryUpdateWoProcessing(wo.getWoId(), obj.getLoggedInUser());

        woChecklistDAO.updateObject(bo);
        String logContent = "Đầu việc " + bo.getChecklistName() + " báo vướng. Loại vướng: " + obj.getProblemName()
                + " với lý do/nội dung: " + obj.getContent();
        WoDTO woDto = wo.toDTO();
        logWoWorkLogs(woDto, "1", logContent, gson.toJson(woDto), obj.getLoggedInUser());

    }

    @Override
    public void resolveHcqtProblem(WoChecklistDTO obj) throws Exception {
        Gson gson = new Gson();
        WoChecklistBO bo = woChecklistDAO.getOneRaw(obj.getChecklistId());
        bo.setState("NEW");
        woChecklistDAO.updateObject(bo);

        WoBO wo = woDAO.getOneRaw(bo.getWoId());
        WoDTO woDto = wo.toDTO();
        String logContent = "Đầu việc " + bo.getChecklistName() + " đã xử lý xong vướng. ";
        logWoWorkLogs(woDto, "1", logContent, gson.toJson(woDto), obj.getLoggedInUser());
    }

    @Override
    public void completeHcqtChecklist(WoChecklistDTO obj) {
        Gson gson = new Gson();
        Date completeDate = new Date();
        WoChecklistBO bo = woChecklistDAO.getOneRaw(obj.getChecklistId());
        bo.setState("DONE");
        bo.setCompletedDate(completeDate);

        if (obj.getDnqtValue() != null)
            bo.setDnqtValue(obj.getDnqtValue());
        if (obj.getVtnetSentValue() != null)
            bo.setVtnetSentValue(obj.getVtnetSentValue());
        if (obj.getVtnetConfirmValue() != null)
            bo.setVtnetConfirmValue(obj.getVtnetConfirmValue());
        if (obj.getAprovedDocValue() != null)
            bo.setAprovedDocValue(obj.getAprovedDocValue());
        if (obj.getFinalValue() != null)
            bo.setFinalValue(obj.getFinalValue());

        if (obj.getDnqtDate() != null)
            bo.setDnqtDate(obj.getDnqtDate());
        if (obj.getVtnetSendDate() != null)
            bo.setVtnetSendDate(obj.getVtnetSendDate());
        if (obj.getVtnetConfirmDate() != null)
            bo.setVtnetConfirmDate(obj.getVtnetConfirmDate());
        if (obj.getAprovedDocDate() != null)
            bo.setAprovedDocDate(obj.getAprovedDocDate());
        if (bo.getChecklistOrder() == 5)
            bo.setFinalDate(new Date());

        if (obj.getAprovedPerson() != null)
            bo.setAprovedPerson(obj.getAprovedPerson());
        if (obj.getCompletePersonId() != null)
            bo.setCompletePersonId(obj.getCompletePersonId());
        if (obj.getCompletePersonName() != null)
            bo.setCompletePersonName(obj.getCompletePersonName());

        try {
            woChecklistDAO.updateObject(bo);
            WoBO wo = woDAO.getOneRaw(bo.getWoId());

            List<ImgChecklistDTO> lstImgs = obj.getLstImgs();

            if (lstImgs != null && lstImgs.size() > 0) {
                // Delete WO_MAPPING_ATTACH (old image)
                woDAO.removeAttachOfChecklist(bo.getWoId(), bo.getChecklistId());
                // Insert new image
                insertWoMappingAttach(wo.toDTO(), bo.getChecklistId(), lstImgs);
            }

            wo.setChecklistStep(bo.getChecklistOrder());

            if (bo.getChecklistOrder() == 4) {
                wo.setEndTime(completeDate);
                wo.setState(WAIT_TC_TCT);

                if (!StringUtils.isNullOrEmpty(obj.getEmailTcTct()))
                    wo.setEmailTcTct(obj.getEmailTcTct());
            }

            if (bo.getChecklistOrder() == 5) {
                wo.setState(OK);

                wo.setUserTcTctApproveWo(obj.getLoggedInUser());
                wo.setUpdateTcTctApproveWo(new Date());

                Date reportDate = calculateReportDate();
                wo.setApproveDateReportWo(reportDate);

                wo.setClosedTime(completeDate);
            }

            woDAO.updateObject(wo);
            WoDTO dto = wo.toDTO();
            dto.setWoTypeCode("HCQT");
            logWoWorkLogs(dto, "1", "Hoàn thành đầu việc: " + bo.getChecklistName(), gson.toJson(wo),
                    obj.getLoggedInUser());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String exportExcelHcqtWo(List<WoHcqtDTO> data) throws Exception {
        // ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        // String filePath = classloader.getResource("../" + "doc-template").getPath();
        // InputStream file = new BufferedInputStream(new FileInputStream(filePath +
        // "WO_XL_ExportWoHcqtTemplate.xlsx"));
        File template = createImportHCQTWoExcelTemplate();
        InputStream file = new BufferedInputStream(new FileInputStream(template));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        NumberFormat formatter = new DecimalFormat("#,###");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "WO_XL_ExportWoHcqt.xlsx");

        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
            XSSFCellStyle styleRight = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.CENTER);
            styleRight.setAlignment(HorizontalAlignment.RIGHT);
            int i = 3;
            for (WoHcqtDTO dto : data) {

                Row row = sheet.createRow(i++);

                // Cell cell = row.createCell(0, CellType.STRING);
                // cell.setCellValue("" + (i - 2));
                // cell.setCellStyle(styleNumber);

                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue((dto.getFtName() != null) ? dto.getFtName() : "");
                cell.setCellStyle(style);

                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getApWorkSrcName() != null)
                        ? dto.getApWorkSrcCode() + dto.getApWorkSrcName().substring(dto.getApWorkSrcName().indexOf("-"))
                        : "");
                cell.setCellStyle(style);

                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
                cell.setCellStyle(style);

                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getCatProvinceCode() != null) ? dto.getCatProvinceCode() : "");
                cell.setCellStyle(style);

                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getHcqtProjectName() != null) ? dto.getHcqtProjectName() : "");
                cell.setCellStyle(style);

                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getCnkv() != null) ? dto.getCnkv() : "");
                cell.setCellStyle(style);

                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getHcqtContractCode() != null) ? dto.getHcqtContractCode() : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getMoneyValue() != null) ? dto.getMoneyValue() : 0);
                cell.setCellStyle(style);

                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue(
                        (dto.getHshcReceiveDate() != null) ? dateFormatter.format(dto.getHshcReceiveDate()) : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getFinishDate() != null) ? dateFormatter.format(dto.getFinishDate()) : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getDnqtDate() != null) ? dateFormatter.format(dto.getDnqtDate()) : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getDnqtValue() != null) ? dto.getDnqtValue() : 0);
                cell.setCellStyle(style);

                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getDnqtPerson() != null) ? dto.getDnqtPerson() : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getVtnetSendDate() != null) ? dateFormatter.format(dto.getVtnetSendDate()) : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue(
                        (dto.getVtnetConfirmDate() != null) ? dateFormatter.format(dto.getVtnetConfirmDate()) : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(15, CellType.STRING);
                cell.setCellValue((dto.getVtnetConfirmPerson() != null) ? dto.getVtnetConfirmPerson() : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(16, CellType.STRING);
                cell.setCellValue(
                        (dto.getAprovedDocDate() != null) ? dateFormatter.format(dto.getAprovedDocDate()) : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(17, CellType.STRING);
                cell.setCellValue((dto.getVtnetConfirmValue() != null) ? dto.getVtnetConfirmValue() : 0);
                cell.setCellStyle(style);

                cell = row.createCell(18, CellType.STRING);
                cell.setCellValue((dto.getAprovedPerson() != null) ? dto.getAprovedPerson() : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(19, CellType.STRING);
                cell.setCellValue((dto.getProblemContent() != null) ? dto.getProblemContent() : "");
                cell.setCellStyle(styleRight);

                cell = row.createCell(20, CellType.STRING);
                cell.setCellValue((dto.getProblemName() != null) ? dto.getProblemName() : "");
                cell.setCellStyle(styleRight);

            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "WO_XL_ExportWoHcqt.xlsx");
        return path;
    }

    // Huypq-14102020-start
    public SysGroupDto getSysGroupNameById(Long id) {
        return woDAO.getSysGroupNameById(id);
    }
    // Huypq-14102020-end

    public File getImportFTResult(List<WoDTO> dtos) throws Exception {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_ImportFT_Result.xlsx";
        String outFileName = "Template_WO_XL_ImportFT.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                Sheet importSheet = workbook.getSheetAt(0);
                CellStyle errorStyle = importSheet.getWorkbook().createCellStyle();
                errorStyle.setFillForegroundColor(IndexedColors.RED.index);
                errorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

                int rowNo = 0;
                int stt = 0;
                for (WoDTO wo : dtos) {
                    stt = stt + 1;
                    Row row = importSheet.createRow(++rowNo);
                    row.createCell(0).setCellValue(stt);
                    row.createCell(1).setCellValue(wo.getWoCode());
                    row.createCell(2).setCellValue(wo.getApWorkSrcName());
                    row.createCell(3).setCellValue(wo.getContractCode());
                    row.createCell(4).setCellValue(wo.getStationCode());
                    row.createCell(5).setCellValue(wo.getConstructionCode());
                    row.createCell(6).setCellValue(wo.getMoneyValueString());
                    row.createCell(7).setCellValue(wo.getFinishDateStr());
                    row.createCell(8).setCellValue(wo.getFtName());
                    row.createCell(9).setCellValue(wo.getCustomField());
                }
                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }
        return outFile;
    }

    @Override
    public boolean declareThdtAddedValue(WoMappingChecklistDTO obj) {

        Long checklistId = obj.getId();
        Double addValue = obj.getAddedQuantityLength();

        WoMappingChecklistBO bo = woMappingChecklistDAO.getOneRaw(checklistId);

        Long woId = bo.getWoId();
        WoBO wo = woDAO.getOneRaw(woId);
        Double moneyValue = wo.getMoneyValue();
        Double approvedValue = woTaskMonthlyDAO.getApprovedCollected(woId);
        Double unapprovedValue = woTaskMonthlyDAO.getUnapprovedCollected(woId);

        if (approvedValue + unapprovedValue + addValue > moneyValue) {
            obj.setCustomField("Tổng giá trị tiền thu hồi không được vượt quá giá trị phải thu của WO! ");
            return false;
        }

        WoTaskMonthlyBO thisMonthTaskMonthly = woTaskMonthlyDAO.searchThisMonth(woId);

        if (thisMonthTaskMonthly != null && "1".equalsIgnoreCase(thisMonthTaskMonthly.getConfirm())) {
            obj.setCustomField("Tháng này đã được ghi nhận sản lượng! ");
            return false;
        }

        if (thisMonthTaskMonthly == null) {
            thisMonthTaskMonthly = new WoTaskMonthlyBO();
            thisMonthTaskMonthly.setWoTaskMonthlyId(0l);
            thisMonthTaskMonthly.setWoId(woId);
            thisMonthTaskMonthly.setCreatedDate(new Date());
            thisMonthTaskMonthly.setWoMappingChecklistId(checklistId);
            thisMonthTaskMonthly.setConfirm("0");
            thisMonthTaskMonthly.setStatus(1l);
            thisMonthTaskMonthly.setQuantity(addValue.doubleValue());
            woTaskMonthlyDAO.saveObject(thisMonthTaskMonthly);
        } else {
            Double currentQty = thisMonthTaskMonthly.getQuantity();
            thisMonthTaskMonthly.setQuantity(currentQty + addValue);
            woTaskMonthlyDAO.updateObject(thisMonthTaskMonthly);
        }

        return true;
    }

    public List<WoDTO> importApproveWo(String fileInput, List<String> listGroup, HttpServletRequest request)
            throws Exception {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        List<WoDTO> workLst = Lists.newArrayList();
        List<WoDTO> listWoDTOCheck = Lists.newArrayList();
        List<WoSimpleConstructionDTO> listConstrCheckHtct = Lists.newArrayList(); // Huypq-05072021-add
        String error = "";
        List<WoCdDTO> listWOcd = woDAO.getCdLevel1();
        boolean checkCD = false;
        boolean checkCDLevel5 = false;
        boolean isTcTct = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
                Constant.AdResourceKey.TC_TCT, request);
        boolean isTcBranch = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
                Constant.AdResourceKey.TC_BRANCH, request);
        String type = "";
        for (String domainData : listGroup) {
            for (WoCdDTO woCdDTO : listWOcd) {
                if (domainData.equals(woCdDTO.getSysGroupId().toString())) {
                    checkCD = true;
                }
            }
        }
        if (checkCD == true) {
            // TTHT
            type = "2";
        } else {
            // CNKT
            type = "1";
        }
        if (isTcTct) {
            type = "TCTCT";
        }
        if (isTcBranch) {
            type = "TCBRANCH";
        }
        boolean resul = true;
        try {
            File f = new File(fileInput);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<String> listCodeWO = Lists.newArrayList();
            List<String> listConstrCode = Lists.newArrayList(); // Huypq-05072021-add
            List<String> listContractCode = Lists.newArrayList(); // Huypq-27082021-add
            HashMap<String, String> mapWoCodeEmailTcTct = new HashMap<>();
            int count = 0;
            for (Row row : sheet) {
                WoDTO obj = new WoDTO();
                count++;
                if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                    String woCode = formatter.formatCellValue(row.getCell(1));
                    String woTypename = formatter.formatCellValue(row.getCell(2));
                    String workName = formatter.formatCellValue(row.getCell(3));
                    String contract = formatter.formatCellValue(row.getCell(4));
                    String station = formatter.formatCellValue(row.getCell(5));
                    String contrustion = formatter.formatCellValue(row.getCell(6));
                    String workItiem = formatter.formatCellValue(row.getCell(7));
                    String moneyValue = formatter.formatCellValue(row.getCell(8));
                    String finshDate = formatter.formatCellValue(row.getCell(9));
                    String endTime = formatter.formatCellValue(row.getCell(10));
                    String cdlevl2 = formatter.formatCellValue(row.getCell(11));
                    String ft = formatter.formatCellValue(row.getCell(12));
                    String approveWO = formatter.formatCellValue(row.getCell(13));
                    String reason = formatter.formatCellValue(row.getCell(14));
                    obj.setWoTypeName(woTypename);
                    obj.setApWorkSrcName(workName);
                    obj.setContractCode(contract);
                    obj.setConstructionCode(contrustion);
                    obj.setCatWorkItemTypeName(workItiem);
                    obj.setFinishDateStr(finshDate);
                    obj.setEndTimeStr(endTime);
                    obj.setStationCode(station);
                    obj.setCdLevel2Name(cdlevl2);
                    obj.setFtName(ft);
                    obj.setWoCode(woCode);
                    obj.setType(approveWO);
                    if (!StringUtils.isNullOrEmpty(moneyValue)) {
                        obj.setMoneyValue(Double.parseDouble(moneyValue));
                    }
                    // obj.setOpinionComment(reason);
                    obj.setText(reason);
                    obj.setUserCreated(user.getVpsUserInfo().getEmployeeCode());
                    obj.setLoggedInUser(user.getVpsUserInfo().getEmployeeCode());

                    // Nếu là tài chính trụ
                    if ("TCBRANCH".equalsIgnoreCase(type) && "1".equalsIgnoreCase(approveWO)) {
                        obj.setEmailTcTct(formatter.formatCellValue(row.getCell(15)));
                    }
                    mapWoCodeEmailTcTct.put(woCode, formatter.formatCellValue(row.getCell(15)));
                    workLst.add(obj);
                    listCodeWO.add(woCode);
                    listConstrCode.add(contrustion); // Huypq-05072021-add
                    listContractCode.add(contract != null ? contract.trim().toUpperCase() : null);
                }
            }
            if (listCodeWO.size() > 0) {
                listWoDTOCheck = woDAO.getWoIDByCode(listCodeWO, type);
                listConstrCheckHtct = woDAO.getDetailConstructionByListCode(listConstrCode); // Huypq-05072021-add
            }
            Map<String, WoDTO> mapWoDTO = new HashMap<>();
            for (WoDTO woDTOCheck : listWoDTOCheck) {
                mapWoDTO.put(woDTOCheck.getWoCode(), woDTOCheck);
            }

            // Huypq-05072021-start
            Map<String, Long> mapConstrCheck = new HashMap<>();
            for (WoSimpleConstructionDTO woDTOCheck : listConstrCheckHtct) {
                mapConstrCheck.put(woDTOCheck.getConstructionCode(), woDTOCheck.getCheckHtct());
            }
            // Huy-end

            // Huypq30_20210728_tao map hop dong lay ra id hop dong,don vi quyet toan hop
            // dong_start
            Map<Long, String> mapContractUnitSettlement = cntContractDAO
                    .mapContractUnitSettlementByLstContract(listContractCode);
            // Huypq30_20210728_tao map hop dong lay ra id hop dong,don vi quyet toan hop
            // dong_end
            if (workLst.size() > 0) {
                for (WoDTO woDTO : workLst) {
                    String code = woDTO.getWoCode();
                    WoDTO woDTOCheck = mapWoDTO.get(code);
                    Long isConstrHTCT = mapConstrCheck.get(woDTO.getConstructionCode()); // Huypq-05072021-add
                    if (woDTOCheck != null) {
                        woDTO.setWoId(woDTOCheck.getWoId());
                        woDTO.setWoTypeCode(woDTOCheck.getWoTypeCode());
                        // Duonghv13 add 13122021
                        if (StringUtils.isNullOrEmpty(woDTO.getType()) || woDTO.getType().length() == 0) {
                            woDTO.setCustomField("Giá trị ghi nhận/ từ chối không được để trống !");
                        }
                        // Duonghv13 end 13122021
                        if (woDTO.getType() != null && "TCBRANCH".equalsIgnoreCase(type)
                                && "1".equalsIgnoreCase(woDTO.getType())
                                && StringUtils.isNullOrEmpty(woDTO.getEmailTcTct())) {
                            woDTO.setCustomField("Chưa nhập người phê duyệt !");
                        }
                        // Huypq-01112021-start open comment theo yêu cầu
                        if ("THICONG".equalsIgnoreCase(woDTO.getWoTypeCode())
                                && "CD_OK".equalsIgnoreCase(woDTOCheck.getState())
                                && ("1".equalsIgnoreCase(woDTO.getType()) || "0".equalsIgnoreCase(woDTO.getType()))) {
                            DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
                            String currentDate = dateFormat.format(new Date());

                            String logTime = woDAO.getLogTimeByWoId(woDTO.getWoId(), "Điều phối duyệt OK");

                            if (!currentDate.equalsIgnoreCase(logTime)) {
//								if (woDTOCheck.getApWorkSrc() != null
//										&& (woDTOCheck.getApWorkSrc() != 6l || (woDTOCheck.getApWorkSrc() == 6l
//												&& !woDTO.getCatWorkItemTypeName().equalsIgnoreCase("Khởi công")))) {
                                if (!woDTO.getCatWorkItemTypeName().equalsIgnoreCase("Khởi công trạm HTCT") && !woDTO.getCatWorkItemTypeName().equalsIgnoreCase("Khởi công")) {
                                    woDTO.setCustomField(
                                            "Chỉ được duyệt hoặc từ chối WO trong cùng tháng mà CD CNKT đã duyệt điều phối OK ! ");
                                }
                            }
                        }

                        // Loai UCTT, FT chua xac nhan vat tu thi ko duoc duyet
                        if ("UCTT".equalsIgnoreCase(woDTOCheck.getWoTypeCode())) {
                            if (woDTOCheck.getWoOrderConfirm() == null || woDTOCheck.getWoOrderConfirm() == 0) {
                                woDTO.setCustomField("FT chưa xác nhận vật tư !");
                            }
                        }

                        if (org.apache.commons.lang3.StringUtils.isNotBlank(woDTOCheck.getWoTypeCode())
                                && woDTOCheck.getWoTypeCode().equalsIgnoreCase("HSHC")) {
                            if (woDTOCheck.getState().equals("CD_OK")
                                    && !(woDTOCheck.getApWorkSrc() == 4l && woDTOCheck.getTrId() != null)) { // CD_OK
                                // với
                                // wo
                                // HSHC
                                // không
                                // phải
                                // của
                                // ĐTHT
                                boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(
                                        Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY, request);
                                if (!checkRoleApproveHshc) {
                                    woDTO.setCustomField("Không có quyền phê duyệt hoàn thành đối với WO này !");
                                } else {
                                    if (woDTO.getMoneyValue() != null && woDTO.getMoneyValue() > 10000000000d) {
                                        woDTO.setCustomField("Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!");
                                    }
                                }
                            }
                            // Huypq-22102021-start code check validate quyền với wo hshc ĐTHT
                            else {
                                if (woDTOCheck.getApWorkSrc() == 4l && woDTOCheck.getTrId() != null) {
                                    if (woDTOCheck.getState().equals("CD_OK")) {
                                        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(
                                                Constant.OperationKey.APPROVED, Constant.AdResourceKey.HTCT_HSHC,
                                                request);
                                        if (!checkRoleApproveHshc) {
                                            woDTO.setCustomField(
                                                    "Không có quyền phê duyệt hoàn thành đối với WO này !");
                                        } else {
                                            if (woDTO.getMoneyValue() != null
                                                    && woDTO.getMoneyValue() > 10000000000d) {
                                                woDTO.setCustomField("Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!");
                                            }
                                        }
                                    }

                                    if (woDTOCheck.getState().equals("WAIT_PQT")) {
                                        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(
                                                Constant.OperationKey.APPROVED, Constant.AdResourceKey.REVENUE_SALARY,
                                                request);
                                        if (!checkRoleApproveHshc) {
                                            woDTO.setCustomField(
                                                    "Không có quyền phê duyệt hoàn thành đối với WO này !");
                                        } else {
                                            if (woDTO.getMoneyValue() != null
                                                    && woDTO.getMoneyValue() > 10000000000d) {
                                                woDTO.setCustomField("Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!");
                                            }
                                        }
                                    }

                                    if (woDTOCheck.getState().equals("WAIT_TTDTHT")) {
                                        boolean checkRoleApproveHshc = VpsPermissionChecker.hasPermission(
                                                Constant.OperationKey.APPROVE_DTHT, Constant.AdResourceKey.HTCT_HSHC,
                                                request);
                                        if (!checkRoleApproveHshc) {
                                            woDTO.setCustomField(
                                                    "Không có quyền phê duyệt hoàn thành đối với WO này !");
                                        } else {
                                            if (woDTO.getMoneyValue() != null
                                                    && woDTO.getMoneyValue() > 10000000000d) {
                                                woDTO.setCustomField("Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!");
                                            }
                                        }
                                    }
                                }
                            }
                            // Huy-end

                            //Duyệt Wo HTCT
                            if (woDTOCheck.getApWorkSrc() == 4l && woDTOCheck.getTrId() != null) {
                                if (woDTOCheck.getState().equals(CD_OK)) {
                                    woDTO.setState(WAIT_PQT);
                                }

                                if (woDTOCheck.getState().equals(WAIT_PQT)) {
                                    woDTO.setState(RECEIVED_PQT);
                                }

                                if (woDTOCheck.getState().equals(RECEIVED_PQT)) {
                                    woDTO.setState(WAIT_TTDTHT);
                                }

                                if (woDTOCheck.getState().equals(WAIT_TTDTHT)) {
                                    woDTO.setState(RECEIVED_TTDTHT);
                                }

                                if (woDTOCheck.getState().equals(RECEIVED_TTDTHT)) {
                                    woDTO.setState(WAIT_TC_TCT);
                                    if (!StringUtils.isNullOrEmpty(mapWoCodeEmailTcTct.get(code))) {
                                        woDTO.setEmailTcTct(mapWoCodeEmailTcTct.get(code));
                                    } else {
                                        woDTO.setCustomField("TC TCT không được để trống !");
                                    }
                                }
                                if (woDTOCheck.getState().equals(WAIT_TC_TCT)) {
                                    woDTO.setState(OK);
                                }
                            } else {
                                //Từ chối Wo HTCT
                                if (woDTOCheck.getApWorkSrc() == 4l && woDTOCheck.getTrId() != null) {
                                    if (woDTOCheck.getState().equals(WAIT_PQT)) {
                                        woDTO.setState(REJECT_PQT);
                                    }

                                    if (woDTOCheck.getState().equals(WAIT_TTDTHT)) {
                                        woDTO.setState(REJECT_TTDTHT);
                                    }
                                } else {
                                    // Huypq30_20210728_check cos phai la HSHC cho cong trinh HTCT hay khong_start
                                    if (woDTO.getType().equals("1") && type.equals("2") && isConstrHTCT != null
                                            && isConstrHTCT == 1l) {
                                        woDTO.setState("OK");
                                    } else {
                                        // Huypq30_20210728_check cos phai la HSHC cho cong trinh HTCT hay khong_end
                                        String unitSettlement = mapContractUnitSettlement.get(woDTOCheck.getContractId());
                                        boolean isHdNdt = woDAO.checkContractIsNDT(woDTOCheck.getContractId());
                                        boolean isHdHtct = woDAO.checkContractIsHTCT(woDTOCheck.getContractId());

                                        if (isHdNdt || isHdHtct) {
                                            if (woDTO.getType().equals("1") && type.equals("1")) {
                                                woDTO.setState("CD_OK");
                                            }
                                            if (woDTO.getType().equals("1") && type.equals("2")) {
                                                if (unitSettlement != null && !unitSettlement.equals("7")) {
                                                    woDTO.setState("OK");
                                                } else {
                                                    woDTO.setState("WAIT_TC_BRANCH");
                                                }
                                            }
                                            if (woDTO.getType().equals("1") && type.equals("TCBRANCH")) {
                                                woDTO.setState("WAIT_TC_TCT");
                                            }
                                            if (woDTO.getType().equals("1") && type.equals("TCTCT")) { // Huypq-05072021-edit
                                                woDTO.setState("OK");
                                                woDTO.setCheckHTCT("1");
                                            }
                                            if (woDTO.getType().equals("0")
                                                    && !StringUtils.isNullOrEmpty(woDTO.getText())) {
                                                if (woDTO.getType().equals("0") && type.equals("1")) {
                                                    woDTO.setState("CD_NG");
                                                }
                                                if (woDTO.getType().equals("0") && type.equals("2")) {
                                                    woDTO.setState("NG");
                                                }
                                            }
                                            if (woDTO.getType().equals("0") && StringUtils.isNullOrEmpty(woDTO.getText())) {
                                                woDTO.setCustomField("Lý do từ chối không được trống !");
                                            }

                                        } else {
                                            if (woDTO.getType().equals("1") && type.equals("1")) {
                                                woDTO.setState("CD_OK");
                                            }
                                            if (woDTO.getType().equals("1") && type.equals("2")) {
                                                if (unitSettlement != null && !unitSettlement.equals("7")) {
                                                    woDTO.setState("OK");
                                                } else {
                                                    woDTO.setState("WAIT_TC_BRANCH");
                                                }
                                            }
                                            if (woDTO.getType().equals("1") && type.equals("TCBRANCH")) {
                                                woDTO.setState("WAIT_TC_TCT");
                                            }
                                            if (woDTO.getType().equals("1") && type.equals("TCTCT")) { // Huypq-05072021-edit
                                                woDTO.setState("OK");
                                                woDTO.setCheckHTCT("1");
                                            }
                                            if (woDTO.getType().equals("0")) {
                                                if (StringUtils.isNullOrEmpty(woDTO.getText())) {
                                                    woDTO.setCustomField("Lý do từ chối không được trống !");
                                                } else {
                                                    if (type.equals("1")) { // CNKT từ chối
                                                        woDTO.setState("CD_NG");
                                                    } else if (type.equals("2")) { // Trụ từ chối
                                                        woDTO.setState("NG");
                                                    } else if (type.equals("TCBRANCH")) { // TC trụ từ chối
                                                        woDTO.setState("TC_BRANCH_REJECTED");
                                                    } else if (type.equals("TCTCT")) { // TC TCT từ chối
                                                        woDTO.setState("TC_TCT_REJECTED");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (woDTO.getType().trim().equals("0") || woDTO.getType().trim().equals("1")) {
                                if (!StringUtils.isNullOrEmpty(woDTO.getType())) {
                                    if (woDTO.getType().equals("1") && type.equals("1")) {
                                        if (woDTOCheck.getWoTypeCode().equals("5S")) {
                                            woDTO.setState("OK");
                                        } else {
                                            woDTO.setState("CD_OK");
                                        }
                                    }
                                    if (woDTO.getType().equals("1") && type.equals("2")) {
                                        woDTO.setState("OK");
                                    }
                                    if (woDTO.getType().equals("1") && type.equals("TCBRANCH")) {
                                        woDTO.setState("WAIT_TC_TCT");
                                    }
                                    if (woDTO.getType().equals("1") && type.equals("TCTCT")) {
                                        woDTO.setState("OK");
                                    }
                                    if (woDTO.getType().equals("0") && !StringUtils.isNullOrEmpty(woDTO.getText())) {
                                        if (woDTO.getType().equals("0") && type.equals("1")) {
                                            woDTO.setState("CD_NG");
                                        } else {
                                            woDTO.setState("NG");
                                        }
                                    }
                                    if (woDTO.getType().equals("0") && StringUtils.isNullOrEmpty(woDTO.getText())) {
                                        woDTO.setCustomField("Lý do từ chối không được trống !");
                                    }
                                }
                            } else {
                                woDTO.setCustomField("Ghi nhận  hoặc từ chối không tồn tại hoặc không được bỏ trống !");
                            }
                        }

                        if ("TMBT".equalsIgnoreCase(woDTOCheck.getWoTypeCode())
                                || "DOANHTHU_DTHT".equalsIgnoreCase(woDTOCheck.getWoTypeCode())) {
                            woDTO.setCustomField("Không hỗ trợ import phê duyệt đối với loại WO này !");
                        }

                    } else {
                        woDTO.setCustomField("Mã WO không tồn tại hoặc không có quyền phê duyệt!");
                    }
                }
            }
            return workLst;
        } catch (NullPointerException pointerException) {
            pointerException.printStackTrace();
            throw new Exception(error);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(error);
        }
    }

    public File getImportApprovreResult(List<WoDTO> dtos) throws Exception {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_Approve_Result.xlsx";
        String outFileName = "Template_WO_XL_Approve.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                Sheet importSheet = workbook.getSheetAt(0);
                CellStyle errorStyle = importSheet.getWorkbook().createCellStyle();
                errorStyle.setFillForegroundColor(IndexedColors.RED.index);
                errorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

                int rowNo = 1;
                int stt = 0;
                for (WoDTO wo : dtos) {
                    stt = stt + 1;
                    Row row = importSheet.createRow(++rowNo);
                    row.createCell(0).setCellValue(stt);
                    row.createCell(1).setCellValue(wo.getWoCode());
                    row.createCell(2).setCellValue(wo.getWoTypeName());
                    row.createCell(3).setCellValue(wo.getApWorkSrcName());
                    row.createCell(4).setCellValue(wo.getContractCode());
                    row.createCell(5).setCellValue(wo.getStationCode());
                    row.createCell(6).setCellValue(wo.getConstructionCode());
                    row.createCell(7).setCellValue(wo.getCatWorkItemTypeName());
                    row.createCell(8).setCellValue(wo.getMoneyValue().toString());
                    row.createCell(9).setCellValue(wo.getFinishDateStr());
                    row.createCell(10).setCellValue(wo.getEndTimeStr());
                    row.createCell(11).setCellValue(wo.getCdLevel2Name());
                    row.createCell(12).setCellValue(wo.getFtName());
                    row.createCell(13).setCellValue(wo.getType());
                    row.createCell(14).setCellValue(wo.getOpinionComment());
                    row.createCell(15).setCellValue(wo.getEmailTcTct());
                    row.createCell(16).setCellValue(wo.getCustomField());
                }
                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }
        return outFile;
    }

    public String exportexcelScheduleConfig(String loggedInUser, List<String> groupIdList) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "WO_XL_ScheduleWiConfig.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "WO_XL_ScheduleWiConfigTemplate.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);

        // cong viec dinh ky
        WoScheduleWorkItemDTO dto = new WoScheduleWorkItemDTO();
        List<WoScheduleWorkItemDTO> listTrDto = woScheduleWorkItemDAO.doSearch(dto);
        XSSFSheet sheet2 = workbook.getSheetAt(1);
        if (listTrDto != null && !listTrDto.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet2);
            int i = 1;
            for (WoScheduleWorkItemDTO record : listTrDto) {
                Row row = sheet2.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(
                        (record.getWorkItemCode() != null)
                                ? record.getWorkItemCode() + "-"
                                + ((record.getWorkItemName() != null) ? record.getWorkItemName() : " ")
                                : "");
                cell.setCellStyle(style);

            }
        }
        // cd level 2
        XSSFSheet sheet3 = workbook.getSheetAt(2);
        List<WoSimpleSysGroupDTO> cdLv2s = woDAO.getCnktCdLevel2();
        if (cdLv2s != null && !cdLv2s.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet3);
            int i = 1;
            for (WoSimpleSysGroupDTO record : cdLv2s) {
                Row row = sheet3.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue(record.getSysGroupId());
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(
                        record.getSysGroupId() + "-" + (record.getGroupName() != null ? record.getGroupName() : " "));
                cell.setCellStyle(style);
            }
        }
        // loai chu ky
        XSSFSheet sheet4 = workbook.getSheetAt(3);
        List<String> cycleTypeList = Arrays.asList("Ngày", "Tuần", "Tháng");
        if (cdLv2s != null && !cdLv2s.isEmpty()) {
            List<WoNameDTO> listWoName = woScheduleWorkItemDAO.getWoNameByWoTypeNLMT();
            XSSFCellStyle style = ExcelUtils.styleText(sheet4);
            int i = 1;
            for (String record : cycleTypeList) {
                Row row = sheet4.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue(record);
                cell.setCellStyle(style);
            }

            int j = 1;
            for (WoNameDTO record : listWoName) {
                Row row = sheet4.getRow(j++);
                if (row == null) {
                    row = sheet4.createRow(j++);
                }
                Cell cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(record.getWoTypeName() != null ? record.getWoTypeName() : "");
                cell.setCellStyle(style);

                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(record.getName() != null ? record.getName() : "");
                cell.setCellStyle(style);
            }
        }
        // get tr
        XSSFSheet sheet5 = workbook.getSheetAt(4);
        WoTrDTO trDTO = new WoTrDTO();
        trDTO.setLoggedInUser(loggedInUser);
        trDTO.setPage((long) 1);
        trDTO.setPageSize(99999);
        List<WoTrDTO> listWoTr = trDAO.doSearch(trDTO, groupIdList, AVAILABLE_TYPE, true, true);
        if (listWoTr != null && !listWoTr.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet5);
            int i = 1;
            for (WoTrDTO record : listWoTr) {
                Row row = sheet5.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue(record.getTrId());
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(record.getTrId() + "-" + (record.getTrCode() != null ? record.getTrCode() : " "));
                cell.setCellStyle(style);
            }
        }

        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "WO_XL_ScheduleWiConfigTemplate.xlsx");
        return path;
    }

    public List<WoScheduleConfigDTO> importFileScheduleConfig(String fileInput, HttpServletRequest request)
            throws Exception {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        List<WoScheduleConfigDTO> workLst = Lists.newArrayList();
        List<WoScheduleConfigDTO> listWoDTOCheck = Lists.newArrayList();
        String error = "";
        boolean resul = true;
        //Huypq-23112021-start
        List<WoNameDTO> listWoName = woScheduleWorkItemDAO.getWoNameByWoTypeNLMT();
        HashMap<String, WoNameDTO> mapWoName = new HashMap<>();
        for (WoNameDTO name : listWoName) {
            mapWoName.put(name.getName().toUpperCase(), name);
        }
        //Huy-end
        try {
            File f = new File(fileInput);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<String> listCodeWO = Lists.newArrayList();
            int count = 0;
            for (Row row : sheet) {
                WoScheduleConfigDTO obj = new WoScheduleConfigDTO();
                count++;
                if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                    obj.setScheduleConfigCode(formatter.formatCellValue(row.getCell(0)));
                    obj.setScheduleConfigName(formatter.formatCellValue(row.getCell(1)));
                    String startTimeString = formatter.formatCellValue(row.getCell(2));
                    String endTimeString = formatter.formatCellValue(row.getCell(3));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date startTime = null;
                    if (!Strings.isNullOrEmpty(startTimeString)) {
                        startTime = dateFormat.parse(startTimeString);
                    }
                    Date endTime = null;
                    if (!Strings.isNullOrEmpty(endTimeString)) {
                        endTime = dateFormat.parse(endTimeString);
                    }
                    obj.setStartTime(startTime);
                    obj.setWoTime(startTime);
                    obj.setEndTime(endTime);
                    if (formatter.formatCellValue(row.getCell(4)) != "") {
                        obj.setCycleLength(Long.parseLong(formatter.formatCellValue(row.getCell(4))));
                    } else {
                        obj.setCycleLength(null);
                    }
                    if (formatter.formatCellValue(row.getCell(5)) != "") {
                        obj.setQuotaTime(Long.parseLong(formatter.formatCellValue(row.getCell(5))));
                    } else {
                        obj.setQuotaTime(null);
                    }
                    String wiScheduleCode = "";
                    if (formatter.formatCellValue(row.getCell(6)) != "") {
                        String wiScheduleCodeName = formatter.formatCellValue(row.getCell(6));
                        String[] split1 = wiScheduleCodeName.split("-");
                        wiScheduleCode = split1[0];
                    }
                    WoScheduleWorkItemDTO woScheduleWorkItemDTO = woScheduleWorkItemDAO
                            .getIdInfoWorkItem(wiScheduleCode);
                    String idCdLevel2 = "";
                    String nameCdLevel2 = "";
                    if (formatter.formatCellValue(row.getCell(7)) != "") {
                        String cdLevel2IdName = formatter.formatCellValue(row.getCell(7));
                        String[] split2 = cdLevel2IdName.split("-");
                        idCdLevel2 = split2[0];
                        for (int i = 1; i < split2.length; i++) {
                            if (i < split2.length - 1) {
                                nameCdLevel2 = split2[i].concat("-").concat(split2[i + 1]);
                            }
                        }
                    }
                    Long cycleType = null;
                    if (row.getCell(8).toString() != "") {
                        if ("Ngày".equalsIgnoreCase(row.getCell(8).toString())) {
                            cycleType = 1L;
                        } else {
                            if ((row.getCell(8).toString()).equalsIgnoreCase("Tuần"))
                                cycleType = 2L;
                            else
                                cycleType = 3L;
                        }
                    }
                    String idTR = "";
                    String nameTR = "";
                    if (formatter.formatCellValue(row.getCell(9)) != "") {
                        String tRIdName = formatter.formatCellValue(row.getCell(9));
                        String[] split3 = tRIdName.split("-");
                        idTR = split3[0];
                        String[] splitNameTR = tRIdName.split(split3[0].concat("-"));
                        nameTR = splitNameTR[1];
                    }
                    //Huypq-23112021-start
                    if (formatter.formatCellValue(row.getCell(10)) != "") {
                        String woName = formatter.formatCellValue(row.getCell(10));
                        WoNameDTO woNameDto = mapWoName.get(woName.toUpperCase());
                        obj.setWoTypeId(woNameDto.getWoTypeId());
                        obj.setWoNameId(woNameDto.getId());
                    }
                    //Huy-end
                    obj.setScheduleWorkItemId(
                            !Objects.isNull(woScheduleWorkItemDTO) ? woScheduleWorkItemDTO.getWoWorkItemId() : null);
                    obj.setCdLevel2(idCdLevel2);
                    obj.setCdLevel2Name(nameCdLevel2);
                    obj.setCycleType(cycleType);
                    if (idTR != "") {
                        obj.setTrId(Long.parseLong(idTR));
                    } else {
                        obj.setTrId(null);
                    }
                    obj.setWoTRCode(nameTR);
                    obj.setUserCreated(user.getEmployeeCode());
                    obj.setCdLevel1("270120");
                    obj.setCdLevel1Name("TTVHKT - Trung tâm vận hành khai thác");
                    workLst.add(obj);
                    // listCodeWO.add(woCode);
                }
            }
            // if (listCodeWO.size() > 0) {
            // listWoDTOCheck = woDAO.getWoIDByCode(listCodeWO,"0");
            // }
            if (workLst.size() > 0) {
                List errorCustom = new ArrayList<>();
                for (WoScheduleConfigDTO woDTO : workLst) {
                    if (woDTO.getScheduleConfigName() == "")
                        errorCustom.add("Tên cấu hình không được để trống");
                    if (woDTO.getScheduleConfigCode() != "") {
                        boolean checkExistCode = woScheduleConfigDAO
                                .checkExistScheduleConfigCode(woDTO.getScheduleConfigCode());
                        if (checkExistCode == false) {
                            woDTO.setCustomField("Mã cấu hình công việc định kỳ đã tồn tại!");
                            continue;
                        }
                    } else {
                        errorCustom.add("Mã cấu hình không được để trống");
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentDate = new Date();
                    if (woDTO.getStartTime() != null && woDTO.getEndTime() != null) {
                        if (DateTimeComparator.getDateOnlyInstance().compare(woDTO.getStartTime(), currentDate) < 0) {
                            woDTO.setCustomField("Ngày bắt đầu nhỏ hơn ngày hiện tại!");
                            continue;
                        }
                        if (DateTimeComparator.getDateOnlyInstance().compare(woDTO.getEndTime(), currentDate) <= 0) {
                            woDTO.setCustomField("Ngày kết thúc nhỏ hơn ngày hiện tại!");
                            continue;
                        }
                        if (DateTimeComparator.getDateOnlyInstance().compare(woDTO.getEndTime(),
                                woDTO.getStartTime()) < 0) {
                            woDTO.setCustomField("Ngày kết thúc nhỏ hơn ngày bắt đầu!");
                            continue;
                        }
                    } else {
                        if (woDTO.getStartTime() == null)
                            errorCustom.add("Ngày bắt đầu không được để trống");
                        if (woDTO.getEndTime() == null)
                            errorCustom.add("Ngày kết không được để trống");
                    }
                    if (woDTO.getScheduleWorkItemId() != null) {
                        WoScheduleWorkItemDTO dto = woScheduleWorkItemDAO
                                .getOneInfoWorkItem(woDTO.getScheduleWorkItemId());
                        boolean checkExistWorkItemCode = woScheduleWorkItemDAO
                                .checkExistWorkItemCode(dto.getWorkItemCode());
                        if (checkExistWorkItemCode == true) {
                            woDTO.setCustomField("Mã công việc định kỳ không tồn tại!");
                            continue;
                        }
                    } else {
                        errorCustom.add("Công việc định kỳ không được để trống");
                    }
                    if (woDTO.getCdLevel2() != "") {
                        boolean checkExistCDLevel2 = woDAO.checkExistCDLevel2(Long.parseLong(woDTO.getCdLevel2()));
                        if (checkExistCDLevel2 == false) {
                            woDTO.setCustomField("CD level 2 không tồn tại!");
                            continue;
                        }
                    } else {
                        errorCustom.add("CD level 2 không được để trống");
                    }
                    if (woDTO.getTrId() != null) {
                        WoTrDTO trDTO = new WoTrDTO();
                        trDTO.setLoggedInUser(user.getEmployeeCode());
                        trDTO.setPage((long) 1);
                        trDTO.setPageSize(99999);
                        List<String> groupIdList = new ArrayList<>();
                        String groupIdDomain = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                                Constant.AdResourceKey.WOXL, request);
                        groupIdList = ConvertData.convertStringToList(groupIdDomain, ",");
                        List<WoTrDTO> listWoTr = trDAO.doSearch(trDTO, groupIdList, AVAILABLE_TYPE, true, true);
                        // c1
                        WoTrDTO woTrDTO = listWoTr.stream().filter(x -> woDTO.getTrId().equals(x.getTrId())).findAny()
                                .orElse(null);
                        if (Objects.isNull(woTrDTO))
                            woDTO.setCustomField("Mã TR không tồn tại!");
                    } else {
                        errorCustom.add("Mã TR không được để trống");
                    }

                    if (woDTO.getWoNameId() == null) {
                        errorCustom.add("Tên WO không được để trống");
                    }

                    if (errorCustom.size() > 0) {
                        String listError = "";
                        for (int i = 0; i < errorCustom.size(); i++) {
                            listError = errorCustom.get(i) + ", ";
                        }
                        woDTO.setCustomField(listError);
                    }
                }
            }

            return workLst;

        } catch (NullPointerException pointerException) {
            throw new Exception(error);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(error);
        }
    }

    @Override
    public boolean createScheduleWoConfig(WoScheduleConfigDTO woDto) {
        try {
            woDto.setStatus(1);
            woDto.setState(1L);
            woDto.setCreatedDate(new Date());
            woScheduleConfigDAO.save(woDto.toModel());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public File getImportScheduleConfigResult(List<WoScheduleConfigDTO> dtos) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_ImportScheduleConfig_Result.xlsx";
        String outFileName = "Template_WO_XL_ImportScheduleConfig.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                Sheet importSheet = workbook.getSheetAt(0);
                CellStyle errorStyle = importSheet.getWorkbook().createCellStyle();
                errorStyle.setFillForegroundColor(IndexedColors.RED.index);
                errorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

                int rowNo = 0;
                int stt = 0;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                for (WoScheduleConfigDTO wo : dtos) {
                    stt = stt + 1;
                    Row row = importSheet.createRow(++rowNo);
                    row.createCell(0).setCellValue(stt);
                    row.createCell(2).setCellValue(wo.getScheduleConfigName() != "" ? wo.getScheduleConfigName() : "");
                    row.createCell(1).setCellValue(wo.getScheduleConfigCode() != "" ? wo.getScheduleConfigCode() : "");
                    if (wo.getStartTime() != null) {
                        row.createCell(3).setCellValue(formatter.format(wo.getStartTime()));
                    } else {
                        row.createCell(3).setCellValue("");
                    }
                    if (wo.getEndTime() != null) {
                        row.createCell(4).setCellValue(formatter.format(wo.getEndTime()));
                    } else {
                        row.createCell(4).setCellValue("");
                    }
                    if (wo.getCycleLength() != null) {
                        row.createCell(5).setCellValue(wo.getCycleLength());
                    } else {
                        row.createCell(5).setCellValue("");
                    }
                    if (wo.getQuotaTime() != null) {
                        row.createCell(6).setCellValue(wo.getQuotaTime());
                    } else {
                        row.createCell(6).setCellValue("");
                    }
                    if (wo.getScheduleWorkItemId() != null) {
                        WoScheduleWorkItemDTO woScheduleWorkItemDTO = woScheduleWorkItemDAO
                                .getOneInfoWorkItem(wo.getScheduleWorkItemId());
                        row.createCell(7).setCellValue(woScheduleWorkItemDTO.getWorkItemName());
                    } else {
                        row.createCell(7).setCellValue("");
                    }
                    if (wo.getCdLevel2Name() != "") {
                        row.createCell(8).setCellValue(wo.getCdLevel2Name());
                    } else {
                        row.createCell(8).setCellValue("");
                    }
                    if (wo.getCycleType() != null) {
                        row.createCell(9).setCellValue(
                                wo.getCycleType() == 1 ? "Ngày" : (wo.getCycleType() == 2 ? "Tuần" : "Tháng"));
                    } else {
                        row.createCell(9).setCellValue("");
                    }
                    if (wo.getWoTRCode() != "") {
                        row.createCell(10).setCellValue(wo.getWoTRCode());
                    } else {
                        row.createCell(10).setCellValue("");
                    }
                    if (wo.getCustomField() != "") {
                        row.createCell(11).setCellValue(wo.getCustomField());
                    } else {
                        row.createCell(11).setCellValue("");
                    }
                }
                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }
        return outFile;
    }

    public DataListDTO doSearchReportWoTr(ReportWoTrDTO obj) {
        List<ReportWoTrDTO> ls = new ArrayList<>();
        if (obj.getType().equals("1")) {
            ls = trDAO.doSearchReportWoTr(obj);
        }
        if (obj.getType().equals("2")) {
            ls = trDAO.doSearchReportWoGroup(obj);
        }
        if (obj.getType().equals("3")) {
            ls = trDAO.doSearchReportWoPro(obj);
        }
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    private boolean checkSameWo(WoDTO dto) {
        WoDTO checkDto = new WoDTO();
        String errorMsg = "";
        checkDto.setWoTypeId(dto.getWoTypeId());

        if ("XLSC".equalsIgnoreCase(dto.getWoTypeCode()) || "UCTT".equalsIgnoreCase(dto.getWoTypeCode()))
            return false;

        if ("HSHC".equalsIgnoreCase(dto.getWoTypeCode()) && dto.getCatConstructionTypeId() != null
                && dto.getCatConstructionTypeId() == 8)
            return false;

        if ("HCQT".equalsIgnoreCase(dto.getWoTypeCode())) {
            checkDto.setStationCode(dto.getStationCode());
            checkDto.setHcqtContractCode(dto.getHcqtContractCode());
            errorMsg = "Đã tồn tại wo hoàn công quyết toán tương ứng cho trạm này trong cùng hợp đồng; ";
        } else if ("TMBTHTTC".equalsIgnoreCase(dto.getWoTypeCode())) {
            if (StringUtils.isNullOrEmpty(dto.getStationCode()))
                return false;
            checkDto.setStationCode(dto.getStationCode());
            errorMsg = SAME_STATION;

            WoSimpleStationDTO station = trDAO.getStationByCode(dto.getStationCode());
            if (station.getCompleteStatus() != null) {
                errorMsg = STATION_COMPLETE;
                dto.setCustomField(errorMsg);
                return true;
            }

        } else if ("THDT".equalsIgnoreCase(dto.getWoTypeCode())) {
            checkDto.setContractId(dto.getContractId());
            checkDto.setMoneyFlowBill(dto.getMoneyFlowBill());
            // errorMsg = "Hợp đồng và hóa đơn này đã có WO thu tiền tương ứng. ";
        } else if ("AIO".equalsIgnoreCase(dto.getWoTypeCode())) {
            checkDto.setTrCode(dto.getTrCode());
            checkDto.setTrId(dto.getTrId());
            errorMsg = "Hợp đồng AIO đã có WO tương ứng. ";
        } else {
            if (dto.getConstructionId() == null)
                return false;
            checkDto.setConstructionId(dto.getConstructionId());

            if ("QUYLUONG".equalsIgnoreCase(dto.getWoTypeCode())) {
                errorMsg = SAME_CONSTRUCTION_SALARY_FUND;
            } else if ("HSHC".equalsIgnoreCase(dto.getWoTypeCode())) {
                errorMsg = SAME_WO_HSHC;
            } else if ("HCQT".equalsIgnoreCase(dto.getWoTypeCode())) {
                errorMsg = SAME_HCQT_WO;
            } else if ("DOANHTHU".equalsIgnoreCase(dto.getWoTypeCode())) {
                errorMsg = SAME_CONSTRUCTION_MONEY_INCOME;
            } else if ("THICONG".equalsIgnoreCase(dto.getWoTypeCode()) && dto.getCatWorkItemTypeId() != null) {
                checkDto.setCatWorkItemTypeId(dto.getCatWorkItemTypeId());
                errorMsg = SAME_WO;
            }
        }

        List<WoDTO> lst = woDAO.doSearch(checkDto, new ArrayList<>(), false, false, new ArrayList<>());
        if (lst.size() > 0) {
            if ("THDT".equalsIgnoreCase(dto.getWoTypeCode())) {
                Long totalMoneyFlowRequiredSameBill = 0l;

                for (WoDTO existedWo : lst) {
                    if (existedWo.getMoneyFlowRequired() != null)
                        totalMoneyFlowRequiredSameBill += existedWo.getMoneyFlowRequired();
                }

            }

            if (dto.getCustomField() == null)
                dto.setCustomField(errorMsg);
            else
                dto.setCustomField(dto.getCustomField() + errorMsg);

            return true;
        } else
            return false;
    }

    public boolean validateImportData(List<WoDTO> wos, List<String> groupIdList, boolean isCnkt,
                                      boolean isRevenueRole) {
        boolean result = true;

        List<Long> woTypeIdList = new ArrayList<>();
        List<String> trCodeList = new ArrayList<>();
        List<String> projectCodeList = new ArrayList<>();
        List<String> contractCodeList = new ArrayList<>();
        List<String> constructionCodeList = new ArrayList<>();
        List<Long> catWorkItemTypeIdList = new ArrayList<>();
        List<Long> woNameIdList = new ArrayList<>();
        List<String> stationCodeList = new ArrayList<>();
        List<String> billList = new ArrayList<>();
        List<String> checkDuplicateKey = new ArrayList<>();

        // prepare list
        for (int i = 0; i < wos.size(); i++) {
            WoDTO wo = wos.get(i);
            if (wo.getWoTypeId() != null && !woTypeIdList.contains(wo.getWoTypeId()))
                woTypeIdList.add(wo.getWoTypeId());
            if (wo.getTrCode() != null && !trCodeList.contains(wo.getTrCode()))
                trCodeList.add(wo.getTrCode());
            if (wo.getProjectCode() != null && !projectCodeList.contains(wo.getProjectCode()))
                projectCodeList.add(wo.getProjectCode());
            if (wo.getContractCode() != null && !contractCodeList.contains(wo.getContractCode()))
                contractCodeList.add(wo.getContractCode());
            if (wo.getConstructionCode() != null && !constructionCodeList.contains(wo.getConstructionCode()))
                constructionCodeList.add(wo.getConstructionCode());
            if (wo.getWoNameId() != null && !woNameIdList.contains(wo.getWoNameId()))
                woNameIdList.add(wo.getWoNameId());
            if (wo.getCatWorkItemTypeId() != null && !catWorkItemTypeIdList.contains(wo.getCatWorkItemTypeId()))
                catWorkItemTypeIdList.add(wo.getCatWorkItemTypeId());
            if (wo.getStationCode() != null && !stationCodeList.contains(wo.getStationCode()))
                stationCodeList.add(wo.getStationCode());
            if (wo.getMoneyFlowBill() != null)
                billList.add(wo.getMoneyFlowBill());

            String uniqueKey = "";
            if (wo.getWoTypeId() != null)
                uniqueKey += wo.getWoTypeId().toString();
            if (wo.getTrCode() != null)
                uniqueKey += wo.getTrCode();
            if (wo.getProjectCode() != null)
                uniqueKey += wo.getProjectCode();
            if (wo.getContractCode() != null)
                uniqueKey += wo.getContractCode();
            if (wo.getConstructionCode() != null)
                uniqueKey += wo.getConstructionCode();
            if (wo.getStationCode() != null)
                uniqueKey += wo.getStationCode();
            if (wo.getCatWorkItemTypeName() != null)
                uniqueKey += wo.getCatWorkItemTypeName();
            if (wo.getMoneyFlowBill() != null)
                uniqueKey += wo.getMoneyFlowBill();

            if (checkDuplicateKey.contains(uniqueKey)) {
                result = false;
                wo.setCustomField("Trùng lặp dữ liệu");
            } else {
                checkDuplicateKey.add(uniqueKey);
            }
        }

        // prepare wo type map
        WoTypeDTO woTypeDTO = new WoTypeDTO();
        woTypeDTO.setIdRange(woTypeIdList);
        List<WoTypeDTO> woTypeList = woTypeDAO.doSearch(woTypeDTO);
        Map<Long, WoTypeDTO> woTypeMap = new HashMap<>();
        for (WoTypeDTO type : woTypeList)
            woTypeMap.put(type.getWoTypeId(), type);

        // prepare name map
        WoNameDTO woNameDTO = new WoNameDTO();
        woNameDTO.setIdRange(woNameIdList);
        List<WoNameDTO> woNameList = woNameDAO.doSearch(woNameDTO);
        Map<Long, WoNameDTO> woNameMap = new HashMap<>();
        for (WoNameDTO name : woNameList)
            woNameMap.put(name.getId(), name);

        // prepare tr map
        Map<Long, WoTrDTO> trMap = new HashMap<>();
        if (trCodeList.size() > 0) {
            WoTrDTO trDTO = new WoTrDTO();
            trDTO.setCodeRange(trCodeList);
            trDTO.setLoggedInUser(wos.get(0).getLoggedInUser());
            List<WoTrDTO> trList = trDAO.doSearch(trDTO, groupIdList, AVAILABLE_TYPE, true, true);
            for (WoTrDTO tr : trList)
                trMap.put(tr.getTrId(), tr);
        }

        // prepare construction map
        Map<String, WoSimpleConstructionDTO> constructionMap = new HashMap<>();
        List<String> contractConstructionList = new ArrayList<>();
        List<String> projectConstructionList = new ArrayList<>();
        if (constructionCodeList.size() > 0) {
            List<WoSimpleConstructionDTO> constructionList = trDAO.getConstructionByCodeRange(constructionCodeList);
            for (WoSimpleConstructionDTO construction : constructionList)
                constructionMap.put(construction.getConstructionCode(), construction);
            contractConstructionList = trDAO.getConstructionMappingWithContract(constructionCodeList);
            projectConstructionList = trDAO.getConstructionMappingWithProject(constructionCodeList);
        }

        // prepare contractMap
        Map<String, WoSimpleContractDTO> contractMap = new HashMap<>();
        Map<String, Long> contractTypeOMap = new HashMap<>();
        if (contractCodeList.size() > 0) {
            WoTrDTO trDto = new WoTrDTO();
            trDto.setContractCodeRange(contractCodeList);
            String domain = "";
            for (String item : groupIdList) {
                domain += item + ",";
            }
            trDto.setCreateDoanhThuDomain(domain);
            if (isRevenueRole)
                trDto.setContractFilter("DOANHTHU");
            List<WoSimpleContractDTO> contractList = trDAO.doSearchContracts(trDto);
            for (WoSimpleContractDTO contract : contractList) {
                contractMap.put(contract.getContractCode(), contract);
                contractTypeOMap.put(contract.getContractCode(), contract.getContractTypeO());
            }

        }

        // prepare project
        Map<String, WoSimpleProjectDTO> projectMap = new HashMap<>();
        if (projectCodeList.size() > 0) {
            WoTrDTO trDto = new WoTrDTO();
            trDto.setProjectCodeRange(projectCodeList);
            List<WoSimpleProjectDTO> projectList = trDAO.doSearchProjects(trDto);
            for (WoSimpleProjectDTO project : projectList)
                projectMap.put(project.getProjectCode(), project);
        }

        // prepare workitem
        Map<String, WorkItemDetailDTO> workItemMap = new HashMap<>();
        if (catWorkItemTypeIdList.size() > 0) {
            List<WorkItemDetailDTO> workItemLst = constructionBusinessImpl.getCatWorkItemType();
            for (WorkItemDetailDTO workItem : workItemLst)
                workItemMap.put(workItem.getName(), workItem);
        }

        // prepare station
        Map<String, WoSimpleStationDTO> stationMap = new HashMap<>();
        if (stationCodeList.size() > 0) {
            WoSimpleStationDTO stationDto = new WoSimpleStationDTO();
            List<WoSimpleStationDTO> stationList = trDAO.doSearchStations(stationDto);
            for (WoSimpleStationDTO station : stationList)
                stationMap.put(station.getStationCode(), station);
        }

        // prepare money flow bill
        List<String> billsExisted = woDAO.getAllBills();

        for (WoDTO wo : wos) {
            String errorString = "";
            WoTypeDTO workType = woTypeMap.get(wo.getWoTypeId());

            if (wo.getWoTypeId() == null || workType == null) {
                result = false;
                errorString += "Loại wo không tồn tại trong danh sách hoặc bị trống; ";
            } else {
                if (wo.getApWorkSrc() == null && workType.getHasApWorkSrc() != null) {
                    result = false;
                    errorString += "Loại wo này cần chọn nguồn việc theo danh sách; ";
                } else {
                    if (isCnkt && wo.getApWorkSrc() != 6) {
                        result = false;
                        errorString += "Loại nguồn việc này không hỗ trợ CNKT tạo WO; ";
                    }
                }

                if (wo.getApConstructionType() == null && workType.getHasConstruction() != null) {
                    result = false;
                    errorString += "Loại wo này cần chọn loại công trình theo danh sách; ";
                }

                if (StringUtils.isNullOrEmpty(wo.getConstructionCode()) && workType.getHasWorkItem() != null) {
                    result = false;
                    errorString += "Loại wo này cần có mã công trình; ";
                }

                if (wo.getCatWorkItemTypeId() == null && workType.getHasWorkItem() != null) {
                    result = false;
                    errorString += "Hạng mục không hợp lệ với loại work cần hạng mục; ";
                }

                if (wo.getCatWorkItemTypeId() != null && workType.getHasWorkItem() == null) {
                    errorString += "Tự động loại bỏ hạng mục với loại work không cần hạng mục; ";
                    wo.setCatWorkItemTypeId(null);
                }
            }

            // Huypq-14102020-comment Bo check TR cho CNKT giao WO
            // if (wo.getTrId() == null && isCnkt) {
            // result = false;
            // errorString += "Mã TR không được để trống; ";
            // }
            // Huypq-14102020-comment end

            // wo từ VHKT phải có tr
            if (wo.getTrId() == null && VHKT_GROUP_ID.equalsIgnoreCase(wo.getCdLevel1())) {
                result = false;
                errorString += "Mã TR không được để trống; ";
            }

            if (wo.getWoNameId() == null) {
                result = false;
                errorString += "Tên wo không tồn tại trong danh sách hoặc bị trống; ";
            }

            if (wo.getMoneyValue() == null) {
                if (!"THICONG".equalsIgnoreCase(wo.getWoTypeCode()) || wo.getApWorkSrc() != 6) {
                    result = false;
                    errorString += "Giá trị sản lượng không được để trống; ";
                }
            } else {
                if (wo.getMoneyValue() > 10000000000d) {
                    result = false;
                    errorString += "Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!";
                }
            }

            if (wo.getFinishDate() == null) {
                result = false;
                errorString += "Cần có hạn hoàn thành; ";
            } else {
                Date a = wo.getFinishDate();
            }

            if (wo.getQoutaTime() == null) {
                result = false;
                errorString += "Cần có định mức hoàn thành; ";
            } else {
                if (wo.getQoutaTime() > 10000) {
                    result = false;
                    errorString += "Định mức hoàn thành không được lớn hơn 10000 giờ; ";
                }
            }

            if (!"DOANHTHU".equalsIgnoreCase(wo.getWoTypeCode())) {
                if (StringUtils.isNullOrEmpty(wo.getCdLevel2()) && !"THICONG".equalsIgnoreCase(wo.getWoTypeCode())) {
                    result = false;
                    errorString += "CD level 2 không hợp lệ; ";
                } else {
                    if (StringUtils.isNullOrEmpty(wo.getCdLevel2())
                            && StringUtils.isNullOrEmpty(wo.getCdLevel5Email())) {
                        result = false;
                        errorString += "WO thi công cần có CD Level 2 hoặc CD Level 5; ";
                    }
                }
            }

            // if wo name not belong to wo type or null woName
            if (wo.getWoNameId() != null) {
                WoNameDTO name = woNameMap.get(wo.getWoNameId());
                if (name == null || !name.getWoTypeId().equals(wo.getWoTypeId())) {
                    result = false;
                    errorString += "Tên wo không thuộc loại wo; ";
                }
            } else {
                result = false;
                errorString += "Tên wo không được để trống; ";
            }

            if (!StringUtils.isNullOrEmpty(wo.getProjectCode()) && !StringUtils.isNullOrEmpty(wo.getContractCode())) {
                result = false;
                errorString += "Mã dự án và mã hợp đồng không được cùng tồn tại; ";
            }

            if (StringUtils.isNullOrEmpty(wo.getProjectCode()) && StringUtils.isNullOrEmpty(wo.getContractCode())) {
                result = false;
                errorString += "Phải có mã dự án hoặc mã hợp đồng; ";
            }

            // if wo has tr and contract not belong to tr
            if (wo.getTrId() != null) {
                WoTrDTO tr = trMap.get(wo.getTrId());
                if (tr == null) {
                    result = false;
                    errorString += "TR không tồn tại hoặc đã xóa; ";
                } else {
                    if (!StringUtils.isNullOrEmpty(wo.getProjectCode())
                            && !tr.getProjectCode().equalsIgnoreCase(wo.getProjectCode())) {
                        result = false;
                        errorString += "Dự án không thuộc TR; ";
                    }

                    if (!StringUtils.isNullOrEmpty(wo.getContractCode())
                            && !tr.getContractCode().equalsIgnoreCase(wo.getContractCode())) {
                        result = false;
                        errorString += "Hợp đồng không thuộc TR; ";
                    }

                    if (!StringUtils.isNullOrEmpty(wo.getConstructionCode())
                            && !tr.getConstructionCode().equalsIgnoreCase(wo.getConstructionCode())) {
                        result = false;
                        errorString += "Công trình không thuộc TR; ";
                    }
                }
            }

            // if has projectCode, check exist and get projectId
            if (!StringUtils.isNullOrEmpty(wo.getProjectCode())) {
                WoSimpleProjectDTO project = projectMap.get(wo.getProjectCode());
                if (project == null) {
                    result = false;
                    errorString += "Dự án không tồn tại; ";
                } else {
                    wo.setProjectId(project.getProjectId());
                }
            }

            // if has contractCode, check exist and get contractId
            if (!StringUtils.isNullOrEmpty(wo.getContractCode())) {
                WoSimpleContractDTO contract = contractMap.get(wo.getContractCode());
                if (contract == null) {
                    result = false;
                    errorString += "Hợp đồng không tồn tại hoặc chưa được duyệt; ";
                } else {
                    wo.setContractId(contract.getContractId());
                    if ("DOANHTHU".equalsIgnoreCase(wo.getWoTypeCode())) {
                        String branchCntCode = getBranchCode(groupIdList);
                        if (!woDAO.checkContractBelongToBranch(contract.getContractId(), branchCntCode)) {
                            result = false;
                            errorString += "Hợp đồng không thuộc trụ; ";
                        }
                    }

                    if (wo.getApWorkSrc() != null) {
                        if (wo.getApWorkSrc() == 1l && contractTypeOMap.get(wo.getContractCode()) != 1l) {
                            result = false;
                            errorString += "Hợp đồng không thuộc nguồn việc Xây lắp; ";
                        } else if (wo.getApWorkSrc() == 2l && contractTypeOMap.get(wo.getContractCode()) != 2l) {
                            result = false;
                            errorString += "Hợp đồng không thuộc nguồn việc Chi phí; ";
                        } else if (wo.getApWorkSrc() == 3l && contractTypeOMap.get(wo.getContractCode()) != 3l) {
                            result = false;
                            errorString += "Hợp đồng không thuộc nguồn việc Ngoài tập đoàn; ";
                        } else if (wo.getApWorkSrc() == 4l && contractTypeOMap.get(wo.getContractCode()) != 10l) {
                            result = false;
                            errorString += "Hợp đồng không thuộc nguồn việc Hạ tầng cho thuê; ";
                        }
                    }
                }
            }

            // if construction not belong to contract
            if (!StringUtils.isNullOrEmpty(wo.getConstructionCode())) {
                if (!StringUtils.isNullOrEmpty(wo.getContractCode())) {
                    String check = wo.getContractCode() + "-" + wo.getConstructionCode();
                    if (!contractConstructionList.contains(check)) {
                        result = false;
                        errorString += "Công trình không thuộc hợp đồng; ";
                    }
                }

                if (!StringUtils.isNullOrEmpty(wo.getProjectCode())) {
                    String check = wo.getProjectCode() + "-" + wo.getConstructionCode();
                    if (!projectConstructionList.contains(check)) {
                        result = false;
                        errorString += "Công trình không thuộc dự án; ";
                    }
                }
            }

            // ngoài loại AIO, thuê trạm, thu tiền không cần công trình còn lại phải có
            if (StringUtils.isNullOrEmpty(wo.getConstructionCode())) {
                if ("AIO".equalsIgnoreCase(wo.getWoTypeCode()) == false
                        && "TMBTHTTC".equalsIgnoreCase(wo.getWoTypeCode()) == false
                        && "THDT".equalsIgnoreCase(wo.getWoTypeCode()) == false
                        && "DOANHTHU".equalsIgnoreCase(wo.getWoTypeCode()) == false) {
                    result = false;
                    errorString += "Công trình không được để trống; ";
                }
            } else {
                WoSimpleConstructionDTO construction = constructionMap.get(wo.getConstructionCode());
                if (construction != null)
                    wo.setCatConstructionTypeId(construction.getCatConstructionTypeId());
            }

            // if cat work item not belong to construction
            if (wo.getCatWorkItemTypeId() != null) {
                WoSimpleConstructionDTO construction = constructionMap.get(wo.getConstructionCode());
                if (construction == null || wo.getCatWorkItemTypeCatCon() != construction.getCatConstructionTypeId()) {
                    result = false;
                    errorString += "Hạng mục không thuộc loại công trình của công trình " + wo.getConstructionCode()
                            + "; ";
                } else {
                    WorkItemDetailDTO workItem = workItemMap.get(wo.getCatWorkItemTypeName());
                    wo.setCatWorkItemTypeCode(workItem.getCode());
                }
            }

            if (!StringUtils.isNullOrEmpty(wo.getContractCode())) {
                WoSimpleContractDTO woContract = contractMap.get(wo.getContractCode());
                if (woContract != null) {
                    wo.setContractId(woContract.getContractId());

                    // kiểm tra nếu có cdlv5
                    if (!StringUtils.isNullOrEmpty(wo.getCdLevel5Email())) {
                        if (wo.getCdLevel2() != null) {
                            result = false;
                            errorString += "Wo giao cho phụ trách công trình (CD lv5) không cần có CD lv2; ";
                        } else {
                            WoSimpleSysUserDTO cd5 = woDAO.getContractCdLevel5(wo.getCdLevel5Email(),
                                    wo.getContractId());
                            if (cd5 == null) {
                                result = false;
                                errorString += "Phụ trách công trình không tồn tại trong cấu hình; ";
                            } else {
                                // gán cdLv5 cho wo theo email import
                                wo.setCdLevel5(cd5.getSysUserId().toString());
                                wo.setCdLevel5Name(cd5.getFullName());
                            }
                        }
                    }
                    // end
                }
            }

            // build construction wo status must != 5
            if ("THICONG".equalsIgnoreCase(wo.getWoTypeCode())
                    && !StringUtils.isNullOrEmpty(wo.getConstructionCode())) {
                WoSimpleConstructionDTO construction = constructionMap.get(wo.getConstructionCode());
                if (construction != null && construction.getStatus() == 5) {
                    result = false;
                    errorString += "Không được giao công trình đã hoàn thành cho loại wo thi công; ";
                }
            }

            // không được giao hshc đối với công trình chưa thi công xong
            if ("HSHC".equalsIgnoreCase(wo.getWoTypeCode()) && !StringUtils.isNullOrEmpty(wo.getConstructionCode())) {
                WoSimpleConstructionDTO construction = constructionMap.get(wo.getConstructionCode());
                if (construction != null && construction.getStatus() < 5) {
                    result = false;
                    errorString += "Công trình chưa hoàn thành, không thể tạo wo hồ sơ hoàn công; ";
                }
            }

            // rent station WO
            if ("TMBTHTTC".equalsIgnoreCase(wo.getWoTypeCode())) {
                if (StringUtils.isNullOrEmpty(wo.getStationCode())) {
                    result = false;
                    errorString += "Mã trạm không được để trống đối với loại wo thuê trạm; ";
                } else {
                    WoSimpleStationDTO station = stationMap.get(wo.getStationCode());
                    if (station == null) {
                        result = false;
                        errorString += "Mã trạm không tồn tại hoặc đã hoàn thành thuê trạm; ";
                    }
                }
            }

            if ("THDT".equalsIgnoreCase(wo.getWoTypeCode())) {
                if (StringUtils.isNullOrEmpty(wo.getContractCode())) {
                    result = false;
                    errorString += "Mã hợp đồng không được để trống với WO thu hồi dòng tiền; ";
                }

                if (StringUtils.isNullOrEmpty(wo.getMoneyFlowBill())) {
                    result = false;
                    errorString += "Số hóa đơn không được để trống với WO thu hồi dòng tiền; ";
                } else {
                    String bill = wo.getMoneyFlowBill();
                    if (billsExisted.contains(bill)) {
                        result = false;
                        errorString += "Số hóa đơn đã có WO dòng tiền tiền tương ứng; ";
                    }
                }

                if (wo.getMoneyFlowValue() == null) {
                    result = false;
                    errorString += "Giá trị hóa đơn không được để trống với WO thu hồi dòng tiền; ";
                }

                if (wo.getMoneyFlowRequired() == null) {
                    result = false;
                    errorString += "Giá trị phải thu không được để trống với WO thu hồi dòng tiền; ";
                }

                if (StringUtils.isNullOrEmpty(wo.getMoneyFlowContent())) {
                    result = false;
                    errorString += "Nội dung thu tiền không được để trống với WO thu hồi dòng tiền; ";
                }
            }

            if ("UCTT".equalsIgnoreCase(wo.getWoTypeCode())) {
                if (StringUtils.isNullOrEmpty(wo.getDescription())) {
                    result = false;
                    errorString += "Mô tả WO ứng cứu thông tin không được để trống; ";
                }

                if (StringUtils.isNullOrEmpty(wo.getVtnetWoCode())) {
                    result = false;
                    errorString += "Mã WO VTNET không được để trống; ";
                }

                if (StringUtils.isNullOrEmpty(wo.getPartner())) {
                    result = false;
                    errorString += "Đối tác không được để trống; ";
                }
            }

            // prepare to check is there any same wo created b4
            if (!StringUtils.isNullOrEmpty(wo.getConstructionCode())) {
                WoSimpleConstructionDTO construction = constructionMap.get(wo.getConstructionCode());
                if (construction != null) {
                    wo.setStationCode(construction.getStationCode());
                    wo.setConstructionId(construction.getConstructionId());
                }
            }

            // check if construction already has same wo created
            if (checkSameWo(wo)) {
                result = false;
                errorString += wo.getCustomField();
            }

            if (wo.getCustomField() == null)
                wo.setCustomField(errorString);
            else
                wo.setCustomField(wo.getCustomField() + errorString);

        }

        return result;
    }

    private String getBranchCode(List<String> groupIdList) {
        String resultCode = "";
        if (groupIdList.contains(TTGPTH_ID))
            resultCode += "1";
        if (groupIdList.contains(TTHT_ID))
            resultCode += "2";
        if (groupIdList.contains(TTXDDTHT_ID))
            resultCode += "3";
        if (groupIdList.contains(TTVHKT_ID))
            resultCode += "4";
        if (groupIdList.contains(TTCNTT_ID))
            resultCode += "5";
        if (groupIdList.contains(PKD_ID))
            resultCode += "6";
        if (groupIdList.contains(PDT_ID))
            resultCode += "7";
        if (groupIdList.contains(PHC_ID))
            resultCode += "8";

        return resultCode;
    }

    public String exportFileTrWo(ReportWoTrDTO obj) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_BC_TrWo.xlsx"));

        if (obj.getType().equals("2")) {
            file = new BufferedInputStream(new FileInputStream(filePath + "Export_BC_TrWoGroup.xlsx"));
        }

        if (obj.getType().equals("3")) {
            file = new BufferedInputStream(new FileInputStream(filePath + "Export_BC_TrWoProvine.xlsx"));
        }

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        String path = "";
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);

        if (!udir.exists()) {
            udir.mkdirs();
        }

        List<ReportWoTrDTO> data = new ArrayList<>();
        if (obj.getType().equals("1")) {
            OutputStream outFile = new FileOutputStream(
                    udir.getAbsolutePath() + File.separator + "Export_BC_TrWo.xlsx");
            data = trDAO.doSearchReportWoTr(obj);
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (data != null && !data.isEmpty()) {
                XSSFCellStyle style = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
                XSSFCreationHelper createHelper = workbook.getCreationHelper();
                styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                styleNumber.setAlignment(HorizontalAlignment.RIGHT);
                styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
                int i = 1;
                for (ReportWoTrDTO dto : data) {
                    Row row = sheet.createRow(i++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue("" + (i - 1));
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue((dto.getName() != null) ? dto.getName() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue((dto.getTthtApprove() != null)
                            ? dto.getTthtApprove().toString() + '(' + dto.getTthtApprovePrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue((dto.getTthtSystem() != null)
                            ? dto.getTthtSystem().toString() + '(' + dto.getTthtSystemPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue((dto.getTthtAssignCd() != null)
                            ? dto.getTthtAssignCd().toString() + '(' + dto.getTthtAssignCdPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellValue((dto.getTthtRejectCd() != null)
                            ? dto.getTthtRejectCd().toString() + '(' + dto.getTthtRejectCdPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellValue((dto.getTthtTrWo() != null)
                            ? dto.getTthtTrWo().toString() + '(' + dto.getTthtTrWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellValue((dto.getTthtNotTrWo() != null)
                            ? dto.getTthtNotTrWo().toString() + '(' + dto.getTthtNotTrWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellValue((dto.getCnktApprove() != null)
                            ? dto.getCnktApprove().toString() + '(' + dto.getCnktApprovePrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellValue((dto.getCnktSystemWo() != null)
                            ? dto.getCnktSystemWo().toString() + '(' + dto.getCnktSystemWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellValue((dto.getWoAssignCd() != null)
                            ? dto.getWoAssignCd().toString() + '(' + dto.getWoAssignCdPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(11, CellType.STRING);
                    cell.setCellValue((dto.getWoAcceptFt() != null)
                            ? dto.getWoAcceptFt().toString() + '(' + dto.getWoAcceptFtPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(12, CellType.STRING);
                    cell.setCellValue((dto.getWoAcceptFtSystem() != null)
                            ? dto.getWoAcceptFtSystem().toString() + '(' + dto.getWoAcceptFtSystemPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(13, CellType.STRING);
                    cell.setCellValue((dto.getWoAcceptFtWo() != null)
                            ? dto.getWoAcceptFtWo().toString() + '(' + dto.getWoAcceptFtWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(14, CellType.STRING);
                    cell.setCellValue((dto.getWoAssignFt() != null)
                            ? dto.getWoAssignFt().toString() + '(' + dto.getWoAssignFtPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(15, CellType.STRING);
                    cell.setCellValue((dto.getWoFinish() != null)
                            ? dto.getWoFinish().toString() + '(' + dto.getWoFinishPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(16, CellType.STRING);
                    cell.setCellValue((dto.getWoCompleted() != null)
                            ? dto.getWoCompleted().toString() + '(' + dto.getWoCompletedPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(17, CellType.STRING);
                    cell.setCellValue((dto.getWoNotCompleted() != null)
                            ? dto.getWoNotCompleted().toString() + '(' + dto.getWoNotCompletedPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(18, CellType.STRING);
                    cell.setCellValue((dto.getWoNotFinish() != null)
                            ? dto.getWoNotFinish().toString() + '(' + dto.getWoNotFinishPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(19, CellType.STRING);
                    cell.setCellValue((dto.getWoNotFinishDate() != null)
                            ? dto.getWoNotFinishDate().toString() + '(' + dto.getWoNotFinishDatePrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(20, CellType.STRING);
                    cell.setCellValue((dto.getWoFinishDayEx() != null)
                            ? dto.getWoFinishDayEx().toString() + '(' + dto.getWoFinishDayExPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                }
            }
            workbook.write(outFile);
            workbook.close();
            outFile.close();
            path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_BC_TrWo.xlsx");
        }

        if (obj.getType().equals("2")) {
            data = trDAO.doSearchReportWoGroup(obj);
            OutputStream outFile = new FileOutputStream(
                    udir.getAbsolutePath() + File.separator + "Export_BC_TrWoGroup.xlsx");
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (data != null && !data.isEmpty()) {
                XSSFCellStyle style = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
                XSSFCreationHelper createHelper = workbook.getCreationHelper();
                styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                styleNumber.setAlignment(HorizontalAlignment.RIGHT);
                styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
                int i = 1;
                for (ReportWoTrDTO dto : data) {
                    Row row = sheet.createRow(i++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue("" + (i - 1));
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue((dto.getName() != null) ? dto.getName() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue((dto.getCnktApprove() != null)
                            ? dto.getCnktApprove().toString() + '(' + dto.getCnktApprovePrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue((dto.getCnktSystemWo() != null)
                            ? dto.getCnktSystemWo().toString() + '(' + dto.getCnktSystemWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue((dto.getWoAssignCd() != null)
                            ? dto.getWoAssignCd().toString() + '(' + dto.getWoAssignCdPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellValue((dto.getWoAcceptFt() != null)
                            ? dto.getWoAcceptFt().toString() + '(' + dto.getWoAcceptFtPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellValue((dto.getCnktSystemWo() != null)
                            ? dto.getCnktSystemWo().toString() + '(' + dto.getCnktSystemWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellValue((dto.getWoAcceptFtWo() != null)
                            ? dto.getWoAcceptFtWo().toString() + '(' + dto.getWoAcceptFtWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellValue((dto.getWoAssignFt() != null)
                            ? dto.getWoAssignFt().toString() + '(' + dto.getWoAssignFtPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellValue((dto.getWoFinish() != null)
                            ? dto.getWoFinish().toString() + '(' + dto.getWoFinishPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellValue((dto.getWoCompleted() != null)
                            ? dto.getWoCompleted().toString() + '(' + dto.getWoCompletedPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(11, CellType.STRING);
                    cell.setCellValue((dto.getWoNotCompleted() != null)
                            ? dto.getWoNotCompleted().toString() + '(' + dto.getWoNotCompletedPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(12, CellType.STRING);
                    cell.setCellValue((dto.getWoNotFinish() != null)
                            ? dto.getWoNotFinish().toString() + '(' + dto.getWoNotFinishPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(13, CellType.STRING);
                    cell.setCellValue((dto.getWoNotFinishDate() != null)
                            ? dto.getWoNotFinishDate().toString() + '(' + dto.getWoNotFinishDatePrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(14, CellType.STRING);
                    cell.setCellValue((dto.getWoFinishDayEx() != null)
                            ? dto.getWoFinishDayEx().toString() + '(' + dto.getWoFinishDayExPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                }
            }
            workbook.write(outFile);
            workbook.close();
            outFile.close();
            path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_BC_TrWoGroup.xlsx");
        }

        if (obj.getType().equals("3")) {
            data = trDAO.doSearchReportWoPro(obj);
            OutputStream outFile = new FileOutputStream(
                    udir.getAbsolutePath() + File.separator + "Export_BC_TrWoProvine.xlsx");
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (data != null && !data.isEmpty()) {
                XSSFCellStyle style = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
                XSSFCreationHelper createHelper = workbook.getCreationHelper();
                styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                styleNumber.setAlignment(HorizontalAlignment.RIGHT);
                styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
                int i = 1;
                for (ReportWoTrDTO dto : data) {
                    Row row = sheet.createRow(i++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue("" + (i - 1));
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue((dto.getName() != null) ? dto.getName() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue((dto.getCnktApprove() != null)
                            ? dto.getCnktApprove().toString() + '(' + dto.getCnktApprovePrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue((dto.getCnktSystemWo() != null)
                            ? dto.getCnktSystemWo().toString() + '(' + dto.getCnktSystemWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue((dto.getWoAssignCd() != null)
                            ? dto.getWoAssignCd().toString() + '(' + dto.getWoAssignCdPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellValue((dto.getWoAcceptFt() != null)
                            ? dto.getWoAcceptFt().toString() + '(' + dto.getWoAcceptFtPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellValue((dto.getCnktSystemWo() != null)
                            ? dto.getCnktSystemWo().toString() + '(' + dto.getCnktSystemWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellValue((dto.getWoAcceptFtWo() != null)
                            ? dto.getWoAcceptFtWo().toString() + '(' + dto.getWoAcceptFtWoPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellValue((dto.getWoAssignFt() != null)
                            ? dto.getWoAssignFt().toString() + '(' + dto.getWoAssignFtPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellValue((dto.getWoFinish() != null)
                            ? dto.getWoFinish().toString() + '(' + dto.getWoFinishPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellValue((dto.getWoCompleted() != null)
                            ? dto.getWoCompleted().toString() + '(' + dto.getWoCompletedPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(11, CellType.STRING);
                    cell.setCellValue((dto.getWoNotCompleted() != null)
                            ? dto.getWoNotCompleted().toString() + '(' + dto.getWoNotCompletedPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(12, CellType.STRING);
                    cell.setCellValue((dto.getWoNotFinish() != null)
                            ? dto.getWoNotFinish().toString() + '(' + dto.getWoNotFinishPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(13, CellType.STRING);
                    cell.setCellValue((dto.getWoNotFinishDate() != null)
                            ? dto.getWoNotFinishDate().toString() + '(' + dto.getWoNotFinishDatePrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(14, CellType.STRING);
                    cell.setCellValue((dto.getWoFinishDayEx() != null)
                            ? dto.getWoFinishDayEx().toString() + '(' + dto.getWoFinishDayExPrecent() + "%)"
                            : "");
                    cell.setCellStyle(styleNumber);
                }
            }
            workbook.write(outFile);
            workbook.close();
            outFile.close();
            path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_BC_TrWoProvine.xlsx");
        }
        return path;
    }

    // Huypq-24112020-start
    public String exportDoneWo(WoDTO woDto, HttpServletRequest request) throws Exception {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_Done_WO_TC.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_Done_WO_TC.xlsx");
        WoDTO sysGroupWo = woDAO.getCDLevel(user.getVpsUserInfo().getSysGroupId());
        List<String> groupIdList = new ArrayList<>();
        groupIdList.add(sysGroupWo.getCdLevel2());
        List<WoCdDTO> listWOcd = woDAO.getCdLevel1();
        boolean checkCD = false;
        String type = "";
        for (String domainData : groupIdList) {
            for (WoCdDTO woCdDTO : listWOcd) {
                if (domainData.equals(woCdDTO.getSysGroupId().toString())) {
                    checkCD = true;
                }
            }
        }
        if (checkCD == true) {
            type = "2";
        } else {
            type = "1";
        }
        List<WoDTO> data = woDAO.exportDoneWo(type, groupIdList);
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            // styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
            int i = 2;
            for (WoDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getWoCode() != null) ? dto.getWoCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getWoTypeName() != null) ? dto.getWoTypeName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getApWorkSrcName() != null) ? dto.getApWorkSrcName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getCatWorkItemTypeName() != null) ? dto.getCatWorkItemTypeName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getMoneyValue() != null) ? dto.getMoneyValue() : 0d);
                cell.setCellStyle(styleNumber);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getFinishDateStr() != null) ? dto.getFinishDateStr() : null);
                cell.setCellStyle(style);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getEndTimeStr() != null) ? dto.getEndTimeStr() : null);
                cell.setCellStyle(style);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getCdLevel2Name() != null) ? dto.getCdLevel2Name() : null);
                cell.setCellStyle(style);
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getFtCode() != null) ? dto.getFtCode() : null);
                cell.setCellStyle(style);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getFtName() != null) ? dto.getFtName() : null);
                cell.setCellStyle(style);
                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue("");
                cell.setCellStyle(style);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_Done_WO_TC.xlsx");
        return path;

    }

    public List<WoDTO> importDoneWo(String fileInput, List<String> listGroup, HttpServletRequest request)
            throws Exception {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        List<WoDTO> workLst = new ArrayList<>();
        List<WoDTO> listWoDTOCheck = new ArrayList<>();
        String error = "";
        List<WoCdDTO> listWOcd = woDAO.getCdLevel1();
        boolean checkCD = false;

        String type = "";
        for (String domainData : listGroup) {
            for (WoCdDTO woCdDTO : listWOcd) {
                if (domainData.equals(woCdDTO.getSysGroupId().toString())) {
                    checkCD = true;
                }
            }
        }

        if (checkCD == true) {
            // TTHT
            type = "2";
        } else {
            // CNKT
            type = "1";
        }
        boolean resul = true;

        try {
            File f = new File(fileInput);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            List<String> lstWorkItemName = new ArrayList<>();
            List<String> lstFtCode = new ArrayList<>();
            List<String> lstCdLv2 = new ArrayList<>();
            List<String> lstConsCode = new ArrayList<>();
            List<String> lstWoCode = new ArrayList<>();
            List<String> lstContractCode = new ArrayList<>();
            int count1 = 0;
            for (Row row : sheet) {
                WoDTO obj = new WoDTO();
                count1++;
                if (count1 >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                    if (!StringUtils.isNullOrEmpty(formatter.formatCellValue(row.getCell(1)))) {
                        lstWoCode.add(formatter.formatCellValue(row.getCell(1)).trim().toUpperCase());
                    }
                    lstWorkItemName.add(formatter.formatCellValue(row.getCell(7)));
                    if (!StringUtils.isNullOrEmpty(formatter.formatCellValue(row.getCell(12)))) {
                        lstFtCode.add(formatter.formatCellValue(row.getCell(12)).trim());
                    }
                    lstCdLv2.add(formatter.formatCellValue(row.getCell(11)));
                    if (!StringUtils.isNullOrEmpty(formatter.formatCellValue(row.getCell(6)))) {
                        lstConsCode.add(formatter.formatCellValue(row.getCell(6)).trim().toUpperCase());
                    }
                    if (!StringUtils.isNullOrEmpty(formatter.formatCellValue(row.getCell(4)))) {
                        lstContractCode.add(formatter.formatCellValue(row.getCell(4)).trim().toUpperCase());
                    }
                }
            }

            Map<String, String> mapWoCode = woDAO.getStateByWoCode(lstWoCode);
            Map<String, Long> mapNameId = woDAO.getCatWorkItemTypeIdByName(lstWorkItemName);
            Map<String, Long> mapUserId = woDAO.getSysUserIdByCode(lstFtCode);
            Map<String, String> mapCdLv2 = woDAO.getSysGroupIdByName(lstCdLv2);
            Map<String, Long> mapConsType = woDAO.getConsTypeByConsCode(lstConsCode);
            Map<String, String> mapStateConsCode = woDAO.getStateByConsCode(lstConsCode);
            Map<String, String> mapContractCheckImport = woDAO.getCheckImportWoByContractCode(lstContractCode);

            List<String> listCodeWO = Lists.newArrayList();
            int count = 0;
            for (Row row : sheet) {
                WoDTO obj = new WoDTO();
                count++;
                if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                    String woCode = formatter.formatCellValue(row.getCell(1));
                    String woTypename = formatter.formatCellValue(row.getCell(2));
                    String workName = formatter.formatCellValue(row.getCell(3));
                    String contract = formatter.formatCellValue(row.getCell(4));
                    String station = formatter.formatCellValue(row.getCell(5));
                    String contrustion = formatter.formatCellValue(row.getCell(6));
                    String workItiem = formatter.formatCellValue(row.getCell(7));
                    String moneyValue = formatter.formatCellValue(row.getCell(8));
                    String finshDate = formatter.formatCellValue(row.getCell(9));
                    String endTime = formatter.formatCellValue(row.getCell(10));
                    String cdlevl2 = formatter.formatCellValue(row.getCell(11));
                    String ftCode = formatter.formatCellValue(row.getCell(12));
                    String ft = formatter.formatCellValue(row.getCell(13));
                    String doneWO = formatter.formatCellValue(row.getCell(14));
                    obj.setWoTypeName(woTypename != null ? woTypename.trim() : null);
                    obj.setApWorkSrcName(workName != null ? workName.trim() : null);
                    obj.setContractCode(contract != null ? contract.trim() : null);
                    obj.setConstructionCode(contrustion != null ? contrustion.trim() : null);
                    if (!StringUtils.isNullOrEmpty(contrustion)) {
                        if (mapConsType.size() > 0) {
                            obj.setCatConstructionTypeId(mapConsType.get(contrustion.trim().toUpperCase()));
                        }
                    }
                    if (!StringUtils.isNullOrEmpty(workItiem)) {
                        obj.setCatWorkItemTypeName(workItiem.trim());
                        if (mapNameId.size() > 0) {
                            obj.setCatWorkItemTypeId(mapNameId.get(workItiem.trim()));
                        }
                    }
                    obj.setFinishDateStr(finshDate != null ? finshDate.trim() : null);
                    obj.setEndTimeStr(endTime != null ? endTime.trim() : null);
                    obj.setStationCode(station != null ? station.trim() : null);
                    obj.setCdLevel2Name(cdlevl2 != null ? cdlevl2.trim() : null);
                    if (!StringUtils.isNullOrEmpty(cdlevl2)) {
                        if (mapCdLv2.size() > 0) {
                            obj.setCdLevel2(mapCdLv2.get(cdlevl2));
                        }
                    }
                    obj.setFtName(ft != null ? ft.trim() : null);
                    if (!StringUtils.isNullOrEmpty(ftCode)) {
                        if (mapUserId.size() > 0) {
                            obj.setFtId(mapUserId.get(ftCode.trim()));
                        }
                        obj.setFtCode(ftCode.trim());
                    }
                    obj.setWoCode(woCode != null ? woCode.trim() : null);
                    if (!StringUtils.isNullOrEmpty(moneyValue)) {
                        obj.setMoneyValue(Double.parseDouble(moneyValue));
                    }
                    obj.setUserCreated(user.getVpsUserInfo().getEmployeeCode());
                    obj.setLoggedInUser(user.getVpsUserInfo().getEmployeeCode());
                    obj.setDoneStateWo(doneWO);
                    workLst.add(obj);
                    listCodeWO.add(woCode);
                }
            }
            if (listCodeWO.size() > 0) {
                listWoDTOCheck = woDAO.getWoIDByCode(listCodeWO, null);
            }

            if (workLst.size() > 0) {
                for (WoDTO woDTO : workLst) {
                    boolean isCheck = false;
                    String code = woDTO.getWoCode();
                    for (WoDTO woDTOCheck : listWoDTOCheck) {
                        if (woDTOCheck.getWoCode().equals(code)) {
                            isCheck = true;
                            woDTO.setWoId(woDTOCheck.getWoId());
                            woDTO.setWoTypeId(woDTOCheck.getWoTypeId());
                            woDTO.setWoTypeCode(woDTOCheck.getWoTypeCode());
                            if (!woDTO.getFtId().equals(woDTOCheck.getFtId())) {
                                woDTO.setCustomField("FT của wo và ở file import không giống nhau.");
                                woDTO.setFtId(woDTOCheck.getFtId());
                            }
                        }
                    }
                    if (mapWoCode.size() == 0) {
                        woDTO.setCustomField("Chưa nhập Mã WO hoặc Mã WO không tồn tại!");
                    } else {
                        String checkWoCode = mapWoCode.get(woDTO.getWoCode().toUpperCase());
                        if (StringUtils.isNullOrEmpty(checkWoCode)) {
                            woDTO.setCustomField("FT (id = " + woDTO.getFtId() + ") không tồn tài trên hệ thống !");
                        }
                    }

                    if (mapConsType.size() > 0 && !StringUtils.isNullOrEmpty(woDTO.getConstructionCode())) {
                        Long catConsTypeId = mapConsType.get(woDTO.getConstructionCode().toUpperCase());
                        if (catConsTypeId == null) {
                            woDTO.setCustomField("Mã công trình không tồn tại!");
                        }
                    } else {
                        woDTO.setCustomField("Chưa nhập Mã công trình hoặc Mã công trình không tồn tại!");
                    }

                    if (!StringUtils.isNullOrEmpty(woDTO.getWoCode())) {
                        if (mapStateConsCode.size() > 0) {
                            if (mapStateConsCode.get(woDTO.getWoCode().toUpperCase()) != null
                                    && !mapStateConsCode.get(woDTO.getWoCode().toUpperCase()).equals("ASSIGN_FT")
                                    && !mapStateConsCode.get(woDTO.getWoCode().toUpperCase()).equals("ACCEPT_FT")
                                    && !mapStateConsCode.get(woDTO.getWoCode().toUpperCase()).equals("PROCESSING")) {
                                woDTO.setCustomField("WO không đúng trạng thái để hoàn thành !");
                            }
                        }
                    }

                    if (woDTO.getApWorkSrcName() != null) {
                        if (!woDTO.getApWorkSrcName().trim().equals("CP - Chi phí")) {
                            // Huypq-24072021-check cho nguồn đầu tư
                            if (mapContractCheckImport.size() > 0) {
                                String checkImprortWo = mapContractCheckImport
                                        .get(woDTO.getContractCode().toUpperCase());
                                if (!checkImprortWo.equals("1")) {
                                    woDTO.setCustomField("Hợp đồng không cho phép Import đóng WO !");
                                }
                            } else {
                                woDTO.setCustomField("Chức năng không hỗ trợ hoàn thành nguồn việc này !");
                            }
                            // Huy-end
                        }
                    } else {
                        woDTO.setCustomField("Chưa nhập nguồn việc !");
                    }
                    if (!StringUtils.isNullOrEmpty(woDTO.getFinishDateStr())) {
                        if (!validateDateFormat(woDTO.getFinishDateStr())) {
                            woDTO.setCustomField("Hạn hoàn thành sai định dạng (VD: 30/12/2020) !");
                        }
                    } else {
                        woDTO.setCustomField("Hạn hoàn thành chưa nhập (VD: 30/12/2020) !");
                    }

                    if (!StringUtils.isNullOrEmpty(woDTO.getEndTimeStr())) {
                        if (!validateDateFormat(woDTO.getEndTimeStr())) {
                            woDTO.setCustomField("Ngày FT  hoàn thành chưa nhập hoặc sai định dạng (VD: 30/12/2020) !");
                        }
                    }

                    if (StringUtils.isNullOrEmpty(woDTO.getFtCode())) {
                        woDTO.setCustomField("Chưa nhập Mã nhân viên !");
                    } else {
                        Long userId = mapUserId.get(woDTO.getFtCode());
                        if (userId == null) {
                            woDTO.setCustomField("Mã nhân viên không tồn tại !");
                        } else {
                            String checkFtCode = mapWoCode.get(woDTO.getWoCode().toUpperCase());
                            if (!StringUtils.isNullOrEmpty(checkFtCode)) {
                                if (!checkFtCode.equals(woDTO.getFtCode())) {
                                    woDTO.setCustomField("Mã nhân viên không ứng với WO này !");
                                }
                            }
                        }
                    }

                    if (!StringUtils.isNullOrEmpty(woDTO.getDoneStateWo())) {
                        try {
                            Long doneState = Long.parseLong(woDTO.getDoneStateWo());
                            if (woDTO.getDoneStateWo().equals("1")) {
                                woDTO.setState("DONE");
                                if (!StringUtils.isNullOrEmpty(woDTO.getWoTypeName())
                                        && woDTO.getWoTypeName().equals("Thi công")) {
                                    if (woDTO.getCatConstructionTypeId() != null) {
                                        if (woDTO.getCatConstructionTypeId().equals(2l)
                                                || woDTO.getCatConstructionTypeId().equals(3l)) {
                                            woDTO.setCustomField(
                                                    "Công trình tuyến/Gpon không được áp dụng tính năng import này !");
                                        }
                                    } else {
                                        woDTO.setCustomField(
                                                "Công trình tuyến/Gpon không được áp dụng tính năng import này !");
                                    }
                                }
                            } else {
                                woDTO.setCustomField("Ghi nhận đóng nhập không hợp lệ !");
                            }
                        } catch (Exception e) {
                            woDTO.setCustomField("Ghi nhận đóng phải nhập dạng số !");
                        }
                    }
                }
            }
            return workLst;
        } catch (NullPointerException pointerException) {
            pointerException.printStackTrace();
            throw new Exception(pointerException);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(error);
        }
    }

    public Long updateStateWoImportDone(WoDTO dto, KttsUserSession userSession, String content) throws Exception {
        try {
            Gson gson = new Gson();
            Long id = 0l;
            if (dto != null) {
                WoBO incomingBo = dto.toModel();
                WoTypeBO woTypeBO = woTypeDAO.getOneRaw(incomingBo.getWoTypeId());
                // Update WO
                if ("PROCESSING".equalsIgnoreCase(dto.getState())) {
                    if (dto.getConstructionId() != null && "THICONG".equalsIgnoreCase(woTypeBO.getWoTypeCode())) {
                        woDAO.updateStartConstruction(dto.getConstructionId());
                        if (dto.getCatWorkItemTypeId() != null) {
                            woDAO.updateStartWorkItiem(dto.getConstructionId(), dto.getCatWorkItemTypeId());
                        }
                    }
                }

                WoBO bo = woDAO.getOneRaw(incomingBo.getWoId());
                dto.setUpdateFtReceiveWo(bo.getUpdateFtReceiveWo());
                dto.setUserFtReceiveWo(bo.getUserFtReceiveWo());
                dto.setState("DONE");
                id = woDAO.updateStateWo(dto, userSession);
                // Update TR
                if (dto.getTrId() != null) {
                    WoTrDTO woTrDTO = trDAO.getOneDetails(dto.getTrId());
                    if (woTrDTO != null && !woTrDTO.getState().contains("OPINION_RQ")) {
                        if ("PROCESSING".equalsIgnoreCase(dto.getState())) {
                            woTrDTO.setState("PROCESSING");
                        } else if (dto.getState().contains("OPINION_RQ")) {
                            woTrDTO.setState("OPINION_RQ");
                        }
                        trDAO.update(woTrDTO.toModel());
                    }
                }

                // Worklogs
                String logContent = "Trạng thái WO: " + woDAO.getNameAppParam("DONE", WO_XL_STATE)
                        + ", Import hoàn thành trên web";
                if (!StringUtils.isNullOrEmpty(content)) {
                    logContent += "\n " + content;
                }

                dto.setApWorkSrc(2l);
                logWoWorkLogs(dto, "1", logContent, gson.toJson(bo), dto.getLoggedInUser());
            }

            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getImportDoneResult(List<WoDTO> lstData) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_Done_WO_Result.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_Done_WO_Result.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (lstData != null && !lstData.isEmpty()) {
            int rowNo = 2;
            for (WoDTO wo : lstData) {
                Row row = sheet.createRow(rowNo++);
                row.createCell(0).setCellValue(rowNo - 2);
                row.createCell(1).setCellValue(wo.getWoCode());
                row.createCell(2).setCellValue(wo.getWoTypeName());
                row.createCell(3).setCellValue(wo.getApWorkSrcName());
                row.createCell(4).setCellValue(wo.getContractCode());
                row.createCell(5).setCellValue(wo.getStationCode());
                row.createCell(6).setCellValue(wo.getConstructionCode());
                row.createCell(7).setCellValue(wo.getCatWorkItemTypeName());
                row.createCell(8).setCellValue(wo.getMoneyValue().toString());
                row.createCell(9).setCellValue(wo.getFinishDateStr());
                row.createCell(10).setCellValue(wo.getEndTimeStr());
                row.createCell(11).setCellValue(wo.getCdLevel2Name());
                row.createCell(12).setCellValue(wo.getFtCode());
                row.createCell(13).setCellValue(wo.getFtName());
                row.createCell(14).setCellValue(wo.getDoneStateWo());
                row.createCell(15).setCellValue(wo.getCustomField() != null ? wo.getCustomField() : "");
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_Done_WO_Result.xlsx");
        return path;
    }
    // Huy-end

    @Override
    public boolean acceptXdddChecklistItemValue(Long id, String loggedInUser) {
        if (id == null)
            return false;

        try {
            // accept value
            WoXdddChecklistBO checklistItem = woXdddChecklistDAO.getOneRaw(id);
            if (checklistItem != null) {
                checklistItem.setConfirm(1l);
                checklistItem.setConfirmDate(new Date());
                checklistItem.setConfirmBy(loggedInUser);
                woXdddChecklistDAO.updateObject(checklistItem);
            }

            // add value to wo value
            WoBO wo = woDAO.getOneRaw(checklistItem.getWoId());
            Double woValue = wo.getMoneyValue();
            if (woValue == null)
                woValue = 0d;
            woValue += checklistItem.getValue();
            wo.setMoneyValue(woValue);
            wo.setApproveDateReportWo(new Date());
            woDAO.updateObject(wo);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public File exportFileReport5s(List<Report5sDTO> listObj) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "Export_WO_5S.xlsx";
        String outFileName = "Export_WO5S.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                Sheet importSheet = workbook.getSheetAt(0);
                CellStyle errorStyle = importSheet.getWorkbook().createCellStyle();
                errorStyle.setFillForegroundColor(IndexedColors.RED.index);
                errorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

                int rowNo = 0;
                int stt = 0;
                for (Report5sDTO dto : listObj) {
                    stt = stt + 1;
                    Row row = importSheet.createRow(++rowNo);
                    row.createCell(0).setCellValue(stt);
                    row.createCell(1).setCellValue((dto.getCdLevel2Name() != null) ? dto.getCdLevel2Name() : "");
                    row.createCell(2).setCellValue((dto.getFtName() != null) ? dto.getFtName() : "");
                    row.createCell(3).setCellValue(dto.getTotalRecord5s() != null ? dto.getTotalRecord5s() : 0l);
                    row.createCell(4).setCellValue(dto.getCountDone() != null ? dto.getCountDone() : 0l);
                    row.createCell(5).setCellValue(dto.getCountDoneOver() != null ? dto.getCountDoneOver() : 0l);
                    row.createCell(6).setCellValue(dto.getCountNotDone() != null ? dto.getCountNotDone() : 0l);
                    row.createCell(7).setCellValue(dto.getCountNotDoneOver() != null ? dto.getCountNotDoneOver() : 0l);
                }
                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }
        return outFile;
    }

    @Override
    public void tryCreate5SForXDDD(WoBO bo, Date startDate) {
        WoTypeBO woType = woTypeDAO.getOneRaw(bo.getWoTypeId());
        boolean isContractCanCreate5S = woDAO.isContractCanCreate5S(bo.getContractId());
        if (isContractCanCreate5S && "THICONG".equalsIgnoreCase(woType.getWoTypeCode())
                && XDDD_AP_WORK_SRC == bo.getApWorkSrc()) {
            WoAppParamDTO config5S = woDAO.getCodeAppParam("5S_CONFIG", "1");
            Integer cycle5S = Integer.parseInt(config5S.getCode());
            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(bo.getFinishDate());
            WoFTConfig5SDTO searchObj = new WoFTConfig5SDTO();

            // WoSimpleSysGroupDTO cnkt = woDAO.getParentGroup(bo.getCdLevel2());
            // searchObj.setCdLevel2(cnkt.getSysGroupId().toString());
            // searchObj.setCdLevel2(bo.getCdLevel2());

            // List<WoFTConfig5SDTO> ftConfigs = woFTConfig5SDAO.doSearch(searchObj);
            // if (ftConfigs == null || ftConfigs.size() == 0) return;
            // WoFTConfig5SDTO ftConfig = ftConfigs.get(0);

            WoDTO dto = bo.toDTO();
            dto.setWoTypeCode(woType.getWoTypeCode());

            while (!start.after(end)) {
                Date targetDay = start.getTime();
                createWo5s(dto, targetDay);
                start.add(Calendar.DATE, cycle5S);
            }
        }
    }

    @Override
    public boolean createWo5s(WoDTO triggerWo, Date finsishDate) {
        Gson gson = new Gson();
        WoBO wo = new WoBO();
        Long id = woDAO.getNextSeqVal();
        wo.setWoId(id);
        wo.setWoCode("VNM_5S_PMXL_2_1_" + id);
        wo.setWoName("Kiểm tra 5S công trường");
        wo.setWoNameId(WO_NAME_5S_ID);
        wo.setWoTypeId(WO_TYPE_5S_ID);
        wo.setState(ASSIGN_FT);
        wo.setConstructionId(triggerWo.getConstructionId());
        wo.setConstructionCode(triggerWo.getConstructionCode());
        wo.setContractId(triggerWo.getContractId());
        wo.setContractCode(triggerWo.getContractCode());
        wo.setProjectId(triggerWo.getProjectId());
        wo.setProjectCode(triggerWo.getProjectCode());
        wo.setStationCode(triggerWo.getStationCode());
        wo.setCatWorkItemTypeId(triggerWo.getCatWorkItemTypeId());
        wo.setCreatedDate(new Date());
        wo.setCreatedDate5s(finsishDate); // Huypq-30102021-add value create date 5s
        wo.setFinishDate(finsishDate);
        wo.setQoutaTime(24);

        wo.setCdLevel1(triggerWo.getCdLevel1());
        wo.setCdLevel1Name(triggerWo.getCdLevel1Name());

        wo.setCdLevel2(triggerWo.getCdLevel2());
        wo.setCdLevel2Name(triggerWo.getCdLevel2Name());
        wo.setCdLevel5(triggerWo.getCdLevel5());
        wo.setCdLevel5Name(triggerWo.getCdLevel5Name());

        wo.setFtId(triggerWo.getFtId());
        wo.setFtName(triggerWo.getFtName());
        wo.setFtEmail(triggerWo.getFtEmail());
        wo.setStatus(0l);

        wo.setMoneyValue(0d);

        try {
            Long woId = woDAO.saveObject(wo);
            wo.setWoId(woId);
            tryCreate5SChecklist(wo);

            // log
            String content = "Tạo mới; " + "Giao cho nhân viên: " + wo.getFtName() + ";";
            content += "Trạng thái: " + woDAO.getNameAppParam(ASSIGN_FT, WO_XL_STATE);
            logWoWorkLogs(wo.toDTO(), "1", content, gson.toJson(wo), "");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void tryCreate5SChecklist(WoBO wo) throws Exception {
        boolean isB2B = woDAO.checkConstructionIsB2B(wo.getConstructionId());

        List<WoAppParamDTO> checklistItems;
        if (isB2B)
            checklistItems = woDAO.getAppParam("WO5S_B2B_CHECKLIST");
        else
            checklistItems = woDAO.getAppParam("WO5S_B2C_CHECKLIST");

        Long i = 1l;
        for (WoAppParamDTO item : checklistItems) {
            WoMappingChecklistBO checklistItem = new WoMappingChecklistBO();
            checklistItem.setWoId(wo.getWoId());
            checklistItem.setName(item.getName());
            checklistItem.setCheckListId(i++);
            checklistItem.setState("NEW");
            checklistItem.setStatus(1l);
            checklistItem.setId(0l);

            String desc = item.getDescription();
            Integer numImgReuire = 0;
            try {
                numImgReuire = Integer.parseInt(desc);
            } catch (NumberFormatException e) {
                numImgReuire = 0;
            }

            checklistItem.setNumImgRequire(numImgReuire);

            woMappingChecklistDAO.saveObject(checklistItem);
        }
    }

    private boolean checkDebt(WoDTO wo) {
        if ("AIO".equalsIgnoreCase(wo.getWoTypeCode()))
            return woDAO.checkAIOFtDebt(wo.getFtId());
        else
            return woDAO.checkFtDebt(wo.getFtId());
    }

    // duonghv13 - add 18102021
    private boolean checkCertificate(WoDTO woDto) {
        boolean result = false;
        if (woDto.getFtId() != null) {
            if (woDto.getWoTypeCode().equalsIgnoreCase("XLSC") == true
                    || woDto.getWoTypeCode().equalsIgnoreCase("NLMT_BDDK")
                    || woDto.getWoTypeCode().equalsIgnoreCase("NLMT_KTDK")
                    || woDto.getWoTypeCode().equalsIgnoreCase("NLMT_XLSC")) {
                ManageCertificateDTO temp = new ManageCertificateDTO();
                temp.setSysUserId(woDto.getFtId());
                temp.setWoIdList(woDto.getWoTypeId().toString());
                List<ManageCareerDTO> listCareerSearch = manageCareerDAO.findByWoIdList(woDto.getWoTypeId());
                if (listCareerSearch == null || listCareerSearch.size() == 0) {
                    result = false;
                } else {
                    List<ManageCertificateDTO> ls = manageCertificateDAO.getListCertificateEnableFT(temp);
                    if (ls.size() == 0) {
                        result = true;
                    } else
                        result = false;
                }

            }
        }
        return result;
    }
    // duoghv13 end

    private Double confirmChecklistQuantityByDate(Long woMappingChecklistId, Long sysUserId, String employeeCode) {
        if (woMappingChecklistId == null || sysUserId == null)
            return 0d;

        Double confirmedQuantity = 0d;

        WoTaskDailyDTO searchDto = new WoTaskDailyDTO();
        searchDto.setWoMappingChecklistId(woMappingChecklistId);
        searchDto.setConfirm("0");

        // tìm task daily chưa được duyệt (confirm = 0)
        List<WoTaskDailyBO> listTaskDaily = woTaskDailyDAO.doSearch(searchDto);

        if (listTaskDaily.size() == 0)
            return 0d;

        // duyệt hết 1 lượt
        for (WoTaskDailyBO task : listTaskDaily) {
            confirmedQuantity += task.getQuantity();

            task.setConfirm("1");
            task.setApproveDate(new Date());
            task.setApproveBy(employeeCode);
            task.setConfirmUserId(sysUserId);

            woTaskDailyDAO.updateObject(task);
        }

        return confirmedQuantity;
    }

    private void confirmAllQuantityByDate(Long woId, Long sysUserId, String employeeCode) {
        WoMappingChecklistDTO searchDto = new WoMappingChecklistDTO();
        searchDto.setWoId(woId);
        List<WoMappingChecklistDTO> checklist = woMappingChecklistDAO.doSearch(searchDto);
        for (WoMappingChecklistDTO item : checklist) {
            if ("1".equalsIgnoreCase(item.getQuantityByDate())) {
                Double confirmedQuantity = confirmChecklistQuantityByDate(item.getId(), sysUserId, employeeCode);
                Double currentQuantity = item.getQuantityLength();
                if (currentQuantity == null)
                    currentQuantity = 0d;
                Double newQuantity = currentQuantity + confirmedQuantity;
                item.setQuantityLength(newQuantity);
                woMappingChecklistDAO.updateObject(item.toModel());
            }
        }
    }

    private Double confirmChecklistQuantityByMonth(Long woMappingChecklistId, Long sysUserId, String employeeCode) {
        if (woMappingChecklistId == null || sysUserId == null)
            return 0d;

        Double confirmedQuantity = 0d;

        WoTaskMonthlyDTO searchDto = new WoTaskMonthlyDTO();
        searchDto.setWoMappingChecklistId(woMappingChecklistId);
        searchDto.setConfirm("0");

        // tìm task monthly chưa được duyệt (confirm = 0)
        List<WoTaskMonthlyDTO> listTaskMonthly = woTaskMonthlyDAO.doSearch(searchDto);

        if (listTaskMonthly.size() == 0)
            return 0d;

        // duyệt hết 1 lượt
        for (WoTaskMonthlyDTO task : listTaskMonthly) {
            confirmedQuantity += task.getQuantity();

            task.setConfirm("1");
            task.setApproveDate(new Date());
            task.setApproveBy(employeeCode);
            task.setConfirmUserId(sysUserId);

            woTaskMonthlyDAO.updateObject(task.toModel());
        }

        return confirmedQuantity;
    }

    private void confirmAllQuantityByMonth(Long woId, Long sysUserId, String employeeCode) {
        WoMappingChecklistDTO searchDto = new WoMappingChecklistDTO();
        searchDto.setWoId(woId);
        List<WoMappingChecklistDTO> checklist = woMappingChecklistDAO.doSearch(searchDto);
        for (WoMappingChecklistDTO item : checklist) {
            if ("2".equalsIgnoreCase(item.getQuantityByDate())) {
                Double confirmedQuantity = confirmChecklistQuantityByMonth(item.getId(), sysUserId, employeeCode);
                Double currentQuantity = item.getQuantityLength();
                if (currentQuantity == null)
                    currentQuantity = 0d;
                Double newQuantity = currentQuantity + confirmedQuantity;
                item.setQuantityLength(newQuantity);
                woMappingChecklistDAO.updateObject(item.toModel());
            }
        }
    }

    private void tryCompleteConstruction(Long constructionId) {
        if (constructionId == null)
            return;
        List<Long> workItemStatusList = woDAO.getAllWorkItemStatusByConstruction(constructionId);

        boolean isComplete = true;

        for (Long status : workItemStatusList) {
            if (status != 3)
                isComplete = false;
        }

        if (isComplete)
            woDAO.tryCompleteConstruction(constructionId);
    }

    public boolean acceptQuantityByDate(WoTaskDailyDTO dto) {
        try {
            Gson gson = new Gson();
            // chấp nhận sản lượng
            WoTaskDailyBO task = woTaskDailyDAO.getOneRaw(dto.getId());
            task.setConfirm("1");
            task.setApproveDate(new Date());
            task.setApproveBy(dto.getApproveBy());
            task.setConfirmUserId(dto.getConfirmUserId());
            woTaskDailyDAO.updateObject(task);

            // ghi nhận sản lượng đã chấp nhận vào WO
            Double taskValue = task.getQuantity();
            WoMappingChecklistBO checklistItem = woMappingChecklistDAO.getOneRaw(task.getWoMappingChecklistId());
            Double checklistItemValue = checklistItem.getQuantityLength();
            if (checklistItemValue == null)
                checklistItemValue = 0d;
            Double newChecklistItemValue = checklistItemValue + taskValue;
            checklistItem.setQuantityLength(newChecklistItemValue);
            woMappingChecklistDAO.updateObject(checklistItem);

            // ghi log
            String logStr = "Cập nhật sản lượng theo ngày. ";
            Double approvedQuantity = taskValue / 1000000;
            WoBO wo = woDAO.getOneRaw(task.getWoId());
            logWoWorkLogs(wo.toDTO(), "1", logStr + "Sản lượng: " + approvedQuantity + " triệu.", null,
                    dto.getApproveBy());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void tryGetCdNames(WoDTO wo) {
        if (StringUtils.isNullOrEmpty(wo.getCdLevel1Name()) && !StringUtils.isNullOrEmpty(wo.getCdLevel1())) {
            Long cd1 = Long.parseLong(wo.getCdLevel1());
            WoSimpleSysGroupDTO cdLv1 = trDAO.getSysGroupById(cd1);
            wo.setCdLevel1Name(cdLv1.getGroupName());
        }

        if (StringUtils.isNullOrEmpty(wo.getCdLevel2Name()) && !StringUtils.isNullOrEmpty(wo.getCdLevel2())) {
            Long cd2 = Long.parseLong(wo.getCdLevel2());
            WoSimpleSysGroupDTO cdLv2 = trDAO.getSysGroupById(cd2);
            wo.setCdLevel2Name(cdLv2.getGroupName());
        }

        if (StringUtils.isNullOrEmpty(wo.getCdLevel3Name()) && !StringUtils.isNullOrEmpty(wo.getCdLevel3())) {
            Long cd3 = Long.parseLong(wo.getCdLevel3());
            WoSimpleSysGroupDTO cdLv3 = trDAO.getSysGroupById(cd3);
            wo.setCdLevel3Name(cdLv3.getGroupName());
        }

        if (StringUtils.isNullOrEmpty(wo.getCdLevel4Name()) && !StringUtils.isNullOrEmpty(wo.getCdLevel4())) {
            Long cd4 = Long.parseLong(wo.getCdLevel4());
            WoSimpleSysGroupDTO cdLv4 = trDAO.getSysGroupById(cd4);
            wo.setCdLevel4Name(cdLv4.getGroupName());
        }
    }

    boolean validateDateFormat(String date) {
        String dateBreaking[] = date.split("/");
        if (Integer.parseInt(dateBreaking[1]) > 12) {
            return false;
        }
        if (Integer.parseInt(dateBreaking[2]) < 1900) {
            return false;
        }
        if (Integer.parseInt(dateBreaking[0]) > 31) {
            return false;
        }
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        sdfrmt.setLenient(false);
        try {
            Date javaDate = sdfrmt.parse(date);
            // System.out.println(date+" is valid date format");
        } catch (Exception e) {
            // System.out.println(date+" is Invalid Date format");
            return false;
        }
        return true;
    }

    public DataListDTO doSearchDetailReportWoTr(ReportWoTrDTO obj) {
        List<WoDTO> ls = new ArrayList<>();
        if (obj.getType().equals("1")) {
            ls = trDAO.doSearchReportTrWoDetail(obj);
        }
        if (obj.getType().equals("2")) {
            ls = trDAO.doSearchReportWoGroupDetail(obj);
        }
        if (obj.getType().equals("3")) {
            ls = trDAO.doSearchReportWoProDetail(obj);
        }
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public DataListDTO doSearchDetailReportTr(ReportWoTrDTO obj) {
        List<WoTrDTO> ls = new ArrayList<>();
        if (obj.getType().equals("1")) {
            ls = trDAO.doSearchReportTrDetail(obj);
        }
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    @Override
    public boolean acceptWo(WoDTO woDto) {
        String assignState = "";
        boolean acceptResult = false;

        WoBO bo = woDAO.getOneRaw(woDto.getWoId());
        WoDTO woDetail = woDAO.getOneDetails(woDto.getWoId());
        if (woDetail.getWoTypeCode().equalsIgnoreCase("TTHQ")) {
            assignState = PROCESSING;
            woDto.setState(assignState);
            acceptResult = cdAcceptWoTTHQ(woDto);
        } else {
            if (bo.getFtId() != null && !"DOANHTHU_DTHT".equalsIgnoreCase(woDetail.getWoTypeCode())) {
                if(Constant.WO_TYPE_CODE.BGBTS_VHKT.equalsIgnoreCase(woDetail.getWoTypeCode())
                && Constant.WO_STATE.ASSIGN_FT.equals(bo.getState())){
                    acceptResult=updateBoWhenAssignFt(woDto,bo);
                }else {
                assignState = ACCEPT_FT;
                woDto.setFtId(bo.getFtId());
                woDto.setState(assignState);
                woDto.setAcceptTime(new Date());
                acceptResult = changeWoState(woDto, ACCEPT_FT);
                if (acceptResult)
                    tryCreate5SForXDDD(bo, new Date());}
            } else {
                assignState = ACCEPT_CD;
                woDto.setState(assignState);
                acceptResult = cdAcceptAssignment(woDto);
            }
        }
        return acceptResult;
    }

    private boolean updateBoWhenAssignFt(WoDTO woDto, WoBO bo) {
        try {
            String loggedInUser = woDto.getLoggedInUser();
            bo.setState(CD_OK);
            bo.setAcceptTime(new Date());
            bo.setUserFtReceiveWo(loggedInUser);
            bo.setUpdateFtReceiveWo(new Date());
            bo.setUserCdApproveWo(loggedInUser);
            bo.setUpdateCdApproveWo(new Date());
            bo.setBgbtsResult(null);
            woDAO.update(bo);
            woDto = bo.toDTO();
            String content = woDAO.getNameAppParam(ACCEPT_CD, WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(bo), loggedInUser);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<WoSimpleSysGroupDTO> getCdLv3List(WoDTO woDto) {
        int level = 3;
        long higherCdId = Long.valueOf(woDto.getCdLevel2());
        return woDAO.getCds(level, higherCdId, null, false);
    }

    @Override
    public List<WoSimpleSysGroupDTO> getCdLv4List(WoDTO woDto) {
        int level = 4;
        Long higherCdId = null;
        if (woDto.getCdLevel3() != null)
            higherCdId = Long.valueOf(woDto.getCdLevel3());
        if (woDto.getCdLevel2() != null)
            higherCdId = Long.valueOf(woDto.getCdLevel2());

        return woDAO.getCds(level, higherCdId, null, false);
    }

    @Override
    public List<WoSimpleFtDTO> getFtList(WoDTO woDto) {
        long parentGroup = 0;
        boolean isFt2 = false;
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(woDto.getCdLevel2())) { // có chỉ số cdLv2 thì tìm ft trong
            // cả tỉnh (có thể thay đổi nếu định
            // nghĩa lại ft2)
            parentGroup = Long.valueOf(woDto.getCdLevel2());
            isFt2 = true;
            // Duonghv13 -start 14092021//
        } else if (org.apache.commons.lang3.StringUtils.isNotEmpty(woDto.getCdLevel3())) { // có chỉ số cdLv3 thì tìm
            // trong cả cd lv3 - only Wo
            // to cdlv4 or FT
            parentGroup = Long.valueOf(woDto.getCdLevel3());
            isFt2 = true;
        }
        // Duonghv13 - end
        else if (org.apache.commons.lang3.StringUtils.isNotEmpty(woDto.getCdLevel4())) { // có chỉ số cdLv4 thì tìm
            // trong group/đơn
            // vị/cụm/đội
            parentGroup = Long.valueOf(woDto.getCdLevel4());
        } else if (org.apache.commons.lang3.StringUtils.isNotEmpty(woDto.getCdLevel5())) { // level 5
            return getFtListCdLevel5(woDto);
        }

        return woDAO.getFtList(parentGroup, isFt2, woDto.getKeySearch());
    }

    public void tryUpdateWorkItem(List<Long> ids, Long woId) {
        for (Long id : ids) {
            WoXdddChecklistBO item = woXdddChecklistDAO.getOneRaw(id);
            if (item != null) {
                item.setHshc(woId);
                woXdddChecklistDAO.updateObject(item);
            }
        }
    }

    public boolean changeStateAndAcceptTcTct(WoDTO dto) {
        boolean result = true;
        try {
            if (dto.getWoId() != null) {
                WoDTO woDTO = woDAO.getOneDetails(dto.getWoId());
                woDTO.setState(dto.getState());
                woDTO.setLoggedInUser(dto.getLoggedInUser());
                woDTO.setText(dto.getText());
                woDTO.setEmailTcTct(dto.getEmailTcTct());
                if (dto.getMoneyValue() != null) {
                    woDTO.setMoneyValue(dto.getMoneyValue() / 1000000);
                }
                changeWoState(woDTO, dto.getState());
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public void calculateRelated5S(WoBO triggerWo, Date oldFinishDate) {
        String state = triggerWo.getState();
        if (ACCEPT_FT.equalsIgnoreCase(state) || PROCESSING.equalsIgnoreCase(state) || CD_NG.equalsIgnoreCase(state)
                || NG.equalsIgnoreCase(state)) {
            WoDTO searchObj = new WoDTO();
            searchObj.setConstructionId(triggerWo.getConstructionId());
            searchObj.setCatWorkItemTypeId(triggerWo.getCatWorkItemTypeId());
            List<WoDTO> listWo5S = woDAO.get5SWoByConstructionWorkItem(searchObj);

            // xóa wo 5s nếu hạn hoàn thành lớn hơn hạn hoàn thành mới của wo thi công
            Date newFinishDate = triggerWo.getFinishDate();
            boolean needAddMore5S = true;
            for (WoDTO wo : listWo5S) {
                if (wo.getFinishDate().after(newFinishDate)) {
                    woDAO.delete(wo.getWoId());
                    needAddMore5S = false;
                }
            }

            if (needAddMore5S) {
                // thêm wo 5s nếu wo thi công tăng thêm hạn so với wo 5s muộn nhất
                WoAppParamDTO config5S = woDAO.getCodeAppParam("5S_CONFIG", "1");
                Integer cycle5S = Integer.parseInt(config5S.getCode());

                Date startingPoint = new Date();
                if (listWo5S.size() > 0) {
                    WoDTO latestWo5S = listWo5S.get(0);
                    startingPoint = latestWo5S.getFinishDate();
                } else
                    startingPoint = triggerWo.getCreatedDate();

                Calendar start = Calendar.getInstance();
                start.setTime(startingPoint);
                Calendar end = Calendar.getInstance();
                end.setTime(newFinishDate);

                Long diff = newFinishDate.getTime() - startingPoint.getTime();
                diff = Math.abs(diff);
                int dayInMilisec = 1000 * 60 * 60 * 24;
                int diffInDays = Math.round(diff / dayInMilisec);

                if (diffInDays >= cycle5S) {
                    start.add(Calendar.DATE, cycle5S);
                    while (!start.after(end)) {
                        Date targetDay = start.getTime();
                        createWo5s(triggerWo.toDTO(), targetDay);
                        start.add(Calendar.DATE, cycle5S);
                    }
                }
            }
        }
    }

    @Override
    public BaseResponseOBJ approvedWo(WoDTO woDto, boolean checkRoleApproveHshc, String state) {
        BaseResponseOBJ resp = new BaseResponseOBJ();
        WoBO woBO = woDAO.getOneRaw(woDto.getWoId());
        WoDTO woDetails = woDAO.getOneDetails(woDto.getWoId());

        // Loai UCTT, FT chua xac nhan vat tu thi ko duoc duyet
        if ("UCTT".equalsIgnoreCase(woDetails.getWoTypeCode())) {
            if (woBO.getWoOrderConfirm() == null || woBO.getWoOrderConfirm() == 0) {
                return new BaseResponseOBJ(ERROR_CODE, "FT chưa xác nhận vật tư !", null);
            }
        }

        // Huypq-22102021-start
        if ("HSHC".equalsIgnoreCase(woDetails.getWoTypeCode()) && woDetails.getApWorkSrc() != null && woDetails.getApWorkSrc() == 4l
                && woDetails.getTrId() != null && !"DONE".equalsIgnoreCase(woDto.getState())) {
            if (checkRoleApproveHshc) {
                // Phòng điều hành TTHT duyệt wo HSHC
                if (state.equalsIgnoreCase(WAIT_PQT)) {
                    if(woDto.getMoneyValue()==null) {
                        woDto.setMoneyValue(0d);
                    }
                    if (woDto.getMoneyValue() > 10000000000d) {
                        return new BaseResponseOBJ(ERROR_CODE, "Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!", null);
                    }
                    resp = changeWoStateWrapper(woDto, WAIT_PQT);
                }

                if (state.equalsIgnoreCase(RECEIVED_PQT)) {
//					if (woDto.getMoneyValueHtct() > 100000000000d) {
//						return new BaseResponseOBJ(ERROR_CODE, "Giá trị sản lượng không được lớn hơn 100 tỷ !", null);
//					}
                    resp = changeWoStateWrapper(woDto, RECEIVED_PQT);
                }

                // Phòng quyết toán duyệt wo HSHC
                if (state.equalsIgnoreCase(WAIT_TTDTHT)) {
                    if (woDto.getMoneyValueHtct() == null) {
                        woDto.setMoneyValueHtct(0d);
                    }
                    if (woDto.getMoneyValueHtct() > 10000000000d) {
                        return new BaseResponseOBJ(ERROR_CODE, "Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!", null);
                    }
                    resp = changeWoStateWrapper(woDto, WAIT_TTDTHT);
                }

                if (state.equalsIgnoreCase(RECEIVED_TTDTHT)) {
//					if (woDto.getMoneyValueHtct() > 100000000000d) {
//						return new BaseResponseOBJ(ERROR_CODE, "Giá trị sản lượng không được lớn hơn 100 tỷ !", null);
//					}
                    resp = changeWoStateWrapper(woDto, RECEIVED_TTDTHT);
                }

                // TT ĐTHT duyệt WO HSHC
                if (state.equalsIgnoreCase(WAIT_TC_TCT)) {
                    if (woDto.getMoneyValueHtct() > 10000000000d) {
                        return new BaseResponseOBJ(ERROR_CODE, "Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!", null);
                    }
                    resp = changeWoStateWrapper(woDto, WAIT_TC_TCT);
                }

                //Huypq-20012022-start
                if (state.equalsIgnoreCase(PQT_NG)) {
                    resp = changeWoStateWrapper(woDto, PQT_NG);
                }

                if (state.equalsIgnoreCase(TTDTHT_NG)) {
                    resp = changeWoStateWrapper(woDto, TTDTHT_NG);
                }
                //Huy-end
                //Huypq-28022022-start
                if (state.equalsIgnoreCase(NG)) {
                    resp = changeWoStateWrapper(woDto, NG);
                }
                //Huy-end
//				taotq start
                if (state.equalsIgnoreCase(DTHT_PAUSE)) {
                    resp = changeWoStateWrapper(woDto, state);
                }
//				taotq end
            } else {
                if (state.equalsIgnoreCase(RECEIVED_PQT) || state.equalsIgnoreCase(RECEIVED_TTDTHT)) {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Bạn không có quyền nhận hồ sơ bản cứng!", null);
                } else if (state.equalsIgnoreCase(PQT_NG) || state.equalsIgnoreCase(TTDTHT_NG)) {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Bạn không có quyền từ chối hồ sơ bản cứng!", null);
                }
                else if (state.equalsIgnoreCase(CD_PAUSE)) {
                    resp = changeWoStateWrapper(woDto, state);
                }else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Bạn không có quyền duyệt WO hồ sơ hoàn công trụ ĐTHT!", null);
                }
            }
        } else
            // Huy-end

            if ("HSHC".equalsIgnoreCase(woDetails.getWoTypeCode())
                    && ("CD_OK".equalsIgnoreCase(woDto.getState())
                    || "TC_BRANCH_REJECTED".equalsIgnoreCase(woDto.getState()))
                    && !(woDetails.getApWorkSrc() == 4l && woDetails.getTrId() != null)) {
                // Neu co quyen duyet wo HSHC, hoac CD1 la trung tam XDDD (khong can quyen) thi
                // se duoc duyet
                if (checkRoleApproveHshc || "9006003".equalsIgnoreCase(woDetails.getCdLevel1())) {
                    if (woDto.getMoneyValue() > 10000000000d) {
                        return new BaseResponseOBJ(ERROR_CODE, "Giá trị wo lớn bất thường, đề nghị đơn vị xem lại!", null);
                    }
                    if (OK.equalsIgnoreCase(state)) {
                        // Luồng cũ
                        // boolean isHdNdt = woDAO.checkContractIsNDT(woDto.getContractId());
                        // boolean isHdHtct = woDAO.checkContractIsHTCT(woDto.getContractId());
                        // if (isHdHtct || isHdNdt) {
                        // resp = changeWoStateWrapper(woDto, state);
                        // resp.setMessage("Auto open create WO HCQT");
                        // } else {
                        // resp = changeWoStateWrapper(woDto, WAIT_TC_BRANCH);
                        // }

                        // Quy tắc mới
                        // Hợp đồng có đơn vị quyết toán là các trụ (UNIT_SETTLEMENT = 1 -> 6) thì phòng
                        // quyết toàn duyệt
                        // Hợp đồng có đơn vị quyết toán là CNKT (UNIT_SETTLEMENT = 7) thì phòng tài
                        // chính duyệt
                        // Lay thong tin hop dong
                        CntContractDTO cntContractDTO = cntContractDAO.getCntContractByCode(woBO.getContractCode());
                        // Huypq-30062021-start
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(woDetails.getCheckHTCT())
                                && woDetails.getCheckHTCT().equals("1")) {
                            resp = changeWoStateWrapper(woDto, state);
                        } else {
                            if (cntContractDTO.getUnitSettlement() != null
                                    && "7".equalsIgnoreCase(cntContractDTO.getUnitSettlement())) { // Hop dong do CNKT quyet
                                // toan
                                resp = changeWoStateWrapper(woDto, WAIT_TC_BRANCH);

                                //Trungtv-start 18082022
                                //Update hop dong khi trang thai cua wo la WAIT_TC_BRANCH và pmtStatus = 2
                                if (!ObjectUtils.isEmpty(woDto.getPmtStatus()) && woDto.getPmtStatus() == 2) {
                                    updateContractByHandoverUseDateReality(woDto);
                                }
                                //Trungtv-end
                            } else { // Cac tru quyet toan
                                resp = changeWoStateWrapper(woDto, state);
                                //trungtv-start
                                if (!ObjectUtils.isEmpty(woDto.getPmtStatus()) && woDto.getPmtStatus() == 2) {
                                    updateContractByHandoverUseDateReality(woDto);
                                }
                                //trungtv-end
                            }
                        }
                        // Huypq-30062021-end
                    } else {
                        resp = changeWoStateWrapper(woDto, state);
                    }
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Bạn không có quyền duyệt WO hồ sơ hoàn công !", null);
                }
            } else if ("DOANHTHU".equalsIgnoreCase(woDetails.getWoTypeCode())) {
                resp = changeWoStateWrapper(woDto, WAIT_TC_TCT);
            } else {
                resp = changeWoStateWrapper(woDto, state);
                if (!ObjectUtils.isEmpty(woDto.getPmtStatus()) && woDto.getPmtStatus() == 2) {
                    updateContractByHandoverUseDateReality(woDto);
                }
            }

        return resp;
    }

    @Override
    public List<WoTaskDailyBO> doSearchWoTaskDaily(WoTaskDailyDTO searchObj) {
        return woTaskDailyDAO.doSearch(searchObj);
    }

    public List<WoConfigContractCommitteeDTO> importConfigContract(String fileInput, HttpServletRequest request)
            throws Exception {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        List<WoConfigContractCommitteeDTO> workLst = Lists.newArrayList();
        String error = "";
        try {
            File f = new File(fileInput);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            int count = 0;
            for (Row row : sheet) {

                WoConfigContractCommitteeDTO obj = new WoConfigContractCommitteeDTO();
                count++;
                if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                    String contractCode = formatter.formatCellValue(row.getCell(0));
                    String ftName = formatter.formatCellValue(row.getCell(1));
                    String role = formatter.formatCellValue(row.getCell(2));
                    String userPosition = formatter.formatCellValue(row.getCell(3));
                    obj.setContractCode(contractCode.trim());
                    obj.setUserCodeEx(ftName.trim());
                    obj.setUserRoleText(role);
                    obj.setUserPosition(userPosition);
                    obj.setUserCreated(user.getVpsUserInfo().getSysUserId().toString());
                    obj.setUserCreatedDate(new Date());
                    obj.setStatus(1l);
                    workLst.add(obj);
                }
            }
            if (workLst.size() > 0) {
                for (WoConfigContractCommitteeDTO woDTO : workLst) {
                    error = "";
                    if (StringUtils.isNullOrEmpty(woDTO.getUserRoleText())) {
                        error += "Quyền nhân viên không được để trống; ";
                    } else {
                        if (!woDTO.getUserRoleText().toUpperCase().equals("CD")
                                && !woDTO.getUserRoleText().toUpperCase().equals("FT")) {
                            error += "Quyền nhân viên không tồn tại; ";
                        } else {
                            if (woDTO.getUserRoleText().toUpperCase().equals("CD")) {
                                woDTO.setUserRole(1L);
                            }
                            if (woDTO.getUserRoleText().toUpperCase().equals("FT")) {
                                woDTO.setUserRole(2L);
                            }
                        }
                    }
                    if (!StringUtils.isNullOrEmpty(woDTO.getUserCodeEx())) {
                        SysUserDTO sysUserDTO = woDAO.checkUserFT(woDTO.getUserCodeEx(), 0L);
                        if (sysUserDTO != null) {
                            woDTO.setUserName(sysUserDTO.getFullName());
                            woDTO.setUserId(sysUserDTO.getUserId());
                            woDTO.setUserCode(sysUserDTO.getEmployeeCode());
                        } else {
                            error += "Nhân viên thực hiện không tồn tại hoặc không có quyền !; ";
                        }
                    } else {
                        error += "Nhân viên thực hiện không được để trống; ";
                    }
                    if (StringUtils.isNullOrEmpty(woDTO.getContractCode())) {
                        error += "Mã hợp đồng không được để trống; ";
                    } else {
                        boolean checkType = woConfigContractDAO.checkBooleanContractType(woDTO);
                        if (checkType == true) {
                            error += "Hợp đồng chọn không phải hợp đồng XDDD!";
                        } else {
                            WoConfigContractCommitteeDTO contractId = woConfigContractDAO.getWoConfigContract(woDTO);
                            if (contractId != null) {
                                woDTO.setContractId(contractId.getContractId());
                            } else {
                                error += "Mã hợp đồng không tồn tại !; ";
                            }
                        }
                    }
                    if (woDTO.getUserPosition().length() > 200) {
                        error += "Chức danh vượt quá độ dài ";
                    }

                    if (woDTO.getContractId() != null) {
                        boolean checkInser = woConfigContractDAO.checkWoConfigContractCommitte(woDTO);
                        if (checkInser == true) {
                            error += "Hợp đồng đã được giao cho nhân viên; ";
                        }
                    }
                    woDTO.setCustomField(error);
                }
            }
            return workLst;
        } catch (NullPointerException pointerException) {
            throw new Exception(error);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(error);
        }
    }

    private int compareDateIgnoreTime(Date d1, Date d2) {
        if (d1 == null || d2 == null)
            return -1;
        if (d1.getYear() != d2.getYear())
            return d1.getYear() - d2.getYear();
        if (d1.getMonth() != d2.getMonth())
            return d1.getMonth() - d2.getMonth();
        return d1.getDate() - d2.getDate();
    }

    private Date calculateReportDate() {
        Date now = new Date();
        if (now.getDate() > 9)
            return now;

        Calendar aCalendar = Calendar.getInstance();
        aCalendar.add(Calendar.MONTH, -1);
        aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDateOfPreviousMonth = aCalendar.getTime();
        return lastDateOfPreviousMonth;
    }

    @Override
    public List<WoSimpleFtDTO> getFtListCdLevel5(WoDTO woDto) {
        return woDAO.getFtListCdLevel5(woDAO.getOneRaw(woDto.getWoId()).getContractCode(), woDto.getKeySearch());
    }

    public File getWoConfigContractResult(List<WoConfigContractCommitteeDTO> dtos) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_ContractConfig_Result.xlsx";
        String outFileName = "Template_WO_XL_ContractConfig_Result.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);
        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 30000)) {
                Sheet importSheet = workbook.getSheetAt(0);
                CellStyle errorStyle = importSheet.getWorkbook().createCellStyle();
                errorStyle.setFillForegroundColor(IndexedColors.RED.index);
                errorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

                int rowNo = 0;
                int stt = 0;
                for (WoConfigContractCommitteeDTO wo : dtos) {
                    stt = stt + 1;
                    Row row = importSheet.createRow(++rowNo);
                    row.createCell(0).setCellValue(stt);
                    row.createCell(1).setCellValue(wo.getContractCode() != "" ? wo.getContractCode() : "");
                    row.createCell(2).setCellValue(wo.getUserCodeEx() != "" ? wo.getUserCodeEx() : "");
                    row.createCell(3).setCellValue(wo.getUserRoleText() != "" ? wo.getUserRoleText() : "");
                    row.createCell(4).setCellValue(wo.getUserPosition() != "" ? wo.getUserPosition() : "");
                    row.createCell(5).setCellValue(wo.getCustomField() != "" ? wo.getCustomField() : "");
                }
                // write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }

        return outFile;
    }

    @Override
    public void tryResetXdddHshc(Long woId) {
        woXdddChecklistDAO.tryResetXdddHshc(woId);
    }

    @Override
    public WoOverdueReasonBO getWoOverdueReason(WoOverdueReasonDTO dto) {
        Long overdueReasonId = woOverdueReasonDAO.getIdByWoId(dto.getWoId());
        if (overdueReasonId == null)
            return null;
        else
            return woOverdueReasonDAO.getOneRaw(overdueReasonId);
    }

    @Override
    public void insertOverdueReason(WoDTO dto) {
        WoBO wo = woDAO.getOneRaw(dto.getWoId());
        if (wo == null)
            return;

        WoSimpleSysUserDTO user = trDAO.getSysUser(dto.getSysUserId());
        if (user == null)
            return;

        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String reasonLog = dto.getOverdueReasonText() + "|" + user.getEmployeeCode() + "|" + user.getFullName() + "|"
                + df.format(now);
        wo.setOverdueApproveState("WAIT");
        wo.setOverdueReason(reasonLog);

        woDAO.updateObject(wo);
    }

    public void acceptRejectOverdueReason(WoDTO dto) {
        WoBO wo = woDAO.getOneRaw(dto.getWoId());
        if (wo == null)
            return;

        WoSimpleSysUserDTO user = trDAO.getSysUser(dto.getSysUserId());
        if (user == null)
            return;

        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String stateString = "";
        if (dto.getOverdueApproveState().equals("APPROVED")) {
            stateString = "Lý do được chấp thuận";
        }
        if (dto.getOverdueApproveState().equals("REJECTED")) {
            stateString = "Lý do không được chấp thuận";
        }
        String approveLog = stateString + "|" + user.getEmployeeCode() + "|" + user.getFullName() + "|"
                + df.format(now);
        wo.setOverdueApproveState(dto.getOverdueApproveState());
        wo.setOverdueApprovePerson(approveLog);
        woDAO.updateObject(wo);
    }

    public String exportFileReportXDDTHT(WoGeneralReportDTO obj) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "WO_XL_GeneralReportXDDHT.xlsx"));

        if (obj.getType().equals("2")) {
            file = new BufferedInputStream(new FileInputStream(filePath + "WO_XL_DetailsReportXDDHT.xlsx"));
        }
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        String path = "";
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);

        if (!udir.exists()) {
            udir.mkdirs();
        }
        if (obj.getType().equals("1")) {
            List<WoGeneralReportDTO> data = new ArrayList<>();
            OutputStream outFile = new FileOutputStream(
                    udir.getAbsolutePath() + File.separator + "WO_XL_GeneralReportXDDHT.xlsx");
            obj.setPageSize(null);
            obj.setPage(null);
            data = woDAO.getGeneralReportXDDTHT(obj);
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (data != null && !data.isEmpty()) {
                XSSFCellStyle style = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
                XSSFCreationHelper createHelper = workbook.getCreationHelper();
                styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                styleNumber.setAlignment(HorizontalAlignment.RIGHT);
                styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
                int i = 1;
                for (WoGeneralReportDTO dto : data) {
                    Row row = sheet.createRow(i++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue("" + (i - 1));
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue((dto.getFtName() != null) ? dto.getFtName() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue((dto.getTotalWO() != null) ? dto.getTotalWO() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue((dto.getTotalAssignCd() != null) ? dto.getTotalAssignCd() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue((dto.getTotalAcceptCd() != null) ? dto.getTotalAcceptCd() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellValue((dto.getTotalRejectCd() != null) ? dto.getTotalRejectCd() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellValue((dto.getTotalAssignFt() != null) ? dto.getTotalAssignFt() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellValue((dto.getTotalAcceptFt() != null) ? dto.getTotalAcceptFt() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellValue((dto.getTotalRejectFt() != null) ? dto.getTotalRejectFt() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellValue((dto.getTotalProcessing() != null) ? dto.getTotalProcessing() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellValue((dto.getTotalDone() != null) ? dto.getTotalDone() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(11, CellType.STRING);
                    cell.setCellValue((dto.getTotalCdOk() != null) ? dto.getTotalCdOk() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(12, CellType.STRING);
                    cell.setCellValue((dto.getTotalCdNotGood() != null) ? dto.getTotalCdNotGood() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(13, CellType.STRING);
                    cell.setCellValue((dto.getTotalOk() != null) ? dto.getTotalOk() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(14, CellType.STRING);
                    cell.setCellValue((dto.getTotalNotGood() != null) ? dto.getTotalNotGood() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(15, CellType.STRING);
                    cell.setCellValue((dto.getTotalOpinionRequest() != null) ? dto.getTotalOpinionRequest() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(16, CellType.STRING);
                    cell.setCellValue((dto.getTotalOverDue() != null) ? dto.getTotalOverDue() : 0L);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(17, CellType.STRING);
                    cell.setCellValue((dto.getTotalFinishOverDue() != null) ? dto.getTotalFinishOverDue() : 0L);
                    cell.setCellStyle(styleNumber);
                }
            }
            workbook.write(outFile);
            workbook.close();
            outFile.close();
            path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "WO_XL_GeneralReportXDDHT.xlsx");
        }

        if (obj.getType().equals("2")) {
            List<WoDTO> data = new ArrayList<>();
            obj.setPageSize(null);
            obj.setPage(null);
            data = woDAO.getWoDetailReportXDDTHT(obj);
            OutputStream outFile = new FileOutputStream(
                    udir.getAbsolutePath() + File.separator + "WO_XL_DetailsReportXDDHT.xlsx");
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (data != null && !data.isEmpty()) {
                XSSFCellStyle style = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
                XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
                XSSFCreationHelper createHelper = workbook.getCreationHelper();
                styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                styleNumber.setAlignment(HorizontalAlignment.LEFT);
                styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
                int i = 1;
                for (WoDTO dto : data) {
                    Row row = sheet.createRow(i++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue("" + (i - 1));
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue((dto.getCdLevel5Name() != null) ? dto.getCdLevel5Name() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue((dto.getFtName() != null) ? dto.getFtName() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue((dto.getWoCode() != null) ? dto.getWoCode() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue((dto.getWoName() != null) ? dto.getWoName() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellValue((dto.getState() != null) ? dto.getState() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellValue((dto.getCatWorkItemTypeName() != null) ? dto.getCatWorkItemTypeName() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellValue(
                            (dto.getCatConstructionTypeName() != null) ? dto.getCatConstructionTypeName() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(11, CellType.STRING);
                    cell.setCellValue((dto.getApWorkSrcName() != null) ? dto.getApWorkSrcName() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(12, CellType.STRING);
                    cell.setCellValue((dto.getFinishDateStr() != null) ? dto.getFinishDateStr() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(13, CellType.STRING);
                    cell.setCellValue((dto.getEndTimeStr() != null) ? dto.getEndTimeStr() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(14, CellType.STRING);
                    cell.setCellValue(
                            (dto.getUpdateCdLevel5ReceiveWoStr() != null) ? dto.getUpdateCdLevel5ReceiveWoStr() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(15, CellType.STRING);
                    cell.setCellValue((dto.getApproveDateReportWoStr() != null) ? dto.getApproveDateReportWoStr() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(16, CellType.STRING);
                    cell.setCellValue((dto.getWoNameFromConfig() != null) ? dto.getWoNameFromConfig() : "");
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(17, CellType.STRING);
                    cell.setCellValue((dto.getMoneyFlowValue() != null) ? dto.getMoneyFlowValue() : 0d);
                    cell.setCellStyle(styleNumber);
                    cell = row.createCell(18, CellType.STRING);
                    cell.setCellValue((dto.getMoneyValue() != null) ? dto.getMoneyValue() : 0d);
                    cell.setCellStyle(styleNumber);
                }
            }
            workbook.write(outFile);
            workbook.close();
            outFile.close();
            path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "WO_XL_DetailsReportXDDHT.xlsx");
        }
        return path;
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // Connect timeout
        clientHttpRequestFactory.setConnectTimeout(50000);

        // Read timeout
        clientHttpRequestFactory.setReadTimeout(50000);
        return clientHttpRequestFactory;
    }

    @Override
    public String callOutsideApi(Object req, String url) {
        try {
            RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            System.out.println(new Gson().toJson(req));
            HttpEntity<Object> request = new HttpEntity<>(req, headers);
            ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public WoDTOResponse callOutsideApiWraper(WoDTORequest request, String url) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();

        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            String apiResult = callOutsideApi(request.getOutsideApiRequest(), url);
            JSONObject jsonObject = new JSONObject(apiResult);
            String errorCode = jsonObject.getString("errorCode");

            if (errorCode.equalsIgnoreCase("00")) {
                JSONArray jsonArray = (JSONArray) jsonObject.get("listData");
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setLstData(jsonArray.toList());
            } else {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }

        response.setResultInfo(resultInfo);
        return response;
    }

    @Override
    public WoDTOResponse woSignVoffice(WoDTORequest request, String url) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();

        try {
            WoBO woBO = woDAO.getOneRaw(request.getWoId());
            WoSimpleSysUserDTO user = trDAO.getSysUser(request.getSysUserRequest().getSysUserId());
            WoSimpleSysGroupDTO group = trDAO.getSysGroupById(user.getSysGroupId());

            WoSignVofficeDTORequest req = new WoSignVofficeDTORequest();
            // "listId": [115592]
            List<Long> listId = new ArrayList<>();
            listId.add(woBO.getWoOrderId());
            req.setListId(listId);
            // "listBussinessType":["36"]
            List<String> listBussinessType = new ArrayList<>();
            listBussinessType.add(woBO.getBusinessType());
            req.setListBussinessType(listBussinessType);
            // "listEmail": ["datnv5@viettel.com.vn", "hanhpt27@viettel.com.vn",
            // "tuandn5@viettel.com.vn"]
            List<String> listEmail = new ArrayList<>();
            for (VofficeUserDTO iUser : request.getLstVofficeUsers()) {
                listEmail.add(iUser.getStrEmail());
            }
            req.setListEmail(listEmail);
            // "sysUserId": 55321
            req.setSysUserId(request.getSysUserRequest().getSysUserId());
            // "sysGroupName": "Đội Kỹ thuật Quận Nam Từ Liêm"
            req.setSysGroupName(group.getGroupName());
            // "listSignVoffice"
            List<VofficeUserDTO> lstVofficeUsers = request.getLstVofficeUsers();
            for (int i = 0; i < lstVofficeUsers.size(); i++) {
                lstVofficeUsers.get(i).setOder(i + 1);
            }
            req.setListSignVoffice(lstVofficeUsers);
            request.setOutsideApiRequest(req);

            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            String apiResult = callOutsideApi(req, url);

            JSONObject jsonObject = new JSONObject(apiResult);
            String error = jsonObject.getString("error");

            if ("".equalsIgnoreCase(error)) {
                resultInfo.setStatus(ResultInfo.RESULT_OK);
            } else {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage(error);
            }

            // resultInfo.setStatus(ResultInfo.RESULT_OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }

        response.setResultInfo(resultInfo);

        if (resultInfo.getStatus().equalsIgnoreCase(ResultInfo.RESULT_OK)) {
            updateWoVofficeInfo(request);
        }

        return response;
    }

    private void updateWoVofficeInfo(WoDTORequest request) {
        WoBO wo = woDAO.getOneRaw(request.getWoId());
        WoSignVofficeDTORequest outsideApiReq = (WoSignVofficeDTORequest) request.getOutsideApiRequest();

        List<VofficeUserDTO> listUnit = outsideApiReq.getListSignVoffice();

        wo.setVoState(1l);
        VofficeUserDTO requestUnit = listUnit.get(0);
        VofficeUserDTO approvedUnit = listUnit.get(1);
        VofficeUserDTO mngtUnit = listUnit.get(2);

        wo.setVoRequestRole(requestUnit.getSysRoleName());
        wo.setVoRequestDept(requestUnit.getStrEmail() + " - " + requestUnit.getFullName());

        wo.setVoApprovedRole(approvedUnit.getSysRoleName());
        wo.setVoApprovedDept(approvedUnit.getStrEmail() + " - " + approvedUnit.getFullName());

        wo.setVoMngtRole(mngtUnit.getSysRoleName());
        wo.setVoMngtDept(mngtUnit.getStrEmail() + " - " + mngtUnit.getFullName());

        woDAO.updateObject(wo);
    }

    @Override
    public WoDTOResponse woSaveImportOrder(WoDTORequest request, String url) {
        WoBO woBo = woDAO.getOneRaw(request.getWoId());
        WoSimpleStationDTO station = woDAO.getStationByCode(woBo.getStationCode());
        WoSimpleSysUserDTO user = trDAO.getSysUser(request.getSysUserRequest().getSysUserId());
        WoSimpleSysGroupDTO group = trDAO.getSysGroupById(user.getSysGroupId());

        WoSaveImportOrderDtoReq outsideReq = new WoSaveImportOrderDtoReq();
        List<VoGoodsDTO> goods = request.getListOrderGoodsDTO();

        for (VoGoodsDTO item : goods) {
            item.setGoodsCode(item.getCode());
            item.setGoodsName(item.getName());
            item.setAmountBk(item.getAmount());
            item.setAmount(item.getAmountNeed());
        }

        outsideReq.setListOrderGoodsDTO(goods);
        outsideReq.setStockId(request.getCatStockId());
        outsideReq.setWoOrderId(request.getWoId());
        outsideReq.setCatStationId(station.getStationId());
        outsideReq.setCatStationCode(station.getStationCode());

        outsideReq.setCntContractId(woBo.getContractId());
        outsideReq.setContractCode(woBo.getContractCode());

        outsideReq.setDeptReceiveId(group.getSysGroupId());
        outsideReq.setDeptReceiveName(group.getGroupName());

        outsideReq.setShipperId(user.getSysUserId());
        outsideReq.setShipperName(user.getFullName());

        outsideReq.setCreatedBy(user.getSysUserId());
        outsideReq.setCreatedByName(user.getFullName());

        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();

        if (!"CMC".equalsIgnoreCase(woBo.getPartner())) {
            try {
                String resp = callOutsideApi(outsideReq, url);

                JSONObject jsonObj = new JSONObject(resp);
                String errorCode = jsonObj.getString("errorCode");

                if (errorCode.equalsIgnoreCase("00")) {
                    // JSONArray jsonArray = (JSONArray) jsonObj.get("listData");
                    JSONObject jData = jsonObj.getJSONObject("data");
                    String code = jData.getString("code");
                    Long orderId = jData.getLong("orderId");
                    String businessType = jData.getString("bussinessType");

                    WoBO wo = woDAO.getOneRaw(request.getWoId());
                    wo.setWoOrder(code);
                    wo.setWoOrderId(orderId);
                    wo.setBusinessType(businessType);
                    woDAO.updateObject(wo);

                    resultInfo.setStatus(ResultInfo.RESULT_OK);
                    resultInfo.setMessage(code);
                } else {
                    resultInfo.setMessage(jsonObj.getString("description"));
                    resultInfo.setStatus(ResultInfo.RESULT_NOK);
                }

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage(e.getMessage());
            }
        }

        if (ResultInfo.RESULT_OK.equalsIgnoreCase(resultInfo.getStatus())
                || "CMC".equalsIgnoreCase(woBo.getPartner())) {
            tryUpdateWoMappingGoods(goods, woBo);
            resultInfo.setStatus(ResultInfo.RESULT_OK);
        }

        response.setResultInfo(resultInfo);
        return response;
    }

    private void tryUpdateWoMappingGoods(List<VoGoodsDTO> goods, WoBO woBo) {
        woMappingGoodsDAO.delMappingObjs(woBo.getWoId());
        for (VoGoodsDTO item : goods) {
            WoMappingGoodsBO mappingBo = new WoMappingGoodsBO();
            mappingBo.setWoMappingGoodsId(0l);
            mappingBo.setWoId(woBo.getWoId());
            mappingBo.setAmount(item.getAmountBk());
            mappingBo.setAmountNeed(item.getAmountNeed());
            mappingBo.setGoodsId(item.getGoodsId());
            mappingBo.setGoodsUnitName(item.getGoodsUnitName());
            mappingBo.setName(item.getName());
            mappingBo.setIsSerial(item.getIsSerial());

            woMappingGoodsDAO.saveObject(mappingBo);
        }
    }

    public WoDTOResponse getListGoodsByWoId(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        try {
            // authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            Long woId = request.getWoId();
            WoMappingGoodsDTO searchObj = new WoMappingGoodsDTO();
            searchObj.setWoId(woId);
            List<Object> goods = (List<Object>) (Object) woMappingGoodsDAO.doSearch(searchObj);
            response.setLstGoods(goods);
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

    @Override
    public WoDTOResponse woUpdateAmountReal(WoDTORequest request) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();
        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            Long woId = request.getWoId();
            WoBO woBO = woDAO.getOneRaw(woId);
            List<VoGoodsDTO> goods = request.getListOrderGoodsDTO();

            if ("CMC".equalsIgnoreCase(woBO.getPartner())) {
                woMappingGoodsDAO.delMappingObjs(woId);
                for (VoGoodsDTO item : goods) {
                    WoMappingGoodsDTO dto = new WoMappingGoodsDTO();
                    dto.setWoId(woId);
                    dto.setGoodsId(item.getGoodsId());
                    dto.setName(StringUtils.isNullOrEmpty(item.getGoodsName()) ? item.getName() : item.getGoodsName());
                    dto.setAmount(item.getAmount());
                    dto.setAmountNeed(item.getAmountNeed());
                    dto.setAmountReal(item.getAmountReal());
                    dto.setIsSerial(item.getIsSerial());
                    Long isUsed = 0l;
                    if (StringUtils.isNullOrEmpty(item.getSerial())) {
                        if (item.getAmountReal() != null) {
                            isUsed = 1l;
                        }
                    } else {
                        isUsed = item.getIsUsed();
                    }
                    dto.setIsUsed(isUsed);
                    dto.setSerial(item.getSerial());
                    woMappingGoodsDAO.saveObject(dto.toModel());
                }
            } else {
                Set<Long> lstGoodsSerial = new HashSet<>();
                Set<Long> lstGoodsNoSerial = new HashSet<>();
                for (VoGoodsDTO item : goods) {
                    // Goods nay la loai serial
                    if (item.getIsSerial() != null && item.getIsSerial() == 1
                            && !StringUtils.isNullOrEmpty(item.getSerial())) {
                        lstGoodsSerial.add(item.getGoodsId());
                    } else {
                        lstGoodsNoSerial.add(item.getGoodsId());
                    }
                }

                // Xoa goodIds serial
                for (Long goodsId : lstGoodsSerial) {
                    woMappingGoodsDAO.delMappingObjsFollowGoodsId(goodsId);
                }

                // Insert chi tiet
                for (VoGoodsDTO item : goods) {
                    if (lstGoodsSerial.contains(item.getGoodsId())) {
                        WoMappingGoodsDTO dto = new WoMappingGoodsDTO();
                        dto.setWoId(woId);
                        dto.setGoodsId(item.getGoodsId());
                        dto.setName(item.getName());
                        dto.setAmount(item.getAmount());
                        dto.setAmountNeed(item.getAmountNeed());
                        dto.setIsSerial(item.getIsSerial());
                        dto.setIsUsed(item.getIsUsed());
                        dto.setSerial(item.getSerial());
                        woMappingGoodsDAO.saveObject(dto.toModel());
                    }

                    // Update
                    if (lstGoodsNoSerial.contains(item.getGoodsId())) {
                        WoMappingGoodsDTO searchObj = new WoMappingGoodsDTO();
                        searchObj.setWoId(woId);
                        searchObj.setGoodsId(item.getGoodsId());

                        WoMappingGoodsDTO dto = woMappingGoodsDAO.doSearch(searchObj).get(0);
                        dto.setAmountReal(item.getAmountReal());

                        woMappingGoodsDAO.updateObject(dto.toModel());
                    }
                }
            }
            woBO.setWoOrderConfirm(1l);
            woDAO.update(woBO);

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

    private void trySubmitReduceGoods(WoDTO dto) throws Exception {
        Gson gson = new Gson();
        try {
            WoComfirmAmsAssetDTORequest req = new WoComfirmAmsAssetDTORequest();
            WoSimpleSysUserDTO user = trDAO.getSysUser(dto.getFtId());
            WoBO woBo = woDAO.getOneRaw(dto.getWoId());

            WoMappingGoodsDTO searchObj = new WoMappingGoodsDTO();
            searchObj.setWoId(dto.getWoId());
            List<WoMappingGoodsDTO> goods = woMappingGoodsDAO.doSearch(searchObj);

            if ("CMC".equalsIgnoreCase(woBo.getPartner()) || "AVG".equalsIgnoreCase(dto.getWoTypeCode())) {
                String employeeCode = user.getEmployeeCode();
                Map<String, Object> mapRequest = new HashMap<>();
                mapRequest.put("employeeCode", employeeCode);
                // Get list goods
                String apiResult = callOutsideApi(mapRequest, getListGoodsForWO);
                JSONObject jsonObject = new JSONObject(apiResult);

                Type typeResultInfo = new TypeToken<ResultInfo>() {
                }.getType();
                ResultInfo resultInfo = gson.fromJson(jsonObject.getJSONObject("resultInfo").toString(),
                        typeResultInfo);

                if ("OK".equalsIgnoreCase(resultInfo.getStatus())) {
                    Type typeListMerEntity = new TypeToken<List<VoGoodsDTO>>() {
                    }.getType();
                    List<VoGoodsDTO> listMerEntity = gson.fromJson(jsonObject.getJSONArray("listMerEntity").toString(),
                            typeListMerEntity);
                    List<VoGoodsDTO> listReduces = new ArrayList<>();

                    for (WoMappingGoodsDTO iMapping : goods) {
                        // Check mapping voi vat tu KTTS
                        for (VoGoodsDTO iGoods : listMerEntity) {
                            boolean checkMapData = false;
                            if (iMapping.getIsUsed() != null && iMapping.getIsUsed() == 1) {
                                if (iMapping.getIsSerial() == 1) { // Vat tu co serial
                                    if (iMapping.getGoodsId().equals(iGoods.getGoodsId())
                                            && iMapping.getSerial().equalsIgnoreCase(iGoods.getSerial())) {
                                        iGoods.setAmountReal(1.0);
                                        checkMapData = true;
                                    }
                                } else if (iMapping.getGoodsId().equals(iGoods.getGoodsId())) { // Vat tu ko co serial
                                    iGoods.setAmountReal(iMapping.getAmountReal());
                                    checkMapData = true;
                                }
                            }
                            if (checkMapData) {
                                iGoods.setGoodsUnitId(iGoods.getCatUnitId());
                                iGoods.setGoodsUnitName(iGoods.getCatUnitName());
                                iGoods.setGoodsState(iGoods.getState());
                                iGoods.setGoodsStateName(iGoods.getState() == null ? ""
                                        : iGoods.getState() == 1 ? "Bình thường"
                                        : iGoods.getState() == 2 ? "Hỏng" : "");
                                iGoods.setPrice(iGoods.getApplyPrice());
                                listReduces.add(iGoods);
                                break;
                            }
                        }
                    }

                    mapRequest.put("listGoods", listReduces);
                    mapRequest.put("woId", dto.getWoId());
                    System.out.println(
                            "reduceGoodsInStock Bodyyyyyyyyyyyyyyyyyyyyyyyyyyy: " + String.valueOf(mapRequest));
                    String apiResultReduce = callOutsideApi(mapRequest, reduceGoodsInStock);
                    JSONObject jsonObjectReduce = new JSONObject(apiResultReduce);

                    ResultInfo resultInfoReduce = gson.fromJson(jsonObjectReduce.getJSONObject("resultInfo").toString(),
                            typeResultInfo);
                    if ("OK".equalsIgnoreCase(resultInfoReduce.getStatus())) {
                        dto.setCustomField("Trừ kho cá nhân thành công !");
                    } else {
                        dto.setCustomField(resultInfoReduce.getMessage());
                    }
                }
            } else {
                req.setCreatedBy(user.getSysUserId());
                req.setCreatedByName(user.getFullName());
                req.setOrderCode(woBo.getWoOrder());

                // Lay danh sach vat tu
                Set<Long> lstGoodsSerial = new HashSet<>();
                Set<Long> lstGoodsNoSerial = new HashSet<>();

                for (WoMappingGoodsDTO item : goods) {
                    if (item.getIsSerial() == null || item.getIsSerial() == 0) {
                        if (item.getAmountReal() != null && item.getAmountReal() != 0) {
                            lstGoodsNoSerial.add(item.getGoodsId());
                        }
                    } else {
                        lstGoodsSerial.add(item.getGoodsId());
                    }
                }

                Map<String, Object> mapReq = new HashMap<>();

                // Generate goood normal
                for (Long iGoods : lstGoodsNoSerial) {
                    Map<String, Object> mapGoods = new HashMap<>();
                    for (WoMappingGoodsDTO item : goods) {
                        if (item.getGoodsId().equals(iGoods)) {
                            mapGoods.put("number", item.getAmountReal());
                            mapReq.put("" + iGoods, mapGoods);
                            break;
                        }
                    }
                }

                // Generate goood serial
                for (Long iGoods : lstGoodsSerial) {
                    Map<String, Object> mapValue = new HashMap<>();
                    Map<String, Object> mapValueFollowGoods = new HashMap<>();
                    for (WoMappingGoodsDTO item : goods) {
                        if (item.getGoodsId().equals(iGoods) && item.getIsUsed() != null && item.getIsUsed() == 1) {
                            mapValueFollowGoods.put(item.getSerial(), iGoods);
                        }
                    }

                    if (mapValueFollowGoods.keySet().size() > 0) {
                        mapValue.put("mapValue", mapValueFollowGoods);
                        mapReq.put("" + iGoods, mapValue);
                    }

                }

                req.setMapReq(mapReq);
                System.out.println("woComfirmAmsAssetReq Bodyyyyyyyyyyyyyyyyyyyyyyyyyyy: " + String.valueOf(req));
                String resp = callOutsideApi(req, woConfirmAmsAssetReqUrl);

                JSONObject jsonObj = new JSONObject(resp);
                String errorCode = jsonObj.getString("errorCode");

                if ("00".equalsIgnoreCase(errorCode)) {
                    dto.setCustomField("Trừ kho thành công !");
                } else {
                    dto.setCustomField(jsonObj.getString("description"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public WoDTOResponse checkVofficeInfo(WoDTORequest request, String url) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();

        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            WoBO wo = woDAO.getOneRaw(request.getWoId());
            if (StringUtils.isNullOrEmpty(wo.getWoOrder())) {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Chưa trình ký Voffice");
                response.setResultInfo(resultInfo);
                return response;
            }
            Map<String, String> req = new HashMap<>();
            req.put("keySearch", wo.getWoOrder());
            String resp = callOutsideApi(req, url);

            JSONObject jsonObj = new JSONObject(resp);
            Long total = jsonObj.getLong("total");

            if (total == 1) {
                JSONArray resultArray = jsonObj.getJSONArray("data");
                JSONObject data0 = resultArray.getJSONObject(0);
                Long signState = Long.parseLong(data0.getString("signState"));

                if (!signState.equals(wo.getVoState())) {
                    wo.setVoState(signState);
                    if (signState == 4) {
                        wo.setWoOrderId(null);
                        wo.setWoOrder(null);
                        wo.setVoRequestDept(null);
                        wo.setVoRequestRole(null);
                        wo.setVoApprovedDept(null);
                        wo.setVoApprovedRole(null);
                        wo.setVoMngtDept(null);
                        wo.setVoMngtRole(null);

                        woMappingGoodsDAO.delMappingObjs(wo.getWoId());
                    }
                    woDAO.updateObject(wo);
                }

                WoDTO dto = new WoDTO();
                dto.setVoState(signState);
                response.setWoDTO(dto);

                resultInfo.setStatus(ResultInfo.RESULT_OK);
            } else {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Không tìm thấy dữ liệu!");
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }

        response.setResultInfo(resultInfo);
        return response;
    }

    @Override
    public WoDTOResponse getOrderGoodsDetail(WoDTORequest request, String url) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();

        try {
            authenticateWsBusiness.validateRequest(request.getSysUserRequest());

            String apiResult = callOutsideApi(request.getOutsideApiRequest(), url);
            JSONObject jsonObject = new JSONObject(apiResult);
            String errorCode = jsonObject.getString("errorCode");

            if (errorCode.equalsIgnoreCase("00")) {
                JSONArray jsonArray = (JSONArray) jsonObject.get("listData");
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setLstData(jsonArray.toList());
            } else {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }

        response.setResultInfo(resultInfo);
        return response;
    }

    @Override
    public WoDTOResponse saveVofficepassMB(WoDTORequest request, String url) {
        WoDTOResponse response = new WoDTOResponse();
        ResultInfo resultInfo = new ResultInfo();

        try {
            SysUserRequest sysUserRequest = request.getSysUserRequest();
            authenticateWsBusiness.validateRequest(sysUserRequest);
            WoSimpleSysUserDTO user = trDAO.getSysUser(request.getSysUserRequest().getSysUserId());

            Map<String, Object> mapReq = new HashMap<>();
            mapReq.put("sysUserId", sysUserRequest.getSysUserId());
            mapReq.put("sysUserName", user.employeeCode);
            mapReq.put("vofficePass", request.getVofficePass());
            String apiResult = callOutsideApi(mapReq, url);

            JSONObject jsonObject = new JSONObject(apiResult);
            String errorCode = jsonObject.getString("errorCode");
            if (errorCode.equalsIgnoreCase("00")) {
                resultInfo.setStatus(ResultInfo.RESULT_OK);
            } else {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage(e.getMessage());
        }

        response.setResultInfo(resultInfo);
        return response;
    }

    @Override
    public void tcAcceptAllSelected(WoTcMassApproveRejectReqDTO req) throws Exception {
        Gson gson = new Gson();
        Date now = new Date();
        for (WoDTO dto : req.getListWo()) {
            WoBO bo = woDAO.getOneRaw(dto.getWoId());
            bo.setState(req.getNewState());
            String logContent = "Tài chính trụ đã duyệt.";
            if ("OK".equalsIgnoreCase(req.getNewState())) {
                bo.setUserTcTctApproveWo(req.getLoggedInUser());
                bo.setUpdateTcTctApproveWo(new Date());
                bo.setClosedTime(now);
                // Huypq-03062021-start
//				if (!"THICONG".equalsIgnoreCase(dto.getWoTypeCode()) && !"AIO".equalsIgnoreCase(dto.getWoTypeCode())
//						&& ("HSHC".equalsIgnoreCase(dto.getWoTypeCode())
//								&& !(dto.getApWorkSrc() == 4l && dto.getTrId() != null))) { // Huypq-22102021-add
                if (!"THICONG".equalsIgnoreCase(dto.getWoTypeCode()) && !"AIO".equalsIgnoreCase(dto.getWoTypeCode())) {
                    if (!"HSHC".equalsIgnoreCase(dto.getWoTypeCode()) || ("HSHC".equalsIgnoreCase(dto.getWoTypeCode())
                            && !(bo.getApWorkSrc() == 4l && bo.getTrId() != null))) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        if (day < 4) {
                            Date date = new Date(cal.getTime().getTime() - (day) * 24 * 60 * 60 * 1000);
                            bo.setApproveDateReportWo(date);
                        } else {
                            bo.setApproveDateReportWo(new Date());
                        }
                    }
                }
                // Huy-end
                // Huypq-22102021-start
                if ("HSHC".equalsIgnoreCase(dto.getWoTypeCode()) && dto.getApWorkSrc() == 4l && dto.getTrId() != null
                        && "WAIT_TC_TCT".equalsIgnoreCase(bo.getState())) {
                    bo.setMoneyValueHtct(dto.getMoneyValueHtct() * 1000000);
                    bo.setState(OK);
                }
                // Huy-end
                logContent = "Tài chính tổng công ty đã duyệt.";
            } else {
                bo.setEmailTcTct(dto.getEmailTcTct());
                bo.setUserTcBranchApproveWo(req.getLoggedInUser());
                bo.setUpdateTcBranchApproveWo(now);
            }
            woDAO.updateObject(bo);

            dto.setDontSendMocha(true);
            logWoWorkLogs(dto, "1", logContent, gson.toJson(bo), req.getLoggedInUser());
        }
    }

    @Override
    public void tcRejectAllSelected(WoTcMassApproveRejectReqDTO req) throws Exception {
        Gson gson = new Gson();
        Date now = new Date();
        for (WoDTO dto : req.getListWo()) {
            WoBO bo = woDAO.getOneRaw(dto.getWoId());
            bo.setState(req.getNewState());
            String logContent = "Tài chính trụ từ chối. Lý do: " + dto.getText();
            if ("TC_TCT_REJECTED".equalsIgnoreCase(req.getNewState())) {
                bo.setUserTcTctApproveWo(req.getLoggedInUser());
                bo.setClosedTime(now);
                logContent = "Tài chính tổng công ty từ chối." + dto.getText();
            } else {
                bo.setUserTcBranchApproveWo(req.getLoggedInUser());
                bo.setUpdateTcBranchApproveWo(now);
            }
            woDAO.updateObject(bo);

            dto.setDontSendMocha(true);
            logWoWorkLogs(dto, "1", logContent, gson.toJson(bo), req.getLoggedInUser());
        }
    }

    @Override
    public List<WoDTO> getListWoByWoHshcId(WoMappingHshcTcDTO dto) {
        List<WoDTO> lstResults = new ArrayList<>();
        List<WoDTO> lstAllWoFollowHshc = woMappingHshcTcDAO.doSearch(dto);
        WoDTO woDetail = woDAO.getOneDetails(dto.getWoHshcId());
        for (WoDTO iWoDto : lstAllWoFollowHshc) {
            if ("TR_DONG_BO_HA_TANG".equalsIgnoreCase(woDetail.getTrTypeCode())) {
                if (iWoDto.getWoOrder() != null && Long.parseLong(iWoDto.getWoOrder()) <= 5) {
                    lstResults.add(iWoDto);
                }
            } else {
                lstResults.add(iWoDto);
            }
        }
        return lstResults;
    }

    @Transactional
    @Override
    public void updateChecklistTmbt(WoDTORequest request) throws Exception {
        Long woId = request.getWoId();
        List<WoMappingStationBO> lstMappings = new ArrayList<>();
        List<CatStationBO> lstCatStions = new ArrayList<>();
        for (CatStationDTO catStationDTO : request.getLstCatStation()) {
            Long rentStatus = Long.parseLong(catStationDTO.getStatus());
            // CAT_STATION
            CatStationBO catStationBO = catStationDAO.getOneRaw(catStationDTO.getCatStationId());
            catStationBO.setRentStatus(rentStatus);
            lstCatStions.add(catStationBO);

            // WO_MAPPING_STATION
            WoMappingStationBO woMappingStationBO = new WoMappingStationBO();
            woMappingStationBO.setWoId(request.getWoId());
            woMappingStationBO.setCatStationId(catStationDTO.getCatStationId());
            woMappingStationBO.setStatus(rentStatus);
            if ("0".equalsIgnoreCase(catStationDTO.getStatus())) {
                woMappingStationBO.setReason(catStationDTO.getDescription());
            }
            lstMappings.add(woMappingStationBO);
        }

        WoMappingChecklistDTO woMappingChecklistDTO = request.getAcceptChecklistObj();
        Long checklistId = woMappingChecklistDTO.getChecklistId();
        List<ImgChecklistDTO> lstImgs = woMappingChecklistDTO.getLstImgs();

        // Delete WO_MAPPING_ATTACH
        woDAO.removeAttachOfChecklist(woId, checklistId);

        // Update WO_MAPPING_CHECKLIST
        WoMappingChecklistBO incomingBO = woMappingChecklistDTO.toModel();
        WoMappingChecklistBO dbBO = woMappingChecklistDAO.getOneRaw(incomingBO.getId());
        dbBO = transferWoMappingChecklistBoData(incomingBO, dbBO);
        woMappingChecklistDAO.updateObject(dbBO);

        // Insert image
        WoDTO woDTO = woDAO.getOneDetails(woId);
        insertWoMappingAttach(woDTO, checklistId, lstImgs);

        // Update wo mapping station
        if (lstMappings.size() > 0) {
            woMappingStationDAO.removeWoMappingStation(woId);
            woMappingStationDAO.saveList(lstMappings);
        }
        // Update cat station
        if (lstCatStions != null && lstCatStions.size() > 0) {
            catStationDAO.saveList(lstCatStions);
        }
    }

    @Transactional
    @Override
    public void updateChecklistTthq(WoDTORequest request) throws Exception {
        Long woId = request.getWoId();

        // Update wo_mapping_checklist
        WoMappingChecklistDTO woMappingChecklistDTO = request.getAcceptChecklistObj();
        Long checklistId = woMappingChecklistDTO.getChecklistId();
        List<ImgChecklistDTO> lstImgs = woMappingChecklistDTO.getLstImgs();

        // Delete WO_MAPPING_ATTACH
        woDAO.removeAttachOfChecklist(woId, checklistId);

        // Update WO_MAPPING_CHECKLIST
        WoMappingChecklistBO incomingBO = woMappingChecklistDTO.toModel();
        if (StringUtils.isNullOrEmpty(incomingBO.getName())) {
            incomingBO.setName(woMappingChecklistDTO.getChecklistName());
        }
        woMappingChecklistDAO.updateObject(incomingBO);

        // Insert image
        WoDTO woDTO = woDAO.getOneDetails(woId);
        insertWoMappingAttach(woDTO, checklistId, lstImgs);
    }

    private Long tryCreateHcqt(WoDTO woDTO, CntContractDTO cntContractDTO) throws Exception {
        WoBO woHshc = woDAO.getOneRaw(woDTO.getWoId());
        Date now = new Date();
        // Declare wo HCQT
        WoBO hcqtWo = new WoBO();
        hcqtWo.setWoId(0l);
        // Loai wo, ten wo
        hcqtWo.setWoTypeId(woTypeDAO.getIdByCode("HCQT"));
        hcqtWo.setWoName("Hoàn công quyết toán");
        hcqtWo.setState(ACCEPT_FT);

        WoSimpleStationDTO woSimpleStationDTO = woDAO.getStationByCode(woHshc.getStationCode());
        // Mã TỈNH theo mã tỉnh của mã nhà trạm CAT_PROVINCE_ID trong bảng
        // CAT_STATION_TYPE_HOUSE
        String provinceCode = "";
        if (woSimpleStationDTO != null) {
            provinceCode = woSimpleStationDTO.getCatProvinceCode();
        }

        hcqtWo.setCatProvinceCode(provinceCode);
        // Mã khu vực theo mã tỉnh
        hcqtWo.setCnkv(provinceCode);

        // Ma tram (MÃ trạm thay bằng mã nhà trạm)
        hcqtWo.setStationCode(woHshc.getStationCode());
        hcqtWo.setCatStationHouseId(woSimpleStationDTO.getCatStationHouseId());

        // Ngay tao
        hcqtWo.setCreatedDate(now);
        // Ngay nhan HSHC
        hcqtWo.setHshcReceiveDate(now);
        // Han hoan thanh
        hcqtWo.setFinishDate(new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000));
        // Dinh muc hoan thanh
        hcqtWo.setQoutaTime(24);

        // CD
        hcqtWo.setCdLevel1(woHshc.getCdLevel1());
        hcqtWo.setCdLevel1Name(woHshc.getCdLevel1Name());

        // FT
        WoSimpleSysUserDTO woSimpleSysUserDTO = trDAO.getSysUser(woDTO.getLoggedInUser());
        hcqtWo.setFtId(woSimpleSysUserDTO.getSysUserId());
        hcqtWo.setFtName(woSimpleSysUserDTO.getFullName());
        hcqtWo.setStatus(1l);

        // Sản lượng = sản lượng của các HSHC
        hcqtWo.setMoneyValue(woDTO.getMoneyValue());

        // Hop dong
        hcqtWo.setContractId(woHshc.getContractId());
        hcqtWo.setContractCode(woHshc.getContractCode());
        hcqtWo.setHcqtContractCode(woHshc.getContractCode());

        // Mã công trình = Danh sách các công trình làm thuộc mã nhà trạm của HĐONG làm
        // HCQT
        ConstructionDTO obj = new ConstructionDTO();
        obj.setCatStationId(woSimpleStationDTO.getStationId());
        List<ConstructionDetailDTO> lstConstractions = constructionDAO.getConstructionByStationId(obj);
        if (lstConstractions != null && lstConstractions.size() > 0) {
            String consCode = "";
            for (ConstructionDetailDTO i : lstConstractions) {
                consCode += i.getCode() + ", ";
            }
            hcqtWo.setConstructionCode(consCode);
        }

        // MÃ dự án lấy theo trường HCQT_PROJECT_ID trong bảng CNT_CONTRACT
        hcqtWo.setHcqtProjectId(cntContractDTO.getHcqtProjectId());

        // Mã WO
        String woCode = this.generateWoCode(hcqtWo.toDTO());
        hcqtWo.setWoCode(woCode);

        // Kế hoạch tháng = tự động sinh tháng n thì kế hoạch tháng n+1
        Long totalMonthPlanId = now.getMonth() + 2l;
        hcqtWo.setTotalMonthPlanId(totalMonthPlanId);

        Long woId = woDAO.saveObject(hcqtWo);

        // Tạo checklist
        List<WoChecklistBO> hcqtChecklist = createHcqtChecklistSet(woId);
        woChecklistDAO.saveListNoId(hcqtChecklist);

        WoDTO woDto = hcqtWo.toDTO();
        Gson gson = new Gson();
        logWoWorkLogs(woDto, "1", "Tự động tạo wo HCQT sau khi đóng wo HSHC nguồn đầu tư.", gson.toJson(woDto),
                woDTO.getLoggedInUser());

        return woId;
    }

    private Long tryCreateTthq(WoDTO woTmbt) {
        Gson gson = new Gson();
        Long woTthqId = null;
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
            List<CatStationDTO> lstCatStations = woDAO.getCatStationOfWoTmbt(woTmbt.getWoId(), true);
            for (CatStationDTO iCatStation : lstCatStations) {
                String iStationCode = iCatStation.getCode();
                // Check ma tram da duoc tao wo TTHQ chua
                if (woDAO.checkStationHasTthq(iStationCode) > 0) {
                    logWoWorkLogs(woTmbt, "1", "Mã trạm " + iStationCode + " đã có WO TTHQ và có kết quả là hiệu quả.",
                            gson.toJson(woTmbt), woTmbt.getLoggedInUser());
                }

                // Generate TMBT follow trDTO
                WoBO woBO = new WoBO();
                woBO.setStationCode(iStationCode);
                // Wo code
                String code = "VNM_PMXL_1_";
                Long woTypeId = woTypeDAO.getIdByCode("TTHQ");
                Long nextSq = woDAO.getNextSeqVal();
                Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
                code += woTypeId + "_" + countIdType + "_" + nextSq;
                woBO.setWoCode(code);
                woBO.setTrId(woTmbt.getTrId());
                woBO.setTrCode(woTmbt.getTrCode());
                // Wo name
                WoNameDTO dtoName = new WoNameDTO();
                dtoName.setWoTypeId(woTypeId);
                List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
                if (lstNames != null && lstNames.size() > 0) {
                    woBO.setWoName(lstNames.get(0).getName());
                }
                woBO.setUserCreated(woTmbt.getUserCreated());
                woBO.setCdLevel1("166677");
                woBO.setCdLevel1Name("Trung tâm Đầu tư hạ tầng ");
                //Huypq-29112021-start
//				woBO.setCdLevel2(woTmbt.getCdLevel2());
//				woBO.setCdLevel2Name(woTmbt.getCdLevel2Name());
                woBO.setCdLevel2("166677");
                woBO.setCdLevel2Name("Trung tâm Đầu tư hạ tầng ");
                //Huy-end
                woBO.setConstructionId(woTmbt.getConstructionId());
                woBO.setConstructionCode(woTmbt.getConstructionCode());
                woBO.setContractId(woTmbt.getContractId());
                woBO.setContractCode(woTmbt.getContractCode());
                woBO.setMoneyValue(0.0);
                woBO.setCreatedDate(today);
                woBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
                woBO.setWoTypeId(woTypeId);
                woBO.setQoutaTime(woTmbt.getQoutaTime());
                // Finish date
                woBO.setFinishDate(new Date(today.getTime() + 5 * 24 * 60 * 60 * 1000));
                woBO.setState(PROCESSING); // Huypq-05072021-edit CD_OK thanh ASSIGN_CD
//				woBO.setFtId(woTmbt.getFtId());
//				woBO.setFtName(woTmbt.getFtName());
                woBO.setStatus(1l);
                woTthqId = woDAO.saveObject(woBO);

                WoDTO dto = woBO.toDTO();
                dto.setIsCreateNew(true);
                // Worklogs
                logWoWorkLogs(dto, "1", "Tự động sinh WO TTHQ khi TTHT duyệt đóng WO TMBT.", gson.toJson(woBO),
                        woTmbt.getUserCreated());

                // Create checklist
                WoMappingChecklistBO woMappingChecklistBO = new WoMappingChecklistBO();
                woMappingChecklistBO.setWoId(woBO.getWoId());
                woMappingChecklistBO.setCheckListId(1l);
                woMappingChecklistBO.setState("DONE");
                woMappingChecklistBO.setStatus(1l);
                woMappingChecklistBO.setName(lstNames.get(0).getName());
                woMappingChecklistBO.setTthqResult("Hiệu quả");
                woMappingCheckListDAO.saveObject(woMappingChecklistBO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return woTthqId;
    }

    private Long tryCreateTkdt(WoDTO woTthq) {
        Gson gson = new Gson();
        Long woTthqId = null;
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
            // Generate TMBT follow trDTO
            WoBO woBO = new WoBO();
            woBO.setStationCode(woTthq.getStationCode());
            // Wo code
            String code = "VNM_PMXL_1_";
            Long woTypeId = woTypeDAO.getIdByCode("TKDT");
            Long nextSq = woDAO.getNextSeqVal();
            Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
            code += woTypeId + "_" + countIdType + "_" + nextSq;
            woBO.setWoCode(code);
            woBO.setTrId(woTthq.getTrId());
            woBO.setTrCode(woTthq.getTrCode());
            // Wo name
            WoNameDTO dtoName = new WoNameDTO();
            dtoName.setWoTypeId(woTypeId);
            List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
            if (lstNames != null && lstNames.size() > 0) {
                woBO.setWoName(lstNames.get(0).getName());
            }
            woBO.setUserCreated(woTthq.getUserCreated());
            woBO.setCdLevel1("166677");
            woBO.setCdLevel1Name("Trung tâm Đầu tư hạ tầng ");
            woBO.setCdLevel2("166689");
            woBO.setCdLevel2Name("Phòng tư vấn thiết kế");
            woBO.setCdLevel3("166686");
            woBO.setCdLevel3Name("Phòng quản lý dự án");
            woBO.setConstructionId(woTthq.getConstructionId());
            woBO.setContractCode(woTthq.getContractCode());
            woBO.setMoneyValue(0.0);
            woBO.setCreatedDate(today);
            woBO.setStartTime(today);
            woBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
            woBO.setWoTypeId(woTypeId);
            woBO.setQoutaTime(woTthq.getQoutaTime());
            // Finish date
//			cal.add(Calendar.MONTH, 1);
//			cal.set(Calendar.DAY_OF_MONTH, 1);
//			cal.add(Calendar.DATE, -1);
            woBO.setFinishDate(new Date(today.getTime() + 7 * 24 * 60 * 60 * 1000));
            woBO.setState(PROCESSING);
            woBO.setStatus(1l);
            woTthqId = woDAO.saveObject(woBO);

            WoDTO dto = woBO.toDTO();
            dto.setIsCreateNew(true);
            // Worklogs
            logWoWorkLogs(woBO.toDTO(), "1", "Tự động sinh WO TKDT khi TTĐTHT duyệt đóng WO TTHQ.", gson.toJson(woBO),
                    woTthq.getUserCreated());

            // Create checklist
            List<WoChecklistBO> hcqtChecklist = createChecklistByParType(woTthqId, "TKDT_CHECKLIST");
            woChecklistDAO.saveListNoId(hcqtChecklist);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return woTthqId;
    }

    public List<WoChecklistBO> createChecklistByParType(Long woId, String parType) {
        List<WoChecklistBO> newSet = new ArrayList<>();
        List<WoAppParamDTO> lstChecklistNames = woDAO.getAppParam(parType);
        for (WoAppParamDTO param : lstChecklistNames) {
            WoChecklistBO item = new WoChecklistBO();
            item.setStatus(1l);
            item.setWoId(woId);
            item.setChecklistName(param.getName());
            item.setState("NEW");
            item.setChecklistOrder(param.getParOrder());
            if ("AVG_CHECKLIST".equals(param.getParType())) {
                item.setCode(param.getCode());
            }
            newSet.add(item);
        }

        return newSet;
    }

    public void completeTkdtChecklist(WoChecklistDTO obj) {
        Gson gson = new Gson();
        Date completeDate = new Date();
        WoChecklistBO bo = woChecklistDAO.getOneRaw(obj.getChecklistId());
        bo.setState("DONE");
        if (bo.getChecklistOrder() == 1) {
            bo.setDnqtDate(completeDate);
        } else if (bo.getChecklistOrder() == 2) {
            bo.setVtnetSendDate(completeDate);
        } else if (bo.getChecklistOrder() == 3) {
            bo.setVtnetConfirmDate(completeDate);
        }
        bo.setCompletedDate(completeDate);
        try {
            // Update wo_checklist
            woChecklistDAO.updateObject(bo);

            // Update wo
            WoBO wo = woDAO.getOneRaw(bo.getWoId());
            wo.setChecklistStep(bo.getChecklistOrder());
            if (bo.getChecklistOrder() == 3) {
                wo.setEndTime(completeDate);
                wo.setState("DONE");
            }
            woDAO.updateObject(wo);

            // Write log
            WoDTO dto = wo.toDTO();
            dto.setWoTypeCode("TKDT");
            logWoWorkLogs(dto, "1", "Hoàn thành đầu việc: " + bo.getChecklistName(), gson.toJson(wo),
                    obj.getLoggedInUser());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void approvedWoOk(WoDTO dto, HttpServletRequest request) {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        Gson gson = new Gson();
        try {
            WoBO woBO = woDAO.getOneRaw(dto.getWoId());

            woBO.setState(OK);

            // Hop dong
            CntContractDTO cntContractDTO = cntContractDAO.getCntContractByCode(dto.getContractCode());
            woBO.setContractId(cntContractDTO.getCntContractId());
            woBO.setContractCode(cntContractDTO.getCode());

            // Cong trinh
            ConstructionDTO constructionDTO = constructionDAO.getConstructionByCode(dto.getConstructionCode());
            if(constructionDTO != null) {
                woBO.setConstructionId(constructionDTO.getConstructionId());
                woBO.setConstructionCode(constructionDTO.getCode());
            }

            // Huypq-02102021-start
            woBO.setApproveDateReportWo(new Date());
            woDAO.update(woBO);
            // Huy-end

            // Huypq-25102021-start
            List<String> lstWiName = new ArrayList<>();
            for (CatWorkItemTypeDTO wi : dto.getListWorkItem()) {
                lstWiName.add(wi.getName());
            }

            HashMap<String, Long> mapWi = new HashMap<>();
            HashMap<String, String> mapWiCode = new HashMap<>();
            List<WoCatWorkItemTypeDTO> lstWiType = woDAO.getCatWorkItemTypeIdByLstName(lstWiName);
            for (WoCatWorkItemTypeDTO wi : lstWiType) {
                mapWi.put(wi.getName(), wi.getCatWorkItemTypeId());
                mapWiCode.put(wi.getName(), wi.getCatWorkItemTypeCode());
            }

            if (dto.getListWorkItem() != null && dto.getListWorkItem().size() > 0) {
                for (CatWorkItemTypeDTO wi : dto.getListWorkItem()) {
                    Long wiId = mapWi.get(wi.getName());
                    String wiCode = mapWiCode.get(wi.getName());
                    WoMappingWorkItemHtctDTO mappingHtct = new WoMappingWorkItemHtctDTO();
                    mappingHtct.setWoId(woBO.getWoId());
                    mappingHtct.setWoCode(woBO.getWoCode());
                    mappingHtct.setContractId(cntContractDTO.getCntContractId());
                    mappingHtct.setContractCode(cntContractDTO.getCode());
                    mappingHtct.setConstructionId(constructionDTO.getConstructionId());
                    mappingHtct.setConstructionCode(constructionDTO.getCode());
                    mappingHtct.setWorkItemId(wiId);
                    mappingHtct.setWorkItemName(wi.getName());
                    mappingHtct.setCreatedDate(new Date());
                    mappingHtct.setCreatedUserId(user.getSysUserId());
                    mappingHtct.setWorkItemValue((wi.getWorkItemValue() != null ? wi.getWorkItemValue() : 0d) * 1000000);
                    mappingHtct.setStatus("1");
                    mappingHtct.setTrId(woBO.getTrId());
                    mappingHtct.setTrCode(woBO.getTrCode());
                    woMappingWorkItemHtctDAO.saveObject(mappingHtct.toModel());

                    if (wiId != null && woBO.getConstructionId() != null) {
                        if (!woDAO.checkExistConstructionWorkItem(woBO.getConstructionId(),
                                wiId)) {
                            WoDTO newWo = new WoDTO();
                            newWo.setConstructionId(woBO.getConstructionId());
                            newWo.setCatWorkItemTypeId(wiId);
                            newWo.setConstructionCode(constructionDTO.getCode());
                            newWo.setCatWorkItemTypeCode(wiCode);
                            newWo.setCatWorkItemTypeName(wi.getName());
                            newWo.setCdLevel2(woBO.getCdLevel2());
                            newWo.setSysUserId(user.getSysUserId());
                            newWo.setSysGroupId(user.getSysUserId());
                            // newWo.setMoneyValueHtct(wi.getWorkItemValue());
                            woDAO.insertWorkItem(newWo);
                        }
                    }

//					if(wi.getWorkItemValue() > 0) {
//						createWoThiCongWhenAccessWoKhoiCong(woBO, constructionDTO, cntContractDTO);
//					}

                }
            }

            // Sinh wo Khởi công khi đóng wo TKDT
            createWoByParOrder(woBO, constructionDTO, cntContractDTO, "1");
            // Huy-end

            // Write log
            WoDTO woDTO = woBO.toDTO();
            logWoWorkLogs(woDTO, "1", "Hoàn thành WO", gson.toJson(woDTO), dto.getLoggedInUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<WorkItemDTO> getListItem(String code) {
        // TODO Auto-generated method stub
        List<WorkItemDTO> lst = woDAO.getListItem(code);
        return lst;
    }

    // taotq 07052021 start
    public File exportConfigWorkItemList(List<WoScheduleConfigDTO> lst) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "Export_WO_Config_Work_ItemS.xlsx";
        String outFileName = "Export_List_WO_Config_Work_ItemS.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        List<WoAppParamDTO> stateList = woDAO.getAppParam("WO_TR_XL_STATE");
        Map<String, String> stateMap = new HashMap<>();
        for (WoAppParamDTO item : stateList) {
            stateMap.put(item.getCode(), item.getName());
        }
        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 100)) {
                SXSSFSheet sheet = workbook.getSheetAt(0);

                int rowNum = 0;
                for (WoScheduleConfigDTO woSC : lst) {
                    Row row = sheet.createRow(++rowNum);
                    row.createCell(0).setCellValue(rowNum);
                    if (woSC.getScheduleConfigCode() != null)
                        row.createCell(1).setCellValue(woSC.getScheduleConfigCode());
                    if (woSC.getScheduleConfigName() != null)
                        row.createCell(2).setCellValue(woSC.getScheduleConfigName());
                    if (woSC.getWoTRCode() != null)
                        row.createCell(3).setCellValue(woSC.getWoTRCode());
                    if (woSC.getStartTimeString() != null)
                        row.createCell(4).setCellValue(woSC.getStartTimeString());
                    if (woSC.getEndTimeString() != null)
                        row.createCell(5).setCellValue(woSC.getEndTimeString());
                    if (woSC.getStatus() == 1)
                        row.createCell(6).setCellValue("Bật");
                    if (woSC.getStatus() == 0)
                        row.createCell(6).setCellValue("Tắt");
                    if (woSC.getCdLevel1Name() != null)
                        row.createCell(7).setCellValue(woSC.getCdLevel1Name());
                    if (woSC.getCdLevel2Name() != null)
                        row.createCell(8).setCellValue(woSC.getCdLevel2Name());
                }

                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }
        return outFile;
    }
    // taotq 07052021 end

    private Long tryCreateYcDoanhThu(WoDTO woPs) {
        Gson gson = new Gson();
        Long woPsId = null;
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
            WoBO woBO = new WoBO();
            woBO.setStationCode(woPs.getStationCode());
            // Wo code
            String code = "VNM_PMXL_1_";
            Long woTypeId = woTypeDAO.getIdByCode("KY_HD");
            Long nextSq = woDAO.getNextSeqVal();
            Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
            code += woTypeId + "_" + countIdType + "_" + nextSq;
            woBO.setWoCode(code);
            woBO.setTrId(woPs.getTrId());
            woBO.setTrCode(woPs.getTrCode());
            // Wo name
            WoNameDTO dtoName = new WoNameDTO();
            dtoName.setWoTypeId(woTypeId);
            List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
            if (lstNames != null && lstNames.size() > 0) {
                woBO.setWoName(lstNames.get(0).getName());
            }
            woBO.setUserCreated(woPs.getUserCreated());
            woBO.setCdLevel1("166677");
            woBO.setCdLevel1Name("Trung tâm Đầu tư hạ tầng ");
            woBO.setCdLevel2(woPs.getCdLevel2());
            woBO.setCdLevel2Name(woPs.getCdLevel2Name());
            woBO.setConstructionId(woPs.getConstructionId());
            woBO.setContractCode(woPs.getContractCode());
            woBO.setMoneyValue(0.0);
            woBO.setCreatedDate(today);
            woBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
            woBO.setWoTypeId(woTypeId);
            woBO.setQoutaTime(woPs.getQoutaTime());
            // Finish date
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.DATE, -1);
            woBO.setFinishDate(cal.getTime());
            woBO.setState(ASSIGN_CD);
            woBO.setStatus(1l);
            woPsId = woDAO.saveObject(woBO);

            // Worklogs
            logWoWorkLogs(woBO.toDTO(), "1",
                    "Tự động sinh WO y/c ký HĐ lên doanh thu khi TTHT duyệt đóng WO phát sóng.", gson.toJson(woBO),
                    woPs.getUserCreated());

            // Create checklist
            List<WoChecklistBO> hcqtChecklist = createChecklistByParType(woPsId, "YC_DOANHTHU_CHECKLIST");
            woChecklistDAO.saveListNoId(hcqtChecklist);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return woPsId;
    }

    // Huypq-28062021-start
    public String exportFileImportTthq(WoDTO obj, HttpServletRequest request) throws Exception {
        //Huypq-28122021-start
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_Import_ChiTiet_TTHQ.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "BM_Import_ChiTiet_TTHQ.xlsx");

        Session session = effectiveCalculationDetailsDAO.getSessionFactory().openSession();
        EffectiveCalculationDetailsDTO effect = new EffectiveCalculationDetailsDTO();
        effect.setWoId(obj.getWoId());
//		List<EffectiveCalculationDetailsDTO> data = effectiveCalculationDetailsDAO.getDetailStationByContract(session, effect);
        List<EffectiveCalculationDetailsDTO> data = new ArrayList<>();

        XSSFSheet sheet0 = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet0);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet0);
            XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet0);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));
            int i = 2;
            for (EffectiveCalculationDetailsDTO dto : data) {
                Row row = sheet0.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getStationCodeVtn() != null) ? dto.getStationCodeVtn() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getStationCodeVcc() != null) ? dto.getStationCodeVcc() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getAddress() != null) ? dto.getAddress() : "");
                cell.setCellStyle(style);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getStationType() != null) ? dto.getStationType() : "");
                cell.setCellStyle(style);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getMaiDat() != null) ? dto.getMaiDat() : "");
                cell.setCellStyle(style);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getDoCaoCot() != null) ? dto.getDoCaoCot() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getLoaiCot() != null) ? dto.getLoaiCot() : "");
                cell.setCellStyle(style);
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue((dto.getMongCo() != null) ? dto.getMongCo() : "");
                cell.setCellStyle(style);
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue((dto.getLoaiNha() != null) ? dto.getLoaiNha() : "");
                cell.setCellStyle(style);
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue((dto.getTiepDia() != null) ? dto.getTiepDia() : "");
                cell.setCellStyle(style);
                cell = row.createCell(13, CellType.STRING);
                cell.setCellValue((dto.getDienCnkt() != null) ? dto.getDienCnkt() : "");
                cell.setCellStyle(style);
                cell = row.createCell(14, CellType.STRING);
                cell.setCellValue((dto.getSoCotDien() != null) ? dto.getSoCotDien() : "");
                cell.setCellStyle(style);
                cell = row.createCell(15, CellType.STRING);
                cell.setCellValue((dto.getVanChuyenBo() != null) ? dto.getVanChuyenBo() : "");
                cell.setCellStyle(style);
                cell = row.createCell(16, CellType.STRING);
                cell.setCellValue((dto.getThueAcquy() != null) ? dto.getThueAcquy() : "");
                cell.setCellStyle(style);
                cell = row.createCell(17, CellType.STRING);
                cell.setCellValue((dto.getGiaThueMbThucTe() != null) ? dto.getGiaThueMbThucTe() : 0l);
                cell.setCellStyle(style);
                cell = row.createCell(18, CellType.STRING);
                cell.setCellValue((dto.getTongCapexHt() != null) ? dto.getTongCapexHt() : 0l);
                cell.setCellStyle(style);
                cell = row.createCell(19, CellType.STRING);
                cell.setCellValue((dto.getVccChaoGiaHt() != null) ? dto.getVccChaoGiaHt() : 0l);
                cell.setCellStyle(style);
                cell = row.createCell(20, CellType.STRING);
                cell.setCellValue((dto.getVccChaoGiaAcquy() != null) ? dto.getVccChaoGiaAcquy() : 0l);
                cell.setCellStyle(style);
                cell = row.createCell(21, CellType.STRING);
                cell.setCellValue((dto.getTongCong() != null) ? dto.getTongCong() : 0l);
                cell.setCellStyle(style);
                cell = row.createCell(22, CellType.STRING);
                cell.setCellValue((dto.getNpv() != null) ? dto.getNpv() : 0l);
                cell.setCellStyle(style);
                cell = row.createCell(23, CellType.STRING);
                cell.setCellValue((dto.getIrr() != null) ? dto.getIrr() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(24, CellType.STRING);
                cell.setCellValue((dto.getThoiGianHv() != null) ? dto.getThoiGianHv() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(25, CellType.STRING);
                cell.setCellValue((dto.getLnstDt() != null) ? dto.getLnstDt() : 0d);
                cell.setCellStyle(style);
                cell = row.createCell(26, CellType.STRING);
                cell.setCellValue((dto.getConclude() != null) ? dto.getConclude() : "");
                cell.setCellStyle(style);
            }
        }

        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_Import_ChiTiet_TTHQ.xlsx");
        //Huy-end
        return path;
    }
    // Huy-end

    // Huypq-29062021-start
    private CellValue getValueCellFormula(XSSFWorkbook workbook, XSSFFormulaEvaluator evaluator, XSSFSheet sheet,
                                          String columnCell, String typeData) {
        System.out.println(columnCell);
        CellReference cellReference = new CellReference(columnCell);
        XSSFRow row = sheet.getRow(cellReference.getRow());
        XSSFCell cell = row.getCell(cellReference.getCol());
        if (typeData.equals("string")) {
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        } else if (typeData.equals("number")) {
            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
        }
        CellValue cellValue = evaluator.evaluate(cell);
        return cellValue;
    }

    private String validateCellString(CellValue cellValue) {
        if (cellValue != null) {
            return cellValue.getStringValue();
        }
        return "";
    }

    private Double validateCellDouble(CellValue cellValue) {
        if (cellValue != null) {
            return cellValue.getNumberValue();
        }
        return 0d;
    }

    private Long validateCellLong(CellValue cellValue) {
        if (cellValue != null) {
            return (long) cellValue.getNumberValue();
        }
        return 0l;
    }

    public List<EffectiveCalculationDetailsDTO> importFileTthqBk(String fileInput, Long woId, HttpServletRequest request)
            throws Exception {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        List<EffectiveCalculationDetailsDTO> workLst = Lists.newArrayList();
        List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
        EffectiveCalculationDetailsDTO obj = new EffectiveCalculationDetailsDTO();
        try {
            File f = new File(fileInput);
            XSSFWorkbook workbook = new XSSFWorkbook(f);

            // XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            XSSFFormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            // evaluator.evaluateAllFormulaCells(workbook);

            XSSFSheet sheet0 = workbook.getSheetAt(workbook.getSheetIndex("Template tram"));
            XSSFSheet sheet1 = workbook.getSheetAt(workbook.getSheetIndex("Chào giá"));
            XSSFSheet sheet2 = workbook.getSheetAt(workbook.getSheetIndex("Đầu vào"));
            XSSFSheet sheet3 = workbook.getSheetAt(workbook.getSheetIndex("Hiệu quả"));

            obj.setAreaCode(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "D13", "string")));
            obj.setProvinceCode(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "C13", "string")));
            obj.setStationCodeVtn(
                    validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "H16", "string")));
            obj.setStationCodeVcc(
                    validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "I16", "string")));
            obj.setAddress(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "H17", "string")));
            obj.setStationType(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "Q20", "string")));
            obj.setMaiDat(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "Q16", "string")));
            obj.setDoCaoCot(validateCellDouble(getValueCellFormula(workbook, evaluator, sheet0, "H13", "number")));
            obj.setLoaiCot(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "D30", "string")));
            obj.setMongCo(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "J13", "string")));
            obj.setLoaiNha(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "D33", "string")));
            obj.setTiepDia(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "D34", "string")));
            obj.setDienCnkt(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "H14", "string")));
            obj.setSoCotDien(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "L14", "string")));
            obj.setVanChuyenBo(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "N14", "string")));
            obj.setThueAcquy(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "E22", "string")));
            obj.setGiaThueMbThucTe(validateCellLong(getValueCellFormula(workbook, evaluator, sheet0, "I84", "number")));
            obj.setGiaThueMbTheoDinhMuc(
                    validateCellLong(getValueCellFormula(workbook, evaluator, sheet0, "I85", "number")));
            obj.setCapexCot(validateCellLong(getValueCellFormula(workbook, evaluator, sheet2, "L30", "number")));
            obj.setCapexTiepDia(validateCellLong(getValueCellFormula(workbook, evaluator, sheet2, "L31", "number")));
            obj.setCapexAc(validateCellLong(getValueCellFormula(workbook, evaluator, sheet2, "L32", "number")));
            obj.setCapexPhongMay(validateCellLong(getValueCellFormula(workbook, evaluator, sheet2, "L33", "number")));
            obj.setTongCapexHt(validateCellLong(getValueCellFormula(workbook, evaluator, sheet1, "I14", "number")));
            obj.setVccChaoGiaHt(validateCellDouble(getValueCellFormula(workbook, evaluator, sheet1, "J29", "number")));
            obj.setVccChaoGiaAcquy(validateCellDouble(getValueCellFormula(workbook, evaluator, sheet1, "J30", "number")));
            obj.setTongCong(validateCellLong(getValueCellFormula(workbook, evaluator, sheet1, "J29", "number"))
                    + validateCellLong(getValueCellFormula(workbook, evaluator, sheet1, "J30", "number")));
            obj.setDescription(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "D42", "string")));
            obj.setNgayGui(new Date());
            if (obj.getTongCapexHt() != null && obj.getTongCapexHt() != 0) {
                obj.setNgayHoanThanh(new Date());
            }
            obj.setNguoiLap(user.getEmail().replace("@viettel.com.vn", ""));
            obj.setCapexHtVcc(validateCellLong(getValueCellFormula(workbook, evaluator, sheet3, "I3", "number")));
            obj.setNpv(validateCellLong(getValueCellFormula(workbook, evaluator, sheet3, "D72", "number")));
            obj.setIrr(validateCellDouble(getValueCellFormula(workbook, evaluator, sheet3, "D73", "number")));
            obj.setThoiGianHv(validateCellDouble(getValueCellFormula(workbook, evaluator, sheet3, "D74", "number")));
            obj.setLnstDt(validateCellDouble(getValueCellFormula(workbook, evaluator, sheet3, "C40", "number"))
                    / validateCellDouble(getValueCellFormula(workbook, evaluator, sheet3, "C27", "number")));
            obj.setConclude(validateCellString(getValueCellFormula(workbook, evaluator, sheet0, "C92", "string")));

            obj.setWoId(woId);
            workLst.add(obj);

            if (errorList.size() > 0) {
                String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
                List<EffectiveCalculationDetailsDTO> emptyArray = Lists.newArrayList();
                workLst = emptyArray;
                EffectiveCalculationDetailsDTO errorContainer = new EffectiveCalculationDetailsDTO();
                errorContainer.setErrorList(errorList);
                errorContainer.setMessageColumn(10); // cột dùng để in ra lỗi
                errorContainer.setFilePathError(filePathError);
                workLst.add(errorContainer);
            }

            workbook.close();
            return workLst;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ExcelErrorDTO errorDTO = createError(0, 0, e.toString());
            errorList.add(errorDTO);
            String filePathError = null;
            try {
                filePathError = UEncrypt.encryptFileUploadPath(fileInput);
            } catch (Exception ex) {
                LOGGER.error(e.getMessage(), e);
                errorDTO = createError(0, 0, ex.toString());
                errorList.add(errorDTO);
            }
            List<EffectiveCalculationDetailsDTO> emptyArray = Lists.newArrayList();
            workLst = emptyArray;
            EffectiveCalculationDetailsDTO errorContainer = new EffectiveCalculationDetailsDTO();
            errorContainer.setErrorList(errorList);
            errorContainer.setMessageColumn(10); // cột dùng để in ra lỗi
            errorContainer.setFilePathError(filePathError);
            workLst.add(errorContainer);
            return workLst;
        }
    }

    public boolean cdAcceptWoTTHQ(WoDTO woDto) {
        // KttsUserSession user = (KttsUserSession)
        // request.getSession().getAttribute("kttsUserSession");
        Gson gson = new Gson();
        long woId = woDto.getWoId();
        String loggedInUser = woDto.getLoggedInUser();

        WoBO bo = woDAO.getOneRaw(woId);
        bo.setState(woDto.getState());
        bo.setFtEmail(woDto.getFtEmail());
        bo.setFtId(woDto.getFtId());
        bo.setFtName(woDto.getFtName());
        bo.setUpdateCdLevel2ReceiveWo(new Date());
        bo.setUserCdLevel2ReceiveWo(loggedInUser);
        bo.setUpdateFtReceiveWo(new Date());
        bo.setUserFtReceiveWo(loggedInUser);
        bo.setStartTime(new Date());
        bo.setAcceptTime(new Date());

        try {
            woDAO.update(bo);
            woDto = bo.toDTO();
            String content = woDAO.getNameAppParam(PROCESSING, WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(bo), loggedInUser);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    // Huy-end

    @Transactional
    @Override
    public void updateChecklistDbht(WoDTORequest request) throws Exception {
        Long woId = request.getWoId();

        // Update wo_mapping_checklist
        WoMappingChecklistDTO woMappingChecklistDTO = request.getAcceptChecklistObj();
        Long checklistId = woMappingChecklistDTO.getChecklistId();
        List<ImgChecklistDTO> lstImgs = woMappingChecklistDTO.getLstImgs();

        // Delete WO_MAPPING_ATTACH
        woDAO.removeAttachOfChecklist(woId, checklistId);

        // Update WO_MAPPING_CHECKLIST
        WoMappingChecklistBO incomingBO = woMappingChecklistDTO.toModel();
        if (StringUtils.isNullOrEmpty(incomingBO.getName())) {
            incomingBO.setName(woMappingChecklistDTO.getChecklistName());
        }
        woMappingChecklistDAO.updateObject(incomingBO);

        // Insert image
        WoDTO woDTO = woDAO.getOneDetails(woId);
        insertWoMappingAttach(woDTO, checklistId, lstImgs);
    }

    @Override
    public Map<String, List<WoChartDataDto>> getWoDataForChart(WoChartDataDto obj) throws Exception {
        Map<String, List<WoChartDataDto>> listData = new HashMap<>();
        for (String woType : obj.getWoTypes()) {
            List<WoChartDataDto> woChartDataDtos = woDAO.getChartDataByWoType(woType, obj);
            listData.put(woType, woChartDataDtos);
        }
        return listData;
    }

    public void createNewTmbtWO(WoTrDTO dto) {
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

                    WoDTO woDto = woBO.toDTO();
                    woDto.setIsCreateNew(true);
                    logWoWorkLogs(woDto, "1", "Tự động sinh WO TMBT khi TTHT tiếp nhận TR.", gson.toJson(woBO),
                            dto.getLoggedInUser());

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewDbhtWO(WoTrDTO trDTO) {
        Gson gson = new Gson();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
            // Get AP_WORK_SRC
            List<WoAppParamDTO> lstApWorkSrcs = woDAO.getAppParam("AP_WORK_SRC");
            Long apWorkSrc = null;
            for (WoAppParamDTO iAp : lstApWorkSrcs) {
                if (iAp.getName().contains("HTCT")) {
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
                Long woTypeId = iWo.getParOrder() == 7l ? woTypeDAO.getIdByCode("PHATSONG")
                        : woTypeDAO.getIdByCode("THICONG");
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
                woBO.setCdLevel2(trDTO.getCdLevel2());
                woBO.setCdLevel2Name(trDTO.getCdLevel2Name());
                woBO.setTrId(trDTO.getTrId());
                woBO.setConstructionId(trDTO.getConstructionId());
                woBO.setConstructionCode(trDTO.getConstructionCode());
                if (trDTO.getContractId() != null) {
                    woBO.setContractId(trDTO.getContractId());
                    woBO.setContractCode(trDTO.getContractCode());
                } else {
                    woBO.setContractId(trDTO.getProjectId());
                    woBO.setContractCode(trDTO.getProjectCode());
                }

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
                woBO.setStatus(0l);
                woBO.setWoOrder("" + iWo.getParOrder());

                // Nếu wo 2,3,4,5 thì tự động lấy giá trị sản lượng trong bảng
                // CAT_STATION_WO_CONFIG_VALUE
                if (iWo.getParOrder() >= 2 && iWo.getParOrder() <= 5) {
                    WoSimpleStationDTO station = woDAO.getStationByCode(trDTO.getStationCode());
                    Double moneyValue = woDAO.getMoneyValueOfDhbtWo(station.getStationId(),
                            woBO.getCatWorkItemTypeId());
                    woBO.setMoneyValue(moneyValue == null ? 0.0 : moneyValue);
                }

                woDAO.saveObject(woBO);

                WoDTO woDTO = woBO.toDTO();
                logWoWorkLogsNotSendSms(woDTO, "1", "Tự động sinh khi TTHT tiếp nhận TR.", gson.toJson(woBO),
                        trDTO.getLoggedInUser());

                // Create checklist
                tryCreateChecklistForNewWo(woBO.toDTO());

                // Add work item for construction
                if (woDTO.getCatWorkItemTypeId() != null && woDTO.getConstructionId() != null) {
                    if (!woDAO.checkExistConstructionWorkItem(woDTO.getConstructionId(),
                            woDTO.getCatWorkItemTypeId())) {
                        woDTO.setSysUserId(-1l);
                        woDTO.setSysGroupId(-1l);
                        WoCatWorkItemTypeDTO catWI = woDAO.getCatWorkTypeById(woDTO.getCatWorkItemTypeId());
                        woDTO.setCatWorkItemTypeCode(catWI.getCatWorkItemTypeCode());
                        woDTO.setCatWorkItemTypeName(catWI.getName());
                        woDAO.insertWorkItem(woDTO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void approvedWoOkTthq(WoDTO dto) {
//		Session session = effectiveCalculationDetailsDAO.getSessionFactory().openSession();
        Gson gson = new Gson();
        try {
            WoBO woBO = woDAO.getOneRaw(dto.getWoId());

            woBO.setState(OK); //Huypq-29112021-edit

            woBO.setUserTthtApproveWo(dto.getLoggedInUser());
            woBO.setUpdateTthtApproveWo(new Date());
            woDAO.update(woBO);

//			EffectiveCalculationDetailsDTO obj = dto.getEffectiveDTO();
//			obj.setStatus("1");
//			obj.setWoCode(woBO.getWoCode());
//			effectiveCalculationDetailsDAO.saveObject(obj.toModel());
//			effectiveCalculationDetailsDAO.updateTthqResultWoMappingChecklist(session, dto.getWoId(),"HIỆU QUẢ");

//			List<WoMappingChecklistDTO> lstChecklists = woMappingChecklistDAO.getMappingCheclistByWoId(dto.getWoId());
//			boolean isHq = true;
//			for (WoMappingChecklistDTO iChecklist : lstChecklists) {
//				if (StringUtils.isNullOrEmpty(iChecklist.getTthqResult())
//						|| "Không hiệu quả".equalsIgnoreCase(iChecklist.getTthqResult())) {
//					isHq = false;
//					break;
//				}
//			}

            if ("TTHQ".equalsIgnoreCase(dto.getWoTypeCode())) {
                WoDTO woTthq = woBO.toDTO();
                woTthq.setUserCreated(dto.getLoggedInUser());
                woTthq.setIsCreateNew(true);
                tryCreateTkdt(woTthq);
            }

            // Write log
            WoDTO woDTO = woBO.toDTO();
            String logContent = "Trạng thái WO: " + woDAO.getNameAppParam(woBO.getState(), WO_XL_STATE);
            logWoWorkLogs(woDTO, "1", logContent, gson.toJson(woDTO), dto.getLoggedInUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewDoanhThuWO(WoDTO dto, Double completeValue) {
        Gson gson = new Gson();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        try {
            Long trId = dto.getTrId();
            WoTrDTO trDTO = trDAO.getOneDetails(trId);

            WoBO woBO = new WoBO();

            // Wo code
            String code = "VNM_PMXL_1_";
            Long woTypeId = woTypeDAO.getIdByCode("DOANHTHU_DTHT");
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
            woBO.setUserCreated(dto.getUserCreated());
            woBO.setCdLevel1("166677");
            woBO.setCdLevel1Name("Trung tâm Đầu tư hạ tầng ");
            woBO.setCdLevel2(dto.getCdLevel2());
            woBO.setCdLevel2Name(dto.getCdLevel2Name());
            woBO.setTrId(trDTO.getTrId());
            woBO.setConstructionId(dto.getConstructionId());
            woBO.setConstructionCode(dto.getConstructionCode());
            woBO.setContractId(dto.getContractId());
            woBO.setContractCode(dto.getContractCode());
            woBO.setProjectId(dto.getProjectId());
            woBO.setProjectCode(dto.getProjectCode());
            woBO.setMoneyValue(completeValue);
            woBO.setCreatedDate(today);
            woBO.setWoTypeId(woTypeId);
            woBO.setQoutaTime(trDTO.getQoutaTime());
            // Finish date
            Date finishDate = new Date(today.getTime() + (long) 60 * 24 * 60 * 60 * 1000);
            woBO.setFinishDate(finishDate);
            // Kế hoạch tháng
            LocalDate localDate = finishDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long month = localDate.getMonthValue();
            woBO.setTotalMonthPlanId(month);

            woBO.setCatStationHouseId(dto.getCatStationHouseId());
            woBO.setStationCode(dto.getStationCode());
            woBO.setQoutaTime(60 * 24);
            woBO.setState(ASSIGN_CD);
            woBO.setStatus(1l);
            woDAO.saveObject(woBO);

            WoDTO woDto = woBO.toDTO();
            woDto.setIsCreateNew(true);
            // Worklog tu sinh wo
            logWoWorkLogs(woDto, "1", "Tự động sinh WO Doanh thu khi đồng hộ hạ tầng.", gson.toJson(woBO),
                    dto.getLoggedInUser());

            // Create checklist
            WoMappingChecklistBO woMappingChecklistBO = new WoMappingChecklistBO();
            woMappingChecklistBO.setWoId(woBO.getWoId());
            woMappingChecklistBO.setCheckListId(1l);
            woMappingChecklistBO.setState("NEW");
            woMappingChecklistBO.setStatus(1l);
            woMappingChecklistBO.setName(lstNames.get(0).getName());
            woMappingCheckListDAO.saveObject(woMappingChecklistBO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // HienLT56 start 01062021
    public void checkWOThiCongItem(Long woId) {
        // Long id = woDAO.checkWOThiCongItem(woId);
        // if(id != null && id != 0l) {
        woDAO.updateStatusWI(woId);
        // }
    }

    public void checkWOCttGpon(Long woId) {
        // List<Long> lstId = woDAO.checkWOCttGpon(woId);
        // if(lstId.size() > 0) {
        // for(Long id : lstId) {
        woDAO.updateStatusCttGpon(woId);
        // }
        // }
    }

    public void checkWOThdt(Long woId) {
        // List<Long> lstId = woDAO.checkWOThdt(woId);
        // if(lstId.size() > 0) {
        // for(Long id : lstId) {
        woDAO.updateStatusThdt(woId);
        // }
        // }
    }

    public void checkWOMcl(Long woId) {
        // List<Long> lstId = woDAO.checkWOMcl(woId);
        // if(lstId.size() > 0) {
        // for(Long id : lstId) {
        woDAO.updateStatusWoMapCl(woId);
        // }
        // }
    }

    public void checkWoXdddCl(Long woId) {
        // List<Long> lstId = woDAO.checkWoXdddCl(woId);
        // if(lstId.size() > 0 ){
        // for(Long id : lstId) {
        woDAO.updateStatusWoXdddCl(woId);
        // }
        // }
    }

    public Long saveChangeForTTHT(WoDTO woDto) {
        return woDAO.saveChangeForTTHT(woDto);
    }

    // HienLT56 start 03062021
    @Override
    public void logWoWorkLogEditTTHT(WoDTO dto, String logType, String content, String contentDetail,
                                     String loggedInUser) {
        WoWorkLogsBO workLogs = new WoWorkLogsBO();
        workLogs.setWoId(dto.getWoId());
        workLogs.setContent(content);
        workLogs.setContentDetail(contentDetail);
        workLogs.setLogTime(new Date());
        workLogs.setLogType(logType);
        workLogs.setStatus(1);

        String loggedInUserStr = "Hệ thống";
        if (!StringUtils.isNullOrEmpty(loggedInUser)) {
            WoSimpleSysUserDTO sysUser = trDAO.getSysUser(loggedInUser);
            WoSimpleSysGroupDTO sysGroup = trDAO.getSysUserGroup(loggedInUser);
            loggedInUserStr = sysUser.getFullName() + " - " + sysUser.getEmployeeCode() + " - ";
            if (sysGroup.getGroupLevel() == 1)
                loggedInUserStr += sysGroup.getGroupNameLevel1();
            if (sysGroup.getGroupLevel() == 2)
                loggedInUserStr += sysGroup.getGroupNameLevel2();
            if (sysGroup.getGroupLevel() == 3)
                loggedInUserStr += sysGroup.getGroupNameLevel3();
        }
        workLogs.setUserCreated(loggedInUserStr);

        if ("UCTT".equalsIgnoreCase(dto.getWoTypeCode()) && !StringUtils.isNullOrEmpty(dto.getCustomField())) {
            WoWorkLogsBO workLogsUctt = workLogs;
            workLogsUctt.setContent(dto.getCustomField());
            woWorkLogsDAO.saveObject(workLogsUctt);
        }

        // Write worklogs
        woWorkLogsDAO.saveObject(workLogs);
    }
    // HienLT56 end 03062021

    @Transactional
    @Override
    public WoAvgResponseDTO createWoAvg(WoAvgVhktInputDTO woAvgInputDTO) {
        Gson gson = new Gson();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        WoBO woBO = new WoBO();
        WoAvgResponseDTO woAvgResponseDTO = new WoAvgResponseDTO();
        woAvgResponseDTO = validateAvgData(woAvgInputDTO);
        if (woAvgResponseDTO.getStatus() == 1) {
            return woAvgResponseDTO;
        }

        if (!isCheckSumOK(woAvgInputDTO)) {
            woAvgResponseDTO.setStatus(1);
            woAvgResponseDTO.setErrorCode("503");
            woAvgResponseDTO.setErrorDesc("Checksum value not match");
            return woAvgResponseDTO;
        }
        Long woId = woAvgInputDTO.getWoId();
        // find existing
        WoAvgMappingEntityBO woAvgMappingEntityBO = woAvgMappingDAO.findByOrderId(woAvgInputDTO.getOrderCodeTgdd(),
                woAvgInputDTO.getOrderCodeAvg());
        try {
            if (woId == null && woAvgMappingEntityBO == null) {
                try {
                    // Wo code
                    String code = "VNM_PMXL_1_";
                    Long woTypeId = woTypeDAO.getIdByCode("AVG");
                    Long nextSq = woDAO.getNextSeqVal();
                    Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
                    code += woTypeId + "_" + countIdType + "_" + nextSq;
                    woBO.setWoCode(code);
                    WoNameDTO dtoName = new WoNameDTO();
                    dtoName.setWoTypeId(woTypeId);
                    List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
                    if (lstNames != null && lstNames.size() > 0) {
                        woBO.setWoName(lstNames.get(0).getName());
                    } else {
                        woBO.setWoName("WO AVG");

                    }
                    woBO.setUserCreated("AVG");
                    woBO.setCdLevel1("270120");
                    woBO.setCdLevel1Name("Trung tâm vận hành khai thác");
                    woBO.setMoneyValue(woAvgInputDTO.getMoneyValue());
                    woBO.setCreatedDate(today);
                    woBO.setWoTypeId(woTypeId);
                    woBO.setFinishDate(new Date(today.getTime() + 2 * 24 * 60 * 60 * 1000));
                    // Kế hoạch tháng
                    LocalDate localDate = woAvgInputDTO.getFinishDate().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    long month = localDate.getMonthValue();
                    woBO.setTotalMonthPlanId(month);

                    woBO.setQoutaTime(60 * 24);
                    woBO.setState(ASSIGN_CD);
                    woBO.setStatus(1l);
                    woId = woDAO.saveObject(woBO);

                    // Worklog tu sinh wo
                    logWoWorkLogs(woBO.toDTO(), "1", "WO sinh từ AVG.", gson.toJson(woBO), null);

                    // Create checklist
                    List<WoChecklistBO> avgChecklist = createChecklistByParType(woId, "AVG_CHECKLIST");
                    woChecklistDAO.saveListNoId(avgChecklist);
                } catch (Exception ex) {
                    woAvgResponseDTO.setErrorCode("203");
                    woAvgResponseDTO.setStatus(1);
                    woAvgResponseDTO.setErrorDesc("Fail to create AVG work order");
                    LOGGER.error("Fail to create AVG WO:" + ex.getMessage(), ex);
                    return woAvgResponseDTO;
                }
                // create new AVG record in WO_MAPPING_AVG
                try {
                    woAvgMappingEntityBO = new WoAvgMappingEntityBO();
                    woAvgMappingEntityBO.setWoId(woId);
                    woAvgMappingEntityBO.setOrderCodeAvg(woAvgInputDTO.getOrderCodeAvg());
                    woAvgMappingEntityBO.setOrderCodeTgdd(woAvgInputDTO.getOrderCodeTgdd());
                    // woAvgMappingEntityBO.setChipNumber(woAvgInputDTO.getChipNumber());
                    woAvgMappingEntityBO.setAddress(woAvgInputDTO.getAddress());
                    woAvgMappingEntityBO.setPersonalId(woAvgInputDTO.getPersonalId());
                    woAvgMappingEntityBO.setCustomerName(woAvgInputDTO.getCustomerName());
                    woAvgMappingEntityBO.setPhoneNumber(woAvgInputDTO.getPhoneNumber());
                    woAvgMappingEntityBO.setServicePackage(woAvgInputDTO.getServicePackage());
                    woAvgMappingEntityBO.setProductCode(woAvgInputDTO.getProductCode());
                    woAvgMappingEntityBO.setPaymentStatus(woAvgInputDTO.getPaymentStatus());
                    // woAvgMappingEntityBO.setCardNumber(woAvgInputDTO.getCardNumber());
                    woAvgMappingDAO.saveObject(woAvgMappingEntityBO);
                } catch (Exception ex) {
                    woAvgResponseDTO.setErrorCode("501");
                    woAvgResponseDTO.setStatus(1);
                    woAvgResponseDTO.setErrorDesc("Fail to create AVG information");
                    LOGGER.error("Fail to create AVG information" + ex.getMessage(), ex);
                    return woAvgResponseDTO;
                }

            } else {

                if (woId == null && woAvgMappingEntityBO != null) {
                    woAvgResponseDTO.setErrorCode("502");
                    woAvgResponseDTO.setStatus(500);
                    woAvgResponseDTO.setErrorDesc("Work order for provided AVG information already created!");
                    LOGGER.error("WO for this AVG information already created");
                    return woAvgResponseDTO;
                } else {
                    woAvgMappingEntityBO = woAvgMappingDAO.getWoAvgInformation(woId);
                    try {
                        // update existing
                        // if (woAvgInputDTO.getChipNumber() != null) {
                        // woAvgMappingEntityBO.setChipNumber(woAvgInputDTO.getChipNumber());
                        // }

                        if (woAvgInputDTO.getAddress() != null) {
                            woAvgMappingEntityBO.setAddress(woAvgInputDTO.getAddress());
                        }
                        if (woAvgInputDTO.getPersonalId() != null) {
                            woAvgMappingEntityBO.setPersonalId(woAvgInputDTO.getPersonalId());
                        }
                        if (woAvgInputDTO.getCustomerName() != null) {
                            woAvgMappingEntityBO.setCustomerName(woAvgInputDTO.getCustomerName());
                        }
                        if (woAvgInputDTO.getPhoneNumber() != null) {
                            woAvgMappingEntityBO.setPhoneNumber(woAvgInputDTO.getPhoneNumber());
                        }
                        if (woAvgInputDTO.getServicePackage() != null) {
                            woAvgMappingEntityBO.setServicePackage(woAvgInputDTO.getServicePackage());
                        }
                        if (woAvgInputDTO.getProductCode() != null) {
                            woAvgMappingEntityBO.setProductCode(woAvgInputDTO.getProductCode());
                        }
                        if (woAvgInputDTO.getPaymentStatus() != null) {
                            woAvgMappingEntityBO.setPaymentStatus(woAvgInputDTO.getPaymentStatus());
                        }
                        // if (woAvgInputDTO.getCardNumber() != null) {
                        // woAvgMappingEntityBO.setCardNumber(woAvgInputDTO.getCardNumber());
                        // }
                        woAvgMappingDAO.update(woAvgMappingEntityBO);
                    } catch (Exception ex) {
                        woAvgResponseDTO.setErrorCode("501");
                        woAvgResponseDTO.setStatus(1);
                        woAvgResponseDTO.setErrorDesc("Fail to update AVG information");
                        LOGGER.error("Fail to update AVG information" + ex.getMessage(), ex);
                        return woAvgResponseDTO;
                    }
                }

            }
        } catch (Exception ex) {
            woAvgResponseDTO.setErrorCode("500");
            woAvgResponseDTO.setStatus(1);
            woAvgResponseDTO.setErrorDesc("Fail to create or update AVG wo");
            LOGGER.error("Fail to create or update AVG WO:" + ex.getMessage(), ex);
            return woAvgResponseDTO;
        }
        Map<String, Object> data = new HashedMap();
        data.put("woId", woId);
        woAvgResponseDTO.setData(data);
        woAvgResponseDTO.setStatus(0);
        return woAvgResponseDTO;
    }

    private WoAvgResponseDTO validateAvgData(WoAvgVhktInputDTO woAvgInputDTO) {
        WoAvgResponseDTO avgResponseDTO = new WoAvgResponseDTO();
        avgResponseDTO.setStatus(1);
        avgResponseDTO.setErrorCode("401");

        if (woAvgInputDTO.getOrderCodeAvg() == null && woAvgInputDTO.getOrderCodeTgdd() == null) {
            avgResponseDTO.setErrorDesc("Invalid data for TGDD/AVG order code ");
            return avgResponseDTO;
        }

        if (isEmpty(woAvgInputDTO.getCustomerName())) {
            avgResponseDTO.setErrorDesc("Invalid data for customer name order code ");
            return avgResponseDTO;
        }
        // if(isEmpty(woAvgInputDTO.getCardNumber())){
        // avgResponseDTO.setErrorDesc("Invalid data for card number");
        // return avgResponseDTO;
        // }
        // if(isEmpty(woAvgInputDTO.getChipNumber())){
        // avgResponseDTO.setErrorDesc("Invalid data for chip number");
        // return avgResponseDTO;
        // }
        if (isEmpty(woAvgInputDTO.getPhoneNumber())) {
            avgResponseDTO.setErrorDesc("Invalid data for phone number");
            return avgResponseDTO;
        }

        // if(isEmpty(woAvgInputDTO.getServicePackage())){
        // avgResponseDTO.setErrorDesc("Invalid data for service package");
        // return avgResponseDTO;
        // }
        if (isEmpty(woAvgInputDTO.getCheckSum())) {
            avgResponseDTO.setErrorDesc("Invalid data for checksum");
            return avgResponseDTO;
        }
        if (isEmpty(woAvgInputDTO.getAddress())) {
            avgResponseDTO.setErrorDesc("Invalid data for customer address");
            return avgResponseDTO;
        }
        if (isEmpty(woAvgInputDTO.getPersonalId())) {
            avgResponseDTO.setErrorDesc("Invalid data for customer personal id");
            return avgResponseDTO;
        }
        avgResponseDTO.setStatus(0);
        avgResponseDTO.setErrorCode(null);
        return avgResponseDTO;
    }

    private boolean isEmpty(String str) {
        if (str == null || str.trim().equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public List<GetWoAvgOutputDto> getWoAvg(WoAvgVhktInputDTO request) {
        return woAvgMappingDAO.doSearch(request);
    }

    @Override
    public AvgResponse getAvgToken() {

        AvgGetTokenInputDTO avgInput = new AvgGetTokenInputDTO();
        avgInput.setUsername(avgUsername);
        avgInput.setPassword(avgPassword);
        avgInput.setOrganisation("AVG");
        String responseBody = null;
        try {
            responseBody = callOutsideApi(avgInput, avgGetTokenUrl);
        } catch (Exception ex) {
            LOGGER.error("Get AVG token error" + ex.getMessage(), ex);
            return null;
        }
        Gson gson = new Gson();
        try {
            AvgResponse response = gson.fromJson(responseBody, AvgResponse.class);
            if (response.getData() != null && response.getData().getToken() != null) {
                this.setAvgTokenStr(response.getData().getToken());
            } else {
                LOGGER.error("Could not get AVG token " + responseBody);
            }
            return response;
        } catch (Exception ex) {
            LOGGER.error("Could not get AVG token " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public AvgResponse activeAvgDevice(WoChecklistDTO checklistDTO) {
        try {
            WoAvgMappingEntityBO woAvgDTO = woAvgMappingDAO.getWoAvgInformation(checklistDTO.getWoId());
            if (woAvgDTO != null) {
                if (getAvgTokenStr() == null) {
                    getAvgToken();
                }
                AvgUpdateActiveDeviceRequestInput updateStatusRequest = new AvgUpdateActiveDeviceRequestInput();
                updateStatusRequest.setToken(getAvgTokenStr());
                updateStatusRequest.setAddress_detail(woAvgDTO.getAddress());
                String[] customerName = extractCustomerName(woAvgDTO.getCustomerName());
                updateStatusRequest.setLast_name(customerName[0]);
                updateStatusRequest.setMiddle_name(customerName[1]);
                updateStatusRequest.setFirst_name(customerName[2]);
                updateStatusRequest.setId_card_type("IDENTITY_CARD");
                updateStatusRequest.setId_card_number(woAvgDTO.getPersonalId());
                updateStatusRequest.setPackage_id(woAvgDTO.getServicePackage());
                updateStatusRequest.setPartner("VT_VCC");
                String refNumber = (woAvgDTO.getOrderCodeTgdd() != null
                        && !"".equals(woAvgDTO.getOrderCodeTgdd().trim())) ? woAvgDTO.getOrderCodeTgdd()
                        : woAvgDTO.getOrderCodeAvg();
                updateStatusRequest.setReference_number(refNumber);
                updateStatusRequest.setPhone_number(woAvgDTO.getPhoneNumber());
                updateStatusRequest.setSettopbox(checklistDTO.getSettopBox());
                updateStatusRequest.setSmartcard(checklistDTO.getSmartCard());
                updateStatusRequest.setWard_code("");// todo have no data for this
                // send to avg update status api
                String response = callOutsideApi(updateStatusRequest, avgActiveDeviceUrl);
                LOGGER.info("Active Avg device response: " + response);
                Gson gson = new Gson();
                AvgResponse avgResponseStatus = gson.fromJson(response, AvgResponse.class);
                if (avgResponseStatus.getStatus() != null && !"OK".equals(avgResponseStatus.getStatus().getCode())) {
                    if (AVG_ERR_CODE_TOKEN_EXPIRE.equalsIgnoreCase(avgResponseStatus.getStatus().getCode())) {
                        // get token again and recall api
                        getAvgToken();
                        if (this.avgTokenStr == null) {
                        } else {
                            updateStatusRequest.setToken(this.avgTokenStr);
                        }
                        response = callOutsideApi(updateStatusRequest, avgUpdateStatusUrl);
                        avgResponseStatus = gson.fromJson(response, AvgResponse.class);

                    }
                    if (!"OK".equals(avgResponseStatus.getStatus().getCode())) {
                        LOGGER.error("Update Avg order status response failed : " + response);
                    }
                }

                return avgResponseStatus;
            }
        } catch (Exception ex) {
            LOGGER.error("updateWoAvgStatus error" + ex.getMessage(), ex);
        }
        return null;
    }

    // split name to first,middle,last name
    private String[] extractCustomerName(String customerName) {
        String[] names = new String[3];
        if (customerName != null) {
            String[] extractNames = customerName.split(" ");
            if (extractNames.length > 2) {
                // last name
                names[0] = extractNames[0];
                // fisrt name
                names[2] = extractNames[extractNames.length - 1];
                extractNames[0] = "";
                extractNames[extractNames.length - 1] = "";
                // middle name
                names[1] = String.join(" ", extractNames);
            } else if (extractNames.length == 2) {
                names[0] = extractNames[0];
                names[1] = "";
                names[2] = extractNames[1];
            } else {
                names[0] = extractNames[0];
                names[1] = "";
                names[2] = "";
            }
        }
        return names;
    }

    @Transactional
    @Override
    public boolean updateChecklistAvg(WoDTORequest woDTORequest, WoChecklistDTO woChecklistDTO) {
        try {
            Long checklistId = woChecklistDTO.getChecklistId();
            WoChecklistBO woChecklistBO = woChecklistDAO.getOneRaw(checklistId);

            // Call to AVG
            String cancelledNote = "";
            // send update status to avg
            String orderStatus = "";
            if ("1".equals(woChecklistBO.getCode().trim())) {
                if ("Done".equalsIgnoreCase(woChecklistDTO.getState())) {
                    if (woChecklistDTO.getRejectReason() != null
                            && !"".equals(woChecklistDTO.getRejectReason().trim())) {
                        orderStatus = "CANCELLED";
                        cancelledNote = woChecklistDTO.getRejectReason();
                    } else {
                        orderStatus = "CONFIRMED";
                    }
                }
            }
            if ("2".equals(woChecklistBO.getCode().trim())) {
                if (woChecklistDTO.getRejectReason() != null && !"".equals(woChecklistDTO.getRejectReason().trim())) {
                    orderStatus = "CANCELLED";
                    cancelledNote = woChecklistDTO.getRejectReason();
                } else {
                    orderStatus = "DELIVERING";
                }
            }

            if ("3".equals(woChecklistBO.getCode().trim())) {
                if (woChecklistDTO.getRejectReason() != null && !"".equals(woChecklistDTO.getRejectReason().trim())) {
                    orderStatus = "CANCELLED";
                    cancelledNote = woChecklistDTO.getRejectReason();
                } else {
                    orderStatus = "COMPLETED";
                    // AvgResponse response = activeAvgDevice(woChecklistDTO);
                    // if (response == null || !"OK".equals(response.getStatus().getCode())) {
                    // return false;
                    // }
                }

            }
            // WoAvgMappingEntityBO entityBO =
            // woAvgMappingDAO.getWoAvgInformation(woChecklistDTO.getWoId());
            // if (orderStatus != null) {
            // if (getAvgTokenStr() == null) {
            // getAvgToken();
            // }
            // if (this.avgTokenStr == null) {
            // return false;
            // }
            // AvgUpdateStatusInput avgUpdateStatusInput = new AvgUpdateStatusInput();
            // avgUpdateStatusInput.setToken(this.avgTokenStr);
            // avgUpdateStatusInput.setLife_cycle_state(orderStatus);
            // String refNumber = (entityBO.getOrderCodeTgdd() != null &&
            // !"".equals(entityBO.getOrderCodeTgdd().trim())) ? entityBO.getOrderCodeTgdd()
            // : entityBO.getOrderCodeAvg();
            // avgUpdateStatusInput.setReference_number(refNumber);
            // avgUpdateStatusInput.setNote(cancelledNote);
            // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // String installtionDate =
            // formatter.format(woChecklistBO.getCustomerConfirmDate());
            // avgUpdateStatusInput.setInstallation_date(installtionDate);
            // String response = callOutsideApi(avgUpdateStatusInput, avgUpdateStatusUrl);
            // if (response == null) {
            // LOGGER.error("Update Avg order status response failed ");
            // return false;
            // }
            // LOGGER.info("Response from avg update:" + response);
            // Gson gson = new Gson();
            // AvgResponse avgResponseStatus = gson.fromJson(response, AvgResponse.class);
            // if (avgResponseStatus.getStatus() != null &&
            // !"OK".equals(avgResponseStatus.getStatus().getCode())) {
            // if
            // (AVG_ERR_CODE_TOKEN_EXPIRE.equalsIgnoreCase(avgResponseStatus.getStatus().getCode()))
            // {
            // //get token again and recall api
            // getAvgToken();
            // if (this.avgTokenStr == null) {
            // return false;
            // } else {
            // avgUpdateStatusInput.setToken(this.avgTokenStr);
            // }
            // response = callOutsideApi(avgUpdateStatusInput, avgUpdateStatusUrl);
            // avgResponseStatus = gson.fromJson(response, AvgResponse.class);
            //
            // }
            // if (!"OK".equals(avgResponseStatus.getStatus().getCode())) {
            // LOGGER.error("Update Avg order status response failed : " + response);
            // return false;
            // }
            // }
            // }

            List<ImgChecklistDTO> lstImgs = woChecklistDTO.getLstImgs();
            WoDTO woDTO = woDTORequest.getWoDTO();
            Long woId = woDTO.getWoId();
            // Delete WO_MAPPING_ATTACH
            woDAO.removeAttachOfChecklist(woId, checklistId);
            // Update WO_MAPPING_CHECKLIST
            woChecklistBO.setProductCodeId(woChecklistDTO.getProductCodeId());
            woChecklistBO.setProductCode(woChecklistDTO.getProductCode());
            woChecklistBO.setCatStockId(woChecklistDTO.getCatStockId());
            woChecklistBO.setCatStockName(woChecklistDTO.getCatStockName());
            woChecklistBO.setSettopBox(woChecklistDTO.getSettopBox());
            woChecklistBO.setSmartCard(woChecklistDTO.getSmartCard());
            woChecklistBO.setState(woChecklistDTO.getState());
            woChecklistBO.setRejectReason(woChecklistDTO.getRejectReason());
            woChecklistBO.setCustomerConfirmDate(woChecklistDTO.getCustomerConfirmDate());
            woChecklistBO.setContent(orderStatus + " " + cancelledNote);
            woChecklistBO.setCompletedDate(new Date());
            woChecklistDAO.updateObject(woChecklistBO);
            // Insert image
            insertWoMappingAttach(woDTO, checklistId, lstImgs);

            // Insert WoMappingGoods
            List<VoGoodsDTO> goods = woDTORequest.getListOrderGoodsDTO();
            woMappingGoodsDAO.delMappingObjs(woId);
            for (VoGoodsDTO item : goods) {
                WoMappingGoodsDTO dto = new WoMappingGoodsDTO();
                dto.setWoId(woId);
                dto.setGoodsId(item.getGoodsId());
                dto.setName(StringUtils.isNullOrEmpty(item.getGoodsName()) ? item.getName() : item.getGoodsName());
                dto.setAmount(item.getAmount());
                dto.setAmountNeed(item.getAmountNeed());
                dto.setAmountReal(item.getAmountReal());
                dto.setIsSerial(item.getIsSerial());
                dto.setIsUsed(item.getIsUsed());
                dto.setSerial(item.getSerial());
                woMappingGoodsDAO.saveObject(dto.toModel());
            }
            return true;
        } catch (Exception ex) {
            LOGGER.error("Failed to update AVG order status");
            ex.printStackTrace();
            return false;
        }

    }

    private boolean isCheckSumOK(WoAvgVhktInputDTO dto) {
        try {
            String checkSumData = (dto.getOrderCodeTgdd() + dto.getOrderCodeAvg() + dto.getPersonalId()).replaceAll(" ",
                    ""); // Chỗ này dev 2 bên thống nhất xem những trường nào quan trọng thì nối vào mới
            // nhau để thành chuỗi chữ ký
            SecretKeySpec siginKey = new SecretKeySpec(checkSumKey.getBytes(), HMAC_SHA256_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(siginKey);
            byte[] rawHmac = mac.doFinal(checkSumData.getBytes());
            String expectedCheckSum = new String(Base64.getEncoder().encode(rawHmac), "UTF-8");
            if (expectedCheckSum.equals(dto.getCheckSum())) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            LOGGER.error("Failed to compare checkSum data: ", ex.getMessage());
            return false;
        }
    }

    // HienLT56 end 03062021
    // taotq start 27082021
    public List<WorkItemDTO> getListItemByWorkSrc() {
        // TODO Auto-generated method stub
        List<WorkItemDTO> lst = woDAO.getListItemByWorkSrc();
        return lst;
    }

    public List<WorkItemDTO> getListItemN(String code) {
        // TODO Auto-generated method stub
        List<WorkItemDTO> lst = woDAO.getListItemN(code);
        return lst;
    }
    // taotq end 27082021

    // Huypq-08092021-start
    @Transactional
    public void logWoWorkLogsNotSendSms(WoDTO dto, String logType, String content, String contentDetail,
                                        String loggedInUser) throws Exception {
        WoWorkLogsBO workLogs = new WoWorkLogsBO();
        workLogs.setWoId(dto.getWoId());
        workLogs.setContent(content);
        workLogs.setContentDetail(contentDetail);
        workLogs.setLogTime(new Date());
        workLogs.setLogType(logType);
        workLogs.setStatus(1);

        String loggedInUserStr = "Hệ thống";
        if (!StringUtils.isNullOrEmpty(loggedInUser)) {
            WoSimpleSysUserDTO sysUser = trDAO.getSysUser(loggedInUser);
            WoSimpleSysGroupDTO sysGroup = trDAO.getSysUserGroup(loggedInUser);
            loggedInUserStr = sysUser.getFullName() + " - " + sysUser.getEmployeeCode() + " - ";
            if (sysGroup.getGroupLevel() == 1)
                loggedInUserStr += sysGroup.getGroupNameLevel1();
            if (sysGroup.getGroupLevel() == 2)
                loggedInUserStr += sysGroup.getGroupNameLevel2();
            if (sysGroup.getGroupLevel() == 3)
                loggedInUserStr += sysGroup.getGroupNameLevel3();
        }
        workLogs.setUserCreated(loggedInUserStr);

        if ("UCTT".equalsIgnoreCase(dto.getWoTypeCode()) && !StringUtils.isNullOrEmpty(dto.getCustomField())) {
            WoWorkLogsBO workLogsUctt = workLogs;
            workLogsUctt.setContent(dto.getCustomField());
            woWorkLogsDAO.saveObject(workLogsUctt);
        }

        // Write worklogs
        woWorkLogsDAO.saveObject(workLogs);
    }

    // Huy-end
    // taotq start 16092021
    public void updateWoCheckList(Long id) {
        woDAO.updateWoCheckList(id);
    }

    public void updateWoMappingCheckList(Long id) {
        woDAO.updateWoMappingCheckList(id);
    }

    // taotq end 16092021

    // Huypq-23102021-start
    public List<CatWorkItemTypeDTO> getDataWorkItemByConsTypeId(CatWorkItemTypeDTO obj) {
        if (obj.getCatConstructionTypeId() == null) {
            return new ArrayList<>();
        }
        return woDAO.getDataWorkItemByConsTypeId(obj);
    }

    // Tạo wo theo parOrder appParam
    private void createWoByParOrder(WoBO woBO, ConstructionDTO constructionDTO, CntContractDTO cntContractDTO,
                                    String parOrder) throws Exception {
        Gson gson = new Gson();
        // Get AP_WORK_SRC
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        List<WoAppParamDTO> lstApWorkSrcs = woDAO.getAppParam("AP_WORK_SRC");
        Long apWorkSrc = null;
        for (WoAppParamDTO iAp : lstApWorkSrcs) {
            if (iAp.getName().contains("HTCT")) {
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

        WoTrDTO trDTO = trDAO.getOneDetails(woBO.getTrId());

        // Lấy cd_lv_2 theo trạm
        List<WoTrMappingStationDTO> lstTrMapStations = woTrMappingStationDAO.getStationsOfTr(woBO.getTrId());
        WoSimpleSysGroupDTO sysGroupDTO = woDAO.getSysGroup(lstTrMapStations.get(0).getSysGroupId().toString());

        List<WoAppParamDTO> lstDbhtWos = woDAO.getAppParamByParOrder("DBHT_WO", parOrder);
        for (WoAppParamDTO iWo : lstDbhtWos) {
            WoBO newBO = new WoBO();
            newBO.setCatWorkItemTypeId(Long.parseLong(iWo.getCode()));
            newBO.setApWorkSrc(apWorkSrc);
            newBO.setApConstructionType(apConstructionType);
            newBO.setStationCode(trDTO.getStationCode());
            // Wo code
            String code = "VNM_PMXL_1_";
            Long woTypeId = parOrder.equals("7") ? woTypeDAO.getIdByCode("PHATSONG") : woTypeDAO.getIdByCode("THICONG");
            Long nextSq = woDAO.getNextSeqVal();
            Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
            code += woTypeId + "_" + countIdType + "_" + nextSq;
            newBO.setWoCode(code);
            newBO.setTrCode(trDTO.getTrCode());
            // Wo name
            WoNameDTO dtoName = new WoNameDTO();
            dtoName.setWoTypeId(woTypeId);
            List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
            if (lstNames != null && lstNames.size() > 0) {
                newBO.setWoName(lstNames.get(0).getName());
            }
            newBO.setUserCreated(trDTO.getUserCreated());
//			taotq start 270142022 chuyển CDLV1 về ĐTHT
            if (apWorkSrc == 4) {
                newBO.setCdLevel1("166677");
                newBO.setCdLevel1Name("TTDTHT - Trung tâm đầu tư hạ tầng");
            } else {
                newBO.setCdLevel1(trDTO.getCdLevel1());
                newBO.setCdLevel1Name(trDTO.getCdLevel1Name());
            }
//			taotq end 270142022
            newBO.setCdLevel2(lstTrMapStations.get(0).getSysGroupId().toString());
            newBO.setCdLevel2Name(sysGroupDTO.getGroupName());
            newBO.setTrId(trDTO.getTrId());
            newBO.setConstructionId(constructionDTO.getConstructionId());
            newBO.setConstructionCode(constructionDTO.getCode());
            newBO.setContractId(cntContractDTO.getCntContractId());
            newBO.setContractCode(cntContractDTO.getCode());
            if (parOrder.equals("7")) {
                newBO.setMoneyValue(0d);
            } else {
                newBO.setMoneyValue(woBO.getMoneyValue());
            }

            newBO.setCreatedDate(today);
            newBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
            newBO.setWoTypeId(woTypeId);
            newBO.setQoutaTime(trDTO.getQoutaTime());
            // Finish date
            // cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            // cal.add(Calendar.DATE, -1);
            newBO.setFinishDate(cal.getTime());
            newBO.setState(ASSIGN_CD);
            newBO.setStatus(1l);
            newBO.setWoOrder("" + iWo.getParOrder());
            newBO.setStationCode(woBO.getStationCode());
            woDAO.saveObject(newBO);

            WoDTO woDTO = newBO.toDTO();
            woDTO.setIsCreateNew(true);
            logWoWorkLogs(woDTO, "1", "Tự động sinh khi hoàn thành WO thiết kế dự toán.", gson.toJson(newBO),
                    trDTO.getLoggedInUser());

            // Create checklist
            tryCreateChecklistForNewWo(newBO.toDTO());

            if (parOrder.equals("1") || parOrder.equals("7")) {
                if (woDTO.getCatWorkItemTypeId() != null && woDTO.getConstructionId() != null) {
                    if (!woDAO.checkExistConstructionWorkItem(woDTO.getConstructionId(),
                            woDTO.getCatWorkItemTypeId())) {
                        woDTO.setSysUserId(-1l);
                        woDTO.setSysGroupId(-1l);
                        WoCatWorkItemTypeDTO catWI = woDAO.getCatWorkTypeById(woDTO.getCatWorkItemTypeId());
                        woDTO.setCatWorkItemTypeCode(catWI.getCatWorkItemTypeCode());
                        woDTO.setCatWorkItemTypeName(catWI.getName());
                        woDAO.insertWorkItem(woDTO);
                    }
                }
            }
        }
    }

    // Tạo wo thi công khi đóng wo khởi công
    private void createWoThiCongWhenAccessWoKhoiCong(WoBO woBO, ConstructionDTO constructionDTO,
                                                     CntContractDTO cntContractDTO) throws Exception {
        Gson gson = new Gson();
        // Get AP_WORK_SRC
        List<WoAppParamDTO> lstApWorkSrcs = woDAO.getAppParam("AP_WORK_SRC");
        Long apWorkSrc = null;
        for (WoAppParamDTO iAp : lstApWorkSrcs) {
            if (iAp.getName().contains("HTCT")) {
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

        WoTrDTO trDTO = trDAO.getOneDetails(woBO.getTrId());

        // Lấy cd_lv_2 theo trạm
        // List<WoTrMappingStationDTO> lstTrMapStations =
        // woTrMappingStationDAO.getStationsOfTr(woBO.getTrId());
        // WoSimpleSysGroupDTO sysGroupDTO =
        // woDAO.getSysGroup(lstTrMapStations.get(0).getSysGroupId().toString());

        List<WoMappingWorkItemHtctBO> lstDbhtWos = woDAO
                .getWoMappingWiHtctByConsId(constructionDTO.getConstructionId());
        for (WoMappingWorkItemHtctBO iWo : lstDbhtWos) {
            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            WoBO newBO = new WoBO();
            newBO.setCatWorkItemTypeId(iWo.getWorkItemId());
            newBO.setApWorkSrc(apWorkSrc);
            newBO.setApConstructionType(apConstructionType);
            newBO.setStationCode(trDTO.getStationCode());
            // Wo code
            String code = "VNM_PMXL_1_";
            Long woTypeId = woTypeDAO.getIdByCode("THICONG");
            Long nextSq = woDAO.getNextSeqVal();
            Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
            code += woTypeId + "_" + countIdType + "_" + nextSq;
            newBO.setWoCode(code);
            newBO.setTrCode(trDTO.getTrCode());
            // Wo name
            WoNameDTO dtoName = new WoNameDTO();
            dtoName.setWoTypeId(woTypeId);
            List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
            if (lstNames != null && lstNames.size() > 0) {
                newBO.setWoName(lstNames.get(0).getName());
            }
            newBO.setUserCreated(trDTO.getUserCreated());
//			taotq start 27042022 đổi CDLV1 về DTHT
            if (woBO.getApWorkSrc() == 4) {
                newBO.setCdLevel1("166677");
                newBO.setCdLevel1Name("TTDTHT - Trung tâm đầu tư hạ tầng");
            } else {
                newBO.setCdLevel1(trDTO.getCdLevel1());
                newBO.setCdLevel1Name(trDTO.getCdLevel1Name());
            }
//			taotq end 27042022
            newBO.setCdLevel2(woBO.getCdLevel2());
            newBO.setCdLevel2Name(woBO.getCdLevel2Name());
            newBO.setTrId(trDTO.getTrId());
            newBO.setConstructionId(constructionDTO.getConstructionId());
            newBO.setConstructionCode(constructionDTO.getCode());
            newBO.setContractId(cntContractDTO.getCntContractId());
            newBO.setContractCode(cntContractDTO.getCode());
            newBO.setMoneyValue(iWo.getWorkItemValue());
            newBO.setCreatedDate(today);
            newBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
            newBO.setWoTypeId(woTypeId);
            newBO.setQoutaTime(trDTO.getQoutaTime());
            // Finish date
            // cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, 45);
            // cal.add(Calendar.DATE, -1);
            newBO.setFinishDate(cal.getTime());
            newBO.setState(ASSIGN_CD);
            newBO.setStatus(1l);
            newBO.setWoOrder("2");
            newBO.setStationCode(woBO.getStationCode());
            woDAO.saveObject(newBO);

            WoDTO woDTO = newBO.toDTO();
            woDTO.setIsCreateNew(true);
            logWoWorkLogs(woDTO, "1", "Tự động sinh khi hoàn thành WO khởi công.", gson.toJson(newBO),
                    trDTO.getLoggedInUser());

            // Create checklist
            tryCreateChecklistForNewWo(newBO.toDTO());
        }
    }

    private boolean checkContractCanCreateHSHC(Long contractId) {
        if (contractId == null)
            return false;
        return woDAO.checkContractCanCreateHSHC(contractId);
    }

    public WoDTO getDataConstructionContractByStationCode(String stationCode) {
        return woDAO.getDataConstructionContractByStationCode(stationCode);
    }

    public Boolean checkRoleCDPKTCNKT(HttpServletRequest request) {
        // TODO Auto-generated method stub
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CD, Constant.AdResourceKey.PKTCNKT, request)) {
            return false;
        }
        return true;
    }

    @Override
    public List<ManageCertificateDTO> getListCertificateEnableFT(ManageCertificateDTO certificateDTO) {
        // TODO Auto-generated method stub
        return manageCertificateDAO.getListCertificateEnableFT(certificateDTO);
    }

    public WoDTO createWoDbht(WoBO woBO, ConstructionDTO constructionDTO, CntContractDTO cntContractDTO, String loggedInUser) throws Exception {
        Gson gson = new Gson();

        // Get AP_WORK_SRC
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        List<WoAppParamDTO> lstApWorkSrcs = woDAO.getAppParam("AP_WORK_SRC");
        Long apWorkSrc = null;
        for (WoAppParamDTO iAp : lstApWorkSrcs) {
            if (iAp.getName().contains("HTCT")) {
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

        WoTrDTO trDTO = trDAO.getOneDetails(woBO.getTrId());

        // Lấy cd_lv_2 theo trạm
        List<WoTrMappingStationDTO> lstTrMapStations = woTrMappingStationDAO.getStationsOfTr(woBO.getTrId());

        WoSimpleSysGroupDTO sysGroupDTO = woDAO.getSysGroup(lstTrMapStations.get(0).getSysGroupId().toString());

        WoSimpleSysGroupDTO cd1 = trDAO.getSysGroupById(Long.parseLong("242656"));

        WoBO newBO = new WoBO();
        newBO.setApWorkSrc(apWorkSrc);
        newBO.setApConstructionType(apConstructionType);
        // Wo code
        String code = "VNM_PMXL_1_";
        Long woTypeId = woTypeDAO.getIdByCode("DBHT");
        Long nextSq = woDAO.getNextSeqVal();
        Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
        code += woTypeId + "_" + countIdType + "_" + nextSq;
        newBO.setWoCode(code);
        newBO.setTrCode(trDTO.getTrCode());
        // Wo name
        WoNameDTO dtoName = new WoNameDTO();
        dtoName.setWoTypeId(woTypeId);
        List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
        if (lstNames != null && lstNames.size() > 0) {
            newBO.setWoName(lstNames.get(0).getName());
        }
        newBO.setUserCreated(trDTO.getUserCreated());
        if (woBO.getApWorkSrc() == 4) {
            newBO.setCdLevel1("166677");
            newBO.setCdLevel1Name("TTDTHT - Trung tâm đầu tư hạ tầng");
//			if(woBO.getCatWorkItemTypeId() == 2083){
            newBO.setCdLevel2("166677");
            newBO.setCdLevel2Name("TTDTHT - Trung tâm đầu tư hạ tầng");
//			}
        } else {
            newBO.setCdLevel1("242656");
            newBO.setCdLevel1Name(cd1.getGroupName());
            newBO.setCdLevel2("242656");
            newBO.setCdLevel2Name(cd1.getGroupName());
        }
        newBO.setTrId(trDTO.getTrId());
        newBO.setConstructionId(constructionDTO.getConstructionId());
        newBO.setConstructionCode(constructionDTO.getCode());
        newBO.setContractId(cntContractDTO.getCntContractId());
        newBO.setContractCode(cntContractDTO.getCode());
        newBO.setMoneyValue(woDAO.getHshcMoneyValueByConstructionId(woBO.getConstructionId()));
        newBO.setCreatedDate(today);
        newBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
        newBO.setWoTypeId(woTypeId);
        newBO.setQoutaTime(trDTO.getQoutaTime());
        // Finish date
        cal.add(Calendar.DAY_OF_MONTH, 7);
        newBO.setFinishDate(cal.getTime());
        newBO.setState(CD_OK);
        newBO.setStatus(1l);
        newBO.setStationCode(woBO.getStationCode());
        woDAO.saveObject(newBO);

        WoDTO woDTO = newBO.toDTO();
        woDTO.setIsCreateNew(true);
        logWoWorkLogs(woDTO, "1", "Tự động sinh khi các hạng mục hoàn thành.", gson.toJson(newBO), loggedInUser);
        return woDTO;
    }



    public WoDTO createWoBts(WoBO woBO, ConstructionDTO constructionDTO, CntContractDTO cntContractDTO, String loggedInUser) throws Exception {
        Gson gson = new Gson();

        // Get AP_WORK_SRC
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        List<WoAppParamDTO> lstApWorkSrcs = woDAO.getAppParam("AP_WORK_SRC");
        Long apWorkSrc = null;
        for (WoAppParamDTO iAp : lstApWorkSrcs) {
            if (iAp.getName().contains("HTCT")) {
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

        WoTrDTO trDTO = trDAO.getOneDetails(woBO.getTrId());

        // Lấy cd_lv_2 theo trạm

        SysGroupDTO sysGroup = sysGroupDAO.getListCdLv2BTS(cntContractDTO.getCntContractId());
        //save
        WoBO newBO = new WoBO();
        Long id = saveWoBTS(apWorkSrc,apConstructionType,sysGroup,newBO,trDTO,cntContractDTO,constructionDTO,woBO,today,cal);


        // tạo check list
        createCheckListBTS(cntContractDTO,id);

        // log
        WoDTO woDTO = writeLogWoBts(newBO,loggedInUser,gson);

        return woDTO;
    }

    private void createWoBtsVHKT(WoBO bo, ConstructionDTO constructionDTO, CntContractDTO cntContractDTO, String loggedInUser, WoDTO woDto, Gson gson) {
        try {
            Long woTypeId = woTypeDAO.getIdByCode("BGBTS_VHKT");
        WoBO woBO = woDAO.findByWoIdAndWoTypeIdAndStatus(bo.getTrId(),woTypeId,Constant.STATUS.ACTIVE);
        if(!ObjectUtils.isEmpty(woBO)){
            woBO.setState(Constant.WO_STATE.ACCEPT_CD);
            woDAO.update(woBO);
            // xóa ảnh các đầu việc
            woMappingAttachDAO.deleteImg(woBO.getWoId());

            //copy ảnh lại của dtht
            insertCheckListFromDTHTToVHKT(woDto,woBO);

            woDto = woBO.toDTO();
            String content = woDAO.getNameAppParam(ACCEPT_CD, WO_XL_STATE);
            logWoWorkLogs(woDto, "1", content, gson.toJson(bo), loggedInUser);
        }else {
            // nếu không có bản ghi thì sinh mới

            // Get AP_WORK_SRC
            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);

            List<WoAppParamDTO> lstApWorkSrcs = woDAO.getAppParam("AP_WORK_SRC");
            Long apWorkSrc = null;
            for (WoAppParamDTO iAp : lstApWorkSrcs) {
                if (iAp.getName().contains("HTCT")) {
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

            WoTrDTO trDTO = trDAO.getOneDetails(bo.getTrId());

            // Lấy cd_lv_2 theo trạm

            SysGroupDTO sysGroup = sysGroupDAO.getListCdLv2BTSVHKT(cntContractDTO.getCntContractId());
            //save
            WoBO newBO = new WoBO();
            Long id = saveWoBtsVHKT(apWorkSrc,apConstructionType,sysGroup,newBO,trDTO,cntContractDTO,constructionDTO,bo,today,cal);


            // tạo check list
            createAndUpdateCheckListBTSVHKT(bo,id,woDto);

            // log
            WoDTO woDTO = writeLogWoBts(newBO,loggedInUser,gson);
        }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void insertCheckListFromDTHTToVHKT(WoDTO woDto, WoBO woBO) {
        List<WoMappingAttachBO> mappingAttachBOList = woMappingAttachDAO.findByWoIdAndStatus(woDto.toModel());
        if(!CollectionUtils.isEmpty(mappingAttachBOList)){
            for(WoMappingAttachBO woMappingAttachBO : mappingAttachBOList){
                woMappingAttachBO.setWoId(woBO.getWoId());
                woMappingAttachDAO.saveObject(woMappingAttachBO);
            }
        }
    }


    private Long saveWoBtsVHKT(Long apWorkSrc, Long apConstructionType, SysGroupDTO sysGroup, WoBO newBO, WoTrDTO trDTO, CntContractDTO cntContractDTO, ConstructionDTO constructionDTO, WoBO woBO, Date today, Calendar cal) {
        newBO.setApWorkSrc(apWorkSrc);
        newBO.setApConstructionType(apConstructionType);
        // Wo code
        String code = "VNM_PMXL_1_";
        Long woTypeId = woTypeDAO.getIdByCode("BGBTS_VHKT");
        Long nextSq = woDAO.getNextSeqVal();
        Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
        code += woTypeId + "_" + countIdType + "_" + nextSq;
        newBO.setWoCode(code);
        newBO.setTrCode(trDTO.getTrCode());
        // Wo name
        WoNameDTO dtoName = new WoNameDTO();
        dtoName.setWoTypeId(woTypeId);
        List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
        if (lstNames != null && lstNames.size() > 0) {
            newBO.setWoName(lstNames.get(0).getName());
        }
        newBO.setCdLevel1("270120");
        newBO.setCdLevel1Name("TTVHKT - Trung tâm vận hành khai thác");
        newBO.setCdLevel2(String.valueOf(sysGroup.getSysGroupId()));
        newBO.setCdLevel2Name(sysGroup.getName());
        newBO.setCdLevel3(String.valueOf(sysGroup.getSysGroupId()));
        newBO.setCdLevel3Name(sysGroup.getName());
        newBO.setContractId(cntContractDTO.getCntContractId());

        newBO.setUserCreated(trDTO.getUserCreated());
        newBO.setTrId(trDTO.getTrId());
        newBO.setConstructionId(constructionDTO.getConstructionId());
        newBO.setConstructionCode(constructionDTO.getCode());
        newBO.setContractId(cntContractDTO.getCntContractId());
        newBO.setContractCode(cntContractDTO.getCode());
        newBO.setMoneyValue(woDAO.getHshcMoneyValueByConstructionId(woBO.getConstructionId()));
        newBO.setCreatedDate(today);
        newBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
        newBO.setWoTypeId(woTypeId);
        newBO.setQoutaTime(trDTO.getQoutaTime());
        // Finish date
        cal.add(Calendar.DAY_OF_MONTH, 7);
        newBO.setFinishDate(cal.getTime());
        newBO.setState(ACCEPT_CD);
        newBO.setStatus(1l);
        newBO.setStationCode(woBO.getStationCode());
        Long id = woDAO.saveObject(newBO);
        return id ;
    }

    private WoDTO writeLogWoBts(WoBO newBO, String loggedInUser, Gson gson) {
        WoDTO woDTO = newBO.toDTO();
        woDTO.setIsCreateNew(true);
        try {
            logWoWorkLogs(woDTO, "1", "Tự động sinh khi các hạng mục hoàn thành.", gson.toJson(newBO), loggedInUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return woDTO;
    }

    private Long saveWoBTS(Long apWorkSrc, Long apConstructionType, SysGroupDTO sysGroup, WoBO newBO, WoTrDTO trDTO, CntContractDTO cntContractDTO, ConstructionDTO constructionDTO, WoBO woBO, Date today, Calendar cal) {
        newBO.setApWorkSrc(apWorkSrc);
        newBO.setApConstructionType(apConstructionType);
        // Wo code
        String code = "VNM_PMXL_1_";
        Long woTypeId = woTypeDAO.getIdByCode("BGBTS_DTHT");
        Long nextSq = woDAO.getNextSeqVal();
        Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
        code += woTypeId + "_" + countIdType + "_" + nextSq;
        newBO.setWoCode(code);
        newBO.setTrCode(trDTO.getTrCode());
        // Wo name
        WoNameDTO dtoName = new WoNameDTO();
        dtoName.setWoTypeId(woTypeId);
        List<WoNameDTO> lstNames = woNameDAO.doSearch(dtoName);
        if (lstNames != null && lstNames.size() > 0) {
            newBO.setWoName(lstNames.get(0).getName());
        }
        newBO.setCdLevel1("166677");
        newBO.setCdLevel1Name("TTDTHT - Trung tâm đầu tư hạ tầng");
        newBO.setCdLevel2(String.valueOf(sysGroup.getSysGroupId()));
        newBO.setCdLevel2Name(sysGroup.getName());
        newBO.setContractId(cntContractDTO.getCntContractId());

        newBO.setUserCreated(trDTO.getUserCreated());
        newBO.setTrId(trDTO.getTrId());
        newBO.setConstructionId(constructionDTO.getConstructionId());
        newBO.setConstructionCode(constructionDTO.getCode());
        newBO.setContractId(cntContractDTO.getCntContractId());
        newBO.setContractCode(cntContractDTO.getCode());
        newBO.setMoneyValue(woDAO.getHshcMoneyValueByConstructionId(woBO.getConstructionId()));
        newBO.setCreatedDate(today);
        newBO.setTotalMonthPlanId((long) cal.get(Calendar.MONTH) + 1);
        newBO.setWoTypeId(woTypeId);
        newBO.setQoutaTime(trDTO.getQoutaTime());
        // Finish date
        cal.add(Calendar.DAY_OF_MONTH, 90);
        newBO.setFinishDate(cal.getTime());
        newBO.setState(ASSIGN_CD);
        newBO.setStatus(1l);
        newBO.setStationCode(woBO.getStationCode());
        Long id = woDAO.saveObject(newBO);
        return id ;
    }



    private void createCheckListBTS(CntContractDTO cntContractDTO, Long woId) {
        List<AppParamDTO> appParamDTO = appParamDAO.getAppParamByParType(Constant.PAR_TYPE.WO_BGBTS_CHECKLIST);
        for (AppParamDTO appParam : appParamDTO){
        WoMappingChecklistBO checklistItem = new WoMappingChecklistBO();
            checklistItem.setWoId(woId);
            checklistItem.setCheckListId(Long.valueOf(appParam.getParOrder()));
            checklistItem.setStatus(Constant.CHECK_LIST_STATUS.ACTIVE);
            checklistItem.setState(Constant.CHECK_LIST_STATE.NEW);
            checklistItem.setName(appParam.getName());
            checklistItem.setNumImgRequire(appParam.getOptionNumber());
        woMappingChecklistDAO.saveObject(checklistItem);
        }
    }
    private void createAndUpdateCheckListBTSVHKT(WoBO bo, Long id, WoDTO woDto) {
    List<WoMappingAttachBO> mappingAttachBOList = woMappingAttachDAO.findByWoIdAndStatus(bo);
    List<AppParamDTO> appParamDTO = appParamDAO.getAppParamByParType(Constant.PAR_TYPE.WO_BGBTS_CHECKLIST);
        for (AppParamDTO appParam : appParamDTO){
            WoMappingChecklistBO checklistItem = new WoMappingChecklistBO();
            checklistItem.setWoId(id);
            checklistItem.setCheckListId(Long.valueOf(appParam.getParOrder()));
            checklistItem.setStatus(Constant.CHECK_LIST_STATUS.ACTIVE);
            checklistItem.setState(Constant.CHECK_LIST_STATE.NEW);
            checklistItem.setName(appParam.getName());
            checklistItem.setNumImgRequire(appParam.getOptionNumber());
            woMappingChecklistDAO.saveObject(checklistItem);
        }
    if(!CollectionUtils.isEmpty(mappingAttachBOList)){
        for(WoMappingAttachBO woMappingAttachBO : mappingAttachBOList){
            woMappingAttachBO.setWoId(id);
            woMappingAttachDAO.saveObject(woMappingAttachBO);
        }
    }
    // update check list của DTHT
    if(!CollectionUtils.isEmpty(woDto.getListChecklistId())){
        List<WoMappingChecklistBO> woMappingChecklistBOList = woMappingChecklistDAO.findByWoIdEntity(woDto.getWoId());
        if(!CollectionUtils.isEmpty(woMappingChecklistBOList)){
            for (WoMappingChecklistBO woMappingChecklistBO : woMappingChecklistBOList){
                if(woDto.getListChecklistId().contains(woMappingChecklistBO.getCheckListId())){
                    woMappingChecklistBO.setState(NEW);
                    woMappingChecklistDAO.updateObject(woMappingChecklistBO);
                }
            }
        }
    }
    }

//            if(!ObjectUtils.isEmpty(woDto.getChecklistId())){
//        if(woMappingAttachBO.getChecklistId().equals(woDto.getChecklistId())){
//            woMappingAttachBO.setS
//        }
//    }
    @Override
    public List<WoChecklistDTO> getListChecklistsOfWoAvg(Long woId) {
        WoChecklistDTO searchDto = new WoChecklistDTO();
        searchDto.setWoId(woId);
        return woChecklistDAO.doSearch(searchDto);
    }

    //Huypq-10122021-start
    private boolean checkStationConstruction(WoDTO dto) {
        if ("THICONG".equalsIgnoreCase(dto.getWoTypeCode()) && dto.getConstructionId() != null) {
            boolean checkResult = catStationDAO.checkStationConstruction(dto.getConstructionId());

            if (!checkResult) {
                dto.setCustomField("Công trình chưa được gán mã trạm; ");
                return false;
            }

        }

        return true;
    }

    //Huy-end
    //Huypq-28122021-start
    @SuppressWarnings("deprecation")
    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
                    && !StringUtils.isStringNullOrEmpty(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    private String getStringValueInCell(DataFormatter formatter, Cell cell) {
        String value = null;
        if (validateString(formatter.formatCellValue(cell))) {
            value = formatter.formatCellValue(cell);
        }
        return value;
    }

    private Long getLongValueInCell(DataFormatter formatter, Cell cell) {
        Long value = 0l;
        if (validateString(formatter.formatCellValue(cell))) {
            value = Long.valueOf(formatter.formatCellValue(cell));
        }
        return value;
    }

    private Double getDoubleValueInCell(DataFormatter formatter, Cell cell) {
        Double value = 0d;
        if (validateString(formatter.formatCellValue(cell))) {
            value = Double.valueOf(formatter.formatCellValue(cell));
        }
        return value;
    }

    public List<EffectiveCalculationDetailsDTO> importFileTthq(String fileInput, Long woId, HttpServletRequest request)
            throws Exception {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        List<EffectiveCalculationDetailsDTO> workLst = Lists.newArrayList();
        List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
        try {
            File f = new File(fileInput);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            int count = 0;
            for (Row row : sheet) {
                count++;
                if (count >= 3 && checkIfRowIsEmpty(row))
                    continue;
                EffectiveCalculationDetailsDTO obj = new EffectiveCalculationDetailsDTO();
                if (count >= 3) {
                    obj.setAreaCode(getStringValueInCell(formatter, row.getCell(1)));
                    obj.setProvinceCode(getStringValueInCell(formatter, row.getCell(2)));
                    obj.setStationCodeVtn(getStringValueInCell(formatter, row.getCell(3)));
                    obj.setStationCodeVcc(getStringValueInCell(formatter, row.getCell(4)));
                    obj.setAddress(getStringValueInCell(formatter, row.getCell(5)));
                    obj.setStationType(getStringValueInCell(formatter, row.getCell(6)));
                    obj.setMaiDat(getStringValueInCell(formatter, row.getCell(7)));
                    obj.setDoCaoCot(getDoubleValueInCell(formatter, row.getCell(8)));
                    obj.setLoaiCot(getStringValueInCell(formatter, row.getCell(9)));
                    obj.setMongCo(getStringValueInCell(formatter, row.getCell(10)));
                    obj.setLoaiNha(getStringValueInCell(formatter, row.getCell(11)));
                    obj.setTiepDia(getStringValueInCell(formatter, row.getCell(12)));
                    obj.setDienCnkt(getStringValueInCell(formatter, row.getCell(13)));
                    obj.setSoCotDien(getStringValueInCell(formatter, row.getCell(14)));
                    obj.setVanChuyenBo(getStringValueInCell(formatter, row.getCell(15)));
                    obj.setThueAcquy(getStringValueInCell(formatter, row.getCell(16)));
                    obj.setGiaThueMbThucTe(getLongValueInCell(formatter, row.getCell(17)));
//					obj.setGiaThueMbTheoDinhMuc(getNumberValueInCell(formatter, row.getCell(18)));
//					obj.setCapexCot(getNumberValueInCell(formatter, row.getCell(19)));
//					obj.setCapexTiepDia(getNumberValueInCell(formatter, row.getCell(20)));
//					obj.setCapexAc(getNumberValueInCell(formatter, row.getCell(21)));
//					obj.setCapexPhongMay(getNumberValueInCell(formatter, row.getCell(22)));
                    obj.setCapexTruocVatString(getStringValueInCell(formatter, row.getCell(18)));
                    obj.setVccChaoGiaHt(getDoubleValueInCell(formatter, row.getCell(19)));
                    obj.setVccChaoGiaAcquy(getDoubleValueInCell(formatter, row.getCell(20)));
                    obj.setTongCong(getLongValueInCell(formatter, row.getCell(21)));
//					obj.setDescription(getStringValueInCell(formatter, row.getCell(26)));
                    obj.setNgayGui(new Date());
                    if (obj.getTongCapexHt() != null && obj.getTongCapexHt() != 0) {
                        obj.setNgayHoanThanh(new Date());
                    }
                    obj.setNguoiLap(user.getEmail().replace("@viettel.com.vn", ""));
//					obj.setCapexHtVcc(validateCellLong(getValueCellFormula(workbook, evaluator, sheet3, "I3", "number")));
                    obj.setNpvString(getStringValueInCell(formatter, row.getCell(22)));
                    obj.setIrrString(getStringValueInCell(formatter, row.getCell(23)));
                    obj.setThoiGianHvString(getStringValueInCell(formatter, row.getCell(24)));
                    obj.setLnstDtString(getStringValueInCell(formatter, row.getCell(25)));
                    obj.setConclude(getStringValueInCell(formatter, row.getCell(26)));

                    obj.setWoId(woId);
                    workLst.add(obj);
                }
            }

            if (errorList.size() > 0) {
                String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
                List<EffectiveCalculationDetailsDTO> emptyArray = Lists.newArrayList();
                workLst = emptyArray;
                EffectiveCalculationDetailsDTO errorContainer = new EffectiveCalculationDetailsDTO();
                errorContainer.setErrorList(errorList);
                errorContainer.setMessageColumn(10); // cột dùng để in ra lỗi
                errorContainer.setFilePathError(filePathError);
                workLst.add(errorContainer);
            }

            workbook.close();
            return workLst;

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            ExcelErrorDTO errorDTO = createError(0, 0, e.toString());
            errorList.add(errorDTO);
            String filePathError = null;
            try {
                filePathError = UEncrypt.encryptFileUploadPath(fileInput);
            } catch (Exception ex) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
                errorDTO = createError(0, 0, ex.toString());
                errorList.add(errorDTO);
            }
            List<EffectiveCalculationDetailsDTO> emptyArray = Lists.newArrayList();
            workLst = emptyArray;
            EffectiveCalculationDetailsDTO errorContainer = new EffectiveCalculationDetailsDTO();
            errorContainer.setErrorList(errorList);
            errorContainer.setMessageColumn(10); // cột dùng để in ra lỗi
            errorContainer.setFilePathError(filePathError);
            workLst.add(errorContainer);
            return workLst;
        }
    }

    //Huy-end
    public void changeStateReProcessWoDoanhThuDTHT(WoDTO dto) {
        Gson gson = new Gson();
        try {
            WoBO woBO = woDAO.getOneRaw(dto.getWoId());
            woBO.setState(CD_OK);
            woDAO.update(woBO);

            // Write log
            WoDTO woDTO = woBO.toDTO();
            String logContent = "Trạng thái WO: " + woDAO.getNameAppParam(woBO.getState(), WO_XL_STATE);
            logWoWorkLogs(woDTO, "1", logContent, gson.toJson(woDTO), dto.getLoggedInUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //	taotq start 06012022
    public Long CreateWOHTCTDR(WoDTO inputWo) throws Exception {
        Long woId = 0l;
        try {
            Integer defaultQuotaTime = 24 * 30; // 30 days in hours
            long woTypeId = 461l;
            Integer quotaInDays = Math.round(defaultQuotaTime / 24);
            Date createdDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(createdDate);
            c.add(Calendar.DATE, quotaInDays);
            Date finishDate = c.getTime();

            // tạo wo
            WoBO woBO = new WoBO();
            woBO.setWoId(0l);
            woBO.setWoName("WO HDHTCT đầu ra");
            woBO.setWoTypeId(woTypeId);
            woBO.setTrId(inputWo.getTrId());
            woBO.setTrCode(inputWo.getTrCode());
            woBO.setState(ASSIGN_CD);
            woBO.setStationCode(inputWo.getStationCode());
            woBO.setCreatedDate(createdDate);
            woBO.setFinishDate(finishDate);
            woBO.setQoutaTime(defaultQuotaTime);
            woBO.setCdLevel1(inputWo.getCdLevel1());
            woBO.setCdLevel1Name(inputWo.getCdLevel1Name());
            woBO.setCdLevel2(inputWo.getCdLevel2());
            woBO.setCdLevel2Name(inputWo.getCdLevel2Name());
            woBO.setStatus(1l);
            woBO.setApConstructionType(inputWo.getApConstructionType());
            woBO.setApWorkSrc(inputWo.getApWorkSrc());
            woBO.setConstructionId(inputWo.getConstructionId());
            woBO.setConstructionCode(inputWo.getConstructionCode());
            woBO.setCatConstructionTypeId(inputWo.getCatConstructionTypeId());
//			woBO.setContractId(inputWo.getContractId());
//			woBO.setContractCode(inputWo.getContractCode());
            woBO.setProjectId(inputWo.getProjectId());
            woBO.setProjectCode(inputWo.getProjectCode());

            String woCode = this.generateWoCode(woBO.toDTO());
            woBO.setWoCode(woCode);
            Long totalMonthPlanId = finishDate.getMonth() + 1l;
            woBO.setTotalMonthPlanId(totalMonthPlanId);
            woBO.setUserCreated("-1");

            woId = woDAO.saveObject(woBO);

            // tạo checklist
//			List<WoAppParamDTO> checklistItems = woDAO.getAppParam("HSHC_CHECKLIST");
//			createChecklistByAppParam(woId, checklistItems);

            String content = "Tự động tạo WO hạ tầng cho thuê đầu ra do công trình đã hoàn thành. ";
            WoDTO woDto = woBO.toDTO();
            Gson gson = new Gson();
            logWoWorkLogs(woDto, "1", content, gson.toJson(woDto), inputWo.getLoggedInUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return woId;
    }

    public List<WoDTO> getCD(Long constructionId) {
        // TODO Auto-generated method stub
        List<WoDTO> lstDto = woDAO.getCD(constructionId);
        return lstDto;
    }
//	taotq end 06012022

    public Long getCntContractId(String contractCode) {
        // TODO Auto-generated method stub
        Long id = woDAO.getCntContractId(contractCode);
        return id;
    }

    //Huypq-01062022-start
    @Transactional
    private void saveInfoWoFromIoc(WoDTO dto) throws Exception {
        List<WoMappingChecklistBO> listMappingChecklist = new ArrayList<>();
        List<WoXdddChecklistBO> listWoXdddChecklist = dto.getListWoChecklists();
        List<WoWorkLogsBO> listWoWorklog = dto.getListWoWorkLogs();
        List<WoMappingAttachBO> listMappingAttach = new ArrayList<>();

        for (WoMappingChecklistBO bo : dto.getListWoMappingChecklists()) {
            bo.setWoId(dto.getWoId());
            bo.setStatus(1l);
            listMappingChecklist.add(bo);
//			for(WoMappingAttachBO attach : bo.getListWoMappingAttach()) {
//				listMappingAttach.add(attach);
//			}
        }

        for (WoXdddChecklistBO bo : listWoXdddChecklist) {
            bo.setWoId(dto.getWoId());
            bo.setStatus(1l);
        }

        for (WoWorkLogsBO bo : listWoWorklog) {
            bo.setWoId(dto.getWoId());
        }

        woMappingCheckListDAO.saveListNoId(listMappingChecklist);

        woXdddChecklistDAO.saveListNoId(listWoXdddChecklist);

        woWorkLogsDAO.saveListNoId(listWoWorklog);

        woMappingAttachDAO.saveListNoId(listMappingAttach);
    }

    public Long approveExtentionWoHcFromIoc(WoDTO dto) throws Exception {
        Long result = 0l;
        Long woId = woDAO.saveObject(dto.toModel());

        dto.setWoId(woId);

        this.saveInfoWoFromIoc(dto);
        return result;
    }

    public List<WoDTO> sendApproveWoHcFromIoc(WoDTO dto) throws Exception {
        for (WoDTO wo : dto.getListWos()) {

            Long woId = woDAO.saveObject(wo.toModel());
            wo.setWoId(woId);
            wo.setListWoMappingChecklists(dto.getListWoMappingChecklists());
//			wo.setListWoChecklists(dto.getListWoChecklists());
            wo.setListWoWorkLogs(dto.getListWoWorkLogs());
            wo.setChecklistId(dto.getListWoChecklists().get(0).getId());
            this.saveInfoWoFromIoc(wo);
        }

        return dto.getListWos();
    }

    //Huy-end

    //	taotq start 26042022
    public void createNewTmbtWONew(WoTrDTO dto) {
        Gson gson = new Gson();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        try {
            WoTrDTO trDTO = dto;
            List<WoTrMappingStationDTO> lstTrMapStations = new ArrayList<>();
            String lstStation = "";
            for (CatStationDetailDTO dto1 : dto.getSelectedStations()) {
                WoTrMappingStationDTO dtoMap = new WoTrMappingStationDTO();
                dtoMap.setCatStationId(dto1.getCatStationId());
                dtoMap.setTrId(dto.getId());
                dtoMap.setSysGroupId(dto1.getTmbtSysGroupId());
                dtoMap.setStatus(1L);
                lstTrMapStations.add(dtoMap);
                if (lstStation.equals("")) {
                    lstStation = dto1.getCatStationId().toString();
                } else {
                    lstStation = lstStation + "," + dto1.getCatStationId().toString();
                }
            }
            // Nếu có trạm thì sinh wo theo danh sách đó
            if (lstTrMapStations.size() > 0) {
                for (WoTrMappingStationDTO iCsr : lstTrMapStations) {
                    WoSimpleSysGroupDTO sysGroupDTO = woDAO.getSysGroup("" + iCsr.getSysGroupId());
                    // Generate TMBT follow trDTO
                    WoBO woBO = new WoBO();
                    String code = "VNM_PMXL_1_";
                    Long woTypeId = woTypeDAO.getIdByCode("TMBT");
                    Long nextSq = woDAO.getNextSeqVal();
                    Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
                    code += woTypeId + "_" + countIdType + "_" + nextSq;
                    woBO.setWoCode(code);
                    woBO.setTrCode(trDTO.getTrCode());
                    woBO.setWoName("Thuê mặt bằng trạm");
                    WoNameDTO dtoName = new WoNameDTO();
                    dtoName.setWoTypeId(woTypeId);
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
                    cal.add(Calendar.MONTH, 1);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.add(Calendar.DATE, -1);
                    woBO.setFinishDate(cal.getTime());
                    woBO.setState(ASSIGN_CD);
                    woBO.setStatus(1l);
                    Long id = woDAO.saveObject(woBO);
                    // Worklog tu sinh wo

                    WoDTO woDto = woBO.toDTO();
                    woDto.setIsCreateNew(true);
                    logWoWorkLogs(woDto, "1", "Tự động sinh WO TMBT khi tạo TR.", gson.toJson(woBO),
                            dto.getLoggedInUser());

                    // Create checklist
                    WoMappingChecklistBO woMappingChecklistBO = new WoMappingChecklistBO();
                    woMappingChecklistBO.setWoId(id);
                    woMappingChecklistBO.setCheckListId(1l);
                    woMappingChecklistBO.setState("NEW");
                    woMappingChecklistBO.setStatus(1l);
//					woMappingChecklistBO.setName(lstNames.get(0).getName());
                    woMappingCheckListDAO.saveObject(woMappingChecklistBO);

                    // Generate station code and insert wo_mapping_station
//					List<WoMappingStationBO> lstSaves = new ArrayList<>();
//					List<CatStationBO> lstCatStations = new ArrayList<>();
                    // List cat station new
//					String[] lstStations = iCsr.getLstStations().split(",");
//					String[] lstStations = lstStation.split(",");

//					for (String iStationId : lstStations) {
//						WoMappingStationBO woMappingStationBO = new WoMappingStationBO();
//						woMappingStationBO.setWoId(id);
//						woMappingStationBO.setStatus(2l);
//						CatStationBO catStationBO = catStationDAO.getByStationId(Long.parseLong(iStationId));
//						if (catStationBO == null) {
//							woMappingStationBO.setReason("Mã trạm không tồn tại !");
//						} else {
//							woMappingStationBO.setCatStationId(catStationBO.getCatStationId());
//							catStationBO.setRentStatus(3l);
//							lstCatStations.add(catStationBO);
//						}
//						lstSaves.add(woMappingStationBO);
//					}
//					woMappingStationDAO.saveList(lstSaves);
//					catStationDAO.saveList(lstCatStations);

                    WoMappingStationBO woMappingStationBO = new WoMappingStationBO();
                    woMappingStationBO.setWoId(id);
                    woMappingStationBO.setStatus(2l);
                    CatStationBO catStationBO = catStationDAO.getByStationId(iCsr.getCatStationId());
                    if (catStationBO == null) {
                        woMappingStationBO.setReason("Mã trạm không tồn tại !");
                    } else {
                        woMappingStationBO.setCatStationId(catStationBO.getCatStationId());
                        catStationBO.setRentStatus(3l);
                    }
                    woMappingStationDAO.saveObject(woMappingStationBO);
                    catStationDAO.updateObject(catStationBO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//	taotq end 26042022

    //Huypq-23062022-start
    @Transactional
    public boolean createNewWOFromIOC(WoDTO woDto) throws Exception {
        Gson gson = new Gson();

        // Nếu chưa có loại hạng mục thì thêm mới loại hạng mục và hạng mục
        if (woDto.getCatWorkItemTypeName() != null) {
            CatWorkItemTypeDTO checkWorkItemType = woDAO.checkExistCatWorkItemType(woDto.getCatWorkItemTypeName());
            if (checkWorkItemType != null) {
                woDto.setCatWorkItemTypeId(checkWorkItemType.getCatWorkItemTypeId());
            } else {

                Long catWorkItemTypeId = constructionBusinessImpl.saveCatWorkItemType(woDto);

                woDto.setCatWorkItemTypeId(catWorkItemTypeId);
                woDAO.insertWorkItem(woDto);
            }
        }

        // add work item for construction
        if (woDto.getCatWorkItemTypeId() != null && woDto.getConstructionId() != null) {
            // Huypq10122021-start Check hợp đồng chia sẻ doanh thu
            if (!woDAO.checkExistConstructionWorkItem(woDto.getConstructionId(), woDto.getCatWorkItemTypeId())) {
                woDAO.insertWorkItem(woDto);
            }
            // Huy-end
        }

        if (checkSameWo(woDto) || checkConstructionDone(woDto) || checkWorkItemDone(woDto)
                || !checkStationConstruction(woDto))
            return false;

        // generate wo code
        if (StringUtils.isStringNullOrEmpty(woDto.getWoCode())) {
            woDto.setWoCode(generateWoCode(woDto));
        }

        if (woDto.getStationCode() != null) {
            if (woDto.getWoTypeCode() != null && !"HCQT".equalsIgnoreCase(woDto.getWoTypeCode())) {
                CatStationDTO station = woDAO.checkStationByCode(woDto.getStationCode());
                if (station == null) {
                    woDto.setCustomField("Mã trạm không tồn tại");
                    return false;
                }
            }
        }

        if (woDto.getCdLevel5() != null) {
            woDto.setType("3");
        }

        // nếu thiếu cd name sẽ điền thêm cd name
        tryGetCdNames(woDto);

        // create new wo
        woDto.setStatus(1l);
        woDto.setCreatedDate(new Date());
        Long newWoId = woDAO.saveObject(woDto.toModel());
        woDto.setWoId(newWoId);
        // log creation
        // logWoWorkLogs(woDto, "1", CREATE_NEW_MSG, gson.toJson(woDto),
        // woDto.getLoggedInUser());

        // log assignment if there's any

        List<WoXdddChecklistRequestDTO> lstXdddChecklist = woDto.getXdddChecklist();
        woDto.setXdddChecklist(new ArrayList<>());
        String content = "Thực hiện WO từ hệ thống IOC";
        logWoWorkLogs(woDto, "1", content, gson.toJson(woDto), woDto.getLoggedInUser());

        // create checklist
        woDto.setXdddChecklist(lstXdddChecklist);
        tryCreateXdddChecklistForNewWoFromIOC(woDto);
        woDto.setCustomField("Thành công. ");
        return true;
    }

    public void tryCreateXdddChecklistForNewWoFromIOC(WoDTO woDto) {
        if ("THICONG".equalsIgnoreCase(woDto.getWoTypeCode()) && woDto.getApWorkSrc() == XDDD_AP_WORK_SRC) {
            for (WoXdddChecklistRequestDTO dto : woDto.getXdddChecklist()) {
                dto.setWoId(woDto.getWoId());
                Long checklistId = woXdddChecklistDAO.saveObject(dto.toModel());
                if (dto.getLstAttach() != null && !dto.getLstAttach().isEmpty()) {
                    insertWoMappingAttachFromIOC(dto, checklistId, dto.getLstAttach());
                }
            }
        }
    }

    //Huy-end
    public Boolean checkRoleApproveCDLV5(HttpServletRequest request) {
        // TODO Auto-generated method stub
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.CD_LEVEL5, request)) {
            return false;
        }
        return true;
    }

    public void updateConstruction(Long constructionId) {
        // TODO Auto-generated method stub
        woDAO.updateConstruction(constructionId);
    }

    public void updateLicenceName(WoDTO woDto) {
        // TODO Auto-generated method stub
        woDAO.updateLicenceName(woDto);
    }

    private void insertWoMappingAttachFromIOC(WoXdddChecklistRequestDTO checklistDto, Long checklistId, List<ImgChecklistDTO> lstImgs) {
        if (lstImgs != null && lstImgs.size() > 0) {
            int imgIdx = 1;
            String dateString = new SimpleDateFormat("yyyymmdd_hhMMss").format(new Date());
            for (ImgChecklistDTO iImg : lstImgs) {
                WoMappingAttachBO woMappingAttachBO = new WoMappingAttachBO();
                woMappingAttachBO.setWoId(checklistDto.getWoId());
                woMappingAttachBO.setUserCreated(checklistDto.getUserCreated());
                woMappingAttachBO.setChecklistId(checklistId);

                String fileName = dateString + "_" + imgIdx + ".jpg";
                byte[] decodedBytes = com.itextpdf.text.pdf.codec.Base64.decode(iImg.getImgBase64());
                InputStream is = new ByteArrayInputStream(decodedBytes);
                String path = null;
                try {
                    path = UFile.writeToFileServerATTT2(is, fileName, UPLOAD_SUB_FOLDER, folderUpload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                woMappingAttachBO.setFileName(fileName);
                woMappingAttachBO.setFilePath(path);
                woMappingAttachBO.setStatus(1l);
                woMappingAttachBO.setLatitude(iImg.getLatitude());
                woMappingAttachBO.setLongtitude(iImg.getLongtitude());

                woMappingAttachDAO.save(woMappingAttachBO);

                imgIdx++;
            }
        }
    }

	public void updateWoMapingPlan(Long woId) {
		// TODO Auto-generated method stub
		woMappingAttachDAO.updateWoPlan(woId);
		woMappingAttachDAO.updateWoMapingPlan(woId);
	}

    public String isValidDeploymentDateReality(java.util.Date deploymentDateReality) throws Exception {
        String errMessage = "";
        if (ObjectUtils.isEmpty(deploymentDateReality)) {errMessage = "Vui lòng chọn ngày khởi công thực tế";}
        else {
            if (deploymentDateReality.after(new Date())) {errMessage = "Ngày khởi công thực tế không được phép chọn ngày tương lai";}
        }
        return errMessage;
    }
    
    public void updateContractByHandoverUseDateReality(WoDTO dto){
        CntContractBO cntContractBO = cntContractDAO.getContractByContractId(dto.getContractId());
        if (cntContractBO!=null){
            Date handoverDateReality = cntContractBO.getHandoverUseDateReality();
            Date dateDto = dto.getHandoverUseDateReality();
            if (handoverDateReality == null){
                cntContractBO.setHandoverUseDateReality(dateDto);
            }else {
                if (handoverDateReality.before(new Date())) {
                    if(dateDto.after(handoverDateReality) && dateDto.before(new Date())){
                        cntContractBO.setHandoverUseDateReality(dateDto);
                    }else if(dateDto.after(new Date())){
                        if((dateDto.getTime()+ handoverDateReality.getTime())/2<=new Date().getTime()){
                            cntContractBO.setHandoverUseDateReality(dateDto);
                        }
                    }
                }else if (handoverDateReality.after(new Date())){
                    if(dateDto.before(handoverDateReality) && dateDto.after(new Date())){
                        cntContractBO.setHandoverUseDateReality(dateDto);
                    }else if(dateDto.before(new Date()) && (dateDto.getTime()+ handoverDateReality.getTime())/2>new Date().getTime()){
                        cntContractBO.setHandoverUseDateReality(dateDto);
                    }
                }}
        }
        cntContractDAO.update(cntContractBO);
    }

    public List<AssetHandoverList> getDataAssetHandoverList(String stationCode) {
        List<AssetHandoverList> assetHandoverLists = new ArrayList<>();
        for (int i = 0; i <2 ; i++) {
            AssetHandoverList handoverList = new AssetHandoverList();
            handoverList.setStationId(Long.valueOf(i));
            handoverList.setGoodsName("Tên tài sản " + i);
            handoverList.setGoodsCode("Mã tài sản " + i);
            handoverList.setQuantity(" số lượng  " + i);
            handoverList.setPrice(" Đơn giá" + i);
            handoverList.setTotalPrice(" Tổng tiền " + i);
            handoverList.setGoodsStateName("Trạng thái " +i);
            handoverList.setProposedGoodsState(" Báo hỏng - mất");
            handoverList.setSysGroupName("Đơn vị" +i);
            handoverList.setStationCode( " mã trạm " +i);
            handoverList.setSysUserName(" người quản lý " +i);
            handoverList.setSysUserCode( " mã nhân viên " +i);
            handoverList.setSerial("serial" +i);
            assetHandoverLists.add(handoverList);
        }
        return assetHandoverLists;
    }

    public void updateAndWriteLogWo(WoBO bo, String loggedInUser, WoDTO woDTO) {
        try {
            bo.setState(REJECT_FT);
            bo.setFtId(null);
            bo.setFtName(null);
            bo.setFtEmail(null);
            woDAO.update(bo);

            WoDTO woDto = bo.toDTO();
            woDto.setText(convertBgbtsResult(woDTO.getBgbtsResult()));
            woDto.setRejectedFtId(null);
            String logContent = woDAO.getNameAppParam(REJECT_FT, "WO_XL_STATE");
            if (!org.apache.commons.lang3.StringUtils.isNotEmpty(woDto.getText())) {
                logContent += " - " + woDto.getText();
            }
            logWoWorkLogs(woDto, "1", logContent, gson.toJson(bo), loggedInUser);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateAndWriteLogWoBtsDHDT(WoDTO woDto, WoBO bo) {
        try {
            Long woTypeId = woTypeDAO.getIdByCode("BGBTS_DTHT");
            WoBO woBo = woDAO.findByWoIdAndWoTypeIdAndStatus(bo.getTrId(), woTypeId, Constant.STATUS.ACTIVE);
            woBo.setState(CD_NG);
            woBo.setBgbtsResult(woDto.getBgbtsResult());
            woDAO.updateObject(woBo);
            // cập nhật các đầu việc tương ứng thành new
            List<WoMappingChecklistBO> woMappingChecklistBOList = woMappingCheckListDAO.findByWoIdEntity(woBo.getWoId());
            if(Constant.BGBTS_RESULT.MISSING_PHOTO.equals(woDto.getBgbtsResult())) {
                if (!CollectionUtils.isEmpty(woDto.getListChecklistId())) {
                    if (!CollectionUtils.isEmpty(woMappingChecklistBOList)) {
                        for (WoMappingChecklistBO woMappingChecklistBO : woMappingChecklistBOList) {
                            if (woDto.getListChecklistId().contains(woMappingChecklistBO.getCheckListId())) {
                                woMappingChecklistBO.setState(NEW);
                                woMappingChecklistDAO.updateObject(woMappingChecklistBO);
                            }
                        }
                    }
                }
            }else {
                updateWoMappingCheckList(woBo.getWoId());
            }

            woDto = woBo.toDTO();
            woDto.setText(convertBgbtsResult(woDto.getBgbtsResult()));
            woDto.setRejectedFtId(null);
            String logContent = woDAO.getNameAppParam(CD_NG, "WO_XL_STATE");
            if (!org.apache.commons.lang3.StringUtils.isNotEmpty(woDto.getText())) {
                logContent += " - " + woDto.getText();
            }
            logWoWorkLogs(woDto, "1", logContent, gson.toJson(bo), woDto.getLoggedInUser());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String convertBgbtsResult(String bgbtsResult) {
        String text = "";
        if(Constant.BGBTS_RESULT.MISSING_PHOTO.equals(bgbtsResult)){
            text = "Thiếu ảnh";
        }else if(Constant.BGBTS_RESULT.FAILED_CONSTRUCTION.equals(bgbtsResult)){
            text = "Thi công chưa đạt";
        }
        return text;
    }
}
