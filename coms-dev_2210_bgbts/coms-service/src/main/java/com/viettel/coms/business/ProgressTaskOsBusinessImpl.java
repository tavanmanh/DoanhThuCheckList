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

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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

import com.viettel.coms.bo.ProgressTaskOsBO;
import com.viettel.coms.dao.ProgressTaskOsDAO;
import com.viettel.coms.dto.ProgressTaskOsDTO;
import com.viettel.coms.dto.RpProgressMonthPlanOsDTO;
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

@Service("progressTaskOsBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProgressTaskOsBusinessImpl extends
		BaseFWBusinessImpl<ProgressTaskOsDAO, ProgressTaskOsDTO, ProgressTaskOsBO> implements ProgressTaskOsBusiness {

	protected final Logger log = Logger.getLogger(ProgressTaskOsBusinessImpl.class);
	@Autowired
	private ProgressTaskOsDAO progressTaskOsDAO;

	public ProgressTaskOsBusinessImpl() {
		tModel = new ProgressTaskOsBO();
		tDAO = progressTaskOsDAO;
	}

	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	@Override
	public ProgressTaskOsDAO gettDAO() {
		return progressTaskOsDAO;
	}

	public DataListDTO doSearch(ProgressTaskOsDTO obj, HttpServletRequest request) {
		List<ProgressTaskOsDTO> ls = new ArrayList<ProgressTaskOsDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty()) {
			obj.setGroupIdList(groupIdList);
			ls = progressTaskOsDAO.doSearch(obj);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setTotal(obj.getTotalRecord());
		data.setStart(1);
		return data;
	}

	public DataListDTO getById(ProgressTaskOsDTO obj) {
		List<ProgressTaskOsDTO> ls = progressTaskOsDAO.getById(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setTotal(obj.getTotalRecord());
		data.setStart(1);
		return data;
	}

	public Long saveAdd(ProgressTaskOsDTO obj, HttpServletRequest request) throws Exception{
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Long id = 0l;
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty()) {
			if(groupIdList.contains(obj.getTtktId().toString())) {
				List<ProgressTaskOsDTO> ls = progressTaskOsDAO.getById(obj);
				if (ls.size() > 0) {
					throw new BusinessException(
							"Tiến độ công việc tháng " + obj.getMonthYear() + " của " + obj.getTtkt() + " đã tồn tại");
				}
				for (ProgressTaskOsDTO dto : obj.getListProgressTask()) {
					dto.setCreatedDate(new Date());
					dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
					dto.setStatus("1");
					dto.setTtkv(obj.getTtkv());
					dto.setTtkt(obj.getTtkt());
					dto.setMonthYear(obj.getMonthYear());
					dto.setTtktId(obj.getTtktId());
					id = progressTaskOsDAO.saveObject(dto.toModel());
				}
			} else {
				throw new BusinessException(
						"Bạn không có quyền tạo tiến độ cho tỉnh: " + obj.getTtkt());
			}
		} else {
			throw new BusinessException(
					"Bạn không có quyền tạo tiến độ ngoài OS");
		}
		
		return id;
	}

	public Long saveUpdate(ProgressTaskOsDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Long id = null;
		for (ProgressTaskOsDTO dto : obj.getListProgressTask()) {
			obj.setUpdatedDate(new Date());
			obj.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
			id = progressTaskOsDAO.updateObject(dto.toModel());
		}
		return id;
	}

	public Long deleteRecord(ProgressTaskOsDTO obj, HttpServletRequest request) {
//		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
//		obj.setUpdatedDate(new Date());
//		obj.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
//		obj.setStatus("0");
		Long id = progressTaskOsDAO.deleteRecord(obj);
		return id;
	}

	public DataListDTO getDataTaskByProvince(ProgressTaskOsDTO obj) {
		List<ProgressTaskOsDTO> ls = progressTaskOsDAO.getDataTaskByProvince(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setTotal(obj.getTotalRecord());
		data.setStart(1);
		return data;
	}

	public DataListDTO doSearchMain(ProgressTaskOsDTO obj, HttpServletRequest request) {
		List<ProgressTaskOsDTO> ls = new ArrayList<ProgressTaskOsDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		log.info("List tinhhhhhhhhhhhhhhhhh: "+ groupId);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty()) {
			ls = progressTaskOsDAO.doSearchMain(obj, groupIdList);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setTotal(obj.getTotalRecord());
		data.setStart(1);
		return data;
	}

	public Long updateProgressTaskOs(ProgressTaskOsDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		HashMap<String, ProgressTaskOsDTO> mapTask = new HashMap<>();
		HashMap<String, ProgressTaskOsDTO> mapPro = new HashMap<>();
		List<ProgressTaskOsDTO> lsById = progressTaskOsDAO.getById(obj);
		Long ids = 0l;
		for(ProgressTaskOsDTO task : lsById) {
			String key = task.getConstructionCode().trim().toUpperCase() +"|"+ task.getCntContractCode().trim().toUpperCase()+"|"+task.getSourceTask()+"|"+task.getConstructionType();
			mapTask.put(key, task);
			mapPro.put(key, task);
		}
		List<ProgressTaskOsDTO> ls = progressTaskOsDAO.getDataTaskByProvince(obj);
		for(ProgressTaskOsDTO dto : ls) {
			String key = dto.getConstructionCode().trim().toUpperCase() +"|"+ dto.getCntContractCode().trim().toUpperCase()+"|"+dto.getSourceTask()+"|"+dto.getConstructionType();
			ProgressTaskOsDTO pro = mapTask.get(key);
			if(pro!=null) {
				pro.setTdslAccomplishedDate(dto.getTdslAccomplishedDate());
				pro.setDescription(dto.getDescription());
				pro.setSourceTask(dto.getSourceTask());
				pro.setConstructionType(dto.getConstructionType());
				pro.setQuantityValue(dto.getQuantityValue());
				pro.setHshcValue(dto.getHshcValue());
				pro.setSalaryValue(dto.getSalaryValue());
				pro.setBillValue(dto.getBillValue());
				pro.setTdttCollectMoneyDate(dto.getTdttCollectMoneyDate());
				pro.setApproveRevenueDate(dto.getApproveRevenueDate());
				pro.setApproveCompleteDate(dto.getApproveCompleteDate());
				ids = progressTaskOsDAO.updateDataEdit(pro);
				mapPro.remove(key);
			} else {
				dto.setCreatedDate(new Date());
				dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
				dto.setStatus("1");
				dto.setTtkv(obj.getTtkv());
				dto.setTtkt(obj.getTtkt());
				dto.setMonthYear(obj.getMonthYear());
				ids = progressTaskOsDAO.saveObject(dto.toModel());
			}
		}
		if(mapPro.size()>0) {
			List<ProgressTaskOsDTO>  lstRemove = new ArrayList<>(mapPro.values());
			for(ProgressTaskOsDTO pro : lstRemove) {
				progressTaskOsDAO.delete(pro.toModel());
			}
		}
		return ids;
	}
	
	public void createCellString(Row row, int cellNumber, Object data, XSSFCellStyle style) {
		Cell cell = row.createCell(cellNumber, CellType.STRING);
		if (data instanceof String) {
			cell.setCellValue(data != null ? String.valueOf(data) : "");
		} else if (data instanceof Long) {
			cell.setCellValue(data != null ? (long) data : 0l);
		} else if (data instanceof Double) {
			cell.setCellValue(data != null ? (double) data : 0d);
		} else {
			cell.setCellValue("");
		}
		cell.setCellStyle(style);
	}

	public String exportCompleteProgress(ProgressTaskOsDTO obj, HttpServletRequest request) throws Exception {
		obj.setPage(1L);
		obj.setPageSize(100);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Tien_do_ngoai_OS.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Tien_do_ngoai_OS.xlsx");
		List<ProgressTaskOsDTO> data = new ArrayList<ProgressTaskOsDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		obj.setGroupIdList(groupIdList);
		if (groupIdList != null && !groupIdList.isEmpty()) {
			data = progressTaskOsDAO.getDataExportSheet1(obj);
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// Huypq-20200110-start sheet Toàn quốc
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
//			stylePercent.setDataFormat(workbook.createDataFormat().getFormat(BuiltinFormats.getBuiltinFormat(10)));
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("##0.00%"));
			stylePercent.setAlignment(HorizontalAlignment.RIGHT);
			
			XSSFCellStyle styleCurrency = ExcelUtils.styleText(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));

			int i = 5;
			for (ProgressTaskOsDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);
				int k = 1;
				while (k < 160) {
					createCellString(row, k, dto.getTtkv(), style);
					k++;
					createCellString(row, k, dto.getTtkt(), style);
					k++;
					createCellString(row, k, dto.getTdQlKhThang(), styleCurrency);
					k++;
					//Tiến độ quỹ lương
					createCellString(row, k, dto.getTdQlTrenDuongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdQlTrenDuongTyle(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdQlGdCnKyValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdQlGdCnKyTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdQlDoiSoat4aValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdQlDoiSoat4aTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdQlPhtThamDuyetValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdQlPhtThamDuyetTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdQlPhtNghiemThuValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdQlPhtNghiemThuTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdQlDangLamHoSoValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdQlDangLamHoSoTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdQlDuKienHoanThanhValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdQlDuKienHoanThanhTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdQlNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ Hshc chi phí
					createCellString(row, k, dto.getTdHshcCpKhThang(), styleCurrency);
					k++;
//					createCellString(row, k, dto.getTdHshcCpTong(), style);
//					k++;
//					createCellString(row, k, dto.getTdHshcCpTthtDaDuyet(), style);
//					k++;
					createCellString(row, k, dto.getTdHshcCpTrenDuongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcCpTrenDuongTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcCpGdCnKyValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcCpGdCnKyTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcCpDoiSoat4aValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcCpDoiSoat4aTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcCpPhtThamDuyetValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcCpPhtThamDuyetTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcCpPhtNghiemThuValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcCpPhtNghiemThuTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcCpDangLamHoSoValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcCpDangLamHoSoTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcCpDuKienHoanThanhValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcCpDuKienHoanThanhTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcCpNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ Hshc Xây lắp
					createCellString(row, k, dto.getTdHshcXlKhThang(), styleCurrency);
					k++;
//					createCellString(row, k, dto.getTdHshcXlTong(), style);
//					k++;
//					createCellString(row, k, dto.getTdHshcXlTthtDaDuyet(), style);
//					k++;
					createCellString(row, k, dto.getTdHshcXlTrenDuongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcXlTrenDuongTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcXlGdCnKyValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcXlGdCnKyTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcXlDoiSoat4aValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcXlDoiSoat4aTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcXlPhtThamDuyetValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcXlPhtThamDuyetTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcXlPhtNghiemThuValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcXlPhtNghiemThuTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcXlDangLamHoSoValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcXlDangLamHoSoTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcXlDuKienHoanThanhValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcXlDuKienHoanThanhTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcXlNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ HSHC Ngoài tập đoàn
					createCellString(row, k, dto.getTdHshcNtdKhThang(), styleCurrency);
					k++;
//					createCellString(row, k, dto.getTdHshcNtdTong(), style);
//					k++;
//					createCellString(row, k, dto.getTdHshcNtdPtkDaDuyet(), style);
//					k++;
					createCellString(row, k, dto.getTdHshcNtdTrenDuongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNtdTrenDuongTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNtdGdCnKyValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNtdGdCnKyTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNtdDoiSoat4aValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNtdDoiSoat4aTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNtdPhtThamDuyetValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNtdPhtThamDuyetTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNtdPhtNghiemThuValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNtdPhtNghiemThuTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNtdDangLamHoSoValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNtdDangLamHoSoTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNtdDuKienHoanThanhValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNtdDuKienHoanThanhTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNtdNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ sản lượng Chi phí
					createCellString(row, k, dto.getTdSlCpKhThang(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlCpDaHoanThanh(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlCpDangThiCongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlCpDangThiCongTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlCpDuKienHoanThanhValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlCpDuKienHoanThanhTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlCpNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ sản lượng xây lắp
					createCellString(row, k, dto.getTdSlXlKhThang(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlXlDaHoanThanh(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlXlDangThiCongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlXlDangThiCongTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlXlDuKienHoanThanhValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlXlDuKienHoanThanhTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlXlNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ sản lượng ngoài tập đoàn
					createCellString(row, k, dto.getTdSlNtdKhThang(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlNtdDaHoanThanh(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlNtdDangThiCongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlNtdDangThiCongTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlNtdDuKienHoanThanhValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlNtdDuKienHoanThanhTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlNtdNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ sản lượng xây dựng dân dụng
					createCellString(row, k, dto.getTdSlXdddKhThang(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlXdddDaHoanThanh(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlXdddDangThiCongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlXdddDangThiCongTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlXdddDuKienHoanThanhValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlXdddDuKienHoanThanhTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlXdddNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ Thu hồi dòng tiền
					createCellString(row, k, dto.getTdThdtKhThang(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdThdtDaHoanThanh(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdThdtPhtDangKiemTraValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdThdtPhtDangKiemTraTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdThdtPtcDangKiemTraValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdThdtPtcDangKiemTraTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdThdtDuKienHoanThanhValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdThdtDuKienHoanThanhTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdThdtNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ HTTC Xây dựng móng
					createCellString(row, k, dto.getTdHtctXdmKhThang(), style);
					k++;
					createCellString(row, k, dto.getTdHtctXdmDaHt(), style);
					k++;
					createCellString(row, k, dto.getTdHtctXdmDangTc(), style);
					k++;
					createCellString(row, k, dto.getTdHtctXdmDuKienHt(), style);
					k++;
					createCellString(row, k, dto.getTdHtctXdmTyLe(), stylePercent);
					k++;
					
					//Tiến độ HTTC hoàn thiện
					createCellString(row, k, dto.getTdHtctHtKhThang(), style);
					k++;
					createCellString(row, k, dto.getTdHtctHtDaHt(), style);
					k++;
					createCellString(row, k, dto.getTdHtctHtDangTc(), style);
					k++;
					createCellString(row, k, dto.getTdHtctHtDuKienHt(), style);
					k++;
					createCellString(row, k, dto.getTdHtctHtTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHtctNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ HSHC HTCT
					createCellString(row, k, dto.getTdHshcHtctKhThang(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcHtctTrenDuongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcHtctTrenDuongTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcHtctGdCnKyValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcHtctGdCnKyTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcHtctDoiSoat4aValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcHtctDoiSoat4aTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcHtctPhtThamDuyetValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcHtctPhtThamDuyetTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcHtctPhtNghiemThuValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcHtctPhtNghiemThuTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcHtctTtktHoSoValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcHtctTtktHoSoTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcHtctDuKienHtValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcHtctDuKienHtTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcHtctNguyenNhanKoHt(), style);
					k++;
					
					//Tiến độ Tổng sản lượng CP + XL + NTĐ
					createCellString(row, k, dto.getTdTongKhThang(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdTongDaHt(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdTongDangTcValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdTongDangTcTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdTongDuKienHtValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdTongDuKienHtTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdTongDeXuatVuongMac(), style);
					k++;
					
					//Tiến độ HSHC XDDD
					createCellString(row, k, dto.getTdHshcNdKhThang(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNdTrenDuongValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNdTrenDuongTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNdGdCnKyValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNdGdCnKyTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNdDoiSoat4aValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNdDoiSoat4aTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNdThamDuyetValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNdPhtThamDuyetTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNdPhtNghiemThuValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNdPhtNghiemThuTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNdHoSoValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNdHoSoTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdHshcNdDuKienHtValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdHshcNdDuKienHtTyLe(), stylePercent);
					k++;
					
					//Tiến độ Sản lượng HTCT
					createCellString(row, k, dto.getTdSlHtctKhThang(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlHtctDaHt(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlHtctDangTcValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlHtctDangTcTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlHtctDuKienHtValue(), styleCurrency);
					k++;
					createCellString(row, k, dto.getTdSlHtctDuKienHtTyLe(), stylePercent);
					k++;
					createCellString(row, k, dto.getTdSlHtctNguyenNhanKoHt(), style);
					k++;
				}
			}
		}
		// Huypq-end

		//Sheet 2 Chi tiết
		obj.setPage(null);
		obj.setPageSize(null);
		List<ProgressTaskOsDTO> dataDetail = progressTaskOsDAO.doSearch(obj);
		XSSFSheet sheetDetail = workbook.getSheetAt(1);
		if (dataDetail != null && !dataDetail.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheetDetail);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheetDetail);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheetDetail);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			XSSFCellStyle styleCurrency = ExcelUtils.styleText(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("###,###"));
			int i = 2;
			for (ProgressTaskOsDTO dto : dataDetail) {
				Row row = sheetDetail.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getTtkt() != null) ? dto.getTtkt() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getCntContractCode() != null) ? dto.getCntContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getWorkItemName() != null) ? dto.getWorkItemName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				if (dto.getSourceTask() != null) {
					if (dto.getSourceTask().equals("1")) {
						cell.setCellValue("Xây lắp");
					} else if (dto.getSourceTask().equals("2")) {
						cell.setCellValue("Chi phí");
					} else if (dto.getSourceTask().equals("3")) {
						cell.setCellValue("Ngoài Tập đoàn");
					} else if (dto.getSourceTask().equals("4")) {
						cell.setCellValue("Hạ tầng cho thuê xây dựng móng");
					} else if (dto.getSourceTask().equals("5")) {
						cell.setCellValue("Hạ tầng cho thuê hoàn thiện");
					} else if (dto.getSourceTask().equals("6")) {
						cell.setCellValue("Công trình XDDD");
					}
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				if (dto.getConstructionType() != null) {
					if (dto.getConstructionType().equals("1")) {
						cell.setCellValue(
								"Các Công trình BTS, Costie, SWAP và các công trình nguồn đầu tư mảng xây lắp");
					} else if (dto.getConstructionType().equals("2")) {
						cell.setCellValue("Các công trình nguồn chi phí");
					} else if (dto.getConstructionType().equals("3")) {
						cell.setCellValue("Các công trình Bảo dưỡng ĐH và MFĐ");
					} else if (dto.getConstructionType().equals("4")) {
						cell.setCellValue("Các công trình Gpon");
					} else if (dto.getConstructionType().equals("5")) {
						cell.setCellValue("Các công trình Hợp đồng 12 đầu việc");
					} else if (dto.getConstructionType().equals("6")) {
						cell.setCellValue("Các công trình Ngoài Tập đoàn");
					} else if (dto.getConstructionType().equals("7")) {
						cell.setCellValue("Hạ tầng cho thuê");
					} else if (dto.getConstructionType().equals("8")) {
						cell.setCellValue("Công trình XDDD");
					} else if (dto.getConstructionType().equals("9")) {
						cell.setCellValue("Các hợp đồng cơ điện nguồn đầu tư TCTY ký");
					}
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getQuantityValue() != null) ? dto.getQuantityValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getHshcValue() != null) ? dto.getHshcValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getSalaryValue() != null) ? dto.getSalaryValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getBillValue() != null) ? dto.getBillValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getTdslAccomplishedDate() != null) ? dto.getTdslAccomplishedDate() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getTdslConstructing() != null) ? dto.getTdslConstructing() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue(
						(dto.getTdslExpectedCompleteDate() != null) ? dto.getTdslExpectedCompleteDate() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getTdhsTctNotApproval() != null) ? dto.getTdhsTctNotApproval() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getTdhsSigningGdcn() != null) ? dto.getTdhsSigningGdcn() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getTdhsControl4a() != null) ? dto.getTdhsControl4a() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getTdhsPhtApprovaling() != null) ? dto.getTdhsPhtApprovaling() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getTdhsPhtAcceptancing() != null) ? dto.getTdhsPhtAcceptancing() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getTdhsTtktProfile() != null) ? dto.getTdhsTtktProfile() : null);
				cell.setCellStyle(styleCenter);

				// Quỹ lương
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue(
						(dto.getTdhsExpectedCompleteDate() != null) ? dto.getTdhsExpectedCompleteDate() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue((dto.getTdttCollectMoneyDate() != null) ? dto.getTdttCollectMoneyDate() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getTdttProfilePht() != null) ? dto.getTdttProfilePht() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue((dto.getTdttProfilePtc() != null) ? dto.getTdttProfilePtc() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue(
						(dto.getTdttExpectedCompleteDate() != null) ? dto.getTdttExpectedCompleteDate() : null);
				cell.setCellStyle(styleCenter);

			}
		}

		// sheet 3 Dự kiến HT
		List<ProgressTaskOsDTO> dataDKKQHT = progressTaskOsDAO.doSearchDkHt(obj);
		XSSFSheet sheet1 = workbook.getSheetAt(2);
		if (dataDKKQHT != null && !dataDKKQHT.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet1);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet1);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet1);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			XSSFCellStyle styleCurrency = ExcelUtils.styleText(sheet1);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("###,###"));
			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("##0.00%"));
			stylePercent.setAlignment(HorizontalAlignment.RIGHT);
			int i = 5;
			for (ProgressTaskOsDTO dto : dataDKKQHT) {
				Row row = sheet1.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getTtkt() != null) ? dto.getTtkt() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
				cell.setCellStyle(style);

				// sản lượng xây lắp
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getTdSlXlKhThang() != null) ? dto.getTdSlXlKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getTdSlXlDuKienHoanThanhValue() != null) ? dto.getTdSlXlDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getTdSlXlDuKienHoanThanhTyLe() != null) ? dto.getTdSlXlDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getGiaTriSLXLXepThu() != null) ? dto.getGiaTriSLXLXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// sản lượng chi phí
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getTdSlCpKhThang() != null) ? dto.getTdSlCpKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getTdSlCpDuKienHoanThanhValue() != null) ? dto.getTdSlCpDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getTdSlCpDuKienHoanThanhTyLe() != null) ? dto.getTdSlCpDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getGiaTriSLCPXepThu() != null) ? dto.getGiaTriSLCPXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// sản lượng NTĐ
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getTdSlNtdKhThang() != null) ? dto.getTdSlNtdKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getTdSlNtdDuKienHoanThanhValue() != null) ? dto.getTdSlNtdDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getTdSlNtdDuKienHoanThanhTyLe() != null) ? dto.getTdSlNtdDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getGiaTriSLNTDXepThu() != null) ? dto.getGiaTriSLNTDXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
				// sản lượng XDDD
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getTdSlXdddKhThang() != null) ? dto.getTdSlXdddKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getTdSlXdddDuKienHoanThanhValue() != null) ? dto.getTdSlXdddDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getTdSlXdddDuKienHoanThanhTyLe() != null) ? dto.getTdSlXdddDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getGiaTriSLXdddXepThu() != null) ? dto.getGiaTriSLXdddXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// Quỹ lương
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getTdQlKhThang() != null) ? dto.getTdQlKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue((dto.getTdQlDuKienHoanThanhValue() != null) ? dto.getTdQlDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue((dto.getTdQlDuKienHoanThanhTyLe() != null) ? dto.getTdQlDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(26, CellType.STRING);
				cell.setCellValue((dto.getGiaTriQuyLuongXepThu() != null) ? dto.getGiaTriQuyLuongXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(27, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// HSHC xây lắp
				cell = row.createCell(28, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXlKhThang() != null) ? dto.getTdHshcXlKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(29, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXlDuKienHoanThanhValue() != null) ? dto.getTdHshcXlDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(30, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXlDuKienHoanThanhTyLe() != null) ? dto.getTdHshcXlDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(31, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXlXepThu() != null) ? dto.getTdHshcXlXepThu() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(32, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// HSHC NTĐ
				cell = row.createCell(33, CellType.STRING);
				cell.setCellValue((dto.getTdHshcNtdKhThang() != null) ? dto.getTdHshcNtdKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(34, CellType.STRING);
				cell.setCellValue((dto.getTdHshcNtdDuKienHoanThanhValue() != null) ? dto.getTdHshcNtdDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(35, CellType.STRING);
				cell.setCellValue((dto.getTdHshcNtdDuKienHoanThanhTyLe() != null) ? dto.getTdHshcNtdDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(36, CellType.STRING);
				cell.setCellValue((dto.getTdHshcNtdXepThu() != null) ? dto.getTdHshcNtdXepThu() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(37, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// HSHC chi phí
				cell = row.createCell(38, CellType.STRING);
				cell.setCellValue((dto.getTdHshcCpKhThang() != null) ? dto.getTdHshcCpKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(39, CellType.STRING);
				cell.setCellValue((dto.getTdHshcCpDuKienHoanThanhValue() != null) ? dto.getTdHshcCpDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(40, CellType.STRING);
				cell.setCellValue((dto.getTdHshcCpDuKienHoanThanhTyLe() != null) ? dto.getTdHshcCpDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(41, CellType.STRING);
				cell.setCellValue((dto.getTdHshcCpXepThu() != null) ? dto.getTdHshcCpXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(42, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// HSHC HTCT
				cell = row.createCell(43, CellType.STRING);
				cell.setCellValue((dto.getTdHshcHtctKhThang() != null) ? dto.getTdHshcHtctKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(44, CellType.STRING);
				cell.setCellValue((dto.getTdHshcHtctDuKienHtValue() != null) ? dto.getTdHshcHtctDuKienHtValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(45, CellType.STRING);
				cell.setCellValue((dto.getTdHshcHtctDuKienHtTyLe() != null) ? dto.getTdHshcHtctDuKienHtTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(46, CellType.STRING);
				cell.setCellValue((dto.getTdHshcHtctXepThu() != null) ? dto.getTdHshcHtctXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(47, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// Thu hồi dòng tiền
				cell = row.createCell(48, CellType.STRING);
				cell.setCellValue((dto.getTdThdtKhThang() != null) ? dto.getTdThdtKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(49, CellType.STRING);
				cell.setCellValue((dto.getTdThdtDuKienHoanThanhValue() != null) ? dto.getTdThdtDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(50, CellType.STRING);
				cell.setCellValue((dto.getTdThdtDuKienHoanThanhTyLe() != null) ? dto.getTdThdtDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(51, CellType.STRING);
				cell.setCellValue((dto.getTdThdtXepThu() != null) ? dto.getTdThdtXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(52, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
				// HSHC XDDD
				cell = row.createCell(53, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXdddKhThang() != null) ? dto.getTdHshcXdddKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(54, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXdddDuKienHoanThanhValue() != null) ? dto.getTdHshcXdddDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(55, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXdddDuKienHoanThanhTyLe() != null) ? dto.getTdHshcXdddDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(56, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXdddXepThu() != null) ? dto.getTdHshcXdddXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(57, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
				// sản lượng HTCT
				cell = row.createCell(58, CellType.STRING);
				cell.setCellValue((dto.getTdSlHtctKhThang() != null) ? dto.getTdSlHtctKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(59, CellType.STRING);
				cell.setCellValue((dto.getTdSlHtctDuKienHtValue() != null) ? dto.getTdSlHtctDuKienHtValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(60, CellType.STRING);
				cell.setCellValue((dto.getTdSlHtctDuKienHtTyLe() != null) ? dto.getTdSlHtctDuKienHtTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(61, CellType.STRING);
				cell.setCellValue((dto.getTdSlHtctXepThu() != null) ? dto.getTdSlHtctXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(62, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
			}
		}
		// sheet 3 

		// sheet 4 Chốt HT 
		List<ProgressTaskOsDTO> dataCKQHT = progressTaskOsDAO.doSearchCHT(obj);
		XSSFSheet sheet2 = workbook.getSheetAt(3);
		if (dataCKQHT != null && !dataCKQHT.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet2);
			XSSFCellStyle styleNumber = ExcelUtils.styleCurrencyV2(sheet2);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet2);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			XSSFCellStyle styleCurrency = ExcelUtils.styleText(sheet2);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("###,###"));
			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("##0.00%"));
			stylePercent.setAlignment(HorizontalAlignment.RIGHT);
			int i = 5;
			for (ProgressTaskOsDTO dto : dataCKQHT) {
				Row row = sheet2.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getTtkt() != null) ? dto.getTtkt() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
				cell.setCellStyle(style);

				// sản lượng xây lắp
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getTdSlXlKhThang() != null) ? dto.getTdSlXlKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getTdSlXlDuKienHoanThanhValue() != null) ? dto.getTdSlXlDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getTdSlXlDuKienHoanThanhTyLe() != null) ? dto.getTdSlXlDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getGiaTriSLXLXepThu() != null) ? dto.getGiaTriSLXLXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// sản lượng chi phí
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getTdSlCpKhThang() != null) ? dto.getTdSlCpKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getTdSlCpDuKienHoanThanhValue() != null) ? dto.getTdSlCpDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getTdSlCpDuKienHoanThanhTyLe() != null) ? dto.getTdSlCpDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getGiaTriSLCPXepThu() != null) ? dto.getGiaTriSLCPXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// sản lượng NTĐ
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getTdSlNtdKhThang() != null) ? dto.getTdSlNtdKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getTdSlNtdDuKienHoanThanhValue() != null) ? dto.getTdSlNtdDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getTdSlNtdDuKienHoanThanhTyLe() != null) ? dto.getTdSlNtdDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getGiaTriSLNTDXepThu() != null) ? dto.getGiaTriSLNTDXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
				// Quỹ lương
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getTdQlKhThang() != null) ? dto.getTdQlKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getTdQlDuKienHoanThanhValue() != null) ? dto.getTdQlDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getTdQlDuKienHoanThanhTyLe() != null) ? dto.getTdQlDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getGiaTriQuyLuongXepThu() != null) ? dto.getGiaTriQuyLuongXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// HSHC xây lắp
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXlKhThang() != null) ? dto.getTdHshcXlKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXlDuKienHoanThanhValue() != null) ? dto.getTdHshcXlDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXlDuKienHoanThanhTyLe() != null) ? dto.getTdHshcXlDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(26, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXlXepThu() != null) ? dto.getTdHshcXlXepThu() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(27, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// HSHC NTĐ
				cell = row.createCell(28, CellType.STRING);
				cell.setCellValue((dto.getTdHshcNtdKhThang() != null) ? dto.getTdHshcNtdKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(29, CellType.STRING);
				cell.setCellValue((dto.getTdHshcNtdDuKienHoanThanhValue() != null) ? dto.getTdHshcNtdDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(30, CellType.STRING);
				cell.setCellValue((dto.getTdHshcNtdDuKienHoanThanhTyLe() != null) ? dto.getTdHshcNtdDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(31, CellType.STRING);
				cell.setCellValue((dto.getTdHshcNtdXepThu() != null) ? dto.getTdHshcNtdXepThu() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(32, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// HSHC chi phí
				cell = row.createCell(33, CellType.STRING);
				cell.setCellValue((dto.getTdHshcCpKhThang() != null) ? dto.getTdHshcCpKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(34, CellType.STRING);
				cell.setCellValue((dto.getTdHshcCpDuKienHoanThanhValue() != null) ? dto.getTdHshcCpDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(35, CellType.STRING);
				cell.setCellValue((dto.getTdHshcCpDuKienHoanThanhTyLe() != null) ? dto.getTdHshcCpDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(36, CellType.STRING);
				cell.setCellValue((dto.getTdHshcCpXepThu() != null) ? dto.getTdHshcCpXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(37, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// HSHC HTCT
				cell = row.createCell(38, CellType.STRING);
				cell.setCellValue((dto.getTdHshcHtctKhThang() != null) ? dto.getTdHshcHtctKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(39, CellType.STRING);
				cell.setCellValue((dto.getTdHshcHtctDuKienHtValue() != null) ? dto.getTdHshcHtctDuKienHtValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(40, CellType.STRING);
				cell.setCellValue((dto.getTdHshcHtctDuKienHtTyLe() != null) ? dto.getTdHshcHtctDuKienHtTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(41, CellType.STRING);
				cell.setCellValue((dto.getTdHshcHtctXepThu() != null) ? dto.getTdHshcHtctXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(42, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// Thu hồi dòng tiền
				cell = row.createCell(43, CellType.STRING);
				cell.setCellValue((dto.getTdThdtKhThang() != null) ? dto.getTdThdtKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(44, CellType.STRING);
				cell.setCellValue((dto.getTdThdtDuKienHoanThanhValue() != null) ? dto.getTdThdtDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(45, CellType.STRING);
				cell.setCellValue((dto.getTdThdtDuKienHoanThanhTyLe() != null) ? dto.getTdThdtDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(46, CellType.STRING);
				cell.setCellValue((dto.getTdThdtXepThu() != null) ? dto.getTdThdtXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(47, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
				// sản lượng XDDD
				cell = row.createCell(48, CellType.STRING);
				cell.setCellValue((dto.getTdSlXdddKhThang() != null) ? dto.getTdSlXdddKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(49, CellType.STRING);
				cell.setCellValue((dto.getTdSlXdddDuKienHoanThanhValue() != null) ? dto.getTdSlXdddDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(50, CellType.STRING);
				cell.setCellValue((dto.getTdSlXdddDuKienHoanThanhTyLe() != null) ? dto.getTdSlXdddDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(51, CellType.STRING);
				cell.setCellValue((dto.getGiaTriSLXdddXepThu() != null) ? dto.getGiaTriSLXdddXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(52, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
				// HSHC XDDD
				cell = row.createCell(53, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXdddKhThang() != null) ? dto.getTdHshcXdddKhThang() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(54, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXdddDuKienHoanThanhValue() != null) ? dto.getTdHshcXdddDuKienHoanThanhValue() : 0L);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(55, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXdddDuKienHoanThanhTyLe() != null) ? dto.getTdHshcXdddDuKienHoanThanhTyLe() : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(56, CellType.STRING);
				cell.setCellValue((dto.getTdHshcXdddXepThu() != null) ? dto.getTdHshcXdddXepThu() : 0L);
				cell.setCellStyle(style);
				cell = row.createCell(57, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
			}
		}
		// sheet 4

		// Huypq-start sheet 5 - Báo cáo
		// -------------------------------Quỹ lương------------------------------------
		List<ProgressTaskOsDTO> dataSheetBcQl = progressTaskOsDAO.getDataTienDoQuyLuongKv(obj);
		XSSFSheet sheet4 = workbook.getSheetAt(4);
		sheet4.setColumnWidth(3, 25 * 256);
		sheet4.setColumnWidth(4, 25 * 256);
		XSSFCellStyle style = ExcelUtils.styleText(sheet4);
		XSSFCellStyle styleTitle = ExcelUtils.styleText(sheet4);
		XSSFCellStyle styleNumber = ExcelUtils.styleCurrencyV2(sheet4);
		XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet4);
		XSSFCellStyle styleData = ExcelUtils.styleText(sheet4);
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		
		XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
		stylePercent.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));
		stylePercent.setAlignment(HorizontalAlignment.RIGHT);
		style.setAlignment(HorizontalAlignment.CENTER);
		
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);

		styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		styleNumber.setAlignment(HorizontalAlignment.RIGHT);
		styleTitle.setAlignment(HorizontalAlignment.CENTER);
		styleTitle.setFont(font);
		styleTitle.setWrapText(true);
		styleTitle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		XSSFCellStyle styleCurrency = ExcelUtils.styleText(sheet4);
		styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("###,###"));
		
		int m = 3;
		if (dataSheetBcQl != null && !dataSheetBcQl.isEmpty()) {
			for (ProgressTaskOsDTO dto : dataSheetBcQl) {
				Row row = sheet4.createRow(m++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (m - 3));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getTdQlKhThang() != null) ? dto.getTdQlKhThang() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getTdQlTrenDuongValue() != null) ? dto.getTdQlTrenDuongValue() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getTdQlTrenDuongTyle() != null) ? dto.getTdQlTrenDuongTyle()*100 : 0D);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getTdQlGdCnKyValue() != null) ? dto.getTdQlGdCnKyValue() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getTdQlGdCnKyTyLe() != null) ? dto.getTdQlGdCnKyTyLe()*100 : 0d);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getTdQlPhtThamDuyetValue() != null) ? dto.getTdQlPhtThamDuyetValue() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getTdQlPhtThamDuyetTyLe() != null) ? dto.getTdQlPhtThamDuyetTyLe()*100 : 0d);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getTdQlPhtNghiemThuValue() != null) ? dto.getTdQlPhtNghiemThuValue() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getTdQlPhtNghiemThuTyLe() != null) ? dto.getTdQlPhtNghiemThuTyLe()*100 : 0d);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getTdQlDangLamHoSoValue() != null) ? dto.getTdQlDangLamHoSoValue() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getTdQlDangLamHoSoTyLe() != null) ? dto.getTdQlDangLamHoSoTyLe()*100 : 0d);
				cell.setCellStyle(stylePercent);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getTdQlDuKienHoanThanhValue() != null) ? dto.getTdQlDuKienHoanThanhValue() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getTdQlDuKienHoanThanhTyLe() != null) ? dto.getTdQlDuKienHoanThanhTyLe()*100 : 0d);
				cell.setCellStyle(stylePercent);
			}
		}
		//----------------------Sản lượng chi phí-------------------------//
		m++;
		List<ProgressTaskOsDTO> dataSheetSlCp = progressTaskOsDAO.getDataTienDoSlCp(obj);
		if (dataSheetSlCp != null && dataSheetSlCp.size()>0) {
			Row row = sheet4.createRow(m);
			sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
			Cell cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("STT");
			cell.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue("Đơn vị thực hiện");
			cell.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 8));
			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue("Sản lượng Chi phí");
			cell.setCellStyle(styleTitle);
			m++;
			Row row2 = sheet4.createRow(m);
			sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
			Cell cell2 = row2.createCell(2, CellType.STRING);
			cell2.setCellValue("KH Tháng");
			cell2.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 3, 3));
			cell2 = row2.createCell(3, CellType.STRING);
			cell2.setCellValue("Đã hoàn thành");
			cell2.setCellStyle(styleTitle);
			
			sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 4, 4));
			cell2 = row2.createCell(4, CellType.STRING);
			cell2.setCellValue("Tỷ lệ");
			cell2.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
			cell2 = row2.createCell(5, CellType.STRING);
			cell2.setCellValue("Đang thi công");
			cell2.setCellStyle(styleTitle);
			
			sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
			cell2 = row2.createCell(7, CellType.STRING);
			cell2.setCellValue("Dự kiến HT");
			cell2.setCellStyle(styleTitle);

			m++;
			Row row3 = sheet4.createRow(m);
			Cell cell3 = row3.createCell(5, CellType.STRING);
			cell3.setCellValue("Giá trị");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(6, CellType.STRING);
			cell3.setCellValue("Tỷ lệ");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(7, CellType.STRING);
			cell3.setCellValue("Giá trị");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(8, CellType.STRING);
			cell3.setCellValue("Tỷ lệ");
			cell3.setCellStyle(styleTitle);
			m++;
			int countSlCp = 1;
			for (ProgressTaskOsDTO dto : dataSheetSlCp) {
				Row rowQl = sheet4.createRow(m++);
				Cell cellSlCp = rowQl.createCell(0, CellType.STRING);
				cellSlCp.setCellValue("" + (countSlCp++));
				cellSlCp.setCellStyle(styleNumber);
				cellSlCp = rowQl.createCell(1, CellType.STRING);
				cellSlCp.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
				cellSlCp.setCellStyle(style);
				cellSlCp = rowQl.createCell(2, CellType.STRING);
				cellSlCp.setCellValue((dto.getTdSlCpKhThang() != null) ? dto.getTdSlCpKhThang() : 0l);
				cellSlCp.setCellStyle(styleCurrency);
				cellSlCp = rowQl.createCell(3, CellType.STRING);
				cellSlCp.setCellValue((dto.getTdSlCpDaHoanThanh() != null) ? dto.getTdSlCpDaHoanThanh() : 0l);
				cellSlCp.setCellStyle(styleCurrency);
				cellSlCp = rowQl.createCell(4, CellType.STRING);
				cellSlCp.setCellValue((dto.getTdSlCpDaHoanThanhTyLe() != null) ? dto.getTdSlCpDaHoanThanhTyLe()*100 : 0d);
				cellSlCp.setCellStyle(stylePercent);
				cellSlCp = rowQl.createCell(5, CellType.STRING);
				cellSlCp.setCellValue((dto.getTdSlCpDangThiCongValue() != null) ? dto.getTdSlCpDangThiCongValue() : 0l);
				cellSlCp.setCellStyle(styleCurrency);
				cellSlCp = rowQl.createCell(6, CellType.STRING);
				cellSlCp.setCellValue((dto.getTdSlCpDangThiCongTyLe() != null) ? dto.getTdSlCpDangThiCongTyLe()*100 : 0d);
				cellSlCp.setCellStyle(stylePercent);
				cellSlCp = rowQl.createCell(7, CellType.STRING);
				cellSlCp.setCellValue((dto.getTdSlCpDuKienHoanThanhValue() != null) ? dto.getTdSlCpDuKienHoanThanhValue() : 0l);
				cellSlCp.setCellStyle(styleCurrency);
				cellSlCp = rowQl.createCell(8, CellType.STRING);
				cellSlCp.setCellValue((dto.getTdSlCpDuKienHoanThanhTyLe() != null) ? dto.getTdSlCpDuKienHoanThanhTyLe()*100 : 0d);
				cellSlCp.setCellStyle(stylePercent);
			}
			
		}
		
		m++;
		// ----------------------------HSHC Chi phí--------------------------------
		List<ProgressTaskOsDTO> dataSheetBcHshcCp = progressTaskOsDAO.getDataTienDoHshcCp(obj);
		if (dataSheetBcHshcCp != null && dataSheetBcHshcCp.size()>0) {
			m++;
			Row row = sheet4.createRow(m);
			sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
			Cell cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("STT");
			cell.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue("Đơn vị thực hiện");
			cell.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 16));
			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue("HSHC Chi phí");
			cell.setCellStyle(styleTitle);
			
			m++;
			Row row2 = sheet4.createRow(m);
			sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
			Cell cell2 = row2.createCell(2, CellType.STRING);
			cell2.setCellValue("KH Tháng");
			cell2.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m, 3, 4));
			cell2 = row2.createCell(3, CellType.STRING);
			cell2.setCellValue("HSHC đang trên đường + TCTy chưa thẩm duyệt");
			cell2.setCellStyle(styleTitle);

