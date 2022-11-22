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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.viettel.coms.business.EffectiveCalculateDasCapexBusinessImpl;
import com.viettel.coms.dao.EffectiveCalculateDasCapexDAO;
import com.viettel.coms.dto.EffectiveCalculateDasCapexDTO;
import com.viettel.erp.dto.DataListDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.wms.business.UserRoleBusinessImpl;

public class EffectiveCalculateDasCapexRsServiceImpl implements EffectiveCalculateDasCapexRsService {
	@Autowired
	EffectiveCalculateDasCapexBusinessImpl effectiveCalculateDasCapexBusinessImpl;
	@Autowired
	private UserRoleBusinessImpl userRoleBusinessImpl;
	@Autowired
	private EffectiveCalculateDasCapexDAO effectiveCalculateDasCapexDAO;
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
	@Value("${folder_upload}")
	private String folderTemp;
	@Override
	public Response getAssumptionsCapex(EffectiveCalculateDasCapexDTO obj) {
		List<EffectiveCalculateDasCapexDTO> ls = effectiveCalculateDasCapexBusinessImpl.getAssumptionsCapex(obj);
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
	public Response importDasCapex(Attachment attachments,
			HttpServletRequest request) throws Exception {
		
		KttsUserSession objUser=userRoleBusinessImpl.getUserSession(request);
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		String filePath;
		
		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}

		DataHandler dataHandler = attachments.getDataHandler();
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);

		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}		
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileServerATTT2(inputStream, fileName, folderParam, folderUpload);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}
		try {

			try {
				List<EffectiveCalculateDasCapexDTO> result = effectiveCalculateDasCapexBusinessImpl.importDasCapex(folderUpload + filePath);
				if(result != null && !result.isEmpty() && (result.get(0).getErrorList()==null || result.get(0).getErrorList().size() == 0)){
						for (EffectiveCalculateDasCapexDTO obj : result) {
							if(obj.getAssumptionsCapexId()==null){
								obj.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());							
								obj.setCreatedDate(new Date());
								effectiveCalculateDasCapexDAO.saveObject(obj.toModel());
							} else {
								obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
								obj.setUpdatedDate(new Date());
								effectiveCalculateDasCapexDAO.updateObject(obj.toModel());
							}
						}
					return Response.ok(result).build();
				}
				else if (result == null || result.isEmpty()) {
					return Response.ok().entity(Response.Status.NO_CONTENT).build();
				} 
				else{
					return Response.ok(result).build();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}
	
	private boolean isFolderAllowFolderSave(String folderDir) {
		return UString.isFolderAllowFolderSave(folderDir, allowFolderDir);
	}
	
	private boolean isExtendAllowSave(String fileName) {
		return UString.isExtendAllowSave(fileName, allowFileExt);
	}


}
