package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.viettel.coms.bo.WorkItemBO;
import com.viettel.coms.dao.RpQuantityDAO;
import com.viettel.coms.dto.RpConstructionDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.coms.dto.couponExportDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.coms.utils.PermissionUtils;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;

@Service("rpQuantityBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RpQuantityBusinessImpl extends BaseFWBusinessImpl<RpQuantityDAO, WorkItemDTO, WorkItemBO> implements RpQuantityBusiness{
	
	@Autowired
	private RpQuantityDAO rpQuantityDAO;
	
	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	
	public RpQuantityBusinessImpl() {
		tModel = new WorkItemBO();
		tDAO = rpQuantityDAO;
	}

	@Override
	public RpQuantityDAO gettDAO() {
		return rpQuantityDAO;
	}
	
	public DataListDTO doSearchQuantity(WorkItemDetailDTO obj, HttpServletRequest request) {
		List<WorkItemDetailDTO> ls = new ArrayList<WorkItemDetailDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty()) {
			ls = rpQuantityDAO.doSearchQuantity(obj, groupIdList);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
//	hungtd_20181217_start
	public DataListDTO doSearch(RpConstructionDTO obj, HttpServletRequest request) {
		List<RpConstructionDTO> ls = new ArrayList<RpConstructionDTO>();
		ls = rpQuantityDAO.doSearch(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	//NHAN_BGMB
	public DataListDTO doSearchNHAN(RpConstructionDTO obj, HttpServletRequest request) {
		List<RpConstructionDTO> ls = new ArrayList<RpConstructionDTO>();
		ls = rpQuantityDAO.doSearchNHAN(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	//KC
	public DataListDTO doSearchKC(RpConstructionDTO obj, HttpServletRequest request) {
		List<RpConstructionDTO> ls = new ArrayList<RpConstructionDTO>();
		ls = rpQuantityDAO.doSearchKC(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	//TONTHICON
	public DataListDTO doSearchTONTC(RpConstructionDTO obj, HttpServletRequest request) {
		List<RpConstructionDTO> ls = new ArrayList<RpConstructionDTO>();
		ls = rpQuantityDAO.doSearchTONTC(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	public DataListDTO doSearchHSHC(RpConstructionDTO obj, HttpServletRequest request) {
		List<RpConstructionDTO> ls = new ArrayList<RpConstructionDTO>();
		ls = rpQuantityDAO.doSearchHSHC(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	public String export(RpConstructionDTO obj, HttpServletRequest request) throws Exception {
		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_Danh_sach_thong_tin_BGMB_detail.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Danh_sach_thong_tin_chi_tiet_BGMB.xlsx");
		List provinceListId = PermissionUtils.getListIdInDomainData(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<RpConstructionDTO> data = new ArrayList<RpConstructionDTO>();
		if (provinceListId != null && !provinceListId.isEmpty()) {
			data = rpQuantityDAO.doSearchNHAN(obj);
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			// HuyPQ-22/08/2018-edit-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			styleDate.setAlignment(HorizontalAlignment.CENTER);
			int i = 2;
			RpConstructionDTO objCount = null;
			for (RpConstructionDTO dto : data) {
				if (i == 2) {
					objCount = dto;
				}
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysgroupname() != null) ? dto.getSysgroupname() : "");
				cell.setCellStyle(styleDate);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getCatprovincecode() != null) ? dto.getCatprovincecode() : "");
				cell.setCellStyle(style);
//				hoanm1_20181011_start
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getCatstattionhousecode() != null) ? dto.getCatstattionhousecode() : "");
				cell.setCellStyle(style);
//				hoanm1_20181011_end
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getCntContractCodeBGMB() != null) ? dto.getCntContractCodeBGMB() : "");
				cell.setCellStyle(style);
				//Huypq-20190604-start
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
				cell.setCellStyle(style);
				//Huy-end
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getCompanyassigndate() != null) ? dto.getCompanyassigndate() : "");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				if(dto.getHeightTM() != null){
					cell.setCellValue(dto.getHeightTM());
				}else{
					cell.setCellValue("");	
				}
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				if(dto.getNumberCoTM() != null){
					cell.setCellValue(dto.getNumberCoTM());
				}else{
					cell.setCellValue("");	
				}
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getHouseTypeNameTM() != null) ? dto.getHouseTypeNameTM() : "");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				if(dto.getHeightDD() != null){
					cell.setCellValue(dto.getHeightDD());
				}else{
					cell.setCellValue("");	
				}
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				if(dto.getNumberCoDD() != null){
					cell.setCellValue(dto.getNumberCoDD());
				}else{
					cell.setCellValue("");	
				}
				cell.setCellStyle(style);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getHouseTypeNameDD() != null) ? dto.getHouseTypeNameDD() : "");
				cell.setCellStyle(style);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getGroundingTypeName() != null) ? dto.getGroundingTypeName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : "");
				cell.setCellStyle(style);
