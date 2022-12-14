package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.bo.CertificateExtendBO;
import com.viettel.coms.dao.CertificateExtendDAO;
import com.viettel.coms.dao.ManageCertificateDAO;
import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.coms.dto.ResultTangentDetailNoDTO;
import com.viettel.coms.dto.ResultTangentDetailYesDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.util.DateTimeUtils;
//duonghv13-start 22092021
@Service("certificateExtendBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CertificateExtendBusinessImpl
		extends BaseFWBusinessImpl<CertificateExtendDAO, CertificateExtendDTO, CertificateExtendBO>
		implements CertificateExtendBusiness {

	static Logger LOGGER = LoggerFactory.getLogger(CertificateExtendBusinessImpl.class);
	@Context
	HttpServletRequest request;
	
	@Autowired
	private CertificateExtendDAO certificateExtendDAO;
	
	@Autowired
	private ManageCertificateDAO manageCertificateDAO;

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CertificateExtendDTO getById(Long id) {
		// TODO Auto-generated method stub
		return certificateExtendDAO.getById(id);
	}

	@Override
	public List<CertificateExtendDTO> doSearch(CertificateExtendDTO obj) {
		// TODO Auto-generated method stub
		return certificateExtendDAO.doSearch(obj);
	}

	@Override
	public Long createExtend(CertificateExtendDTO obj, HttpServletRequest request) throws Exception {
		//L???y ra danh s??ch ng?????i c?? quy???n ????? g???i tin nh???n
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		ManageCertificateDTO  result = manageCertificateDAO.getCertificateInfor(obj.getCertificateId());
		Long sysGroupId = 270120l;
		List<SysUserCOMSDTO> lstUser = certificateExtendDAO.getLstUsertoSend();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		for(SysUserCOMSDTO temp: lstUser) {
			String content="TH??NG B??O ????? XU???T GIA H???N:"
					+ "??/c " + objUser.getVpsUserInfo().getFullName()+" v???a t???o m???i ????? xu???t gia h???n ch???ng ch???.Th??ng tin ch???ng ch??? xin gia h???n: M?? ch???ng ch???: "+result.getCertificateCode()+"-------"+ "T??n ch???ng ch???: "+result.getCertificateName()+" .Th???i gian t???o:  "+simpleDateFormat.format(date)+". Vui l??ng ki???m tra tr??n h??? th???ng v?? th???c hi???n nghi???p v???.";
			certificateExtendDAO.insertSendSMS(temp.getEmail(),temp.getPhoneNumber(),objUser.getVpsUserInfo().getSysUserId(),content);
		}
		return certificateExtendDAO.saveObject(obj.toModel());
	}

	@Override
	public Long updateExtend(CertificateExtendDTO obj, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		ManageCertificateDTO  result = manageCertificateDAO.getCertificateInfor(obj.getCertificateId());
		Long sysGroupId = 270120l;
		List<SysUserCOMSDTO> lstUser = certificateExtendDAO.getLstUsertoSend();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		for(SysUserCOMSDTO temp: lstUser) {
			String content="TH??NG B??O THAY ?????I TH??NG TIN GIA H???N:"
					+ "??/c " + objUser.getVpsUserInfo().getFullName()+" v???a s???a ?????i gia h???n .Th??ng tin c???a ch???ng ch???: M?? ch???ng ch???: "+result.getCertificateCode()+"-------"+ "T??n ch???ng ch???: "+result.getCertificateName()+" .Th???i gian t??c ?????ng:  "+simpleDateFormat.format(date)+". Vui l??ng ki???m tra tr??n h??? th???ng v?? th???c hi???n nghi???p v???.";
			certificateExtendDAO.insertSendSMS(temp.getEmail(),temp.getPhoneNumber(),objUser.getVpsUserInfo().getSysUserId(),content);
		}
		return certificateExtendDAO.updateObject(obj.toModel());
	}

	public CertificateExtendBusinessImpl() {
		tModel = new CertificateExtendBO();
		tDAO = certificateExtendDAO;
	}

	@Override
	public CertificateExtendDAO gettDAO() {
		return certificateExtendDAO;
	}

	public UtilAttachDocumentDTO getResultFileByExtendId(CertificateExtendDTO obj) {
		// TODO Auto-generated method stub
		UtilAttachDocumentDTO result  = certificateExtendDAO.getFileAttachByResultId(obj.getCertificateExtendId(), "QLCC");
		return result;
	}
	// Duonghv13 end-21/09/2021//

	public List<SysUserCOMSDTO> getLstUsertoSend() {
		// TODO Auto-generated method stub
		return certificateExtendDAO.getLstUsertoSend();
	}


}
