package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.bo.TotalMonthPlanOSBO;
import com.viettel.coms.dao.DepartmentDAO;
import com.viettel.coms.dao.TmpnContractDAO;
import com.viettel.coms.dao.TmpnFinanceDAO;
import com.viettel.coms.dao.TmpnForceMaintainDAO;
import com.viettel.coms.dao.TmpnForceNewBtsDAO;
import com.viettel.coms.dao.TmpnForceNewLineDAO;
import com.viettel.coms.dao.TmpnMaterialDAO;
import com.viettel.coms.dao.TmpnSourceDAO;
import com.viettel.coms.dao.TmpnTargetOSDAO;
import com.viettel.coms.dao.TotalMonthPlanOSDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.TmpnContractDTO;
import com.viettel.coms.dto.TmpnFinanceDTO;
import com.viettel.coms.dto.TmpnForceMaintainDTO;
import com.viettel.coms.dto.TmpnForceNewBtsDTO;
import com.viettel.coms.dto.TmpnForceNewLineDTO;
import com.viettel.coms.dto.TmpnMaterialDTO;
import com.viettel.coms.dto.TmpnSourceDTO;
import com.viettel.coms.dto.TmpnTargetDTO;
import com.viettel.coms.dto.TmpnTargetOSDTO;
import com.viettel.coms.dto.TotalMonthPlanOSDTO;
import com.viettel.coms.dto.TotalMonthPlanOSSimpleDTO;
import com.viettel.coms.dto.TotalMonthPlanSimpleDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.YearPlanDetailOSDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.utils.StringUtils;
import com.viettel.utils.Constant;
import com.viettel.wms.utils.ValidateUtils;

/**
 * @author HoangNH38
 */
