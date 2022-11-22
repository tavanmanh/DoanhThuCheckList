package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.Column;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ElectricAirConditioningACBO;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

@XmlRootElement(name = "ELECTRIC_AIR_CONDITIONING_ACBO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ElectricAirConditioningACDTO extends ComsBaseFWDTO<ElectricAirConditioningACBO>{
	private Long id;
	private Long deviceId;
	private String electricAirConditioningACName;
	private Long stateAirConditioning;
	private String seriNL;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date maxFixTme ;
	private String manufacturer ;
	private String model;
	private String goodName ;
	private String goodCode;
	private String goodCodeKTTS;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date timeIntoUse;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date lastMajorMaintenanceTime;
	private String modelColdUnit ;
	private String manufacturerColdUnit;
	private String goodCodeColdUnit;
	private Long typeCodeUnit;
	private Long wattageDHBTU;
	private Long wattageElictronic;
	private String modelHotUnit;
	private String manufacturerHotUnit;
	private Long totalRepairCost;
	private Long totalNumberFailures;
	private String typeOfGas;
	private Long state;
	private List<AttachElectronicStationDTO> listFileAttach;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listImage3;
	private List<AttachElectronicStationDTO> listImage4;
	private List<AttachElectronicStationDTO> listImage5;
	private String areaCode;
	private String provinceCode;
	private String districtCode;
	private String stationCode;
	
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

	public String getElectricAirConditioningACName() {
		return electricAirConditioningACName;
	}

	public void setElectricAirConditioningACName(String electricAirConditioningACName) {
		this.electricAirConditioningACName = electricAirConditioningACName;
	}

	public Long getStateAirConditioning() {
		return stateAirConditioning;
	}

	public void setStateAirConditioning(Long stateAirConditioning) {
		this.stateAirConditioning = stateAirConditioning;
	}

	public String getSeriNL() {
		return seriNL;
	}

	public void setSeriNL(String seriNL) {
		this.seriNL = seriNL;
	}

	public Date getMaxFixTme() {
		return maxFixTme;
	}

	public void setMaxFixTme(Date maxFixTme) {
		this.maxFixTme = maxFixTme;
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

	public String getGoodCodeKTTS() {
		return goodCodeKTTS;
	}

	public void setGoodCodeKTTS(String goodCodeKTTS) {
		this.goodCodeKTTS = goodCodeKTTS;
	}

	public Date getTimeIntoUse() {
		return timeIntoUse;
	}

	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}

	public Date getLastMajorMaintenanceTime() {
		return lastMajorMaintenanceTime;
	}

	public void setLastMajorMaintenanceTime(Date lastMajorMaintenanceTime) {
		this.lastMajorMaintenanceTime = lastMajorMaintenanceTime;
	}

	public String getModelColdUnit() {
		return modelColdUnit;
	}

	public void setModelColdUnit(String modelColdUnit) {
		this.modelColdUnit = modelColdUnit;
	}

	public String getManufacturerColdUnit() {
		return manufacturerColdUnit;
	}

	public void setManufacturerColdUnit(String manufacturerColdUnit) {
		this.manufacturerColdUnit = manufacturerColdUnit;
	}

	public String getGoodCodeColdUnit() {
		return goodCodeColdUnit;
	}

	public void setGoodCodeColdUnit(String goodCodeColdUnit) {
		this.goodCodeColdUnit = goodCodeColdUnit;
	}

	public Long getTypeCodeUnit() {
		return typeCodeUnit;
	}

	public void setTypeCodeUnit(Long typeCodeUnit) {
		this.typeCodeUnit = typeCodeUnit;
	}

	public Long getWattageDHBTU() {
		return wattageDHBTU;
	}

	public void setWattageDHBTU(Long wattageDHBTU) {
		this.wattageDHBTU = wattageDHBTU;
	}

	public Long getWattageElictronic() {
		return wattageElictronic;
	}

	public void setWattageElictronic(Long wattageElictronic) {
		this.wattageElictronic = wattageElictronic;
	}

	public String getModelHotUnit() {
		return modelHotUnit;
	}

	public void setModelHotUnit(String modelHotUnit) {
		this.modelHotUnit = modelHotUnit;
	}

	public String getManufacturerHotUnit() {
		return manufacturerHotUnit;
	}

	public void setManufacturerHotUnit(String manufacturerHotUnit) {
		this.manufacturerHotUnit = manufacturerHotUnit;
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

	public String getTypeOfGas() {
		return typeOfGas;
	}

	public void setTypeOfGas(String typeOfGas) {
		this.typeOfGas = typeOfGas;
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

	public List<AttachElectronicStationDTO> getListImage3() {
		return listImage3;
	}

	public void setListImage3(List<AttachElectronicStationDTO> listImage3) {
		this.listImage3 = listImage3;
	}

	public List<AttachElectronicStationDTO> getListImage4() {
		return listImage4;
	}

	public void setListImage4(List<AttachElectronicStationDTO> listImage4) {
		this.listImage4 = listImage4;
	}

	public List<AttachElectronicStationDTO> getListImage5() {
		return listImage5;
	}

	public void setListImage5(List<AttachElectronicStationDTO> listImage5) {
		this.listImage5 = listImage5;
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
	public ElectricAirConditioningACBO toModel() {
		// TODO Auto-generated method stub
		ElectricAirConditioningACBO obj = new ElectricAirConditioningACBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setElectricAirConditioningACName(this.electricAirConditioningACName);
		obj.setSeriNL(this.seriNL);
		obj.setMaxFixTme(this.maxFixTme);
		obj.setManufacturer(this.manufacturer);
		obj.setModel(this.model);
		obj.setGoodName(this.goodName);
		obj.setGoodCode(this.goodCode);
		obj.setGoodCodeKTTS(this.goodCodeKTTS);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setLastMajorMaintenanceTime(this.lastMajorMaintenanceTime);
		obj.setModelColdUnit(this.modelColdUnit);
		obj.setManufacturerColdUnit(this.manufacturerColdUnit);
		obj.setGoodCodeColdUnit(this.goodCodeColdUnit);
		obj.setTypeCodeUnit(this.typeCodeUnit);
		obj.setWattageDHBTU(this.wattageDHBTU);
		obj.setWattageElictronic(this.wattageElictronic);
		obj.setModelHotUnit(this.modelHotUnit);
		obj.setManufacturerHotUnit(this.manufacturerHotUnit);
		obj.setTotalRepairCost(this.totalRepairCost);
		obj.setTotalNumberFailures(this.totalNumberFailures);
		obj.setTypeOfGas(this.typeOfGas);
		obj.setStateAirConditioning(this.stateAirConditioning);
		return obj;
	}

}
