/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import com.viettel.coms.dto.CommonDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.ReportDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author HungLQ9
 */
public interface ReportRsService {

    @POST
    @Path("/exportPdf")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_HTML})
    public Response exportPdf(CommonDTO obj)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    @POST
    @Path("/signVoffice")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response signVoffice(List<CommonDTO> list) throws Exception;

    @POST
    @Path("/previewVoffice")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response previewVoffice(CommonDTO dto) throws Exception;

    @POST
    @Path("/downloadAttachFile")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response downloadAttachFile(CommonDTO dto) throws Exception;

    @POST
    @Path("/viewSignedDoc")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response viewSignedDoc(CommonDTO dto) throws Exception;

    @POST
    @Path("/signVofficeKHSXVT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response signVofficeKHSXVT(List<CommonDTO> list) throws Exception;
    
    @POST
	@Path("/previewSignedDoc")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response previewSignedDoc(CommonDTO dto) throws Exception;
    
    //HuyPq-start
    @POST
    @Path("/signVofficeYCSXVT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response signVofficeYCSXVT(List<CommonDTO> list) throws Exception;
    //Huypq-end
    
    //Huypq-21062021-start
    @POST
    @Path("/doSearchReportMassSearchConstr")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReportMassSearchConstr(ReportDTO obj) throws Exception;
    
    @POST
    @Path("/doSearchReportResultDeployBts")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReportResultDeployBts(ReportDTO obj) throws Exception;
    
    @POST
	@Path("/exportRpMassSearchConstr")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportRpMassSearchConstr(ReportDTO obj) throws Exception;
    //Huy-end
    
    //Huypq-08072021-start
    @POST
    @Path("/doSearchReportAcceptHSHC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchReportAcceptHSHC(ReportDTO obj) throws Exception;
    //Huy-end
}