//				hoanm1_20190827_start
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getHouseColumnReady() != null) ? dto.getHouseColumnReady() : "");
				cell.setCellStyle(style);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getHaveStartPoint() != null) ? dto.getHaveStartPoint() : "");
				cell.setCellStyle(style);
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getLengthMeter() != null) ? dto.getLengthMeter() : "");
				cell.setCellStyle(style);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getTypeMetter() != null) ? dto.getTypeMetter() : "");
				cell.setCellStyle(style);
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getNumColumnsAvaible() != null) ? dto.getNumColumnsAvaible() : "");
				cell.setCellStyle(style);
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getNumNewColumn() != null) ? dto.getNumNewColumn() : "");
				cell.setCellStyle(style);
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue((dto.getTypeColumns() != null) ? dto.getTypeColumns() : "");
				cell.setCellStyle(style);
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getACReady() != null) ? dto.getACReady() : "");
				cell.setCellStyle(style);
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue((dto.getStationReceive() != null) ? dto.getStationReceive() : "");
				cell.setCellStyle(style);
//				hoanm1_20190827_end
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Danh_sach_thong_tin_chi_tiet_BGMB.xlsx");
		return path;
	}
//	hungtd_20181217_end
	
	public String exportWorkItemServiceTask(WorkItemDetailDTO obj, HttpServletRequest request) throws Exception {
		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_sanluong_detail.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Export_sanluong_detail.xlsx");
		List provinceListId = PermissionUtils.getListIdInDomainData(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<WorkItemDetailDTO> data = new ArrayList<WorkItemDetailDTO>();
		if (provinceListId != null && !provinceListId.isEmpty()) {
			data = rpQuantityDAO.doSearchQuantity(obj, provinceListId);
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			// HuyPQ-22/08/2018-edit-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			styleDate.setAlignment(HorizontalAlignment.CENTER);
			int i = 2;
			WorkItemDetailDTO objCount = null;
			for (WorkItemDetailDTO dto : data) {
				if (i == 2) {
					objCount = dto;
				}
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getDateComplete() != null) ? dto.getDateComplete() : "");
				cell.setCellStyle(styleDate);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getConstructorName() != null) ? dto.getConstructorName() : "");
				cell.setCellStyle(style);
//				hoanm1_20181011_start
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getConstructorName1() != null) ? dto.getConstructorName1() : "");
				cell.setCellStyle(style);
