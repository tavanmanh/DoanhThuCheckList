package com.viettel.coms.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.viettel.coms.bo.CertificateExtendBO;
import com.viettel.coms.bo.ManageCertificateBO;
import com.viettel.coms.bo.WoBO;
import com.viettel.coms.business.CertificateExtendBusinessImpl;
import com.viettel.coms.business.ManagementCareerBusinessImpl;
import com.viettel.coms.business.ManagementCertificateBusinessImpl;
import com.viettel.coms.business.UtilAttachDocumentBusinessImpl;
import com.viettel.coms.dao.CertificateExtendDAO;
import com.viettel.coms.dao.ManageCertificateDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoMappingAttachDTO;
import com.viettel.coms.dto.WoSimpleSysGroupDTO;
import com.viettel.coms.utils.BaseResponseOBJ;
import com.viettel.erp.dto.DataListDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
//Duonghv13-start 21092021
public class ManageCertificateRsServiceImpl implements ManageCertificateRsService{
	
	static Logger log = Logger.getLogger(ManageCertificateRsService.class);

	@Autowired
	ManagementCertificateBusinessImpl managementCertificateBusinessImpl;
	@Autowired
	CertificateExtendBusinessImpl certificateExtendBusinessImpl;
	
    private final String WAIT_APPROVE = "WAIT_APPROVE";
    private final String APPROVED = "APPROVED";
    private final String REJECT = "REJECT";
    private final String NOTFOUND_MSG = "NOT FOUND";
    private final String UNEXPIRED = "UNEXPIRED";
    private final String UNDER_DATE = "UNDER_DATE";
    private final String EXPIRED = "EXPIRED";
    
    private final String TTHT_ID = "242656";
    private final String TTVHKT_ID = "270120";
    private final String TTXDDTHT_ID = "166677";
    private final String TTGPTH_ID = "280483";
    private final String TTCNTT_ID = "280501";
    
    private final int SUCCESS_CODE = 1;
    private final int ERROR_CODE = -1;
    private final String SUCCESS_MSG = "SUCCESS";
    private final String ERROR_MSG = "Có lỗi xảy ra.";

    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
    @Value("${allow.file.ext}")
    private String allowFileExt;
    @Value("${folder_upload2}")
    private String folderUpload;
    @Value("${allow.folder.dir}")
    private String allowFolderDir;
    @Value("${input_image_sub_folder_upload}")
    private String input_image_sub_folder_upload;
    @Value("${input_image_folder_upload_wo}")
    private String input_image_folder_upload_wo;
    
    @Autowired
    ManageCertificateDAO managementCertificateDAO;
    @Autowired
    UtilAttachDocumentDAO utilAttachDocumentDAO;

    @Autowired
	UtilAttachDocumentBusinessImpl  utilAttachDocumentBusinessImpl;
    


