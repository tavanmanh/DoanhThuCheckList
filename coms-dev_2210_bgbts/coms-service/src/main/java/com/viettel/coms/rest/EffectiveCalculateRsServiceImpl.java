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

import com.viettel.coms.business.CalculateEfficiencyHtctBusinessImpl;
import com.viettel.coms.business.CapexBtsHtctBusinessImpl;
import com.viettel.coms.business.CapexSourceHTCTBusinessImpl;
import com.viettel.coms.business.Cost1477HtctBusinessImpl;
import com.viettel.coms.business.CostVtnetHtctBusinessImpl;
import com.viettel.coms.business.GpmbHtctBusinessImpl;
import com.viettel.coms.business.OfferHtctBusinessImpl;
import com.viettel.coms.business.RatioDeliveryHtctBusinessImpl;
import com.viettel.coms.business.WaccHTCTBusinessImpl;
import com.viettel.coms.dto.CalculateEfficiencyHtctDTO;
import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.coms.dto.CapexSourceHTCTDTO;
import com.viettel.coms.dto.Cost1477HtctDTO;
import com.viettel.coms.dto.CostVtnetHtctDTO;
import com.viettel.coms.dto.GpmbHtctDTO;
import com.viettel.coms.dto.OfferHtctDTO;
import com.viettel.coms.dto.RatioDeliveryHtctDTO;
import com.viettel.coms.dto.WaccHtctDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;

public class EffectiveCalculateRsServiceImpl implements EffectiveCalculateRsService {
	@Context
	HttpServletRequest request;
	@Autowired
	CapexSourceHTCTBusinessImpl capexSourceHTCTBusinessImpl;
	@Autowired
	WaccHTCTBusinessImpl waccHTCTBusinessImpl;
	@Autowired
	RatioDeliveryHtctBusinessImpl ratioDeliveryHtctBusinessImpl;
	@Autowired
	GpmbHtctBusinessImpl gpmbHtctBusinessImpl;
	@Autowired
	CostVtnetHtctBusinessImpl costVtnetHtctBusinessImpl;
	@Autowired
	Cost1477HtctBusinessImpl cost1477HtctBusinessImpl;
	@Autowired
	CapexBtsHtctBusinessImpl capexBtsHtctBusinessImpl;
	@Autowired
	OfferHtctBusinessImpl offerHtctBusinessImpl;
	@Autowired
	CalculateEfficiencyHtctBusinessImpl calculateEfficiencyHtctBusinessImpl;
	@Value("${folder_upload2}")
	private String folderUpload;

	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	@Value("${allow.folder.dir}")
	private String allowFolderDir;

	@Value("${allow.file.ext}")
	private String allowFileExt;

