package com.viettel.coms.rest;

import java.io.InputStream;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.coms.business.RecommendContactUnitBusinessImpl;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.ContactUnitDTO;
import com.viettel.coms.dto.ContactUnitDetailDTO;
import com.viettel.coms.dto.ContactUnitLibraryDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.business.UserRoleBusiness;

/**
 * @author HoangNH38
 */
public class RecommendContactUnitRsServiceImpl implements RecommendContactUnitRsService {

	protected final Logger log = Logger.getLogger(RecommendContactUnitRsServiceImpl.class);
	@Autowired
	RecommendContactUnitBusinessImpl recommendContactUnitBusinessImpl;
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
	public Response doSearch(ContactUnitDTO obj) {
		DataListDTO data = recommendContactUnitBusinessImpl.doSearch(obj, request);
		return Response.ok(data).build();
	}
	@Override
	public Response doSearchDetail(ContactUnitDetailDTO obj) {
		DataListDTO data = recommendContactUnitBusinessImpl.doSearchDetail(obj, request);
		return Response.ok(data).build();
	}
	
	@Override
	public Response doSearchDetailById(ContactUnitDetailDTO obj) {
		DataListDTO data = recommendContactUnitBusinessImpl.doSearchDetailById(obj, request);
		return Response.ok(data).build();
	}
	
	@Override
	public Response doSearchContactUnitLibrary(ContactUnitDetailDTO obj) {
		DataListDTO data = recommendContactUnitBusinessImpl.doSearchContactUnitLibrary(obj, request);
		return Response.ok(data).build();
	}
	
	@Override
	public Response updateDescription(ContactUnitDetailDTO obj) {
		recommendContactUnitBusinessImpl.updateDescription(obj);
		return Response.ok().build();
	}

	@Override
	public Response update(ContactUnitDetailDTO obj) {
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			recommendContactUnitBusinessImpl.updateContactUnitDetail(obj);
			return Response.ok(Response.Status.NO_CONTENT).build();
		}
	}
	
	@Override
	public Response save(ContactUnitDetailDTO obj) {
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			recommendContactUnitBusinessImpl.saveContactUnitDetail(obj);
			return Response.ok(Response.Status.NO_CONTENT).build();
		}
	}

	@Override
	public Response getExcelTemplate(ContactUnitDTO obj) throws Exception {
		try {
			String strReturn = recommendContactUnitBusinessImpl.getExcelTemplate(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	@Override
	public Response getExcelTemplateManageContactUnit(ContactUnitDTO obj) throws Exception {
		try {
			String strReturn = recommendContactUnitBusinessImpl.getExcelTemplateManageContactUnit(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	@Override
	public Response getExcelTemplateConstructionDTOS(ConstructionTaskDetailDTO obj) throws Exception {
		try {
			String strReturn = recommendContactUnitBusinessImpl.getExcelTemplateConstructionDTOS(obj,request);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	@Override
	public Response exportListContact(ContactUnitDTO obj) throws Exception {
		try {
			String strReturn = recommendContactUnitBusinessImpl.exportListContact(obj);
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
	public Response importRecommendContactUnit(Attachment attachments, HttpServletRequest request) throws Exception {
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
			filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ContactUnitDTO> result = recommendContactUnitBusinessImpl.importRecommendContactUnit(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				result.forEach(dto ->{
					dto.setCreateDate(new Date());
					dto.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
					recommendContactUnitBusinessImpl.saveContactUnit(dto);
					
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
	
	@Override
	public Response importRecommendContactUnitLibrary(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<ContactUnitLibraryDTO> result = recommendContactUnitBusinessImpl.importRecommendContactUnitLibrary(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				result.forEach(dto ->{
					Integer id = recommendContactUnitBusinessImpl.getMaxId();
					dto.setContactUnitId(id.longValue());
					recommendContactUnitBusinessImpl.saveContactUnitLibrary(dto);
					
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
	
	@Override
	public Response importConstructionDTOS(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<ConstructionTaskDetailDTO> result = recommendContactUnitBusinessImpl.importConstructionDTOS(filePath,request);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				result.forEach(dto ->{
					recommendContactUnitBusinessImpl.updateConstruction(dto, request);
					recommendContactUnitBusinessImpl.updateRevenue(dto);
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
	
	//HienLT56 start 01072020
	@Override
	public Response getForAutoCompleteProvince(ContactUnitDTO obj) {
//		List<ContactUnitDTO> results = recommendContactUnitBusinessImpl.getForAutoCompleteProvince(obj);
		List<ContactUnitDTO> ls = recommendContactUnitBusinessImpl.getForAutoCompleteProvince(obj);
		if (ls == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			DataListDTO data = new DataListDTO();
			data.setData(ls);
			data.setTotal(obj.getTotalRecord());
			data.setSize(ls.size());
			data.setStart(1);
			return Response.ok(data).build();
		}
	}
	public Response getForAutoCompleteProvinceS(ContactUnitDTO obj) {
		List<ContactUnitDTO> results = recommendContactUnitBusinessImpl.getForAutoCompleteProvince(obj);
		return Response.ok(results).build();
	}

	@Override
	public Response addContactt(ContactUnitDTO obj) {
//		ContactUnitDTO existing = (ContactUnitDTO) recommendContactUnitBusinessImpl.findByCode(obj);
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Long id = 0l;
		try {
			HttpServletRequest test = request;
			boolean check = test == null;
			obj.setCreateUserId(objUser.getSysUserId());
			obj.setCreateDate(new Date());
			id = recommendContactUnitBusinessImpl.saveAddFormContact(obj);
			obj.setContactUnitId(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(id == 0l) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}else {
			return Response.ok(obj).build();
		}
	}
	@Override
	public Response importRecommendContactUnitAll(Attachment attachments, HttpServletRequest request) throws Exception {
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
			filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ContactUnitDTO> result = recommendContactUnitBusinessImpl.importRecommendContactUnitAll(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(ContactUnitDTO dto: result) {
					dto.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
					dto.setCreateDate(new Date());
					recommendContactUnitBusinessImpl.saveContactUnitAll(dto, request);
				}
//				result.forEach(dto ->{
//					dto.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
//					dto.setCreateDate(new Date());
//					recommendContactUnitBusinessImpl.saveContactUnitAll(dto, request);
//					
//				});
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
	@Override
	public Response getExcelTemplateAll(ContactUnitDTO obj) throws Exception {
		try {
			String strReturn = recommendContactUnitBusinessImpl.getExcelTemplateAll(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	//HienLT56 end 01072020
}
