package com.viettel.coms.business;

import com.viettel.coms.bo.DeviceStationElectricalBO;
import java.util.Date;
import java.util.List;

import com.viettel.coms.dto.DeviceStationElectricalDTO;
import com.viettel.coms.dto.StationElectricalDTO;
import com.viettel.coms.dto.StationElectricalRequest;
import com.viettel.wms.dto.AppParamDTO;

public interface ManageMEBusiness {

	public List<StationElectricalDTO> doSearch(StationElectricalDTO obj, Long sysUserId);
	
	public List<DeviceStationElectricalDTO> getDevices(DeviceStationElectricalDTO obj);
	
	public List<AppParamDTO> getAppParamByParType(String parType);
	
	public void saveDeviceDetail(StationElectricalRequest request);

	public DeviceStationElectricalDTO createBroken(DeviceStationElectricalDTO obj);

	public DeviceStationElectricalDTO updateBroken(DeviceStationElectricalDTO obj);
}
