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

import com.viettel.coms.bo.ContactUnitBO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.ContactUnitDTO;
import com.viettel.coms.dto.ContactUnitDetailDTO;
import com.viettel.coms.dto.ManageHcqtDTO;
import com.viettel.coms.dto.ManageVttbDTO;

/**
 * @author HoangNH38
 */
public interface RecommendContactUnitRsService {

	// tatph - start 19/12/2019
	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(ContactUnitDTO obj);
	
	@POST
	@Path("/doSearchDetail")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchDetail(ContactUnitDetailDTO obj);
	
	@POST
	@Path("/doSearchDetailById")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchDetailById(ContactUnitDetailDTO obj);
	
	@POST
	@Path("/doSearchContactUnitLibrary")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchContactUnitLibrary(ContactUnitDetailDTO obj);

	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(ContactUnitDetailDTO obj);
	
	@POST
	@Path("/updateDescription")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateDescription(ContactUnitDetailDTO obj);
	
	@POST
	@Path("/save")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response save(ContactUnitDetailDTO obj);

	@POST
	@Path("/getExcelTemplate")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getExcelTemplate(ContactUnitDTO obj) throws Exception;
	
	@POST
	@Path("/getExcelTemplateManageContactUnit")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getExcelTemplateManageContactUnit(ContactUnitDTO obj) throws Exception;
	
	@POST
	@Path("/getExcelTemplateConstructionDTOS")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getExcelTemplateConstructionDTOS(ConstructionTaskDetailDTO obj) throws Exception;
	
	@POST
	@Path("/exportListContact")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportListContact(ContactUnitDTO obj) throws Exception;

	@POST
	@Path("/importRecommendContactUnit")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importRecommendContactUnit(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importRecommendContactUnitLibrary")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importRecommendContactUnitLibrary(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/importConstructionDTOS")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importConstructionDTOS(Attachment attachments, @Context HttpServletRequest request) throws Exception;

	// tatph - end 19/12/2019
	
	//HienLT56 start 01072020
	@POST
	@Path("/getForAutoCompleteProvince")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getForAutoCompleteProvince(ContactUnitDTO obj);
	
	@POST
	@Path("/getForAutoCompleteProvinceS")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getForAutoCompleteProvinceS(ContactUnitDTO obj);

	
	@POST
	@Path("/addContactt")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addContactt(ContactUnitDTO obj);
	
	@POST
	@Path("/importRecommendContactUnitAll")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importRecommendContactUnitAll(Attachment attachments, @Context HttpServletRequest request) throws Exception;
	
	@POST
	@Path("/getExcelTemplateAll")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getExcelTemplateAll(ContactUnitDTO obj) throws Exception;
	
	//HienLT56 end 01072020

}
