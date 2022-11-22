package com.viettel.coms.business;

import java.util.List;

import com.viettel.coms.dto.ManageCertificateDTO;

//Duonghv13-start 21092021
public interface ManagementCertificateBusiness {

	long count();

	ManageCertificateDTO getById(Long id);
	ManageCertificateDTO getOneCertificateDetails(Long id);
	List<ManageCertificateDTO> doSearch(ManageCertificateDTO obj);
	
	Long createCertificate(ManageCertificateDTO obj) throws Exception;

	Long updateCertificate(ManageCertificateDTO obj) throws Exception;
	int deleteCertificate(Long id) ;
	
	String exportCertificate(ManageCertificateDTO obj)  throws Exception;

	//Duonghv13 end-21/09/2021//

}
