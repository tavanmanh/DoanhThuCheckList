package com.viettel.coms.business;

import javax.servlet.http.HttpServletRequest;

import com.viettel.coms.dto.ReportDTO;
import com.viettel.service.base.dto.DataListDTO;

public interface RpBTSBusiness {

	long count();
	
	public DataListDTO doSearchReportMassSearchConstr(ReportDTO obj);
	
	public DataListDTO doSearchReportResultDeployBts(ReportDTO obj);
	
	public String exportRpMassSearchConstr(ReportDTO obj, HttpServletRequest request) throws Exception;

	public DataListDTO doSearchReportAcceptHSHC(ReportDTO obj);
}
