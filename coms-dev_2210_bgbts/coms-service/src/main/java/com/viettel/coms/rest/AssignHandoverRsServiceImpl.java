package com.viettel.coms.rest;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.viettel.coms.business.AssignHandoverBusinessImpl;
import com.viettel.coms.business.ConstructionBusinessImpl;
import com.viettel.coms.dto.*;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.wms.business.UserRoleBusinessImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts.vps.VpsUserToken;

import org.springframework.beans.factory.annotation.Value;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//VietNT_20181210_created
public class AssignHandoverRsServiceImpl implements AssignHandoverRsService {

    protected final Logger log = Logger.getLogger(RpHSHCRsServiceImpl.class);

    @Autowired
    private AssignHandoverBusinessImpl assignHandoverBusinessImpl;

    @Autowired
    private UserRoleBusinessImpl userRoleBusinessImpl;

    @Autowired
    private ConstructionBusinessImpl constructionBusiness;

    @Context
    private HttpServletRequest request;

    @Value("${folder_upload2}")
    private String folderUpload;

    @Value("${allow.file.ext}")
    private String allowFileExt;

    @Value("${allow.folder.dir}")
    private String allowFolderDir;

    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;


    //VietNT_20190109_start
    @Override
    public Response doSearch(AssignHandoverDTO obj) {
        DataListDTO data = assignHandoverBusinessImpl.doSearch(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response addNewAssignHandover(AssignHandoverDTO dto) {
        KttsUserSession session = userRoleBusinessImpl.getUserSession(request);
        try {
            Long sysUserId = session.getVpsUserInfo().getSysUserId();
            Date today = new Date();
            dto.setCreateUserId(sysUserId);
            dto.setCreateDate(today);
            dto.setCompanyAssignDate(today);
            dto.setStatus(1L);
            dto.setReceivedStatus(1L);
            Long id = assignHandoverBusinessImpl.addNewAssignHandover(dto,request);

            if (id == 0L) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                return Response.ok(Response.Status.CREATED).build();
            }
        } catch (Exception e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response removeAssignHandover(AssignHandoverDTO obj) {
        KttsUserSession session = userRoleBusinessImpl.getUserSession(request);
        assignHandoverBusinessImpl.deleteDataRpStation(obj);
        Boolean check = assignHandoverBusinessImpl.checkRolePermissionSMS(request);
        VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
        if(check) {
        	List<AssignHandoverDTO> lstAssign = assignHandoverBusinessImpl.checkGroupList(obj,request);
        	if(lstAssign.size()>0) {
        		for(AssignHandoverDTO  assign : lstAssign) {
        			obj.setUserNumberPhone(token.getPhoneNumber());
        			obj.setUserEmail(token.getEmail());
        			obj.setCreateUserId(token.getSysUserId());
        			obj.setCreatedGroupId(session.getVpsUserInfo().getSysGroupId());
        			assignHandoverBusinessImpl.insertSmsemail(obj);
        		}
        	}
        }
        String result = assignHandoverBusinessImpl.removeAssignHandover(obj.getAssignHandoverId(), session.getVpsUserInfo().getSysUserId());
        if (result != "SUCCESS") {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response attachDesignFileEdit(AssignHandoverDTO dto) throws Exception {
        try {
            Long id = assignHandoverBusinessImpl.updateAttachFileDesign(dto);

            if (id == 0L) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                return Response.ok(Response.Status.CREATED).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response getById(Long id) throws Exception {
    	DataListDTO res = assignHandoverBusinessImpl.getAttachFile(id);
        return Response.ok(res).build();
    }

    private String getFolderParam(HttpServletRequest request) {
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!UString.isFolderAllowFolderSave(folderParam, allowFolderDir)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }
        return folderParam;
    }

    private String uploadToServer(Attachment attachments, String folderParam) {
        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);

        if (!UString.isExtendAllowSave(fileName, allowFileExt)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }

        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream()) {
            // upload to server, return file path
            return UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }
    }

    @Override
    public Response importExcel(Attachment attachments, HttpServletRequest request) throws Exception {
        KttsUserSession session = userRoleBusinessImpl.getUserSession(request);
        String folderParam = this.getFolderParam(request);
        String filePath = this.uploadToServer(attachments, folderParam);
        try {
            List<AssignHandoverDTO> result = assignHandoverBusinessImpl.doImportExcel(filePath, session.getVpsUserInfo().getSysUserId());
            if (result != null && !result.isEmpty()
                    && (result.get(0).getErrorList() == null
                    || result.get(0).getErrorList().size() == 0)) {
                assignHandoverBusinessImpl.addNewAssignHandoverImport(result);
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

    @Override
    public Response downloadTemplate() throws Exception {
    	try {
    		String filePath = assignHandoverBusinessImpl.downloadTemplate();
    		return Response.ok(Collections.singletonMap("fileName", filePath)).build();
    	} catch (Exception e) {
    		log.error(e);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    //VietNT_20181218_start
    @Override
    public Response readFileConstructionCode(Attachment attachments, HttpServletRequest request) {
        List<String> constructionCodeList = null;
        try {
            constructionCodeList = constructionBusiness.readFileConstruction(attachments);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(Collections.singletonMap("constructionCodeList", constructionCodeList)).build();
    }

    @Override
    public Response doAssignHandover(AssignHandoverDTO dto) {
        KttsUserSession session = userRoleBusinessImpl.getUserSession(request);
        Long sysUserId = session.getVpsUserInfo().getSysUserId();
        Long createdGroupId = session.getVpsUserInfo().getSysGroupId();
        Long result = assignHandoverBusinessImpl.doAssignHandover(dto, sysUserId, createdGroupId);
        if (result == 0L) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response doSearchNV(AssignHandoverDTO obj) {
        String sysGroupIdStr = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.ASSIGN, Constant.AdResourceKey.TASK, request);
//        String sysGroupIdStr = "166617";
        List<String> groupIdList = ConvertData.convertStringToList(sysGroupIdStr, ",");
        DataListDTO data = new DataListDTO();
        if (StringUtils.isNotEmpty(sysGroupIdStr)) {
            data = assignHandoverBusinessImpl.doSearchNV(obj, groupIdList);
        } else {
            data.setData(new ArrayList<>());
            data.setTotal(0);
            data.setSize(10);
            data.setStart(1);
        }
        return Response.ok(data).build();
    }

    @Override
    public Response getListImageHandover(Long handoverId) {
        try {
            List<UtilAttachDocumentDTO> imageList = assignHandoverBusinessImpl.getListImageHandover(handoverId);
            return Response.ok().entity(Collections.singletonMap("imgList", imageList)).build();
//            return Response.ok(imageList).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra trong quá trình lấy dữ liệu")).build();
        }
    }

    @Override
    public Response getConstructionProvinceByCode(String constructionCode) {
        ConstructionDetailDTO construction = assignHandoverBusinessImpl.getConstructionProvinceByCode(constructionCode);
        return Response.ok(Collections.singletonMap("construction", construction)).build();
    }

    @Override
    public Response getForSysUserAutoComplete(SysUserCOMSDTO obj) {
    	DataListDTO result = assignHandoverBusinessImpl.getForSysUserAutoComplete(obj);
        return Response.ok(result).build();
    }

    @Override
    public Response updateWorkItemPartner(ConstructionTaskDetailDTO dto) {
            Long id = assignHandoverBusinessImpl.updateWorkItemConstructor(dto);

            if (id < 0) {
            	return Response.ok().entity(Collections.singletonMap("error", "Lỗi xảy ra khi cập nhật hạng mục")).build();
            } else {
                return Response.ok(Response.Status.CREATED).build();
            }
    }

    //VietNT_end
	//VietNT_20180225_start
	@Override
	public Response exportHandoverNV(AssignHandoverDTO dto) {
        Response res;
        try {
        	String sysGroupIdStr = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.ASSIGN, Constant.AdResourceKey.TASK, request);
        	List<String> groupIdList = ConvertData.convertStringToList(sysGroupIdStr, ",");
            String filePath = assignHandoverBusinessImpl.exportHandoverNV(dto, groupIdList);
            if (StringUtils.isEmpty(filePath)) {
                res = this.buildErrorResponse("Không có dữ liệu!");
            } else {
                res = Response.ok(Collections.singletonMap("fileName", filePath)).build();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            res = this.buildErrorResponse("Không tìm thấy biểu mẫu");
        } catch (Exception e) {
            e.printStackTrace();
            res = this.buildErrorResponse("Có lỗi xảy ra trong quá trình xuất file!");
        }
        return res;
	}

    private Response buildErrorResponse(String message) {
        return Response.ok().entity(Collections.singletonMap("error", message)).build();
    }
    //VietNT_end
    
    //Huypq-20190315-start
    @Override
	public Response checkStationContractBGMB(AssignHandoverDTO dto) {
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.checkStationContractBGMB(dto)).build();
	}
    
    @Override
    public Response doSearchTTKT(AssignHandoverDTO obj, HttpServletRequest request) {
        DataListDTO data = assignHandoverBusinessImpl.doSearchTTKT(obj,request);
        return Response.ok(data).build();
    }
    
    @Override
	public Response updateSysGroupInAssignHandover(AssignHandoverDTO obj) {
    	Boolean check = assignHandoverBusinessImpl.checkRolePermissionSMS(request);
        VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
        if(check) {
        	List<AssignHandoverDTO> lstAssign = assignHandoverBusinessImpl.checkGroupList(obj,request);
        	if(lstAssign.size()>0) {
//        		for(AssignHandoverDTO  assign : lstAssign) {
        			obj.setUserNumberPhone(token.getPhoneNumber());
        			obj.setUserEmail(token.getEmail());
        			assignHandoverBusinessImpl.insertSmsemailTTKT(obj,request);
//        		}
        	}
        }
		return Response.ok(assignHandoverBusinessImpl.updateSysGroupInAssignHandover(obj)).build();
	}
    
    @Override
	public Response removeAssignById(Long id) {
		return Response.ok(assignHandoverBusinessImpl.removeAssignById(id)).build();
	}
    
    @Override
	public Response getCheckDataWorkItem(AssignHandoverDTO dto) {
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.getCheckDataWorkItem(dto)).build();
	}

	@Override
	public Response checkStationContract(AssignHandoverDTO dto) {
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.checkStationContract(dto)).build();
	}

	@Override
	public Response removeAssignHandoverById(AssignHandoverDTO dto) {
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.removeAssignHandoverById(dto)).build();
	}

	@Override
	public Response getListHouseType(AssignHandoverDTO dto) {
		DataListDTO result = assignHandoverBusinessImpl.getListHouseType(dto);
        return Response.ok(result).build();
	}

	@Override
	public Response getListGroundingType(AssignHandoverDTO dto) {
		DataListDTO result = assignHandoverBusinessImpl.getListGroundingType(dto);
        return Response.ok(result).build();
	}

	@Override
	public Response updateEditDataAssignHandoverNv(AssignHandoverDTO dto) {
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.updateEditDataAssignHandoverNv(dto)).build();
	}

	@Override
	public Response updateAssignHandoverVuong(AssignHandoverDTO dto) throws Exception{
		// TODO Auto-generated method stub\
		Long id = assignHandoverBusinessImpl.updateAssignHandoverVuong(dto,request);
		return Response.ok(id).build();
	}
	
	@Override
	public Response updateAssignHandoverVtmd(AssignHandoverDTO dto) throws Exception{
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.updateAssignHandoverVtmd(dto,request)).build();
	}
	
	@Override
	public Response updateAssignHandoverVuongVtmd(AssignHandoverDTO dto) throws Exception{
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.updateAssignHandoverVuongVtmd(dto,request)).build();
	}
	
	@Override
	public Response updateAssignHandoverNotVuongVtmd(AssignHandoverDTO dto) throws Exception{
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.updateAssignHandoverNotVuongVtmd(dto,request)).build();
	}
	
	@Override
	public Response downloadTemplateTTKT() throws Exception {
		try {
			String filePath = assignHandoverBusinessImpl.downloadTemplateTTKT();
			return Response.ok(Collections.singletonMap("fileName", filePath)).build();
		} catch (Exception e) {
			log.error(e);
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
    public Response importExcelTTKT(Attachment attachments, HttpServletRequest request) throws Exception {
        KttsUserSession session = userRoleBusinessImpl.getUserSession(request);
        String folderParam = this.getFolderParam(request);
        String filePath = this.uploadToServer(attachments, folderParam);
        Boolean check = assignHandoverBusinessImpl.checkRolePermissionSMS(request);
        VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
        String idClient = UString.getSafeFileName(request.getParameter("id"));
        try {
            List<AssignHandoverDTO> result = assignHandoverBusinessImpl.doImportExcelTTKT(filePath, session.getVpsUserInfo().getSysUserId(),request);
            if (result != null && !result.isEmpty()
                    && (result.get(0).getErrorList() == null
                    || result.get(0).getErrorList().size() == 0)) {
            	if(check) {
            			
            			assignHandoverBusinessImpl.addNewAssignHandoverImportTTKT(result);
                    		for(AssignHandoverDTO  assign : result) {
                    			assignHandoverBusinessImpl.insertSmsemailImportTTKT(assign,request);
                    		}
                    	}
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

	@Override
	public void checkWorkItemConsTask(AssignHandoverDTO dto) {
		// TODO Auto-generated method stub
		assignHandoverBusinessImpl.checkWorkItemConsTask(dto,request);
	}

	@Override
	public void updateWorkItem(AssignHandoverDTO dto) {
		// TODO Auto-generated method stub
		assignHandoverBusinessImpl.updateWorkItem(dto, request);
	}

	@Override
	public Response checkStationBGMB(AssignHandoverDTO list) {
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.checkStationBGMB(list)).build();
	}
	
	@Override
	public Response downloadTemplateBGMB() throws Exception {
		try {
			String filePath = assignHandoverBusinessImpl.downloadTemplateBGMB();
			return Response.ok(Collections.singletonMap("fileName", filePath)).build();
		} catch (Exception e) {
			log.error(e);
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}
	
	@Override
    public Response importExcelBGMB(Attachment attachments, HttpServletRequest request) throws Exception {
        KttsUserSession session = userRoleBusinessImpl.getUserSession(request);
        String folderParam = this.getFolderParam(request);
        String filePath = this.uploadToServer(attachments, folderParam);
//        Boolean check = assignHandoverBusinessImpl.checkRolePermissionSMS(request);
        VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
        String idClient = UString.getSafeFileName(request.getParameter("id"));
        try {
            List<AssignHandoverDTO> result = assignHandoverBusinessImpl.doImportExcelBGMB(filePath, session.getVpsUserInfo().getSysUserId(),request);
            if (result != null && !result.isEmpty()
                    && (result.get(0).getErrorList() == null
                    || result.get(0).getErrorList().size() == 0)) {
//            	if(check) {
            			assignHandoverBusinessImpl.updateAssignHandoverImportBGMB(result);
                    		for(AssignHandoverDTO  assign : result) {
                    			assignHandoverBusinessImpl.insertSmsemailImportBGMB(assign,request);
                    		}
//                    	}
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
	
	@Override
    public Response exportAssignHandoverCN(AssignHandoverDTO obj) throws Exception {
        try {
            String strReturn = assignHandoverBusinessImpl.exportAssignHandoverCN(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
    public Response exportAssignHandoverTTKT(AssignHandoverDTO obj) throws Exception {
        try {
            String strReturn = assignHandoverBusinessImpl.exportAssignHandoverTTKT(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

	@Override
	public Response findSignStateGoodsPlanByConsId(Long id) {
		// TODO Auto-generated method stub
		return Response.ok(assignHandoverBusinessImpl.findSignStateGoodsPlanByConsId(id)).build();
	}

	@Override
	public void updateConstructionTuyen(AssignHandoverDTO dto) {
		// TODO Auto-generated method stub
		assignHandoverBusinessImpl.updateConstructionTuyen(dto,request);
	}
	
   //Huy-end

	//Huy-20190828-start
	@Override
    public Response getDataReportStartInMonth(AssignHandoverDTO obj) {
        DataListDTO data = assignHandoverBusinessImpl.getDataReportStartInMonth(obj);
        return Response.ok(data).build();
    }
	//Huy-end
}
