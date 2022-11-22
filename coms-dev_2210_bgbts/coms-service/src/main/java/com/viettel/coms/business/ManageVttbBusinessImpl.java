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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
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

import com.google.common.collect.Lists;
import com.viettel.coms.bo.DetailMonthPlanBO;
import com.viettel.coms.bo.ManageVttbBO;
import com.viettel.coms.dao.ManageVttbDAO;
import com.viettel.coms.dao.RevokeCashMonthPlanDAO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.dto.ManageUsedMaterialDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;


/**
 * @author HoangNH38
 */
@Service("manageVttbBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ManageVttbBusinessImpl
		extends BaseFWBusinessImpl<ManageVttbDAO, ManageVttbDTO, ManageVttbBO>
		implements ManageVttbBusiness {

	static Logger LOGGER = LoggerFactory.getLogger(ManageVttbBusinessImpl.class);

	@Autowired
	private ManageVttbDAO manageVttbDAO;



	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	public ManageVttbBusinessImpl() {
		tModel = new ManageVttbBO();
		tDAO = manageVttbDAO;
	}

	@Override
	public ManageVttbDAO gettDAO() {
		return manageVttbDAO;
	}


	// tatph-start-12/12/2019
	public DataListDTO doSearch(ManageVttbDTO obj, HttpServletRequest request) {
		List<ManageVttbDTO> ls = new ArrayList<ManageVttbDTO>();
		obj.setTotalRecord(0);
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = manageVttbDAO.doSearch(obj, groupIdList);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public DataListDTO doSearchUsedMaterial(ManageVttbDTO obj, HttpServletRequest request) {
		List<ManageVttbDTO> ls = new ArrayList<ManageVttbDTO>();
		obj.setTotalRecord(0);
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = manageVttbDAO.doSearchUsedMaterial(obj, groupIdList);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	protected static final String USER_SESSION_KEY = "kttsUserSession";

	public KttsUserSession getUserSession(HttpServletRequest request) {
		KttsUserSession s = (KttsUserSession) request.getSession().getAttribute(USER_SESSION_KEY);
		if (s == null) {
			throw new BusinessException("user is not authen");
		}
		return s;

	}


	public void saveUsedMaterial(ManageUsedMaterialDTO obj) {
		Long count = manageVttbDAO.countUsedMaterial(obj);
		if(count != 0) {
			obj.setUpdateDate(new Date());
			manageVttbDAO.updateUsedMaterial(obj);
		}else {
			obj.setCreateDate(new Date());
			manageVttbDAO.saveUsedMaterial(obj);
		}
		
	}
	
	public String getExcelTemplate(ManageVttbDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Bao_cao_quan_ly_vttb.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Bao_cao_quan_ly_vttb.xlsx");
		List<String> groupIdList = new ArrayList<>();
		List<ManageVttbDTO> data = manageVttbDAO.doSearchUsedMaterial(obj, groupIdList);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		Long totalSoLuongPxk = 0l;
		Long totalGiaTriPxk = 0l;
		Long totalSoLuongSuDung = 0l;
		Long totalGiaTriSuDung = 0l;
		Long totalSoLuongDuThua = 0l;
		Long totalGiaTriDuThua = 0l;
		Long totalSoLuongThuHoi = 0l;
		Long totalGiaTriThuHoi = 0l;
				
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleBold = ExcelUtils.styleBold(sheet);
			XSSFCellStyle styleBoldCurrency = ExcelUtils.styleBoldCurrency(sheet);
			XSSFCellStyle styleCurrency = ExcelUtils.styleCurrencyV2(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 6;
			for (ManageVttbDTO dto : data) {
				//
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 6));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getPxkCode() != null ? dto.getPxkCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getVttbCode() != null ? dto.getVttbCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getSoLuongPxk());
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getGiaTriPxk());
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getSoLuongSuDung());
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getGiaTriSuDung());
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(dto.getSoLuongThuHoi());
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(dto.getGiaTriThuHoi());
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(dto.getSoLuongDuThua() );
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue(dto.getGiaTriDuThua());
				cell.setCellStyle(styleCurrency);
			
				
				 totalSoLuongPxk = totalSoLuongPxk + (dto.getSoLuongPxk());
				 totalGiaTriPxk = totalGiaTriPxk + (dto.getGiaTriPxk());
				 totalSoLuongSuDung = totalSoLuongSuDung + (dto.getSoLuongSuDung());
				 totalGiaTriSuDung = totalGiaTriSuDung + (dto.getGiaTriSuDung()) ;
				 totalSoLuongDuThua = totalSoLuongDuThua + (dto.getSoLuongDuThua());
				 totalGiaTriDuThua = totalGiaTriDuThua + (dto.getGiaTriDuThua());
				 totalSoLuongThuHoi = totalSoLuongThuHoi + (dto.getSoLuongThuHoi());
				 totalGiaTriThuHoi = totalGiaTriThuHoi + (dto.getGiaTriThuHoi());
			}
			Row row = sheet.createRow(data.size()+6);
			Cell cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleBold);
			//
			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleBold);
			//
			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue("");
			cell.setCellStyle(styleBold);
			//
			cell = row.createCell(3, CellType.STRING);
			cell.setCellValue(" Tổng cộng ");
			cell.setCellStyle(styleBold);
			//
			cell = row.createCell(4, CellType.STRING);
			cell.setCellValue(totalSoLuongPxk);
			cell.setCellStyle(styleBoldCurrency);
			//
			cell = row.createCell(5, CellType.STRING);
			cell.setCellValue(totalGiaTriPxk);
			cell.setCellStyle(styleBoldCurrency);
			//
			cell = row.createCell(6, CellType.STRING);
			cell.setCellValue(totalSoLuongSuDung);
			cell.setCellStyle(styleBoldCurrency);
			//
			cell = row.createCell(7, CellType.STRING);
			cell.setCellValue(totalGiaTriSuDung);
			cell.setCellStyle(styleBoldCurrency);
			//
			cell = row.createCell(8, CellType.STRING);
			cell.setCellValue(totalSoLuongThuHoi);
			cell.setCellStyle(styleBoldCurrency);
			//
			cell = row.createCell(9, CellType.STRING);
			cell.setCellValue(totalGiaTriThuHoi);
			cell.setCellStyle(styleBoldCurrency);
			//
			cell = row.createCell(10, CellType.STRING);
			cell.setCellValue(totalSoLuongDuThua);
			cell.setCellStyle(styleBoldCurrency);
			//
			cell = row.createCell(11, CellType.STRING);
			cell.setCellValue(totalGiaTriDuThua);
			cell.setCellStyle(styleBoldCurrency);
			
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Bao_cao_quan_ly_vttb.xlsx");
		return path;
	}


}
