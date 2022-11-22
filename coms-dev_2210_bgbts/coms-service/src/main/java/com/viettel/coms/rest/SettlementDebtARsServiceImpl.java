package com.viettel.coms.rest;

import java.util.Collections;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.coms.business.SettlementDebtABusinessImpl;
import com.viettel.coms.dto.SettlementDebtADTO;
import com.viettel.coms.dto.SettlementDebtARpDTO;
import com.viettel.service.base.dto.DataListDTO;

public class SettlementDebtARsServiceImpl implements SettlementDebtARsService{

	protected final Logger log = Logger.getLogger(SettlementDebtARsServiceImpl.class);
	
	@Autowired
	SettlementDebtABusinessImpl settlementDebtABusinessImpl;
	
	@Override
	public Response doSearch(SettlementDebtADTO obj) {
		DataListDTO data = settlementDebtABusinessImpl.doSearch(obj);
		return Response.ok(data).build();
	}

	@Override
	public Response exportExcelTonACap(SettlementDebtADTO obj) throws Exception {
		try {
            String strReturn = settlementDebtABusinessImpl.exportExcelTonACap(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
	}
	
	@Override
	public Response doSearchThreeMonth(SettlementDebtARpDTO obj) {
		DataListDTO data = settlementDebtABusinessImpl.doSearchThreeMonth(obj);
		return Response.ok(data).build();
	}

	@Override
	public Response exportExcelACapThreeMonth(SettlementDebtARpDTO obj) throws Exception {
		try {
            String strReturn = settlementDebtABusinessImpl.exportExcelACapThreeMonth(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
	}
}
