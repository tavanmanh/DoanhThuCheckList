package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.BatteryBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

public class BatteryDTO extends ComsBaseFWDTO<BatteryBO>{

	private Long id;
	private Long deviceId;
	private String batteryName;
	private String model;
	private String productionTechnology;
	private String goodCode;
	private String manufactuber;
	private String batteryType;
	private Long capacity;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeIntoUse;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date stationOutputTimeAfterRecover;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date lastMaintenanceTime;
	private Long state;
	private List<AttachElectronicStationDTO> listFileAttach;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listImage3;
	private List<AttachElectronicStationDTO> listImage4;
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

	public String getBatteryName() {
		return batteryName;
	}

	public void setBatteryName(String batteryName) {
		this.batteryName = batteryName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getGoodCode() {
		return goodCode;
	}

	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}

	public String getManufactuber() {
		return manufactuber;
	}

	public void setManufactuber(String manufactuber) {
		this.manufactuber = manufactuber;
	}

	public String getBatteryType() {
		return batteryType;
	}

	public void setBatteryType(String batteryType) {
		this.batteryType = batteryType;
	}

	public Long getCapacity() {
		return capacity;
	}

	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}

	public Date getTimeIntoUse() {
		return timeIntoUse;
	}

	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}

	public Date getStationOutputTimeAfterRecover() {
		return stationOutputTimeAfterRecover;
	}

	public void setStationOutputTimeAfterRecover(Date stationOutputTimeAfterRecover) {
		this.stationOutputTimeAfterRecover = stationOutputTimeAfterRecover;
	}

	public Date getLastMaintenanceTime() {
		return lastMaintenanceTime;
	}

	public void setLastMaintenanceTime(Date lastMaintenanceTime) {
		this.lastMaintenanceTime = lastMaintenanceTime;
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

	public List<AttachElectronicStationDTO> getListImage3() {
		return listImage3;
	}

	public void setListImage3(List<AttachElectronicStationDTO> listImage3) {
		this.listImage3 = listImage3;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public String getProductionTechnology() {
		return productionTechnology;
	}

	public void setProductionTechnology(String productionTechnology) {
		this.productionTechnology = productionTechnology;
	}

	public List<AttachElectronicStationDTO> getListImage4() {
		return listImage4;
	}

	public void setListImage4(List<AttachElectronicStationDTO> listImage4) {
		this.listImage4 = listImage4;
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
	public BatteryBO toModel() {
		// TODO Auto-generated method stub
		BatteryBO obj = new BatteryBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setBatteryName(this.batteryName);
		obj.setGoodCode(this.goodCode);
		obj.setModel(this.model);
		obj.setManufactuber(this.manufactuber);
		obj.setBatteryType(this.batteryType);
		obj.setCapacity(this.capacity);
		obj.setProductionTechnology(this.productionTechnology);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setStationOutputTimeAfterRecover(this.stationOutputTimeAfterRecover);
		obj.setLastMaintenanceTime(this.lastMaintenanceTime);
		return obj;
	}

}
