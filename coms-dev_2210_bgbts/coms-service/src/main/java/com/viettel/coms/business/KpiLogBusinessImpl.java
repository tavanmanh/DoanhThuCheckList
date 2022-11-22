package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.bo.KpiLogBO;
import com.viettel.coms.dao.KpiLogDAO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;

@Service("kpiLogBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KpiLogBusinessImpl extends BaseFWBusinessImpl<KpiLogDAO, KpiLogDTO, KpiLogBO> implements KpiLogBusiness {

    @Autowired
    private KpiLogDAO kpiLogDAO;

    @Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
    
    public KpiLogBusinessImpl() {
        tModel = new KpiLogBO();
        tDAO = kpiLogDAO;
    }

    @Override
    public KpiLogDAO gettDAO() {
        return kpiLogDAO;
    }

    @Override
    public long count() {
        return kpiLogDAO.count("KpiLogBO", null);
    }

    public DataListDTO doSearch(KpiLogDTO obj){
    	List<KpiLogDTO> ls = kpiLogDAO.doSearch(obj);
    	DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
    }
    
    public String exportExcelDataLog(KpiLogDTO obj) throws Exception{
    	obj.setPage(null);
		obj.setPageSize(null);
		InputStream file = null;
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		file = new BufferedInputStream(new FileInputStream(filePath + "BaoCaoThongKeLogTruyCap.xlsx"));
		List<KpiLogDTO> data = kpiLogDAO.doSearch(obj);

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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BaoCaoThongKeLogTruyCap.xlsx");
		// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			// HuyPQ-22/08/2018-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			XSSFCellStyle styleCurrency = ExcelUtils.styleCurrency(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 1;
			for (KpiLogDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getSysUserName() != null ? dto.getSysUserName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getEmail() != null ? dto.getEmail() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getSysGroupName() != null ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getDescription() != null ? dto.getDescription() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getSubmitDay() != null ? dto.getSubmitDay() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getSubmitStartTime() != null ? dto.getSubmitStartTime() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getSubmitEndTime() != null ? dto.getSubmitEndTime() : "");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BaoCaoThongKeLogTruyCap.xlsx");
		return path;
    }
}
