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
import com.viettel.coms.bo.DetailMonthPlanBO;
import com.viettel.coms.bo.ManageAnnualTargetPlanBO;
import com.viettel.coms.bo.ManageVttbBO;
import com.viettel.coms.dao.ManageAnnualTargetPlanDAO;
import com.viettel.coms.dao.RevokeCashMonthPlanDAO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.ContactUnitDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.ManageAnnualTargetPlanDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.dto.ManageHcqtDTO;
import com.viettel.coms.dto.ManageUsedMaterialDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.utils.ExcelUtils;
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
@Service("manageAnnualTargetPlanBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ManageAnnualTargetPlanBusinessImpl
		extends BaseFWBusinessImpl<ManageAnnualTargetPlanDAO, ManageAnnualTargetPlanDTO, ManageAnnualTargetPlanBO>
		implements ManageAnnualTargetPlanBusiness {

	static Logger LOGGER = LoggerFactory.getLogger(ManageAnnualTargetPlanBusinessImpl.class);

	@Autowired
	private ManageAnnualTargetPlanDAO manageAnnualTargetPlanDAO;



	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	public ManageAnnualTargetPlanBusinessImpl() {
		tModel = new ManageAnnualTargetPlanBO();
		tDAO = manageAnnualTargetPlanDAO;
	}

	@Override
	public ManageAnnualTargetPlanDAO gettDAO() {
		return manageAnnualTargetPlanDAO;
	}


	// tatph-start-12/12/2019
	public DataListDTO doSearch(ManageAnnualTargetPlanDTO obj, HttpServletRequest request) {
		List<ManageAnnualTargetPlanDTO> ls = new ArrayList<ManageAnnualTargetPlanDTO>();
		obj.setTotalRecord(0);
////		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
////				Constant.AdResourceKey.DATA, request);
////		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
//		if (groupIdList != null && !groupIdList.isEmpty())
			ls = manageAnnualTargetPlanDAO.doSearch(obj, Collections.singletonList(""));
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public DataListDTO getById(ManageAnnualTargetPlanDTO obj, HttpServletRequest request) {
		List<ManageAnnualTargetPlanDTO> ls = new ArrayList<ManageAnnualTargetPlanDTO>();
		obj.setTotalRecord(0);
////		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
////				Constant.AdResourceKey.DATA, request);
////		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
//		if (groupIdList != null && !groupIdList.isEmpty())
			ls = manageAnnualTargetPlanDAO.getById(obj, Collections.singletonList(""));
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public Long save(ManageAnnualTargetPlanDTO obj) {
		obj.setStatus(1L);
		obj.setCreateDate(new Date());
		Long id = manageAnnualTargetPlanDAO.saveObject(obj.toModel());
		return id;
	}
	
	public Long update(ManageAnnualTargetPlanDTO obj) {
		obj.setUpdateDate(new Date());
		Long id = manageAnnualTargetPlanDAO.updateObject(obj.toModel());
		return id;
	}
	
	public void remove(ManageAnnualTargetPlanDTO obj) {
		manageAnnualTargetPlanDAO.remove(obj);
	}
	

	protected static final String USER_SESSION_KEY = "kttsUserSession";

	public KttsUserSession getUserSession(HttpServletRequest request) {
		KttsUserSession s = (KttsUserSession) request.getSession().getAttribute(USER_SESSION_KEY);
		if (s == null) {
			throw new BusinessException("user is not authen");
		}
		return s;

	}


	
	public String getExcelTemplate(ManageAnnualTargetPlanDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Bieu_mau_manage_annual_target_plan.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Bieu_mau_manage_annual_target_plan.xlsx");
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Bieu_mau_manage_annual_target_plan.xlsx");
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
	
	public List<ManageAnnualTargetPlanDTO> importManageAnnualTargetPlan(String fileInput) {
		List<ManageAnnualTargetPlanDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();

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
					String year = formatter.formatCellValue(row.getCell(1));
					String month = formatter.formatCellValue(row.getCell(2));
					String contractValue = formatter.formatCellValue(row.getCell(3));
					String tcValue = formatter.formatCellValue(row.getCell(4));
					String doanhThu = formatter.formatCellValue(row.getCell(5));
					ManageAnnualTargetPlanDTO dto = new ManageAnnualTargetPlanDTO();
					dto.setYear(year);
					dto.setMonth(month);
					dto.setContractValue(Long.parseLong(!"".equals(contractValue) ? contractValue : "0"));
					dto.setTcValue(Long.parseLong(!"".equals(tcValue) ? tcValue : "0"));
					dto.setDoanhThu(Long.parseLong(!"".equals(doanhThu) ? doanhThu : "0"));

					if (errorList.size() == 0) {
						workLst.add(dto);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ManageAnnualTargetPlanDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ManageAnnualTargetPlanDTO errorContainer = new ManageAnnualTargetPlanDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(6); // cột dùng để in ra lỗi
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
			List<ManageAnnualTargetPlanDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ManageAnnualTargetPlanDTO errorContainer = new ManageAnnualTargetPlanDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(6); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}


}
