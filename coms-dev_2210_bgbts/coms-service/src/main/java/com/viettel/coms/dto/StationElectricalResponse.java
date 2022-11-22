package com.viettel.coms.dto;

import java.util.List;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.wms.dto.AppParamDTO;

public class StationElectricalResponse {

	private ResultInfo resultInfo;
	private List<StationElectricalDTO> lstStationElectricalDTO;
	private List<DeviceStationElectricalDTO> lstDeviceStationElectricalDTO;
	private List<AppParamDTO> lstDeviceType;
	private Object data;
	
	public ResultInfo getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(ResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}
	public List<StationElectricalDTO> getLstStationElectricalDTO() {
		return lstStationElectricalDTO;
	}
	public void setLstStationElectricalDTO(List<StationElectricalDTO> lstStationElectricalDTO) {
		this.lstStationElectricalDTO = lstStationElectricalDTO;
	}
	public List<DeviceStationElectricalDTO> getLstDeviceStationElectricalDTO() {
		return lstDeviceStationElectricalDTO;
	}
	public void setLstDeviceStationElectricalDTO(List<DeviceStationElectricalDTO> lstDeviceStationElectricalDTO) {
		this.lstDeviceStationElectricalDTO = lstDeviceStationElectricalDTO;
	}
	public List<AppParamDTO> getLstDeviceType() {
		return lstDeviceType;
	}
	public void setLstDeviceType(List<AppParamDTO> lstDeviceType) {
		this.lstDeviceType = lstDeviceType;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
