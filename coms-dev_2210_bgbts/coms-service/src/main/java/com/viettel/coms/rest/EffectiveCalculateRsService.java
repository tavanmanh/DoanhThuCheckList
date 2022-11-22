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

import com.viettel.coms.dto.CalculateEfficiencyHtctDTO;
import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.coms.dto.CapexSourceHTCTDTO;
import com.viettel.coms.dto.Cost1477HtctDTO;
import com.viettel.coms.dto.CostVtnetHtctDTO;
import com.viettel.coms.dto.GpmbHtctDTO;
import com.viettel.coms.dto.OfferHtctDTO;
import com.viettel.coms.dto.RatioDeliveryHtctDTO;
import com.viettel.coms.dto.WaccHtctDTO;

public interface EffectiveCalculateRsService {
	
	@POST
    @Path("/getCapexSource")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCapexSource(CapexSourceHTCTDTO obj);
	
	@POST
    @Path("/getData2")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getData2(WaccHtctDTO obj);
	
	@POST
    @Path("/getData3")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getData3(RatioDeliveryHtctDTO obj);
	
	@POST
	@Path("/exportData3")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportData3(RatioDeliveryHtctDTO obj) throws Exception;
	
	@POST
    @Path("/getData4")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getData4(GpmbHtctDTO obj);
	
	@POST
    @Path("/getData5")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getData5(CostVtnetHtctDTO obj);
	
	@POST
	@Path("/exportData5")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportData5(CostVtnetHtctDTO obj) throws Exception;
	
	@POST
    @Path("/getData6")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getData6(Cost1477HtctDTO obj);
	
	@POST
    @Path("/getData7")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getData7(CapexBtsHtctDTO obj);
	
	@POST
    @Path("/getData8")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getData8(OfferHtctDTO obj);
	
	@POST
    @Path("/getData9")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getData9(CalculateEfficiencyHtctDTO obj);
	
	@POST
    @Path("/downloadTemplateCapexNguon")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadTemplateCapexNguon(CapexSourceHTCTDTO obj) throws Exception;
	
	@POST
    @Path("/downloadTemplateCapex")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadTemplateCapex(CapexBtsHtctDTO obj) throws Exception;
	
	@POST
    @Path("/downloadTemplateChaoGia")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadTemplateChaoGia(OfferHtctDTO obj) throws Exception;
	
	@POST
    @Path("/downloadTemplateWACC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadTemplateWACC(WaccHtctDTO obj) throws Exception;
	
	@POST
    @Path("/downloadTemplateTLGK")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadTemplateTLGK(RatioDeliveryHtctDTO obj) throws Exception;
	
	@POST
    @Path("/downloadTemplateGiaThue1477")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadTemplateGiaThue1477(Cost1477HtctDTO obj) throws Exception;
	
	@POST
    @Path("/downloadTemplateEffectiveCalculate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadTemplateEffectiveCalculate(CalculateEfficiencyHtctDTO obj) throws Exception;
	
	@POST
    @Path("/downloadTemplateDgiaThueVTNet")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadTemplateDgiaThueVTNet(CostVtnetHtctDTO obj) throws Exception;
	
	@POST
    @Path("/downloadTemplateGPMB")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadTemplateGPMB(GpmbHtctDTO obj) throws Exception;
	
	@POST
	@Path("/importCapexNguon")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importCapexNguon(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importCapex")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importCapex(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importWACC")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importWACC(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importChaoGia")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importChaoGia(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importTLGK")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importTLGK(Attachment attachments, @Context HttpServletRequest request) throws Exception;

	@POST
	@Path("/importGiaThue1477")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importGiaThue1477(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importEffectiveCalculate")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importEffectiveCalculate(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importDgiaThueVTNet")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importDgiaThueVTNet(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importGPMB")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importGPMB(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
}
