package com.viettel.coms.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;
import com.viettel.cat.dto.CatPartnerDTO;
import com.viettel.coms.business.ManageMEBusinessImpl;
import com.viettel.coms.business.UtilAttachDocumentBusinessImpl;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.CabinetsSourceACDTO;
import com.viettel.coms.dto.CabinetsSourceDCDTO;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.DeviceStationElectricalDTO;
import com.viettel.coms.dto.DocManagementDTO;
import com.viettel.coms.dto.ElectricAirConditioningACDTO;
import com.viettel.coms.dto.ElectricAirConditioningDCDTO;
import com.viettel.coms.dto.ElectricDetailDTO;
import com.viettel.coms.dto.ElectricHeatExchangerDTO;
import com.viettel.coms.dto.ElectricNotificationFilterDustDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.ReportWoDTO;
import com.viettel.coms.dto.StationElectricalDTO;
import com.viettel.coms.dto.UnitListDTO;
import com.viettel.coms.dto.UserDirectoryDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.utils.BaseResponseOBJ;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.wms.business.UserRoleBusinessImpl;

public class ManageMERsServiceImpl implements ManageMERsService {

	protected final Logger log = Logger.getLogger(ManageMERsServiceImpl.class);
	
	@Autowired
	ManageMEBusinessImpl manageMEBusinessImpl;
	@Context
	HttpServletRequest request;
	@Autowired
	private UserRoleBusinessImpl userRoleBusinessImpl;
	
	@Autowired
    UtilAttachDocumentDAO utilAttachDocumentDAO;

	@Autowired
	UtilAttachDocumentBusinessImpl  utilAttachDocumentBusinessImpl;
	
	@Value("${folder_upload2}")
	private String folderUpload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	@Value("${allow.file.ext}")
	private String allowFileExt;
	@Value("${allow.folder.dir}")
	private String allowFolderDir;
	@Value("${folder_upload}")
	private String folderTemp;
	
	
	
	@Override
	public Response doSearch(StationElectricalDTO obj) {
		System.out.println(obj.getPageSize());
		System.out.println(obj.getTotalRecord());
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Boolean roleVhkt = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.DEVICE_ELECTRICT, request);
		Boolean roleCNKT = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.DEVICE_ELECTRICT_CNKT, request);
		Long sysUserId = objUser.getSysUserId();
		if(roleVhkt) {
			sysUserId = null;
		}
		if(roleCNKT) {
			Long provinceId = manageMEBusinessImpl.getProvinceId(objUser.getVpsUserInfo().getSysGroupId());
			if(obj.getProvinceId() != null) {
				if(obj.getProvinceId() !=  provinceId) {
					List<StationElectricalDTO> ls = new ArrayList<StationElectricalDTO>();
					 DataListDTO data = new DataListDTO();
			            data.setData(ls);
			            data.setTotal(obj.getTotalRecord());
			            data.setSize(obj.getPageSize());
			            data.setStart(1);
			            return Response.ok(data).build();
				}
			}
			obj.setProvinceId(provinceId);
			sysUserId = null;
		}
		List<StationElectricalDTO> ls = manageMEBusinessImpl.doSearch(obj, sysUserId);
		if (ls == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
        	DataListDTO data = new DataListDTO();
        	data.setData(ls);
            data.setTotal(obj.getTotalRecord());
            data.setSize(obj.getPageSize());
            data.setStart(1);
            return Response.ok(data).build();
        }
	}
	