	@Override
	public Response getCapexSource(CapexSourceHTCTDTO obj) {
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<CapexSourceHTCTDTO> ls = capexSourceHTCTBusinessImpl.getCapexSource(obj);
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

	@Override
	public Response getData2(WaccHtctDTO obj) {
		List<WaccHtctDTO> ls = waccHTCTBusinessImpl.getData2(obj);
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

	@Override
	public Response getData3(RatioDeliveryHtctDTO obj) {
		List<RatioDeliveryHtctDTO> ls = ratioDeliveryHtctBusinessImpl.getData3(obj);
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

	@Override
	public Response exportData3(RatioDeliveryHtctDTO obj) throws Exception {
		try {
			String strReturn = ratioDeliveryHtctBusinessImpl.exportData3(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response getData4(GpmbHtctDTO obj) {
		List<GpmbHtctDTO> ls = gpmbHtctBusinessImpl.getData4(obj);
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

	@Override
	public Response getData5(CostVtnetHtctDTO obj) {
		List<CostVtnetHtctDTO> ls = costVtnetHtctBusinessImpl.getData5(obj);
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

	@Override
	public Response getData6(Cost1477HtctDTO obj) {
		List<Cost1477HtctDTO> ls = cost1477HtctBusinessImpl.getData6(obj);
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

	@Override
	public Response getData7(CapexBtsHtctDTO obj) {
		List<CapexBtsHtctDTO> ls = capexBtsHtctBusinessImpl.getData7(obj);
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

	@Override
	public Response getData8(OfferHtctDTO obj) {
		List<OfferHtctDTO> ls = offerHtctBusinessImpl.getData8(obj);
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

	@Override
	public Response getData9(CalculateEfficiencyHtctDTO obj) {
		List<CalculateEfficiencyHtctDTO> ls = calculateEfficiencyHtctBusinessImpl.getData9(obj);
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

	@Override
	public Response exportData5(CostVtnetHtctDTO obj) throws Exception {
		try {
			String strReturn = costVtnetHtctBusinessImpl.exportData5(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response downloadTemplateCapexNguon(CapexSourceHTCTDTO obj) throws Exception {
		try {
			String strReturn = capexSourceHTCTBusinessImpl.downloadTemplateCapexNguon(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response downloadTemplateCapex(CapexBtsHtctDTO obj) throws Exception {
		try {
			String strReturn = capexBtsHtctBusinessImpl.downloadTemplateCapex(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response downloadTemplateChaoGia(OfferHtctDTO obj) throws Exception {
		try {
			String strReturn = offerHtctBusinessImpl.downloadTemplateChaoGia(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response downloadTemplateWACC(WaccHtctDTO obj) throws Exception {
		try {
			String strReturn = waccHTCTBusinessImpl.downloadTemplateWACC(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response downloadTemplateTLGK(RatioDeliveryHtctDTO obj) throws Exception {
		try {
			String strReturn = ratioDeliveryHtctBusinessImpl.downloadTemplateTLGK(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response downloadTemplateGiaThue1477(Cost1477HtctDTO obj) throws Exception {
		try {
			String strReturn = cost1477HtctBusinessImpl.downloadTemplateGiaThue1477(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response downloadTemplateEffectiveCalculate(CalculateEfficiencyHtctDTO obj) throws Exception {
		try {
			String strReturn = calculateEfficiencyHtctBusinessImpl.downloadTemplateEffectiveCalculate(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response downloadTemplateDgiaThueVTNet(CostVtnetHtctDTO obj) throws Exception {
		try {
			String strReturn = costVtnetHtctBusinessImpl.downloadTemplateDgiaThueVTNet(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Response downloadTemplateGPMB(GpmbHtctDTO obj) throws Exception {
		try {
			String strReturn = gpmbHtctBusinessImpl.downloadTemplateGPMB(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {

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
	public Response importCapexNguon(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<CapexSourceHTCTDTO> result = capexSourceHTCTBusinessImpl.importCapexNguon(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(CapexSourceHTCTDTO dto : result) {
					if(dto.getCapexSourceId() != null) {
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
//						dto.setCreatedDate(dto.getCreatedDate());
//						dto.setCreatedUserId(dto.getCreatedUserId());
						
						capexSourceHTCTBusinessImpl.updateCapex(dto.getUnit(), dto.getCostCapex() , dto.getCapexSourceId(), dto.getContentCapex(), dto.getUpdatedDate(), dto.getUpdateUserId());
					} else { 
						dto.setCreatedDate(new Date());
						dto.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
						capexSourceHTCTBusinessImpl.saveCapex(dto);
					}
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Response.Status.CONFLICT).build();
		}
	}

	@Override
	public Response importCapex(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<CapexBtsHtctDTO> result = capexBtsHtctBusinessImpl.importCapex(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(CapexBtsHtctDTO dto : result) {
					if(dto.getCapexBtsId() != null) {
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
						capexBtsHtctBusinessImpl.updateCapex(dto.getItemType(), dto.getItem() , dto.getWorkCapex(), dto.getProvinceCode(), dto.getCostCapexBts(), dto.getCapexBtsId(), dto.getUpdatedDate(), dto.getUpdateUserId());
					} else { 
						dto.setCreatedDate(new Date());
						dto.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
						dto.setProvinceId(capexBtsHtctBusinessImpl.getProvinceIdForImport(dto.getProvinceCode()));
						capexBtsHtctBusinessImpl.saveCapex(dto);
					}
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Response.Status.CONFLICT).build();
		}
	}

	@Override
	public Response importWACC(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<WaccHtctDTO> result = waccHTCTBusinessImpl.importWACC(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(WaccHtctDTO dto : result) {
					if(dto.getWaccId() != null) {
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
						waccHTCTBusinessImpl.updateWacc(dto.getWaccRex(), dto.getWaccName(), dto.getWaccId(), dto.getUpdatedDate(), dto.getUpdateUserId());
					} else { 
						dto.setCreatedDate(new Date());
						dto.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
						waccHTCTBusinessImpl.saveWacc(dto);
					}
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Response.Status.CONFLICT).build();
		}
	}

	@Override
	public Response importChaoGia(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<OfferHtctDTO> result = offerHtctBusinessImpl.importChaoGia(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(OfferHtctDTO dto : result) {
					if(dto.getOfferId() != null) {
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
						offerHtctBusinessImpl.updateOffer(dto.getCategoryOffer(), dto.getSymbol(), dto.getUnit(), dto.getOfferId(), dto.getUpdatedDate(), dto.getUpdateUserId());
					} else { 
						dto.setCreatedDate(new Date());
						dto.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
						offerHtctBusinessImpl.saveOffer(dto);
					}
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Response.Status.CONFLICT).build();
		}
	}

	@Override
	public Response importTLGK(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<RatioDeliveryHtctDTO> result = ratioDeliveryHtctBusinessImpl.importTLGK(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(RatioDeliveryHtctDTO dto : result) {
					if(dto.getRatioDeliveryId() != null) {
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
						dto.setCreatedDate(dto.getCreatedDate());
						dto.setCreatedUserId(dto.getCreatedUserId());
						dto.setCatProvinceId(dto.getCatProvinceId());
						ratioDeliveryHtctBusinessImpl.updateTLGK(dto);
					} else { 
						dto.setCreatedDate(new Date());
						dto.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
						dto.setCatProvinceId(ratioDeliveryHtctBusinessImpl.getProvinceId(dto.getCatProvinceCode()));
						ratioDeliveryHtctBusinessImpl.saveTLGK(dto);
					}
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Response.Status.CONFLICT).build();
		}
		
	}
	
	@Override
	public Response importGiaThue1477(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<Cost1477HtctDTO> result = cost1477HtctBusinessImpl.importGiaThue1477(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(Cost1477HtctDTO dto : result) {
					if(dto.getCost1477HtctId() != null) {
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
						dto.setCreatedDate(dto.getCreatedDate());
						dto.setCreatedUserId(dto.getCreatedUserId());
						cost1477HtctBusinessImpl.updateCost1477(dto);
					} else { 
						dto.setCreatedDate(new Date());
						dto.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
						cost1477HtctBusinessImpl.saveCost1477(dto);
					}
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Response.Status.CONFLICT).build();
		}
	}

	@Override
	public Response importEffectiveCalculate(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<CalculateEfficiencyHtctDTO> result = calculateEfficiencyHtctBusinessImpl.importEffectiveCalculate(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(CalculateEfficiencyHtctDTO dto : result) {
					if(dto.getCalculateEfficiencyId() != null) {
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
						dto.setCreatedDate(dto.getCreatedDate());
						dto.setCreatedUserId(dto.getCreatedUserId());
						calculateEfficiencyHtctBusinessImpl.updateCalEff(dto);
					} else { 
						dto.setCreatedDate(new Date());
						dto.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
						calculateEfficiencyHtctBusinessImpl.saveCalEff(dto);
					}
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Response.Status.CONFLICT).build();
		}
	}

	@Override
	public Response importDgiaThueVTNet(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<CostVtnetHtctDTO> result = costVtnetHtctBusinessImpl.importDgiaThueVTNet(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(CostVtnetHtctDTO dto : result) {
					if(dto.getCostVtnetId() != null) {
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
						dto.setCreatedDate(dto.getCreatedDate());
						dto.setCreatedUserId(dto.getCreatedUserId());
						costVtnetHtctBusinessImpl.updateCostVTNet(dto);
					} else { 
						dto.setCreatedDate(new Date());
						dto.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
						costVtnetHtctBusinessImpl.saveCostVTNet(dto);
					}
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Response.Status.CONFLICT).build();
		}
	}

	@Override
	public Response importGPMB(Attachment attachments, HttpServletRequest request) throws Exception {
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
			List<GpmbHtctDTO> result = gpmbHtctBusinessImpl.importGPMB(filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for(GpmbHtctDTO dto : result) {
					if(dto.getGpmbId() != null) {
						dto.setUpdatedDate(new Date());
						dto.setUpdateUserId(objUser.getVpsUserInfo().getSysUserId());
						dto.setCreatedDate(dto.getCreatedDate());
						dto.setCreatedUserId(dto.getCreatedUserId());
						dto.setProvinceId(dto.getProvinceId());
						gpmbHtctBusinessImpl.updateGpmb(dto);
					} else { 
						dto.setCreatedDate(new Date());
						dto.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
						dto.setProvinceId(gpmbHtctBusinessImpl.getProvinceId(dto.getProvinceCode())) ;
						gpmbHtctBusinessImpl.saveGpmb(dto);
					}
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (BusinessException e) {
			return Response.ok().entity(Response.Status.CONFLICT).build();
		}
	}
	

}
