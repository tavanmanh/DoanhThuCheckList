package com.viettel.coms.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.viettel.coms.business.ManageHcqtBusinessImpl;
import com.viettel.coms.business.YearPlanBusinessImpl;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.DetailMonthPlanSimpleDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.dto.ManageHcqtDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserDetailCOMSDTO;
import com.viettel.coms.dto.TmpnTargetDetailDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.erp.dto.AMaterialRecoveryListDTO;
import com.viettel.erp.dto.AMaterialRecoveryListModelDTO;
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
public class ManageHcqtRsServiceImpl implements ManageHcqtRsService {

	protected final Logger log = Logger.getLogger(ManageHcqtRsServiceImpl.class);
	@Autowired
	ManageHcqtBusinessImpl manageHcqtBusinessImpl;
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
	public Response doSearch(ManageHcqtDTO obj) {
		DataListDTO data = manageHcqtBusinessImpl.doSearch(obj, request);
		return Response.ok(data).build();
	}
	@Override
	public Response doSearchV2(ManageHcqtDTO obj) {
//		DataListDTO data = manageHcqtBusinessImpl.doSearch(obj, request);
//		return Response.ok(data).build();
		Long constructId = 0L;
		List<AMaterialRecoveryListModelDTO> devices = (List<AMaterialRecoveryListModelDTO>) manageHcqtBusinessImpl
				.device(obj);
		List<AMaterialRecoveryListModelDTO> materials = (List<AMaterialRecoveryListModelDTO>) manageHcqtBusinessImpl
				.materials(constructId);
		
		List<AMaterialRecoveryListDTO> sum = manageHcqtBusinessImpl.checkSum(constructId);
		if(sum.size() > 0){
			for (int i = 0; i < materials.size(); i++) {
				for (int j = 0; j < sum.size(); j++) {
//					if(materials.get(i).getMerID().longValue() == sum.get(j).getMerEntityId().longValue() && (sum.get(j).getSerialNumber()!=null||"".equals(sum.get(j).getSerialNumber()))){
					if(materials.get(i).getMerCompare().equals(sum.get(j).getMerCompare()) && (sum.get(j).getSerialNumber()!=null||"".equals(sum.get(j).getSerialNumber()))){
						materials.get(i).setRecoveryQuantity((double) materials.get(i).getHandoverQuantity() - (double) materials.get(i).getAcceptQuantity() - (double)sum.get(j).getSumRecoveryQuantity());
						break;
					}
				}
			}
		} else {
			
			//Huypq-09042020-start
			for(int i = 0 ;i < materials.size() ; i++){
				materials.get(i).setRecoveryQuantity((double) materials.get(i).getHandoverQuantity() - (double) materials.get(i).getAcceptQuantity());
			}
		}
		HashMap<String, AMaterialRecoveryListModelDTO> mapListModel = new HashMap<>();
		for(int i = 0 ;i < devices.size() ; i++){
			devices.get(i).setRecoveryQuantity((double) devices.get(i).getHandoverQuantity() - (double) devices.get(i).getAcceptQuantity());
			if(devices.get(i).getRecoveryQuantity()<=0) {
				devices.remove(i);
			} else {
				if(devices.get(i).getMerCompare()!=null) {
					mapListModel.put(devices.get(i).getMerCompare(), devices.get(i));
				}
			}
		}
		
//		for (Iterator i = materials.iterator(); i.hasNext();) {
//			AMaterialRecoveryListModelDTO dto = (AMaterialRecoveryListModelDTO) i.next();
//			if(dto.getRecoveryQuantity() <= 0){
//				i.remove();
//			} else {
//				mapListModel.put(dto.getMerCompare(), i);
//			}
//		}
//		for (Iterator i = devices.iterator(); i.hasNext();) {
//			AMaterialRecoveryListModelDTO dto = (AMaterialRecoveryListModelDTO) i.next();
//			for (Iterator j = sum.iterator(); j.hasNext();) {
//				AMaterialRecoveryListDTO dto1 = (AMaterialRecoveryListDTO) j.next();
//					if (dto.getMerCompare().equals(dto1.getMerCompare())) {
//					i.remove();
//				}
//			}
//		}
		
		for(AMaterialRecoveryListDTO dto : sum) {
			if(mapListModel.get(dto.getMerCompare())!=null) {
				devices.remove(mapListModel.get(dto.getMerCompare()));
			}
		}
		//Huy-end
		DataListDTO list = manageHcqtBusinessImpl.getTwoList(devices, materials, obj);
		return Response.ok(list).build();
	}

	@Override
	public Response update(ManageVttbDTO obj) {
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			manageHcqtBusinessImpl.updateManageVttb(obj);
			return Response.ok(Response.Status.NO_CONTENT).build();
		}
	}

	@Override
	public Response getExcelTemplate(ManageHcqtDTO obj) throws Exception {
		try {
			String strReturn = manageHcqtBusinessImpl.getExcelTemplate(obj);
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
	public Response importManageHcqt(Attachment attachments, HttpServletRequest request) throws Exception {
		KttsUserSession objUser = manageHcqtBusinessImpl.getUserSession(request);
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
			List<ManageHcqtDTO> result = manageHcqtBusinessImpl.importManageHcqt(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				result.forEach(dto ->{
					manageHcqtBusinessImpl.saveManageHcqt(dto);
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
