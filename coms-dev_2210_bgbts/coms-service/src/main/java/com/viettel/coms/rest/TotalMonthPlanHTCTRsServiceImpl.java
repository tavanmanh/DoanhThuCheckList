package com.viettel.coms.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
//Duonghv13 start-16/08/2021//
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.coms.business.TotalMonthPlanHTCTBusinessImpl;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TotalMonthPlanHTCTDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.DateTimeUtils;

import viettel.passport.client.UserToken;

public class TotalMonthPlanHTCTRsServiceImpl implements TotalMonthPlanHTCTRsService {

	protected final Logger log = Logger.getLogger(TotalMonthPlanHTCTRsService.class);
	@Context
	HttpServletRequest request;

	@Autowired
	TotalMonthPlanHTCTBusinessImpl totalMonthPlanHTCTBusinessImpl;

	@Value("${folder_upload}")
	private String folder2Upload;

	@Value("${folder_upload2}")
	private String folderUpload;

	@Value("${folder_upload}")
	private String folderTemp;

	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	@Value("${allow.file.ext}")
	private String allowFileExt;

	@Value("${allow.folder.dir}")
	private String allowFolderDir;

	@Override
	public Response doSearch(TotalMonthPlanHTCTDTO obj) {
		// TODO Auto-generated method stub
		List<TotalMonthPlanHTCTDTO> ls = totalMonthPlanHTCTBusinessImpl.doSearch(obj);
		if (ls == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			DataListDTO data = new DataListDTO();
			data.setData(ls);
			data.setTotal(obj.getTotalRecord());
			data.setSize(obj.getPageSize());
			data.setStart(1);
			return Response.ok(data).build();
		}
	}

	@Override
	public Response remove(TotalMonthPlanHTCTDTO obj) {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {

			obj.setUpdatedTime(new Date());
			obj.setUpdatedBy(Long.parseLong(objUser.getSysUserId().toString()));
			obj.setStatus("0");
			Long id = totalMonthPlanHTCTBusinessImpl.updateTotalMonthPlanHTCT(obj);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok().build();
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response add(TotalMonthPlanHTCTDTO obj) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			obj.setInsertTime(new Date());
			obj.setCreatedBy(Long.parseLong(objUser.getSysUserId().toString()));
			obj.setStatus("1");
			Long id = totalMonthPlanHTCTBusinessImpl.createTotalMonthPlanHTCT(obj);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response update(TotalMonthPlanHTCTDTO obj) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			obj.setUpdatedTime(new Date());
			obj.setUpdatedBy(Long.parseLong(objUser.getSysUserId().toString()));
			obj.setStatus("1");
			Long id = totalMonthPlanHTCTBusinessImpl.updateTotalMonthPlanHTCT(obj);

			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response getById(Long id) throws Exception {
		// TODO Auto-generated method stub
		TotalMonthPlanHTCTDTO obj = (TotalMonthPlanHTCTDTO) totalMonthPlanHTCTBusinessImpl.getById(id);
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(obj).build();
		}
	}

	@Override
	public Response exportExcelTemplate() throws Exception {
		String strReturn = totalMonthPlanHTCTBusinessImpl.exportExcelTemplate();
		return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
	}

	@Override
	public Response importReport(Attachment attachments) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		String filePathReturn;
		String filePath;

		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}

		DataHandler dataHandler = attachments.getDataHandler();

		// get filename to be uploaded
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);
		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}

		// write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileServerATTT2(inputStream, fileName, folderParam, folderUpload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}
		try {
			java.util.List<TotalMonthPlanHTCTDTO> result = totalMonthPlanHTCTBusinessImpl
					.importReport(folderUpload + filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				result.forEach(dto -> {
					dto.setInsertTime(new Date());
					dto.setCreatedBy(Long.parseLong(objUser.getSysUserId().toString()));
					dto.setStatus("1");
					totalMonthPlanHTCTBusinessImpl.save(dto);
				});
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}

		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	private boolean isFolderAllowFolderSave(String folderDir) {
		return UString.isFolderAllowFolderSave(folderDir, allowFolderDir);

	}

	private boolean isExtendAllowSave(String fileName) {
		return UString.isExtendAllowSave(fileName, allowFileExt);
	}

	@Override
	public Response exportMonthPlan(TotalMonthPlanHTCTDTO obj) throws Exception {

		String strReturn = totalMonthPlanHTCTBusinessImpl.exportMonthPlan(obj);
		return Response.ok(Collections.singletonMap("fileName", strReturn)).build();

	}

	@Override
	public Response exportPDFReport(TotalMonthPlanHTCTDTO criteria) throws Exception {
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		String user = objUser.getVpsUserInfo().getFullName().toString();
		criteria.setSysUserName(user);
		String strReturn = totalMonthPlanHTCTBusinessImpl.exportPDFReport(criteria);
		if (strReturn == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		}
	}

	@Override
	public Response downloadFileTempATTT(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String fileName = UEncrypt.decryptFileUploadPath(request.getQueryString());
		File file = new File(folderTemp + File.separatorChar + fileName);
		InputStream ExcelFileToRead = new FileInputStream(file);
		if (!file.exists()) {
			file = new File(folderUpload + File.separatorChar + fileName);
			if (!file.exists()) {
				// log.warn("File {} is not found", fileName);
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

		}
		XSSFWorkbook workbook = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFCellStyle stt = ExcelUtils.styleText(sheet);
		stt.setAlignment(HorizontalAlignment.CENTER);
		XSSFCellStyle sttbold = ExcelUtils.styleBold(sheet);
		XSSFCellStyle color = workbook.createCellStyle();
		color.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		Row rowS2 = sheet.getRow(2);
		Cell cellS12 = rowS2.createCell(13, CellType.STRING);
		cellS12.setCellValue("Nội dung lỗi:  ");
		cellS12.setCellStyle(sttbold);
		cellS12.setCellStyle(color);

		FileOutputStream fileOut = new FileOutputStream(file);
		// write this workbook to an Outputstream.
		workbook.write(fileOut);
		workbook.close();
		fileOut.flush();
		fileOut.close();
		int lastIndex = fileName.lastIndexOf(File.separatorChar);
		String fileNameReturn = fileName.substring(lastIndex + 1);

		return Response.ok((Object) file)
				.header("Content-Disposition", "attachment; filename=\"" + fileNameReturn + "\"").build();
	}
	// Duonghv13 end-16/08/2021//

	@Override
	public Response checkRoleCreateMonthPlan(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Boolean checkRole = totalMonthPlanHTCTBusinessImpl.checkRoleCreateMonthPlan(request);
		if (checkRole) {
			return Response.ok(Status.OK).build();
		}
		return Response.ok().build();
	}
	// Duong end//

	@Override
	public Response getAllStationVCCHTCT(TotalMonthPlanHTCTDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(totalMonthPlanHTCTBusinessImpl.getAllStationVCCHTCT(obj, request)).build();
	}

}