//	@Override
//	public Response saveDevice(DeviceStationElectricalDTO obj) {
//		// TODO Auto-generated method stub
//		String returnStr = manageMEBusinessImpl.saveDevice(obj);
//        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
//        return Response.ok(resp).build();
//	}
//	
//	@Override
//	public Response updateDevice(DeviceStationElectricalDTO obj) {
//		// TODO Auto-generated method stub
//		String returnStr = manageMEBusinessImpl.updateDevice(obj);
//        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
//        return Response.ok(resp).build();
//	}
	
	@Override
	public Response getDevices(DeviceStationElectricalDTO obj) {

		List<DeviceStationElectricalDTO> ls = manageMEBusinessImpl.getDevices(obj);
		if (ls == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            DataListDTO data = new DataListDTO();
            data.setData(ls);
            data.setTotal(obj.getTotalRecord());
            data.setSize(obj.getPageSize());
            data.setStart(1);
            return Response.ok(data).build();
        }
	}
	
	@Override
	public Response getEquipments(AppParamDTO obj) {
		obj.setParType("DIVICE_ELECTRIC");
		DataListDTO data = manageMEBusinessImpl.getEquipments(obj);
		return Response.ok(data).build();
		
	}
	
	@Override
	public Response getDeviceDetails(DeviceStationElectricalDTO obj) {
		DataListDTO data = manageMEBusinessImpl.getDeviceDetails(obj, false);
		return Response.ok(data).build();
	}

	@Override
	public void approve(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		manageMEBusinessImpl.approve(obj,objUser.getSysUserId());
	}
	@Override
	public void reject(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		manageMEBusinessImpl.reject(obj,objUser.getSysUserId());
	}

	@Override
	public Response saveDevice(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		DeviceStationElectricalDTO existing = (DeviceStationElectricalDTO) manageMEBusinessImpl.findByTypeAndSerial(obj);
        if (existing != null) {
            return Response.status(Response.Status.CONFLICT).build();
        } else {
            obj.setCreateDate(new Timestamp(System.currentTimeMillis()));
            obj.setCreateUser(objUser.getSysUserId());
            obj.setStatus("1");
            obj.setState("1");
            Long deviceId = manageMEBusinessImpl.save(obj);
            obj.setDeviceId(deviceId);
            if (deviceId == 0l) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
            	UtilAttachDocumentDTO attachFile = obj.getAttachFile();
            	attachFile.setObjectId(deviceId);
            	attachFile.setType("ELECTRIC_FILE");
				Long id2 = utilAttachDocumentBusinessImpl.save(attachFile);
				if (id2 == 0l) {
		            return Response.status(Response.Status.BAD_REQUEST).build();
		        } else {
		        	return Response.ok(Response.Status.CREATED).build();
		        }
                
            }
        }
	}
	
	@Override
	public Response updateDevice(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		if(obj.getStatus().equals("2")) {
			Long id = manageMEBusinessImpl.update(obj);
			return Response.ok(Response.Status.CREATED).build();
		}else {
			KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
			DeviceStationElectricalDTO existing = (DeviceStationElectricalDTO) manageMEBusinessImpl.findByTypeAndSerial(obj);
	        if (existing != null) {
	            return Response.status(Response.Status.CONFLICT).build();
	        } else {
	            Long id = manageMEBusinessImpl.update(obj);
	            if (id == 0l) {
	                return Response.status(Response.Status.BAD_REQUEST).build();
	            } else {
	            	List<String> lstType = Lists.newArrayList();
	            	lstType.add("ATTACH_FILE");
	            	utilAttachDocumentDAO.deleteUtilAttachDocument(obj.getDeviceId(),lstType);
	            	UtilAttachDocumentDTO attachFile = obj.getAttachFile();
	            	attachFile.setObjectId(obj.getDeviceId());
	            	attachFile.setType("ATTACH_FILE");
					Long id2 = utilAttachDocumentBusinessImpl.save(attachFile);
					if (id2 == 0l) {
			            return Response.status(Response.Status.BAD_REQUEST).build();
			        } else {
			        	return Response.ok(Response.Status.CREATED).build();
			        }
	                
	            }
	        }
		}
		
	}
	
	@Override
	public Response saveDeviceLD(ElectricDetailDTO obj) {
		// TODO Auto-generated method stub
		String returnStr = manageMEBusinessImpl.saveDeviceLD(obj);
        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
        return Response.ok(resp).build();
		
	}
	
	@Override
	public Response saveDeviceTuNguonAC(CabinetsSourceACDTO obj) {
		// TODO Auto-generated method stub
		String returnStr = manageMEBusinessImpl.saveDeviceTuNguonAC(obj);
        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
        return Response.ok(resp).build();
		
	}
	
	@Override
	public Response saveDeviceTuNguonDC(CabinetsSourceDCDTO obj) {
		// TODO Auto-generated method stub
		String returnStr = manageMEBusinessImpl.saveDeviceTuNguonDC(obj);
        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
        return Response.ok(resp).build();
		
	}
	
	@Override
	public Response saveDeviceNHIET(ElectricHeatExchangerDTO obj) {
		// TODO Auto-generated method stub
		String returnStr = manageMEBusinessImpl.saveDeviceNHIET(obj);
        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
        return Response.ok(resp).build();
		
	}
	
	@Override
	public Response saveDeviceDHAC(ElectricAirConditioningACDTO obj) {
		// TODO Auto-generated method stub
		String returnStr = manageMEBusinessImpl.saveDeviceDHAC(obj);
        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
        return Response.ok(resp).build();
	}
	
	@Override
	public Response saveDeviceTG(ElectricNotificationFilterDustDTO obj) {
		// TODO Auto-generated method stub
		String returnStr = manageMEBusinessImpl.saveDeviceTG(obj);
        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
        return Response.ok(resp).build();
	}
	
	@Override
	public Response saveDeviceDHDC(ElectricAirConditioningDCDTO obj) {
		// TODO Auto-generated method stub
		String returnStr = manageMEBusinessImpl.saveDeviceDHDC(obj);
        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
        return Response.ok(resp).build();
	}
	

	@Override
	public StationElectricalDTO checkRoleCD(StationElectricalDTO obj) {
		Boolean checkRoleCD = VpsPermissionChecker.hasPermission(Constant.OperationKey.CD,
				Constant.AdResourceKey.DEVICE_ELECTRICT_CNKT, request);
		StationElectricalDTO dto = new StationElectricalDTO();
		dto.setCheckRoleCD(checkRoleCD);
		Long id = manageMEBusinessImpl.getProvinceId(obj.getSysGroupId());
		dto.setProvinceId(id);
        return dto;
	}

	@Override
	public Response updateBroken(DeviceStationElectricalDTO obj) {
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setApprovedUser(objUser.getSysUserId());
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.BROCKEN_ELECTRIC,
			request)) {
			return Response.ok()
				.entity(Collections.singletonMap("error", "Không có quyền thực hiện duyệt"))
				.build();
		}
		manageMEBusinessImpl.updateBroken(obj);
		return Response.ok(obj).build();
	}

	@Override
	public Response doSearchUser(SysUserDTO obj) {
		// TODO Auto-generated method stub
		List<SysUserDTO> lst = manageMEBusinessImpl.doSearchUser(obj);
		DataListDTO data = new DataListDTO();
        data.setData(lst);
        data.setStart(1);
        return Response.ok(data).build();
	}
	
	@Override
	public Response saveManageStation(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		String returnStr = manageMEBusinessImpl.saveManageStation(obj);
        BaseResponseOBJ resp = new BaseResponseOBJ(1, returnStr, null);
        return Response.ok(resp).build();
	}

	@Override
	public Response getInforDashboard(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StationElectricalDTO dto = manageMEBusinessImpl.getInforDashboard(obj);
        return Response.ok(dto).build();
	}
	
	@Override
	public Response getStation(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
        List<StationElectricalDTO> lst = manageMEBusinessImpl.getStation(obj);
		DataListDTO data = new DataListDTO();
        data.setData(lst);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return Response.ok(data).build();
	}
	
	@Override
    public Response doSearchWo(WoDTO obj, HttpServletRequest request) throws Exception {
        List<WoDTO> lst = manageMEBusinessImpl.doSearchWo(obj);
		DataListDTO data = new DataListDTO();
        data.setData(lst);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return Response.ok(data).build();
    }

	@Override
	public Boolean checkUserKCQTD(String employeeCode) throws Exception {
		// TODO Auto-generated method stub
		return manageMEBusinessImpl.checkUserKCQTD(employeeCode);
	}

	@Override
	public Response doSearchUserSysGroup(SysUserDTO obj) {
		// TODO Auto-generated method stub
		List<SysUserDTO> lst = new ArrayList<>();
		if(obj.getCheckUserKCQTD()) {
			lst = manageMEBusinessImpl.doSearchUserSysGroup(obj);
		}else {
			lst = manageMEBusinessImpl.doSearchUser(obj);
		}
		DataListDTO data = new DataListDTO();
        data.setData(lst);
        data.setStart(1);
        return Response.ok(data).build();
	}

	@Override
	public Response exportExcel(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		try {
			String strReturn = manageMEBusinessImpl.exportExcel(obj, request);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch(Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	//ducpm-start
	@Override
	public Response getManager(WoDTO obj) {
		List<WoDTO> list = new ArrayList<>();
		list = manageMEBusinessImpl.getManager(obj);
		DataListDTO data = new DataListDTO();
        data.setData(list);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return Response.ok(data).build();
	}

	@Override
	public Response getUpdateElectric(WoDTO obj) {
		List<WoDTO> list = new ArrayList<>();
		list = manageMEBusinessImpl.getUpdateElectric(obj);
		DataListDTO data = new DataListDTO();
		data.setTotal(obj.getTotalRecord());
	    data.setSize(obj.getPageSize());
        data.setData(list);
        return Response.ok(data).build();
	}
	
	@Override
	public Response getUserDirectory(UserDirectoryDTO obj) {
		List<UserDirectoryDTO> list = new ArrayList<>();
		list = manageMEBusinessImpl.getListUserDirectory(obj);
		DataListDTO data = new DataListDTO();
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setData(list);
		return Response.ok(data).build();
	}
	
	@Override
	public Response doSearchtUserDirectory(UserDirectoryDTO obj) {
		List<UserDirectoryDTO> list = new ArrayList<>();
		list = manageMEBusinessImpl.getDoSearchUserDirectoryList(obj);
		DataListDTO data = new DataListDTO();
		data.setData(list);
		return Response.ok(data).build();
	}

	@Override
	public Response saveUserDirectory(UserDirectoryDTO obj) {
		UserDirectoryDTO userTmp = new UserDirectoryDTO();
		userTmp.setLoginName(obj.getLoginName());
		List<UserDirectoryDTO> exsiting = manageMEBusinessImpl.getListUserDirectory(userTmp);
		if(exsiting.size() > 0) {
			return Response.status(Response.Status.CONFLICT).build();
		}else {
			Long res = manageMEBusinessImpl.saveUserDirectory(obj);
			return Response.ok(res).build();
		}
	}
	
	@Override
	public Response updateUserDirectory(UserDirectoryDTO obj) {
		manageMEBusinessImpl.updateUserDirectory(obj);
		return Response.ok(1).build();
	}

	@Override
	public Response removeUserDirectory(UserDirectoryDTO obj) {
		manageMEBusinessImpl.removeUserDirectory(obj);
		return Response.ok(1).build();
	}

	@Override
	public Response getUnitList(UnitListDTO obj) {
		List<UnitListDTO> list = new ArrayList<>();
		list = manageMEBusinessImpl.getUnitList(obj);
		DataListDTO data = new DataListDTO();
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setData(list);
		return Response.ok(data).build();
	}

	@Override
	public Response doSearchUnitList(UnitListDTO obj) {
		List<UnitListDTO> list = new ArrayList<>();
		list = manageMEBusinessImpl.doSearchUnitList(obj);
		DataListDTO data = new DataListDTO();
		data.setData(list);
		return Response.ok(data).build();
	}

	@Override
	public Response saveUnitList(UnitListDTO obj) {
		UnitListDTO unitTmp = new UnitListDTO();
		unitTmp.setUnitCode(obj.getUnitCode());
		List<UnitListDTO> exsiting = manageMEBusinessImpl.getUnitList(unitTmp);
		if(exsiting.size() > 0) {
			return Response.status(Response.Status.CONFLICT).build();
		}else {
			Long res = manageMEBusinessImpl.saveUnitList(obj);
			return Response.ok(res).build();
		}
	}
	
	@Override
	public Response removeUnitList(UnitListDTO obj) {
		manageMEBusinessImpl.removeUnitList(obj);
		return Response.ok(1).build();
	}

	@Override
	public Response updateUnitList(UnitListDTO obj) {
		manageMEBusinessImpl.updateUnitList(obj);
		return Response.ok(1).build();
	}

	@Override
	public Response getDocManagementList(DocManagementDTO obj) {
		List<DocManagementDTO> list = new ArrayList<>();
		list = manageMEBusinessImpl.getDocManagementList(obj);
		DataListDTO data = new DataListDTO();
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setData(list);
		return Response.ok(data).build();
	}

	@Override
	public Response saveDocManagement(DocManagementDTO obj) {
		//KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<DocManagementDTO> existing = manageMEBusinessImpl.getDocManagementList(obj);
		 if (existing.size() > 0 ) {
	            return Response.status(Response.Status.CONFLICT).build();
	      }else {
	    	  Long docManagementId = manageMEBusinessImpl.save(obj);
	          obj.setId(docManagementId);
	          UtilAttachDocumentDTO attachFile = obj.getAttachFile();
	       	attachFile.setObjectId(docManagementId);
	       	attachFile.setType("DOCUMENT_MANAGEMENT");
	  			Long id2 = utilAttachDocumentBusinessImpl.save(attachFile);
	  			if (id2 == 0l) {
	  	            return Response.status(Response.Status.BAD_REQUEST).build();
	  	        } else {
	  	        	return Response.ok(Response.Status.CREATED).build();
	  	        }
	      } 
	}

	@Override
	public Response removeDocManagement(DocManagementDTO obj) {
		manageMEBusinessImpl.removeDocManagement(obj);
		return Response.ok(1).build();
	}

	@Override
	public Response updateDocManagement(DocManagementDTO obj) {
		manageMEBusinessImpl.updateDocManagement(obj);
		return Response.ok(1).build();
	}

	//ducpm-end
}
