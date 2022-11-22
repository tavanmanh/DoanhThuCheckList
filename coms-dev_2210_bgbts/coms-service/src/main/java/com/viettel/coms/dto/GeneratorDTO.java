package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.CabinetsSourceDCBO;
import com.viettel.coms.bo.GeneratorBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

public class GeneratorDTO extends ComsBaseFWDTO<GeneratorBO>{

	private Long id;
	private Long deviceId;
	private String generatorName;
	private String model;
	private String manufactuber;
	private String fuelType;
	private String serial;
	private String stationCodeBeforeTransfer;
	private Long ratedPower;
	private Long wattageMax;
	private Long workstationStatus;
	private Long distanceGeneratorStation;
	private Long status;
	private Long totalRunningTime;
	private Long totalRepairCost;
	private Long totalNumberFailures;
	private String generatorOverCapacity;
	private Long ftConfirmGeneratorOverCapacity;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeIntoUse;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeLastMaintenance;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date lastRepairTime;
	private Long state;
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

	public String getGeneratorName() {
		return generatorName;
	}

	public void setGeneratorName(String generatorName) {
		this.generatorName = generatorName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufactuber() {
		return manufactuber;
	}

	public void setManufactuber(String manufactuber) {
		this.manufactuber = manufactuber;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getStationCodeBeforeTransfer() {
		return stationCodeBeforeTransfer;
	}

	public void setStationCodeBeforeTransfer(String stationCodeBeforeTransfer) {
		this.stationCodeBeforeTransfer = stationCodeBeforeTransfer;
	}

	public Long getRatedPower() {
		return ratedPower;
	}

	public void setRatedPower(Long ratedPower) {
		this.ratedPower = ratedPower;
	}

	public Long getWattageMax() {
		return wattageMax;
	}

	public void setWattageMax(Long wattageMax) {
		this.wattageMax = wattageMax;
	}

	public Long getWorkstationStatus() {
		return workstationStatus;
	}

	public void setWorkstationStatus(Long workstationStatus) {
		this.workstationStatus = workstationStatus;
	}

	public Long getDistanceGeneratorStation() {
		return distanceGeneratorStation;
	}

	public void setDistanceGeneratorStation(Long distanceGeneratorStation) {
		this.distanceGeneratorStation = distanceGeneratorStation;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getTotalRunningTime() {
		return totalRunningTime;
	}

	public void setTotalRunningTime(Long totalRunningTime) {
		this.totalRunningTime = totalRunningTime;
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

	public String getGeneratorOverCapacity() {
		return generatorOverCapacity;
	}

	public void setGeneratorOverCapacity(String generatorOverCapacity) {
		this.generatorOverCapacity = generatorOverCapacity;
	}

	public Long getFtConfirmGeneratorOverCapacity() {
		return ftConfirmGeneratorOverCapacity;
	}

	public void setFtConfirmGeneratorOverCapacity(Long ftConfirmGeneratorOverCapacity) {
		this.ftConfirmGeneratorOverCapacity = ftConfirmGeneratorOverCapacity;
	}

	public Date getTimeIntoUse() {
		return timeIntoUse;
	}

	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}

	public Date getTimeLastMaintenance() {
		return timeLastMaintenance;
	}

	public void setTimeLastMaintenance(Date timeLastMaintenance) {
		this.timeLastMaintenance = timeLastMaintenance;
	}

	public Date getLastRepairTime() {
		return lastRepairTime;
	}

	public void setLastRepairTime(Date lastRepairTime) {
		this.lastRepairTime = lastRepairTime;
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
	public GeneratorBO toModel() {
		// TODO Auto-generated method stub
		GeneratorBO obj = new GeneratorBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setGeneratorName(this.generatorName);
		obj.setModel(this.model);
		obj.setRatedPower(this.ratedPower);
		obj.setManufactuber(this.manufactuber);
		obj.setFuelType(this.fuelType);
		obj.setWattageMax(this.wattageMax);
		obj.setSerial(this.serial);
		obj.setWorkstationStatus(this.workstationStatus);
		obj.setDistanceGeneratorStation(this.distanceGeneratorStation);
		obj.setStatus(this.status);
		obj.setStationCodeBeforeTransfer(this.stationCodeBeforeTransfer);
		obj.setTotalRunningTime(this.totalRunningTime);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setTimeLastMaintenance(this.timeLastMaintenance);
		obj.setLastRepairTime(this.lastRepairTime);
		obj.setTotalRepairCost(this.totalRepairCost);
		obj.setTotalNumberFailures(this.totalNumberFailures);
		obj.setGeneratorOverCapacity(this.generatorOverCapacity);
		obj.setFtConfirmGeneratorOverCapacity(this.ftConfirmGeneratorOverCapacity);
		return obj;
	}

}
