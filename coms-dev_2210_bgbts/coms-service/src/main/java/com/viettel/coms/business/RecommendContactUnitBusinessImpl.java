package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.viettel.coms.bo.ManageHcqtBO;
import com.viettel.coms.dao.ConstructionTaskDAO;
import com.viettel.coms.dao.RecommendContactUnitDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.ContactUnitDTO;
import com.viettel.coms.dto.ContactUnitDetailDTO;
import com.viettel.coms.dto.ContactUnitDetailDescriptionDTO;
import com.viettel.coms.dto.ContactUnitLibraryDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.ManageHcqtDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
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
@Service("recommendContactUnitBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RecommendContactUnitBusinessImpl extends BaseFWBusinessImpl<RecommendContactUnitDAO, ManageHcqtDTO, ManageHcqtBO>
		implements ManageHcqtBusiness {

	static Logger LOGGER = LoggerFactory.getLogger(RecommendContactUnitBusinessImpl.class);

	@Autowired
	private RecommendContactUnitDAO recommendContactUnitDAO;
	
	@Autowired
	private ConstructionTaskDAO constructionTaskDAO;
	
	@Autowired
	private UtilAttachDocumentDAO utilAttachDocumentDAO;
	

	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	HashMap<Integer, String> colName = new HashMap();
	{
		colName.put(1, "Mã tỉnh");
		colName.put(2, "Tên đơn vị hoặc chủ đầu tư đến tiếp xúc ");
		colName.put(3, "Địa chỉ của đơn vị hoặc chủ đầu tư đến tiếp xúc");
		colName.put(4, "Lĩnh vực hoạt động của đơn vị hoặc chủ đầu tư đến tiếp xúc");
		colName.put(5, "Chủ đầu tư nhận được Công văn");
		colName.put(6, "Hạn hoàn thành tiếp xúc");
	}
	HashMap<Integer, String> colName1 = new HashMap();
	{
		colName1.put(1, "Ký hiệu lĩnh vực của đơn vị đến tiếp xúc");
		colName1.put(2, "Khu vực");
		colName1.put(3, "Mã tỉnh");
		colName1.put(4, "Tên đơn vị hoặc chủ đầu tư đến tiếp xúc ");
		colName1.put(5, "Địa chỉ của đơn vị hoặc chủ đầu tư đến tiếp xúc");
		colName1.put(6, "Lĩnh vực hoạt động của đơn vị hoặc chủ đầu tư đến tiếp xúc");
		colName1.put(7, "Chủ đầu tư nhận được Công văn");
		colName1.put(8, "Hạn hoàn thành tiếp xúc");
		colName1.put(9, "Ngày đến tiếp xúc");
		colName1.put(10, "Họ và tên");
		colName1.put(11, "Chức vụ");
		colName1.put(12, "Số điện thoại");
		colName1.put(13, "Mail");
		colName1.put(14, "Kết quả tiếp xúc");
		colName1.put(15, "Diễn giải tóm tắt nội dung");
		colName1.put(16, "Họ và tên");
		colName1.put(17, "Số điện thoại");
		colName1.put(18, "Mail");
		colName1.put(19, "Thuộc loại");
		colName1.put(20, "Ghi chú");
	}

	HashMap<Integer, String> colAlias = new HashMap();
	{
		colAlias.put(1, "B");
		colAlias.put(2, "C");
		colAlias.put(3, "D");
		colAlias.put(4, "E");
		colAlias.put(5, "F");
		colAlias.put(6, "G");
		colAlias.put(7, "H");
		colAlias.put(8, "I");
		colAlias.put(9, "J");
		colAlias.put(10, "K");
		colAlias.put(11, "L");
		colAlias.put(12, "M");
		colAlias.put(13, "N");
		colAlias.put(14, "O");
		colAlias.put(15, "P");
		colAlias.put(16, "Q");
		colAlias.put(17, "R");
		colAlias.put(18, "S");
		colAlias.put(19, "T");
		colAlias.put(20, "U");
		colAlias.put(21, "V");
		colAlias.put(22, "W");
	}
	
	HashMap<Integer, String> colNameDTOS = new HashMap();
	{
		colNameDTOS.put(1, "Ngày hoàn thành");
		colNameDTOS.put(2, "Đơn vị thực hiện");
		colNameDTOS.put(3, "Mã tỉnh");
		colNameDTOS.put(4, "Mã nhà trạm");
		colNameDTOS.put(5, "Mã trạm");
		colNameDTOS.put(6, "Mã công trình");
		colNameDTOS.put(7, "Hợp đồng");
		colNameDTOS.put(8, "HSHC kế hoạch");
		colNameDTOS.put(9, "HSHC phê duyệt");
		colNameDTOS.put(10, "Hạng mục thực hiện");
		colNameDTOS.put(11, "Phê duyệt/Từ chối");
		colNameDTOS.put(12, "Lý do từ chối");
		colNameDTOS.put(13, "Ngày thi công xong");
	}

	

	public RecommendContactUnitBusinessImpl() {
		tModel = new ManageHcqtBO();
		tDAO = recommendContactUnitDAO;
	}

	@Override
	public RecommendContactUnitDAO gettDAO() {
		return recommendContactUnitDAO;
	}
	
	// tatph-start-12/12/2019
	public DataListDTO doSearch(ContactUnitDTO obj, HttpServletRequest request) {
		List<ContactUnitDTO> ls = new ArrayList<ContactUnitDTO>();
		obj.setTotalRecord(0);
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = recommendContactUnitDAO.doSearch(obj, groupIdList);
		ls.forEach( dto ->{
			recommendContactUnitDAO.countDetail(dto);
			dto.setCountDetail(dto.getTotalRecord());
		});
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public DataListDTO doSearchDetail(ContactUnitDetailDTO obj, HttpServletRequest request) {
		List<ContactUnitDetailDTO> ls = new ArrayList<ContactUnitDetailDTO>();
		obj.setTotalRecord(0);
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = recommendContactUnitDAO.doSearchDetail(obj, groupIdList);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public void updateDescription(ContactUnitDetailDTO obj) {
		recommendContactUnitDAO.updateDescription(obj);
	}
	
	public DataListDTO doSearchDetailById(ContactUnitDetailDTO obj, HttpServletRequest request) {
		List<ContactUnitDetailDTO> ls = new ArrayList<ContactUnitDetailDTO>();
		obj.setTotalRecord(0);
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = recommendContactUnitDAO.doSearchDetailById(obj, groupIdList);
		ls.forEach(dto ->{
			List<UtilAttachDocumentDTO> fileLst = recommendContactUnitDAO.getListAttachmentByIdAndType(dto.getContactUnitDetailId(), TYPE);
			if(fileLst != null && fileLst.size() > 0) {
				dto.setUtilAttachDocumentDTOs(fileLst);
			}
			
			List<ContactUnitDetailDescriptionDTO> contactUnitDetailDescriptionDTOs = recommendContactUnitDAO.getListDescription(dto);
			if(contactUnitDetailDescriptionDTOs != null && contactUnitDetailDescriptionDTOs.size() > 0) {
				dto.setContactUnitDetailDescriptionDTOs(contactUnitDetailDescriptionDTOs);
			}
		});
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public DataListDTO doSearchContactUnitLibrary(ContactUnitDetailDTO obj, HttpServletRequest request) {
		List<ContactUnitLibraryDTO> ls = new ArrayList<ContactUnitLibraryDTO>();
		obj.setTotalRecord(0);
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = recommendContactUnitDAO.doSearchContactUnitLibrary(obj, groupIdList);
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

	public String getExcelTemplate(ContactUnitDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Bieu_mau_ds_goi_y_tiep_xuc.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Bieu_mau_ds_goi_y_tiep_xuc.xlsx");
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Bieu_mau_ds_goi_y_tiep_xuc.xlsx");
		return path;
	}
	
	public String getExcelTemplateManageContactUnit(ContactUnitDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Bieu_mau_quan_ly_ds_goi_y_tiep_xuc.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Bieu_mau_quan_ly_ds_goi_y_tiep_xuc.xlsx");
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Bieu_mau_quan_ly_ds_goi_y_tiep_xuc.xlsx");
		return path;
	}
	
	public String getExcelTemplateConstructionDTOS(ConstructionTaskDetailDTO obj,HttpServletRequest request) throws Exception {
		
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BieuMau_Doanh_thu_ngoai_OS.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "BieuMau_Doanh_thu_ngoai_OS.xlsx");
		// sheet 1 
		obj.setPage(null);
		obj.setPageSize(null);
		obj.setListAppRevenueState(Collections.singletonList("1"));
		List<ConstructionTaskDetailDTO> ls = new ArrayList<ConstructionTaskDetailDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = constructionTaskDAO.doSearchForRevenue(obj, groupIdList);
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (ls != null && !ls.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			for (ConstructionTaskDetailDTO dto : ls) {
				
				
				//
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(style);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getSysGroupName() != null ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getSysGroupName() != null ? dto.getCatStationCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getCatStationCode() != null ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getConstructionCode() != null ? dto.getCntContract() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(dto.getCompleteValue() != null ? dto.getCompleteValue() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(dto.getConsAppRevenueValue() != null ? dto.getConsAppRevenueValue() : 0D);
				cell.setCellStyle(style);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style); 
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				//
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				//
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
			

			}
		}

		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "BieuMau_Doanh_thu_ngoai_OS.xlsx");
		return path;
	}
	
	public ContactUnitDetailDTO getResult (ContactUnitDetailDTO contactUnitDetailDTO) {
		if(contactUnitDetailDTO.getResult() == 1L) {
			contactUnitDetailDTO.setNeedHireStationBts(contactUnitDetailDTO.getShortContent());
			return contactUnitDetailDTO;
		}else if(contactUnitDetailDTO.getResult() == 2L) {
			contactUnitDetailDTO.setNeedHireTransmission(contactUnitDetailDTO.getShortContent());
			return contactUnitDetailDTO;
		}else if(contactUnitDetailDTO.getResult() == 3L) {
			contactUnitDetailDTO.setNeedDoDasCdbr(contactUnitDetailDTO.getShortContent());
			return contactUnitDetailDTO;
		}else if(contactUnitDetailDTO.getResult() == 4L) {
			contactUnitDetailDTO.setNeedDoSunEnergy(contactUnitDetailDTO.getShortContent());
			return contactUnitDetailDTO;
		}else if(contactUnitDetailDTO.getResult() == 5L) {
			contactUnitDetailDTO.setNeedOther(contactUnitDetailDTO.getShortContent());
			return contactUnitDetailDTO;
		}else if(contactUnitDetailDTO.getResult() == 6L) {
			contactUnitDetailDTO.setCustomerAddress(contactUnitDetailDTO.getShortContent());
			return contactUnitDetailDTO;
		}else if(contactUnitDetailDTO.getResult() == 7L) {
			contactUnitDetailDTO.setNoNeed(contactUnitDetailDTO.getShortContent());
			return contactUnitDetailDTO;
		}else if(contactUnitDetailDTO.getResult() == 8L) {
			contactUnitDetailDTO.setDuringDisscus(contactUnitDetailDTO.getShortContent());
			return contactUnitDetailDTO;
		}else if(contactUnitDetailDTO.getResult() == 9L) {
			contactUnitDetailDTO.setSignContract(contactUnitDetailDTO.getShortContent());
			return contactUnitDetailDTO;
		}else {
			return contactUnitDetailDTO;
		}
	}
	
	public String exportListContact(ContactUnitDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Danh_sach_goi_y_tiep_xuc.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Danh_sach_goi_y_tiep_xuc.xlsx");
		// sheet 1 
		List<ContactUnitDetailDTO> contactUnitDetailDTOs = recommendContactUnitDAO.exportListContact(obj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (contactUnitDetailDTOs != null && !contactUnitDetailDTOs.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 4;
			for (ContactUnitDetailDTO dto2 : contactUnitDetailDTOs) {
				ContactUnitDetailDTO dto ;
				if(dto2.getResult() != null) {
					dto= this.getResult(dto2);
				}else {
					dto = dto2;
				}
				
				//
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 4));
				cell.setCellStyle(style);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getUnitCode() != null ? dto.getUnitCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getAreaCode() != null ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getProvinceCode() != null ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getUnitName() != null ? dto.getUnitName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getUnitAddress() != null ? dto.getUnitAddress() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getUnitField() != null ? dto.getUnitField() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getUnitBoss() != null ? dto.getUnitBoss() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(dto.getDeadlineDateCompleteS() != null ? dto.getDeadlineDateCompleteS() : null);
				cell.setCellStyle(style);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getTypeS() != null) ? dto.getTypeS() : null);
				cell.setCellStyle(style);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(dto.getContactDateS() != null ? dto.getContactDateS() : null);
				cell.setCellStyle(style);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue(dto.getFullNameCus() != null ? dto.getFullNameCus() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue(dto.getPositionCus() != null ? dto.getPositionCus() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue(dto.getPhoneNumberCus() != null ? dto.getPhoneNumberCus() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue(dto.getMailCus() != null ? dto.getMailCus() : "");
				cell.setCellStyle(style);
				//------------------------------------------------------------------------------------------------
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue(dto.getNeedHireStationBts() != null ? dto.getNeedHireStationBts() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue(dto.getNeedHireTransmission() != null ? dto.getNeedHireTransmission() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue(dto.getNeedDoDasCdbr() != null ? dto.getNeedDoDasCdbr() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue(dto.getNeedDoSunEnergy() != null ? dto.getNeedDoSunEnergy() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue(dto.getNeedOther() != null ? dto.getNeedOther() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue(dto.getCustomerAddress() != null ? dto.getCustomerAddress() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue(dto.getNoNeed() != null ? dto.getNoNeed() : "");
				cell.setCellStyle(style);
				/*//
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue(dto.getShortContent() != null ? dto.getShortContent() : "");
				cell.setCellStyle(style);*/
				//
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue(dto.getDuringDisscus() != null ? dto.getDuringDisscus() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue(dto.getSignContract() != null ? dto.getSignContract() : "");
				cell.setCellStyle(style);
				
				//--------------------------------------------------------------------------------------------------
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue(dto.getFullNameEmploy() != null ? dto.getFullNameEmploy() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue(dto.getPhoneNumberEmploy() != null ? dto.getPhoneNumberEmploy() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(26, CellType.STRING);
				cell.setCellValue(dto.getMailEmploy() != null ? dto.getMailEmploy() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(27, CellType.STRING);
				cell.setCellValue(dto.getDescription() != null ? dto.getDescription() : "");
				cell.setCellStyle(style);

			}
		}

		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Danh_sach_goi_y_tiep_xuc.xlsx");
		return path;
	}

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
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())) {
				return false;
			}
		}
		return true;
	}

	public boolean validateString(String str) {
		return (str != null && str.length() > 0);
	}

	private ExcelErrorDTO createError(int row, String column, String detail) {
		ExcelErrorDTO err = new ExcelErrorDTO();
		err.setColumnError(column);
		err.setLineError(String.valueOf(row));
		err.setDetailError(detail);
		return err;
	}
	
	public void saveContactUnit(ContactUnitDTO contactUnitDTO) {
		Long id = recommendContactUnitDAO.saveObject(contactUnitDTO.toModel());
		contactUnitDTO.getContactUnitLibraryDTO().setContactUnitId(id);
		this.saveContactUnitLibrary(contactUnitDTO.getContactUnitLibraryDTO());
	}
	
	public void saveContactUnitDetail(ContactUnitDetailDTO contactUnitDTO) {
		Long id = recommendContactUnitDAO.saveContactUnitDetail(contactUnitDTO);
		contactUnitDTO.setContactUnitDetailId(id);
		recommendContactUnitDAO.updateContactUnit(contactUnitDTO.getType(), contactUnitDTO.getContactUnitId());
		if(contactUnitDTO.getUtilAttachDocumentDTOs() != null && contactUnitDTO.getUtilAttachDocumentDTOs().size() > 0) {
			this.saveAttachment(contactUnitDTO);
		}
		if(contactUnitDTO.getDescription() != null && !"".equals(contactUnitDTO.getDescription())) {
			ContactUnitDetailDescriptionDTO contactUnitDetailDescriptionDTO = new ContactUnitDetailDescriptionDTO();
			contactUnitDetailDescriptionDTO.setContactUnitDetailId(id);
			contactUnitDetailDescriptionDTO.setCreateDate(new Date());
			contactUnitDetailDescriptionDTO.setDescription(contactUnitDTO.getUserLogin() + " : " +contactUnitDTO.getDescription());
			contactUnitDetailDescriptionDTO.setCreateUserId(contactUnitDTO.getUserLoginId());
			contactUnitDetailDescriptionDTO.setIsView(0L);
			recommendContactUnitDAO.saveContactUnitDetailDescription(contactUnitDetailDescriptionDTO);
		}
		
		this.updateContactUnitLibrary(contactUnitDTO.getContactUnitLibraryDTO());
		
	}
	
	public void updateContactUnitDetail(ContactUnitDetailDTO contactUnitDTO) {
		recommendContactUnitDAO.updateContactUnitDetail(contactUnitDTO);
		recommendContactUnitDAO.updateContactUnit(contactUnitDTO.getType(), contactUnitDTO.getContactUnitId());
		if(contactUnitDTO.getUtilAttachDocumentDTOs() != null && contactUnitDTO.getUtilAttachDocumentDTOs().size() > 0) {
			this.saveAttachment(contactUnitDTO);
		}
		if(contactUnitDTO.getDescription() != null && !"".equals(contactUnitDTO.getDescription())) {
			ContactUnitDetailDescriptionDTO contactUnitDetailDescriptionDTO = new ContactUnitDetailDescriptionDTO();
			contactUnitDetailDescriptionDTO.setContactUnitDetailId(contactUnitDTO.getContactUnitDetailId());
			contactUnitDetailDescriptionDTO.setCreateDate(new Date());
			contactUnitDetailDescriptionDTO.setDescription(contactUnitDTO.getUserLogin() + " : " +contactUnitDTO.getDescription());
			contactUnitDetailDescriptionDTO.setCreateUserId(contactUnitDTO.getUserLoginId());
			contactUnitDetailDescriptionDTO.setIsView(0L);
			recommendContactUnitDAO.saveContactUnitDetailDescription(contactUnitDetailDescriptionDTO);
		}
		
		this.updateContactUnitLibrary(contactUnitDTO.getContactUnitLibraryDTO());
		
	}
	final String TYPE = "CONTACT_UNIT_FILE_TYPE";
	public void saveAttachment(ContactUnitDetailDTO contactUnitDTO) {
		utilAttachDocumentDAO.deleteUtils(contactUnitDTO, TYPE);
		contactUnitDTO.getUtilAttachDocumentDTOs().forEach( dto ->{
			dto.setType(TYPE);
			dto.setObjectId(contactUnitDTO.getContactUnitDetailId());
			dto.setDescription(" File đính kèm . ");
			utilAttachDocumentDAO.saveUtilAttach(dto);
		});
		
	}
	//HienLT56 start 06072020
	public static boolean contains(String[] arr, String item) {
		for (String n : arr) {
			if (item.trim().toUpperCase().equals(n.trim().toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	String[] areaCodeLst = {"KV1","KV2","KV3"};
	//HienLT56 end 06072020

	public List<ContactUnitDTO> importRecommendContactUnit(String fileInput) {
		List<ContactUnitDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		List<String> provinceLst = recommendContactUnitDAO.getProvinceLst(); //HienLT56 add 06072020
		List<String> provinceKV1Lst = recommendContactUnitDAO.getProvinceKV1Lst();//HienLT56 add 16072020
		List<String> provinceKV2Lst = recommendContactUnitDAO.getProvinceKV2Lst();//HienLT56 add 16072020
		List<String> provinceKV3Lst = recommendContactUnitDAO.getProvinceKV3Lst();//HienLT56 add 16072020
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			for (Row row : sheet) {
				count++;
				//HienLT56 start 02072020
				if (count >= 4 && checkIfRowIsEmpty(row))
					continue;
				if (count >= 4) {
					String unitCode = formatter.formatCellValue(row.getCell(1));
					String areaCode = formatter.formatCellValue(row.getCell(2));
					//HienLT56 end 02072020
					String provinceCode = formatter.formatCellValue(row.getCell(3));
					String unitName = formatter.formatCellValue(row.getCell(4));
					String unitAddress = formatter.formatCellValue(row.getCell(5));
					String unitField = formatter.formatCellValue(row.getCell(6));
					String unitBoss = formatter.formatCellValue(row.getCell(7));
					String deadlineDateComplete = formatter.formatCellValue(row.getCell(8));
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String validateDate = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
					ContactUnitDTO obj = new ContactUnitDTO();
					ContactUnitLibraryDTO obj2 = new ContactUnitLibraryDTO();
					if(validateString(unitCode)) {
						obj.setUnitCode(unitCode);
					}else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
								colName1.get(1) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if(validateString(areaCode)) {
						if(!contains(areaCodeLst, areaCode.trim().toUpperCase())) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum()+1, colAlias.get(2), colName1.get(2) + " không tồn tại");
							errorList.add(errorDTO);
						}
						obj.setAreaCode(areaCode.trim().toUpperCase());
					}else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
								colName1.get(2) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					if (validateString(provinceCode)) {
						if(!provinceLst.contains(provinceCode.trim().toUpperCase())) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum()+1, colAlias.get(3), colName1.get(3) + " không tồn tại");
							errorList.add(errorDTO);
						} else {
							if(validateString(areaCode)) {
								if((areaCode.trim().toUpperCase().equals("KV1") && provinceKV1Lst.contains(provinceCode.trim().toUpperCase())) || (areaCode.trim().toUpperCase().equals("KV2") && provinceKV2Lst.contains(provinceCode.trim().toUpperCase())) || (areaCode.trim().toUpperCase().equals("KV3") && provinceKV3Lst.contains(provinceCode.trim().toUpperCase()))){
									obj.setProvinceCode(provinceCode.trim().toUpperCase());
									String provinceName = recommendContactUnitDAO.getProvinceNameForImport(provinceCode.trim().toUpperCase()); //HienLT56 add 15072020
									obj.setProvinceName(provinceName);
								} else {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
											"Không tồn tại tỉnh " + provinceCode + " ở " + areaCode);
									errorList.add(errorDTO);
								}
							}
						}
						
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
								colName1.get(3) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(unitName)) {
					obj.setUnitName(unitName);
					obj2.setUnitName(unitName);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
								colName1.get(4) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(unitAddress)) {
						obj.setUnitAddress(unitAddress);
						obj2.setUnitAddress(unitAddress);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
								colName1.get(5) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(unitField)) {
						obj.setUnitField(unitField);
						obj2.setUnitField(unitField);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
								colName1.get(6) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(unitBoss)) {
						obj.setUnitBoss(unitBoss);
						obj2.setUnitBoss(unitBoss);
					}
//					else {
//						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
//								colName1.get(7) + " bị bỏ trống");
//						errorList.add(errorDTO);
//					}
					
					if (validateString(deadlineDateComplete)) {
						if(deadlineDateComplete.matches(validateDate)) {
							Date date = sdf.parse(deadlineDateComplete);
							obj.setDeadlineDateComplete(date);
						} else{
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
									colName1.get(8) + " không hợp lệ");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
								colName1.get(8) + " bị bỏ trống");
						errorList.add(errorDTO);
					}

					if (errorList.size() == 0) {
						obj.setContactUnitLibraryDTO(obj2);
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ContactUnitDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ContactUnitDTO errorContainer = new ContactUnitDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(9); // cột dùng để in ra lỗi
				errorContainer.setFilePathError(filePathError);
				workLst.add(errorContainer);
			}
			workbook.close();
			return workLst;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorList.add(errorDTO);
			String filePathError = null;
			try {
				filePathError = UEncrypt.encryptFileUploadPath(fileInput);
			} catch (Exception ex) {
				LOGGER.error(e.getMessage(), e);
				errorDTO = createError(0, "", ex.toString());
				errorList.add(errorDTO);
			}
			List<ContactUnitDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ContactUnitDTO errorContainer = new ContactUnitDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(9); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}
	
	public List<ContactUnitLibraryDTO> importRecommendContactUnitLibrary(String fileInput) {
		List<ContactUnitLibraryDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();

		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			for (Row row : sheet) {
				count++;
				if (count >= 5 && checkIfRowIsEmpty(row))
					continue;
				if (count >= 5) {
					String provinceCode = formatter.formatCellValue(row.getCell(1));
					String unitName = formatter.formatCellValue(row.getCell(2));
					String unitAddress = formatter.formatCellValue(row.getCell(3));
					String unitField = formatter.formatCellValue(row.getCell(4));
					String unitBoss = formatter.formatCellValue(row.getCell(5));
					String deadlineDateComplete = formatter.formatCellValue(row.getCell(6));
					
					String contactDate = formatter.formatCellValue(row.getCell(7));
					String fullNameCus = formatter.formatCellValue(row.getCell(8));
					String positionCus  = formatter.formatCellValue(row.getCell(9));
					String phoneNumberCus  = formatter.formatCellValue(row.getCell(10));
					String mailCus  = formatter.formatCellValue(row.getCell(11));
					
					String result  = formatter.formatCellValue(row.getCell(12));
					String shortContent  = formatter.formatCellValue(row.getCell(13));
					
					String fullNameEmploy  = formatter.formatCellValue(row.getCell(14));
					String phoneNumberEmploy  = formatter.formatCellValue(row.getCell(15));
					String mailEmploy  = formatter.formatCellValue(row.getCell(16));
					String type  = formatter.formatCellValue(row.getCell(17));
					String description  = formatter.formatCellValue(row.getCell(18));
					
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					ContactUnitLibraryDTO obj = new ContactUnitLibraryDTO();
					obj.setProvinceCode(provinceCode);
					obj.setProvinceName(provinceCode);
					obj.setUnitName(unitName);
					obj.setUnitAddress(unitAddress);
					obj.setUnitField(unitField);
					obj.setUnitBoss(unitBoss);
					obj.setDeadlineDateComplete(sdf.parse(deadlineDateComplete));
					obj.setContactDate(sdf.parse(contactDate));
					obj.setFullNameCus(fullNameCus);
					obj.setPositionCus(positionCus);
					obj.setPhoneNumberCus(phoneNumberCus);
					obj.setMailCus(mailCus);
				
					obj.setShortContent(shortContent);
					obj.setFullNameEmploy(fullNameEmploy);
					obj.setPhoneNumberEmploy(phoneNumberEmploy);
					obj.setMailEmploy(mailEmploy);
				
					obj.setDescription(description);
					
					
					try {
						obj.setResult(Long.parseLong(result));
					}catch (Exception e) {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "Cột M",
								"Kết quả nhập không đúng định dạng , nhập số từ 1 -> 9");
						errorList.add(errorDTO);
					}
					
					try {
						obj.setType(Long.parseLong(type));
					}catch (Exception e) {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "Cột R",
								"Loại nhập không đúng định dạng , nhập số từ 1 -> 4");
						errorList.add(errorDTO);
					}
					
					
					
					
					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ContactUnitLibraryDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ContactUnitLibraryDTO errorContainer = new ContactUnitLibraryDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(19); // cột dùng để in ra lỗi
				errorContainer.setFilePathError(filePathError);
				workLst.add(errorContainer);
			}
			workbook.close();
			return workLst;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorList.add(errorDTO);
			String filePathError = null;
			try {
				filePathError = UEncrypt.encryptFileUploadPath(fileInput);
			} catch (Exception ex) {
				LOGGER.error(e.getMessage(), e);
				errorDTO = createError(0, "", ex.toString());
				errorList.add(errorDTO);
			}
			List<ContactUnitLibraryDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ContactUnitLibraryDTO errorContainer = new ContactUnitLibraryDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(19); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}
	
	public List<ConstructionTaskDetailDTO> importConstructionDTOS(String fileInput,HttpServletRequest request) {
		List<ConstructionTaskDetailDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();

		ConstructionTaskDetailDTO obj2 = new ConstructionTaskDetailDTO();
		obj2.setPage(null);
		obj2.setPageSize(null);
		obj2.setType("1");
		List<ConstructionTaskDetailDTO> ls = new ArrayList<ConstructionTaskDetailDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = constructionTaskDAO.doSearchForRevenue(obj2, groupIdList);
		Map<String,ConstructionTaskDetailDTO> mapCons = new HashMap<>();
		ls.forEach( dto ->  {
			mapCons.put(dto.getConstructionCode(),dto);
		});
		
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
				if (count >= 3) {
					String completeDate = formatter.formatCellValue(row.getCell(1));
					String sysGroupName = formatter.formatCellValue(row.getCell(2));
					String provinceCode = formatter.formatCellValue(row.getCell(3));
					String catStationHouseCode = formatter.formatCellValue(row.getCell(4));
					String catStationCode = formatter.formatCellValue(row.getCell(5));
					String constructionCode = formatter.formatCellValue(row.getCell(6));
					String cntContractCode = formatter.formatCellValue(row.getCell(7));
					String hshcPlan = formatter.formatCellValue(row.getCell(8));
					String hshcApprove = formatter.formatCellValue(row.getCell(9));
					String category = formatter.formatCellValue(row.getCell(10));
					String action = formatter.formatCellValue(row.getCell(11));
					String reasonReject = formatter.formatCellValue(row.getCell(12));
					String doneDate = formatter.formatCellValue(row.getCell(13));
					
					
					
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					ConstructionTaskDetailDTO obj = new ConstructionTaskDetailDTO();
					obj.setSysGroupName(sysGroupName);
					obj.setProvinceCode(provinceCode);
					obj.setCatStationHouseCode(catStationHouseCode);
					obj.setCatStationCode(catStationCode);
					obj.setCntContract(cntContractCode);
					obj.setApproceCompleteDescription(reasonReject);
					//
					try {
						Double.parseDouble(hshcPlan);
						obj.setCompleteValue(hshcPlan);
						}catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
									colNameDTOS.get(8) + " chỉ được nhập số");
							errorList.add(errorDTO);
						}
					//
					try {
					obj.setConsAppRevenueValue(Double.parseDouble(hshcApprove));
					}catch (Exception e) {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
								colNameDTOS.get(9) + " chỉ được nhập số");
						errorList.add(errorDTO);
					}
					
					if(Double.parseDouble(hshcApprove) != 0D) {
						obj.setValueApproveDTOS(Double.parseDouble(hshcApprove));
					}else {
						obj.setValueApproveDTOS(Double.parseDouble(hshcPlan));
					}
					//
					if (validateString(completeDate)) {
						String[] dateBreaking = completeDate.split("/");
						if(dateBreaking.length<3) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
									colNameDTOS.get(1) + " không đúng định dạng");
							errorList.add(errorDTO);
						} else {
							if(validateDate(completeDate)) {
								obj.setDateComplete(completeDate);
							} else {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
										colNameDTOS.get(1) + " không đúng định dạng");
								errorList.add(errorDTO);
							}
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
								colNameDTOS.get(1) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(constructionCode)) {
						ConstructionTaskDetailDTO constructionTaskDetailDTO = mapCons.get(constructionCode);
						if(constructionTaskDetailDTO == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
									colNameDTOS.get(6) + " không có trong quản lý doanh thu ngoài OS");
							errorList.add(errorDTO);
						}else {
							obj.setConstructionCode(constructionCode);
							obj.setConstructionId(constructionTaskDetailDTO.getConstructionId());
						}
							
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
								colNameDTOS.get(6) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					//
					if (validateString(action)) {
						if("2".equals(action)) {
						obj.setAction("2");
						}else if("3".equals(action)) {
							obj.setAction("3");
						}else{
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(11),
									colNameDTOS.get(11) + " chỉ nhập 2 hoặc 3");
							errorList.add(errorDTO);
						}
							
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(11),
								colNameDTOS.get(11) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					//
					if (validateString(doneDate)) {
					try {
						Date date = sdf.parse(doneDate);
						obj.setDoneDate(date);
					} catch (Exception e) {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(13),
								colNameDTOS.get(13) + " không hợp lệ");
						errorList.add(errorDTO);
					}
							
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(13),
								colNameDTOS.get(13) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					

					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ConstructionTaskDetailDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ConstructionTaskDetailDTO errorContainer = new ConstructionTaskDetailDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(14); // cột dùng để in ra lỗi
				errorContainer.setFilePathError(filePathError);
				workLst.add(errorContainer);
			}
			workbook.close();
			return workLst;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorList.add(errorDTO);
			String filePathError = null;
			try {
				filePathError = UEncrypt.encryptFileUploadPath(fileInput);
			} catch (Exception ex) {
				LOGGER.error(e.getMessage(), e);
				errorDTO = createError(0, "", ex.toString());
				errorList.add(errorDTO);
			}
			List<ConstructionTaskDetailDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ConstructionTaskDetailDTO errorContainer = new ConstructionTaskDetailDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(10); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}
	public void updateConstruction(ConstructionTaskDetailDTO constructionTaskDetailDTO, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute(USER_SESSION_KEY);
		recommendContactUnitDAO.updateConstruction(constructionTaskDetailDTO, user.getVpsUserInfo().getSysUserId());
	}
	public void updateRevenue(ConstructionTaskDetailDTO constructionTaskDetailDTO) {
		Double completeValue = Double.parseDouble(constructionTaskDetailDTO.getCompleteValue()) * 1000000;
		constructionTaskDetailDTO.setCompleteValue(completeValue.toString());
		constructionTaskDetailDTO.setValueApproveDTOS(constructionTaskDetailDTO.getValueApproveDTOS() * 1000000);
		recommendContactUnitDAO.updateRpRevenue(constructionTaskDetailDTO);
	}
	
	public void updateContactUnitLibrary(ContactUnitLibraryDTO contactUnitDetailDTO) {
		recommendContactUnitDAO.updateContactUnitLibrary(contactUnitDetailDTO);
	}
	public void saveContactUnitLibrary(ContactUnitLibraryDTO constructionTaskDetailDTO) {
		recommendContactUnitDAO.saveContactUnitLibrary(constructionTaskDetailDTO);
	}

	public Integer getMaxId() {
		Integer id =  recommendContactUnitDAO.getMaxId();
		return id;
	}
	
	boolean validateDate(String date){
    	String dateBreaking[] = date.split("/");
    	if(Integer.parseInt(dateBreaking[1])>12){
    		return false;
    	}
    	if( Integer.parseInt(dateBreaking[2]) < (new Date()).getYear()+1900 ){
    		return false;
    	}
    	if(Integer.parseInt(dateBreaking[0])>31){
    		return false;
    	}
    	SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
	    sdfrmt.setLenient(false);
	    try
	    {
	        Date javaDate = sdfrmt.parse(date); 
	    }
	    catch (Exception e)
	    {
	        return false;
	    }
    	return true;
    }

	//HienLT56 start 01072020
	public List<ContactUnitDTO> getForAutoCompleteProvince(ContactUnitDTO obj) {
		return recommendContactUnitDAO.getForAutoCompleteProvince(obj);
	}
	//HienLT56 end 01072020

	//HienLT56 start 02072020
	public Long saveAddFormContact(ContactUnitDTO obj) {
		return recommendContactUnitDAO.saveObject(obj.toModel());
	}

	public List<ContactUnitDTO> importRecommendContactUnitAll(String fileInput) {

		List<ContactUnitDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		List<String> provinceLst = recommendContactUnitDAO.getProvinceLst();
		List<String> provinceKV1Lst = recommendContactUnitDAO.getProvinceKV1Lst();
		List<String> provinceKV2Lst = recommendContactUnitDAO.getProvinceKV2Lst();
		List<String> provinceKV3Lst = recommendContactUnitDAO.getProvinceKV3Lst();

		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			for (Row row : sheet) {
				count++;
				
				if (count >= 5 && checkIfRowIsEmpty(row))
					continue;
				if (count >= 5) {
					String unitCode = formatter.formatCellValue(row.getCell(1));
					String areaCode = formatter.formatCellValue(row.getCell(2));
					
					String provinceCode = formatter.formatCellValue(row.getCell(3));
					String unitName = formatter.formatCellValue(row.getCell(4));
					String unitAddress = formatter.formatCellValue(row.getCell(5));
					String unitField = formatter.formatCellValue(row.getCell(6));
					String unitBoss = formatter.formatCellValue(row.getCell(7));
					String deadlineDateComplete = formatter.formatCellValue(row.getCell(8));
					String contactDate = formatter.formatCellValue(row.getCell(9));
					String fullNameCus = formatter.formatCellValue(row.getCell(10));
					String positionCus = formatter.formatCellValue(row.getCell(11));
					String phoneNumberCus = formatter.formatCellValue(row.getCell(12));
					String mailCus = formatter.formatCellValue(row.getCell(13));
					String resultS = formatter.formatCellValue(row.getCell(14));
					String shortContent = formatter.formatCellValue(row.getCell(15));
					String fullNameEmploy = formatter.formatCellValue(row.getCell(16));
					String phoneNumberEmploy = formatter.formatCellValue(row.getCell(17));
					String mailEmploy = formatter.formatCellValue(row.getCell(18));
					String typeS = formatter.formatCellValue(row.getCell(19));
					String description = formatter.formatCellValue(row.getCell(20));
					String phoneRegex = "^(09|03|07|08|05)([0-9]{8})$";
					String validateDate = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					ContactUnitDTO obj = new ContactUnitDTO();
					ContactUnitDetailDTO obj2 = new ContactUnitDetailDTO();
					if(validateString(unitCode)) {
						obj.setUnitCode(unitCode);
					}else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
								colName1.get(1) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					if(validateString(areaCode)) {
						if(!contains(areaCodeLst, areaCode.trim().toUpperCase())) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum()+1, colAlias.get(2), colName1.get(2) + " không tồn tại");
							errorList.add(errorDTO);
						}
						obj.setAreaCode(areaCode.trim().toUpperCase());
					}else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
								colName1.get(2) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					if (validateString(provinceCode)) {
						if(!provinceLst.contains(provinceCode.trim().toUpperCase())) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum()+1, colAlias.get(3), colName1.get(3) + " không tồn tại");
							errorList.add(errorDTO);
						} else {
							if(validateString(areaCode)) {
								if((areaCode.trim().toUpperCase().equals("KV1") && provinceKV1Lst.contains(provinceCode.trim().toUpperCase())) || (areaCode.trim().toUpperCase().equals("KV2") && provinceKV2Lst.contains(provinceCode.trim().toUpperCase())) || (areaCode.trim().toUpperCase().equals("KV3") && provinceKV3Lst.contains(provinceCode.trim().toUpperCase()))){
									obj.setProvinceCode(provinceCode.trim().toUpperCase());
									String provinceName = recommendContactUnitDAO.getProvinceNameForImport(provinceCode.trim().toUpperCase()); //HienLT56 add 15072020
									obj.setProvinceName(provinceName);
								} else {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
											"Không tồn tại tỉnh " + provinceCode + " ở " + areaCode);
									errorList.add(errorDTO);
								}
							}
						}
						
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
								colName1.get(3) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(unitName)) {
					obj.setUnitName(unitName);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
								colName1.get(4) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(unitAddress)) {
						obj.setUnitAddress(unitAddress);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
								colName1.get(5) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(unitField)) {
						obj.setUnitField(unitField);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
								colName1.get(6) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(unitBoss)) {
						obj.setUnitBoss(unitBoss);
					}
//					else {
//						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
//								colName1.get(7) + " bị bỏ trống");
//						errorList.add(errorDTO);
//					}
					
					if (validateString(deadlineDateComplete)) {
						if(deadlineDateComplete.matches(validateDate)) {
							Date date = sdf.parse(deadlineDateComplete);
							obj.setDeadlineDateComplete(date);
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
									colName1.get(8) + " không hợp lệ");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
								colName1.get(8) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if(validateString(contactDate)) {
						if(contactDate.matches(validateDate)) {
							Date date = sdf.parse(contactDate);
							obj2.setContactDate(date);
						} else{
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9), colName1.get(9) + " không hợp lệ");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9), colName1.get(9) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if(validateString(fullNameCus)) {
						obj2.setFullNameCus(fullNameCus);
					}
					
					if(validateString(positionCus)) {
						obj2.setPositionCus(positionCus);
					}
					
					if(validateString(phoneNumberCus)) {
						if(phoneNumberCus.matches(phoneRegex)) {
							obj2.setPhoneNumberCus(phoneNumberCus);
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(12), colName1.get(12) + " không hợp lệ.");
							errorList.add(errorDTO);
						}
						
					}
					
					if(validateString(mailCus)) {
						obj2.setMailCus(mailCus);
					}
					
					if(validateString(resultS)) {
						if(resultS.toUpperCase().trim().equals("CÓ NHU CẦU THUÊ NHÀ TRẠM + CỘT BTS") || resultS.equals("1")) {
							obj2.setResult(1l);
						} else if(resultS.toUpperCase().trim().equals("CÓ NHU CẦU THUÊ TRUYỀN DẪN") || resultS.equals("2")) {
							obj2.setResult(2l);
						} else if(resultS.toUpperCase().trim().equals("CÓ NHU CẦU TRIỂN KHAI DAS CĐBR") || resultS.equals("3")) {
							obj2.setResult(3l);
						} else if(resultS.toUpperCase().trim().equals("CÓ NHU CẦU TRIỂN KHAI THUÊ NĂNG LƯỢNG MẶT TRỜI") || resultS.equals("4")) {
							obj2.setResult(4l);
						} else if(resultS.toUpperCase().trim().equals("CÓ NHU CẦU KHÁC") || resultS.equals("5")) {
							obj2.setResult(5l);
						} else if(resultS.toUpperCase().trim().equals("ĐỊA CHỈ VỊ TRÍ CỦA ĐỐI TÁC CẦN THUÊ HOẶC DỰ ÁN CỦA ĐỐI TÁC CẦN HỢP TÁC") || resultS.equals("6")) {
							obj2.setResult(6l);
						} else if(resultS.toUpperCase().trim().equals("KHÔNG CÓ NHU CẦU") || resultS.equals("7")) {
							obj2.setResult(7l);
						} else if(resultS.toUpperCase().trim().equals("ĐANG ĐÀM PHÁN THƯƠNG THẢO CHO THUÊ HOẶC ĐẦU TƯ CHO THUÊ") || resultS.equals("8")) {
							obj2.setResult(8l);
						} else if(resultS.toUpperCase().trim().equals("KÝ HỢP ĐỒNG CHO THUÊ HOẶC ĐẦU TƯ CHO THUÊ")|| resultS.equals("9")) {
							obj2.setResult(9l);
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(14), colName1.get(14) + " không hợp lệ.");
							errorList.add(errorDTO);
						}
					}
					if(validateString(shortContent)) {
						obj2.setShortContent(shortContent);
					}
					if(validateString(fullNameEmploy)) {
						obj2.setFullNameEmploy(fullNameEmploy);
					}
					if(validateString(phoneNumberEmploy)) {
						if(phoneNumberEmploy.matches(phoneRegex)) {
							obj2.setPhoneNumberEmploy(phoneNumberEmploy);
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(17), colName1.get(17) + " không hợp lệ.");
							errorList.add(errorDTO);
						}
					}
					
					if(validateString(mailEmploy)) {
						obj2.setMailEmploy(mailEmploy);
					}
					
					if(validateString(typeS)) {
						if(typeS.toUpperCase().trim().equals("TRẠM + CỘT BTS") || typeS.equals("1")) {
							obj.setType(1l);
							obj2.setType(1l);
						} else if(typeS.toUpperCase().trim().equals("TRUYỀN DẪN") || typeS.equals("2")) {
							obj.setType(2l);
							obj2.setType(2l);
						}else if(typeS.toUpperCase().trim().equals("DAS CĐBR") || typeS.equals("3")) {
							obj.setType(3l);
							obj2.setType(3l);
						}else if (typeS.toUpperCase().trim().equals("KHÁC") || typeS.equals("4")) {
							obj.setType(4l);
							obj.setType(4l);
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(19), colName1.get(19) + " không hợp lệ.");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(19), colName1.get(19) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					if(validateString(description)) {
						obj2.setDescription(description);
					}

					if (errorList.size() == 0) {
						obj.setContactUnitDetailDTO(obj2);
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ContactUnitDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ContactUnitDTO errorContainer = new ContactUnitDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(21); // cột dùng để in ra lỗi
				errorContainer.setFilePathError(filePathError);
				workLst.add(errorContainer);
			}
			workbook.close();
			return workLst;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorList.add(errorDTO);
			String filePathError = null;
			try {
				filePathError = UEncrypt.encryptFileUploadPath(fileInput);
			} catch (Exception ex) {
				LOGGER.error(e.getMessage(), e);
				errorDTO = createError(0, "", ex.toString());
				errorList.add(errorDTO);
			}
			List<ContactUnitDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ContactUnitDTO errorContainer = new ContactUnitDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(21); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}

	public void saveContactUnitAll(ContactUnitDTO contactUnitDTO, HttpServletRequest request) {
		Long id = recommendContactUnitDAO.saveObject(contactUnitDTO.toModel());
		contactUnitDTO.getContactUnitDetailDTO().setContactUnitId(id);
		contactUnitDTO.getContactUnitDetailDTO().setUserLoginId(contactUnitDTO.getCreateUserId().toString());
		this.saveContactUnitDetailChange1(contactUnitDTO.getContactUnitDetailDTO(),request);
	}

	public String getExcelTemplateAll(ContactUnitDTO obj) throws Exception {

		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_Bieu_mau_quan_ly_ds_goi_y_tiep_xuc.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Import_Bieu_mau_quan_ly_ds_goi_y_tiep_xuc.xlsx");
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_Bieu_mau_quan_ly_ds_goi_y_tiep_xuc.xlsx");
		return path;
	}

	public void saveContactUnitDetailChange1(ContactUnitDetailDTO contactUnitDTO,HttpServletRequest request) {
		KttsUserSession userObj = (KttsUserSession) request.getSession().getAttribute(USER_SESSION_KEY);
		contactUnitDTO.setUserLogin(userObj.getFullName());
		contactUnitDTO.setUserLoginId(userObj.getEmployeeCode());
		Long id = recommendContactUnitDAO.saveContactUnitDetail(contactUnitDTO);
		contactUnitDTO.setContactUnitDetailId(id);
		recommendContactUnitDAO.updateContactUnit(contactUnitDTO.getType(), contactUnitDTO.getContactUnitId());
		if(contactUnitDTO.getUtilAttachDocumentDTOs() != null && contactUnitDTO.getUtilAttachDocumentDTOs().size() > 0) {
			this.saveAttachment(contactUnitDTO);
		}
		if(contactUnitDTO.getDescription() != null && !"".equals(contactUnitDTO.getDescription())) {
			ContactUnitDetailDescriptionDTO contactUnitDetailDescriptionDTO = new ContactUnitDetailDescriptionDTO();
			contactUnitDetailDescriptionDTO.setContactUnitDetailId(id);
			contactUnitDetailDescriptionDTO.setCreateDate(new Date());
			contactUnitDetailDescriptionDTO.setDescription(contactUnitDTO.getUserLogin() + " : " +contactUnitDTO.getDescription());
			contactUnitDetailDescriptionDTO.setCreateUserId(contactUnitDTO.getUserLoginId());
			contactUnitDetailDescriptionDTO.setIsView(0L);
			recommendContactUnitDAO.saveContactUnitDetailDescription(contactUnitDetailDescriptionDTO);
		}
		
		
	}
	
	
//	public ContactUnitDTO findByCode(ContactUnitDTO obj) {
//		return recommendContactUnitDAO.findByCode(obj);
//	}
	
	//HienLT56 end 02072020
}