//			sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 5, 5));
//			cell2 = row2.createCell(5, CellType.STRING);
//			cell2.setCellValue("TTKT đã hoàn thiện hồ sơ, P.TK chưa duyệt/đang trên đường gửi");
//			cell2.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
			cell2 = row2.createCell(5, CellType.STRING);
			cell2.setCellValue("Đang trình GĐ CN ký");
			cell2.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
			cell2 = row2.createCell(7, CellType.STRING);
			cell2.setCellValue("Đang làm đối soát 4A");
			cell2.setCellStyle(styleTitle);
			
			sheet4.addMergedRegion(new CellRangeAddress(m, m, 9, 10));
			cell2 = row2.createCell(9, CellType.STRING);
			cell2.setCellValue("PHT chi nhánh đang thẩm duyệt");
			cell2.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m, 11, 12));
			cell2 = row2.createCell(11, CellType.STRING);
			cell2.setCellValue("PHT chi nhánh đang nghiệm thu");
			cell2.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m, 13, 14));
			cell2 = row2.createCell(13, CellType.STRING);
			cell2.setCellValue("TTKT đang làm hồ sơ");
			cell2.setCellStyle(styleTitle);

			sheet4.addMergedRegion(new CellRangeAddress(m, m, 15, 16));
			cell2 = row2.createCell(15, CellType.STRING);
			cell2.setCellValue("Dự kiến HT");
			cell2.setCellStyle(styleTitle);
			m++;
			Row row3 = sheet4.createRow(m);
			Cell cell3 = row3.createCell(3, CellType.STRING);
			cell3.setCellValue("Giá trị");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(4, CellType.STRING);
			cell3.setCellValue("Tỷ lệ");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(5, CellType.STRING);
			cell3.setCellValue("Giá trị");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(6, CellType.STRING);
			cell3.setCellValue("Tỷ lệ");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(7, CellType.STRING);
			cell3.setCellValue("Giá trị");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(8, CellType.STRING);
			cell3.setCellValue("Tỷ lệ");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(9, CellType.STRING);
			cell3.setCellValue("Giá trị");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(10, CellType.STRING);
			cell3.setCellValue("Tỷ lệ");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(11, CellType.STRING);
			cell3.setCellValue("Giá trị");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(12, CellType.STRING);
			cell3.setCellValue("Tỷ lệ");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(13, CellType.STRING);
			cell3.setCellValue("Giá trị");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(14, CellType.STRING);
			cell3.setCellValue("Tỷ lệ");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(15, CellType.STRING);
			cell3.setCellValue("Giá trị");
			cell3.setCellStyle(styleTitle);
			cell3 = row3.createCell(16, CellType.STRING);
			cell3.setCellValue("Tỷ lệ");
			cell3.setCellStyle(styleTitle);

			m++;
			int countHshcCp = 1; 
			for (ProgressTaskOsDTO dto : dataSheetBcHshcCp) {
				Row rowHshcCp = sheet4.createRow(m++);
				Cell cellHshcCp = rowHshcCp.createCell(0, CellType.STRING);
				cellHshcCp.setCellValue("" + (countHshcCp++));
				cellHshcCp.setCellStyle(styleNumber);
				cellHshcCp = rowHshcCp.createCell(1, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
				cellHshcCp.setCellStyle(style);
				cellHshcCp = rowHshcCp.createCell(2, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpKhThang() != null) ? dto.getTdHshcCpKhThang() : 0l);
				cellHshcCp.setCellStyle(styleCurrency);
				cellHshcCp = rowHshcCp.createCell(3, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpTrenDuongValue() != null) ? dto.getTdHshcCpTrenDuongValue() : 0l);
				cellHshcCp.setCellStyle(styleCurrency);
				cellHshcCp = rowHshcCp.createCell(4, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpTrenDuongTyLe() != null) ? dto.getTdHshcCpTrenDuongTyLe()*100 : 0D);
				cellHshcCp.setCellStyle(stylePercent);
				cellHshcCp = rowHshcCp.createCell(5, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpGdCnKyValue() != null) ? dto.getTdHshcCpGdCnKyValue() : 0l);
				cellHshcCp.setCellStyle(styleCurrency);
				cellHshcCp = rowHshcCp.createCell(6, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpGdCnKyTyLe() != null) ? dto.getTdHshcCpGdCnKyTyLe()*100 : 0d);
				cellHshcCp.setCellStyle(stylePercent);
				
				cellHshcCp = rowHshcCp.createCell(7, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpDoiSoat4aValue() != null) ? dto.getTdHshcCpDoiSoat4aValue() : 0l);
				cellHshcCp.setCellStyle(styleCurrency);
				cellHshcCp = rowHshcCp.createCell(8, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpDoiSoat4aTyLe() != null) ? dto.getTdHshcCpDoiSoat4aTyLe()*100 : 0d);
				cellHshcCp.setCellStyle(stylePercent);
				
				cellHshcCp = rowHshcCp.createCell(9, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpPhtThamDuyetValue() != null) ? dto.getTdHshcCpPhtThamDuyetValue() : 0l);
				cellHshcCp.setCellStyle(styleCurrency);
				cellHshcCp = rowHshcCp.createCell(10, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpPhtThamDuyetTyLe() != null) ? dto.getTdHshcCpPhtThamDuyetTyLe()*100 : 0d);
				cellHshcCp.setCellStyle(stylePercent);
				cellHshcCp = rowHshcCp.createCell(11, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpPhtNghiemThuValue() != null) ? dto.getTdHshcCpPhtNghiemThuValue() : 0l);
				cellHshcCp.setCellStyle(styleCurrency);
				cellHshcCp = rowHshcCp.createCell(12, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpPhtNghiemThuTyLe() != null) ? dto.getTdHshcCpPhtNghiemThuTyLe()*100 : 0d);
				cellHshcCp.setCellStyle(stylePercent);
				cellHshcCp = rowHshcCp.createCell(13, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpDangLamHoSoValue() != null) ? dto.getTdHshcCpDangLamHoSoValue() : 0l);
				cellHshcCp.setCellStyle(styleCurrency);
				cellHshcCp = rowHshcCp.createCell(14, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpDangLamHoSoTyLe() != null) ? dto.getTdHshcCpDangLamHoSoTyLe()*100 : 0d);
				cellHshcCp.setCellStyle(stylePercent);
				cellHshcCp = rowHshcCp.createCell(15, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpDuKienHoanThanhValue() != null) ? dto.getTdHshcCpDuKienHoanThanhValue() : 0l);
				cellHshcCp.setCellStyle(styleCurrency);
				cellHshcCp = rowHshcCp.createCell(16, CellType.STRING);
				cellHshcCp.setCellValue((dto.getTdHshcCpDuKienHoanThanhTyLe() != null) ? dto.getTdHshcCpDuKienHoanThanhTyLe()*100 : 0d);
				cellHshcCp.setCellStyle(stylePercent);
			}
		}

		//----------------------Sản lượng Xây lắp-------------------------//
				m++;
				m++;
				List<ProgressTaskOsDTO> dataSheetSlXl = progressTaskOsDAO.getDataTienDoSlXl(obj);
				if (dataSheetSlXl != null && dataSheetSlXl.size()>0) {
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 8));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("Sản lượng Xây lắp");
					cell.setCellStyle(styleTitle);
					m++;
					Row row2 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
					Cell cell2 = row2.createCell(2, CellType.STRING);
					cell2.setCellValue("KH Tháng");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 3, 3));
					cell2 = row2.createCell(3, CellType.STRING);
					cell2.setCellValue("Đã hoàn thành");
					cell2.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 4, 4));
					cell2 = row2.createCell(4, CellType.STRING);
					cell2.setCellValue("Tỷ lệ");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell2 = row2.createCell(5, CellType.STRING);
					cell2.setCellValue("Đang thi công");
					cell2.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell2 = row2.createCell(7, CellType.STRING);
					cell2.setCellValue("Dự kiến HT");
					cell2.setCellStyle(styleTitle);

					m++;
					Row row3 = sheet4.createRow(m);
					Cell cell3 = row3.createCell(5, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(6, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(7, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(8, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);

					m++;
					int countSlXl = 1;
					for (ProgressTaskOsDTO dto : dataSheetSlXl) {
						Row rowSlXl = sheet4.createRow(m++);
						Cell cellSlXl = rowSlXl.createCell(0, CellType.STRING);
						cellSlXl.setCellValue("" + (countSlXl++));
						cellSlXl.setCellStyle(styleNumber);
						cellSlXl = rowSlXl.createCell(1, CellType.STRING);
						cellSlXl.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellSlXl.setCellStyle(style);
						cellSlXl = rowSlXl.createCell(2, CellType.STRING);
						cellSlXl.setCellValue((dto.getTdSlXlKhThang() != null) ? dto.getTdSlXlKhThang() : 0l);
						cellSlXl.setCellStyle(styleCurrency);
						cellSlXl = rowSlXl.createCell(3, CellType.STRING);
						cellSlXl.setCellValue((dto.getTdSlXlDaHoanThanh() != null) ? dto.getTdSlXlDaHoanThanh() : 0l);
						cellSlXl.setCellStyle(styleCurrency);
						cellSlXl = rowSlXl.createCell(4, CellType.STRING);
						cellSlXl.setCellValue((dto.getTdSlXlDaHoanThanhTyLe() != null) ? dto.getTdSlXlDaHoanThanhTyLe()*100 : 0l);
						cellSlXl.setCellStyle(stylePercent);
						cellSlXl = rowSlXl.createCell(5, CellType.STRING);
						cellSlXl.setCellValue((dto.getTdSlXlDangThiCongValue() != null) ? dto.getTdSlXlDangThiCongValue() : 0l);
						cellSlXl.setCellStyle(styleCurrency);
						cellSlXl = rowSlXl.createCell(6, CellType.STRING);
						cellSlXl.setCellValue((dto.getTdSlXlDangThiCongTyLe() != null) ? dto.getTdSlXlDangThiCongTyLe()*100 : 0d);
						cellSlXl.setCellStyle(stylePercent);
						cellSlXl = rowSlXl.createCell(7, CellType.STRING);
						cellSlXl.setCellValue((dto.getTdSlXlDuKienHoanThanhValue() != null) ? dto.getTdSlXlDuKienHoanThanhValue() : 0l);
						cellSlXl.setCellStyle(styleCurrency);
						cellSlXl = rowSlXl.createCell(8, CellType.STRING);
						cellSlXl.setCellValue((dto.getTdSlXlDuKienHoanThanhTyLe() != null) ? dto.getTdSlXlDuKienHoanThanhTyLe()*100 : 0d);
						cellSlXl.setCellStyle(stylePercent);
					}
				}
		
				// ----------------------------HSHC Xây lắp--------------------------------
				m++;
				List<ProgressTaskOsDTO> dataSheetBcHshcXl = progressTaskOsDAO.getDataTienDoHshcXl(obj);
				if (dataSheetBcHshcXl != null && dataSheetBcHshcXl.size()>0) {
					m++;
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 16));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("HSHC Xây lắp");
					cell.setCellStyle(styleTitle);
					
					m++;
					Row row2 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
					Cell cell2 = row2.createCell(2, CellType.STRING);
					cell2.setCellValue("KH Tháng");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 3, 4));
					cell2 = row2.createCell(3, CellType.STRING);
					cell2.setCellValue("HSHC đang trên đường + TCTy chưa thẩm duyệt");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell2 = row2.createCell(5, CellType.STRING);
					cell2.setCellValue("Đang trình GĐ CN ký");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell2 = row2.createCell(7, CellType.STRING);
					cell2.setCellValue("Đang làm đối soát 4A");
					cell2.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 9, 10));
					cell2 = row2.createCell(9, CellType.STRING);
					cell2.setCellValue("PHT chi nhánh đang thẩm duyệt");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 11, 12));
					cell2 = row2.createCell(11, CellType.STRING);
					cell2.setCellValue("PHT chi nhánh đang nghiệm thu");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 13, 14));
					cell2 = row2.createCell(13, CellType.STRING);
					cell2.setCellValue("TTKT đang làm hồ sơ");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 15, 16));
					cell2 = row2.createCell(15, CellType.STRING);
					cell2.setCellValue("Dự kiến HT");
					cell2.setCellStyle(styleTitle);
					m++;
					Row row3 = sheet4.createRow(m);
					Cell cell3 = row3.createCell(3, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(4, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(5, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(6, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(7, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(8, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(9, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(10, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(11, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(12, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(13, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(14, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(15, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(16, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);

					m++;
					int countHshcXl = 1; 
					for (ProgressTaskOsDTO dto : dataSheetBcHshcXl) {
						Row rowHshcXl = sheet4.createRow(m++);
						Cell cellHshcXl = rowHshcXl.createCell(0, CellType.STRING);
						cellHshcXl.setCellValue("" + (countHshcXl++));
						cellHshcXl.setCellStyle(styleNumber);
						cellHshcXl = rowHshcXl.createCell(1, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellHshcXl.setCellStyle(style);
						cellHshcXl = rowHshcXl.createCell(2, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlKhThang() != null) ? dto.getTdHshcXlKhThang() : 0l);
						cellHshcXl.setCellStyle(styleCurrency);
						cellHshcXl = rowHshcXl.createCell(3, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlTrenDuongValue() != null) ? dto.getTdHshcXlTrenDuongValue() : 0l);
						cellHshcXl.setCellStyle(styleCurrency);
						cellHshcXl = rowHshcXl.createCell(4, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlTrenDuongTyLe() != null) ? dto.getTdHshcXlTrenDuongTyLe()*100 : 0D);
						cellHshcXl.setCellStyle(stylePercent);
						cellHshcXl = rowHshcXl.createCell(5, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlGdCnKyValue() != null) ? dto.getTdHshcXlGdCnKyValue() : 0l);
						cellHshcXl.setCellStyle(styleCurrency);
						cellHshcXl = rowHshcXl.createCell(6, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlGdCnKyTyLe() != null) ? dto.getTdHshcXlGdCnKyTyLe()*100 : 0d);
						cellHshcXl.setCellStyle(stylePercent);
						cellHshcXl = rowHshcXl.createCell(7, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlDoiSoat4aValue() != null) ? dto.getTdHshcXlDoiSoat4aValue() : 0l);
						cellHshcXl.setCellStyle(styleCurrency);
						cellHshcXl = rowHshcXl.createCell(8, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlDoiSoat4aTyLe() != null) ? dto.getTdHshcXlDoiSoat4aTyLe()*100 : 0d);
						cellHshcXl.setCellStyle(stylePercent);
						cellHshcXl = rowHshcXl.createCell(9, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlPhtThamDuyetValue() != null) ? dto.getTdHshcXlPhtThamDuyetValue() : 0l);
						cellHshcXl.setCellStyle(styleCurrency);
						cellHshcXl = rowHshcXl.createCell(10, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlPhtThamDuyetTyLe() != null) ? dto.getTdHshcXlPhtThamDuyetTyLe()*100 : 0d);
						cellHshcXl.setCellStyle(stylePercent);
						cellHshcXl = rowHshcXl.createCell(11, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlPhtNghiemThuValue() != null) ? dto.getTdHshcXlPhtNghiemThuValue() : 0l);
						cellHshcXl.setCellStyle(styleCurrency);
						cellHshcXl = rowHshcXl.createCell(12, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlPhtNghiemThuTyLe() != null) ? dto.getTdHshcXlPhtNghiemThuTyLe()*100 : 0d);
						cellHshcXl.setCellStyle(stylePercent);
						cellHshcXl = rowHshcXl.createCell(13, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlDangLamHoSoValue() != null) ? dto.getTdHshcXlDangLamHoSoValue() : 0l);
						cellHshcXl.setCellStyle(styleCurrency);
						cellHshcXl = rowHshcXl.createCell(14, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlDangLamHoSoTyLe() != null) ? dto.getTdHshcXlDangLamHoSoTyLe()*100 : 0d);
						cellHshcXl.setCellStyle(stylePercent);
						cellHshcXl = rowHshcXl.createCell(15, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlDuKienHoanThanhValue() != null) ? dto.getTdHshcXlDuKienHoanThanhValue() : 0l);
						cellHshcXl.setCellStyle(styleCurrency);
						cellHshcXl = rowHshcXl.createCell(16, CellType.STRING);
						cellHshcXl.setCellValue((dto.getTdHshcXlDuKienHoanThanhTyLe() != null) ? dto.getTdHshcXlDuKienHoanThanhTyLe()*100 : 0d);
						cellHshcXl.setCellStyle(stylePercent);
					}
				}
				
				//----------------------Tiến độ triển khai HTCT-------------------------//
				m++;
				m++;
				List<ProgressTaskOsDTO> dataSheetHtct = progressTaskOsDAO.getDataTienDoHtct(obj);
				if (dataSheetHtct != null && dataSheetHtct.size()>0) {
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 3, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 3, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 15));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("Tiến độ triển khai hạ tầng cho thuê (trạm)");
					cell.setCellStyle(styleTitle);
					
					m++;
					Row row2 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 8));
					Cell cell2 = row2.createCell(2, CellType.STRING);
					cell2.setCellValue("Xây dựng móng");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 9, 15));
					cell2 = row2.createCell(9, CellType.STRING);
					cell2.setCellValue("Hoàn thiện");
					cell2.setCellStyle(styleTitle);
					
					//------Xây dựng móng-----
					m++;
					Row row4 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
					Cell cell4 = row4.createCell(2, CellType.STRING);
					cell4.setCellValue("KH tháng");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 3, 3));
					cell4 = row4.createCell(3, CellType.STRING);
					cell4.setCellValue("Đã hoàn thành");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 4, 4));
					cell4 = row4.createCell(4, CellType.STRING);
					cell4.setCellValue("Tỷ lệ");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell4 = row4.createCell(5, CellType.STRING);
					cell4.setCellValue("Đang thi công");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell4 = row4.createCell(7, CellType.STRING);
					cell4.setCellValue("Dự kiến hoàn thành");
					cell4.setCellStyle(styleTitle);

					//--------Hoàn thiện---------
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 9, 9));
					cell4 = row4.createCell(9, CellType.STRING);
					cell4.setCellValue("KH tháng");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 10, 10));
					cell4 = row4.createCell(10, CellType.STRING);
					cell4.setCellValue("Đã hoàn thành");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 11, 11));
					cell4 = row4.createCell(11, CellType.STRING);
					cell4.setCellValue("Tỷ lệ");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 12, 13));
					cell4 = row4.createCell(12, CellType.STRING);
					cell4.setCellValue("Đang thi công");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 14, 15));
					cell4 = row4.createCell(14, CellType.STRING);
					cell4.setCellValue("Dự kiến hoàn thành");
					cell4.setCellStyle(styleTitle);
					
					m++;
					Row row3 = sheet4.createRow(m);
					Cell cell3 = row3.createCell(5, CellType.STRING);
					cell3.setCellValue("Số trạm");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(6, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(7, CellType.STRING);
					cell3.setCellValue("Số trạm");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(8, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(12, CellType.STRING);
					cell3.setCellValue("Số trạm");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(13, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(14, CellType.STRING);
					cell3.setCellValue("Số trạm");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(15, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);

					m++;
					int countHtct = 1; 
					for (ProgressTaskOsDTO dto : dataSheetHtct) {
						Row rowHtct = sheet4.createRow(m++);
						Cell cellHtct = rowHtct.createCell(0, CellType.STRING);
						cellHtct.setCellValue("" + (countHtct++));
						cellHtct.setCellStyle(styleNumber);
						cellHtct = rowHtct.createCell(1, CellType.STRING);
						cellHtct.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellHtct.setCellStyle(style);
						cellHtct = rowHtct.createCell(2, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctXdmKhThang() != null) ? dto.getTdHtctXdmKhThang() : 0l);
						cellHtct.setCellStyle(styleCurrency);
						cellHtct = rowHtct.createCell(3, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctXdmDaHt() != null) ? dto.getTdHtctXdmDaHt() : 0l);
						cellHtct.setCellStyle(styleCurrency);
						cellHtct = rowHtct.createCell(4, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctXdmTyLe() != null) ? dto.getTdHtctXdmTyLe()*100 : 0D);
						cellHtct.setCellStyle(stylePercent);
						cellHtct = rowHtct.createCell(5, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctXdmDangTc() != null) ? dto.getTdHtctXdmDangTc() : 0l);
						cellHtct.setCellStyle(styleCurrency);
						cellHtct = rowHtct.createCell(6, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctXdmDangTcTyLe() != null) ? dto.getTdHtctXdmDangTcTyLe()*100 : 0D);
						cellHtct.setCellStyle(stylePercent);
						cellHtct = rowHtct.createCell(7, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctXdmDuKienHt() != null) ? dto.getTdHtctXdmDuKienHt() : 0l);
						cellHtct.setCellStyle(styleCurrency);
						cellHtct = rowHtct.createCell(8, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctXdmDuKienHtTyLe() != null) ? dto.getTdHtctXdmDuKienHtTyLe()*100 : 0D);
						cellHtct.setCellStyle(stylePercent);
						cellHtct = rowHtct.createCell(9, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctHtKhThang() != null) ? dto.getTdHtctHtKhThang() : 0l);
						cellHtct.setCellStyle(styleCurrency);
						cellHtct = rowHtct.createCell(10, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctHtDaHt() != null) ? dto.getTdHtctHtDaHt() : 0l);
						cellHtct.setCellStyle(styleCurrency);
						cellHtct = rowHtct.createCell(11, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctHtTyLe() != null) ? dto.getTdHtctHtTyLe()*100 : 0D);
						cellHtct.setCellStyle(stylePercent);
						cellHtct = rowHtct.createCell(12, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctHtDangTc() != null) ? dto.getTdHtctHtDangTc() : 0l);
						cellHtct.setCellStyle(styleCurrency);
						cellHtct = rowHtct.createCell(13, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctHtDangTcTyLe() != null) ? dto.getTdHtctHtDangTcTyLe()*100 : 0d);
						cellHtct.setCellStyle(stylePercent);
						rowHtct.createCell(14, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctHtDuKienHt() != null) ? dto.getTdHtctHtDuKienHt() : 0l);
						cellHtct.setCellStyle(styleCurrency);
						rowHtct.createCell(15, CellType.STRING);
						cellHtct.setCellValue((dto.getTdHtctHtDuKienHtTyLe() != null) ? dto.getTdHtctHtDuKienHtTyLe()*100 : 0d);
						cellHtct.setCellStyle(stylePercent);
					}
				}
				
				//------------------------------Sản lượng HTCT----------------------------
				m++;
				m++;
				List<ProgressTaskOsDTO> dataSheetSlHtct = progressTaskOsDAO.getDataTienDoSlHtct(obj);
				if (dataSheetSlHtct != null && dataSheetSlHtct.size()>0) {
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 8));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("Sản lượng Hạ tầng cho thuê");
					cell.setCellStyle(styleTitle);
					
					m++;
					Row row4 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
					Cell cell4 = row4.createCell(2, CellType.STRING);
					cell4.setCellValue("KH tháng");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 3, 3));
					cell4 = row4.createCell(3, CellType.STRING);
					cell4.setCellValue("Đã hoàn thành");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 4, 4));
					cell4 = row4.createCell(4, CellType.STRING);
					cell4.setCellValue("Tỷ lệ");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell4 = row4.createCell(5, CellType.STRING);
					cell4.setCellValue("Đang thi công");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell4 = row4.createCell(7, CellType.STRING);
					cell4.setCellValue("Dự kiến hoàn thành");
					cell4.setCellStyle(styleTitle);

					m++;
					Row row3 = sheet4.createRow(m);
					Cell cell3 = row3.createCell(5, CellType.STRING);
					cell3.setCellValue("Số trạm");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(6, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(7, CellType.STRING);
					cell3.setCellValue("Số trạm");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(8, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					
					m++;
					int countSlHtct = 1; 
					for (ProgressTaskOsDTO dto : dataSheetSlHtct) {
						Row rowSlHtct = sheet4.createRow(m++);
						Cell cellSlHtct = rowSlHtct.createCell(0, CellType.STRING);
						cellSlHtct.setCellValue("" + (countSlHtct++));
						cellSlHtct.setCellStyle(styleNumber);
						cellSlHtct = rowSlHtct.createCell(1, CellType.STRING);
						cellSlHtct.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellSlHtct.setCellStyle(style);
						cellSlHtct = rowSlHtct.createCell(2, CellType.STRING);
						cellSlHtct.setCellValue((dto.getTdSlHtctKhThang() != null) ? dto.getTdSlHtctKhThang() : 0l);
						cellSlHtct.setCellStyle(styleCurrency);
						cellSlHtct = rowSlHtct.createCell(3, CellType.STRING);
						cellSlHtct.setCellValue((dto.getTdSlHtctDaHt() != null) ? dto.getTdSlHtctDaHt() : 0l);
						cellSlHtct.setCellStyle(styleCurrency);
						cellSlHtct = rowSlHtct.createCell(4, CellType.STRING);
						cellSlHtct.setCellValue((dto.getTdSlHtctDaHtTyLe() != null) ? dto.getTdSlHtctDaHtTyLe()*100 : 0D);
						cellSlHtct.setCellStyle(stylePercent);
						cellSlHtct = rowSlHtct.createCell(5, CellType.STRING);
						cellSlHtct.setCellValue((dto.getTdSlHtctDangTcValue() != null) ? dto.getTdSlHtctDangTcValue() : 0l);
						cellSlHtct.setCellStyle(styleCurrency);
						cellSlHtct = rowSlHtct.createCell(6, CellType.STRING);
						cellSlHtct.setCellValue((dto.getTdSlHtctDangTcTyLe() != null) ? dto.getTdSlHtctDangTcTyLe()*100 : 0D);
						cellSlHtct.setCellStyle(stylePercent);
						cellSlHtct = rowSlHtct.createCell(7, CellType.STRING);
						cellSlHtct.setCellValue((dto.getTdSlHtctDuKienHtValue() != null) ? dto.getTdSlHtctDuKienHtValue() : 0l);
						cellSlHtct.setCellStyle(styleCurrency);
						cellSlHtct = rowSlHtct.createCell(8, CellType.STRING);
						cellSlHtct.setCellValue((dto.getTdSlHtctDuKienHtTyLe() != null) ? dto.getTdSlHtctDuKienHtTyLe()*100 : 0D);
						cellSlHtct.setCellStyle(stylePercent);
					}
				}
				
				//------------------HSHC HTCT------------------------
				m++;
				List<ProgressTaskOsDTO> dataSheetBcHshcHtct = progressTaskOsDAO.getDataTienDoHshcHtct(obj);
				if (dataSheetBcHshcHtct != null && dataSheetBcHshcHtct.size()>0) {
					m++;
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 16));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("HSHC Hạ tầng cho thuê");
					cell.setCellStyle(styleTitle);
					
					m++;
					Row row2 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
					Cell cell2 = row2.createCell(2, CellType.STRING);
					cell2.setCellValue("KH Tháng");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 3, 4));
					cell2 = row2.createCell(3, CellType.STRING);
					cell2.setCellValue("HSHC đang trên đường + TCTy chưa thẩm duyệt");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell2 = row2.createCell(5, CellType.STRING);
					cell2.setCellValue("Đang trình GĐ CN ký");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell2 = row2.createCell(7, CellType.STRING);
					cell2.setCellValue("Đang làm đối soát 4A");
					cell2.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 9, 10));
					cell2 = row2.createCell(9, CellType.STRING);
					cell2.setCellValue("PHT chi nhánh đang thẩm duyệt");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 11, 12));
					cell2 = row2.createCell(11, CellType.STRING);
					cell2.setCellValue("PHT chi nhánh đang nghiệm thu");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 13, 14));
					cell2 = row2.createCell(13, CellType.STRING);
					cell2.setCellValue("TTKT đang làm hồ sơ");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 15, 16));
					cell2 = row2.createCell(15, CellType.STRING);
					cell2.setCellValue("Dự kiến HT");
					cell2.setCellStyle(styleTitle);
					m++;
					Row row3 = sheet4.createRow(m);
					Cell cell3 = row3.createCell(3, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(4, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(5, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(6, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(7, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(8, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(9, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(10, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(11, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(12, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(13, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(14, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(15, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(16, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);

					m++;
					int countHshcHtct = 1; 
					for (ProgressTaskOsDTO dto : dataSheetBcHshcHtct) {
						Row rowHshcHtct = sheet4.createRow(m++);
						Cell cellHshcHtct = rowHshcHtct.createCell(0, CellType.STRING);
						cellHshcHtct.setCellValue("" + (countHshcHtct++));
						cellHshcHtct.setCellStyle(styleNumber);
						cellHshcHtct = rowHshcHtct.createCell(1, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellHshcHtct.setCellStyle(style);
						cellHshcHtct = rowHshcHtct.createCell(2, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctKhThang() != null) ? dto.getTdHshcHtctKhThang() : 0l);
						cellHshcHtct.setCellStyle(styleCurrency);
						cellHshcHtct = rowHshcHtct.createCell(3, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctTrenDuongValue() != null) ? dto.getTdHshcHtctTrenDuongValue() : 0l);
						cellHshcHtct.setCellStyle(styleCurrency);
						cellHshcHtct = rowHshcHtct.createCell(4, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctTrenDuongTyLe() != null) ? dto.getTdHshcHtctTrenDuongTyLe()*100 : 0D);
						cellHshcHtct.setCellStyle(stylePercent);
						cellHshcHtct = rowHshcHtct.createCell(5, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctGdCnKyValue() != null) ? dto.getTdHshcHtctGdCnKyValue() : 0l);
						cellHshcHtct.setCellStyle(styleCurrency);
						cellHshcHtct = rowHshcHtct.createCell(6, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctGdCnKyTyLe() != null) ? dto.getTdHshcHtctGdCnKyTyLe()*100 : 0d);
						cellHshcHtct.setCellStyle(stylePercent);
						
						cellHshcHtct = rowHshcHtct.createCell(7, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctDoiSoat4aValue() != null) ? dto.getTdHshcHtctDoiSoat4aValue() : 0l);
						cellHshcHtct.setCellStyle(styleCurrency);
						cellHshcHtct = rowHshcHtct.createCell(8, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctDoiSoat4aTyLe() != null) ? dto.getTdHshcHtctDoiSoat4aTyLe()*100 : 0d);
						cellHshcHtct.setCellStyle(stylePercent);
						
						cellHshcHtct = rowHshcHtct.createCell(9, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctPhtThamDuyetValue() != null) ? dto.getTdHshcHtctPhtThamDuyetValue() : 0l);
						cellHshcHtct.setCellStyle(styleCurrency);
						cellHshcHtct = rowHshcHtct.createCell(10, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctPhtThamDuyetTyLe() != null) ? dto.getTdHshcHtctPhtThamDuyetTyLe()*100 : 0d);
						cellHshcHtct.setCellStyle(stylePercent);
						cellHshcHtct = rowHshcHtct.createCell(11, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctPhtNghiemThuValue() != null) ? dto.getTdHshcHtctPhtNghiemThuValue() : 0l);
						cellHshcHtct.setCellStyle(styleCurrency);
						cellHshcHtct = rowHshcHtct.createCell(12, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctPhtNghiemThuTyLe() != null) ? dto.getTdHshcHtctPhtNghiemThuTyLe()*100 : 0d);
						cellHshcHtct.setCellStyle(stylePercent);
						cellHshcHtct = rowHshcHtct.createCell(13, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctTtktHoSoValue() != null) ? dto.getTdHshcHtctTtktHoSoValue() : 0l);
						cellHshcHtct.setCellStyle(styleCurrency);
						cellHshcHtct = rowHshcHtct.createCell(14, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctTtktHoSoTyLe() != null) ? dto.getTdHshcHtctTtktHoSoTyLe()*100 : 0d);
						cellHshcHtct.setCellStyle(stylePercent);
						cellHshcHtct = rowHshcHtct.createCell(15, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctDuKienHtValue() != null) ? dto.getTdHshcHtctDuKienHtValue() : 0l);
						cellHshcHtct.setCellStyle(styleCurrency);
						cellHshcHtct = rowHshcHtct.createCell(16, CellType.STRING);
						cellHshcHtct.setCellValue((dto.getTdHshcHtctDuKienHtTyLe() != null) ? dto.getTdHshcHtctDuKienHtTyLe()*100 : 0d);
						cellHshcHtct.setCellStyle(stylePercent);
					}
				}
				
				//-------------------Thu hồi dòng tiền--------------------------
				m++;
				m++;
				List<ProgressTaskOsDTO> dataSheetThdt = progressTaskOsDAO.getDataTienDoThdt(obj);
				if (dataSheetThdt != null && dataSheetThdt.size()>0) {
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 8));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("Thu hồi dòng tiền");
					cell.setCellStyle(styleTitle);
					
					m++;
					Row row4 = sheet4.createRow(m);
