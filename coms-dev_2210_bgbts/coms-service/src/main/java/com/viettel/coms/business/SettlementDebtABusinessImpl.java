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
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.dao.SettlementDebtADAO;
import com.viettel.coms.dto.SettlementDebtADTO;
import com.viettel.coms.dto.SettlementDebtARpDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Service("settlementDebtABusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SettlementDebtABusinessImpl extends BaseFWBusinessImpl<SettlementDebtADAO, SettlementDebtADTO, BaseFWModelImpl>
implements SettlementDebtABusiness{
	
	@Autowired
	private SettlementDebtADAO settlementDebtADAO;
	
	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	
	static Logger LOGGER = LoggerFactory.getLogger(SettlementDebtABusinessImpl.class);

	public DataListDTO doSearch(SettlementDebtADTO obj) {
		List<SettlementDebtADTO> ls = settlementDebtADAO.doSearch(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public String exportExcelTonACap(SettlementDebtADTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BaoCaoTonCongNoVTACap.xlsx"));
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
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BaoCaoTonCongNoVTACap.xlsx");
		List<SettlementDebtADTO> data = settlementDebtADAO.doSearch(obj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		Row row1 = sheet.getRow(3);
		Cell cell1 = row1.getCell(2);
		cell1.setCellValue("Thời gian lấy số liệu từ: "+ formatter.format(obj.getDateFrom()));
		Cell cell2 = row1.getCell(4);
		cell2.setCellValue("Thời gian lấy số liệu đến: "+ formatter.format(obj.getDateTo()));
		Cell cell3 = row1.getCell(7);
		cell3.setCellValue("Ngày xuất: "+ formatter.format(new Date()));
		
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			styleDate.setAlignment(HorizontalAlignment.CENTER);
			int i = 7;
			for (SettlementDebtADTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 7));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getBpartnerCrName() != null) ? dto.getBpartnerCrName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProjectCrName() != null) ? dto.getProjectCrName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getContractName() != null) ? dto.getContractName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getConstructionValue() != null) ? dto.getConstructionValue() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getProductCrValue() != null) ? dto.getProductCrValue() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getProductCrName() != null) ? dto.getProductCrName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getUnitTypeName() != null) ? dto.getUnitTypeName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getAmountTHDu() != null) ? dto.getAmountTHDu() : 0l);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getMoneyTHDu() != null) ? dto.getMoneyTHDu() : 0l);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getAmountTHNhap() != null) ? dto.getAmountTHNhap() : 0l);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getMoneyTHNhap() != null) ? dto.getMoneyTHNhap() : 0l);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getAmountErp() != null) ? dto.getAmountErp() : 0l);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getMoneyErp() != null) ? dto.getMoneyErp() : 0l);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue(((dto.getAmountTHDu() != null) ? dto.getAmountTHDu() : 0l) + ((dto.getAmountTHNhap() != null) ? dto.getAmountTHNhap() : 0l) -((dto.getAmountErp() != null) ? dto.getAmountErp() : 0l));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue(((dto.getMoneyTHDu() != null) ? dto.getMoneyTHDu() : 0l)+ ((dto.getMoneyTHNhap() != null) ? dto.getMoneyTHNhap() : 0l)-((dto.getMoneyErp() != null) ? dto.getMoneyErp() : 0l));
				cell.setCellStyle(styleNumber);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BaoCaoTonCongNoVTACap.xlsx");
		return path;
	}
	
	public DataListDTO doSearchThreeMonth(SettlementDebtARpDTO obj) {
		List<SettlementDebtARpDTO> ls = settlementDebtADAO.doSearchThreeMonth(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public String exportExcelACapThreeMonth(SettlementDebtARpDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BaoCaoVtACapQuaHan135ngay.xlsx"));
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
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BaoCaoVtACapQuaHan3Thang.xlsx");
		List<SettlementDebtARpDTO> data = settlementDebtADAO.doSearchThreeMonth(obj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		Row row1 = sheet.getRow(3);
		Cell cell1 = row1.getCell(2);
		cell1.setCellValue("Thời gian lấy số liệu từ: "+ formatter.format(obj.getDateFrom()));
		Cell cell2 = row1.getCell(3);
		cell2.setCellValue("Thời gian lấy số liệu đến: "+ formatter.format(obj.getDateTo()));
		Cell cell3 = row1.getCell(7);
		cell3.setCellValue("Ngày xuất: "+ formatter.format(new Date()));
		
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			styleDate.setAlignment(HorizontalAlignment.CENTER);
			int i = 7;
			for (SettlementDebtARpDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 7));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getRealIeTransDate() != null) ? dto.getRealIeTransDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getGoodsCode() != null) ? dto.getGoodsCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getGoodsName() != null) ? dto.getGoodsName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getGoodsUnitName() != null) ? dto.getGoodsUnitName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getAmountTotal() != null) ? dto.getAmountTotal() : "");
				cell.setCellStyle(styleNumber);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getMoneyTotal() != null) ? dto.getMoneyTotal() : "");
				cell.setCellStyle(styleNumber);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(styleNumber);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue(dto.getOverDay() != null ? dto.getOverDay() : "");
				cell.setCellStyle(styleNumber);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BaoCaoVtACapQuaHan3Thang.xlsx");
		return path;
	}
}
