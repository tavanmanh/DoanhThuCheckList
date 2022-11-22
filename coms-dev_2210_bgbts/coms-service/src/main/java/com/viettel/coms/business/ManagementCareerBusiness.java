package com.viettel.coms.business;

import java.util.List;

import com.viettel.coms.dto.ManageCareerDTO;

//Duonghv13-start 16092021
public interface ManagementCareerBusiness {
	
	long count();
	
	
	ManageCareerDTO getById(Long id);
	
	List<ManageCareerDTO> doSearch(ManageCareerDTO obj);
	
	Long createCareer(ManageCareerDTO obj) throws Exception;

	Long updateCareer(ManageCareerDTO obj) throws Exception;

	String exportCareer(ManageCareerDTO obj)  throws Exception;
	
	//Duonghv13 end-16/09/2021//

}
