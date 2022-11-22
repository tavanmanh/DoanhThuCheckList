package com.viettel.coms.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.ReportBTSByDADTO;
import com.viettel.coms.dto.ReportDTO;
import com.viettel.coms.dto.ReportEffectiveDTO;
import com.viettel.coms.dto.RpBTSDTO;
import com.viettel.coms.dto.RpKHBTSDTO;

public interface RpBTSRsService {

	@POST
	@Path("/doSearchBTS")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchBTS(RpBTSDTO obj);

	@POST
	@Path("/readFileStationReport")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	Response readFileStationReport(Attachment attachments, @Context HttpServletRequest request);

	@POST
	@Path("/readFileContractReport")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	Response readFileContractReport(Attachment attachments, @Context HttpServletRequest request);

	@POST
	@Path("/exportCompleteProgressBTS")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportCompleteProgressBTS(RpBTSDTO obj) throws Exception;

	// Huypq-20191126-start
	@POST
	@Path("/doSearchChart")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchChart(RpBTSDTO obj);

	@POST
	@Path("/getDataChart")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDataChart(RpBTSDTO obj);

	@POST
	@Path("/exportFileDetailKpi45Days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportFileDetailKpi45Days(RpBTSDTO obj) throws Exception;
	// Huypq-end

	// tatph-start-6/2/2019
	@POST
	@Path("/doSearchCongNoTonVTAC")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchCongNoTonVTAC(ManageVttbDTO obj);
	
	@POST
	@Path("/exportFileCongNoTonVTAC")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportFileCongNoTonVTAC(ManageVttbDTO obj) throws Exception;
	
	@POST
	@Path("/doSearchTongHopVTTB")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchTongHopVTTB(ManageVttbDTO obj);
	
	@POST
	@Path("/exportFileTongHopVTTB")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportFileTongHopVTTB(ManageVttbDTO obj) throws Exception;
	
	@POST
	@Path("/doSearchTongHopPXK")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchTongHopPXK(ManageVttbDTO obj);
	
	@POST
	@Path("/exportFileTongHopPXK")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportFileTongHopPXK(ManageVttbDTO obj) throws Exception;
	
	@POST
	@Path("/doSearchChart60days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchChart60days(RpBTSDTO obj);

	@POST
	@Path("/getDataChart60days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDataChart60days(RpBTSDTO obj);

	@POST
	@Path("/exportFileDetailKpi60Days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportFileDetailKpi60Days(RpBTSDTO obj) throws Exception;
	
	@POST
	@Path("/doSearchChart135days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchChart135days(RpBTSDTO obj);

	@POST
	@Path("/getDataChart135days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDataChart135days(RpBTSDTO obj);

	@POST
	@Path("/exportFileDetailKpi135Days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportFileDetailKpi135Days(RpBTSDTO obj) throws Exception;
	
	@POST
	@Path("/getDataChartPxk7days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDataChartPxk7days(RpBTSDTO obj);
	
	@POST
	@Path("/exportChartPxk7Days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportChartPxk7Days(RpBTSDTO obj) throws Exception;
	
	@POST
	@Path("/getDataChartVttb45days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDataChartVttb45days(RpBTSDTO obj);
	
	@POST
	@Path("/exportChartVttb45Days")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportChartVttb45Days(RpBTSDTO obj) throws Exception;
	// tatph-end-6/2/2019
	
	//Huypq-07072020-start
	@POST
	@Path("/exportRpGeneralPaymentCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportRpGeneralPaymentCtv(ReportDTO obj) throws Exception;
	
	@POST
	@Path("/exportRpDetailContractCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportRpDetailContractCtv(ReportDTO obj) throws Exception;
	
	@POST
	@Path("/exportRpTranfersCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportRpTranfersCtv(ReportDTO obj) throws Exception;
	
	@POST
	@Path("/doSearchRpGeneralPaymentCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchRpGeneralPaymentCtv(ReportDTO obj);
	
	@POST
	@Path("/doSearchRpDetailContractCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchRpDetailContractCtv(ReportDTO obj);
	
	@POST
	@Path("/doSearchRpTranfersCtv")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchRpTranfersCtv(ReportDTO obj);
	//Huy-end
	
	//Huy-20072020-start
	@POST
	@Path("/doSearchEffective")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchEffective(ReportEffectiveDTO obj);
	//Huy-end
//	taotq start 27092021
	@POST
	@Path("/doSearchReportKHBTS")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchReportKHBTS(RpKHBTSDTO obj);
	
	@POST
	@Path("/doSearchStation")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchStation(RpKHBTSDTO obj);
	
	@POST
	@Path("/doSearchWO")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchWO(RpKHBTSDTO obj);
	
	@POST
	@Path("/exportStation")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportStation(RpKHBTSDTO obj) throws Exception;
	
	@POST
	@Path("/exportexcelKHBTS")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportexcelKHBTS(RpKHBTSDTO obj) throws Exception;
	
	@POST
	@Path("/doSearchReportBTSByDA")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchReportBTSByDA(ReportBTSByDADTO obj) throws Exception;
	
	@POST
	@Path("/exportexcelRPBTSByDA")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportexcelRPBTSByDA(ReportBTSByDADTO obj) throws Exception;
//	taotq end 27092021
	
	//Huypq-12012022-start
	@POST
	@Path("/doSearchReportResultPerform")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchReportResultPerform(ReportBTSByDADTO obj) throws Exception;
	
	@POST
	@Path("/exportExcelReportResultPerform")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportExcelReportResultPerform(ReportBTSByDADTO obj) throws Exception;
	//Huy-end
	
//	taotq start 25042022
	@POST
	@Path("/getProjectForAutocomplete")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getProjectForAutocomplete(ReportBTSByDADTO obj) throws Exception;
//	taotq end 25042022
	
}
