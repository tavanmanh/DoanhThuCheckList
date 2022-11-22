package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.dao.ReportCostOfSalesDAO;
import com.viettel.coms.dto.ReportCostofSalesDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Service("reportCostOfSalesBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReportCostOfSalesBusinessImpl extends BaseFWBusinessImpl<BaseFWDAOImpl, BaseFWDTOImpl, BaseFWModelImpl>{

	@Value("${folder_upload}")
	private String folder2Upload;

	@Value("${folder_upload2}")
	private String folderUpload;

	@Value("${folder_upload}")
	private String folderTemp;

	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	
	@Autowired
	ReportCostOfSalesDAO reportCostOfSalesDAO;
	
	public DataListDTO doSearchDetailContract(ReportCostofSalesDTO obj) {
        List<ReportCostofSalesDTO> ls = reportCostOfSalesDAO.doSearchDetailContract(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public DataListDTO doSearchTHProvince(ReportCostofSalesDTO obj) {
		 List<ReportCostofSalesDTO> ls = reportCostOfSalesDAO.doSearchTHProvince(obj);
	        DataListDTO data = new DataListDTO();
	        data.setData(ls);
	        data.setTotal(obj.getTotalRecord());
	        data.setSize(obj.getPageSize());
	        data.setStart(1);
	        return data;
	}
	
	public DataListDTO doSearchDetailAllocation(ReportCostofSalesDTO obj) {
        List<ReportCostofSalesDTO> ls = reportCostOfSalesDAO.doSearchDetailAllocation(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

	public String exportFile(ReportCostofSalesDTO obj) throws Exception {
		// TODO Auto-generated method stub
			obj.setPage(null);
			obj.setPageSize(null);
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			String filePath = classloader.getResource("../" + "doc-template").getPath();
			InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BC_Chi_Tiet_HD_Chi_Phi_Ban_Hang.xlsx"));
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
					udir.getAbsolutePath() + File.separator + "BC_Chi_Tiet_HD_Chi_Phi_Ban_Hang.xlsx");
			List<ReportCostofSalesDTO> data = reportCostOfSalesDAO.doSearchDetailContract(obj);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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

				XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
				styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
				styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setItalic(false);
				font.setFontName("Times New Roman");
				font.setFontHeightInPoints((short) 12);

				XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
				styleYear.setAlignment(HorizontalAlignment.CENTER);
				styleYear.setFont(font);
				styleYear.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());

				Row rowCount = sheet.createRow(4);
				Cell cell1 = rowCount.createCell(0, CellType.STRING);
				cell1.setCellValue("Tổng");
				cell1.setCellStyle(style);
				
				Cell cell6 = rowCount.createCell(6, CellType.STRING);
				cell6.setCellValue((data.get(0).getPrirceContract() != null) ? data.get(0).getPrirceContract() : 0d);
				cell6.setCellStyle(styleCurrency);
				
				Cell cell16 = rowCount.createCell(16, CellType.STRING);
				cell16.setCellValue((data.get(0).getCostOfSales() != null) ? data.get(0).getCostOfSales() : 0d);
				cell16.setCellStyle(styleCurrency);
				data.remove(0);
				int i = 5;
				for (ReportCostofSalesDTO dto : data) {
					Row row = sheet.createRow(i++);
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("" + (i - 5));
					cell.setCellStyle(styleNumber);
					
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue((dto.getArea() != null) ? dto.getArea() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
					cell.setCellStyle(style);
					cell = row.createCell(3, CellType.STRING);
					cell.setCellValue((dto.getContendContract() != null) ? dto.getContendContract() : "");
					cell.setCellStyle(style);
					cell = row.createCell(4, CellType.STRING);
					cell.setCellValue((dto.getcDT() != null) ? dto.getcDT() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(5, CellType.STRING);
					cell.setCellValue((dto.getContractNumber() != null) ? dto.getContractNumber() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(6, CellType.STRING);
					cell.setCellValue((dto.getPrirceContract() != null) ? dto.getPrirceContract() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(7, CellType.STRING);
					cell.setCellValue((dto.getSignDate() != null) ? dto.getSignDate() : null);
					cell.setCellStyle(style);
					
					cell = row.createCell(8, CellType.STRING);
					cell.setCellValue((dto.getStartDate() != null) ? dto.getStartDate() : null);
					cell.setCellStyle(style);
					
					cell = row.createCell(9, CellType.STRING);
					cell.setCellValue((dto.getDayNumber() != null) ? dto.getDayNumber() : 0l);
					cell.setCellStyle(styleNumber);
					
					cell = row.createCell(10, CellType.STRING);
					cell.setCellValue((dto.getEndDate() != null) ? dto.getEndDate() : null);
					cell.setCellStyle(style);
					
					cell = row.createCell(11, CellType.STRING);
					cell.setCellValue((dto.getFilter() != null) ? dto.getFilter() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(12, CellType.STRING);
					cell.setCellValue((dto.getRecordedInMonth() != null) ? dto.getRecordedInMonth() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(13, CellType.STRING);
					cell.setCellValue((dto.getEmployeeCode() != null) ? dto.getEmployeeCode() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(14, CellType.STRING);
					cell.setCellValue((dto.getEmployeeName() != null) ? dto.getEmployeeName() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(15, CellType.STRING);
					cell.setCellValue((dto.getTilte() != null) ? dto.getTilte() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(16, CellType.STRING);
					cell.setCellValue((dto.getCostOfSales() != null) ? dto.getCostOfSales() : 0l);
					cell.setCellStyle(styleCurrency);
					
				}
			}

			workbook.write(outFile);
			workbook.close();
			outFile.close();

			String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BC_Chi_Tiet_HD_Chi_Phi_Ban_Hang.xlsx");
			return path;
	}
	
	public String exportFileTHProvince(ReportCostofSalesDTO obj) throws Exception {
		// TODO Auto-generated method stub
			obj.setPage(null);
			obj.setPageSize(null);
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			String filePath = classloader.getResource("../" + "doc-template").getPath();
			InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BC_TH_TINH.xlsx"));
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
					udir.getAbsolutePath() + File.separator + "BC_TH_TINH.xlsx");
			List<ReportCostofSalesDTO> data = reportCostOfSalesDAO.doSearchTHProvince(obj);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			XSSFSheet sheet = workbook.getSheetAt(0);
			if (data != null && !data.isEmpty()) {
				XSSFCellStyle style = ExcelUtils.styleText(sheet);
				XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
				XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
				XSSFCreationHelper createHelper = workbook.getCreationHelper();
				styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				styleNumber.setAlignment(HorizontalAlignment.RIGHT);

				XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
				styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
				styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setItalic(false);
				font.setFontName("Times New Roman");
				font.setFontHeightInPoints((short) 12);

				XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
				styleYear.setAlignment(HorizontalAlignment.CENTER);
				styleYear.setFont(font);
				styleYear.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());

				Row rowCount = sheet.createRow(3);
				Cell c1 = rowCount.createCell(1, CellType.STRING);
				c1.setCellValue("Tổng");
				c1.setCellStyle(style);
				
				Cell c2 = rowCount.createCell(2, CellType.STRING);
				c2.setCellValue((data.get(0).getGiamdoc() != null) ? data.get(0).getGiamdoc() : 0d);
				c2.setCellStyle(styleCurrency);
				
				Cell c3 = rowCount.createCell(3, CellType.STRING);
				c3.setCellValue((data.get(0).getPgdkythuat() != null) ? data.get(0).getPgdkythuat() : 0d);
				c3.setCellStyle(styleCurrency);
				
				Cell c4 = rowCount.createCell(4, CellType.STRING);
				c4.setCellValue((data.get(0).getPgdhatang() != null) ? data.get(0).getPgdhatang() : 0d);
				c4.setCellStyle(styleCurrency);
				
				Cell c5 = rowCount.createCell(5, CellType.STRING);
				c5.setCellValue((data.get(0).getPgdkinhdoanh() != null) ? data.get(0).getPgdkinhdoanh() : 0d);
				c5.setCellStyle(styleCurrency);
				
				Cell c6 = rowCount.createCell(6, CellType.STRING);
				c6.setCellValue((data.get(0).getPhongkythuat() != null) ? data.get(0).getPhongkythuat() : 0d);
				c6.setCellStyle(styleCurrency);
				
				Cell c7 = rowCount.createCell(7, CellType.STRING);
				c7.setCellValue((data.get(0).getPhonghatang() != null) ? data.get(0).getPhonghatang() : 0d);
				c7.setCellStyle(styleCurrency);
				
				Cell c8 = rowCount.createCell(8, CellType.STRING);
				c8.setCellValue((data.get(0).getPhongkinhdoanh() != null) ? data.get(0).getPhongkinhdoanh() : 0d);
				c8.setCellStyle(styleCurrency);
				
				Cell c9 = rowCount.createCell(9, CellType.STRING);
				c9.setCellValue((data.get(0).getKhoihotro() != null) ? data.get(0).getKhoihotro() : 0d);
				c9.setCellStyle(styleCurrency);
				
				Cell c10 = rowCount.createCell(10, CellType.STRING);
				c10.setCellValue((data.get(0).getGdttqh() != null) ? data.get(0).getGdttqh() : 0d);
				c10.setCellStyle(styleCurrency);
				
				Cell c11 = rowCount.createCell(11, CellType.STRING);
				c11.setCellValue((data.get(0).getEmployee() != null) ? data.get(0).getEmployee() : 0d);
				c11.setCellStyle(styleCurrency);
				
				Cell c12 = rowCount.createCell(12, CellType.STRING);
				c12.setCellValue((data.get(0).getTotalMoney() != null) ? data.get(0).getTotalMoney() : 0d);
				c12.setCellStyle(styleCurrency);
				
				data.remove(0);
				int i = 4;
				for (ReportCostofSalesDTO dto : data) {
					Row row = sheet.createRow(i++);
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("" + (i - 4));
					cell.setCellStyle(styleNumber);
					
					Cell cell1 = row.createCell(1, CellType.STRING);
					cell1.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
					cell1.setCellStyle(style);
					
					Cell cell2 = row.createCell(2, CellType.STRING);
					cell2.setCellValue((dto.getGiamdoc() != null) ? dto.getGiamdoc() : 0d);
					cell2.setCellStyle(styleCurrency);
					
					Cell cell3 = row.createCell(3, CellType.STRING);
					cell3.setCellValue((dto.getPgdkythuat() != null) ? dto.getPgdkythuat() : 0d);
					cell3.setCellStyle(styleCurrency);
					
					Cell cell4 = row.createCell(4, CellType.STRING);
					cell4.setCellValue((dto.getPgdhatang() != null) ? dto.getPgdhatang() : 0d);
					cell4.setCellStyle(styleCurrency);
					
					Cell cell5 = row.createCell(5, CellType.STRING);
					cell5.setCellValue((dto.getPgdkinhdoanh() != null) ? dto.getPgdkinhdoanh() : 0d);
					cell5.setCellStyle(styleCurrency);
					
					Cell cell6 = row.createCell(6, CellType.STRING);
					cell6.setCellValue((dto.getPhongkythuat() != null) ? dto.getPhongkythuat() : 0d);
					cell6.setCellStyle(styleCurrency);
					
					Cell cell7 = row.createCell(7, CellType.STRING);
					cell7.setCellValue((dto.getPhonghatang() != null) ? dto.getPhonghatang() : 0d);
					cell7.setCellStyle(styleCurrency);
					
					Cell cell8 = row.createCell(8, CellType.STRING);
					cell8.setCellValue((dto.getPhongkinhdoanh() != null) ? dto.getPhongkinhdoanh() : 0d);
					cell8.setCellStyle(styleCurrency);
					
					Cell cell9 = row.createCell(9, CellType.STRING);
					cell9.setCellValue((dto.getKhoihotro() != null) ? dto.getKhoihotro() : 0d);
					cell9.setCellStyle(styleCurrency);
					
					Cell cell10 = row.createCell(10, CellType.STRING);
					cell10.setCellValue((dto.getGdttqh() != null) ? dto.getGdttqh() : 0d);
					cell10.setCellStyle(styleCurrency);
					
					Cell cell11 = row.createCell(11, CellType.STRING);
					cell11.setCellValue((dto.getEmployee() != null) ? dto.getEmployee() : 0d);
					cell11.setCellStyle(styleCurrency);
					
					Cell cell12 = row.createCell(12, CellType.STRING);
					cell12.setCellValue((dto.getTotalMoney() != null) ? dto.getTotalMoney() : 0d);
					cell12.setCellStyle(styleCurrency);
				}
			}

			workbook.write(outFile);
			workbook.close();
			outFile.close();

			String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BC_TH_TINH.xlsx");
			return path;
	}
	
	public String exportFileAllocation(ReportCostofSalesDTO obj) throws Exception {
		// TODO Auto-generated method stub
			obj.setPage(null);
			obj.setPageSize(null);
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			String filePath = classloader.getResource("../" + "doc-template").getPath();
			InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BC_Chi_Tiet_Phan_Bo_Chi_Phi_Ban_Hang.xlsx"));
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
					udir.getAbsolutePath() + File.separator + "BC_Chi_Tiet_Phan_Bo_Chi_Phi_Ban_Hang.xlsx");
			List<ReportCostofSalesDTO> data = reportCostOfSalesDAO.doSearchDetailAllocation(obj);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			XSSFSheet sheet = workbook.getSheetAt(0);
			if (data != null && !data.isEmpty()) {
				XSSFCellStyle style = ExcelUtils.styleText(sheet);
				XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
				XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
				XSSFCreationHelper createHelper = workbook.getCreationHelper();
				styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				styleNumber.setAlignment(HorizontalAlignment.RIGHT);

				XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
				styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
				styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setItalic(false);
				font.setFontName("Times New Roman");
				font.setFontHeightInPoints((short) 12);

				XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
				styleYear.setAlignment(HorizontalAlignment.CENTER);
				styleYear.setFont(font);
				styleYear.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());

				Row rowCount = sheet.createRow(4);
				Cell cell1 = rowCount.createCell(0, CellType.STRING);
				cell1.setCellValue("Tổng");
				cell1.setCellStyle(style);
				
				Cell cell5 = rowCount.createCell(5, CellType.STRING);
				cell5.setCellValue((data.get(0).getBranchFund() != null) ? data.get(0).getBranchFund() : 0d);
				cell5.setCellStyle(styleCurrency);
				
				Cell cell7 = rowCount.createCell(7, CellType.STRING);
				cell7.setCellValue((data.get(0).getTotalMoney() != null) ? data.get(0).getTotalMoney() : 0d);
				cell7.setCellStyle(styleCurrency);
				
				Cell cell8 = rowCount.createCell(8, CellType.STRING);
				cell8.setCellValue((data.get(0).getGiamdoc() != null) ? data.get(0).getGiamdoc() : 0d);
				cell8.setCellStyle(styleCurrency);
				
				Cell cell9 = rowCount.createCell(10, CellType.STRING);
				cell9.setCellValue((data.get(0).getPgdhatang() != null) ? data.get(0).getPgdhatang() : 0d);
				cell9.setCellStyle(styleCurrency);
				
				Cell cell10 = rowCount.createCell(11, CellType.STRING);
				cell10.setCellValue((data.get(0).getPgdkinhdoanh() != null) ? data.get(0).getPgdkinhdoanh() : 0d);
				cell10.setCellStyle(styleCurrency);
				
				Cell cell11 = rowCount.createCell(13, CellType.STRING);
				cell11.setCellValue((data.get(0).getPhonghatang() != null) ? data.get(0).getPhonghatang() : 0d);
				cell11.setCellStyle(styleCurrency);
				
				Cell cell12 = rowCount.createCell(14, CellType.STRING);
				cell12.setCellValue((data.get(0).getPhongkinhdoanh() != null) ? data.get(0).getPhongkinhdoanh() : 0d);
				cell12.setCellStyle(styleCurrency);
				
				Cell cell13 = rowCount.createCell(15, CellType.STRING);
				cell13.setCellValue((data.get(0).getKhoihotro() != null) ? data.get(0).getKhoihotro() : 0d);
				cell13.setCellStyle(styleCurrency);
				
				Cell cell14 = rowCount.createCell(16, CellType.STRING);
				cell14.setCellValue((data.get(0).getGdttqh() != null) ? data.get(0).getGdttqh() : 0d);
				cell14.setCellStyle(styleCurrency);
				
				data.remove(0);
				int i = 5;
				for (ReportCostofSalesDTO dto : data) {
					Row row = sheet.createRow(i++);
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("" + (i - 5));
					cell.setCellStyle(styleNumber);
					
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue((dto.getContractNumber() != null) ? dto.getContractNumber() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(3, CellType.STRING);
					cell.setCellValue((dto.getPrirceContract() != null) ? dto.getPrirceContract() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(4, CellType.STRING);
					cell.setCellValue((dto.getCostOfSales() != null) ? dto.getCostOfSales() : 0l);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(5, CellType.STRING);
					cell.setCellValue((data.get(0).getBranchFund() != null) ? data.get(0).getBranchFund() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(6, CellType.STRING);
					cell.setCellValue((dto.getEmployeeCode() != null) ? dto.getEmployeeCode() : "");
					cell.setCellStyle(style);
					
					cell = row.createCell(7, CellType.STRING);
					cell.setCellValue((data.get(0).getTotalMoney() != null) ? data.get(0).getTotalMoney() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(8, CellType.STRING);
					cell.setCellValue((data.get(0).getGiamdoc() != null) ? data.get(0).getGiamdoc() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(9, CellType.STRING);
					cell.setCellValue((data.get(0).getPgdkythuat() != null) ? data.get(0).getPgdkythuat() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(10, CellType.STRING);
					cell.setCellValue((data.get(0).getPgdhatang() != null) ? data.get(0).getPgdhatang() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(11, CellType.STRING);
					cell.setCellValue((data.get(0).getPgdkinhdoanh() != null) ? data.get(0).getPgdkinhdoanh() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(12, CellType.STRING);
					cell.setCellValue((data.get(0).getPhongkythuat() != null) ? data.get(0).getPhongkythuat() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(13, CellType.STRING);
					cell.setCellValue((data.get(0).getPhonghatang() != null) ? data.get(0).getPhonghatang() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(14, CellType.STRING);
					cell.setCellValue((data.get(0).getPhongkinhdoanh() != null) ? data.get(0).getPhongkinhdoanh() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(15, CellType.STRING);
					cell.setCellValue((data.get(0).getKhoihotro() != null) ? data.get(0).getKhoihotro() : 0d);
					cell.setCellStyle(styleCurrency);
					
					cell = row.createCell(16, CellType.STRING);
					cell.setCellValue((data.get(0).getGdttqh() != null) ? data.get(0).getGdttqh() : 0d);
					cell.setCellStyle(styleCurrency);
					
				}
			}

			workbook.write(outFile);
			workbook.close();
			outFile.close();

			String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BC_Chi_Tiet_Phan_Bo_Chi_Phi_Ban_Hang.xlsx");
			return path;
	}

}
