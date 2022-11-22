package com.viettel.coms.business;

import javax.servlet.http.HttpServletRequest;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.dto.ReportDTO;
import com.viettel.coms.dto.ReportErpAmsWoDTO;
import com.viettel.coms.dto.RpOrderlyWoDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.service.base.dto.DataListDTO;

public interface RpOrderlyBusiness {

	public DataListDTO getDataReceiveWoSynthetic(RpOrderlyWoDTO obj);
	
	public DataListDTO getDataReceiveWoDetail(RpOrderlyWoDTO obj);
	
	public String exportFile(RpOrderlyWoDTO obj) throws Exception;
	
	public DataListDTO doSearchGeneralCtv(ReportDTO obj);
	
	public DataListDTO doSearchDetailCtv(ReportDTO obj);
	
	public DataListDTO doSearchZoningCtv(ReportDTO obj);
	
	public DataListDTO doSearchRevenueCtv(ReportDTO obj);
	
	public DataListDTO getForAutoCompleteByGroupLv2(SysGroupDTO obj);
	
	public String exportFileCtv(ReportDTO obj) throws Exception;
	
	public DataListDTO doSearchProvinceInPopup(CatProvinceDTO obj);
	
	//Huypq-07062021-start
	public DataListDTO doSearchReportErpAmsWo(ReportErpAmsWoDTO obj);
	//Huypq-end
}
