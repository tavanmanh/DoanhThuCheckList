package com.viettel.coms.rest;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.coms.business.RpBTSBusinessImpl;
import com.viettel.coms.business.YearPlanBusinessImpl;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.ReportBTSByDADTO;
import com.viettel.coms.dto.ReportDTO;
import com.viettel.coms.dto.ReportEffectiveDTO;
import com.viettel.coms.dto.RpBTSDTO;
import com.viettel.coms.dto.RpKHBTSDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;

public class RpBTSRsServiceImpl implements RpBTSRsService{

	protected final Logger log = Logger.getLogger(RpBTSRsServiceImpl.class);
    @Autowired
    RpBTSBusinessImpl rpBTSBusinessImpl;
    @Context
    HttpServletRequest request;
    @Autowired
    YearPlanBusinessImpl yearPlanBusinessImpl;
	@Override
	public Response doSearchBTS(RpBTSDTO obj) {
		//tanqn start 20181113
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DOSEARCH_BTS");
        //objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setDescription("Báo cáo công nợ vật tư");
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        DataListDTO data = rpBTSBusinessImpl.doSearchBTS(obj);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanBusinessImpl.addKpiLog(objKpiLog);//TANQN 20181113 END
        return Response.ok(data).build();
	}
	
	@Override
    public Response readFileStationReport(Attachment attachments, HttpServletRequest request) {
        List<String> stationCodeLst = null;
        try {
            stationCodeLst = rpBTSBusinessImpl.readFileStation(attachments);
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        return Response.ok(Collections.singletonMap("stationCodeLst", stationCodeLst)).build();
    }
	
	@Override
    public Response readFileContractReport(Attachment attachments, HttpServletRequest request) {
        List<String> contractCodeLst = null;
        try {
        	contractCodeLst = rpBTSBusinessImpl.readFileContract(attachments);
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        return Response.ok(Collections.singletonMap("contractCodeLst", contractCodeLst)).build();
    }

	@Override
    public Response exportCompleteProgressBTS(RpBTSDTO obj) throws Exception {
		//tanqn start 20181113
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_COMPLETE_PROGRESS_BTS");
        //objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setDescription("Báo cáo công nợ vật tư");
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            String strReturn = rpBTSBusinessImpl.exportCompleteProgressBTS(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);//TANQN 20181113 END
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanBusinessImpl.addKpiLog(objKpiLog);//TANQN 20181113 END
        }
        return null;
    }

	//Huypq-20191126-start
	@Override
	public Response doSearchChart(RpBTSDTO obj) {
		DataListDTO ls = rpBTSBusinessImpl.doSearchChart(obj);
		return Response.ok(ls).build();
	}

	@Override
	public Response getDataChart(RpBTSDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(rpBTSBusinessImpl.getDataChart(obj)).build();
	}
	
	@Override
    public Response exportFileDetailKpi45Days(RpBTSDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportFileDetailKpi45Days(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	//Huy-end
	
	//tatph-start-6/2/2019
	@Override
	public Response doSearchCongNoTonVTAC(ManageVttbDTO obj) {
        DataListDTO data = rpBTSBusinessImpl.doSearchCongNoTonVTAC(obj);
        return Response.ok(data).build();
	}
	@Override
    public Response exportFileCongNoTonVTAC(ManageVttbDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportFileCongNoTonVTAC(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
	public Response doSearchTongHopVTTB(ManageVttbDTO obj) {
        DataListDTO data = rpBTSBusinessImpl.doSearchTongHopVTTB(obj);
        return Response.ok(data).build();
	}
	@Override
    public Response exportFileTongHopVTTB(ManageVttbDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportFileTongHopVTTB(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
	public Response doSearchTongHopPXK(ManageVttbDTO obj) {
        DataListDTO data = rpBTSBusinessImpl.doSearchTongHopPXK(obj);
        return Response.ok(data).build();
	}
	@Override
    public Response exportFileTongHopPXK(ManageVttbDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportFileTongHopPXK(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	
	
	@Override
	public Response doSearchChart60days(RpBTSDTO obj) {
		DataListDTO ls = rpBTSBusinessImpl.doSearchChart60days(obj);
		return Response.ok(ls).build();
	}

	@Override
	public Response getDataChart60days(RpBTSDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(rpBTSBusinessImpl.getDataChart60days(obj)).build();
	}
	
	@Override
    public Response exportFileDetailKpi60Days(RpBTSDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportFileDetailKpi60Days(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
	public Response doSearchChart135days(RpBTSDTO obj) {
		DataListDTO ls = rpBTSBusinessImpl.doSearchChart135days(obj);
		return Response.ok(ls).build();
	}

	@Override
	public Response getDataChart135days(RpBTSDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(rpBTSBusinessImpl.getDataChart135days(obj)).build();
	}
	
	@Override
    public Response exportFileDetailKpi135Days(RpBTSDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportFileDetailKpi135Days(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
    public Response exportChartPxk7Days(RpBTSDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportChartPxk7Days(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
	public Response getDataChartPxk7days(RpBTSDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(rpBTSBusinessImpl.getDataChartPxk7days(obj)).build();
	}
	
	@Override
    public Response exportChartVttb45Days(RpBTSDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportChartVttb45Days(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
	public Response getDataChartVttb45days(RpBTSDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(rpBTSBusinessImpl.getDataChartVttb45days(obj)).build();
	}
	//tatph-end-6/2/2019
	
	//Huypq-07072020-start
	@Override
    public Response exportRpGeneralPaymentCtv(ReportDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportRpGeneralPaymentCtv(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e);
        }
        return null;
    }
	
	@Override
    public Response exportRpDetailContractCtv(ReportDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportRpDetailContractCtv(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e);
        }
        return null;
    }
	
	@Override
    public Response exportRpTranfersCtv(ReportDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportRpTranfersCtv(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e);
        }
        return null;
    }
	
	@Override
	public Response doSearchRpGeneralPaymentCtv(ReportDTO obj) {
		DataListDTO ls = rpBTSBusinessImpl.doSearchRpGeneralPaymentCtv(obj, request);
		return Response.ok(ls).build();
	}
	
	@Override
	public Response doSearchRpDetailContractCtv(ReportDTO obj) {
		DataListDTO ls = rpBTSBusinessImpl.doSearchRpDetailContractCtv(obj, request);
		return Response.ok(ls).build();
	}
	
	@Override
	public Response doSearchRpTranfersCtv(ReportDTO obj) {
		DataListDTO ls = rpBTSBusinessImpl.doSearchRpTranfersCtv(obj, request);
		return Response.ok(ls).build();
	}

	@Override
	public Response doSearchEffective(ReportEffectiveDTO obj) {
		DataListDTO data = rpBTSBusinessImpl.doSearchEffective(obj);
		return Response.ok(data).build();
	}
	
	//Huy-end
//	taotq start 27092021
	@Override
	public Response doSearchReportKHBTS(RpKHBTSDTO obj) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DOSEARCH_BTS");
        objKpiLog.setDescription("Báo cáo công nợ vật tư");
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        DataListDTO data = rpBTSBusinessImpl.doSearchReportKHBTS(obj);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanBusinessImpl.addKpiLog(objKpiLog);
        return Response.ok(data).build();
	}
	
	@Override
	public Response doSearchStation(RpKHBTSDTO obj) {
        DataListDTO data = rpBTSBusinessImpl.doSearchStation(obj);
        return Response.ok(data).build();
	}
	
	public Response doSearchWO(RpKHBTSDTO obj) {
        DataListDTO data = rpBTSBusinessImpl.doSearchWO(obj);
        return Response.ok(data).build();
	}
	
	@Override
    public Response exportStation(RpKHBTSDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportStation(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e);
        }
        return null;
    }
	
	@Override
    public Response exportexcelKHBTS(RpKHBTSDTO obj) throws Exception {
        try {
            String strReturn = rpBTSBusinessImpl.exportexcelKHBTS(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e);
        }
        return null;
    }
	
	@Override
	public Response doSearchReportBTSByDA(ReportBTSByDADTO obj) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DOSEARCH_BTS");
        objKpiLog.setDescription("Báo cáo công nợ vật tư");
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        DataListDTO data = rpBTSBusinessImpl.doSearchReportBTSByDA(obj);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanBusinessImpl.addKpiLog(objKpiLog);
        return Response.ok(data).build();
	}

	@Override
	public Response exportexcelRPBTSByDA(ReportBTSByDADTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
            String strReturn = rpBTSBusinessImpl.exportexcelRPBTSByDA(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e);
        }
        return null;
	}
	
//	taotq end 27092021
	
	//Huypq-12012022-start
	@Override
	public Response doSearchReportResultPerform(ReportBTSByDADTO obj) {
        DataListDTO data = rpBTSBusinessImpl.doSearchReportResultPerform(obj);
        return Response.ok(data).build();
	}
	
	@Override
	public Response exportExcelReportResultPerform(ReportBTSByDADTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
            String strReturn = rpBTSBusinessImpl.exportExcelReportResultPerform(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e);
        }
        return null;
	}
	//Huy-end
	
	@Override
	public Response getProjectForAutocomplete(ReportBTSByDADTO obj) {
        DataListDTO data = rpBTSBusinessImpl.getProjectForAutocomplete(obj);
        return Response.ok(data).build();
	}
}