//					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 2));
					Cell cell4 = row4.createCell(2, CellType.STRING);
					cell4.setCellValue("KH tháng");
					cell4.setCellStyle(styleTitle);
					
//					sheet4.addMergedRegion(new CellRangeAddress(m, m, 3, 3));
					cell4 = row4.createCell(3, CellType.STRING);
					cell4.setCellValue("Đã hoàn thành");
					cell4.setCellStyle(styleTitle);
					
//					sheet4.addMergedRegion(new CellRangeAddress(m, m, 4, 4));
					cell4 = row4.createCell(4, CellType.STRING);
					cell4.setCellValue("Tỷ lệ");
					cell4.setCellStyle(styleTitle);
					
//					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell4 = row4.createCell(5, CellType.STRING);
					cell4.setCellValue("P.KTHT đang kiểm tra");
					cell4.setCellStyle(styleTitle);
					
//					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell4 = row4.createCell(6, CellType.STRING);
					cell4.setCellValue("T.TC đang kiểm tra");
					cell4.setCellStyle(styleTitle);
					
					cell4 = row4.createCell(7, CellType.STRING);
					cell4.setCellValue("Dự kiến hoàn thành");
					cell4.setCellStyle(styleTitle);
					
					cell4 = row4.createCell(8, CellType.STRING);
					cell4.setCellValue("Tỷ lệ");
					cell4.setCellStyle(styleTitle);
					
					m++;
					int countThdt = 1; 
					for (ProgressTaskOsDTO dto : dataSheetThdt) {
						Row rowThdt = sheet4.createRow(m++);
						Cell cellThdt = rowThdt.createCell(0, CellType.STRING);
						cellThdt.setCellValue("" + (countThdt++));
						cellThdt.setCellStyle(styleNumber);
						cellThdt = rowThdt.createCell(1, CellType.STRING);
						cellThdt.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellThdt.setCellStyle(style);
						cellThdt = rowThdt.createCell(2, CellType.STRING);
						cellThdt.setCellValue((dto.getTdThdtKhThang() != null) ? dto.getTdThdtKhThang() : 0l);
						cellThdt.setCellStyle(styleCurrency);
						cellThdt = rowThdt.createCell(3, CellType.STRING);
						cellThdt.setCellValue((dto.getTdThdtDaHoanThanh() != null) ? dto.getTdThdtDaHoanThanh() : 0l);
						cellThdt.setCellStyle(styleCurrency);
						cellThdt = rowThdt.createCell(4, CellType.STRING);
						cellThdt.setCellValue((dto.getTdThdtDaHoanThanhTyLe() != null) ? dto.getTdThdtDaHoanThanhTyLe()*100 : 0D);
						cellThdt.setCellStyle(stylePercent);
						cellThdt = rowThdt.createCell(5, CellType.STRING);
						cellThdt.setCellValue((dto.getTdThdtPhtDangKiemTraValue() != null) ? dto.getTdThdtPhtDangKiemTraValue() : 0l);
						cellThdt.setCellStyle(styleCurrency);
						cellThdt = rowThdt.createCell(6, CellType.STRING);
						cellThdt.setCellValue((dto.getTdThdtPtcDangKiemTraValue() != null) ? dto.getTdThdtPtcDangKiemTraValue() : 0l);
						cellThdt.setCellStyle(stylePercent);
						cellThdt = rowThdt.createCell(7, CellType.STRING);
						cellThdt.setCellValue((dto.getTdThdtDuKienHoanThanhValue() != null) ? dto.getTdThdtDuKienHoanThanhValue() : 0l);
						cellThdt.setCellStyle(styleCurrency);
						cellThdt = rowThdt.createCell(8, CellType.STRING);
						cellThdt.setCellValue((dto.getTdThdtDuKienHoanThanhTyLe() != null) ? dto.getTdThdtDuKienHoanThanhTyLe()*100 : 0D);
						cellThdt.setCellStyle(stylePercent);
					}
				}
				
				//-------------------Sản lượng ngoài tập đoàn------------------------
				m++;
				m++;
				List<ProgressTaskOsDTO> dataSheetSlNtd = progressTaskOsDAO.getDataTienDoSlNtd(obj);
				if (dataSheetSlNtd != null && dataSheetSlNtd.size()>0) {
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 8));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("Sản lượng Ngoài Tập đoàn");
					cell.setCellStyle(styleTitle);
					
					m++;
					Row row4 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
					Cell cell4 = row4.createCell(2, CellType.STRING);
					cell4.setCellValue("KH tháng");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 3, 3));
					cell4 = row4.createCell(3, CellType.STRING);
					cell4.setCellValue("Đã hoàn thành");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 4, 4));
					cell4 = row4.createCell(4, CellType.STRING);
					cell4.setCellValue("Tỷ lệ");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell4 = row4.createCell(5, CellType.STRING);
					cell4.setCellValue("Đang thi công");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell4 = row4.createCell(7, CellType.STRING);
					cell4.setCellValue("Dự kiến hoàn thành");
					cell4.setCellStyle(styleTitle);

					m++;
					Row row3 = sheet4.createRow(m);
					Cell cell3 = row3.createCell(5, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(6, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(7, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(8, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					
					m++;
					int countSlNtd = 1; 
					for (ProgressTaskOsDTO dto : dataSheetSlNtd) {
						Row rowSlNtd = sheet4.createRow(m++);
						Cell cellSlNtd = rowSlNtd.createCell(0, CellType.STRING);
						cellSlNtd.setCellValue("" + (countSlNtd++));
						cellSlNtd.setCellStyle(styleNumber);
						cellSlNtd = rowSlNtd.createCell(1, CellType.STRING);
						cellSlNtd.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellSlNtd.setCellStyle(style);
						cellSlNtd = rowSlNtd.createCell(2, CellType.STRING);
						cellSlNtd.setCellValue((dto.getTdSlNtdKhThang() != null) ? dto.getTdSlNtdKhThang() : 0l);
						cellSlNtd.setCellStyle(styleCurrency);
						cellSlNtd = rowSlNtd.createCell(3, CellType.STRING);
						cellSlNtd.setCellValue((dto.getTdSlNtdDaHoanThanh() != null) ? dto.getTdSlNtdDaHoanThanh() : 0l);
						cellSlNtd.setCellStyle(styleCurrency);
						cellSlNtd = rowSlNtd.createCell(4, CellType.STRING);
						cellSlNtd.setCellValue((dto.getTdSlNtdDaHoanThanhTyLe() != null) ? dto.getTdSlNtdDaHoanThanhTyLe()*100 : 0D);
						cellSlNtd.setCellStyle(stylePercent);
						cellSlNtd = rowSlNtd.createCell(5, CellType.STRING);
						cellSlNtd.setCellValue((dto.getTdSlNtdDangThiCongValue() != null) ? dto.getTdSlNtdDangThiCongValue() : 0l);
						cellSlNtd.setCellStyle(styleCurrency);
						cellSlNtd = rowSlNtd.createCell(6, CellType.STRING);
						cellSlNtd.setCellValue((dto.getTdSlNtdDangThiCongTyLe() != null) ? dto.getTdSlNtdDangThiCongTyLe()*100 : 0D);
						cellSlNtd.setCellStyle(stylePercent);
						cellSlNtd = rowSlNtd.createCell(7, CellType.STRING);
						cellSlNtd.setCellValue((dto.getTdSlNtdDuKienHoanThanhValue() != null) ? dto.getTdSlNtdDuKienHoanThanhValue() : 0l);
						cellSlNtd.setCellStyle(styleCurrency);
						cellSlNtd = rowSlNtd.createCell(8, CellType.STRING);
						cellSlNtd.setCellValue((dto.getTdSlNtdDuKienHoanThanhTyLe() != null) ? dto.getTdSlNtdDuKienHoanThanhTyLe()*100 : 0D);
						cellSlNtd.setCellStyle(stylePercent);
					}
				}
				
				//--------------------------HSHC Ngoài tập đoàn--------------------------
				m++;
				List<ProgressTaskOsDTO> dataSheetBcHshcNtd = progressTaskOsDAO.getDataTienDoHshcNtd(obj);
				if (dataSheetBcHshcNtd != null && dataSheetBcHshcNtd.size()>0) {
					m++;
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 16));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("HSHC Ngoài tập đoàn");
					cell.setCellStyle(styleTitle);
					
					m++;
					Row row2 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
					Cell cell2 = row2.createCell(2, CellType.STRING);
					cell2.setCellValue("KH Tháng");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 3, 4));
					cell2 = row2.createCell(3, CellType.STRING);
					cell2.setCellValue("HSHC đang trên đường + TCTy chưa thẩm duyệt");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell2 = row2.createCell(5, CellType.STRING);
					cell2.setCellValue("Đang trình GĐ CN ký");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell2 = row2.createCell(7, CellType.STRING);
					cell2.setCellValue("Đang làm đối soát 4A");
					cell2.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 9, 10));
					cell2 = row2.createCell(9, CellType.STRING);
					cell2.setCellValue("PHT chi nhánh đang thẩm duyệt");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 11, 12));
					cell2 = row2.createCell(11, CellType.STRING);
					cell2.setCellValue("PHT chi nhánh đang nghiệm thu");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 13, 14));
					cell2 = row2.createCell(13, CellType.STRING);
					cell2.setCellValue("TTKT đang làm hồ sơ");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 15, 16));
					cell2 = row2.createCell(15, CellType.STRING);
					cell2.setCellValue("Dự kiến HT");
					cell2.setCellStyle(styleTitle);
					m++;
					Row row3 = sheet4.createRow(m);
					Cell cell3 = row3.createCell(3, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(4, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(5, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(6, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(7, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(8, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(9, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(10, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(11, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(12, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(13, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(14, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(15, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(16, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);

					m++;
					int countHshcNtd = 1; 
					for (ProgressTaskOsDTO dto : dataSheetBcHshcNtd) {
						Row rowHshcNtd = sheet4.createRow(m++);
						Cell cellHshcNtd = rowHshcNtd.createCell(0, CellType.STRING);
						cellHshcNtd.setCellValue("" + (countHshcNtd++));
						cellHshcNtd.setCellStyle(styleNumber);
						cellHshcNtd = rowHshcNtd.createCell(1, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellHshcNtd.setCellStyle(style);
						cellHshcNtd = rowHshcNtd.createCell(2, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdKhThang() != null) ? dto.getTdHshcNtdKhThang() : 0l);
						cellHshcNtd.setCellStyle(styleCurrency);
						cellHshcNtd = rowHshcNtd.createCell(3, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdTrenDuongValue() != null) ? dto.getTdHshcNtdTrenDuongValue() : 0l);
						cellHshcNtd.setCellStyle(styleCurrency);
						cellHshcNtd = rowHshcNtd.createCell(4, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdTrenDuongTyLe() != null) ? dto.getTdHshcNtdTrenDuongTyLe()*100 : 0D);
						cellHshcNtd.setCellStyle(stylePercent);
						cellHshcNtd = rowHshcNtd.createCell(5, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdGdCnKyValue() != null) ? dto.getTdHshcNtdGdCnKyValue() : 0l);
						cellHshcNtd.setCellStyle(styleCurrency);
						cellHshcNtd = rowHshcNtd.createCell(6, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdGdCnKyTyLe() != null) ? dto.getTdHshcNtdGdCnKyTyLe()*100 : 0d);
						cellHshcNtd.setCellStyle(stylePercent);
						cellHshcNtd = rowHshcNtd.createCell(7, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdDoiSoat4aValue() != null) ? dto.getTdHshcNtdDoiSoat4aValue() : 0l);
						cellHshcNtd.setCellStyle(style);
						cellHshcNtd = rowHshcNtd.createCell(8, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdDoiSoat4aTyLe() != null) ? dto.getTdHshcNtdDoiSoat4aTyLe()*100 : 0d);
						cellHshcNtd.setCellStyle(stylePercent);
						cellHshcNtd = rowHshcNtd.createCell(9, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdPhtThamDuyetValue() != null) ? dto.getTdHshcNtdPhtThamDuyetValue() : 0l);
						cellHshcNtd.setCellStyle(style);
						cellHshcNtd = rowHshcNtd.createCell(10, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdPhtThamDuyetTyLe() != null) ? dto.getTdHshcNtdPhtThamDuyetTyLe()*100 : 0d);
						cellHshcNtd.setCellStyle(stylePercent);
						cellHshcNtd = rowHshcNtd.createCell(11, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdPhtNghiemThuValue() != null) ? dto.getTdHshcNtdPhtNghiemThuValue() : 0l);
						cellHshcNtd.setCellStyle(styleCurrency);
						cellHshcNtd = rowHshcNtd.createCell(12, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdPhtNghiemThuTyLe() != null) ? dto.getTdHshcNtdPhtNghiemThuTyLe()*100 : 0d);
						cellHshcNtd.setCellStyle(stylePercent);
						cellHshcNtd = rowHshcNtd.createCell(13, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdDangLamHoSoValue() != null) ? dto.getTdHshcNtdDangLamHoSoValue() : 0l);
						cellHshcNtd.setCellStyle(styleCurrency);
						cellHshcNtd = rowHshcNtd.createCell(14, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdDangLamHoSoTyLe() != null) ? dto.getTdHshcNtdDangLamHoSoTyLe()*100 : 0d);
						cellHshcNtd.setCellStyle(stylePercent);
						cellHshcNtd = rowHshcNtd.createCell(15, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdDuKienHoanThanhValue() != null) ? dto.getTdHshcNtdDuKienHoanThanhValue() : 0l);
						cellHshcNtd.setCellStyle(styleCurrency);
						cellHshcNtd = rowHshcNtd.createCell(16, CellType.STRING);
						cellHshcNtd.setCellValue((dto.getTdHshcNtdDuKienHoanThanhTyLe() != null) ? dto.getTdHshcNtdDuKienHoanThanhTyLe()*100 : 0d);
						cellHshcNtd.setCellStyle(stylePercent);
					}
				}
				
				//---------------------------Sản lượng xây dựng dân dụng---------------------
				m++;
				m++;
				List<ProgressTaskOsDTO> dataSheetSlXddd = progressTaskOsDAO.getDataTienDoSlXddd(obj);
				if (dataSheetSlXddd != null && dataSheetSlXddd.size()>0) {
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 8));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("Sản lượng Xây dựng dân dụng");
					cell.setCellStyle(styleTitle);
					
					m++;
					Row row4 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
					Cell cell4 = row4.createCell(2, CellType.STRING);
					cell4.setCellValue("KH tháng");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 3, 3));
					cell4 = row4.createCell(3, CellType.STRING);
					cell4.setCellValue("Đã hoàn thành");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 4, 4));
					cell4 = row4.createCell(4, CellType.STRING);
					cell4.setCellValue("Tỷ lệ");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell4 = row4.createCell(5, CellType.STRING);
					cell4.setCellValue("Đang thi công");
					cell4.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell4 = row4.createCell(7, CellType.STRING);
					cell4.setCellValue("Dự kiến hoàn thành");
					cell4.setCellStyle(styleTitle);

					m++;
					Row row3 = sheet4.createRow(m);
					Cell cell3 = row3.createCell(5, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(6, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(7, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(8, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					
					m++;
					int countSlXddd = 1; 
					for (ProgressTaskOsDTO dto : dataSheetSlXddd) {
						Row rowSlXddd = sheet4.createRow(m++);
						Cell cellSlXddd = rowSlXddd.createCell(0, CellType.STRING);
						cellSlXddd.setCellValue("" + (countSlXddd++));
						cellSlXddd.setCellStyle(styleNumber);
						cellSlXddd = rowSlXddd.createCell(1, CellType.STRING);
						cellSlXddd.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellSlXddd.setCellStyle(style);
						cellSlXddd = rowSlXddd.createCell(2, CellType.STRING);
						cellSlXddd.setCellValue((dto.getTdSlXdddKhThang() != null) ? dto.getTdSlXdddKhThang() : 0l);
						cellSlXddd.setCellStyle(styleCurrency);
						cellSlXddd = rowSlXddd.createCell(3, CellType.STRING);
						cellSlXddd.setCellValue((dto.getTdSlXdddDaHoanThanh() != null) ? dto.getTdSlXdddDaHoanThanh() : 0l);
						cellSlXddd.setCellStyle(styleCurrency);
						cellSlXddd = rowSlXddd.createCell(4, CellType.STRING);
						cellSlXddd.setCellValue((dto.getTdSlXdddDaHoanThanhTyLe() != null) ? dto.getTdSlXdddDaHoanThanhTyLe()*100 : 0D);
						cellSlXddd.setCellStyle(stylePercent);
						cellSlXddd = rowSlXddd.createCell(5, CellType.STRING);
						cellSlXddd.setCellValue((dto.getTdSlXdddDangThiCongValue() != null) ? dto.getTdSlXdddDangThiCongValue() : 0l);
						cellSlXddd.setCellStyle(styleCurrency);
						cellSlXddd = rowSlXddd.createCell(6, CellType.STRING);
						cellSlXddd.setCellValue((dto.getTdSlXdddDangThiCongTyLe() != null) ? dto.getTdSlXdddDangThiCongTyLe()*100 : 0D);
						cellSlXddd.setCellStyle(stylePercent);
						cellSlXddd = rowSlXddd.createCell(7, CellType.STRING);
						cellSlXddd.setCellValue((dto.getTdSlXdddDuKienHoanThanhValue() != null) ? dto.getTdSlXdddDuKienHoanThanhValue() : 0l);
						cellSlXddd.setCellStyle(styleCurrency);
						cellSlXddd = rowSlXddd.createCell(8, CellType.STRING);
						cellSlXddd.setCellValue((dto.getTdSlXdddDuKienHoanThanhTyLe() != null) ? dto.getTdSlXdddDuKienHoanThanhTyLe()*100 : 0D);
						cellSlXddd.setCellStyle(stylePercent);
					}
				}
				
				//------------------------------HSHC Xây dựng dân dụng---------------------------
				m++;
				List<ProgressTaskOsDTO> dataSheetBcHshcXddd =progressTaskOsDAO.getDataTienDoHshcXddd(obj);
				if (dataSheetBcHshcXddd != null && dataSheetBcHshcXddd.size()>0) {
					m++;
					Row row = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 0, 0));
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("STT");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m + 2, 1, 1));
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue("Đơn vị thực hiện");
					cell.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 2, 16));
					cell = row.createCell(2, CellType.STRING);
					cell.setCellValue("HSHC Xây dựng dân dụng");
					cell.setCellStyle(styleTitle);
					
					m++;
					Row row2 = sheet4.createRow(m);
					sheet4.addMergedRegion(new CellRangeAddress(m, m + 1, 2, 2));
					Cell cell2 = row2.createCell(2, CellType.STRING);
					cell2.setCellValue("KH Tháng");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 3, 4));
					cell2 = row2.createCell(3, CellType.STRING);
					cell2.setCellValue("HSHC đang trên đường + TCTy chưa thẩm duyệt");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 5, 6));
					cell2 = row2.createCell(5, CellType.STRING);
					cell2.setCellValue("Đang trình GĐ CN ký");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 7, 8));
					cell2 = row2.createCell(7, CellType.STRING);
					cell2.setCellValue("Đang làm đối soát 4A");
					cell2.setCellStyle(styleTitle);
					
					sheet4.addMergedRegion(new CellRangeAddress(m, m, 9, 10));
					cell2 = row2.createCell(9, CellType.STRING);
					cell2.setCellValue("PHT chi nhánh đang thẩm duyệt");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 11, 12));
					cell2 = row2.createCell(11, CellType.STRING);
					cell2.setCellValue("PHT chi nhánh đang nghiệm thu");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 13, 14));
					cell2 = row2.createCell(13, CellType.STRING);
					cell2.setCellValue("TTKT đang làm hồ sơ");
					cell2.setCellStyle(styleTitle);

					sheet4.addMergedRegion(new CellRangeAddress(m, m, 15, 16));
					cell2 = row2.createCell(15, CellType.STRING);
					cell2.setCellValue("Dự kiến HT");
					cell2.setCellStyle(styleTitle);
					m++;
					Row row3 = sheet4.createRow(m);
					Cell cell3 = row3.createCell(3, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(4, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(5, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(6, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(7, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(8, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(9, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(10, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(11, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(12, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(13, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(14, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(15, CellType.STRING);
					cell3.setCellValue("Giá trị");
					cell3.setCellStyle(styleTitle);
					cell3 = row3.createCell(16, CellType.STRING);
					cell3.setCellValue("Tỷ lệ");
					cell3.setCellStyle(styleTitle);

					m++;
					int countHshcXddd = 1; 
					for (ProgressTaskOsDTO dto : dataSheetBcHshcXddd) {
						Row rowHshcXddd = sheet4.createRow(m++);
						Cell cellHshcXddd = rowHshcXddd.createCell(0, CellType.STRING);
						cellHshcXddd.setCellValue("" + (countHshcXddd++));
						cellHshcXddd.setCellStyle(styleNumber);
						cellHshcXddd = rowHshcXddd.createCell(1, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTtkv() != null) ? dto.getTtkv() : "");
						cellHshcXddd.setCellStyle(style);
						cellHshcXddd = rowHshcXddd.createCell(2, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddKhThang() != null) ? dto.getTdHshcXdddKhThang() : 0l);
						cellHshcXddd.setCellStyle(styleCurrency);
						cellHshcXddd = rowHshcXddd.createCell(3, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddTrenDuongValue() != null) ? dto.getTdHshcXdddTrenDuongValue() : 0l);
						cellHshcXddd.setCellStyle(styleCurrency);
						cellHshcXddd = rowHshcXddd.createCell(4, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddTrenDuongTyLe() != null) ? dto.getTdHshcXdddTrenDuongTyLe()*100 : 0D);
						cellHshcXddd.setCellStyle(stylePercent);
						cellHshcXddd = rowHshcXddd.createCell(5, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddGdCnKyValue() != null) ? dto.getTdHshcXdddGdCnKyValue() : 0l);
						cellHshcXddd.setCellStyle(styleCurrency);
						cellHshcXddd = rowHshcXddd.createCell(6, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddGdCnKyTyLe() != null) ? dto.getTdHshcXdddGdCnKyTyLe()*100 : 0d);
						cellHshcXddd.setCellStyle(stylePercent);
						cellHshcXddd = rowHshcXddd.createCell(7, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddDoiSoat4aValue() != null) ? dto.getTdHshcXdddDoiSoat4aValue() : 0l);
						cellHshcXddd.setCellStyle(styleCurrency);
						cellHshcXddd = rowHshcXddd.createCell(8, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddDoiSoat4aTyLe() != null) ? dto.getTdHshcXdddDoiSoat4aTyLe()*100 : 0d);
						cellHshcXddd.setCellStyle(stylePercent);
						cellHshcXddd = rowHshcXddd.createCell(9, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddPhtThamDuyetValue() != null) ? dto.getTdHshcXdddPhtThamDuyetValue() : 0l);
						cellHshcXddd.setCellStyle(styleCurrency);
						cellHshcXddd = rowHshcXddd.createCell(10, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddPhtThamDuyetTyLe() != null) ? dto.getTdHshcXdddPhtThamDuyetTyLe()*100 : 0d);
						cellHshcXddd.setCellStyle(stylePercent);
						cellHshcXddd = rowHshcXddd.createCell(11, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddPhtNghiemThuValue() != null) ? dto.getTdHshcXdddPhtNghiemThuValue() : 0l);
						cellHshcXddd.setCellStyle(styleCurrency);
						cellHshcXddd = rowHshcXddd.createCell(12, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddPhtNghiemThuTyLe() != null) ? dto.getTdHshcXdddPhtNghiemThuTyLe()*100 : 0d);
						cellHshcXddd.setCellStyle(stylePercent);
						cellHshcXddd = rowHshcXddd.createCell(13, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddDangLamHoSoValue() != null) ? dto.getTdHshcXdddDangLamHoSoValue() : 0l);
						cellHshcXddd.setCellStyle(styleCurrency);
						cellHshcXddd = rowHshcXddd.createCell(14, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddDangLamHoSoTyLe() != null) ? dto.getTdHshcXdddDangLamHoSoTyLe()*100 : 0d);
						cellHshcXddd.setCellStyle(stylePercent);
						cellHshcXddd = rowHshcXddd.createCell(15, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddDuKienHoanThanhValue() != null) ? dto.getTdHshcXdddDuKienHoanThanhValue() : 0l);
						cellHshcXddd.setCellStyle(styleCurrency);
						cellHshcXddd = rowHshcXddd.createCell(16, CellType.STRING);
						cellHshcXddd.setCellValue((dto.getTdHshcXdddDuKienHoanThanhTyLe() != null) ? dto.getTdHshcXdddDuKienHoanThanhTyLe()*100 : 0d);
						cellHshcXddd.setCellStyle(stylePercent);
					}
				}
				
		setBordersToMergedCells(workbook, sheet4);
		// end báo cáo

		// Huy-end

		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Tien_do_ngoai_OS.xlsx");
		return path;
	}

	private void setBordersToMergedCells(Workbook workBook, Sheet sheet) {
		List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
		for (CellRangeAddress rangeAddress : mergedRegions) {
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, rangeAddress, sheet, workBook);
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, rangeAddress, sheet, workBook);
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, rangeAddress, sheet, workBook);
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, rangeAddress, sheet, workBook);
		}
	}
	
	public DataListDTO doSearchBaoCaoTienDoOs(ProgressTaskOsDTO obj, HttpServletRequest request) {
		List<RpProgressMonthPlanOsDTO> ls = new ArrayList<RpProgressMonthPlanOsDTO>();
//		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
//				Constant.AdResourceKey.DATA, request);
//		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
//		if (groupIdList != null && !groupIdList.isEmpty()) {
//			obj.setGroupIdList(groupIdList);
			ls = progressTaskOsDAO.doSearchBaoCaoTienDoOs(obj);
//		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setTotal(obj.getTotalRecord());
		data.setStart(1);
		return data;
	}
	
	public String exportFileBaoCaoKHOs(ProgressTaskOsDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_BaoCao_TienDo_KhThang_NgoaiOS.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Export_BaoCao_TienDo_KhThang_NgoaiOS.xlsx");
		List<RpProgressMonthPlanOsDTO> data = progressTaskOsDAO.getDataExportFileBaoCaoKHOs(obj);
		List<RpProgressMonthPlanOsDTO> dataTongHop = progressTaskOsDAO.doSearchBaoCaoTienDoOs(obj);
		//Sheet 1
		XSSFSheet sheet0 = workbook.getSheetAt(0);
		if (dataTongHop != null && !dataTongHop.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet0);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet0);
			// HuyPQ-22/08/2018-start
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet0);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));
			
