package com.viettel.coms.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.coms.bo.ManageCertificateBO;
import com.viettel.coms.business.CertificateExtendBusinessImpl;
import com.viettel.coms.business.ManagementCareerBusinessImpl;
import com.viettel.coms.business.ManagementCertificateBusinessImpl;
import com.viettel.coms.business.UtilAttachDocumentBusinessImpl;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.utils.BaseResponseOBJ;
import com.viettel.erp.business.UtilAttachedDocumentsBusinessImpl;
import com.viettel.erp.dto.DataListDTO;
import com.viettel.ktts2.dto.KttsUserSession;
//duonghv13-start 22092021
public class CertificateExtendRsServiceImpl implements CertificateExtendRsService {

	protected final Logger log = Logger.getLogger(CertificateExtendRsService.class);
	@Context
	HttpServletRequest request;
	
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

	@Autowired
	CertificateExtendBusinessImpl certificateExtendBusinessImpl;
	@Autowired
	ManagementCertificateBusinessImpl managementCertificateBusinessImpl;
	
	@Autowired
    UtilAttachDocumentDAO utilAttachDocumentDAO;
	
	@Autowired
	UtilAttachDocumentBusinessImpl utilAttachDocumentBusinessImpl;

	@Override
	public Response doSearch(CertificateExtendDTO obj) {
		// TODO Auto-generated method stub
		List<CertificateExtendDTO> ls = certificateExtendBusinessImpl.doSearch(obj);
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
	public Response remove(CertificateExtendDTO obj) {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			obj.setUpdatedDate(new Date());
			obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
			obj.setStatus(0l);
			utilAttachDocumentBusinessImpl.deleteExtend(obj);
			Long id = certificateExtendBusinessImpl.updateExtend(obj,request);
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
	public Response add(CertificateExtendDTO obj) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {	
			obj.setCreatedDate(new Date());
			String s = objUser.getSysUserId().toString();
			obj.setCreatedUserId(Long.parseLong(s));
			obj.setStatus(1l);
			obj.setUpdatedUserId(Long.parseLong(s));
			obj.setUpdatedDate(new Date());
			Long id1 = certificateExtendBusinessImpl.createExtend(obj,request);
			if (id1 == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				
				obj.getUtilAttachDocumentDTO().setObjectId(id1);
				Long id = utilAttachDocumentBusinessImpl.save(obj.getUtilAttachDocumentDTO());
				if (id == 0l) {
		            return Response.status(Response.Status.BAD_REQUEST).build();
		        } else {
		        	ManageCertificateDTO result = managementCertificateBusinessImpl.getById(obj.getCertificateId());
		        	result.setApproveStatus(1l);
		        	result.setUpdatedDate(new Date());
		        	result.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
		        	Long id2 = managementCertificateBusinessImpl.updateCertificate(result);
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
	public Response update(CertificateExtendDTO obj) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			
			obj.setUpdatedDate(new Date());
			obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
			Long id = certificateExtendBusinessImpl.updateExtend(obj,request);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				obj.getUtilAttachDocumentDTO().setObjectId(obj.getCertificateExtendId());
				utilAttachDocumentBusinessImpl.deleteExtend(obj);
				Long id1 = utilAttachDocumentBusinessImpl.save(obj.getUtilAttachDocumentDTO());
				if (id1 == 0l) {
		            return Response.status(Response.Status.BAD_REQUEST).build();
		        } else {
		        	return Response.ok(Response.Status.CREATED).build();
		        }
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}
	
	
	

	@Override
	public Response getById(Long id) throws Exception {
		// TODO Auto-generated method stub
		CertificateExtendDTO obj = (CertificateExtendDTO) certificateExtendBusinessImpl.getById(id);
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(obj).build();
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
	public Response getResultFileByExtendId(CertificateExtendDTO obj) {
		// TODO Auto-generated method stub
		UtilAttachDocumentDTO ls = certificateExtendBusinessImpl.getResultFileByExtendId(obj);
		return Response.ok(ls).build();
	}

	@Override
	public Response accept(CertificateExtendDTO obj) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			
			obj.setUpdatedDate(new Date());
			obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
			Long id = certificateExtendBusinessImpl.updateExtend(obj,request);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
		        	ManageCertificateDTO result = managementCertificateBusinessImpl.getById(obj.getCertificateId());
		        	result.setFinishDate(obj.getFinishDate());
		        	result.setApproveStatus(2l);
		        	result.setUpdatedDate(new Date());
		        	result.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
		        	Long id2 = managementCertificateBusinessImpl.updateCertificate(result);
					if (id2 == 0l) {
						return Response.status(Response.Status.BAD_REQUEST).build();
					} else {
						return Response.ok(Response.Status.CREATED).build();
					}
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response reject(CertificateExtendDTO obj) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {

			obj.setUpdatedDate(new Date());
			obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
			Long id = certificateExtendBusinessImpl.updateExtend(obj,request);
			if (id == 0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				ManageCertificateDTO result = managementCertificateBusinessImpl.getById(obj.getCertificateId());
				result.setApproveStatus(3l);
				result.setUpdatedDate(new Date());
				result.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
				Long id2 = managementCertificateBusinessImpl.updateCertificate(result);
				if (id2 == 0l) {
					return Response.status(Response.Status.BAD_REQUEST).build();
				} else {
					return Response.ok(Response.Status.CREATED).build();
				}
			}
		} catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}
	
	// Duonghv13 end-16/09/2021//
}
