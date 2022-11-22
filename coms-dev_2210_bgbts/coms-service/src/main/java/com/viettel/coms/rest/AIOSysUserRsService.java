package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.AIOSysGroupDTO;
import com.viettel.coms.dto.AIOSysUserDTO;

//VietNT_20190506_created
public interface AIOSysUserRsService {

	//Huypq-19082020-start
    @POST
    @Path("/doSearchRegisterCtv")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response doSearch(AIOSysUserDTO obj);
    
    @POST
    @Path("/saveRegisterCtv")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response saveRegisterCtv(AIOSysUserDTO obj) throws Exception;
    
    @POST
    @Path("/getSysGroupTree")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getSysGroupTree(AIOSysGroupDTO obj);
    
    @POST
    @Path("/getImageById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getImageById(AIOSysUserDTO obj);
    
    @POST
    @Path("/updateRegisterCtv")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response updateRegisterCtv(AIOSysUserDTO obj) throws Exception;
    
    @POST
    @Path("/removeRecord")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response removeRecord(AIOSysUserDTO obj);
    
    //Huy-end
    
}
