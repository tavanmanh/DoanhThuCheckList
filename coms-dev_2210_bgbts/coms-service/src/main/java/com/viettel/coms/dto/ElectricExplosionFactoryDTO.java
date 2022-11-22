package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ElectricExplosionFactoryBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
public class ElectricExplosionFactoryDTO extends ComsBaseFWDTO<ElectricExplosionFactoryBO>{

	private Long id;
	private Long deviceId;
	private String electricExplosionName;
	private String houseType;
	private Long igniterSettingStatus;
	private Long state;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeIntoUse;
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

	public String getElectricExplosionName() {
		return electricExplosionName;
	}

	public void setElectricExplosionName(String electricExplosionName) {
		this.electricExplosionName = electricExplosionName;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public Long getIgniterSettingStatus() {
		return igniterSettingStatus;
	}

	public void setIgniterSettingStatus(Long igniterSettingStatus) {
		this.igniterSettingStatus = igniterSettingStatus;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Date getTimeIntoUse() {
		return timeIntoUse;
	}

	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
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
	public ElectricExplosionFactoryBO toModel() {
		// TODO Auto-generated method stub
		ElectricExplosionFactoryBO obj = new ElectricExplosionFactoryBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setHouseType(this.houseType);
		obj.setIgniterSettingStatus(this.igniterSettingStatus);
		obj.setElectricExplosionName(this.electricExplosionName);
		obj.setTimeIntoUse(this.timeIntoUse);
		return obj;
	}

}
