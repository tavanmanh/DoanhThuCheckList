package com.viettel.coms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationElectricalRequest {

	private SysUserRequest sysUserRequest;
	private StationElectricalDTO stationElectricalDTO;
	private DeviceStationElectricalDTO deviceStationElectricalDTO;

	@JsonProperty("sysUserRequest")
	public SysUserRequest getSysUserRequest() {
		return sysUserRequest;
	}

	public void setSysUserRequest(SysUserRequest sysUserRequest) {
		this.sysUserRequest = sysUserRequest;
	}

	@JsonProperty("stationElectricalDTO")
	public StationElectricalDTO getStationElectricalDTO() {
		return stationElectricalDTO;
	}

	public void setStationElectricalDTO(StationElectricalDTO stationElectricalDTO) {
		this.stationElectricalDTO = stationElectricalDTO;
	}

	@JsonProperty("deviceStationElectricalDTO")
	public DeviceStationElectricalDTO getDeviceStationElectricalDTO() {
		return deviceStationElectricalDTO;
	}

	public void setDeviceStationElectricalDTO(DeviceStationElectricalDTO deviceStationElectricalDTO) {
		this.deviceStationElectricalDTO = deviceStationElectricalDTO;
	}

}
