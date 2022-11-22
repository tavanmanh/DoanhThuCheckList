package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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

import com.viettel.coms.bo.YearPlanOSBO;
import com.viettel.coms.dao.DepartmentDAO;
import com.viettel.coms.dao.KpiLogDAO;
import com.viettel.coms.dao.YearPlanDetailOSDAO;
import com.viettel.coms.dao.YearPlanOSDAO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.YearPlanDetailOSDTO;
import com.viettel.coms.dto.YearPlanOSDTO;
import com.viettel.coms.dto.YearPlanSimpleOSDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.utils.StringUtils;

/**
 * @author HoangNH38
 */
@Service("yearPlanOSDetailBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class YearPlanOSBusinessImpl extends BaseFWBusinessImpl<YearPlanOSDAO, YearPlanOSDTO, YearPlanOSBO>
implements YearPlanOSBusiness{


    @Autowired
    private YearPlanOSDAO yearPlanOSDAO;
    
    @Autowired
    private DepartmentDAO departmentDAO;

    @Autowired
    private YearPlanDetailOSDAO yearPlanDetailOSDAO;
    
    @Autowired
    private KpiLogDAO KpiLogDAO;

    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;

    public YearPlanOSBusinessImpl() {
        tModel = new YearPlanOSBO();
        tDAO = yearPlanOSDAO;
    }

    @Override
    public YearPlanOSDAO gettDAO() {
        return yearPlanOSDAO;
    }

    @Override
    public long count() {
        return yearPlanOSDAO.count("YearPlanOSBO", null);
    }

    public DataListDTO doSearch(YearPlanSimpleOSDTO obj, Long sysGroupId) {
        List<YearPlanOSDTO> ls = yearPlanOSDAO.doSearch(obj, sysGroupId);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public YearPlanSimpleOSDTO getById(Long id) {
        YearPlanSimpleOSDTO data = yearPlanOSDAO.getYeaPlanById(id);
        List<YearPlanDetailOSDTO> detailList = yearPlanOSDAO.getYearPlanDetailByParentId(id);
        data.setDetailList(detailList);
        return data;
    }

    public Long add(YearPlanSimpleOSDTO obj) {
        checkYear(obj.getYear(), null);
        Long yearPlanId = yearPlanOSDAO.saveObject(obj.toModel());
        if (obj.getDetailList() != null) {
            for (YearPlanDetailOSDTO dto : obj.getDetailList()) {
                dto.setYearPlanId(yearPlanId);
                dto.setYear(obj.getYear());
                yearPlanDetailOSDAO.saveObject(dto.toModel());
            }
        }
        return yearPlanId;
    }

    public void addKpiLog(KpiLogDTO obj) {
        Long id = KpiLogDAO.saveObject(obj.toModel());
    }

    public Long updateYearPlan(YearPlanSimpleOSDTO obj) {
        checkYear(obj.getYear(), obj.getYearPlanId());
        Long status = yearPlanOSDAO.updateObject(obj.toModel());
        List<Long> yearPlanDetailIdListInDB = yearPlanOSDAO.getYearPlanDetailIdListInDB(obj.getYearPlanId());
        List<YearPlanDetailOSDTO> detailList = obj.getDetailList();
        List<YearPlanDetailOSDTO> detailListInUse = new ArrayList<YearPlanDetailOSDTO>();
        // add new detail to db
        for (YearPlanDetailOSDTO detail : detailList) {
            if (detail.getYearPlanDetailId() != null) {
                detailListInUse.add(detail);
                yearPlanDetailOSDAO.updateObject(detail.toModel());
            } else {
                detail.setYearPlanId(obj.getYearPlanId());
                detail.setYear(obj.getYear());
                yearPlanDetailOSDAO.saveObject(detail.toModel());
            }
        }
        List<Long> deleteList = new ArrayList<Long>(yearPlanDetailIdListInDB);
        for (Long yId : yearPlanDetailIdListInDB) {
            boolean exist = false;
            for (YearPlanDetailOSDTO detail : detailListInUse) {
                if (yId.longValue() == detail.getYearPlanDetailId().longValue()) {
                    deleteList.remove(yId);
                    break;
                }
            }
        }
        if (!deleteList.isEmpty())
            yearPlanOSDAO.deleteYearPlanDetail(deleteList);
        return obj.getYearPlanId();
    }

    public Long getSequence() {
        return yearPlanOSDAO.getSequence();
    }

    public void remove(YearPlanOSDTO obj) {
        yearPlanOSDAO.remove(obj.getYearPlanId());
    }

    public void checkYear(Long year, Long yearPlanId) {
        boolean isExist = yearPlanOSDAO.checkYear(year, yearPlanId);
        if (isExist)
            throw new IllegalArgumentException("Năm tạo kế hoạch đã tồn tại!");
    }

    public String exportExcelTemplate() throws Exception {
        String folderParam = defaultSubFolderUpload;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_KeHoachNam_NgoaiOS.xlsx"));
        // String filePath1 = UFile.writeToFileServerATTT2(file,
        // "Import_KeHoachNam.xlsx", folderParam, folder2Upload);
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
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Import_KeHoachNam_NgoaiOS.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(1);
        // chinhpxn20180619
        List<DepartmentDTO> departmentList = departmentDAO.getAll();
        // chinhpxn20180619
        if (departmentList != null && !departmentList.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            int i = 1;
            for (DepartmentDTO dto : departmentList) {
//				Row row = sheet.createRow(i++);
//				Cell cell = row.createCell(0, CellType.STRING);
//				cell.setCellValue(StringUtils.isNotEmpty(dto
//						.getCode()) ? dto.getCode() : "");
//				cell.setCellStyle(style);
//				cell = row.createCell(1, CellType.STRING);
//				cell.setCellValue(StringUtils.isNotEmpty(dto
//						.getName()) ? dto.getName() : "");
//				cell.setCellStyle(style);
                // chinhpxn20180619
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);

                cell.setCellValue(!StringUtils.isNullOrEmpty(dto.getCode()) ? dto.getCode() : "");
                //
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
                // chinhpxn20180619
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_KeHoachNam_NgoaiOS.xlsx");
        return path;
    }

    public List<YearPlanDetailOSDTO> getDataForSignVOffice(Long yearPlanId) {
        return yearPlanOSDAO.getDataForSignVOffice(yearPlanId);
    }

    public String exportYearPlan(YearPlanSimpleOSDTO obj) throws Exception {
//		YearPlanSimpleOSDTO obj = new YearPlanSimpleOSDTO();

        obj.setPage(1L);
        obj.setPageSize(100);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "KeHoachNam.xlsx"));
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
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "KeHoachNam.xlsx");
        List<YearPlanSimpleOSDTO> data = yearPlanOSDAO.exportYearPlan(obj);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            int i = 2;
            for (YearPlanSimpleOSDTO dto : data) {
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
                cell.setCellValue((dto.getYear() != null) ? numberFormat(dto.getYear()) : "");
                cell.setCellStyle(styleNumber);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(getStringForSignState(dto.getSignState()));
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(getStringForStatus(dto.getStatus()));
                cell.setCellStyle(style);

                // thiếu quantity

            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "KeHoachNam.xlsx");
        return path;
    }

    private String numberFormat(double value) {
        DecimalFormat myFormatter = new DecimalFormat("###.###");
        String output = myFormatter.format(value);
        return output;
    }

    private String getStringForStatus(String status) {
        if ("0".equals(status)) {
            return "Hết hiệu lực";
        } else if ("1".equals(status)) {
            return "Hiệu lực";
        }
        return null;
    }

    private String getStringForSignState(String signState) {
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

    public List<AppParamDTO> getAppParamByType(String type) {
        return yearPlanOSDAO.getAppParamByType(type);
    }
    
    public void updateRegistry(YearPlanOSDTO obj) {
    	yearPlanOSDAO.updateRegistry(obj.getYearPlanId());
    }

}
