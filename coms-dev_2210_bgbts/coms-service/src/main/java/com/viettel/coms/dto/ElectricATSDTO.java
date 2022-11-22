package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ElectricATSBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
public class ElectricATSDTO extends ComsBaseFWDTO<ElectricATSBO>{

	private Long id;
	private Long deviceId;
	private String goodCodeKTTS;
	private Long phaseNumber;
	private Long electricQuota;
	private Long stateEA;
	private String goodName;
	private String goodElectricName;
	private String serial;
	private String model;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeIntoUse;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date lastMaintenanceTime;
	private Long state;
	private String manufacturer;
	private List<AttachElectronicStationDTO> listFileAttach;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listImage3;
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

	public String getGoodCodeKTTS() {
		return goodCodeKTTS;
	}

	public void setGoodCodeKTTS(String goodCodeKTTS) {
		this.goodCodeKTTS = goodCodeKTTS;
	}

	public Long getPhaseNumber() {
		return phaseNumber;
	}

	public void setPhaseNumber(Long phaseNumber) {
		this.phaseNumber = phaseNumber;
	}

	public Long getElectricQuota() {
		return electricQuota;
	}

	public void setElectricQuota(Long electricQuota) {
		this.electricQuota = electricQuota;
	}

	public Long getStateEA() {
		return stateEA;
	}

	public void setStateEA(Long stateEA) {
		this.stateEA = stateEA;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getGoodElectricName() {
		return goodElectricName;
	}

	public void setGoodElectricName(String goodElectricName) {
		this.goodElectricName = goodElectricName;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Date getTimeIntoUse() {
		return timeIntoUse;
	}

	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
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

	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}

	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<AttachElectronicStationDTO> getListImage3() {
		return listImage3;
	}

	public void setListImage3(List<AttachElectronicStationDTO> listImage3) {
		this.listImage3 = listImage3;
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
	public ElectricATSBO toModel() {
		// TODO Auto-generated method stub
		ElectricATSBO obj = new ElectricATSBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setGoodName(this.goodName);
		obj.setGoodElectricName(this.goodElectricName);
		obj.setSerial(this.serial);
		obj.setModel(this.model);
		obj.setGoodCodeKTTS(this.goodCodeKTTS);
		obj.setManufacturer(this.manufacturer);
		obj.setPhaseNumber(this.phaseNumber);
		obj.setElectricQuota(this.electricQuota);
		obj.setStateEA(this.stateEA);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setLastMaintenanceTime(this.lastMaintenanceTime);
		return obj;
	}

}
