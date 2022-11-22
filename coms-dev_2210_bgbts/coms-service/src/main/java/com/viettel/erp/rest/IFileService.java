/**
 *
 */
package com.viettel.erp.rest;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.viettel.coms.dto.ExcelErrorDTO;

/**
 * @author Huy
 */
public interface IFileService {

    @POST
    @Path("/uploadATTT")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadATTT(List<Attachment> attachments, @Context HttpServletRequest request);

    @POST
    @Path("/uploadATTTInput")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadATTTInput(List<Attachment> attachments, @Context HttpServletRequest request);


    @POST
    @Path("/uploadATTT2")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadATTT2(@RequestParam("multipartFile") MultipartFile multipartFile, @Context HttpServletRequest request);

    @POST
    @Path("/uploadTemp")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadTemp(List<Attachment> attachments, @Context HttpServletRequest request);


    @GET
    @Path("/downloadImport")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileImport(@Context HttpServletRequest request) throws Exception;


    @GET
    @Path("/downloadFileATTT")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileATTT(@Context HttpServletRequest request) throws Exception;

    //chinhpxn 20180608 start
    @POST
    @Path("/exportExcelError")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportExcelError(ExcelErrorDTO errorList);
    //chinhpxn 20180608 end

    @GET
	@Path("/downloadFile")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@Context HttpServletRequest request)
			throws Exception;
    
    @POST
    @Path("/uploadATTTImage")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadATTTImage(List<Attachment> attachments, @Context HttpServletRequest request);
    
    //Huypq-22082020-start
    @POST
    @Path("/uploadATTTImageAIO")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadATTTImageAIO(List<Attachment> attachments, @Context HttpServletRequest request);
    //Huy-end

    //picasso start
    @POST
    @Path("/uploadWoFileATTT")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadWoFileATTT(List<Attachment> attachments, @Context HttpServletRequest request);
    //picasso end
    
    //Huypq-01072021-start
    @POST
    @Path("/uploadWoFileAndSaveWoMappingAttach")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadWoFileAndSaveWoMappingAttach(List<Attachment> attachments, @Context HttpServletRequest request);
  //Huy-end
}