//			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet0);
//			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("##0.00%"));
//			stylePercent.setAlignment(HorizontalAlignment.RIGHT);
			
			int i = 2;
			for (RpProgressMonthPlanOsDTO dto : dataTongHop) {
				Row row = sheet0.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getMonth() != null) ? dto.getMonth() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getYear() != null) ? dto.getYear() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName()!= null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//Sản lượng
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getCurrentQuantity() != null) ? dto.getCurrentQuantity() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getProgressQuantity() != null) ? dto.getProgressQuantity() : 0d);
				cell.setCellStyle(styleNumber);
				//Quỹ lương
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getComplete() != null) ? dto.getComplete() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getCurrentComplete() != null) ? dto.getCurrentComplete() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getProgressComplete() != null) ? dto.getProgressComplete() : 0d);
				cell.setCellStyle(styleNumber);
				//HSHC
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getRevenue() != null) ? dto.getRevenue() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getRevenueNotApprove() != null) ? dto.getRevenueNotApprove() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getProcessRevenueNotApprove() != null) ? dto.getProcessRevenueNotApprove() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getCurrentRevenueMonth() != null) ? dto.getCurrentRevenueMonth() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getProgressRevenueMonth() != null) ? dto.getProgressRevenueMonth() : 0d);
				cell.setCellStyle(styleNumber);
				//Dòng tiền
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getRevokeCashTarget() != null) ? dto.getRevokeCashTarget() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getRevokeCashComplete() != null) ? dto.getRevokeCashComplete() : 0d);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getProcessRevokeCash() != null) ? dto.getProcessRevokeCash() : 0d);
				cell.setCellStyle(styleNumber);
			}
		}
		//Sheet 2
		XSSFSheet sheet = workbook.getSheetAt(1);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			// HuyPQ-22/08/2018-start
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 3;
			for (RpProgressMonthPlanOsDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 3));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getMonthYear() != null) ? dto.getMonthYear() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName()!= null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//Quỹ lương
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getQuyLuongTarget() != null) ? dto.getQuyLuongTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getQuyLuongComplete() != null) ? dto.getQuyLuongComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getProcessQuyLuong() != null) ? dto.getProcessQuyLuong() : 0d);
				cell.setCellStyle(style);
				//HSHC xây lắp
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getHshcXlTarget() != null) ? dto.getHshcXlTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getHshcXlComplete() != null) ? dto.getHshcXlComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getProcessHshcXl() != null) ? dto.getProcessHshcXl() : 0d);
				cell.setCellStyle(style);
				//Doanh thu nguồn CP
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getRevenueCpTarget() != null) ? dto.getRevenueCpTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getRevenueCpComplete() != null) ? dto.getRevenueCpComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getProcessRevenueCp() != null) ? dto.getProcessRevenueCp() : 0d);
				cell.setCellStyle(style);
				// DOANH THU NTĐ (GPDN)
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getRevenueNtdgpdnTarget() != null) ? dto.getRevenueNtdgpdnTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getRevenueNtdgpdnComplete() != null) ? dto.getRevenueNtdgpdnComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getProcessRevenueNtdgpdn() != null) ? dto.getProcessRevenueNtdgpdn() : 0d);
				cell.setCellStyle(style);
				//DOANH THU NTĐ (XDDD)
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getRevenueNtdxdddTarget() != null) ? dto.getRevenueNtdxdddTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getRevenueNtdxdddComplete() != null) ? dto.getRevenueNtdxdddComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getProcessRevenueNtdxddd() != null) ? dto.getProcessRevenueNtdxddd() : 0d);
				cell.setCellStyle(style);
				//HSHC HẠ TẦNG CHO THUÊ
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getHshcHtctTarget() != null) ? dto.getHshcHtctTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getHshcHtctComplete() != null) ? dto.getHshcHtctComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getProcessHshcHtct() != null) ? dto.getProcessHshcHtct() : 0d);
				cell.setCellStyle(style);
				//SẢN LƯỢNG XÂY LẮP
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getQuantityXlTarget() != null) ? dto.getQuantityXlTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue((dto.getQuantityXlComplete() != null) ? dto.getQuantityXlComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getProcessQuantityXl() != null) ? dto.getProcessQuantityXl() : 0d);
				cell.setCellStyle(style);
				//SẢN LƯỢNG Ngoài OS
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue((dto.getQuantityCpTarget() != null) ? dto.getQuantityCpTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue((dto.getQuantityCpComplete() != null) ? dto.getQuantityCpComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(26, CellType.STRING);
				cell.setCellValue((dto.getProcessQuantityCp() != null) ? dto.getProcessQuantityCp() : 0d);
				cell.setCellStyle(style);
				//SẢN  LƯỢNG NTĐ (GPDN)
				cell = row.createCell(27, CellType.STRING);
				cell.setCellValue((dto.getQuantityNtdGpdnTarget() != null) ? dto.getQuantityNtdGpdnTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(28, CellType.STRING);
				cell.setCellValue((dto.getQuantityNtdGpdnComplete() != null) ? dto.getQuantityNtdGpdnComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(29, CellType.STRING);
				cell.setCellValue((dto.getProcessQuantityNtdGpdn() != null) ? dto.getProcessQuantityNtdGpdn() : 0d);
				cell.setCellStyle(style);
				//SẢN LƯỢNG NTĐ (XDDD)
				cell = row.createCell(30, CellType.STRING);
				cell.setCellValue((dto.getQuantityNtdXdddTarget() != null) ? dto.getQuantityNtdXdddTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(31, CellType.STRING);
				cell.setCellValue((dto.getQuantityNtdXdddComplete() != null) ? dto.getQuantityNtdXdddComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(32, CellType.STRING);
				cell.setCellValue((dto.getProcessQuantityNtdXddd() != null) ? dto.getProcessQuantityNtdXddd() : 0d);
				cell.setCellStyle(style);
				//Tìm kiếm việc XDDD
				cell = row.createCell(33, CellType.STRING);
				cell.setCellValue((dto.getTaskXdddTarget() != null) ? dto.getTaskXdddTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(34, CellType.STRING);
				cell.setCellValue((dto.getTaskXdddComplete() != null) ? dto.getTaskXdddComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(35, CellType.STRING);
				cell.setCellValue((dto.getProcessTaskXddd() != null) ? dto.getProcessTaskXddd() : 0d);
				cell.setCellStyle(style);
				//Thu hồi dòng tiền
				cell = row.createCell(36, CellType.STRING);
				cell.setCellValue((dto.getRevokeCashTarget() != null) ? dto.getRevokeCashTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(37, CellType.STRING);
				cell.setCellValue((dto.getRevokeCashComplete() != null) ? dto.getRevokeCashComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(38, CellType.STRING);
				cell.setCellValue((dto.getProcessRevokeCash() != null) ? dto.getProcessRevokeCash() : 0d);
				cell.setCellStyle(style);
				//Tổng triển khai trạm
				cell = row.createCell(39, CellType.STRING);
				cell.setCellValue((dto.getSumDeployHtctTarget() != null) ? dto.getSumDeployHtctTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(40, CellType.STRING);
				cell.setCellValue((dto.getSumDeployHtctComplete() != null) ? dto.getSumDeployHtctComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(41, CellType.STRING);
				cell.setCellValue((dto.getProcessSumDeployHtct() != null) ? dto.getProcessSumDeployHtct() : 0d);
				cell.setCellStyle(style);
				//Xong móng
				cell = row.createCell(42, CellType.STRING);
				cell.setCellValue((dto.getMongHtctTarget() != null) ? dto.getMongHtctTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(43, CellType.STRING);
				cell.setCellValue((dto.getMongHtctComplete() != null) ? dto.getMongHtctComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(44, CellType.STRING);
				cell.setCellValue((dto.getProcessMongHtct() != null) ? dto.getProcessMongHtct() : 0d);
				cell.setCellStyle(style);
				//Xong ĐB
				cell = row.createCell(45, CellType.STRING);
				cell.setCellValue((dto.getDbHtctTarget() != null) ? dto.getDbHtctTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(46, CellType.STRING);
				cell.setCellValue((dto.getDbHtctComplete() != null) ? dto.getDbHtctComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(47, CellType.STRING);
				cell.setCellValue((dto.getProcessDbHtct() != null) ? dto.getProcessDbHtct() : 0d);
				cell.setCellStyle(style);
				//Thuê trạm
				cell = row.createCell(48, CellType.STRING);
				cell.setCellValue((dto.getRentHtctTarget() != null) ? dto.getRentHtctTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(49, CellType.STRING);
				cell.setCellValue((dto.getRentHtctComplete() != null) ? dto.getRentHtctComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(50, CellType.STRING);
				cell.setCellValue((dto.getProcessRentHtct() != null) ? dto.getProcessRentHtct() : 0d);
				cell.setCellStyle(style);

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_BaoCao_TienDo_KhThang_NgoaiOS.xlsx");
		return path;
	}
	
	public DataListDTO doSearchBaoCaoChamDiemKpi(ProgressTaskOsDTO obj, HttpServletRequest request) {
		List<RpProgressMonthPlanOsDTO> ls = new ArrayList<RpProgressMonthPlanOsDTO>();
		ls = progressTaskOsDAO.doSearchBaoCaoChamDiemKpi(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setTotal(obj.getTotalRecord());
		data.setStart(1);
		return data;
	}
	
	public String exportFileBaoCaoChamDiemKpi(ProgressTaskOsDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_BaoCao_ChamDiem_KPI.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Export_BaoCao_ChamDiem_KPI.xlsx");
		List<RpProgressMonthPlanOsDTO> data = progressTaskOsDAO.doSearchBaoCaoChamDiemKpi(obj);
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
			int i = 3;
			for (RpProgressMonthPlanOsDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 3));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getAreaCode()!= null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				//Tổng điểm
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDiemDatTong() != null) ? dto.getDiemDatTong() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongTong() != null) ? dto.getDiemThuongTong() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getTongDiem() != null) ? dto.getTongDiem() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getQuyDoiDiem() != null) ? dto.getQuyDoiDiem() : 0d);
				cell.setCellStyle(style);
				//Quỹ lương
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getQuyLuongTarget() != null) ? dto.getQuyLuongTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getQuyLuongComplete() != null) ? dto.getQuyLuongComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getProcessQuyLuong() != null) ? dto.getProcessQuyLuong() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getDiemDatQuyLuong() != null) ? dto.getDiemDatQuyLuong() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongQuyLuong() != null) ? dto.getDiemThuongQuyLuong() : 0d);
				cell.setCellStyle(style);
				// HSHC xây lắp
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getHshcXlTarget() != null) ? dto.getHshcXlTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getHshcXlComplete() != null) ? dto.getHshcXlComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getProcessHshcXl() != null) ? dto.getProcessHshcXl() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getDiemDatHschXl() != null) ? dto.getDiemDatHschXl() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongHshcXl() != null) ? dto.getDiemThuongHshcXl() : 0d);
				cell.setCellStyle(style);
				//Doanh thu nguồn CP
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getRevenueCpTarget() != null) ? dto.getRevenueCpTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getRevenueCpComplete() != null) ? dto.getRevenueCpComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getProcessRevenueCp() != null) ? dto.getProcessRevenueCp() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getDiemDatRevenueCp() != null) ? dto.getDiemDatRevenueCp() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongRevenueCp() != null) ? dto.getDiemThuongRevenueCp() : 0d);
				cell.setCellStyle(style);
				//DOANH THU NTĐ (XDDD)
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue((dto.getRevenueNtdXdddTarget() != null) ? dto.getRevenueNtdXdddTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getRevenueNtdXdddComplete() != null) ? dto.getRevenueNtdXdddComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue((dto.getProcessRevenueNtdXddd() != null) ? dto.getProcessRevenueNtdXddd() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue((dto.getDiemDatRevenueNtdXddd() != null) ? dto.getDiemDatRevenueNtdXddd() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(26, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongRevenueNtdXddd() != null) ? dto.getDiemThuongRevenueNtdXddd() : 0d);
				cell.setCellStyle(style);
				//SẢN  LƯỢNG XÂY LẮP
				cell = row.createCell(27, CellType.STRING);
				cell.setCellValue((dto.getQuantityXlTarget() != null) ? dto.getQuantityXlTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(28, CellType.STRING);
				cell.setCellValue((dto.getQuantityXlComplete() != null) ? dto.getQuantityXlComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(29, CellType.STRING);
				cell.setCellValue((dto.getProcessQuantityXl() != null) ? dto.getProcessQuantityXl() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(30, CellType.STRING);
				cell.setCellValue((dto.getDiemDatQuantityXl() != null) ? dto.getDiemDatQuantityXl() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(31, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongQuantityXl() != null) ? dto.getDiemThuongQuantityXl() : 0d);
				cell.setCellStyle(style);
				//Sản lượng nguồn chi phí
				cell = row.createCell(32, CellType.STRING);
				cell.setCellValue((dto.getQuantityCpTarget() != null) ? dto.getQuantityCpTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(33, CellType.STRING);
				cell.setCellValue((dto.getQuantityCpComplete() != null) ? dto.getQuantityCpComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(34, CellType.STRING);
				cell.setCellValue((dto.getProcessQuantityCp() != null) ? dto.getProcessQuantityCp() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(35, CellType.STRING);
				cell.setCellValue((dto.getDiemDatQuantityCp() != null) ? dto.getDiemDatQuantityCp() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(36, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongQuantityCp() != null) ? dto.getDiemThuongQuantityCp() : 0d);
				cell.setCellStyle(style);
				//SẢN LƯỢNG NTĐ (XDDD)
				cell = row.createCell(37, CellType.STRING);
				cell.setCellValue((dto.getQuantityNtdXdddTarget() != null) ? dto.getQuantityNtdXdddTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(38, CellType.STRING);
				cell.setCellValue((dto.getQuantityNtdXdddComplete() != null) ? dto.getQuantityNtdXdddComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(39, CellType.STRING);
				cell.setCellValue((dto.getProcessQuantityNtdXddd() != null) ? dto.getProcessQuantityNtdXddd() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(40, CellType.STRING);
				cell.setCellValue((dto.getDiemDatQuantityNtdXddd() != null) ? dto.getDiemDatQuantityNtdXddd() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(41, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongQuantityNtdXddd() != null) ? dto.getDiemThuongQuantityNtdXddd() : 0d);
				cell.setCellStyle(style);
				//Tìm kiếm việc XDDD
				cell = row.createCell(42, CellType.STRING);
				cell.setCellValue((dto.getTaskXdddTarget() != null) ? dto.getTaskXdddTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(43, CellType.STRING);
				cell.setCellValue((dto.getTaskXdddComplete() != null) ? dto.getTaskXdddComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(44, CellType.STRING);
				cell.setCellValue((dto.getProcessTaskXddd() != null) ? dto.getProcessTaskXddd() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(45, CellType.STRING);
				cell.setCellValue((dto.getDiemDatTaskXddd() != null) ? dto.getDiemDatTaskXddd() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(46, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongTaskXddd() != null) ? dto.getDiemThuongTaskXddd() : 0d);
				cell.setCellStyle(style);
				//Thu hồi dòng tiền
				cell = row.createCell(47, CellType.STRING);
				cell.setCellValue((dto.getRevokeCashTarget() != null) ? dto.getRevokeCashTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(48, CellType.STRING);
				cell.setCellValue((dto.getRevokeCashComplete() != null) ? dto.getRevokeCashComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(49, CellType.STRING);
				cell.setCellValue((dto.getProcessRevokeCash() != null) ? dto.getProcessRevokeCash() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(50, CellType.STRING);
				cell.setCellValue((dto.getDiemDatRevokeCash() != null) ? dto.getDiemDatRevokeCash() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(51, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongRevokeCash() != null) ? dto.getDiemThuongRevokeCash() : 0d);
				cell.setCellStyle(style);
				//Tổng triển khai trạm HTCT
				cell = row.createCell(52, CellType.STRING);
				cell.setCellValue((dto.getTkHtctTarget() != null) ? dto.getTkHtctTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(53, CellType.STRING);
				cell.setCellValue((dto.getTkHtctComplete() != null) ? dto.getTkHtctComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(54, CellType.STRING);
				cell.setCellValue((dto.getProcessTkHtct() != null) ? dto.getProcessTkHtct() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(55, CellType.STRING);
				cell.setCellValue((dto.getDiemDatTkHtct() != null) ? dto.getDiemDatTkHtct() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(56, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongTkHtct() != null) ? dto.getDiemThuongTkHtct() : 0d);
				cell.setCellStyle(style);
				//Xd móng
				cell = row.createCell(57, CellType.STRING);
				cell.setCellValue((dto.getMongHtctTarget() != null) ? dto.getMongHtctTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(58, CellType.STRING);
				cell.setCellValue((dto.getMongHtctComplete() != null) ? dto.getMongHtctComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(59, CellType.STRING);
				cell.setCellValue((dto.getProcessMongHtct() != null) ? dto.getProcessMongHtct() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(60, CellType.STRING);
				cell.setCellValue((dto.getDiemDatMongHtct() != null) ? dto.getDiemDatMongHtct() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(61, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongMongHtct() != null) ? dto.getDiemThuongMongHtct() : 0d);
				cell.setCellStyle(style);
				//Xong đb trạm
				cell = row.createCell(62, CellType.STRING);
				cell.setCellValue((dto.getDbHtctTarget() != null) ? dto.getDbHtctTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(63, CellType.STRING);
				cell.setCellValue((dto.getDbHtctComplete() != null) ? dto.getDbHtctComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(64, CellType.STRING);
				cell.setCellValue((dto.getProcessDbHtct() != null) ? dto.getProcessDbHtct() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(65, CellType.STRING);
				cell.setCellValue((dto.getDiemDatDbHtct() != null) ? dto.getDiemDatDbHtct() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(66, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongDbHtct() != null) ? dto.getDiemThuongDbHtct() : 0d);
				cell.setCellStyle(style);
				//
				cell = row.createCell(67, CellType.STRING);
				cell.setCellValue((dto.getRentHtctTarget() != null) ? dto.getRentHtctTarget() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(68, CellType.STRING);
				cell.setCellValue((dto.getRentHtctComplete() != null) ? dto.getRentHtctComplete() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(69, CellType.STRING);
				cell.setCellValue((dto.getProcessRentHtct() != null) ? dto.getProcessRentHtct() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(70, CellType.STRING);
				cell.setCellValue((dto.getDiemdatRentHtct() != null) ? dto.getDiemdatRentHtct() : 0d);
				cell.setCellStyle(style);
				cell = row.createCell(71, CellType.STRING);
				cell.setCellValue((dto.getDiemThuongRentHtct() != null) ? dto.getDiemThuongRentHtct() : 0d);
				cell.setCellStyle(style);
				

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_BaoCao_ChamDiem_KPI.xlsx");
		return path;
	}
}
