/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.SynStockTransDTO;
import com.viettel.coms.dto.SynStockTransDetailDTO;

/**
 * @author HungLQ9
 */
public interface SynStockTransRsService {

    @GET
    @Path("/synStockTrans")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSynStockTrans();

    @GET
    @Path("/synStockTrans/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSynStockTransById(@PathParam("id") Long id);

    @PUT
    @Path("/synStockTrans/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateSynStockTrans(SynStockTransDTO obj);

    @POST
    @Path("/synStockTrans/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addSynStockTrans(SynStockTransDTO obj);

    @DELETE
    @Path("/synStockTrans/{id}/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteSynStockTrans(@PathParam("id") Long id);

    //VietNT_20190116_start
    @POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response doSearch(SynStockTransDTO obj);

    @POST
    @Path("/doForwardGroup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response doForwardGroup(SynStockTransDTO dto);

//    @POST
//    @Path("/getProvinceChiefId")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    Response getProvinceChiefId(SynStockTransDTO dto);

    //VietNT_end
    
    //HuyPQ-start
    @POST
    @Path("/reportDetailAWaitReceive")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response reportDetailAWaitReceive(SynStockTransDTO obj);
    //Huy-end
    
    //Huypq-20190904-start
    @POST
    @Path("/doSearchAcceptManage")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response doSearchAcceptManage(SynStockTransDTO obj);
    
    @POST
	@Path("/exportFile")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportFile(SynStockTransDTO obj)throws Exception;
    
    @POST
   	@Path("/updateAcceptPXK")
   	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	public Response updateAcceptPXK(SynStockTransDTO obj)throws Exception;
    
    @POST
   	@Path("/updateDenyPXK")
   	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	public Response updateDenyPXK(SynStockTransDTO obj)throws Exception;
    
    @POST
   	@Path("/updateAssignPXK")
   	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	public Response updateAssignPXK(SynStockTransDTO obj)throws Exception;
    
    @POST
   	@Path("/updateAcceptAssignPXK")
   	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	public Response updateAcceptAssignPXK(SynStockTransDTO obj)throws Exception;
    
    @POST
   	@Path("/updateAcceptAssign")
   	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   	public Response updateAcceptAssign(SynStockTransDetailDTO obj)throws Exception;
    
    @POST
    @Path("/getDataFilePXK")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getDataFilePXK(SynStockTransDetailDTO obj);
    
    @POST
    @Path("/checkRolePGD")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkRolePGD();
    //Huy-end
}
