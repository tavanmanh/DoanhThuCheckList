package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
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

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.dao.RpOrderlyWoDAO;
import com.viettel.coms.dto.ReportDTO;
import com.viettel.coms.dto.ReportErpAmsWoDTO;
import com.viettel.coms.dto.RpOrderlyWoDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Service("rpOrderlyBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RpOrderlyBusinessImpl extends BaseFWBusinessImpl<RpOrderlyWoDAO, RpOrderlyWoDTO, BaseFWModelImpl> implements RpOrderlyBusiness{

	@Autowired
	private RpOrderlyWoDAO rpOrderlyWoDAO;
	
	@Value("${folder_upload2}")
	private String folder2Upload;
	
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	private XSSFSheet sheet1;
	
	@Override
	public DataListDTO getDataReceiveWoSynthetic(RpOrderlyWoDTO obj) {
		List<RpOrderlyWoDTO> ls = new ArrayList<RpOrderlyWoDTO>();
		ls = rpOrderlyWoDAO.getDataReceiveWoSynthetic(obj, false);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	@Override
	public DataListDTO getDataReceiveWoDetail(RpOrderlyWoDTO obj) {
		List<RpOrderlyWoDTO> ls = new ArrayList<RpOrderlyWoDTO>();
		ls = rpOrderlyWoDAO.getDataReceiveWoDetail(obj, false);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	@Override
	public String exportFile(RpOrderlyWoDTO obj) throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		String fileNameBm = null;
		List<RpOrderlyWoDTO> dataSheet1 = new ArrayList<>();
		List<RpOrderlyWoDTO> dataSheet2 = new ArrayList<>();
		dataSheet1 = rpOrderlyWoDAO.getDataReceiveWoSynthetic(obj, true);
		dataSheet2 = rpOrderlyWoDAO.getDataReceiveWoDetail(obj, true);
		if (obj.getStatus().equals("1")) {
			fileNameBm = "BaoCao_TiepNhanWO_QuaHan.xlsx";
		}
		if (obj.getStatus().equals("2")) {
			fileNameBm = "BaoCao_GiaoWO_QuaHan.xlsx";
		}
		if (obj.getStatus().equals("3")) {
			fileNameBm = "BaoCao_DuyetWO_QuaHan.xlsx";
		}

		InputStream file = new BufferedInputStream(new FileInputStream(filePath + fileNameBm));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + fileNameBm);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet1 = workbook.getSheetAt(0);
		XSSFSheet sheet2 = workbook.getSheetAt(1);
		if (dataSheet1 != null && !dataSheet1.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet1);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet1);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet1);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet1);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0%"));
			int i = 1;
			int j = 1;
			for (RpOrderlyWoDTO dto : dataSheet1) {
				Row row = sheet1.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getCdLevel2Name() != null) ? dto.getCdLevel2Name() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getTong() != null) ? dto.getTong() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getQuahan() != null) ? dto.getQuahan() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getTyle() != null) ? dto.getTyle()/100d : 0d);
				cell.setCellStyle(styleCurrency);
			}
			for (RpOrderlyWoDTO dto : dataSheet2) {
				Row row = sheet2.createRow(j++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (j - 1));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getWoCode() != null) ? dto.getWoCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getWoName() != null) ? dto.getWoName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getNgayGiaoWo() != null) ? dto.getNgayGiaoWo() : "");
				cell.setCellStyle(style);
				if (obj.getStatus().equals("1") || obj.getStatus().equals("2")) {
					cell = row.createCell(6, CellType.STRING);
					cell.setCellValue((dto.getNgayNhanWo() != null) ? dto.getNgayNhanWo() : "");
					cell.setCellStyle(style);
					cell = row.createCell(7, CellType.STRING);
					cell.setCellValue((dto.getNgayGiaoFt() != null) ? dto.getNgayGiaoFt() : "");
					cell.setCellStyle(style);
				} else {
					cell = row.createCell(6, CellType.STRING);
					cell.setCellValue((dto.getNgayFtThucHienXong() != null) ? dto.getNgayFtThucHienXong() : "");
					cell.setCellStyle(style);
					cell = row.createCell(7, CellType.STRING);
					cell.setCellValue((dto.getNgayCdDuyet() != null) ? dto.getNgayCdDuyet() : "");
					cell.setCellStyle(style);
				}

				if (obj.getStatus().equals("2") || obj.getStatus().equals("3")) {
					cell = row.createCell(8, CellType.STRING);
					cell.setCellValue((dto.getCdLevel2Name() != null) ? dto.getCdLevel2Name() : "");
					cell.setCellStyle(style);
					cell = row.createCell(9, CellType.STRING);
					cell.setCellValue((dto.getFullName() != null) ? dto.getFullName() : "");
					cell.setCellStyle(style);
					cell = row.createCell(10, CellType.STRING);
					cell.setCellValue((dto.getEmail() != null) ? dto.getEmail() : "");
					cell.setCellStyle(style);
					cell = row.createCell(11, CellType.STRING);
					cell.setCellValue((dto.getPhoneNumber() != null) ? dto.getPhoneNumber() : "");
					cell.setCellStyle(style);
					cell = row.createCell(12, CellType.STRING);
					cell.setCellValue((dto.getMoneyValue() != null) ? dto.getMoneyValue() : 0d);
					cell.setCellStyle(style);
					cell = row.createCell(13, CellType.STRING);
					cell.setCellValue((dto.getState() != null) ? dto.getState() : "");
					cell.setCellStyle(style);
					cell = row.createCell(14, CellType.STRING);
					cell.setCellValue((dto.getTrangThai() != null) ? dto.getTrangThai() : "");
					cell.setCellStyle(style);
				} else {
					cell = row.createCell(8, CellType.STRING);
					cell.setCellValue((dto.getNgayFtTiepNhan() != null) ? dto.getNgayFtTiepNhan() : "");
					cell.setCellStyle(style);
					cell = row.createCell(9, CellType.STRING);
					cell.setCellValue((dto.getCdLevel2Name() != null) ? dto.getCdLevel2Name() : "");
					cell.setCellStyle(style);
					cell = row.createCell(10, CellType.STRING);
					cell.setCellValue((dto.getFullName() != null) ? dto.getFullName() : "");
					cell.setCellStyle(style);
					cell = row.createCell(11, CellType.STRING);
					cell.setCellValue((dto.getEmail() != null) ? dto.getEmail() : "");
					cell.setCellStyle(style);
					cell = row.createCell(12, CellType.STRING);
					cell.setCellValue((dto.getPhoneNumber() != null) ? dto.getPhoneNumber() : "");
					cell.setCellStyle(style);
					cell = row.createCell(13, CellType.STRING);
					cell.setCellValue((dto.getMoneyValue() != null) ? dto.getMoneyValue() : 0d);
					cell.setCellStyle(style);
					cell = row.createCell(14, CellType.STRING);
					cell.setCellValue((dto.getState() != null) ? dto.getState() : "");
					cell.setCellStyle(style);
					cell = row.createCell(15, CellType.STRING);
					cell.setCellValue((dto.getTrangThai() != null) ? dto.getTrangThai() : "");
					cell.setCellStyle(style);
				}
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileNameBm);
		return path;
	}
	
	//Huypq-07062021-start
	@Override
	public DataListDTO doSearchReportErpAmsWo(ReportErpAmsWoDTO obj) {
		List<ReportErpAmsWoDTO> ls = new ArrayList<ReportErpAmsWoDTO>();
		ls = rpOrderlyWoDAO.doSearchReportErpAmsWo(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	//Huy-end
	
	@Override
	public DataListDTO getForAutoCompleteByGroupLv2(SysGroupDTO obj) {
		List<SysGroupDTO> ls = rpOrderlyWoDAO.getForAutoCompleteByGroupLv2(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getTotalRecord());
		data.setStart(1);
		return data;
	}
	
	@Override
	public DataListDTO doSearchGeneralCtv(ReportDTO obj) {
		List<ReportDTO> ls = new ArrayList<ReportDTO>();
		if(obj.getSysGroupId()!=null && obj.getDateFrom()!=null && obj.getDateTo()!=null) {
			ls = rpOrderlyWoDAO.doSearchGeneralCtv(obj);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	@Override
	public DataListDTO doSearchDetailCtv(ReportDTO obj) {
		List<ReportDTO> ls = new ArrayList<ReportDTO>();
		if(obj.getSysGroupId()!=null) {
			ls = rpOrderlyWoDAO.doSearchDetailCtv(obj);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	@Override
	public DataListDTO doSearchZoningCtv(ReportDTO obj) {
		List<ReportDTO> ls = new ArrayList<ReportDTO>();
		if(StringUtils.isNotBlank(obj.getSysGroupName())) {
			ls = rpOrderlyWoDAO.doSearchZoningCtv(obj);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	@Override
	public DataListDTO doSearchRevenueCtv(ReportDTO obj) {
		List<ReportDTO> ls = new ArrayList<ReportDTO>();
		if((obj.getTypeBc().equals("1") && obj.getSysGroupId()!=null) || (obj.getTypeBc().equals("2") && StringUtils.isNotBlank(obj.getSysGroupName()))) {
			ls = rpOrderlyWoDAO.doSearchRevenueCtv(obj);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public DataListDTO doSearchProvinceInPopup(CatProvinceDTO obj) {
		List<CatProvinceDTO> ls = new ArrayList<CatProvinceDTO>();
		ls = rpOrderlyWoDAO.doSearchProvince(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	@Override
	public String exportFileCtv(ReportDTO obj) throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		String fileNameBm = null;
		List<ReportDTO> lsTh = new ArrayList<ReportDTO>();
		List<ReportDTO> lsDt = new ArrayList<ReportDTO>();
		
		if (obj.getStatus().equals("Tổng hợp")) {
			fileNameBm = "BaoCao_TongHop_CTV.xlsx";
			if(obj.getSysGroupId()!=null && obj.getDateFrom()!=null && obj.getDateTo()!=null) {
				lsTh = rpOrderlyWoDAO.doSearchGeneralCtv(obj);
			}
		}
		if (obj.getStatus().equals("Doanh thu")) {
			if(obj.getTypeBc().equals("1")) {
				fileNameBm = "BaoCao_DoanhThu_CTV.xlsx";
			} else {
				fileNameBm = "BaoCao_DoanhThu_CTV_Xa.xlsx";
			}
			lsDt = rpOrderlyWoDAO.doSearchRevenueCtv(obj);
		}

		InputStream file = new BufferedInputStream(new FileInputStream(filePath + fileNameBm));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + fileNameBm);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet1 = workbook.getSheetAt(0);
		if (lsTh != null && !lsTh.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet1);
			XSSFCellStyle styleNoborder = ExcelUtils.styleText(sheet1);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet1);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet1);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0%"));
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet1);
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			XSSFCellStyle styleDateNoBorder = ExcelUtils.styleDate(sheet1);
			styleDateNoBorder.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNoborder.setBorderTop(BorderStyle.NONE);
			styleNoborder.setBorderLeft(BorderStyle.NONE);
			styleNoborder.setBorderRight(BorderStyle.NONE);
			styleNoborder.setBorderBottom(BorderStyle.NONE);
			styleDateNoBorder.setBorderTop(BorderStyle.NONE);
			styleDateNoBorder.setBorderLeft(BorderStyle.NONE);
			styleDateNoBorder.setBorderRight(BorderStyle.NONE);
			styleDateNoBorder.setBorderBottom(BorderStyle.NONE);
			
			int i = 6;
			
			Row rowS10 = sheet1.getRow(0);
			Cell cellS10 = rowS10.createCell(2, CellType.STRING);
			cellS10.setCellValue(obj.getSysGroupName());
			cellS10.setCellStyle(styleNoborder);
			rowS10 = sheet1.getRow(1);
			cellS10 = rowS10.createCell(2, CellType.STRING);
			cellS10.setCellValue(obj.getSysGroupCode());
			cellS10.setCellStyle(styleNoborder);
			rowS10 = sheet1.getRow(2);
			cellS10 = rowS10.createCell(3, CellType.STRING);
			cellS10.setCellValue(obj.getDateFrom());
			cellS10.setCellStyle(styleDateNoBorder);
			cellS10 = rowS10.createCell(5, CellType.STRING);
			cellS10.setCellValue(obj.getDateTo());
			cellS10.setCellStyle(styleDateNoBorder);
			
			for (ReportDTO dto : lsTh) {
				Row row = sheet1.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 6));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getDistrictName() != null) ? dto.getDistrictName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getNumberCtvActiveFirstStage() != null) ? dto.getNumberCtvActiveFirstStage() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getNumberCtvNewInsideStage() != null) ? dto.getNumberCtvNewInsideStage() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getNumberCtvEndInsideStage() != null) ? dto.getNumberCtvEndInsideStage() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getNumberCtvActiveFinalStage() != null) ? dto.getNumberCtvActiveFinalStage() : 0l);
				cell.setCellStyle(style);
			}
		}
		
		if (lsDt != null && !lsDt.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet1);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet1);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet1);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet1);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0%"));
			XSSFCellStyle styleBold = ExcelUtils.styleBold(sheet1);
			styleBold.setAlignment(HorizontalAlignment.CENTER);
			int j = 4;
		
			if(obj.getTypeBc().equals("1")) {
				Row rowS10 = sheet1.createRow(0);
				Cell cellS10 = rowS10.createCell(0, CellType.STRING);
				cellS10.setCellValue("BÁO CÁO DOANH THU, HOA HỒNG CTV THEO ĐƠN VỊ");
				cellS10.setCellStyle(styleBold);
				
				for (ReportDTO dto : lsDt) {
					Row row = sheet1.createRow(j++);
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("" + (j - 4));
					cell.setCellStyle(styleNumber);
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue((dto.getCommuneName() != null) ? dto.getCommuneName() : "");
					cell.setCellStyle(style);
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue((dto.getNumberCtvCurrent() != null) ? dto.getNumberCtvCurrent() : 0l);
					cell.setCellStyle(style);
					cell = row.createCell(3, CellType.STRING);
					cell.setCellValue((dto.getNumberCtvRevenueIncurred() != null) ? dto.getNumberCtvRevenueIncurred() : 0l);
					cell.setCellStyle(style);
					cell = row.createCell(4, CellType.STRING);
					cell.setCellValue((dto.getSumRevenueXhh() != null) ? dto.getSumRevenueXhh() : 0d);
					cell.setCellStyle(styleNumber);
					cell = row.createCell(5, CellType.STRING);
					cell.setCellValue((dto.getMediumRevenueXhh() != null) ? dto.getMediumRevenueXhh() : 0d);
					cell.setCellStyle(styleCurrency);
					cell = row.createCell(6, CellType.STRING);
					cell.setCellValue((dto.getSumDiscountXhh() != null) ? dto.getSumDiscountXhh() : 0d);
					cell.setCellStyle(styleNumber);
					cell = row.createCell(7, CellType.STRING);
					cell.setCellValue((dto.getMediumDiscountXhh() != null) ? dto.getMediumDiscountXhh() : 0d);
					cell.setCellStyle(styleCurrency);
				}
			} else {
				Row rowS10 = sheet1.createRow(0);
				Cell cellS10 = rowS10.createCell(0, CellType.STRING);
				cellS10.setCellValue("BÁO CÁO DOANH THU, HOA HỒNG CTV THEO XÃ/PHƯỜNG");
				cellS10.setCellStyle(styleBold);
				
				for (ReportDTO dto : lsDt) {
					Row row = sheet1.createRow(j++);
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("" + (j - 4));
					cell.setCellStyle(styleNumber);
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue((dto.getDistrictName() != null) ? dto.getDistrictName() : "");
					cell.setCellStyle(style);
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue((dto.getCommuneName() != null) ? dto.getCommuneName() : "");
					cell.setCellStyle(style);
					cell = row.createCell(3, CellType.STRING);
					cell.setCellValue((dto.getNumberCtvCurrent() != null) ? dto.getNumberCtvCurrent() : 0l);
					cell.setCellStyle(style);
					cell = row.createCell(4, CellType.STRING);
					cell.setCellValue((dto.getNumberCtvRevenueIncurred() != null) ? dto.getNumberCtvRevenueIncurred() : 0l);
					cell.setCellStyle(style);
					cell = row.createCell(5, CellType.STRING);
					cell.setCellValue((dto.getSumRevenueXhh() != null) ? dto.getSumRevenueXhh() : 0d);
					cell.setCellStyle(styleNumber);
					cell = row.createCell(6, CellType.STRING);
					cell.setCellValue((dto.getMediumRevenueXhh() != null) ? dto.getMediumRevenueXhh() : 0d);
					cell.setCellStyle(styleCurrency);
					cell = row.createCell(7, CellType.STRING);
					cell.setCellValue((dto.getSumDiscountXhh() != null) ? dto.getSumDiscountXhh() : 0d);
					cell.setCellStyle(styleNumber);
					cell = row.createCell(8, CellType.STRING);
					cell.setCellValue((dto.getMediumDiscountXhh() != null) ? dto.getMediumDiscountXhh() : 0d);
					cell.setCellStyle(styleCurrency);
				}
			}
			
		}
			
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileNameBm);
		return path;
	}
}
