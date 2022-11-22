package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import com.viettel.coms.bo.ElectricNotificationFilterDustBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ElectricNotificationFilterDustDTO extends ComsBaseFWDTO<ElectricNotificationFilterDustBO>{
	
	private Long id;
	private Long deviceId;
	private String electricNotificationFilterDustName;
	private String serial;
	private String stateSerial;
	private String manufacturer;
	private String model;
	private String goodCode;
	private Long maximumCapacity;
	private String stateKtts;
	private Long state;
	private Long stateENFD;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeIntoUse;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date lastMaintenanceTime;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date nearestRepairTime;
	private Long totalRepairCost;
	private Long totalNumberFailures;
	private List<AttachElectronicStationDTO> listFileAttach;
	private List<AttachElectronicStationDTO> listImage1;
	private String areaCode;
	private String provinceCode;
	private String districtCode;
	private String stationCode;
	private String deviceName;
	private String goodName;
	
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
	public String getElectricNotificationFilterDustName() {
		return electricNotificationFilterDustName;
	}
	public void setElectricNotificationFilterDustName(String electricNotificationFilterDustName) {
		this.electricNotificationFilterDustName = electricNotificationFilterDustName;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getStateSerial() {
		return stateSerial;
	}
	public void setStateSerial(String stateSerial) {
		this.stateSerial = stateSerial;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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
	public Long getMaximumCapacity() {
		return maximumCapacity;
	}
	public void setMaximumCapacity(Long maximumCapacity) {
		this.maximumCapacity = maximumCapacity;
	}
	public String getStateKtts() {
		return stateKtts;
	}
	public void setStateKtts(String stateKtts) {
		this.stateKtts = stateKtts;
	}
	
	public Long getStateENFD() {
		return stateENFD;
	}
	public void setStateENFD(Long stateENFD) {
		this.stateENFD = stateENFD;
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
	public Date getNearestRepairTime() {
		return nearestRepairTime;
	}
	public void setNearestRepairTime(Date nearestRepairTime) {
		this.nearestRepairTime = nearestRepairTime;
	}
	public Long getTotalRepairCost() {
		return totalRepairCost;
	}
	public void setTotalRepairCost(Long totalRepairCost) {
		this.totalRepairCost = totalRepairCost;
	}
	public Long getTotalNumberFailures() {
		return totalNumberFailures;
	}
	public void setTotalNumberFailures(Long totalNumberFailures) {
		this.totalNumberFailures = totalNumberFailures;
	}
	
	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}
	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
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
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
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
	public ElectricNotificationFilterDustBO toModel() {
		ElectricNotificationFilterDustBO bo = new ElectricNotificationFilterDustBO();
		bo.setId(this.getId());
		bo.setDeviceId(this.getDeviceId());
		bo.setElectricNotificationFilterDustName(this.getElectricNotificationFilterDustName());
		bo.setSerial(this.getSerial());
		bo.setStateSerial(this.getStateSerial());
		bo.setManufacturer(this.getManufacturer());
		bo.setModel(this.getModel());
		bo.setGoodCode(this.getGoodCode());
		bo.setMaximumCapacity(this.getMaximumCapacity());
		bo.setStateKtts(this.getStateKtts());
		bo.setStateENFD(this.getStateENFD());
		bo.setTimeIntoUse(this.getTimeIntoUse());
		bo.setLastMaintenanceTime(this.getLastMaintenanceTime());
		bo.setNearestRepairTime(this.getNearestRepairTime());
		bo.setTotalRepairCost(this.getTotalRepairCost());
		bo.setTotalNumberFailures(this.getTotalNumberFailures());
		return bo;
	}
	
	
}
