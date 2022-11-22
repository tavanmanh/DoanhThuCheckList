package com.viettel.coms.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;

public interface ConfigStaffTangentRsService {

	@POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearch(ConfigStaffTangentDTO obj);
	
	@POST
    @Path("/catProvince/doSearchProvinceInPopup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchProvinceInPopup(CatProvinceDTO obj);
	
	@POST
    @Path("/catProvince/doSearchStaffByPopup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchStaffByPopup(ConfigStaffTangentDTO obj);
	
	@POST
    @Path("/saveConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveConfig(ConfigStaffTangentDTO obj) throws Exception;
	
	@POST
    @Path("/updateConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateConfig(ConfigStaffTangentDTO obj);
	
	@POST
    @Path("/catProvince/doSearchProvinceInPopupByRole")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchProvinceInPopupByRole(CatProvinceDTO obj);
	
	@POST
    @Path("/catProvince/doSearchStaffByConfigProvinceId")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchStaffByConfigProvinceId(ConfigStaffTangentDTO obj);
	
	@POST
    @Path("/removeConfig")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response removeConfig(ConfigStaffTangentDTO obj) throws Exception;
	
}