	@Override
	public Response create(ManageCertificateDTO obj,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			obj.setCreatedDate(new Date());
			obj.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
			obj.setUpdatedDate(new Date());
			obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
			obj.setStatus(1l);//Xử lý  status for Date
			obj.setApproveStatus(2l);//Xử lý  status for Date			
			Long id = managementCertificateBusinessImpl.createCertificate(obj);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				CertificateExtendDTO certificateExtendDTO = new CertificateExtendDTO();
				certificateExtendDTO.setCertificateId(id);
				certificateExtendDTO.setFinishDate(obj.getFinishDate());
				certificateExtendDTO.setCreatedDate(new Date());
				certificateExtendDTO.setDescription("Gia hạn tự động khi tạo mới chứng chỉ");
				certificateExtendDTO.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
				certificateExtendDTO.setUpdatedDate(new Date());
				certificateExtendDTO.setUpdatedUserId((objUser.getVpsUserInfo().getSysUserId()));
				certificateExtendDTO.setStatus(2l);//Xử lý  status for Date
				Long id1 = certificateExtendBusinessImpl.createExtend(certificateExtendDTO,request);
				if (id1 == 0l) {
					return Response.status(Response.Status.BAD_REQUEST).build();
				}
				else {

					UtilAttachDocumentDTO utilAttachDocumentDTO = obj.getUtilAttachDocumentDTO();
					utilAttachDocumentDTO.setObjectId(id1);
					Long id2 = utilAttachDocumentBusinessImpl.save(obj.getUtilAttachDocumentDTO());
					if (id2 == 0l) {
			            return Response.status(Response.Status.BAD_REQUEST).build();
			        } else {
			        	return Response.ok(Response.Status.CREATED).build();
			        }
				}
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response update(ManageCertificateDTO obj,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			obj.setUpdatedDate(new Date());
			obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
			obj.setStatus(1l);
			Long id = managementCertificateBusinessImpl.updateCertificate(obj);

			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				CertificateExtendDTO certificateExtendDTO = certificateExtendBusinessImpl.getById(obj.getCertificateExtendId());
				
				certificateExtendDTO.setFinishDate(obj.getFinishDate());
				certificateExtendDTO.setUpdatedDate(new Date());
				certificateExtendDTO.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
				certificateExtendDTO.setStatus(2l);
				Long id2 = certificateExtendBusinessImpl.updateExtend(certificateExtendDTO,request);
				if (id2 == 0l) {
					return Response.status(Response.Status.BAD_REQUEST).build();
				} else {
					UtilAttachDocumentDTO utilAttachDocumentDTO = obj.getUtilAttachDocumentDTO();
					utilAttachDocumentDTO.setObjectId(certificateExtendDTO.getCertificateExtendId());
					certificateExtendDTO.setUtilAttachDocumentDTO(utilAttachDocumentDTO);
					utilAttachDocumentBusinessImpl.deleteExtend(certificateExtendDTO);
					Long id3 = utilAttachDocumentBusinessImpl.save(obj.getUtilAttachDocumentDTO());
					if (id3 == 0l) {
			            return Response.status(Response.Status.BAD_REQUEST).build();
			        } else {
			        	return Response.ok(Response.Status.CREATED).build();
			        }
				}
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response delete(ManageCertificateDTO obj) throws Exception {
		// TODO Auto-generated method stub
        BaseResponseOBJ resp;

        ManageCertificateDTO certificateDTO = managementCertificateBusinessImpl.getOneCertificateDetails(obj.getCertificateId());
        if (certificateDTO == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        int rowAffected = managementCertificateBusinessImpl.deleteCertificate(obj.getCertificateId());
        if (rowAffected > 0) {
        	obj.setUpdatedDate(new Date());
			obj.setUpdatedUserId(Long.parseLong(obj.getLoggedInUser()));
			obj.setStatus(0l);
        }
        CertificateExtendDTO certificateExtendDTO = new CertificateExtendDTO();
        certificateExtendDTO.setCertificateExtendId(certificateDTO.getCertificateExtendId());
        utilAttachDocumentBusinessImpl.deleteExtend(certificateExtendDTO);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, rowAffected);
        return Response.ok(resp).build();
		
	}

	@Override
	public Response getById(long Id) throws Exception {
		// TODO Auto-generated method stub
		ManageCertificateDTO obj = (ManageCertificateDTO) managementCertificateBusinessImpl.getById(Id);
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(obj).build();
		}
	}

	@Override
	public Response exportFile(ManageCertificateDTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
            String strReturn = managementCertificateBusinessImpl.exportCertificate(obj);
    		return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
	}

	@Override
	public Response getOneCertificateDetails(ManageCertificateDTO certificateDto) throws Exception {
		// TODO Auto-generated method stub
		Long Id = certificateDto.getCertificateId();
        BaseResponseOBJ resp;
        if (Id == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        ManageCertificateDTO obj = managementCertificateBusinessImpl.getOneCertificateDetails(Id);
        CertificateExtendDTO one = new CertificateExtendDTO();
        one.setCertificateId(Id);
        one.setPage(1l);
        one.setPageSize(10); 
        List<CertificateExtendDTO> listSearch = certificateExtendBusinessImpl.doSearch(one);
        obj.setTotal(listSearch.size());
        one.setCertificateExtendId(listSearch.get(0).getCertificateExtendId());
        UtilAttachDocumentDTO result  = certificateExtendBusinessImpl.getResultFileByExtendId(one);
        obj.setUtilAttachDocumentDTO(result);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, obj);
        return Response.ok(resp).build();
	}

	@Override
	public Response doSearch(ManageCertificateDTO obj, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		boolean checkRoleApprove = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.WOXL, request);
        boolean checkRoleCNKT = VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATED,
                Constant.AdResourceKey.WO_UCTT, request);
        if (checkRoleCNKT) {
            String sysGroupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATED,
                    Constant.AdResourceKey.WO_UCTT, request);
            List<String> groupIdList = ConvertData.convertStringToList(sysGroupId, ",");
            if(groupIdList.contains(objUser.getVpsUserInfo().getSysGroupId().toString())) {
            	obj.setSysGroupId(Long.parseLong(sysGroupId));
            }
            else{
            	obj.setSysGroupId(objUser.getVpsUserInfo().getSysGroupId());
            }
        }
		List<ManageCertificateDTO> ls = managementCertificateBusinessImpl.doSearch(obj);
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
	public Response doSearchMappingAttach(UtilAttachDocumentDTO dto) throws Exception {
		if (dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        dto.setPageSize(100);
        List<UtilAttachDocumentDTO> fileList = utilAttachDocumentDAO.doSearchOrderby(dto);
        List<UtilAttachDocumentDTO> finalFileList = new ArrayList<>();
        for (UtilAttachDocumentDTO file : fileList) {
            finalFileList.add(file);
        }
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, finalFileList);
        return Response.ok(resp).build();
	}


	@Override
	public Response checkRoleVHKTApprove(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.WOXL, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(groupIdList.contains("270120")) {
        	return Response.ok(Response.Status.OK).build();
        } else {
        	return Response.ok(Response.Status.UNAUTHORIZED).build();
        }
	}

	@Override
	public Response checkRoleCNKT(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATED,
                Constant.AdResourceKey.WO_UCTT, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(!groupIdList.contains("270120")) {
        	return Response.ok(Response.Status.OK).build();
        } else {
        	return Response.ok(Response.Status.UNAUTHORIZED).build();
        }
	}

	@Override
	public Response getCertificatePopup(ManageCertificateDTO woDto) {
		// TODO Auto-generated method stub
		return Response.ok(managementCertificateBusinessImpl.getForAutoCompleteInSign(woDto)).build();
	}
	
}
//Duonghv13-end 21092021
