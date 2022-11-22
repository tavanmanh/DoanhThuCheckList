package com.viettel.coms.rest;

import com.viettel.coms.business.CompletedWorkBusinessImpl;
import com.viettel.coms.business.QuantityConstructionBusinessImpl;
import com.viettel.coms.business.YearPlanBusinessImpl;
import com.viettel.coms.dto.ConstructionTaskDailyDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CompletedWorkRsServiceImpl implements CompletedWorkRsService {

	protected final Logger log = Logger.getLogger(CompletedWorkRsServiceImpl.class);
	@Autowired
	private CompletedWorkBusinessImpl completedWorkBusiness;

	@Autowired
	YearPlanBusinessImpl yearPlanBusinessImpl;
	@Context
	HttpServletRequest request;

	@Override
	public Response doSearch(WorkItemDetailDTO obj) {
		//tanqn 20181113 start
		KttsUserSession objUsers = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		KpiLogDTO objKpiLog = new KpiLogDTO();
		Date dStart = new Date();
		objKpiLog.setStartTime(dStart);
		objKpiLog.setCreateDatetime(dStart);
		objKpiLog.setFunctionCode("DOSERACH_DETAIL_MONTH_PLAN");
		objKpiLog.setTransactionCode(obj.getConstructionCode());
		objKpiLog.setDescription("Phê duyệt công trình kết thúc đồng bộ");
		objKpiLog.setCreateUserId(objUsers.getVpsUserInfo().getSysUserId());
		DataListDTO data = completedWorkBusiness.doSearch(obj);
		Date dEnd = new Date();
		objKpiLog.setEndTime(dEnd);
		objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
		objKpiLog.setStatus("1");
		yearPlanBusinessImpl.addKpiLog(objKpiLog);
		return Response.ok(data).build();//end
	}

	@Override
	public Response approveCompletedWork(WorkItemDetailDTO obj) {
		try {
			completedWorkBusiness.approveCompletedWork(obj, request);
			return Response.ok(Response.Status.CREATED).build();
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response rejectCompletedWork(WorkItemDetailDTO obj) {
		try {
			completedWorkBusiness.rejectCompletedWork(obj, request);
			return Response.ok(Response.Status.CREATED).build();
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response approveCompletedWorkChecked(List<WorkItemDetailDTO> lstObj) {
		KttsUserSession objUsers = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		KpiLogDTO objKpiLog = new KpiLogDTO();
		Date dStart = new Date();
		objKpiLog.setStartTime(dStart);
		objKpiLog.setCreateDatetime(dStart);
		objKpiLog.setCreateUserId(objUsers.getVpsUserInfo().getSysUserId());
		try {
			for (WorkItemDetailDTO obj : lstObj) {
				completedWorkBusiness.approveCompletedWork(obj, request);
			}
			Date dEnd = new Date();
			objKpiLog.setEndTime(dEnd);
			objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
			objKpiLog.setStatus("1");
			yearPlanBusinessImpl.addKpiLog(objKpiLog);
			return Response.ok(Response.Status.CREATED).build();
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

}
