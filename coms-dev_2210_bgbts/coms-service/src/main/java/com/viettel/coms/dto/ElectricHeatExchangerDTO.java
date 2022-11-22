package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import javax.persistence.Column;
import com.viettel.coms.bo.CabinetsSourceDCBO;

import com.viettel.coms.bo.ElectricHeatExchangerBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "ELECTRIC_HEAT_EXCHANGERBO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ElectricHeatExchangerDTO extends ComsBaseFWDTO<ElectricHeatExchangerBO>{

	private Long id;
	private Long deviceId;
	private String deviceName;
	private String stateEHE;
	private String  serial;
	private Long wattage;
	private String manufacturer ;
	private String model;
	private String goodName ;
	private String goodCode;
	private String goodCodeKtts;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeIntoUse;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date lastMajorMaintenanceTime;
	private List<AttachElectronicStationDTO> listFileAttach;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listImage3;
	private String state;
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

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getStateEHE() {
		return stateEHE;
	}

	public void setStateEHE(String stateEHE) {
		this.stateEHE = stateEHE;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Long getWattage() {
		return wattage;
	}

	public void setWattage(Long wattage) {
		this.wattage = wattage;
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

	public String getGoodCodeKtts() {
		return goodCodeKtts;
	}

	public void setGoodCodeKtts(String goodCodeKtts) {
		this.goodCodeKtts = goodCodeKtts;
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

	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}

	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
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
	public ElectricHeatExchangerBO toModel() {
		// TODO Auto-generated method stub
		ElectricHeatExchangerBO bo = new ElectricHeatExchangerBO();
		bo.setId(this.id);
		bo.setDeviceId(this.deviceId);
		bo.setDeviceName(this.deviceName);
		bo.setStateEHE(this.getStateEHE());
		bo.setSerial(this.getSerial());
		bo.setWattage(this.getWattage());
		bo.setManufacturer(this.getManufacturer());
		bo.setModel(this.getModel());
		bo.setGoodName(this.getGoodName());
		bo.setGoodCode(this.getGoodCode());
		bo.setGoodCodeKtts(this.getGoodCodeKtts());
		bo.setTimeIntoUse(this.getTimeIntoUse());
		bo.setLastMajorMaintenanceTime(this.getLastMajorMaintenanceTime());
		return bo;
	}

}