@Service("totalMonthPlanOSBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TotalMonthPlanOSBusinessImpl extends
BaseFWBusinessImpl<TotalMonthPlanOSDAO, TotalMonthPlanOSDTO, TotalMonthPlanOSBO> implements TotalMonthPlanOSBusiness{


    @Autowired
    private TotalMonthPlanOSDAO totalMonthPlanOSDAO;
    @Autowired
    private TmpnTargetOSDAO tmpnTargetOSDAO;
    @Autowired
    private TmpnSourceDAO tmpnSourceDAO;

    @Autowired
    private TmpnForceMaintainDAO tmpnForceMaintainDAO;
    @Autowired
    private TmpnForceNewBtsDAO tmpnForceNewBtsDAO;

    @Autowired
    private TmpnForceNewLineDAO tmpnForceNewLineDAO;
    @Autowired
    private TmpnMaterialDAO tmpnMaterialDAO;
    @Autowired
    private TmpnFinanceDAO tmpnFinanceDAO;
    @Autowired
    private TmpnContractDAO tmpnContractDAO;

    @Value("${folder_upload2}")
    private String folder2Upload;

    @Autowired
    private UtilAttachDocumentDAO utilAttachDocumentDAO;

    @Value("${folder_upload2}")
    private String folderUpload;

    @Value("${folder_upload}")
    private String folderTemp;

    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;

    @Value("${allow.file.ext}")
    private String allowFileExt;
    @Value("${allow.folder.dir}")
    private String allowFolderDir;
    
    @Autowired
    private DepartmentDAO departmentDAO;
    private String VAT_TU = "Import_vattu_thang_tongthe.xlsx";
    private static ArrayList<String> listOfTypePhuLuc = new ArrayList<String>() {
        {
            add("OS");
        }
    };

    public TotalMonthPlanOSBusinessImpl() {
        tModel = new TotalMonthPlanOSBO();
        tDAO = totalMonthPlanOSDAO;
    }

    @Override
    public TotalMonthPlanOSDAO gettDAO() {
        return totalMonthPlanOSDAO;
    }

    @Override
    public long count() {
        return totalMonthPlanOSDAO.count("TotalMonthPlanOSBO", null);
    }

    public DataListDTO doSearch(TotalMonthPlanOSSimpleDTO obj) {
        List<TotalMonthPlanOSDTO> ls = totalMonthPlanOSDAO.doSearch(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public Long add(TotalMonthPlanOSSimpleDTO obj, String userName, Long userId, HttpServletRequest request)
            throws Exception {
        // TODO Auto-generated method stub
        checkMonthYear(obj.getMonth(), obj.getYear(), null);
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.TOTAL_MONTH_PLAN,
                request)) {
            throw new IllegalArgumentException("Bạn không có quyền tạo mới thông tin tháng tổng thể ngoài OS");
        }

        Long totalMonthPlanId = totalMonthPlanOSDAO.saveObject(obj.toModel());
        if (obj.getAppendixFileDTOList() != null) {
            for (UtilAttachDocumentDTO file : obj.getAppendixFileDTOList()) {
                if (file.getUtilAttachDocumentId() == null) {
                    file.setObjectId(totalMonthPlanId);
                    file.setType("OS");
                    file.setCreatedDate(new Date());
                    file.setCreatedUserId(userId);
                    file.setCreatedUserName(userName);
                    file.setDescription("file Phu luc thang tong the ngoai OS");
                    file.setFilePath(UEncrypt.decryptFileUploadPath(file.getFilePath()));
                    utilAttachDocumentDAO.saveObject(file.toModel());
                }
            }
        }
        if (obj.getListAttachFile() != null) {
            for (UtilAttachDocumentDTO file : obj.getListAttachFile()) {
                if (file.getUtilAttachDocumentId() == null) {
                    file.setObjectId(totalMonthPlanId);
                    file.setType("OS");
                    file.setCreatedDate(new Date());
                    file.setCreatedUserId(userId);
                    file.setCreatedUserName(userName);
                    file.setDescription("file Ke hoach thang tong the ngoai OS");
                    file.setFilePath(UEncrypt.decryptFileUploadPath(file.getFilePath()));
                    utilAttachDocumentDAO.saveObject(file.toModel());
                }
            }
        }
        if (obj.getTmpnTargetDTOList() != null) {
            for (TmpnTargetOSDTO dto : obj.getTmpnTargetDTOList()) {
                dto.setTotalMonthPlanId(totalMonthPlanId);
                dto.setYear(obj.getYear());
                dto.setMonth(obj.getMonth());
                tmpnTargetOSDAO.saveObject(dto.toModel());
            }
        }
        
        return totalMonthPlanId;
    }

    public void checkMonthYear(Long month, Long year, Long totalMonthId) {
        boolean isExist = totalMonthPlanOSDAO.checkMonthYear(month, year, totalMonthId);
        if (isExist)
            throw new IllegalArgumentException("Tháng và năm tạo kế hoạch tổng thể đã tồn tại!");
    }

    public Long getSequence() {
        // TODO Auto-generated method stub
        return totalMonthPlanOSDAO.getSequence();
    }

    public void remove(TotalMonthPlanOSSimpleDTO obj, HttpServletRequest request) {
        // TODO Auto-generated method stub
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.TOTAL_MONTH_PLAN,
                request)) {
            throw new IllegalArgumentException("Bạn không có quyền xóa thông tin tháng tổng thể");
        }
        totalMonthPlanOSDAO.remove(obj.getTotalMonthPlanId());
    }

    public TotalMonthPlanOSSimpleDTO getById(Long id) throws Exception {
        // TODO Auto-generated method stub
        TotalMonthPlanOSSimpleDTO data = totalMonthPlanOSDAO.getById(id);
//        data.setAppendixFileDTOList(utilAttachDocumentDAO.getByListTypeAndObject(id, listOfTypePhuLuc));
        data.setListAttachFile(utilAttachDocumentDAO.getByTypeAndObjectOS(id, 53L));
        data.setTmpnTargetDTOList(totalMonthPlanOSDAO.getTmpnTargetDTOListByParent(id));
//        data.setTmpnSourceDTOList(totalMonthPlanOSDAO.getTmpnSourceDTOListByParent(id));
//        data.setTmpnForceMaintainDTOList(totalMonthPlanOSDAO.getTmpnForceMaintainDTOListByParent(id));
//        data.setTmpnForceNewBtsDTOList(totalMonthPlanOSDAO.getTmpnForceNewBtsDTOListByParent(id));
//        data.setTmpnForceNewLineDTOList(totalMonthPlanOSDAO.getTmpnForceNewLineDTOListByParent(id));
//        data.setTmpnMaterialDTOList(totalMonthPlanOSDAO.getTmpnMaterialDTOListByParent(id));
//        data.setTmpnFinanceDTOList(totalMonthPlanOSDAO.getTmpnFinanceDTOListByParent(id));
//        data.setTmpnContractDTOList(totalMonthPlanOSDAO.getTmpnContractDTOListByParent(id));
        return data;
    }
    
    public TotalMonthPlanOSSimpleDTO getByIdV2(Long id) throws Exception {
        TotalMonthPlanOSSimpleDTO data = totalMonthPlanOSDAO.getById(id);
        data.setListAttachFile(utilAttachDocumentDAO.getByTypeAndObjectOS(id, 53L));
        return data;
    }
    
    public TotalMonthPlanOSSimpleDTO getTmpnList(Long id) throws Exception {
    	 TotalMonthPlanOSSimpleDTO data = totalMonthPlanOSDAO.getById(id);
//    	 data.setTmpnTargetDTOList(totalMonthPlanOSDAO.getTmpnTargetDTOListByParent(id));
    	 data.setTmpnTargetDTOList(totalMonthPlanOSDAO.getTmpnTargetDTOListByParentNew(id));
         return data;
    }

    public TotalMonthPlanOSSimpleDTO getByIdCopy(Long id) throws Exception {
        // TODO Auto-generated method stub
        TotalMonthPlanOSSimpleDTO data = totalMonthPlanOSDAO.getById(id);
//        data.setAppendixFileDTOList(utilAttachDocumentDAO.getByListTypeAndObject(id, listOfTypePhuLuc));
        data.setListAttachFile(utilAttachDocumentDAO.getByTypeAndObjectOS(id, 50L));
        data.setTmpnTargetDTOList(totalMonthPlanOSDAO.getTmpnTargetDTOListByParent(id));
//        data.setTmpnSourceDTOList(totalMonthPlanOSDAO.getTmpnSourceDTOListByParent(id));
//        data.setTmpnForceMaintainDTOList(totalMonthPlanOSDAO.getTmpnForceMaintainDTOListByParent(id));
//        data.setTmpnForceNewBtsDTOList(totalMonthPlanOSDAO.getTmpnForceNewBtsDTOListByParent(id));
//        data.setTmpnForceNewLineDTOList(totalMonthPlanOSDAO.getTmpnForceNewLineDTOListByParent(id));
//        data.setTmpnMaterialDTOList(totalMonthPlanOSDAO.getTmpnMaterialDTOListByParent(id));
//        data.setTmpnFinanceDTOList(totalMonthPlanOSDAO.getTmpnFinanceDTOListByParent(id));
//        data.setTmpnContractDTOList(totalMonthPlanOSDAO.getTmpnContractDTOListByParent(id));
        return data;
    }

    public TmpnTargetOSDTO getByIdTarget(Long month, Long year, Long sysGroupId) {
        if (month != null && year != null && sysGroupId != null) {
            TmpnTargetOSDTO dto = totalMonthPlanOSDAO.getTmpnTargetDTOListChoseByParent(month, year, sysGroupId);
            return dto;
        }
        return null;
    }

    public String exportExcelTemplate(String fileName) throws Exception {
        // TODO Auto-generated method stub
        String folderParam = defaultSubFolderUpload;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + fileName));
        // String filePath1 = UFile.writeToFileServerATTT2(file,
        // "Import_KeHoachNam.xlsx", folderParam, folder2Upload);
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
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + fileName);
        XSSFSheet sheet = workbook.getSheetAt(1);
        List<DepartmentDTO> departmentList = departmentDAO.getAll();
        if (departmentList != null && !departmentList.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            int i = 1;
            for (DepartmentDTO dto : departmentList) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                // chinhpxn20180619
//				cell.setCellValue(dto.getId()!=null?dto.getId().toString():"");
                cell.setCellValue(!StringUtils.isNullOrEmpty(dto.getCode()) ? dto.getCode() : "");
                // chinhpxn20180619
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(!StringUtils.isNullOrEmpty(dto.getName()) ? dto.getName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(!StringUtils.isNullOrEmpty(dto.getGroupNameLevel1()) ? dto.getGroupNameLevel1() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(!StringUtils.isNullOrEmpty(dto.getGroupNameLevel2()) ? dto.getGroupNameLevel2() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(!StringUtils.isNullOrEmpty(dto.getGroupNameLevel3()) ? dto.getGroupNameLevel3() : "");
                cell.setCellStyle(style);
            }
        }
        if (VAT_TU.equals(fileName)) {
            XSSFSheet sheet2 = workbook.getSheetAt(2);
//			XSSFSheet sheet3 = workbook.getSheetAt(3);
            List<TotalMonthPlanOSSimpleDTO> constructionTypeList = totalMonthPlanOSDAO.getAllConstructionType();
//			List<TotalMonthPlanOSSimpleDTO> constructionDoployList = totalMonthPlanOSDAO
//					.getAllConstructionDeploy();
            if (constructionTypeList != null && !constructionTypeList.isEmpty()) {
                XSSFCellStyle style = ExcelUtils.styleText(sheet);
                int i = 1;
                for (TotalMonthPlanOSSimpleDTO dto : constructionTypeList) {
                    Row row = sheet2.createRow(i++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue(
                            dto.getCatConstructionTypeId() != null ? dto.getCatConstructionTypeId().toString() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue(!StringUtils.isNullOrEmpty(dto.getCatConstructionTypeName())
                            ? dto.getCatConstructionTypeName()
                            : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue(
                            dto.getCatConstructionDeployId() != null ? dto.getCatConstructionDeployId().toString()
                                    : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue(!StringUtils.isNullOrEmpty(dto.getCatConstructionDeployName())
                            ? dto.getCatConstructionDeployName()
                            : "");
                    cell.setCellStyle(style);
                }
            }
            /*
             * if (constructionDoployList != null && !constructionDoployList.isEmpty()) {
             * XSSFCellStyle style = ExcelUtils.styleText(sheet); int i = 1; for
             * (TotalMonthPlanOSSimpleDTO dto : constructionDoployList) { Row row =
             * sheet3.createRow(i++); Cell cell = row.createCell(0, CellType.STRING);
             * cell.setCellValue(dto.getCatConstructionDeployId() != null ? dto
             * .getCatConstructionDeployId().toString() : ""); cell.setCellStyle(style);
             * cell = row.createCell(1, CellType.STRING);
             * cell.setCellValue(!StringUtils.isNullOrEmpty(dto
             * .getCatConstructionDeployName()) ? dto .getCatConstructionDeployName() : "");
             * cell.setCellStyle(style); } }
             */
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
        return path;
    }

    public List<TmpnTargetOSDTO> importTarget(String fileInput, String filePath) throws Exception {

        List<TmpnTargetOSDTO> workLst = new ArrayList<TmpnTargetOSDTO>();
        List<Long> sysGroupIdList = new ArrayList<Long>();
        File f = new File(fileInput);
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);

        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        List<String> listGroupCode = new ArrayList<String>();
        boolean isExistError = false; 
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn1 = true;
                boolean checkColumn2 = true;
                boolean checkColumn3 = true;
                boolean checkColumn4 = true;
                boolean checkColumn5 = true;
                boolean checkColumn6 = true;
                boolean checkColumn7 = true;
                boolean checkColumn8 = true;
                boolean checkColumn9 = true;
                boolean checkColumn10 = true;
                boolean checkColumn11 = true;
                boolean checkColumn12 = true;
                boolean checkColumn13 = true;
                boolean checkColumn14 = true;
                boolean checkColumn15 = true;
                boolean checkColumn16 = true;

                // chinhpxn20180704_start
                boolean checkLength = true;
                // chinhpxn20180704_end
                String sysGroupCode = "";
                StringBuilder errorMesg = new StringBuilder();
                TmpnTargetOSDTO newObj = new TmpnTargetOSDTO();
                
                for (int i = 0; i < 17; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        // Check format file exel
                        if (cell.getColumnIndex() == 1) {
                            try {
                                sysGroupCode = formatter.formatCellValue(cell).trim();
                                if (listGroupCode.contains(sysGroupCode)) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị đã bị trùng!");
                                } else {
                                    listGroupCode.add(sysGroupCode.trim());
                                }
                                DepartmentDTO dep = departmentDAO.getSysGroupData(sysGroupCode);
                                if (dep.getId() != null) {
                                    newObj.setSysGroupId(dep.getId());
                                    newObj.setSysGroupName(dep.getName());
                                    sysGroupIdList.add(dep.getId());
                                } else
                                    checkColumn1 = false;
                                if (!checkColumn1) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị không hợp lệ!");
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("Mã đơn vị không hợp lệ!");
                            }
                        }
                        else if (cell.getColumnIndex() == 2) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                	isExistError = true;
                                    checkColumn2 = false;
                                    errorMesg.append("\nSản lượng xây lắp không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                		isExistError = true;
                                        checkColumn2 = false;
                                        errorMesg.append("\nĐộ dài Sản lượng xây lắp vượt quá 14 số !");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                        checkColumn2 = false;
                                        errorMesg.append("\nSản lượng xây lắp phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setQuantityXlTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                            	isExistError = true;
                                checkColumn2 = false;
                                errorMesg.append("\nSản lượng xây lắp không hợp lệ !");
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 3) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn3 = false;
                                    isExistError = true;
                                    errorMesg.append("\nSản lượng Nguồn chi phí không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn3 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Sản lượng Nguồn chi phí vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                        checkColumn3 = false;
                                        errorMesg.append("\nSản lượng Nguồn chi phí phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setQuantityCpTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                            	isExistError = true;
                                checkColumn3 = false;
                                errorMesg.append("\nSản lượng Nguồn chi phí không hợp lệ!");
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 4) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn4 = false;
                                    isExistError = true;
                                    errorMesg.append("\nSản lượng Ngoài TĐ(GPDN) không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn4 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Sản lượng Ngoài TĐ(GPDN) vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn4 = false;
                                        errorMesg.append("\nSản lượng Ngoài TĐ(GPDN) phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setQuantityNtdGpdnTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn4 = false;
                                isExistError = true;
                                errorMesg.append("\nSản lượng Ngoài TĐ(GPDN) không hợp lệ!");
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 5) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn5 = false;
                                    isExistError = true;
                                    errorMesg.append("\nSản lượng Ngoài TĐ(XDDD) không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn5 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Sản lượng Ngoài TĐ(XDDD) vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn5 = false;
                                        errorMesg.append("\nSản lượng Ngoài TĐ(XDDD) phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setQuantityNtdXdddTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn5 = false;
                                isExistError = true;
                                errorMesg.append("\nSản lượng Ngoài TĐ(XDDD) không hợp lệ !");
                            }
                        } 
                        
						else if (cell.getColumnIndex() == 6) {
							try {
								if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
									checkColumn6 = false;
                                    isExistError = true;
                                    errorMesg.append("\nQuỹ lương không được để trống !");
								} else {
									if (formatter.formatCellValue(cell).length() > 14) {
										checkColumn6 = false;
	                                    isExistError = true;
	                                    errorMesg.append("\nĐộ dài quỹ lương vượt quá 14 số!");
									} else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn6 = false;
                                        errorMesg.append("\nQuỹ lương phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setCompleteTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
								}
							} catch (Exception e) {
								checkColumn6 = false;
                                isExistError = true;
                                errorMesg.append("\nQuỹ lương không hợp lệ!");
							}
						}
                        
                        else if (cell.getColumnIndex() == 7) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn7 = false;
                                    isExistError = true;
                                    errorMesg.append("\nHSHC xây lắp không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn7 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài HSHC xây lắp vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn7 = false;
                                        errorMesg.append("\nHSHC xây lắp phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setHshcXlTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn7 = false;
                                isExistError = true;
                                errorMesg.append("\nHSHC xây lắp không hợp lệ!");
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 8) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn8 = false;
                                    isExistError = true;
                                    errorMesg.append("\nDoanh thu nguồn chi phí không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn8 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Doanh thu nguồn chi phí vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn8 = false;
                                        errorMesg.append("\nDoanh thu nguồn chi phí phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setRevenueCpTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn8 = false;
                                isExistError = true;
                                errorMesg.append("\nDoanh thu nguồn chi phí không hợp lệ!");
                            }
                        }
                        
                        else if (cell.getColumnIndex() == 9) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn9 = false;
                                    isExistError = true;
                                    errorMesg.append("\nDoanh thu Ngoài TĐ(GPDN) không được để trống!");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn9 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Doanh thu Ngoài TĐ(GPDN) vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn9 = false;
                                        errorMesg.append("\nDoanh thu Ngoài TĐ(GPDN) phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setRevenueNtdGpdnTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn9 = false;
                                isExistError = true;
                                errorMesg.append("\nDoanh thu Ngoài TĐ(GPDN) không hợp lệ!");
                            }
                        } 
                        else if (cell.getColumnIndex() == 10) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn10 = false;
                                    isExistError = true;
                                    errorMesg.append("\nDoanh thu Ngoài TĐ(XDDD) không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn10 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Doanh thu Ngoài TĐ(XDDD) vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn10 = false;
                                        errorMesg.append("\nDoanh thu Ngoài TĐ(XDDD) phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setRevenueNtdXdddTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn10 = false;
                                isExistError = true;
                                errorMesg.append("\nDoanh thu Ngoài TĐ(XDDD) không hợp lệ!");
                            }
                        } 
                        else if (cell.getColumnIndex() == 11) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn11 = false;
                                    isExistError = true;
                                    errorMesg.append("\nHSHC HTCT không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn11 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài HSHC HTCT vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn11 = false;
                                        errorMesg.append("\nHSHC HTCT phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setHshcHtctTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn11 = false;
                                isExistError = true;
                                errorMesg.append("\nHSHC HTCT không hợp lệ!");
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 12) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn12 = false;
                                    isExistError = true;
                                    errorMesg.append("\nTìm kiếm việc XDDD không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn12 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Tìm kiếm việc XDDD vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn12 = false;
                                        errorMesg.append("\nTìm kiếm việc XDDD phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setTaskXdddTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn12 = false;
                                isExistError = true;
                                errorMesg.append("\nTìm kiếm việc XDDD không hợp lệ!");
                            }
                        } 
                        else if (cell.getColumnIndex() == 13) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn13 = false;
                                    isExistError = true;
                                    errorMesg.append("\nThu hồi dòng tiền không được để trống!");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 14) {
                                        checkColumn13 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Thu hồi dòng tiền vượt quá 14 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn13 = false;
                                        errorMesg.append("\nThu hồi dòng tiền phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setRevokeCashTarget(Double.parseDouble(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn13 = false;
                                isExistError = true;
                                errorMesg.append("\nThu hồi dòng tiền không hợp lệ!");
                            }
                        } 
                        else if (cell.getColumnIndex() == 14) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn14 = false;
                                    isExistError = true;
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 5) {
                                        checkColumn14 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Xây dựng xong móng HTCT vượt quá 5 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn14 = false;
                                        errorMesg.append("\nXây dựng xong móng HTCT phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setBuildCompleteHtct(Long.parseLong(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn14 = false;
                                isExistError = true;
                                errorMesg.append("\nXây dựng xong móng HTCT không hợp lệ!");
                            }
                        } 
                        else if (cell.getColumnIndex() == 15) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn15 = false;
                                    isExistError = true;
                                    errorMesg.append("\nXong ĐB trạm HTCT không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 5) {
                                        checkColumn15 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Xong ĐB trạm HTCT vượt quá 5 số!");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn15 = false;
                                        errorMesg.append("\nXong ĐB trạm HTCT phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setCompleteDbHtct(Long.parseLong(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn15 = false;
                                isExistError = true;
                                errorMesg.append("\nXong ĐB trạm HTCT không hợp lệ!");
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 16) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn16 = false;
                                    isExistError = true;
                                    errorMesg.append("\nThuê mặt bằng HTCT không được để trống !");
                                } else {
                                	if (formatter.formatCellValue(cell).length() > 5) {
                                        checkColumn16 = false;
                                        isExistError = true;
                                        errorMesg.append("\nĐộ dài Thuê mặt bằng HTCT vượt quá 5 số !");
                                    } else if (Double.parseDouble(df.format(cell.getNumericCellValue()))<0) {
                                    	isExistError = true;
                                    	checkColumn16 = false;
                                        errorMesg.append("\nThuê mặt bằng HTCT phải lớn hơn 0 !");
                                    } else {
                                    	newObj.setRentHtct(Long.parseLong(df.format(cell.getNumericCellValue())));
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn16 = false;
                                isExistError = true;
                                errorMesg.append("\nThuê mặt bằng HTCT không hợp lệ!");
                            }
                            
                            Cell cell1 = row.createCell(17);
                            cell1.setCellValue(errorMesg.toString());
                        } 
                        
                    } else {
                    	if(i==1) {
                    		isExistError = true;
                    		errorMesg.append("\nMã đơn vị không được để trống!");
                    	} else if(i==2) {
                    		isExistError = true;
                    		errorMesg.append("\nSản lượng xây lắp không được để trống!");
                    	} else if(i==3) {
                    		isExistError = true;
                    		errorMesg.append("\nSản lượng chi phí (ngoài OS) không được để trống!");
                    	} else if(i==4) {
                    		isExistError = true;
                    		errorMesg.append("\nSản lượng NTĐ GPDN không được để trống!");
                    	} else if(i==5) {
                    		isExistError = true;
                    		errorMesg.append("\nSản lượng NTĐ XDDD không được để trống!");
                    	} else if(i==6) {
                    		isExistError = true;
                    		errorMesg.append("\nQuỹ lương không được để trống!");
                    	} else if(i==7) {
                    		isExistError = true;
                    		errorMesg.append("\nHSHC xây lắp không được để trống!");
                    	} else if(i==8) {
                    		isExistError = true;
                    		errorMesg.append("\nQT lên doanh thu  chi phí (ngoài OS) không được để trống!");
                    	} else if(i==9) {
                    		isExistError = true;
                    		errorMesg.append("\nQT lên doanh thu NTĐ GPDN không được để trống!");
                    	} else if(i==10) {
                    		isExistError = true;
                    		errorMesg.append("\nQT lên doanh thu NTĐ XDDD không được để trống!");
                    	} else if(i==11) {
                    		isExistError = true;
                    		errorMesg.append("\nHSHC Hạ tầng cho thuê không được để trống!");
                    	} else if(i==12) {
                    		isExistError = true;
                    		errorMesg.append("\nTìm kiếm việc XDDD không được để trống!");
                    	} else if(i==13) {
                    		isExistError = true;
                    		errorMesg.append("\nThu hồi dòng tiền không được để trống!");
                    	} else if(i==14) {
                    		isExistError = true;
                    		errorMesg.append("\nXD xong móng HTCT không được để trống!");
                    	} else if(i==15) {
                    		isExistError = true;
                    		errorMesg.append("\nXong ĐB trạm HTCT không được để trống!");
                    	} else if(i==16) {
                    		isExistError = true;
                    		errorMesg.append("\nThuê mặt bằng HTCT không được để trống!");
                    	}
                    	
                    }
                }
                if (!isExistError) {
                	newObj.setSumDeployHtct((newObj.getBuildCompleteHtct()!=null?newObj.getBuildCompleteHtct():0l) + (newObj.getCompleteDbHtct()!=null?newObj.getCompleteDbHtct():0l) + (newObj.getRentHtct()!=null?newObj.getRentHtct():0l));
                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            sheet.setColumnWidth(17, 1000);
            style.setAlignment(HorizontalAlignment.CENTER);
            Cell cell = sheet.getRow(1).createCell(17);
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            TmpnTargetOSDTO objErr = new TmpnTargetOSDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        workLst.get(0).setSysGroupIdList(sysGroupIdList);
        return workLst;
    }

    public List<TotalMonthPlanOSSimpleDTO> fillterAllActiveCatConstructionType(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        List<TotalMonthPlanOSSimpleDTO> ls = totalMonthPlanOSDAO.fillterAllActiveCatConstructionType(obj);
        return ls;
    }

    public List<TotalMonthPlanOSSimpleDTO> fillterAllActiveCatConstructionDeploy(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        List<TotalMonthPlanOSSimpleDTO> ls = totalMonthPlanOSDAO.fillterAllActiveCatConstructionDeploy(obj);
        return ls;
    }

    public Long updateMonth(TotalMonthPlanOSSimpleDTO obj, String userName, HttpServletRequest request) throws Exception {
        // TODO Auto-generated method stub
        checkMonthYear(obj.getMonth(), obj.getYear(), obj.getTotalMonthPlanId());
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.TOTAL_MONTH_PLAN,
                request)) {
            throw new IllegalArgumentException("Bạn không có quyền sửa thông tin tháng tổng thể ngoài OS");
        }
        totalMonthPlanOSDAO.updateObject(obj.toModel());
        List<Long> listId = utilAttachDocumentDAO.getAttachIdByObjectAndType(obj.getTotalMonthPlanId(), "OS");
        List<Long> deleteId = new ArrayList<Long>(listId);
        if (obj.getListAttachFile() != null) {
            for (UtilAttachDocumentDTO file : obj.getListAttachFile()) {
                if (file.getUtilAttachDocumentId() == null) {
                    file.setObjectId(obj.getTotalMonthPlanId());
                    file.setType("OS");
                    file.setCreatedDate(new Date());
                    file.setCreatedUserId(obj.getUpdatedUserId());
                    file.setCreatedUserName(userName);
                    file.setDescription("file Ke hoach thang tong the ngoai OS");
                    file.setFilePath(UEncrypt.decryptFileUploadPath(file.getFilePath()));
                    utilAttachDocumentDAO.saveObject(file.toModel());
                }
            }
            for (Long id : listId) {
                for (UtilAttachDocumentDTO file : obj.getListAttachFile()) {
                    if (file.getUtilAttachDocumentId() != null) {
                        if (id.longValue() == file.getUtilAttachDocumentId().longValue())
                            deleteId.remove(id);
                    }
                }
            }
            utilAttachDocumentDAO.deleteListUtils(deleteId);
        } else {
            utilAttachDocumentDAO.deleteListUtils(deleteId);
        }
        return obj.getTotalMonthPlanId();
    }

    public void saveTarget(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        if (!StringUtils.isNullOrEmpty(obj.getTargetNote())) {
            totalMonthPlanOSDAO.updateTargetNote(obj.getTargetNote(), obj.getTotalMonthPlanId());
        }
        List<TmpnTargetOSDTO> targetList = obj.getTmpnTargetDTOList();
        List<Long> listInDb = tmpnTargetOSDAO.getTargetInDB(obj.getTotalMonthPlanId());
        List<TmpnTargetOSDTO> listInUse = new ArrayList<TmpnTargetOSDTO>();
        if (targetList != null) {
            for (TmpnTargetOSDTO dto : targetList) {
                if (dto.getTmpnTargetId() != null) {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    listInUse.add(dto);
                    tmpnTargetOSDAO.update(dto.toModel());
                } else {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    dto.setTotalMonthPlanId(obj.getTotalMonthPlanId());
                    tmpnTargetOSDAO.save(dto.toModel());
                }
            }
        }
        List<Long> deleteList = new ArrayList<Long>(listInDb);
        for (Long yId : listInDb) {
            boolean exist = false;
            for (TmpnTargetOSDTO dto : listInUse) {
                if (yId.longValue() == dto.getTmpnTargetId().longValue()) {
                    deleteList.remove(yId);
                    break;
                }
            }
        }
        if (!deleteList.isEmpty())
            tmpnTargetOSDAO.deleteTarget(deleteList);
    }

    public void saveSource(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        if (!StringUtils.isNullOrEmpty(obj.getSourceNote())) {
            totalMonthPlanOSDAO.updateSourceNote(obj.getSourceNote(), obj.getTotalMonthPlanId());
        }
        List<TmpnSourceDTO> list = obj.getTmpnSourceDTOList();
        List<Long> listInDb = tmpnSourceDAO.getTargetInDB(obj.getTotalMonthPlanId());
        List<TmpnSourceDTO> listInUse = new ArrayList<TmpnSourceDTO>();
        if (list != null) {
            for (TmpnSourceDTO dto : list) {
                if (dto.getTmpnSourceId() != null) {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    listInUse.add(dto);
                    tmpnSourceDAO.update(dto.toModel());
                } else {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    dto.setTotalMonthPlanId(obj.getTotalMonthPlanId());
                    tmpnSourceDAO.save(dto.toModel());
                }
            }
        }
        List<Long> deleteList = new ArrayList<Long>(listInDb);
        for (Long yId : listInDb) {
            boolean exist = false;
            for (TmpnSourceDTO dto : listInUse) {
                if (yId.longValue() == dto.getTmpnSourceId().longValue()) {
                    deleteList.remove(yId);
                    break;
                }
            }
        }
        if (!deleteList.isEmpty())
            tmpnSourceDAO.deleteSource(deleteList);
    }

    public void saveContract(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        if (!StringUtils.isNullOrEmpty(obj.getContractNote())) {
            totalMonthPlanOSDAO.updateContractNote(obj.getContractNote(), obj.getTotalMonthPlanId());
        }
        List<TmpnContractDTO> list = obj.getTmpnContractDTOList();
        List<Long> listInDb = tmpnContractDAO.getTargetInDB(obj.getTotalMonthPlanId());
        List<TmpnContractDTO> listInUse = new ArrayList<TmpnContractDTO>();
        if (list != null) {
            for (TmpnContractDTO dto : list) {
                if (dto.getTmpnContractId() != null) {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    listInUse.add(dto);
                    tmpnContractDAO.update(dto.toModel());
                } else {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    dto.setTotalMonthPlanId(obj.getTotalMonthPlanId());
                    tmpnContractDAO.save(dto.toModel());
                }
            }
        }
        List<Long> deleteList = new ArrayList<Long>(listInDb);
        for (Long yId : listInDb) {
            boolean exist = false;
            for (TmpnContractDTO dto : listInUse) {
                if (yId.longValue() == dto.getTmpnContractId().longValue()) {
                    deleteList.remove(yId);
                    break;
                }
            }
        }
        if (!deleteList.isEmpty())
            tmpnContractDAO.deleteContract(deleteList);
    }

    public void saveFinance(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        if (!StringUtils.isNullOrEmpty(obj.getFinanceNote())) {
            totalMonthPlanOSDAO.updateFinanceNote(obj.getFinanceNote(), obj.getTotalMonthPlanId());
        }
        List<TmpnFinanceDTO> list = obj.getTmpnFinanceDTOList();
        List<Long> listInDb = tmpnFinanceDAO.getTargetInDB(obj.getTotalMonthPlanId());
        List<TmpnFinanceDTO> listInUse = new ArrayList<TmpnFinanceDTO>();
        if (list != null) {
            for (TmpnFinanceDTO dto : list) {
                if (dto.getTmpnFinanceId() != null) {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    listInUse.add(dto);
                    tmpnFinanceDAO.update(dto.toModel());
                } else {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    dto.setTotalMonthPlanId(obj.getTotalMonthPlanId());
                    tmpnFinanceDAO.save(dto.toModel());
                }
            }
        }
        List<Long> deleteList = new ArrayList<Long>(listInDb);
        for (Long yId : listInDb) {
            boolean exist = false;
            for (TmpnFinanceDTO dto : listInUse) {
                if (yId.longValue() == dto.getTmpnFinanceId().longValue()) {
                    deleteList.remove(yId);
                    break;
                }
            }
        }
        if (!deleteList.isEmpty())
            tmpnFinanceDAO.deleteFinance(deleteList);
    }

    public void saveMaterial(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        if (!StringUtils.isNullOrEmpty(obj.getMaterialNote())) {
            totalMonthPlanOSDAO.updateMaterialNote(obj.getMaterialNote(), obj.getTotalMonthPlanId());
        }
        List<TmpnMaterialDTO> list = obj.getTmpnMaterialDTOList();
        List<Long> listInDb = tmpnMaterialDAO.getTargetInDB(obj.getTotalMonthPlanId());
        List<TmpnMaterialDTO> listInUse = new ArrayList<TmpnMaterialDTO>();
        if (list != null) {
            for (TmpnMaterialDTO dto : list) {
                if (dto.getTmpnMaterialId() != null) {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    listInUse.add(dto);
                    tmpnMaterialDAO.update(dto.toModel());
                } else {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    dto.setTotalMonthPlanId(obj.getTotalMonthPlanId());
                    tmpnMaterialDAO.save(dto.toModel());
                }
            }
        }
        List<Long> deleteList = new ArrayList<Long>(listInDb);
        for (Long yId : listInDb) {
            boolean exist = false;
            for (TmpnMaterialDTO dto : listInUse) {
                if (yId.longValue() == dto.getTmpnMaterialId().longValue()) {
                    deleteList.remove(yId);
                    break;
                }
            }
        }
        if (!deleteList.isEmpty())
            tmpnMaterialDAO.deleteMaterial(deleteList);
    }

    public void saveForceNewBTS(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        if (!StringUtils.isNullOrEmpty(obj.getBtsNote())) {
            totalMonthPlanOSDAO.updateBTSNote(obj.getBtsNote(), obj.getTotalMonthPlanId());
        }
        List<TmpnForceNewBtsDTO> list = obj.getTmpnForceNewBtsDTOList();
        List<Long> listInDb = tmpnForceNewBtsDAO.getTargetInDB(obj.getTotalMonthPlanId());
        List<TmpnForceNewBtsDTO> listInUse = new ArrayList<TmpnForceNewBtsDTO>();
        if (list != null) {
            for (TmpnForceNewBtsDTO dto : list) {
                if (dto.getTmpnForceNewBtsId() != null) {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    listInUse.add(dto);
                    tmpnForceNewBtsDAO.update(dto.toModel());
                } else {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    dto.setTotalMonthPlanId(obj.getTotalMonthPlanId());
                    tmpnForceNewBtsDAO.save(dto.toModel());
                }
            }
        }
        List<Long> deleteList = new ArrayList<Long>(listInDb);
        for (Long yId : listInDb) {
            boolean exist = false;
            for (TmpnForceNewBtsDTO dto : listInUse) {
                if (yId.longValue() == dto.getTmpnForceNewBtsId().longValue()) {
                    deleteList.remove(yId);
                    break;
                }
            }
        }
        if (!deleteList.isEmpty())
            tmpnForceNewBtsDAO.deleteForceBTS(deleteList);
    }

    public void saveForceNew(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        if (!StringUtils.isNullOrEmpty(obj.getLineNote())) {
            totalMonthPlanOSDAO.updateLineNote(obj.getLineNote(), obj.getTotalMonthPlanId());
        }
        List<TmpnForceNewLineDTO> list = obj.getTmpnForceNewLineDTOList();
        List<Long> listInDb = tmpnForceNewLineDAO.getTargetInDB(obj.getTotalMonthPlanId());
        List<TmpnForceNewLineDTO> listInUse = new ArrayList<TmpnForceNewLineDTO>();
        if (list != null) {
            for (TmpnForceNewLineDTO dto : list) {
                if (dto.getTmpnForceNewLineId() != null) {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    listInUse.add(dto);
                    tmpnForceNewLineDAO.update(dto.toModel());
                } else {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    dto.setTotalMonthPlanId(obj.getTotalMonthPlanId());
                    tmpnForceNewLineDAO.save(dto.toModel());
                }
            }
        }
        List<Long> deleteList = new ArrayList<Long>(listInDb);
        for (Long yId : listInDb) {
            boolean exist = false;
            for (TmpnForceNewLineDTO dto : listInUse) {
                if (yId.longValue() == dto.getTmpnForceNewLineId().longValue()) {
                    deleteList.remove(yId);
                    break;
                }
            }
        }
        if (!deleteList.isEmpty())
            tmpnForceNewLineDAO.deleteForceNewLine(deleteList);
    }

    public void saveForceMaintain(TotalMonthPlanOSSimpleDTO obj) {
        // TODO Auto-generated method stub
        if (!StringUtils.isNullOrEmpty(obj.getMaintainNote())) {
            totalMonthPlanOSDAO.updateMaintainNote(obj.getMaintainNote(), obj.getTotalMonthPlanId());
        }
        List<TmpnForceMaintainDTO> list = obj.getTmpnForceMaintainDTOList();
        List<Long> listInDb = tmpnForceMaintainDAO.getTargetInDB(obj.getTotalMonthPlanId());
        List<TmpnForceMaintainDTO> listInUse = new ArrayList<TmpnForceMaintainDTO>();
        if (list != null) {
            for (TmpnForceMaintainDTO dto : list) {
                if (dto.getTmpnForceMaintainId() != null) {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    listInUse.add(dto);
                    tmpnForceMaintainDAO.update(dto.toModel());
                } else {
                    dto.setMonth(obj.getMonth());
                    dto.setYear(obj.getYear());
                    dto.setTotalMonthPlanId(obj.getTotalMonthPlanId());
                    tmpnForceMaintainDAO.save(dto.toModel());
                }
            }
        }
        List<Long> deleteList = new ArrayList<Long>(listInDb);
        for (Long yId : listInDb) {
            boolean exist = false;
            for (TmpnForceMaintainDTO dto : listInUse) {
                if (yId.longValue() == dto.getTmpnForceMaintainId().longValue()) {
                    deleteList.remove(yId);
                    break;
                }
            }
        }
        if (!deleteList.isEmpty())
            tmpnForceMaintainDAO.deleteForceMaintain(deleteList);
    }

    public List<TmpnSourceDTO> importSource(String fileInput, String filePath) throws Exception {

        List<TmpnSourceDTO> workLst = new ArrayList<TmpnSourceDTO>();
        File f = new File(fileInput);
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);

        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        List<String> listGroupCode = new ArrayList<String>();
        boolean isExistError = false;
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn1 = true;
                boolean checkColumn2 = true;
                // chinhpxn20180704_start
                boolean checkLength = true;
                // chinhpxn20180704_end

                String sysGroupCode = "";
                StringBuilder errorMesg = new StringBuilder();
                TmpnSourceDTO newObj = new TmpnSourceDTO();
                for (int i = 0; i < 4; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        // Check format file exel
                        if (cell.getColumnIndex() == 1) {
                            try {
                                sysGroupCode = formatter.formatCellValue(cell).trim();
                                if (listGroupCode.contains(sysGroupCode)) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị đã bị trùng!");
                                } else {
                                    listGroupCode.add(sysGroupCode.trim());
                                }
                                DepartmentDTO dep = departmentDAO.getSysGroupData(sysGroupCode.trim());
                                if (dep.getId() != null) {
                                    newObj.setSysGroupId(dep.getId());
                                    newObj.setSysGroupName(dep.getName());
                                } else
                                    checkColumn1 = false;
                                if (!checkColumn1) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị không hợp lệ!");
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("Mã đơn vị không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 2) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn2 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 14) {
                                    checkLength = false;
                                    checkColumn2 = false;
                                }
                                newObj.setSource(Double.parseDouble(df.format(cell.getNumericCellValue())) * 1000000);
                            } catch (Exception e) {
                                checkColumn2 = false;
                            }
                            if (!checkColumn2) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nNguồn việc sản lượng vượt quá 14 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nNguồn việc không hợp lệ!");
                                } // chinhpxn20180704_end
                            }
                            Cell cell1 = row.createCell(4);
                            cell1.setCellValue(errorMesg.toString());
                        } else if (cell.getColumnIndex() == 3) {
                            // chinhpxn20180704_start
                            if (formatter.formatCellValue(cell).trim().length() > 1000) {
                                isExistError = true;
                                errorMesg.append("\nGhi chú vượt quá 1000 ký tự!");
                            }
                            // chinhpxn20180704_end

                            newObj.setDescription(formatter.formatCellValue(cell).trim());
                        }

                    }
                }
                if (checkColumn1 && checkColumn2) {

                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            sheet.setColumnWidth(4, 5000);
            style.setAlignment(HorizontalAlignment.CENTER);
            Cell cell = sheet.getRow(1).createCell(4);
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            TmpnSourceDTO objErr = new TmpnSourceDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }

    public List<TmpnForceMaintainDTO> importForceMaintain(String fileInput, String filePath) throws Exception {

        List<TmpnForceMaintainDTO> workLst = new ArrayList<TmpnForceMaintainDTO>();
        File f = new File(fileInput);
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);

        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        List<String> listGroupCode = new ArrayList<String>();
        boolean isExistError = false;
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn1 = true;
                boolean checkColumn2 = true;
                boolean checkColumn3 = true;
                boolean checkColumn4 = true;
                boolean checkColumn5 = true;
                boolean checkColumn6 = true;
                boolean checkColumn7 = true;
                boolean checkColumn8 = true;
                // chinhpxn20180704_start
                boolean checkLength = true;
                // chinhpxn20180704_end
                String sysGroupCode = "";
                StringBuilder errorMesg = new StringBuilder();
                TmpnForceMaintainDTO newObj = new TmpnForceMaintainDTO();
                for (int i = 0; i < 9; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        // Check format file exel
                        if (cell.getColumnIndex() == 1) {
                            try {
                                sysGroupCode = formatter.formatCellValue(cell).trim();
                                if (listGroupCode.contains(sysGroupCode)) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị đã bị trùng!");
                                } else {
                                    listGroupCode.add(sysGroupCode.trim());
                                }
                                DepartmentDTO dep = departmentDAO.getSysGroupData(sysGroupCode.trim());
                                if (dep.getId() != null) {
                                    newObj.setSysGroupId(dep.getId());
                                    newObj.setSysGroupName(dep.getName());
                                } else
                                    checkColumn1 = false;
                                if (!checkColumn1) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị không hợp lệ!");
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("Mã đơn vị không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 2) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn2 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn2 = false;
                                }
                                newObj.setBuildPlan(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn2 = false;
                            }
                            if (!checkColumn2) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Xây dựng vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nXây dựng không hợp lệ!");
                                } // chinhpxn20180704_end
                            }
                        } else if (cell.getColumnIndex() == 3) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn3 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn3 = false;
                                }
                                newObj.setInstallPlan(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn3 = false;
                            }
                            if (!checkColumn3) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài KH lắp cột bao vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nKH lắp cột bao không hợp lệ!");
                                } // chinhpxn20180704_end
                            }
                        } else if (cell.getColumnIndex() == 4) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn4 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn4 = false;
                                }
                                newObj.setReplacePlan(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn4 = false;
                            }
                            if (!checkColumn4) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài KH thay thân vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nKH thay thân cột không hợp lệ!");
                                }
                            }
                        } else if (cell.getColumnIndex() == 5) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn5 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn5 = false;
                                }
                                newObj.setTeamBuildRequire(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn5 = false;
                            }
                            if (!checkColumn5) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Số đội cần XD vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nSố đội cần XD không hợp lệ!");
                                }
                            }
                        } else if (cell.getColumnIndex() == 6) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn6 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn6 = false;
                                }
                                newObj.setTeamBuildAvaiable(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn6 = false;
                            }
                            if (!checkColumn6) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Số đội đang XD vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nSố đội đang XD không hợp lệ!");
                                }
                            }
                        } else if (cell.getColumnIndex() == 7) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn7 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn7 = false;
                                }
                                newObj.setTeamInstallRequire(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn7 = false;
                            }
                            if (!checkColumn7) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Số đội cần lắp dựng vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nSố đội cần lắp dựng không hợp lệ!");
                                }
                            }
                        } else if (cell.getColumnIndex() == 8) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn8 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn8 = false;
                                }
                                newObj.setTeamInstallAvaiable(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn8 = false;
                            }
                            if (!checkColumn8) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Số đội đang lắp dựng vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nSố đội đang lắp dựng không hợp lệ!");
                                }
                            }
                            Cell cell1 = row.createCell(9);
                            cell1.setCellValue(errorMesg.toString());
                        }
                    }
                }
                if (checkColumn1 && checkColumn2 && checkColumn3 && checkColumn4 && checkColumn5 && checkColumn6
                        && checkColumn7 && checkColumn8) {

                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            sheet.setColumnWidth(9, 5000);
            style.setAlignment(HorizontalAlignment.CENTER);
            Cell cell = sheet.getRow(1).createCell(9);
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            TmpnForceMaintainDTO objErr = new TmpnForceMaintainDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }

    public List<TmpnForceNewBtsDTO> importForceNewBts(String fileInput, String filePath) throws Exception {

        List<TmpnForceNewBtsDTO> workLst = new ArrayList<TmpnForceNewBtsDTO>();
        File f = new File(fileInput);
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);

        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        List<String> listGroupCode = new ArrayList<String>();
        boolean isExistError = false;
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn1 = true;
                boolean checkColumn2 = true;
                boolean checkColumn3 = true;
                boolean checkColumn4 = true;
                boolean checkColumn5 = true;
                // chinhpxn20180704_start
                boolean checkLength = true;
                // chinhpxn20180704_end
                String sysGroupCode = "";
                StringBuilder errorMesg = new StringBuilder();
                TmpnForceNewBtsDTO newObj = new TmpnForceNewBtsDTO();
                for (int i = 0; i < 7; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        // Check format file exel
                        if (cell.getColumnIndex() == 1) {
                            try {
                                sysGroupCode = formatter.formatCellValue(cell).trim();
                                if (listGroupCode.contains(sysGroupCode.trim())) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị đã bị trùng!");
                                } else {
                                    listGroupCode.add(sysGroupCode.trim());
                                }
                                DepartmentDTO dep = departmentDAO.getSysGroupData(sysGroupCode);
                                if (dep.getId() != null) {
                                    newObj.setSysGroupId(dep.getId());
                                    newObj.setSysGroupName(dep.getName());
                                } else
                                    checkColumn1 = false;
                                if (!checkColumn1) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị không hợp lệ!;");
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("Mã đơn vị không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 2) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn2 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn2 = false;
                                }
                                newObj.setStationNumber(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn2 = false;
                            }
                            if (!checkColumn2) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Tổng số trạm vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nTổng số trạm không hợp lệ!;");
                                }
                            }
                        } else if (cell.getColumnIndex() == 3) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn3 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn3 = false;
                                }
                                newObj.setTeamBuildRequire(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn3 = false;
                            }
                            if (!checkColumn3) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Đội xây dựng cần vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("Đội xây dựng cần không hợp lệ!");
                                }
                            }
                        } else if (cell.getColumnIndex() == 4) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn4 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn4 = false;
                                }
                                newObj.setTeamBuildAvaiable(Long.parseLong(longf.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn4 = false;
                            }
                            if (!checkColumn4) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Số đội đang triển khai vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nSố đội đang triển khai không hợp lệ!");
                                }
                            }
                        } else if (cell.getColumnIndex() == 5) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn5 = false;
                                }
                                double value = Double.parseDouble(df.format(cell.getNumericCellValue()));
                                if (value > 100 || value < 0) {
                                    checkColumn5 = false;
                                } else
                                    newObj.setSelfImplementPercent(
                                            Double.parseDouble(df.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn5 = false;
                            }
                            if (!checkColumn5) {
                                isExistError = true;
                                errorMesg.append("\nThi công trực tiếp không hợp lệ!");
                            }
                            Cell cell1 = row.createCell(7);
                            cell1.setCellValue(errorMesg.toString());
                        } else if (cell.getColumnIndex() == 6) {
                            if (formatter.formatCellValue(cell).trim().length() > 1000) {
                                isExistError = true;
                                errorMesg.append("\nGhi chú vượt quá 1000 ký tự");
                            }
                            newObj.setDescription(formatter.formatCellValue(cell).trim());
                        }

                    }
                }
                if (checkColumn1 && checkColumn2 && checkColumn3 && checkColumn4 && checkColumn5) {

                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            sheet.setColumnWidth(7, 5000);
            style.setAlignment(HorizontalAlignment.CENTER);
            Cell cell = sheet.getRow(1).createCell(7);
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            TmpnForceNewBtsDTO objErr = new TmpnForceNewBtsDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }

    public List<TmpnForceNewLineDTO> importForceNewLine(String fileInput, String filePath) throws Exception {

        List<TmpnForceNewLineDTO> workLst = new ArrayList<TmpnForceNewLineDTO>();
        File f = new File(fileInput);
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<String> listGroupCode = new ArrayList<String>();
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn1 = true;
                boolean checkColumn2 = true;
                boolean checkColumn3 = true;
                boolean checkColumn4 = true;
                boolean checkColumn5 = true;
                boolean checkColumn6 = true;
                boolean checkColumn7 = true;
                boolean checkColumn8 = true;
                // chinhpxn20180704_start
                boolean checkLength = true;
                // chinhpxn20180704_end
                String sysGroupCode = "";
                StringBuilder errorMesg = new StringBuilder();
                TmpnForceNewLineDTO newObj = new TmpnForceNewLineDTO();
                for (int i = 0; i < 10; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        // Check format file exel
                        if (cell.getColumnIndex() == 1) {
                            try {
                                sysGroupCode = formatter.formatCellValue(cell).trim();
                                if (listGroupCode.contains(sysGroupCode)) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị đã bị trùng!");
                                } else {
                                    listGroupCode.add(sysGroupCode.trim());
                                }
                                DepartmentDTO dep = departmentDAO.getSysGroupData(sysGroupCode.trim());
                                if (dep.getId() != null) {
                                    newObj.setSysGroupId(dep.getId());
                                    newObj.setSysGroupName(dep.getName());
                                } else
                                    checkColumn1 = false;
                                if (!checkColumn1) {
                                    isExistError = true;
                                    errorMesg.append("Mã đơn vị không hợp lệ!;");
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("Mã đơn vị không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 2) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn2 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn2 = false;
                                }
                                newObj.setStationNumber(Double.parseDouble(df.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn2 = false;
                            }
                            if (!checkColumn2) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Tổng tuyến vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nTổng tuyến không hợp lệ!;");
                                }
                            }
                        } else if (cell.getColumnIndex() == 3) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn3 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn3 = false;
                                }
                                newObj.setCurrentHaveLicense(Double.parseDouble(df.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn3 = false;
                            }
                            if (!checkColumn3) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Có phép thi công vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("Có phép thi công không hợp lệ!;");
                                }
                            }
                        } else if (cell.getColumnIndex() == 4) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn4 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn4 = false;
                                }
                                newObj.setCurrentQuantity(Double.parseDouble(df.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn4 = false;
                            }
                            if (!checkColumn4) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Khối lượng vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nKhối lượng không hợp lệ!;");
                                }
                            }
                        } else if (cell.getColumnIndex() == 5) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn5 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn5 = false;
                                }
                                newObj.setCurentStationComplete(
                                        Double.parseDouble(df.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn5 = false;
                            }
                            if (!checkColumn5) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Hoàn thành tuyến vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nHoàn thành tuyến không hợp lệ!;");
                                }
                            }
                        } else if (cell.getColumnIndex() == 6) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn6 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn6 = false;
                                }
                                newObj.setRemainHaveLicense(Double.parseDouble(df.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn6 = false;
                            }
                            if (!checkColumn6) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Có phép thi công còn tồn vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nCó phép thi công còn tồn không hợp lệ!;");
                                }
                            }
                        } else if (cell.getColumnIndex() == 7) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn7 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn7 = false;
                                }
                                newObj.setRemainQuantity(Double.parseDouble(df.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn7 = false;
                            }
                            if (!checkColumn7) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Khối lượng tồn vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nKhối lượng tồn không hợp lệ!;");
                                }
                            }
                        } else if (cell.getColumnIndex() == 8) {
                            try {
                                if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
                                    checkColumn8 = false;
                                }
                                // chinhpxn20180704_start
                                if (formatter.formatCellValue(cell).length() > 10) {
                                    checkLength = false;
                                    checkColumn8 = false;
                                }
                                newObj.setRemainStationComplete(
                                        Double.parseDouble(df.format(cell.getNumericCellValue())));
                            } catch (Exception e) {
                                checkColumn8 = false;
                            }
                            if (!checkColumn8) {
                                isExistError = true;
                                if (!checkLength) {
                                    errorMesg.append("\nĐộ dài Hoàn thành tuyến vượt quá 10 số!");
                                    checkLength = true;
                                } else {
                                    errorMesg.append("\nHoàn thành tuyến tồn không hợp lệ!;");
                                }
                            }
                            Cell cell1 = row.createCell(10);
                            cell1.setCellValue(errorMesg.toString());
                        } else if (cell.getColumnIndex() == 9) {
                            if (formatter.formatCellValue(cell).trim().length() > 1000) {
                                isExistError = true;
                                errorMesg.append("\nĐộ dài ghi chú vượt quá 1000 ký tự!");
                            }
                            newObj.setDescription(formatter.formatCellValue(cell).trim());
                        }

                    }
                }
                if (checkColumn1 && checkColumn2 && checkColumn3 && checkColumn4 && checkColumn5 && checkColumn6
                        && checkColumn7 && checkColumn8) {

                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            sheet.setColumnWidth(10, 5000);
            style.setAlignment(HorizontalAlignment.CENTER);
            Cell cell = sheet.getRow(1).createCell(10);
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            TmpnForceNewLineDTO objErr = new TmpnForceNewLineDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }

    public List<TmpnMaterialDTO> importMaterial(String fileInput, String filePath) throws Exception {

        List<TmpnMaterialDTO> workLst = new ArrayList<TmpnMaterialDTO>();
        File f = new File(fileInput);
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<String> listGroupCode = new ArrayList<String>();
        List<String> listConsType = new ArrayList<String>();
        Map<String, Map<String, List<String>>> groupMapCons = new ConcurrentHashMap<String, Map<String, List<String>>>();
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        DataFormatter dfS = new DataFormatter();
        boolean isExistError = false;
        int count = 0;

//		chinhpxn20180704_start
        HashMap<String, String> catConstructionTypeMap = new HashMap<String, String>();
        List<TotalMonthPlanOSSimpleDTO> constructionTypeList = totalMonthPlanOSDAO.getAllConstructionType();
        for (TotalMonthPlanOSSimpleDTO obj : constructionTypeList) {
            String deployId = obj.getCatConstructionDeployId() != null ? obj.getCatConstructionDeployId().toString()
                    : "";
            String constructionTypeId = obj.getCatConstructionTypeId() != null
                    ? obj.getCatConstructionTypeId().toString()
                    : "";
            catConstructionTypeMap.put(deployId, constructionTypeId);
        }
//		chinhpxn20180704_end
        for (Row row : sheet) {
            count++;
            if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn1 = true;
                boolean checkColumn2 = true;
                boolean checkColumn3 = true;
                boolean checkColumn4 = true;
                boolean checkColumn5 = true;

                String sysGroupCode = "";
                String constructionType = "";
                StringBuilder errorMesg = new StringBuilder();
                TmpnMaterialDTO newObj = new TmpnMaterialDTO();
                for (int i = 0; i < 5; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        // Check format file exel
                        // chinhpxn20180705_start
                        if (cell.getColumnIndex() == 1) {
                            try {
                                sysGroupCode = formatter.formatCellValue(cell).trim();
//							if(!listGroupCode.contains(sysGroupCode)){
//								listGroupCode.add(sysGroupCode.trim());
                                groupMapCons.put(sysGroupCode, new ConcurrentHashMap<String, List<String>>());
//							}else{
//								checkColumn1 = false;
//							}
                                DepartmentDTO dep = departmentDAO.getSysGroupData(sysGroupCode);
                                if (dep.getId() != null) {
                                    newObj.setSysGroupId(dep.getId());
                                    newObj.setSysGroupName(dep.getName());
                                } else
                                    checkColumn1 = false;
                                if (!checkColumn1) {
                                    isExistError = true;
//								if(listGroupCode.contains(sysGroupCode)){
//									errorMesg.append("Mã đơn vị đã bị trùng!");
//								}else{
                                    errorMesg.append("Mã đơn vị không hợp lệ!;");
//								}
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("Mã đơn vị không hợp lệ!");
                            }
                            // chinhpxn20180705_end
                        } else if (cell.getColumnIndex() == 2) {
                            try {
                                constructionType = formatter.formatCellValue(cell);
                                if (StringUtils.isStringNullOrEmpty(constructionType)) {
                                    checkColumn2 = false;
                                } else {
                                    TotalMonthPlanOSSimpleDTO type = totalMonthPlanOSDAO
                                            .getConstructionTypeByCode(constructionType);
                                    if (type.getCatConstructionTypeId() != null) {
                                        newObj.setCatConstructionTypeId(type.getCatConstructionTypeId());
                                        newObj.setCatConstructionTypeName(type.getCatConstructionTypeName());
                                        Map<String, List<String>> conDepType = groupMapCons.get(sysGroupCode);
                                        if (!conDepType.containsKey(constructionType)) {
                                            listConsType.add(constructionType.trim());
                                            conDepType.put(constructionType, new ArrayList<String>());
                                        }
                                    } else {
                                        checkColumn2 = false;
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn2 = false;
                            }
                            if (!checkColumn2) {
                                isExistError = true;
                                if (StringUtils.isStringNullOrEmpty(constructionType)) {
                                    errorMesg.append("Loại công trình chưa nhập!");
                                } else {
                                    errorMesg.append("\nLoại công trình không hợp lệ!;");
                                }
                            }
                        } else if (cell.getColumnIndex() == 3) {
                            try {
                                String constructionDeploy = formatter.formatCellValue(cell).trim();
                                if (StringUtils.isStringNullOrEmpty(constructionDeploy)) {
                                    checkColumn3 = false;
                                } else {
                                    TotalMonthPlanOSSimpleDTO deploy = totalMonthPlanOSDAO
                                            .getConstructionDeployById(constructionDeploy);
//									chinhpxn20180704_start
                                    if (deploy.getCatConstructionDeployId() != null) {
                                        if (newObj.getCatConstructionTypeId() != null) {
                                            if (!catConstructionTypeMap
                                                    .get(deploy.getCatConstructionDeployId().toString())
                                                    .equals(newObj.getCatConstructionTypeId().toString())) {
                                                checkColumn3 = false;
                                                errorMesg.append("Hình thức triển khai không hợp lệ!");
                                            }
                                        }
//										chinhpxn20180704_end
                                        newObj.setCatConstructionDeployId(deploy.getCatConstructionDeployId());
                                        newObj.setCatConstructionDeployName(deploy.getCatConstructionDeployName());

                                        Map<String, List<String>> conDepType = groupMapCons.get(sysGroupCode);
                                        List<String> conDeploy = conDepType.get(constructionType);
                                        if (conDeploy.contains(constructionDeploy)) {
                                            checkColumn3 = false;
                                            errorMesg.append(
                                                    "Đơn vị, loại công trình, hình thức triển khai đã bị trùng!");
                                        } else {
                                            conDeploy.add(constructionDeploy.trim());
                                        }
                                    } else {
                                        checkColumn3 = false;
                                    }
                                }
                            } catch (Exception e) {
                                checkColumn3 = false;
                            }
                            if (!checkColumn3) {

                                isExistError = true;

//								errorMesg
//											.append("Đơn vị, loại công trình, hình thức triển khai đã bị trùng!");
//								errorMesg
//										.append("Hình thức triển khai không hợp lệ!;");
							}
							Cell cell1 = row.createCell(5);
							cell1.setCellValue(errorMesg.toString());

						} else if (cell.getColumnIndex() == 4) {
							if (formatter.formatCellValue(cell).trim().length() > 1000) {
								isExistError = true;
								errorMesg.append("\nGhi chú vượt quá 1000 ký tự");
							}
							newObj.setDescription(dfS.formatCellValue(cell).trim());
						}
					}
				}
				if (checkColumn1 && checkColumn2 && checkColumn3) {

					workLst.add(newObj);
				}
			}
		}
		if (isExistError) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			sheet.setColumnWidth(5, 5000);
			style.setAlignment(HorizontalAlignment.CENTER);
			Cell cell = sheet.getRow(1).createCell(5);
			cell.setCellValue("Cột lỗi");
			cell.setCellStyle(style);
			TmpnMaterialDTO objErr = new TmpnMaterialDTO();
			OutputStream out = new FileOutputStream(f, true);
			workbook.write(out);
			out.close();
			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
		}
		workbook.close();
		return workLst;
	}

	public List<TmpnFinanceDTO> importFinance(String fileInput, String filePath) throws Exception {

		List<TmpnFinanceDTO> workLst = new ArrayList<TmpnFinanceDTO>();
		File f = new File(fileInput);
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<String> listGroupCode = new ArrayList<String>();
		DataFormatter formatter = new DataFormatter();
		DecimalFormat df = new DecimalFormat("#.###");
		DecimalFormat longf = new DecimalFormat("#");
		DataFormatter dfS = new DataFormatter();
		boolean isExistError = false;
		int count = 0;
		for (Row row : sheet) {
			count++;
			if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
				boolean checkColumn1 = true;
				boolean checkColumn2 = true;
				boolean checkColumn3 = true;
				boolean checkColumn4 = true;
				// chinhpxn20180704_start
				boolean checkLength = true;
				// chinhpxn20180704_end
				String sysGroupCode = "";
				StringBuilder errorMesg = new StringBuilder();
				TmpnFinanceDTO newObj = new TmpnFinanceDTO();
				for (int i = 0; i < 6; i++) {
					Cell cell = row.getCell(i);
					if (cell != null) {
						// Check format file exel
						if (cell.getColumnIndex() == 1) {
							try {
								sysGroupCode = formatter.formatCellValue(cell).trim();
								if (listGroupCode.contains(sysGroupCode)) {
									isExistError = true;
									errorMesg.append("Mã đơn vị đã bị trùng!");
								} else {
									listGroupCode.add(sysGroupCode.trim());
								}
								DepartmentDTO dep = departmentDAO.getSysGroupData(sysGroupCode.trim());
								if (dep.getId() != null) {
									newObj.setSysGroupId(dep.getId());
									newObj.setSysGroupName(dep.getName());
								} else
									checkColumn1 = false;
								if (!checkColumn1) {
									isExistError = true;
									errorMesg.append("Mã đơn vị không hợp lệ!;");
								}
							} catch (Exception e) {
								isExistError = true;
								errorMesg.append("Mã đơn vị không hợp lệ!");
							}
						} else if (cell.getColumnIndex() == 2) {
							try {
								if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
									checkColumn2 = false;
								}
								// chinhpxn20180704_start
								if (formatter.formatCellValue(cell).length() > 14) {
									checkLength = false;
									checkColumn2 = false;
								}
								newObj.setFirstTimes(
										Double.parseDouble(df.format(cell.getNumericCellValue())) * 1000000);
							} catch (Exception e) {
								checkColumn2 = false;
							}
							if (!checkColumn2) {
								isExistError = true;
								if (!checkLength) {
									errorMesg.append("\nĐộ dài Số tiền đợt 1 vượt quá 14 số!");
									checkLength = true;
								} else {
									errorMesg.append("\nSố tiền đợt 1 không hợp lệ!;");
								}
							}
						} else if (cell.getColumnIndex() == 3) {
							try {
								// chinhpxn20180704_start
								if (formatter.formatCellValue(cell).length() > 14) {
									checkLength = false;
									checkColumn3 = false;
								}
								newObj.setSecondTimes(
										Double.parseDouble(df.format(cell.getNumericCellValue())) * 1000000);
							} catch (Exception e) {
								checkColumn3 = false;
							}
							if (!checkColumn3) {
								isExistError = true;
								if (!checkLength) {
									errorMesg.append("\nĐộ dài Số tiền đợt 2 vượt quá 14 số!");
									checkLength = true;
								} else {
									errorMesg.append("Số tiền đợt 2 không hợp lệ!;");
								}
							}
						} else if (cell.getColumnIndex() == 4) {
							try {
								if (formatter.formatCellValue(cell).length() > 14) {
									checkLength = false;
									checkColumn4 = false;
								}
								newObj.setThreeTimes(
										Double.parseDouble(df.format(cell.getNumericCellValue())) * 1000000);
							} catch (Exception e) {
								checkColumn4 = false;
							}
							if (!checkColumn4) {
								isExistError = true;
								if (!checkLength) {
									errorMesg.append("\nĐộ dài Số tiền đợt 3 vượt quá 14 số!");
									checkLength = true;
								} else {
									errorMesg.append("\nSố tiền đợt 3 không hợp lệ!;");
								}
							}
							Cell cell1 = row.createCell(6);
							cell1.setCellValue(errorMesg.toString());

						} else if (cell.getColumnIndex() == 5) {
							if (formatter.formatCellValue(cell).trim().length() > 1000) {
								isExistError = true;
								errorMesg.append("\nGhi chú vượt quá 1000 ký tự!");
							}
							newObj.setDescription(dfS.formatCellValue(cell).trim());
						}

					}
				}
				if (checkColumn1 && checkColumn2 && checkColumn3 && checkColumn4) {
					workLst.add(newObj);
				}

			}
		}
		if (isExistError) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			sheet.setColumnWidth(6, 5000);
			style.setAlignment(HorizontalAlignment.CENTER);
			Cell cell = sheet.getRow(1).createCell(6);
			cell.setCellValue("Cột lỗi");
			cell.setCellStyle(style);
			TmpnFinanceDTO objErr = new TmpnFinanceDTO();
			OutputStream out = new FileOutputStream(f, true);
			workbook.write(out);
			out.close();
			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
		}
		workbook.close();
		return workLst;
	}

	public List<TmpnContractDTO> importContract(String fileInput, String filePath) throws Exception {

		List<TmpnContractDTO> workLst = new ArrayList<TmpnContractDTO>();
		File f = new File(fileInput);
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<String> listGroupCode = new ArrayList<String>();
		DataFormatter formatter = new DataFormatter();
		DecimalFormat df = new DecimalFormat("#.###");
		DecimalFormat longf = new DecimalFormat("#");
		DataFormatter dfS = new DataFormatter();
		boolean isExistError = false;
		int count = 0;
		for (Row row : sheet) {
			count++;
			if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
				boolean checkColumn1 = true;
				boolean checkColumn2 = true;
				boolean checkColumn3 = true;
				boolean checkColumn4 = true;
				boolean checkColumn5 = true;
				// chinhpxn20180704_start
				boolean checkLength = true;
				// chinhpxn20180704_end
				String sysGroupCode = "";
				StringBuilder errorMesg = new StringBuilder();
				TmpnContractDTO newObj = new TmpnContractDTO();
				for (int i = 0; i < 6; i++) {
					Cell cell = row.getCell(i);
					if (cell != null) {
						// Check format file exel
						if (cell.getColumnIndex() == 1) {
							try {
								sysGroupCode = formatter.formatCellValue(cell).trim();
								if (listGroupCode.contains(sysGroupCode)) {
									isExistError = true;
									errorMesg.append("Mã đơn vị đã bị trùng!");
								} else {
									listGroupCode.add(sysGroupCode.trim());
								}
								DepartmentDTO dep = departmentDAO.getSysGroupData(sysGroupCode.trim());
								if (dep.getId() != null) {
									newObj.setSysGroupId(dep.getId());
									newObj.setSysGroupName(dep.getName());
								} else
									checkColumn1 = false;
								if (!checkColumn1) {
									isExistError = true;
									errorMesg.append("Mã đơn vị không hợp lệ!;");
								}
							} catch (Exception e) {
								isExistError = true;
								errorMesg.append("Mã đơn vị không hợp lệ!");
							}
						} else if (cell.getColumnIndex() == 2) {
							try {
								if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
									checkColumn2 = false;
								}
								// chinhpxn20180704_start
								if (formatter.formatCellValue(cell).length() > 20) {
									checkLength = false;
									checkColumn2 = false;
								}
								newObj.setComplete(Double.parseDouble(df.format(cell.getNumericCellValue())));
							} catch (Exception e) {
								checkColumn2 = false;
							}
							if (!checkColumn2) {
								isExistError = true;
								if (!checkLength) {
									errorMesg.append("\nĐộ dài HSHC vượt quá 20 số!");
									checkLength = true;
								} else {
									errorMesg.append("\nHSHC không hợp lệ!;");
								}
							}
						} else if (cell.getColumnIndex() == 3) {
							try {
								if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
									checkColumn3 = false;
								}
								// chinhpxn20180704_start
								if (formatter.formatCellValue(cell).length() > 20) {
									checkLength = false;
									checkColumn3 = false;
								}

								newObj.setEnoughCondition(Double.parseDouble(df.format(cell.getNumericCellValue())));
							} catch (Exception e) {
								checkColumn3 = false;
							}
							if (!checkColumn3) {
								isExistError = true;
								if (!checkLength) {
									errorMesg.append("\nĐộ dài HSHC đủ điều kiện vượt quá 20 số!");
									checkLength = true;
								} else {
									errorMesg.append("HSHC đủ điều kiện không hợp lệ!;");
								}
							}
						} else if (cell.getColumnIndex() == 4) {
							try {
								if (StringUtils.isStringNullOrEmpty(formatter.formatCellValue(cell))) {
									checkColumn4 = false;
								}

								// chinhpxn20180704_start
								if (formatter.formatCellValue(cell).length() > 20) {
									checkLength = false;
									checkColumn4 = false;
								}
								newObj.setNoEnoughCondition(Double.parseDouble(df.format(cell.getNumericCellValue())));
							} catch (Exception e) {
								checkColumn4 = false;
							}
							if (!checkColumn4) {
								isExistError = true;
								if (!checkLength) {
									errorMesg.append("\nĐộ dài HSHC phải đối soát vật tư vượt quá 20 số!");
									checkLength = true;
								} else {
									errorMesg.append("\nHSHC phải đối soát vật tư không hợp lệ!;");
								}
							}
							Cell cell1 = row.createCell(6);
							cell1.setCellValue(errorMesg.toString());

						} else if (cell.getColumnIndex() == 5) {
							if (formatter.formatCellValue(cell).trim().length() > 1000) {
								isExistError = true;
								errorMesg.append("\nGhi chú vượt quá 1000 ký tự");
							}
							newObj.setDescription(dfS.formatCellValue(cell).trim());
						}

					}
				}
				if (checkColumn1 && checkColumn2 && checkColumn3 && checkColumn4) {
					workLst.add(newObj);
				}

			}
		}
		if (isExistError) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			sheet.setColumnWidth(6, 5000);
			style.setAlignment(HorizontalAlignment.CENTER);
			Cell cell = sheet.getRow(1).createCell(6);
			cell.setCellValue("Cột lỗi");
			cell.setCellStyle(style);
			TmpnContractDTO objErr = new TmpnContractDTO();
			OutputStream out = new FileOutputStream(f, true);
			workbook.write(out);
			out.close();
			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
		}
		workbook.close();
		return workLst;
	}

	public YearPlanDetailOSDTO getYearPlanDetail(TotalMonthPlanOSSimpleDTO obj) {
		// TODO Auto-generated method stub
		YearPlanDetailOSDTO data = new YearPlanDetailOSDTO();
		if (obj.getYear() != null && obj.getMonth() != null && obj.getSysGroupId() != null)
			data = totalMonthPlanOSDAO.getYearPlanDetail(obj);
		return data;
	}

	public TotalMonthPlanOSSimpleDTO getDataForSignVOffice(Long totalMonthPlanId) {
		// TODO Auto-generated method stub
		TotalMonthPlanOSSimpleDTO data = new TotalMonthPlanOSSimpleDTO();
		data.setTmpnTargetDTOList(totalMonthPlanOSDAO.getTargetForExport(totalMonthPlanId));
		data.setTmpnSourceDTOList(totalMonthPlanOSDAO.getSourceForExport(totalMonthPlanId));
		data.setTmpnForceMaintainDTOList(totalMonthPlanOSDAO.getForceMaintainForExport(totalMonthPlanId));
		data.setTmpnForceNewBtsDTOList(totalMonthPlanOSDAO.getBTSForExport(totalMonthPlanId));
		data.setTmpnForceNewLineDTOList(totalMonthPlanOSDAO.getNewLineForExport(totalMonthPlanId));
		data.setTmpnMaterialDTOList(totalMonthPlanOSDAO.getMaterialForExport(totalMonthPlanId));
		data.setTmpnFinanceDTOList(totalMonthPlanOSDAO.getFinanceForExport(totalMonthPlanId));
		data.setTmpnContractDTOList(totalMonthPlanOSDAO.setContractForExport(totalMonthPlanId));
		return data;
	}

	public String exportTotalMonthPlan(TotalMonthPlanOSSimpleDTO obj) throws Exception {
		// WorkItemDetailDTO obj = new WorkItemDetailDTO();
		obj.setPage(1L);
		obj.setPageSize(100);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_Kehoach_thang_tongthe.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_Kehoach_thang_tongthe.xlsx");
		List<TotalMonthPlanOSDTO> data = totalMonthPlanOSDAO.doSearch(obj);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			for (TotalMonthPlanOSDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getCode() != null) ? dto.getCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getName() != null) ? dto.getName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(
						(dto.getYear() != null && dto.getMonth() != null) ? dto.getMonth() + "/" + dto.getYear() : "");
				cell.setCellStyle(styleNumber);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(getStringForSignState(dto.getSignState()));
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(getStatusTotalMonthPlan(dto.getStatus()));
				cell.setCellStyle(style);

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_Kehoach_thang_tongthe.xlsx");
		return path;
	}

	private String getStatusTotalMonthPlan(String status) {
		// TODO Auto-generated method stub
		if (status != null) {
			if ("0".equals(status)) {
				return "Hết hiệu lực ";
			} else if ("1".equals(status)) {
				return "Hiệu lực";
			}
		}
		return null;
	}

	private String getStringForSignState(String signState) {
		// TODO Auto-generated method stub
		if ("1".equals(signState)) {
			return "Chưa trình ký";
		} else if ("2".equals(signState)) {
			return "Đã trình ký";
		} else if ("3".equals(signState)) {
			return "Đã ký duyệt";
		} else if ("4".equals(signState)) {
			return "Từ chối ký duyệt";
		}
		return null;
	}

	public String exportDetailTargetTotalMonthPlan(TmpnTargetDTO obj) throws Exception {
		// WorkItemDetailDTO obj = new WorkItemDetailDTO();
		obj.setPage(1L);
		obj.setPageSize(100);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_Chitieu_thang_tongthe.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_Chitieu_thang_tongthe.xlsx");
		List<TmpnTargetOSDTO> data = totalMonthPlanOSDAO.getTmpnTargetDTOListByParentNew(obj.getTotalMonthPlanId());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			for (TmpnTargetOSDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(styleNumber);

				cell = row.createCell(2, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantityXlTarget() != null) ? dto.getQuantityXlTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(3, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantityCpTarget() != null) ? dto.getQuantityCpTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(4, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantityNtdGpdnTarget() != null) ? dto.getQuantityNtdGpdnTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(5, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantityNtdXdddTarget() != null) ? dto.getQuantityNtdXdddTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(6, CellType.NUMERIC);
				cell.setCellValue((dto.getCompleteTarget() != null) ? dto.getCompleteTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(7, CellType.NUMERIC);
				cell.setCellValue((dto.getHshcXlTarget() != null) ? dto.getHshcXlTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(8, CellType.NUMERIC);
				cell.setCellValue((dto.getRevenueCpTarget() != null) ? dto.getRevenueCpTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(9, CellType.NUMERIC);
				cell.setCellValue((dto.getRevenueNtdGpdnTarget() != null) ? dto.getRevenueNtdGpdnTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(10, CellType.NUMERIC);
				cell.setCellValue((dto.getRevenueNtdXdddTarget() != null) ? dto.getRevenueNtdXdddTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(11, CellType.NUMERIC);
				cell.setCellValue((dto.getHshcHtctTarget() != null) ? dto.getHshcHtctTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(12, CellType.NUMERIC);
				cell.setCellValue((dto.getTaskXdddTarget() != null) ? dto.getTaskXdddTarget() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(13, CellType.NUMERIC);
				cell.setCellValue((dto.getRevokeCashTarget() != null) ? dto.getRevokeCashTarget() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(14, CellType.NUMERIC);
				cell.setCellValue((dto.getSumDeployHtct() != null) ? dto.getSumDeployHtct() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(15, CellType.NUMERIC);
				cell.setCellValue((dto.getBuildCompleteHtct() != null) ? dto.getBuildCompleteHtct() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(16, CellType.NUMERIC);
				cell.setCellValue((dto.getCompleteDbHtct() != null) ? dto.getCompleteDbHtct() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(17, CellType.NUMERIC);
				cell.setCellValue((dto.getRentHtct() != null) ? dto.getRentHtct() : 0);
				cell.setCellStyle(styleNumber);

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_Chitieu_thang_tongthe.xlsx");
		return path;
	}

	private int getRatioRevenueTarget(Double revenueLk, Double revenueInYear) {
		// TODO Auto-generated method stub
		if (revenueInYear != null && revenueInYear != 0) {
			return (int) ((revenueLk * 100) / revenueInYear);

		}
		return 0;
	}

	private int getRatioCompleteTarget(Double completeLk, Double completeInYear) {
		// TODO Auto-generated method stub
		if (completeInYear != null && completeInYear != 0) {
			return (int) ((completeLk * 100) / completeInYear);

		}
		return 0;
	}

	private String numberFormat(double value) {
		DecimalFormat myFormatter = new DecimalFormat("###,###.###");
		NumberFormat numEN = NumberFormat.getPercentInstance();
		String percentageEN = myFormatter.format(value);

		return percentageEN;
	}

	private int getRatioQuantityTarget(Double quantityLk, Double quantityInYear) {
		// TODO Auto-generated method stub
		if (quantityInYear != null && quantityInYear != 0) {
			return (int) ((quantityLk * 100) / quantityInYear);

		}
		return 0;
	}

	public String exportDetailSourceTotalMonthPlan(TmpnSourceDTO obj) throws Exception {
		obj.setPage(1L);
		obj.setPageSize(100);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(
				new FileInputStream(filePath + "Export_nguonviec_thang_tongthe.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_nguonviec_thang_tongthe.xlsx");
		List<TmpnSourceDTO> data = totalMonthPlanOSDAO.getTmpnSourceDTOListByParent(obj.getTotalMonthPlanId());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			double nguonviec = 0;
			double giaochitieu = 0;
			double thuathieu = 0;

			for (TmpnSourceDTO dto : data) {
				nguonviec += dto.getSource();
				giaochitieu += dto.getQuantity();
				thuathieu += (dto.getSource() - dto.getQuantity());
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(2, CellType.NUMERIC);
				cell.setCellValue((dto.getSource() != null) ? dto.getSource() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(3, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(4, CellType.NUMERIC);
				cell.setCellValue((dto.getSource() != null && dto.getQuantity() != null)
						? (dto.getSource() - dto.getQuantity())
						: 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(5, CellType.NUMERIC);
				cell.setCellValue(
						(dto.getSource() != null && dto.getQuantity() != null && dto.getSource() - dto.getQuantity() < 0
								&& dto.getQuantity() != 0) ? getRatioSource(dto.getSource(), dto.getQuantity()) : "0");

				cell.setCellStyle(styleNumber);

				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : "");
				cell.setCellStyle(style);

			}
			Row row = sheet.createRow(i);
			Cell cell = row.createCell(0, CellType.STRING);
			TmpnSourceDTO dto1 = new TmpnSourceDTO();
			cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(style);

			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleNumber);

			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue(numberFormat(nguonviec));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(3, CellType.STRING);
			cell.setCellValue(numberFormat(giaochitieu));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(4, CellType.STRING);
			cell.setCellValue(numberFormat(thuathieu));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(5, CellType.STRING);
//			cell.setCellValue("");
            cell.setCellStyle(styleNumber);

            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue("");
            cell.setCellStyle(style);

		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_nguonviec_thang_tongthe.xlsx");
		return path;

	}

	private String getRatioSource(Double source, Double quantity) {
		// TODO Auto-generated method stub
		if (source != null && quantity != null && quantity != 0) {
			return (int) (((source - quantity) / (quantity)) * 100) + "%";

		}
		return null;
	}

	public List<TmpnTargetOSDTO> getLKBySysList(Long month, Long year, List<Long> sysGroupIdList) {
		// TODO Auto-generated method stub

		if (month != null && year != null && sysGroupIdList != null) {
			List<TmpnTargetOSDTO> dto = totalMonthPlanOSDAO.getLKBySysList(month, year, sysGroupIdList);
			return dto;
		}
		return null;

	}

	public String exportDetailMaterialTotalMonthPlan(TmpnMaterialDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(
				new FileInputStream(filePath + "Export_dambao_vattu_thietbi_thang_tongthe.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_dambao_vattu_thietbi_thang_tongthe.xlsx");
		List<TmpnMaterialDTO> data = totalMonthPlanOSDAO.getTmpnMaterialDTOListByParent(obj.getTotalMonthPlanId());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;

			for (TmpnMaterialDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getCatConstructionTypeName() != null) ? dto.getCatConstructionTypeName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(
						(dto.getCatConstructionDeployName() != null) ? dto.getCatConstructionDeployName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : "");
				cell.setCellStyle(style);

			}

		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(
				uploadPathReturn + File.separator + "Export_dambao_vattu_thietbi_thang_tongthe.xlsx");
		return path;

	}

	public String exportDetailFinanceTotalMonthPlan(TmpnFinanceDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(
				new FileInputStream(filePath + "Export_taichinh_thang_tongthe.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_taichinh_thang_tongthe.xlsx");
		List<TmpnFinanceDTO> data = totalMonthPlanOSDAO.getTmpnFinanceDTOListByParent(obj.getTotalMonthPlanId());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			double dot1 = 0;
			double dot2 = 0;
			double dot3 = 0;
			for (TmpnFinanceDTO dto : data) {
				dot1 += dto.getFirstTimes();
				dot2 += dto.getSecondTimes();
				dot3 += dto.getThreeTimes();

				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(2, CellType.NUMERIC);
				cell.setCellValue((dto.getFirstTimes() != null) ? dto.getFirstTimes() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(3, CellType.NUMERIC);
				cell.setCellValue((dto.getSecondTimes() != null) ? dto.getSecondTimes() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(4, CellType.NUMERIC);
				cell.setCellValue((dto.getThreeTimes() != null) ? dto.getThreeTimes() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : "");
				cell.setCellStyle(style);

			}
			Row row = sheet.createRow(i);
			Cell cell = row.createCell(0, CellType.STRING);
			TmpnFinanceDTO dto1 = new TmpnFinanceDTO();
			cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleNumber);

			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleNumber);

			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue(numberFormat(dot1));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(3, CellType.STRING);
			cell.setCellValue(numberFormat(dot2));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(4, CellType.STRING);
			cell.setCellValue(numberFormat(dot3));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(5, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleNumber);

		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_taichinh_thang_tongthe.xlsx");
		return path;

	}

	public String exportDetailContractTotalMonthPlan(TmpnContractDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_hopdong_thang_tongthe.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_hopdong_thang_tongthe.xlsx");
		List<TmpnContractDTO> data = totalMonthPlanOSDAO.getTmpnContractDTOListByParent(obj.getTotalMonthPlanId());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			double HSHC = 0;
			double HSHCDDK = 0;
			double DSVT = 0;
			double KDSVT = 0;
			for (TmpnContractDTO dto : data) {
				HSHC += dto.getComplete();
				HSHCDDK += dto.getEnoughCondition();
				DSVT += dto.getNoEnoughCondition();
				KDSVT += getNoEnoughDS(dto.getNoEnoughCondition(), dto.getEnoughCondition());

				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(2, CellType.NUMERIC);
				cell.setCellValue((dto.getComplete() != null) ? dto.getComplete() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(3, CellType.NUMERIC);
				cell.setCellValue((dto.getEnoughCondition() != null) ? dto.getEnoughCondition() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(4, CellType.NUMERIC);
				cell.setCellValue(
						(dto.getNoEnoughCondition() != null) ? dto.getNoEnoughCondition() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(5, CellType.NUMERIC);
				cell.setCellValue((dto.getNoEnoughCondition() != null && dto.getEnoughCondition() != null)
						? (dto.getEnoughCondition() - dto.getNoEnoughCondition())
						: 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : "");
				cell.setCellStyle(style);

			}
			Row row = sheet.createRow(i);
			Cell cell = row.createCell(0, CellType.STRING);
			TmpnContractDTO dto1 = new TmpnContractDTO();
			cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleNumber);

			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleNumber);

			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue(numberFormat(HSHC));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(3, CellType.STRING);
			cell.setCellValue(numberFormat(HSHCDDK));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(4, CellType.STRING);
			cell.setCellValue(numberFormat(DSVT));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(5, CellType.STRING);
			cell.setCellValue(numberFormat(KDSVT));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(6, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleNumber);
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_hopdong_thang_tongthe.xlsx");
		return path;

	}

	private double getNoEnoughDS(Double noEnough, Double Enough) {
		// TODO Auto-generated method stub
		if (noEnough != null && Enough != null) {
			double result;
			result = Enough - noEnough;
			return result;
		}
		return 0;
	}

	public String exportDetailForceMaintainTotalMonthPlan(TmpnForceMaintainDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_giaco_thang_tongthe.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_giaco_thang_tongthe.xlsx");
		List<TmpnForceMaintainDTO> data = totalMonthPlanOSDAO
				.getTmpnForceMaintainDTOListByParent(obj.getTotalMonthPlanId());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			long xayDung = 0;
			long tong = 0;
			long lapBaoCot = 0;
			long thayThanCot = 0;
			long soDoiCanXD = 0;
			long soDoiDangXD = 0;
			long soDoiCanTangXD = 0;
			long soDoiCanLD = 0;
			long soDoiDangLD = 0;
			long soDoiCanTangLD = 0;

			for (TmpnForceMaintainDTO dto : data) {
				if (dto.getBuildPlan() != null) {
					xayDung += dto.getBuildPlan();
				}
				if (dto.getInstallPlan() != null) {
					lapBaoCot += dto.getInstallPlan();
				}

				if (dto.getReplacePlan() != null) {
					thayThanCot += dto.getReplacePlan();
				}
				if (dto.getTeamBuildRequire() != null) {
					soDoiCanXD += dto.getTeamBuildRequire();
				}
				if (dto.getTeamBuildAvaiable() != null) {
					soDoiDangXD += dto.getTeamBuildAvaiable();
				}
				if (dto.getTeamInstallRequire() != null) {
					soDoiCanLD += dto.getTeamInstallRequire();
				}
				if (dto.getTeamInstallAvaiable() != null) {
					soDoiDangLD += dto.getTeamInstallAvaiable();
				}

				tong += getXD(dto.getReplacePlan(), dto.getInstallPlan());
				soDoiCanTangXD += getTeamBuildNeed(dto.getTeamBuildRequire(), dto.getTeamBuildAvaiable());
				soDoiCanTangLD += getTeamInstallNeed(dto.getTeamInstallRequire(), dto.getTeamInstallAvaiable());

				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(styleNumber);

				cell = row.createCell(2, CellType.NUMERIC);
				cell.setCellValue((dto.getBuildPlan() != null) ? dto.getBuildPlan() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(3, CellType.NUMERIC);
				cell.setCellValue((dto.getInstallPlan() != null && dto.getReplacePlan() != null)
						? (dto.getInstallPlan() + dto.getReplacePlan())
						: 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(4, CellType.NUMERIC);
				cell.setCellValue((dto.getInstallPlan() != null) ? dto.getInstallPlan() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(5, CellType.NUMERIC);
				cell.setCellValue((dto.getReplacePlan() != null) ? dto.getReplacePlan() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(6, CellType.NUMERIC);
				cell.setCellValue((dto.getTeamBuildRequire() != null) ? dto.getTeamBuildRequire() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(7, CellType.NUMERIC);
				cell.setCellValue((dto.getTeamBuildAvaiable() != null) ? dto.getTeamBuildAvaiable() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(8, CellType.NUMERIC);
				cell.setCellValue(
						getTeamBuildNeed(dto.getTeamBuildRequire(), dto.getTeamBuildAvaiable()));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(9, CellType.NUMERIC);
				cell.setCellValue(
						(dto.getTeamInstallRequire() != null) ? dto.getTeamInstallRequire() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(10, CellType.NUMERIC);
				cell.setCellValue(
						(dto.getTeamInstallAvaiable() != null) ? dto.getTeamInstallAvaiable() : 0);
				cell.setCellStyle(styleNumber);

				cell = row.createCell(11, CellType.NUMERIC);
				cell.setCellValue(
						getTeamInstallNeed(dto.getTeamInstallRequire(), dto.getTeamInstallAvaiable()));
				cell.setCellStyle(styleNumber);

			}
			Row row = sheet.createRow(i);
			Cell cell = row.createCell(0, CellType.STRING);
			TmpnSourceDTO dto1 = new TmpnSourceDTO();
			cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleNumber);

			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(style);

			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue(numberFormat(xayDung));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(3, CellType.STRING);
			cell.setCellValue(numberFormat(tong));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(4, CellType.STRING);
			cell.setCellValue(numberFormat(lapBaoCot));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(5, CellType.STRING);
			cell.setCellValue(numberFormat(thayThanCot));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(6, CellType.STRING);
			cell.setCellValue(numberFormat(soDoiCanXD));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(7, CellType.STRING);
			cell.setCellValue(numberFormat(soDoiDangXD));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(8, CellType.STRING);
			cell.setCellValue(numberFormat(soDoiCanTangXD));
			cell.setCellStyle(styleNumber);
			cell = row.createCell(9, CellType.STRING);
			cell.setCellValue(numberFormat(soDoiCanLD));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(10, CellType.STRING);
			cell.setCellValue(numberFormat(soDoiDangLD));
			cell.setCellStyle(styleNumber);

			cell = row.createCell(11, CellType.STRING);
			cell.setCellValue(numberFormat(soDoiCanTangLD));
			cell.setCellStyle(styleNumber);

//			cell = row.createCell(3, CellType.STRING);
//			cell.setCellValue((dto1.getQuantity() != null) ? numberFormat(dto1
//					.getQuantity()) : "0");
//			cell.setCellStyle(styleNumber);
//
//			cell = row.createCell(4, CellType.STRING);
//			cell.setCellValue((dto1.getDifference() != null) ? numberFormat(dto1
//					.getDifference()) : "0");
//			cell.setCellStyle(styleNumber);

        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Export_giaco_thang_tongthe.xlsx");
        return path;

    }

    private double getTeamInstallNeed(Long teamInstallRequire, Long teamInstallAvaiable) {
        // TODO Auto-generated method stub
        if (teamInstallRequire != null && teamInstallAvaiable != null) {
            long result;
            result = teamInstallRequire - teamInstallAvaiable;
            return result;
        }
        return 0;
    }

    private double getTeamBuildNeed(Long teamBuildRequire, Long teamBuildAvaiable) {
        // TODO Auto-generated method stub
        if (teamBuildRequire != null && teamBuildAvaiable != null) {
            long result;
            result = teamBuildRequire - teamBuildAvaiable;
            return result;
        }
        return 0;
    }

    private long getXD(Long replacePlan, Long installPlan) {
        // TODO Auto-generated method stub
        if (replacePlan != null && installPlan != null) {
            long result;
            result = replacePlan + installPlan;
            return result;
        }
        return 0;
    }

    public void saveAppendixFile(TotalMonthPlanOSSimpleDTO obj, Long userId) throws Exception {
        // TODO Auto-generated method stub
        List<Long> listId = utilAttachDocumentDAO.getIdByObjectAndTypeListOS(obj.getTotalMonthPlanId(), listOfTypePhuLuc);
        List<Long> deleteId = new ArrayList<Long>(listId);
        if (obj.getAppendixFileDTOList() != null) {
            for (UtilAttachDocumentDTO file : obj.getAppendixFileDTOList()) {
                if (file.getUtilAttachDocumentId() == null) {
                    file.setObjectId(obj.getTotalMonthPlanId());
                    file.setCreatedDate(new Date());
                    file.setType("OS");
                    file.setCreatedUserId(userId);
                    file.setDescription("file phu luc Ke hoach thang tong the ngoai OS");
                    file.setFilePath(UEncrypt.decryptFileUploadPath(file.getFilePath()));
                    utilAttachDocumentDAO.saveObject(file.toModel());
                }
            }
            for (Long id : listId) {
                for (UtilAttachDocumentDTO file : obj.getAppendixFileDTOList()) {
                    if (file.getUtilAttachDocumentId() != null) {
                        if (id.longValue() == file.getUtilAttachDocumentId().longValue()) {
                            deleteId.remove(id);
                        }
                        utilAttachDocumentDAO.updateAppParamCode(file.getUtilAttachDocumentId(),
                                file.getAppParamCode());

                    }

                }
            }
            utilAttachDocumentDAO.deleteListUtils(deleteId);
        } else {
            utilAttachDocumentDAO.deleteListUtils(deleteId);
        }

    }

    public List<AppParamDTO> getFileAppendixParam() {
        // TODO Auto-generated method stub
        return totalMonthPlanOSDAO.getFileAppendixParam();
    }

    public List<UtilAttachDocumentDTO> getAppendixFile(Long objectId) throws Exception {
        // TODO Auto-generated method stub
        return utilAttachDocumentDAO.getByListTypeAndObjectOS(objectId, listOfTypePhuLuc);
    }

    public String exportDetailBTSGTotalMonthPlan(Long id) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_bts_thang_tongthe.xlsx"));
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
                udir.getAbsolutePath() + File.separator + "Export_bts_thang_tongthe.xlsx");
        List<TmpnForceNewBtsDTO> data = totalMonthPlanOSDAO.getTmpnForceNewBtsDTOListByParent(id);
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            int i = 1;

            for (TmpnForceNewBtsDTO dto : data) {

                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(styleNumber);

                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
                cell.setCellStyle(style);

                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getStationNumber() != null) ? numberFormat(dto.getStationNumber()) : "");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getTeamBuildRequire() != null) ? numberFormat(dto.getTeamBuildRequire()) : "");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getTeamBuildAvaiable() != null) ? numberFormat(dto.getTeamBuildAvaiable()) : "");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(
                        (dto.getSelfImplementPercent() != null) ? numberFormat(dto.getSelfImplementPercent()) + "%"
                                : "0%");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getSelfImplementPercent() != null)
                        ? numberFormat(100 - dto.getSelfImplementPercent().doubleValue()) + "%"
                        : "0%");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getTeamBuildRequire() != null && dto.getTeamBuildAvaiable() != null)
                        ? numberFormat(
                        dto.getTeamBuildRequire().doubleValue() - dto.getTeamBuildAvaiable().doubleValue())
                        : "0");
                cell.setCellStyle(styleNumber);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : "");
                cell.setCellStyle(style);

            }

        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Export_bts_thang_tongthe.xlsx");
        return path;

    }

    public String exportDetailForceNewTotalMonthPlan(Long id) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_ngamhoa_thang_tongthe.xlsx"));
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
                udir.getAbsolutePath() + File.separator + "Export_ngamhoa_thang_tongthe.xlsx");
        List<TmpnForceNewLineDTO> data = totalMonthPlanOSDAO.getTmpnForceNewLineDTOListByParent(id);
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            int i = 2;

            for (TmpnForceNewLineDTO dto : data) {

                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(styleNumber);

                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
                cell.setCellStyle(style);

                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getStationNumber() != null) ? numberFormat(dto.getStationNumber()) : "");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(
                        (dto.getCurrentHaveLicense() != null) ? numberFormat(dto.getCurrentHaveLicense()) : "");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getCurrentQuantity() != null) ? numberFormat(dto.getCurrentQuantity()) : "");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(
                        (dto.getCurentStationComplete() != null) ? numberFormat(dto.getCurentStationComplete()) : "");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue(
                        (dto.getRemainHaveLicense() != null) ? numberFormat(dto.getCurentStationComplete()) : "");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getRemainQuantity() != null) ? numberFormat(dto.getRemainQuantity()) : "");
                cell.setCellStyle(styleNumber);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue(
                        (dto.getRemainStationComplete() != null) ? numberFormat(dto.getRemainStationComplete()) : "");
                cell.setCellStyle(styleNumber);

                cell = row.createCell(9, CellType.STRING);
                cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : "");
                cell.setCellStyle(style);

            }

        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Export_ngamhoa_thang_tongthe.xlsx");
        return path;

    }

    public DataListDTO doSearchTarget(TotalMonthPlanOSSimpleDTO obj) {

        List<TmpnTargetOSDTO> ls;

        ls = totalMonthPlanOSDAO.getTmpnTargetDTOListByParentNew(obj.getTotalMonthPlanId());

        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;

    }

    public DataListDTO doSearchSource(TotalMonthPlanOSSimpleDTO obj) {

        List<TmpnSourceDTO> ls;

        ls = totalMonthPlanOSDAO.getTmpnSourceDTOListByParent(obj.getTotalMonthPlanId());

        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;

    }

    public DataListDTO doSearchForcemaintain(TotalMonthPlanOSSimpleDTO obj) {

        List<TmpnForceMaintainDTO> ls;

        ls = totalMonthPlanOSDAO.getTmpnForceMaintainDTOListByParent(obj.getTotalMonthPlanId());

        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;

    }

    public DataListDTO doSearchBTS(TotalMonthPlanOSSimpleDTO obj) {

        List<TmpnForceNewBtsDTO> ls;

        ls = totalMonthPlanOSDAO.getTmpnForceNewBtsDTOListByParent(obj.getTotalMonthPlanId());

        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;

    }

    public DataListDTO doSearchForceNew(TotalMonthPlanOSSimpleDTO obj) {

        List<TmpnForceNewLineDTO> ls;

        ls = totalMonthPlanOSDAO.getTmpnForceNewLineDTOListByParent(obj.getTotalMonthPlanId());

        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;

    }

    public DataListDTO doSearchMaterial(TotalMonthPlanOSSimpleDTO obj) {

        List<TmpnMaterialDTO> ls;

        ls = totalMonthPlanOSDAO.getTmpnMaterialDTOListByParent(obj.getTotalMonthPlanId());

        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;

    }

    public DataListDTO doSearchFinance(TotalMonthPlanOSSimpleDTO obj) {

        List<TmpnFinanceDTO> ls;

        ls = totalMonthPlanOSDAO.getTmpnFinanceDTOListByParent(obj.getTotalMonthPlanId());

        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;

    }

    public DataListDTO doSearchContract(TotalMonthPlanOSSimpleDTO obj) {

        List<TmpnContractDTO> ls;

        ls = totalMonthPlanOSDAO.getTmpnContractDTOListByParent(obj.getTotalMonthPlanId());

        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;

    }

    public DataListDTO doSearchAppendix(TotalMonthPlanOSSimpleDTO obj) throws Exception {

        List<UtilAttachDocumentDTO> ls;

        ls = utilAttachDocumentDAO.getByListTypeAndObjectOS(obj.getTotalMonthPlanId(), listOfTypePhuLuc);

        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;

    }

    public Long checkPermissionsAdd(Long sysGroupId, HttpServletRequest request) {

        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.TOTAL_MONTH_PLAN,
                request)) {
            throw new IllegalArgumentException("Bạn không có quyền tạo mới thông tin tháng tổng thể");
        }
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }

    public Long checkPermissionsCopy(Long sysGroupId, HttpServletRequest request) {

        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.TOTAL_MONTH_PLAN,
                request)) {
            throw new IllegalArgumentException("Bạn không có quyền sao chép thông tin tháng tổng thể");
        }
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }

    public Long checkPermissionsDelete(Long sysGroupId, HttpServletRequest request) {

        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.TOTAL_MONTH_PLAN,
                request)) {
            throw new IllegalArgumentException("Bạn không có quyền xóa thông tin tháng tổng thể");
        }
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }

    public Long checkPermissionsRegistry(Long sysGroupId, HttpServletRequest request) {

        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.TOTAL_MONTH_PLAN,
                request)) {
            throw new IllegalArgumentException("Bạn không có quyền trình kí thông tin tháng tổng thể");
        }
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }

    public Long checkPermissionsUpdate(Long sysGroupId, HttpServletRequest request) {

        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.TOTAL_MONTH_PLAN,
                request)) {
            return 2L;
        }
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }
    
    public void updateRegistry(TotalMonthPlanOSSimpleDTO obj, HttpServletRequest request) {
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE, Constant.AdResourceKey.TOTAL_MONTH_PLAN,
                request)) {
            throw new IllegalArgumentException("Bạn không có quyền trình ký thông tin tháng tổng thể ngoài OS");
        }
        totalMonthPlanOSDAO.updateRegistry(obj.getTotalMonthPlanId());
    }

}
