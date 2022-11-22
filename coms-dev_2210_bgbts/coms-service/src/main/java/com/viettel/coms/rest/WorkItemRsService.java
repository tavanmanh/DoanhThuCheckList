/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.CNTContractDTO;
import com.viettel.coms.dto.CommonDTO;
import com.viettel.coms.dto.ObstructedDetailDTO;
import com.viettel.coms.dto.SynStockTransDTO;
import com.viettel.coms.dto.SynStockTransDetailDTO;
import com.viettel.coms.dto.SynStockTransDetailSerialDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.coms.dto.WorkItemGponDTO;

/**
 * @author HungLQ9
 */
public interface WorkItemRsService {

    @POST
    @Path("/remove")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response remove(WorkItemDetailDTO obj);

    @POST
    @Path("/approveWorkItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response approveWorkItem(WorkItemDetailDTO obj);

    @POST
    @Path("/approveCompleteWorkItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response approveCompleteWorkItem(WorkItemDetailDTO obj);

    @POST
    @Path("/saveCancelConfirmPopup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveCancelConfirmPopup(WorkItemDetailDTO obj);

    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response add(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response update(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/updateInConstruction")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateInConstruction(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/getById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getById(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearch(WorkItemDetailDTO obj);
    
    @POST
    @Path("/doSearchGpon")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchGpon(WorkItemDetailDTO obj);

    @POST
    @Path("/doSearchForTask")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchForTask(WorkItemDetailDTO obj);

    @POST
    @Path("/doSearchComplete")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchComplete(WorkItemDetailDTO obj);

    @POST
    @Path("/doSearchQuantity")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchQuantity(WorkItemDetailDTO obj);

    @POST
    @Path("/doSearchCompleteDate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchCompleteDate(WorkItemDetailDTO obj);

    @POST
    @Path("/doSearchCovenant")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchCovenant(CNTContractDTO obj);

    @POST
    @Path("/doSearchContractInput")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchContractInput(CNTContractDTO obj);

    @POST
    @Path("/doSearchDeliveryBill")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchDeliveryBill(SynStockTransDTO obj);

    @POST
    @Path("/doSearchEntangled")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchEntangled(ObstructedDetailDTO obj);

    @POST
    @Path("/doSearchForReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchForReport(WorkItemDetailDTO obj);

    @POST
    @Path("/doSearchDetailForReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchDetailForReport(WorkItemDetailDTO obj);

    @POST
    @Path("/exportCompleteProgress")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportCompleteProgress(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/exportCovenantProgress")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportCovenantProgress(CNTContractDTO obj) throws Exception;

    @POST
    @Path("/GoodsListTable")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response GoodsListTable(SynStockTransDetailDTO obj);

    @POST
    @Path("/GoodsListDetail")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response GoodsListDetail(SynStockTransDetailSerialDTO obj);

    @POST
    @Path("/removeFillterWorkItem")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response removeFillterWorkItem(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/getListImageById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListImageById(CommonDTO obj) throws Exception;

    @POST
    @Path("/getListImageWorkItemId")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListImageWorkItemId(Long id) throws Exception;

    @POST
    @Path("/exportWorkItemServiceTask")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportWorkItemServiceTask(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/exportSLTN")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportSLTN(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/exportPdfSLTN")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response exportPdfSLTN(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/exportPdfSLTNCT")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response exportPdfSLTNCT(WorkItemDetailDTO obj) throws Exception;

    @POST
    @Path("/exportVuongFile")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportVuongFile(ObstructedDetailDTO obj) throws Exception;

    @POST
    @Path("/exportDeliveryBill")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportDeliveryBill(SynStockTransDTO obj) throws Exception;

    @POST
    @Path("/checkPermissionsCancelConfirm")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkPermissionsCancelConfirm(WorkItemDetailDTO obj);

    @POST
    @Path("/checkPermissionsApproved")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response checkPermissionsApproved(WorkItemDetailDTO obj);
    
    /**Hoangnh start 20022019**/
    @POST
    @Path("/doSearchOS")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchOS(WorkItemDetailDTO obj);
    
    @POST
    @Path("/addWorkItemGPon")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addWorkItemGPon(WorkItemDetailDTO obj) throws Exception;
    /**Hoangnh end 20022019**/
    
    
    //tatph - start - 11/11/2019

	@POST
	@Path("/removeGpon")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeGpon(WorkItemGponDTO obj) throws Exception;
	@POST
	@Path("/removeDetailitemGpon")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeDetailitemGpon(WorkItemGponDTO obj);
	@POST
	@Path("/editGpon")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response editGpon(WorkItemGponDTO obj);
    //tatph - end - 11/11/2019
	@POST
	@Path("/saveImage")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveImage(WorkItemDetailDTO obj);
	
	@POST
	@Path("/getAllSourceWork")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllSourceWork(String parType);
}