//				hoanm1_20181011_end
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getCatstationCode() != null) ? dto.getCatstationCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getName() != null) ? dto.getName() : "");
				cell.setCellStyle(style);
				// HuyPQ-17/08/2018-edit-start
				cell = row.createCell(7, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
				cell.setCellStyle(styleNumber);
				// HuyPQ-17/08/2018-edit-end
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(getStringForStatus(dto.getStatus()));
				cell.setCellStyle(style);
//				cell = row.createCell(8, CellType.STRING);
//				cell.setCellValue((dto.getCatProvinceCode() != null) ? dto.getCatProvinceCode() : "");
//				cell.setCellStyle(style);
//				cell = row.createCell(9, CellType.STRING);
//				cell.setCellValue((dto.getPerformerName() != null) ? dto.getPerformerName() : "");
//				cell.setCellStyle(style);
//				// HuyPQ-22/08/2018-edit-start
//				cell = row.createCell(10, CellType.STRING);
//				cell.setCellValue((dto.getCompleteDate() != null) ? dto.getCompleteDate() : null);
//				cell.setCellStyle(styleDate);
				// HuyPQ-end
			}
			Row row = sheet.createRow(i++);
			Cell cell = row.createCell(1, CellType.STRING);
			cell.setCellValue(objCount == null ? 0 : objCount.getCountDateComplete());
			cell.setCellStyle(styleNumber);
			Cell cellCat = row.createCell(4, CellType.STRING);
			cellCat.setCellValue(objCount == null ? 0 : objCount.getCountCatstationCode());
			cellCat.setCellStyle(styleNumber);
			Cell cellConstr = row.createCell(5, CellType.STRING);
			cellConstr.setCellValue(objCount == null ? 0 : objCount.getCountConstructionCode());
			cellConstr.setCellStyle(styleNumber);
			Cell cellWI = row.createCell(6, CellType.STRING);
			cellWI.setCellValue(objCount == null ? 0 : objCount.getCountWorkItemName());
			cellWI.setCellStyle(styleNumber);
			Cell cellQ = row.createCell(7, CellType.STRING);
			cellQ.setCellValue(objCount == null ? "0" : numberFormat(objCount.getTotalQuantity()));
			cellQ.setCellStyle(styleNumber);
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_sanluong_detail.xlsx");
		return path;
	}
	
	private String numberFormat(double value) {
		DecimalFormat myFormatter = new DecimalFormat("###,###.####");
//		NumberFormat numEN = NumberFormat.getPercentInstance();
		String percentageEN = myFormatter.format(value);
		
		return percentageEN;
	}

	private String getStringForStatus(String status) {
		// TODO Auto-generated method stub
		if ("1".equals(status)) {
			return "Chưa thực hiện";
		} else if ("2".equals(status)) {
			return "Đang thực hiện";
		} else if ("3".equals(status)) {
			return "Đã hoàn thành";
		}
		return null;
	}

	@Override
	public List<RpConstructionDTO> doSearch(RpConstructionDTO obj) {
		// TODO Auto-generated method stub
		return null;
	}
