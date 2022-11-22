package com.viettel.coms.rest;

import java.util.Collections;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.business.RpOrderlyBusiness;
import com.viettel.coms.dto.ReportDTO;
import com.viettel.coms.dto.ReportErpAmsWoDTO;
import com.viettel.coms.dto.RpOrderlyWoDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.service.base.dto.DataListDTO;

public class RpOrderlyWoRsServiceImpl implements RpOrderlyWoRsService{

	@Autowired
	RpOrderlyBusiness rpOrderlyBusiness;
	
	@Override
	public Response getDataReceiveWoSynthetic(RpOrderlyWoDTO obj) {
		DataListDTO ls = rpOrderlyBusiness.getDataReceiveWoSynthetic(obj);
		return Response.ok(ls).build();
	}

	@Override
	public Response getDataReceiveWoDetail(RpOrderlyWoDTO obj) {
		DataListDTO ls = rpOrderlyBusiness.getDataReceiveWoDetail(obj);
		return Response.ok(ls).build();
	}
	
	@Override
    public Response exportFile(RpOrderlyWoDTO obj) throws Exception {
        try {
            String strReturn = rpOrderlyBusiness.exportFile(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
	
	//Huypq-07062021-start
	@Override
	public Response doSearchReportErpAmsWo(ReportErpAmsWoDTO obj) {
		DataListDTO ls = rpOrderlyBusiness.doSearchReportErpAmsWo(obj);
		return Response.ok(ls).build();
	}
	//Huy-end
	
	@Override
	public Response getForAutoCompleteByGroupLv2(SysGroupDTO obj) {
		DataListDTO data = rpOrderlyBusiness.getForAutoCompleteByGroupLv2(obj);
		return Response.ok(data).build();
	}
	
	@Override
	public Response doSearchGeneralCtv(ReportDTO obj) {
		DataListDTO ls = rpOrderlyBusiness.doSearchGeneralCtv(obj);
		return Response.ok(ls).build();
	}
	
	@Override
	public Response doSearchDetailCtv(ReportDTO obj) {
		DataListDTO ls = rpOrderlyBusiness.doSearchDetailCtv(obj);
		return Response.ok(ls).build();
	}
	
	@Override
	public Response doSearchZoningCtv(ReportDTO obj) {
		DataListDTO ls = rpOrderlyBusiness.doSearchZoningCtv(obj);
		return Response.ok(ls).build();
	}
	
	@Override
	public Response doSearchRevenueCtv(ReportDTO obj) {
		DataListDTO ls = rpOrderlyBusiness.doSearchRevenueCtv(obj);
		return Response.ok(ls).build();
	}

	@Override
    public Response exportFileCtv(ReportDTO obj) throws Exception {
        try {
            String strReturn = rpOrderlyBusiness.exportFileCtv(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
	
	@Override
	public Response doSearchProvinceInPopup(CatProvinceDTO obj) {
		return Response.ok(rpOrderlyBusiness.doSearchProvinceInPopup(obj)).build();
	}
}
