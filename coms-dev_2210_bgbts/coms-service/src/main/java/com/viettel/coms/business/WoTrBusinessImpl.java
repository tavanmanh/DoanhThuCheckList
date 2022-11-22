package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espringtran.compressor4j.bean.CompressionLevel;
import com.espringtran.compressor4j.bean.CompressionType;
import com.espringtran.compressor4j.bean.ZipLevel;
import com.espringtran.compressor4j.compressor.FileCompressor;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.cat.dao.CatProvinceDAO;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.CatStationBO;
import com.viettel.coms.bo.TrWorkLogsBO;
import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoMappingAttachBO;
import com.viettel.coms.bo.WoMappingChecklistBO;
import com.viettel.coms.bo.WoMappingStationBO;
import com.viettel.coms.bo.WoTrBO;
import com.viettel.coms.bo.WoTrMappingStationBO;
import com.viettel.coms.dao.CatStationDAO;
import com.viettel.coms.dao.SysGroupDAO;
import com.viettel.coms.dao.TrWorkLogsDAO;
import com.viettel.coms.dao.WoConfigContractDAO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dao.WoMappingAttachDAO;
import com.viettel.coms.dao.WoTrDAO;
import com.viettel.coms.dao.WoTrMappingStationDAO;
import com.viettel.coms.dao.WoTrTypeDAO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.CatStationDetailDTO;
import com.viettel.coms.dto.CatStationRentDTO;
import com.viettel.coms.dto.CatWorkItemTypeDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.dto.WoAppParamDTO;
import com.viettel.coms.dto.WoCatWorkItemTypeDTO;
import com.viettel.coms.dto.WoConfigContractCommitteeDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoMappingAttachDTO;
import com.viettel.coms.dto.WoNameDTO;
import com.viettel.coms.dto.WoSimpleConstructionDTO;
import com.viettel.coms.dto.WoSimpleStationDTO;
import com.viettel.coms.dto.WoSimpleSysGroupDTO;
import com.viettel.coms.dto.WoSimpleSysUserDTO;
import com.viettel.coms.dto.WoTrDTO;
import com.viettel.coms.dto.WoTrMappingStationDTO;
import com.viettel.coms.dto.WoTrTypeDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;

