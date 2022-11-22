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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.viettel.coms.bo.DetailMonthPlanBO;
import com.viettel.coms.bo.ManageHcqtBO;
import com.viettel.coms.bo.ManageVttbBO;
import com.viettel.coms.dao.DepartmentDAO;
import com.viettel.coms.dao.ManageHcqtDAO;
import com.viettel.coms.dao.RevokeCashMonthPlanDAO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.dto.ManageHcqtDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.erp.dto.AMaterialRecoveryListDTO;
import com.viettel.erp.dto.AMaterialRecoveryListModelDTO;
import com.viettel.erp.dto.CntContractDTO;
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
@Service("manageHcqtBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ManageHcqtBusinessImpl extends BaseFWBusinessImpl<ManageHcqtDAO, ManageHcqtDTO, ManageHcqtBO>
		implements ManageHcqtBusiness {

	static Logger LOGGER = LoggerFactory.getLogger(ManageHcqtBusinessImpl.class);

	@Autowired
	private ManageHcqtDAO manageHcqtDAO;

	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	HashMap<Integer, String> colName = new HashMap();
	{
		colName.put(1, "Đơn vị thi công");
		colName.put(2, "Dự án");
		colName.put(3, "Hợp đồng");
		colName.put(4, "Công trình");
		colName.put(5, "Mã vật tư thiết bị");
		colName.put(6, "Tên vật tư");
		colName.put(7, "Đơn vị tính");
		colName.put(8, "Số lượng");
		colName.put(9, "Thành tiền");
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
	}

	public ManageHcqtBusinessImpl() {
		tModel = new ManageHcqtBO();
		tDAO = manageHcqtDAO;
	}

	@Override
	public ManageHcqtDAO gettDAO() {
		return manageHcqtDAO;
	}
	
	public List<AMaterialRecoveryListModelDTO> device(ManageHcqtDTO obj) {
		return manageHcqtDAO.device(obj);
	}

	public List<AMaterialRecoveryListModelDTO> materials(Long constructId) {
		return manageHcqtDAO.materials(constructId);
	}
	public List<AMaterialRecoveryListDTO> checkSum(Long constructId) {
		return manageHcqtDAO.checkSum(constructId);
	}

	public DataListDTO getTwoList(List<AMaterialRecoveryListModelDTO> device,
			List<AMaterialRecoveryListModelDTO> materials, ManageHcqtDTO obj) {
		List<AMaterialRecoveryListModelDTO> list = Lists.newArrayList(Iterables.concat(device, materials));
		DataListDTO data = new DataListDTO();
		data.setData(list);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	// tatph-start-12/12/2019
	public DataListDTO doSearch(ManageHcqtDTO obj, HttpServletRequest request) {
		List<ManageHcqtDTO> ls = new ArrayList<ManageHcqtDTO>();
		obj.setTotalRecord(0);
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = manageHcqtDAO.doSearch(obj, groupIdList);
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

	public void updateManageVttb(ManageVttbDTO obj) {
		obj.setUpdateDate(new Date());
		manageHcqtDAO.updateManageVttbByCommand(obj);
	}

	public void saveManageHcqt(ManageHcqtDTO obj) {
		obj.setCreateDate(new Date());
		manageHcqtDAO.saveObject(obj.toModel());
	}
	public String getExcelTemplate(ManageHcqtDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(
				new FileInputStream(filePath + "Bieu_mau_quan_ly_hoan_cong_qt_cong_trinh.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Bieu_mau_quan_ly_hoan_cong_qt_cong_trinh.xlsx");
		List<String> groupIdList = new ArrayList<>();

		List<DepartmentDTO> listDonvi = manageHcqtDAO.getForAutoCompleteDept(new DepartmentDTO());
		List<ConstructionDTO> listCongTrinh = manageHcqtDAO.getForAutoCompleteConstruction(new ConstructionDTO());
		List<CntContractDTO> listHopDong = manageHcqtDAO.getForAutoCompleteContract(new CntContractDTO());
		// sheet 1 - công trình
		XSSFSheet sheet = workbook.getSheetAt(1);
		if (listCongTrinh != null && !listCongTrinh.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 1;
			for (ConstructionDTO dto : listCongTrinh) {
				//
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getCode() != null ? dto.getCode() : "");
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getName() != null ? dto.getName() : "");
				cell.setCellStyle(styleNumber);

			}
		}

		// sheet 2 - hợp đồng
		XSSFSheet sheet2 = workbook.getSheetAt(2);
		if (listHopDong != null && !listHopDong.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet2);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet2);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet2);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 1;
			for (CntContractDTO dto : listHopDong) {
				//
				Row row = sheet2.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getCode() != null ? dto.getCode() : "");
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getName() != null ? dto.getName() : "");
				cell.setCellStyle(styleNumber);

			}
		}

		// sheet 3 - Đơn vị thi công
		XSSFSheet sheet3 = workbook.getSheetAt(3);
		if (listDonvi != null && !listDonvi.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet3);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet3);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet3);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 1;
			for (DepartmentDTO dto : listDonvi) {
				//
				Row row = sheet3.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getCode() != null ? dto.getCode() : "");
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getName() != null ? dto.getName() : "");
				cell.setCellStyle(styleNumber);

			}
		}

		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(
				uploadPathReturn + File.separator + "Bieu_mau_quan_ly_hoan_cong_qt_cong_trinh.xlsx");
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

	public List<ManageHcqtDTO> importManageHcqt(String fileInput) {
		List<ManageHcqtDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();

		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();

			List<String> listCodeConstruction = new ArrayList<>();
			List<String> listCodeContract = new ArrayList<>();
			int counts = 0;
			for (Row row : sheet) {
				counts++;
				if (counts >= 3 && checkIfRowIsEmpty(row))
					continue;
				if (counts >= 3) {
					String contractCode = formatter.formatCellValue(row.getCell(3));
					String constructionCode = formatter.formatCellValue(row.getCell(4));
					listCodeConstruction.add(constructionCode);
					listCodeContract.add(contractCode);

				}
			}
			ConstructionDTO constructionDTO = new ConstructionDTO();
			constructionDTO.setListCode(listCodeConstruction);
			CntContractDTO cntContractDTO = new CntContractDTO();
			cntContractDTO.setListCode(listCodeContract);

			List<DepartmentDTO> listDonvi = manageHcqtDAO.getForAutoCompleteDept(new DepartmentDTO());
			List<ConstructionDTO> listCongTrinh = manageHcqtDAO.getForAutoCompleteConstruction(constructionDTO);
			List<CntContractDTO> listHopDong = manageHcqtDAO.getForAutoCompleteContract(cntContractDTO);

			Map<String, DepartmentDTO> mapDonvi = new HashMap<>();
			Map<String, ConstructionDTO> mapcongTrinh = new HashMap<>();
			Map<String, CntContractDTO> mapHopDong = new HashMap<>();
			listDonvi.forEach(dto -> {
				mapDonvi.put(dto.getCode().toUpperCase().trim(), dto);
			});
			listCongTrinh.forEach(dto -> {
				mapcongTrinh.put(dto.getCode().toUpperCase().trim(), dto);
			});
			listHopDong.forEach(dto -> {
				mapHopDong.put(dto.getCode().toUpperCase().trim(), dto);
			});

			int count = 0;
			for (Row row : sheet) {
				count++;
				if (count >= 3 && checkIfRowIsEmpty(row))
					continue;
				if (count >= 3) {
					String departmentCode = formatter.formatCellValue(row.getCell(1));
					String project = formatter.formatCellValue(row.getCell(2));
					String contractCode = formatter.formatCellValue(row.getCell(3));
					String constructionCode = formatter.formatCellValue(row.getCell(4));
					String vttbCode = formatter.formatCellValue(row.getCell(5));
					String vttbName = formatter.formatCellValue(row.getCell(6));
					String dvt = formatter.formatCellValue(row.getCell(7));
					String soLuong = formatter.formatCellValue(row.getCell(8));
					String thanhTien = formatter.formatCellValue(row.getCell(9));

					ManageHcqtDTO obj = new ManageHcqtDTO();
					// validate cong trinh
					if (validateString(departmentCode)) {
						DepartmentDTO departmentDTO2 = mapDonvi.get(departmentCode.toUpperCase().trim());
						if(departmentDTO2 == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
									colName.get(1) + " không tồn tại");
							errorList.add(errorDTO);
						}else {
							obj.setDeptCode(departmentDTO2.getCode());
							obj.setDeptName(departmentDTO2.getName());
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
								colName.get(1) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(project)) {
						obj.setProject(project);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
								colName.get(2) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(contractCode)) {
						CntContractDTO cntContractDTO2 = mapHopDong.get(contractCode.toUpperCase().trim());
						if(cntContractDTO2 == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
									colName.get(3) + " không tồn tại");
							errorList.add(errorDTO);
						}else {
							obj.setContractCode(cntContractDTO2.getCode());
							obj.setContractName(cntContractDTO2.getName());
						}
						
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
								colName.get(3) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(constructionCode)) {
						ConstructionDTO constructionDTO2 = mapcongTrinh.get(constructionCode.toUpperCase().trim());
						if(constructionDTO2 == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
									colName.get(4) + " không tồn tại");
							errorList.add(errorDTO);
						}else {
							obj.setConstructionCode(constructionDTO2.getCode());
							obj.setConstructionName(constructionDTO2.getName());
						}
						
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
								colName.get(4) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(vttbCode)) {
						obj.setVttbCode(vttbCode);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
								colName.get(5) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(vttbName)) {
						obj.setVttbName(vttbName);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
								colName.get(6) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(dvt)) {
						obj.setDvt(dvt);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
								colName.get(7) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(soLuong)) {
						try{
							Long soLuongL = Long.parseLong(soLuong);
							obj.setSoLuong(soLuongL);
						}catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
									colName.get(8) + " chỉ nhập số");
							errorList.add(errorDTO);
						}
					
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
								colName.get(8) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(thanhTien)) {
						try{
							Long thanhTienL = Long.parseLong(thanhTien);
							obj.setThanhTien(thanhTienL);
						}catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
									colName.get(9) + " chỉ nhập số");
							errorList.add(errorDTO);
						}
					
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
								colName.get(9) + " bị bỏ trống");
						errorList.add(errorDTO);
					}

					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ManageHcqtDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ManageHcqtDTO errorContainer = new ManageHcqtDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(10); // cột dùng để in ra lỗi
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
			List<ManageHcqtDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ManageHcqtDTO errorContainer = new ManageHcqtDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(10); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}

}
