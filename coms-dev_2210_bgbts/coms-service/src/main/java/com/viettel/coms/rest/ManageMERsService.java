package com.viettel.coms.rest;

import javax.servlet.http.HttpServletRequest;
import com.viettel.service.base.dto.DataListDTO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.CabinetsSourceACDTO;
import com.viettel.coms.dto.CabinetsSourceDCDTO;
import com.viettel.coms.dto.DeviceStationElectricalDTO;
import com.viettel.coms.dto.DocManagementDTO;
import com.viettel.coms.dto.ElectricAirConditioningACDTO;
import com.viettel.coms.dto.ElectricAirConditioningDCDTO;
import com.viettel.coms.dto.ElectricDetailDTO;
import com.viettel.coms.dto.ElectricHeatExchangerDTO;
import com.viettel.coms.dto.ElectricNotificationFilterDustDTO;
import com.viettel.coms.dto.StationElectricalDTO;
import com.viettel.coms.dto.UnitListDTO;
import com.viettel.coms.dto.UserDirectoryDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.erp.dao.SysUserDAO;
import com.viettel.erp.dto.SysUserDTO;

public interface ManageMERsService {
	@POST
    @Path("/station/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearch(StationElectricalDTO obj);
	
	@POST
    @Path("/station/device/getDevices")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDevices(DeviceStationElectricalDTO obj);
	
	@POST
	@Path("/getEquipments")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getEquipments(AppParamDTO obj);
	
	@POST
    @Path("/getDeviceDetails")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeviceDetails(DeviceStationElectricalDTO obj);
	
//	@POST
//    @Path("/saveDevice")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response saveDevice(DeviceStationElectricalDTO obj);
//	
//	@POST
//    @Path("/updateDevice")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response updateDevice(DeviceStationElectricalDTO obj);
	
	@POST
    @Path("/approve")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void approve(DeviceStationElectricalDTO obj);
	
	@POST
    @Path("/reject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void reject(DeviceStationElectricalDTO obj);
	
	@POST
    @Path("/saveDeviceLD")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveDeviceLD(ElectricDetailDTO obj);
	
	@POST
    @Path("/saveDevice")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveDevice(DeviceStationElectricalDTO obj);
	
	@POST
    @Path("/updateDevice")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateDevice(DeviceStationElectricalDTO obj);
	
	@POST
    @Path("/saveDeviceTuNguonAC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveDeviceTuNguonAC(CabinetsSourceACDTO obj);
	
	@POST
    @Path("/saveDeviceTuNguonDC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveDeviceTuNguonDC(CabinetsSourceDCDTO obj);
	
	@POST
    @Path("/saveDeviceNHIET")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveDeviceNHIET(ElectricHeatExchangerDTO obj);
	
	@POST
    @Path("/saveDeviceDHAC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveDeviceDHAC(ElectricAirConditioningACDTO obj);
	
	@POST
    @Path("/saveDeviceTG")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveDeviceTG(ElectricNotificationFilterDustDTO obj);
	
	@POST
    @Path("/saveDeviceDHDC")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveDeviceDHDC(ElectricAirConditioningDCDTO obj);
	
	@POST
    @Path("/doSearchUser")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchUser(SysUserDTO obj);
	
	@POST
    @Path("/saveManageStation")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveManageStation(StationElectricalDTO obj);
	
	@POST
    @Path("/checkRoleCD")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public StationElectricalDTO checkRoleCD(StationElectricalDTO obj);
	
	@POST
    @Path("/getInforDashboard")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getInforDashboard(StationElectricalDTO obj);
	
	@POST
    @Path("/station/doSearchDetail")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getStation(StationElectricalDTO obj);
	
	@POST
    @Path("/wo/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWo(WoDTO woDto, @Context HttpServletRequest request) throws Exception;
	
	@POST
    @Path("/checkUserKCQTD")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Boolean checkUserKCQTD(String employeeCode) throws Exception;
	
	@POST
    @Path("/doSearchUserSysGroup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchUserSysGroup(SysUserDTO obj);
	
	@POST
	@Path("/exportExcel")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportExcel(StationElectricalDTO obj);
	

	
//	  @POST
//    @Path("history/getUpdateGenerator")
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response getUpdateGenerator(WoDTO obj);
	
//ducpm23-start
	@POST
    @Path("/getManager")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getManager(WoDTO obj);
	
	@POST
    @Path("/history/getUpdateElectric")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUpdateElectric(WoDTO obj);
	
	@POST
    @Path("/userDirectory/doSearchUserDirectory")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUserDirectory(UserDirectoryDTO obj);
	
	@POST
    @Path("/userDirectory/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchtUserDirectory(UserDirectoryDTO obj);
	
	@POST
    @Path("/userDirectory/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveUserDirectory(UserDirectoryDTO obj);
	
	@POST
    @Path("/userDirectory/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateUserDirectory(UserDirectoryDTO obj);
	
	@POST
    @Path("/userDirectory/remove")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response removeUserDirectory(UserDirectoryDTO obj);
	
	@POST
    @Path("/unitList/doSearchUnitList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUnitList(UnitListDTO obj);
	
	@POST
    @Path("/unitList/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchUnitList(UnitListDTO obj);
	
	@POST
    @Path("/unitList/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveUnitList(UnitListDTO obj);
	
	@POST
    @Path("/unitList/remove")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response removeUnitList(UnitListDTO obj);
	
	@POST
    @Path("/unitList/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateUnitList(UnitListDTO obj);
	
	@POST
    @Path("/documentManagement/getList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDocManagementList(DocManagementDTO obj);
	
	@POST
    @Path("/documentManagement/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response saveDocManagement(DocManagementDTO obj);
	
	@POST
    @Path("/documentManagement/remove")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response removeDocManagement(DocManagementDTO obj);
	
	@POST
    @Path("/documentManagement/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateDocManagement(DocManagementDTO obj);
//ducpm23-end
	
//	@POST
//	@Path("/exportWo")
//	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public Response exportWo(WoDTO obj);


    @POST
    @Path("/updateBroken")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateBroken(DeviceStationElectricalDTO obj);

}
