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

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.business.ProgressPlanProjectBusinessImpl;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.ProgressPlanProjectDTO;
import com.viettel.coms.dto.SynStockTransDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;

public class ProgressPlanProjectRsServiceImpl implements ProgressPlanProjectRsService {

	@Autowired
	ProgressPlanProjectBusinessImpl progressPlanProjectBusinessImpl;
	
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
	public Response doSearch(ProgressPlanProjectDTO obj) {
		DataListDTO ls = progressPlanProjectBusinessImpl.doSearch(obj, request);
		return Response.ok(ls).build();
	}
	
	@Override
	public Response doSearchProvinceInPopup(CatProvinceDTO obj) {
		return Response.ok(progressPlanProjectBusinessImpl.doSearchProvinceInPopup(obj, request)).build();
	}
	
	@Override
	public Response getCntContractInHtct(ProgressPlanProjectDTO obj) {
		return Response.ok(progressPlanProjectBusinessImpl.getCntContractInHtct(obj)).build();
	}

	@Override
	public Response saveProject(ProgressPlanProjectDTO obj) throws Exception{
		try {
//			try {
				Long id;
				id = progressPlanProjectBusinessImpl.saveProject(obj, request);
				if(id==0) {
					return Response.status(Response.Status.BAD_REQUEST).build();
				} else {
					return Response.ok(Response.Status.CREATED).build();
				}
//			} catch (Exception e) {
//				return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
//			}
		} catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
	}
	
	@Override
	public Response updateProject(ProgressPlanProjectDTO obj) throws Exception{
		try {
//			try {
				Long id = progressPlanProjectBusinessImpl.updateProject(obj, request);
				if(id==0) {
					return Response.status(Response.Status.BAD_REQUEST).build();
				} else {
					return Response.ok(Response.Status.CREATED).build();
				}
//			} catch (Exception e) {
//				return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
//			}
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
	
	private String writeFileToServer(Attachment attachments, HttpServletRequest request2) {
        // TODO Auto-generated method stub
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
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
        fileName = "file_loi_" + fileName;
        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }
        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            return UFile.writeToFileServerATTT2(inputStream, fileName, folderParam, folderUpload);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }

    }
	
	@Override
    public Response importProject(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(progressPlanProjectBusinessImpl.importProject(folderUpload + filePath, folderUpload + filePath, request)).build();
            } catch (Exception e) {
                // TODO Auto-generated catch block
            	e.printStackTrace();
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
        	e.printStackTrace();
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
	
	@Override
	public Response saveImportProject(ProgressPlanProjectDTO obj) throws Exception{
		try {
			Long id = progressPlanProjectBusinessImpl.saveImportProject(obj, request);
			if(id==0) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (IllegalArgumentException e) {
        	e.printStackTrace();
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
	}

	@Override
	public Response checkDomainUser() {
		// TODO Auto-generated method stub
		return Response.ok(progressPlanProjectBusinessImpl.checkDomainUser(request)).build();
	}
	
	@Override
    public Response exportExcelProject(ProgressPlanProjectDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        try {
            String strReturn = progressPlanProjectBusinessImpl.exportExcelProject(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

	@Override
	public Response getListFile(Long objId) {
		// TODO Auto-generated method stub
		return Response.ok(progressPlanProjectBusinessImpl.getListFile(objId)).build();
	}
}