//	hungtd_20192101_start
	public DataListDTO doSearchCoupon(couponExportDTO obj, HttpServletRequest request) {
		List<couponExportDTO> ls = new ArrayList<couponExportDTO>();
		String provinceId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> provinceIdList = ConvertData.convertStringToList(provinceId, ",");
		if (provinceIdList != null && !provinceIdList.isEmpty()) {
			ls = rpQuantityDAO.doSearchCoupon(obj, provinceIdList);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
//	hungtd_20192101_end
//	hungtd_20192101_start
	public DataListDTO doSearchPopup(couponExportDTO obj, HttpServletRequest request) {
		List<couponExportDTO> ls = new ArrayList<couponExportDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty()) {
			ls = rpQuantityDAO.doSearchPopup(obj);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
//	hungtd_20192101_end
	
	//HuyPq-20190724-start
	public List<Long> getSysGroupIdByTTKT(){
		List<WorkItemDetailDTO> lst = rpQuantityDAO.getSysGroupIdByTTKT();
		List<Long> lstId = new ArrayList<Long>();
		for(WorkItemDetailDTO dto : lst) {
			lstId.add(dto.getSysGroupId());
		}
		return lstId;
	}
	
	public WorkItemDetailDTO getGroupLv2ByGroupUser(Long id){
		return rpQuantityDAO.getGroupLv2ByGroupUser(id);
	}
	//Huy-end
		//hienvd: START_20192507_start
	public DataListDTO doSearchSysPXK(couponExportDTO obj, HttpServletRequest request) {
		List<couponExportDTO> ls = new ArrayList<couponExportDTO>();
		ls = rpQuantityDAO.doSearchSysPXK(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	public DataListDTO doSearchSysPXK60(couponExportDTO obj, HttpServletRequest request) {
		List<couponExportDTO> ls = new ArrayList<couponExportDTO>();
		ls = rpQuantityDAO.doSearchSysPXK60(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

//	hienvd: END
	
	public String exportFileTonThiCong(RpConstructionDTO obj, HttpServletRequest request) throws Exception {
		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BaoCaoTonThiCong.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BaoCaoTonThiCong.xlsx");
		List<RpConstructionDTO> data = rpQuantityDAO.doSearchTONTC(obj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			// HuyPQ-17/08/2018-edit-end
			// HuyPQ-22/08/2018-edit-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end 
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			styleDate.setAlignment(HorizontalAlignment.CENTER);
			int i = 3;
			for (RpConstructionDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 3));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getSysGroupCode() != null ? dto.getSysGroupCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getCatprovincecode() != null ? dto.getCatprovincecode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getCatstattionhousecode() != null ? dto.getCatstattionhousecode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getCntContractCodeBGMB() != null ? dto.getCntContractCodeBGMB() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getCompanyassigndate() != null ? dto.getCompanyassigndate() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getOutOfdate() != null ? dto.getOutOfdate() : 0l);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getOutofdatereceivedBGMB() != null ? dto.getOutofdatereceivedBGMB() : "");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(dto.getConstructionTypeName() != null ? dto.getConstructionTypeName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(dto.getWorkItemComplete() != null ? dto.getWorkItemComplete() : "");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue(dto.getXd_dodang() != null ? dto.getXd_dodang() : "");
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue(dto.getEmai_XD_dodang() != null ? dto.getEmai_XD_dodang() : "");
				cell.setCellStyle(style);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue(dto.getAc_dodang() != null ? dto.getAc_dodang() : "");
				cell.setCellStyle(style);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue(dto.getEmai_AC_dodang() != null ? dto.getEmai_AC_dodang() : "");
				cell.setCellStyle(style);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue(dto.getXong_XD_LD() != null ? dto.getXong_XD_LD() : "");
				cell.setCellStyle(style);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue(dto.getXong_LD_TB() != null ? dto.getXong_LD_TB() : "");
				cell.setCellStyle(style);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue(dto.getLd_dodang() != null ? dto.getLd_dodang() : "");
				cell.setCellStyle(style);
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue(dto.getEmai_LD_dodang() != null ? dto.getEmai_LD_dodang() : "");
				cell.setCellStyle(style);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue(dto.getThietbi_dodang() != null ? dto.getThietbi_dodang() : "");
				cell.setCellStyle(style);
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue(dto.getEmai_thietbi_dodang() != null ? dto.getEmai_thietbi_dodang() : "");
				cell.setCellStyle(style);
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue(dto.getWorkItemOutStanding() != null ? dto.getWorkItemOutStanding() : "");
				cell.setCellStyle(style);
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue(dto.getColumnHeight() != null ? dto.getColumnHeight() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue(dto.getColumnHeight() != null ? dto.getColumnHeight() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue(dto.getDescription() != null ? dto.getDescription() : "");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BaoCaoTonThiCong.xlsx");
		return path;
	}
	
	//Huypq-20191004-start
	public DataListDTO doSearchEvaluateKpiHshc(RpConstructionDTO obj) {
		List<RpConstructionDTO> ls = new ArrayList<RpConstructionDTO>();
		ls = rpQuantityDAO.doSearchEvaluateKpiHshc(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	//Huy-end
}