@Service("woTrBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class WoTrBusinessImpl implements WoTrBusiness {

    private final String CREATE_NEW = "CREATE_NEW";
    private final String UPDATED = "UPDATED";
    private final String DELETED = "DELETED";
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
    private final String NG = "NG";
    private final String OPINION_RQ = "OPINION_RQ";
    private final String CD_LEVEL_1 = "CD_LEVEL_1";

    private final String TR_XL_MSG_SUBJECT = "Thông báo công việc Task Request";

    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
    @Value("${allow.file.ext}")
    private String allowFileExt;
    @Value("${folder_upload2}")
    private String folderUpload;


    private static final CompressionType TYPE = CompressionType.ZIP;
    private static final CompressionLevel LEVEL = ZipLevel.NORMAL;

    Gson gson = new Gson();

    @Autowired
    WoTrDAO trDAO;

    @Autowired
    WoTrTypeDAO woTrTypeDAO;

    @Autowired
    TrWorkLogsDAO trWorkLogsDAO;

    @Autowired
    WoDAO woDAO;

    @Autowired
    WoMappingAttachDAO woMappingAttachDAO;

    @Autowired
    WoConfigContractDAO woConfigContractDAO;

    @Autowired
    WoBusinessImpl woBusiness;

    @Autowired
    CatStationDAO catStationDAO;

    @Autowired
    WoTrMappingStationDAO woTrMappingStationDAO;

    @Autowired
    CatProvinceDAO catProvinceDAO;
    
    @Autowired
    SysGroupDAO sysGroupDAO;
    
    @Override
    public boolean createTR(WoTrDTO trDto, WoSimpleSysGroupDTO creatorDomainGroup, boolean isImporting) throws Exception {

        try {
        	
            if (StringUtils.isNotEmpty(trDto.getCdLevel1())) {
            	if(trDto.getTrTypeCode() != null && trDto.getTrTypeCode().equals("TR_THUE_MAT_BANG_TRAM")) {
            		trDto.setState("ACCEPT_CD");
            	}else {
            		trDto.setState(ASSIGN_CD);
            	}
            } else {
                trDto.setState(UNASSIGN);
            }

            if ("TR_DONG_BO_HA_TANG".equalsIgnoreCase(trDto.getTrTypeCode())) {
                // TR dong bo ha tang, neu ma tram chua co wo tmbt thi ko duoc tao
                boolean isCanCreateTrDbht = trDAO.checkCanCreateTrDbht(trDto.getStationCode());
                if (!isCanCreateTrDbht) {
                    trDto.setCustomField("Mã trạm chưa có hoặc chưa hoàn thành WO thuê mặt bằng trạm !");
                    return false;
                }

                // Cong trinh hoan thanh roi thi ko duoc tao
                WoSimpleConstructionDTO constructionDTO = trDAO.getConstructionById(trDto.getConstructionId());
                if (constructionDTO != null && constructionDTO.getStatus() >= 3) {
                    trDto.setCustomField("Công trình đã thực hiện nên không thể tạo TR !");
                    return false;
                }
            }

            String generatedTRCode = generateTRCode(trDto, creatorDomainGroup);

            trDto.setTrCode(generatedTRCode);
            trDto.setCreatedDate(new Date());

            if (StringUtils.isNotEmpty(trDto.getContractCode())) {
                Long contractId = trDAO.getContractIdByCode(trDto.getContractCode());
                trDto.setContractId(contractId);
            }

            //import: lấy mã trạm, long, lat theo mã công trình
            if (StringUtils.isNotEmpty(trDto.getConstructionCode()) && StringUtils.isEmpty(trDto.getStationCode())) {
                WoSimpleStationDTO station = trDAO.tryGetStation(trDto.getConstructionCode());
                if (station != null) {
                    trDto.setStationCode(station.getStationCode());
                    trDto.setExecuteLat(station.getLatitude());
                    trDto.setExecuteLong(station.getLongitude());
                }
            }

            //kiểm tra 1 loại tr 1 công trình 1 hợp đồng (hoặc dự án) chỉ tạo TR 1 lần
            WoTrDTO checkDto = new WoTrDTO();
            checkDto.setTrTypeId(trDto.getTrTypeId());
            checkDto.setContractCode(trDto.getContractCode());
            checkDto.setProjectCode(trDto.getProjectCode());
            checkDto.setConstructionCode(trDto.getConstructionCode());
            List<WoTrDTO> listCheck = trDAO.doSearch(checkDto, null, "VALIDATE", false, false);
            if ("TR_THUE_MAT_BANG_TRAM".equalsIgnoreCase(trDto.getTrTypeCode()) || "TR_DONG_BO_HA_TANG".equalsIgnoreCase(trDto.getTrTypeCode())) {
                for (WoTrDTO woTrDTO : listCheck) {
                    if (trDto.getTrName().equalsIgnoreCase(woTrDTO.getTrName())) {
                        trDto.setCustomField("Tên TR đã tồn tại !");
                        return false;
                    }
                }
            } else if (listCheck.size() > 0) {
                trDto.setCustomField("Đã tồn tại TR tương ứng! 1 loại TR 1 công trình 1 hợp đồng (hoặc dự án) chỉ tạo TR 1 lần. ");
                return false;
            }

            long trId = trDAO.saveObject(trDto.toModel());
            trDto.setTrId(trId);
            if (StringUtils.isNotEmpty(trDto.getCdLevel1())) {
                trDto.setState(ASSIGN_CD);
                logTrWorkLogs(trDto, "1", "Tạo mới TR. Trạng thái: Chờ CD tiếp nhận. ", gson.toJson(trDto.toModel()), trDto.getLoggedInUser(), isImporting);
            }
            trDto.setCustomField("Thành công. ");

            // Tự tạo WO cho tr giao từ tt xd dt ht
            if (trDto.getCatWorkItemTypeList() != null) tryAutoCreateWo(trDto);

            // Tu dong tao cac WO cho TR đồng bộ hạ tầng
            //Huypq-29102021-start comment luồng cũ
            if ("TR_DONG_BO_HA_TANG".equalsIgnoreCase(trDto.getTrTypeCode())) {
                woBusiness.createNewDbhtWO(trDto);
            }
            //Huy-end
            // Nếu có chọn trạm cho CNKT thi insert vào bảng WO_TR_MAPPING_STATION
            if ("TR_THUE_MAT_BANG_TRAM".equalsIgnoreCase(trDto.getTrTypeCode())) {
            	WoTrDTO trDetail = trDto;
            	trDetail.setTrId(trId);
            	woBusiness.createNewTmbtWONew(trDetail);
                if (StringUtils.isNotEmpty(trDto.getTmbtTargetDetail())) {
                    // Parse string to CatStationRentDTO
                    List<CatStationRentDTO> lstCatStationRents = gson.fromJson(trDto.getTmbtTargetDetail(), new TypeToken<ArrayList<CatStationRentDTO>>() {
                    }.getType());

                    for (CatStationRentDTO iCsr : lstCatStationRents) {
                        Long sysGroupId = iCsr.getSysGroupId();
                        WoSimpleSysGroupDTO sysGroupDTO = woDAO.getSysGroup("" + sysGroupId);
                        // Generate station code and insert wo_mapping_station
                        List<CatStationBO> lstCatStations = new ArrayList<>();
                        // List cat station new
                        List<CatStationRentDTO> lstCatStationRentDTOS = trDAO.getRentStationNew(sysGroupDTO.getProvinceId());
                        List<WoTrMappingStationBO> lstSaves = new ArrayList<>();
                        List<String> lstStations = lstCatStationRentDTOS.stream()
                                .map(CatStationRentDTO::getCode)
                                .collect(Collectors.toList());
                        for (int i = 0; i < iCsr.getRentTarget().intValue(); i++) {
                            WoTrMappingStationBO woTrMappingStationBO = new WoTrMappingStationBO();
                            woTrMappingStationBO.setTrId(trId);
                            woTrMappingStationBO.setSysGroupId(sysGroupId);
                            CatStationBO catStationBO = catStationDAO.getByStationCode(lstStations.get(i));
                            if (catStationBO != null) {
                                // Add to list save
                                woTrMappingStationBO.setCatStationId(catStationBO.getCatStationId());
                                woTrMappingStationBO.setStatus(1l);
                                lstSaves.add(woTrMappingStationBO);
                                // Add catStation to list update
                                catStationBO.setRentStatus(3l);
                                lstCatStations.add(catStationBO);
                            }
                        }
                        woTrMappingStationDAO.saveList(lstSaves);
                        catStationDAO.saveList(lstCatStations);
                    }
                } else {
                    List<CatStationBO> lstCatStations = new ArrayList<>();
                    List<WoTrMappingStationBO> lstSaves = new ArrayList<>();
                    for (CatStationDetailDTO iCsr : trDto.getSelectedStations()) {
                        WoTrMappingStationBO woTrMappingStationBO = new WoTrMappingStationBO();
                        woTrMappingStationBO.setTrId(trId);
                        woTrMappingStationBO.setSysGroupId(iCsr.getTmbtSysGroupId());
                        woTrMappingStationBO.setCatStationId(iCsr.getCatStationId());
                        woTrMappingStationBO.setStatus(1l);
                        lstSaves.add(woTrMappingStationBO);

                        CatStationBO catStationBO = catStationDAO.getOneRaw(iCsr.getCatStationId());
                        catStationBO.setRentStatus(3l);
                        lstCatStations.add(catStationBO);
                    }
                    woTrMappingStationDAO.saveList(lstSaves);
                    catStationDAO.saveList(lstCatStations);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String updateTR(WoTrDTO trDto) throws Exception {
        String result = trDAO.update(trDto.toModel());
        logTrWorkLogs(trDto, "1", UPDATED, gson.toJson(trDto.toModel()), trDto.getLoggedInUser(), false);
        return result;
    }

    @Override
    public int deleteTR(WoTrDTO trDto) throws Exception {
        boolean checkDeletable = trDAO.checkDeletable(trDto.getTrId());

        if (!checkDeletable) return -1;

        int result = trDAO.delete(trDto.getTrId());
        logTrWorkLogs(trDto, "1", DELETED, gson.toJson(trDAO.getOneRaw(trDto.getTrId())), trDto.getLoggedInUser(), false);

        return result;
    }

    public boolean giveAssignmentToCD(WoTrDTO trDto) {
        long trId = trDto.getTrId();

        String loggedInUser = trDto.getLoggedInUser();
        String assignedCd = trDto.getAssignedCd();

        WoTrBO bo = trDAO.getOneRaw(trId);

        if (StringUtils.isNotEmpty(bo.getCdLevel1()) && bo.getCdLevel1() != assignedCd) {
            String oldTrCode = bo.getTrCode();
            String newTrCode = reGenerateTRCode(oldTrCode, assignedCd);
            bo.setTrCode(newTrCode);
            tryReAssignWo(trId, assignedCd);
        }

        bo.setCdLevel1(assignedCd);
        bo.setState(ASSIGN_CD);

        trDAO.update(bo);

        // worklogs
        try {
            logTrWorkLogs(bo.toDTO(), "1", trDto.getState(), gson.toJson(bo), loggedInUser, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean cdReject(WoTrDTO trDto) {
        long trId = trDto.getTrId();

        WoTrBO bo = trDAO.getOneRaw(trId);

        bo.setCdLevel1(null);
        bo.setState(REJECT_CD);
        trDAO.update(bo);

        // worklogs
        try {
            trDto.setState(REJECT_CD);
            logTrWorkLogs(trDto, "1", trDto.getState(), gson.toJson(bo), trDto.getLoggedInUser(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean cdAccept(WoTrDTO trDto) {
        long trId = trDto.getTrId();

        WoTrBO bo = trDAO.getOneRaw(trId);

        bo.setState(ACCEPT_CD);
        trDAO.update(bo);

        // worklogs
        try {
            logTrWorkLogs(trDto, "1", ACCEPT_CD, gson.toJson(bo), trDto.getLoggedInUser(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional
    public String generateTRCode(WoTrDTO trDto, WoSimpleSysGroupDTO creatorDomainGroup) {
        String code = "VNM_";
        String loggedInUser = trDto.getLoggedInUser();

        if (creatorDomainGroup == null) creatorDomainGroup = trDAO.getSysGroupById(trDto.getCreateTrDomainGroupId());
        String sysGroupCode = creatorDomainGroup.getCode();

        Long nextSequence = trDAO.getNextValSequence("WO_TR_SEQ") + 1;

        String assignedCd = trDto.getAssignedCd();
        String coopCode = assignedCd.split("-")[0].trim();

        Long trTypeId = trDto.getTrTypeId();
        String trTypeOrder = "00";
        String numTrInAType = "00";
        if (trTypeId != null) {
            trTypeOrder = String.valueOf(trTypeId);
            long countTrInAType = trDAO.countTrInAType(trTypeId);
            numTrInAType = String.valueOf(countTrInAType + 1);
        }

        code += sysGroupCode + "-" + coopCode + "_PMXL_" + trTypeOrder + "_" + numTrInAType + "_" + nextSequence;

        return code;
    }

    @Override
    public File createImportTrExcelTemplate(boolean isCNKT, String cnktGroupId) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_CreateTr.xlsx";
        String outFileName = "Template_WO_XL_CreateTr.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 100)) {
                SXSSFSheet sheet = workbook.getSheetAt(1);

                int rowNumber = 0;
                List<WoTrTypeDTO> trTypes = woTrTypeDAO.getListTRType();

                for (WoTrTypeDTO trType : trTypes) {
                    writeBook(trType, sheet, ++rowNumber);
                }

                List<WoAppParamDTO> cdLv1s = new ArrayList<>();
                //nếu tạo từ CNKT thì hard code cd level 1 là TTHT
                if (isCNKT) {
                    WoAppParamDTO cdLv1 = new WoAppParamDTO();
                    cdLv1.setCode("242656");
                    cdLv1.setName("Trung tâm hạ tầng");
                    cdLv1s.add(cdLv1);
                } else {
                    //không thì lấy trong app_parram
                    cdLv1s = woDAO.getAppParam(CD_LEVEL_1);
                }

                int rowCd = 0;
                SXSSFSheet cd1Sheet = workbook.getSheetAt(2);
                for (WoAppParamDTO cd : cdLv1s) {
                    writeCd(cd, cd1Sheet, ++rowCd);
                }

                //nếu tạo từ CNKT thì có thêm cdlv2 là đơn vị trong miền dữ liệu tạo TR
                if (isCNKT) {
                    WoSimpleSysGroupDTO cdLevel2 = trDAO.getSysGroupById(Long.parseLong(cnktGroupId));
                    SXSSFSheet cd2Sheet = workbook.getSheetAt(3);
                    Row row = cd2Sheet.createRow(1);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(cdLevel2.getSysGroupId());
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(cdLevel2.getCode());
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(cdLevel2.getGroupName());
                }

                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }

        return outFile;
    }

    @Override
    public String changeStateTr(WoTrDTO dto) throws Exception {
        WoTrBO trBo = trDAO.getOneRaw(dto.getTrId());
        trBo.setState(dto.getState());

        String logContent = "";
        if (REJECT_CD.equalsIgnoreCase(dto.getState())) {
//            if (StringUtils.isEmpty(trBo.getCdLevel2())) {
            	trBo.setCdLevel1(null);
            	trBo.setCdLevel1Name(null);
            	trBo.setCdLevel2(null);
            	trBo.setCdLevel2Name(null);
//            }
            logContent = "Từ chối TR. Lý do: " + dto.getText();
        } else if (ACCEPT_CD.equalsIgnoreCase(dto.getState())) {
            trBo.setUserReceiveTr(dto.getLoggedInUser());
            trBo.setUpdateReceiveTr(new Date());
            logContent = "Tiếp nhận TR. ";
            tryEnableAutoGeneratedWo(dto);
        }

        String returnStr = trDAO.update(trBo);
        logTrWorkLogs(dto, "1", logContent, gson.toJson(trBo), dto.getLoggedInUser(), false);
        return returnStr;
    }

    private void writeBookHeader(Sheet sheet, int rowNum, CellStyle cellStyleHeader, CellStyle cellStyleInfo) {
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(rowNum);
        cell.setCellStyle(cellStyleInfo);
        cell.setCellValue("Loại TR");
    }

    private void writeBook(WoTrTypeDTO record, Sheet sheet, int rowNum) {
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(0);
        cell.setCellValue(record.getWoTrTypeId());
        Cell cell1 = row.createCell(1);
        cell1.setCellValue(record.getTrTypeName());
    }

    private void writeCd(WoAppParamDTO record, Sheet sheet, int rowNum) {
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(0);
        cell.setCellValue(record.getCode());
        Cell cell1 = row.createCell(1);
        cell1.setCellValue(record.getName());

    }

    @Transactional
    @Override
    public void logTrWorkLogs(WoTrDTO dto, String logType, String content, String contentDetail, String loggedInUser, boolean isImporting) throws Exception {
        TrWorkLogsBO workLogs = new TrWorkLogsBO();
        workLogs.setTrId(dto.getTrId());
        workLogs.setContent(content);
        workLogs.setContentDetail(contentDetail);
        workLogs.setLogTime(new Date());
        workLogs.setLogType(logType);
        workLogs.setStatus(1);

        WoSimpleSysUserDTO sysUser = trDAO.getSysUser(loggedInUser);
        WoSimpleSysGroupDTO sysGroup = trDAO.getSysUserGroup(loggedInUser);
        String loggedInUserInfo = "";
        if(sysUser != null) {
        	loggedInUserInfo = sysUser.getFullName() + " - " + sysUser.getEmployeeCode() + " - ";
        }
        if (sysGroup.getGroupLevel() == 1) loggedInUserInfo += sysGroup.getGroupNameLevel1();
        if (sysGroup.getGroupLevel() == 2) loggedInUserInfo += sysGroup.getGroupNameLevel2();
        if (sysGroup.getGroupLevel() == 3) loggedInUserInfo += sysGroup.getGroupNameLevel3();
        workLogs.setUserCreated(loggedInUserInfo);

        // Write worklogs
        trWorkLogsDAO.saveObject(workLogs);

        if (!isImporting) sendMocha(dto);
    }

    private void sendMocha(WoTrDTO dto) {
        if (ASSIGN_CD.equalsIgnoreCase(dto.getState())) {
            //thêm nội dung và hạn
            String content = "Đơn vị vừa được giao Task Request số " + dto.getTrCode() + ".";
            content += " Nội dung: " + dto.getTrName() + ".";
            content += " Hạn hoàn thành: " + dto.getFinishDate() + "!";

//            WoSimpleSysGroupDTO assignedGroup = woDAO.getSysGroup(dto.getAssignedCd());
            WoSimpleSysGroupDTO assignedGroup;
            if (StringUtils.isNotEmpty(dto.getCdLevel2())) assignedGroup = woDAO.getSysGroup(dto.getCdLevel2());
            else assignedGroup = woDAO.getSysGroup(dto.getCdLevel1());

            sendSmsToGroup(assignedGroup, content);
        }
        if (REJECT_CD.equalsIgnoreCase(dto.getState()))
            woDAO.createSendSmsEmailForMobile("Đơn vị được giao Task Request số " + dto.getTrCode() + " vừa từ chối thực hiện. Đề nghị đồng chí vào kiểm tra!", TR_XL_MSG_SUBJECT, trDAO.getSysUserId(dto.getUserCreated()), null);
    }

    @Override
    public void sendSmsToGroup(WoSimpleSysGroupDTO assignedGroup, String content) {
        if (assignedGroup.getGroupLevel() > 2) {
            woDAO.createSendSmsEmailForMobile(content, TR_XL_MSG_SUBJECT, null, assignedGroup.getSysGroupId());
        } else {
            List<WoSimpleSysGroupDTO> childrenGroup = woDAO.getChildrenGroup(assignedGroup.getSysGroupId().toString());
            for (WoSimpleSysGroupDTO group : childrenGroup) {
                woDAO.createSendSmsEmailForMobile(content, TR_XL_MSG_SUBJECT, null, group.getSysGroupId());
            }
        }
    }

    public File importTrTypeExcelTemplate() throws IOException {
        String fileName = "WO_XL_CreateTrType.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        File templateFile = new File(filePath + fileName);

        return templateFile;
    }

    public File exportExcelTrList(List<WoTrDTO> trs) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_TrList.xlsx";
        String outFileName = "Template_WO_XL_TrList.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        List<WoAppParamDTO> stateList = woDAO.getAppParam("WO_TR_XL_STATE");
        Map<String, String> stateMap = new HashMap<>();
        for (WoAppParamDTO item : stateList) {
            stateMap.put(item.getCode(), item.getName());
        }

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 100)) {
                SXSSFSheet sheet = workbook.getSheetAt(0);

                int rowNum = 0;
                for (WoTrDTO tr : trs) {
                    Row row = sheet.createRow(++rowNum);
                    row.createCell(0).setCellValue(rowNum);
                    if (tr.getTrName() != null) row.createCell(1).setCellValue(tr.getTrName());
                    if (tr.getTrCode() != null) row.createCell(2).setCellValue(tr.getTrCode());
                    if (tr.getTrTypeName() != null) row.createCell(3).setCellValue(tr.getTrTypeName());
                    if (tr.getUserCreatedFullName() != null)
                        row.createCell(4).setCellValue(tr.getUserCreatedFullName());
                    if (tr.getProjectCode() != null) row.createCell(5).setCellValue(tr.getProjectCode());
                    if (tr.getContractCode() != null) row.createCell(6).setCellValue(tr.getContractCode());
                    if (tr.getConstructionCode() != null) row.createCell(7).setCellValue(tr.getConstructionCode());
                    if (tr.getStationCode() != null) row.createCell(8).setCellValue(tr.getStationCode());
                    if (tr.getExecuteLat() != null) row.createCell(9).setCellValue(tr.getExecuteLat());
                    if (tr.getExecuteLong() != null) row.createCell(10).setCellValue(tr.getExecuteLong());
                    if (tr.getQoutaTime() != null) row.createCell(11).setCellValue(tr.getQoutaTime());
                    if (tr.getFinishDate() != null)
                        row.createCell(12).setCellValue(dateFormat.format(tr.getFinishDate()));
                    if (tr.getWoCode() != null) row.createCell(13).setCellValue(tr.getWoCode());
                    if (tr.getWoTypeName() != null) row.createCell(14).setCellValue(tr.getWoTypeName());
                    if (tr.getQuantityValue() != null) row.createCell(15).setCellValue(tr.getQuantityValue());
                    if (tr.getState() != null) row.createCell(16).setCellValue(stateMap.get(tr.getState()));
                }

                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }

        return outFile;
    }


    public File exportExcelTrTypeList(List<WoTrTypeDTO> types) throws IOException {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_TrTypeList.xlsx";
        String outFileName = "Template_WO_XL_TrTypeList.xlsx";

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        templateFile = new File(filePath + fileName);
        outFile = new File(filePath + outFileName);

        try (FileInputStream inputStream = new FileInputStream(templateFile)) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream), 100)) {
                SXSSFSheet sheet = workbook.getSheetAt(0);

                int rowNum = 0;
                for (WoTrTypeDTO type : types) {
                    Row row = sheet.createRow(++rowNum);
                    row.createCell(0).setCellValue(rowNum);
                    row.createCell(1).setCellValue(type.getTrTypeName());
                    row.createCell(2).setCellValue(type.getTrTypeCode());
                }

                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                }
            }
        }

        return outFile;
    }

    private String reGenerateTRCode(String oldCode, String assignedUnitId) {
        String newCode = "";
        String[] parts = oldCode.split("_");
        String coopCode = parts[1];
        String[] coopParts = coopCode.split("-");
        WoSimpleSysGroupDTO assignedGroup = trDAO.getSysGroupById(Long.valueOf(assignedUnitId));
        coopParts[1] = assignedGroup.getCode();
        coopCode = coopParts[0] + "-" + coopParts[1];
        parts[1] = coopCode;
        for (String str : parts) {
            newCode += str + "_";
        }
        newCode = newCode.substring(0, newCode.length() - 1);
        return newCode;
    }

    @Override
    public boolean validateImportData(List<WoTrDTO> trs, boolean isCNKT) throws Exception {
    	boolean result = true;
    	try {
	        List<String> constructionCodes = new ArrayList<>();
	        List<String> constructionContract = new ArrayList<>();
	        List<String> constructionPro = new ArrayList<>();
	        List<String> lstStationCode = new ArrayList<>(); //Huypq-12072021-add
	        for (WoTrDTO tr : trs) {
	            if (StringUtils.isNotEmpty(tr.getConstructionCode()) && !constructionCodes.contains(tr.getConstructionCode())) {
	                constructionCodes.add(tr.getConstructionCode());
	            }
	            if(tr.getStationCode()!=null) {
	            	lstStationCode.add(tr.getStationCode()); //Huypq-12072021-add
	            }
	        }
	        List<String> listkey = trDAO.getConstructionKey(constructionCodes);
	
	        List<String> projectConstructions = trDAO.getConstructionMappingWithProject(constructionCodes);
	        List<String> contractConstructions = trDAO.getConstructionMappingWithContract(constructionCodes);
	        Map<String, Long> lstStation = new HashMap<>();
	        if(!lstStationCode.isEmpty()) {
	        	lstStation = catStationDAO.getWorkItemConfigByStationId(lstStationCode); //Huypq-12072021-add
	        }
	        //prepare construction map
	        Map<String, WoSimpleConstructionDTO> constructionMap = new HashMap<>();
	        List<WoSimpleConstructionDTO> constructionList = trDAO.getConstructionByCodeRange(constructionCodes);
	        for (WoSimpleConstructionDTO construction : constructionList)
	            constructionMap.put(construction.getConstructionCode(), construction);
	
	        for (WoTrDTO tr : trs) {
	            String errorStr = "";
	            if (StringUtils.isEmpty(tr.getTrName())) {
	                result = false;
	                errorStr += "\"Tên TR\" không được để trống; ";
	            }
	
	            if (tr.getTrTypeId() == null) {
	                result = false;
	                errorStr += "\"Loại TR\" không hợp lệ; ";
	            }
	
	            if (StringUtils.isEmpty(tr.getContractCode()) && StringUtils.isEmpty(tr.getProjectCode())) {
	                result = false;
	                errorStr += "Phải có mã hợp đồng hoặc mã dự án; ";
	            }
	
	            if (StringUtils.isEmpty(tr.getConstructionCode())) {
	                result = false;
	                errorStr += "Mã công trình không được để trống; ";
	            } else {
	                WoSimpleConstructionDTO construction = constructionMap.get(tr.getConstructionCode());
	                if (construction == null) {
	                    result = false;
	                    errorStr += "Công trình không tồn tại hoặc đã hoàn thành; ";
	                } else {
	                    tr.setConstructionId(construction.getConstructionId());
	                    tr.setStationCode(construction.getStationCode());
	                    tr.setExecuteLat(construction.getLatitude());
	                    tr.setExecuteLong(construction.getLongitude());
	                }
	            }
	
	            if (tr.getQoutaTime() == null) {
	                result = false;
	                errorStr += "\"Định mức thời gian(Số giờ)\" không được để trống; ";
	            }
	            if (tr.getFinishDate() == null) {
	                result = false;
	                errorStr += "\"Hạn hoàn thành\" không được để trống; ";
	            }
	
	            if (StringUtils.isEmpty(tr.getCdLevel1())) {
	                result = false;
	                errorStr += "\"Đơn vị điều phối cấp 1\" không hợp lệ; ";
	            }
	
	            if (StringUtils.isNotEmpty(tr.getProjectCode()) && StringUtils.isNotEmpty(tr.getConstructionCode())) {
	                String check = tr.getProjectCode() + "-" + tr.getConstructionCode();
	                boolean isConsInProject = projectConstructions.contains(check);
	                String checkPlusTrType = tr.getTrTypeId() + "-" + check;
	                boolean ischeck = listkey.contains(checkPlusTrType);
	                if (!isConsInProject) {
	                    result = false;
	                    errorStr += "Công trình không thuộc dự án; ";
	                }
	                if (ischeck) {
	                    result = false;
	                    errorStr += "Công trình và dự án đã tạo TR cùng loại; ";
	                }
	                if (constructionPro.contains(check)) {
	                    result = false;
	                    errorStr += "Mã công trình đã tồn tại ";
	                } else {
	                    constructionPro.add(tr.getProjectCode() + "-" + tr.getConstructionCode());
	                }
	            }
	
	            if (StringUtils.isNotEmpty(tr.getContractCode()) && StringUtils.isNotEmpty(tr.getConstructionCode())) {
	                String check = tr.getContractCode() + "-" + tr.getConstructionCode();
	                boolean isConsInContract = contractConstructions.contains(check);
	                String checkPlusTrType = tr.getTrTypeId() + "-" + check;
	                boolean ischeck = listkey.contains(checkPlusTrType);
	                if (!isConsInContract) {
	                    result = false;
	                    errorStr += "Công trình không thuộc hợp đồng; ";
	                }
	                if (ischeck) {
	                    result = false;
	                    errorStr += "Công trình và hợp đồng đã tạo TR cùng loại; ";
	                }
	                if (constructionContract.contains(check)) {
	                    result = false;
	                    errorStr += "\"Mã công trình\" đã tồn tại ";
	                } else {
	                    constructionContract.add(tr.getContractCode() + "-" + tr.getConstructionCode());
	                }
	            }
	            
				// Huypq-09072021-add
	            if(tr.getCheckDBHT()!=null && tr.getCheckDBHT().equals("1")) {
	            	if (tr.getDbTkdaDate() == null) {
	    				result = false;
	    				errorStr += "Ngày Đảm bảo thiết kế dự án không được để trống; ";
	    			}
	
	    			if (tr.getDbTtkdtDate() == null) {
	    				result = false;
	    				errorStr += "Ngày Đảm bảo thẩm thiết kế dự toán không được để trống; ";
	    			}
	
	    			if (tr.getDbVtDate() == null) {
	    				result = false;
	    				errorStr += "Ngày Đảm bảo vật tư không được để trống; ";
	    			}
	
	    			if (!StringUtils.isNotBlank(tr.getCdLevel2())) {
	    				result = false;
	    				errorStr += "Trạm chưa được cấu hình tỉnh; ";
	    			}
	
	    			if (lstStation.get(tr.getStationCode()) == null) {
	    				result = false;
	    				errorStr += "Trạm chưa được gán hạng mục; ";
	    			}
	            }
				// Huypq-09072021-end
	//            if (isCNKT && StringUtils.isEmpty(tr.getCdLevel2())) {
	//                result = false;
	//                errorStr += "\"Đơn vị điều phối cấp 2\" không được để trống; ";
	//            }
	
	//            if (StringUtils.isEmpty(tr.getCatWorkItemTypeListString())) {
	//                result = false;
	//                errorStr += "\"Hạng mục tạo WO tự động (TTXDDTHT)\" không được để trống;";
	//            } else {
	//                Map<String, Long> mapCatWorkItemType = new HashMap<>();
	//                String[] catWorkItemStrArray = tr.getCatWorkItemTypeListString().split("\n");
	//                if (tr.getConstructionId() != null) {
	//                    mapCatWorkItemType = trDAO.getCatWorkItemTypeId(tr.getConstructionId());
	//                }
	//                Map<String, String> mapWorkItiem = new HashMap<>();
	//                for (String catWorkItemStr : catWorkItemStrArray) {
	//                    String[] workItemProperties = catWorkItemStr.split("\\|");
	//                    if (workItemProperties.length == 5) {
	//                        String woCode = workItemProperties[0].trim() + "-" + tr.getConstructionId();
	//                        if (StringUtils.isEmpty(workItemProperties[0].trim())) {
	//                            result = false;
	//                            errorStr += "\"Hạng mục tạo WO tự động (TTXDDTHT)\" không được để trống;";
	//                        } else {
	//                            if (mapWorkItiem.get(woCode) != null && mapWorkItiem.size() > 0) {
	//                                result = false;
	//                                errorStr += "Mã hạng mục " + workItemProperties[0].trim() + " không được phép trùng;";
	//                            }
	//                            if (mapCatWorkItemType.size() > 0 && mapCatWorkItemType.get(woCode) == null) {
	//                                result = false;
	//                                errorStr += "Mã hạng mục " + workItemProperties[0].trim() + " không tồn tại hoặc không thuộc công trình;";
	//                            }
	//                        }
	//                        mapWorkItiem.put(woCode, workItemProperties[0].trim());
	//
	//                        if (StringUtils.isEmpty(workItemProperties[3].trim())) {
	//                            result = false;
	//                            errorStr += "Giá trị không được bỏ trống; ";
	//                        } else {
	//                            if (!StringUtils.isNumeric(workItemProperties[3].trim())) {
	//                                result = false;
	//                                errorStr += "Giá trị phải là số; ";
	//                            }
	//                        }
	//                        if (StringUtils.isEmpty(workItemProperties[4].trim())) {
	//                            result = false;
	//                            errorStr += "\"Định mức thời gian(Số giờ)\" không được bỏ trống; ";
	//                        } else {
	//                            if (!StringUtils.isNumeric(workItemProperties[4].trim())) {
	//                                result = false;
	//                                errorStr += "\"Định mức thời gian(Số giờ)\" phải là số; ";
	//                            }
	//                        }
	//                    } else {
	//                        result = false;
	//                        errorStr += "\"Hạng mục tạo WO tự động (TTXDDTHT)\" không đúng định dạng; ";
	//                    }
	//                }
	//            }
	            tr.setCustomField(errorStr);
	        }
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
    }

    @Override
    public File genImportResultExcelFile(List<WoTrDTO> trs) throws Exception {
        File outFile = null;
        File templateFile = null;

        String fileName = "WO_XL_CreateTr_Result.xlsx";
        String outFileName = "Template_WO_XL_CreateTr_Result.xlsx";

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
                for (WoTrDTO tr : trs) {
                    Row row = importSheet.createRow(++rowNo);

                    if (StringUtils.isEmpty(tr.getCustomField()) && !"Thành công. ".equalsIgnoreCase(tr.getCustomField())) {
                        row.setRowStyle(errorStyle);
                    }
                    row.createCell(0).setCellValue(tr.getTrName());
                    row.createCell(1).setCellValue(tr.getTrTypeName());
                    row.createCell(2).setCellValue(tr.getContractCode());
                    row.createCell(3).setCellValue(tr.getProjectCode());
                    row.createCell(4).setCellValue(tr.getConstructionCode());
                    if (tr.getFinishDate() != null) {
                        row.createCell(5).setCellValue(dateFormat.format(tr.getFinishDate()));
                    }
                    row.createCell(6).setCellValue(tr.getQoutaTime());
                    row.createCell(7).setCellValue(tr.getCdLevel1Name());
                    row.createCell(8).setCellValue(tr.getFileName());
//                    row.createCell(9).setCellValue(tr.getCatWorkItemTypeListString());
                    row.createCell(9).setCellValue(tr.getDbTkdaDate());
                    row.createCell(10).setCellValue(tr.getDbTtkdtDate());
                    row.createCell(11).setCellValue(tr.getDbVtDate());
                    row.createCell(12).setCellValue(tr.getCustomField());
                }
                //write to output
                try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    workbook.write(outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outFile;
    }

    public String exportFileZip(boolean isCNKT, String cnktGroupId, boolean isXddtht) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "TrCreateExampleZip.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folderUpload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND) + File.separator + "TemplateUploadTR";
        String uploadZip = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND) + File.separator + "TemplateUploadTR";
        ;
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "TrCreateExampleZip.xlsx");
        File udir2 = new File(udir.getAbsolutePath() + File.separator + "DS_file_dinh_kem");
        if (!udir2.exists()) {
            udir2.mkdirs();
        }
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            inStream = new FileInputStream(new File(filePath + "Huongdaninport.txt"));
            outStream = new FileOutputStream(new File(udir2.getAbsolutePath() + File.separator + "Huongdaninport.txt"));
            int length;
            byte[] buffer = new byte[1024];

            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inStream.close();
            outStream.close();
        }
        XSSFSheet sheet1 = workbook.getSheetAt(1);
        List<WoTrTypeDTO> trTypes = woTrTypeDAO.getListTRType();
        if (trTypes != null && !trTypes.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet1);
            int i = 1;
            for (WoTrTypeDTO record : trTypes) {
                Row row = sheet1.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((record.getWoTrTypeId() + "-" + record.getTrTypeName() != null) ? (record.getWoTrTypeId() + "-" + record.getTrTypeName() + "-" + record.getTrTypeCode()) : "");
                cell.setCellStyle(style);
            }
        }
        List<WoAppParamDTO> cdLv1s = new ArrayList<>();
        //nếu tạo từ CNKT thì hard code cd level 1 là TTHT
        if (isCNKT) {
            WoAppParamDTO cdLv1 = new WoAppParamDTO();
            cdLv1.setCode("242656");
            cdLv1.setName("Trung tâm hạ tầng");
            cdLv1s.add(cdLv1);
        } else {
            //không thì lấy trong app_parram
            cdLv1s = woDAO.getAppParam(CD_LEVEL_1);
        }

        XSSFSheet sheet2 = workbook.getSheetAt(2);
        if (cdLv1s != null && !cdLv1s.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet2);
            int i = 1;
            for (WoAppParamDTO record : cdLv1s) {
                Row row = sheet2.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((record.getCode() + "-" + record.getName() != null) ? (record.getCode() + "-" + record.getName()) : "");
                cell.setCellStyle(style);
            }
        }

        //nếu tạo từ CNKT thì có thêm cdlv2 là đơn vị trong miền dữ liệu tạo TR
        if (isCNKT) {
            WoSimpleSysGroupDTO cdLevel2 = trDAO.getSysGroupById(Long.parseLong(cnktGroupId));
            XSSFSheet sheet3 = workbook.getSheetAt(3);
            if (cdLevel2 != null) {
                XSSFCellStyle style = ExcelUtils.styleText(sheet3);
                Row row = sheet3.createRow(1);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (0));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((cdLevel2.getCode() != null) ? cdLevel2.getCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((cdLevel2.getSysGroupId() + "-" + cdLevel2.getGroupName() != null) ? (cdLevel2.getSysGroupId() + "-" + cdLevel2.getGroupName()) : "");
                cell.setCellStyle(style);
            }
        } else {
            //nếu tạo TR từ TT XDDT HT thì load thêm danh sách hạng mục tạo wo tự động
            if (isXddtht) {
                XSSFSheet sheet4 = workbook.getSheetAt(4);
                XSSFCellStyle style = ExcelUtils.styleText(sheet4);
                Long trBranch = 3l; //mã trụ của TTXDĐTHT
                CatWorkItemTypeDTO searchObj = new CatWorkItemTypeDTO();
                searchObj.setTrBranch(trBranch);
                List<CatWorkItemTypeDTO> catAutoWorkItemTypeDTOList = trDAO.getAutoWoWorkItems(searchObj);

                int rowCount = 1;

                for (CatWorkItemTypeDTO item : catAutoWorkItemTypeDTOList) {
                    String hmTypeValue = "Mét";
                    if (item.getHmTypeValue() == 2) hmTypeValue = "VND";
                    String cellContent = item.getCode() + " | " + item.getName() + " | " + hmTypeValue + " | " + String.format("%.0f", item.getHmValue()) + " | " + item.getHmQuotaTime();
                    Row row = sheet4.createRow(rowCount++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue(cellContent);
                    cell.setCellStyle(style);
                }
            }
        }

        workbook.write(outFile);
        workbook.close();
        outFile.close();

        FileCompressor fileCompressor = new FileCompressor();
        for (final File filename : udir.listFiles()) {
            String srcPath = uploadPath + File.separator + filename.getName();
            fileCompressor.add(srcPath, filename.getName());
        }
        fileCompressor.setType(TYPE);
        fileCompressor.setLevel(LEVEL);
        fileCompressor.setCompressedPath(uploadPath + File.separator + "TemplateUploadTR." + TYPE.getExtension());
        fileCompressor.compress();
        String path = UEncrypt.encryptFileUploadPath(uploadZip + File.separator + "TemplateUploadTR." + TYPE.getExtension());
        return path;
    }

    @Override
    public boolean createTRImportZip(WoTrDTO trDto, WoSimpleSysGroupDTO creatorDomainGroup, Map<String, String> mapFile, boolean isImporting, boolean xddtht) throws Exception {
        try {
            if (StringUtils.isNotEmpty(trDto.getCdLevel1())) {
                trDto.setState(ASSIGN_CD);
            } else {
                trDto.setState(UNASSIGN);
            }

            String generatedTRCode = generateTRCode(trDto, creatorDomainGroup);

            trDto.setTrCode(generatedTRCode);
            trDto.setCreatedDate(new Date());

            if (StringUtils.isNotEmpty(trDto.getContractCode())) {
                Long contractId = trDAO.getContractIdByCode(trDto.getContractCode());
                trDto.setContractId(contractId);
            }
            if (StringUtils.isNotEmpty(trDto.getProjectCode())) {
                Long projectId = trDAO.getProject(trDto.getProjectCode());
                trDto.setProjectId(projectId);
            }

            //import: lấy mã trạm, long, lat theo mã công trình
            if (StringUtils.isNotEmpty(trDto.getConstructionCode()) && StringUtils.isEmpty(trDto.getStationCode())) {
                WoSimpleStationDTO station = trDAO.tryGetStation(trDto.getConstructionCode());
                if (station != null) {
                    trDto.setStationCode(station.getStationCode());
                    trDto.setExecuteLat(station.getLatitude());
                    trDto.setExecuteLong(station.getLongitude());
                }
            }

            //lưu TR
            long trId = trDAO.saveObject(trDto.toModel());

            //lưu file đính kèm
            if (trDto.getFileName() != null) {
                List<WoMappingAttachBO> lisWoMappingAttachBO = Lists.newArrayList();
                String[] listCD1 = trDto.getFileName().split(";");
                for (int i = 0; i < listCD1.length; i++) {
                    String name = listCD1[i];
                    if (name != null) {
                        if (!StringUtils.isEmpty(name.trim())) {
                            WoMappingAttachDTO woMappingAttachDTO = new WoMappingAttachDTO();
                            Date date = new Date();
                            woMappingAttachDTO.setCreatedDate(date);
                            woMappingAttachDTO.setStatus(1L);
                            woMappingAttachDTO.setTrId(trId);
                            woMappingAttachDTO.setUserCreated(trDto.getLoggedInUser());
                            woMappingAttachDTO.setFileName(listCD1[i]);
                            woMappingAttachDTO.setFilePath(UEncrypt.encryptFileUploadPath(mapFile.get(name.trim().toUpperCase())));
                            lisWoMappingAttachBO.add(woMappingAttachDTO.toModel());
                        }
                    }
                }
                woMappingAttachDAO.saveList(lisWoMappingAttachBO);
            }
            trDto.setTrId(trId);

            //tự động tạo WO nếu có
            if (xddtht == true) {
                if ("TR_DONG_BO_HA_TANG".equalsIgnoreCase(trDto.getTrTypeCode())) {
                    woBusiness.createNewDbhtWO(trDto);
                } else {
                    tryAutoCreateWoFromImport(trDto);
                }
            }
            if (StringUtils.isNotEmpty(trDto.getCdLevel1())) {
                trDto.setState(ASSIGN_CD);
                logTrWorkLogs(trDto, "1", "Tạo mới TR. Trạng thái: Chờ CD tiếp nhận. ", gson.toJson(trDto.toModel()), trDto.getLoggedInUser(), isImporting);
            }
            trDto.setCustomField("Thành công. ");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean validateFileName(List<WoTrDTO> trs, Map<String, String> mapFile) {
        boolean result = true;
        for (WoTrDTO tr : trs) {
            String errorStr = "";
            if (tr.getFileName() != null) {
                if (StringUtils.isEmpty(tr.getFileName().trim())) {
                    result = false;
                    errorStr += "File đính kèm không được để trống; ";
                }
                String[] list = tr.getFileName().split(";");
                for (int i = 0; i < list.length; i++) {
                    String name = list[i];
                    if (name != null) {
                        if (!StringUtils.isEmpty(name.trim())) {
                            boolean isfile = mapFile.containsKey(name.trim().toUpperCase());
                            if (!isfile) {
                                result = false;
                                errorStr += "File đính kèm không tồn tại trong thư mục upload! ";
                            }
                        }
                    }
                }
            } else {
                result = false;
                errorStr += "File đính kèm không được để trống; ";
            }
            tr.setCustomField(errorStr);
        }
        return result;
    }

    @Transactional
    @Override
    public List<WoTrDTO> list(String employeeCode, List<String> lstGroupIds) {
        WoTrDTO trDto = new WoTrDTO();
        trDto.setLoggedInUser(employeeCode);
        List<WoTrDTO> lst = trDAO.doSearch(trDto, lstGroupIds, "ALL TYPE", false, false);

        return lst;
    }

    public String exportWoConfigContract() throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "WO_XL_WoConfigContract.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folderUpload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "WO_XL_WoConfigContractTemplate.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);

        XSSFSheet sheet1 = workbook.getSheetAt(1);
        WoConfigContractCommitteeDTO woConfigDTO = new WoConfigContractCommitteeDTO();
        List<WoConfigContractCommitteeDTO> listContract = woConfigContractDAO.doSearchWoConfigContract(woConfigDTO);
        if (listContract != null && !listContract.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet1);
            int i = 1;
            for (WoConfigContractCommitteeDTO record : listContract) {
                Row row = sheet1.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue(record.getContractId());
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((record.getContractCode() != null) ? record.getContractCode() : "");
                cell.setCellStyle(style);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "WO_XL_WoConfigContractTemplate.xlsx");
        return path;
    }

    public List<CatWorkItemTypeDTO> getAutoWoWorkItems(CatWorkItemTypeDTO searchObj) {
        return trDAO.getAutoWoWorkItems(searchObj);
    }

    private void tryAutoCreateWoFromImport(WoTrDTO triggerTr) {
        try {
            if (StringUtils.isEmpty(triggerTr.getCatWorkItemTypeListString())) return;

            List<CatWorkItemTypeDTO> catWorkItemTypeList = new ArrayList<>();
            String[] catWorkItemStrArray = triggerTr.getCatWorkItemTypeListString().split("\n");
            for (String catWorkItemStr : catWorkItemStrArray) {
                String[] workItemProperties = catWorkItemStr.split("\\|");

                String catWCode = workItemProperties[0].trim();
                String hmValue = workItemProperties[3].trim();
                String hmQuota = workItemProperties[4].trim();

                CatWorkItemTypeDTO catWorkItemType = trDAO.getCatWorkItemType(catWCode);
                if (catWorkItemType != null) {
                    catWorkItemType.setHmValue(Double.valueOf(hmValue));
                    catWorkItemType.setHmQuotaTime(Long.valueOf(hmQuota));
                    catWorkItemTypeList.add(catWorkItemType);
                }

            }
            triggerTr.setCatWorkItemTypeList(catWorkItemTypeList);
            tryAutoCreateWo(triggerTr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tryAutoCreateWo(WoTrDTO triggerTr) {
        for (CatWorkItemTypeDTO catWorkItemType : triggerTr.getCatWorkItemTypeList()) {
            //CatWorkItemTypeDTO workItem = trDAO.getCatWorkItemType(catWorkItemType.getCatWorkItemTypeId());

            Double woValue = calculateWorkItemValue(catWorkItemType);
            String woTypeCode = "THICONG";
            Long quotaTime = catWorkItemType.getHmQuotaTime();
            Date now = new Date();
            Date finishDate = new Date(now.getTime() + quotaTime * 60 * 60 * 1000);
            WoSimpleSysGroupDTO cdLevel2 = trDAO.tryGetCdLevel2FromConstruction(triggerTr.getConstructionId());

            WoDTO wo = new WoDTO();
            wo.setWoId(0l);
            wo.setState(ASSIGN_CD);
            wo.setWoTypeId(1l);
            wo.setWoTypeCode(woTypeCode);

            //nguồn việc & loại công trình fix
            wo.setApWorkSrc(4l);
            wo.setApConstructionType(7l);

            wo.setWoName("Thi công công trình");
            wo.setWoNameId(1l);
            wo.setTrId(triggerTr.getTrId());
            wo.setTrCode(triggerTr.getTrCode());
            wo.setConstructionId(triggerTr.getConstructionId());
            wo.setConstructionCode(triggerTr.getConstructionCode());
            wo.setCatConstructionTypeId(catWorkItemType.getCatConstructionTypeId());
            wo.setCatWorkItemTypeId(catWorkItemType.getCatWorkItemTypeId());
            wo.setCatWorkItemTypeName(catWorkItemType.getName());
            wo.setStationCode(triggerTr.getStationCode());
            wo.setUserCreated("-1");
            wo.setCreatedDate(now);
            wo.setFinishDate(finishDate);
            wo.setQoutaTime(quotaTime.intValue());
            wo.setCdLevel1(triggerTr.getCdLevel1());
            wo.setCdLevel1Name(triggerTr.getCdLevel1Name());
            if (cdLevel2 != null) {
                wo.setCdLevel2(cdLevel2.getSysGroupId().toString());
                wo.setCdLevel2Name(cdLevel2.getGroupName());
            }

            wo.setExecuteLat(triggerTr.getExecuteLat());
            wo.setExecuteLong(triggerTr.getExecuteLong());
            wo.setStatus(0l);
            wo.setMoneyValue(woValue);
            if (catWorkItemType.getHmTypeValue() == 1) wo.setQuantityValue("" + catWorkItemType.getHmValue());

            wo.setContractId(triggerTr.getContractId());
            wo.setContractCode(triggerTr.getContractCode());
            wo.setProjectId(triggerTr.getProjectId());
            wo.setProjectCode(triggerTr.getProjectCode());

            String woCode = woBusiness.generateWoCode(wo);
            wo.setWoCode(woCode);

            try {
                Long woId = woDAO.saveObject(wo.toModel());
                wo.setWoId(woId);

                woBusiness.tryCreateChecklistForNewWo(wo);

                //add work item for construction
                if (wo.getCatWorkItemTypeId() != null && wo.getConstructionId() != null) {
                    if (!woDAO.checkExistConstructionWorkItem(wo.getConstructionId(), wo.getCatWorkItemTypeId())) {

                        wo.setSysUserId(-1l);
                        wo.setSysGroupId(-1l);
                        WoCatWorkItemTypeDTO catWI = woDAO.getCatWorkTypeById(wo.getCatWorkItemTypeId());
                        wo.setCatWorkItemTypeCode(catWI.getCatWorkItemTypeCode());

                        woDAO.insertWorkItem(wo);
                    }
                }

                woBusiness.logWoWorkLogs(wo, "1", "Tự động tạo mới", "", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Double calculateWorkItemValue(CatWorkItemTypeDTO workItem) {
        if (workItem.getHmTypeValue() == 2) return workItem.getHmValue();
        else {
            Double amount = workItem.getHmValue();
            int value = 0;
            if (amount >= 36 && amount < 42) value = 50;
            if (amount >= 42 && amount < 48) value = 60;
            if (amount >= 48 && amount < 54) value = 65;
            if (amount >= 54 && amount < 60) value = 70;
            if (amount >= 60) value = 80;

            return value * 1000000D;
        }
    }

    private void tryEnableAutoGeneratedWo(WoTrDTO trDto) {

        List<WoDTO> inactiveWoList;

        if (trDto.getInactiveWoList() == null) {
            WoDTO searchObj = new WoDTO();
            searchObj.setTrId(trDto.getTrId());
            inactiveWoList = woDAO.getInactiveWoList(searchObj);
        } else {
            inactiveWoList = trDto.getInactiveWoList();
        }

        for (WoDTO wo : inactiveWoList) {
            WoBO woBo = woDAO.getOneRaw(wo.getWoId());
            if (StringUtils.isNotEmpty(wo.getQuantityValue())) {
                woBo.setQuantityValue(wo.getQuantityValue());

                CatWorkItemTypeDTO workItem = new CatWorkItemTypeDTO();
                workItem.setHmTypeValue(1l);
                workItem.setHmValue(Double.valueOf(wo.getQuantityValue()));

                //tính toán lại sản lượng theo số mét mới nhập
                Double woValue = calculateWorkItemValue(workItem);
                woBo.setMoneyValue(woValue);
            } else {
                woBo.setMoneyValue(wo.getMoneyValue());
            }
            woBo.setStatus(1l);
            woBo.setQoutaTime(wo.getQoutaTime());
            woDAO.updateObject(woBo);
        }

        WoTrDTO trDetail = trDAO.getOneDetails(trDto.getTrId());
        if ("TR_THUE_MAT_BANG_TRAM".equalsIgnoreCase(trDetail.getTrTypeCode()) || "TR_DONG_BO_HA_TANG".equalsIgnoreCase(trDetail.getTrTypeCode())) {
            woBusiness.createNewTmbtWO(trDetail);
        }
    }

    private void tryReAssignWo(Long trId, String cdLevel1) {
        if (trId == null || cdLevel1 == null) return;
        WoSimpleSysGroupDTO cdGroup = trDAO.getSysGroupById(Long.valueOf(cdLevel1));
        List<Long> inactiveWoIdList = woDAO.getInactiveWoIdListFromTr(trId);
        if (inactiveWoIdList != null) {
            for (Long woId : inactiveWoIdList) {
                WoBO wo = woDAO.getOneRaw(woId);
                wo.setCdLevel1(cdLevel1);
                wo.setCdLevel1Name(cdGroup.getGroupName());
                woDAO.updateObject(wo);
            }
        }
    }
    
    //Huypq-09072021-start
    public String getExcelTemplate(CatStationDTO obj) throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
  		String filePath = classloader.getResource("../" + "doc-template").getPath();
  		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_Import_Tr_TMBT.xlsx"));
  		XSSFWorkbook workbook = new XSSFWorkbook(file);
  		file.close();
  		Calendar cal = Calendar.getInstance();
  		String uploadPath = folderUpload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
  				+ File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
  				+ File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
  		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
  				+ cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
  				+ cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
  		File udir = new File(uploadPath);
  		if (!udir.exists()) {
  			udir.mkdirs();
  		}
  		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_Import_Tr_TMBT.xlsx");
  		
  		CatProvinceDTO province = new CatProvinceDTO();
  		province.setStatus("1");
  		List<CatProvinceDTO> lstProvince = catProvinceDAO.getForComboBox(province);
  		
		
		if(lstProvince != null && !lstProvince.isEmpty()) {
			XSSFSheet sheet = workbook.getSheetAt(1);
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			int i = 1;
			for(CatProvinceDTO dto :  lstProvince) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue(dto.getCode() != null ? dto.getCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getName() != null ? dto.getName() : "");
				cell.setCellStyle(style);
			}
		}
		
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_Import_Tr_TMBT.xlsx");
		return path;
	}
    
    private ExcelErrorDTO createError(int row, int column, String detail) {
        ExcelErrorDTO err = new ExcelErrorDTO();
        err.setColumnError(String.valueOf(column));
        err.setLineError(String.valueOf(row));
        err.setDetailError(detail);
        return err;
    }
    
    public boolean validateString(String str){
		return (str != null && str.length()>0);
	}
    
	public List<WoTrDTO> importFileTrTmbt(String fileInput, HttpServletRequest request) throws Exception {
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<WoTrDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		String error = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			int counts = 0;

			WoTrDTO consWi = new WoTrDTO();
			consWi.setPage(null);
			consWi.setPageSize(null);
			// get ma tram
			List<String> lstStation = new ArrayList<>();
			List<String> lstProvince = new ArrayList<>();
			List<String> lstStationPro = new ArrayList<>();
			for (Row row : sheet) {
				counts++;
				if (counts >= 3 && ValidateUtils.checkIfRowIsEmpty(row))
					continue;
				if (counts >= 3) {
					lstStation.add(formatter.formatCellValue(row.getCell(2)).trim());
					lstProvince.add(formatter.formatCellValue(row.getCell(3)).trim());
					lstStationPro.add(formatter.formatCellValue(row.getCell(2)).trim() + "_" + formatter.formatCellValue(row.getCell(3)).trim());
				}
			}

			List<CatStationDTO> lstMapStationProvince = catStationDAO.checkDataDbImportTrStation(lstStationPro);
			HashMap<String, String> mapStationProvince = new HashMap<>();
			for(CatStationDTO dto : lstMapStationProvince) {
				mapStationProvince.put(dto.getCode().toUpperCase(), dto.getCode());
			}
			
			//Check tồn tại trong file
            HashMap<String, String> mapStationFile = new HashMap<>();
            for (int i = 0; i < lstStationPro.size(); i++) {
                if (mapStationFile.size() == 0) {
                	if(mapStationProvince.get(lstStationPro.get(i))!=null) {
                		ExcelErrorDTO errorDTO = createError(i + 3, 2, "Cặp mã trạm - tỉnh đã tồn tại");
                        errorList.add(errorDTO);
                	} else {
                		mapStationFile.put(lstStationPro.get(i), lstStationPro.get(i));
                	}
                } else {
                    if (!StringUtils.isNoneBlank(mapStationFile.get(lstStationPro.get(i)))) {
                    	if(mapStationProvince.get(lstStationPro.get(i))!=null) {
                    		ExcelErrorDTO errorDTO = createError(i + 3, 2, "Cặp mã trạm - tỉnh đã tồn tại");
                            errorList.add(errorDTO);
                    	} else {
                    		mapStationFile.put(lstStationPro.get(i), lstStationPro.get(i));
                    	}
                    } else {
                        ExcelErrorDTO errorDTO = createError(i + 3, 2, "Cặp mã trạm - tỉnh trong file import trùng nhau");
                        errorList.add(errorDTO);
                    }
                }
            }
			
			List<CatStationDTO> listStation = catStationDAO.getCodeStationForImport(lstStation);
			HashMap<String, CatStationDTO> mapStation = new HashMap<>();
			for (CatStationDTO dto : listStation) {
				mapStation.put(dto.getCode() != null ? dto.getCode().toUpperCase() : "", dto);
			}

			CatProvinceDTO province = new CatProvinceDTO();
			province.setStatus("1");
			province.setLstProvince(lstProvince);
			List<CatProvinceDTO> lstPro = catProvinceDAO.getForComboBox(province);
			HashMap<String, CatProvinceDTO> mapProvince = new HashMap<>();
			for (CatProvinceDTO pro : lstPro) {
				mapProvince.put(pro.getCode() != null ? pro.getCode().toUpperCase() : "", pro);
			}

			SysGroupDTO group = new SysGroupDTO();
			group.setGroupLevel("3");
			group.setCode("P.HT");
			List<SysGroupDTO> lstGroup = sysGroupDAO.getSysGroupLv3PHT(group);
			HashMap<String, SysGroupDTO> mapGroup = new HashMap<>();
			for (SysGroupDTO grou : lstGroup) {
				mapGroup.put(grou.getCatProvinceCode() != null ? grou.getCatProvinceCode().toUpperCase() : "", grou);
			}
			
			String validateDate = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
			
			for (Row row : sheet) {
				int rowNum = row.getRowNum() + 1;

				if (rowNum < 3) {
					continue;
				}

				count++;
				if (count > 3 && ValidateUtils.checkIfRowIsEmpty(row))
					continue;

				String trName = formatter.formatCellValue(row.getCell(1)).trim();
				String stationCode = formatter.formatCellValue(row.getCell(2)).trim();
				String provinceCode = formatter.formatCellValue(row.getCell(3)).trim();
				String finishDate = formatter.formatCellValue(row.getCell(4)).trim();

				WoTrDTO obj = new WoTrDTO();

				if (validateString(trName)) {
					obj.setTrName(trName);
				} else {
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 1, " Tên TR không được bỏ trống");
					errorList.add(errorDTO);
				}

				if (validateString(stationCode)) {
					CatStationDTO station = mapStation.get(stationCode.toUpperCase());
					if (station != null) {
						obj.setStationCode(stationCode);
						obj.setStationId(station.getCatStationId());
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 2, " Mã trạm không tồn tại hoặc đang ở trạng thái không hợp lệ");
						errorList.add(errorDTO);
					}
				} else {
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 2, " Mã trạm không được bỏ trống");
					errorList.add(errorDTO);
				}

				if (validateString(provinceCode)) {
					CatProvinceDTO provinc = mapProvince.get(provinceCode.toUpperCase());
					if (provinc != null) {
						SysGroupDTO sysGroup = mapGroup.get(provinc.getCode().toUpperCase());
						obj.setCdLevel2(sysGroup.getSysGroupId().toString());
						obj.setCdLevel2Name(sysGroup.getName());
						obj.setStationSysGroup(sysGroup.getSysGroupId().toString());
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 3, " Mã tỉnh không tồn tại");
						errorList.add(errorDTO);
					}
				} else {
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 3, " Mã tỉnh không được bỏ trống");
					errorList.add(errorDTO);
				}
				
				if (validateString(finishDate)) {
					if(finishDate.matches(validateDate)) {
						Date date = sdf.parse(finishDate);
						obj.setFinishDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 4, " Hạn hoàn thành không hợp lệ");
						errorList.add(errorDTO);
					}
				} else {
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 4, " Hạn hoàn thành không được bỏ trống");
					errorList.add(errorDTO);
				}
				
				if (errorList.size() == 0) {
					obj.setCdLevel1("242656");
					obj.setCdLevel1Name("TTHT - Trung tâm hạ tầng");
					obj.setTrTypeId(444522l);
					obj.setUserCreated(objUser.getVpsUserInfo().getEmployeeCode());
					obj.setCreatedDate(new Date());
					obj.setState(ASSIGN_CD);
					obj.setStatus(1);
					obj.setUserReceiveTr(objUser.getVpsUserInfo().getEmployeeCode());
					obj.setUpdateReceiveTr(new Date());
					obj.setTmbtTarget(1l);
					obj.setTmbtTargetDetail(stationCode);
					workLst.add(obj);
				}

			}
			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<WoTrDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				WoTrDTO errorContainer = new WoTrDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(6);
				errorContainer.setFilePathError(filePathError);
				workLst.add(errorContainer);
			}
			workbook.close();
			return workLst;

		} catch (NullPointerException pointerException) {
			// LOGGER.error(pointerException.getMessage(), pointerException);
			pointerException.printStackTrace();
			throw new Exception(pointerException.getMessage());
		} catch (Exception e) {
			// LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
    //Huy-end
}
