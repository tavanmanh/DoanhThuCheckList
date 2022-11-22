package com.viettel.coms.business;

//Duonghv13 start-16/08/2021//
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.TotalMonthPlanHTCTDTO;

public interface TotalMonthPlanHTCTBusiness {
	long count();
	TotalMonthPlanHTCTDTO findbyProvinceCodeandMonth(String code,String month,String year,String stationCodeVCC);
	
	TotalMonthPlanHTCTDTO getById(Long id);
	
	List<TotalMonthPlanHTCTDTO> doSearch(TotalMonthPlanHTCTDTO obj);
	
	Long createTotalMonthPlanHTCT(TotalMonthPlanHTCTDTO obj) throws Exception;

	Long updateTotalMonthPlanHTCT(TotalMonthPlanHTCTDTO obj) throws Exception;

	Long deleteTotalMonthPlanHTCT(TotalMonthPlanHTCTDTO obj);

	List<TotalMonthPlanHTCTDTO> importTotalMonthPlanHTCT(String fileInput) throws Exception;
	
	List<TotalMonthPlanHTCTDTO> importReport(String fileInput) throws Exception;
	
	String exportExcelTemplate()  throws Exception;
	
	String exportMonthPlan(TotalMonthPlanHTCTDTO obj) throws Exception;
	
	String exportPDFReport(TotalMonthPlanHTCTDTO criteria) throws Exception;

	//Duonghv13 end-16/08/2021//
}
