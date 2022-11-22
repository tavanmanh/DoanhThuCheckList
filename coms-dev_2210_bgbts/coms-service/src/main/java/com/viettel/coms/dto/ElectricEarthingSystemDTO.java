package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ElectricEarthingSytemBO;
import com.viettel.coms.bo.ElectricLightningCutFilterBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

public class ElectricEarthingSystemDTO extends ComsBaseFWDTO<ElectricEarthingSytemBO>{

	private Long id;
	private Long deviceId;
	private String electricEarthingSystemName;
	private Long groundResistance;
	private Long groundStatus;
	private Long primaryCondition;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date lastMaintenanceTime;
	private Long state;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listFileAttach;
	private String areaCode;
	private String provinceCode;
	private String districtCode;
	private String stationCode;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getElectricEarthingSystemName() {
		return electricEarthingSystemName;
	}

	public void setElectricEarthingSystemName(String electricEarthingSystemName) {
		this.electricEarthingSystemName = electricEarthingSystemName;
	}

	public Long getGroundResistance() {
		return groundResistance;
	}

	public void setGroundResistance(Long groundResistance) {
		this.groundResistance = groundResistance;
	}

	public Long getGroundStatus() {
		return groundStatus;
	}

	public void setGroundStatus(Long groundStatus) {
		this.groundStatus = groundStatus;
	}

	public Long getPrimaryCondition() {
		return primaryCondition;
	}

	public void setPrimaryCondition(Long primaryCondition) {
		this.primaryCondition = primaryCondition;
	}

	public Date getLastMaintenanceTime() {
		return lastMaintenanceTime;
	}

	public void setLastMaintenanceTime(Date lastMaintenanceTime) {
		this.lastMaintenanceTime = lastMaintenanceTime;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public List<AttachElectronicStationDTO> getListImage1() {
		return listImage1;
	}

	public void setListImage1(List<AttachElectronicStationDTO> listImage1) {
		this.listImage1 = listImage1;
	}

	public List<AttachElectronicStationDTO> getListImage2() {
		return listImage2;
	}

	public void setListImage2(List<AttachElectronicStationDTO> listImage2) {
		this.listImage2 = listImage2;
	}

	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}

	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElectricEarthingSytemBO toModel() {
		// TODO Auto-generated method stub
		ElectricEarthingSytemBO obj = new ElectricEarthingSytemBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setElectricEarthingSystemName(this.electricEarthingSystemName);
		obj.setGroundResistance(this.groundResistance);
		obj.setGroundStatus(this.groundStatus);
		obj.setPrimaryCondition(this.primaryCondition);
		obj.setLastMaintenanceTime(this.lastMaintenanceTime);
		return obj;
	}

}
