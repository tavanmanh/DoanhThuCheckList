package com.viettel.coms.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import viettel.passport.client.UserToken;

import com.viettel.coms.business.ConstructionTaskBusinessImpl;
import com.viettel.coms.business.DmpnOrderBusinessImpl;
import com.viettel.coms.business.ManageAnnualTargetPlanBusinessImpl;
import com.viettel.coms.business.YearPlanBusinessImpl;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.ContactUnitDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.DetailMonthPlanSimpleDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.ManageAnnualTargetPlanDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.dto.ManageHcqtDTO;
import com.viettel.coms.dto.ManageUsedMaterialDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserDetailCOMSDTO;
import com.viettel.coms.dto.TmpnTargetDetailDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;


/**
 * @author HoangNH38
 */
public class ManageAnnualTargetPlanRsServiceImpl implements ManageAnnualTargetPlanRsService {

	protected final Logger log = Logger.getLogger(ManageAnnualTargetPlanRsServiceImpl.class);
	@Autowired
	ManageAnnualTargetPlanBusinessImpl manageAnnualTargetPlanBusinessImpl;
	@Context
	HttpServletRequest request;
	
	
	@Value("${folder_upload2}")
	private String folderUpload;

	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	@Value("${allow.file.ext}")
	private String allowFileExt;

	@Value("${allow.folder.dir}")
	private String allowFolderDir;


	// tatph-start 19/12/2019
	@Override
	public Response doSearch(ManageAnnualTargetPlanDTO obj) {
		DataListDTO data = manageAnnualTargetPlanBusinessImpl.doSearch(obj, request);
		return Response.ok(data).build();
	}
	
	@Override
	public Response getById(ManageAnnualTargetPlanDTO obj) {
		DataListDTO data = manageAnnualTargetPlanBusinessImpl.getById(obj, request);
		return Response.ok(data).build();
	}
	
	@Override
	public Response add(ManageAnnualTargetPlanDTO obj) {
		obj.setCreateDate(new Date());
		
		Long id = manageAnnualTargetPlanBusinessImpl.save(obj);
		return Response.ok().build();
	}
	
	@Override
	public Response update(ManageAnnualTargetPlanDTO obj) {
		obj.setUpdateDate(new Date());
		Long id = manageAnnualTargetPlanBusinessImpl.update(obj);
		return Response.ok().build();
	}
	
	@Override
	public Response remove(ManageAnnualTargetPlanDTO obj) {
		manageAnnualTargetPlanBusinessImpl.remove(obj);
		return Response.ok().build();
	}
	



	
	@Override
	public Response getExcelTemplate(ManageAnnualTargetPlanDTO obj) throws Exception {
		try {
			String strReturn = manageAnnualTargetPlanBusinessImpl.getExcelTemplate(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	private boolean isFolderAllowFolderSave(String folderDir) {
		return UString.isFolderAllowFolderSave(folderDir, allowFolderDir);
	}

	private boolean isExtendAllowSave(String fileName) {
		return UString.isExtendAllowSave(fileName, allowFileExt);
	}
	
	
	@Override
	public Response importManageAnnualTargetPlan(Attachment attachments, HttpServletRequest request) throws Exception {
		KttsUserSession objUser = manageAnnualTargetPlanBusinessImpl.getUserSession(request);
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
			filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ManageAnnualTargetPlanDTO> result = manageAnnualTargetPlanBusinessImpl.importManageAnnualTargetPlan(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				result.forEach(dto ->{
					manageAnnualTargetPlanBusinessImpl.save(dto);
				});
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}

	}


	// tatph-end 19/12/2019


}
