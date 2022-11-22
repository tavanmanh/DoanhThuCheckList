package com.viettel.coms.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;

//Duonghv13-start 22092021
public interface CertificateExtendBusiness {
	long count();
	
	
	CertificateExtendDTO getById(Long id);
	
	List<CertificateExtendDTO> doSearch(CertificateExtendDTO obj);
	
	Long createExtend(CertificateExtendDTO obj, HttpServletRequest request) throws Exception;

	Long updateExtend(CertificateExtendDTO obj, HttpServletRequest request) throws Exception;
	List<SysUserCOMSDTO> getLstUsertoSend();

	//Duonghv13 end-22/09/2021//
}
