package com.viettel.coms.rest;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.coms.business.ReportCostOfSalesBusinessImpl;
import com.viettel.coms.dto.ReportCostofSalesDTO;
import com.viettel.service.base.dto.DataListDTO;

public class ReportCostOfSalesServiceImpl implements ReportCostOfSalesService {

	@Context
	HttpServletRequest request;
	protected final Logger log = Logger.getLogger(ManageMERsServiceImpl.class);	
	@Autowired
	ReportCostOfSalesBusinessImpl reportCostOfSalesBusinessImpl;
	
	@Override
	public Response doSearchDetailContract(ReportCostofSalesDTO obj) {
		// TODO Auto-generated method stub
		DataListDTO data = reportCostOfSalesBusinessImpl.doSearchDetailContract(obj);
		return Response.ok(data).build();
	}
	
	@Override
	public Response doSearchTHProvince(ReportCostofSalesDTO obj) {
		// TODO Auto-generated method stub
		DataListDTO data = reportCostOfSalesBusinessImpl.doSearchTHProvince(obj);
		return Response.ok(data).build();
	}
	
	@Override
	public Response doSearchDetailAllocation(ReportCostofSalesDTO obj) {
		// TODO Auto-generated method stub
		DataListDTO data = reportCostOfSalesBusinessImpl.doSearchDetailAllocation(obj);
		return Response.ok(data).build();
	}

	@Override
	public Response exportFile(ReportCostofSalesDTO obj) {
		// TODO Auto-generated method stub
		try {
			String strReturn = "";
			if(obj.getType() == 1) {
				strReturn = reportCostOfSalesBusinessImpl.exportFile(obj);
			}else if(obj.getType() == 2) {
				strReturn = reportCostOfSalesBusinessImpl.exportFileTHProvince(obj);
			}else {
				strReturn = reportCostOfSalesBusinessImpl.exportFileAllocation(obj);
			}
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

}
