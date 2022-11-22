package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ElectricAirConditioningDCBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ElectricAirConditioningDCDTO extends ComsBaseFWDTO<ElectricAirConditioningDCBO>{

	private Long id;
	private Long deviceId;
	private String electricAirConditioningDcName;
	private String serial;
	private Long state;
	private String manufacturer;
	private String model;
	private Long refrigerationCapacity;
	private Long powerCapacity;
	private String goodName;
	private String goodCode;
	private String goodCodeKtts;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date lastMaintenanceTime;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date nearestRepairTime;
	private Long totalRepairCost;
	private Long totalNumberFailures;
	private String typeGas;
	private Long stateEACD;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeIntoUse;
	private List<AttachElectronicStationDTO> listFileAttach;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
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

	public String getElectricAirConditioningDcName() {
		return electricAirConditioningDcName;
	}

	public void setElectricAirConditioningDcName(String electricAirConditioningDcName) {
		this.electricAirConditioningDcName = electricAirConditioningDcName;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
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

	public Long getRefrigerationCapacity() {
		return refrigerationCapacity;
	}

	public void setRefrigerationCapacity(Long refrigerationCapacity) {
		this.refrigerationCapacity = refrigerationCapacity;
	}

	public Long getPowerCapacity() {
		return powerCapacity;
	}

	public void setPowerCapacity(Long powerCapacity) {
		this.powerCapacity = powerCapacity;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getGoodCode() {
		return goodCode;
	}

	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}

	public String getGoodCodeKtts() {
		return goodCodeKtts;
	}

	public void setGoodCodeKtts(String goodCodeKtts) {
		this.goodCodeKtts = goodCodeKtts;
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

	public String getTypeGas() {
		return typeGas;
	}

	public void setTypeGas(String typeGas) {
		this.typeGas = typeGas;
	}

	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}

	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
	}

	public Long getStateEACD() {
		return stateEACD;
	}

	public void setStateEACD(Long stateEACD) {
		this.stateEACD = stateEACD;
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
	public ElectricAirConditioningDCBO toModel() {
		ElectricAirConditioningDCBO bo = new ElectricAirConditioningDCBO();
		bo.setId(this.getId());
		bo.setDeviceId(this.getDeviceId());
		bo.setElectricAirConditioningDcName(this.getElectricAirConditioningDcName());
		bo.setSerial(this.getSerial());
		bo.setStateEACD(this.getStateEACD());
		bo.setManufacturer(this.getManufacturer());
		bo.setModel(this.getModel());
		bo.setRefrigerationCapacity(this.getRefrigerationCapacity());
		bo.setPowerCapacity(this.getPowerCapacity());
		bo.setGoodName(this.getGoodName());
		bo.setGoodCode(this.getGoodCode());
		bo.setGoodCodeKtts(this.getGoodCodeKtts());
		bo.setLastMaintenanceTime(this.getLastMaintenanceTime());
		bo.setNearestRepairTime(this.getNearestRepairTime());
		bo.setTotalRepairCost(this.getTotalRepairCost());
		bo.setTotalNumberFailures(this.getTotalNumberFailures());
		bo.setTypeGas(this.getTypeGas());
		bo.setTimeIntoUse(this.getTimeIntoUse());
		return bo;
	}

}
